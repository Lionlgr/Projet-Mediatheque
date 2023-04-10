package documents;

public interface Document {
	int numero();
	// return null si pas emprunt� ou pas r�serv�
	Abonne emprunteur() ; // Abonn� qui a emprunt� ce document
	Abonne reserveur() ; // Abonn� qui a r�serv� ce document
	
	// pr�condition ni r�serv� ni emprunt�
	void reservationPour(Abonne ab) ;
	
	// pr�condition libre ou r�serv� par l�abonn� qui vient emprunter
	void empruntPar(Abonne ab);
	
	// brief retour d�un document ou annulation d�une r�servation
	void retour();
}