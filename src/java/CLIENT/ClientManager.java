
package CLIENT;

import CLIENT.Client;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import PROTOCOL.Protocol;
import SERVER.Server;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientManager extends Thread {
   
    // Instance of "Client"
    private Server server;
    
    // Client Name
    private String name = "Anonymous";
    
    // Socket
    private Socket sock;
    
    // Input - Output
    PrintWriter output;
    Scanner input;
    
    public ClientManager(Socket sock, Server server) throws IOException {
        
        this.sock = sock;
        this.server = server;
            
        // Instantiate - output, input
        output = new PrintWriter(sock.getOutputStream(), true);
        input = new Scanner(sock.getInputStream());

    }
    
    // Getter
    public String getNamee() {
        return name;
    }
    
    public void sendMessage(String message) {
        output.println(message);
        Logger.getLogger(Server.class.getName()).log(Level.INFO, String.format("Received Message: %1$S", message));
    }
    
    public void run() {
        
        try {
            
            // Read input, assign to message
            String message = input.nextLine();
            
            // Log
            Logger.getLogger(Server.class.getName()).log(Level.INFO, String.format("Received Message: %1$S", message));
            
            while(!message.equals(Protocol.STOP)) {
                
                String[] data = message.split("#");
                
                if(data[0].equals("USER")) {
                    name = data[1];
                    
                    // Server Method - "returnClientList()"
                    server.returnClientList();
                    
                    // Server Method - "connect()"
                    server.connect(name);
                }
                
                if(data[0].equals("MSG")) {
                    server.message(data[1], Protocol.MSGTOCLIENT(name, data[2]));
                }
                
                try {
                    
                    // Read Message
                    message = input.nextLine();
                    
                } catch (Exception e) {
                    break;
                }
            }
            output.println(Protocol.STOP);
            sock.close();
            server.disconnectClient(this);
            Logger.getLogger(Server.class.getName()).log(Level.INFO, "A Connection Closed!");
            
        } catch (Exception e) {
            
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }
    
    
}
