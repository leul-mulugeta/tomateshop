package modèle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * La classe OutilsBaseTomates fournit des méthodes utilitaires pour la gestion
 * d'une base de données de tomates à partir de fichiers JSON.
 */
public class OutilsBaseDonneesTomates {

    /**
     * Point d'entrée principal pour exécuter les outils de base de données de
     * tomates.
     *
     * @param args les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        String cheminFichier = "src/main/resources/data/tomates.json";
        Tomates baseTomates = générationBaseDeTomates(cheminFichier);
        System.out.println("base créée");
        System.out.println(baseTomates);
    }

    /**
     * Génère une base de données de tomates à partir d'un fichier JSON.
     *
     * @param cheminFichier le chemin du fichier JSON contenant les données des
     *                      tomates
     * @return une instance de Tomates représentant la base de données de
     *         tomates
     */
    public static Tomates générationBaseDeTomates(String cheminFichier) {
        List<Tomate> tomates = lectureTomatesDepuisJson(cheminFichier);
        ajoutAléatoireTomatesApparentées(tomates);
        Tomates base = new Tomates();
        base.addTomates(tomates);
        return base;
    }

    /**
     * Sauvegarde la base de données de tomates dans un fichier JSON.
     *
     * @param base          la base de données de tomates à sauvegarder
     * @param cheminFichier le chemin du fichier JSON où sauvegarder les données
     */
    public static void sauvegarderBaseDeTomates(Tomates base,
            String cheminFichier) {
        List<Tomate> tomates = base.getTomates();
        écritureTomatesVersJson(tomates, cheminFichier);
    }

    /**
     * Ajoute aléatoirement des tomates apparentées à chaque tomate de la liste.
     *
     * @param tomates la liste des tomates à laquelle ajouter des tomates
     *                apparentées
     */
    private static void ajoutAléatoireTomatesApparentées(List<Tomate> tomates) {
        for (Tomate t : tomates) {
            while (t.getTomatesApparentées().size() < 4) {
                int random = (int) (tomates.size() * Math.random());
                t.addTomateApparentée(tomates.get(random));
            }
        }
    }

    /**
     * Lit les données des tomates à partir d'un fichier JSON et les ajoute à
     * une liste.
     *
     * @param cheminFichier le chemin du fichier JSON contenant les données des
     *                      tomates
     * @return une liste de tomates lues à partir du fichier JSON
     */
    private static List<Tomate> lectureTomatesDepuisJson(String cheminFichier) {
        List<Tomate> tomates = new ArrayList<>();
        try {
            String content = new String(
                    Files.readAllBytes(Paths.get(cheminFichier)));
            System.out.println("Contenu JSON lu :");
            System.out.println(content); // 👈 ajoute ceci

            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TypeTomate type = TypeTomate
                        .getTypeTomate(jsonObject.getString("type"));
                Couleur couleur = Couleur
                        .getCouleur(jsonObject.getString("couleur"));
                String désignation = jsonObject.getString("désignation");
                String sousTitre = jsonObject.getString("sousTitre");
                String nomImage = jsonObject.getString("nomImage");
                String description = jsonObject.getString("description");
                int stock = jsonObject.getInt("stock");
                int nbGrainesParSachet = jsonObject
                        .getInt("nbGrainesParSachet");
                float prixTTC = (float) jsonObject.getDouble("prixTTC");

                Tomate tomate = new Tomate(type, couleur, désignation,
                        sousTitre, nomImage, description, stock,
                        nbGrainesParSachet, prixTTC);
                tomates.add(tomate);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("fichier non trouvé");
            System.exit(0);
        }
        return tomates;
    }

    /**
     * Écrit les données des tomates dans un fichier JSON.
     *
     * @param tomates       la liste des tomates à écrire dans le fichier JSON
     * @param cheminFichier le chemin du fichier JSON où écrire les données
     */
    private static void écritureTomatesVersJson(List<Tomate> tomates,
            String cheminFichier) {
        JSONArray jsonArray = new JSONArray();

        for (Tomate tomate : tomates) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", tomate.getType().getDénomination());
            jsonObject.put("couleur", tomate.getCouleur().getDénomination());
            jsonObject.put("désignation", tomate.getDésignation());
            jsonObject.put("sousTitre", tomate.getSousTitre());
            jsonObject.put("nomImage", tomate.getNomImage());
            jsonObject.put("description", tomate.getDescription());
            jsonObject.put("stock", tomate.getStock());
            jsonObject.put("nbGrainesParSachet",
                    tomate.getNbGrainesParSachet());
            jsonObject.put("prixTTC", tomate.getPrixTTC());
            jsonArray.put(jsonObject);
        }

        try {
            Files.write(Paths.get(cheminFichier),
                    jsonArray.toString(4).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
