# Projet_DACSC - Application d'encodage gestion de livres

##Etape 1 — Encodage de livres
Application client-serveur développée en C/C++ sous Linux.
Le serveur utilise des sockets TCP, un pool de threads POSIX et une base de données MySQL. Le client possède une interface graphique Qt.

##Fonctionnalités
- LOGIN / LOGOUT
- GET_AUTHORS / GET_SUBJECTS
- ADD_AUTHOR / ADD_SUBJECT / ADD_BOOK
- Affichage des livres ajoutés pendant la session
- Gestion de plusieurs clients simultanément

##Fonctionnement
Le client communique avec le serveur grâce au protocole OBEP. Les champs des requêtes sont séparés par #.
Après connexion, les auteurs et les sujets sont chargés depuis MySQL.

##Exécution
- Démarrer MySQL
- Lancer le serveur d'encodage (port 5000)
- Lancer le client Qt et se connecter
