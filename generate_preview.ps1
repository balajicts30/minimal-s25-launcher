Add-Type -AssemblyName System.Drawing

$width = 1600
$height = 1100
$bmp = New-Object System.Drawing.Bitmap $width, $height
$g = [System.Drawing.Graphics]::FromImage($bmp)
$g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
$g.TextRenderingHint = [System.Drawing.Text.TextRenderingHint]::AntiAliasGridFit

# Outer Background - Modern dark graphite
$bgBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(10, 10, 12))
$g.FillRectangle($bgBrush, 0, 0, $width, $height)

# Header Title
$titleFont = New-Object System.Drawing.Font("Segoe UI", 20, [System.Drawing.FontStyle]::Bold)
$titleBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(255, 255, 255))
$subFont = New-Object System.Drawing.Font("Consolas", 12, [System.Drawing.FontStyle]::Regular)
$subBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(140, 140, 150))

$g.DrawString('SAMSUNG GALAXY S25 -- MINIMAL LAUNCHER', $titleFont, $titleBrush, 50, 30)
$g.DrawString('Adjustable Layout (Pull Sections Up/Down)  |  Custom Font Styles and Scalable Typography', $subFont, $subBrush, 50, 70)

# ----------------- PANEL 1: S25 HOME SCREEN (Left) -----------------
$phoneX = 60
$phoneY = 120
$phoneW = 460
$phoneH = 920

# Phone Bezel & Display
$bezelBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(20, 20, 24))
$g.FillRectangle($bezelBrush, $phoneX - 8, $phoneY - 8, $phoneW + 16, $phoneH + 16)
$bezelPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(50, 50, 60), 2)
$g.DrawRectangle($bezelPen, $phoneX - 8, $phoneY - 8, $phoneW + 16, $phoneH + 16)

# AMOLED Pitch Black Screen
$screenBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::Black)
$g.FillRectangle($screenBrush, $phoneX, $phoneY, $phoneW, $phoneH)

# Camera hole punch at top center
$punchBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(15, 15, 18))
$g.FillEllipse($punchBrush, $phoneX + ($phoneW / 2) - 8, $phoneY + 12, 16, 16)

# Fonts for Home Screen
$monoFontSm = New-Object System.Drawing.Font("Consolas", 10, [System.Drawing.FontStyle]::Regular)
$monoFontBold = New-Object System.Drawing.Font("Consolas", 11, [System.Drawing.FontStyle]::Bold)
$clockFont = New-Object System.Drawing.Font("Segoe UI", 48, [System.Drawing.FontStyle]::Regular)
$dateFont = New-Object System.Drawing.Font("Segoe UI", 12, [System.Drawing.FontStyle]::Regular)
$sectionFont = New-Object System.Drawing.Font("Consolas", 9, [System.Drawing.FontStyle]::Bold)

# 1. Top Bar: Battery & Action Controls
$cyanBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(56, 189, 248))
$whiteBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::White)
$grayBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(150, 150, 150))
$dimBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(90, 90, 90))

# Battery
$g.DrawString('98% [PWR]', $monoFontBold, $whiteBrush, $phoneX + 24, $phoneY + 45)

# Buttons: Arrange & Customize
$btnPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(56, 189, 248), 1)
$btnBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(20, 35, 55))
$g.FillRectangle($btnBg, $phoneX + 250, $phoneY + 42, 95, 24)
$g.DrawRectangle($btnPen, $phoneX + 250, $phoneY + 42, 95, 24)
$g.DrawString('Arrange ^v', $monoFontBold, $cyanBrush, $phoneX + 256, $phoneY + 46)

$btnPen2 = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(50, 50, 50), 1)
$btnBg2 = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(18, 18, 18))
$g.FillRectangle($btnBg2, $phoneX + 355, $phoneY + 42, 85, 24)
$g.DrawRectangle($btnPen2, $phoneX + 355, $phoneY + 42, 85, 24)
$g.DrawString('Customize *', $monoFontSm, $grayBrush, $phoneX + 360, $phoneY + 46)

