package com.example.minimals25

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Data model for installed app
data class AppEntry(
    val label: String,
    val packageName: String,
    val activityName: String
)

// Data model for pinned monochrome favorite app
data class PinnedApp(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val intentAction: String? = null,
    val candidatePackages: List<String> = emptyList()
)

val PINNED_FAVORITES = listOf(
    PinnedApp(
        id = "phone",
        label = "Phone",
        icon = Icons.Rounded.Phone,
        intentAction = Intent.ACTION_DIAL,
        candidatePackages = listOf("com.samsung.android.dialer", "com.google.android.dialer")
    ),
    PinnedApp(
        id = "messages",
        label = "Messages",
        icon = Icons.Rounded.ChatBubbleOutline,
        intentAction = Intent.ACTION_MAIN,
        candidatePackages = listOf("com.samsung.android.messaging", "com.google.android.apps.messaging")
    ),
    PinnedApp(
        id = "whatsapp",
        label = "WhatsApp",
        icon = Icons.Rounded.ChatBubbleOutline,
        candidatePackages = listOf("com.whatsapp", "com.whatsapp.w4b")
    ),
    PinnedApp(
        id = "camera",
        label = "Camera",
        icon = Icons.Rounded.CameraAlt,
        intentAction = MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA,
        candidatePackages = listOf("com.sec.android.app.camera", "com.google.android.GoogleCamera")
    ),
    PinnedApp(
        id = "browser",
        label = "Browser",
        icon = Icons.Rounded.Language,
        intentAction = Intent.ACTION_VIEW,
        candidatePackages = listOf("com.android.chrome", "com.sec.android.app.sbrowser")
    ),
    PinnedApp(
        id = "gallery",
        label = "Photos",
        icon = Icons.Rounded.PhotoLibrary,
        candidatePackages = listOf("com.sec.android.gallery3d", "com.google.android.apps.photos")
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    background = Color.Black,
                    surface = Color(0xFF0A0A0A),
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
    var isDrawerOpen by remember { mutableStateOf(false) }
    var installedApps by remember { mutableStateOf<List<AppEntry>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    // Live Clock & Date
    var timeString by remember { mutableStateOf("") }
    var dateString by remember { mutableStateOf("") }

    // Live Battery & Charging Status
    var batteryPct by remember { mutableIntStateOf(100) }
    var isCharging by remember { mutableStateOf(false) }

    // Screen Time Today
    var screenTimeText by remember { mutableStateOf("Calculating...") }
    var hasUsagePermission by remember { mutableStateOf(checkUsageStatsPermission(context)) }

    // Gesture cumulative drag tracking
    var totalDragX by remember { mutableFloatStateOf(0f) }
    var totalDragY by remember { mutableFloatStateOf(0f) }

    // Load installed apps
    LaunchedEffect(Unit) {
        installedApps = loadInstalledApps(context)
    }

    // Update Clock every second
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

    // Monitor Battery via BroadcastReceiver
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
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    // Calculate Today's Screen Time
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

    BackHandler(enabled = isDrawerOpen) {
        isDrawerOpen = false
        searchQuery = ""
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
                .pointerInput(isDrawerOpen) {
                    if (!isDrawerOpen) {
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
                                // Swipe Up -> Open App Drawer
                                if (totalDragY < -100f && kotlin.math.abs(totalDragY) > kotlin.math.abs(totalDragX)) {
                                    isDrawerOpen = true
                                }
                                // Swipe Right -> Quick launch Phone / Dialer
                                else if (totalDragX > 120f && kotlin.math.abs(totalDragX) > kotlin.math.abs(totalDragY)) {
                                    launchPhoneDialer(context)
                                }
                                // Swipe Left -> Quick launch Camera
                                else if (totalDragX < -120f && kotlin.math.abs(totalDragX) > kotlin.math.abs(totalDragY)) {
                                    launchCamera(context)
                                }
                            }
                        )
                    }
                }
        ) {
            // Main Home Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Section: Time, Date, Battery, Screen Time Detox Badge
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    // System Status Row: Battery & Settings Shortcut
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

                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = "Settings",
                            tint = Color(0xFF757575),
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    context.startActivity(Intent(Settings.ACTION_SETTINGS))
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Large Digital Clock
                    Text(
                        text = timeString,
                        color = Color.White,
                        fontSize = 72.sp,
                        fontWeight = FontWeight.ExtraLight,
                        letterSpacing = (-2).sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Full Date
                    Text(
                        text = dateString,
                        color = Color(0xFFAAAAAA),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Screen Time / Digital Detox Badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF111111))
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
                            tint = Color(0xFFE0E0E0),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = screenTimeText,
                            color = Color(0xFFE0E0E0),
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Middle/Center Section: Pinned Monochrome Favorite Icons Grid
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val rows = PINNED_FAVORITES.chunked(3)
                    rows.forEach { rowApps ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            rowApps.forEach { pinned ->
                                MonochromeAppItem(
                                    app = pinned,
                                    onClick = { launchPinnedApp(context, pinned) }
                                )
                            }
                        }
                    }
                }

                // Bottom Gesture Hint: Swipe Up for Drawer
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
                        tint = Color(0xFF616161),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "SWIPE UP FOR APPS",
                        color = Color(0xFF616161),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.8.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "◀ Camera   •   Dialer ▶",
                        color = Color(0xFF424242),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Searchable All-Apps Drawer Sheet (Slides Up)
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

@Composable
private fun MonochromeAppItem(
    app: PinnedApp,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0xFF0F0F0F))
                .border(1.dp, Color(0xFF262626), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = app.icon,
                contentDescription = app.label,
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = app.label,
            color = Color(0xFFE0E0E0),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
    }
}

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
            // Search Bar Header
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

            // App Counter Header
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

            // Alphabetical All Apps List
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
                        // Monochrome initial letter icon badge
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

// ---------------- Helper Functions ----------------

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

private fun launchPinnedApp(context: Context, pinned: PinnedApp) {
    // 1. Try explicit candidate packages first
    for (pkg in pinned.candidatePackages) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(pkg)
        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launchIntent)
            return
        }
    }

    // 2. Try generic intent action
    if (pinned.intentAction != null) {
        try {
            val intent = Intent(pinned.intentAction).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            return
        } catch (_: Exception) {
        }
    }

    Toast.makeText(context, "${pinned.label} app not found", Toast.LENGTH_SHORT).show()
}

private fun launchPhoneDialer(context: Context) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, "Dialer unavailable", Toast.LENGTH_SHORT).show()
    }
}

private fun launchCamera(context: Context) {
    try {
        val intent = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, "Camera unavailable", Toast.LENGTH_SHORT).show()
    }
}

private fun checkUsageStatsPermission(context: Context): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager ?: return false
    val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        appOps.unsafeCheckOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
    } else {
        @Suppress("DEPRECATION")
        appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
    }
    return mode == AppOpsManager.MODE_ALLOWED
}

private fun computeTodayScreenTime(context: Context): String {
    return try {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
                ?: return "--"

        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startTime = cal.timeInMillis
        val endTime = System.currentTimeMillis()

        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        )

        var totalMs = 0L
        for (item in stats) {
            totalMs += item.totalTimeInForeground
        }

        val totalMinutes = totalMs / (1000 * 60)
        val hours = totalMinutes / 60
        val mins = totalMinutes % 60

        if (hours > 0) {
            "${hours}h ${mins}m today"
        } else {
            "${mins}m today"
        }
    } catch (_: Exception) {
        "Usage access needed"
    }
}
