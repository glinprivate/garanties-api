# garanties-api — conteneurisation

## Le problème client

Aujourd'hui, l'API des garanties est livrée à la main : on compile un jar sur un poste, puis on le copie sur le serveur. Chaque environnement (poste du développeur, recette, production) a sa propre version de Java et sa propre configuration. Résultat : des livraisons lentes et sources d'erreurs, et des environnements qui divergent (« ça marche chez moi »).

## La solution

Une image Docker unique, construite de la même façon partout et lancée à l'identique sur chaque environnement.

- **Build multi-stage** : un premier étage compile avec le JDK et Maven, et seul le jar part dans l'étage final, basé sur un JRE. L'image livrée ne contient ni les outils de build, ni le code source, ni le cache Maven.
- **Couches ordonnées** : `pom.xml` est copié et les dépendances sont téléchargées avant de copier `src/`. Modifier le code ne relance pas le téléchargement des dépendances, ce qui rend les rebuilds rapides grâce au cache Docker.
- **Utilisateur non-root** : l'application tourne sous un utilisateur système `app`. Si une faille est exploitée, l'attaquant n'obtient pas les droits root dans le conteneur.
- Un fichier `.dockerignore` exclut `target/`, `.git/` et `.idea/` du contexte de build.

## Les mesures

| Image                    | Base                    | Taille |
|--------------------------|-------------------------|--------|
| `garanties-api:naif`     | JDK + Maven + sources   | 263 Mo   |
| `garanties-api:1.0.0`    | JRE + jar (multi-stage) | 141 Mo   |

*Tailles relevées avec `docker images garanties-api`.*

## Le lancement

```bash
# Construire l'image
docker build -t garanties-api:1.0.0 .

# Lancer le conteneur
docker run -d --name garanties -p 8080:8080 garanties-api:1.0.0

# Vérifier
curl localhost:8080/api/guarantees
docker exec garanties whoami        # → app
```