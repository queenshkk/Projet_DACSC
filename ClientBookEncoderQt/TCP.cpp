#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h> 
#include <netinet/in.h> 
#include "TCP.h"

int ServerSocket(int port){
	int sEcoute;

	printf("PID = %d\n", getpid());

	// Création de la socket
	if ((sEcoute = socket(AF_INET, SOCK_STREAM, 0)) == -1)
	{
		perror("Erreur de socket()");
		return -1;
	}
	printf("Socket créee = %d\n", sEcoute);

	// Construction de l'adresse réseau de la socket
	struct addrinfo hints;
	struct addrinfo *results;

	memset(&hints, 0,sizeof(struct addrinfo));
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_flags = AI_PASSIVE | AI_NUMERICSERV;
	
	char portServ[10];
    sprintf(portServ, "%d", port);

	if (getaddrinfo(NULL, portServ, &hints, &results) != 0)
	{
		printf("Erreur de getaddrinfo()\n");
		close(sEcoute);
		return -1;
	}

	// Affichage du contenu de l'adresse obtenue
	char host[NI_MAXHOST];
	char port2[NI_MAXSERV];

	getnameinfo(results->ai_addr, results->ai_addrlen, 
				host, NI_MAXHOST, port2, NI_MAXSERV,
				NI_NUMERICSERV | NI_NUMERICHOST);

	printf("Mon adresse IP : %s -- Mon port : %s\n", host, port2);

	// Liaison de la socket à l'adresse réseau
	if (bind(sEcoute, results->ai_addr, results->ai_addrlen) < 0)
	{
		perror("Erreur de bind()");
		close(sEcoute);
		return -1;
	}

	freeaddrinfo(results);
	printf("bind() réussi !\n");

	// Mise à l'écoute de la socket
	if (listen(sEcoute, SOMAXCONN) == -1)
	{
		perror("Erreur de listen()");
		close(sEcoute);
		return -1;
	}
	printf("listen() réussi !\n");

	return sEcoute;
}

int Accept(int sEcoute, char *ipClient){
	// Attente d'une connexion
	int sService;
	
	if ((sService= accept(sEcoute, NULL, NULL)) == -1)
	{
		perror("Erreur de accept()");
		return -1;
	}
	printf("accept() réussi !");
	printf("Socket de service = %d\n", sService);

	// Récuperation d'information sur le client connecté
	char host[NI_MAXHOST];
	char port[NI_MAXSERV];

	struct sockaddr_in adrClient; 
	socklen_t adrClientLen = sizeof(struct sockaddr_in);

	getpeername(sService, (struct sockaddr*)&adrClient, &adrClientLen);
	getnameinfo((struct sockaddr*)&adrClient, adrClientLen, host, NI_MAXHOST, port, NI_MAXSERV, NI_NUMERICSERV | NI_NUMERICHOST);
	
	printf("Client connecté --> Adresse IP : %s -- Port : %s\n", host, port);

	if (ipClient != NULL)
	{
	    strcpy(ipClient, host);
	}

	return sService;
}

int ClientSocket(char* ipServeur, int portServeur){
	int sService ;

	printf("PID = %d\n", getpid());

	// Création de la socket
	if ((sService = socket(AF_INET, SOCK_STREAM, 0)) == -1)
	{
		perror("Erreur de socket()");
		return -1;
	}
	
	printf("Socket créee = %d\n", sService);
	
	// Construction de l'adresse réseau
	struct addrinfo hints;
	struct addrinfo *results;

	memset(&hints,0,sizeof(struct addrinfo));
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_flags = AI_NUMERICSERV;

	char portServ[10];
    sprintf(portServ, "%d", portServeur);

	if (getaddrinfo(ipServeur, portServ, &hints, &results) != 0){
		close(sService);
        return -1;
	}

	// Demande de connexion
	if (connect(sService, results->ai_addr, results->ai_addrlen) == -1)
	{
		perror("Erreur de connect()");
		close(sService);
		return -1;
	}

    freeaddrinfo(results);

	printf("connect() réussi !");

	return sService ;
}

int Send(int sSocket, char* data, int taille){
	char buffer[5];

    if (taille < 0 || taille > 9999)
    {
        return -1;
    }

    sprintf(buffer, "%04d", taille);

    // Envoi de la taille
    if (write(sSocket, buffer, 4) != 4){
        return -1;
    }

    // Envoi des données
    if (write(sSocket, data, taille) != taille){
        return -1;
    }

    return taille;

}

int Receive(int sSocket, char* data){
	char buffer[5];
    int taille;

    if (read(sSocket, buffer, 4) != 4)
    {
        return -1;
    }

    buffer[4] = '\0';
    taille = atoi(buffer);

    if (read(sSocket, data, taille) != taille)
    {
        return -1;
    }

    return taille;
}