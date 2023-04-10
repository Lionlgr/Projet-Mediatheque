package documents;

public class PasAssezDePlacesException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private Document document;
	private int nbPlaces;

	public PasAssezDePlacesException(Document document, int nbPlaces) {
		this.document = document;
		this.nbPlaces = nbPlaces;
	}

	@Override
	public String toString() {
		return "D�sol�, il n'y a pas "+this.nbPlaces+" places pour le cours "+document.numero();
	}

}
