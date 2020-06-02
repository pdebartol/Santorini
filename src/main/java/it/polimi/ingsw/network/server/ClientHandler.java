package it.polimi.ingsw.network.server;
import it.polimi.ingsw.msgUtilities.server.UpdateMsgWriter;
import it.polimi.ingsw.network.MsgSender;
import it.polimi.ingsw.network.XMLInputStream;
import it.polimi.ingsw.view.server.VirtualView;
import it.polimi.ingsw.msgUtilities.server.RequestParser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class manages a single client TCP socket connection.
 * @author marcoDige
 */

public class ClientHandler implements Runnable{

    //attributes

    private final Socket client;
    private final VirtualView virtualView;
    private final int lobbyNumber;

    private Document request;

    //constructors

    public ClientHandler(Socket socket, VirtualView vrtV, int lobbyNumber) {
        this.client = socket;
        this.virtualView = vrtV;
        this.request = null;
        this.lobbyNumber = lobbyNumber;
    }

    //methods

    /**
     * This method allows to receive an XML from socket connection and to start the request processing.
     * The communication go down when client send an "end" mode message.
     */

    @Override
    public void run() {

        System.out.println("Client " + client + " has connected in lobby number " + lobbyNumber + "!");

        //This is the ping received timeout.
        //If in 3 seconds server doesn't receive a ping message, the connection is declared dropped.
        try {
            client.setSoTimeout(3000);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        //The ping process start if game is not playing on the same machine.
        if(!client.getInetAddress().getHostAddress().equals("127.0.0.1")) new Thread(this::pingClient).start();

        //The client is added to the wait list
        virtualView.addInWaitList(client);

        //Server notifies this client that it have to login in the lobby
        virtualView.toDoLogin(client);


        //Server wait for a request from client
        try {
            InputStream in = client.getInputStream();

            while(true) {
                receiveXML(in);

                //If the message is not a ping message, processing of the message begins.
                if(!isPingMode())
                    if(isEndMode()){
                        break;
                    }else{
                        processRequest();
                    }
            }

            in.close();
            System.out.println("Connection with " + client + " closed!");
            client.close();
        } catch(IOException | SAXException | ParserConfigurationException  e) {
            //Management of client disconnection
            clientDisconnection();
        }
    }

    /**
     * This method allows to extract the incoming XML on the network, it takes advantage of the XMLInputStream class
     * and its methods.
     * @param in Input stream from connection
     * @throws ParserConfigurationException if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws IOException If any IO errors occur.
     * @throws SAXException If any parse errors occur.
     */

    private void receiveXML(InputStream in) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        XMLInputStream xmlIn = new XMLInputStream(in);


        docBuilder = docBuilderFact.newDocumentBuilder();
        xmlIn.receive();
        request = docBuilder.parse(xmlIn);
    }

    /**
     * This method verifies if the request mode is "ping"
     * @return true -> "ping" request mode
     *         false -> not "ping" request mode
     */

    public boolean isPingMode(){return new RequestParser(request).parsePing();}

    /**
     * This method verifies if the request mode is "end".
     * @return true -> "end" request mode
     *         false -> not "end" request mode
     */

    private boolean isEndMode(){
        return new RequestParser(request).parseEndRequest(virtualView);
    }

    /**
     * This method start a client request processing in server. It uses a RequestParser to start this process.
     */

    private void processRequest(){
        if(!isLoginRequest())
            new RequestParser(request).parseRequest(virtualView);
    }

    /**
     * This method verify if the request mode is "login".
     * @return true -> "login" request mode
     *         false -> not "login" request mode
     */

    private boolean isLoginRequest() {return new RequestParser(request).parseLoginRequest(virtualView,client);}

    /**
     * This method manages a client disconnection, it show on server a disconnection message, close the client's socket
     * and notify the virtualView that a client has disconnected.
     */

    private void clientDisconnection(){
        System.err.println("Client " + client + " has disconnected!");
        try {
            client.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        if(virtualView.isOn())
            virtualView.clientDown(client);
    }

    /**
     * This is an asynchronous method that send a ping message every 1,5 seconds.
     */

    private void pingClient() {
        do{
            try {
                Thread.sleep(1500);
                new MsgSender(client,new UpdateMsgWriter().extraUpdate("ping")).sendMsg();
            } catch (InterruptedException ignored) {
            }
        }while(!client.isClosed());

        Thread.currentThread().interrupt();
    }
}

