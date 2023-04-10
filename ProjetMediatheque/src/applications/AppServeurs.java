package applications;

import java.io.IOException;

import mediatheque.*;

public class AppServeurs {
	private static int PORTreserv = 3000;
	private static int PORTemprunt = 4000;
	private static int PORTretour = 5000;
	
    public static void main(String[] args) {
    	mediatheque m = new mediatheque();

        try {
            new Thread(new ServeurDocuments(ServiceReservation.class, PORTreserv)).start();
            System.out.println("Serveur lance sur le port " + PORTreserv);
            new Thread(new ServeurDocuments(ServiceEmprunt.class, PORTemprunt)).start();
            System.out.println("Serveur lance sur le port " + PORTemprunt);
            new Thread(new ServeurDocuments(ServiceRetours.class, PORTretour)).start();
            System.out.println("Serveur lance sur le port " + PORTretour);
        } catch(IOException e) {
            System.out.println("Connexion interrompu par le serveur : " + e);
        }
    }
}
