package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import modèle.Article;
import modèle.Panier;
import modèle.Tomates;

/**
 * Fenêtre de gestion du panier d'achat, permettant de consulter, modifier et
 * valider les articles sélectionnés.
 */
public class FenetrePanier extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPane = new JPanel();

	// Composants d'affichage
	private JTable tableProduits;
	private DefaultTableModel modeleTable;
	private JLabel etiquetteSousTotal;
	private JLabel etiquetteForfait;
	private JLabel etiquetteTotal;

	// Boutons d'action
	private JButton boutonRecalculer;
	private JButton boutonValiderPanier;
	private JButton boutonViderPanier;
	private JButton boutonContinuerAchats;

	// Objets métier
	private Panier panier;
	private Tomates baseTomates;
	private DecimalFormat formatDec = new DecimalFormat("0.00");

	/**
	 * Crée la fenêtre de gestion du panier.
	 *
	 * @param parent      la fenêtre parente
	 * @param panier      le panier à afficher
	 * @param baseTomates la base de données des tomates
	 */
	public FenetrePanier(Window parent, Panier panier, Tomates baseTomates) {
		super(parent, "Ô'Tomates", ModalityType.APPLICATION_MODAL);

		// Configuration de base de la fenêtre
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(650, 450));
		this.setMinimumSize(new Dimension(650, 450));

		// Configuration de l'icône
		ImageIcon iconeFenetre = new ImageIcon("src/main/resources/images/Icones/icone-tomates.png");
		this.setIconImage(iconeFenetre.getImage());

		// Initialisation des données
		this.panier = panier;
		this.baseTomates = baseTomates;

		// Construction de l'interface utilisateur
		this.initialiserComposants();
		this.configurerDisposition();

		// Attachement des contrôleurs d'événements
		this.construireControleurBoutonRecalculer();
		this.construireControleurBoutonValiderPanier();
		this.construireControleurBoutonViderPanier();
		this.construireControleurBoutonContinuerAchats();

		// Finalisation de l'affichage
		this.rafraichirAffichage();
		this.pack();
		this.setLocationRelativeTo(parent);
	}

	/**
	 * Initialise tous les composants de l'interface utilisateur.
	 */
	private void initialiserComposants() {
		// Configuration du panneau principal
		this.contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		this.contentPane.setBackground(new Color(240, 240, 240));
		this.setContentPane(this.contentPane);

		// Configuration de la table des produits
		this.initialiserTable();

		// Étiquettes des totaux
		this.etiquetteSousTotal = new JLabel("0,00 €");
		this.etiquetteSousTotal.setFont(new Font("Dialog", Font.PLAIN, 12));
		this.etiquetteSousTotal.setHorizontalAlignment(JLabel.CENTER);
		this.etiquetteSousTotal.setOpaque(true);
		this.etiquetteSousTotal.setBackground(new Color(255, 255, 200));
		this.etiquetteSousTotal.setBorder(new LineBorder(Color.GRAY));

		this.etiquetteForfait = new JLabel("5,50 €");
		this.etiquetteForfait.setFont(new Font("Dialog", Font.PLAIN, 12));
		this.etiquetteForfait.setHorizontalAlignment(JLabel.CENTER);
		this.etiquetteForfait.setOpaque(true);
		this.etiquetteForfait.setBackground(new Color(255, 255, 200));
		this.etiquetteForfait.setBorder(new LineBorder(Color.GRAY));

		this.etiquetteTotal = new JLabel("0,00 €");
		this.etiquetteTotal.setFont(new Font("Dialog", Font.BOLD, 12));
		this.etiquetteTotal.setHorizontalAlignment(JLabel.CENTER);
		this.etiquetteTotal.setOpaque(true);
		this.etiquetteTotal.setBackground(new Color(200, 255, 200));
		this.etiquetteTotal.setBorder(new LineBorder(Color.GRAY));

		// Boutons d'action
		this.boutonRecalculer = new JButton("Recalculer le panier");
		this.boutonRecalculer.setFont(new Font("Dialog", Font.PLAIN, 12));
		this.boutonRecalculer.setBackground(new Color(240, 240, 240));

		this.boutonValiderPanier = new JButton("Valider le panier");
		this.boutonValiderPanier.setFont(new Font("Dialog", Font.PLAIN, 12));
		this.boutonValiderPanier.setBackground(new Color(240, 240, 240));

		this.boutonViderPanier = new JButton("Vider le panier");
		this.boutonViderPanier.setFont(new Font("Dialog", Font.PLAIN, 12));
		this.boutonViderPanier.setBackground(new Color(240, 240, 240));

		this.boutonContinuerAchats = new JButton("Continuer les achats");
		this.boutonContinuerAchats.setFont(new Font("Dialog", Font.PLAIN, 12));
		this.boutonContinuerAchats.setBackground(new Color(240, 240, 240));
	}

	/**
	 * Configure la disposition de tous les composants dans la fenêtre.
	 */
	private void configurerDisposition() {
		this.contentPane.setLayout(new BorderLayout(10, 15));

		// Panneau titre
		JPanel panneauTitre = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panneauTitre.setBackground(new Color(240, 240, 240));

		JLabel etiquetteIcone = new JLabel("");
		etiquetteIcone.setIcon(new ImageIcon("src/main/resources/images/Icones/icone-panier-osier.png"));
		panneauTitre.add(etiquetteIcone);

		JLabel etiquetteTitre = new JLabel(" Votre panier");
		etiquetteTitre.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 24));
		etiquetteTitre.setForeground(new Color(0, 150, 0));
		panneauTitre.add(etiquetteTitre);

		this.contentPane.add(panneauTitre, BorderLayout.NORTH);

		// Table au centre
		JScrollPane defileur = new JScrollPane(this.tableProduits);
		defileur.setBorder(new LineBorder(Color.GRAY));
		this.contentPane.add(defileur, BorderLayout.CENTER);

		// Panneau du bas
		JPanel panneauBas = new JPanel(new BorderLayout(10, 10));
		panneauBas.setBackground(new Color(240, 240, 240));

		// Bouton recalculer à gauche
		JPanel panneauRecalculer = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panneauRecalculer.setBackground(new Color(240, 240, 240));
		panneauRecalculer.add(this.boutonRecalculer);

		// Panneau totaux à droite
		JPanel panneauTotaux = new JPanel(new GridBagLayout());
		panneauTotaux.setBackground(new Color(240, 240, 240));

		this.ajouterLigneTotaux(panneauTotaux, "Sous-Total :", this.etiquetteSousTotal, 0, Font.PLAIN);
		this.ajouterLigneTotaux(panneauTotaux, "Expédition (forfait) :", this.etiquetteForfait, 1, Font.PLAIN);
		this.ajouterLigneTotaux(panneauTotaux, "TOTAL :", this.etiquetteTotal, 2, Font.BOLD);

		JPanel panneauMilieu = new JPanel(new BorderLayout());
		panneauMilieu.setBackground(new Color(240, 240, 240));
		panneauMilieu.add(panneauRecalculer, BorderLayout.WEST);
		panneauMilieu.add(panneauTotaux, BorderLayout.EAST);
		panneauBas.add(panneauMilieu, BorderLayout.NORTH);

		// Panneau boutons
		JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		panneauBoutons.setBackground(new Color(240, 240, 240));
		panneauBoutons.add(this.boutonValiderPanier);
		panneauBoutons.add(this.boutonViderPanier);
		panneauBoutons.add(this.boutonContinuerAchats);

		panneauBas.add(panneauBoutons, BorderLayout.SOUTH);
		this.contentPane.add(panneauBas, BorderLayout.SOUTH);
	}

	/**
	 * Configure le contrôleur du bouton Recalculer.
	 */
	private void construireControleurBoutonRecalculer() {
		this.boutonRecalculer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (FenetrePanier.this.tableProduits.isEditing()) {
					FenetrePanier.this.tableProduits.getCellEditor().stopCellEditing();
				}

				boolean modificationEffectuee = false;
				ArrayList<Article> produits = FenetrePanier.this.panier.getArticles();
				List<Article> articlesASupprimer = new ArrayList<>();

				// Première passe : mise à jour des quantités et collecte des articles à
				// supprimer
				for (int i = 0; i < FenetrePanier.this.modeleTable.getRowCount() && i < produits.size(); i++) {
					int nouvelleQuantite = Integer.parseInt(FenetrePanier.this.modeleTable.getValueAt(i, 3).toString());
					Article produit = produits.get(i);
					int ancienneQuantite = produit.getQuantite();

					if (nouvelleQuantite != ancienneQuantite) {
						int difference = nouvelleQuantite - ancienneQuantite;
						if (produit.getTomate() != null) {
							produit.getTomate().setStock(produit.getTomate().getStock() - difference);
						}

						if (nouvelleQuantite == 0) {
							articlesASupprimer.add(produit);
						} else {
							produit.setQuantite(nouvelleQuantite);
						}
						modificationEffectuee = true;
					}
				}

				// Deuxième passe : suppression des articles avec quantité 0
				for (Article article : articlesASupprimer) {
					FenetrePanier.this.panier.retirerArticle(article);
				}

				if (modificationEffectuee) {
					FenetrePanier.this.rafraichirAffichage();
					JOptionPane.showMessageDialog(FenetrePanier.this, "Le panier a été recalculé avec succès !",
							"Recalcul effectué", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(FenetrePanier.this, "Aucune modification détectée.", "Information",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
	}

	/**
	 * Configure le contrôleur du bouton Valider le panier.
	 */
	private void construireControleurBoutonValiderPanier() {
		this.boutonValiderPanier.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (FenetrePanier.this.panier.estVide()) {
					JOptionPane.showMessageDialog(FenetrePanier.this, "Votre panier est vide.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				} else {
					FenetreCoordonnees fenetreCoordonnees = new FenetreCoordonnees(FenetrePanier.this,
							FenetrePanier.this.panier, FenetrePanier.this.baseTomates);
					fenetreCoordonnees.setVisible(true);
				}
			}
		});
	}

	/**
	 * Configure le contrôleur du bouton Vider le panier.
	 */
	private void construireControleurBoutonViderPanier() {
		this.boutonViderPanier.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] options = { "Oui", "Non" };
				int reponse = JOptionPane.showOptionDialog(FenetrePanier.this, "Voulez-vous vraiment vider le panier ?",
						"Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
						options[1]);

				if (reponse == JOptionPane.YES_OPTION) {
					FenetrePanier.this.panier.viderPanier();
					FenetrePanier.this.rafraichirAffichage();
					JOptionPane.showMessageDialog(FenetrePanier.this, "Le panier a été vidé.", "Information",
							JOptionPane.INFORMATION_MESSAGE);
					FenetrePanier.this.dispose();
				}
			}
		});
	}

	/**
	 * Configure le contrôleur du bouton Continuer les achats.
	 */
	private void construireControleurBoutonContinuerAchats() {
		this.boutonContinuerAchats.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FenetrePanier.this.dispose();
			}
		});
	}

	/**
	 * Met à jour l'affichage de la table et des totaux.
	 */
	public void rafraichirAffichage() {
		this.modeleTable.setRowCount(0);

		ArrayList<Article> produits = this.panier.getArticles();
		for (Article produit : produits) {
			Object[] ligne = new Object[5];
			ligne[0] = produit.getTomate().getNomImage();
			ligne[1] = produit.getTomate().getDésignation();
			ligne[2] = this.formatDec.format(produit.getTomate().getPrixTTC()) + " €";
			ligne[3] = produit.getQuantite();
			ligne[4] = this.formatDec.format(produit.getTotal()) + " €";
			this.modeleTable.addRow(ligne);
		}

		float sousTotal = this.panier.getSousTotal();
		float total = this.panier.getTotal();
		float forfaitExpedition = this.panier.getForfait();

		this.etiquetteSousTotal.setText(this.formatDec.format(sousTotal) + " €");
		this.etiquetteTotal.setText(this.formatDec.format(total) + " €");
		this.etiquetteForfait.setText(sousTotal == 0 ? "0,00 €" : this.formatDec.format(forfaitExpedition) + " €");
	}

	/**
	 * Initialise la table des produits avec ses colonnes et renderers.
	 */
	private void initialiserTable() {
		String[] colonnes = { "", "produit", "prix", "quantité", "total" };
		this.modeleTable = new DefaultTableModel(colonnes, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int ligne, int colonne) {
				return colonne == 3;
			}
		};

		this.tableProduits = new JTable(this.modeleTable);
		this.tableProduits.setRowHeight(50);
		this.tableProduits.setBackground(Color.WHITE);
		this.tableProduits.setGridColor(Color.GRAY);
		this.tableProduits.setShowGrid(true);

		// Configuration des largeurs de colonnes
		this.tableProduits.getColumnModel().getColumn(0).setPreferredWidth(80);
		this.tableProduits.getColumnModel().getColumn(1).setPreferredWidth(250);
		this.tableProduits.getColumnModel().getColumn(2).setPreferredWidth(80);
		this.tableProduits.getColumnModel().getColumn(3).setPreferredWidth(80);
		this.tableProduits.getColumnModel().getColumn(4).setPreferredWidth(80);

		// Configuration des renderers
		this.tableProduits.getColumnModel().getColumn(0).setCellRenderer(new RenduImage());

		DefaultTableCellRenderer renduCentre = new DefaultTableCellRenderer();
		renduCentre.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		this.tableProduits.getColumnModel().getColumn(2).setCellRenderer(renduCentre);
		this.tableProduits.getColumnModel().getColumn(3).setCellRenderer(renduCentre);
		this.tableProduits.getColumnModel().getColumn(4).setCellRenderer(renduCentre);

		this.tableProduits.getColumnModel().getColumn(3).setCellEditor(new EditeurSpinner());
	}

	/**
	 * Ajoute une ligne de totaux au panneau avec GridBagLayout.
	 */
	private void ajouterLigneTotaux(JPanel panneau, String texte, JLabel etiquette, int ligne, int styleFonte) {
		GridBagConstraints gbcLabel = new GridBagConstraints();
		gbcLabel.insets = new Insets(2, 5, 2, 5);
		gbcLabel.gridx = 0;
		gbcLabel.gridy = ligne;
		gbcLabel.anchor = GridBagConstraints.EAST;

		JLabel label = new JLabel(texte);
		label.setFont(new Font("Dialog", styleFonte, 12));
		if (styleFonte == Font.BOLD) {
			label.setForeground(new Color(0, 150, 0));
		}
		panneau.add(label, gbcLabel);

		GridBagConstraints gbcEtiquette = new GridBagConstraints();
		gbcEtiquette.insets = new Insets(2, 5, 2, 5);
		gbcEtiquette.gridx = 1;
		gbcEtiquette.gridy = ligne;
		gbcEtiquette.fill = GridBagConstraints.HORIZONTAL;
		gbcEtiquette.weightx = 1.0;
		panneau.add(etiquette, gbcEtiquette);
	}

	/**
	 * Renderer pour afficher les images des tomates dans la table.
	 */
	private class RenduImage extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int ligne, int colonne) {
			if (value != null && value instanceof String) {
				String nomImage = (String) value;
				String cheminImage = "src/main/resources/images/Tomates40x40/" + nomImage + ".jpg";
				ImageIcon icone = new ImageIcon(cheminImage);
				this.setIcon(icone);
				this.setText("");
			} else {
				this.setIcon(null);
				this.setText("");
			}
			this.setHorizontalAlignment(CENTER);
			if (isSelected) {
				this.setBackground(table.getSelectionBackground());
				this.setForeground(table.getSelectionForeground());
			} else {
				this.setBackground(table.getBackground());
				this.setForeground(table.getForeground());
			}
			return this;
		}
	}

	/**
	 * Éditeur personnalisé pour les spinners de quantité dans la table.
	 */
	private class EditeurSpinner extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = 1L;
		private JSpinner spinner;

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int ligne,
				int colonne) {
			Article produit = FenetrePanier.this.panier.getArticles().get(ligne);
			int quantiteDansPanier = produit.getQuantite();
			int stockDisponible = produit.getTomate().getStock();
			int max = quantiteDansPanier + stockDisponible;
			this.spinner = new JSpinner(new SpinnerNumberModel(quantiteDansPanier, 0, max, 1));
			return this.spinner;
		}

		@Override
		public Object getCellEditorValue() {
			return this.spinner.getValue();
		}
	}
}