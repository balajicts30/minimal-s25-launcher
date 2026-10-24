Add-Type -AssemblyName System.Drawing

$width = 1600
$height = 1100
$bmp = New-Object System.Drawing.Bitmap $width, $height
$g = [System.Drawing.Graphics]::FromImage($bmp)
$g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
$g.TextRenderingHint = [System.Drawing.Text.TextRenderingHint]::AntiAliasGridFit

# Outer Background - Modern dark graphite
$bgBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(6, 6, 8))
$g.FillRectangle($bgBrush, 0, 0, $width, $height)

# Header Title
$titleFont = New-Object System.Drawing.Font("Segoe UI", 20, [System.Drawing.FontStyle]::Bold)
$titleBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(255, 255, 255))
$subFont = New-Object System.Drawing.Font("Consolas", 12, [System.Drawing.FontStyle]::Regular)
$subBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(140, 140, 150))

$g.DrawString('SAMSUNG GALAXY S25 -- ADVANCED MINIMAL AMOLED EDITION', $titleFont, $titleBrush, 50, 30)
$g.DrawString('Gemini Sparkle | Quick Toggles | Unified Gestures (Swipe UP/DOWN, Double-Tap Lock) | Top 10 Recent Drawer', $subFont, $subBrush, 50, 70)

# ----------------- PANEL 1: S25 FULLSCREEN AMOLED SCREEN (Left) -----------------
$phoneX = 60
$phoneY = 120
$phoneW = 460
$phoneH = 920

# Phone Bezel
$bezelBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(18, 18, 22))
$g.FillRectangle($bezelBrush, $phoneX - 6, $phoneY - 6, $phoneW + 12, $phoneH + 12)
$bezelPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(40, 40, 50), 2)
$g.DrawRectangle($bezelPen, $phoneX - 6, $phoneY - 6, $phoneW + 12, $phoneH + 12)

# AMOLED Pitch Black Screen (100% #000000)
$screenBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::Black)
$g.FillRectangle($screenBrush, $phoneX, $phoneY, $phoneW, $phoneH)

# Camera hole punch at top center
$punchBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(12, 12, 14))
$g.FillEllipse($punchBrush, $phoneX + ($phoneW / 2) - 7, $phoneY + 10, 14, 14)

# Typography
$monoFontSm = New-Object System.Drawing.Font("Consolas", 10, [System.Drawing.FontStyle]::Regular)
$monoFontBold = New-Object System.Drawing.Font("Consolas", 11, [System.Drawing.FontStyle]::Bold)
$clockFont = New-Object System.Drawing.Font("Segoe UI", 48, [System.Drawing.FontStyle]::Regular)
$dateFont = New-Object System.Drawing.Font("Segoe UI", 12, [System.Drawing.FontStyle]::Regular)
$sectionFont = New-Object System.Drawing.Font("Consolas", 9, [System.Drawing.FontStyle]::Bold)

$cyanBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(0, 229, 255))
$whiteBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::White)
$grayBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(150, 150, 150))
$dimBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(75, 75, 75))
$neonGreen = [System.Drawing.Color]::FromArgb(37, 211, 102)
$neonPink = [System.Drawing.Color]::FromArgb(255, 20, 147)
$neonCyan = [System.Drawing.Color]::FromArgb(0, 229, 255)
$neonEmerald = [System.Drawing.Color]::FromArgb(16, 185, 129)
$neonIndigo = [System.Drawing.Color]::FromArgb(129, 140, 248)
$neonRed = [System.Drawing.Color]::FromArgb(255, 51, 102)
$neonAmber = [System.Drawing.Color]::FromArgb(255, 214, 0)

# 1. TOP BAR: Battery + Live Internet Speed + Icon-Only Buttons (NO TEXT)
$g.DrawString('98% [PWR]', $monoFontBold, $whiteBrush, $phoneX + 20, $phoneY + 36)
$g.DrawString('WIFI 4.2 MB/s', $monoFontBold, $cyanBrush, $phoneX + 115, $phoneY + 36)

