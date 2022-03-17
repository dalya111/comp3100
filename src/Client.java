import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private Socket socket;
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

            String response1 = c.dis.readLine();

            System.out.println("response from the server: " + response1);

            c.dos.write("AUTH dummy_user\n".getBytes());
            c.dos.flush();

            String response2 = c.dis.readLine();

            System.out.println("response from the server: " + response2);

        } catch (IOException e) {
            System.out.println(e);
        } 



        // send HELO to the server
        // check response from server
        // if server responds with OK
        // send AUTH fake_user
        // check response from server 
        // if server responds with OK
        // close the connection


    }
}
