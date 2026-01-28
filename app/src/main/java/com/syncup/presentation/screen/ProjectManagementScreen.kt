package com.syncup.presentation.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syncup.domain.model.*
import com.syncup.domain.service.MilestoneService
import com.syncup.domain.service.TaskService
import com.syncup.presentation.mock.ProjectManagementMockData

// Teal Color Palette
private val TealPrimary = Color(0xFF00897B)
private val TealDark = Color(0xFF00695C)
private val TealLight = Color(0xFF4DB6AC)
private val BackgroundLight = Color(0xFFF8F9FA)
private val DarkText = Color(0xFF1A1A2E)
private val SecondaryText = Color(0xFF6B7280)
private val LightGray = Color(0xFF9CA3AF)
private val DividerGray = Color(0xFFE5E7EB)
private val SurfaceWhite = Color(0xFFFFFFFF)

// Status Colors
private val CriticalRed = Color(0xFFEF4444)
private val CriticalRedLight = Color(0xFFFEE2E2)
private val WarningOrange = Color(0xFFF59E0B)
private val WarningOrangeLight = Color(0xFFFEF3C7)
private val SuccessGreen = Color(0xFF22C55E)
private val SuccessGreenLight = Color(0xFFDCFCE7)
private val InfoBlue = Color(0xFF3B82F6)
private val InfoBlueLight = Color(0xFFDBEAFE)

