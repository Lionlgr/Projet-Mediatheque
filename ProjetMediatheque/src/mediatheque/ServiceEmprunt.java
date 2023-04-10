package mediatheque;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import documents.*;

// ce service envoie les questions au client
// on pourrait lors du premier envoi lister les cours o� il reste de la place
// mais il faudrait coder les \n en ##
// (et d�coder du cot� client)

public class ServiceEmprunt extends Service implements Runnable {
	private final Object verrou = new Object();

	public ServiceEmprunt(Socket socket) {
		super(socket);
	}

	@Override
	public void run() {
		Socket client = getClient();
		String reponse = null;
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			System.out.println("je marche1");

			out.println("Saisissez votre num�ro d'abonn� : ");
			int numAbonne = Integer.parseInt(in.readLine());
			out.println("Saisissez le numero du DVD : ");
			int numDVD = Integer.parseInt(in.readLine());
			DVD recherche = mediatheque.chercherDVD(numDVD);
			Abonne abonne = mediatheque.chercherAbonne(numAbonne); // chercher l'abonné dans une liste donnée

			if (abonne != null) {
				int age = LocalDate.now().getYear() - new GregorianCalendar().get(Calendar.YEAR);
				LocalDateTime now = LocalDateTime.now();
				LocalDateTime reservationDate = recherche.getDateReservation();
				Date dateRetour = new Date(); // date actuelle
				Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
				if (abonne.getDatefinInterdiction() == null || abonne.getDatefinInterdiction().compareTo(date) <= 0) {				

					if (reservationDate != null) {
						long minutesElapsed = ChronoUnit.MINUTES.between(reservationDate, now);
						if (minutesElapsed >= 120) {
							recherche.reservationPour(null); // Annuler la réservation
							recherche.setDateReservation(null);
							out.println("La réservation a été annulée car le réservateur n'a pas récupéré le DVD dans les deux heures.");
						}                	
					}

					if (recherche.reserveur()==abonne || recherche.reserveur() == null) {
						if (recherche.emprunteur() == null) {
							if (recherche.isAdulte() == true) {
								if (age >= 18) {
									synchronized(verrou) {
										// Modification pour le DVD
										recherche.empruntPar(abonne);
										// Modification pour la base de donn�e
										mediatheque.modifierDVD(recherche,null,abonne.id());
										out.println("Votre emprunt est confirmé");
									}
								} else {
									out.println("vous n’avez pas l’âge pour emprunter ce DVD");
								}
							} else {
								synchronized(verrou) {
									// Modification pour le DVD
									recherche.empruntPar(abonne);
									// Modification pour la base de donn�e
									mediatheque.modifierDVD(recherche,null,abonne.id());
									out.println("Votre emprunt est confirmé");
								}
							}
						}else {
							out.println("ce DVD est déjà emprunté");
						}
					} else {
						out.println("Ce DVD est r�serv� par quelqu'un d'autre");
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
