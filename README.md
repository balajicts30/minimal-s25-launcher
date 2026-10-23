# Minimal Launcher — Samsung Galaxy S25 Edition

An ultra-clean, battery-efficient Kotlin Jetpack Compose Android launcher engineered for the Samsung Galaxy S25 / S25+ / S25 Ultra (Dynamic AMOLED 2X, 120Hz display, `targetSdk 34`, `minSdk 26`).

![S25 Minimal Launcher Preview](preview.png)

# Minimal Launcher — Samsung Galaxy S25 Edition

An ultra-clean, battery-efficient Kotlin Jetpack Compose Android launcher engineered for the Samsung Galaxy S25 / S25+ / S25 Ultra (Dynamic AMOLED 2X, 120Hz display, `targetSdk 34`, `minSdk 26`).

![S25 Minimal Launcher Preview](preview.png)

## Core Architecture & 3-Section Home Screen

The launcher organizes your digital life into 3 focused, clutter-free sections designed for maximum productivity and pure AMOLED power efficiency:

### Section 1: Minimal Favorites App Icons
- Fully customizable circular monochrome tiles.
- Supported quick-toggles: **Maps**, **WhatsApp**, **Instagram**, **Telegram**, **Phone**, **Messages**, **Camera**, **Browser (Chrome/Samsung Internet)**, **Microsoft Teams**, **Gmail**, and **Photos**.
- Direct launch with auto-detection of Samsung OneUI & Google system packages.

### Section 2: Rolling Notifications (Scrollable Ticker)
- A compact, horizontal scrollable cards section displaying incoming notifications in real-time.
- Captures sender, title, message snippet, and timestamp.
- Backed by an Android `NotificationListenerService` (`S25NotificationService`) with tap-to-open direct intent resolution.

### Section 3: Meetings & Missed Calls (Minimal Mode)
- **Microsoft Teams (Work)**: Minimal tagged calendar meetings (`[WORK]`).
- **Personal & Gmail**: Google Calendar events and reminders (`[PERSONAL]`).
- **Missed Calls**: Live call log alerts (`[MISSED CALL]`) with one-tap dial back.
- Integrated with Android `CalendarContract` and `CallLog.Calls` runtime permissions.

---

## Interactive Settings Menu
Tap **"Customize ⚙"** in the top header to reveal the slide-up settings sheet:
- **Home Screen App Selection**: Check/uncheck individual apps (Maps, WhatsApp, Instagram, Telegram, Teams, Gmail, etc.) to immediately add or remove them from your home screen.
- **Section Toggles**: Independently enable or disable the Notifications ticker or the Meetings & Missed Calls agenda.
- **System Permissions**: One-tap deep-links to grant Notification Access, Calendar, Call Log, and Screen Time Usage Access.
- Settings are saved automatically in `SharedPreferences` (`"s25_launcher_prefs"`).

---

## Additional Features
- **Pure AMOLED Pitch Black (`#000000`)**: Zero power draw on Samsung OLED displays.
- **Hero Status Header**: Large digital clock (`58.sp`), date, battery percentage with `⚡` charging indicator, and screen time detox badge (`⏳ 1h 48m today`).
- **Full App Drawer**: Swipe UP to slide up the complete, searchable alphabetical app drawer.
- **Quick Gestures**: Swipe RIGHT for Phone / Dialer, Swipe LEFT for Camera.
- **Automated CI/CD**: GitHub Actions workflow (`.github/workflows/build.yml`) compiling `app-debug.apk` using Java 17 (Temurin) and Gradle 8.7.

