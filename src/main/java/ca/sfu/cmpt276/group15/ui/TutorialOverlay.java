package ca.sfu.cmpt276.group15.ui;

import ca.sfu.cmpt276.group15.HackingGame;
import ca.sfu.cmpt276.group15.ui.menu.Menu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Overlay shown at the start of a game to explain the controls and objectives.
 * Pauses the board until the player dismisses it.
 */
public class TutorialOverlay extends Pane {

    /**
     * Creates the tutorial overlay.
     *
     * @param onDismiss called when the player clicks the start button
     */
    public TutorialOverlay(Runnable onDismiss) {
        // Semi-transparent full-screen background
        Rectangle bg = new Rectangle();
        bg.widthProperty().bind(this.widthProperty());
        bg.heightProperty().bind(this.heightProperty());
        bg.setFill(Color.rgb(0, 0, 0, 0.75));

        // Title
        Text title = new Text("HOW TO PLAY");
        title.setFont(ResourceManager.loadFont(28));
        title.setFill(Color.WHITE);

        // Tutorial rows
        VBox rows = new VBox(16,
            makeRow("data.png",           false, "Collect all 6 data packets to unlock the exit"),
            makeRow("exit_up.png",        true,  "Reach the exit to escape and win"),
            makeRow("decryption_key.png", true,  "Find the decryption key to unlock the server room door"),
            makeRow("sourcecode.png",     true,  "Grab source code for bonus points"),
            makeRow("firewall.png",       true,  "Avoid firewalls! They drain your data"),
            makeRow("antivirus.png",      true,  "Don't get caught by the antivirus!")
        );
        rows.setAlignment(Pos.CENTER_LEFT);

        // Dismiss button
        Button btnStart = new Button("GOT IT");
        btnStart.setGraphic(Menu.iconView("resume.png"));
        btnStart.setFont(ResourceManager.loadFont(18));
        btnStart.setStyle(Menu.BUTTON_STYLE);
        btnStart.setGraphicTextGap(8);
        btnStart.setOnAction(e -> onDismiss.run());

        // Card
        VBox card = new VBox(24, title, rows, btnStart);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(500);
        card.setPadding(new Insets(40));
        card.setBackground(new Background(new BackgroundFill(Color.rgb(10, 10, 10), null, null)));

        card.layoutXProperty().bind(this.widthProperty().subtract(card.maxWidthProperty()).divide(2));
        card.layoutYProperty().bind(this.heightProperty().subtract(490).divide(2));

        this.getChildren().addAll(bg, card);
        this.setPickOnBounds(false);
    }

    private HBox makeRow(String asset, boolean isSprite, String description) {
        ImageView icon = new ImageView(loadImage(asset, isSprite));
        icon.setFitWidth(32);
        icon.setFitHeight(32);
        icon.setPreserveRatio(true);

        Text label = new Text(description);
        label.setFont(ResourceManager.loadFont(14));
        label.setFill(Color.WHITE);
        label.setWrappingWidth(380);

        HBox row = new HBox(16, icon, label);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Image loadImage(String asset, boolean isSprite) {
        return isSprite ? ResourceManager.loadSprite(asset) : ResourceManager.loadIcon(asset);
    }
}