# Section Reorder Handle (Arrange Mode Active indicator)
$barBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(16, 28, 45))
$barPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(37, 99, 235), 1)
$g.FillRectangle($barBg, $phoneX + 24, $phoneY + 80, $phoneW - 48, 26)
$g.DrawRectangle($barPen, $phoneX + 24, $phoneY + 80, $phoneW - 48, 26)
$g.DrawString('1. CLOCK & STATS', $monoFontBold, $cyanBrush, $phoneX + 34, $phoneY + 86)
$g.DrawString('[PULL DOWN v]', $monoFontBold, $whiteBrush, $phoneX + $phoneW - 150, $phoneY + 86)

# Clock & Date
$g.DrawString('08:45', $clockFont, $whiteBrush, $phoneX + 20, $phoneY + 112)
$g.DrawString('Friday, October 23, 2026', $dateFont, $grayBrush, $phoneX + 24, $phoneY + 188)

# Screen time pill
$pillBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(15, 15, 15))
$pillPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(40, 40, 40), 1)
$g.FillRectangle($pillBg, $phoneX + 24, $phoneY + 215, 160, 24)
$g.DrawRectangle($pillPen, $phoneX + 24, $phoneY + 215, 160, 24)
$g.DrawString('[DETOX] 1h 48m today', $monoFontSm, $grayBrush, $phoneX + 32, $phoneY + 219)

# 2. Section 2: FAVORITES (App Icons)
$g.FillRectangle($barBg, $phoneX + 24, $phoneY + 260, $phoneW - 48, 26)
$g.DrawRectangle($barPen, $phoneX + 24, $phoneY + 260, $phoneW - 48, 26)
$g.DrawString('2. FAVORITES APPS', $monoFontBold, $cyanBrush, $phoneX + 34, $phoneY + 266)
$g.DrawString('[^ UP] [v DOWN]', $monoFontBold, $whiteBrush, $phoneX + $phoneW - 150, $phoneY + 266)

$g.DrawString('FAVORITES', $sectionFont, $dimBrush, $phoneX + 24, $phoneY + 295)

$favApps = @(
    @{ Name = 'Maps'; Icon = 'MAP'; X = $phoneX + 45 },
    @{ Name = 'WhatsApp'; Icon = 'WA'; X = $phoneX + 145 },
    @{ Name = 'Instagram'; Icon = 'IG'; X = $phoneX + 245 },
    @{ Name = 'Telegram'; Icon = 'TG'; X = $phoneX + 345 }
)
$appCircleBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(15, 15, 15))
$appCirclePen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(42, 42, 42), 1)

foreach ($app in $favApps) {
    $g.FillEllipse($appCircleBg, $app.X, $phoneY + 315, 48, 48)
    $g.DrawEllipse($appCirclePen, $app.X, $phoneY + 315, 48, 48)
    $g.DrawString($app.Icon, $monoFontBold, $whiteBrush, $app.X + 11, $phoneY + 328)
    $g.DrawString($app.Name, $monoFontSm, $grayBrush, $app.X - 2, $phoneY + 370)
}

# 3. Section 3: ROLLING NOTIFICATIONS
$g.FillRectangle($barBg, $phoneX + 24, $phoneY + 405, $phoneW - 48, 26)
$g.DrawRectangle($barPen, $phoneX + 24, $phoneY + 405, $phoneW - 48, 26)
$g.DrawString('3. NOTIFICATIONS', $monoFontBold, $cyanBrush, $phoneX + 34, $phoneY + 411)
$g.DrawString('[^ UP] [v DOWN]', $monoFontBold, $whiteBrush, $phoneX + $phoneW - 150, $phoneY + 411)

$g.DrawString('NOTIFICATIONS', $sectionFont, $dimBrush, $phoneX + 24, $phoneY + 440)

# Ticker cards
$cardBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(14, 14, 14))
$cardPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(35, 35, 35), 1)

