package org.delusion.katengine.window;

import org.joml.Vector2i;

public class WindowConfig {
    public Vector2i size = new Vector2i(-1, -1);
    public String title = "";
    public boolean resizable = true;


    public Vector2i size() {
        return size;
    }

    public WindowConfig size(Vector2i size) {
        this.size.set(size);
        return this;
    }

    public WindowConfig size(int width, int height) {
        this.size.set(width, height);
        return this;
    }

    public String title() {
        return title;
    }

    public WindowConfig title(String title) {
        this.title = title;
        return this;
    }

    public boolean resizable() {
        return resizable;
    }

    public WindowConfig resizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }
}
