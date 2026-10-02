#ifndef OBEP_H
#define OBEP_H
#define NB_MAX_CLIENTS 100

typedef struct {
  int  id;
  char last_name[20];
  char first_name[20];
  char birth_date[20];
} AUTHOR;

typedef struct {
  int  id;
  char name[20];
} SUBJECT;

typedef struct {
  int   id;
  int   author_id;    // clé étrangère
  int   subject_id;   // clé étrangère
  char  title[100];
  char  isbn[20];
  int   page_count;
  int   stock_quantity;
  float price;
  int   publish_year;
} BOOK;

bool OBEP(char* requete, char* reponse, int socket);
bool OBEP_Login(const char* user, const char* password);
void OBEP_Close();
int OBEP_Get_Authors(AUTHOR auteurs[]);
int OBEP_Get_Subjects(SUBJECT subjects[]);
int OBEP_Add_Author(char* lastName, char* firstName);
int OBEP_Add_Subject(char* name);
int OBEP_Add_Book(int author_id, int subject_id, char* title, char* isbn, int pageCount, int stockQuantity, double price, int publishYear);

#endif