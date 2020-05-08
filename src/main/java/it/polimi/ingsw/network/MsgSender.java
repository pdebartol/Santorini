package it.polimi.ingsw.network;

import it.polimi.ingsw.network.XMLOutputStream;
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
 * This class implements sending a file to a client through a socket connection.
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
     * This method allows to send a Request to the server through socket. It read toSendRequest in a buffer and write content into
     * OutputStream.
     */

    public void sendMsg() {

        try {

            OutputStream out = socket.getOutputStream();

            XMLOutputStream xmlOut = new XMLOutputStream(out);

            StreamResult streamResult = new StreamResult(xmlOut);
            DOMSource domSource = new DOMSource(msgOut);

            try {
                Transformer tf = TransformerFactory.newInstance().newTransformer();
                tf.transform(domSource, streamResult);
            } catch (TransformerException ex) {
                ex.printStackTrace();
            }

            xmlOut.send();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
