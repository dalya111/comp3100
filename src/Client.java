import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

public class Client {
    private static Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public void setupConnection() {
        try {
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
        try {
            System.out.println("Hello, starting client...");
            Client c = new Client();

            c.setupConnection();

            c.dos.write("HELO\n".getBytes());
            c.dos.flush();

            String response = c.dis.readLine();

            System.out.println("response from the server:7 " + response);

            c.dos.write("AUTH dummy_user\n".getBytes());
            c.dos.flush();

            response = c.dis.readLine();

            System.out.println("response from the server:6 " + response);
            
            
            while(!(response.equals("."))){

                c.dos.write("REDY\n".getBytes());
                c.dos.flush();      

                response = c.dis.readLine();

                System.out.println("response from the server:1 " + response);
            
                if(response.contains("JOBN")){

                    String[] splitJob = response.split("\\s+");
                    Integer numOfJObs = Integer.valueOf(splitJob[1]);
                    System.out.println("number of jobs: " + numOfJObs);
                    

                    c.dos.write("GETS Capable 3 700 3800\n".getBytes());
                    c.dos.flush();

                    response = c.dis.readLine();

                    System.out.println("response from the server:2 " + response);

                    String[] splitData = response.split("\\s+");
                    Integer numOfServers = Integer.valueOf(splitData[1]);
                    System.out.println("number of servers: " + numOfServers);
                    String[] servers = new String[numOfServers];

                    c.dos.write("OK\n".getBytes());
                    c.dos.flush();

                    String schdeuleCommand ="";
                    Integer max = 0;
                    for(int i = 0; i < numOfServers; i++){
  
                        response = c.dis.readLine();
                        System.out.println("response from the server:3 " + response);
                        servers[i] = response;
                        String[] server = servers[i].split("\\s+");
                        if (Integer.valueOf(server[4])> max){
                            schdeuleCommand = "SCHD " +server[1] + " " + server[0] + " " +server[8] + "\n";
                            max = Integer.valueOf(server[4]);
                        }

                    }
                
                    c.dos.write("OK\n".getBytes());
                    c.dos.flush();
                    
                    c.dos.write(schdeuleCommand.getBytes());
                    c.dos.flush();

                    response = c.dis.readLine();

                    System.out.println("response from the server:6 " + response);

                }
                
            }


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