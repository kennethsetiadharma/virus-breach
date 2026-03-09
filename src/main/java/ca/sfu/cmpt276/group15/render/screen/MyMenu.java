package ca.sfu.cmpt276.group15.render.screen;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.Updatable;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.scene.Scene;
import javafx.beans.property.DoubleProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;

public class MyMenu extends FXGLMenu {

    public MyMenu(@NotNull MenuType type) {
        super(type);
    }

    @Override
    public void bindSize(@NotNull DoubleProperty scaledWidth, @NotNull DoubleProperty scaledHeight, @NotNull DoubleProperty scaleRatioX, @NotNull DoubleProperty scaleRatioY) {
        super.bindSize(scaledWidth, scaledHeight, scaleRatioX, scaleRatioY);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
