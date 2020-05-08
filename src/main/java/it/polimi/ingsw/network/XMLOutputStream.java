package it.polimi.ingsw.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class XMLOutputStream extends ByteArrayOutputStream {

    private final DataOutputStream outChannel;

    public XMLOutputStream(OutputStream outChannel) {
        super();
        this.outChannel = new DataOutputStream(outChannel);
    }

    public void send() throws IOException {
        byte[] data = toByteArray();
        outChannel.writeInt(data.length);
        outChannel.write(data);
        reset();
    }
}
