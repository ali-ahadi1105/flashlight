package com.example

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.ui.draw.scale
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameplayScreen(
    viewModel: GameViewModel,
    onBack: () -> Unit,
    onNextLevel: () -> Unit
) {
    val state by viewModel.gameState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DeepSlate
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header: System Active & Time
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(CyanAccent, CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SYSTEM ACTIVE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 2.sp,
                        color = TextPrimary.copy(alpha = 0.6f)
                    )
                }
                IconButton(onClick = onBack, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary.copy(alpha = 0.6f)
                    )
                }
            }

            // Title and Moves Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "Level ",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Light,
                            color = TextPrimary
                        )
                        Text(
                            text = "${state.currentLevel?.id ?: ""}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = "Network Optimization",
                        fontSize = 14.sp,
                        color = CyanAccent.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .background(DeepSlateSurface.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .border(1.dp, DeepSlateSurface, RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "MOVES",
                            fontSize = 10.sp,
                            letterSpacing = 1.sp,
                            color = TextPrimary.copy(alpha = 0.6f)
                        )
                        val movesTaken = state.currentLevel?.moveLimit?.minus(state.movesLeft) ?: 0
                        val limit = state.currentLevel?.moveLimit ?: 0
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = movesTaken.toString().padStart(2, '0'),
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimary
                            )
                            Text(
                                text = "/${limit.toString().padStart(2, '0')}",
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimary.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Game Grid Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .aspectRatio(4f / 5f)
                    .background(DeepSlateSurface.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                    .border(1.dp, DeepSlateSurface, RoundedCornerShape(24.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background grid pattern could be drawn here, but we will just overlay GameGrid
                if (state.currentLevel != null) {
                    GameGrid(
                        level = state.currentLevel!!,
                        nodes = state.nodes,
                        paths = state.activePaths,
                        onNodeClick = { viewModel.rotateRouter(it) }
                    )
                }
            }
            
            // Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("BANDWIDTH", fontSize = 10.sp, letterSpacing = 1.sp, color = TextPrimary.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("1.2 GB/s", fontSize = 14.sp, fontFamily = FontFamily.Monospace, color = TextPrimary)
                }
                Box(modifier = Modifier.width(1.dp).height(32.dp).background(DeepSlateSurface))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("EFFICIENCY", fontSize = 10.sp, letterSpacing = 1.sp, color = TextPrimary.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("98.4%", fontSize = 14.sp, fontFamily = FontFamily.Monospace, color = LimeAccent)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Power-Up Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Overclock Button
                Button(
                    onClick = { viewModel.useExtraMoves() },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepSlateSurface),
                    border = BorderStroke(1.dp, if (state.extraMovesItemCount > 0) CyanAccent else DeepSlateSurface),
                    modifier = Modifier.weight(1f).height(64.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Add, contentDescription = "Add Moves", modifier = Modifier.size(16.dp), tint = CyanAccent)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("OVERCLOCK", fontSize = 10.sp, letterSpacing = 1.sp, color = TextPrimary)
                        }
                        Text("${state.extraMovesItemCount} left", fontSize = 10.sp, color = TextPrimary.copy(alpha=0.5f))
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))

                // Bypass Button
                Button(
                    onClick = { viewModel.toggleBypassMode() },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (state.bypassModeActive) MagentaAccent.copy(alpha=0.3f) else DeepSlateSurface),
                    border = BorderStroke(1.dp, if (state.bypassItemCount > 0) MagentaAccent else DeepSlateSurface),
                    modifier = Modifier.weight(1f).height(64.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LockOpen, contentDescription = "Unlock", modifier = Modifier.size(16.dp), tint = MagentaAccent)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("BYPASS", fontSize = 10.sp, letterSpacing = 1.sp, color = TextPrimary)
                        }
                        Text("${state.bypassItemCount} left", fontSize = 10.sp, color = TextPrimary.copy(alpha=0.5f))
                    }
                }
            }

            // Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(DeepSlateSurface, CircleShape)
                            .border(1.dp, DeepSlateSurface.copy(alpha = 0.5f), CircleShape)
                            .clickable { state.currentLevel?.id?.let { viewModel.loadLevel(it) } },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Restart", tint = TextPrimary)
                    }
                }

                Button(
                    onClick = {
                        if (state.isLevelCleared) {
                            onNextLevel()
                        }
                    },
                    modifier = Modifier.height(48.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.isLevelCleared) CyanAccent else DeepSlateSurface,
                        contentColor = if (state.isLevelCleared) DeepSlate else TextPrimary
                    )
                ) {
                    Text(
                        text = if (state.isLevelCleared) "NEXT LEVEL" else "EXECUTE FLOW",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
        
        // Level Failed Overlay
        if (state.isLevelFailed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "OVERLOAD",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.error,
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "NETWORK CAPACITY EXCEEDED",
                        fontSize = 12.sp,
                        letterSpacing = 2.sp,
                        color = TextPrimary.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { state.currentLevel?.id?.let { viewModel.loadLevel(it) } },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = CircleShape,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("REBOOT", fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    }
                }
            }
        }

        // Level Cleared Overlay
        if (state.isLevelCleared) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DeepSlate.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "NETWORK SECURED",
                        style = MaterialTheme.typography.headlineLarge,
                        color = CyanAccent,
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "REWARDS EARNED:",
                        fontSize = 12.sp,
                        letterSpacing = 2.sp,
                        color = TextPrimary.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "+1 OVERCLOCK",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanAccent
                    )
                    val currentLvl = state.currentLevel
                    if (currentLvl != null && currentLvl.id % 2 == 0) {
                        Text(
                            text = "+1 BYPASS",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MagentaAccent
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { onNextLevel() },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                        shape = CircleShape,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("PROCEED", fontWeight = FontWeight.Bold, letterSpacing = 2.sp, color = DeepSlate)
                    }
                }
            }
        }
    }
}

