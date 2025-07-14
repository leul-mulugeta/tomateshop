package modèle;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests unitaires pour la classe Article. Vérifie le bon fonctionnement des
 * getters, setters et calculs de total.
 */
public class ArticleTest {

	private Tomates tomates;
	private Tomate tomate;
	private Article article;

	/**
	 * Initialise les objets de test avant chaque test.
	 */
	@Before
	public void setUp() {
		this.tomates = OutilsBaseDonneesTomates.générationBaseDeTomates("src/main/resources/data/tomates.json");
		this.tomate = this.tomates.getTomates().get(0);
		this.article = new Article(this.tomate, 3);
	}

	/**
	 * Nettoie les objets après chaque test.
	 */
	@After
	public void tearDown() {
		this.tomates = null;
		this.tomate = null;
		this.article = null;
	}

	/**
	 * Teste que getTomate() retourne la tomate associée à l'article.
	 */
	@Test
	public void testGetTomate() {
		assertEquals(this.tomate, this.article.getTomate());
	}

	/**
	 * Teste que getQuantite() retourne la quantité initiale de l'article.
	 */
	@Test
	public void testGetQuantite() {
		assertEquals(3, this.article.getQuantite());
	}

	/**
	 * Teste que setQuantite() modifie correctement la quantité.
	 */
	@Test
	public void testSetQuantite() {
		this.article.setQuantite(5);
		assertEquals(5, this.article.getQuantite());
	}

	/**
	 * Teste le calcul du total (prix unitaire × quantité).
	 */
	@Test
	public void testGetTotal() {
		assertEquals(14.85f, this.article.getTotal(), 0.001);
	}

	/**
	 * Teste la création d'un article avec une quantité différente.
	 */
	@Test
	public void testConstructeurAvecQuantiteDifferente() {
		Article articleTest = new Article(this.tomate, 10);
		assertEquals(this.tomate, articleTest.getTomate());
		assertEquals(10, articleTest.getQuantite());
		assertEquals(this.tomate.getPrixTTC() * 10, articleTest.getTotal(), 0.001);
	}
}