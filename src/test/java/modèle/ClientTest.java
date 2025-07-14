package modèle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests unitaires pour la classe Client. Vérifie que tous les getters
 * retournent les bonnes valeurs et que les objets associés sont correctement
 * initialisés.
 */
public class ClientTest {

	private Client client;
	private Panier panier;

	/**
	 * Initialise un client avec toutes ses informations pour les tests.
	 */
	@Before
	public void setUp() {
		this.panier = new Panier();
		this.client = new Client("Jean", "Dupont", "123 rue de Toulouse", "Toulouse", "31000",
				"0600000000", "jean@gmail.com", "Paypal", true, this.panier);
	}
	
	/**
	 * Nettoie les objets après chaque test.
	 */
	@After
	public void tearDown() {
		this.client = null;
		this.panier = null;
	}

	/**
	 * Teste tous les getters pour les informations personnelles du client.
	 */
	@Test
	public void testGetters() {
		assertEquals("Jean", this.client.getPrenom());
		assertEquals("Dupont", this.client.getNom());
		assertEquals("123 rue de Toulouse", this.client.getAdresse());
		assertEquals("Toulouse", this.client.getVille());
		assertEquals("31000", this.client.getCodePostal());
		assertEquals("0600000000", this.client.getNumeroTel());
		assertEquals("jean@gmail.com", this.client.getMail());
	}

	/**
	 * Teste le getter pour le moyen de paiement.
	 */
	@Test
	public void testGetMoyenPaiement() {
		assertEquals("Paypal", this.client.getMoyenPaiement());
	}

	/**
	 * Teste le getter pour la newsletter (valeur true).
	 */
	@Test
	public void testGetNewsletterTrue() {
		assertTrue(this.client.getNewsletter());
	}

	/**
	 * Teste le getter pour la newsletter (valeur false).
	 */
	@Test
	public void testGetNewsletterFalse() {
		Client clientSansNewsletter = new Client("Marie", "Martin", "456 avenue de Paris", "Paris", "75001",
				"0700000000", "marie@gmail.com", "Carte bancaire", false, new Panier());
		assertFalse(clientSansNewsletter.getNewsletter());
	}

	/**
	 * Teste que getPanier() retourne le panier associé au client.
	 */
	@Test
	public void testGetPanier() {
		assertNotNull(this.client.getPanier());
		assertEquals(this.panier, this.client.getPanier());
	}
}