package CLIENT;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import SERVER.Server;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ClientTest {

    // Class Instance
    Client client;

    // Test Strings
    String message = "";
    String clientList = "";
    String name = "test";

    public ClientTest() {
    }

    @BeforeClass
    public static void setUpClass() {

        // Create Thread and start
        new Thread(new Runnable() {
            public void run() {
                Server.main(null);
            }
        }).start();
    }

    @AfterClass
    public static void tearDownClass() {
        Server.stopServer();
    }

    @Before
    public void setUp() {

        // Create new Client
        client = new Client(name);

        Observer observer;

        observer = new Observer() {
            public void update(Observable observable, Object arg) {
                message = arg.toString();
                if (message.startsWith("USERLIST#")) {
                    clientList = message;
                }
            }
        };
        
        client.addObserver(observer);
        client.connection("localhost", 9090);

    }

    @After
    public void tearDown() {

    }

    /**
     * Test of getName method, of class Client.
     */
//    @Test
//    public void testGetName() {
//        System.out.println("getName");
//        Client instance = new Client();
//        String expResult = "";
//        String result = instance.getName();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of connection method, of class Client.
     */
//    @Test
//    public void testConnection() {
//        System.out.println("connection");
//        String address = "";
//        int PORT = 0;
//        Client instance = new Client();
//        instance.connection(address, PORT);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of register method, of class Client.
     */
//    @Test
//    public void testRegister() {
//        System.out.println("register");
//        Client instance = new Client();
//        instance.register();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of stop method, of class Client.
     */
    @Test
    public void testStop() {
        
        try {
            client.stop();
            Thread.sleep(1000);
        } catch (Exception e) {
            
        }
        
        assertEquals("STOP#", message);
        
    }

    /**
     * Test of sendToEveryone method, of class Client.
     */
    @Test
    public void testSendToEveryone() {
       
        client.sendToEveryone("Hello");
        
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            
        }
        
        assertEquals("MSG#" + name + "#Hello", message);
    }

    @Test
    public void clientListWithManyClients() throws IOException, InterruptedException {
        
        
        String ip = "localhost";
        int port = 9090;
        
        // Test list of clients 
        ArrayList<Client> clientListTest = new ArrayList<>();
        
        String expected = "USERLIST#";
        
        // Create new clients - add to test list
        for(int i = 0; i < 50; i++) {
            clientListTest.add(new Client(name + i));
            expected += "," + name + i;
        }
        
        // Create connection - PORT, IP 
        for(Client c : clientListTest) {
            c.connection(ip, port);
            sleep(1000);
        }
        
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            
        }
        
        assertEquals(expected, clientList);
        
        for(Client c : clientListTest) {
            c.stop();
        }
        
    }
    
    /**
     * Test of sendToSpecific method, of class Client.
     */
//    @Test
//    public void testSendToSpecific() {
//        System.out.println("sendToSpecific");
//        String receiver = "";
//        String msg = "";
//        Client instance = new Client();
//        instance.sendToSpecific(receiver, msg);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of run method, of class Client.
     */
//    @Test
//    public void testRun() {
//        System.out.println("run");
//        Client instance = new Client();
//        instance.run();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of main method, of class Client.
     */
//    @Test
//    public void testMain() {
//        System.out.println("main");
//        String[] args = null;
//        Client.main(args);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}
