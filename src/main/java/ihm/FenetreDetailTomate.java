package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import modèle.Article;
import modèle.Panier;
import modèle.Tomate;
import modèle.Tomates;

/**
 * Fenêtre de détail pour une tomate, permettant de consulter ses informations
 * et de l'ajouter au panier.
 */
public class FenetreDetailTomate extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane = new JPanel();

	// Composants d'affichage
	private JLabel etiquetteNomTomate;
	private JLabel etiquetteImageTomate;
	private JLabel etiquetteEnStock;
	private JTextArea zoneDescription;
	private JTextField champGraines;
	private JTextField champPrix;

	// Composants de sélection
	private JComboBox<String> comboTomatesApparentees;
	private JSpinner selecteurQuantite;

	// Boutons d'action
	private JButton boutonAjouterAuPanier;
	private JButton boutonAnnuler;

	// Panneaux pour l'organisation
	private JPanel panneauImageStockApparentees;
	private JPanel panneauCombo;

	// Objets métier
	private Tomate tomateAffichee;
	private Panier panier;
	private Tomates baseTomates;
	private FenetrePrincipale fenetrePrincipale;

	/**
	 * Crée la fenêtre de détail d'une tomate.
	 *
	 * @param parent      la fenêtre parente
	 * @param tomate      la tomate à afficher
	 * @param baseTomates la base de données des tomates
	 * @param panier      le panier courant
	 */
	public FenetreDetailTomate(Window parent, Tomate tomate, Tomates baseTomates, Panier panier) {
		super(parent, "Ô'Tomates");

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setMinimumSize(new Dimension(550, 500));

		// Configuration de l'icône
		ImageIcon iconeFenetre = new ImageIcon("src/main/resources/images/Icones/icone-tomates.png");
		Image imageFenetre = iconeFenetre.getImage();
		this.setIconImage(imageFenetre);

		// Initialisation des données
		this.fenetrePrincipale = (FenetrePrincipale) parent;
		this.baseTomates = baseTomates;
		this.panier = panier;
		this.tomateAffichee = tomate;

		// Construction de l'interface utilisateur
		this.initialiserComposants();
		this.configurerDisposition();

		// Attachement des contrôleurs d'événements
		this.construireControleurSelecteurQuantite();
		this.construireControleurComboTomatesApparentees();
		this.construireControleurBoutonAjouterAuPanier();
		this.construireControleurBoutonAnnuler();

		// Finalisation de l'affichage
		this.mettreAJourUIStock(tomate);
		this.pack();
		this.setLocationRelativeTo(parent);
	}

	/**
	 * Initialise tous les composants de l'interface utilisateur.
	 */
	private void initialiserComposants() {
		// Configuration du panneau principal
		this.contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.contentPane.setBackground(new Color(230, 230, 230));

		// Titre de la tomate
		this.etiquetteNomTomate = new JLabel(this.tomateAffichee.getDésignation());
		this.etiquetteNomTomate.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 22));
		this.etiquetteNomTomate.setForeground(new Color(34, 139, 34));

		// Image de la tomate
		this.etiquetteImageTomate = new JLabel();
		String cheminImage = "src/main/resources/images/Tomates200x200/" + this.tomateAffichee.getNomImage() + ".jpg";
		this.etiquetteImageTomate.setIcon(new ImageIcon(cheminImage));
		this.etiquetteImageTomate.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Statut du stock
		this.etiquetteEnStock = new JLabel();
		this.etiquetteEnStock.setHorizontalAlignment(SwingConstants.CENTER);
		this.etiquetteEnStock.setFont(new Font("Dialog", Font.BOLD, 14));
		this.etiquetteEnStock.setAlignmentX(Component.CENTER_ALIGNMENT);

		// ComboBox des tomates apparentées
		this.comboTomatesApparentees = new JComboBox<>();
		this.comboTomatesApparentees.addItem("Produits similaires");
		List<Tomate> tomatesApparentees = this.tomateAffichee.getTomatesApparentées();
		for (Tomate ta : tomatesApparentees) {
			this.comboTomatesApparentees.addItem(ta.getDésignation());
		}
		this.comboTomatesApparentees.setBackground(Color.WHITE);
		this.comboTomatesApparentees.setPreferredSize(new Dimension(200, 25));

		// Zone de description
		this.zoneDescription = new JTextArea();
		this.zoneDescription.setBackground(Color.WHITE);
		this.zoneDescription.setText(this.tomateAffichee.getDescription());
		this.zoneDescription.setWrapStyleWord(true);
		this.zoneDescription.setLineWrap(true);
		this.zoneDescription.setEditable(false);
		this.zoneDescription.setFont(new Font("Dialog", Font.PLAIN, 13));
		this.zoneDescription.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		this.zoneDescription.setMargin(new Insets(5, 5, 5, 5));

		// Champs d'informations
		this.champGraines = new JTextField(String.valueOf(this.tomateAffichee.getNbGrainesParSachet()));
		this.champGraines.setEditable(false);
		this.champGraines.setColumns(8);
		this.champGraines.setFont(new Font("Dialog", Font.PLAIN, 12));

		this.champPrix = new JTextField(
				String.format(java.util.Locale.FRANCE, "%.2f", this.tomateAffichee.getPrixTTC()) + " €");
		this.champPrix.setEditable(false);
		this.champPrix.setColumns(10);
		this.champPrix.setFont(new Font("Dialog", Font.BOLD, 14));

		// Sélecteur de quantité
		this.selecteurQuantite = new JSpinner(
				new SpinnerNumberModel(1, 1, Math.max(1, this.tomateAffichee.getStock()), 1));

		// Boutons d'action
		this.boutonAjouterAuPanier = new JButton("Ajouter au panier");
		this.boutonAjouterAuPanier.setBackground(new Color(144, 238, 144));
		this.boutonAjouterAuPanier.setForeground(Color.BLACK);
		this.boutonAjouterAuPanier.setFont(new Font("Dialog", Font.BOLD, 12));

		this.boutonAnnuler = new JButton("Annuler");
		this.boutonAnnuler.setBackground(new Color(255, 192, 203));
		this.boutonAnnuler.setForeground(Color.BLACK);
		this.boutonAnnuler.setFont(new Font("Dialog", Font.BOLD, 12));
	}

	/**
	 * Configure la disposition de tous les composants dans la fenêtre.
	 */
	private void configurerDisposition() {
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(new BorderLayout(10, 10));

		// Titre en haut
		this.contentPane.add(this.etiquetteNomTomate, BorderLayout.NORTH);

		// Panneau central avec image/stock à gauche et description à droite
		JPanel panneauCentre = new JPanel();
		panneauCentre.setBackground(new Color(230, 230, 230));
		panneauCentre.setLayout(new BoxLayout(panneauCentre, BoxLayout.X_AXIS));

		// Sous-panneau gauche avec image et stock
		this.panneauImageStockApparentees = new JPanel();
		this.panneauImageStockApparentees.setBackground(new Color(230, 230, 230));
		this.panneauImageStockApparentees.setLayout(new BoxLayout(this.panneauImageStockApparentees, BoxLayout.Y_AXIS));
		this.panneauImageStockApparentees.add(this.etiquetteImageTomate);
		this.panneauImageStockApparentees.add(Box.createVerticalStrut(20));
		this.panneauImageStockApparentees.add(this.etiquetteEnStock);
		this.panneauImageStockApparentees.add(Box.createVerticalStrut(20));

		this.panneauCombo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.panneauCombo.setBackground(new Color(230, 230, 230));
		this.panneauCombo.add(this.comboTomatesApparentees);

		panneauCentre.add(this.panneauImageStockApparentees);
		panneauCentre.add(Box.createHorizontalStrut(50));

		// Sous-panneau droit avec description et informations
		JPanel panneauDescription = new JPanel(new BorderLayout());
		panneauDescription.setBackground(new Color(230, 230, 230));

		JLabel etiquetteDescriptionTitre = new JLabel("Description");
		etiquetteDescriptionTitre.setFont(new Font("Dialog", Font.BOLD, 14));
		etiquetteDescriptionTitre.setForeground(new Color(34, 139, 34));
		etiquetteDescriptionTitre.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		panneauDescription.add(etiquetteDescriptionTitre, BorderLayout.NORTH);

		JScrollPane defileurDescription = new JScrollPane(this.zoneDescription);
		panneauDescription.add(defileurDescription, BorderLayout.CENTER);

		// Panneau des informations (graines, prix, quantité)
		JPanel panneauInfos = new JPanel(new GridBagLayout());
		panneauInfos.setBackground(new Color(230, 230, 230));

		// Ligne 1: Graines
		GridBagConstraints gbcEtiquetteGraines = new GridBagConstraints();
		gbcEtiquetteGraines.insets = new Insets(5, 5, 5, 5);
		gbcEtiquetteGraines.gridx = 0;
		gbcEtiquetteGraines.gridy = 0;
		gbcEtiquetteGraines.anchor = GridBagConstraints.WEST;
		panneauInfos.add(new JLabel("Nombre de graines :"), gbcEtiquetteGraines);

		GridBagConstraints gbcChampGraines = new GridBagConstraints();
		gbcChampGraines.insets = new Insets(5, 5, 5, 5);
		gbcChampGraines.gridx = 1;
		gbcChampGraines.gridy = 0;
		gbcChampGraines.fill = GridBagConstraints.HORIZONTAL;
		gbcChampGraines.weightx = 1.0;
		panneauInfos.add(this.champGraines, gbcChampGraines);

		// Ligne 2: Prix et quantité
		GridBagConstraints gbcEtiquettePrix = new GridBagConstraints();
		gbcEtiquettePrix.insets = new Insets(5, 5, 5, 5);
		gbcEtiquettePrix.gridx = 0;
		gbcEtiquettePrix.gridy = 1;
		gbcEtiquettePrix.anchor = GridBagConstraints.WEST;
		JLabel etiquettePrix = new JLabel("Prix :");
		etiquettePrix.setFont(new Font("Dialog", Font.BOLD, 14));
		panneauInfos.add(etiquettePrix, gbcEtiquettePrix);

		GridBagConstraints gbcChampPrix = new GridBagConstraints();
		gbcChampPrix.insets = new Insets(5, 5, 5, 5);
		gbcChampPrix.gridx = 1;
		gbcChampPrix.gridy = 1;
		gbcChampPrix.fill = GridBagConstraints.HORIZONTAL;
		gbcChampPrix.weightx = 1.0;
		panneauInfos.add(this.champPrix, gbcChampPrix);

		GridBagConstraints gbcSelecteurQuantite = new GridBagConstraints();
		gbcSelecteurQuantite.insets = new Insets(5, 5, 5, 5);
		gbcSelecteurQuantite.gridx = 2;
		gbcSelecteurQuantite.gridy = 1;
		gbcSelecteurQuantite.anchor = GridBagConstraints.EAST;
		panneauInfos.add(this.selecteurQuantite, gbcSelecteurQuantite);

		panneauDescription.add(panneauInfos, BorderLayout.SOUTH);
		panneauCentre.add(panneauDescription);
		this.contentPane.add(panneauCentre, BorderLayout.CENTER);

		// Panneau des boutons en bas
		JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		panneauBoutons.setBackground(new Color(230, 230, 230));
		panneauBoutons.add(this.boutonAjouterAuPanier);
		panneauBoutons.add(this.boutonAnnuler);
		this.contentPane.add(panneauBoutons, BorderLayout.SOUTH);
	}

	/**
	 * Configure le contrôleur du sélecteur de quantité.
	 */
	private void construireControleurSelecteurQuantite() {
		this.selecteurQuantite.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int quantite = (int) FenetreDetailTomate.this.selecteurQuantite.getValue();
				float prixTotal;
				if (quantite == 0) {
					prixTotal = FenetreDetailTomate.this.tomateAffichee.getPrixTTC();
				} else {
					prixTotal = FenetreDetailTomate.this.tomateAffichee.getPrixTTC() * quantite;
				}
				FenetreDetailTomate.this.champPrix
						.setText(String.format(java.util.Locale.FRANCE, "%.2f", prixTotal) + " €");
			}
		});
	}

	/**
	 * Configure le contrôleur de la ComboBox des tomates apparentées.
	 */
	private void construireControleurComboTomatesApparentees() {
		this.comboTomatesApparentees.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String designationTomateApparentee = (String) FenetreDetailTomate.this.comboTomatesApparentees
						.getSelectedItem();
				if (designationTomateApparentee != null && !designationTomateApparentee.equals("Produits similaires")) {
					Tomate tomateSelectionnee = FenetreDetailTomate.this.baseTomates
							.getTomate(designationTomateApparentee);
					FenetreDetailTomate nouvelleFenetre = new FenetreDetailTomate(
							FenetreDetailTomate.this.fenetrePrincipale, tomateSelectionnee,
							FenetreDetailTomate.this.baseTomates, FenetreDetailTomate.this.panier);
					nouvelleFenetre.setVisible(true);
				}
			}
		});
	}

	/**
	 * Configure le contrôleur du bouton Ajouter au panier.
	 */
	private void construireControleurBoutonAjouterAuPanier() {
		this.boutonAjouterAuPanier.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int quantiteSachets = (int) FenetreDetailTomate.this.selecteurQuantite.getValue();
				Article nouveauArticle = new Article(FenetreDetailTomate.this.tomateAffichee, quantiteSachets);
				FenetreDetailTomate.this.panier.ajouterArticle(nouveauArticle);

				FenetreDetailTomate.this.fenetrePrincipale.mettreAJourBoutonPanier();

				String message = String.format(
						"%d sachet%s de %s ajouté%s au panier.\nNombre total d'articles dans le panier: %d",
						quantiteSachets, quantiteSachets > 1 ? "s" : "",
						FenetreDetailTomate.this.tomateAffichee.getDésignation(), quantiteSachets > 1 ? "s" : "",
						FenetreDetailTomate.this.panier.getNombreArticles());

				JOptionPane.showMessageDialog(FenetreDetailTomate.this, message, "Ajout au panier",
						JOptionPane.INFORMATION_MESSAGE);
				FenetreDetailTomate.this.dispose();
			}
		});
	}

	/**
	 * Configure le contrôleur du bouton Annuler.
	 */
	private void construireControleurBoutonAnnuler() {
		this.boutonAnnuler.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FenetreDetailTomate.this.dispose();
			}
		});
	}

	/**
	 * Met à jour l'affichage selon le stock de la tomate.
	 * 
	 * @param tomate la tomate à traiter
	 */
	private void mettreAJourUIStock(Tomate tomate) {
		int stock = tomate.getStock();

		// Configuration du spinner selon le stock
		SpinnerNumberModel modele = (SpinnerNumberModel) this.selecteurQuantite.getModel();
		if (stock > 0) {
			modele.setMinimum(1);
			modele.setMaximum(stock);
			modele.setValue(1);
			this.selecteurQuantite.setEnabled(true);
		} else {
			modele.setMinimum(0);
			modele.setMaximum(0);
			modele.setValue(0);
			this.selecteurQuantite.setEnabled(false);
		}

		// Mise à jour de l'affichage du stock
		if (stock > 0) {
			this.etiquetteEnStock.setText("En Stock");
			this.etiquetteEnStock.setForeground(new Color(34, 139, 34));
			this.boutonAjouterAuPanier.setEnabled(true);

			// Masquer les produits similaires si en stock
			if (this.panneauCombo.getParent() != null) {
				this.panneauImageStockApparentees.remove(this.panneauCombo);
			}
		} else {
			this.etiquetteEnStock.setText("Rupture de Stock");
			this.etiquetteEnStock.setForeground(Color.RED);
			this.boutonAjouterAuPanier.setEnabled(false);

			// Afficher les produits similaires si rupture
			if (this.panneauCombo.getParent() == null) {
				this.panneauImageStockApparentees.add(this.panneauCombo);
			}
		}

		this.panneauImageStockApparentees.revalidate();
		this.panneauImageStockApparentees.repaint();
	}
}