#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include "TCP.h"

int sClient;

void HandlerSIGINT(int s);

void Echange(char* requete, char* reponse);
bool OBEP_Login(const char* user, const char* password);
void OBEP_Logout();
void OBEP_Get_Authors();
void OBEP_Get_Subjects();
void OBEP_Add_Author(const char* lastName, const char* firstName);
void OBEP_Add_Subject(const char* name);
void OBEP_Add_Book(int authorId, int subjectId, const char* title, const char* isbn, int pageCount, int stockQuantity, double price, int publishYear);

int main(int argc, char* argv[]){
	if (argc != 3)
	{
		printf("Erreur...\n");
		printf("USAGE : ClientTest ipServeur portServeur\n");
		exit(1);
	}
	
	// Armement des signaux
	struct sigaction A;
	A.sa_flags = 0;
	sigemptyset(&A.sa_mask);
	A.sa_handler = HandlerSIGINT;
	
	if (sigaction(SIGINT, &A, NULL) == -1)
	{
		perror("Erreur de sigaction");
		exit(1);
	}

	// Connexion sur le serveur
	if ((sClient = ClientSocket(argv[1], atoi(argv[2]))) == -1)
	{
		perror("Erreur de ClientSocket");
		exit(1);
	}
	printf("Connecté sur le serveur.\n");

	// Phase de login
	char user[50], password[50];

	printf("user: "); 
	fgets(user,50,stdin);
	user[strlen(user)-1] = 0;

	printf("password: "); 
	fgets(password,50,stdin);
	password[strlen(password)-1] = 0;
	

	if (!OBEP_Login(user,password))
	{
		close(sClient);
		exit(1);
	}

	// Récupération des auteurs et des sujets
    OBEP_Get_Authors();
    OBEP_Get_Subjects();

    OBEP_Add_Author("Dupont", "Pierre");
	OBEP_Add_Subject("Informatique");
	OBEP_Add_Book(1, 1, "Livre 1", "9781234567890", 250, 10, 19.99, 2026);

	OBEP_Logout();
	close(sClient);


	exit(0);
}

//***** Fin de connexion ********************************************
void HandlerSIGINT(int s)
{
	printf("\nArrêt du client.\n");
	OBEP_Logout();
	close(sClient);
	exit(0);
}

//***** Gestion du protocole OBEP ***********************************
bool OBEP_Login(const char* user,const char* password)
{
	char requete[200],reponse[10000];
	bool onContinue = true;

	// ***** Construction de la requete *********************
	sprintf(requete,"LOGIN#%s#%s", user, password);

	// ***** Envoi requete + réception réponse **************
	Echange(requete, reponse);

	// ***** Parsing de la réponse **************************
	char *ptr = strtok(reponse, "#"); // entête = LOGIN (normalement...)
	ptr = strtok(NULL, "#"); // statut = ok ou ko
	
	if (strcmp(ptr, "ok") == 0)
	{
		printf("Login OK.\n");
	}
	else
	{
		ptr = strtok(NULL, "#"); 
		printf("Erreur de login : %s\n", ptr);
		onContinue = false;
	}

	return onContinue;
}

void OBEP_Logout()
{
	char requete[200],reponse[1000];
	int nbEcrits, nbLus;

	// ***** Construction de la requete *********************
	sprintf(requete,"LOGOUT");

	// ***** Envoi requete + réception réponse **************
	Echange(requete, reponse);

	// ***** Parsing de la réponse **************************
	// pas vraiment utile...
}

void OBEP_Get_Authors()
{
    char requete[200],reponse[10000];

    // ***** Construction de la requete *********************
    sprintf(requete,"GET_AUTHORS");

    // ***** Envoi requete + réception réponse **************
    Echange(requete, reponse);

    // ***** Parsing de la réponse **************************
    char *ptr = strtok(reponse, "#"); // entête = GET_AUTHORS
    ptr = strtok(NULL, "#"); // statut = ok ou ko

    if (strcmp(ptr, "ok") == 0)
    {
        printf("Liste des auteurs :\n");

        while ((ptr = strtok(NULL, "#")) != NULL)
        {
            printf("ID : %s", ptr);

            ptr = strtok(NULL, "#");
            printf(" -- Nom : %s", ptr);

            ptr = strtok(NULL, "#");
            printf(" -- Prenom : %s\n", ptr);
        }
    }
    else
    {
        ptr = strtok(NULL, "#");
        printf("Erreur GET_AUTHORS : %s\n",ptr);
    }


}

