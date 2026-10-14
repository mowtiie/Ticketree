# CLAUDE.md

## Project

Event Ticketing App for the Tagisan Ng Talino Local 2026 Code Fest (Android hackathon, about 6 hours).

Core flow: **Discover Event → Select Ticket → Pick Seats → Checkout → Pay → Receive QR Ticket**

This is a **simulation**. There is no backend, no payment gateway, no email/SMS, no real push notifications and no QR generation. Everything must look and behave correctly on screen during a demo. The only real storage is a small local SQLite table for user accounts.

Source of requirements: the Code Fest brief (modules 1 to 10: account, discovery, checkout, digital tickets, notifications, history, event details, ticket and seat management, organizer management, organizer dashboard). Password reset is only listed as "Password reset", so a simulated flow is enough. QR and seat map are images only, with no generation or dynamic mapping.

Status: all five build-order steps plus UI polish are implemented. The app builds with `./gradlew assembleDebug` and passes lint with no errors. It has **not** been run end to end on an emulator yet.

## Prime directive: build the fastest way possible

- Speed beats elegance. Pick the shortest path that works in a live demo.
- **No structures.** No architecture patterns (MVVM, MVP, repositories, use cases, DI), no layers, no feature packages, no interfaces or abstract classes, no custom model/POJO classes. The only exception is the single shared `GenericAdapter<T>` (and its nested `Binder` interface).
- One Activity per screen, one shared `Data.java` holding public static fields and arrays for all app state.
- Represent records with parallel arrays (`String[] EVENT_NAMES`, `int[] EVENT_PRICES`, ...) and `ArrayList<String>` for lists that grow (orders, tickets, notifications).
- Hardcode seed data. Never fetch or sync anything. Persistence is pure SQLite for accounts only; everything else lives in memory and resets on app restart.
- Duplicate code freely if it is faster than abstracting it.
- No tests, no logging frameworks, no error handling beyond field validation.
- Do not add libraries or touch `build.gradle` dependencies. Use only what the project template ships with. Enabling `buildFeatures { viewBinding = true }` is allowed because it is not a dependency.

## How the code is built

Source lives in three flat folders under `com.mowtiie.ticketree`. Do not nest them or add per-feature packages.

- `activities/` holds every Activity.
- `data/` holds `Data.java` (all state and helpers) and `DbHelper.java` (the SQLite helper).
- `adapters/` holds `GenericAdapter.java`.

Key facts about `Data.java`:
- **Events:** parallel arrays for name, category, date, location, venue, price, description, organizer, age restriction and speakers. They are non-final so `Data.addEvent()` can grow them. `EVENT_PUBLISHED[]` controls whether customers see an event.
- **Tickets:** `TICKET_TYPES`, `TICKET_PRICES`, `TICKET_REMAINING`, `TICKET_MIN_QTY`. Stock is per ticket type and shared across all events. Maximum 6 per order. Group needs at least 4.
- **Seats:** `SEAT_TAKEN[event][box][seat]`, 3 boxes (`BOX_NAMES`: Lower, Middle, Upper) of 20 seats. Some seats are pre-taken.
- **Selection state during booking:** `SEL_EVENT`, `SEL_TYPE`, `SEL_QTY`, `SEL_BOX`, `SEL_SEATS`, `HOLD_END`.
- **Hold:** picking the first seat marks seats taken and sets `HOLD_END` (120 seconds, `HOLD_SECONDS`). `Data.releaseHold()` frees them. The countdown continues on Checkout. On payment `HOLD_END` is set to 0. If it expires, release the seats and return to the event detail screen.
- **Orders:** `ORDERS` and `RECEIPTS` are index-aligned. `ORDER_TYPES`, `ORDER_QTYS`, `ORDER_TOTALS`, `ORDER_TIMES` feed the dashboard.
- **Tickets:** `TICKETS` holds strings in the form `orderId|eventName|eventDate|ticketType|box|seat`. Split on `|`.
- **Session:** `USER_NAME`, `USER_EMAIL`, `USER_PASSWORD`, `USER_IS_ORGANIZER`. Accounts are stored in the SQLite `users` table (`DbHelper`).
- **Helpers:** `money(int)`, `categoryEmoji`, `categoryColor`, `bindEvent(row, event)`, `eventIndex(name)`, `styleTimer(textView, msLeft)`, `addNotification`, `notify(appContext, message)`, `UNREAD`.

Navigation is `startActivity(new Intent(...))` with `putExtra`. After payment, `CheckoutActivity` starts `ConfirmationActivity` with `NEW_TASK | CLEAR_TASK`, and Confirmation returns to Home the same way. Logout clears the task and returns to Login.

## Stack

