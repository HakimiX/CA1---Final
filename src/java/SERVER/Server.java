
package SERVER;

import CLIENT.ClientManager;
import java.net.ServerSocket;
import java.util.ArrayList;
import UTILS.Utils;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import PROTOCOL.Protocol;

public class Server {
    
    // Stop Server
    private static boolean run = true;
    
    // ServerSocket
    private static ServerSocket serverSocket;
        
    // Properties - Access File "server.properties"
    private static final Properties properties = Utils.initProperties("server.properties"); // Contains IP and PORT
    
    // Collection - Clients
    private ArrayList<ClientManager> clients = new ArrayList<>();
    
    public static void stopServer() {
        run = false;
    }
    
    public void startServer() {
        
        // PORT - IP
        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");
        
        Logger.getLogger(Server.class.getName()).log(Level.INFO, "Sever started. Listening on: " + port + ", bound to: " + ip);
         
        try {
            // Instantiate ServerSocket
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            
            do {
                // Accept connection
                Socket sock = serverSocket.accept();

                // Logger to "Log" class
                Logger.getLogger(Server.class.getName()).log(Level.INFO, "Client is connected!");

                // Instance of ClientManager 
                ClientManager clientmanager = new ClientManager(sock, this);
                clientmanager.start();
                
                // Add Client
                clients.add(clientmanager);
                
                // Calling Method - Protocol - CLIENTLIST
                returnClientList();
                
            } while (run);
            
        } catch (Exception e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void disconnectClient(ClientManager clientmanager) {
        
        // Disconnect a Client
        clients.remove(clientmanager);
        
        // Call Method
        returnClientList();
        
        // Call "disconnect" method
        disconnect(clientmanager.getNamee());
    }
    
    
    // "CLIENTLIST" - Protocol
    public void returnClientList() {
        
        String message = "";
        
        for(ClientManager clientmanager : clients) {
            
            message += clientmanager.getNamee() + ",";
        }
        
        if(message.endsWith(",")) {
            message = message.substring(0, message.length() - 1);
        }
        
        // Protocol - "CLIENTLIST"
        for(ClientManager clientmanager : clients) {
            clientmanager.sendMessage(Protocol.CLIENTLIST(message));
        }
    }
    
    public void connect(String name) {
        
        for(ClientManager clientmanager : clients) {
            clientmanager.sendMessage(Protocol.MSGTOCLIENT("Server - ", name + " - Connected"));
        }
    }
    
    public void disconnect(String name) {
        
        for(ClientManager clientmanager : clients) {
            clientmanager.sendMessage(Protocol.MSGTOCLIENT("Server - ", name + " - disconnected"));
        }
    }
    
    // "MSGFROMCLIENT" - Protocol
    public void message(String receiver, String message) {
        
        if(receiver.equals("*")) {
            
            for(ClientManager clientmanager : clients) {
                clientmanager.sendMessage(message);
            }
        } else {
            
            String[] data = receiver.split(",");
            
            for(String string : data) {
                
                for(ClientManager clientmanager : clients) {
                    
                    if(string.equals(clientmanager.getNamee())) {
                        clientmanager.sendMessage(message);
                    }
                } // Enhanced for 2
            } // Enchanced for 1
        }
    }
    
    
    public static void main(String[] args) {
        
        // Save to log
        String logFile = properties.getProperty("logFile");
        Utils.setLogFile(logFile, Server.class.getName());
        
        // Start Server
        new Server().startServer();
        Utils.closeLogger(Server.class.getName());
        
    }
    
}
