package com.syncup.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syncup.presentation.components.SimpleAvatar
import com.syncup.presentation.mock.MockData
import kotlinx.coroutines.launch

private val TealPrimary = Color(0xFF1DB584)
private val TealLight = Color(0xFFE8F5F3)
private val DarkText = Color(0xFF1A1A1A)
private val LightGray = Color(0xFFB0B0B0)

enum class MainTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    DASHBOARD("DASHBOARD", Icons.Filled.Home, Icons.Outlined.Home),
    TASKS("TASKS", Icons.Filled.CheckCircle, Icons.Outlined.CheckCircle),
    COLLABORATE("COLLABORATE", Icons.Filled.Email, Icons.Outlined.Email),
    TEAM("TEAM", Icons.Filled.Person, Icons.Outlined.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToProject: (String) -> Unit = {},
    onNavigateToInvite: () -> Unit = {},
    onNavigateToCreateMilestone: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToProjectManagement: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(MainTab.DASHBOARD) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = Color.White
            ) {
                DrawerContent(
                    onNavigateToProfile = {
                        scope.launch { drawerState.close() }
                        onNavigateToProfile()
                    },
                    onNavigateToInvite = {
                        scope.launch { drawerState.close() }
                        onNavigateToInvite()
                    },
                    onNavigateToProjectManagement = {
                        scope.launch { drawerState.close() }
                        onNavigateToProjectManagement()
                    },
                    onLogout = {
                        scope.launch { drawerState.close() }
                        onLogout()
                    },
                    onCloseDrawer = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Main Content
            when (selectedTab) {
                MainTab.DASHBOARD -> DashboardScreen(
                    onProjectClick = onNavigateToProject,
                    onInviteClick = onNavigateToInvite,
                    onProfileClick = onNavigateToProfile,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
                MainTab.TASKS -> TaskBoardScreen(
                    onBackClick = { selectedTab = MainTab.DASHBOARD },
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
                MainTab.COLLABORATE -> CollaborateScreen(
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
                MainTab.TEAM -> TeamScreen(
                    onInviteClick = onNavigateToInvite,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
            
            // Bottom Navigation
            MainBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun DrawerContent(
    onNavigateToProfile: () -> Unit,
    onNavigateToInvite: () -> Unit,
    onNavigateToProjectManagement: () -> Unit,
    onLogout: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    val currentUser = MockData.mockTeamMembers.first()
    
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .statusBarsPadding()
    ) {
        // Header with user info
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(TealPrimary)
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    SimpleAvatar(
                        name = "Alex Rivera",
                        size = 64.dp,
                        showBorder = true
                    )
                    
                    IconButton(onClick = onCloseDrawer) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Alex Rivera",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Text(
                    text = "alex.student@university.edu",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Box(
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "LEAD",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Menu Items
        DrawerMenuItem(
            icon = Icons.Default.Person,
            title = "My Profile",
            onClick = onNavigateToProfile
        )
        
        DrawerMenuItem(
            icon = Icons.Default.Home,
            title = "Dashboard",
            onClick = onCloseDrawer
        )
        
        DrawerMenuItem(
            icon = Icons.Default.CheckCircle,
            title = "My Tasks",
            onClick = onCloseDrawer
        )
        
        DrawerMenuItem(
            icon = Icons.Default.Person,
            title = "Team Members",
            onClick = onCloseDrawer
        )
        
        DrawerMenuItem(
            icon = Icons.Default.Add,
            title = "Invite Teammates",
            onClick = onNavigateToInvite
        )
        
        DrawerMenuItem(
            icon = Icons.Default.List,
            title = "Project Management",
            onClick = onNavigateToProjectManagement
        )
        
        Divider(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            color = Color(0xFFE0E0E0)
        )
        
        DrawerMenuItem(
            icon = Icons.Default.Settings,
            title = "Settings",
            onClick = { }
        )
        
        DrawerMenuItem(
            icon = Icons.Default.Info,
            title = "Help & Support",
            onClick = { }
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Logout at bottom
        DrawerMenuItem(
            icon = Icons.Default.ExitToApp,
            title = "Log Out",
            onClick = onLogout,
            tintColor = Color(0xFFE53935)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // App version
        Text(
            text = "SyncUp v1.0.0",
            fontSize = 12.sp,
            color = LightGray,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
        
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    tintColor: Color = DarkText
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = tintColor,
            modifier = Modifier.size(24.dp)
        )
        
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = tintColor
        )
    }
}

@Composable
fun MainBottomNavigation(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        MainTab.values().forEach { tab ->
            val isSelected = selectedTab == tab
            
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = tab.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        fontSize = 9.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        letterSpacing = 0.5.sp
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
}
