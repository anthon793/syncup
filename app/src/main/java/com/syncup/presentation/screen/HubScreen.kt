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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.syncup.domain.model.Project
import com.syncup.presentation.mock.MockData
import com.syncup.presentation.mock.TeamMember
import com.syncup.presentation.viewmodel.HubViewModel

private val TealPrimary = Color(0xFF1DB584)
private val TealLight = Color(0xFFE8F5F3)
private val DarkText = Color(0xFF1A1A1A)
private val LightGray = Color(0xFFB0B0B0)
private val CardBackground = Color(0xFFFAFAFA)

@Composable
fun HubScreenWithNavigation(
    viewModel: HubViewModel = hiltViewModel(),
    onProjectClick: (String) -> Unit = {},
    onNavigateToBoard: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    var showAddTeammateDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        when (selectedTab) {
            0 -> HubScreenContent(
                onProjectClick = onProjectClick,
                onAddTeammate = { showAddTeammateDialog = true }
            )
            1 -> BoardScreen()
            2 -> FilesScreen()
            3 -> ProfileScreen()
        }

        if (showAddTeammateDialog) {
            AddTeammateDialog(onDismiss = { showAddTeammateDialog = false })
        }

        BottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun HubScreenContent(
    onProjectClick: (String) -> Unit = {},
    onAddTeammate: () -> Unit = {}
) {
    val projects = MockData.mockProjects
    val teamMembers = MockData.mockTeamMembers.drop(1) // Skip current user, get teammates

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TealLight)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            HeaderSection()
        }

        item {
            ActiveTeammatesSection(teamMembers, onAddTeammate)
        }

        item {
            PriorityTaskCard()
        }

        item {
            ActiveProjectsSection(projects, onProjectClick)
        }

        item {
            CollaborationFeedSection()
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1DB584)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AS",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Column(
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Text(
                    text = "THE LEAD",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightGray,
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

        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = DarkText,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun ActiveTeammatesSection(teammates: List<TeamMember>, onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ACTIVE TEAMMATES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = LightGray,
                letterSpacing = 1.sp
            )
            Text(
                text = "4 Online",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimary
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(teammates) { teammate ->
                TeammateAvatar(teammate)
            }

            item {
                InviteButton(onAddClick)
            }
        }
    }
}

@Composable
fun TeammateAvatar(teammate: TeamMember) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color(teammate.avatarColorValue)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = teammate.avatar,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Online indicator as separate element
        if (teammate.isOnline) {
            Box(
                modifier = Modifier
                    .offset(y = (-12).dp)
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
                    .border(2.dp, Color.White, CircleShape)
            )
        }

        Text(
            text = teammate.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = DarkText,
            modifier = Modifier.padding(top = if (teammate.isOnline) 0.dp else 8.dp)
        )
    }
}

@Composable
fun InviteButton(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = LightGray,
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = "Invite",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = LightGray,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun PriorityTaskCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFFFE5B4), shape = RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "PRIORITY HIGH",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF9800)
                    )
                }

                Text(
                    text = "Marketing Capstone",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkText,
                    modifier = Modifier.padding(top = 12.dp)
                )

                Text(
                    text = "Final Presentation",
                    fontSize = 14.sp,
                    color = LightGray,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(-8.dp)
                ) {
                    // Sarah's avatar
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6366F1))
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "S",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    // Jamie's avatar
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEC4899))
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "J",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(LightGray)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+3", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = 0.35f,
                    modifier = Modifier.size(90.dp),
                    color = TealPrimary,
                    trackColor = Color(0xFFE0E0E0),
                    strokeWidth = 6.dp
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.size(90.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(DarkText),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "d",
                                fontSize = 16.sp,
                                color = TealPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "LEFT",
                                fontSize = 8.sp,
                                color = TealPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveProjectsSection(projects: List<Project>, onProjectClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Active Projects",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
            Text(
                text = "See All",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimary
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(projects) { project ->
                ProjectCard(project)
            }
        }
    }
}

@Composable
fun ProjectCard(project: Project) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(240.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2D5F5F),
                            Color(0xFF1A3A3A)
                        )
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        color = Color(0xFF2D5F5F),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    ),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    text = if (project.name.contains("Capstone")) "BIO 101" else "DES X",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1A3A3A)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📷", fontSize = 36.sp)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = if (project.name.contains("Capstone")) "Biology Lab Report" else "UX Design",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(
                        text = "Group A • Due Friday",
                        fontSize = 12.sp,
                        color = LightGray
                    )
                }

                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Progress",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = LightGray
                        )
                        Text(
                            text = "60%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                    }

                    LinearProgressIndicator(
                        progress = 0.6f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = TealPrimary,
                        trackColor = Color(0xFFE0E0E0)
                    )
                }
            }
        }
    }
}

