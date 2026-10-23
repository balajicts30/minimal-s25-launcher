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

$g.DrawString('SAMSUNG GALAXY S25 -- PURE AMOLED EDITION', $titleFont, $titleBrush, 50, 30)
$g.DrawString('True Fullscreen (No Status Bar) | Neon Glow Icons | Live Internet Speed | Real Teams & Gmail Sync', $subFont, $subBrush, 50, 70)

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
$clockFont = New-Object System.Drawing.Font("Segoe UI", 52, [System.Drawing.FontStyle]::Regular)
$dateFont = New-Object System.Drawing.Font("Segoe UI", 13, [System.Drawing.FontStyle]::Regular)
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

# 1. TOP BAR: Battery + Live Internet Speed + Icon-Only Buttons (NO TEXT)
$g.DrawString('98% [PWR]', $monoFontBold, $whiteBrush, $phoneX + 20, $phoneY + 36)
$g.DrawString('WIFI 3.4 MB/s', $monoFontBold, $cyanBrush, $phoneX + 115, $phoneY + 36)

# Right: Icon-only buttons (Arrange & Settings)
$iconBtnPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(50, 50, 50), 1)
$g.DrawEllipse($iconBtnPen, $phoneX + 365, $phoneY + 32, 28, 28)
$g.DrawString('^v', $monoFontBold, $cyanBrush, $phoneX + 372, $phoneY + 38)

$g.DrawEllipse($iconBtnPen, $phoneX + 405, $phoneY + 32, 28, 28)
$g.DrawString('*', $monoFontBold, $grayBrush, $phoneX + 414, $phoneY + 38)

# CLOCK & DATE (Directly on pure black)
$g.DrawString('09:15', $clockFont, $whiteBrush, $phoneX + 16, $phoneY + 85)
$g.DrawString('Friday, October 23, 2026', $dateFont, $grayBrush, $phoneX + 22, $phoneY + 165)

# Detox Pill
$pillPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(34, 34, 34), 1)
$g.DrawRectangle($pillPen, $phoneX + 22, $phoneY + 195, 155, 24)
$greenBrush = New-Object System.Drawing.SolidBrush $neonGreen
$g.DrawString('[DETOX] 1h 48m today', $monoFontSm, $grayBrush, $phoneX + 30, $phoneY + 200)

# 2. FAVORITES WITH NEON GLOW ICONS
$g.DrawString('FAVORITES', $sectionFont, $dimBrush, $phoneX + 22, $phoneY + 250)

$neonApps = @(
    @{ Name = 'Maps'; Abbr = 'MAP'; Color = $neonEmerald; X = $phoneX + 38 },
    @{ Name = 'WhatsApp'; Abbr = 'WA'; Color = $neonGreen; X = $phoneX + 142 },
    @{ Name = 'Instagram'; Abbr = 'IG'; Color = $neonPink; X = $phoneX + 246 },
    @{ Name = 'Telegram'; Abbr = 'TG'; Color = $neonCyan; X = $phoneX + 350 }
)

foreach ($app in $neonApps) {
    # Radiant outer glow aura
    $auraColor = [System.Drawing.Color]::FromArgb(45, $app.Color.R, $app.Color.G, $app.Color.B)
    $auraBrush = New-Object System.Drawing.SolidBrush $auraColor
    $g.FillEllipse($auraBrush, $app.X - 10, $phoneY + 265, 74, 74)

    # Mid glow ring
    $ringColor = [System.Drawing.Color]::FromArgb(85, $app.Color.R, $app.Color.G, $app.Color.B)
    $ringBrush = New-Object System.Drawing.SolidBrush $ringColor
    $g.FillEllipse($ringBrush, $app.X - 4, $phoneY + 271, 62, 62)

    # Pure black center
    $blackBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::Black)
    $g.FillEllipse($blackBrush, $app.X, $phoneY + 275, 54, 54)

    # Crisp neon border
    $borderPen = New-Object System.Drawing.Pen ($app.Color), 2
    $g.DrawEllipse($borderPen, $app.X, $phoneY + 275, 54, 54)

    # Glyph inside
    $appBrush = New-Object System.Drawing.SolidBrush ($app.Color)
    $g.DrawString($app.Abbr, $monoFontBold, $appBrush, $app.X + 13, $phoneY + 292)
    $g.DrawString($app.Name, $monoFontSm, $grayBrush, $app.X - 2, $phoneY + 338)
}

