package com.root.toolbox

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.sin
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainActivityTheme {
                RootXApp()
            }
        }
    }
}

// 管理员账号密码
private const val ADMIN_ACCOUNT = "3291908720"
private const val ADMIN_PASSWORD = "3291908720ye"

// SharedPreferences 键名
private const val PREFS_NAME = "rootx_prefs"
private const val KEY_REMEMBER = "remember_password"
private const val KEY_SAVED_ACCOUNT = "saved_account"
private const val KEY_SAVED_PASSWORD = "saved_password"

@Composable
fun RootXApp() {
    var screenState by remember { mutableStateOf("splash") }
    when (screenState) {
        "splash" -> ModernSplashScreen(onAnimationEnd = { screenState = "login" })
        "login" -> ModernLoginScreen(onLoginSuccess = { screenState = "dashboard" })
        "dashboard" -> DashboardScreen()
    }
}

// ==========================================
// 1. 现代科技感开屏动画
// ==========================================
data class GlowParticle(
    val x: Float,
    var y: Float,
    val speed: Float,
    val size: Float,
    val alpha: Float
)

@Composable
fun ModernSplashScreen(onAnimationEnd: () -> Unit) {
    var showLogo by remember { mutableStateOf(false) }
    var showTagline by remember { mutableStateOf(false) }
    val logoScale = remember { Animatable(0.3f) }
    val logoAlpha = remember { Animatable(0f) }
    val fadeAlpha = remember { Animatable(1f) }
    val glowPulse = remember { Animatable(0.5f) }
    
    val scope = rememberCoroutineScope()
    
    // 发光粒子
    val particles = remember {
        mutableStateListOf<GlowParticle>().apply {
            repeat(30) {
                add(GlowParticle(
                    x = Random.nextFloat() * 1200f,
                    y = Random.nextFloat() * 2400f,
                    speed = Random.nextFloat() * 2f + 0.5f,
                    size = Random.nextFloat() * 4f + 2f,
                    alpha = Random.nextFloat() * 0.5f + 0.2f
                ))
            }
        }
    }
    
    LaunchedEffect(Unit) {
        // 阶段 1：粒子动画 2 秒
        delay(2000)
        
        // 阶段 2：Logo 出现
        showLogo = true
        logoAlpha.animateTo(1f, tween(600, easing = FastOutSlowInEasing))
        logoScale.animateTo(1f, tween(600, easing = FastOutSlowInEasing))
        
        // 发光脉冲动画
        scope.launch {
            while (true) {
                glowPulse.animateTo(1f, tween(1000, easing = FastOutSlowInEasing))
                glowPulse.animateTo(0.5f, tween(1000, easing = FastOutSlowInEasing))
            }
        }
        
        delay(800)
        showTagline = true
        
        delay(2000)
        
        // 阶段 3：淡出
        fadeAlpha.animateTo(0f, tween(600, easing = FastOutSlowInEasing))
        onAnimationEnd()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0F1A),
                        Color(0xFF050A12),
                        Color.Black
                    )
                )
            )
            .graphicsLayer { alpha = fadeAlpha.value },
        contentAlignment = Alignment.Center
    ) {
        // 粒子背景
        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { p ->
                p.y -= p.speed
                if (p.y < -50f) {
                    p.y = size.height + 50f
                    p.x = Random.nextFloat() * size.width
                }
                drawCircle(
                    color = Color(0xFF00D4FF).copy(alpha = p.alpha * 0.6f),
                    radius = p.size,
                    center = Offset(p.x, p.y)
                )
            }
        }
        
        // Logo 区域
        if (showLogo) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer {
                    scaleX = logoScale.value
                    scaleY = logoScale.value
                    alpha = logoAlpha.value
                }
            ) {
                // 主标题 - 发光效果
                Text(
                    text = "ROOT X",
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        fontSize = 64.sp,
                        shadow = Shadow(
                            color = Color(0xFF00D4FF).copy(alpha = glowPulse.value),
                            blurRadius = 30f * glowPulse.value
                        )
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 副标题
                Text(
                    text = "系统工具箱",
                    style = TextStyle(
                        color = Color(0xFF00D4FF),
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp
                    )
                )
                
                // 版本号
                Text(
                    text = "v1.0.0",
                    style = TextStyle(
                        color = Color.Gray,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp
                    )
                )
            }
        }
        
        // 底部标语
        if (showTagline) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp)
            ) {
                Text(
                    text = "安全 · 高效 · 专业",
                    style = TextStyle(
                        color = Color.White.copy(alpha = 0.6f),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

// ==========================================
// 2. 现代登录界面
// ==========================================
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ModernLoginScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }
    
    var account by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberPassword by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    
    // 加载保存的账号密码
    LaunchedEffect(Unit) {
        rememberPassword = prefs.getBoolean(KEY_REMEMBER, false)
        if (rememberPassword) {
            account = prefs.getString(KEY_SAVED_ACCOUNT, "") ?: ""
            password = prefs.getString(KEY_SAVED_PASSWORD, "") ?: ""
        }
    }
    
    // 请求权限
    val permissionsToRequest = remember {
        mutableListOf<String>().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
                add(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toList()
    }
    val multiplePermissionsState = rememberMultiplePermissionsState(permissionsToRequest)
    
    LaunchedEffect(Unit) {
        multiplePermissionsState.launchMultiplePermissionRequest()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0F1A),
                        Color(0xFF050A12),
                        Color.Black
                    )
                )
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // 标题
            Text(
                text = "管理员登录",
                style = TextStyle(
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    shadow = Shadow(color = Color(0xFF00D4FF), blurRadius = 10f)
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "请输入管理员账号和密码",
                style = TextStyle(
                    color = Color.Gray,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // 账号输入框
            ModernTextField(
                value = account,
                onValueChange = { account = it },
                label = "账号",
                placeholder = "请输入管理员账号"
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // 密码输入框
            ModernTextField(
                value = password,
                onValueChange = { password = it },
                label = "密码",
                placeholder = "请输入密码",
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 记住密码
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        rememberPassword = !rememberPassword
                    }
            ) {
                Checkbox(
                    checked = rememberPassword,
                    onCheckedChange = { rememberPassword = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF00D4FF),
                        uncheckedColor = Color.Gray
                    )
                )
                Text(
                    text = "记住密码",
                    color = Color.Gray,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
            }
            
            // 错误提示
            if (errorMsg.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMsg,
                    color = Color.Red,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // 登录按钮
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(50.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = if (isLoading) {
                                listOf(Color.Gray, Color.Gray)
                            } else {
                                listOf(Color(0xFF00D4FF), Color(0xFF0099CC))
                            }
                        )
                    )
                    .clickable(enabled = !isLoading) {
                        if (account.isBlank() || password.isBlank()) {
                            errorMsg = "账号和密码不能为空"
                            return@clickable
                        }
                        
                        if (account == ADMIN_ACCOUNT && password == ADMIN_PASSWORD) {
                            isLoading = true
                            errorMsg = ""
                            
                            // 保存密码设置
                            prefs.edit()
                                .putBoolean(KEY_REMEMBER, rememberPassword)
                                .apply()
                            
                            if (rememberPassword) {
                                prefs.edit()
                                    .putString(KEY_SAVED_ACCOUNT, account)
                                    .putString(KEY_SAVED_PASSWORD, password)
                                    .apply()
                            } else {
                                prefs.edit()
                                    .remove(KEY_SAVED_ACCOUNT)
                                    .remove(KEY_SAVED_PASSWORD)
                                    .apply()
                            }
                            
                            // 模拟加载
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(500)
                                onLoginSuccess()
                            }
                        } else {
                            errorMsg = "账号或密码错误"
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isLoading) "登录中..." else "登 录",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// 现代化输入框组件
@Composable
fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(0.85f)
    ) {
        Text(
            text = label,
            color = Color(0xFF00D4FF),
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFF1A1F2E))
                .drawBehind {
                    drawRect(
                        color = Color(0xFF00D4FF).copy(alpha = 0.3f),
                        style = Stroke(width = 1.dp.toPx())
                    )
                }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = Color.Gray,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
            }
            
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                ),
                singleLine = true,
                visualTransformation = if (isPassword && !passwordVisible) {
                    PasswordVisualTransformation(mask = '•')
                } else {
                    VisualTransformation.None
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            // 密码可见切换按钮
            if (isPassword) {
                Text(
                    text = if (passwordVisible) "隐藏" else "显示",
                    color = Color(0xFF00D4FF),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { onTogglePassword() }
                )
            }
        }
    }
}

