package ca.sfu.cmpt201.group15.render.screen;

import ca.sfu.cmpt201.group15.GameOptions;

public class OptionsScreen extends Screen {
    private final GameOptions options;

    public OptionsScreen(Screen parent, GameOptions options) {
        super(parent);
        this.options = options;
    }

    @Override
    public boolean onClick(double mouseX, double mouseY) {
        return super.onClick(mouseX, mouseY);
    }

    @Override
    public boolean onKeyPress(int keyCode) {
        return super.onKeyPress(keyCode);
    }

    @Override
    public void render(double mouseX, double mouseY) {
    }
}
