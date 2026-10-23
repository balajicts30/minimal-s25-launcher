# Minimal Launcher — Samsung Galaxy S25 Pure AMOLED Edition

An ultra-clean, battery-efficient Kotlin Jetpack Compose Android launcher engineered for the Samsung Galaxy S25 / S25+ / S25 Ultra (Dynamic AMOLED 2X, 120Hz display, `targetSdk 34`, `minSdk 26`).

![S25 Minimal Launcher Preview](preview.png)

## What's New: Compact Untrimmed Icons & Crystal-Clear Agenda

### 1. Compact Untrimmed Neon Icons (44dp)
- **Reduced Sizing**: Reduced icon containers from 54dp to **44dp** with **20dp** vector glyphs.
- **Contained Subtle Neon Halo**: Reduced glow radius to `0.58x` with soft alpha (`0.16`), keeping the luminous glow contained within bounds so it never trims or bleeds against adjacent items.
- **Unclipped Container**: Removed outer rounded corner clipping on the parent column so icons render perfectly circular and clean.
- **Comfortable 4-Column Spacing**: Provides ample breathing room across all AMOLED screen widths.

### 2. Crystal-Clear Meetings (Teams Work Profile, MS Teams & Gmail)
- **Multi-Source Calendar Metadata Resolution**: Pre-queries `CalendarContract.Calendars` for `account_name`, `account_type`, and `calendar_displayName`. This accurately detects corporate work emails (even when they don't contain the word "microsoft" in the domain) alongside Google accounts.
- **Dual Profile & Fallback Engine**: Queries:
  1. `CalendarContract.Instances.CONTENT_URI` (Personal Profile)
  2. `CalendarContract.Instances.ENTERPRISE_CONTENT_URI` (API 29+ Samsung Knox Work Profile)
  3. `CalendarContract.Events.CONTENT_URI` fallback (catches direct Samsung OneUI synchronized events)
- **High-Clarity Time Formatting**: Displays exact status: `TODAY • 10:30 AM - 11:30 AM`, `TOMORROW • 02:00 PM`, `MON, OCT 26 • 09:00 AM`.
- **Unclipped Full Title**: Meeting names display across up to 2 lines without truncation.
- **Teams Deep-Link Detection**: Automatically identifies `teams.microsoft.com/l/meetup-join` URLs in descriptions/locations with a 1-tap `TAP TO JOIN ↗` shortcut.
- **Provider Tags**:
  - `[TEAMS WORK]`: Cyan badge for Work Profile corporate meetings.
  - `[MS TEAMS]`: Indigo badge for Personal Profile Teams calls.
  - `[GMAIL]`: Coral red badge for Google Calendar meetings.
  - `[MISSED CALL]`: Crimson badge with 1-tap dial back.
- **Zero Fake Data**: Fallback demo data has been completely removed. When no meetings are scheduled, an empty state provides 1-tap `[Open Teams]`, `[Open Gmail]`, and `[Refresh ↻]` buttons.

### 3. Pure Fullscreen AMOLED & Live Internet Speed
- **Hidden Status Bar & Notification Drop**: Full-screen immersion via `WindowInsetsControllerCompat.hide(Type.statusBars())`.
- **Live Internet Speedometer**: Real-time `TrafficStats` download speed (e.g. `↓ 3.4 MB/s`, `↓ 240 KB/s`) in the header next to battery status.
- **Icon-Only Header Controls**: Minimal circular buttons for layout reordering (`Icons.Rounded.SwapVert`) and settings (`Icons.Rounded.Settings`).

---

## Core Features & Architecture

### Adjustable Home Screen Layout (Pull Sections Up / Down)
- Tap the **Arrange (`⇅`)** icon button in the top header to enter Arrange Mode.
- Each section provides clickable **`[▲ UP]`** and **`[▼ DOWN]`** buttons to swap section order.
- Quick Presets in Settings: `Default`, `Agenda First`, `Apps First`.

### Typography Customization
- **4 Font Styles**: Monospace, Sans-Serif, Serif, and Cursive.
- **4 Font Size Scales**: Compact (85%), Standard (100%), Large (115%), Extra Large (130%).

### Intuitive Gestures
- **Swipe UP**: Slides up the full alphabetical All-Apps drawer with live search.
- **Swipe RIGHT**: Instant quick-launch Phone / Dialer.
- **Swipe LEFT**: Instant quick-launch Camera.
- **Automated CI/CD**: GitHub Actions workflow compiling `app-debug.apk` using Java 17 (Temurin) and Gradle 8.7.
