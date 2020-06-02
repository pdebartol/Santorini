package it.polimi.ingsw.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class contains the logic behind write an XML onto Output Stream.
 * @author marcoDige
 */

public class XMLOutputStream extends ByteArrayOutputStream {

    private final DataOutputStream outChannel;

    public XMLOutputStream(OutputStream outChannel) {
        super();
        this.outChannel = new DataOutputStream(outChannel);
    }

    /**
     * This method allows to send an XML through the socket connection.
     * @throws IOException if an I/O error occurs.
     */

    public void send() throws IOException {
        byte[] data = toByteArray();
        outChannel.writeInt(data.length);
        outChannel.write(data);
        reset();
    }
}
