package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import modèle.Article;
import modèle.Client;
import modèle.OutilsBaseDonneesTomates;
import modèle.Tomates;

/**
 * Fenêtre d'affichage de la facture du client, permettant de consulter le
 * détail de la commande et d'imprimer la facture.
 */
public class FenetreFacture extends JDialog {

	private static final long serialVersionUID = 1L;

	// Constantes d'affichage
	private static final int HAUTEUR_LIGNE = 25;
	private static final int HAUTEUR_ENTETE = 30;
	private static final int HAUTEUR_TABLEAU_MIN = 150;
	private static final int HAUTEUR_TABLEAU_MAX = 400;

	// Panneau principal
	private JPanel panneauContenu;

	// Composants d'affichage
	private JTable tableauFacture;
	private DefaultTableModel modeleTableau;
	private JLabel etiquetteTotalCommande;
	private JLabel etiquetteForfaitPort;
	private JLabel etiquettePrixTotal;

	// Boutons d'action
	private JButton boutonImprimer;
	private JButton boutonQuitter;

	// Objets métier
	private Client client;
	private Tomates baseTomates;
	private final DecimalFormat formatDecimal = new DecimalFormat("0.00");
	private final String cheminFichier = "src/main/resources/data/tomates.json";

	/**
	 * Crée la fenêtre d'affichage de la facture.
	 *
	 * @param parent      la fenêtre parente
	 * @param client      le client pour lequel la facture est affichée
	 * @param baseTomates la base de données des tomates
	 */
	public FenetreFacture(Window parent, Client client, Tomates baseTomates) {
		super(parent, "Ô'Tomates", ModalityType.APPLICATION_MODAL);

		// Configuration de base de la fenêtre
		this.setResizable(true);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setBounds(100, 100, 750, 600);

		// Configuration de l'icône
		ImageIcon iconeFenetre = new ImageIcon("src/main/resources/images/Icones/icone-tomates.png");
		Image imageFenetre = iconeFenetre.getImage();
		this.setIconImage(imageFenetre);

		// Initialisation des données
		this.client = client;
		this.baseTomates = baseTomates;

		// Construction de l'interface utilisateur
		this.initialiserComposants();
		this.configurerDisposition();

		// Attachement des contrôleurs d'événements
		this.construireControleurBoutonImprimer();
		this.construireControleurBoutonQuitter();
		this.construireControleurFermetureFenetre();

		// Finalisation de l'affichage
		this.remplirFacture();
		this.setLocationRelativeTo(parent);
	}

