
package CLIENT;



import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;
import PROTOCOL.Protocol;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Client extends Observable implements Runnable{
    
    // Input - Output
    private PrintWriter output;
    private Scanner input;
    
    // Socket - Address - Name
    private InetAddress ADDRESS;
    private String name;
    Socket sock;
    int PORT;
    
    // Collection of Clients
    private ArrayList<String> clients;
    
    // Constructor - 1
    public Client(String name) {
        this.name = name;
        clients = new ArrayList<>();
    }
    
    // Constructor - 2
    public Client() {
        
        Random random = new Random();
 
        name = "client" + random.nextInt(1000);
        
        // Initialize ArrayList
        clients = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }

    
    public void connection(String address, int PORT) {
        this.PORT = PORT;
        
        try {
            
            ADDRESS = InetAddress.getByName(address);
            
            // Create Socket
            sock = new Socket(ADDRESS, PORT);
            
            // Input - Output
            input = new Scanner(sock.getInputStream());
            output = new PrintWriter(sock.getOutputStream(), true);
            
            register();
            
            // Create new Thread and Start
            new Thread(this).start();
            
        } catch (Exception e) {
            
        }
    }
    
    // Add Client to list of Clients - "clients"
    public void register() {
        output.println(Protocol.CONNECT(name));
    }
    
    // PROTOCOL METHOD - Stop
    public void stop() {
        output.println(Protocol.STOP);
    }
    
    // PROTOCOL METHOD - Message to everyone 
    public void sendToEveryone(String msg) {
        output.println(Protocol.MSGFROMCLIENT("*", msg));
    }
    
    // PROTOFOL METHOD - Message to specific
    public void sendToSpecific(String receiver, String msg) {
        output.println(Protocol.MSGFROMCLIENT(receiver + "," + name, msg));
    }
    
    @Override
    public void run() {
        
        while(true) {
            
            // Read NextLine and assign to "msg"
            String msg = input.nextLine();
            
            String[] data = msg.split("#");
            
            if(data[0].equals("USERLIST")) {
                clients.clear();
                
                for(String string : data[1].split(",")) {
                    clients.add(string);
                }
            }
            
            // Marks the "observable" object as "changed" - method "hasChanged()" will return true
            setChanged(); // Observable method
            
            // Notify all of its observers 
            notifyObservers(msg); // Observable method
            
            if(msg.equals(Protocol.STOP)) {
                
                try {
                    sock.close();
                    break;
                } catch (Exception e) {
                   
                }
            }
        }
    }
    
    
    public static void main(String[] args) {
        
        int port = 9090;
        String ip = "localhost";
        
        // Sofus - TCP Tutorial
        if(args.length == 2) {
            port = Integer.parseInt(args[0]);
            ip = args[1];
        }
        
        try {
            
            Client clientInstance = new Client();
            
            Observer observer = new Observer() {
                
                // Called whenever the "observed object" is changed
                // Calls an "Observable" object's "notifyObservers()" method, to have all the objects observers notified
                public void update(Observable observable, Object arg) {
                    System.out.println("Message Received: " + arg);
                }
                
            };
            
            clientInstance.addObserver(observer);
            clientInstance.connection(ip, port);
            
            // Test Application
            
        } catch (Exception e) {
           Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }
    
    
    
    
    
}
