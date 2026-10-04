#include "OBEP.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <mysql.h>

#define NB_MAX_AUTHORS 100

AUTHOR auteurs[NB_MAX_AUTHORS];

int clients[NB_MAX_CLIENTS];
int nbClients = 0;

int estPresent(int socket);
void ajoute(int socket);
void retire(int socket);

pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER;

//***** Parsing de la requête et création de la reponse *************
bool OBEP(char* requete, char* reponse, int socket){
	// ***** Récupération nom de la requete *****************
	char *ptr = strtok(requete, "#");

	// ***** LOGIN ******************************************
	if (strcmp(ptr, "LOGIN") == 0)
	{
		char user[50], password[50];

		strcpy(user,strtok(NULL,"#"));
		strcpy(password,strtok(NULL,"#"));

		printf("\t[THREAD %p] LOGIN de %s\n", pthread_self(), user);

		if (estPresent(socket) >= 0) // client déjà loggé
		{
			sprintf(reponse, "LOGIN#ko#Client déjà loggé !");
			return false;
		}
		else
		{
			if (OBEP_Login(user, password))
			{
				sprintf(reponse,"LOGIN#ok");
				ajoute(socket);
			}
			else
			{
				sprintf(reponse,"LOGIN#ko#Mauvais identifiants !");
				return false;
			}
		}
	}

	// ***** LOGOUT *****************************************
	if (strcmp(ptr, "LOGOUT") == 0)
	{
		printf("\t[THREAD %p] LOGOUT\n", pthread_self());
		retire(socket);
		sprintf(reponse,"LOGOUT#ok");
		return false;
	}

	

	if (strcmp(ptr,"GET_AUTHORS") == 0)
	{
	    printf("\t[THREAD %p] GET_AUTHORS\n",pthread_self());

	    if (estPresent(socket) == -1)
	    {
	        sprintf(reponse,"GET_AUTHORS#ko#Client non loggé !");
	    }
	    else
	    {
	        AUTHOR auteurs[100];

	        int nbAuthors = OBEP_Get_Authors(auteurs);

	        sprintf(reponse,"GET_AUTHORS#ok");

	        for (int i=0 ; i<nbAuthors ; i++)
			{
			    sprintf(reponse + strlen(reponse),"#%d#%s#%s",auteurs[i].id,auteurs[i].last_name,auteurs[i].first_name);
			}
	    }
	}
	if (strcmp(ptr,"GET_SUBJECTS") == 0)
	{
	    printf("\t[THREAD %p] GET_SUBJECTS\n",pthread_self());

	    if (estPresent(socket) == -1)
	    {
	        sprintf(reponse,"GET_SUBJECTS#ko#Client non loggé !");
	    }
	    else
	    {
	        SUBJECT subjects[100];

	        int nbSubjects = OBEP_Get_Subjects(subjects);

	        sprintf(reponse,"GET_SUBJECTS#ok");

	        for (int i=0; i<nbSubjects; i++)
	        {
	            sprintf(reponse + strlen(reponse),"#%d#%s",subjects[i].id,subjects[i].name);
	        }
	    }
	}

	if (strcmp(ptr,"ADD_AUTHOR") == 0)
	{
	    char lastName[50], firstName[50];

	    strcpy(lastName, strtok(NULL,"#"));
	    strcpy(firstName, strtok(NULL,"#"));

	    printf("\t[THREAD %p] ADD_AUTHOR %s %s\n",pthread_self(), lastName, firstName);

	    if (estPresent(socket) == -1)
	    {
	        sprintf(reponse,"ADD_AUTHOR#ko#Client non loggé !");
	    }
	    else
	    {
	        int id = OBEP_Add_Author(lastName, firstName);

	        if (id == -1){
	            sprintf(reponse,"ADD_AUTHOR#ko#-1");
	        }
	        else{
	            sprintf(reponse,"ADD_AUTHOR#ok#%d",id);
	        }
	    }
	}

	if (strcmp(ptr,"ADD_SUBJECT") == 0)
	{
	    char name[50];

	    strcpy(name, strtok(NULL,"#"));

	    printf("\t[THREAD %p] ADD_SUBJECT %s\n", pthread_self(), name);

	    if (estPresent(socket) == -1)
	    {
	        sprintf(reponse,"ADD_SUBJECT#ko#Client non loggé !");
	    }
	    else
	    {
	        int id = OBEP_Add_Subject(name);

	        if (id == -1){
	            sprintf(reponse,"ADD_SUBJECT#ko#-1");
	        }
	        else{
	            sprintf(reponse,"ADD_SUBJECT#ok#%d",id);
	        }
	    }
	}


	if (strcmp(ptr,"ADD_BOOK") == 0)
	{
	    int authorId, subjectId, pageCount, stockQuantity, publishYear;
	    double price;
	    char title[100], isbn[20];

	    authorId = atoi(strtok(NULL,"#"));
	    subjectId = atoi(strtok(NULL,"#"));
	    strcpy(title, strtok(NULL,"#"));
	    strcpy(isbn, strtok(NULL,"#"));
	    pageCount = atoi(strtok(NULL,"#"));
	    stockQuantity = atoi(strtok(NULL,"#"));
	    price = atof(strtok(NULL,"#"));
	    publishYear = atoi(strtok(NULL,"#"));

	    printf("\t[THREAD %p] ADD_BOOK %s\n",pthread_self(), title);

	    if (estPresent(socket) == -1)
	    {
	        sprintf(reponse,"ADD_BOOK#ko#Client non loggé !");
	    }
	    else
	    {
	        int id = OBEP_Add_Book(authorId,subjectId,title,isbn,pageCount,stockQuantity,price,publishYear);

	        if (id == -1){
	            sprintf(reponse,"ADD_BOOK#ko#-1");
	        }
	        else{
	            sprintf(reponse,"ADD_BOOK#ok#%d",id);
	        }
	    }
	}

	return true;
}

