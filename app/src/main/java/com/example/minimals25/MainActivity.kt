package com.example.minimals25

import android.Manifest
import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.CallMissed
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// ---------------- Data Models ----------------

enum class AgendaType {
    TEAMS_WORK,
    GMAIL_PERSONAL,
    MISSED_CALL
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
    val icon: ImageVector,
    val candidatePackages: List<String>,
    val fallbackIntentAction: String? = null
)

data class AppEntry(
    val label: String,
    val packageName: String,
    val activityName: String
)

val AVAILABLE_HOME_APPS = listOf(
    AppChoice("whatsapp", "WhatsApp", Icons.Rounded.ChatBubbleOutline, listOf("com.whatsapp", "com.whatsapp.w4b")),
    AppChoice("instagram", "Instagram", Icons.Rounded.PhotoCamera, listOf("com.instagram.android")),
    AppChoice("telegram", "Telegram", Icons.Rounded.Send, listOf("org.telegram.messenger", "org.telegram.messenger.web")),
    AppChoice("maps", "Maps", Icons.Rounded.Map, listOf("com.google.android.apps.maps")),
    AppChoice("phone", "Phone", Icons.Rounded.Phone, listOf("com.samsung.android.dialer", "com.google.android.dialer"), Intent.ACTION_DIAL),
    AppChoice("messages", "Messages", Icons.Rounded.Email, listOf("com.samsung.android.messaging", "com.google.android.apps.messaging")),
    AppChoice("camera", "Camera", Icons.Rounded.CameraAlt, listOf("com.sec.android.app.camera", "com.google.android.GoogleCamera"), MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA),
    AppChoice("chrome", "Browser", Icons.Rounded.Language, listOf("com.android.chrome", "com.sec.android.app.sbrowser")),
    AppChoice("teams", "Teams", Icons.Rounded.Groups, listOf("com.microsoft.teams")),
    AppChoice("gmail", "Gmail", Icons.Rounded.Email, listOf("com.google.android.gm")),
    AppChoice("photos", "Photos", Icons.Rounded.PhotoLibrary, listOf("com.sec.android.gallery3d", "com.google.android.apps.photos"))
)

val DEFAULT_SELECTED_APP_IDS = setOf("maps", "whatsapp", "instagram", "telegram", "phone", "messages")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    background = Color.Black,
                    surface = Color(0xFF090909),
                    onBackground = Color.White,
                    onSurface = Color.White
                )
            ) {
                MinimalS25LauncherScreen()
            }
        }
    }
}

