package ca.sfu.cmpt201.group15.render.screen;

public abstract class Screen {
    private final Screen parent;

    public Screen(Screen parent) {
        this.parent = parent;
    }

    public boolean onClick(double mouseX, double mouseY) {
        return false;
    }

    public boolean onKeyPress(int keyCode) {
        return false;
    }

    public abstract void render(double mouseX, double mouseY);

    public Screen getParent() {
        return parent;
    }
}
