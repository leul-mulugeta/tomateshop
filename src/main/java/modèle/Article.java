package modèle;

/**
 * Représente un article dans le panier d'achat, composé d'une tomate et de sa
 * quantité commandée.
 */
public class Article {

	private Tomate tomate;
	private int quantite;

	/**
	 * Crée un nouvel article avec une tomate et sa quantité.
	 * 
	 * @param tomate   la tomate associée à cet article
	 * @param quantite la quantité commandée
	 */
	public Article(Tomate tomate, int quantite) {
		this.tomate = tomate;
		this.quantite = quantite;
	}

	/**
	 * Retourne la tomate associée à cet article.
	 * 
	 * @return la tomate de l'article
	 */
	public Tomate getTomate() {
		return this.tomate;
	}

	/**
	 * Retourne la quantité commandée pour cet article.
	 * 
	 * @return la quantité de l'article
	 */
	public int getQuantite() {
		return this.quantite;
	}

	/**
	 * Modifie la quantité de cet article.
	 * 
	 * @param quantite la nouvelle quantité
	 */
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

	/**
	 * Calcule le prix total de cet article (prix unitaire × quantité).
	 * 
	 * @return le montant total TTC de l'article
	 */
	public float getTotal() {
		return this.tomate.getPrixTTC() * this.quantite;
	}
}