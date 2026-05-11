package com.root.toolbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import android.graphics.Canvas as NativeCanvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainActivityTheme {
                SplashScreen()
            }
        }
    }
}

@Composable
fun SplashScreen() {
    var animationPhase by remember { mutableStateOf(0) }
    var showMainScreen by remember { mutableStateOf(false) }
    
    val cursorAlpha = remember { Animatable(1f) }
    val logoRevealProgress = remember { Animatable(0f) }
    val scanProgress = remember { Animatable(0f) }
    val glowIntensity = remember { Animatable(0f) }
    val fadeOutAlpha = remember { Animatable(1f) }
    
    val glitchOffsetX = remember { Animatable(0f) }
    val glitchOffsetY = remember { Animatable(0f) }
    
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathingAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing"
    )

    LaunchedEffect(Unit) {
        launch {
            delay(300)
            repeat(2) {
                cursorAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
                delay(300)
                cursorAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
                delay(300)
            }
            
            animationPhase = 1
            
            launch {
                repeat(8) {
                    glitchOffsetX.animateTo(
                        targetValue = Random.nextFloat() * 6f - 3f,
                        animationSpec = tween(30)
                    )
                    glitchOffsetY.animateTo(
                        targetValue = Random.nextFloat() * 4f - 2f,
                        animationSpec = tween(25)
                    )
                    delay(40)
                }
                glitchOffsetX.animateTo(0f, animationSpec = tween(100))
                glitchOffsetY.animateTo(0f, animationSpec = tween(100))
            }
            
            logoRevealProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
            
            animationPhase = 2
            
            scanProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(1500, easing = LinearEasing)
            )
            
            glowIntensity.animateTo(
                targetValue = 1f,
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
            
            delay(1000)
            
            fadeOutAlpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            )
            
            showMainScreen = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (!showMainScreen) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                
                if (animationPhase == 0) {
                    drawCursor(
                        centerX = centerX,
                        centerY = centerY,
                        alpha = cursorAlpha.value
                    )
                }
                
                if (animationPhase >= 1) {
                    drawLogo(
                        centerX = centerX,
                        centerY = centerY,
                        revealProgress = logoRevealProgress.value,
                        scanProgress = scanProgress.value,
                        glowIntensity = glowIntensity.value,
                        breathingAlpha = if (animationPhase == 2 && scanProgress.value >= 1f) breathingAlpha else 0f,
                        glitchOffsetX = glitchOffsetX.value,
                        glitchOffsetY = glitchOffsetY.value,
                        fadeOutAlpha = fadeOutAlpha.value
                    )
                }
            }
        } else {
            MainPlaceholderScreen()
        }
    }
}

private fun DrawScope.drawCursor(
    centerX: Float,
    centerY: Float,
    alpha: Float
) {
    val cursorWidth = 40f
    val cursorHeight = 6f
    
    drawRect(
        color = Color(0xFF00FF41).copy(alpha = alpha),
        topLeft = Offset(centerX - cursorWidth / 2, centerY - cursorHeight / 2),
        size = androidx.compose.ui.geometry.Size(cursorWidth, cursorHeight)
    )
}

private fun DrawScope.drawLogo(
    centerX: Float,
    centerY: Float,
    revealProgress: Float,
    scanProgress: Float,
    glowIntensity: Float,
    breathingAlpha: Float,
    glitchOffsetX: Float,
    glitchOffsetY: Float,
    fadeOutAlpha: Float
) {
    val logoText = "ROOTX"
    val fontSize = 72.sp.toPx()
    val letterSpacingEm = 0.15f
    
    val textPaint = Paint().apply {
        textSize = fontSize
        letterSpacing = letterSpacingEm
        isFakeBoldText = true
        typeface = Typeface.MONOSPACE
    }
    
    val textBounds = Rect()
    textPaint.getTextBounds(logoText, 0, logoText.length, textBounds)
    val textWidth = textPaint.measureText(logoText)
    val startX = centerX - textWidth / 2
    
    val baseY = centerY + fontSize / 3 + glitchOffsetY
    
    val nativeCanvas = drawContext.canvas.nativeCanvas
    
    if (glowIntensity > 0f || breathingAlpha > 0f) {
        val glowAlpha = (glowIntensity * 0.5f + breathingAlpha * 0.3f) * fadeOutAlpha
        
        val glowPaint = Paint(textPaint).apply {
            color = Color(0xFF00FF41).copy(alpha = glowAlpha).toArgb()
            setShadowLayer(20f, 0f, 0f, Color(0xFF00FF41).copy(alpha = glowAlpha * 0.5f).toArgb())
        }
        nativeCanvas.drawText(
            logoText,
            startX + glitchOffsetX,
            baseY,
            glowPaint
        )
        
        for (i in 0 until 3) {
            val blurOffset = (i + 1) * 4f
            val blurAlpha = glowAlpha * (0.25f - i * 0.06f)
            val blurPaint = Paint(textPaint).apply {
                color = Color(0xFF00FF41).copy(alpha = blurAlpha).toArgb()
            }
            nativeCanvas.drawText(
                logoText,
                startX + glitchOffsetX + Random.nextFloat() * blurOffset - blurOffset / 2,
                baseY + Random.nextFloat() * blurOffset - blurOffset / 2,
                blurPaint
            )
        }
    }
    
    val scanLineX = startX + textWidth * scanProgress
    
    if (scanProgress in 0.01f..0.99f) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    Color(0xFF00FF41).copy(alpha = 0.8f),
                    Color(0xFF00FF41).copy(alpha = 0.8f),
                    Color.Transparent
                ),
                startY = centerY - fontSize,
                endY = centerY + fontSize
            ),
            topLeft = Offset(scanLineX - 3f, centerY - fontSize),
            size = androidx.compose.ui.geometry.Size(6f, fontSize * 2)
        )
        
        drawRect(
            color = Color(0xFF00FF41).copy(alpha = 0.3f * fadeOutAlpha),
            topLeft = Offset(scanLineX - 15f, centerY - fontSize),
            size = androidx.compose.ui.geometry.Size(30f, fontSize * 2)
        )
    }
    
    val mainPaint = Paint(textPaint).apply {
        color = Color.White.copy(alpha = fadeOutAlpha).toArgb()
    }
    nativeCanvas.drawText(
        logoText,
        startX + glitchOffsetX,
        baseY,
        mainPaint
    )
    
    if (revealProgress < 1f) {
        val tearWidth = textWidth * (1f - revealProgress) / 2
        drawRect(
            color = Color.Black,
            topLeft = Offset(startX - 10f, centerY - fontSize),
            size = androidx.compose.ui.geometry.Size(tearWidth + 10f, fontSize * 2)
        )
        drawRect(
            color = Color.Black,
            topLeft = Offset(centerX + textWidth / 2 - tearWidth, centerY - fontSize),
            size = androidx.compose.ui.geometry.Size(tearWidth + 10f, fontSize * 2)
        )
    }
    
    if (scanProgress >= 1f && glowIntensity >= 1f) {
        val lineY = baseY + fontSize * 0.6f
        drawRect(
            color = Color(0xFF00FF41).copy(alpha = 0.6f * fadeOutAlpha),
            topLeft = Offset(startX, lineY),
            size = androidx.compose.ui.geometry.Size(textWidth * breathingAlpha, 2f)
        )
    }
}

@Composable
fun MainPlaceholderScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
    }
}
