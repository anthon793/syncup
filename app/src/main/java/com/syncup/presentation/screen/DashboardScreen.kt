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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syncup.presentation.components.SimpleAvatar
import com.syncup.presentation.components.AvatarStack
import com.syncup.presentation.mock.FocusProject
import com.syncup.presentation.mock.MockData

// Teal Color Palette
private val TealPrimary = Color(0xFF00897B)
private val TealDark = Color(0xFF00695C)
private val TealLight = Color(0xFF4DB6AC)
private val BackgroundLight = Color(0xFFF8F9FA)
private val SurfaceWhite = Color(0xFFFFFFFF)
private val DarkText = Color(0xFF1A1A2E)
private val SecondaryText = Color(0xFF6B7280)
private val LightGray = Color(0xFF9CA3AF)
private val DividerGray = Color(0xFFE5E7EB)
private val SuccessGreen = Color(0xFF22C55E)
private val WarningOrange = Color(0xFFF59E0B)
private val CriticalRed = Color(0xFFEF4444)

@Composable
fun DashboardScreen(
    onProjectClick: (String) -> Unit = {},
    onInviteClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val teammates = MockData.mockTeamMembers.take(4)
    val focusProjects = MockData.focusProjects
    val priorityProject = MockData.priorityProject
    var showQuickActionsMenu by remember { mutableStateOf(false) }
    var showNotifications by remember { mutableStateOf(false) }
    
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
                DashboardHeader(
                    onProfileClick = onProfileClick,
                    onMenuClick = onMenuClick,
                    onNotificationClick = { showNotifications = !showNotifications }
                )
            }
            
            // Notifications Panel (expandable)
            if (showNotifications) {
                item {
                    NotificationsPanel(
                        onDismiss = { showNotifications = false }
                    )
                }
            }
            
            // Priority Project Card
            item {
                PriorityProjectCard(
                    projectName = priorityProject.projectName,
                    nextMilestone = priorityProject.nextMilestone,
                    hoursRemaining = priorityProject.hoursRemaining,
                    memberCount = priorityProject.totalMembers,
                    memberNames = teammates.map { it.name },
                    onClick = { onProjectClick(priorityProject.projectId) }
                )
            }
            
            // Active Teammates
            item {
                ActiveTeammatesSection(
                    teammates = teammates,
                    onlineCount = teammates.count { it.isOnline },
                    onInviteClick = onInviteClick
                )
            }
            
            // Focus Projects Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Focus Projects",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    
                    Text(
                        text = "VIEW ALL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary,
                        modifier = Modifier.clickable { }
                    )
                }
            }
            
            // Focus Projects Row
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(focusProjects) { project ->
                        FocusProjectCard(
                            project = project,
                            onClick = { onProjectClick(project.id) }
                        )
                    }
                }
            }
            
            // Bottom spacer for nav bar
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
        
        // FAB with Quick Actions
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = 72.dp)
        ) {
            // Quick Actions Menu
            androidx.compose.animation.AnimatedVisibility(
                visible = showQuickActionsMenu,
                enter = androidx.compose.animation.fadeIn() + androidx.compose.animation.scaleIn(),
                exit = androidx.compose.animation.fadeOut() + androidx.compose.animation.scaleOut()
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 64.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    QuickActionFab(
                        icon = Icons.Default.Add,
                        text = "New Task",
                        onClick = { showQuickActionsMenu = false }
                    )
                    QuickActionFab(
                        icon = Icons.Default.Person,
                        text = "Invite Member",
                        onClick = { 
                            showQuickActionsMenu = false
                            onInviteClick()
                        }
                    )
                    QuickActionFab(
                        icon = Icons.Default.Email,
                        text = "Send Update",
                        onClick = { showQuickActionsMenu = false }
                    )
                }
            }
            
            FloatingActionButton(
                onClick = { showQuickActionsMenu = !showQuickActionsMenu },
                containerColor = TealPrimary,
                shape = CircleShape
            ) {
                Icon(
                imageVector = if (showQuickActionsMenu) Icons.Default.Close else Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }
        }
    }
}

@Composable
fun QuickActionFab(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
        
        SmallFloatingActionButton(
            onClick = onClick,
            containerColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = TealPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun NotificationsPanel(onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notifications",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = SecondaryText,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onDismiss() }
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            NotificationItem(
                icon = "🎯",
                title = "Task Due Soon",
                message = "Finalize Physics Research - Due in 3 hours",
                time = "Just now",
                isUrgent = true
            )
            
            NotificationItem(
                icon = "💬",
                title = "New Comment",
                message = "Sarah commented on your design",
                time = "5 min ago",
                isUrgent = false
            )
            
            NotificationItem(
                icon = "✅",
                title = "Task Completed",
                message = "Jamie marked 'UI Audit' as done",
                time = "1 hour ago",
                isUrgent = false
            )
        }
    }
}