@Composable
fun CollaborationFeedSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Collaboration Feed",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        CollaborationFeedItem(
            name = "Sarah",
            action = "completed a task",
            description = "Marked 'Competitor Analysis' as done in Marketing Capstone.",
            time = "2m ago",
            icon = "✅"
        )

        CollaborationFeedItem(
            name = "Jamie",
            action = "uploaded assets",
            description = "Final_Report_Draft_v2.pdf",
            time = "1h ago",
            icon = "📄"
        )

        CollaborationFeedItem(
            name = "Mike",
            action = "flagged a blocker",
            description = "\"I can't access the shared drive folder for UX...\"",
            time = "3h ago",
            icon = "⚠️"
        )
    }
}

@Composable
fun CollaborationFeedItem(
    name: String,
    action: String,
    description: String,
    time: String,
    icon: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Generate color from name hash
        val avatarColor = when (kotlin.math.abs(name.hashCode()) % 8) {
            0 -> Color(0xFF1DB584)
            1 -> Color(0xFF6366F1)
            2 -> Color(0xFFEC4899)
            3 -> Color(0xFFF59E0B)
            4 -> Color(0xFF8B5CF6)
            5 -> Color(0xFF14B8A6)
            6 -> Color(0xFF3B82F6)
            else -> Color(0xFFF97316)
        }
        val initials = name.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("")
        
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(avatarColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "$name $action",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
            Text(
                text = description,
                fontSize = 13.sp,
                color = LightGray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Text(
            text = time,
            fontSize = 12.sp,
            color = LightGray
        )
    }
}

@Composable
fun ScheduleScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TealLight)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(TealPrimary)
                .padding(16.dp)
        ) {
            Text(
                text = "Schedule",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📅",
                fontSize = 48.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Schedule Coming Soon",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
            Text(
                text = "View your upcoming deadlines and events here",
                fontSize = 14.sp,
                color = LightGray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun FilesScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Header
        item {
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
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Capstone Project A",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "Team SyncUp",
                            fontSize = 12.sp,
                            color = LightGray
                        )
                    }
                    
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        
        // File Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FileChip(icon = "📄", name = "Brief.pdf", color = Color(0xFFFF6B6B))
                FileChip(icon = "🎨", name = "UI_Kit.fig", color = Color(0xFF9B59B6))
                FileChip(icon = "📊", name = "Data.xls", color = Color(0xFF27AE60))
            }
        }
        
        // Date Separator
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Today, Oct 24",
                    fontSize = 12.sp,
                    color = LightGray,
                    modifier = Modifier
                        .background(Color(0xFFE8E8E8), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
        
        // Upload Activity
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF6366F1), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SJ",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Row {
                            Text(
                                text = "Sarah Jenkins",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                            Text(
                                text = " uploaded ",
                                fontSize = 14.sp,
                                color = LightGray
                            )
                            Text(
                                text = "UI Style Guide v2.pdf",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkText
                            )
                        }
                        Text(
                            text = "to Shared Files.",
                            fontSize = 14.sp,
                            color = LightGray
                        )
                    }
                }
            }
        }
        
        // Milestone Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color(0xFF6366F1), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "SJ",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Column {
                                    Text(
                                        text = "Sarah Jenkins",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkText
                                    )
                                    Text(
                                        text = "10:42 AM",
                                        fontSize = 11.sp,
                                        color = LightGray
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "🏁 MILESTONE REACHED",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TealPrimary
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Text(
                                text = "Research Phase Complete",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Phase 1 is officially done! I've compiled all the sources and citations into the shared doc. We are ready to move to drafting.",
                                fontSize = 13.sp,
                                color = Color(0xFF666666),
                                lineHeight = 18.sp
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Audio Waveform Placeholder
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .background(Color(0xFF1A1A2E), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    repeat(30) { index ->
                                        Box(
                                            modifier = Modifier
                                                .width(4.dp)
                                                .height((20..90).random().dp)
                                                .background(
                                                    Color(0xFF4DD4AC).copy(alpha = 0.7f),
                                                    RoundedCornerShape(2.dp)
                                                )
                                        )
                                    }
                                }
                            }
                        }
                        
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(TealPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✓", fontSize = 14.sp, color = Color.White)
                        }
                    }
                }
            }
        }
        
        // Comment
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFF59E0B), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "MC",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = "Mike Chen",
                            fontSize = 12.sp,
                            color = LightGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Great work Sarah! 👏 I'll start reviewing the outline tonight.",
                            fontSize = 14.sp,
                            color = DarkText,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
        
        // Inactivity Alert Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E6)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFFFF3E0), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Alert",
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column {
                            Text(
                                text = "Team Inactivity Alert",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "The \"Front-end\" subgroup hasn't posted an update in 48 hours. Keep the momentum going!",
                                fontSize = 12.sp,
                                color = Color(0xFF666666),
                                lineHeight = 16.sp
                            )
                        }
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFF0F0F0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Team",
                            tint = Color(0xFF666666),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Send a Friendly Nudge ▶",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkText
                    )
                }
            }
        }
        
        // Deadline Risk Message
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(2.dp, Color(0xFFFFB74D), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "11:15 AM",
                                    fontSize = 11.sp,
                                    color = LightGray
                                )
                                
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "⚠️ DEADLINE RISK",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFF9800)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(4.dp))
                                
                                Text(
                                    text = "Formatting Issues",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkText
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = "I'm stuck on the table alignment. The export keeps breaking. Can someone help?",
                                    fontSize = 13.sp,
                                    color = Color(0xFF666666),
                                    lineHeight = 18.sp
                                )
                            }
                            
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color(0xFF1DB584), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "AR",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        
                        Text(
                            text = "Alex Rivera",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkText,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
        
        // Response Message
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .background(Color(0xFF1A1A2E), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "I can hop on a call in 15 mins to help you fix that, Alex.",
                        fontSize = 13.sp,
                        color = Color.White,
                        lineHeight = 18.sp
                    )
                }
            }
        }
        
        // FAB Spacer
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
    
    // Floating Action Button
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp, end = 16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = { },
            containerColor = TealPrimary,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }
    }
}

