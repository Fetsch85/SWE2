package browser_v1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class BrowserModel {
    public String browse(String urlString) {
        BufferedReader inReader = null;
        String lineIn;
        StringBuffer urlContent = new StringBuffer();

        // Network errors are always possible
        try {
            // Create a new URL-Object
            URL url = new URL(urlString);

            // Set up the reader classes
            InputStream in1 = url.openStream();
            InputStreamReader in2 = new InputStreamReader(in1);
            inReader = new BufferedReader(in2);
            
            // Read the URL contents
            while ((lineIn = inReader.readLine()) != null) {
                urlContent.append(lineIn + "\n");
            }

            return urlContent.toString();
        }

        // If an error occurred, show the error message in txtInhalt
        catch (Exception err) {
            return "ERROR: " + err.toString();
        } finally {
            try {
                if (inReader != null) inReader.close();
            } catch (IOException e) {
            }
        }
    }
}
