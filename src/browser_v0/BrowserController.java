package browser_v0;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public class BrowserController {
    
    final private BrowserModel model;
    final private BrowserView view;
    
    protected BrowserController(BrowserModel model, BrowserView view) {
        this.model = model;
        this.view = view;
        
        // register ourselves to listen for button clicks
        view.btnGo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String ipAddress = view.txtIP.getText();
                Integer port = new Integer(view.txtPort.getText());
                String webPage = model.browse(ipAddress, port);
                view.txtWebPage.setText(webPage);
            }
        });

        // register ourselves to handle window-closing event
        view.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                view.stop();
                Platform.exit();
            }
        });
    }
}