//***** Traitement des requêtes *************************************
bool OBEP_Login(const char* user, const char* password){
    MYSQL* connexion;
    connexion= mysql_init(NULL);

    if (mysql_real_connect(connexion,
                           "localhost",
                           "Student",
                           "PassStudent1_",
                           "PourStudent",
                           0,NULL,0) == NULL)
    {
        printf("Erreur connexion BD : %s\n", mysql_error(connexion));
        exit(1);
    }

    printf("Connexion établie avec succès à la BD.\n");

    char requete[256];
    sprintf(requete,"SELECT * FROM employees WHERE login='%s' AND password='%s';", user, password);

	if (mysql_query(connexion,requete) != 0)
	{
		fprintf(stderr, "Erreur de mysql_query: %s\n",mysql_error(connexion));
		exit(1);
	}

	printf("Requete SELECT réussie.\n");

	MYSQL_RES* ResultSet;
    if ((ResultSet=mysql_store_result(connexion))==NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n",mysql_error(connexion));
		exit(1);
    }

    MYSQL_ROW ligne;
    bool ok=false;

	int nbChamps = mysql_num_fields(ResultSet);
	while ((ligne = mysql_fetch_row(ResultSet)) != NULL)
	{
		for(int i=0; i<nbChamps ; i++){
			printf("%10s\t",ligne[i]);
		}
		printf("\n");

		ok=true;
	}

   
    mysql_close(connexion);

    return ok;
}


//***** Gestion de l'état du protocole ******************************
int estPresent(int socket)
{
	int indice = -1;
	
	pthread_mutex_lock(&mutexClients);
	for(int i=0; i<nbClients; i++)
	{
		if (clients[i] == socket) 
		{
		 	indice = i; 
		 	break; 
		}
	}
	pthread_mutex_unlock(&mutexClients);

	return indice;
}

void ajoute(int socket)
{
	pthread_mutex_lock(&mutexClients);
	clients[nbClients] = socket;
	nbClients++;
	pthread_mutex_unlock(&mutexClients);
}

void retire(int socket)
{
	int pos = estPresent(socket);
	if (pos == -1) return;

	pthread_mutex_lock(&mutexClients);
	for (int i=pos ; i<=nbClients-2 ; i++){
		clients[i] = clients[i+1];
	}
	nbClients--;
	pthread_mutex_unlock(&mutexClients);
}

//***** Fin prématurée **********************************************
void OBEP_Close(){
	pthread_mutex_lock(&mutexClients);
	for (int i=0; i<nbClients; i++){
		close(clients[i]);
	}

	pthread_mutex_unlock(&mutexClients);

}