# Right: Icon-only buttons (Arrange & Settings)
$iconBtnPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(50, 50, 50), 1)
$g.DrawEllipse($iconBtnPen, $phoneX + 365, $phoneY + 32, 28, 28)
$g.DrawString('^v', $monoFontBold, $cyanBrush, $phoneX + 372, $phoneY + 38)

$g.DrawEllipse($iconBtnPen, $phoneX + 405, $phoneY + 32, 28, 28)
$g.DrawString('*', $monoFontBold, $grayBrush, $phoneX + 414, $phoneY + 38)

# CLOCK + GEMINI AI SPARKLE BUTTON ON THE RIGHT
$g.DrawString('09:15', $clockFont, $whiteBrush, $phoneX + 16, $phoneY + 74)

# Gemini AI Sparkle Icon Button
$geminiPen = New-Object System.Drawing.Pen ($neonCyan), 1.5
$g.DrawEllipse($geminiPen, $phoneX + 385, $phoneY + 86, 36, 36)
$geminiBrush = New-Object System.Drawing.SolidBrush $neonCyan
$geminiFont = New-Object System.Drawing.Font("Consolas", 11, [System.Drawing.FontStyle]::Bold)
$g.DrawString('AI', $geminiFont, $geminiBrush, $phoneX + 394, $phoneY + 94)

$g.DrawString('Friday, October 24, 2026', $dateFont, $grayBrush, $phoneX + 22, $phoneY + 148)

# Detox Pill
$pillPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(34, 34, 34), 1)
$g.DrawRectangle($pillPen, $phoneX + 22, $phoneY + 176, 155, 24)
$g.DrawString('[DETOX] 1h 48m today', $monoFontSm, $grayBrush, $phoneX + 30, $phoneY + 181)

# Double Tap to Lock Area Hint
$g.DrawString('DOUBLE TAP TO LOCK', $sectionFont, $dimBrush, $phoneX + 260, $phoneY + 182)

# 2. FAVORITES: ALL APPS / CURATED SELECTION (ADJUSTABLE 44dp ICONS)
$g.DrawString('FAVORITES (CUSTOM / ALL APPS)', $sectionFont, $dimBrush, $phoneX + 22, $phoneY + 220)

$neonApps = @(
    @{ Name = 'Maps'; Abbr = 'MAP'; Color = $neonEmerald; X = $phoneX + 45 },
    @{ Name = 'WhatsApp'; Abbr = 'WA'; Color = $neonGreen; X = $phoneX + 145 },
    @{ Name = 'Instagram'; Abbr = 'IG'; Color = $neonPink; X = $phoneX + 245 },
    @{ Name = 'Telegram'; Abbr = 'TG'; Color = $neonCyan; X = $phoneX + 345 }
)

foreach ($app in $neonApps) {
    # Subtle contained neon halo (doesn't bleed out or get trimmed)
    $auraColor = [System.Drawing.Color]::FromArgb(35, $app.Color.R, $app.Color.G, $app.Color.B)
    $auraBrush = New-Object System.Drawing.SolidBrush $auraColor
    $g.FillEllipse($auraBrush, $app.X - 5, $phoneY + 237, 54, 54)

    # Pure black center
    $blackBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::Black)
    $g.FillEllipse($blackBrush, $app.X, $phoneY + 242, 44, 44)

    # Crisp neon border (1.2dp)
    $borderPen = New-Object System.Drawing.Pen ($app.Color), 1.5
    $g.DrawEllipse($borderPen, $app.X, $phoneY + 242, 44, 44)

    # Glyph inside
    $appBrush = New-Object System.Drawing.SolidBrush ($app.Color)
    $g.DrawString($app.Abbr, $monoFontBold, $appBrush, $app.X + 9, $phoneY + 256)
    $g.DrawString($app.Name, $monoFontSm, $grayBrush, $app.X - 2, $phoneY + 294)
}

