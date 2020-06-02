package it.polimi.ingsw.network;

import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This class implements sending an XML message to a client through a socket connection.
 * @author marcoDige
 */

public class MsgSender {

    private final Socket socket;

    private final Document msgOut;

    public MsgSender(Socket s, Document msgOut){
        this.socket = s;
        this.msgOut = msgOut;
    }

    /**
     * This method sends an XML through connection with client. it takes advantage of the XMLOutputStream class
     * and its methods.
     */

    public synchronized boolean sendMsg() {
        if (!socket.isClosed())
            try {

                OutputStream out = socket.getOutputStream();

                //Create an XMLOutputStream object.
                //After uploading the document to be sent, this object is used to send the message on the network.
                XMLOutputStream xmlOut = new XMLOutputStream(out);

                StreamResult streamResult = new StreamResult(xmlOut);
                DOMSource domSource = new DOMSource(msgOut);

                //Uploading the document to be sent
                try {
                    Transformer tf = TransformerFactory.newInstance().newTransformer();
                    tf.transform(domSource, streamResult);
                } catch (TransformerException ex) {
                    ex.printStackTrace();
                }

                xmlOut.send();

                return true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        return false;
    }
}
