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
- Messages: `Snackbar` for validation and errors on a screen that stays open. `Toast` only for simulated notifications and for messages shown as a screen closes. `MaterialAlertDialogBuilder` (never the plain `AlertDialog.Builder`) for the receipt popup and the price and stock editors, and a `BottomSheetDialog` for adding a payment method.

## UI components and layout patterns

- Use Material 3 components, not plain framework widgets: `MaterialToolbar`, `MaterialTextView`, `MaterialButton`, `MaterialCardView`, `MaterialDivider`, `TextInputLayout` with `TextInputEditText`, `MaterialSwitch`, `TabLayout`, `Chip`/`ChipGroup`, `CircularProgressIndicator`, `LinearProgressIndicator`.
- Every screen except Login has a `MaterialToolbar` with a title. Show the back arrow on every screen except the root screens (Home, OrganizerHome, Confirmation).
- Toolbar actions use menu resources in `res/menu/` via `app:menu` and `binding.toolbar.setOnMenuItemClickListener` (no `setSupportActionBar`). Home has only a bell action with an unread badge. OrganizerHome has an add action plus an overflow with Dashboard, Ticket pricing, Venue management and Profile. Logging out is only on the Profile screen. The event list has "Clear filters" and Notifications has "Simulate reminder" in the overflow.
- Empty lists use the shared `view_empty.xml` through `<include android:id="@+id/empty">` (emoji, message, optional button). Show or hide `binding.empty.getRoot()`.
- Events everywhere are shown with `Data.bindEvent(row, event)`: category emoji on a themed color block, title, date, location and venue, and a "From ₱X" chip.
- Money is always formatted with `Data.money(int)`. Never write a raw "P" prefix.
- Seat hold timers use `Data.styleTimer`, which turns red in the last 15 seconds.
- View ID prefixes: `til` (TextInputLayout), `et` (edit text), `dd` (dropdown), `tv` (text), `btn` (button), `rv` (RecyclerView), `chip`, `sw` (switch), `card`, `ll` (LinearLayout container).

## Theme

- The app theme is `AppTheme`, generated by Material Theme Builder (`values/themes.xml`, `values-night/themes.xml`, `colors.xml`, `theme_overlays.xml`). Do not edit those generated files by hand. Re-export from the builder instead.
- `Theme.Ticketree` (in `values/app_themes.xml`) extends `AppTheme` and adds `adjustResize`. The manifest uses `Theme.Ticketree`.
- Every color comes from the Material Theme Builder palette. Do not add custom colors, `colors.xml` entries or hex values. The only exceptions are the white shapes `bg_icon` and `bg_banner` (tinted in code) and the 40% black scrim over the event image on the detail screen.
- Use `?attr/color*` theme attributes and the Material text appearances (`?attr/textAppearance*`) in layouts and code. Category colors cycle through six theme pairs (`colorPrimary/Secondary/TertiaryContainer` and `colorPrimary/Secondary/TertiaryFixed` with their matching `colorOn…` attributes) via `Data.categoryColor` and `Data.categoryOnColor`.

## Window insets and manifest

- Every Activity calls `EdgeToEdge.enable(this)` right after `super.onCreate`. Every layout root has `android:fitsSystemWindows="true"`.
- Login, Sign up and Reset password are wrapped in a `ScrollView` with `fillViewport="true"` so the keyboard never hides fields.
- Only `LoginActivity` is `android:exported="true"` (launcher). Every other Activity is `exported="false"`. Register every new Activity in `AndroidManifest.xml`.

## Form conventions

- Every `TextInputLayout` has helper text that says what to enter, with the format when it matters (dates, passwords).
- Validate with inline `setError` on the field that is wrong, not a generic Snackbar. Clear the error on the next attempt.
- Password fields use the password toggle end icon. Emails are checked with `Patterns.EMAIL_ADDRESS`. Passwords need 6 or more characters.
- Event dates are picked with `MaterialDatePicker` then `MaterialTimePicker` (new events cannot be in the past) and stored as `Dec 20, 2026 9:00 AM`. The date field is read-only; the format is still checked on save.

## Code conventions

- Constants (`static final`, enum values) use `ALL_CAPS_WITH_UNDERSCORES`.
- Add comments only when truly needed. Do not narrate obvious code.
- Match the surrounding code: 4-space indent, `binding.xxx` access, lambdas for click listeners.

## Simulation rules by module

