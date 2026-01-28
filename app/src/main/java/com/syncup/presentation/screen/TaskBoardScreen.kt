package com.syncup.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syncup.domain.model.Task
import com.syncup.domain.model.TaskPriority
import com.syncup.domain.model.TaskStatus
import com.syncup.presentation.components.SimpleAvatar
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

enum class BoardTab(val title: String) {
    TODO("To-Do"),
    DOING("Doing"),
    DONE("Done"),
    BACKLOG("Backlog")
}

@Composable
fun TaskBoardScreen(
    onBackClick: () -> Unit = {},
    onCreateTask: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(BoardTab.TODO) }
    var showCreateTaskDialog by remember { mutableStateOf(false) }
    var taskUpdateTrigger by remember { mutableStateOf(0) }
    
    // Use mutableStateListOf for reactive updates
    val allTasks = remember { mutableStateListOf<Task>().apply { addAll(MockData.mockTasks) } }
    
    // Filter tasks based on selected tab
    val filteredTasks = when (selectedTab) {
        BoardTab.TODO -> allTasks.filter { it.status == TaskStatus.TODO && !it.isBacklog }
        BoardTab.DOING -> allTasks.filter { it.status == TaskStatus.IN_PROGRESS }
        BoardTab.DONE -> allTasks.filter { it.status == TaskStatus.DONE }
        BoardTab.BACKLOG -> allTasks.filter { it.isBacklog || it.labels.contains("future") }
    }
    
    // Force recomposition on trigger
    LaunchedEffect(taskUpdateTrigger) { }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .statusBarsPadding()
        ) {
            // Header
            TaskBoardHeader(
                onBackClick = onBackClick,
                selectedTab = selectedTab,
                onMenuClick = onMenuClick
            )
            
            // Tab Row
            TaskBoardTabs(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
            
            // Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                
                // Section Header
                item {
                    TaskSectionHeader(
                        tab = selectedTab,
                        taskCount = filteredTasks.size
                    )
                }
                
                // Task Cards
                items(filteredTasks, key = { "${it.id}_${it.status}" }) { task ->
                    AdaptiveTaskCard(
                        task = task,
                        currentTab = selectedTab,
                        onStatusChange = { newStatus ->
                            val index = allTasks.indexOfFirst { it.id == task.id }
                            if (index != -1) {
                                allTasks[index] = task.copy(status = newStatus)
                                taskUpdateTrigger++
                            }
                        }
                    )
                }
                
                // Coming Up Section (only for TODO tab)
                if (selectedTab == BoardTab.TODO) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        TeamProgressSection()
                    }
                    
                    item {
                        ComingUpCard()
                    }
                }
                
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
        
        // FAB
        FloatingActionButton(
            onClick = { showCreateTaskDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = 72.dp),
            containerColor = TealPrimary,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Task",
                tint = Color.White
            )
        }
        
        // Create Task Dialog
        if (showCreateTaskDialog) {
            CreateTaskDialog(
                onDismiss = { showCreateTaskDialog = false },
                onTaskCreated = { newTask ->
                    allTasks.add(0, newTask)
                    showCreateTaskDialog = false
                    taskUpdateTrigger++
                }
            )
        }
    }
}

@Composable
fun TaskBoardHeader(
    onBackClick: () -> Unit,
    selectedTab: BoardTab,
    onMenuClick: () -> Unit = {}
) {
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
                    text = when (selectedTab) {
                        BoardTab.TODO -> "Adaptive Task Board"
                        BoardTab.DOING -> "Adaptive Task Board"
                        BoardTab.DONE -> "Task Board"
                        BoardTab.BACKLOG -> "Project Backlog"
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Text(
                    text = "SYNCUP PROJECT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = SecondaryText,
                    letterSpacing = 1.sp
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
fun TaskBoardTabs(
    selectedTab: BoardTab,
    onTabSelected: (BoardTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BoardTab.values().forEach { tab ->
            TaskTabChip(
                text = tab.title,
                isSelected = selectedTab == tab,
                onClick = { onTabSelected(tab) }
            )
        }
    }
}

@Composable
fun TaskTabChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) TealPrimary else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.White else DarkText
        )
    }
}

