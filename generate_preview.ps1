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
$g.DrawString('Untrimmed Compact Neon Icons (44dp) | Crystal-Clear Teams & Gmail Agenda | Live Internet Speed', $subFont, $subBrush, 50, 70)

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
$g.DrawString('09:15', $clockFont, $whiteBrush, $phoneX + 16, $phoneY + 80)
$g.DrawString('Friday, October 23, 2026', $dateFont, $grayBrush, $phoneX + 22, $phoneY + 160)

# Detox Pill
$pillPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(34, 34, 34), 1)
$g.DrawRectangle($pillPen, $phoneX + 22, $phoneY + 190, 155, 24)
$greenBrush = New-Object System.Drawing.SolidBrush $neonGreen
$g.DrawString('[DETOX] 1h 48m today', $monoFontSm, $grayBrush, $phoneX + 30, $phoneY + 195)

# 2. FAVORITES: COMPACT 44dp ICONS WITH SUBTLE NEON GLOW (NOT TRIMMED)
$g.DrawString('FAVORITES', $sectionFont, $dimBrush, $phoneX + 22, $phoneY + 240)

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
    $g.FillEllipse($auraBrush, $app.X - 5, $phoneY + 257, 54, 54)

    # Pure black center
    $blackBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::Black)
    $g.FillEllipse($blackBrush, $app.X, $phoneY + 262, 44, 44)

    # Crisp neon border (1.2dp)
    $borderPen = New-Object System.Drawing.Pen ($app.Color), 1.5
    $g.DrawEllipse($borderPen, $app.X, $phoneY + 262, 44, 44)

    # Glyph inside
    $appBrush = New-Object System.Drawing.SolidBrush ($app.Color)
    $g.DrawString($app.Abbr, $monoFontBold, $appBrush, $app.X + 9, $phoneY + 276)
    $g.DrawString($app.Name, $monoFontSm, $grayBrush, $app.X - 2, $phoneY + 314)
}

# 3. NOTIFICATIONS (Clean True AMOLED)
$g.DrawString('NOTIFICATIONS', $sectionFont, $dimBrush, $phoneX + 22, $phoneY + 355)

$cPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(35, 35, 35), 1)
$g.DrawRectangle($cPen, $phoneX + 22, $phoneY + 375, 200, 66)

$waPen = New-Object System.Drawing.Pen $neonGreen, 1.2
$g.DrawEllipse($waPen, $phoneX + 32, $phoneY + 390, 22, 22)
$g.DrawString('W', $monoFontBold, $greenBrush, $phoneX + 37, $phoneY + 393)
$g.DrawString('WhatsApp | 2m', $monoFontSm, $cyanBrush, $phoneX + 62, $phoneY + 384)
$g.DrawString('David: Standup in 10', $monoFontBold, $whiteBrush, $phoneX + 62, $phoneY + 402)
$g.DrawString('Let us sync on prod', $monoFontSm, $dimBrush, $phoneX + 62, $phoneY + 420)

$g.DrawRectangle($cPen, $phoneX + 232, $phoneY + 375, 200, 66)
$tgPen = New-Object System.Drawing.Pen $neonCyan, 1.2
$g.DrawEllipse($tgPen, $phoneX + 242, $phoneY + 390, 22, 22)
$g.DrawString('T', $monoFontBold, $cyanBrush, $phoneX + 247, $phoneY + 393)
$g.DrawString('Telegram | 8m', $monoFontSm, $cyanBrush, $phoneX + 272, $phoneY + 384)
$g.DrawString('Dev Team: v2.5 Live', $monoFontBold, $whiteBrush, $phoneX + 272, $phoneY + 402)
$g.DrawString('Samsung S25 APK ready', $monoFontSm, $dimBrush, $phoneX + 272, $phoneY + 420)

# 4. CRYSTAL-CLEAR AGENDA (TEAMS WORK PROFILE + MS TEAMS + GMAIL)
$g.DrawString('UPCOMING MEETINGS & CALLS', $sectionFont, $dimBrush, $phoneX + 22, $phoneY + 465)

# Row 1: Real Work Profile Teams Meeting
$g.DrawRectangle($cPen, $phoneX + 22, $phoneY + 488, $phoneW - 44, 66)
$g.DrawString('TODAY * 10:30 AM - 11:30 AM', $monoFontBold, $cyanBrush, $phoneX + 34, $phoneY + 496)
$g.DrawString('[TEAMS WORK]', $monoFontBold, $cyanBrush, $phoneX + $phoneW - 130, $phoneY + 496)
$g.DrawString('Sprint Architecture & Review Sync', $monoFontBold, $whiteBrush, $phoneX + 34, $phoneY + 514)
$g.DrawString('corporate.onmicrosoft.com', $monoFontSm, $dimBrush, $phoneX + 34, $phoneY + 534)
$g.DrawString('TAP TO JOIN ^', $monoFontBold, $cyanBrush, $phoneX + $phoneW - 120, $phoneY + 534)

