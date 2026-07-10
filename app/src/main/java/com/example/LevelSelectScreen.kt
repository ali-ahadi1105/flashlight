package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.DeepSlate
import com.example.ui.theme.DeepSlateSurface
import com.example.ui.theme.LimeAccent
import com.example.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelSelectScreen(
    viewModel: GameViewModel,
    onBack: () -> Unit,
    onLevelSelected: (Int) -> Unit
) {
    val unlockedLevel by viewModel.unlockedLevel.collectAsState(initial = 1)
    val levelScores by viewModel.levelScores.collectAsState(initial = emptyMap())
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SELECT SECTOR", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Light, letterSpacing = 2.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepSlate,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary.copy(alpha = 0.6f)
                )
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .background(DeepSlate)
                .padding(padding)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(300) { index ->
                val levelId = index + 1
                val isUnlocked = levelId <= unlockedLevel
                
                Button(
                    onClick = { if (isUnlocked) onLevelSelected(levelId) },
                    modifier = Modifier.aspectRatio(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isUnlocked) CyanAccent.copy(alpha = 0.2f) else DeepSlateSurface,
                        contentColor = if (isUnlocked) CyanAccent else TextPrimary.copy(alpha = 0.3f)
                    ),
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text(
                            text = "$levelId",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Light
                        )
                        if (isUnlocked) {
                            val stars = levelScores[levelId] ?: 0
                            Row(modifier = Modifier.padding(top = 4.dp)) {
                                for(i in 1..3) {
                                    Icon(
                                        Icons.Default.Star, 
                                        contentDescription = null, 
                                        modifier = Modifier.size(12.dp),
                                        tint = if (i <= stars) LimeAccent else TextPrimary.copy(alpha = 0.2f)
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
