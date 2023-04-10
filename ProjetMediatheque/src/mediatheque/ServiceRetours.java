package mediatheque;

import java.io.*;
import java.net.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import documents.Abonne;
import documents.DVD;
import documents.PasAssezDePlacesException;

// ce service envoie les questions au client
// on pourrait lors du premier envoi lister les cours o� il reste de la place
// mais il faudrait coder les \n en ##
// (et d�coder du cot� client)

public class ServiceRetours extends Service implements Runnable {

	public ServiceRetours(Socket socket) {
		super(socket);
	}

	@Override
	public void run() {
		Socket client = getClient();
		String reponse = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);


			out.println("Saisissez le num�ro du DVD à retourner: ");
			int numDVD = Integer.parseInt(in.readLine());
			out.println("Si le DVD est abim�, tapez 1, si il est neuf, tapez 2 : ");
			int etat = Integer.parseInt(in.readLine());
			DVD recherche = mediatheque.chercherDVD(numDVD);

			if (recherche != null) {
				if (recherche.emprunteur() != null) {				
				Date dateRetour = new Date(); // date actuelle
				System.out.println(recherche.emprunteur());
				if (recherche.emprunteur().estEnRetard(dateRetour) || etat == 1) {
					// interdire l'emprunt ou la réservation pendant un mois
					Calendar cal = Calendar.getInstance();
					cal.setTime(dateRetour); // date actuelle
					cal.add(Calendar.MONTH, 1); // ajoute 1 mois
					Date dateFinInterdiction = cal.getTime(); // date actuelle + 30 jours
					recherche.emprunteur().setInterditJusquA(dateFinInterdiction);
					
					mediatheque.ModifierBan(recherche.emprunteur().id(),dateFinInterdiction);
					out.println("Vous avez rendu un DVD en retard ou vous l'avez abim�. Vous ne pouvez pas emprunter ou r�server de DVD pendant un mois.");
					recherche.retour();
					System.out.println(dateFinInterdiction);
				} else {
					recherche.retour();
					out.println("Votre retour est confirm�");
				}
				mediatheque.modifierDVD(recherche,null,null);
			} else {
				
				out.println("Le DVD que n'a pas été emprunté.");
			}
			}else {
				out.println("Le DVD que vous avez rentr� n'existe pas.");
			}
		}
		catch (IOException e) {
		}
		try {

			client.close();
		} catch (IOException e2) {
		}
	}


} 



