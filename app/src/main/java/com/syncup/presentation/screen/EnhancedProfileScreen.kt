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
import com.syncup.presentation.components.SimpleAvatar
import com.syncup.presentation.components.SvgAvatar
import com.syncup.presentation.mock.MockData

private val TealPrimary = Color(0xFF1DB584)
private val TealLight = Color(0xFFE8F5F3)
private val DarkText = Color(0xFF1A1A1A)
private val LightGray = Color(0xFFB0B0B0)
private val CriticalRed = Color(0xFFE53935)

@Composable
fun EnhancedProfileScreen(
    onBackClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val currentUser = MockData.mockTeamMembers.first()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Header with back button
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBackClick() },
                    tint = DarkText
                )
                
                Text(
                    text = "SyncUp",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TealPrimary
                )
                
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        modifier = Modifier.size(24.dp),
                        tint = DarkText
                    )
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(24.dp),
                        tint = DarkText
                    )
                }
            }
        }
        
        // Profile Picture and Info
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Picture with Lead Badge
                Box {
                    SvgAvatar(
                        name = "Alex Rivera",
                        size = 120.dp,
                        showBorder = true,
                        borderColor = TealPrimary
                    )
                    
                    // Lead Badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 10.dp)
                            .background(TealPrimary, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "LEAD",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = "Alex Rivera",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Lead Designer • Computer Science Senior",
                    fontSize = 14.sp,
                    color = LightGray
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Follow",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    
                    OutlinedButton(
                        onClick = { },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = DarkText
                        ),
                        border = ButtonDefaults.outlinedButtonBorder,
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Message",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
        
        item { Spacer(modifier = Modifier.height(16.dp)) }
        
        // Project Contribution Stats
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Project Contribution Stats",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        
                        Text(
                            text = "LIVE VIEW",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Stats Bars
                    ContributionBar(
                        icon = "📊",
                        label = "Tasks Completed",
                        percentage = currentUser.tasksCompleted,
                        color = TealPrimary
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    ContributionBar(
                        icon = "💻",
                        label = "Code Commits",
                        percentage = currentUser.codeCommits,
                        color = TealPrimary
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    ContributionBar(
                        icon = "✏️",
                        label = "Design Reviews",
                        percentage = currentUser.designReviews,
                        color = TealPrimary
                    )
                }
            }
        }
        
        item { Spacer(modifier = Modifier.height(16.dp)) }
        
        // Technical Skills
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Technical Skills",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Skills Chips - Using FlowRow-like layout
                    val skills = currentUser.skills
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            skills.take(3).forEach { skill ->
                                SkillChip(
                                    skill = skill,
                                    isHighlighted = skill == "UI/UX Design"
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            skills.drop(3).forEach { skill ->
                                SkillChip(skill = skill)
                            }
                        }
                    }
                }
            }
        }
        
        item { Spacer(modifier = Modifier.height(16.dp)) }
        
        // Manage Account Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    Text(
                        text = "Manage Account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        modifier = Modifier.padding(16.dp)
                    )
                    
                    AccountMenuItem(
                        icon = Icons.Default.Person,
                        title = "Edit Profile"
                    )
                    
                    Divider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 16.dp))
                    
                    AccountMenuItem(
                        icon = Icons.Default.Notifications,
                        title = "Notification Preferences"
                    )
                    
                    Divider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 16.dp))
                    
                    AccountMenuItem(
                        icon = Icons.Default.Share,
                        title = "Linked Accounts"
                    )
                    
                    Divider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(horizontal = 16.dp))
                    
                    // Logout
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLogout() }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Log Out",
                                tint = CriticalRed,
                                modifier = Modifier.size(24.dp)
                            )
                            
                            Text(
                                text = "Log Out",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = CriticalRed
                            )
                        }
                        
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Go",
                            tint = LightGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
        
        // Bottom Spacer
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun ContributionBar(
    icon: String,
    label: String,
    percentage: Int,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(icon, fontSize = 16.sp)
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = DarkText
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mini Progress Bar
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(6.dp)
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(3.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(percentage / 100f)
                            .background(color, RoundedCornerShape(3.dp))
                    )
                }
                
                Text(
                    text = "$percentage%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }
        }
    }
}

@Composable
fun SkillChip(
    skill: String,
    isHighlighted: Boolean = false
) {
    Box(
        modifier = Modifier
            .background(
                if (isHighlighted) TealLight else Color(0xFFF5F5F5),
                RoundedCornerShape(20.dp)
            )
            .border(
                width = if (isHighlighted) 1.dp else 0.dp,
                color = if (isHighlighted) TealPrimary else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = skill,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (isHighlighted) TealPrimary else DarkText
        )
    }
}

@Composable
fun AccountMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = DarkText,
                modifier = Modifier.size(24.dp)
            )
            
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText
            )
        }
        
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Go",
            tint = LightGray,
            modifier = Modifier.size(20.dp)
        )
    }
}