	/**
	 * Initialise tous les composants de l'interface utilisateur.
	 */
	private void initialiserComposants() {
		// Configuration du panneau principal
		this.panneauContenu = new JPanel();
		this.panneauContenu.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.panneauContenu.setBackground(Color.WHITE);
		this.setContentPane(this.panneauContenu);

		// Configuration de la table de facture
		String[] colonnes = { "Produit", "Prix unitaire", "Quantité", "Prix TTC" };
		this.modeleTableau = new DefaultTableModel(colonnes, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		this.tableauFacture = new JTable(this.modeleTableau);
		this.tableauFacture.setRowHeight(HAUTEUR_LIGNE);
		this.tableauFacture.setBackground(Color.WHITE);
		this.tableauFacture.setGridColor(new Color(200, 200, 200));
		this.tableauFacture.setShowGrid(true);
		this.tableauFacture.setIntercellSpacing(new Dimension(1, 1));
		this.tableauFacture.setFont(new Font("Dialog", Font.PLAIN, 12));
		this.tableauFacture.setFillsViewportHeight(true);

		// Configuration de l'en-tête du tableau
		JTableHeader enTete = this.tableauFacture.getTableHeader();
		enTete.setBackground(new Color(245, 245, 245));
		enTete.setForeground(Color.BLACK);
		enTete.setFont(new Font("Dialog", Font.BOLD, 12));
		enTete.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		enTete.setPreferredSize(new Dimension(enTete.getPreferredSize().width, HAUTEUR_ENTETE));

		// Configuration des largeurs de colonnes
		this.tableauFacture.getColumnModel().getColumn(0).setPreferredWidth(250);
		this.tableauFacture.getColumnModel().getColumn(1).setPreferredWidth(120);
		this.tableauFacture.getColumnModel().getColumn(2).setPreferredWidth(80);
		this.tableauFacture.getColumnModel().getColumn(3).setPreferredWidth(120);
		this.tableauFacture.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

		// Configuration des renderers de colonnes
		DefaultTableCellRenderer centreRenderer = new DefaultTableCellRenderer();
		centreRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		DefaultTableCellRenderer droiteRenderer = new DefaultTableCellRenderer();
		droiteRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		DefaultTableCellRenderer gaucheRenderer = new DefaultTableCellRenderer();
		gaucheRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		this.tableauFacture.getColumnModel().getColumn(0).setCellRenderer(gaucheRenderer);
		this.tableauFacture.getColumnModel().getColumn(1).setCellRenderer(droiteRenderer);
		this.tableauFacture.getColumnModel().getColumn(2).setCellRenderer(centreRenderer);
		this.tableauFacture.getColumnModel().getColumn(3).setCellRenderer(droiteRenderer);

		// Étiquettes des totaux
		this.etiquetteTotalCommande = new JLabel("TOTAL TTC COMMANDE : 0,00 €");
		this.etiquetteTotalCommande.setFont(new Font("Dialog", Font.PLAIN, 12));

		this.etiquetteForfaitPort = new JLabel("FORFAIT FRAIS DE PORT : 0,00 €");
		this.etiquetteForfaitPort.setFont(new Font("Dialog", Font.PLAIN, 12));

		this.etiquettePrixTotal = new JLabel("PRIX TOTAL TTC : 0,00 € payé par Paypal");
		this.etiquettePrixTotal.setFont(new Font("Dialog", Font.BOLD, 14));
		this.etiquettePrixTotal.setForeground(Color.BLACK);

		// Boutons d'action
		this.boutonImprimer = new JButton("Imprimer");
		this.boutonImprimer.setFont(new Font("Dialog", Font.BOLD, 12));
		this.boutonImprimer.setPreferredSize(new Dimension(100, 30));
		this.boutonImprimer.setBackground(new Color(144, 238, 144));
		this.boutonImprimer.setToolTipText("Imprimer la facture");

		this.boutonQuitter = new JButton("Quitter");
		this.boutonQuitter.setFont(new Font("Dialog", Font.BOLD, 12));
		this.boutonQuitter.setPreferredSize(new Dimension(100, 30));
		this.boutonQuitter.setBackground(new Color(255, 192, 203));
		this.boutonQuitter.setToolTipText("Fermer la facture");
	}

	/**
	 * Configure la disposition de tous les composants dans la fenêtre.
	 */
	private void configurerDisposition() {
		this.panneauContenu.setLayout(new BorderLayout(5, 5));

		JPanel panneauPrincipal = new JPanel();
		panneauPrincipal.setLayout(new BoxLayout(panneauPrincipal, BoxLayout.Y_AXIS));
		panneauPrincipal.setBackground(Color.WHITE);

		// Panneau en-tête avec titre et icône
		JPanel panneauEnTete = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		panneauEnTete.setBackground(Color.WHITE);

		JLabel etiquetteTitre = new JLabel(" Votre facture ");
		etiquetteTitre.setFont(new Font("Dialog", Font.BOLD, 18));
		etiquetteTitre.setForeground(new Color(0, 128, 0));

		panneauEnTete.add(etiquetteTitre);
		panneauEnTete.setAlignmentX(Component.CENTER_ALIGNMENT);
		panneauEnTete.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

		// Panneau de remerciement
		JPanel panneauMerci = new JPanel();
		panneauMerci.setLayout(new BoxLayout(panneauMerci, BoxLayout.X_AXIS));
		panneauMerci.setBackground(Color.WHITE);
		panneauMerci.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		JLabel etiquetteMerci = new JLabel("Merci de votre visite !");
		etiquetteMerci.setFont(new Font("Droid Serif", Font.BOLD, 12));
		etiquetteMerci.setForeground(new Color(0, 128, 0));
		etiquetteMerci
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0, 128, 0), 2),
						BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		etiquetteMerci.setOpaque(true);
		etiquetteMerci.setBackground(Color.WHITE);
		etiquetteMerci.setHorizontalAlignment(SwingConstants.LEFT);
		etiquetteMerci.setAlignmentX(Component.LEFT_ALIGNMENT);
		etiquetteMerci.setMaximumSize(new Dimension(Integer.MAX_VALUE, etiquetteMerci.getPreferredSize().height));

