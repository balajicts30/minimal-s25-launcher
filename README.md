# Minimal Launcher — Samsung Galaxy S25 Edition

An ultra-clean, battery-efficient Kotlin Jetpack Compose Android launcher engineered for the Samsung Galaxy S25 / S25+ / S25 Ultra (Dynamic AMOLED 2X, 120Hz display, `targetSdk 34`, `minSdk 26`).

![S25 Minimal Launcher Preview](preview.png)

## Features

- **Pure AMOLED Pitch Black (`#000000`)**: Zero power draw for unlit pixels, saving battery on Samsung OLED displays.
- **Monochrome Minimal Icons**: Clean glyph icons with subtle contrast borders for Phone, Messages, WhatsApp, Camera, Browser, and Photos.
- **Hero Status Header**:
  - Large, elegant digital clock (`72.sp`) & formatted date.
  - Live battery percentage (`%`) with charging lightning bolt indicator (`⚡`).
  - **Screen Time / Digital Detox Badge**: Displays live daily screen time usage (`⏳ 1h 48m today`) via `UsageStatsManager`.
- **Intuitive Gestures**:
  - **Swipe UP**: Slides up the searchable Minimal All-Apps Drawer.
  - **Swipe RIGHT**: Instant quick-launch Phone / Dialer.
  - **Swipe LEFT**: Instant quick-launch Camera.
- **Searchable App Drawer**:
  - Full alphabetical list of installed apps.
  - Live filter search bar with clear button.
- **Cloud APK Build**: Automated GitHub Actions CI workflow using Java 17 (Temurin) and Gradle 8.7 producing `app-debug.apk`.
