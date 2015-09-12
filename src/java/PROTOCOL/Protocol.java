
package PROTOCOL;


public class Protocol {
    
    // CLIENT COMMANDS 
    
    // Notify the server, that it can close the connection for this client
    public static final String STOP = "STOP#";
    
    // Tag that includes the username of the client - Must be sent first
    public static String CONNECT(String name) {
        return "USER#" + name;
    }
    
    // Tag used to send messages - includes "receivers" and "message"
    public static String MSGFROMCLIENT(String receivers, String msg) {
        return "MSG#" + receivers + "#" + msg;
    }
    
    // SERVER COMMANDS
    
    // Tag that includes all users currently online
    public static String CLIENTLIST(String clients) {
        return "USERLIST#" + clients;
    }
    
    // Tag used to forward a message received from a client 
    public static String MSGTOCLIENT(String sender, String msg) {
        return "MSG#" + sender + "#" + msg;
    }
}
