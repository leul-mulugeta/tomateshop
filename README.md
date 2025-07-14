# 🍅 TomateShop

## 🛠 Présentation

**TomateShop** est une application Java de type e-commerce qui permet de **visualiser, filtrer, commander et facturer** des sachets de graines de tomates, à travers une **interface graphique développée avec WindowBuilder (Swing)**.

L’application suit une architecture claire :

* `modèle` : logique métier (articles, panier, clients, stock…)
* `ihm` : interface graphique
* `resources` : images et données JSON (base de tomates)

## 🎯 Fonctionnalités

### 🧺 Visualisation & sélection

* Liste de toutes les variétés de tomates disponibles
* **Filtrage par type** (ex. : cocktail, cerise, ancienne…) et **par couleur**
* Affichage d’une tomate sélectionnée (image, description, prix, stock)
* Suggestions automatiques si le produit est en rupture

### 🛒 Panier

* Création d’un panier à l’ouverture
* Ajout, modification automatique des quantités, total dynamique
* Réinitialisation du panier

### 🧾 Facture

* Formulaire client : prénom, nom, adresse, ville, code postal, téléphone, email
* Saisie du mode de paiement
* **Génération d’une facture visuelle** en fin de commande

### 📦 Stock dynamique

* Le fichier `tomates.json` est copié la première fois à l’exécution
* Toute commande modifie dynamiquement le stock local
* Un fichier de sauvegarde permet de **restaurer l’état initial**

## 🧪 Tests unitaires

* Vérification du panier (ajout, suppression, calcul total)
* Vérification des stocks
* Tests réalisés avec **JUnit 4**

## 📁 Structure du projet

```
tomateshop/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── ihm/
│   │   │   └── modèle/
│   │   └── resources/
│   │       ├── data/
│   │       │   └── tomates.json
│   │       └── images/
│   │           └── ...
│   └── test/
│       └── java/
│           └── modèle/
├── pom.xml
```

## ▶️ Exécution du `.jar`

Un `.jar` exécutable avec toutes les dépendances est disponible dans l'onglet [**Releases**](https://github.com/leul-mulugeta/tomateshop/releases).

### Lancer l'application :

```bash
java -jar tomateshop.jar
```

### Réinitialisation du stock :

Supprimer le fichier local `data/tomates.json`. Il sera régénéré au prochain lancement.

---

## 💡 Installation du projet source

1. Cloner le dépôt :

```bash
git clone https://github.com/leul-mulugeta/tomateshop.git
```

2. Ouvrir sous **Eclipse** :

* `File → Import → Maven → Existing Projects into Workspace`
* Cochez : "Copy projects into workspace"

3. Compiler :

```bash
mvn clean package
```

Le `.jar` sera généré dans le dossier `/target`.

⚠️ Pour utiliser l’interface graphique, l’extension **WindowBuilder** doit être installée dans Eclipse.


---

## 👨‍💻 À propos

Projet développé par **Leul Nebeyu MULUGETA**, et ses camarades.

Ce projet a été réalisé dans le cadre de la **SAE S2.01** du BUT Informatique (Semestre 2) à l'IUT de Toulouse.

---

## 📄 Licence

Ce projet est sous licence MIT - voir le fichier [LICENSE](LICENSE) pour plus de détails.