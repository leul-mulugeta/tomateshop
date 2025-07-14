package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import modèle.Couleur;
import modèle.OutilsBaseDonneesTomates;
import modèle.Panier;
import modèle.Tomate;
import modèle.Tomates;
import modèle.TypeTomate;

/**
 * Fenêtre principale de l'application Ô'Tomates permettant de naviguer dans le
 * catalogue des tomates, de filtrer par type et couleur, et d'accéder au panier
 * d'achat.
 */
public class FenetrePrincipale extends JFrame {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPane = new JPanel();

	// Panneaux principaux
	private JPanel panneauHaut;
	private JPanel panneauPanier;
	private JPanel panneauBas;
	private JPanel panneauFiltres;

	// Composants d'affichage du titre et catalogue
	private JLabel etiquetteTitrePage;
	private JList<String> listeAffichageTomates;
	private JScrollPane defileur;

	// Composants de filtrage
	private JLabel etiquetteImage;
	private JLabel etiquetteCouleur;
	private JComboBox<String> comboTousLesTypes;
	private JComboBox<String> comboToutesLesCouleurs;

	// Boutons d'action
	private JButton boutonPanier;
	private JButton boutonConseilsCulture;

	// Objets métier
	private Tomates baseTomates;
	private List<Tomate> listeTomates;
	private Panier panier;

	// Variables d'état des filtres
	private String valeurActuelleType;
	private String valeurActuelleCouleur;

	/**
	 * Lancer l'application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					FenetrePrincipale fenetre = new FenetrePrincipale();
					fenetre.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Crée la fenêtre principale de l'application.
	 */
	public FenetrePrincipale() {
		// Configuration de base de la fenêtre
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(600, 600));
		this.setTitle("Ô'Tomates");

		// Configuration de l'icône
		ImageIcon iconeFenetre = new ImageIcon("src/main/resources/images/Icones/icone-tomates.png");
		Image imageFenetre = iconeFenetre.getImage();
		this.setIconImage(imageFenetre);

		// Initialisation des données
		String cheminFichier = "src/main/resources/data/tomates.json";
		this.baseTomates = OutilsBaseDonneesTomates.générationBaseDeTomates(cheminFichier);
		this.listeTomates = this.baseTomates.getTomates();
		this.panier = new Panier();

		// Construction de l'interface utilisateur
		this.initialiserComposants();
		this.configurerDisposition();

		// Attachement des contrôleurs d'événements
		this.construireControleurBoutonPanier();
		this.construireControleurBoutonConseilsCulture();
		this.construireControleurListe();
		this.construireControleurComboBoxTypes();
		this.construireControleurComboBoxCouleurs();

