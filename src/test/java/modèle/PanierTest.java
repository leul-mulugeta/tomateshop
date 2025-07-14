package modèle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests unitaires pour la classe Panier. Vérifie la gestion des articles, les
 * calculs de totaux et la gestion du stock.
 */
public class PanierTest {

	private Tomates tomates;
	private Panier panier;
	private Tomate tomate1;
	private Tomate tomate2;
	private Tomate tomate3;

	/**
	 * Initialise un panier avec 2 articles pour les tests.
	 */
	@Before
	public void setUp() {
		this.tomates = OutilsBaseDonneesTomates.générationBaseDeTomates("src/main/resources/data/tomates.json");
		this.tomate1 = this.tomates.getTomates().get(0);
		this.tomate2 = this.tomates.getTomates().get(1);
		this.tomate3 = this.tomates.getTomates().get(2);

		this.panier = new Panier();
		this.panier.ajouterArticle(new Article(this.tomate1, 2));
		this.panier.ajouterArticle(new Article(this.tomate2, 1));
	}

	/**
	 * Nettoie les objets après chaque test.
	 */
	@After
	public void tearDown() {
		this.tomates = null;
		this.panier = null;
		this.tomate1 = null;
		this.tomate2 = null;
		this.tomate3 = null;
	}

	/**
	 * Teste que getArticles() retourne la liste correcte des articles.
	 */
	@Test
	public void testGetArticles() {
		assertEquals(2, this.panier.getArticles().size());

		boolean contientTomate1 = false;
		boolean contientTomate2 = false;

		for (Article p : this.panier.getArticles()) {
			if (p.getTomate().equals(this.tomate1)) {
				contientTomate1 = true;
			}
			if (p.getTomate().equals(this.tomate2)) {
				contientTomate2 = true;
			}
		}

		assertTrue(contientTomate1);
		assertTrue(contientTomate2);
	}

	/**
	 * Teste la méthode estVide() avec un panier rempli puis vidé.
	 */
	@Test
	public void testEstVide() {
		assertFalse(this.panier.estVide());
		this.panier.viderPanier();
		assertTrue(this.panier.estVide());
	}

	/**
	 * Teste que getForfait() retourne le montant correct (5,50€).
	 */
	@Test
	public void testGetForfait() {
		assertEquals(5.5f, this.panier.getForfait(), 0.001);
	}

	/**
	 * Teste le calcul du nombre total d'articles (somme des quantités).
	 */
	@Test
	public void testGetNombreArticles() {
		assertEquals(3, this.panier.getNombreArticles());
	}

	/**
	 * Teste le calcul du sous-total (prix des articles sans frais de port).
	 */
	@Test
	public void testGetSousTotal() {
		float attendu = this.tomate1.getPrixTTC() * 2 + this.tomate2.getPrixTTC();
		assertEquals(attendu, this.panier.getSousTotal(), 0.001);
	}

	/**
	 * Teste le calcul du total (sous-total + forfait de port).
	 */
	@Test
	public void testGetTotal() {
		float attendu = (this.tomate1.getPrixTTC() * 2 + this.tomate2.getPrixTTC()) + 5.5f;
		assertEquals(attendu, this.panier.getTotal(), 0.001);
	}

	/**
	 * Teste l'ajout d'une tomate déjà présente (fusion des quantités).
	 */
	@Test
	public void testAjouterProduitFusionneQuantite() {
		this.panier.ajouterArticle(new Article(this.tomate1, 3));
		for (Article p : this.panier.getArticles()) {
			if (p.getTomate().equals(this.tomate1)) {
				assertEquals(5, p.getQuantite());
			}
		}
	}

	/**
	 * Teste l'ajout d'une nouvelle tomate au panier.
	 */
	@Test
	public void testAjouterNouveauProduit() {
		int tailleAvant = this.panier.getArticles().size();
		this.panier.ajouterArticle(new Article(this.tomate3, 2));
		assertEquals(tailleAvant + 1, this.panier.getArticles().size());

		boolean contient = false;
		for (Article p : this.panier.getArticles()) {
			if (p.getTomate().equals(this.tomate3)) {
				assertEquals(2, p.getQuantite());
				contient = true;
			}
		}
		assertTrue(contient);
	}

	/**
	 * Teste la suppression d'un article existant du panier.
	 */
	@Test
	public void testRetirerProduit() {
		Article produitARetirer = null;
		for (Article p : this.panier.getArticles()) {
			if (p.getTomate().equals(this.tomate2)) {
				produitARetirer = p;
				break;
			}
		}
		this.panier.retirerArticle(produitARetirer);

		assertEquals(1, this.panier.getArticles().size());
		for (Article p : this.panier.getArticles()) {
			assertNotEquals(this.tomate2, p.getTomate());
		}
	}

	/**
	 * Teste la suppression d'un article inexistant (ne doit rien changer).
	 */
	@Test
	public void testRetirerProduitInexistant() {
		Article produitInexistant = new Article(this.tomate3, 1);
		int tailleAvant = this.panier.getArticles().size();
		this.panier.retirerArticle(produitInexistant);
		assertEquals(tailleAvant, this.panier.getArticles().size());
	}

	/**
	 * Teste que viderPanier() supprime tous les articles.
	 */
	@Test
	public void testViderPanier() {
		this.panier.viderPanier();
		assertTrue(this.panier.getArticles().isEmpty());
	}
}