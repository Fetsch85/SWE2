package browser_v1;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class BrowserView {
    private BrowserModel model;
    private Stage stage;

    protected Label lblURL = new Label("URL");
    protected TextField txtURL = new TextField("http://127.0.0.1:8080/");
    protected Button btnGo = new Button("Go");
    protected TextArea txtWebPage = new TextArea();
    
    protected BrowserView(Stage stage, BrowserModel model) {
        this.stage = stage;
        this.model = model;
        
        stage.setTitle("Browser v1");
        
        BorderPane root = new BorderPane();

        HBox topBox = new HBox();
        topBox.setId("TopBox");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        root.setTop(topBox);
        
        topBox.getChildren().addAll(lblURL, txtURL, spacer, btnGo);
        txtURL.setId("URL");
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);
        scrollPane.setContent(txtWebPage);
        txtWebPage.setWrapText(true);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("Browser.css").toExternalForm());
        stage.setScene(scene);;
    }
    
    public void start() {
        stage.show();
    }
    
    /**
     * Stopping the view - just make it invisible
     */
    public void stop() {
        stage.hide();
    }
    
    /**
     * Getter for the stage, so that the controller can access window events
     */
    public Stage getStage() {
        return stage;
    }
}