# 3. QUICK SYSTEM TOGGLES BAR (ABOVE NOTIFICATIONS IN MIDDLE OF SCREEN)
$g.DrawString('QUICK SYSTEM TOGGLES', $sectionFont, $dimBrush, $phoneX + 22, $phoneY + 332)

$toggleBoxPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(35, 35, 42), 1)
$g.DrawRectangle($toggleBoxPen, $phoneX + 22, $phoneY + 350, $phoneW - 44, 56)

# Wi-Fi Toggle
$wifiPen = New-Object System.Drawing.Pen ($neonCyan), 1.2
$g.DrawEllipse($wifiPen, $phoneX + 45, $phoneY + 358, 26, 26)
$g.DrawString('WF', $monoFontBold, $cyanBrush, $phoneX + 50, $phoneY + 363)
$g.DrawString('WIFI ON', $monoFontSm, $cyanBrush, $phoneX + 36, $phoneY + 388)

# Silent / Sound Mode
$ringerPen = New-Object System.Drawing.Pen ($neonGreen), 1.2
$ringerBrush = New-Object System.Drawing.SolidBrush $neonGreen
$g.DrawEllipse($ringerPen, $phoneX + 150, $phoneY + 358, 26, 26)
$g.DrawString('VOL', $monoFontBold, $ringerBrush, $phoneX + 152, $phoneY + 363)
$g.DrawString('SOUND', $monoFontSm, $ringerBrush, $phoneX + 144, $phoneY + 388)

# Bluetooth Toggle
$btPen = New-Object System.Drawing.Pen ($neonIndigo), 1.2
$btBrush = New-Object System.Drawing.SolidBrush $neonIndigo
$g.DrawEllipse($btPen, $phoneX + 255, $phoneY + 358, 26, 26)
$g.DrawString('BT', $monoFontBold, $btBrush, $phoneX + 261, $phoneY + 363)
$g.DrawString('BT ON', $monoFontSm, $btBrush, $phoneX + 252, $phoneY + 388)

# Android Settings
$setPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(120, 120, 120)), 1.2
$g.DrawEllipse($setPen, $phoneX + 360, $phoneY + 358, 26, 26)
$g.DrawString('SET', $monoFontBold, $whiteBrush, $phoneX + 364, $phoneY + 363)
$g.DrawString('SYSTEM', $monoFontSm, $grayBrush, $phoneX + 354, $phoneY + 388)

# 4. NOTIFICATIONS (Clean True AMOLED + SWIPE DOWN TO EXPAND SYSTEM SHADE)
$g.DrawString('NOTIFICATIONS (SWIPE DOWN FOR SYSTEM SHADE)', $sectionFont, $dimBrush, $phoneX + 22, $phoneY + 424)

$cPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(35, 35, 35), 1)
$g.DrawRectangle($cPen, $phoneX + 22, $phoneY + 442, 200, 62)

$greenBrush = New-Object System.Drawing.SolidBrush $neonGreen
$waPen = New-Object System.Drawing.Pen $neonGreen, 1.2
$g.DrawEllipse($waPen, $phoneX + 32, $phoneY + 454, 20, 20)
$g.DrawString('W', $monoFontBold, $greenBrush, $phoneX + 37, $phoneY + 456)
$g.DrawString('WhatsApp | 2m', $monoFontSm, $cyanBrush, $phoneX + 60, $phoneY + 448)
$g.DrawString('David: Standup in 10', $monoFontBold, $whiteBrush, $phoneX + 60, $phoneY + 465)
$g.DrawString('Let us sync on prod', $monoFontSm, $dimBrush, $phoneX + 60, $phoneY + 482)