@Composable
fun FileChip(icon: String, name: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = name,
                fontSize = 11.sp,
                color = DarkText,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProfileScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Header Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TealPrimary)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Profile Picture
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile Picture",
                            tint = TealPrimary,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Alex Johnson",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Text(
                        text = "alex.johnson@university.edu",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "Edit Profile",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TealPrimary
                        )
                    }
                }
            }
        }
        
        // Stats Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 20.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStatItem(number = "12", label = "Projects")
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(Color(0xFFE0E0E0))
                )
                ProfileStatItem(number = "48", label = "Tasks Done")
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(Color(0xFFE0E0E0))
                )
                ProfileStatItem(number = "89%", label = "On Time")
            }
        }
        
        // Spacer
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Account Section
        item {
            Text(
                text = "Account",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LightGray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    ProfileMenuItem(icon = Icons.Filled.Person, title = "Personal Information", subtitle = "Update your details")
                    Divider(color = Color(0xFFF0F0F0))
                    ProfileMenuItem(icon = Icons.Filled.Notifications, title = "Notifications", subtitle = "Manage your alerts")
                    Divider(color = Color(0xFFF0F0F0))
                    ProfileMenuItem(icon = Icons.Filled.Lock, title = "Privacy & Security", subtitle = "Control your data")
                }
            }
        }
        
        // Spacer
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Preferences Section
        item {
            Text(
                text = "Preferences",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LightGray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    ProfileMenuItem(icon = Icons.Filled.Settings, title = "Appearance", subtitle = "Theme and display")
                    Divider(color = Color(0xFFF0F0F0))
                    ProfileMenuItem(icon = Icons.Filled.Settings, title = "Language", subtitle = "English")
                    Divider(color = Color(0xFFF0F0F0))
                    ProfileMenuItem(icon = Icons.Filled.Favorite, title = "Analytics", subtitle = "View your progress")
                }
            }
        }
        
        // Spacer
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // About Section
        item {
            Text(
                text = "About",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LightGray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    ProfileMenuItem(icon = Icons.Filled.Info, title = "Help & Support", subtitle = "Get assistance")
                    Divider(color = Color(0xFFF0F0F0))
                    ProfileMenuItem(icon = Icons.Filled.Info, title = "Terms & Conditions", subtitle = "Legal information")
                    Divider(color = Color(0xFFF0F0F0))
                    ProfileMenuItem(icon = Icons.Filled.Star, title = "Rate SyncUp", subtitle = "Share your feedback")
                }
            }
        }
        
        // Spacer
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Logout Button
        item {
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = "Log Out",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Log Out",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
        
        // Version Info
        item {
            Text(
                text = "SyncUp v1.0.0",
                fontSize = 12.sp,
                color = LightGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
        
        // Bottom Spacer
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun ProfileStatItem(number: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = number,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TealPrimary
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = LightGray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun ProfileMenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
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
                    .size(40.dp)
                    .background(TealLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = TealPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Column {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkText
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = LightGray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        
        Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = "Navigate",
            tint = LightGray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun AddTeammateDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Invite Teammate",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = DarkText
                    )
                }
            }
        },
        text = {
            Column {
                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Email Address") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color.White
                    )
                )

                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color.White
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
            ) {
                Text("Send Invite", color = Color.White)
            }
        },
        containerColor = Color.White
    )
}

@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Hub",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Hub",
                    fontSize = 10.sp,
                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TealPrimary,
                unselectedIconColor = LightGray,
                selectedTextColor = TealPrimary,
                unselectedTextColor = LightGray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == 1) Icons.Filled.DateRange else Icons.Outlined.DateRange,
                    contentDescription = "Schedule",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Schedule",
                    fontSize = 10.sp,
                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TealPrimary,
                unselectedIconColor = LightGray,
                selectedTextColor = TealPrimary,
                unselectedTextColor = LightGray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == 2) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Files",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Files",
                    fontSize = 10.sp,
                    fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TealPrimary,
                unselectedIconColor = LightGray,
                selectedTextColor = TealPrimary,
                unselectedTextColor = LightGray,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            icon = {
                Icon(
                    imageVector = if (selectedTab == 3) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Me",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Me",
                    fontSize = 10.sp,
                    fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = TealPrimary,
                unselectedIconColor = LightGray,
                selectedTextColor = TealPrimary,
                unselectedTextColor = LightGray,
                indicatorColor = Color.Transparent
            )
        )
    }
}
