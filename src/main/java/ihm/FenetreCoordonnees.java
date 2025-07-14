package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import modèle.Client;
import modèle.Panier;
import modèle.Tomates;

/**
 * Fenêtre de saisie des coordonnées client pour finaliser une commande. Permet
 * de saisir les informations personnelles, choisir le moyen de paiement et
 * l'abonnement à la newsletter.
 */
public class FenetreCoordonnees extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane = new JPanel();

	// Champs de saisie des coordonnées
	private JTextField champNom;
	private JTextField champPrenom;
	private JTextField champAdresse;
	private JTextField champCodePostal;
	private JTextField champVille;
	private JTextField champTelephone;
	private JTextField champMail;

	// Composants pour le choix du moyen de paiement
	private JRadioButton radioCarteCredit;
	private JRadioButton radioPaypal;
	private JRadioButton radioCheque;
	private ButtonGroup groupePaiement;

	// Composants pour l'abonnement newsletter
	private JRadioButton radioNewsletterOui;
	private JRadioButton radioNewsletterNon;
	private ButtonGroup groupeNewsletter;

	// Boutons d'action
	private JPanel panneauBoutons;
	private JButton btnOK;
	private JButton btnAnnuler;

	// Objets métier
	private Client client;
	private Panier panier;
	private Tomates baseTomates;

	/**
	 * Crée la fenêtre de saisie des coordonnées.
	 *
	 * @param parent      la fenêtre parente
	 * @param panier      le panier contenant les articles à commander
	 * @param baseTomates la base de données des tomates
	 */
	public FenetreCoordonnees(Window parent, Panier panier, Tomates baseTomates) {
		super(parent, "Ô'Tomates", ModalityType.APPLICATION_MODAL);

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(true);

		// Configuration de l'icône
		ImageIcon iconeFenetre = new ImageIcon("src/main/resources/images/Icones/icone-tomates.png");
		Image imageFenetre = iconeFenetre.getImage();
		this.setIconImage(imageFenetre);

		this.panier = panier;
		this.baseTomates = baseTomates;

		// Construction de l'interface utilisateur
		this.initialiserComposants();
		this.configurerDisposition();

		// Attachement des contrôleurs d'événements
		this.construireControleurBoutonOK();
		this.construireControleurBoutonAnnuler();

		// Finalisation de l'affichage
		this.pack();
		this.setLocationRelativeTo(parent);
	}

	/**
	 * Initialise tous les composants de l'interface utilisateur.
	 */
	private void initialiserComposants() {
		// Initialisation des champs de saisie
		this.champNom = new JTextField(20);
		this.champPrenom = new JTextField(20);
		this.champAdresse = new JTextField(20);
		this.champCodePostal = new JTextField(20);
		this.champVille = new JTextField(20);
		this.champTelephone = new JTextField(20);
		this.champMail = new JTextField(20);

		// Configuration des options de paiement
		this.radioCarteCredit = new JRadioButton("Carte de crédit", true);
		this.radioPaypal = new JRadioButton("Paypal");
		this.radioCheque = new JRadioButton("Chèque");
		this.groupePaiement = new ButtonGroup();
		this.groupePaiement.add(this.radioCarteCredit);
		this.groupePaiement.add(this.radioPaypal);
		this.groupePaiement.add(this.radioCheque);

		// Configuration de l'abonnement newsletter
		this.radioNewsletterOui = new JRadioButton("Oui", true);
		this.radioNewsletterNon = new JRadioButton("Non");
		this.groupeNewsletter = new ButtonGroup();
		this.groupeNewsletter.add(this.radioNewsletterOui);
		this.groupeNewsletter.add(this.radioNewsletterNon);
	}

	/**
	 * Configure la disposition de tous les composants dans la fenêtre.
	 */
	private void configurerDisposition() {
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPane.setLayout(new BoxLayout(this.contentPane, BoxLayout.Y_AXIS));
		this.contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Création de l'en-tête avec icône et titre
		JPanel panneauEntete = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel etiquetteIcone = new JLabel("");
		etiquetteIcone.setIcon(new ImageIcon("src/main/resources/images/Icones/icone-utilisateur.png"));
		etiquetteIcone.setFont(new Font("Dialog", Font.BOLD, 16));
		JLabel etiquetteTitre = new JLabel("Vos coordonnées");
		etiquetteTitre.setFont(new Font("Dialog", Font.BOLD, 16));
		etiquetteTitre.setForeground(new Color(0, 128, 0));
		panneauEntete.add(etiquetteIcone);
		panneauEntete.add(etiquetteTitre);

		// Création du formulaire de coordonnées
		JPanel panneauCoordonnees = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;

		String[] etiquettes = { "Nom :", "Prénom :", "Adresse :", "Code postal :", "Ville :", "Téléphone :", "Mail :" };
		JTextField[] champs = { this.champNom, this.champPrenom, this.champAdresse, this.champCodePostal,
				this.champVille, this.champTelephone, this.champMail };

		// Ajout dynamique des étiquettes et champs
		for (int i = 0; i < etiquettes.length; i++) {
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.fill = GridBagConstraints.NONE;
			gbc.weightx = 0.0;
			panneauCoordonnees.add(new JLabel(etiquettes[i]), gbc);

			gbc.gridx = 1;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1.0;
			panneauCoordonnees.add(champs[i], gbc);
		}

		// Panneau de sélection du moyen de paiement
		JPanel panneauPaiement = new JPanel(new FlowLayout(FlowLayout.LEFT));
		TitledBorder bordurePaiement = BorderFactory
				.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 128, 0), 2), "Moyen de paiement ");
		panneauPaiement.setBorder(bordurePaiement);
		panneauPaiement.add(this.radioCarteCredit);
		panneauPaiement.add(this.radioPaypal);
		panneauPaiement.add(this.radioCheque);

		// Panneau d'abonnement à la newsletter
		JPanel panneauNewsletter = new JPanel(new FlowLayout(FlowLayout.LEFT));
		TitledBorder bordureNewsletter = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new Color(0, 128, 0), 2), "Abonnement à notre Newsletter ");
		panneauNewsletter.setBorder(bordureNewsletter);
		panneauNewsletter.add(this.radioNewsletterOui);
		panneauNewsletter.add(this.radioNewsletterNon);

		// Assembly des composants principaux
		this.contentPane.add(panneauEntete);
		this.contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
		this.contentPane.add(panneauCoordonnees);
		this.contentPane.add(Box.createVerticalGlue());
		this.contentPane.add(panneauPaiement);
		this.contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
		this.contentPane.add(panneauNewsletter);

		this.getContentPane().add(this.contentPane, BorderLayout.CENTER);

		// Création des boutons d'action
		this.panneauBoutons = new JPanel();
		this.getContentPane().add(this.panneauBoutons, BorderLayout.SOUTH);
		this.panneauBoutons.setLayout(new FlowLayout(FlowLayout.RIGHT));

		this.btnOK = new JButton("OK");
		this.panneauBoutons.add(this.btnOK);

		this.btnAnnuler = new JButton("Annuler");
		this.panneauBoutons.add(this.btnAnnuler);
	}

	/**
	 * Configure le contrôleur du bouton OK pour valider et traiter la commande.
	 */
	private void construireControleurBoutonOK() {
		this.btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (FenetreCoordonnees.this.validerSaisie()) {
					// Détermination du moyen de paiement sélectionné
					String moyenPaiement = "Carte de crédit";
					if (FenetreCoordonnees.this.radioPaypal.isSelected()) {
						moyenPaiement = "Paypal";
					} else if (FenetreCoordonnees.this.radioCheque.isSelected()) {
						moyenPaiement = "Chèque";
					}

					boolean newsletter = FenetreCoordonnees.this.radioNewsletterOui.isSelected();

					// Création du client avec les données saisies
					String[] valeurs = FenetreCoordonnees.this.getValeursChampsNettoyees();
					FenetreCoordonnees.this.client = new Client(valeurs[0], valeurs[1], valeurs[2], valeurs[3],
							valeurs[4], valeurs[5], valeurs[6], moyenPaiement, newsletter,
							FenetreCoordonnees.this.panier);

					// Ouverture de la fenêtre de facture
					FenetreFacture fenetreFacture = new FenetreFacture(FenetreCoordonnees.this,
							FenetreCoordonnees.this.client, FenetreCoordonnees.this.baseTomates);
					fenetreFacture.setVisible(true);

					FenetreCoordonnees.this.dispose();
				}
			}
		});
	}

	/**
	 * Configure le contrôleur du bouton Annuler.
	 */
	private void construireControleurBoutonAnnuler() {
		this.btnAnnuler.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FenetreCoordonnees.this.dispose();
			}
		});
	}

	/**
	 * Récupère les valeurs des champs de saisie en supprimant les espaces
	 * superflus.
	 *
	 * @return un tableau contenant les valeurs nettoyées des champs
	 */
	private String[] getValeursChampsNettoyees() {
		return new String[] { this.champPrenom.getText().trim(), this.champNom.getText().trim(),
				this.champAdresse.getText().trim(), this.champVille.getText().trim(),
				this.champCodePostal.getText().trim(), this.champTelephone.getText().trim(),
				this.champMail.getText().trim() };
	}

	/**
	 * Valide la saisie des champs obligatoires et le format de l'email.
	 *
	 * @return true si la saisie est valide, false sinon
	 */
	private boolean validerSaisie() {
		String[] valeurs = this.getValeursChampsNettoyees();
		String[] nomsChamps = { "Prénom", "Nom", "Adresse", "Ville", "Code postal", "Téléphone", "Mail" };
		String erreurs = "";

		// Vérification des champs obligatoires
		for (int i = 0; i < valeurs.length; i++) {
			if (valeurs[i].isEmpty()) {
				erreurs += "- Le champ " + nomsChamps[i] + " est obligatoire.\n";
			}
		}

		if (!erreurs.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Les champs suivants sont obligatoires :\n\n" + erreurs,
					"Erreur de saisie", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Validation basique du format email
		String email = this.champMail.getText().trim();
		if (!email.contains("@") || !email.contains(".")) {
			JOptionPane.showMessageDialog(this, "Veuillez saisir une adresse email valide.", "Erreur de saisie",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}
}