$g.DrawRectangle($cPen, $phoneX + 232, $phoneY + 442, 200, 62)
$tgPen = New-Object System.Drawing.Pen $neonCyan, 1.2
$g.DrawEllipse($tgPen, $phoneX + 242, $phoneY + 454, 20, 20)
$g.DrawString('T', $monoFontBold, $cyanBrush, $phoneX + 247, $phoneY + 456)
$g.DrawString('Telegram | 8m', $monoFontSm, $cyanBrush, $phoneX + 270, $phoneY + 448)
$g.DrawString('Dev Team: v2.5 Live', $monoFontBold, $whiteBrush, $phoneX + 270, $phoneY + 465)
$g.DrawString('Galaxy S25 APK ready', $monoFontSm, $dimBrush, $phoneX + 270, $phoneY + 482)

# 5. CRYSTAL-CLEAR AGENDA (TEAMS WORK PROFILE + MS TEAMS + GMAIL)
$g.DrawString('UPCOMING MEETINGS & CALLS', $sectionFont, $dimBrush, $phoneX + 22, $phoneY + 524)

# Row 1: Real Work Profile Teams Meeting
$g.DrawRectangle($cPen, $phoneX + 22, $phoneY + 544, $phoneW - 44, 62)
$g.DrawString('TODAY * 10:30 AM - 11:30 AM', $monoFontBold, $cyanBrush, $phoneX + 34, $phoneY + 550)
$g.DrawString('[TEAMS WORK]', $monoFontBold, $cyanBrush, $phoneX + $phoneW - 130, $phoneY + 550)
$g.DrawString('Sprint Architecture & Review Sync', $monoFontBold, $whiteBrush, $phoneX + 34, $phoneY + 567)
$g.DrawString('corporate.onmicrosoft.com', $monoFontSm, $dimBrush, $phoneX + 34, $phoneY + 584)
$g.DrawString('TAP TO JOIN ^', $monoFontBold, $cyanBrush, $phoneX + $phoneW - 120, $phoneY + 584)

# Row 2: Personal Teams Meeting
$indigoBrush = New-Object System.Drawing.SolidBrush $neonIndigo
$g.DrawRectangle($cPen, $phoneX + 22, $phoneY + 614, $phoneW - 44, 62)
$g.DrawString('TODAY * 02:00 PM - 02:45 PM', $monoFontBold, $indigoBrush, $phoneX + 34, $phoneY + 620)
$g.DrawString('[MS TEAMS]', $monoFontBold, $indigoBrush, $phoneX + $phoneW - 110, $phoneY + 620)
$g.DrawString('Client Consultation & Onboarding', $monoFontBold, $whiteBrush, $phoneX + 34, $phoneY + 637)
$g.DrawString('Personal Profile * Teams Meeting', $monoFontSm, $dimBrush, $phoneX + 34, $phoneY + 654)
$g.DrawString('TAP TO JOIN ^', $monoFontBold, $indigoBrush, $phoneX + $phoneW - 120, $phoneY + 654)

# Row 3: Gmail Google Calendar
$g.DrawRectangle($cPen, $phoneX + 22, $phoneY + 684, $phoneW - 44, 62)
$redBrush = New-Object System.Drawing.SolidBrush $neonRed
$g.DrawString('TOMORROW * 05:15 PM', $monoFontBold, $redBrush, $phoneX + 34, $phoneY + 690)
$g.DrawString('[GMAIL]', $monoFontBold, $redBrush, $phoneX + $phoneW - 85, $phoneY + 690)
$g.DrawString('Flight Check-in & Departure', $monoFontBold, $whiteBrush, $phoneX + 34, $phoneY + 707)
$g.DrawString('Google Calendar * Flights Sync', $monoFontSm, $dimBrush, $phoneX + 34, $phoneY + 724)

# Row 4: Missed Call
$g.DrawRectangle($cPen, $phoneX + 22, $phoneY + 754, $phoneW - 44, 52)
$g.DrawString('MISSED CALL * 14m ago', $monoFontBold, $redBrush, $phoneX + 34, $phoneY + 760)
$g.DrawString('[CALL BACK]', $monoFontBold, $redBrush, $phoneX + $phoneW - 115, $phoneY + 760)
$g.DrawString('Alex Turner', $monoFontBold, $whiteBrush, $phoneX + 34, $phoneY + 778)
$g.DrawString('Tap to call back (07982 123456)', $monoFontSm, $dimBrush, $phoneX + 130, $phoneY + 780)

