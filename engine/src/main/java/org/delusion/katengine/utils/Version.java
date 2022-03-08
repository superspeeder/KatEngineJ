package org.delusion.katengine.utils;

import static org.lwjgl.vulkan.VK13.*;

public class Version {
    public final int major;
    public final int minor;
    public final int patch;

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public Version(int major, int minor) {
        this(major, minor, 0);
    }

    public Version(int major) {
        this(major, 0, 0);
    }

    public int makeVulkanVersion() {
        return VK_MAKE_API_VERSION(0, major, minor, patch);
    }
}
