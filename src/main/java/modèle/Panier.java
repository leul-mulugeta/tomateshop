package modèle;

import java.util.ArrayList;

/**
 * Représente un panier d'achat contenant des articles (tomates + quantités).
 * Gère l'ajout, la suppression d'articles et le calcul des totaux avec forfait
 * de port.
 */
public class Panier {

	private ArrayList<Article> articles;
	private static final float FORFAIT = 5.5f;

	/**
	 * Crée un nouveau panier vide.
	 */
	public Panier() {
		this.articles = new ArrayList<>();
	}
	
	/**
	 * Retourne la liste des articles du panier.
	 * 
	 * @return la liste des articles
	 */
	public ArrayList<Article> getArticles() {
		return this.articles;
	}

	/**
	 * Vérifie si le panier est vide.
	 * 
	 * @return true si le panier ne contient aucun article
	 */
	public boolean estVide() {
		return this.articles.isEmpty();
	}

	/**
	 * Retourne le forfait de frais de port.
	 * 
	 * @return le montant du forfait (5,50€)
	 */
	public float getForfait() {
		return Panier.FORFAIT;
	}

	/**
	 * Calcule le nombre total d'articles dans le panier (somme des quantités).
	 * 
	 * @return le nombre total d'articles
	 */
	public int getNombreArticles() {
		int total = 0;
		for (Article article : this.articles) {
			total += article.getQuantite();
		}
		return total;
	}

	/**
	 * Calcule le sous-total du panier (prix des articles sans frais de port).
	 * 
	 * @return le montant total des articles
	 */
	public float getSousTotal() {
		float sousTotal = 0.0f;
		for (Article article : this.articles) {
			sousTotal += article.getTotal();
		}
		return sousTotal;
	}

	/**
	 * Calcule le total du panier (sous-total + forfait de port). Si le panier est
	 * vide, retourne 0.
	 * 
	 * @return le montant total TTC avec frais de port
	 */
	public float getTotal() {
		float sousTotal = this.getSousTotal();
		if (sousTotal > 0) {
			return sousTotal + Panier.FORFAIT;
		}
		return 0.0f;
	}

	/**
	 * Ajoute un article au panier et met à jour le stock de la tomate. Si la tomate
	 * existe déjà, additionne les quantités.
	 * 
	 * @param article l'article à ajouter
	 */
	public void ajouterArticle(Article article) {
		Tomate tomate = article.getTomate();
		int quantiteDemandee = article.getQuantite();

		// Vérifier si la tomate existe déjà dans le panier
		for (Article a : this.articles) {
			if (a.getTomate().equals(tomate)) {
				a.setQuantite(a.getQuantite() + quantiteDemandee);
				tomate.setStock(tomate.getStock() - quantiteDemandee);
				return;
			}
		}

		// Nouvelle tomate : ajouter l'article
		this.articles.add(article);
		tomate.setStock(tomate.getStock() - quantiteDemandee);
	}
	
	/**
	 * Retire un article du panier et remet la quantité en stock.
	 * 
	 * @param article l'article à retirer
	 */
	public void retirerArticle(Article article) {
		if (this.articles.remove(article)) {
			Tomate t = article.getTomate();
			t.setStock(t.getStock() + article.getQuantite());
		}
	}

	/**
	 * Vide entièrement le panier et remet tous les articles en stock.
	 */
	public void viderPanier() {
		// Remettre tous les articles en stock
		for (Article article : this.articles) {
			article.getTomate().setStock(article.getTomate().getStock() + article.getQuantite());
		}
		this.articles.clear();
	}
}