# Swipe Up for Apps hint
$g.DrawString('^  SWIPE UP FOR ALL APPS & RECENT DRAWER', $monoFontBold, $cyanBrush, $phoneX + 68, $phoneY + 875)


# ----------------- PANEL 2: OVERVIEW & NEW FEATURES BREAKDOWN (Right) -----------------
$setX = 570
$setY = 120
$setW = 970
$setH = 920

$cardBigBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(12, 12, 14))
$cardBigPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(35, 35, 42), 1)
$g.FillRectangle($cardBigBg, $setX, $setY, $setW, $setH)
$g.DrawRectangle($cardBigPen, $setX, $setY, $setW, $setH)

$headFont = New-Object System.Drawing.Font("Segoe UI", 16, [System.Drawing.FontStyle]::Bold)
$g.DrawString('GALAXY S25 MINIMAL LAUNCHER -- FULL FEATURE SUITE', $headFont, $whiteBrush, $setX + 30, $setY + 22)
$g.DrawString('Gemini AI integration, unified gesture controls, quick toggles bar, all-apps favorites & top 10 drawer.', $monoFontSm, $grayBrush, $setX + 30, $setY + 54)

$boxY = $setY + 88
$innerBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(18, 18, 22))
$innerPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(40, 40, 50), 1)

# Card 1: Gemini AI + Unified Gestures
$g.FillRectangle($innerBg, $setX + 30, $boxY, $setW - 60, 245)
$g.DrawRectangle($innerPen, $setX + 30, $boxY, $setW - 60, 245)
$g.DrawString('1. GEMINI AI SPARKLE & UNIFIED GESTURES', $monoFontBold, $cyanBrush, $setX + 45, $boxY + 14)
$g.DrawString('  Google Gemini Sparkle Icon: Dedicated [AI] button next to the time launches Gemini app/assistant.', $monoFontSm, $whiteBrush, $setX + 45, $boxY + 42)
$g.DrawString('  Swipe UP -> App Drawer: Seamlessly fling upwards anywhere on the screen to open the app drawer.', $monoFontSm, $whiteBrush, $setX + 45, $boxY + 70)
$g.DrawString('  Swipe DOWN -> Notifications: Effortlessly pulls down the Android system notification shade.', $monoFontSm, $whiteBrush, $setX + 45, $boxY + 98)
$g.DrawString('  Double-Tap to Lock Screen: Tap twice on any black area of the AMOLED screen to lock phone instantly.', $monoFontSm, $cyanBrush, $setX + 45, $boxY + 126)
$g.DrawString('  Device Admin Security: Backed by S25DeviceAdminReceiver and force-lock policy.', $monoFontSm, $whiteBrush, $setX + 45, $boxY + 154)
$g.DrawString('  Edge Swipes: Swipe RIGHT launches Dialer, swipe LEFT launches Camera.', $monoFontSm, $whiteBrush, $setX + 45, $boxY + 182)
$g.DrawString('  Zero Accidental Triggers: Directional vector checks ensure smooth scrolling without false gestures.', $monoFontSm, $grayBrush, $setX + 45, $boxY + 210)

