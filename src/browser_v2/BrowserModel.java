package browser_v2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;

public class BrowserModel {
    public String browse(String urlString) {
        StringBuffer headers = new StringBuffer();
        BufferedReader inReader = null;
        String lineIn;
        StringBuffer urlContent = new StringBuffer();

        // Network errors are always possible
        try {
            // Create a new URL-Object
            URL url = new URL(urlString);

            // Get a URLConnection from the URL. This provides more
            // features, like access to the header information
            URLConnection inConnection = url.openConnection();
            headers.append("Content encoding: " + inConnection.getContentEncoding() + "\n");
            headers.append("Content type: " + inConnection.getContentType() + "\n");    
            headers.append("Last modified: " + Instant.ofEpochMilli(inConnection.getLastModified()).toString() + "\n\n");
            
            if (inConnection instanceof HttpURLConnection) {
                HttpURLConnection in_http = (HttpURLConnection) inConnection;
                headers.append("HTTP Status-Code: " + in_http.getResponseCode() + "\n\n");
            }

            InputStream in1 = inConnection.getInputStream();
            InputStreamReader in2 = new InputStreamReader(in1);
            inReader = new BufferedReader(in2);
            while ((lineIn = inReader.readLine()) != null) {
                urlContent.append(lineIn + "\n");
            }

            return headers.toString() + urlContent.toString();
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
