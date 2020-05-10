package it.polimi.ingsw.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class contains the logic behind reading an XML from the Input Stream.
 * @author marcoDige
 */

public class XMLInputStream extends ByteArrayInputStream {

    private final DataInputStream inChannel;

    public XMLInputStream(InputStream inChannel) {
        super(new byte[2]);
        this.inChannel = new DataInputStream(inChannel);
    }

    /**
     * This method allows to receive an XML from connection.
     * @throws IOException if the first byte cannot be read for any reason other than end of file, the stream has
     * been closed and the underlying input stream does not support reading after close, or another I/O error occurs.
     */

    public void receive() throws IOException {
        int i = inChannel.readInt();
        byte[] data = new byte[i];
        inChannel.read(data, 0, i);
        this.buf = data;
        this.count = i;
        this.mark = 0;
        this.pos = 0;
    }
}