@Composable
fun GameGrid(
    level: Level,
    nodes: List<Node>,
    paths: List<PathSegment>,
    onNodeClick: (String) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Calculate cell size to fit the container
        val cWidth = maxWidth / level.gridWidth
        val cHeight = maxHeight / level.gridHeight
        
        // In our logical coordinates, X is column, Y is row
        val infiniteTransition = rememberInfiniteTransition()
        val pulseAnim by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        Box(
            modifier = Modifier.size(maxWidth, maxHeight)
        ) {
            // Draw grid dots (background)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val dotColor = Color.White.copy(alpha = 0.1f)
                val dotRadius = 1.dp.toPx()
                for (x in 0..level.gridWidth) {
                    for (y in 0..level.gridHeight) {
                        drawCircle(
                            color = dotColor,
                            radius = dotRadius,
                            center = Offset(x * cWidth.toPx(), y * cHeight.toPx())
                        )
                    }
                }
                
                // Draw Paths
                paths.forEach { segment ->
                    val start = Offset(
                        segment.startX * cWidth.toPx() + cWidth.toPx() / 2,
                        segment.startY * cHeight.toPx() + cHeight.toPx() / 2
                    )
                    val end = Offset(
                        segment.endX * cWidth.toPx() + cWidth.toPx() / 2,
                        segment.endY * cHeight.toPx() + cHeight.toPx() / 2
                    )
                    
                    // Glowing line effect
                    drawLine(
                        color = segment.color.color.copy(alpha = 0.3f),
                        start = start,
                        end = end,
                        strokeWidth = 10.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = segment.color.color,
                        start = start,
                        end = end,
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    
                    // Pulse Particle
                    val dx = end.x - start.x
                    val dy = end.y - start.y
                    val particleX = start.x + dx * pulseAnim
                    val particleY = start.y + dy * pulseAnim
                    drawCircle(
                        color = Color.White,
                        radius = 4.dp.toPx(),
                        center = Offset(particleX, particleY)
                    )
                }
            }

            // Draw Nodes
            nodes.forEach { node ->
                val scaleAnim = remember(level.id) { Animatable(0f) }
                LaunchedEffect(level.id) {
                    scaleAnim.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))
                }
                
                val baseAngle = remember(node.id) { node.direction?.toAngle() ?: 0f }
                val animatedRotation by animateFloatAsState(
                    targetValue = baseAngle + (node.rotations * 90f),
                    animationSpec = tween(300, easing = FastOutSlowInEasing),
                    label = "rotation"
                )
                
                Box(
                    modifier = Modifier
                        .size(cWidth, cHeight)
                        .offset(x = cWidth * node.x, y = cHeight * node.y)
                        .scale(scaleAnim.value)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            enabled = node.type == NodeType.ROUTER
                        ) {
                            onNodeClick(node.id)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                        val radius = size.minDimension / 2
                        
                        when (node.type) {
                            NodeType.SOURCE -> {
                                drawCircle(
                                    color = node.color.color.copy(alpha = 0.2f),
                                    radius = radius
                                )
                                drawCircle(
                                    color = node.color.color,
                                    radius = radius,
                                    style = Stroke(width = 2.dp.toPx())
                                )
                                drawCircle(
                                    color = node.color.color,
                                    radius = radius * 0.3f
                                )
                            }
                            NodeType.TARGET -> {
                                drawCircle(
                                    color = node.color.color.copy(alpha = 0.1f),
                                    radius = radius
                                )
                                drawCircle(
                                    color = node.color.color.copy(alpha = 0.5f),
                                    radius = radius,
                                    style = Stroke(
                                        width = 2.dp.toPx(), 
                                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                    )
                                )
                                if (node.isSatisfied) {
                                    drawCircle(
                                        color = node.color.color,
                                        radius = radius * 0.5f
                                    )
                                }
                            }
                            NodeType.ROUTER -> {
                                val isGlowing = paths.any { (it.endX == node.x && it.endY == node.y) || (it.startX == node.x && it.startY == node.y) }
                                val borderColor = if (node.isLocked) Color.Red.copy(alpha = 0.5f) else if (isGlowing) node.color.color else DeepSlateSurface.copy(alpha = 0.8f)
                                val cornerRad = 12.dp.toPx()
                                
                                drawRoundRect(
                                    color = DeepSlateSurface,
                                    topLeft = Offset(size.width / 2 - radius, size.height / 2 - radius),
                                    size = Size(radius * 2, radius * 2),
                                    cornerRadius = CornerRadius(cornerRad, cornerRad)
                                )
                                drawRoundRect(
                                    color = borderColor,
                                    topLeft = Offset(size.width / 2 - radius, size.height / 2 - radius),
                                    size = Size(radius * 2, radius * 2),
                                    cornerRadius = CornerRadius(cornerRad, cornerRad),
                                    style = Stroke(width = 2.dp.toPx())
                                )
                                
                                if (node.isLocked) {
                                    drawCircle(
                                        color = Color.Red.copy(alpha=0.3f),
                                        radius = radius * 0.8f
                                    )
                                }
                                
                                rotate(animatedRotation) {
                                    val lineRadius = radius * 0.6f
                                    val endX = size.width / 2 + lineRadius
                                    val endY = size.height / 2
                                    
                                    drawLine(
                                        color = if (isGlowing) node.color.color else Color.White,
                                        start = Offset(size.width / 2, size.height / 2),
                                        end = Offset(endX, endY),
                                        strokeWidth = 3.dp.toPx(),
                                        cap = StrokeCap.Round
                                    )
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}
