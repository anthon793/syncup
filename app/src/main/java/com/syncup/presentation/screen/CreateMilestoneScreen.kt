package com.syncup.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TealPrimary = Color(0xFF1DB584)
private val TealLight = Color(0xFFE8F5F3)
private val DarkText = Color(0xFF1A1A1A)
private val LightGray = Color(0xFFB0B0B0)
private val WarningOrange = Color(0xFFFF9800)

@Composable
fun CreateMilestoneScreen(
    onBackClick: () -> Unit = {},
    onCreateMilestone: () -> Unit = {}
) {
    var milestoneTitle by remember { mutableStateOf("") }
    var targetDate by remember { mutableStateOf("Oct 24, 2023") }
    var isCriticalPriority by remember { mutableStateOf(false) }
    var selectedSubGroups by remember { mutableStateOf(setOf("UI/UX")) }
    
    val subGroups = listOf("UI/UX", "Development", "Marketing", "QA")
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        TealLight,
                        Color.White,
                        Color(0xFFFFF9E6).copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                    text = "New Milestone",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                
                Spacer(modifier = Modifier.size(24.dp))
            }
            
            // Title and Subtitle
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Create New Milestone",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Sync your progress with the team",
                    fontSize = 14.sp,
                    color = LightGray
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Form Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Milestone Title
                    Text(
                        text = "MILESTONE TITLE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightGray,
                        letterSpacing = 1.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = milestoneTitle,
                        onValueChange = { milestoneTitle = it },
                        placeholder = { 
                            Text(
                                "e.g., Final Prototype",
                                color = LightGray
                            ) 
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TealPrimary,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color(0xFFF8F9FA),
                            unfocusedContainerColor = Color(0xFFF8F9FA)
                        ),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Target Date
                    Text(
                        text = "TARGET DATE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightGray,
                        letterSpacing = 1.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = targetDate,
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TealPrimary,
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color(0xFFF8F9FA),
                            unfocusedContainerColor = Color(0xFFF8F9FA)
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date",
                                tint = TealPrimary
                            )
                        },
                        singleLine = true,
                        readOnly = true
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Associate Sub-Groups
                    Text(
                        text = "ASSOCIATE SUB-GROUPS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightGray,
                        letterSpacing = 1.sp
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Sub-Group Chips - First Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        subGroups.take(2).forEach { group ->
                            SubGroupChip(
                                name = group,
                                isSelected = selectedSubGroups.contains(group),
                                onToggle = {
                                    selectedSubGroups = if (selectedSubGroups.contains(group)) {
                                        selectedSubGroups - group
                                    } else {
                                        selectedSubGroups + group
                                    }
                                }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Sub-Group Chips - Second Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        subGroups.drop(2).forEach { group ->
                            SubGroupChip(
                                name = group,
                                isSelected = selectedSubGroups.contains(group),
                                onToggle = {
                                    selectedSubGroups = if (selectedSubGroups.contains(group)) {
                                        selectedSubGroups - group
                                    } else {
                                        selectedSubGroups + group
                                    }
                                }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Divider(color = Color(0xFFF0F0F0))
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Critical Priority Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Critical Priority",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                            Text(
                                text = "Alerts all team members immediately",
                                fontSize = 12.sp,
                                color = LightGray
                            )
                        }
                        
                        Switch(
                            checked = isCriticalPriority,
                            onCheckedChange = { isCriticalPriority = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = TealPrimary,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFE0E0E0)
                            )
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E6))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color(0xFF90CAF9), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("ℹ", fontSize = 14.sp, color = Color.White)
                    }
                    
                    Text(
                        text = buildString {
                            append("Setting a ")
                            append("Critical")
                            append(" milestone will override member notification preferences for this event.")
                        },
                        fontSize = 13.sp,
                        color = DarkText
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Create Button
            Button(
                onClick = {
                    onCreateMilestone()
                    onBackClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
            ) {
                Text(
                    text = "Set Milestone",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("🚀", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun SubGroupChip(
    name: String,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) TealPrimary else Color.White)
            .border(
                width = 1.dp,
                color = if (isSelected) TealPrimary else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onToggle() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color.White else DarkText
        )
    }
}
