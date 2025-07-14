package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import modèle.Tomates;

/**
 * Fenêtre d'affichage des conseils de culture pour les tomates. Présente un
 * résumé des étapes de culture et des conseils détaillés.
 */
public class FenetreCulture extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane = new JPanel();

	// Boutons d'action
	private JButton boutonOk;

	/**
	 * Crée la fenêtre d'affichage des conseils de culture.
	 * 
	 * @param parent la fenêtre parente
	 */
	public FenetreCulture(Window parent) {
		super(parent, "Ô'Tomates", ModalityType.APPLICATION_MODAL);

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setBounds(100, 100, 600, 650);

		// Configuration de l'icône
		ImageIcon iconeFenetre = new ImageIcon("src/main/resources/images/Icones/icone-tomates.png");
		Image imageFenetre = iconeFenetre.getImage();
		this.setIconImage(imageFenetre);

		// Construction de l'interface utilisateur
		this.initialiserComposants();
		this.configurerDisposition();

		// Attachement des contrôleurs d'événements
		this.construireControleurBoutonOK();

		// Finalisation de l'affichage
		this.setLocationRelativeTo(parent);
	}

	/**
	 * Initialise tous les composants de l'interface utilisateur.
	 */
	private void initialiserComposants() {
		this.contentPane.setBackground(new Color(230, 230, 230));
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 10));
		this.setContentPane(this.contentPane);

		this.boutonOk = new JButton("OK");
		this.boutonOk.setFont(new Font("Dialog", Font.BOLD, 12));
		this.boutonOk.setPreferredSize(new Dimension(60, 30));
	}

	/**
	 * Configure la disposition de tous les composants dans la fenêtre.
	 */
	private void configurerDisposition() {
		// Titre principal
		JLabel etiquetteTitreConseils = new JLabel("Conseils de culture");
		etiquetteTitreConseils.setFont(new Font("Comic Sans MS", Font.ITALIC, 28));
		etiquetteTitreConseils.setForeground(new Color(0, 128, 0));
		etiquetteTitreConseils.setHorizontalAlignment(SwingConstants.CENTER);
		this.contentPane.add(etiquetteTitreConseils, BorderLayout.NORTH);

		// Panneau principal avec résumé et conseils détaillés
		JPanel panneauPrincipal = new JPanel();
		panneauPrincipal.setLayout(new BorderLayout());
		panneauPrincipal.setBackground(Color.WHITE);

		// Panneau résumé des étapes de culture
		JPanel panneauResume = new JPanel();
		panneauResume.setBorder(new LineBorder(new Color(0, 150, 0), 2));
		panneauResume.setLayout(new BoxLayout(panneauResume, BoxLayout.Y_AXIS));
		panneauResume.setBackground(Color.WHITE);
		panneauResume.setPreferredSize(new Dimension(580, 120));
		panneauResume.setMaximumSize(new Dimension(580, 120));
		panneauResume.setMinimumSize(new Dimension(580, 120));

		panneauResume.add(Box.createVerticalStrut(10));
		panneauResume.add(this.creerSousPanelLabel("Conseils de culture", 16));
		panneauResume.add(this.creerSousPanelLabel("Semis : mars-avril", 16));
		panneauResume.add(this.creerSousPanelLabel("Repiquage : après les gelées", 16));
		panneauResume.add(this.creerSousPanelLabel("Récolte : juillet à septembre, voire octobre", 16));

		panneauPrincipal.add(panneauResume, BorderLayout.NORTH);

		// Zone de texte avec conseils détaillés
		JTextArea zoneTexteConseils = new JTextArea();
		zoneTexteConseils.setText(Tomates.CONSEILS_DE_CULTURE);
		zoneTexteConseils.setCaretPosition(0);
		zoneTexteConseils.setLineWrap(true);
		zoneTexteConseils.setWrapStyleWord(true);
		zoneTexteConseils.setEditable(false);
		zoneTexteConseils.setFont(new Font("Segoe UI", Font.BOLD, 13));
		zoneTexteConseils.setForeground(Color.BLACK);
		zoneTexteConseils.setBackground(Color.WHITE);
		zoneTexteConseils.setMargin(new Insets(10, 10, 10, 10));

		JScrollPane defileurConseils = new JScrollPane(zoneTexteConseils);
		defileurConseils.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		defileurConseils.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		defileurConseils.setBorder(new LineBorder(Color.GRAY, 1));
		panneauPrincipal.add(defileurConseils, BorderLayout.CENTER);

		this.contentPane.add(panneauPrincipal, BorderLayout.CENTER);

		// Panneau des boutons d'action
		JPanel panneauBas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		panneauBas.setBackground(new Color(230, 230, 230));
		panneauBas.add(this.boutonOk);
		this.contentPane.add(panneauBas, BorderLayout.SOUTH);
	}

	/**
	 * Configure le contrôleur du bouton OK.
	 */
	private void construireControleurBoutonOK() {
		this.boutonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FenetreCulture.this.dispose();
			}
		});
	}

	/**
	 * Crée un panneau contenant une étiquette stylisée pour le résumé.
	 * 
	 * @param texte        le texte à afficher
	 * @param taillePolice la taille de la police
	 * @return un panneau contenant l'étiquette formatée
	 */
	private JPanel creerSousPanelLabel(String texte, int taillePolice) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		panel.setBackground(Color.WHITE);
		JLabel label = new JLabel(texte);
		label.setFont(new Font("Comic Sans MS", Font.ITALIC, taillePolice));
		label.setForeground(new Color(0, 128, 0));
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(label);
		return panel;
	}
}