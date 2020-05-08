package it.polimi.ingsw.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XMLInputStream extends ByteArrayInputStream {

    private final DataInputStream inChannel;

    public XMLInputStream(InputStream inChannel) {
        super(new byte[2]);
        this.inChannel = new DataInputStream(inChannel);
    }

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