//***** Récupération des sujets *************************************
void OBEP_Get_Subjects()
{
    char requete[200],reponse[10000];

    // ***** Construction de la requete *********************
    sprintf(requete,"GET_SUBJECTS");

    // ***** Envoi requete + réception réponse **************
    Echange(requete, reponse);

    // ***** Parsing de la réponse **************************
    char *ptr = strtok(reponse, "#"); // entête = GET_SUBJECTS
    ptr = strtok(NULL, "#"); // statut = ok ou ko

    if (strcmp(ptr, "ok") == 0)
    {
        printf("Liste des sujets :\n");

        while ((ptr = strtok(NULL, "#")) != NULL)
        {
            printf("ID : %s", ptr);

            ptr = strtok(NULL, "#");
            printf(" -- Nom : %s\n", ptr);
        }
    }
    else
    {
        ptr = strtok(NULL, "#");
        printf("Erreur GET_SUBJECTS : %s\n",ptr);
    }
}


void OBEP_Add_Author(const char* lastName, const char* firstName)
{
    char requete[200], reponse[10000];

    // ***** Construction de la requete *********************
    sprintf(requete, "ADD_AUTHOR#%s#%s", lastName, firstName);

    // ***** Envoi requete + réception réponse **************
    Echange(requete, reponse);

    // ***** Parsing de la réponse **************************
    char *ptr = strtok(reponse, "#"); // entête = ADD_AUTHOR
    ptr = strtok(NULL, "#"); // statut = ok ou ko

    if (strcmp(ptr, "ok") == 0)
    {
        ptr = strtok(NULL, "#"); // id de l'auteur
        printf("Auteur ajouté avec ID : %s\n", ptr);
    }
    else
    {
        ptr = strtok(NULL, "#");
        printf("Erreur ajout auteur : %s\n", ptr);
    }
}


void OBEP_Add_Subject(const char* name)
{
    char requete[200], reponse[10000];

    // ***** Construction de la requete *********************
    sprintf(requete, "ADD_SUBJECT#%s", name);

    // ***** Envoi requete + réception réponse **************
    Echange(requete, reponse);

    // ***** Parsing de la réponse **************************
    char *ptr = strtok(reponse, "#"); // entête = ADD_SUBJECT
    ptr = strtok(NULL, "#"); // statut = ok ou ko

    if (strcmp(ptr, "ok") == 0)
    {
        ptr = strtok(NULL, "#"); // id du sujet
        printf("Sujet ajouté avec ID : %s\n", ptr);
    }
    else
    {
        ptr = strtok(NULL, "#");
        printf("Erreur ajout sujet : %s\n", ptr);
    }
}


void OBEP_Add_Book(int authorId, int subjectId, const char* title, const char* isbn, int pageCount, int stockQuantity, double price, int publishYear)
{
    char requete[200], reponse[10000];

    // ***** Construction de la requete *********************
    sprintf(requete, "ADD_BOOK#%d#%d#%s#%s#%d#%d#%f#%d",
            authorId, subjectId, title, isbn, pageCount,
            stockQuantity, price, publishYear);

    // ***** Envoi requete + réception réponse **************
    Echange(requete, reponse);

    // ***** Parsing de la réponse **************************
    char *ptr = strtok(reponse, "#"); // entête = ADD_BOOK
    ptr = strtok(NULL, "#"); // statut = ok ou ko

    if (strcmp(ptr, "ok") == 0)
    {
        ptr = strtok(NULL, "#"); // id du livre
        printf("Livre ajouté avec ID : %s\n", ptr);
    }
    else
    {
        ptr = strtok(NULL, "#");
        printf("Erreur ajout livre : %s\n", ptr);
    }
}


//***** Echange de données entre client et serveur ******************
void Echange(char* requete, char* reponse)
{
	int nbEcrits, nbLus;

	// ***** Envoi de la requete ****************************
	if ((nbEcrits = Send(sClient, requete, strlen(requete))) == -1)
	{
		perror("Erreur de Send");
		close(sClient);
		exit(1);
	}

	// ***** Attente de la reponse **************************
	if ((nbLus = Receive(sClient, reponse)) < 0)
	{
		perror("Erreur de Receive");
		close(sClient);
		exit(1);
	}
	
	if (nbLus == 0)
	{
		printf("Serveur arrete, pas de reponse reçue...\n");
		close(sClient);
		exit(1);
	}

	reponse[nbLus] = 0;
}