$g.FillRectangle($cardBg, $phoneX + 24, $phoneY + 460, 200, 70)
$g.DrawRectangle($cardPen, $phoneX + 24, $phoneY + 460, 200, 70)
$g.DrawString('WhatsApp | 2m ago', $monoFontSm, $cyanBrush, $phoneX + 34, $phoneY + 468)
$g.DrawString('David: Meeting 4 PM?', $monoFontBold, $whiteBrush, $phoneX + 34, $phoneY + 488)
$g.DrawString('Sync on launcher design', $monoFontSm, $grayBrush, $phoneX + 34, $phoneY + 508)

$g.FillRectangle($cardBg, $phoneX + 234, $phoneY + 460, 200, 70)
$g.DrawRectangle($cardPen, $phoneX + 234, $phoneY + 460, 200, 70)
$g.DrawString('Telegram | 8m ago', $monoFontSm, $cyanBrush, $phoneX + 244, $phoneY + 468)
$g.DrawString('Eng Group: Build Ready', $monoFontBold, $whiteBrush, $phoneX + 244, $phoneY + 488)
$g.DrawString('v2.1 release tagged', $monoFontSm, $grayBrush, $phoneX + 244, $phoneY + 508)

# 4. Section 4: AGENDA & MISSED CALLS
$g.FillRectangle($barBg, $phoneX + 24, $phoneY + 550, $phoneW - 48, 26)
$g.DrawRectangle($barPen, $phoneX + 24, $phoneY + 550, $phoneW - 48, 26)
$g.DrawString('4. AGENDA & CALLS', $monoFontBold, $cyanBrush, $phoneX + 34, $phoneY + 556)
$g.DrawString('[^ UP] [BOTTOM]', $monoFontBold, $whiteBrush, $phoneX + $phoneW - 150, $phoneY + 556)

$g.DrawString('AGENDA & MISSED CALLS', $sectionFont, $dimBrush, $phoneX + 24, $phoneY + 585)

# Row 1: Teams
$g.FillRectangle($cardBg, $phoneX + 24, $phoneY + 605, $phoneW - 48, 48)
$g.DrawRectangle($cardPen, $phoneX + 24, $phoneY + 605, $phoneW - 48, 48)
$g.DrawString('10:30 AM', $monoFontBold, $grayBrush, $phoneX + 34, $phoneY + 620)
$g.DrawString('Weekly Core Team Sync', $monoFontBold, $whiteBrush, $phoneX + 115, $phoneY + 612)
$g.DrawString('Microsoft Teams Work Call', $monoFontSm, $dimBrush, $phoneX + 115, $phoneY + 630)
$g.DrawString('[WORK]', $monoFontBold, $cyanBrush, $phoneX + $phoneW - 90, $phoneY + 620)

# Row 2: Gmail Personal
$g.FillRectangle($cardBg, $phoneX + 24, $phoneY + 662, $phoneW - 48, 48)
$g.DrawRectangle($cardPen, $phoneX + 24, $phoneY + 662, $phoneW - 48, 48)
$g.DrawString('04:30 PM', $monoFontBold, $grayBrush, $phoneX + 34, $phoneY + 677)
$g.DrawString('Doctor Appointment', $monoFontBold, $whiteBrush, $phoneX + 115, $phoneY + 669)
$g.DrawString('Personal Calendar / Gmail', $monoFontSm, $dimBrush, $phoneX + 115, $phoneY + 687)
$yellowBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(251, 191, 36))
$g.DrawString('[PERSONAL]', $monoFontBold, $yellowBrush, $phoneX + $phoneW - 110, $phoneY + 677)

