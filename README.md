# Minimal Launcher — Samsung Galaxy S25 Pure AMOLED Edition

An ultra-clean, battery-efficient Kotlin Jetpack Compose Android launcher engineered for the Samsung Galaxy S25 / S25+ / S25 Ultra (Dynamic AMOLED 2X, 120Hz display, `targetSdk 34`, `minSdk 26`).

![S25 Minimal Launcher Preview](preview.png)

## What's New: True Fullscreen AMOLED, Neon Glow & Enterprise Teams Sync

### 1. Pure AMOLED Fullscreen (Hidden Status Bar & Notification Drop)
- Immersive edge-to-edge layout hiding the system status bar and notification shade pull-down.
- True pitch-black (`#000000`) background across 100% of the display. Heavy gray background card slabs have been removed so only text, icons, and glowing badges appear.

### 2. Radiant Neon Glow App Icons
- Multi-layered atmospheric radial neon glow rendered around every circular icon matching its authentic brand palette:
  - **WhatsApp**: Neon Emerald Green (`#25D366`)
  - **Instagram**: Cyber Neon Pink (`#FF1493`)
  - **Telegram**: Electric Sky Cyan (`#00E5FF`)
  - **Maps**: Neon Spring Green (`#10B981`)
  - **Phone**: Neon Bright Green (`#39FF14`)
  - **Messages**: Sapphire Blue (`#3B82F6`)
  - **Camera**: Coral Sunset (`#FF5252`)
  - **Browser**: Electric Amber Yellow (`#FFD600`)
  - **Teams**: Neon Indigo (`#818CF8`)
  - **Gmail**: Crimson Red (`#FF3366`)
  - **Photos**: Neon Orange (`#FF9100`)

### 3. Real Agenda Sync: Work Profile Teams, Personal Teams & Gmail (Zero Mock Data)
- **Work Profile / Enterprise Teams**: Cross-profile integration using Android Enterprise `CalendarContract.Instances.ENTERPRISE_CONTENT_URI` (API 29+) to query corporate meetings directly from Samsung Knox Work Profile.
- **Personal Profile Teams & Gmail**: Standard `CalendarContract.Instances.CONTENT_URI` sync for personal Outlook / Teams / Google accounts.
- **Deep-Link Meeting Launcher**: Automatically detects and extracts `teams.microsoft.com/l/meetup-join` URLs from event descriptions/locations so tapping a meeting immediately launches the Teams call.
- **Clear Provider Tags**:
  - `[TEAMS WORK]`: Cyan badge for Work Profile meetings.
  - `[MS TEAMS]`: Indigo badge for Personal Profile Teams calls.
  - `[GMAIL]`: Coral red badge for Google Calendar events.
  - `[MISSED CALL]`: Crimson badge with 1-tap call-back.
- **Zero Fake Data**: All mock/placeholder demo entries have been completely eradicated. When no meetings are pending, an elegant minimal status is shown with 1-tap `[Open Teams]` and `[Open Gmail]` shortcuts.

### 4. Live Internet Speedometer & Clean Icon-Only Top Controls
- **Real-Time Speedometer**: Samples `TrafficStats.getTotalRxBytes()` every 1.5 seconds, dynamically displaying download speed (`↓ 3.4 MB/s`, `↓ 240 KB/s`) in the header next to battery status.
- **Icon-Only Header Buttons**: Removed text words "Arrange" and "Customize". Only sleek, circular icon buttons are retained for layout reordering (`Icons.Rounded.SwapVert`) and settings (`Icons.Rounded.Settings`).

---

## Core Features & Architecture

### Adjustable Home Screen Layout (Pull Sections Up / Down)
- Tap the **Arrange (`⇅`)** icon button in the top header to enter Arrange Mode.
- Each section provides clickable **`[▲ UP]`** and **`[▼ DOWN]`** buttons to immediately swap sections into your preferred position.
- Quick Presets in Settings: `Default`, `Agenda First`, `Apps First`.
- Layout state persists across reboots via `SharedPreferences`.

### Typography Customization
- **4 Font Styles**: Monospace, Sans-Serif, Serif, and Cursive.
- **4 Font Size Scales**: Compact (85%), Standard (100%), Large (115%), Extra Large (130%).
- Real-time preview inside settings.

### Intuitive Gestures
- **Swipe UP**: Slides up the full alphabetical All-Apps drawer with live search.
- **Swipe RIGHT**: Instant quick-launch Phone / Dialer.
- **Swipe LEFT**: Instant quick-launch Camera.
- **Automated CI/CD**: GitHub Actions workflow compiling `app-debug.apk` using Java 17 (Temurin) and Gradle 8.7.