# Row 2: Personal Teams Meeting
$g.DrawRectangle($cPen, $phoneX + 22, $phoneY + 564, $phoneW - 44, 66)
$indigoBrush = New-Object System.Drawing.SolidBrush $neonIndigo
$g.DrawString('TODAY * 02:00 PM - 02:45 PM', $monoFontBold, $indigoBrush, $phoneX + 34, $phoneY + 572)
$g.DrawString('[MS TEAMS]', $monoFontBold, $indigoBrush, $phoneX + $phoneW - 110, $phoneY + 572)
$g.DrawString('Client Consultation & Onboarding', $monoFontBold, $whiteBrush, $phoneX + 34, $phoneY + 590)
$g.DrawString('Personal Profile * Teams Meeting', $monoFontSm, $dimBrush, $phoneX + 34, $phoneY + 610)
$g.DrawString('TAP TO JOIN ^', $monoFontBold, $indigoBrush, $phoneX + $phoneW - 120, $phoneY + 610)

# Row 3: Gmail Google Calendar
$g.DrawRectangle($cPen, $phoneX + 22, $phoneY + 640, $phoneW - 44, 66)
$redBrush = New-Object System.Drawing.SolidBrush $neonRed
$g.DrawString('TOMORROW * 05:15 PM', $monoFontBold, $redBrush, $phoneX + 34, $phoneY + 648)
$g.DrawString('[GMAIL]', $monoFontBold, $redBrush, $phoneX + $phoneW - 85, $phoneY + 648)
$g.DrawString('Flight Check-in & Departure', $monoFontBold, $whiteBrush, $phoneX + 34, $phoneY + 666)
$g.DrawString('Google Calendar * Flights Sync', $monoFontSm, $dimBrush, $phoneX + 34, $phoneY + 686)

# Row 4: Missed Call
$g.DrawRectangle($cPen, $phoneX + 22, $phoneY + 716, $phoneW - 44, 54)
$g.DrawString('MISSED CALL * 14m ago', $monoFontBold, $redBrush, $phoneX + 34, $phoneY + 724)
$g.DrawString('[CALL BACK]', $monoFontBold, $redBrush, $phoneX + $phoneW - 115, $phoneY + 724)
$g.DrawString('Alex Turner', $monoFontBold, $whiteBrush, $phoneX + 34, $phoneY + 744)
$g.DrawString('Tap to call back (07982 123456)', $monoFontSm, $dimBrush, $phoneX + 130, $phoneY + 746)

# Swipe Up hint
$g.DrawString('^  SWIPE UP FOR ALL APPS', $monoFontSm, $dimBrush, $phoneX + 130, $phoneY + 880)


# ----------------- PANEL 2: OVERVIEW & ARCHITECTURAL SUMMARY (Right) -----------------
$setX = 570
$setY = 120
$setW = 970
$setH = 920

$cardBigBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(12, 12, 14))
$cardBigPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(35, 35, 42), 1)
$g.FillRectangle($cardBigBg, $setX, $setY, $setW, $setH)
$g.DrawRectangle($cardBigPen, $setX, $setY, $setW, $setH)

$headFont = New-Object System.Drawing.Font("Segoe UI", 16, [System.Drawing.FontStyle]::Bold)
$g.DrawString('COMPACT UNTRIMMED ICONS & CRYSTAL-CLEAR AGENDA', $headFont, $whiteBrush, $setX + 30, $setY + 25)
$g.DrawString('Reduced icon sizing, contained subtle neon halo, and multi-source calendar metadata resolution.', $monoFontSm, $grayBrush, $setX + 30, $setY + 58)

$boxY = $setY + 95
$innerBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(18, 18, 22))
$innerPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(40, 40, 50), 1)

