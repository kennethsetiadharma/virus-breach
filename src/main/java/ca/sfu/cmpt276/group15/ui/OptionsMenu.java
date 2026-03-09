package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.GameOptions;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class OptionsMenu extends FXGLMenu {
    private final GameOptions options;

    public OptionsMenu(MenuType menuType, GameOptions options) {
        super(menuType);
        this.options = options;

        this.addChild(new Rectangle(100, 100, Color.BLUEVIOLET));
        Button back = new Button("back");
        back.setOnAction(this::backPressed);
        this.addChild(back);
    }

    private void backPressed(ActionEvent action) {
        FXGL.getSceneService().popSubScene();
    }
}