@Composable
fun TaskSectionHeader(tab: BoardTab, taskCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = when (tab) {
                BoardTab.TODO -> "My Priority Tasks"
                BoardTab.DOING -> "Tasks in Progress"
                BoardTab.DONE -> "Completed Tasks"
                BoardTab.BACKLOG -> "Future Ideas"
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Text(
            text = when (tab) {
                BoardTab.TODO -> "$taskCount Active"
                BoardTab.DOING -> "$taskCount active"
                BoardTab.DONE -> "$taskCount Today"
                BoardTab.BACKLOG -> "$taskCount Items"
            },
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TealPrimary
        )
    }
}

@Composable
fun AdaptiveTaskCard(
    task: Task,
    currentTab: BoardTab,
    onStatusChange: (TaskStatus) -> Unit = {}
) {
    val isAssignedToMe = task.assignedTo == "user_1"
    val isDueToday = task.dueDate?.let { 
        it - System.currentTimeMillis() < 86400000 && it > System.currentTimeMillis() 
    } ?: false
    val hoursLeft = task.dueDate?.let {
        ((it - System.currentTimeMillis()) / 3600000).toInt()
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isAssignedToMe && currentTab != BoardTab.DONE) {
                    Modifier.border(2.dp, TealLight.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                } else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Assigned to you badge
            if (isAssignedToMe && currentTab != BoardTab.DONE) {
                Box(
                    modifier = Modifier
                        .background(TealPrimary, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ASSIGNED TO YOU",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Tags row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Priority/Status Tag
                    when {
                        currentTab == BoardTab.DONE -> {
                            StatusChip("COMPLETED", SuccessGreen, Color(0xFFDCFCE7))
                        }
                        currentTab == BoardTab.BACKLOG && task.labels.contains("idea") -> {
                            StatusChip("YOUR IDEA", WarningOrange, Color(0xFFFEF3C7))
                        }
                        task.priority == TaskPriority.CRITICAL -> {
                            StatusChip("CRITICAL", CriticalRed, Color(0xFFFEE2E2))
                        }
                        task.priority == TaskPriority.MEDIUM -> {
                            StatusChip("MEDIUM PRIORITY", WarningOrange, Color(0xFFFEF3C7))
                        }
                        task.priority == TaskPriority.LOW -> {
                            StatusChip("LOW PRIORITY", TealLight, BackgroundLight)
                        }
                    }
                    
                    // Time/Date Tag
                    when {
                        currentTab == BoardTab.DONE -> {
                            Row(
                                modifier = Modifier
                                    .background(BackgroundLight, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("📅", fontSize = 12.sp)
                                Text(
                                    text = "OCT 24",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkText
                                )
                            }
                        }
                        isDueToday && hoursLeft != null -> {
                            Row(
                                modifier = Modifier
                                    .background(Color(0xFFFEF3C7), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("⏰", fontSize = 12.sp)
                                Text(
                                    text = if (currentTab == BoardTab.DOING) "${hoursLeft}H LEFT" else "TODAY",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarningOrange
                                )
                            }
                        }
                        currentTab == BoardTab.BACKLOG && task.labels.contains("idea") -> {
                            Row(
                                modifier = Modifier
                                    .background(Color(0xFFDBEAFE), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("💡", fontSize = 12.sp)
                                Text(
                                    text = "IDEA",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TealPrimary
                                )
                            }
                        }
                        task.dueDate == null && currentTab == BoardTab.BACKLOG -> {
                            Row(
                                modifier = Modifier
                                    .background(BackgroundLight, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("📅", fontSize = 12.sp)
                                Text(
                                    text = "NO DUE DATE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SecondaryText
                                )
                            }
                        }
                    }
                }
                
                // Checkmark for done tasks
                if (currentTab == BoardTab.DONE) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(DividerGray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✓", fontSize = 14.sp, color = SecondaryText)
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = SecondaryText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Title
            Text(
                text = task.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
            
            // Description
            task.description?.let {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = if (currentTab == BoardTab.DONE) SecondaryText else Color(0xFF666666),
                    fontStyle = if (currentTab == BoardTab.DONE) 
                        androidx.compose.ui.text.font.FontStyle.Italic else 
                        androidx.compose.ui.text.font.FontStyle.Normal
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Quick-tap status update
            Text(
                text = "QUICK-TAP STATUS UPDATE",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = SecondaryText,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Status Toggle Buttons
            QuickStatusToggle(
                currentStatus = task.status,
                currentTab = currentTab,
                onStatusChange = onStatusChange
            )
        }
    }
}

@Composable
fun StatusChip(text: String, textColor: Color, bgColor: Color) {
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun QuickStatusToggle(
    currentStatus: TaskStatus,
    currentTab: BoardTab,
    onStatusChange: (TaskStatus) -> Unit = {}
) {
    val options = when (currentTab) {
        BoardTab.BACKLOG -> listOf("Backlog" to TaskStatus.TODO, "To-Do" to TaskStatus.TODO, "Doing" to TaskStatus.IN_PROGRESS)
        else -> listOf("To-Do" to TaskStatus.TODO, 
                       "Doing" to TaskStatus.IN_PROGRESS, 
                       "Done" to TaskStatus.DONE)
    }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { (label, targetStatus) ->
            val actualSelected = when (currentTab) {
                BoardTab.TODO -> label == "To-Do"
                BoardTab.DOING -> label == "Doing"
                BoardTab.DONE -> label == "Done"
                BoardTab.BACKLOG -> label == "Backlog"
            }
            
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (actualSelected) TealPrimary else BackgroundLight)
                    .border(
                        width = if (actualSelected) 0.dp else 1.dp,
                        color = DividerGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { 
                        if (!actualSelected) {
                            onStatusChange(targetStatus)
                        }
                    }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!actualSelected) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    when (label) {
                                        "To-Do" -> SecondaryText
                                        "Doing" -> WarningOrange
                                        "Done" -> SuccessGreen
                                        else -> SecondaryText
                                    },
                                    CircleShape
                                )
                        )
                    }
                    Text(
                        text = label,
                        fontSize = 13.sp,
                        fontWeight = if (actualSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (actualSelected) Color.White else DarkText
                    )
                }
            }
        }
    }
}

@Composable
fun TeamProgressSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "TEAM PROGRESS",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        Text(
            text = "2 tasks",
            fontSize = 12.sp,
            color = SecondaryText
        )
    }
}

@Composable
fun ComingUpCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = TealPrimary)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Text(
                    text = "COMING UP",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.7f),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "UI/UX Brainstorm",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tomorrow • 2:00 PM",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
            
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(48.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Go",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun CreateTaskDialog(
    onDismiss: () -> Unit,
    onTaskCreated: (Task) -> Unit = {}
) {
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var selectedAssignee by remember { mutableStateOf("ME") }
    
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
                    text = "New Task",
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
                // Task Title
                Column {
                    Text(
                        text = "TASK TITLE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryText,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        placeholder = { Text("e.g., Design System Audit", color = SecondaryText) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DarkText,
                            unfocusedTextColor = DarkText,
                            cursorColor = TealPrimary,
                            focusedBorderColor = TealPrimary,
                            unfocusedBorderColor = DividerGray
                        ),
                        singleLine = true
                    )
                }
                
                // Description
                Column {
                    Text(
                        text = "DESCRIPTION",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryText,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = taskDescription,
                        onValueChange = { taskDescription = it },
                        placeholder = { Text("Break down the requirements...", color = SecondaryText) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = DarkText,
                            unfocusedTextColor = DarkText,
                            cursorColor = TealPrimary,
                            focusedBorderColor = TealPrimary,
                            unfocusedBorderColor = DividerGray
                        )
                    )
                }
                
                // Assign To
                Column {
                    Text(
                        text = "ASSIGN TO",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryText,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val assignees = listOf(
                            AssigneeData("ME", "Alex Rivera", TealPrimary),
                            AssigneeData("SARAH", "Sarah Miller", TealLight),
                            AssigneeData("JAMIE", "Jamie Chen", TealDark),
                            AssigneeData("MIKE", "Mike Wilson", Color(0xFFF59E0B)),
                            AssigneeData("MORE", "", DividerGray)
                        )
                        items(assignees) { assignee ->
                            AssigneeAvatar(
                                assignee = assignee,
                                isSelected = assignee.shortName == selectedAssignee,
                                onClick = { selectedAssignee = assignee.shortName }
                            )
                        }
                    }
                }
                
                // Priority Level
                Column {
                    Text(
                        text = "PRIORITY LEVEL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryText,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PriorityButton(
                            label = "CRITICAL",
                            icon = "!",
                            isSelected = selectedPriority == TaskPriority.CRITICAL,
                            color = CriticalRed,
                            onClick = { selectedPriority = TaskPriority.CRITICAL }
                        )
                        PriorityButton(
                            label = "MEDIUM",
                            icon = "◆",
                            isSelected = selectedPriority == TaskPriority.MEDIUM,
                            color = WarningOrange,
                            onClick = { selectedPriority = TaskPriority.MEDIUM }
                        )
                        PriorityButton(
                            label = "LOW",
                            icon = "↓",
                            isSelected = selectedPriority == TaskPriority.LOW,
                            color = TealLight,
                            onClick = { selectedPriority = TaskPriority.LOW }
                        )
                    }
                }
                
                // Due Date
                Column {
                    Text(
                        text = "DUE DATE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryText,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        placeholder = { Text("mm/dd/yyyy", color = SecondaryText) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TealPrimary,
                            unfocusedBorderColor = DividerGray
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date",
                                tint = SecondaryText
                            )
                        },
                        singleLine = true,
                        readOnly = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (taskTitle.isNotBlank()) {
                        val newTask = Task(
                            id = "task_${System.currentTimeMillis()}",
                            projectId = "project_1",
                            milestoneId = null,
                            title = taskTitle,
                            description = taskDescription.ifBlank { null },
                            assignedTo = when (selectedAssignee) {
                                "ME" -> "user_1"
                                "SARAH" -> "user_2"
                                "JAMIE" -> "user_3"
                                "MIKE" -> "user_4"
                                else -> "user_1"
                            },
                            dueDate = System.currentTimeMillis() + 86400000 * 3, // 3 days from now
                            status = TaskStatus.TODO,
                            priority = selectedPriority,
                            labels = listOf("new"),
                            attachments = listOf(),
                            blockerReason = null,
                            createdBy = "user_1",
                            createdAt = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis()
                        )
                        onTaskCreated(newTask)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                enabled = taskTitle.isNotBlank()
            ) {
                Text(
                    text = "Create Task",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun PriorityButton(
    label: String,
    icon: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) color.copy(alpha = 0.15f) else Color.Transparent)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) color else DividerGray,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 16.sp,
                color = color,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) color else SecondaryText
            )
        }
    }
}

// Data class for team assignees
data class AssigneeData(
    val shortName: String,
    val fullName: String,
    val avatarColor: Color
)

@Composable
fun AssigneeAvatar(
    assignee: AssigneeData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    if (assignee.shortName == "MORE") BackgroundLight
                    else assignee.avatarColor
                )
                .border(
                    width = if (isSelected) 3.dp else 0.dp,
                    color = if (isSelected) TealPrimary else Color.Transparent,
                    shape = CircleShape
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            if (assignee.shortName == "MORE") {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add more",
                    tint = SecondaryText,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                // Get initials from full name
                val initials = assignee.fullName.split(" ")
                    .take(2)
                    .mapNotNull { it.firstOrNull()?.uppercase() }
                    .joinToString("")
                Text(
                    text = initials,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        
        // Selected checkmark indicator
        if (isSelected && assignee.shortName != "MORE") {
            Box(
                modifier = Modifier
                    .offset(y = (-8).dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(TealPrimary)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        } else {
            Spacer(modifier = Modifier.height(10.dp))
        }
        
        Text(
            text = assignee.shortName,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) TealPrimary else DarkText
        )
    }
}
