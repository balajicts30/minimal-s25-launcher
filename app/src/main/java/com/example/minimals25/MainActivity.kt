package com.example.minimals25

import android.Manifest
import android.app.AppOpsManager
import android.app.NotificationManager
import android.app.admin.DevicePolicyManager
import android.app.usage.UsageStatsManager
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.AudioManager
import android.net.TrafficStats
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.CallLog
import android.provider.CalendarContract
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.FormatSize
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SwapVert
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// ---------------- Typography Models ----------------

enum class LauncherFontFamily(val id: String, val label: String, val composeFamily: FontFamily) {
    MONOSPACE("monospace", "Monospace", FontFamily.Monospace),
    SANS_SERIF("sans_serif", "Sans-Serif", FontFamily.SansSerif),
    SERIF("serif", "Serif", FontFamily.Serif),
    CURSIVE("cursive", "Cursive", FontFamily.Cursive);

    companion object {
        fun fromId(id: String): LauncherFontFamily =
            entries.find { it.id == id } ?: MONOSPACE
    }
}

enum class LauncherFontSize(val id: String, val label: String, val scale: Float) {
    COMPACT("compact", "Compact (85%)", 0.85f),
    STANDARD("standard", "Standard (100%)", 1.0f),
    LARGE("large", "Large (115%)", 1.15f),
    EXTRA_LARGE("extra_large", "Extra Large (130%)", 1.30f);

    companion object {
        fun fromId(id: String): LauncherFontSize =
            entries.find { it.id == id } ?: STANDARD
    }
}

// ---------------- Layout & Section Models ----------------

enum class HomeSection(val id: String, val title: String, val shortTitle: String) {
    HEADER("header", "Clock & Screen Time Header", "CLOCK & STATS"),
    APPS("apps", "Favorites App Icons Grid", "FAVORITES APPS"),
    QUICK_TOGGLES("quick_toggles", "Quick System Toggles Bar", "QUICK TOGGLES"),
    NOTIFICATIONS("notifications", "Rolling Notifications Ticker", "NOTIFICATIONS"),
    AGENDA("agenda", "Meetings & Missed Calls List", "AGENDA & CALLS");

    companion object {
        fun fromId(id: String): HomeSection? = entries.find { it.id == id }

        fun defaultOrder(): List<HomeSection> =
            listOf(HEADER, APPS, QUICK_TOGGLES, NOTIFICATIONS, AGENDA)

        fun serialize(list: List<HomeSection>): String = list.joinToString(",") { it.id }

        fun deserialize(serialized: String?): List<HomeSection> {
            if (serialized.isNullOrBlank()) return defaultOrder()
            val parsed = serialized.split(",").mapNotNull { fromId(it.trim()) }
            val missing = defaultOrder().filter { it !in parsed }
            return (parsed + missing).distinct()
        }
    }
}

// ---------------- Data Models ----------------

enum class AgendaType {
    TEAMS_WORK,       // Microsoft Teams on Work Profile (Enterprise)
    TEAMS_PERSONAL,   // Microsoft Teams on Personal Profile
    GMAIL_PERSONAL,   // Gmail / Google Calendar
    MISSED_CALL       // Missed Calls
}

data class AgendaItem(
    val id: String,
    val timeLabel: String,
    val title: String,
    val subtitle: String,
    val type: AgendaType,
    val targetIntent: Intent? = null
)

data class AppChoice(
    val id: String,
    val label: String,
    val icon: ImageVector? = null,
    val candidatePackages: List<String>,
    val neonColor: Color,
    val fallbackIntentAction: String? = null
)

data class AppEntry(
    val label: String,
    val packageName: String,
    val activityName: String
)

val NEON_PALETTE = listOf(
    Color(0xFF00E5FF), // Cyan
    Color(0xFF39FF14), // Neon Green
    Color(0xFFFF1493), // Neon Pink
    Color(0xFFFFD600), // Electric Yellow
    Color(0xFF818CF8), // Indigo / Violet
    Color(0xFFFF5252), // Electric Red
    Color(0xFFFF9100), // Neon Orange
    Color(0xFF00E676), // Spring Green
    Color(0xFFE040FB), // Electric Purple
    Color(0xFF1DE9B6)  // Teal
)

fun getNeonColorForPackage(packageName: String): Color {
    val hash = (packageName.hashCode() and 0x7FFFFFFF)
    return NEON_PALETTE[hash % NEON_PALETTE.size]
}

val AVAILABLE_HOME_APPS = listOf(
    AppChoice("whatsapp", "WhatsApp", Icons.Rounded.ChatBubbleOutline, listOf("com.whatsapp", "com.whatsapp.w4b"), Color(0xFF25D366)),
    AppChoice("instagram", "Instagram", Icons.Rounded.PhotoCamera, listOf("com.instagram.android"), Color(0xFFFF1493)),
    AppChoice("telegram", "Telegram", Icons.Rounded.Send, listOf("org.telegram.messenger", "org.telegram.messenger.web"), Color(0xFF00E5FF)),
    AppChoice("maps", "Maps", Icons.Rounded.Map, listOf("com.google.android.apps.maps"), Color(0xFF10B981)),
    AppChoice("phone", "Phone", Icons.Rounded.Phone, listOf("com.samsung.android.dialer", "com.google.android.dialer"), Color(0xFF39FF14), Intent.ACTION_DIAL),
    AppChoice("messages", "Messages", Icons.Rounded.Email, listOf("com.samsung.android.messaging", "com.google.android.apps.messaging"), Color(0xFF3B82F6)),
    AppChoice("camera", "Camera", Icons.Rounded.CameraAlt, listOf("com.sec.android.app.camera", "com.google.android.GoogleCamera"), Color(0xFFFF5252), MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA),
    AppChoice("chrome", "Browser", Icons.Rounded.Language, listOf("com.android.chrome", "com.sec.android.app.sbrowser"), Color(0xFFFFD600)),
    AppChoice("teams", "Teams", Icons.Rounded.Groups, listOf("com.microsoft.teams"), Color(0xFF818CF8)),
    AppChoice("gmail", "Gmail", Icons.Rounded.Email, listOf("com.google.android.gm"), Color(0xFFFF3366)),
    AppChoice("photos", "Photos", Icons.Rounded.PhotoLibrary, listOf("com.sec.android.gallery3d", "com.google.android.apps.photos"), Color(0xFFFF9100))
)

val DEFAULT_SELECTED_APP_IDS = setOf("maps", "whatsapp", "instagram", "telegram", "phone", "messages")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make Home Screen Full Screen & Hide Notification Drop (Status Bar)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController.hide(WindowInsetsCompat.Type.statusBars())

        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    background = Color.Black,
                    surface = Color.Black,
                    onBackground = Color.White,
                    onSurface = Color.White
                )
            ) {
                MinimalS25LauncherScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Re-enforce full screen immersive mode
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.statusBars())
    }
}

// ---------------- GEMINI 4-POINT SPARKLE VECTOR ICON ----------------

@Composable
fun GeminiSparkleIcon(modifier: Modifier = Modifier, color: Color = Color(0xFF00E5FF)) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val path = Path().apply {
            moveTo(cx, 0f)
            quadraticBezierTo(cx, cy, w, cy)
            quadraticBezierTo(cx, cy, cx, h)
            quadraticBezierTo(cx, cy, 0f, cy)
            quadraticBezierTo(cx, cy, cx, 0f)
            close()
        }
        drawPath(path, color = color)
    }
}

