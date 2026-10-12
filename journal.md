# Journal : TrainStation (ALTN72, 2026-27)

**Groupe :** Alexandre GINISTY, Florian DE SOUSA

## README

- **Prérequis** : Java 21, Maven (wrapper `./mvnw` fourni), MariaDB.
- **Configuration** : identifiants de la base lus dans les variables d'environnement `DB_USER` (défaut `root`) et `DB_PASSWORD` (défaut vide). Aucun secret n'est versionné.
- **Lancer** : `./mvnw spring-boot:run`, puis ouvrir `http://localhost:8080`.

## ARCHITECTURE

- **Dépôt** : https://github.com/Ianouu1/TrainStation

## CHOIX TECHNIQUES

- **Droits par profil** :

  | N° | Fonctionnalité | Responsable d'exploitation | Agent |
  | --- | --- | --- | --- |
  |   | F2-F3 Consulter équipements et fiches | Accès complet | Accès complet |
  | F4 | Déclarer un incident | Accès complet | Accès complet |
  |   | F5-F6 Consulter / modifier un incident | Accès complet | Lecture seule |
  | F7 | Affecter ou réaffecter un prestataire | Accès complet | Interdit |
  | F8 | Déclarer un incident résolu | Accès complet | Interdit |
  | F9 | Consulter l'historique | Accès complet | Accès complet |
  | F10 | Évaluer un prestataire | Accès complet | Interdit |
  | F11 | Tableau de bord | Accès complet | Interdit |

  Seul le responsable peut modifier un incident (F6), et uniquement sa description : l'équipement et la date d'ouverture restent figés pour garder un historique fiable.

## LOT ALPHA

### Fonctionnalité transverse 1 : Supervision avancée
- **Objectif** : filtrer et trier le tableau de bord pour repérer rapidement les incidents prioritaires.

### Fonctionnalité transverse 2 : Visualisation des données
- **Objectif** : donner au responsable une vue synthétique de la situation.
