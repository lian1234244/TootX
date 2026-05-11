package com.root.toolbox

import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RootManager {
    /**
     * 检测设备是否已 Root 并授予了权限
     */
    fun isRootAvailable(): Boolean {
        return Shell.getShell().isRoot
    }

    /**
     * 执行 Root 命令并返回结果
     * @param command 要执行的命令，例如 "pm list packages"
     * @return 命令执行输出的字符串，多行用换行符分隔
     */
    suspend fun executeCommand(command: String): String {
        return withContext(Dispatchers.IO) {
            val result = Shell.cmd(command).exec()
            if (result.isSuccess) {
                result.out.joinToString("\n")
            } else {
                "Error: ${result.err.joinToString("\n")}"
            }
        }
    }
}
