package modèle;

/**
 * Représente un client avec ses informations personnelles, de livraison, de
 * paiement et son panier d'achat.
 */
public class Client {

	private String prenom;
	private String nom;
	private String adresse;
	private String ville;
	private String codePostal;
	private String numeroTel;
	private String mail;
	private String moyenPaiement;
	private boolean newsletter;
	private Panier panier;

	/**
	 * Crée un nouveau client avec toutes ses informations.
	 * 
	 * @param prenom        le prénom du client
	 * @param nom           le nom de famille du client
	 * @param adresse       l'adresse de livraison
	 * @param ville         la ville de livraison
	 * @param codePostal    le code postal de livraison
	 * @param numeroTel     le numéro de téléphone
	 * @param mail          l'adresse email
	 * @param moyenPaiement le moyen de paiement choisi
	 * @param newsletter    true si le client souhaite recevoir la newsletter
	 * @param panier        le panier d'achat du client
	 */
	public Client(String prenom, String nom, String adresse, String ville, String codePostal, String numeroTel,
			String mail, String moyenPaiement, boolean newsletter, Panier panier) {
		this.prenom = prenom;
		this.nom = nom;
		this.adresse = adresse;
		this.ville = ville;
		this.codePostal = codePostal;
		this.numeroTel = numeroTel;
		this.mail = mail;
		this.moyenPaiement = moyenPaiement;
		this.newsletter = newsletter;
		this.panier = panier;
	}

	/**
	 * Retourne le prénom du client.
	 * 
	 * @return le prénom
	 */
	public String getPrenom() {
		return this.prenom;
	}

	/**
	 * Retourne le nom de famille du client.
	 * 
	 * @return le nom
	 */
	public String getNom() {
		return this.nom;
	}

	/**
	 * Retourne l'adresse de livraison.
	 * 
	 * @return l'adresse
	 */
	public String getAdresse() {
		return this.adresse;
	}

	/**
	 * Retourne la ville de livraison.
	 * 
	 * @return la ville
	 */
	public String getVille() {
		return this.ville;
	}

	/**
	 * Retourne le code postal de livraison.
	 * 
	 * @return le code postal
	 */
	public String getCodePostal() {
		return this.codePostal;
	}

	/**
	 * Retourne le numéro de téléphone du client.
	 * 
	 * @return le numéro de téléphone
	 */
	public String getNumeroTel() {
		return this.numeroTel;
	}

	/**
	 * Retourne l'adresse email du client.
	 * 
	 * @return l'adresse email
	 */
	public String getMail() {
		return this.mail;
	}

	/**
	 * Retourne le moyen de paiement choisi par le client.
	 * 
	 * @return le moyen de paiement
	 */
	public String getMoyenPaiement() {
		return this.moyenPaiement;
	}

	/**
	 * Indique si le client souhaite recevoir la newsletter.
	 * 
	 * @return true si le client veut la newsletter, false sinon
	 */
	public boolean getNewsletter() {
		return this.newsletter;
	}

	/**
	 * Retourne le panier d'achat du client.
	 * 
	 * @return le panier
	 */
	public Panier getPanier() {
		return this.panier;
	}
}