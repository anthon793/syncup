package com.syncup.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syncup.presentation.components.SimpleAvatar
import com.syncup.presentation.components.AvatarStack
import com.syncup.presentation.mock.MockData
import com.syncup.presentation.mock.SubGroup
import com.syncup.presentation.mock.SubGroupStatus
import com.syncup.presentation.mock.WorkloadLevel

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
fun TeamScreen(
    onBackClick: () -> Unit = {},
    onInviteClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val subGroups = remember { mutableStateListOf<SubGroup>().apply { addAll(MockData.subGroups) } }
    val totalCompletion = subGroups.map { it.completionPercentage }.average().toInt()
    var showMemberDialog by remember { mutableStateOf(false) }
    var selectedMemberIndex by remember { mutableStateOf(-1) }
    
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
                TeamScreenHeader(totalCompletion, onMenuClick)
            }
            
            // Team Members Quick View
            item {
                TeamMembersQuickView(
                    members = MockData.mockTeamMembers,
                    onMemberClick = { index -> 
                        selectedMemberIndex = index
                        showMemberDialog = true
                    }
                )
            }
            
            // Sub-Groups
            items(subGroups.size, key = { subGroups[it].id }) { index ->
                ExpandableSubGroupCard(
                    subGroup = subGroups[index],
                    onProgressUpdate = { newProgress ->
                        subGroups[index] = subGroups[index].copy(completionPercentage = newProgress)
                    }
                )
            }
            
            // Bottom spacer
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
        
        // FAB
        FloatingActionButton(
            onClick = onInviteClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .padding(bottom = 72.dp),
            containerColor = TealPrimary,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Member",
                tint = Color.White
            )
        }
    }
}

@Composable
fun TeamScreenHeader(totalCompletion: Int, onMenuClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onMenuClick() },
                    tint = DarkText
                )
                
                Text(
                    text = "Sub-Group Directory",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }
            
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(BackgroundLight, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "List",
                    tint = DarkText,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TEAM SYNCUP",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SecondaryText,
                letterSpacing = 1.sp
            )
            
            Text(
                text = "Project Active",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SuccessGreen
            )
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Total Completion Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BackgroundLight),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TOTAL COMPLETION",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryText,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "$totalCompletion%",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Overall",
                            fontSize = 14.sp,
                            color = SecondaryText,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
                
                // Progress Bar
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(8.dp)
                        .background(DividerGray, RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(totalCompletion / 100f)
                            .background(TealPrimary, RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    }
}