# Row 3: Missed Call
$g.FillRectangle($cardBg, $phoneX + 24, $phoneY + 719, $phoneW - 48, 48)
$g.DrawRectangle($cardPen, $phoneX + 24, $phoneY + 719, $phoneW - 48, 48)
$redBrush = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(239, 68, 68))
$g.DrawString('CALL MISSED', $monoFontBold, $redBrush, $phoneX + 30, $phoneY + 734)
$g.DrawString('Alex Turner', $monoFontBold, $whiteBrush, $phoneX + 125, $phoneY + 726)
$g.DrawString('Missed call | 14m ago', $monoFontSm, $dimBrush, $phoneX + 125, $phoneY + 744)
$g.DrawString('[CALL BACK]', $monoFontBold, $redBrush, $phoneX + $phoneW - 115, $phoneY + 734)

# Swipe Up hint at bottom
$g.DrawString('^  SWIPE UP FOR ALL APPS', $monoFontSm, $dimBrush, $phoneX + 130, $phoneY + 870)


# ----------------- PANEL 2: SETTINGS & ADJUSTABLE CONTROLS (Right) -----------------
$setX = 570
$setY = 120
$setW = 970
$setH = 920

$cardBigBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(15, 15, 18))
$cardBigPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(38, 38, 45), 1)
$g.FillRectangle($cardBigBg, $setX, $setY, $setW, $setH)
$g.DrawRectangle($cardBigPen, $setX, $setY, $setW, $setH)

$headFont = New-Object System.Drawing.Font("Segoe UI", 16, [System.Drawing.FontStyle]::Bold)
$g.DrawString('INTERACTIVE SETTINGS & CUSTOMIZATION OVERVIEW', $headFont, $whiteBrush, $setX + 30, $setY + 25)
$g.DrawString('Customize section order via pull up/down and fine-tune typography in real-time.', $monoFontSm, $grayBrush, $setX + 30, $setY + 58)

# 1. LAYOUT MANAGER CARD
$boxY = $setY + 95
$boxH = 265
$innerBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(22, 22, 26))
$innerPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(45, 45, 55), 1)

$g.FillRectangle($innerBg, $setX + 30, $boxY, $setW - 60, $boxH)
$g.DrawRectangle($innerPen, $setX + 30, $boxY, $setW - 60, $boxH)

$g.DrawString('1. SECTION ORDER & ADJUSTABLE LAYOUT', $monoFontBold, $cyanBrush, $setX + 45, $boxY + 15)
$g.DrawString('Pull sections up or down. Changes update instantly on the home screen:', $monoFontSm, $grayBrush, $setX + 45, $boxY + 35)

$sectionsList = @(
    @{ Num = "1"; Name = "Clock & Screen Time Header"; Sub = "Large typography, battery % and detox badge"; CanUp = $false; CanDown = $true },
    @{ Num = "2"; Name = "Favorites App Icons Grid"; Sub = "Selected minimal icons (Maps, WhatsApp, Instagram, Telegram...)"; CanUp = $true; CanDown = $true },
    @{ Num = "3"; Name = "Rolling Notifications Ticker"; Sub = "Real-time scrollable incoming notifications cards"; CanUp = $true; CanDown = $true },
    @{ Num = "4"; Name = "Meetings & Missed Calls Agenda"; Sub = "Teams work meetings, personal/Gmail events & missed calls"; CanUp = $true; CanDown = $false }
)

$rowY = $boxY + 60
foreach ($sec in $sectionsList) {
    $secRowBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(28, 28, 34))
    $g.FillRectangle($secRowBg, $setX + 45, $rowY, $setW - 90, 42)
    
    # Badge
    $badgeBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(56, 189, 248))
    $badgeText = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::Black)
    $g.FillEllipse($badgeBg, $setX + 55, $rowY + 11, 20, 20)
    $g.DrawString($sec.Num, $monoFontBold, $badgeText, $setX + 61, $rowY + 13)

    $g.DrawString($sec.Name, $monoFontBold, $whiteBrush, $setX + 85, $rowY + 6)
    $g.DrawString($sec.Sub, $monoFontSm, $dimBrush, $setX + 85, $rowY + 23)

    # Action Pills
    $pillUColor = if ($sec.CanUp) { [System.Drawing.Color]::FromArgb(56, 189, 248) } else { [System.Drawing.Color]::FromArgb(70, 70, 80) }
    $pillDColor = if ($sec.CanDown) { [System.Drawing.Color]::FromArgb(56, 189, 248) } else { [System.Drawing.Color]::FromArgb(70, 70, 80) }
    $pBrushU = New-Object System.Drawing.SolidBrush $pillUColor
    $pBrushD = New-Object System.Drawing.SolidBrush $pillDColor

    $g.DrawString('[^ PULL UP]', $monoFontBold, $pBrushU, $setX + $setW - 240, $rowY + 13)
    $g.DrawString('[v PULL DOWN]', $monoFontBold, $pBrushD, $setX + $setW - 140, $rowY + 13)

    $rowY += 48
}