		// Finalisation de l'affichage
		this.mettreAJourBoutonPanier();
		this.setLocationRelativeTo(null);
	}

	/**
	 * Initialise tous les composants de l'interface utilisateur.
	 */
	private void initialiserComposants() {
		// Configuration du panneau principal
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setBackground(new Color(230, 230, 230));
		this.contentPane.setLayout(new BorderLayout(10, 10));
		this.setContentPane(this.contentPane);

		// Panneaux principaux
		this.panneauHaut = new JPanel(new BorderLayout(10, 0));
		this.panneauHaut.setBackground(new Color(230, 230, 230));

		this.panneauPanier = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		this.panneauPanier.setBackground(new Color(255, 218, 185));
		this.panneauPanier.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		this.panneauBas = new JPanel(new BorderLayout(10, 10));
		this.panneauBas.setBackground(new Color(230, 230, 230));

		this.panneauFiltres = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		this.panneauFiltres.setBackground(new Color(230, 230, 230));
		this.panneauFiltres.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2), "Filtres",
						TitledBorder.LEFT, TitledBorder.TOP, new Font("Dialog", Font.BOLD, 14),
						new Color(34, 139, 34)));

		// Composants du titre
		this.etiquetteTitrePage = new JLabel("Nos graines de tomates");
		this.etiquetteTitrePage.setHorizontalAlignment(SwingConstants.CENTER);
		this.etiquetteTitrePage.setFont(new Font("Comic Sans MS", Font.PLAIN | Font.ITALIC, 24));
		this.etiquetteTitrePage.setForeground(new Color(34, 139, 34));
		this.etiquetteTitrePage.setIcon(new ImageIcon("src/main/resources/images/Icones/icone-panier-osier.png"));

		// Liste des tomates
		this.listeAffichageTomates = new JList<>(this.extraireDesignationTomates());
		this.listeAffichageTomates.setFont(new Font("Dialog", Font.BOLD, 17));
		this.listeAffichageTomates.setBackground(Color.WHITE);

		this.defileur = new JScrollPane();
		this.defileur.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		this.defileur.setViewportView(this.listeAffichageTomates);

		// Composants de filtrage
		this.etiquetteImage = new JLabel();
		this.etiquetteImage.setIcon(new ImageIcon("src/main/resources/images/Icones/icone-filtre-tomates.png"));

		this.comboTousLesTypes = new JComboBox<>();
		this.comboTousLesTypes.addItem("Toutes les tomates");
		for (TypeTomate type : TypeTomate.values()) {
			this.comboTousLesTypes.addItem(type.getDénomination());
		}
		this.comboTousLesTypes.setFont(new Font("Dialog", Font.BOLD, 12));
		this.comboTousLesTypes.setPreferredSize(new Dimension(150, 25));
		this.valeurActuelleType = (String) this.comboTousLesTypes.getSelectedItem();

		this.etiquetteCouleur = new JLabel();
		this.etiquetteCouleur.setIcon(new ImageIcon("src/main/resources/images/Icones/icone-couleurs.png"));

		this.comboToutesLesCouleurs = new JComboBox<>();
		this.comboToutesLesCouleurs.addItem("Toutes les couleurs");
		for (Couleur couleur : Couleur.values()) {
			this.comboToutesLesCouleurs.addItem(couleur.getDénomination());
		}
		this.comboToutesLesCouleurs.setFont(new Font("Dialog", Font.BOLD, 12));
		this.comboToutesLesCouleurs.setPreferredSize(new Dimension(150, 25));
		this.valeurActuelleCouleur = (String) this.comboToutesLesCouleurs.getSelectedItem();

		// Boutons d'action
		this.boutonPanier = new JButton();
		this.boutonPanier.setBorderPainted(false);
		this.boutonPanier.setFont(new Font("Dialog", Font.BOLD, 11));
		this.boutonPanier.setBackground(new Color(255, 218, 185));
		this.boutonPanier.setIcon(new ImageIcon("src/main/resources/images/Icones/icone-panier.png"));

		this.boutonConseilsCulture = new JButton();
		this.boutonConseilsCulture
				.setIcon(new ImageIcon("src/main/resources/images/Icones/icone-conseils-culture.png"));
		this.boutonConseilsCulture.setPreferredSize(new Dimension(60, 60));
	}

	/**
	 * Configure la disposition de tous les composants dans l'interface.
	 */
	private void configurerDisposition() {
		// Panneau Nord (titre et panier)
		this.panneauHaut.add(this.etiquetteTitrePage, BorderLayout.CENTER);
		this.panneauPanier.add(this.boutonPanier);
		this.panneauHaut.add(this.panneauPanier, BorderLayout.EAST);
		this.contentPane.add(this.panneauHaut, BorderLayout.NORTH);

		// Panneau Centre (liste des tomates)
		this.contentPane.add(this.defileur, BorderLayout.CENTER);

		// Panneau Sud (filtres et bouton conseils)
		this.panneauFiltres.add(this.etiquetteImage);
		this.panneauFiltres.add(this.comboTousLesTypes);
		this.panneauFiltres.add(this.etiquetteCouleur);
		this.panneauFiltres.add(this.comboToutesLesCouleurs);

		this.panneauBas.add(this.panneauFiltres, BorderLayout.CENTER);
		this.panneauBas.add(this.boutonConseilsCulture, BorderLayout.EAST);
		this.contentPane.add(this.panneauBas, BorderLayout.SOUTH);
	}

	/**
	 * Configure le contrôleur du bouton Panier.
	 */
	private void construireControleurBoutonPanier() {
		this.boutonPanier.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (FenetrePrincipale.this.panier.estVide()) {
					JOptionPane.showMessageDialog(FenetrePrincipale.this, "Le panier est vide.", "Information",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					FenetrePanier fenetrePanier = new FenetrePanier(FenetrePrincipale.this,
							FenetrePrincipale.this.panier, FenetrePrincipale.this.baseTomates);
					fenetrePanier.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent e) {
							FenetrePrincipale.this.mettreAJourBoutonPanier();
						}
					});
					fenetrePanier.setVisible(true);
				}
			}
		});
	}

	/**
	 * Configure le contrôleur du bouton Conseils de Culture.
	 */
	private void construireControleurBoutonConseilsCulture() {
		this.boutonConseilsCulture.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FenetreCulture fenetreCulture = new FenetreCulture(FenetrePrincipale.this);
				fenetreCulture.setVisible(true);
			}
		});
	}

	/**
	 * Configure le contrôleur de la liste des tomates.
	 */
	private void construireControleurListe() {
		this.listeAffichageTomates.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				if (!event.getValueIsAdjusting()) {
					String nomTomate = FenetrePrincipale.this.listeAffichageTomates.getSelectedValue();
					Tomate tomate = FenetrePrincipale.this.baseTomates.getTomate(nomTomate);

					if (tomate != null) {
						FenetreDetailTomate fenetreDetail = new FenetreDetailTomate(FenetrePrincipale.this, tomate,
								FenetrePrincipale.this.baseTomates, FenetrePrincipale.this.panier);
						fenetreDetail.setVisible(true);
					}
				}
			}
		});
	}

	/**
	 * Configure le contrôleur de la ComboBox des types.
	 */
	private void construireControleurComboBoxTypes() {
		this.comboTousLesTypes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String valeurSelectionnee = (String) FenetrePrincipale.this.comboTousLesTypes.getSelectedItem();
				if (!FenetrePrincipale.this.valeurActuelleType.equals(valeurSelectionnee)) {
					FenetrePrincipale.this.valeurActuelleType = valeurSelectionnee;
					FenetrePrincipale.this.actualiserListe();
				}
			}
		});
	}

	/**
	 * Configure le contrôleur de la ComboBox des couleurs.
	 */
	private void construireControleurComboBoxCouleurs() {
		this.comboToutesLesCouleurs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String valeurSelectionnee = (String) FenetrePrincipale.this.comboToutesLesCouleurs.getSelectedItem();
				if (!FenetrePrincipale.this.valeurActuelleCouleur.equals(valeurSelectionnee)) {
					FenetrePrincipale.this.valeurActuelleCouleur = valeurSelectionnee;
					FenetrePrincipale.this.actualiserListe();
				}
			}
		});
	}

	/**
	 * Extrait les désignations de toutes les tomates pour affichage dans la liste.
	 *
	 * @return tableau des désignations des tomates
	 */
	private String[] extraireDesignationTomates() {
		String[] designationTomates = new String[this.listeTomates.size()];
		for (int i = 0; i < this.listeTomates.size(); i++) {
			designationTomates[i] = this.listeTomates.get(i).getDésignation();
		}
		return designationTomates;
	}

	/**
	 * Met à jour la liste des tomates selon les filtres sélectionnés.
	 */
	private void actualiserListe() {
		List<String> designationTomates = new ArrayList<>();
		for (Tomate t : this.listeTomates) {
			boolean filtreCouleur = this.valeurActuelleCouleur.equals("Toutes les couleurs")
					|| t.getCouleur().getDénomination().equals(this.valeurActuelleCouleur);
			boolean filtreType = this.valeurActuelleType.equals("Toutes les tomates")
					|| t.getType().getDénomination().equals(this.valeurActuelleType);
			if (filtreCouleur && filtreType) {
				designationTomates.add(t.getDésignation());
			}
		}
		this.listeAffichageTomates.setListData(designationTomates.toArray(new String[0]));
	}

	/**
	 * Met à jour l'affichage du bouton panier avec le montant total. Affiche
	 * "0.00€" si le panier est vide, sinon affiche le total.
	 */
	public void mettreAJourBoutonPanier() {
		if (this.panier != null && this.boutonPanier != null) {
			float montantAAfficher = this.panier.estVide() ? 0.0f : this.panier.getTotal();
			this.boutonPanier.setText(String.format("%.2f€", montantAAfficher));
		} else if (this.boutonPanier != null) {
			this.boutonPanier.setText("0.00€");
		}
	}
}