# 3. NOTIFICATIONS (Clean True AMOLED)
$g.DrawString('NOTIFICATIONS', $sectionFont, $dimBrush, $phoneX + 22, $phoneY + 380)

# Card 1 (Pitch black background with neon edge)
$cPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(35, 35, 35), 1)
$g.DrawRectangle($cPen, $phoneX + 22, $phoneY + 402, 200, 68)

$waPen = New-Object System.Drawing.Pen $neonGreen, 1.5
$g.DrawEllipse($waPen, $phoneX + 32, $phoneY + 418, 24, 24)
$g.DrawString('W', $monoFontBold, $greenBrush, $phoneX + 38, $phoneY + 422)
$g.DrawString('WhatsApp | 2m', $monoFontSm, $cyanBrush, $phoneX + 64, $phoneY + 412)
$g.DrawString('David: Standup in 10', $monoFontBold, $whiteBrush, $phoneX + 64, $phoneY + 430)
$g.DrawString('Let us sync on prod', $monoFontSm, $dimBrush, $phoneX + 64, $phoneY + 448)

# Card 2
$g.DrawRectangle($cPen, $phoneX + 232, $phoneY + 402, 200, 68)
$tgPen = New-Object System.Drawing.Pen $neonCyan, 1.5
$g.DrawEllipse($tgPen, $phoneX + 242, $phoneY + 418, 24, 24)
$g.DrawString('T', $monoFontBold, $cyanBrush, $phoneX + 248, $phoneY + 422)
$g.DrawString('Telegram | 8m', $monoFontSm, $cyanBrush, $phoneX + 274, $phoneY + 412)
$g.DrawString('Dev Team: v2.4 Live', $monoFontBold, $whiteBrush, $phoneX + 274, $phoneY + 430)
$g.DrawString('Samsung S25 APK ready', $monoFontSm, $dimBrush, $phoneX + 274, $phoneY + 448)

# 4. REAL AGENDA (WORK PROFILE TEAMS + PERSONAL TEAMS + GMAIL - NO FAKE DATA)
$g.DrawString('AGENDA & MEETINGS', $sectionFont, $dimBrush, $phoneX + 22, $phoneY + 495)

# Row 1: Real Work Profile Teams Meeting
$g.DrawRectangle($cPen, $phoneX + 22, $phoneY + 518, $phoneW - 44, 48)
$g.DrawString('10:30 AM', $monoFontBold, $whiteBrush, $phoneX + 32, $phoneY + 533)
$g.DrawString('Sprint Architecture Review', $monoFontBold, $whiteBrush, $phoneX + 115, $phoneY + 524)
$g.DrawString('Teams Work Profile | corporate.onmicrosoft.com', $monoFontSm, $dimBrush, $phoneX + 115, $phoneY + 542)
$indigoBrush = New-Object System.Drawing.SolidBrush $neonCyan
$g.DrawString('[TEAMS WORK]', $monoFontBold, $indigoBrush, $phoneX + $phoneW - 130, $phoneY + 533)

# Row 2: Personal Teams Meeting
$g.DrawRectangle($cPen, $phoneX + 22, $phoneY + 574, $phoneW - 44, 48)
$g.DrawString('02:00 PM', $monoFontBold, $whiteBrush, $phoneX + 32, $phoneY + 589)
$g.DrawString('Client Consultation Call', $monoFontBold, $whiteBrush, $phoneX + 115, $phoneY + 580)
$g.DrawString('MS Teams | Personal Profile', $monoFontSm, $dimBrush, $phoneX + 115, $phoneY + 598)
$teamsBrush = New-Object System.Drawing.SolidBrush $neonIndigo
$g.DrawString('[MS TEAMS]', $monoFontBold, $teamsBrush, $phoneX + $phoneW - 110, $phoneY + 589)

