package browser_v0;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class BrowserModel {
    public String browse(String ipAddress, Integer port) {
        Socket s = null;
        OutputStreamWriter out = null;
        BufferedReader inReader = null;
        String lineIn;
        StringBuffer urlContent = new StringBuffer();

        // Network errors are always possible
        try {
            // Set up the socket
            s = new Socket(ipAddress, port);

            // Send our request, using the HTTP 1.0 protocol
            // Note: HTTP specifies \r\n line endings, though most programs don't care
            out = new OutputStreamWriter(s.getOutputStream());
            out.write("GET / HTTP/1.0\r\n");
            out.write("User-Agent: Browser0\r\n");
            out.write("Host: " + ipAddress + ":" + port + "\r\n");
            out.write("Accept: text/html, */*\r\n\r\n");
            out.flush();

            // Set up the reader classes
            InputStream in1 = s.getInputStream();
            InputStreamReader in2 = new InputStreamReader(in1);
            inReader = new BufferedReader(in2);

            while ((lineIn = inReader.readLine()) != null) {
                urlContent.append(lineIn + "\n");
            }
        }

        // If an error occurred, show the error message in txtInhalt
        catch (Exception err) {
            urlContent.append("ERROR: " + err.toString());
        } finally {
            try {
                if (out != null)
                    out.close();
                if (inReader != null)
                    inReader.close();
                if (s != null)
                    s.close();
            } catch (Exception e) {
            }
        }
        
        return urlContent.toString();
    }
}
