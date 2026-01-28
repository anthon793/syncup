package com.syncup.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syncup.presentation.components.SimpleAvatar
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.syncup.presentation.mock.Activity
import com.syncup.presentation.mock.ActivityType
import com.syncup.presentation.mock.MockData

// Teal Color Palette
private val TealPrimary = Color(0xFF00897B)
private val TealDark = Color(0xFF00695C)
private val TealLight = Color(0xFF4DB6AC)
private val BackgroundLight = Color(0xFFF8F9FA)
private val DarkText = Color(0xFF1A1A2E)
private val SecondaryText = Color(0xFF6B7280)
private val LightGray = Color(0xFF9CA3AF)
private val DividerGray = Color(0xFFE5E7EB)
private val SuccessGreen = Color(0xFF22C55E)
private val WarningOrange = Color(0xFFF59E0B)
private val CriticalRed = Color(0xFFEF4444)

@Composable
fun CollaborateScreen(
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    var showPostUpdateDialog by remember { mutableStateOf(false) }
    val activities = remember { mutableStateListOf<Activity>().apply { addAll(MockData.mockActivities) } }
    
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header
            item {
                CollaborateHeader(onMenuClick = onMenuClick)
            }
            
            // File Chips
            item {
                FileChipsRow()
            }
            
            // Today Divider
            item {
                TodayDivider()
            }
            
            // Activities
            items(activities, key = { it.id }) { activity ->
                when (activity.type) {
                    ActivityType.FILE_UPLOAD -> FileUploadActivity(activity)
                    ActivityType.MILESTONE -> MilestoneActivity(activity)
                    ActivityType.COMMENT -> CommentActivity(activity)
                    ActivityType.ALERT -> InactivityAlertCard(activity)
                    ActivityType.BLOCKER -> BlockerActivity(activity)
                    else -> {}
                }
            }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
        
        // FAB
        FloatingActionButton(
            onClick = { showPostUpdateDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = 72.dp),
            containerColor = TealPrimary,
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
        }
        
        // Post Update Dialog
        if (showPostUpdateDialog) {
            PostUpdateDialog(
                onDismiss = { showPostUpdateDialog = false },
                onPost = { message, type ->
                    val newActivity = Activity(
                        id = "activity_${System.currentTimeMillis()}",
                        projectId = "project_1",
                        userId = "user_1",
                        userName = "Alex Student",
                        timestamp = System.currentTimeMillis(),
                        type = type,
                        title = when(type) {
                            ActivityType.COMMENT -> "Posted an update"
                            ActivityType.FILE_UPLOAD -> "Shared a file"
                            else -> message.take(50)
                        },
                        description = message,
                        data = mapOf()
                    )
                    activities.add(0, newActivity)
                    showPostUpdateDialog = false
                }
            )
        }
    }
}

@Composable
fun CollaborateHeader(onMenuClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onMenuClick() },
                tint = DarkText
            )
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Team Pulse",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Text(
                    text = "SyncUp Project",
                    fontSize = 12.sp,
                    color = SecondaryText
                )
            }
            
            SimpleAvatar(
                name = "Alex Rivera",
                size = 40.dp
            )
        }
    }
}

@Composable
fun FileChipsRow() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FileChipEnhanced(
                icon = "📄",
                name = "Specs.pdf",
                color = TealPrimary
            )
        }
        item {
            FileChipEnhanced(
                icon = "🎨",
                name = "Canvas.fig",
                color = TealPrimary
            )
        }
        item {
            FileChipEnhanced(
                icon = "🔗",
                name = "Trello",
                color = TealPrimary
            )
        }
    }
}

@Composable
fun FileChipEnhanced(icon: String, name: String, color: Color) {
    Row(
        modifier = Modifier
            .background(BackgroundLight, RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { },
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(color.copy(alpha = 0.2f), RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 10.sp)
        }
        Text(
            text = name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = DarkText
        )
    }
}

