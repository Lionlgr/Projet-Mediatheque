package documents;

import java.util.Date;
import java.time.chrono.ChronoLocalDate;

public class Abonne {
	private int id;
	private String nom;
	private Date dateNaiss;
	private Date derniereDateRetour;
	private Date datefinInterdiction;
	
	public Abonne(int id, String nom, Date dateNaiss, java.sql.Date dateFinInterdiction){
		this.id = id;
		this.nom = nom;
		this.dateNaiss = dateNaiss;
		this.datefinInterdiction = dateFinInterdiction;
	}

	public int id() {
		return id;
	}

	public Date getDateNaiss() {
		// TODO Auto-generated method stub
		return dateNaiss;
	}
	public boolean estEnRetard(Date dateRetour) {
		if (derniereDateRetour != null) {
			long difference = dateRetour.getTime() - derniereDateRetour.getTime();
		    long deuxSemaines = 2 * 7 * 24 * 60 * 60 * 1000; // deux semaines en millisecondes
		    return difference > deuxSemaines;
		}
		else {
			return false;
		}
	}

	public void setDerniereDateRetour(Date date) {
		this.derniereDateRetour = date;
		
	}

	public void setInterditJusquA(Date dateFinInterdiction) {
		this.datefinInterdiction = dateFinInterdiction;
		
	}

	public Date getDatefinInterdiction() {
		return datefinInterdiction;
	}
	
}

