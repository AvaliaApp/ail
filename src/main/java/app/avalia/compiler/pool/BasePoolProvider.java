package app.avalia.compiler.pool;

import app.avalia.compiler.pool.info.CommandPoolInfo;
import app.avalia.compiler.pool.info.InvokePoolInfo;

public class BasePoolProvider {

    private static String pluginName;
    private static String pluginVersion;

    public static String getPluginName() {
        return pluginName;
    }

    public static void setPluginName(String pluginName) {
        BasePoolProvider.pluginName = pluginName;
    }

    public static String getPluginVersion() {
        return pluginVersion;
    }

    public static void setPluginVersion(String pluginVersion) {
        BasePoolProvider.pluginVersion = pluginVersion;
    }

    private static final BasePool<Integer, InvokePoolInfo> INVOKE_BASE_POOL = new BasePool<>();
    private static final BasePool<String, CommandPoolInfo> COMMAND_BASE_POOL = new BasePool<>();

    public static BasePool<Integer, InvokePoolInfo> getInvokePool() {
        return INVOKE_BASE_POOL;
    }

    public static BasePool<String, CommandPoolInfo> getCommandPool() {
        return COMMAND_BASE_POOL;
    }
}