# 2. TYPOGRAPHY CARD (FONT FAMILY & FONT SIZE)
$box2Y = $boxY + $boxH + 20
$box2H = 265
$g.FillRectangle($innerBg, $setX + 30, $box2Y, $setW - 60, $box2H)
$g.DrawRectangle($innerPen, $setX + 30, $box2Y, $setW - 60, $box2H)

$g.DrawString('2. TYPOGRAPHY: FONT STYLES & SCALABLE SIZES', $monoFontBold, $cyanBrush, $setX + 45, $box2Y + 15)
$g.DrawString('Select font families and font scale factors across the entire launcher:', $monoFontSm, $grayBrush, $setX + 45, $box2Y + 35)

# Font Family Pills
$g.DrawString('Font Family Style:', $monoFontBold, $whiteBrush, $setX + 45, $box2Y + 65)

$fontFamilies = @(
    @{ Name = "Monospace"; Sample = "Consolas"; Active = $true },
    @{ Name = "Sans-Serif"; Sample = "Segoe UI"; Active = $false },
    @{ Name = "Serif"; Sample = "Georgia"; Active = $false },
    @{ Name = "Cursive"; Sample = "Segoe Script"; Active = $false }
)

$fX = $setX + 45
foreach ($fam in $fontFamilies) {
    $fBgColor = if ($fam.Active) { [System.Drawing.Color]::FromArgb(25, 45, 75) } else { [System.Drawing.Color]::FromArgb(30, 30, 36) }
    $fBorderColor = if ($fam.Active) { [System.Drawing.Color]::FromArgb(56, 189, 248) } else { [System.Drawing.Color]::FromArgb(50, 50, 60) }
    $fTextColor = if ($fam.Active) { [System.Drawing.Color]::FromArgb(56, 189, 248) } else { [System.Drawing.Color]::FromArgb(180, 180, 190) }

    $fBg = New-Object System.Drawing.SolidBrush $fBgColor
    $fPen = New-Object System.Drawing.Pen $fBorderColor, 1
    $fText = New-Object System.Drawing.SolidBrush $fTextColor

    $g.FillRectangle($fBg, $fX, $box2Y + 90, 205, 36)
    $g.DrawRectangle($fPen, $fX, $box2Y + 90, 205, 36)
    
    $fFamFont = New-Object System.Drawing.Font($fam.Sample, 11, [System.Drawing.FontStyle]::Bold)
    $g.DrawString($fam.Name, $fFamFont, $fText, $fX + 15, $box2Y + 98)

    $fX += 225
}

# Font Size Scale Pills
$g.DrawString('Font Size Scale:', $monoFontBold, $whiteBrush, $setX + 45, $box2Y + 140)

$fontSizes = @(
    @{ Name = "Compact"; Scale = "85%"; Active = $false },
    @{ Name = "Standard"; Scale = "100%"; Active = $true },
    @{ Name = "Large"; Scale = "115%"; Active = $false },
    @{ Name = "Extra Large"; Scale = "130%"; Active = $false }
)

