# Projet_DACSC - Application d'encodage gestion de livres

## Etape 1
Application client-serveur développée en C/C++ sous Linux
Le serveur utilise des sockets TCP, un pool de threads POSIX et une base de données MySQL. Le client possède une interface graphique Qt

## Fonctionnalités
- LOGIN / LOGOUT
- GET_AUTHORS / GET_SUBJECTS
- ADD_AUTHOR / ADD_SUBJECT / ADD_BOOK
- Affichage des livres ajoutés pendant la session
- Gestion de plusieurs clients simultanément

## Fonctionnement
Le client communique avec le serveur grâce au protocole OBEP. Les champs des requêtes sont séparés par #
Après connexion, les auteurs et les sujets sont chargés depuis MySQL

## Exécution
- Démarrer MySQL
- Lancer le serveur d'encodage (port 5000)
- Lancer le client Qt et se connecter

## Étape 2 — Java
Développement d'un Client Encodage, d'un Client Achat et d'un Serveur Achat en Java
Les interfaces graphiques sont réalisées avec Swing selon le modèle MVC

## Fonctionnalités
### Client Encodage
- Connexion au serveur C/C++ avec le protocole OBEP
- LOGIN / LOGOUT
- Consultation des auteurs et des sujets
- Ajout d'auteurs, de sujets et de livres

### Client Achat
- Identification du client
- Recherche de livres
- Ajout et suppression d'articles du panier
- Annulation et paiement du panier

### Serveur Achat
- Communication avec le protocole BSPP
- Gestion des requêtes avec un pool de threads
- Accès à MySQL avec des DAO JDBC

## Installation
- Installer Java JDK 25
- Ouvrir le dossier Java dans IntelliJ IDEA
- Charger le fichier pom.xml avec Maven
- Configurer les connexions réseau et MySQL

 ## Exécution
- Démarrer MySQL.
- Démarrer le serveur d'encodage C/C++ pour le Client Encodage Java
- Démarrer le Serveur Achat Java pour le Client Achat
- Lancer les clients Java correspondants.

## Configuration
Le fichier serveurachat.properties doit être configuré localement pour la connexion MySQL.
Les identifiants personnels ne doivent pas être publiés sur GitHub.