# Row 3: Gmail Google Calendar
$g.DrawRectangle($cPen, $phoneX + 22, $phoneY + 630, $phoneW - 44, 48)
$g.DrawString('05:15 PM', $monoFontBold, $whiteBrush, $phoneX + 32, $phoneY + 645)
$g.DrawString('Flight Check-in & Boarding', $monoFontBold, $whiteBrush, $phoneX + 115, $phoneY + 636)
$g.DrawString('Gmail | Google Calendar Sync', $monoFontSm, $dimBrush, $phoneX + 115, $phoneY + 654)
$redBrush = New-Object System.Drawing.SolidBrush $neonRed
$g.DrawString('[GMAIL]', $monoFontBold, $redBrush, $phoneX + $phoneW - 85, $phoneY + 645)

# Row 4: Missed Call
$g.DrawRectangle($cPen, $phoneX + 22, $phoneY + 686, $phoneW - 44, 48)
$g.DrawString('CALL MISSED', $monoFontBold, $redBrush, $phoneX + 30, $phoneY + 701)
$g.DrawString('Alex Turner', $monoFontBold, $whiteBrush, $phoneX + 125, $phoneY + 692)
$g.DrawString('Missed call | 14m ago', $monoFontSm, $dimBrush, $phoneX + 125, $phoneY + 710)
$g.DrawString('[CALL BACK]', $monoFontBold, $redBrush, $phoneX + $phoneW - 115, $phoneY + 701)

# Swipe Up hint
$g.DrawString('^  SWIPE UP FOR ALL APPS', $monoFontSm, $dimBrush, $phoneX + 130, $phoneY + 880)


# ----------------- PANEL 2: OVERVIEW & ARCHITECTURE (Right) -----------------
$setX = 570
$setY = 120
$setW = 970
$setH = 920

$cardBigBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(12, 12, 14))
$cardBigPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(35, 35, 42), 1)
$g.FillRectangle($cardBigBg, $setX, $setY, $setW, $setH)
$g.DrawRectangle($cardBigPen, $setX, $setY, $setW, $setH)

$headFont = New-Object System.Drawing.Font("Segoe UI", 16, [System.Drawing.FontStyle]::Bold)
$g.DrawString('TRUE AMOLED ARCHITECTURE & REAL ENTERPRISE SYNC', $headFont, $whiteBrush, $setX + 30, $setY + 25)
$g.DrawString('Zero mock data. Direct Work Profile calendar integration, neon glow, and full-screen immersion.', $monoFontSm, $grayBrush, $setX + 30, $setY + 58)

$boxY = $setY + 95
$innerBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(18, 18, 22))
$innerPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(40, 40, 50), 1)

# Card 1: Neon Glow & True AMOLED
$g.FillRectangle($innerBg, $setX + 30, $boxY, $setW - 60, 240)
$g.DrawRectangle($innerPen, $setX + 30, $boxY, $setW - 60, 240)
$g.DrawString('1. RADIANT NEON GLOW & AMOLED PURITY', $monoFontBold, $cyanBrush, $setX + 45, $boxY + 15)
$g.DrawString('  Pure Pitch Black (#000000): Removed heavy gray card containers. Pixels are 100% unlit.', $monoFontSm, $whiteBrush, $setX + 45, $boxY + 45)
$g.DrawString('  Atmospheric Neon Halos: Multi-layered drawBehind radial glow around every icon matching brand accents:', $monoFontSm, $whiteBrush, $setX + 45, $boxY + 75)
$g.DrawString('    * WhatsApp (#25D366) | Instagram (#FF1493) | Telegram (#00E5FF) | Maps (#10B981)', $monoFontSm, $cyanBrush, $setX + 45, $boxY + 105)
$g.DrawString('    * Teams (#818CF8) | Gmail (#FF3366) | Phone (#39FF14) | Messages (#3B82F6)', $monoFontSm, $cyanBrush, $setX + 45, $boxY + 130)
$g.DrawString('  Fullscreen Immersion: Hidden system status bar and notification shade drop for 100% full screen.', $monoFontSm, $whiteBrush, $setX + 45, $boxY + 160)
$g.DrawString('  Top Bar Controls: Removed text labels. Clean icon-only buttons for Arrange (^v) and Settings (*).', $monoFontSm, $whiteBrush, $setX + 45, $boxY + 190)