@Composable
fun TodayDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(DividerGray, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "TODAY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = SecondaryText,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun FileUploadActivity(activity: Activity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),  // Light blue
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFF1976D2), CircleShape),  // Blue
                contentAlignment = Alignment.Center
            ) {
                Text("📎", fontSize = 20.sp)
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.userName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "uploaded ${activity.data["fileName"]?.toString() ?: "a file"}",
                    fontSize = 13.sp,
                    color = SecondaryText
                )
            }
            
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF1976D2).copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("📄", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun MilestoneActivity(activity: Activity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),  // Light amber/gold
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // User info row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SimpleAvatar(
                    name = activity.userName,
                    size = 36.dp
                )
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = activity.userName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(
                        text = "10:42 AM",
                        fontSize = 11.sp,
                        color = SecondaryText
                    )
                }
                
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color(0xFFFF8F00), CircleShape),  // Amber
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏆", fontSize = 14.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Milestone Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("🏁", fontSize = 14.sp)
                Text(
                    text = "MILESTONE COMPLETED",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF8F00)  // Amber
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = activity.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = activity.description,
                fontSize = 13.sp,
                color = Color(0xFF666666)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Celebration Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFF8F00),  // Amber
                                Color(0xFFFFC107),  // Yellow
                                Color(0xFFFF8F00)   // Amber
                            )
                        ),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🎉 Great Achievement! 🎉",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun CommentActivity(activity: Activity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),  // Light green
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFF388E3C), CircleShape),  // Green
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = activity.userName.split(" ").map { it.first() }.take(2).joinToString(""),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = activity.userName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(
                        text = "Just now",
                        fontSize = 11.sp,
                        color = SecondaryText
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = activity.description,
                    fontSize = 14.sp,
                    color = DarkText,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun InactivityAlertCard(activity: Activity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WarningOrange.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(WarningOrange.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⏰", fontSize = 22.sp)
                    }
                    
                    Column {
                        Text(
                            text = activity.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = activity.description,
                            fontSize = 13.sp,
                            color = SecondaryText,
                            lineHeight = 18.sp
                        )
                    }
                }
                
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(DividerGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👥", fontSize = 18.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
            ) {
                Text(
                    text = "Send Friendly Nudge",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("⚡", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun BlockerActivity(activity: Activity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Time and user
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "11:15 AM",
                fontSize = 11.sp,
                color = SecondaryText
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = activity.userName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = DarkText
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(DividerGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤", fontSize = 12.sp)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Blocker Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, WarningOrange.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("⚠️", fontSize = 14.sp)
                    Text(
                        text = "ATTENTION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CriticalRed
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = activity.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = activity.description,
                    fontSize = 13.sp,
                    color = SecondaryText
                )
            }
        }
    }
}

// Extension function for random float
private fun ClosedFloatingPointRange<Float>.random(): Float {
    return (Math.random() * (endInclusive - start) + start).toFloat()
}

@Composable
fun QuickPostCard(onPostClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onPostClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SimpleAvatar(name = "Alex Student", size = 36.dp)
            
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(BackgroundLight, RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Share an update with your team...",
                    fontSize = 14.sp,
                    color = SecondaryText
                )
            }
            
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Post",
                tint = TealPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun PostUpdateDialog(
    onDismiss: () -> Unit,
    onPost: (String, ActivityType) -> Unit
) {
    var updateText by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(ActivityType.COMMENT) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onDismiss() },
                    tint = DarkText
                )
                Text(
                    text = "Post Update",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Spacer(modifier = Modifier.size(24.dp))
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // User info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SimpleAvatar(name = "Alex Student", size = 40.dp)
                    Column {
                        Text(
                            text = "Alex Student",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "Posting to Team Pulse",
                            fontSize = 12.sp,
                            color = SecondaryText
                        )
                    }
                }
                
                // Update type selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    UpdateTypeChip(
                        text = "💬 Comment",
                        isSelected = selectedType == ActivityType.COMMENT,
                        onClick = { selectedType = ActivityType.COMMENT }
                    )
                    UpdateTypeChip(
                        text = "📎 File",
                        isSelected = selectedType == ActivityType.FILE_UPLOAD,
                        onClick = { selectedType = ActivityType.FILE_UPLOAD }
                    )
                    UpdateTypeChip(
                        text = "🏁 Milestone",
                        isSelected = selectedType == ActivityType.MILESTONE,
                        onClick = { selectedType = ActivityType.MILESTONE }
                    )
                }
                
                // Text input
                OutlinedTextField(
                    value = updateText,
                    onValueChange = { updateText = it },
                    placeholder = { 
                        Text(
                            text = when(selectedType) {
                                ActivityType.COMMENT -> "What's on your mind?"
                                ActivityType.FILE_UPLOAD -> "Describe the file you're sharing..."
                                ActivityType.MILESTONE -> "Describe your milestone achievement..."
                                else -> "Write your update..."
                            },
                            color = SecondaryText
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = DarkText,
                        unfocusedTextColor = DarkText,
                        cursorColor = TealPrimary,
                        focusedBorderColor = TealPrimary,
                        unfocusedBorderColor = DividerGray,
                        focusedContainerColor = BackgroundLight,
                        unfocusedContainerColor = BackgroundLight
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (updateText.isNotBlank()) {
                        onPost(updateText, selectedType)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                enabled = updateText.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Post Update",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    )
}

@Composable
fun UpdateTypeChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) TealLight.copy(alpha = 0.15f) else BackgroundLight)
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) TealPrimary else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) TealPrimary else DarkText
        )
    }
}