@Composable
fun MinimalS25LauncherScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("s25_launcher_prefs", Context.MODE_PRIVATE) }

    var isDrawerOpen by remember { mutableStateOf(false) }
    var isSettingsOpen by remember { mutableStateOf(false) }
    var installedApps by remember { mutableStateOf<List<AppEntry>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    // User Configuration from Settings
    var selectedAppIds by remember {
        mutableStateOf(prefs.getStringSet("home_apps", DEFAULT_SELECTED_APP_IDS) ?: DEFAULT_SELECTED_APP_IDS)
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

    // Screen Time Today
    var screenTimeText by remember { mutableStateOf("Calculating...") }
    var hasUsagePermission by remember { mutableStateOf(checkUsageStatsPermission(context)) }

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

    // Gesture drag tracking
    var totalDragX by remember { mutableFloatStateOf(0f) }
    var totalDragY by remember { mutableFloatStateOf(0f) }

    // Load initial data
    LaunchedEffect(Unit) {
        installedApps = loadInstalledApps(context)
        agendaList = loadAgendaAndMissedCalls(context)
    }

    // Refresh notification access status
    LaunchedEffect(isSettingsOpen) {
        isNotificationAccessGranted = S25NotificationService.isNotificationAccessGranted(context)
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

    BackHandler(enabled = isDrawerOpen || isSettingsOpen) {
        if (isDrawerOpen) {
            isDrawerOpen = false
            searchQuery = ""
        } else if (isSettingsOpen) {
            isSettingsOpen = false
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
                .statusBarsPadding()
                .pointerInput(isDrawerOpen || isSettingsOpen) {
                    if (!isDrawerOpen && !isSettingsOpen) {
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
                                if (totalDragY < -100f && kotlin.math.abs(totalDragY) > kotlin.math.abs(totalDragX)) {
                                    isDrawerOpen = true
                                } else if (totalDragX > 120f && kotlin.math.abs(totalDragX) > kotlin.math.abs(totalDragY)) {
                                    launchDialer(context)
                                } else if (totalDragX < -120f && kotlin.math.abs(totalDragX) > kotlin.math.abs(totalDragY)) {
                                    launchCameraApp(context)
                                }
                            }
                        )
                    }
                }
        ) {
            // Main Scrollable 3-Section Home Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                // Top Status Header: Battery, Settings Icon, Clock & Screen Time
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.BatteryChargingFull,
                                contentDescription = "Battery",
                                tint = if (isCharging) Color(0xFF39FF14) else Color(0xFF9E9E9E),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "$batteryPct%${if (isCharging) " ⚡" else ""}",
                                color = Color(0xFFBDBDBD),
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Settings Icon (Opens Home Screen Settings Menu)
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { isSettingsOpen = true }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Customize",
                                color = Color(0xFF888888),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                contentDescription = "Settings",
                                tint = Color(0xFFCCCCCC),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = timeString,
                        color = Color.White,
                        fontSize = 58.sp,
                        fontWeight = FontWeight.ExtraLight,
                        letterSpacing = (-1.5).sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = dateString,
                        color = Color(0xFFAAAAAA),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Screen time pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF0F0F0F))
                            .border(1.dp, Color(0xFF222222), RoundedCornerShape(20.dp))
                            .clickable {
                                if (!hasUsagePermission) {
                                    context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                                    hasUsagePermission = checkUsageStatsPermission(context)
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.HourglassEmpty,
                            contentDescription = "Screen Time",
                            tint = Color(0xFFB0B0B0),
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = screenTimeText,
                            color = Color(0xFFB0B0B0),
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // ========================================================
                // SECTION 1: APPS (CUSTOMIZED IN SETTINGS)
                // ========================================================
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "FAVORITES",
                        color = Color(0xFF666666),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.4.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    val activeApps = AVAILABLE_HOME_APPS.filter { it.id in selectedAppIds }
                    val rows = activeApps.chunked(4)

                    rows.forEach { rowApps ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            rowApps.forEach { app ->
                                MonochromeAppItem(
                                    app = app,
                                    onClick = { launchAppChoice(context, app) }
                                )
                            }
                        }
                    }
                }

                // ========================================================
                // SECTION 2: ROLLING NOTIFICATIONS (SCROLLABLE SECTION)
                // ========================================================
                if (showNotificationsSection) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "NOTIFICATIONS",
                                color = Color(0xFF666666),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.4.sp,
                                fontWeight = FontWeight.Bold
                            )

                            if (!isNotificationAccessGranted) {
                                Text(
                                    text = "Enable Access",
                                    color = Color(0xFF38BDF8),
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.clickable {
                                        context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val displayNotifs = if (incomingNotifications.isNotEmpty()) {
                            incomingNotifications
                        } else {
                            // Minimal sample rolling notifications when listening or idle
                            listOf(
                                S25NotificationItem("n1", "com.whatsapp", "WhatsApp", "David", "Hey, are we still meeting at 4 PM?", System.currentTimeMillis() - 120_000L),
                                S25NotificationItem("n2", "org.telegram.messenger", "Telegram", "Engineering Group", "New build artifacts are uploaded", System.currentTimeMillis() - 480_000L),
                                S25NotificationItem("n3", "com.instagram.android", "Instagram", "alex_photos", "shared a reel with you", System.currentTimeMillis() - 900_000L)
                            )
                        }

                        // Horizontal Scrollable Rolling Ticker
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(vertical = 2.dp)
                        ) {
                            items(displayNotifs, key = { it.key }) { notif ->
                                RollingNotificationCard(
                                    notif = notif,
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

                // ========================================================
                // SECTION 3: MEETINGS & REMINDERS (TEAMS, GMAIL & MISSED CALLS)
                // ========================================================
                if (showAgendaSection) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AGENDA & MISSED CALLS",
                                color = Color(0xFF666666),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.4.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "${agendaList.size} ITEMS",
                                color = Color(0xFF444444),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Minimal List Mode
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            agendaList.forEach { item ->
                                MinimalAgendaRow(
                                    item = item,
                                    onClick = {
                                        if (item.targetIntent != null) {
                                            try {
                                                context.startActivity(item.targetIntent)
                                            } catch (_: Exception) {
                                            }
                                        } else if (item.type == AgendaType.TEAMS_WORK) {
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

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Hint
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
                        tint = Color(0xFF555555),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "SWIPE UP FOR ALL APPS",
                        color = Color(0xFF555555),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
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
                    selectedAppIds = selectedAppIds,
                    showNotifications = showNotificationsSection,
                    showAgenda = showAgendaSection,
                    isNotificationAccessGranted = isNotificationAccessGranted,
                    onToggleApp = { appId ->
                        val updated = if (appId in selectedAppIds) {
                            if (selectedAppIds.size > 1) selectedAppIds - appId else selectedAppIds
                        } else {
                            selectedAppIds + appId
                        }
                        selectedAppIds = updated
                        prefs.edit().putStringSet("home_apps", updated).apply()
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
                    onQueryChange = { searchQuery = it },
                    onAppClick = { app ->
                        isDrawerOpen = false
                        searchQuery = ""
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

// ---------------- Component Views ----------------

@Composable
private fun MonochromeAppItem(
    app: AppChoice,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(Color(0xFF0E0E0E))
                .border(1.dp, Color(0xFF262626), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = app.icon,
                contentDescription = app.label,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = app.label,
            color = Color(0xFFE0E0E0),
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RollingNotificationCard(
    notif: S25NotificationItem,
    onClick: () -> Unit
) {
    val timeAgo = remember(notif.timestamp) {
        val diffMin = Math.max(1, (System.currentTimeMillis() - notif.timestamp) / 60_000L)
        if (diffMin < 60) "${diffMin}m ago" else "${diffMin / 60}h ago"
    }

    Box(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0D0D0D))
            .border(1.dp, Color(0xFF242424), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = notif.appName,
                    color = Color(0xFF38BDF8),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = timeAgo,
                    color = Color(0xFF666666),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (notif.title.isNotBlank()) {
                Text(
                    text = notif.title,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = if (notif.text.isNotBlank()) notif.text else notif.title,
                color = Color(0xFF9E9E9E),
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MinimalAgendaRow(
    item: AgendaItem,
    onClick: () -> Unit
) {
    val (badgeColor, badgeText) = when (item.type) {
        AgendaType.TEAMS_WORK -> Color(0xFF38BDF8) to "Teams Work"
        AgendaType.GMAIL_PERSONAL -> Color(0xFFFBBF24) to "Personal / Gmail"
        AgendaType.MISSED_CALL -> Color(0xFFEF4444) to "Missed Call"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF0C0C0C))
            .border(1.dp, Color(0xFF1E1E1E), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = item.timeLabel,
                color = Color(0xFFAAAAAA),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )

            Column {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (item.subtitle.isNotBlank()) {
                    Text(
                        text = item.subtitle,
                        color = Color(0xFF757575),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Source Tag Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(badgeColor.copy(alpha = 0.15f))
                .border(1.dp, badgeColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                .padding(horizontal = 7.dp, vertical = 3.dp)
        ) {
            Text(
                text = badgeText,
                color = badgeColor,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ---------------- SETTINGS MENU OVERLAY ----------------

@Composable
private fun HomeScreenSettingsMenu(
    selectedAppIds: Set<String>,
    showNotifications: Boolean,
    showAgenda: Boolean,
    isNotificationAccessGranted: Boolean,
    onToggleApp: (String) -> Unit,
    onToggleNotifications: (Boolean) -> Unit,
    onToggleAgenda: (Boolean) -> Unit,
    onRequestNotificationAccess: () -> Unit,
    onRequestCalendarAndCallLog: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp)
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
                        text = "HOME SETTINGS",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Customize icons, notifications & meetings",
                        color = Color(0xFF777777),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Text(
                    text = "DONE",
                    color = Color(0xFF38BDF8),
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onClose)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // SECTION 1 CUSTOMIZATION: CHOOSE HOME APPS
            Text(
                text = "1. CHOOSE HOME SCREEN ICONS",
                color = Color(0xFF9E9E9E),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Select which minimal apps appear on your main screen:",
                color = Color(0xFF616161),
                fontSize = 11.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F0F0F))
                    .border(1.dp, Color(0xFF262626), RoundedCornerShape(12.dp))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AVAILABLE_HOME_APPS.forEach { app ->
                    val isChecked = app.id in selectedAppIds
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onToggleApp(app.id) }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = app.icon,
                                contentDescription = app.label,
                                tint = if (isChecked) Color.White else Color(0xFF666666),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = app.label,
                                color = if (isChecked) Color.White else Color(0xFF888888),
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }

                        Icon(
                            imageVector = if (isChecked) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                            contentDescription = if (isChecked) "Selected" else "Unselected",
                            tint = if (isChecked) Color(0xFF38BDF8) else Color(0xFF444444),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 2 CUSTOMIZATION: ROLLING NOTIFICATIONS
            Text(
                text = "2. ROLLING NOTIFICATIONS SECTION",
                color = Color(0xFF9E9E9E),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F0F0F))
                    .border(1.dp, Color(0xFF262626), RoundedCornerShape(12.dp))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Show Rolling Notifications",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Incoming alerts displayed in a clean scrollable ticker",
                        color = Color(0xFF777777),
                        fontSize = 11.sp
                    )
                }

                Switch(
                    checked = showNotifications,
                    onCheckedChange = onToggleNotifications,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF38BDF8),
                        uncheckedThumbColor = Color(0xFF666666),
                        uncheckedTrackColor = Color(0xFF222222)
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Notification Access Permission Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF141414))
                    .border(1.dp, Color(0xFF2A2A2A), RoundedCornerShape(10.dp))
                    .clickable(onClick = onRequestNotificationAccess)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notification Access Permission",
                    color = Color(0xFFCCCCCC),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = if (isNotificationAccessGranted) "GRANTED ✓" else "GRANT PERMISSION",
                    color = if (isNotificationAccessGranted) Color(0xFF39FF14) else Color(0xFFFBBF24),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SECTION 3 CUSTOMIZATION: MEETINGS & REMINDERS (TEAMS, PERSONAL, GMAIL, MISSED CALLS)
            Text(
                text = "3. AGENDA, TEAMS, GMAIL & MISSED CALLS",
                color = Color(0xFF9E9E9E),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F0F0F))
                    .border(1.dp, Color(0xFF262626), RoundedCornerShape(12.dp))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Show Unified Agenda List",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Teams (Work), Personal / Gmail meetings & Missed Calls in minimal list mode",
                        color = Color(0xFF777777),
                        fontSize = 11.sp
                    )
                }

                Switch(
                    checked = showAgenda,
                    onCheckedChange = onToggleAgenda,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF38BDF8),
                        uncheckedThumbColor = Color(0xFF666666),
                        uncheckedTrackColor = Color(0xFF222222)
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Calendar & Call Log Permission
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF141414))
                    .border(1.dp, Color(0xFF2A2A2A), RoundedCornerShape(10.dp))
                    .clickable(onClick = onRequestCalendarAndCallLog)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Calendar & Call Log Sync",
                    color = Color(0xFFCCCCCC),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "SYNC PERMISSION",
                    color = Color(0xFF38BDF8),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

// ---------------- ALL-APPS DRAWER ----------------

@Composable
private fun AppDrawerSheet(
    apps: List<AppEntry>,
    query: String,
    onQueryChange: (String) -> Unit,
    onAppClick: (AppEntry) -> Unit,
    onClose: () -> Unit
) {
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
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF121212))
                    .border(1.dp, Color(0xFF2A2A2A), RoundedCornerShape(14.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search apps...",
                            color = Color(0xFF616161),
                            fontSize = 15.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.Monospace
                        ),
                        cursorBrush = SolidColor(Color.White),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "Clear",
                        tint = Color(0xFF9E9E9E),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onQueryChange("") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredApps.size} APPS",
                    color = Color(0xFF757575),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.2.sp
                )

                Text(
                    text = "CLOSE",
                    color = Color(0xFFBDBDBD),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.clickable(onClick = onClose)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(filteredApps, key = { it.packageName + it.activityName }) { app ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onAppClick(app) }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF141414))
                                .border(1.dp, Color(0xFF2B2B2B), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = app.label.take(1).uppercase(Locale.getDefault()),
                                color = Color.White,
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = app.label,
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = app.packageName,
                                color = Color(0xFF555555),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
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

private fun loadAgendaAndMissedCalls(context: Context): List<AgendaItem> {
    val result = mutableListOf<AgendaItem>()

    // 1. Try querying real Missed Calls if READ_CALL_LOG is granted
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
                            timeLabel = "📞 Missed",
                            title = titleName,
                            subtitle = "Missed call • $timeStr",
                            type = AgendaType.MISSED_CALL,
                            targetIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
                        )
                    )
                }
            }
        } catch (_: Exception) {
        }
    }

    // 2. Try querying Android Calendar Contract (Teams & Gmail sync into Calendar) if READ_CALENDAR is granted
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
        try {
            val startOfDay = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
            }.timeInMillis

            val endOfDay = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
            }.timeInMillis

            val uri = CalendarContract.Instances.CONTENT_URI.buildUpon()
                .appendPath(startOfDay.toString())
                .appendPath(endOfDay.toString())
                .build()

            val cursor = context.contentResolver.query(
                uri,
                arrayOf(CalendarContract.Instances.TITLE, CalendarContract.Instances.BEGIN, CalendarContract.Instances.ACCOUNT_NAME),
                null,
                null,
                "${CalendarContract.Instances.BEGIN} ASC LIMIT 4"
            )

            val timeFmt = SimpleDateFormat("h:mm a", Locale.getDefault())
            cursor?.use {
                while (it.moveToNext()) {
                    val title = it.getString(0) ?: "Meeting"
                    val begin = it.getLong(1)
                    val account = it.getString(2) ?: ""

                    val isTeams = account.contains("microsoft", ignoreCase = true) ||
                            account.contains("teams", ignoreCase = true) ||
                            account.contains("work", ignoreCase = true)

                    val type = if (isTeams) AgendaType.TEAMS_WORK else AgendaType.GMAIL_PERSONAL

                    result.add(
                        AgendaItem(
                            id = "cal_$begin",
                            timeLabel = timeFmt.format(Date(begin)),
                            title = title,
                            subtitle = account,
                            type = type
                        )
                    )
                }
            }
        } catch (_: Exception) {
        }
    }

    // Fallback minimal demo entries if calendar/call log is empty or permissions pending
    if (result.isEmpty()) {
        result.addAll(
            listOf(
                AgendaItem(
                    id = "demo_teams_1",
                    timeLabel = "10:30 AM",
                    title = "Weekly Core Team Sync",
                    subtitle = "Microsoft Teams • General Channel",
                    type = AgendaType.TEAMS_WORK
                ),
                AgendaItem(
                    id = "demo_teams_2",
                    timeLabel = "02:00 PM",
                    title = "S25 Launcher Architecture Review",
                    subtitle = "Microsoft Teams • Work Call",
                    type = AgendaType.TEAMS_WORK
                ),
                AgendaItem(
                    id = "demo_gmail_1",
                    timeLabel = "04:30 PM",
                    title = "Doctor Appointment & Prescription",
                    subtitle = "Personal Calendar • Gmail",
                    type = AgendaType.GMAIL_PERSONAL
                ),
                AgendaItem(
                    id = "demo_call_1",
                    timeLabel = "📞 Missed",
                    title = "Alex Turner",
                    subtitle = "Missed call • 14m ago",
                    type = AgendaType.MISSED_CALL
                )
            )
        )
    }

    return result
}
