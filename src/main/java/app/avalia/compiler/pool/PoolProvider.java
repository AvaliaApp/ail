package app.avalia.compiler.pool;

import app.avalia.compiler.pool.data.CommandPoolInfo;
import app.avalia.compiler.pool.data.EventPoolInfo;
import app.avalia.compiler.pool.data.InvokePoolInfo;

public class PoolProvider {

    private static String pluginName;
    private static String pluginVersion;

    public static String getPluginName() {
        return pluginName;
    }

    public static void setPluginName(String pluginName) {
        PoolProvider.pluginName = pluginName;
    }

    public static String getPluginVersion() {
        return pluginVersion;
    }

    public static void setPluginVersion(String pluginVersion) {
        PoolProvider.pluginVersion = pluginVersion;
    }

    private static final Pool<Integer, InvokePoolInfo> invokePool = new Pool<>();
    private static final Pool<String, EventPoolInfo> eventPool = new Pool<>();
    private static final Pool<String, CommandPoolInfo> commandPool = new Pool<>();

    public static Pool<Integer, InvokePoolInfo> getInvokePool() {
        return invokePool;
    }

    public static Pool<String, EventPoolInfo> getEventPool() {
        return eventPool;
    }

    public static Pool<String, CommandPoolInfo> getCommandPool() {
        return commandPool;
    }
}
