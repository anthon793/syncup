package com.syncup.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

private val TealPrimary = Color(0xFF1DB584)
private val TealLight = Color(0xFFE8F5F3)
private val TealDark = Color(0xFF158A64)

// Different avatar styles based on user characteristics
enum class AvatarStyle {
    GEOMETRIC,      // Circles and geometric shapes
    ABSTRACT,       // Abstract wave patterns
    INITIALS,       // Stylized initials with pattern
    ROBOT,          // Friendly robot face
    NATURE,         // Leaf/nature pattern
    GRADIENT_RINGS  // Concentric rings with gradient
}

@Composable
fun SvgAvatar(
    name: String,
    size: Dp = 48.dp,
    style: AvatarStyle = AvatarStyle.GEOMETRIC,
    showBorder: Boolean = true,
    borderColor: Color = TealPrimary,
    isOnline: Boolean = false,
    modifier: Modifier = Modifier
) {
    // Generate consistent style based on name hash
    val nameHash = name.hashCode()
    val actualStyle = AvatarStyle.values()[kotlin.math.abs(nameHash) % AvatarStyle.values().size]
    
    // Generate colors based on name
    val baseHue = (kotlin.math.abs(nameHash) % 60) + 140 // Keep in green-teal range
    val backgroundColor = Color.hsl(baseHue.toFloat(), 0.4f, 0.9f)
    val primaryColor = TealPrimary
    val secondaryColor = TealDark
    
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .then(
                    if (showBorder) Modifier.border(2.dp, borderColor, CircleShape)
                    else Modifier
                )
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(4.dp)) {
                when (actualStyle) {
                    AvatarStyle.GEOMETRIC -> drawGeometricAvatar(primaryColor, secondaryColor, nameHash)
                    AvatarStyle.ABSTRACT -> drawAbstractAvatar(primaryColor, secondaryColor, nameHash)
                    AvatarStyle.INITIALS -> drawInitialsBackground(primaryColor, secondaryColor, nameHash)
                    AvatarStyle.ROBOT -> drawRobotAvatar(primaryColor, secondaryColor)
                    AvatarStyle.NATURE -> drawNatureAvatar(primaryColor, secondaryColor, nameHash)
                    AvatarStyle.GRADIENT_RINGS -> drawGradientRingsAvatar(primaryColor, secondaryColor, nameHash)
                }
            }
            
            // Draw initials on top for INITIALS style
            if (actualStyle == AvatarStyle.INITIALS) {
                val initials = name.split(" ")
                    .take(2)
                    .mapNotNull { it.firstOrNull()?.uppercase() }
                    .joinToString("")
                Text(
                    text = initials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = (size.value * 0.35f).sp
                )
            }
        }
        
        // Online indicator
        if (isOnline) {
            Box(
                modifier = Modifier
                    .size(size * 0.25f)
                    .clip(CircleShape)
                    .background(TealPrimary)
                    .border(2.dp, Color.White, CircleShape)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

private fun DrawScope.drawGeometricAvatar(primary: Color, secondary: Color, seed: Int) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.minDimension / 2
    
    // Draw main circle pattern
    drawCircle(
        color = primary.copy(alpha = 0.3f),
        radius = radius * 0.8f,
        center = Offset(centerX, centerY)
    )
    
    // Draw smaller circles in pattern
    val count = (seed % 4) + 3
    for (i in 0 until count) {
        val angle = (i * 360f / count) * (Math.PI / 180)
        val x = centerX + (radius * 0.5f * cos(angle)).toFloat()
        val y = centerY + (radius * 0.5f * sin(angle)).toFloat()
        drawCircle(
            color = secondary.copy(alpha = 0.6f),
            radius = radius * 0.2f,
            center = Offset(x, y)
        )
    }
    
    // Center dot
    drawCircle(
        color = primary,
        radius = radius * 0.25f,
        center = Offset(centerX, centerY)
    )
}

private fun DrawScope.drawAbstractAvatar(primary: Color, secondary: Color, seed: Int) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.minDimension / 2
    
    // Draw wavy lines
    val waveCount = (seed % 3) + 2
    for (i in 0 until waveCount) {
        val yOffset = (i - waveCount / 2f) * (radius * 0.3f)
        val path = Path().apply {
            moveTo(0f, centerY + yOffset)
            for (x in 0..size.width.toInt() step 10) {
                val waveY = centerY + yOffset + (sin(x * 0.1 + seed) * radius * 0.15).toFloat()
                lineTo(x.toFloat(), waveY)
            }
        }
        drawPath(
            path = path,
            color = if (i % 2 == 0) primary else secondary,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

private fun DrawScope.drawInitialsBackground(primary: Color, secondary: Color, seed: Int) {
    // Fill with solid color
    drawCircle(
        color = primary,
        radius = size.minDimension / 2
    )
    
    // Add subtle pattern
    val patternCount = (seed % 5) + 3
    for (i in 0 until patternCount) {
        val angle = (i * 360f / patternCount + seed % 60) * (Math.PI / 180)
        val startX = size.width / 2
        val startY = size.height / 2
        val endX = startX + (size.minDimension * 0.4f * cos(angle)).toFloat()
        val endY = startY + (size.minDimension * 0.4f * sin(angle)).toFloat()
        drawLine(
            color = secondary.copy(alpha = 0.3f),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 2.dp.toPx()
        )
    }
}

private fun DrawScope.drawRobotAvatar(primary: Color, secondary: Color) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.minDimension / 2
    
    // Robot face background
    drawCircle(
        color = primary.copy(alpha = 0.2f),
        radius = radius * 0.9f,
        center = Offset(centerX, centerY)
    )
    
    // Eyes
    val eyeRadius = radius * 0.15f
    val eyeY = centerY - radius * 0.1f
    val eyeSpacing = radius * 0.35f
    
    // Left eye
    drawCircle(
        color = Color.White,
        radius = eyeRadius,
        center = Offset(centerX - eyeSpacing, eyeY)
    )
    drawCircle(
        color = primary,
        radius = eyeRadius * 0.5f,
        center = Offset(centerX - eyeSpacing, eyeY)
    )
    
    // Right eye
    drawCircle(
        color = Color.White,
        radius = eyeRadius,
        center = Offset(centerX + eyeSpacing, eyeY)
    )
    drawCircle(
        color = primary,
        radius = eyeRadius * 0.5f,
        center = Offset(centerX + eyeSpacing, eyeY)
    )
    
    // Smile
    drawArc(
        color = secondary,
        startAngle = 20f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(centerX - radius * 0.4f, centerY),
        size = Size(radius * 0.8f, radius * 0.5f),
        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
    )
    
    // Antenna
    drawLine(
        color = secondary,
        start = Offset(centerX, centerY - radius * 0.6f),
        end = Offset(centerX, centerY - radius * 0.85f),
        strokeWidth = 2.dp.toPx()
    )
    drawCircle(
        color = primary,
        radius = radius * 0.08f,
        center = Offset(centerX, centerY - radius * 0.9f)
    )
}

private fun DrawScope.drawNatureAvatar(primary: Color, secondary: Color, seed: Int) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.minDimension / 2
    
    // Draw leaf pattern
    val leafCount = (seed % 3) + 4
    for (i in 0 until leafCount) {
        val angle = (i * 360f / leafCount) * (Math.PI / 180)
        val leafCenterX = centerX + (radius * 0.35f * cos(angle)).toFloat()
        val leafCenterY = centerY + (radius * 0.35f * sin(angle)).toFloat()
        
        // Simple leaf shape
        drawOval(
            color = if (i % 2 == 0) primary.copy(alpha = 0.7f) else secondary.copy(alpha = 0.5f),
            topLeft = Offset(leafCenterX - radius * 0.15f, leafCenterY - radius * 0.25f),
            size = Size(radius * 0.3f, radius * 0.5f)
        )
    }
    
    // Center circle
    drawCircle(
        color = primary,
        radius = radius * 0.2f,
        center = Offset(centerX, centerY)
    )
}

private fun DrawScope.drawGradientRingsAvatar(primary: Color, secondary: Color, seed: Int) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.minDimension / 2
    
    // Draw concentric rings
    val ringCount = (seed % 3) + 3
    for (i in ringCount downTo 0) {
        val ringRadius = radius * (0.2f + (i * 0.2f))
        val alpha = 0.3f + (i * 0.15f)
        drawCircle(
            color = if (i % 2 == 0) primary.copy(alpha = alpha) else secondary.copy(alpha = alpha * 0.7f),
            radius = ringRadius,
            center = Offset(centerX, centerY),
            style = if (i > 0) Stroke(width = 4.dp.toPx()) else Fill
        )
    }
}

