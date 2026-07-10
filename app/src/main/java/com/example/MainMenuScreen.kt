package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.DeepSlate
import com.example.ui.theme.TextPrimary

@Composable
fun MainMenuScreen(onPlayClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSlate),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "LightFlow",
                fontSize = 48.sp,
                fontWeight = FontWeight.Light,
                color = TextPrimary,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "NETWORK OPTIMIZATION",
                fontSize = 12.sp,
                color = CyanAccent.copy(alpha = 0.8f),
                letterSpacing = 6.sp
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            Button(
                onClick = onPlayClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                modifier = Modifier.padding(horizontal = 32.dp).height(56.dp).fillMaxWidth(0.6f)
            ) {
                Text("EXECUTE FLOW", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DeepSlate, letterSpacing = 2.sp)
            }
        }
    }
}
