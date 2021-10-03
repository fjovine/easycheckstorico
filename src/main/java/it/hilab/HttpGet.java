package it.hilab;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Semplicissimo client HTTP che scarica una stringa in formato JSON.
 */
public class HttpGet {
    /**
     * Lancia la url e attende la risposta.
     * @param url da eseguire.
     * @return Stringa ricevuta dal server.
     */
    public static String wGet(String url)
    {
        URL u;
        InputStream is = null;
        DataInputStream dis;
        String result = "";

        try
        {
            u = new URL(url);
            is = u.openStream();
            dis = new DataInputStream(new BufferedInputStream(is));
            StringBuffer sb = new StringBuffer();
            while (true) {
                int c = is.read();
                if (c==-1) {
                    break;
                }
                sb.append((char) c);
            }

            result = sb.toString();
        }
        catch (MalformedURLException mue)
        {
            Easycheck.error(String.format("Url scorretta : %s", url));
        }
        catch (IOException ioe)
        {
            // Questo succede quando non c'è il test richisto
            Easycheck.error(String.format("Impossibile contattare %s", url));
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (Exception e)
            {
            }
        }

        return result;
    }
}