		panneauMerci.add(etiquetteMerci);
		panneauMerci.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Panneau informations client
		JPanel panneauClient = new JPanel();
		panneauClient.setLayout(new BoxLayout(panneauClient, BoxLayout.Y_AXIS));
		panneauClient.setBackground(Color.WHITE);
		panneauClient.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel etiquetteSlogan = new JLabel("Ô'Tomates, redécouvrez le goût des tomates anciennes !");
		etiquetteSlogan.setFont(new Font("Dialog", Font.BOLD, 15));
		etiquetteSlogan.setForeground(new Color(0, 128, 0));
		etiquetteSlogan.setBorder(BorderFactory.createEmptyBorder(5, 0, 8, 0));
		etiquetteSlogan.setAlignmentX(Component.LEFT_ALIGNMENT);

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMM HH:mm:ss 'heure d''été d''Europe centrale'",
				Locale.FRENCH);
		String dateCommande = "Commande du " + dateFormat.format(new Date());
		JLabel etiquetteDateCommande = new JLabel(dateCommande);
		etiquetteDateCommande.setFont(new Font("Dialog", Font.ITALIC, 12));
		etiquetteDateCommande.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		etiquetteDateCommande.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel panneauInfosClient = new JPanel();
		panneauInfosClient.setLayout(new BoxLayout(panneauInfosClient, BoxLayout.Y_AXIS));
		panneauInfosClient.setBackground(Color.WHITE);
		panneauInfosClient.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		panneauInfosClient.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel etiquetteNomClient = new JLabel();
		JLabel etiquetteAdresseClient = new JLabel();
		JLabel etiquetteTelephoneClient = new JLabel();
		JLabel etiquetteMailClient = new JLabel();

		etiquetteNomClient.setFont(new Font("Dialog", Font.PLAIN, 12));
		etiquetteAdresseClient.setFont(new Font("Dialog", Font.PLAIN, 12));
		etiquetteTelephoneClient.setFont(new Font("Dialog", Font.PLAIN, 12));
		etiquetteMailClient.setFont(new Font("Dialog", Font.PLAIN, 12));

		etiquetteNomClient.setAlignmentX(Component.LEFT_ALIGNMENT);
		etiquetteAdresseClient.setAlignmentX(Component.LEFT_ALIGNMENT);
		etiquetteTelephoneClient.setAlignmentX(Component.LEFT_ALIGNMENT);
		etiquetteMailClient.setAlignmentX(Component.LEFT_ALIGNMENT);

		etiquetteNomClient.setText(this.client.getPrenom() + " " + this.client.getNom().toUpperCase());

		String adresse = "Adresse : " + this.client.getAdresse() + ", " + this.client.getCodePostal() + " "
				+ this.client.getVille();
		etiquetteAdresseClient.setText(adresse);

		etiquetteTelephoneClient.setText("Téléphone : " + this.client.getNumeroTel());
		etiquetteMailClient.setText("Email : " + this.client.getMail());

		panneauInfosClient.add(etiquetteNomClient);
		panneauInfosClient.add(etiquetteAdresseClient);
		panneauInfosClient.add(etiquetteTelephoneClient);
		panneauInfosClient.add(etiquetteMailClient);

		panneauClient.add(etiquetteSlogan);
		panneauClient.add(etiquetteDateCommande);
		panneauClient.add(panneauInfosClient);

		// Configuration du tableau avec scroll
		JScrollPane scrollPaneTableau = new JScrollPane(this.tableauFacture);
		scrollPaneTableau.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		scrollPaneTableau.setBackground(Color.WHITE);
		scrollPaneTableau.getViewport().setBackground(Color.WHITE);
		scrollPaneTableau.setAlignmentX(Component.LEFT_ALIGNMENT);

		int nombreProduits = this.client.getPanier().getArticles().size();
		int hauteurTableau = Math.max(HAUTEUR_TABLEAU_MIN,
				Math.min(300, (nombreProduits + 1) * HAUTEUR_LIGNE + HAUTEUR_ENTETE));
		scrollPaneTableau.setPreferredSize(new Dimension(700, hauteurTableau));
		scrollPaneTableau.setMaximumSize(new Dimension(Integer.MAX_VALUE, hauteurTableau));

		// Panneau des totaux
		JPanel panneauTotal = new JPanel(new BorderLayout());
		panneauTotal.setBackground(Color.WHITE);
		panneauTotal.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		panneauTotal.setAlignmentX(Component.LEFT_ALIGNMENT);
		panneauTotal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

		JPanel conteneurEtiquettes = new JPanel();
		conteneurEtiquettes.setLayout(new BoxLayout(conteneurEtiquettes, BoxLayout.Y_AXIS));
		conteneurEtiquettes.setBackground(Color.WHITE);

		this.etiquetteTotalCommande.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.etiquetteForfaitPort.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.etiquettePrixTotal.setAlignmentX(Component.LEFT_ALIGNMENT);

		conteneurEtiquettes.add(this.etiquetteTotalCommande);
		conteneurEtiquettes.add(Box.createRigidArea(new Dimension(0, 3)));
		conteneurEtiquettes.add(this.etiquetteForfaitPort);
		conteneurEtiquettes.add(Box.createRigidArea(new Dimension(0, 3)));
		conteneurEtiquettes.add(this.etiquettePrixTotal);

		panneauTotal.add(conteneurEtiquettes, BorderLayout.WEST);

		// Assemblage du contenu scrollable
		JPanel panneauContenuScrollable = new JPanel();
		panneauContenuScrollable.setLayout(new BoxLayout(panneauContenuScrollable, BoxLayout.Y_AXIS));
		panneauContenuScrollable.setBackground(Color.WHITE);
		panneauContenuScrollable.setAlignmentX(Component.LEFT_ALIGNMENT);

		panneauContenuScrollable.add(panneauMerci);
		panneauContenuScrollable.add(Box.createRigidArea(new Dimension(0, 10)));
		panneauContenuScrollable.add(panneauClient);
		panneauContenuScrollable.add(Box.createRigidArea(new Dimension(0, 10)));
		panneauContenuScrollable.add(scrollPaneTableau);
		panneauContenuScrollable.add(Box.createRigidArea(new Dimension(0, 15)));
		panneauContenuScrollable.add(panneauTotal);

		JScrollPane scrollPanePrincipal = new JScrollPane(panneauContenuScrollable);
		scrollPanePrincipal.setBorder(null);
		scrollPanePrincipal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPanePrincipal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanePrincipal.getViewport().setBackground(Color.WHITE);

		// Panneau des boutons
		JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		panneauBoutons.setBackground(Color.WHITE);
		panneauBoutons.add(this.boutonImprimer);
		panneauBoutons.add(this.boutonQuitter);
		panneauBoutons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

		// Assemblage final
		panneauPrincipal.add(panneauEnTete);
		panneauPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
		panneauPrincipal.add(scrollPanePrincipal);
		panneauPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
		panneauPrincipal.add(panneauBoutons);

		this.panneauContenu.add(panneauPrincipal, BorderLayout.CENTER);
	}

	/**
	 * Configure le contrôleur du bouton Imprimer.
	 */
	private void construireControleurBoutonImprimer() {
		this.boutonImprimer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Locale.setDefault(Locale.FRENCH);
				PrinterJob job = PrinterJob.getPrinterJob();
				job.setJobName("Facture Ô'Tomates");
				job.setPrintable(new Printable() {
					@Override
					public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
						if (page > 0) {
							return NO_SUCH_PAGE;
						}

						Graphics2D g2 = (Graphics2D) g;
						g2.translate(pf.getImageableX(), pf.getImageableY());

						// Masquer temporairement les boutons pour l'impression
						FenetreFacture.this.boutonImprimer.setVisible(false);
						FenetreFacture.this.boutonQuitter.setVisible(false);

						// Mise à l'échelle pour adapter le contenu à la page
						double scale = Math.min(pf.getImageableWidth() / FenetreFacture.this.panneauContenu.getWidth(),
								pf.getImageableHeight() / FenetreFacture.this.panneauContenu.getHeight());

						g2.scale(scale, scale);
						FenetreFacture.this.panneauContenu.printAll(g2);

						// Restaurer l'affichage des boutons
						FenetreFacture.this.boutonImprimer.setVisible(true);
						FenetreFacture.this.boutonQuitter.setVisible(true);

						return PAGE_EXISTS;
					}
				});

				if (job.printDialog()) {
					try {
						job.print();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(FenetreFacture.this,
								"Erreur lors de l'impression : " + ex.getMessage(), "Erreur",
								JOptionPane.ERROR_MESSAGE);
					}
				}

				FenetreFacture.this.sauvegarderEtQuitter();
			}
		});
	}

	/**
	 * Configure le contrôleur du bouton Quitter.
	 */
	private void construireControleurBoutonQuitter() {
		this.boutonQuitter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FenetreFacture.this.sauvegarderEtQuitter();
			}
		});
	}

	/**
	 * Configure le contrôleur de fermeture de la fenêtre.
	 */
	private void construireControleurFermetureFenetre() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				FenetreFacture.this.sauvegarderEtQuitter();
			}
		});
	}

	/**
	 * Remplit le tableau avec les données de la facture et met à jour les totaux.
	 */
	private void remplirFacture() {
		this.modeleTableau.setRowCount(0);

		int nombreProduits = this.client.getPanier().getArticles().size();
		float sousTotal = 0.0f;

		for (Article article : this.client.getPanier().getArticles()) {
			Object[] ligne = new Object[4];
			ligne[0] = article.getTomate().getDésignation();
			ligne[1] = this.formatDecimal.format(article.getTomate().getPrixTTC()).replace(".", ",") + " €";
			ligne[2] = String.valueOf(article.getQuantite());
			ligne[3] = this.formatDecimal.format(article.getTotal()).replace(".", ",") + " €";

			sousTotal += article.getTotal();
			this.modeleTableau.addRow(ligne);
		}

		this.etiquetteTotalCommande
				.setText("TOTAL TTC COMMANDE : " + this.formatDecimal.format(sousTotal).replace(".", ",") + " €");

		float forfaitPort = this.client.getPanier().getForfait();
		this.etiquetteForfaitPort
				.setText("FORFAIT FRAIS DE PORT : " + this.formatDecimal.format(forfaitPort).replace(".", ",") + " €");

		float total = sousTotal + forfaitPort;
		String moyenPaiement = this.client.getMoyenPaiement();
		this.etiquettePrixTotal.setText("PRIX TOTAL TTC : " + this.formatDecimal.format(total).replace(".", ",")
				+ " € payé par " + moyenPaiement);

		this.ajusterHauteurTableau(nombreProduits);
	}

	/**
	 * Ajuste la hauteur du tableau en fonction du nombre de produits.
	 */
	private void ajusterHauteurTableau(int nombreProduits) {
		int hauteurCalculee = (nombreProduits * HAUTEUR_LIGNE) + HAUTEUR_ENTETE + 20;
		int hauteurFinale = Math.max(HAUTEUR_TABLEAU_MIN, Math.min(HAUTEUR_TABLEAU_MAX, hauteurCalculee));

		Component parent = this.tableauFacture.getParent().getParent();
		if (parent instanceof JScrollPane) {
			JScrollPane scrollPane = (JScrollPane) parent;
			Dimension taille = new Dimension(scrollPane.getPreferredSize().width, hauteurFinale);
			scrollPane.setPreferredSize(taille);
			scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, hauteurFinale));
			scrollPane.revalidate();
		}
	}

	/**
	 * Sauvegarde les données et ferme l'application.
	 */
	private void sauvegarderEtQuitter() {
		OutilsBaseDonneesTomates.sauvegarderBaseDeTomates(this.baseTomates, this.cheminFichier);
		System.exit(0);
	}
}