@Composable
fun SubGroupCard(subGroup: SubGroup) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(BackgroundLight, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(subGroup.icon, fontSize = 20.sp)
                    }
                    
                    Column {
                        Text(
                            text = subGroup.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        when (subGroup.status) {
                                            SubGroupStatus.IN_PROGRESS -> TealLight
                                            SubGroupStatus.DEADLINE_SOON -> CriticalRed
                                            SubGroupStatus.STABLE -> TealDark
                                            SubGroupStatus.DONE -> SuccessGreen
                                        },
                                        CircleShape
                                    )
                            )
                            Text(
                                text = when (subGroup.status) {
                                    SubGroupStatus.IN_PROGRESS -> "IN PROGRESS"
                                    SubGroupStatus.DEADLINE_SOON -> "DEADLINE SOON"
                                    SubGroupStatus.STABLE -> "STABLE"
                                    SubGroupStatus.DONE -> "DONE"
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = when (subGroup.status) {
                                    SubGroupStatus.IN_PROGRESS -> TealLight
                                    SubGroupStatus.DEADLINE_SOON -> CriticalRed
                                    SubGroupStatus.STABLE -> TealDark
                                    SubGroupStatus.DONE -> SuccessGreen
                                }
                            )
                        }
                    }
                }
                
                // Done badge for completed groups
                if (subGroup.status == SubGroupStatus.DONE) {
                    Text(
                        text = "DONE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen
                    )
                }
            }
            
            // Show details only for non-done groups
            if (subGroup.status != SubGroupStatus.DONE) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Capacity
                    Column {
                        Text(
                            text = "CAPACITY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SecondaryText,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "${subGroup.currentMembers}/${subGroup.capacity} members",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = DarkText
                            )
                            if (subGroup.currentMembers == subGroup.capacity) {
                                Text(
                                    text = "FULL",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = WarningOrange,
                                    modifier = Modifier
                                        .background(WarningOrange.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    
                    // Workload
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "WORKLOAD",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SecondaryText,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = when (subGroup.workload) {
                                    WorkloadLevel.HIGH -> "🔥"
                                    WorkloadLevel.MODERATE -> "⚡"
                                    WorkloadLevel.LOW -> "🍃"
                                },
                                fontSize = 14.sp
                            )
                            Text(
                                text = when (subGroup.workload) {
                                    WorkloadLevel.HIGH -> "High"
                                    WorkloadLevel.MODERATE -> "Moderate"
                                    WorkloadLevel.LOW -> "Low"
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = when (subGroup.workload) {
                                    WorkloadLevel.HIGH -> CriticalRed
                                    WorkloadLevel.MODERATE -> WarningOrange
                                    WorkloadLevel.LOW -> SuccessGreen
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Progress
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Sub-group Completion",
                            fontSize = 12.sp,
                            color = SecondaryText
                        )
                        Text(
                            text = "${subGroup.completionPercentage}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(DividerGray, RoundedCornerShape(4.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(subGroup.completionPercentage / 100f)
                                .background(
                                    when (subGroup.status) {
                                        SubGroupStatus.DEADLINE_SOON -> WarningOrange
                                        else -> TealPrimary
                                    },
                                    RoundedCornerShape(4.dp)
                                )
                        )
                    }
                }
            } else {
                // Fun message for completed groups
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (subGroup.name == "Docs & Wiki") "All documentation is up to date." 
                                   else "Great work! All tasks completed.",
                            fontSize = 13.sp,
                            color = SecondaryText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "STATUS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SecondaryText,
                                letterSpacing = 1.sp
                            )
                        }
                        Text(
                            text = "Enjoy your coffee break! ☕",
                            fontSize = 13.sp,
                            color = DarkText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    // Coffee illustration placeholder
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(BackgroundLight, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🏖️", fontSize = 28.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun TeamMembersQuickView(
    members: List<com.syncup.presentation.mock.TeamMember>,
    onMemberClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "TEAM MEMBERS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = SecondaryText,
            letterSpacing = 1.sp
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                members.forEachIndexed { index, member ->
                    TeamMemberRow(
                        member = member,
                        onClick = { onMemberClick(index) }
                    )
                    if (index < members.size - 1) {
                        Divider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = DividerGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TeamMemberRow(
    member: com.syncup.presentation.mock.TeamMember,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box {
            SimpleAvatar(name = member.name, size = 44.dp)
            if (member.isOnline) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(SuccessGreen, CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
        }
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = member.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
            Text(
                text = member.role,
                fontSize = 12.sp,
                color = SecondaryText
            )
        }
        
        // Stats badges
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatBadge(
                value = "${member.tasksCompleted}%",
                label = "Tasks",
                color = TealPrimary
            )
        }
        
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "View",
            tint = SecondaryText,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun StatBadge(value: String, label: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 9.sp,
            color = SecondaryText
        )
    }
}

@Composable
fun ExpandableSubGroupCard(
    subGroup: SubGroup,
    onProgressUpdate: (Int) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(BackgroundLight, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(subGroup.icon, fontSize = 20.sp)
                    }
                    
                    Column {
                        Text(
                            text = subGroup.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        when (subGroup.status) {
                                            SubGroupStatus.IN_PROGRESS -> TealLight
                                            SubGroupStatus.DEADLINE_SOON -> CriticalRed
                                            SubGroupStatus.STABLE -> TealDark
                                            SubGroupStatus.DONE -> SuccessGreen
                                        },
                                        CircleShape
                                    )
                            )
                            Text(
                                text = when (subGroup.status) {
                                    SubGroupStatus.IN_PROGRESS -> "IN PROGRESS"
                                    SubGroupStatus.DEADLINE_SOON -> "DEADLINE SOON"
                                    SubGroupStatus.STABLE -> "STABLE"
                                    SubGroupStatus.DONE -> "DONE"
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = when (subGroup.status) {
                                    SubGroupStatus.IN_PROGRESS -> TealLight
                                    SubGroupStatus.DEADLINE_SOON -> CriticalRed
                                    SubGroupStatus.STABLE -> TealDark
                                    SubGroupStatus.DONE -> SuccessGreen
                                }
                            )
                        }
                    }
                }
                
                // Expand/Collapse icon
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = SecondaryText,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Progress bar always visible
            if (subGroup.status != SubGroupStatus.DONE) {
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Progress",
                        fontSize = 12.sp,
                        color = SecondaryText
                    )
                    Text(
                        text = "${subGroup.completionPercentage}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(DividerGray, RoundedCornerShape(3.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(subGroup.completionPercentage / 100f)
                            .background(TealPrimary, RoundedCornerShape(3.dp))
                    )
                }
            }
            
            // Expanded content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Divider(color = DividerGray)
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Quick Actions
                    Text(
                        text = "QUICK ACTIONS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryText,
                        letterSpacing = 1.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        QuickActionButton(
                            text = "+10%",
                            onClick = { 
                                val newProgress = (subGroup.completionPercentage + 10).coerceAtMost(100)
                                onProgressUpdate(newProgress)
                            },
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionButton(
                            text = "+25%",
                            onClick = { 
                                val newProgress = (subGroup.completionPercentage + 25).coerceAtMost(100)
                                onProgressUpdate(newProgress)
                            },
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionButton(
                            text = "Complete",
                            onClick = { onProgressUpdate(100) },
                            modifier = Modifier.weight(1f),
                            isPrimary = true
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Group details
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Members",
                                fontSize = 11.sp,
                                color = SecondaryText
                            )
                            Text(
                                text = "${subGroup.currentMembers}/${subGroup.capacity}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Workload",
                                fontSize = 11.sp,
                                color = SecondaryText
                            )
                            Text(
                                text = when (subGroup.workload) {
                                    WorkloadLevel.HIGH -> "🔥 High"
                                    WorkloadLevel.MODERATE -> "⚡ Moderate"
                                    WorkloadLevel.LOW -> "🍃 Low"
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (subGroup.workload) {
                                    WorkloadLevel.HIGH -> CriticalRed
                                    WorkloadLevel.MODERATE -> WarningOrange
                                    WorkloadLevel.LOW -> SuccessGreen
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPrimary) TealPrimary else BackgroundLight
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isPrimary) Color.White else DarkText
        )
    }
}