**1. Account**
- Sign up, login, logout, profile edit and password reset are fake but validated (see Form conventions). Accounts live in the SQLite `users` table; the logged-in user is copied into `Data.USER_*`.
- Sign up picks the role with a Customer / Organizer `TabLayout` at the top of the screen. Login has no role switch: the stored account decides whether you land on Home (customer) or OrganizerHome (organizer).
- Login accepts only stored accounts. Two demo accounts are created in the database automatically: `customer@demo.com` and `organizer@demo.com`, both with password `demo1234`. Logging in as the demo customer loads a sample purchase once (`Data.seedDemoPurchase()`): one order, its receipt, two VIP tickets, and four notifications.
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
- Triggers: booking confirmation, payment confirmation and ticket delivery (all right after payment), and an automatic event reminder 15 seconds after purchase. The Notifications toolbar overflow has "Simulate reminder". `Data.UNREAD` drives the badge on the Home bell. After payment the three purchase notifications are stored silently and shown as one combined Toast.

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

## UX patterns

- Booking screens show a step indicator (`view_steps.xml`): Tickets (1 of 3), Seats (2 of 3), Payment (3 of 3). Event detail, Ticket select and Seat select have a sticky bottom bar with the price or selection summary and the main action button.
- Ticket types are selectable cards (`item_ticket_type.xml`), not a dropdown. Sold-out types are dimmed and disabled.
- Home shows a plain welcome line, a row of category chips that open the event list pre-filtered (`category` extra), featured events and the section grid. There is no banner card.
- Payment methods are added in a bottom sheet with an E-wallet / Card tab layout. Tapping a method makes it the default (`Data.DEFAULT_PAYMENT`), which checkout preselects. Labels are built from the input, for example `GCash 0917 *** 4567` or `Visa ****1234`.
- Organizer lists show status as a colored chip (green Published, gray Unpublished). The dashboard colors each ticket type's bar and shows a "Best seller" chip.
- Screen transitions are a short slide and fade (`Animation.Ticketree` in `values/app_themes.xml`).
- Accessibility: back arrows have a content description, seat buttons announce number and state, decorative emoji views are hidden from screen readers.
- Use a `MaterialDivider` only to separate a sticky bottom bar from the content above it (event detail, ticket select, seat select) or to split distinct sections inside one screen (checkout, profile). Do not put a divider directly under a toolbar or tab bar, or between a heading and a list.

## Avoiding redundancy

- Prices come only from the ticket types. There is no per-event price. "From" prices use `Data.fromPrice()` (the lowest ticket price still in stock). The event form has no price field.
- Each destination has one obvious entry point: Notifications is the Home bell (with badge), Profile is a Home grid card, Log out is on Profile only.
- Do not repeat a value on the same screen (for example the starting price or the page title). Step labels are just "Step N of 3".
- The Orders tab shows what was bought (event, ticket, order number and time). The Receipts tab shows what was paid (receipt number, amount, method and date). Both open the same receipt dialog.

## Color roles

- Page background and toolbars: `colorSurfaceContainer` (set as `android:windowBackground` in `Theme.Ticketree`). Sticky bottom bars: `colorSurfaceContainerHigh`.
- Brand color `colorPrimary`: welcome headline, prices and totals, selected seats, the confirmation headline.
- Home grid: Browse is primary container, Tickets and Payments are tertiary container, History is secondary container, Profile is surface container high.
- Status: Published and the ticket "Valid" chip use the tertiary container. Unpublished uses surface variant. Errors and the hold timer use the error colors.
- Secondary actions (Remove, Edit stock, Reset password, quantity +/-) are tonal buttons. The main action on a screen is the filled button.
- Dashboard: stat cards use the primary, secondary, tertiary and surface container colors. Ticket type bars use primary, tertiary, secondary and `colorPrimaryFixedDim`.
- Selected ticket type card: `res/color/ticket_card_bg.xml` and `ticket_card_stroke.xml` (primary container and primary stroke when checked).

## Spacing

- Screen padding is 16dp (24dp on the centered auth screens). Gaps between fields and cards are 8dp, between sections 16dp (24dp between the Home carousel and the grid). Use 4dp only inside a card to attach a chip or button to its text.
- Scrolling lists use `clipToPadding="false"` with 8dp bottom padding so the last card is not flush with the screen edge.
- Multi-line text blocks (event details, order summary, receipt) use `lineSpacingMultiplier="1.15"`.
- Greetings use `Data.greeting()` ("Hello, <first name>!") on both the customer and organizer home screens.