@Composable
fun MinimalS25LauncherScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("s25_launcher_prefs", Context.MODE_PRIVATE) }

    var isDrawerOpen by remember { mutableStateOf(false) }
    var isSettingsOpen by remember { mutableStateOf(false) }
    var isArrangeMode by remember { mutableStateOf(false) }
    var installedApps by remember { mutableStateOf<List<AppEntry>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    // Section Order (Adjustable Layout - Pull Sections Up/Down)
    var sectionOrder by remember {
        mutableStateOf(HomeSection.deserialize(prefs.getString("section_order", null)))
    }

    // Typography: Font Family & Font Size
    var selectedFontFamily by remember {
        mutableStateOf(LauncherFontFamily.fromId(prefs.getString("font_family", "monospace") ?: "monospace"))
    }
    var selectedFontSize by remember {
        mutableStateOf(LauncherFontSize.fromId(prefs.getString("font_size", "standard") ?: "standard"))
    }

    // Dimension Adjusters: Icon Size, Section Padding X, Section Spacing Y
    var iconSizeDp by remember {
        mutableIntStateOf(prefs.getInt("icon_size_dp", 44))
    }
    var layoutPaddingXDp by remember {
        mutableIntStateOf(prefs.getInt("layout_padding_x", 18))
    }
    var sectionSpacingYDp by remember {
        mutableIntStateOf(prefs.getInt("section_spacing_y", 20))
    }

    val activeFont = selectedFontFamily.composeFamily
    val fontScale = selectedFontSize.scale

    // User Configuration from Settings: Selected App IDs (Supports Curated + Any Installed App)
    var selectedAppIds by remember {
        mutableStateOf(prefs.getStringSet("home_apps", DEFAULT_SELECTED_APP_IDS) ?: DEFAULT_SELECTED_APP_IDS)
    }
    var showQuickTogglesSection by remember {
        mutableStateOf(prefs.getBoolean("show_quick_toggles", true))
    }
    var showNotificationsSection by remember {
        mutableStateOf(prefs.getBoolean("show_notifs", true))
    }
    var showAgendaSection by remember {
        mutableStateOf(prefs.getBoolean("show_agenda", true))
    }

    // Live Clock & Date
    var timeString by remember { mutableStateOf("") }
    var dateString by remember { mutableStateOf("") }

    // Battery & Charging
    var batteryPct by remember { mutableIntStateOf(100) }
    var isCharging by remember { mutableStateOf(false) }

    // Live Internet Speed
    var netSpeedText by remember { mutableStateOf("↓ 0 KB/s") }

    // Screen Time Today
    var screenTimeText by remember { mutableStateOf("Calculating...") }
    var hasUsagePermission by remember { mutableStateOf(checkUsageStatsPermission(context)) }

    // Quick System Toggles State
    val wifiManager = remember { context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager }
    val audioManager = remember { context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager }
    val notifManager = remember { context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager }
    val btAdapter = remember { BluetoothAdapter.getDefaultAdapter() }

    var isWifiEnabled by remember { mutableStateOf(wifiManager?.isWifiEnabled ?: false) }
    var currentRingerMode by remember { mutableIntStateOf(audioManager?.ringerMode ?: AudioManager.RINGER_MODE_NORMAL) }
    var isBluetoothEnabled by remember { mutableStateOf(btAdapter?.isEnabled ?: false) }

    // Rolling notifications flow from S25NotificationService
    val incomingNotifications by S25NotificationService.notificationsFlow.collectAsState()
    var isNotificationAccessGranted by remember {
        mutableStateOf(S25NotificationService.isNotificationAccessGranted(context))
    }

    // Meetings & Missed Calls List
    var agendaList by remember { mutableStateOf<List<AgendaItem>>(emptyList()) }

    // Permission Launchers
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        agendaList = loadAgendaAndMissedCalls(context)
    }

    // Drag tracking for directional swipes
    var totalDragX by remember { mutableFloatStateOf(0f) }
    var totalDragY by remember { mutableFloatStateOf(0f) }

    // Section movement helper (Pull Up or Down)
    fun moveSection(section: HomeSection, direction: Int) {
        val idx = sectionOrder.indexOf(section)
        if (idx == -1) return
        val targetIdx = idx + direction
        if (targetIdx !in sectionOrder.indices) return
        val updated = sectionOrder.toMutableList()
        val item = updated.removeAt(idx)
        updated.add(targetIdx, item)
        sectionOrder = updated
        prefs.edit().putString("section_order", HomeSection.serialize(updated)).apply()
    }

    // Load initial data
    LaunchedEffect(Unit) {
        installedApps = loadInstalledApps(context)
        agendaList = loadAgendaAndMissedCalls(context)
    }

    // Live Network Speed Ticker via TrafficStats
    LaunchedEffect(Unit) {
        var lastRx = TrafficStats.getTotalRxBytes()
        var lastTime = System.currentTimeMillis()
        while (true) {
            delay(1500L)
            val nowRx = TrafficStats.getTotalRxBytes()
            val nowTime = System.currentTimeMillis()
            val timeDiff = (nowTime - lastTime) / 1000f
            if (timeDiff > 0 && lastRx != TrafficStats.UNSUPPORTED.toLong() && nowRx != TrafficStats.UNSUPPORTED.toLong()) {
                val speedBytesPerSec = ((nowRx - lastRx) / timeDiff).toLong()
                val b = if (speedBytesPerSec < 0) 0L else speedBytesPerSec
                netSpeedText = when {
                    b >= 1024 * 1024 -> String.format(Locale.US, "↓ %.1f MB/s", b / (1024f * 1024f))
                    b >= 1024 -> "↓ ${b / 1024} KB/s"
                    else -> "↓ ${b} B/s"
                }
            }
            lastRx = nowRx
            lastTime = nowTime
        }
    }

    // Refresh notification access status and toggles
    LaunchedEffect(isSettingsOpen) {
        isNotificationAccessGranted = S25NotificationService.isNotificationAccessGranted(context)
        agendaList = loadAgendaAndMissedCalls(context)
        isWifiEnabled = wifiManager?.isWifiEnabled ?: false
        currentRingerMode = audioManager?.ringerMode ?: AudioManager.RINGER_MODE_NORMAL
        isBluetoothEnabled = btAdapter?.isEnabled ?: false
    }

    // Clock ticker
    LaunchedEffect(Unit) {
        val timeFmt = SimpleDateFormat("h:mm", Locale.getDefault())
        val dateFmt = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        while (true) {
            val now = Date()
            timeString = timeFmt.format(now)
            dateString = dateFmt.format(now)
            delay(1_000L)
        }
    }

    // Battery Broadcast Receiver
    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                if (intent != null && intent.action == Intent.ACTION_BATTERY_CHANGED) {
                    val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    if (level >= 0 && scale > 0) {
                        batteryPct = ((level / scale.toFloat()) * 100).toInt()
                    }
                    val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                    isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                            status == BatteryManager.BATTERY_STATUS_FULL
                }
            }
        }
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(receiver, filter)
        onDispose { context.unregisterReceiver(receiver) }
    }

    // Screen Time calculation
    LaunchedEffect(hasUsagePermission) {
        if (hasUsagePermission) {
            screenTimeText = computeTodayScreenTime(context)
            while (true) {
                delay(60_000L)
                screenTimeText = computeTodayScreenTime(context)
            }
        } else {
            screenTimeText = "Tap to enable screen time"
        }
    }

    BackHandler(enabled = isDrawerOpen || isSettingsOpen || isArrangeMode) {
        if (isDrawerOpen) {
            isDrawerOpen = false
            searchQuery = ""
        } else if (isSettingsOpen) {
            isSettingsOpen = false
        } else if (isArrangeMode) {
            isArrangeMode = false
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        color = Color.Black
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                // 1. Double-Tap on Black Area to Lock Screen
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            lockDevice(context)
                        }
                    )
                }
                // 2. Directional Drag Gestures: Swipe UP for Apps, Swipe DOWN for Notification Shade
                .pointerInput(isDrawerOpen || isSettingsOpen || isArrangeMode) {
                    if (!isDrawerOpen && !isSettingsOpen && !isArrangeMode) {
                        detectDragGestures(
                            onDragStart = {
                                totalDragX = 0f
                                totalDragY = 0f
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                totalDragX += dragAmount.x
                                totalDragY += dragAmount.y
                            },
                            onDragEnd = {
                                if (totalDragY < -90f && kotlin.math.abs(totalDragY) > kotlin.math.abs(totalDragX)) {
                                    // Swipe UP -> All Apps Drawer
                                    isDrawerOpen = true
                                } else if (totalDragY > 90f && kotlin.math.abs(totalDragY) > kotlin.math.abs(totalDragX)) {
                                    // Swipe DOWN -> System Notification Shade
                                    expandNotificationShade(context)
                                } else if (totalDragX > 110f && kotlin.math.abs(totalDragX) > kotlin.math.abs(totalDragY)) {
                                    // Swipe RIGHT -> Phone Dialer
                                    launchDialer(context)
                                } else if (totalDragX < -110f && kotlin.math.abs(totalDragX) > kotlin.math.abs(totalDragY)) {
                                    // Swipe LEFT -> Camera
                                    launchCameraApp(context)
                                }
                            }
                        )
                    }
                }
        ) {
            // Main True-AMOLED Home Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        top = 22.dp,
                        start = layoutPaddingXDp.dp,
                        end = layoutPaddingXDp.dp,
                        bottom = 16.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(sectionSpacingYDp.dp)
            ) {
                // Persistent Top Status Bar: Battery + Live Internet Speed + Icon-Only Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Battery & Internet Speed
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Battery
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.BatteryChargingFull,
                                contentDescription = "Battery",
                                tint = if (isCharging) Color(0xFF39FF14) else Color(0xFF9E9E9E),
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "$batteryPct%${if (isCharging) " ⚡" else ""}",
                                color = Color(0xFFCCCCCC),
                                fontSize = (12 * fontScale).sp,
                                fontFamily = activeFont,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Live Internet Speed
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Wifi,
                                contentDescription = "Internet Speed",
                                tint = Color(0xFF00E5FF),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = netSpeedText,
                                color = Color(0xFF00E5FF),
                                fontSize = (11 * fontScale).sp,
                                fontFamily = activeFont,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Right: Icon-only buttons for Arrange and Settings (NO TEXT)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Arrange Icon Button
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isArrangeMode) Color(0xFF0F1E33) else Color.Transparent)
                                .border(
                                    1.dp,
                                    if (isArrangeMode) Color(0xFF00E5FF) else Color(0xFF262626),
                                    CircleShape
                                )
                                .clickable { isArrangeMode = !isArrangeMode },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.SwapVert,
                                contentDescription = "Arrange Layout",
                                tint = if (isArrangeMode) Color(0xFF00E5FF) else Color(0xFF888888),
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        // Settings Icon Button
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Transparent)
                                .border(1.dp, Color(0xFF262626), CircleShape)
                                .clickable { isSettingsOpen = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                contentDescription = "Settings",
                                tint = Color(0xFF888888),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Arrange Mode Active Guide Banner
                AnimatedVisibility(visible = isArrangeMode) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black)
                            .border(1.dp, Color(0xFF00E5FF), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PULL SECTIONS UP / DOWN",
                            color = Color(0xFF00E5FF),
                            fontSize = (11 * fontScale).sp,
                            fontFamily = activeFont,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "DONE ✓",
                            color = Color(0xFF00E5FF),
                            fontSize = (11 * fontScale).sp,
                            fontFamily = activeFont,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { isArrangeMode = false }
                                .padding(4.dp)
                        )
                    }
                }

                // ========================================================
                // DYNAMIC ADJUSTABLE SECTIONS (RENDERED ON PURE AMOLED)
                // ========================================================
                sectionOrder.forEachIndexed { index, section ->
                    val canMoveUp = index > 0
                    val canMoveDown = index < sectionOrder.size - 1

                    when (section) {
                        HomeSection.HEADER -> {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                if (isArrangeMode) {
                                    SectionReorderBar(
                                        position = index + 1,
                                        section = section,
                                        canMoveUp = canMoveUp,
                                        canMoveDown = canMoveDown,
                                        fontFamily = activeFont,
                                        onMoveUp = { moveSection(section, -1) },
                                        onMoveDown = { moveSection(section, 1) }
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                }

                                // Clock Row with Gemini AI Sparkle Icon Button on the Right
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = timeString,
                                        color = Color.White,
                                        fontSize = (60 * fontScale).sp,
                                        fontFamily = activeFont,
                                        fontWeight = FontWeight.ExtraLight,
                                        letterSpacing = (-2).sp
                                    )

                                    // Small Gemini AI Icon on Right Side of the Time
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color.Black)
                                            .border(1.2.dp, Color(0xFF00E5FF).copy(alpha = 0.8f), CircleShape)
                                            .clickable { launchGemini(context) }
                                            .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        GeminiSparkleIcon(
                                            modifier = Modifier.size(18.dp),
                                            color = Color(0xFF00E5FF)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = dateString,
                                    color = Color(0xFF888888),
                                    fontSize = (14 * fontScale).sp,
                                    fontFamily = activeFont,
                                    fontWeight = FontWeight.Normal
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Screen time detox pill (Pure AMOLED)
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color.Black)
                                        .border(1.dp, Color(0xFF222222), RoundedCornerShape(20.dp))
                                        .clickable {
                                            if (!hasUsagePermission) {
                                                context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                                                hasUsagePermission = checkUsageStatsPermission(context)
                                            }
                                        }
                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.HourglassEmpty,
                                        contentDescription = "Screen Time",
                                        tint = Color(0xFF39FF14),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = screenTimeText,
                                        color = Color(0xFFAAAAAA),
                                        fontSize = (11 * fontScale).sp,
                                        fontFamily = activeFont
                                    )
                                }
                            }
                        }

                        HomeSection.APPS -> {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                if (isArrangeMode) {
                                    SectionReorderBar(
                                        position = index + 1,
                                        section = section,
                                        canMoveUp = canMoveUp,
                                        canMoveDown = canMoveDown,
                                        fontFamily = activeFont,
                                        onMoveUp = { moveSection(section, -1) },
                                        onMoveDown = { moveSection(section, 1) }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "FAVORITES",
                                        color = Color(0xFF555555),
                                        fontSize = (11 * fontScale).sp,
                                        fontFamily = activeFont,
                                        letterSpacing = 1.4.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${selectedAppIds.size} APPS",
                                        color = Color(0xFF444444),
                                        fontSize = (10 * fontScale).sp,
                                        fontFamily = activeFont
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Resolve chosen apps: curated AppChoice or installed AppEntry
                                val activeHomeAppList = remember(selectedAppIds, installedApps) {
                                    val curatedMap = AVAILABLE_HOME_APPS.associateBy { it.id }
                                    val installedMap = installedApps.associateBy { it.packageName }

                                    selectedAppIds.mapNotNull { id ->
                                        curatedMap[id] ?: installedMap[id]?.let { entry ->
                                            AppChoice(
                                                id = entry.packageName,
                                                label = entry.label,
                                                icon = null,
                                                candidatePackages = listOf(entry.packageName),
                                                neonColor = getNeonColorForPackage(entry.packageName)
                                            )
                                        }
                                    }
                                }

                                val rows = activeHomeAppList.chunked(4)

                                rows.forEach { rowApps ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        rowApps.forEach { app ->
                                            MonochromeAppItem(
                                                app = app,
                                                iconSize = iconSizeDp.dp,
                                                fontFamily = activeFont,
                                                fontScale = fontScale,
                                                onClick = {
                                                    recordAppLaunch(prefs, app.candidatePackages.firstOrNull() ?: app.id)
                                                    launchAppChoice(context, app)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        HomeSection.QUICK_TOGGLES -> {
                            if (showQuickTogglesSection) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    if (isArrangeMode) {
                                        SectionReorderBar(
                                            position = index + 1,
                                            section = section,
                                            canMoveUp = canMoveUp,
                                            canMoveDown = canMoveDown,
                                            fontFamily = activeFont,
                                            onMoveUp = { moveSection(section, -1) },
                                            onMoveDown = { moveSection(section, 1) }
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    // Quick System Toggles (Wi-Fi, Silent Mode, Bluetooth, Android Settings)
                                    QuickTogglesBar(
                                        isWifiEnabled = isWifiEnabled,
                                        ringerMode = currentRingerMode,
                                        isBluetoothEnabled = isBluetoothEnabled,
                                        fontFamily = activeFont,
                                        fontScale = fontScale,
                                        onToggleWifi = {
                                            toggleWifi(context, wifiManager)
                                            isWifiEnabled = wifiManager?.isWifiEnabled ?: !isWifiEnabled
                                        },
                                        onCycleRinger = {
                                            currentRingerMode = cycleRingerMode(context, audioManager, notifManager)
                                        },
                                        onToggleBluetooth = {
                                            toggleBluetooth(context, btAdapter)
                                            isBluetoothEnabled = btAdapter?.isEnabled ?: !isBluetoothEnabled
                                        },
                                        onOpenSettings = {
                                            try {
                                                context.startActivity(Intent(Settings.ACTION_SETTINGS).apply {
                                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                })
                                            } catch (_: Exception) {
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        HomeSection.NOTIFICATIONS -> {
                            if (showNotificationsSection) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    if (isArrangeMode) {
                                        SectionReorderBar(
                                            position = index + 1,
                                            section = section,
                                            canMoveUp = canMoveUp,
                                            canMoveDown = canMoveDown,
                                            fontFamily = activeFont,
                                            onMoveUp = { moveSection(section, -1) },
                                            onMoveDown = { moveSection(section, 1) }
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "NOTIFICATIONS",
                                            color = Color(0xFF555555),
                                            fontSize = (11 * fontScale).sp,
                                            fontFamily = activeFont,
                                            letterSpacing = 1.4.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        if (!isNotificationAccessGranted) {
                                            Text(
                                                text = "Enable Access",
                                                color = Color(0xFF00E5FF),
                                                fontSize = (11 * fontScale).sp,
                                                fontFamily = activeFont,
                                                modifier = Modifier.clickable {
                                                    context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                                                }
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    val displayNotifs = incomingNotifications

                                    if (displayNotifs.isEmpty()) {
                                        Text(
                                            text = "No new notifications",
                                            color = Color(0xFF444444),
                                            fontSize = (11 * fontScale).sp,
                                            fontFamily = activeFont,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    } else {
                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            contentPadding = PaddingValues(vertical = 2.dp)
                                        ) {
                                            items(displayNotifs, key = { it.key }) { notif ->
                                                RollingNotificationCard(
                                                    notif = notif,
                                                    fontFamily = activeFont,
                                                    fontScale = fontScale,
                                                    onClick = {
                                                        try {
                                                            notif.pendingIntent?.send()
                                                        } catch (_: Exception) {
                                                            val launchIntent = context.packageManager.getLaunchIntentForPackage(notif.packageName)
                                                            if (launchIntent != null) context.startActivity(launchIntent)
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        HomeSection.AGENDA -> {
                            if (showAgendaSection) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    if (isArrangeMode) {
                                        SectionReorderBar(
                                            position = index + 1,
                                            section = section,
                                            canMoveUp = canMoveUp,
                                            canMoveDown = canMoveDown,
                                            fontFamily = activeFont,
                                            onMoveUp = { moveSection(section, -1) },
                                            onMoveDown = { moveSection(section, 1) }
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "UPCOMING MEETINGS & CALLS",
                                            color = Color(0xFF555555),
                                            fontSize = (11 * fontScale).sp,
                                            fontFamily = activeFont,
                                            letterSpacing = 1.4.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        if (agendaList.isNotEmpty()) {
                                            Text(
                                                text = "${agendaList.size} ITEMS",
                                                color = Color(0xFF00E5FF),
                                                fontSize = (10 * fontScale).sp,
                                                fontFamily = activeFont,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (agendaList.isEmpty()) {
                                        // Clean Minimal Zero-Fake State
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .border(1.dp, Color(0xFF222222), RoundedCornerShape(8.dp))
                                                .padding(14.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = "No upcoming meetings scheduled",
                                                color = Color(0xFF888888),
                                                fontSize = (12 * fontScale).sp,
                                                fontFamily = activeFont
                                            )
                                            Text(
                                                text = "Teams Work Profile, MS Teams & Gmail accounts connected",
                                                color = Color(0xFF555555),
                                                fontSize = (10 * fontScale).sp,
                                                fontFamily = activeFont
                                            )
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                modifier = Modifier.padding(top = 6.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .border(1.dp, Color(0xFF818CF8).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                                        .clickable { launchAppChoice(context, AVAILABLE_HOME_APPS.first { it.id == "teams" }) }
                                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                                ) {
                                                    Text("Open Teams", color = Color(0xFF818CF8), fontSize = (10 * fontScale).sp, fontFamily = activeFont)
                                                }
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .border(1.dp, Color(0xFFFF3366).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                                        .clickable { launchAppChoice(context, AVAILABLE_HOME_APPS.first { it.id == "gmail" }) }
                                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                                ) {
                                                    Text("Open Gmail", color = Color(0xFFFF3366), fontSize = (10 * fontScale).sp, fontFamily = activeFont)
                                                }
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                                        .clickable { agendaList = loadAgendaAndMissedCalls(context) }
                                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                                ) {
                                                    Text("Refresh ↻", color = Color(0xFF00E5FF), fontSize = (10 * fontScale).sp, fontFamily = activeFont)
                                                }
                                            }
                                        }
                                    } else {
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            agendaList.forEach { item ->
                                                MinimalAgendaRow(
                                                    item = item,
                                                    fontFamily = activeFont,
                                                    fontScale = fontScale,
                                                    onClick = {
                                                        if (item.targetIntent != null) {
                                                            try {
                                                                context.startActivity(item.targetIntent)
                                                            } catch (_: Exception) {
                                                            }
                                                        } else if (item.type == AgendaType.TEAMS_WORK || item.type == AgendaType.TEAMS_PERSONAL) {
                                                            launchAppChoice(context, AVAILABLE_HOME_APPS.first { it.id == "teams" })
                                                        } else if (item.type == AgendaType.GMAIL_PERSONAL) {
                                                            launchAppChoice(context, AVAILABLE_HOME_APPS.first { it.id == "gmail" })
                                                        } else if (item.type == AgendaType.MISSED_CALL) {
                                                            launchDialer(context)
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Hint (Swipe Up for Apps)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            isDrawerOpen = true
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowUp,
                        contentDescription = "Swipe up for apps",
                        tint = Color(0xFF444444),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "SWIPE UP FOR ALL APPS",
                        color = Color(0xFF444444),
                        fontSize = (10 * fontScale).sp,
                        fontFamily = activeFont,
                        letterSpacing = 1.6.sp
                    )
                }
            }

            // ---------------- SETTINGS MENU OVERLAY ----------------
            AnimatedVisibility(
                visible = isSettingsOpen,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                HomeScreenSettingsMenu(
                    sectionOrder = sectionOrder,
                    selectedAppIds = selectedAppIds,
                    installedApps = installedApps,
                    showQuickToggles = showQuickTogglesSection,
                    showNotifications = showNotificationsSection,
                    showAgenda = showAgendaSection,
                    isNotificationAccessGranted = isNotificationAccessGranted,
                    selectedFontFamily = selectedFontFamily,
                    selectedFontSize = selectedFontSize,
                    iconSizeDp = iconSizeDp,
                    layoutPaddingXDp = layoutPaddingXDp,
                    sectionSpacingYDp = sectionSpacingYDp,
                    onMoveSection = { section, direction ->
                        moveSection(section, direction)
                    },
                    onResetSectionOrder = {
                        val defaultList = HomeSection.defaultOrder()
                        sectionOrder = defaultList
                        prefs.edit().putString("section_order", HomeSection.serialize(defaultList)).apply()
                    },
                    onSelectFontFamily = { family ->
                        selectedFontFamily = family
                        prefs.edit().putString("font_family", family.id).apply()
                    },
                    onSelectFontSize = { size ->
                        selectedFontSize = size
                        prefs.edit().putString("font_size", size.id).apply()
                    },
                    onUpdateIconSize = { sizeDp ->
                        iconSizeDp = sizeDp
                        prefs.edit().putInt("icon_size_dp", sizeDp).apply()
                    },
                    onUpdateLayoutPaddingX = { paddingX ->
                        layoutPaddingXDp = paddingX
                        prefs.edit().putInt("layout_padding_x", paddingX).apply()
                    },
                    onUpdateSectionSpacingY = { spacingY ->
                        sectionSpacingYDp = spacingY
                        prefs.edit().putInt("section_spacing_y", spacingY).apply()
                    },
                    onToggleApp = { appId ->
                        val updated = if (appId in selectedAppIds) {
                            if (selectedAppIds.size > 1) selectedAppIds - appId else selectedAppIds
                        } else {
                            selectedAppIds + appId
                        }
                        selectedAppIds = updated
                        prefs.edit().putStringSet("home_apps", updated).apply()
                    },
                    onSelectAllApps = {
                        val allIds = (AVAILABLE_HOME_APPS.map { it.id } + installedApps.map { it.packageName }).toSet()
                        selectedAppIds = allIds
                        prefs.edit().putStringSet("home_apps", allIds).apply()
                    },
                    onResetCuratedApps = {
                        selectedAppIds = DEFAULT_SELECTED_APP_IDS
                        prefs.edit().putStringSet("home_apps", DEFAULT_SELECTED_APP_IDS).apply()
                    },
                    onClearAllApps = {
                        selectedAppIds = setOf("phone")
                        prefs.edit().putStringSet("home_apps", selectedAppIds).apply()
                    },
                    onToggleQuickToggles = { enabled ->
                        showQuickTogglesSection = enabled
                        prefs.edit().putBoolean("show_quick_toggles", enabled).apply()
                    },
                    onToggleNotifications = { enabled ->
                        showNotificationsSection = enabled
                        prefs.edit().putBoolean("show_notifs", enabled).apply()
                    },
                    onToggleAgenda = { enabled ->
                        showAgendaSection = enabled
                        prefs.edit().putBoolean("show_agenda", enabled).apply()
                    },
                    onRequestNotificationAccess = {
                        context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                    },
                    onRequestCalendarAndCallLog = {
                        permissionLauncher.launch(
                            arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.READ_CALL_LOG)
                        )
                    },
                    onRequestDeviceAdmin = {
                        lockDevice(context)
                    },
                    onClose = { isSettingsOpen = false }
                )
            }

            // ---------------- ALL-APPS DRAWER OVERLAY ----------------
            AnimatedVisibility(
                visible = isDrawerOpen,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                AppDrawerSheet(
                    apps = installedApps,
                    query = searchQuery,
                    fontFamily = activeFont,
                    fontScale = fontScale,
                    prefs = prefs,
                    onQueryChange = { searchQuery = it },
                    onAppClick = { app ->
                        isDrawerOpen = false
                        searchQuery = ""
                        recordAppLaunch(prefs, app.packageName)
                        launchAppEntry(context, app)
                    },
                    onClose = {
                        isDrawerOpen = false
                        searchQuery = ""
                    }
                )
            }
        }
    }
}

// ---------------- QUICK SYSTEM TOGGLES BAR ----------------

@Composable
private fun QuickTogglesBar(
    isWifiEnabled: Boolean,
    ringerMode: Int,
    isBluetoothEnabled: Boolean,
    fontFamily: FontFamily,
    fontScale: Float,
    onToggleWifi: () -> Unit,
    onCycleRinger: () -> Unit,
    onToggleBluetooth: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val (ringerGlyph, ringerLabel, ringerColor) = when (ringerMode) {
        AudioManager.RINGER_MODE_NORMAL -> Triple("VOL", "SOUND", Color(0xFF39FF14))
        AudioManager.RINGER_MODE_VIBRATE -> Triple("VIB", "VIBRATE", Color(0xFFFFD600))
        else -> Triple("SIL", "SILENT", Color(0xFFFF5252))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black)
            .border(1.dp, Color(0xFF1E1E1E), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Wi-Fi Toggle
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onToggleWifi)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .border(
                        1.dp,
                        if (isWifiEnabled) Color(0xFF00E5FF).copy(alpha = 0.85f) else Color(0xFF333333),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Wifi,
                    contentDescription = "Wi-Fi",
                    tint = if (isWifiEnabled) Color(0xFF00E5FF) else Color(0xFF777777),
                    modifier = Modifier.size(17.dp)
                )
            }
            Text("WI-FI", color = Color(0xFF777777), fontSize = (9 * fontScale).sp, fontFamily = fontFamily, fontWeight = FontWeight.Medium)
            Text(if (isWifiEnabled) "ON" else "OFF", color = if (isWifiEnabled) Color(0xFF00E5FF) else Color(0xFF555555), fontSize = (8 * fontScale).sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold)
        }

        // 2. Silent / Sound Mode Cycle
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onCycleRinger)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .border(
                        1.dp,
                        ringerColor.copy(alpha = 0.85f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ringerGlyph,
                    color = ringerColor,
                    fontSize = 11.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
            Text("MODE", color = Color(0xFF777777), fontSize = (9 * fontScale).sp, fontFamily = fontFamily, fontWeight = FontWeight.Medium)
            Text(ringerLabel, color = ringerColor, fontSize = (8 * fontScale).sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold)
        }

        // 3. Bluetooth Toggle
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onToggleBluetooth)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .border(
                        1.dp,
                        if (isBluetoothEnabled) Color(0xFF818CF8).copy(alpha = 0.85f) else Color(0xFF333333),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "BT",
                    color = if (isBluetoothEnabled) Color(0xFF818CF8) else Color(0xFF777777),
                    fontSize = 11.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
            Text("BLUETOOTH", color = Color(0xFF777777), fontSize = (9 * fontScale).sp, fontFamily = fontFamily, fontWeight = FontWeight.Medium)
            Text(if (isBluetoothEnabled) "ON" else "OFF", color = if (isBluetoothEnabled) Color(0xFF818CF8) else Color(0xFF555555), fontSize = (8 * fontScale).sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold)
        }

        // 4. Android Settings
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onOpenSettings)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .border(1.dp, Color(0xFF333333), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Settings",
                    tint = Color(0xFF888888),
                    modifier = Modifier.size(16.dp)
                )
            }
            Text("SETTINGS", color = Color(0xFF777777), fontSize = (9 * fontScale).sp, fontFamily = fontFamily, fontWeight = FontWeight.Medium)
            Text("SYSTEM", color = Color(0xFF555555), fontSize = (8 * fontScale).sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold)
        }
    }
}

// ---------------- Component Views ----------------

@Composable
private fun SectionReorderBar(
    position: Int,
    section: HomeSection,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    fontFamily: FontFamily,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color.Black)
            .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00E5FF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$position",
                    color = Color.Black,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamily
                )
            }
            Text(
                text = section.shortTitle,
                color = Color(0xFF00E5FF),
                fontSize = 11.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pull UP button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black)
                    .border(
                        1.dp,
                        if (canMoveUp) Color(0xFF00E5FF) else Color(0xFF333333),
                        RoundedCornerShape(4.dp)
                    )
                    .clickable(enabled = canMoveUp, onClick = onMoveUp)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowUp,
                    contentDescription = "Pull Up",
                    tint = if (canMoveUp) Color(0xFF00E5FF) else Color(0xFF444444),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "UP",
                    color = if (canMoveUp) Color.White else Color(0xFF444444),
                    fontSize = 10.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold
                )
            }

            // Pull DOWN button
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black)
                    .border(
                        1.dp,
                        if (canMoveDown) Color(0xFF00E5FF) else Color(0xFF333333),
                        RoundedCornerShape(4.dp)
                    )
                    .clickable(enabled = canMoveDown, onClick = onMoveDown)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "Pull Down",
                    tint = if (canMoveDown) Color(0xFF00E5FF) else Color(0xFF444444),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "DOWN",
                    color = if (canMoveDown) Color.White else Color(0xFF444444),
                    fontSize = 10.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ---------------- NEON GLOW APP ICON (ADJUSTABLE, SLEEK & UNTRIMMED) ----------------

@Composable
private fun MonochromeAppItem(
    app: AppChoice,
    iconSize: Dp,
    fontFamily: FontFamily,
    fontScale: Float,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(horizontal = 4.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(iconSize)
                .drawBehind {
                    // Contained subtle neon halo (does not bleed out or get clipped)
                    drawCircle(
                        color = app.neonColor.copy(alpha = 0.16f),
                        radius = size.minDimension * 0.58f
                    )
                }
                .clip(CircleShape)
                .background(Color.Black)
                .border(1.2.dp, app.neonColor.copy(alpha = 0.85f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (app.icon != null) {
                Icon(
                    imageVector = app.icon,
                    contentDescription = app.label,
                    tint = app.neonColor,
                    modifier = Modifier.size((iconSize.value * 0.46f).dp)
                )
            } else {
                Text(
                    text = app.label.take(1).uppercase(Locale.getDefault()),
                    color = app.neonColor,
                    fontSize = (iconSize.value * 0.42f).sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = app.label,
            color = Color(0xFFD4D4D4),
            fontSize = (10 * fontScale).sp,
            fontFamily = fontFamily,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun RollingNotificationCard(
    notif: S25NotificationItem,
    fontFamily: FontFamily,
    fontScale: Float,
    onClick: () -> Unit
) {
    val timeDiffMinutes = Math.max(1, (System.currentTimeMillis() - notif.timestamp) / 60_000L)
    val timeStr = if (timeDiffMinutes < 60) "${timeDiffMinutes}m ago" else "${timeDiffMinutes / 60}h ago"

    Row(
        modifier = Modifier
            .width(230.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black)
            .border(1.dp, Color(0xFF222222), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(Color.Black)
                .border(1.dp, Color(0xFF00E5FF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = notif.appName.take(1).uppercase(Locale.getDefault()),
                color = Color(0xFF00E5FF),
                fontSize = (11 * fontScale).sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notif.appName,
                    color = Color(0xFF00E5FF),
                    fontSize = (10 * fontScale).sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = timeStr,
                    color = Color(0xFF555555),
                    fontSize = (9 * fontScale).sp,
                    fontFamily = fontFamily
                )
            }

            if (notif.title.isNotBlank() && notif.title != notif.appName) {
                Text(
                    text = notif.title,
                    color = Color.White,
                    fontSize = (12 * fontScale).sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = if (notif.text.isNotBlank()) notif.text else notif.title,
                color = Color(0xFF888888),
                fontSize = (11 * fontScale).sp,
                fontFamily = fontFamily,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ---------------- CRYSTAL-CLEAR AGENDA ROW (TEAMS & GMAIL) ----------------

@Composable
private fun MinimalAgendaRow(
    item: AgendaItem,
    fontFamily: FontFamily,
    fontScale: Float,
    onClick: () -> Unit
) {
    val (badgeColor, badgeText) = when (item.type) {
        AgendaType.TEAMS_WORK -> Color(0xFF00E5FF) to "TEAMS WORK"
        AgendaType.TEAMS_PERSONAL -> Color(0xFF818CF8) to "MS TEAMS"
        AgendaType.GMAIL_PERSONAL -> Color(0xFFFF5252) to "GMAIL"
        AgendaType.MISSED_CALL -> Color(0xFFFF1744) to "MISSED CALL"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black)
            .border(1.dp, Color(0xFF222222), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Top Row: Time & Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.timeLabel,
                color = if (item.type == AgendaType.MISSED_CALL) Color(0xFFFF5252) else Color(0xFF00E5FF),
                fontSize = (11 * fontScale).sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold
            )

            // Tag Badge with Neon Outline
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black)
                    .border(1.dp, badgeColor, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badgeText,
                    color = badgeColor,
                    fontSize = (9 * fontScale).sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Meeting Title (Full, unclipped, up to 2 lines)
        Text(
            text = item.title,
            color = Color.White,
            fontSize = (14 * fontScale).sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // Subtitle & Action hint
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.subtitle.isNotBlank()) {
                Text(
                    text = item.subtitle,
                    color = Color(0xFF777777),
                    fontSize = (11 * fontScale).sp,
                    fontFamily = fontFamily,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }

            if (item.targetIntent != null) {
                Text(
                    text = "TAP TO JOIN ↗",
                    color = Color(0xFF00E5FF),
                    fontSize = (10 * fontScale).sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold
                )
            } else if (item.type == AgendaType.MISSED_CALL) {
                Text(
                    text = "TAP TO DIAL 📞",
                    color = Color(0xFFFF5252),
                    fontSize = (10 * fontScale).sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ---------------- SETTINGS MENU OVERLAY ----------------

@Composable
private fun HomeScreenSettingsMenu(
    sectionOrder: List<HomeSection>,
    selectedAppIds: Set<String>,
    installedApps: List<AppEntry>,
    showQuickToggles: Boolean,
    showNotifications: Boolean,
    showAgenda: Boolean,
    isNotificationAccessGranted: Boolean,
    selectedFontFamily: LauncherFontFamily,
    selectedFontSize: LauncherFontSize,
    iconSizeDp: Int,
    layoutPaddingXDp: Int,
    sectionSpacingYDp: Int,
    onMoveSection: (HomeSection, Int) -> Unit,
    onResetSectionOrder: () -> Unit,
    onSelectFontFamily: (LauncherFontFamily) -> Unit,
    onSelectFontSize: (LauncherFontSize) -> Unit,
    onUpdateIconSize: (Int) -> Unit,
    onUpdateLayoutPaddingX: (Int) -> Unit,
    onUpdateSectionSpacingY: (Int) -> Unit,
    onToggleApp: (String) -> Unit,
    onSelectAllApps: () -> Unit,
    onResetCuratedApps: () -> Unit,
    onClearAllApps: () -> Unit,
    onToggleQuickToggles: (Boolean) -> Unit,
    onToggleNotifications: (Boolean) -> Unit,
    onToggleAgenda: (Boolean) -> Unit,
    onRequestNotificationAccess: () -> Unit,
    onRequestCalendarAndCallLog: () -> Unit,
    onRequestDeviceAdmin: () -> Unit,
    onClose: () -> Unit
) {
    val activeFont = selectedFontFamily.composeFamily
    val fontScale = selectedFontSize.scale
    var appSearchFilter by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "LAUNCHER SETTINGS",
                        color = Color.White,
                        fontSize = (17 * fontScale).sp,
                        fontFamily = activeFont,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Customize layout, dimensions & gestures",
                        color = Color(0xFF666666),
                        fontSize = (11 * fontScale).sp,
                        fontFamily = activeFont
                    )
                }

                Text(
                    text = "DONE",
                    color = Color(0xFF00E5FF),
                    fontSize = (13 * fontScale).sp,
                    fontFamily = activeFont,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable(onClick = onClose)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ========================================================
            // 1. ADJUSTABLE LAYOUT & SECTION ORDER (PULL UP/DOWN)
            // ========================================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "1. SECTION ORDER",
                    color = Color(0xFF888888),
                    fontSize = (12 * fontScale).sp,
                    fontFamily = activeFont,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Reset Default",
                    color = Color(0xFF666666),
                    fontSize = (11 * fontScale).sp,
                    fontFamily = activeFont,
                    modifier = Modifier.clickable(onClick = onResetSectionOrder)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF222222), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                sectionOrder.forEachIndexed { idx, sec ->
                    val canUp = idx > 0
                    val canDown = idx < sectionOrder.size - 1

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black)
                            .border(1.dp, Color(0xFF1E1E1E), RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "${idx + 1}",
                                color = Color(0xFF00E5FF),
                                fontSize = (12 * fontScale).sp,
                                fontFamily = activeFont,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = sec.title,
                                color = Color.White,
                                fontSize = (13 * fontScale).sp,
                                fontFamily = activeFont,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(1.dp, if (canUp) Color(0xFF00E5FF) else Color(0xFF333333), RoundedCornerShape(4.dp))
                                    .clickable(enabled = canUp) { onMoveSection(sec, -1) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("UP", color = if (canUp) Color.White else Color(0xFF444444), fontSize = 10.sp, fontFamily = activeFont)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(1.dp, if (canDown) Color(0xFF00E5FF) else Color(0xFF333333), RoundedCornerShape(4.dp))
                                    .clickable(enabled = canDown) { onMoveSection(sec, 1) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("DOWN", color = if (canDown) Color.White else Color(0xFF444444), fontSize = 10.sp, fontFamily = activeFont)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ========================================================
            // 2. DIMENSIONS: ICON SIZE, LAYOUT WIDTH & SECTION SPACING
            // ========================================================
            Text(
                text = "2. ICON SIZE & SECTION DIMENSIONS",
                color = Color(0xFF888888),
                fontSize = (12 * fontScale).sp,
                fontFamily = activeFont,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF222222), RoundedCornerShape(8.dp))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Icon Size
                Text("Icon Size (Home Screen Grid):", color = Color(0xFFAAAAAA), fontSize = (11 * fontScale).sp, fontFamily = activeFont)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(36 to "36dp", 44 to "44dp", 52 to "52dp", 60 to "60dp").forEach { (size, label) ->
                        val isSelected = iconSizeDp == size
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .border(1.dp, if (isSelected) Color(0xFF00E5FF) else Color(0xFF333333), RoundedCornerShape(6.dp))
                                .clickable { onUpdateIconSize(size) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color(0xFF00E5FF) else Color(0xFF888888),
                                fontSize = (11 * fontScale).sp,
                                fontFamily = activeFont,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Layout Width / Padding
                Text("Layout Width (Edge Margin):", color = Color(0xFFAAAAAA), fontSize = (11 * fontScale).sp, fontFamily = activeFont)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(8 to "Wide (8dp)", 18 to "Standard (18dp)", 28 to "Narrow (28dp)").forEach { (pad, label) ->
                        val isSelected = layoutPaddingXDp == pad
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .border(1.dp, if (isSelected) Color(0xFF00E5FF) else Color(0xFF333333), RoundedCornerShape(6.dp))
                                .clickable { onUpdateLayoutPaddingX(pad) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color(0xFF00E5FF) else Color(0xFF888888),
                                fontSize = (10 * fontScale).sp,
                                fontFamily = activeFont,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Section Spacing / Height
                Text("Section Vertical Spacing:", color = Color(0xFFAAAAAA), fontSize = (11 * fontScale).sp, fontFamily = activeFont)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(12 to "Compact (12dp)", 20 to "Balanced (20dp)", 28 to "Spacious (28dp)").forEach { (sp, label) ->
                        val isSelected = sectionSpacingYDp == sp
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .border(1.dp, if (isSelected) Color(0xFF00E5FF) else Color(0xFF333333), RoundedCornerShape(6.dp))
                                .clickable { onUpdateSectionSpacingY(sp) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color(0xFF00E5FF) else Color(0xFF888888),
                                fontSize = (10 * fontScale).sp,
                                fontFamily = activeFont,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ========================================================
            // 3. TYPOGRAPHY (FONT FAMILY & FONT SIZE)
            // ========================================================
            Text(
                text = "3. TYPOGRAPHY & FONT STYLES",
                color = Color(0xFF888888),
                fontSize = (12 * fontScale).sp,
                fontFamily = activeFont,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF222222), RoundedCornerShape(8.dp))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Font Family:", color = Color(0xFFAAAAAA), fontSize = (11 * fontScale).sp, fontFamily = activeFont)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LauncherFontFamily.entries.forEach { family ->
                        val isSelected = selectedFontFamily == family
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .border(1.dp, if (isSelected) Color(0xFF00E5FF) else Color(0xFF333333), RoundedCornerShape(6.dp))
                                .clickable { onSelectFontFamily(family) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = family.label,
                                color = if (isSelected) Color(0xFF00E5FF) else Color(0xFF888888),
                                fontSize = (11 * fontScale).sp,
                                fontFamily = family.composeFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Text("Font Size Scale:", color = Color(0xFFAAAAAA), fontSize = (11 * fontScale).sp, fontFamily = activeFont)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LauncherFontSize.entries.forEach { sizeOption ->
                        val isSelected = selectedFontSize == sizeOption
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .border(1.dp, if (isSelected) Color(0xFF00E5FF) else Color(0xFF333333), RoundedCornerShape(6.dp))
                                .clickable { onSelectFontSize(sizeOption) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = sizeOption.label.split(" ").first(),
                                color = if (isSelected) Color(0xFF00E5FF) else Color(0xFF888888),
                                fontSize = (11 * sizeOption.scale).sp,
                                fontFamily = activeFont,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ========================================================
            // 4. CHOOSE FAVORITES (ALL APPS OR CURATED)
            // ========================================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "4. FAVORITES APPS SELECTION",
                    color = Color(0xFF888888),
                    fontSize = (12 * fontScale).sp,
                    fontFamily = activeFont,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${selectedAppIds.size} Selected",
                    color = Color(0xFF00E5FF),
                    fontSize = (11 * fontScale).sp,
                    fontFamily = activeFont,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Batch Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .border(1.dp, Color(0xFF00E5FF), RoundedCornerShape(6.dp))
                        .clickable(onClick = onSelectAllApps)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("ADD ALL APPS", color = Color(0xFF00E5FF), fontSize = (10 * fontScale).sp, fontFamily = activeFont, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .border(1.dp, Color(0xFF888888), RoundedCornerShape(6.dp))
                        .clickable(onClick = onResetCuratedApps)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("CURATED (6)", color = Color.White, fontSize = (10 * fontScale).sp, fontFamily = activeFont)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .border(1.dp, Color(0xFF444444), RoundedCornerShape(6.dp))
                        .clickable(onClick = onClearAllApps)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("CLEAR", color = Color(0xFF888888), fontSize = (10 * fontScale).sp, fontFamily = activeFont)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Search input within settings to find apps quickly
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .border(1.dp, Color(0xFF262626), RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.Search, contentDescription = null, tint = Color(0xFF666666), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = appSearchFilter,
                    onValueChange = { appSearchFilter = it },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.White, fontSize = (12 * fontScale).sp, fontFamily = activeFont),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (appSearchFilter.isEmpty()) {
                            Text("Filter apps to add...", color = Color(0xFF555555), fontSize = (12 * fontScale).sp, fontFamily = activeFont)
                        }
                        innerTextField()
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF222222), RoundedCornerShape(8.dp))
                    .verticalScroll(rememberScrollState())
                    .padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // 1. Curated Popular Apps
                AVAILABLE_HOME_APPS.filter {
                    appSearchFilter.isBlank() || it.label.contains(appSearchFilter, ignoreCase = true)
                }.forEach { app ->
                    val isChecked = app.id in selectedAppIds
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onToggleApp(app.id) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (app.icon != null) {
                                Icon(
                                    imageVector = app.icon,
                                    contentDescription = app.label,
                                    tint = if (isChecked) app.neonColor else Color(0xFF555555),
                                    modifier = Modifier.size(18.dp)
                                )
                            } else {
                                Text(
                                    text = app.label.take(1).uppercase(Locale.getDefault()),
                                    color = if (isChecked) app.neonColor else Color(0xFF555555),
                                    fontSize = 12.sp,
                                    fontFamily = activeFont,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = app.label,
                                color = if (isChecked) Color.White else Color(0xFF777777),
                                fontSize = (13 * fontScale).sp,
                                fontFamily = activeFont,
                                fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }

                        Icon(
                            imageVector = if (isChecked) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                            contentDescription = if (isChecked) "Selected" else "Unselected",
                            tint = if (isChecked) app.neonColor else Color(0xFF333333),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // 2. All Other Installed Apps
                installedApps.filter { entry ->
                    AVAILABLE_HOME_APPS.none { it.id == entry.packageName || entry.packageName in it.candidatePackages } &&
                            (appSearchFilter.isBlank() || entry.label.contains(appSearchFilter, ignoreCase = true) || entry.packageName.contains(appSearchFilter, ignoreCase = true))
                }.forEach { entry ->
                    val isChecked = entry.packageName in selectedAppIds
                    val neonColor = getNeonColorForPackage(entry.packageName)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onToggleApp(entry.packageName) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black)
                                    .border(1.dp, if (isChecked) neonColor else Color(0xFF444444), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = entry.label.take(1).uppercase(Locale.getDefault()),
                                    color = if (isChecked) neonColor else Color(0xFF777777),
                                    fontSize = 10.sp,
                                    fontFamily = activeFont,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Text(
                                    text = entry.label,
                                    color = if (isChecked) Color.White else Color(0xFF777777),
                                    fontSize = (12 * fontScale).sp,
                                    fontFamily = activeFont,
                                    fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal
                                )
                                Text(
                                    text = entry.packageName,
                                    color = Color(0xFF444444),
                                    fontSize = (9 * fontScale).sp,
                                    fontFamily = activeFont
                                )
                            }
                        }

                        Icon(
                            imageVector = if (isChecked) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                            contentDescription = if (isChecked) "Selected" else "Unselected",
                            tint = if (isChecked) neonColor else Color(0xFF333333),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ========================================================
            // 5. GESTURES & SYSTEM PERMISSIONS
            // ========================================================
            Text(
                text = "5. GESTURES & SYSTEM INTEGRATIONS",
                color = Color(0xFF888888),
                fontSize = (12 * fontScale).sp,
                fontFamily = activeFont,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF222222), RoundedCornerShape(8.dp))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Double tap to lock permission
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onRequestDeviceAdmin)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Double-Tap to Lock Screen", color = Color.White, fontSize = (12 * fontScale).sp, fontFamily = activeFont)
                        Text("Device Admin Policy", color = Color(0xFF666666), fontSize = (10 * fontScale).sp, fontFamily = activeFont)
                    }
                    Text(
                        text = "CONFIGURE",
                        color = Color(0xFF00E5FF),
                        fontSize = (11 * fontScale).sp,
                        fontFamily = activeFont,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Notification permission
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onRequestNotificationAccess)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Notification Listener Access", color = Color.White, fontSize = (12 * fontScale).sp, fontFamily = activeFont)
                    Text(
                        text = if (isNotificationAccessGranted) "GRANTED ✓" else "GRANT",
                        color = if (isNotificationAccessGranted) Color(0xFF39FF14) else Color(0xFF00E5FF),
                        fontSize = (11 * fontScale).sp,
                        fontFamily = activeFont,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Calendar & Call Log permission
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onRequestCalendarAndCallLog)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Calendar (Teams / Gmail) & Call Log", color = Color.White, fontSize = (12 * fontScale).sp, fontFamily = activeFont)
                    Text(
                        text = "SYNC",
                        color = Color(0xFF00E5FF),
                        fontSize = (11 * fontScale).sp,
                        fontFamily = activeFont,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ---------------- ALL-APPS DRAWER ----------------

@Composable
private fun AppDrawerSheet(
    apps: List<AppEntry>,
    query: String,
    fontFamily: FontFamily,
    fontScale: Float,
    prefs: SharedPreferences,
    onQueryChange: (String) -> Unit,
    onAppClick: (AppEntry) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val recentApps = remember(apps, query) {
        if (query.isBlank()) getTopRecentAndFrequentApps(context, apps, prefs) else emptyList()
    }

    val filteredApps = remember(apps, query) {
        if (query.isBlank()) apps else apps.filter {
            it.label.contains(query, ignoreCase = true) ||
                    it.packageName.contains(query, ignoreCase = true)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF262626), RoundedCornerShape(8.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF888888),
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search apps...",
                            color = Color(0xFF555555),
                            fontSize = (14 * fontScale).sp,
                            fontFamily = fontFamily
                        )
                    }
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = (14 * fontScale).sp,
                            fontFamily = fontFamily
                        ),
                        cursorBrush = SolidColor(Color.White),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "Clear",
                        tint = Color(0xFF888888),
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { onQueryChange("") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ---------------- RECENT & MOST OPENED 10 APPS (ONLY WHEN NOT SEARCHING) ----------------
            if (query.isBlank() && recentApps.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "⏱",
                            fontSize = (12 * fontScale).sp
                        )
                        Text(
                            text = "RECENT & FREQUENT (TOP 10)",
                            color = Color(0xFF00E5FF),
                            fontSize = (11 * fontScale).sp,
                            fontFamily = fontFamily,
                            letterSpacing = 1.2.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(recentApps, key = { "recent_" + it.packageName }) { app ->
                        val neonColor = getNeonColorForPackage(app.packageName)
                        Column(
                            modifier = Modifier
                                .width(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onAppClick(app) }
                                .padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black)
                                    .border(1.2.dp, neonColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = app.label.take(1).uppercase(Locale.getDefault()),
                                    color = neonColor,
                                    fontSize = (13 * fontScale).sp,
                                    fontFamily = fontFamily,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = app.label,
                                color = Color.White,
                                fontSize = (10 * fontScale).sp,
                                fontFamily = fontFamily,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredApps.size} APPS",
                    color = Color(0xFF666666),
                    fontSize = (11 * fontScale).sp,
                    fontFamily = fontFamily,
                    letterSpacing = 1.2.sp
                )

                Text(
                    text = "CLOSE",
                    color = Color(0xFFAAAAAA),
                    fontSize = (11 * fontScale).sp,
                    fontFamily = fontFamily,
                    modifier = Modifier.clickable(onClick = onClose)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(filteredApps, key = { it.packageName + it.activityName }) { app ->
                    val neonColor = getNeonColorForPackage(app.packageName)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAppClick(app) }
                            .padding(vertical = 10.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.Black)
                                .border(1.dp, neonColor.copy(alpha = 0.7f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = app.label.take(1).uppercase(Locale.getDefault()),
                                color = neonColor,
                                fontSize = (13 * fontScale).sp,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = app.label,
                                color = Color.White,
                                fontSize = (14 * fontScale).sp,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = app.packageName,
                                color = Color(0xFF555555),
                                fontSize = (10 * fontScale).sp,
                                fontFamily = fontFamily,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------- Helper Launcher & Data Functions ----------------

private fun loadInstalledApps(context: Context): List<AppEntry> {
    val pm = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN, null).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }

    val activities = pm.queryIntentActivities(intent, 0)
    return activities.map { resolveInfo ->
        val label = resolveInfo.loadLabel(pm).toString()
        val pkg = resolveInfo.activityInfo.packageName
        val name = resolveInfo.activityInfo.name
        AppEntry(label = label, packageName = pkg, activityName = name)
    }.distinctBy { it.packageName }.sortedBy { it.label.lowercase(Locale.getDefault()) }
}

private fun recordAppLaunch(prefs: SharedPreferences, packageName: String) {
    try {
        val current = prefs.getInt("launch_count_$packageName", 0)
        prefs.edit()
            .putInt("launch_count_$packageName", current + 1)
            .putLong("last_launch_$packageName", System.currentTimeMillis())
            .apply()
    } catch (_: Exception) {
    }
}

private fun getTopRecentAndFrequentApps(
    context: Context,
    installedApps: List<AppEntry>,
    prefs: SharedPreferences
): List<AppEntry> {
    val installedMap = installedApps.associateBy { it.packageName }
    val scores = mutableMapOf<String, Long>()

    // 1. Check UsageStatsManager if permission is granted
    if (checkUsageStatsPermission(context)) {
        try {
            val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
            val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }
            val stats = usageStatsManager?.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                cal.timeInMillis,
                System.currentTimeMillis()
            )
            stats?.forEach { stat ->
                if (stat.packageName in installedMap) {
                    val recencyScore = stat.lastTimeUsed / 1000L
                    val usageScore = stat.totalTimeInForeground / 1000L
                    scores[stat.packageName] = (scores[stat.packageName] ?: 0L) + recencyScore + usageScore
                }
            }
        } catch (_: Exception) {
        }
    }

    // 2. Add Launcher's internal launch tracking counts from SharedPreferences
    for (app in installedApps) {
        val launchCount = prefs.getInt("launch_count_${app.packageName}", 0)
        val lastLaunch = prefs.getLong("last_launch_${app.packageName}", 0L)
        if (launchCount > 0 || lastLaunch > 0L) {
            scores[app.packageName] = (scores[app.packageName] ?: 0L) + (launchCount * 1_000_000L) + (lastLaunch / 1000L)
        }
    }

    // Sort by highest score
    val sortedRecent = scores.entries
        .sortedByDescending { it.value }
        .mapNotNull { installedMap[it.key] }
        .take(10)

    return if (sortedRecent.isNotEmpty()) {
        sortedRecent
    } else {
        installedApps.take(10)
    }
}

private fun launchAppEntry(context: Context, app: AppEntry) {
    try {
        val intent = context.packageManager.getLaunchIntentForPackage(app.packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    } catch (_: Exception) {
        Toast.makeText(context, "Cannot open ${app.label}", Toast.LENGTH_SHORT).show()
    }
}

private fun launchAppChoice(context: Context, choice: AppChoice) {
    for (pkg in choice.candidatePackages) {
        val intent = context.packageManager.getLaunchIntentForPackage(pkg)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            return
        }
    }

    if (choice.fallbackIntentAction != null) {
        try {
            val fallback = Intent(choice.fallbackIntentAction).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(fallback)
            return
        } catch (_: Exception) {
        }
    }

    Toast.makeText(context, "${choice.label} is not installed", Toast.LENGTH_SHORT).show()
}

private fun launchGemini(context: Context) {
    // 1. Try Google Gemini App
    val geminiIntent = context.packageManager.getLaunchIntentForPackage("com.google.android.apps.bard")
    if (geminiIntent != null) {
        geminiIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(geminiIntent)
        return
    }

    // 2. Try Assistant Action
    try {
        val assistIntent = Intent(Intent.ACTION_ASSIST).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(assistIntent)
        return
    } catch (_: Exception) {
    }

    // 3. Fallback to Gemini Web URL
    try {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://gemini.google.com")).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(webIntent)
    } catch (_: Exception) {
        Toast.makeText(context, "Cannot open Gemini", Toast.LENGTH_SHORT).show()
    }
}

private fun expandNotificationShade(context: Context) {
    try {
        val statusBarService = context.getSystemService("statusbar")
        val statusBarManager = Class.forName("android.app.StatusBarManager")
        val method = statusBarManager.getMethod("expandNotificationsPanel")
        method.invoke(statusBarService)
    } catch (_: Exception) {
        try {
            val statusBarService = context.getSystemService("statusbar")
            val statusBarManager = Class.forName("android.app.StatusBarManager")
            val method = statusBarManager.getMethod("expand")
            method.invoke(statusBarService)
        } catch (_: Exception) {
        }
    }
}

private fun lockDevice(context: Context) {
    val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as? DevicePolicyManager ?: return
    val adminComponent = ComponentName(context, S25DeviceAdminReceiver::class.java)
    if (dpm.isAdminActive(adminComponent)) {
        try {
            dpm.lockNow()
        } catch (e: Exception) {
            Toast.makeText(context, "Lock failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    } else {
        try {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent)
                putExtra(
                    DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "Minimal S25 needs admin permission to lock the device upon double-tap."
                )
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot enable double-tap to lock: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

private fun toggleWifi(context: Context, wifiManager: WifiManager?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        try {
            context.startActivity(Intent("android.settings.panel.action.WIFI").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } catch (_: Exception) {
            try {
                context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            } catch (_: Exception) {
            }
        }
    } else {
        @Suppress("DEPRECATION")
        wifiManager?.let {
            it.isWifiEnabled = !it.isWifiEnabled
        }
    }
}

private fun cycleRingerMode(context: Context, audioManager: AudioManager?, notifManager: NotificationManager?): Int {
    if (audioManager == null) return AudioManager.RINGER_MODE_NORMAL
    val current = audioManager.ringerMode
    val nextMode = when (current) {
        AudioManager.RINGER_MODE_NORMAL -> AudioManager.RINGER_MODE_VIBRATE
        AudioManager.RINGER_MODE_VIBRATE -> AudioManager.RINGER_MODE_SILENT
        else -> AudioManager.RINGER_MODE_NORMAL
    }

    if (nextMode == AudioManager.RINGER_MODE_SILENT && notifManager != null && !notifManager.isNotificationPolicyAccessGranted) {
        try {
            context.startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } catch (_: Exception) {
        }
        return current
    } else {
        try {
            audioManager.ringerMode = nextMode
            return nextMode
        } catch (_: Exception) {
            return current
        }
    }
}

private fun toggleBluetooth(context: Context, adapter: BluetoothAdapter?) {
    if (adapter == null) return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        try {
            context.startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } catch (_: Exception) {
        }
    } else {
        @Suppress("DEPRECATION")
        try {
            if (adapter.isEnabled) adapter.disable() else adapter.enable()
        } catch (_: Exception) {
        }
    }
}

private fun launchDialer(context: Context) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
    }
}

private fun launchCameraApp(context: Context) {
    try {
        val intent = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
    }
}

private fun checkUsageStatsPermission(context: Context): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager ?: return false
    val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.packageName)
    } else {
        @Suppress("DEPRECATION")
        appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.packageName)
    }
    return mode == AppOpsManager.MODE_ALLOWED
}

private fun computeTodayScreenTime(context: Context): String {
    return try {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager ?: return "--"

        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            cal.timeInMillis,
            System.currentTimeMillis()
        )

        var totalMs = 0L
        for (item in stats) {
            totalMs += item.totalTimeInForeground
        }

        val totalMinutes = totalMs / (1000 * 60)
        val hours = totalMinutes / 60
        val mins = totalMinutes % 60

        if (hours > 0) "${hours}h ${mins}m today" else "${mins}m today"
    } catch (_: Exception) {
        "Usage access needed"
    }
}

// ---------------- TIME FORMATTER FOR CRYSTAL CLEAR AGENDAS ----------------

private fun formatMeetingTime(beginMs: Long, endMs: Long): String {
    val now = Calendar.getInstance()
    val eventCal = Calendar.getInstance().apply { timeInMillis = beginMs }

    val isToday = now.get(Calendar.YEAR) == eventCal.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == eventCal.get(Calendar.DAY_OF_YEAR)

    val isTomorrow = now.get(Calendar.YEAR) == eventCal.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) + 1 == eventCal.get(Calendar.DAY_OF_YEAR)

    val timeFmt = SimpleDateFormat("h:mm a", Locale.getDefault())
    val dateFmt = SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    val datePrefix = when {
        isToday -> "TODAY"
        isTomorrow -> "TOMORROW"
        else -> dateFmt.format(Date(beginMs)).uppercase(Locale.getDefault())
    }

    val startTime = timeFmt.format(Date(beginMs))
    val endTime = if (endMs > beginMs && (endMs - beginMs) < 24 * 3600_000L) {
        " - ${timeFmt.format(Date(endMs))}"
    } else ""

    return "$datePrefix • $startTime$endTime"
}

// ---------------- ROBUST AGENDA RESOLVER (WORK TEAMS, PERSONAL TEAMS & GMAIL) ----------------

private fun loadAgendaAndMissedCalls(context: Context): List<AgendaItem> {
    val result = mutableListOf<AgendaItem>()

    // 1. Query Missed Calls if READ_CALL_LOG is granted
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
        try {
            val cursor: Cursor? = context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                arrayOf(CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.TYPE),
                "${CallLog.Calls.TYPE} = ?",
                arrayOf(CallLog.Calls.MISSED_TYPE.toString()),
                "${CallLog.Calls.DATE} DESC LIMIT 2"
            )
            cursor?.use {
                while (it.moveToNext()) {
                    val name = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME))
                    val number = it.getString(it.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                    val dateMs = it.getLong(it.getColumnIndexOrThrow(CallLog.Calls.DATE))
                    val titleName = if (!name.isNullOrBlank()) name else number ?: "Unknown"
                    val diffMin = Math.max(1, (System.currentTimeMillis() - dateMs) / 60_000L)
                    val timeStr = if (diffMin < 60) "${diffMin}m ago" else "${diffMin / 60}h ago"

                    result.add(
                        AgendaItem(
                            id = "call_$dateMs",
                            timeLabel = "MISSED CALL • $timeStr",
                            title = titleName,
                            subtitle = "Tap to call back (${number ?: "Unknown"})",
                            type = AgendaType.MISSED_CALL,
                            targetIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
                        )
                    )
                }
            }
        } catch (_: Exception) {
        }
    }

    // 2. Query Calendar: Both Personal Profile & Enterprise / Work Profile (Teams & Gmail)
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
        val startWindow = System.currentTimeMillis() - 3600_000L // from 1 hour ago
        val endWindow = System.currentTimeMillis() + (72L * 3600_000L) // up to 72 hours ahead

        // Map calendarId -> (accountName, accountType, displayName)
        val calendarMeta = mutableMapOf<Long, Triple<String, String, String>>()
        try {
            val calCursor = context.contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                arrayOf(
                    CalendarContract.Calendars._ID,
                    CalendarContract.Calendars.ACCOUNT_NAME,
                    CalendarContract.Calendars.ACCOUNT_TYPE,
                    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
                ),
                null,
                null,
                null
            )
            calCursor?.use {
                val idIdx = it.getColumnIndex(CalendarContract.Calendars._ID)
                val nameIdx = it.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME)
                val typeIdx = it.getColumnIndex(CalendarContract.Calendars.ACCOUNT_TYPE)
                val dispIdx = it.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
                while (it.moveToNext()) {
                    val id = it.getLong(idIdx)
                    val name = if (nameIdx >= 0) it.getString(nameIdx) ?: "" else ""
                    val type = if (typeIdx >= 0) it.getString(typeIdx) ?: "" else ""
                    val disp = if (dispIdx >= 0) it.getString(dispIdx) ?: "" else ""
                    calendarMeta[id] = Triple(name, type, disp)
                }
            }
        } catch (_: Exception) {
        }

        val seenEventIds = mutableSetOf<String>()

        val queryCalendarUri = { uri: Uri, isEnterprise: Boolean ->
            try {
                val fullUri = uri.buildUpon()
                    .appendPath(startWindow.toString())
                    .appendPath(endWindow.toString())
                    .build()

                val projection = arrayOf(
                    CalendarContract.Instances.TITLE,
                    CalendarContract.Instances.BEGIN,
                    CalendarContract.Instances.END,
                    CalendarContract.Instances.CALENDAR_ID,
                    CalendarContract.Instances.EVENT_LOCATION,
                    CalendarContract.Instances.DESCRIPTION,
                    CalendarContract.Instances.ORGANIZER
                )

                val cursor = context.contentResolver.query(
                    fullUri,
                    projection,
                    null,
                    null,
                    "${CalendarContract.Instances.BEGIN} ASC LIMIT 10"
                )

                cursor?.use {
                    val titleIdx = it.getColumnIndex(CalendarContract.Instances.TITLE)
                    val beginIdx = it.getColumnIndex(CalendarContract.Instances.BEGIN)
                    val endIdx = it.getColumnIndex(CalendarContract.Instances.END)
                    val calIdIdx = it.getColumnIndex(CalendarContract.Instances.CALENDAR_ID)
                    val locIdx = it.getColumnIndex(CalendarContract.Instances.EVENT_LOCATION)
                    val descIdx = it.getColumnIndex(CalendarContract.Instances.DESCRIPTION)
                    val orgIdx = it.getColumnIndex(CalendarContract.Instances.ORGANIZER)

                    while (it.moveToNext()) {
                        val title = if (titleIdx >= 0) it.getString(titleIdx) ?: "Meeting" else "Meeting"
                        val begin = if (beginIdx >= 0) it.getLong(beginIdx) else System.currentTimeMillis()
                        val end = if (endIdx >= 0) it.getLong(endIdx) else begin + 3600_000L
                        val calId = if (calIdIdx >= 0) it.getLong(calIdIdx) else -1L
                        val loc = if (locIdx >= 0) it.getString(locIdx) ?: "" else ""
                        val desc = if (descIdx >= 0) it.getString(descIdx) ?: "" else ""
                        val org = if (orgIdx >= 0) it.getString(orgIdx) ?: "" else ""

                        val eventKey = "$title-$begin"
                        if (eventKey in seenEventIds) continue
                        seenEventIds.add(eventKey)

                        val (accName, accType, calDisp) = calendarMeta[calId] ?: Triple("", "", "")

                        val isTeams = accType.contains("microsoft", ignoreCase = true) ||
                                accType.contains("teams", ignoreCase = true) ||
                                accType.contains("office", ignoreCase = true) ||
                                accType.contains("workaccount", ignoreCase = true) ||
                                accName.contains("microsoft", ignoreCase = true) ||
                                accName.contains("teams", ignoreCase = true) ||
                                accName.contains("outlook", ignoreCase = true) ||
                                calDisp.contains("teams", ignoreCase = true) ||
                                calDisp.contains("microsoft", ignoreCase = true) ||
                                desc.contains("teams.microsoft.com", ignoreCase = true) ||
                                loc.contains("teams.microsoft.com", ignoreCase = true) ||
                                org.contains("teams", ignoreCase = true)

                        val isWork = isEnterprise ||
                                accType.contains("workaccount", ignoreCase = true) ||
                                accType.contains("corporate", ignoreCase = true) ||
                                accName.contains("work", ignoreCase = true) ||
                                accName.contains("corp", ignoreCase = true) ||
                                accName.contains("company", ignoreCase = true) ||
                                accName.contains("office365", ignoreCase = true)

                        val type = when {
                            isTeams && isWork -> AgendaType.TEAMS_WORK
                            isTeams -> AgendaType.TEAMS_PERSONAL
                            else -> AgendaType.GMAIL_PERSONAL
                        }

                        // Extract Teams meeting URL if present
                        var targetIntent: Intent? = null
                        val teamsUrlRegex = Regex("""(https://teams\.microsoft\.com/l/meetup-join/[^\s"']+)""")
                        val match = teamsUrlRegex.find("$desc $loc")
                        if (match != null) {
                            try {
                                targetIntent = Intent(Intent.ACTION_VIEW, Uri.parse(match.value))
                            } catch (_: Exception) {
                            }
                        }

                        val timeString = formatMeetingTime(begin, end)

                        val subtitleText = when (type) {
                            AgendaType.TEAMS_WORK -> {
                                val label = if (accName.isNotBlank()) accName else if (calDisp.isNotBlank()) calDisp else "Corporate Account"
                                if (targetIntent != null) "$label • Teams call link ready" else label
                            }
                            AgendaType.TEAMS_PERSONAL -> {
                                val label = if (accName.isNotBlank()) accName else "Microsoft Teams"
                                if (targetIntent != null) "$label • Teams call link ready" else label
                            }
                            AgendaType.GMAIL_PERSONAL -> {
                                if (accName.isNotBlank()) accName else if (calDisp.isNotBlank()) calDisp else "Google Calendar"
                            }
                            else -> accName
                        }

                        result.add(
                            AgendaItem(
                                id = "cal_${if (isEnterprise) "ent_" else ""}$begin",
                                timeLabel = timeString,
                                title = title,
                                subtitle = subtitleText,
                                type = type,
                                targetIntent = targetIntent
                            )
                        )
                    }
                }
            } catch (_: Exception) {
            }
        }

        // 1. Query Personal Profile Calendar Instances
        queryCalendarUri(CalendarContract.Instances.CONTENT_URI, false)

        // 2. Query Work Profile Calendar Instances (API 29+ Android Enterprise cross-profile calendar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                queryCalendarUri(CalendarContract.Instances.ENTERPRISE_CONTENT_URI, true)
            } catch (_: Exception) {
            }
        }

        // 3. Also query CalendarContract.Events if Instances returned 0 events
        if (result.count { it.type != AgendaType.MISSED_CALL } == 0) {
            try {
                val eventsCursor = context.contentResolver.query(
                    CalendarContract.Events.CONTENT_URI,
                    arrayOf(
                        CalendarContract.Events._ID,
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND,
                        CalendarContract.Events.CALENDAR_ID,
                        CalendarContract.Events.EVENT_LOCATION,
                        CalendarContract.Events.DESCRIPTION
                    ),
                    "${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTSTART} <= ? AND ${CalendarContract.Events.DELETED} = 0",
                    arrayOf(startWindow.toString(), endWindow.toString()),
                    "${CalendarContract.Events.DTSTART} ASC LIMIT 6"
                )
                eventsCursor?.use {
                    val titleIdx = it.getColumnIndex(CalendarContract.Events.TITLE)
                    val startIdx = it.getColumnIndex(CalendarContract.Events.DTSTART)
                    val endIdx = it.getColumnIndex(CalendarContract.Events.DTEND)
                    val calIdIdx = it.getColumnIndex(CalendarContract.Events.CALENDAR_ID)
                    val locIdx = it.getColumnIndex(CalendarContract.Events.EVENT_LOCATION)
                    val descIdx = it.getColumnIndex(CalendarContract.Events.DESCRIPTION)

                    while (it.moveToNext()) {
                        val title = if (titleIdx >= 0) it.getString(titleIdx) ?: "Meeting" else "Meeting"
                        val begin = if (startIdx >= 0) it.getLong(startIdx) else System.currentTimeMillis()
                        val end = if (endIdx >= 0) it.getLong(endIdx) else begin + 3600_000L
                        val calId = if (calIdIdx >= 0) it.getLong(calIdIdx) else -1L
                        val loc = if (locIdx >= 0) it.getString(locIdx) ?: "" else ""
                        val desc = if (descIdx >= 0) it.getString(descIdx) ?: "" else ""

                        val eventKey = "$title-$begin"
                        if (eventKey in seenEventIds) continue
                        seenEventIds.add(eventKey)

                        val (accName, accType, calDisp) = calendarMeta[calId] ?: Triple("", "", "")
                        val isTeams = accType.contains("microsoft", ignoreCase = true) ||
                                accType.contains("teams", ignoreCase = true) ||
                                accType.contains("office", ignoreCase = true) ||
                                accName.contains("microsoft", ignoreCase = true) ||
                                accName.contains("teams", ignoreCase = true) ||
                                desc.contains("teams.microsoft.com", ignoreCase = true) ||
                                loc.contains("teams.microsoft.com", ignoreCase = true)

                        val isWork = accType.contains("workaccount", ignoreCase = true) ||
                                accName.contains("work", ignoreCase = true) ||
                                accName.contains("corp", ignoreCase = true)

                        val type = when {
                            isTeams && isWork -> AgendaType.TEAMS_WORK
                            isTeams -> AgendaType.TEAMS_PERSONAL
                            else -> AgendaType.GMAIL_PERSONAL
                        }

                        var targetIntent: Intent? = null
                        val teamsUrlRegex = Regex("""(https://teams\.microsoft\.com/l/meetup-join/[^\s"']+)""")
                        val match = teamsUrlRegex.find("$desc $loc")
                        if (match != null) {
                            try {
                                targetIntent = Intent(Intent.ACTION_VIEW, Uri.parse(match.value))
                            } catch (_: Exception) {}
                        }

                        val timeString = formatMeetingTime(begin, end)
                        val subtitleText = if (accName.isNotBlank()) accName else if (calDisp.isNotBlank()) calDisp else "Calendar"

                        result.add(
                            AgendaItem(
                                id = "ev_$begin",
                                timeLabel = timeString,
                                title = title,
                                subtitle = subtitleText,
                                type = type,
                                targetIntent = targetIntent
                            )
                        )
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    // Sort: missed calls first, then meetings chronologically
    return result.sortedWith(compareBy({ it.type != AgendaType.MISSED_CALL }, { it.id }))
}
