package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class TitleMenu extends FXGLMenu {

    public TitleMenu() {
        super(MenuType.MAIN_MENU);

        Button start = new Button("start game");
        start.setOnAction(this::startClicked);
        this.addChild(start);

        Button options = new Button("options");
        options.setOnAction(this::optionsClicked);
        options.setTranslateY(64);
        this.addChild(options);
    }

    private void optionsClicked(ActionEvent event) {
        FXGL.getSceneService().pushSubScene(new OptionsMenu(MenuType.MAIN_MENU, ((HackingGame) FXGL.getAppCast()).getOptions()));
    }

    private void startClicked(ActionEvent event) {
        fireNewGame();
    }
}
