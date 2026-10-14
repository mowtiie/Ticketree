# CLAUDE.md

## Project

Event Ticketing App for the Tagisan Ng Talino Local 2026 Code Fest (Android hackathon).

Core flow: **Discover Event → Select Ticket → Checkout → Pay → Receive QR Ticket**

This is a **simulation**. Nothing is real: no backend, no database, no payment gateway, no email/SMS, no QR generation. Everything must look and behave correctly on screen during a demo.

## Prime directive: build the fastest way possible

- Speed beats elegance. Pick the shortest path that works in a live demo.
- **No structures.** No architecture patterns (MVVM, MVP, repositories, use cases, DI), no layers, no feature packages, no interfaces or abstract classes, no custom model/POJO classes. Keep everything flat.
- One package, one Activity per screen, one shared `Data.java` holding public static fields and arrays for all app state.
- Represent records with parallel arrays (`String[] EVENT_NAMES`, `int[] EVENT_PRICES`, ...) and `ArrayList<String>` for lists that grow (orders, tickets, notifications).
- Hardcode seed data. Never fetch or sync anything. Persistence uses pure SQLite only (see Stack); everything else lives in memory.
- Duplicate code freely if it is faster than abstracting it.
- No tests, no logging frameworks, no error handling beyond empty-field checks.
- Do not add libraries or touch `build.gradle` dependencies. Use only what the project template ships with.

## Stack

- Java, XML layouts, AppCompat/Material, single `app` module.
- Navigation: `startActivity(new Intent(...))` with `putExtra` where needed. No Fragments, no Navigation component, no Jetpack Compose.
- Widgets: `ListView` + `ArrayAdapter<String>`, `Spinner`, `EditText`, `Button`, `AlertDialog`, `Toast`. No RecyclerView unless a layout truly needs it.
- Views via `findViewById`.
- UI: use Material 3 components in layouts (`MaterialTextView`, `MaterialCardView`, `MaterialDivider`, `MaterialButton`, `TextInputLayout`, etc.) instead of the plain framework widgets where a Material equivalent exists.
- Database: pure SQLite (`SQLiteOpenHelper`, `SQLiteDatabase`, `ContentValues`, `Cursor`). No Room, no ORM, no other libraries. Keep the helper flat in a single class, with no DAO or repository layers.
- No permissions, no internet.

## Code conventions

- Constants (`static final`, enum values) use `ALL_CAPS_WITH_UNDERSCORES`.
- Add comments only when truly needed. Do not narrate obvious code.

## Simulation rules by module

**1. Account (customer)**
- Sign up, login, logout, profile edit, password reset are fake. Validate only that fields are non-empty. Store the "user" in `Data` static fields.
- Login accepts the signed-up email and password, or any non-empty pair if none exists yet.
- Password reset: enter email, show a Toast like "Reset link sent". Nothing is sent.
- Payment methods: static `ArrayList<String>` (for example "GCash", "Maya", "Visa ****1234"). Screen to add and remove entries.

**2. Event discovery**
- 6 to 8 hardcoded events in `Data` arrays. Categories include Leadership Training, AI Boot Camp, Toastmasters, plus a few more.
- Home shows featured events. An event list screen shows all events, filtered by category.
- Search by text. Filter by date, location and category using Spinners or simple text matching on the arrays.

**3. Checkout**
- Order summary (event, ticket type, seats, quantity, total), billing info (name, email, address `EditText`s), payment method picker from the saved list.
- "Payment processing" is a 2 second `Handler.postDelayed` with a progress indicator, then always succeeds.
- Confirmation screen plus an invoice/receipt screen built as a plain `TextView` string.

**4. Digital tickets**
- QR/barcode is a **static drawable** shown in an `ImageView` (`res/drawable/qr_ticket.png`). Do not generate QR codes or add a QR library.
- Ticket wallet is a `ListView` of purchased tickets from `Data.TICKETS` (`ArrayList<String>`). Tapping one opens the ticket screen with the static QR image.

**5. Notifications**
- Simulated with an in-app notification list (`ArrayList<String>`) and a `Toast`: booking confirmation, payment confirmation, ticket delivery, event reminder.
- Event reminder is triggered by a "Simulate reminder" button or a short `Handler` delay. No system notification channels.

**6. History**
- Order history and payment receipts are read from `Data.ORDERS` and `Data.RECEIPTS` (`ArrayList<String>`).

**7. Event details / management (organizer)**
- Event detail screen shows type, description, date and time, venue (Onsite, Hotel, Training Camp), organizer info, image, speaker lineup, ticket types and prices, availability, terms and conditions, and age restriction.
- Event images are drawables or plain colored placeholders. No uploads.

**8. Ticket and seat management**
- Ticket types: General Admission, VIP, Early Bird, Group. Each has a price and remaining count in arrays.
- Seat map is a static image (`res/drawable/seat_map.png`). No dynamic map rendering.
- Seat selection: 3 boxes (Lower Box, Upper Box, Middle Box) with **20 seats each**, kept in `boolean[][][] SEAT_TAKEN` (event, box, seat). Show them as a simple grid of buttons or a `GridView` of numbers.
- Ticket hold: selecting seats starts a `CountDownTimer` of `HOLD_SECONDS` (120). If it expires before payment, release the seats and return to the event.
- Availability decreases after every successful order.

**9. Organizer management**
- Organizer registration and profile are fake, same as customer accounts. Role is chosen with a switch on sign up or login.
- Create event, edit event, publish/unpublish toggle, venue management and pricing management all edit the same `Data` arrays. Published events appear in customer discovery.

**10. Organizer dashboard**
- Sales overview, ticket sales, revenue, ticket inventory, sales by ticket type, sales by date/time.
- Compute from `Data.ORDERS` plus a few pre-seeded demo numbers so the dashboard is never empty. Display with `TextView`s. No chart libraries.

## Suggested screens (one Activity each)

Login, SignUp, ResetPassword, Home, EventList, EventDetail, SeatSelect, Checkout, Confirmation, Receipt, TicketWallet, TicketView, Notifications, History, Profile, PaymentMethods, OrganizerHome, EventForm, OrganizerDashboard.

## Build order (stop when time runs out)

1. Login, Home, EventList, EventDetail
2. Ticket selection, SeatSelect, Checkout, Confirmation
3. TicketWallet, TicketView with the QR image
4. History, Notifications, Profile, PaymentMethods
5. Organizer screens and Dashboard

The core flow must work end to end before anything else is polished.

## Commit rules

- Never list Claude as an author or contributor on any commit.
- Do not add a `Co-Authored-By: Claude` trailer or any other co-author line mentioning Claude or Anthropic.
- Do not add "Generated with Claude Code" or similar footers to commit messages or pull request descriptions.
- Do not change `git config user.name` or `user.email`. Commits use the developer's existing git identity.
- Write short, plain commit messages in the imperative, for example `Add event list screen`.

## Commands

- Build: `./gradlew assembleDebug`
- Install and run: use Android Studio's Run button on an emulator.