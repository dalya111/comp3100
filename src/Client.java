import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Client {
    private static Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public void setupConnection() {
        try {
            // Initalisation
            System.out.println("connecting to server...");
            socket = new Socket("localhost", 50000);

            System.out.println("setting up streams...");
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            System.out.println("connection set up successfully");
        } catch (IOException e) {
            System.out.println(e);
        }  
    }

    public static void main(String[] args) throws Exception {
        String[] serverDataSplit ={};
        try {
            System.out.println("Hello, starting client...");
            Client c = new Client();

            c.setupConnection();
            // Initial handshake
            c.dos.write("HELO\n".getBytes());
            c.dos.flush();

            String response = c.dis.readLine();

            System.out.println("response from the server:7 " + response);

            // Authentication
            c.dos.write("AUTH dummy_user\n".getBytes());
            c.dos.flush();

            response = c.dis.readLine();

            System.out.println("response from the server:6 " + response);

            // Send ready
            c.dos.write("REDY\n".getBytes());
            c.dos.flush();      
            response = c.dis.readLine();
            System.out.println("response from the server:1 " + response);

            // Get job value
            String newJob = String.valueOf(response);
            
            while(!(newJob.equals("NONE"))){
                // Split job details to core, memory, etc
                String[] splitJob = newJob.split("\\s+");
                String jobId = splitJob[2];

                // Handles jobs
                if(newJob.contains("JOBN") || newJob.contains("JOBP")){
                    // Sends GETS command 
                    c.dos.write(("GETS Capable " + splitJob[4] + " " + splitJob[5] + " " + splitJob[6] + "\n").getBytes());
                    c.dos.flush();

                    response = c.dis.readLine();

                    System.out.println("response from the server:2 " + response);

                    String[] splitData = response.split("\\s+");
                    Integer numOfServers = Integer.parseInt(splitData[1]);
                    System.out.println("number of servers: " + numOfServers);
                    // Store servers in an array list
                    List<String> servers = new ArrayList<String>();
        
                    // Once completed send OK
                    c.dos.write("OK\n".getBytes());
                    c.dos.flush();

                    // Loop through all the servers and add them
                    for (int i = 0; i < numOfServers; i++) {
                        response = c.dis.readLine();
                        System.out.println("response from the server:3 " + response);
                        //add new server to the list
                        servers.add(response);
                    }

                    c.dos.write("OK\n".getBytes());
                    c.dos.flush();
                    response = c.dis.readLine();
                    
                    //get the first server provided by the GETS command //change
                    String serverData = servers.get(0);
                    //split up the server info so its readable and easily accessable //change
                    serverDataSplit = serverData.split("\\s+");

                }
                break;
            }
            
            c.dos.write("OK\n".getBytes());
            c.dos.flush();
            response = c.dis.readLine();

            c.dos.write("QUIT\n".getBytes());
            c.dos.flush();

            response = c.dis.readLine();

            System.out.println("response from the server:6 " + response);

            socket.close();

        } catch (IOException e) {
            System.out.println(e);
        } 


    }
}
