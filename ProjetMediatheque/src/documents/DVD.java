package documents;

import java.time.LocalDateTime;
import java.util.Date;

public class DVD implements Document {
	private int numero;
	private String titre;
	private boolean adulte;
	private Abonne emprunteur;
	private Abonne reserveur;
	private LocalDateTime dateReservation;
	
	
	
	public DVD(int numero, String titre, boolean adulte, Abonne id_Reserveur, Abonne id_Emprunteur) {
		this.numero = numero;
		this.titre = titre;
		this.setAdulte(adulte);
		this.reserveur = id_Reserveur;
		this.emprunteur = id_Emprunteur;
		
	}

	@Override
	public int numero() {
		return numero;
	}

	@Override
	public Abonne emprunteur() {
		return emprunteur;
	}

	@Override
	public Abonne reserveur() {
		return reserveur;
	}

	@Override
	public void reservationPour(Abonne ab) {
		this.reserveur = ab;
	}

	@Override
	public void empruntPar(Abonne ab) {
		this.emprunteur = ab;
		
	}

	@Override
	public void retour() {
		if ( emprunteur != null) {
			emprunteur.setDerniereDateRetour(new Date());
		}
		
		emprunteur = null;
		reserveur = null;
	}

	public boolean isAdulte() {
		return adulte;
	}

	public void setAdulte(boolean adulte) {
		this.adulte = adulte;
	}

	public void setDateReservation(LocalDateTime date) {
	    this.dateReservation = date;
	}

	public LocalDateTime getDateReservation() {
		
		return dateReservation = LocalDateTime.now();
	}
	

}
