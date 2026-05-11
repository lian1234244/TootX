package com.root.toolbox
import android.app.Application
import com.topjohnwu.superuser.Shell
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // 全局初始化Shell构建器，禁用可能导致崩溃的挂起机制
        Shell.setDefaultBuilder(
            Shell.Builder.create()
                .setFlags(Shell.FLAG_REDIRECT_STDERR)
                .setTimeout(10)
        )
    }
}