// Simple avatar with just initials and color
@Composable
fun SimpleAvatar(
    name: String,
    size: Dp = 48.dp,
    isOnline: Boolean = false,
    showBorder: Boolean = false,
    modifier: Modifier = Modifier
) {
    val initials = name.split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercase() }
        .joinToString("")
    
    // Generate color based on name
    val colorIndex = kotlin.math.abs(name.hashCode()) % avatarColors.size
    val backgroundColor = avatarColors[colorIndex]
    
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(backgroundColor)
                .then(
                    if (showBorder) Modifier.border(2.dp, TealPrimary, CircleShape)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.4f).sp
            )
        }
        
        if (isOnline) {
            Box(
                modifier = Modifier
                    .size(size * 0.25f)
                    .clip(CircleShape)
                    .background(TealPrimary)
                    .border(2.dp, Color.White, CircleShape)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

// Avatar color palette - diverse colors for unique team members
private val avatarColors = listOf(
    Color(0xFF1DB584),  // Teal (primary)
    Color(0xFF6366F1),  // Indigo
    Color(0xFFEC4899),  // Pink
    Color(0xFFF59E0B),  // Amber
    Color(0xFF8B5CF6),  // Purple
    Color(0xFF14B8A6),  // Cyan
    Color(0xFFEF4444),  // Red
    Color(0xFF3B82F6),  // Blue
    Color(0xFF10B981),  // Emerald
    Color(0xFFF97316),  // Orange
    Color(0xFF06B6D4),  // Sky
    Color(0xFFD946EF),  // Fuchsia
)

// Team avatar stack for showing multiple members
@Composable
fun AvatarStack(
    names: List<String>,
    maxDisplay: Int = 3,
    avatarSize: Dp = 32.dp,
    overlap: Dp = 8.dp,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(-overlap)
    ) {
        names.take(maxDisplay).forEachIndexed { index, name ->
            SimpleAvatar(
                name = name,
                size = avatarSize,
                showBorder = true,
                modifier = Modifier.border(2.dp, Color.White, CircleShape)
            )
        }
        
        if (names.size > maxDisplay) {
            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .clip(CircleShape)
                    .background(Color(0xFF9E9E9E))
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+${names.size - maxDisplay}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = (avatarSize.value * 0.35f).sp
                )
            }
        }
    }
}
