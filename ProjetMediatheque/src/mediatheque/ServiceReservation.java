package mediatheque;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;



import documents.Abonne;
import documents.DVD;
import documents.PasAssezDePlacesException;

// ce service envoie les questions au client
// on pourrait lors du premier envoi lister les cours o� il reste de la place
// mais il faudrait coder les \n en ##
// (et d�coder du cot� client)

public class ServiceReservation extends Service implements Runnable {
	private final Object verrou = new Object();

	public ServiceReservation(Socket socket) {
		super(socket);
	}

	@Override
	public void run() {
		Socket client = getClient();
		String reponse = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);

			out.println("Saisissez votre num�ro d'abonn� : ");
			int numAbonne = Integer.parseInt(in.readLine());
			out.println("Saisissez le num�ro du DVD : ");
			int numDVD = Integer.parseInt(in.readLine());
			DVD recherche = mediatheque.chercherDVD(numDVD);
			Abonne abonne = mediatheque.chercherAbonne(numAbonne); // chercher l'abonn� dans une liste donn�e  

			if (abonne != null) {
				System.out.println("L'abonn� est " + abonne.id());
				int age = LocalDate.now().getYear() - new GregorianCalendar().get(Calendar.YEAR);
				LocalDateTime now = LocalDateTime.now();

				Date dateRetour = new Date(); // date actuelle
				Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
				if (abonne.getDatefinInterdiction() == null || abonne.getDatefinInterdiction().compareTo(date) <= 0) {
					mediatheque.ModifierBan(abonne.id(), null);
					if (recherche.reserveur() == null) {
						System.out.println("Le DVD n'a pas de reserveur");
						if (recherche.emprunteur() == null) {
							System.out.println("Le DVD n'a pas d'emprunteur");
							if (recherche.isAdulte() == true) {
								System.out.println("Le DVD est pour les +18");
								if (age >= 18) {
									System.out.println("L'abonne a + de 18 ans");
									synchronized(verrou) {
										// Modification pour le DVD
										recherche.reservationPour(abonne);
										recherche.getDateReservation();
										// Modification dans la base de donn�e
										mediatheque.modifierDVD(recherche, abonne.id(), null);
										out.println("Votre r�servation est confirm�");
									}
								}
							} else {
								out.println("Votre r�servation est confirm�");
								synchronized(verrou) {
									// Modification pour le DVD
									recherche.reservationPour(abonne);
									recherche.getDateReservation();
									// Modification dans la base de donn�e
									mediatheque.modifierDVD(recherche, abonne.id(), null);
									out.println("Votre r�servation est confirm�");
								}
							}
						}
					}else {
						out.println("Ce DVD est d�j� reserv�");
					}

				}else {
					out.println("Vous avez rendu un DVD en retard. Vous ne pouvez pas emprunter ou réserver de DVD jusqu'au " + abonne.getDatefinInterdiction()+ " .");

				}
			} else {
				out.println("Abonné non trouvé.");
			}

		} catch (IOException e) {
		}
		try {
			client.close();
		} catch (IOException e2) {
		}
	}

}
