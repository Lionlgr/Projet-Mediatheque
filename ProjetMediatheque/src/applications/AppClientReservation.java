package applications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class AppClientReservation {
	private static int PORT = 3000;
	
    public static void main(String[] args) throws IOException {
        Socket maSocket = null;
      
        try {
            maSocket = new Socket("localhost", PORT);
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(maSocket.getInputStream()));
            PrintWriter socketOut = new PrintWriter(maSocket.getOutputStream(), true);
            BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Connect� au serveur "+ maSocket.getInetAddress() + ":"+ maSocket.getPort());

            while(maSocket.isConnected()) {
            	System.out.println(socketIn.readLine());
                System.out.print("		>");
                socketOut.println(clavier.readLine());
            }
        } catch(IOException e) {
            System.out.println("Connexion interrompu par le serveur");
        }
        System.out.println("Connexion termin�");
        maSocket.close();
    }

}