// Avatar colors for users - matching persona role colors
private val avatarColors = listOf(
    Color(0xFFD32F2F), // Red (Admin)
    Color(0xFF388E3C), // Green (Member)
    Color(0xFFF57C00), // Orange (Viewer)
    Color(0xFF1976D2), // Blue
    Color(0xFF7B1FA2), // Purple
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectManagementScreen() {
    var selectedUser by remember { mutableStateOf(ProjectManagementMockData.tundeLeadUser) }
    var selectedTab by remember { mutableStateOf(0) }
    // Track task updates to trigger recomposition
    var taskUpdateTrigger by remember { mutableStateOf(0) }
    
    val tabs = listOf(
        TabItem("Tasks", Icons.Default.List, Icons.Filled.List),
        TabItem("Milestones", Icons.Default.Star, Icons.Filled.Star),
        TabItem("Chat", Icons.Default.Email, Icons.Filled.Email),
        TabItem("Overview", Icons.Default.Home, Icons.Filled.Home)
    )
    
    Scaffold(
        containerColor = BackgroundLight,
        topBar = {
            EnhancedProjectHeader(
                currentUser = selectedUser,
                allUsers = ProjectManagementMockData.allUsers,
                onUserSelected = { selectedUser = it }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Enhanced Tab Bar
            EnhancedTabBar(
                tabs = tabs,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
            
            // Content
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                    fadeOut(animationSpec = tween(300))
                },
                label = "tab_content"
            ) { tab ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 20.dp)
                ) {
                    when (tab) {
                        0 -> {
                            item {
                                // Use key to force recomposition on task updates
                                key(taskUpdateTrigger) {
                                    EnhancedTaskView(
                                        currentUser = selectedUser,
                                        taskService = ProjectManagementMockData.taskService,
                                        allTasks = ProjectManagementMockData.tasks,
                                        onTaskStatusChanged = { taskUpdateTrigger++ }
                                    )
                                }
                            }
                        }
                        1 -> {
                            item {
                                // Use key to force recomposition on task updates
                                key(taskUpdateTrigger) {
                                    EnhancedMilestoneView(
                                        currentUser = selectedUser,
                                        milestoneService = ProjectManagementMockData.milestoneService,
                                        allMilestones = ProjectManagementMockData.milestones,
                                        allTasks = ProjectManagementMockData.tasks,
                                        onTaskStatusChanged = { taskUpdateTrigger++ }
                                    )
                                }
                            }
                        }
                        2 -> {
                            item {
                                EnhancedChatView(
                                    currentUser = selectedUser,
                                    chatService = ProjectManagementMockData.chatService
                                )
                            }
                        }
                        3 -> {
                            item {
                                key(taskUpdateTrigger) {
                                    EnhancedOverviewView(
                                        project = ProjectManagementMockData.mainProject,
                                        allUsers = ProjectManagementMockData.allUsers,
                                        allTasks = ProjectManagementMockData.tasks,
                                        allMilestones = ProjectManagementMockData.milestones
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class TabItem(
    val title: String,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedProjectHeader(
    currentUser: RoleBasedUser,
    allUsers: List<RoleBasedUser>,
    onUserSelected: (RoleBasedUser) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SurfaceWhite,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(20.dp)
        ) {
            // Title Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Project Hub",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(
                        text = ProjectManagementMockData.mainProject.name,
                        fontSize = 14.sp,
                        color = TealPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                // Current user badge
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = when (currentUser.role) {
                        UserRole.ADMIN -> Color(0xFFFFEBEE)  // Light red background
                        UserRole.MEMBER -> Color(0xFFE8F5E9)  // Light green background
                        UserRole.VIEWER -> Color(0xFFFFF3E0)  // Light orange background
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = when (currentUser.role) {
                                UserRole.ADMIN -> Icons.Default.Star
                                UserRole.MEMBER -> Icons.Default.Person
                                UserRole.VIEWER -> Icons.Default.Search
                            },
                            contentDescription = null,
                            tint = when (currentUser.role) {
                                UserRole.ADMIN -> Color(0xFFD32F2F)  // Red
                                UserRole.MEMBER -> Color(0xFF388E3C)  // Green
                                UserRole.VIEWER -> Color(0xFFF57C00)  // Orange
                            },
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = currentUser.role.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = when (currentUser.role) {
                                UserRole.ADMIN -> Color(0xFFD32F2F)  // Red
                                UserRole.MEMBER -> Color(0xFF388E3C)  // Green
                                UserRole.VIEWER -> Color(0xFFF57C00)  // Orange
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // User Switcher
            Text(
                text = "Switch User View",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = SecondaryText,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(allUsers) { user ->
                    val isSelected = currentUser.id == user.id
                    // Role-based avatar color
                    val avatarColor = when (user.role) {
                        UserRole.ADMIN -> Color(0xFFD32F2F)  // Red
                        UserRole.MEMBER -> Color(0xFF388E3C)  // Green
                        UserRole.VIEWER -> Color(0xFFF57C00)  // Orange
                    }
                    // Role-based card background
                    val cardBackground = when (user.role) {
                        UserRole.ADMIN -> Color(0xFFFFEBEE)  // Light red
                        UserRole.MEMBER -> Color(0xFFE8F5E9)  // Light green
                        UserRole.VIEWER -> Color(0xFFFFF3E0)  // Light orange
                    }
                    
                    Surface(
                        modifier = Modifier
                            .clickable { onUserSelected(user) }
                            .then(
                                if (isSelected) Modifier.border(
                                    2.dp,
                                    avatarColor,
                                    RoundedCornerShape(16.dp)
                                ) else Modifier
                            ),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) cardBackground else BackgroundLight
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(avatarColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.name.split(" ").map { it.first() }.take(2).joinToString(""),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            
                            Column {
                                Text(
                                    text = user.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DarkText
                                )
                                Text(
                                    text = user.role.toString(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = avatarColor
                                )
                            }
                            
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = avatarColor,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedTabBar(
    tabs: List<TabItem>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SurfaceWhite
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabs.forEachIndexed { index, tab ->
                val isSelected = selectedTab == index
                
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .clickable { onTabSelected(index) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) TealPrimary else Color.Transparent
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isSelected) tab.filledIcon else tab.outlinedIcon,
                            contentDescription = tab.title,
                            tint = if (isSelected) Color.White else SecondaryText,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = tab.title,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (isSelected) Color.White else SecondaryText
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedTaskView(
    currentUser: RoleBasedUser,
    taskService: TaskService,
    allTasks: List<Task>,
    onTaskStatusChanged: () -> Unit = {}
) {
    val tasksToShow = if (currentUser.role == UserRole.MEMBER) {
        taskService.getUserTasks(currentUser.id, allTasks)
    } else {
        allTasks
    }
    
    val todoTasks = tasksToShow.filter { it.status == TaskStatus.TODO }
    val inProgressTasks = tasksToShow.filter { it.status == TaskStatus.IN_PROGRESS }
    val doneTasks = tasksToShow.filter { it.status == TaskStatus.DONE }
    
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        // Stats Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TaskStatCard(
                modifier = Modifier.weight(1f),
                count = todoTasks.size,
                label = "To Do",
                color = InfoBlue,
                bgColor = InfoBlueLight,
                icon = Icons.Default.Clear
            )
            TaskStatCard(
                modifier = Modifier.weight(1f),
                count = inProgressTasks.size,
                label = "In Progress",
                color = WarningOrange,
                bgColor = WarningOrangeLight,
                icon = Icons.Default.Refresh
            )
            TaskStatCard(
                modifier = Modifier.weight(1f),
                count = doneTasks.size,
                label = "Done",
                color = SuccessGreen,
                bgColor = SuccessGreenLight,
                icon = Icons.Outlined.CheckCircle
            )
        }
        
        // Section Header
        SectionHeader(
            title = if (currentUser.role == UserRole.MEMBER) "My Tasks" else "All Tasks",
            subtitle = "${tasksToShow.size} tasks total"
        )
        
        // Task List by Status
        if (inProgressTasks.isNotEmpty()) {
            TaskSection(title = "In Progress", tasks = inProgressTasks, color = WarningOrange)
        }
        
        if (todoTasks.isNotEmpty()) {
            TaskSection(title = "To Do", tasks = todoTasks, color = InfoBlue)
        }
        
        if (doneTasks.isNotEmpty()) {
            TaskSection(title = "Completed", tasks = doneTasks, color = SuccessGreen)
        }
    }
}

@Composable
fun TaskStatCard(
    modifier: Modifier = Modifier,
    count: Int,
    label: String,
    color: Color,
    bgColor: Color,
    icon: ImageVector
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = bgColor
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = count.toString(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = color.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Text(
            text = subtitle,
            fontSize = 13.sp,
            color = SecondaryText
        )
    }
}

@Composable
fun TaskSection(title: String, tasks: List<Task>, color: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )
            Text(
                text = "$title (${tasks.size})",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = SecondaryText
            )
        }
        
        tasks.forEach { task ->
            EnhancedTaskCard(task)
        }
    }
}

@Composable
fun EnhancedTaskCard(task: Task) {
    val priorityColor = when (task.priority) {
        TaskPriority.CRITICAL -> CriticalRed
        TaskPriority.HIGH -> WarningOrange
        TaskPriority.MEDIUM -> TealLight
        TaskPriority.LOW -> SecondaryText
    }
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceWhite,
        shadowElevation = 2.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Priority indicator
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(IntrinsicSize.Max)
                    .background(priorityColor, RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            )
            
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
                        text = task.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkText,
                        modifier = Modifier.weight(1f)
                    )
                    
                    EnhancedStatusBadge(task.status)
                }
                
                if (!task.description.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = task.description,
                        fontSize = 13.sp,
                        color = SecondaryText,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Priority badge
                    EnhancedPriorityBadge(task.priority)
                    
                    // Assigned user
                    task.assignedTo?.let {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(avatarColors[0], CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "Assigned",
                                fontSize = 12.sp,
                                color = SecondaryText
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedStatusBadge(status: TaskStatus) {
    val (bgColor, textColor, text) = when (status) {
        TaskStatus.TODO -> Triple(InfoBlueLight, InfoBlue, "To Do")
        TaskStatus.IN_PROGRESS -> Triple(WarningOrangeLight, WarningOrange, "In Progress")
        TaskStatus.DONE -> Triple(SuccessGreenLight, SuccessGreen, "Done")
        else -> Triple(BackgroundLight, SecondaryText, "Unknown")
    }
    
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun EnhancedPriorityBadge(priority: TaskPriority) {
    val (color, icon) = when (priority) {
        TaskPriority.CRITICAL -> Pair(CriticalRed, Icons.Default.Warning)
        TaskPriority.HIGH -> Pair(WarningOrange, Icons.Default.KeyboardArrowUp)
        TaskPriority.MEDIUM -> Pair(TealLight, Icons.Default.Menu)
        TaskPriority.LOW -> Pair(SecondaryText, Icons.Default.KeyboardArrowDown)
    }
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = priority.toString(),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@Composable
fun EnhancedMilestoneView(
    currentUser: RoleBasedUser,
    milestoneService: MilestoneService,
    allMilestones: List<Milestone>,
    allTasks: List<Task>,
    onTaskStatusChanged: () -> Unit = {}
) {
    val completedCount = allMilestones.count { 
        milestoneService.isMilestoneCompleted(it, allTasks) 
    }
    
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        // Progress Overview Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = TealPrimary
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(TealPrimary, TealDark)
                        )
                    )
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Project Progress",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$completedCount of ${allMilestones.size}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Milestones Completed",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    
                    // Circular progress indicator
                    Box(
                        modifier = Modifier.size(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = if (allMilestones.isNotEmpty()) completedCount.toFloat() / allMilestones.size else 0f,
                            modifier = Modifier.size(80.dp),
                            color = Color.White,
                            strokeWidth = 8.dp,
                            trackColor = Color.White.copy(alpha = 0.3f),
                        )
                        Text(
                            text = "${if (allMilestones.isNotEmpty()) (completedCount.toFloat() / allMilestones.size * 100).toInt() else 0}%",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
        
        // Milestones List
        SectionHeader(
            title = "Milestones",
            subtitle = "${allMilestones.size} total"
        )
        
        allMilestones.forEach { milestone ->
            val status = milestoneService.getMilestoneStatus(milestone, allTasks)
            val milestoneTasks = allTasks.filter { it.milestoneId == milestone.id }
            EnhancedMilestoneCard(
                status = status,
                tasks = milestoneTasks,
                currentUser = currentUser,
                onTaskStatusChanged = onTaskStatusChanged
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedMilestoneCard(
    status: com.syncup.domain.service.MilestoneStatus,
    tasks: List<Task>,
    currentUser: RoleBasedUser,
    onTaskStatusChanged: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        color = SurfaceWhite,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top row with icon, title, percentage and expand icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Milestone icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            if (status.isCompleted) SuccessGreenLight else TealLight.copy(alpha = 0.15f),
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (status.isCompleted) Icons.Default.CheckCircle else Icons.Default.Star,
                        contentDescription = null,
                        tint = if (status.isCompleted) SuccessGreen else TealLight,
                        modifier = Modifier.size(22.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Title and subtitle - takes remaining space
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = status.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (status.isCompleted) "Completed" else "${status.doneCount}/${status.totalTasks} tasks done",
                        fontSize = 12.sp,
                        color = if (status.isCompleted) SuccessGreen else SecondaryText
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Progress percentage - fixed width
                Text(
                    text = "${(status.progress * 100).toInt()}%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (status.isCompleted) SuccessGreen else TealPrimary,
                    modifier = Modifier.widthIn(min = 40.dp)
                )
                
                // Expand/collapse icon
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = SecondaryText,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(DividerGray, RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(status.progress)
                        .fillMaxHeight()
                        .background(
                            if (status.isCompleted) SuccessGreen else TealPrimary,
                            RoundedCornerShape(4.dp)
                        )
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Task breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TaskCountChip(
                    icon = Icons.Default.Clear,
                    count = status.todoCount,
                    label = "To Do",
                    color = InfoBlue
                )
                TaskCountChip(
                    icon = Icons.Default.Refresh,
                    count = status.inProgressCount,
                    label = "In Progress",
                    color = WarningOrange
                )
                TaskCountChip(
                    icon = Icons.Default.CheckCircle,
                    count = status.doneCount,
                    label = "Done",
                    color = SuccessGreen
                )
            }
            
            // Expanded task list
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Divider(color = DividerGray)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Tasks in this milestone",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SecondaryText
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    tasks.forEach { task ->
                        MilestoneTaskItem(
                            task = task,
                            currentUser = currentUser,
                            onStatusChange = { newStatus ->
                                // Update task status in the mock data
                                val index = ProjectManagementMockData.tasks.indexOfFirst { it.id == task.id }
                                if (index != -1) {
                                    ProjectManagementMockData.tasks[index] = task.copy(status = newStatus)
                                    onTaskStatusChanged()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MilestoneTaskItem(
    task: Task,
    currentUser: RoleBasedUser,
    onStatusChange: (TaskStatus) -> Unit
) {
    var showStatusMenu by remember { mutableStateOf(false) }
    val canUpdateStatus = currentUser.canUpdateTaskStatus()
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = BackgroundLight
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Status indicator/checkbox
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            when (task.status) {
                                TaskStatus.DONE -> SuccessGreenLight
                                TaskStatus.IN_PROGRESS -> WarningOrangeLight
                                else -> InfoBlueLight
                            },
                            CircleShape
                        )
                        .then(
                            if (canUpdateStatus) {
                                Modifier.clickable { showStatusMenu = true }
                            } else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (task.status) {
                            TaskStatus.DONE -> Icons.Default.Check
                            TaskStatus.IN_PROGRESS -> Icons.Default.Refresh
                            else -> Icons.Default.Add
                        },
                        contentDescription = null,
                        tint = when (task.status) {
                            TaskStatus.DONE -> SuccessGreen
                            TaskStatus.IN_PROGRESS -> WarningOrange
                            else -> InfoBlue
                        },
                        modifier = Modifier.size(14.dp)
                    )
                }
                
                Column {
                    Text(
                        text = task.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (task.status == TaskStatus.DONE) SecondaryText else DarkText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = task.status.toString().replace("_", " "),
                        fontSize = 11.sp,
                        color = when (task.status) {
                            TaskStatus.DONE -> SuccessGreen
                            TaskStatus.IN_PROGRESS -> WarningOrange
                            else -> InfoBlue
                        }
                    )
                }
            }
            
            // Status change button
            if (canUpdateStatus) {
                Box {
                    Surface(
                        modifier = Modifier.clickable { showStatusMenu = true },
                        shape = RoundedCornerShape(8.dp),
                        color = TealLight.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "Change",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TealPrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                    
                    DropdownMenu(
                        expanded = showStatusMenu,
                        onDismissRequest = { showStatusMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = InfoBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text("To Do")
                                }
                            },
                            onClick = {
                                onStatusChange(TaskStatus.TODO)
                                showStatusMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        tint = WarningOrange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text("In Progress")
                                }
                            },
                            onClick = {
                                onStatusChange(TaskStatus.IN_PROGRESS)
                                showStatusMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = SuccessGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text("Done")
                                }
                            },
                            onClick = {
                                onStatusChange(TaskStatus.DONE)
                                showStatusMenu = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCountChip(
    icon: ImageVector,
    count: Int,
    label: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "$count",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@Composable
fun EnhancedChatView(
    currentUser: RoleBasedUser,
    chatService: com.syncup.domain.service.ChatService
) {
    var messageText by remember { mutableStateOf("") }
    var messages by remember { 
        mutableStateOf(
            chatService.getConversationHistory(
                ConversationType.PROJECT_DISCUSSION,
                "proj_001"
            )
        ) 
    }
    
    val canSendMessage = currentUser.canSendMessages()
    
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Chat Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            shadowElevation = 2.dp
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
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(TealLight.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = null,
                            tint = TealPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Column {
                        Text(
                            text = "Project Discussion",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkText
                        )
                        Text(
                            text = "${messages.size} messages",
                            fontSize = 13.sp,
                            color = SecondaryText
                        )
                    }
                }
                
                // Online indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(SuccessGreen, CircleShape)
                    )
                    Text(
                        text = "Online",
                        fontSize = 12.sp,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        
        // Messages Container
        val listState = rememberLazyListState()
        
        // Auto-scroll to bottom when new messages arrive
        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            shadowElevation = 1.dp
        ) {
            if (messages.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = SecondaryText,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "No messages yet",
                            fontSize = 14.sp,
                            color = SecondaryText
                        )
                        Text(
                            text = "Start the conversation!",
                            fontSize = 12.sp,
                            color = SecondaryText
                        )
                    }
                }
            } else {
                // Messages list with LazyColumn
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp, max = 350.dp)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages.size) { index ->
                        val message = messages[index]
                        val isCurrentUser = message.senderId == currentUser.id
                        EnhancedChatBubble(message, isCurrentUser)
                    }
                }
            }
        }
        
        // Input field
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Permission notice for viewers
                if (!canSendMessage) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = WarningOrangeLight
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = WarningOrange,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Viewers cannot send messages",
                                fontSize = 12.sp,
                                color = WarningOrange
                            )
                        }
                    }
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Text input
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        placeholder = {
                            Text(
                                text = if (canSendMessage) "Type a message..." else "You can only view messages",
                                fontSize = 14.sp,
                                color = SecondaryText
                            )
                        },
                        enabled = canSendMessage,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DarkText,
                            unfocusedTextColor = DarkText,
                            disabledTextColor = SecondaryText,
                            cursorColor = TealPrimary,
                            focusedBorderColor = TealPrimary,
                            unfocusedBorderColor = DividerGray,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = BackgroundLight,
                            disabledContainerColor = BackgroundLight,
                            disabledBorderColor = DividerGray
                        ),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 14.sp,
                            color = DarkText
                        )
                    )
                    
                    // Send button
                    Surface(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(enabled = canSendMessage && messageText.isNotBlank()) {
                                if (messageText.isNotBlank()) {
                                    // Send message
                                    chatService.sendMessage(
                                        ConversationType.PROJECT_DISCUSSION,
                                        "proj_001",
                                        currentUser,
                                        messageText
                                    )
                                    // Refresh messages
                                    messages = chatService.getConversationHistory(
                                        ConversationType.PROJECT_DISCUSSION,
                                        "proj_001"
                                    )
                                    // Clear input
                                    messageText = ""
                                }
                            },
                        shape = CircleShape,
                        color = if (canSendMessage && messageText.isNotBlank()) TealPrimary else SecondaryText
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                
                // Typing indicator area
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sending as ${currentUser.name}",
                    fontSize = 11.sp,
                    color = SecondaryText
                )
            }
        }
        
        // Quick replies (only for members and admins)
        if (canSendMessage) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickReplyChip(
                    text = "👍 Got it!",
                    onClick = {
                        chatService.sendMessage(
                            ConversationType.PROJECT_DISCUSSION,
                            "proj_001",
                            currentUser,
                            "Got it! 👍"
                        )
                        messages = chatService.getConversationHistory(
                            ConversationType.PROJECT_DISCUSSION,
                            "proj_001"
                        )
                    }
                )
                QuickReplyChip(
                    text = "🙋 On it!",
                    onClick = {
                        chatService.sendMessage(
                            ConversationType.PROJECT_DISCUSSION,
                            "proj_001",
                            currentUser,
                            "I'm on it! 🙋"
                        )
                        messages = chatService.getConversationHistory(
                            ConversationType.PROJECT_DISCUSSION,
                            "proj_001"
                        )
                    }
                )
                QuickReplyChip(
                    text = "❓ Question",
                    onClick = {
                        chatService.sendMessage(
                            ConversationType.PROJECT_DISCUSSION,
                            "proj_001",
                            currentUser,
                            "I have a question about this..."
                        )
                        messages = chatService.getConversationHistory(
                            ConversationType.PROJECT_DISCUSSION,
                            "proj_001"
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun QuickReplyChip(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = TealLight.copy(alpha = 0.15f)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TealPrimary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun EnhancedChatBubble(message: ChatMessage, isCurrentUser: Boolean) {
    val avatarColor = avatarColors[message.senderName.hashCode().mod(avatarColors.size).let { if (it < 0) -it else it }]
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isCurrentUser) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(avatarColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message.senderName.take(1),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Column(
            horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
        ) {
            if (!isCurrentUser) {
                Text(
                    text = message.senderName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SecondaryText,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                    bottomEnd = if (isCurrentUser) 4.dp else 16.dp
                ),
                color = if (isCurrentUser) TealPrimary else BackgroundLight
            ) {
                Text(
                    text = message.content,
                    fontSize = 14.sp,
                    color = if (isCurrentUser) Color.White else DarkText,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
            }
        }
        
        if (isCurrentUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(avatarColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message.senderName.take(1),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun EnhancedOverviewView(
    project: Project,
    allUsers: List<RoleBasedUser>,
    allTasks: List<Task>,
    allMilestones: List<Milestone>
) {
    val totalTasks = allTasks.size
    val completedTasks = allTasks.count { it.status == TaskStatus.DONE }
    val progressPercent = if (totalTasks > 0) (completedTasks.toFloat() / totalTasks * 100).toInt() else 0
    
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        // Project Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = SurfaceWhite,
            shadowElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = project.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = project.description,
                            fontSize = 14.sp,
                            color = SecondaryText,
                            lineHeight = 20.sp
                        )
                    }
                    
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SuccessGreenLight
                    ) {
                        Text(
                            text = "Active",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SuccessGreen,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                Divider(color = DividerGray)
                Spacer(modifier = Modifier.height(20.dp))
                
                // Quick Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OverviewStat(
                        value = "$progressPercent%",
                        label = "Progress",
                        icon = Icons.Default.ArrowForward,
                        color = TealPrimary
                    )
                    OverviewStat(
                        value = totalTasks.toString(),
                        label = "Tasks",
                        icon = Icons.Default.List,
                        color = InfoBlue
                    )
                    OverviewStat(
                        value = allMilestones.size.toString(),
                        label = "Milestones",
                        icon = Icons.Default.Star,
                        color = WarningOrange
                    )
                    OverviewStat(
                        value = project.members.size.toString(),
                        label = "Members",
                        icon = Icons.Default.Person,
                        color = Color(0xFF8B5CF6)
                    )
                }
            }
        }
        
        // Team Section
        SectionHeader(
            title = "Team Members",
            subtitle = "${project.members.size} members"
        )
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                allUsers.filter { it.id in project.members }.forEachIndexed { index, user ->
                    val avatarColor = avatarColors[index % avatarColors.size]
                    
                    if (index > 0) {
                        Divider(
                            color = Color(0xFFF3F4F6),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(avatarColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.name.split(" ").map { it.first() }.take(2).joinToString(""),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            
                            Column {
                                Text(
                                    text = user.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DarkText
                                )
                                Text(
                                    text = user.email,
                                    fontSize = 12.sp,
                                    color = SecondaryText
                                )
                            }
                        }
                        
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (user.role) {
                                UserRole.ADMIN -> Color(0xFFFFEBEE)  // Light red background
                                UserRole.MEMBER -> Color(0xFFE8F5E9)  // Light green background
                                UserRole.VIEWER -> Color(0xFFFFF3E0)  // Light orange background
                            }
                        ) {
                            Text(
                                text = user.role.toString(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = when (user.role) {
                                    UserRole.ADMIN -> Color(0xFFD32F2F)  // Red
                                    UserRole.MEMBER -> Color(0xFF388E3C)  // Green
                                    UserRole.VIEWER -> Color(0xFFF57C00)  // Orange
                                },
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OverviewStat(
    value: String,
    label: String,
    icon: ImageVector,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = SecondaryText
        )
    }
}