# Card 2: Quick Toggles Bar & Layout Sizing
$box2Y = $boxY + 260
$g.FillRectangle($innerBg, $setX + 30, $box2Y, $setW - 60, 255)
$g.DrawRectangle($innerPen, $setX + 30, $box2Y, $setW - 60, 255)
$g.DrawString('2. MIDDLE QUICK TOGGLES & SIZING CONTROLS', $monoFontBold, $cyanBrush, $setX + 45, $box2Y + 14)
$g.DrawString('  Center Quick Toggles Bar: Wi-Fi (on/off), Sound Mode (Normal/Vibrate/Silent), Bluetooth, Settings.', $monoFontSm, $whiteBrush, $setX + 45, $box2Y + 42)
$g.DrawString('  Adjustable Icon Sizing: Choose between 36dp (compact), 44dp (standard), 52dp, or 60dp in Settings.', $monoFontSm, $cyanBrush, $setX + 45, $box2Y + 70)
$g.DrawString('  Adjustable Layout Width: Switch horizontal edge margins between Wide (8dp), Standard, Narrow (28dp).', $monoFontSm, $whiteBrush, $setX + 45, $box2Y + 98)
$g.DrawString('  Adjustable Section Spacing: Set vertical spacing between 12dp (compact), 20dp, or 28dp (spacious).', $monoFontSm, $whiteBrush, $setX + 45, $box2Y + 126)
$g.DrawString('  Pull Sections UP / DOWN: Easily reorder Clock, Apps, Toggles, Notifications and Agenda on the fly.', $monoFontSm, $whiteBrush, $setX + 45, $box2Y + 154)
$g.DrawString('  Pure Pitch Black (#000000): Zero battery drain on Samsung S25 Dynamic AMOLED 2X panel.', $monoFontSm, $whiteBrush, $setX + 45, $box2Y + 182)
$g.DrawString('  Live Speedometer: Real-time TrafficStats download meter (e.g. NET 4.2 MB/s) in status header.', $monoFontSm, $cyanBrush, $setX + 45, $box2Y + 210)

# Card 3: All Apps Favorites & Top 10 Drawer
$box3Y = $box2Y + 270
$g.FillRectangle($innerBg, $setX + 30, $box3Y, $setW - 60, 220)
$g.DrawRectangle($innerPen, $setX + 30, $box3Y, $setW - 60, 220)
$g.DrawString('3. ALL-APPS FAVORITES & TOP 10 RECENT DRAWER', $monoFontBold, $cyanBrush, $setX + 45, $box3Y + 14)
$g.DrawString('  Add ALL Apps to Favorites: Settings now features [ADD ALL APPS], [CURATED (6)] & [CLEAR] buttons.', $monoFontSm, $cyanBrush, $setX + 45, $box3Y + 42)
$g.DrawString('  Search & Toggle ANY App: Filter through all installed apps and toggle checkboxes directly into Favorites.', $monoFontSm, $whiteBrush, $setX + 45, $box3Y + 70)
$g.DrawString('  Dynamic Neon Glyphs: Every added app automatically receives a vibrant neon halo and lettermark.', $monoFontSm, $whiteBrush, $setX + 45, $box3Y + 98)
$g.DrawString('  Top 10 Recent & Frequent in Drawer: UsageStatsManager + launch tracker displays top 10 apps first.', $monoFontSm, $cyanBrush, $setX + 45, $box3Y + 126)
$g.DrawString('  A-Z Complete Drawer: Full alphabetical app listing follows with instant real-time search.', $monoFontSm, $whiteBrush, $setX + 45, $box3Y + 154)
$g.DrawString('  Multi-Profile Teams & Gmail: Real Work Profile & personal meeting resolution with zero fake data.', $monoFontSm, $whiteBrush, $setX + 45, $box3Y + 182)

# Save
$outputPath = "C:\Users\BalajiL\minimal-s25-launcher\preview.png"
$bmp.Save($outputPath, [System.Drawing.Imaging.ImageFormat]::Png)

$artifactPath = "C:\Users\BalajiL\.gemini\antigravity-cli\brain\63d8052f-617c-48df-9be1-006ea85fada7\s25_launcher_preview.png"
$bmp.Save($artifactPath, [System.Drawing.Imaging.ImageFormat]::Png)

$g.Dispose()
$bmp.Dispose()

Write-Output "Preview generated successfully at $outputPath and $artifactPath"
