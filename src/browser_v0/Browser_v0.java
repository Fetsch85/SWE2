package browser_v0;

import javafx.application.Application;
import javafx.stage.Stage;

public class Browser_v0 extends Application {
    private BrowserView view;
    private BrowserController controller;
    private BrowserModel model;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Note the dependencies between model, view and controller. Additionally,
     * the view needs the primaryStage created by JavaFX.
     */

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the GUI
        model = new BrowserModel();
        view = new BrowserView(primaryStage, model);
        controller = new BrowserController(model, view);

        // Display the GUI after all initialization is complete
        view.start();
    }

    /**
     * The stop method is the opposite of the start method. It provides an
     * opportunity to close down the program gracefully, when the program has
     * been closed.
     */
    @Override
    public void stop() {
        if (view != null)
            view.stop();
    }
}