- Java, XML layouts, AppCompat and Material 3, single `app` module. minSdk 24, targetSdk 36.
- No Fragments, no Navigation component, no Jetpack Compose. No permissions, no internet.
- Views via view binding. Use the generated `*Binding` classes, never `findViewById`.
- Lists: `RecyclerView` with the one shared `GenericAdapter<T>` and the shared row layout `item_row.xml` (`tvIcon`, `tvTitle`, `tvSubtitle`, `chipBadge`, `btnAction`). Never write a per-screen adapter.
- Database: pure SQLite (`SQLiteOpenHelper`, `SQLiteDatabase`, `ContentValues`, `Cursor`). No Room, no ORM, no DAO layer.
- Dropdowns: `TextInputLayout` (ExposedDropdownMenu style) with `MaterialAutoCompleteTextView`. Never `Spinner`.
- Messages: `Snackbar` for validation and errors on a screen that stays open. `Toast` only for simulated notifications and for messages shown as a screen closes. `AlertDialog` for the receipt popup and the price editor.

## UI components and layout patterns

- Use Material 3 components, not plain framework widgets: `MaterialToolbar`, `MaterialTextView`, `MaterialButton`, `MaterialCardView`, `MaterialDivider`, `TextInputLayout` with `TextInputEditText`, `MaterialSwitch`, `TabLayout`, `Chip`/`ChipGroup`, `CircularProgressIndicator`, `LinearProgressIndicator`.
- Every screen except Login has a `MaterialToolbar` with a title. Show the back arrow on every screen except the root screens (Home, OrganizerHome, Confirmation).
- Toolbar actions use menu resources in `res/menu/` via `app:menu` and `binding.toolbar.setOnMenuItemClickListener` (no `setSupportActionBar`). Home has a bell action plus an overflow with Profile and Log out. OrganizerHome has an add action plus an overflow with Dashboard, Ticket pricing, Venue management, Profile and Log out. The event list has "Clear filters" and Notifications has "Simulate reminder" in the overflow.
- Empty lists use the shared `view_empty.xml` through `<include android:id="@+id/empty">` (emoji, message, optional button). Show or hide `binding.empty.getRoot()`.
- Events everywhere are shown with `Data.bindEvent(row, event)`: category emoji on a pastel block, title, date, location and venue, and a "From ₱X" chip.
- Money is always formatted with `Data.money(int)`. Never write a raw "P" prefix.
- Seat hold timers use `Data.styleTimer`, which turns red in the last 15 seconds.
- View ID prefixes: `til` (TextInputLayout), `et` (edit text), `dd` (dropdown), `tv` (text), `btn` (button), `rv` (RecyclerView), `chip`, `sw` (switch), `card`, `ll` (LinearLayout container).

## Theme

- The app theme is `AppTheme`, generated by Material Theme Builder (`values/themes.xml`, `values-night/themes.xml`, `colors.xml`, `theme_overlays.xml`). Do not edit those generated files by hand. Re-export from the builder instead.
- `Theme.Ticketree` (in `values/app_themes.xml`) extends `AppTheme` and adds `adjustResize`. The manifest uses `Theme.Ticketree`.
- Custom colors that are not part of the builder output (for example `success`) live in `values/app_colors.xml` and `values-night/app_colors.xml`.
- Use `?attr/color*` theme attributes and the Material text appearances (`?attr/textAppearance*`) in layouts and code, never raw hex values or `textSize`. The only fixed colors are the pastel category colors in `Data` and the dark text on them.

## Window insets and manifest

- Every Activity calls `EdgeToEdge.enable(this)` right after `super.onCreate`. Every layout root has `android:fitsSystemWindows="true"`.
- Login, Sign up and Reset password are wrapped in a `ScrollView` with `fillViewport="true"` so the keyboard never hides fields.
- Only `LoginActivity` is `android:exported="true"` (launcher). Every other Activity is `exported="false"`. Register every new Activity in `AndroidManifest.xml`.

## Form conventions

- Every `TextInputLayout` has helper text that says what to enter, with the format when it matters (dates, passwords).
- Validate with inline `setError` on the field that is wrong, not a generic Snackbar. Clear the error on the next attempt.
- Password fields use the password toggle end icon. Emails are checked with `Patterns.EMAIL_ADDRESS`. Passwords need 6 or more characters.
- Event dates must match `Dec 20, 2026 9:00 AM`.

## Code conventions

- Constants (`static final`, enum values) use `ALL_CAPS_WITH_UNDERSCORES`.
- Add comments only when truly needed. Do not narrate obvious code.
- Match the surrounding code: 4-space indent, `binding.xxx` access, lambdas for click listeners.

## Simulation rules by module

**1. Account**
- Sign up, login, logout, profile edit and password reset are fake but validated (see Form conventions). Accounts live in the SQLite `users` table; the logged-in user is copied into `Data.USER_*`.
- Sign up picks the role with a Customer / Organizer `TabLayout` at the top of the screen. Login has no role switch: the stored account decides whether you land on Home (customer) or OrganizerHome (organizer).
- Login accepts only stored accounts. Two demo accounts are created in the database automatically: `customer@demo.com` and `organizer@demo.com`, both with password `demo1234` (the Login helper text shows them; remove before the real event if wanted). Logging in as the demo customer loads a sample purchase once (`Data.seedDemoPurchase()`): one order, its receipt, two VIP tickets, and four notifications.
- Password reset shows a Toast "Reset link sent". Nothing is sent.
- Payment methods: `Data.PAYMENT_METHODS`, add and remove on the PaymentMethods screen. Checkout fills in GCash, Maya and Visa ****1234 if the list is empty.