@Composable
fun NotificationItem(
    icon: String,
    title: String,
    message: String,
    time: String,
    isUrgent: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    if (isUrgent) Color(0xFFFEE2E2) else BackgroundLight,
                    RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 16.sp)
        }
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
            Text(
                text = message,
                fontSize = 12.sp,
                color = SecondaryText
            )
        }
        
        Text(
            text = time,
            fontSize = 11.sp,
            color = if (isUrgent) WarningOrange else SecondaryText
        )
    }
}

@Composable
fun DashboardHeader(
    onProfileClick: () -> Unit,
    onMenuClick: () -> Unit,
    onNotificationClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Menu Button
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = DarkText
                )
            }
            
            // Profile Picture with SVG Avatar
            SimpleAvatar(
                name = "Alex Rivera",
                size = 48.dp,
                showBorder = true,
                modifier = Modifier.clickable { onProfileClick() }
            )
            
            Column {
                Text(
                    text = "THE LEAD",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = SecondaryText,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Alex Student",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }
        }
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = DarkText
                )
            }
            
            Box {
                IconButton(onClick = onNotificationClick) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = DarkText
                    )
                }
                // Notification badge
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(TealPrimary, CircleShape)
                        .align(Alignment.TopEnd)
                        .offset(x = (-4).dp, y = 8.dp)
                )
            }
        }
    }
}

@Composable
fun PriorityProjectCard(
    projectName: String,
    nextMilestone: String,
    hoursRemaining: Int,
    memberCount: Int,
    memberNames: List<String>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Time Crunch Badge
                Box(
                    modifier = Modifier
                        .background(BackgroundLight, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "TIME-CRUNCH",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = projectName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Next Major Milestone: $nextMilestone",
                    fontSize = 13.sp,
                    color = SecondaryText
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Member avatars using SVG avatars
                AvatarStack(
                    names = memberNames,
                    maxDisplay = 3,
                    avatarSize = 32.dp
                )
            }
            
            // Circular Timer
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background circle
                CircularProgressIndicator(
                    progress = 1f,
                    modifier = Modifier.size(100.dp),
                    color = Color(0xFFE0E0E0),
                    strokeWidth = 8.dp,
                    strokeCap = StrokeCap.Round
                )
                
                // Progress arc (outer)
                CircularProgressIndicator(
                    progress = 0.75f, // 48 hours of remaining time visualization
                    modifier = Modifier.size(100.dp),
                    color = TealPrimary,
                    strokeWidth = 8.dp,
                    strokeCap = StrokeCap.Round
                )
                
                // Inner dark circle with hours
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(TealDark),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$hoursRemaining",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "HOURS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.8f),
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveTeammatesSection(
    teammates: List<com.syncup.presentation.mock.TeamMember>,
    onlineCount: Int,
    onInviteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ACTIVE TEAMMATES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SecondaryText,
                letterSpacing = 1.sp
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(SuccessGreen, CircleShape)
                )
                Text(
                    text = "$onlineCount ONLINE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(teammates) { teammate ->
                TeammateAvatarEnhanced(
                    name = teammate.name,
                    isOnline = teammate.isOnline,
                    isEditing = teammate.name == "Sarah"
                )
            }
            
            item {
                InviteAvatarButton(onClick = onInviteClick)
            }
        }
    }
}

@Composable
fun TeammateAvatarEnhanced(name: String, isOnline: Boolean, isEditing: Boolean = false) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            SimpleAvatar(
                name = name,
                size = 56.dp,
                isOnline = false // We'll handle the indicator separately
            )
            
            // Online indicator with pencil for active editing
            if (isOnline && isEditing) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(TealLight)
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editing",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            } else if (isOnline) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(SuccessGreen)
                        .border(2.dp, Color.White, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = name.split(" ").first(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = DarkText
        )
    }
}

@Composable
fun InviteAvatarButton(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .border(2.dp, DividerGray, CircleShape)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Invite",
                tint = SecondaryText,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Invite",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = SecondaryText
        )
    }
}

@Composable
fun FocusProjectCard(
    project: FocusProject,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(260.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Image area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        when (project.imageRes) {
                            "microscope" -> TealPrimary
                            else -> Color(0xFFF5E6D3)
                        }
                    )
            ) {
                // Course code badge
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = project.courseCode,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }
                
                // Active badge
                if (project.isActive) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                            .background(TealPrimary, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "ACTIVE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                
                // Placeholder image content
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (project.imageRes) {
                            "microscope" -> "🔬"
                            else -> "📝"
                        },
                        fontSize = 48.sp
                    )
                }
            }
            
            // Info area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = project.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PROGRESS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryText,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${project.progress}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(DividerGray, RoundedCornerShape(3.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(project.progress / 100f)
                            .background(TealPrimary, RoundedCornerShape(3.dp))
                    )
                }
            }
        }
    }
}