# Card 2: Real Work Profile & Teams Sync
$box2Y = $boxY + 260
$g.FillRectangle($innerBg, $setX + 30, $box2Y, $setW - 60, 250)
$g.DrawRectangle($innerPen, $setX + 30, $box2Y, $setW - 60, 250)
$g.DrawString('2. REAL AGENDA: WORK PROFILE TEAMS, PERSONAL TEAMS & GMAIL', $monoFontBold, $cyanBrush, $setX + 45, $box2Y + 15)
$g.DrawString('  Zero Fake/Mock Data: Eradicated all hardcoded fallback demo entries entirely.', $monoFontSm, $whiteBrush, $setX + 45, $box2Y + 45)
$g.DrawString('  Work Profile Cross-Profile Query: Uses CalendarContract.Instances.ENTERPRISE_CONTENT_URI (API 29+)', $monoFontSm, $whiteBrush, $setX + 45, $box2Y + 75)
$g.DrawString('    to read managed corporate calendar events and meetings directly from Samsung Knox Work Profile.', $monoFontSm, $dimBrush, $setX + 45, $box2Y + 95)
$g.DrawString('  Personal Profile Query: Reads standard CalendarContract.Instances.CONTENT_URI for Gmail & Outlook.', $monoFontSm, $whiteBrush, $setX + 45, $box2Y + 125)
$g.DrawString('  Deep Link Resolution: Automatically extracts teams.microsoft.com meeting URLs to join with 1 tap.', $monoFontSm, $whiteBrush, $setX + 45, $box2Y + 155)
$g.DrawString('  Clear Provider Badges: [TEAMS WORK] (cyan), [MS TEAMS] (indigo), [GMAIL] (coral red).', $monoFontSm, $cyanBrush, $setX + 45, $box2Y + 185)
$g.DrawString('  Empty State: Shows clean "No upcoming meetings" with 1-tap [Open Teams] & [Open Gmail] shortcuts.', $monoFontSm, $whiteBrush, $setX + 45, $box2Y + 215)

# Card 3: Live Internet Speed
$box3Y = $box2Y + 270
$g.FillRectangle($innerBg, $setX + 30, $box3Y, $setW - 60, 225)
$g.DrawRectangle($innerPen, $setX + 30, $box3Y, $setW - 60, 225)
$g.DrawString('3. LIVE NETWORK SPEED & HARDWARE OPTIMIZATION', $monoFontBold, $cyanBrush, $setX + 45, $box3Y + 15)
$g.DrawString('  Real-time Internet Speedometer: Continuously samples android.net.TrafficStats.getTotalRxBytes()', $monoFontSm, $whiteBrush, $setX + 45, $box3Y + 45)
$g.DrawString('    every 1.5 seconds, dynamically formatting in KB/s and MB/s directly in the top header.', $monoFontSm, $whiteBrush, $setX + 45, $box3Y + 70)
$g.DrawString('  Works across all network connections: Wi-Fi 6E/7, 5G Ultra Wideband, 4G LTE and mobile hotspot.', $monoFontSm, $dimBrush, $setX + 45, $box3Y + 95)
$g.DrawString('  Screen Time Tracker: Real-time UsageStatsManager detox pill directly on the home screen.', $monoFontSm, $whiteBrush, $setX + 45, $box3Y + 125)
$g.DrawString('  Adjustable Layout Engine: Pull sections UP/DOWN directly on screen or in Settings.', $monoFontSm, $whiteBrush, $setX + 45, $box3Y + 155)
$g.DrawString('  Customizable Typography: Monospace, Sans-Serif, Serif & Cursive with 85% to 130% scaling.', $monoFontSm, $cyanBrush, $setX + 45, $box3Y + 185)

# Save
$outputPath = "C:\Users\BalajiL\minimal-s25-launcher\preview.png"
$bmp.Save($outputPath, [System.Drawing.Imaging.ImageFormat]::Png)

$artifactPath = "C:\Users\BalajiL\.gemini\antigravity-cli\brain\63d8052f-617c-48df-9be1-006ea85fada7\s25_launcher_preview.png"
$bmp.Save($artifactPath, [System.Drawing.Imaging.ImageFormat]::Png)

$g.Dispose()
$bmp.Dispose()

Write-Output "Preview generated successfully at $outputPath and $artifactPath"