# Card 1: Compact Untrimmed Icons
$g.FillRectangle($innerBg, $setX + 30, $boxY, $setW - 60, 240)
$g.DrawRectangle($innerPen, $setX + 30, $boxY, $setW - 60, 240)
$g.DrawString('1. COMPACT UNTRIMMED NEON ICONS (44dp)', $monoFontBold, $cyanBrush, $setX + 45, $boxY + 15)
$g.DrawString('  Sleek Proportions: Reduced icon container from 54dp to 44dp with 20dp vector glyphs.', $monoFontSm, $whiteBrush, $setX + 45, $boxY + 45)
$g.DrawString('  Contained Neon Aura: Glow radius reduced to 0.58x (alpha 0.16) so it never clips or trims.', $monoFontSm, $whiteBrush, $setX + 45, $boxY + 75)
$g.DrawString('  Clipped Container Fix: Removed outer rounded corner clipping on parent column.', $monoFontSm, $whiteBrush, $setX + 45, $boxY + 105)
$g.DrawString('  Balanced Grid Spacing: 4-column row comfortably fits on all AMOLED display resolutions.', $monoFontSm, $whiteBrush, $setX + 45, $boxY + 135)
$g.DrawString('  Brand Color Matching: Emerald (Maps), Green (WhatsApp), Pink (Instagram), Cyan (Telegram).', $monoFontSm, $cyanBrush, $setX + 45, $boxY + 165)
$g.DrawString('  Pure Pitch Black (#000000): Inside circle remains 100% black with crisp 1.2dp neon border.', $monoFontSm, $whiteBrush, $setX + 45, $boxY + 195)

# Card 2: Crystal-Clear Agenda Resolution
$box2Y = $boxY + 260
$g.FillRectangle($innerBg, $setX + 30, $box2Y, $setW - 60, 255)
$g.DrawRectangle($innerPen, $setX + 30, $box2Y, $setW - 60, 255)
$g.DrawString('2. CRYSTAL-CLEAR MEETINGS (TEAMS WORK, PERSONAL TEAMS & GMAIL)', $monoFontBold, $cyanBrush, $setX + 45, $box2Y + 15)
$g.DrawString('  Calendar Metadata Resolution: Pre-queries CalendarContract.Calendars for account_name,', $monoFontSm, $whiteBrush, $setX + 45, $box2Y + 45)
$g.DrawString('    account_type and calendar_displayName to accurately detect corporate & Google accounts.', $monoFontSm, $dimBrush, $setX + 45, $box2Y + 65)
$g.DrawString('  Dual Profile & Fallback Engine: Queries CalendarContract.Instances.CONTENT_URI,', $monoFontSm, $whiteBrush, $setX + 45, $box2Y + 95)
$g.DrawString('    ENTERPRISE_CONTENT_URI (API 29+ Work Profile), and CalendarContract.Events fallback.', $monoFontSm, $dimBrush, $setX + 45, $box2Y + 115)
$g.DrawString('  Unclipped Full Title: Displays full meeting name across up to 2 lines without truncation.', $monoFontSm, $whiteBrush, $setX + 45, $box2Y + 145)
$g.DrawString('  High-Clarity Time Formatting: "TODAY * 10:30 AM - 11:30 AM", "TOMORROW * 2:00 PM".', $monoFontSm, $cyanBrush, $setX + 45, $box2Y + 175)
$g.DrawString('  Teams Deep-Link Detection: Extracts meetup-join URLs with 1-tap "TAP TO JOIN ^" action.', $monoFontSm, $whiteBrush, $setX + 45, $box2Y + 205)

# Card 3: Summary
$box3Y = $box2Y + 275
$g.FillRectangle($innerBg, $setX + 30, $box3Y, $setW - 60, 205)
$g.DrawRectangle($innerPen, $setX + 30, $box3Y, $setW - 60, 205)
$g.DrawString('3. FULLSCREEN AMOLED & STATUS CONTROLS', $monoFontBold, $cyanBrush, $setX + 45, $box3Y + 15)
$g.DrawString('  Hidden Status Bar: System notification drop is hidden for an immersive AMOLED canvas.', $monoFontSm, $whiteBrush, $setX + 45, $box3Y + 45)
$g.DrawString('  Live Speedometer: Real-time TrafficStats download speed (e.g. WIFI 3.4 MB/s) in header.', $monoFontSm, $whiteBrush, $setX + 45, $box3Y + 75)
$g.DrawString('  Icon-Only Controls: Sleek circular buttons for layout reordering (^v) and settings (*).', $monoFontSm, $whiteBrush, $setX + 45, $box3Y + 105)
$g.DrawString('  Zero Fake/Mock Data: Empty state provides 1-tap [Open Teams], [Open Gmail] and [Refresh].', $monoFontSm, $cyanBrush, $setX + 45, $box3Y + 135)

# Save
$outputPath = "C:\Users\BalajiL\minimal-s25-launcher\preview.png"
$bmp.Save($outputPath, [System.Drawing.Imaging.ImageFormat]::Png)

$artifactPath = "C:\Users\BalajiL\.gemini\antigravity-cli\brain\63d8052f-617c-48df-9be1-006ea85fada7\s25_launcher_preview.png"
$bmp.Save($artifactPath, [System.Drawing.Imaging.ImageFormat]::Png)

$g.Dispose()
$bmp.Dispose()

Write-Output "Preview generated successfully at $outputPath and $artifactPath"