// ==========================================
// 3. 主控台界面
// ==========================================
@Composable
fun DashboardScreen() {
    var rootStatus by remember { mutableStateOf("正在检测 Root 权限...") }
    var systemInfo by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val result = Shell.cmd("id").exec()
                if (result.isSuccess) {
                    val output = result.out.joinToString("\n")
                    if (output.contains("uid=0")) {
                        rootStatus = "Root 权限：已获取 ✓"
                        systemInfo = output
                    } else {
                        rootStatus = "Root 权限：未获取 ✗"
                        systemInfo = "当前用户：$output"
                    }
                } else {
                    rootStatus = "Root 权限：未获取 ✗"
                    systemInfo = result.err.joinToString("\n")
                }
            } catch (e: Exception) {
                rootStatus = "Root 权限：检测失败 ✗"
                systemInfo = e.localizedMessage ?: "未知错误"
            } finally {
                isLoading = false
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0F1A),
                        Color(0xFF050A12),
                        Color.Black
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 标题
            Text(
                text = "系统控制台",
                style = TextStyle(
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    shadow = Shadow(color = Color(0xFF00D4FF), blurRadius = 10f)
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Root 状态卡片
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A1F2E))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Root 状态",
                        color = Color.Gray,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (isLoading) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color(0xFF00D4FF),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "正在检测...",
                                color = Color.White,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        Text(
                            text = rootStatus,
                            style = TextStyle(
                                color = if (rootStatus.contains("已获取")) Color(0xFF00FF88) else Color.Red,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 16.sp,
                                shadow = Shadow(
                                    color = if (rootStatus.contains("已获取")) Color.Green else Color.Red,
                                    blurRadius = 10f
                                )
                            )
                        )
                        
                        if (systemInfo.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = systemInfo,
                                color = Color.Gray,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
            
            // 功能区域标题
            Text(
                text = "功能模块",
                color = Color.Gray,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp
            )
            
            // 功能按钮网格
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FeatureButton("应用管理", "管理已安装应用")
                FeatureButton("文件管理", "浏览系统文件")
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FeatureButton("系统优化", "清理加速")
                FeatureButton("更多功能", "敬请期待")
            }
        }
    }
}

@Composable
fun FeatureButton(title: String, subtitle: String) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(80.dp)
            .background(Color(0xFF1A1F2E))
            .drawBehind {
                drawRect(
                    color = Color(0xFF00D4FF).copy(alpha = 0.2f),
                    style = Stroke(width = 1.dp.toPx())
                )
            }
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = subtitle,
                color = Color.Gray,
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp
            )
        }
    }
}