package com.wizsyst.android.app.login.utilities.connection.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * Created by rmanenti on 22/04/2016.
 */
public class Http {

    /**
     * Read a InputStream, allowing to limit the read length and the charcode.
     *
     * @param stream
     * @param charcode
     * @param len
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static String readString( InputStream stream, String charcode, int len) throws IOException, UnsupportedEncodingException {

        Reader reader = new InputStreamReader( stream, charcode );

        char[] buffer = new char[ len ];
        reader.read( buffer );

        return new String( buffer );
    }

    /**
     * Read a InputStream.
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String readString( InputStream in ) throws IOException {

        return new String(
                Http.readBytes( in ) );
    }

    /**
     * Read the bytes returned in a InputStream object.
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] readBytes( InputStream in ) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            byte[] buffer = new byte[ 1024 ];

            int len;

            while( ( len = in.read( buffer ) ) > 0 ) {
                baos.write( buffer, 0, len );
            }

            byte[] bytes = baos.toByteArray();

            return bytes;
        }
        finally {

            baos.close();
            in.close();
        }
    }
}
