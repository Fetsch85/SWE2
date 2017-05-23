package whacamole_solution;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MoleButton extends Button implements Runnable, EventHandler<ActionEvent> {
    final static Image moleIcon = new Image(MoleButton.class.getResourceAsStream("mole.gif"));
    final static Image emptyIcon = new Image(MoleButton.class.getResourceAsStream("empty.gif"));

    private Whacamole mainProgram;
    private Thread t;
    private ImageView moleImage = new ImageView(moleIcon);
    private ImageView emptyImage = new ImageView(emptyIcon);

    public MoleButton(Whacamole mainProgram) {
        super();
        this.mainProgram = mainProgram;
        this.setGraphic(emptyImage);
        this.setDisable(true);
        this.setOnAction(this);

        t = new Thread(this);
        t.start();
    }

    @Override
    public void handle(ActionEvent event) {
        mainProgram.whack();
    }

    @Override
    public void run() {
        while (true) {
            this.setDisable(Math.random() < 0.7);
            Platform.runLater(() -> {
                if (this.isDisabled()) {
                    this.setGraphic(emptyImage);
                } else {
                    this.setGraphic(moleImage);
                }
            });
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
        }
    }
}
