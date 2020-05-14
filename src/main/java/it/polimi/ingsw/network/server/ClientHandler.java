package it.polimi.ingsw.network.server;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class manages a single client TCP socket connection.
 * @author marcoDige
 */

public class ClientHandler implements Runnable{

    //attributes

    private final Socket client;
    private VirtualView virtualView;
    private int lobbyNumber;

    private Document request;

    //constructors

    public ClientHandler(Socket socket, VirtualView vrtV, int lobbyNumber) {
        this.client = socket;
        this.virtualView = vrtV;
        this.request = null;
        this.lobbyNumber = lobbyNumber;
    }

    //methods

    public void changeLobby(VirtualView vrtV, int lobbyNumber){
        this.virtualView = vrtV;
        this.lobbyNumber = lobbyNumber;
        run();
    }

    /**
     * This method allows to receive an XML from connection with client, to start the Request process and send Answer to client
     * The communication go down when client send a "end" mode.
     */

    @Override
    public void run() {

        System.out.println("Client " + client + " has connected in lobby number " + lobbyNumber + "!");

        if(!client.getInetAddress().getHostAddress().equals("127.0.0.1")) new Thread(this::pingClient).start();

        virtualView.addInWaitList(client);

        //Server notifies this client that it have to login in the lobby
        virtualView.toDoLogin(client);


        //Server wait for a request from client
        try {
            InputStream in = client.getInputStream();

            while(true) {

                receiveXML(in);

                if(isEndMode()){
                    break;
                }else{
                    //create a thread to execute the request
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
     * This method verify if the request mode is "end".
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

    private void pingClient() {
        boolean reachable = true;
        do{
            try {
                Thread.sleep(5000);
                reachable = client.getInetAddress().isReachable(500);
            } catch (IOException e) {
                clientDisconnection();
            } catch (InterruptedException iE){
                iE.getStackTrace();
            }
        }while(reachable);

        clientDisconnection();
        Thread.currentThread().interrupt();
    }
}
