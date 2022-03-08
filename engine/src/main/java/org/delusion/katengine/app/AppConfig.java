package org.delusion.katengine.app;

import org.delusion.katengine.GraphicsApi;
import org.delusion.katengine.KatEngine;
import org.delusion.katengine.utils.Version;
import org.delusion.katengine.window.WindowConfig;

public class AppConfig {
    public static final String DEFAULT_APP_NAME = "App";
    public static final Version DEFAULT_APP_VERSION = new Version(1, 0, 0);
    public static final int DEFAULT_MAX_FPS = -1;
    public static final boolean DEBUG_BY_DEFAULT = true;
    public static final WindowConfig DEFAULT_WINDOW_CONFIG = new WindowConfig()
            .size(800, 800)
            .title(DEFAULT_APP_NAME)
            .resizable(false);
    public static final GraphicsApi DEFAULT_GRAPHICS_API = GraphicsApi.OpenGL;



    public WindowConfig windowConfig = DEFAULT_WINDOW_CONFIG;
    public String appName = DEFAULT_APP_NAME;
    public Version appVersion = DEFAULT_APP_VERSION;
    public int maxFPS = DEFAULT_MAX_FPS; // if -1, then it is unlimited
    public GraphicsApi graphicsApi = DEFAULT_GRAPHICS_API;

    public boolean enableDebugging = DEBUG_BY_DEFAULT;
}