$sX = $setX + 45
foreach ($sz in $fontSizes) {
    $sBgColor = if ($sz.Active) { [System.Drawing.Color]::FromArgb(25, 45, 75) } else { [System.Drawing.Color]::FromArgb(30, 30, 36) }
    $sBorderColor = if ($sz.Active) { [System.Drawing.Color]::FromArgb(56, 189, 248) } else { [System.Drawing.Color]::FromArgb(50, 50, 60) }
    $sTextColor = if ($sz.Active) { [System.Drawing.Color]::FromArgb(56, 189, 248) } else { [System.Drawing.Color]::FromArgb(180, 180, 190) }

    $sBg = New-Object System.Drawing.SolidBrush $sBgColor
    $sPen = New-Object System.Drawing.Pen $sBorderColor, 1
    $sText = New-Object System.Drawing.SolidBrush $sTextColor

    $g.FillRectangle($sBg, $sX, $box2Y + 165, 205, 36)
    $g.DrawRectangle($sPen, $sX, $box2Y + 165, 205, 36)
    
    $label = "$($sz.Name) ($($sz.Scale))"
    $g.DrawString($label, $monoFontBold, $sText, $sX + 15, $box2Y + 174)

    $sX += 225
}

# Live Preview Snippet inside Typography Card
$prevBg = New-Object System.Drawing.SolidBrush ([System.Drawing.Color]::FromArgb(10, 10, 12))
$prevPen = New-Object System.Drawing.Pen ([System.Drawing.Color]::FromArgb(35, 35, 40), 1)
$g.FillRectangle($prevBg, $setX + 45, $box2Y + 212, $setW - 90, 40)
$g.DrawRectangle($prevPen, $setX + 45, $box2Y + 212, $setW - 90, 40)
$g.DrawString('LIVE PREVIEW: [Monospace | 100%]  08:45 AM  |  Minimal Launcher  |  Focus on what matters', $monoFontBold, $cyanBrush, $setX + 60, $box2Y + 224)

# 3. FEATURE SUMMARY FOOTER
$box3Y = $box2Y + $box2H + 20
$g.FillRectangle($innerBg, $setX + 30, $box3Y, $setW - 60, 205)
$g.DrawRectangle($innerPen, $setX + 30, $box3Y, $setW - 60, 205)

$g.DrawString('3. FAVORITES APPS & SECTION TOGGLES', $monoFontBold, $cyanBrush, $setX + 45, $box3Y + 15)
$g.DrawString('  Home Screen Icons: Maps, WhatsApp, Instagram, Telegram, Phone, Messages, Camera, Browser, Teams, Gmail', $monoFontSm, $whiteBrush, $setX + 45, $box3Y + 45)
$g.DrawString('  Rolling Notifications Ticker: Live NotificationListenerService capture with tap-to-open intent routing', $monoFontSm, $whiteBrush, $setX + 45, $box3Y + 75)
$g.DrawString('  Unified Agenda List: Teams Work (cyan tag), Personal/Gmail (amber tag), Missed Calls (red tag)', $monoFontSm, $whiteBrush, $setX + 45, $box3Y + 105)
$g.DrawString('  Persistent Storage: All layout orders, typography styles and toggles persist across device restarts', $monoFontSm, $whiteBrush, $setX + 45, $box3Y + 135)
$g.DrawString('  Pure AMOLED Pitch Black (#000000): Complete pixel shutdown on Samsung Galaxy S25 OLED display', $monoFontSm, $cyanBrush, $setX + 45, $box3Y + 165)

# Save Bitmap
$outputPath = "C:\Users\BalajiL\minimal-s25-launcher\preview.png"
$bmp.Save($outputPath, [System.Drawing.Imaging.ImageFormat]::Png)

$artifactPath = "C:\Users\BalajiL\.gemini\antigravity-cli\brain\63d8052f-617c-48df-9be1-006ea85fada7\s25_launcher_preview.png"
$bmp.Save($artifactPath, [System.Drawing.Imaging.ImageFormat]::Png)

$g.Dispose()
$bmp.Dispose()

Write-Output "Preview generated successfully at $outputPath and $artifactPath"
