# Minimal Launcher — Samsung Galaxy S25 Pure AMOLED Edition

An ultra-clean, battery-efficient Kotlin Jetpack Compose Android launcher engineered for the Samsung Galaxy S25 / S25+ / S25 Ultra (Dynamic AMOLED 2X, 120Hz display, `targetSdk 34`, `minSdk 26`).

![S25 Minimal Launcher Preview](preview.png)

## What's New: Advanced AMOLED Suite & Unified Controls

### 1. Google Gemini AI Sparkle Shortcut
- **Next to the Time**: A dedicated, glowing neon `✦` (AI) circular icon button positioned on the right side of the clock header.
- **Smart Launch Chain**: Launches the official Google Gemini app (`com.google.android.apps.bard`), falls back to Android Google Assistant (`ACTION_ASSIST`), and provides web fallback (`gemini.google.com`).

### 2. Middle Quick System Toggles Bar
- **Directly Above Notifications**: Positioned centrally between Favorites and Notifications.
- **Wi-Fi Toggle**: Displays live Wi-Fi state with glowing cyan accent. Tapping toggles Wi-Fi (or invokes Android Q+ quick panel).
- **Sound / Silent Mode Cycle**: Cycles between **Normal (Sound)**, **Vibrate**, and **Silent** mode using `AudioManager` with adaptive color states (Lime / Amber / Crimson).
- **Bluetooth Toggle**: Displays live Bluetooth adapter state with indigo accent.
- **Android System Settings**: 1-tap quick shortcut to Android system settings.

### 3. Unified Gestures & Double-Tap to Lock
- **Swipe UP Anywhere**: Seamlessly fling upwards anywhere on the home screen to access the All-Apps Drawer.
- **Swipe DOWN Anywhere**: Pull down anywhere on the home screen to expand the Android system notification shade (`StatusBarManager.expandNotificationsPanel`).
- **Double-Tap on Black Area to Lock**: Tap twice on any empty black area of the AMOLED screen to securely lock the phone.
  - Backed by `DevicePolicyManager.lockNow()` via `S25DeviceAdminReceiver`.
  - Prompts standard Android Device Admin permission if not yet configured.
- **Edge Swipes**: Swipe RIGHT opens Phone dialer; swipe LEFT opens Camera.

### 4. Customizable Sizing & Layout Dimensions
Configurable in **Launcher Settings (`*`)**:
- **Icon Sizing**: 4 selectable sizes for home screen app icons:
  - `Compact (36dp)`
  - `Standard (44dp)` *(Default)*
  - `Large (52dp)`
  - `Extra Large (60dp)`
- **Layout Width (Horizontal Edge Padding)**:
  - `Wide (8dp)` — Edge-to-edge full width
  - `Standard (18dp)` — Balanced spacing
  - `Narrow (28dp)` — Focused center alignment
- **Section Spacing (Height)**:
  - `Compact (12dp)`
  - `Balanced (20dp)`
  - `Spacious (28dp)`

### 5. Add ALL Apps to Favorites & Custom App Picker
- **No Longer Limited to Curated Apps**: Pick **ANY** installed application on the phone to place on the home screen.
- **Batch Action Buttons**:
  - `[ADD ALL APPS]`: Instantly promotes every installed application into the home screen grid.
  - `[CURATED (6)]`: Restores the default 6 curated essential apps.
  - `[CLEAR]`: Resets favorites.
- **Dynamic Neon Generator**: Any custom application automatically receives a deterministically assigned, vibrant neon halo and monogram glyph.
- **Settings Filter**: Dedicated search field in Settings to quickly find and toggle apps.

### 6. App Drawer: Top 10 Recent & Most Opened Apps
- **Recent & Frequent Section**: At the top of the All-Apps Drawer, displays the **Top 10 most recent and frequently used apps**.
- **Dual-Scoring Engine**: Leverages `UsageStatsManager` foreground time/recency combined with the launcher's internal persistent launch tracking.
- **Alphabetical Full Directory**: Follows immediately below with instant real-time search.

### 7. Crystal-Clear Multi-Profile Meetings & Calls
- **Teams Work Profile & Personal Support**: Multi-source metadata resolution across `CalendarContract.Instances`, `ENTERPRISE_CONTENT_URI` (Work Profile Knox), and `CalendarContract.Events`.
- **Zero Fake Data**: Displays only genuine meetings with full unclipped titles, clear status (`TODAY • 10:30 AM - 11:30 AM`), and 1-tap `TAP TO JOIN ↗` deep links.
- **Live Speedometer**: Real-time `TrafficStats` download meter (e.g. `↓ 4.2 MB/s`) in the header.

---

## Architecture & Permissions

| Permission | Purpose |
| :--- | :--- |
| `EXPAND_STATUS_BAR` | Swipe down to expand notification drawer |
| `BIND_DEVICE_ADMIN` | Double-tap black area to lock phone via `DevicePolicyManager` |
| `ACCESS_WIFI_STATE` & `CHANGE_WIFI_STATE` | Wi-Fi toggle in Quick Toggles Bar |
| `BLUETOOTH` & `BLUETOOTH_CONNECT` | Bluetooth state toggle in Quick Toggles Bar |
| `ACCESS_NOTIFICATION_POLICY` | Silent mode & DND ringer mode cycling |
| `PACKAGE_USAGE_STATS` | Screen time calculation & Top 10 recent apps ranking |
| `READ_CALENDAR` | Multi-profile Teams & Gmail meeting sync |
| `READ_CALL_LOG` | Recent missed call identification with 1-tap dial back |
| `BIND_NOTIFICATION_LISTENER_SERVICE` | Rolling notification card stream |

---

## Build & CI Verification

The project compiles on GitHub Actions using **Java 17 (Temurin)** and **Gradle 8.7** producing `app-debug.apk`.
