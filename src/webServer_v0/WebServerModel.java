package webServer_v0;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.concurrent.Task;

public class WebServerModel {
    private Integer port;
    private final Logger logger = Logger.getLogger("");
    
    final Task<Void> serverTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            try {
                ServerSocket listener = new ServerSocket(port, 10, null);
                logger.info("Listening on port " + port);

                while (true) {
                    // The "accept" method waits for a request, then creates a socket
                    // connected to the requesting client
                    Socket client = listener.accept();

                    logger.info("Request from client "
                            + client.getInetAddress().toString() + " for server "
                            + client.getLocalAddress().toString());

                    // Create input and output streams to talk to the client
                    BufferedReader inClient = new BufferedReader(new InputStreamReader(client
                            .getInputStream()));
                    PrintWriter outClient = new PrintWriter(client.getOutputStream());
                    
                    

                    // Send our reply using HTTP 1.0 - we could also use the "write" method
                    // Note: HTTP specifies \r\n line endings, though most programs don't care
                    outClient.print("HTTP/1.0 200 \r\n"); // Version and status
                    outClient.print("Content-Type: text/plain\r\n");
                    outClient.print("\r\n");
                    

                    // Read request from client and send it straight back
                    // An empty string (length 0) is the end of an HTTP request
                    StringBuilder received = new StringBuilder();
                    String inString;
                    while ((inString = inClient.readLine()) != null && inString.length() != 0) {
                        received.append(inString + "\n");
                    }
                    String outString = received.toString();
                    
                    
                    outClient.print(outString);              
                    logger.info("Request contents:\n" + outString);
                    
                    outClient.print("\r\n");
                    //outClient.print("Hello\r\n");
                    //outClient.print("\r\n");
                    
                    //get .txt File content 
                    BufferedReader inFile = new BufferedReader(new FileReader("C:/Users/Fetsch/Documents/text.txt"));
                    	String zeile = null;
                    	while ((zeile = inFile.readLine()) != null) {
                    		outClient.print(zeile + "\r\n");
                    	}
                    	
                    //get .png File
                    BufferedImage Img = ImageIO.read(new File("C:/Users/Fetsch/Documents/destiny.png"));
                    
                    outClient.print(Img);
                    
                    
                    outClient.flush(); // Be safe, always "flush"
                    outClient.close();
                    inClient.close();
                    client.close();
                }
            } catch (Exception e) {
                System.err.println(e);
            }
            return null;
        }
    };    
    
    /**
     * Called by the controller, to start the task, to serve web content
     */
    public void serveContent(Integer port) {
        this.port = port;
        new Thread(serverTask).start();
    }
}