**2. Event discovery**
- 8 hardcoded events. Categories include Leadership Training, AI Boot Camp and Toastmasters, plus others. Only published events are shown.
- Home shows up to 3 featured events in a horizontal carousel and a grid of cards to the other sections.
- The event list has text search plus dropdown filters for category, date and location, and "Clear filters" in the toolbar overflow.

**3. Checkout**
- Order summary (event, ticket type, seats, quantity, total), billing info (name, email, address) and a payment method dropdown.
- "Payment processing" is a 2 second `Handler.postDelayed` behind a full-screen "Processing payment..." overlay, then always succeeds.
- The Confirmation screen shows the receipt as a plain `TextView` string (there is no separate Receipt screen).

**4. Digital tickets**
- The QR is a **static drawable** (`res/drawable/qr_ticket.png`) in an `ImageView`. Do not generate QR codes or add a QR library.
- The ticket wallet lists `Data.TICKETS`. Tapping one opens a ticket-style card with the QR, a dashed divider and a "Valid" chip.

**5. Notifications (simulated only)**
- Never use real Android notifications or notification channels.
- Store messages newest first with a time in `Data.NOTIFICATIONS`. Use `Data.notify(appContext, message)` when the screen may close (it stores the message and shows a Toast). Use `Data.addNotification` plus a Snackbar on a screen that stays.
- Triggers: booking confirmation, payment confirmation and ticket delivery (all right after payment), and an automatic event reminder 15 seconds after purchase. The Notifications toolbar overflow has "Simulate reminder". `Data.UNREAD` drives the "(n new)" label on the Home card.

**6. History**
- History has two tabs: Orders (`Data.ORDERS`) and Payment receipts (`Data.RECEIPTS`). Tapping either shows the receipt text in a dialog.

**7. Event details**
- Shows type, description, date and time, venue (Onsite, Hotel, Training Camp), organizer, speaker lineup, ticket types with prices and availability, terms and conditions, and age restriction.
- The event image is one of six bundled placeholder pictures (`event_*.png`, chosen with the "Event image" dropdown in the event form, stored in `EVENT_IMAGES`). No uploads. Lists still show the category emoji block.
- Organizer information (name, contact number, about) comes from the organizer profile (SQLite `users` columns `phone` and `about`) and is copied onto the event when it is created. Terms and conditions are per event (`EVENT_TERMS`, editable in the form).

**8. Ticket and seat management**
- Ticket types: General Admission, VIP, Early Bird, Group. Each has a price and remaining count in arrays.
- Seat map is a static image (`res/drawable/seat_map.png`) shown on the seat screen. Seat selection is a grid of buttons for 3 boxes with 20 seats each, with a Free / Selected / Taken legend.
- Availability decreases after every successful order.

**9. Organizer management**
- Venue management is its own screen (`Data.VENUES`, add and remove; a venue used by an event cannot be removed). The event form venue dropdown reads from it. Organizer home lists all events with Publish/Unpublish; New event, Dashboard, Ticket pricing, Profile and Log out are toolbar actions. EventForm creates or edits an event (all fields plus speakers and a published switch). Ticket pricing edits `TICKET_PRICES` and adds stock.
- Every organizer can edit every event. That is a deliberate demo shortcut.

**10. Organizer dashboard**
- Stat cards (revenue, tickets sold, orders, average order) plus progress bars for sales by ticket type, ticket inventory and sales by date. Computed from `Data.ORDER_*` plus pre-seeded demo numbers so it is never empty. No chart libraries.

## Screens (one Activity each)

Login, SignUp, ResetPassword, Home, EventList, EventDetail, TicketSelect, SeatSelect, Checkout, Confirmation, TicketWallet, TicketView, History, Notifications, Profile, PaymentMethods, OrganizerHome, EventForm, TicketPricing, VenueManagement, OrganizerDashboard.

## Build order (all done)

1. Login, Home, EventList, EventDetail
2. Ticket selection, SeatSelect, Checkout, Confirmation
3. TicketWallet, TicketView with the QR image
4. History, Notifications, Profile, PaymentMethods
5. Organizer screens and Dashboard

The core flow must work end to end before anything else is polished. Remaining work: run the whole flow on an emulator and fix what turns up.

## Commit rules

- Never list Claude as an author or contributor on any commit.
- Do not add a `Co-Authored-By: Claude` trailer or any other co-author line mentioning Claude or Anthropic.
- Do not add "Generated with Claude Code" or similar footers to commit messages or pull request descriptions.
- Do not change `git config user.name` or `user.email`. Commits use the developer's existing git identity.
- Write short, plain commit messages in the imperative, for example `Add event list screen`.

## Commands

- Build: `./gradlew assembleDebug`
- Lint: `./gradlew lintDebug` (report in `app/build/reports/lint-results-debug.html`)
- Install and run: use Android Studio's Run button on an emulator.