int OBEP_Get_Authors(AUTHOR auteurs[])
{
    MYSQL* connexion;
    connexion = mysql_init(NULL);

    if (mysql_real_connect(connexion,
                           "localhost",
                           "Student",
                           "PassStudent1_",
                           "PourStudent",
                           0,NULL,0) == NULL)
    {
        printf("Erreur connexion BD : %s\n", mysql_error(connexion));
        exit(1);
    }

    printf("Connexion établie avec succès à la BD.\n");

    char requete[256];
    sprintf(requete, "SELECT id,last_name,first_name FROM authors;");

    if (mysql_query(connexion,requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n",mysql_error(connexion));
        exit(1);
    }

    printf("Requete SELECT réussie.\n");

    MYSQL_RES* ResultSet;
    if ((ResultSet = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n",mysql_error(connexion));
        exit(1);
    }

    MYSQL_ROW ligne;
    int nbAuthors = 0;
    int nbChamps = mysql_num_fields(ResultSet);

    while ((ligne = mysql_fetch_row(ResultSet)) != NULL)
    {
        for (int i=0; i<nbChamps; i++)
        {
            printf("%10s\t", ligne[i]);
        }

        printf("\n");

        auteurs[nbAuthors].id = atoi(ligne[0]);
        strcpy(auteurs[nbAuthors].last_name, ligne[1]);
        strcpy(auteurs[nbAuthors].first_name, ligne[2]);

        nbAuthors++;
    }

    mysql_close(connexion);

    return nbAuthors;
}

int OBEP_Get_Subjects(SUBJECT subjects[]){
 	MYSQL* connexion;
    connexion = mysql_init(NULL);

    if (mysql_real_connect(connexion,
                           "localhost",
                           "Student",
                           "PassStudent1_",
                           "PourStudent",
                           0,NULL,0) == NULL)
    {
        printf("Erreur connexion BD : %s\n", mysql_error(connexion));
        exit(1);
    }

    printf("Connexion établie avec succès à la BD.\n");

    char requete[256];
    sprintf(requete, "SELECT id,name FROM subjects;");

    if (mysql_query(connexion,requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n",mysql_error(connexion));
        exit(1);
    }

    printf("Requete SELECT réussie.\n");

    MYSQL_RES* ResultSet;

    if ((ResultSet = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n",mysql_error(connexion));
        exit(1);
    }

    MYSQL_ROW ligne;
    int nbSubjects = 0;
    int nbChamps = mysql_num_fields(ResultSet);

    while ((ligne = mysql_fetch_row(ResultSet)) != NULL)
    {
        for (int i=0; i<nbChamps; i++)
        {
            printf("%10s\t", ligne[i]);
        }

        printf("\n");

        subjects[nbSubjects].id = atoi(ligne[0]);
        strcpy(subjects[nbSubjects].name, ligne[1]);

        nbSubjects++;
    }

    mysql_close(connexion);

    return nbSubjects;
}

int OBEP_Add_Author(char* lastName, char* firstName){
	MYSQL* connexion;
    connexion = mysql_init(NULL);

    if (mysql_real_connect(connexion,
                           "localhost",
                           "Student",
                           "PassStudent1_",
                           "PourStudent",
                           0,NULL,0) == NULL)
    {
        printf("Erreur connexion BD : %s\n", mysql_error(connexion));
        exit(1);
    }

    printf("Connexion établie avec succès à la BD.\n");

    char requete[256];

    sprintf(requete,"SELECT id FROM authors WHERE last_name='%s' AND first_name='%s';",lastName,firstName);

    if (mysql_query(connexion,requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(1);
    }

    MYSQL_RES* ResultSet;

    if ((ResultSet = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        exit(1);
    }

    MYSQL_ROW ligne;

    ligne = mysql_fetch_row(ResultSet);

    if (ligne != NULL)
    {
        mysql_close(connexion);
        return -1;
    }

    sprintf(requete,"INSERT INTO authors (last_name,first_name) VALUES ('%s','%s');",lastName,firstName);

    if (mysql_query(connexion,requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(1);
    }

    printf("Insertion auteur réussie.\n");

    sprintf(requete,"SELECT id FROM authors WHERE last_name='%s' AND first_name='%s';",lastName,firstName);

    if (mysql_query(connexion,requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(1);
    }

    if ((ResultSet = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        exit(1);
    }

    ligne = mysql_fetch_row(ResultSet);

    int id = atoi(ligne[0]);

    mysql_close(connexion);

    return id;
}

int OBEP_Add_Subject(char* name){
	MYSQL* connexion;
    connexion = mysql_init(NULL);

    if (mysql_real_connect(connexion,
                           "localhost",
                           "Student",
                           "PassStudent1_",
                           "PourStudent",
                           0,NULL,0) == NULL)
    {
        printf("Erreur connexion BD : %s\n", mysql_error(connexion));
        exit(1);
    }

    printf("Connexion établie avec succès à la BD.\n");

    char requete[256];

    sprintf(requete,"SELECT id FROM subjects WHERE name='%s';",name);

    if (mysql_query(connexion,requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(1);
    }

	printf("Requete SELECT réussie.\n");

    MYSQL_RES* ResultSet;

    if ((ResultSet = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        exit(1);
    }

    MYSQL_ROW ligne;
    ligne = mysql_fetch_row(ResultSet);

    if (ligne != NULL)
    {
        mysql_close(connexion);
        return -1;
    }


    sprintf(requete,"INSERT INTO subjects (name) VALUES ('%s');", name);

    if (mysql_query(connexion,requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(1);
    }

    printf("Insertion sujet réussie.\n");

    sprintf(requete, "SELECT id FROM subjects WHERE name='%s';",name);

    if (mysql_query(connexion,requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(1);
    }

    if ((ResultSet = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        exit(1);
    }

    ligne = mysql_fetch_row(ResultSet);

    int id = atoi(ligne[0]);

    mysql_close(connexion);

    return id;

}

int OBEP_Add_Book(int author_id, int subject_id, char* title, char* isbn, int pageCount, int stockQuantity, double price, int publishYear){
	MYSQL* connexion;
    connexion = mysql_init(NULL);

    if (mysql_real_connect(connexion,
                           "localhost",
                           "Student",
                           "PassStudent1_",
                           "PourStudent",
                           0,NULL,0) == NULL)
    {
        printf("Erreur connexion BD : %s\n", mysql_error(connexion));
        exit(1);
    }

    printf("Connexion établie avec succès à la BD.\n");

    char requete[256];

    sprintf(requete,"SELECT id FROM authors WHERE id=%d;",author_id);

    if (mysql_query(connexion,requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(1);
    }

    printf("Requete SELECT réussie.\n");

    MYSQL_RES* ResultSet;

    if ((ResultSet = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        exit(1);
    }

    MYSQL_ROW ligne;
    ligne = mysql_fetch_row(ResultSet);

    if (ligne == NULL)
    {
        mysql_close(connexion);
        return -1;
    }

    mysql_free_result(ResultSet);

    sprintf(requete,"SELECT id FROM subjects WHERE id=%d;",subject_id);

    if (mysql_query(connexion,requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(1);
    }

    if ((ResultSet = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        exit(1);
    }

    ligne = mysql_fetch_row(ResultSet);

    if (ligne == NULL)
    {
        mysql_close(connexion);
        return -1;
    }


    // Ajouter le livre
    sprintf(requete,
            "INSERT INTO books (author_id,subject_id,title,isbn,page_count,stock_quantity,price,publish_year) "
            "VALUES (%d,%d,'%s','%s',%d,%d,%f,%d);",author_id,subject_id,title,isbn,pageCount,stockQuantity,price,publishYear);

    if (mysql_query(connexion,requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(1);
    }

    printf("Insertion livre réussie.\n");

    // Récupérer l'id du livre
    sprintf(requete,"SELECT id FROM books WHERE title='%s' AND isbn='%s';",title,isbn);

    if (mysql_query(connexion,requete) != 0)
    {
        fprintf(stderr, "Erreur de mysql_query: %s\n", mysql_error(connexion));
        exit(1);
    }

    if ((ResultSet = mysql_store_result(connexion)) == NULL)
    {
        fprintf(stderr, "Erreur de mysql_store_result: %s\n", mysql_error(connexion));
        exit(1);
    }

    ligne = mysql_fetch_row(ResultSet);

    int id = atoi(ligne[0]);

    mysql_close(connexion);

    return id;
}