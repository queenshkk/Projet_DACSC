#include "mainwindowclientbookencoder.h"
#include "ui_mainwindowclientbookencoder.h"
#include "unistd.h"
#include <stdio.h>
#include <QInputDialog>
#include <QMessageBox>
#include <iostream>
#include "TCP.h"

using namespace std;

int sClient;
int idAuthors[100];
int idSubjects[100];
int nbAuthors = 0;
int nbSubjects = 0;

MainWindowClientBookEncoder::MainWindowClientBookEncoder(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindowClientBookEncoder)
{
    ui->setupUi(this);
    ::close(2);

    //this->setFixedSize(1068, 301);

    // Configuration de la table des employes (Personnel Garage)
    ui->tableWidgetEncodedBooks->setColumnCount(9);
    ui->tableWidgetEncodedBooks->setRowCount(0);
    QStringList labelsTableEmployes;
    labelsTableEmployes << "Id" << "Titre" << "Auteur" << "Sujet" << "ISBN" << "Pages" << "Année" << "Prix" << "Stock";
    ui->tableWidgetEncodedBooks->setHorizontalHeaderLabels(labelsTableEmployes);
    ui->tableWidgetEncodedBooks->horizontalHeader()->setVisible(true);
    ui->tableWidgetEncodedBooks->horizontalHeader()->setStretchLastSection(true);
    ui->tableWidgetEncodedBooks->verticalHeader()->setVisible(false);
    ui->tableWidgetEncodedBooks->horizontalHeader()->setStyleSheet("background-color: lightyellow");
    int columnWidths[] = {35, 250, 200, 200, 150, 50, 50, 50, 40};
    for (int col = 0; col < 9; ++col)
        ui->tableWidgetEncodedBooks->setColumnWidth(col, columnWidths[col]);

    this->logoutOk();

    // Exemples d'utilisation (à supprimer)
    /*this->addTupleTableBooks(1,"Les Thanatonautes","Bernard Werber","Science-Fiction","978-2253139225",505,1999,9.7f,3);
    this->addTupleTableBooks(6,"Dune","Frank Herbert","Science-Fiction","978-2266320481",929,2021,11.95f,13);
    this->addTupleTableBooks(13,"Le silence des agneaux","Thomas Harris","Thriller","978-2266208949",377,2015,7.7f,17);

    this->addComboBoxAuthors("Bernard Werber");
    this->addComboBoxAuthors("Dan Brown");

    this->addComboBoxSubjects("Roman");
    this->addComboBoxSubjects("Science-fiction");
    */
}

MainWindowClientBookEncoder::~MainWindowClientBookEncoder() {
    delete ui;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles Table des livres encodés (ne pas modifier) ////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::addTupleTableBooks(int id,
                                                     string title,
                                                     string author,
                                                     string subject,
                                                     string isbn,
                                                     int pageCount,
                                                     int publishYear,
                                                     float price,
                                                     int stockQuantity)
{
    int nb = ui->tableWidgetEncodedBooks->rowCount();
    nb++;
    ui->tableWidgetEncodedBooks->setRowCount(nb);
    ui->tableWidgetEncodedBooks->setRowHeight(nb-1,10);

    // id
    QTableWidgetItem *item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(id));
    ui->tableWidgetEncodedBooks->setItem(nb-1,0,item);

    // title
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setText(QString::fromStdString(title));
    ui->tableWidgetEncodedBooks->setItem(nb-1,1,item);

    // author
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(author));
    ui->tableWidgetEncodedBooks->setItem(nb-1,2,item);

    // subject
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(subject));
    ui->tableWidgetEncodedBooks->setItem(nb-1,3,item);

    // isbn
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::fromStdString(isbn));
    ui->tableWidgetEncodedBooks->setItem(nb-1,4,item);

    // pageCount
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(pageCount));
    ui->tableWidgetEncodedBooks->setItem(nb-1,5,item);

    // publishYear
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(publishYear));
    ui->tableWidgetEncodedBooks->setItem(nb-1,6,item);

    // price
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(price));
    ui->tableWidgetEncodedBooks->setItem(nb-1,7,item);

    // stockQuantity
    item = new QTableWidgetItem;
    item->setFlags(item->flags() & ~Qt::ItemIsSelectable);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(QString::number(stockQuantity));
    ui->tableWidgetEncodedBooks->setItem(nb-1,8,item);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::clearTableBooks() {
    ui->tableWidgetEncodedBooks->setRowCount(0);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles des comboboxes (ne pas modifier) //////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::addComboBoxAuthors(string author){
    ui->comboBoxAuthors->addItem(QString::fromStdString(author));
}

string MainWindowClientBookEncoder::getSelectionAuthor() const {
    return ui->comboBoxAuthors->currentText().toStdString();
}

void MainWindowClientBookEncoder::clearComboBoxAuthors() {
    ui->comboBoxAuthors->clear();
}

void MainWindowClientBookEncoder::addComboBoxSubjects(string subject){
    ui->comboBoxSubjects->addItem(QString::fromStdString(subject));
}

string MainWindowClientBookEncoder::getSelectionSubject() const {
    return ui->comboBoxSubjects->currentText().toStdString();
}

void MainWindowClientBookEncoder::clearComboBoxSubjects() {
    ui->comboBoxSubjects->clear();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonction utiles de la fenêtre (ne pas modifier) ////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
string MainWindowClientBookEncoder::getTitle() const {
    return ui->lineEditTitle->text().toStdString();
}

string MainWindowClientBookEncoder::getIsbn() const {
    return ui->lineEditIsbn->text().toStdString();
}

int MainWindowClientBookEncoder::getPageCount() const {
    return ui->spinBoxPageCount->value();
}

float MainWindowClientBookEncoder::getPrice() const {
    return ui->doubleSpinBoxPrice->value();
}

int MainWindowClientBookEncoder::getPublishYear() const {
    return ui->spinBoxPublishYear->value();
}

int MainWindowClientBookEncoder::getStockQuantity() const {
    return ui->spinBoxStockQuantity->value();
}

void MainWindowClientBookEncoder::loginOk() {
    ui->pushButtonClear->setEnabled(true);
    ui->pushButtonAddBook->setEnabled(true);
    ui->pushButtonAddAuthor->setEnabled(true);
    ui->pushButtonAddSubject->setEnabled(true);
    ui->actionLogin->setEnabled(false);
    ui->actionLogout->setEnabled(true);
}

void MainWindowClientBookEncoder::logoutOk() {
    ui->pushButtonClear->setEnabled(false);
    ui->pushButtonAddBook->setEnabled(false);
    ui->pushButtonAddAuthor->setEnabled(false);
    ui->pushButtonAddSubject->setEnabled(false);
    ui->actionLogin->setEnabled(true);
    ui->actionLogout->setEnabled(false);
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions permettant d'afficher des boites de dialogue (ne pas modifier) ///////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::dialogMessage(const string& title,const string& message) {
   QMessageBox::information(this,QString::fromStdString(title),QString::fromStdString(message));
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::dialogError(const string& title,const string& message) {
   QMessageBox::critical(this,QString::fromStdString(title),QString::fromStdString(message));
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
string MainWindowClientBookEncoder::dialogInputText(const string& title,const string& question) {
    return QInputDialog::getText(this,QString::fromStdString(title),QString::fromStdString(question)).toStdString();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int MainWindowClientBookEncoder::dialogInputInt(const string& title,const string& question) {
    return QInputDialog::getInt(this,QString::fromStdString(title),QString::fromStdString(question));
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions gestion des boutons et items de menu (TO DO) /////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void MainWindowClientBookEncoder::on_pushButtonAddAuthor_clicked() {
    string lastName = this->dialogInputText("Nouvel auteur","Nom ?");
    if (lastName.empty()) return;
    string firstName = this->dialogInputText("Nouvel auteur","Prénom ?");
    if (firstName.empty()) return;
    string birthDate = this->dialogInputText("Nouvel auteur","Date de naissance (yyyy-mm-dd) ?");
    if (birthDate.empty()) return;

    cout << "Nom : " << lastName << endl;
    cout << "Prénom : " << firstName << endl;
    cout << "Date de naissance : " << birthDate << endl;

    char requete[200], reponse[10000];

    sprintf(requete,"ADD_AUTHOR#%s#%s",lastName.c_str(),firstName.c_str());

    Send(sClient,requete,strlen(requete));

    int nbLus = Receive(sClient,reponse);
    if (nbLus <= 0){
        return;
    }

    reponse[nbLus] = 0;

    printf("Reponse recue : %s\n",reponse);
    char *ptr = strtok(reponse,"#");
    ptr = strtok(NULL,"#");

    if (strcmp(ptr,"ok") == 0)
    {
        ptr = strtok(NULL,"#");
        this->dialogMessage("Auteur","Auteur ajouté avec ID : " + string(ptr));
        
        idAuthors[nbAuthors] = atoi(ptr);
        nbAuthors++;

        this->addComboBoxAuthors(firstName + " " + lastName);
    }
    else
    {
        ptr = strtok(NULL,"#");
        this->dialogError("Auteur","Erreur ajout auteur : " + string(ptr));
    }

}

void MainWindowClientBookEncoder::on_pushButtonAddSubject_clicked() {
    string name = this->dialogInputText("Nouveau sujet","Nom ?");
    if (name.empty()) return;
    cout << "Nom : " << name << endl;

    char requete[200], reponse[10000];

    sprintf(requete, "ADD_SUBJECT#%s", name.c_str());

    Send(sClient, requete, strlen(requete));

    int nbLus=Receive(sClient, reponse);

    if(nbLus<=0){
        return;
    }

    reponse[nbLus] = 0;

    printf("Reponse recue : %s\n",reponse);
    char *ptr=strtok(reponse, "#");
    ptr = strtok(NULL,"#");

    if(strcmp(ptr, "ok")==0){
        ptr=strtok(NULL,"#");
        this->dialogMessage("Sujet","Sujet ajouté avec ID: " + string(ptr));

        idSubjects[nbSubjects] = atoi(ptr);
        nbSubjects++;
        this->addComboBoxSubjects(name);
    }
    else
    {
        ptr = strtok(NULL,"#");
        this->dialogError("Sujet","Erreur ajout sujet: " + string(ptr));
    }


}

void MainWindowClientBookEncoder::on_pushButtonAddBook_clicked() {
    cout << "title = " << this->getTitle() << endl;
    cout << "Isbn = " << this->getIsbn() << endl;
    cout << "PageCount = " << this->getPageCount() << endl;
    cout << "Price = " << this->getPrice() << endl;
    cout << "PublishYear = " << this->getPublishYear() << endl;
    cout << "Stock = " << this->getStockQuantity() << endl;

    cout << "selection auteur = " << this->getSelectionAuthor() << endl;
    cout << "selection sujet  = " << this->getSelectionSubject() << endl;

    int indiceAuteur = ui->comboBoxAuthors->currentIndex();
    int indiceSujet = ui->comboBoxSubjects->currentIndex();

    if (indiceAuteur < 0 || indiceSujet < 0)
    {
        this->dialogError("Livre","Selectionnez un auteur et un sujet");
        return;
    }

    int authorId = idAuthors[indiceAuteur];
    int subjectId = idSubjects[indiceSujet];

    char requete[200], reponse[10000];

    if (this->getTitle().empty() || this->getIsbn().empty())
    {
        this->dialogError("Livre", "Remplissez le titre et l'ISBN");
        return;
    }

    sprintf(requete, "ADD_BOOK#%d#%d#%s#%s#%d#%d#%f#%d", authorId, subjectId,
        this->getTitle().c_str(), this->getIsbn().c_str(), this->getPageCount(),
        this->getStockQuantity(), this->getPrice(), this->getPublishYear());

    Send(sClient, requete, strlen(requete));

    int nbLus=Receive(sClient, reponse);

    if(nbLus<=0){
        return;
    }

    reponse[nbLus] = 0;

    printf("Reponse recue : %s\n",reponse);
    char *ptr=strtok(reponse, "#");
    ptr = strtok(NULL,"#");

    if(strcmp(ptr, "ok")==0){
        ptr=strtok(NULL,"#");
        int id = atoi(ptr);

        this->dialogMessage("Livre","Livre ajouté");
        this->addTupleTableBooks(id,
                                 this->getTitle(),
                                 this->getSelectionAuthor(),
                                 this->getSelectionSubject(),
                                 this->getIsbn(),
                                 this->getPageCount(),
                                 this->getPublishYear(),
                                 this->getPrice(),
                                 this->getStockQuantity());
    }
    else
    {
        ptr = strtok(NULL,"#");
        this->dialogError("Livre","Erreur ajout livre: " + string(ptr));
    }

}

void MainWindowClientBookEncoder::on_pushButtonClear_clicked() {
    ui->lineEditTitle->clear();
    ui->lineEditIsbn->clear();
    ui->spinBoxPageCount->setValue(0);
    ui->doubleSpinBoxPrice->setValue(0);
    ui->spinBoxPublishYear->setValue(0);
    ui->spinBoxStockQuantity->setValue(0);
}

void MainWindowClientBookEncoder::on_actionLogin_triggered() {
    string login = this->dialogInputText("Entrée en session","Login ?");
    if (login.empty()) return;
    string password = this->dialogInputText("Entrée en session","Password ?");
    if (password.empty()) return;

    sClient=ClientSocket((char*)"0.0.0.0", 5000);

    if(sClient==-1){
        this->dialogError("Erreur","Connexion au serveur impossible");
        return;
    }

    char requete[200], reponse[10000];
    sprintf(requete,"LOGIN#%s#%s",login.c_str(),password.c_str());

    Send(sClient,requete,strlen(requete));

    int nbLus = Receive(sClient,reponse);

    if (nbLus <= 0)
    {
        this->dialogError("Erreur","Erreur de reception");
        ::close(sClient);
        sClient = -1;
        return;
    }

    reponse[nbLus] = 0;

    // Parsing de la reponse
    char *ptr = strtok(reponse,"#");
    ptr = strtok(NULL,"#");

    if (strcmp(ptr,"ok") == 0)
    {
        this->loginOk();
        this->dialogMessage("Login","Login OK");

        // GET_AUTHORS
        sprintf(requete,"GET_AUTHORS");
        Send(sClient,requete,strlen(requete));

        nbLus = Receive(sClient,reponse);
        if (nbLus <= 0){
            return;
        } 

        reponse[nbLus] = 0;

        ptr = strtok(reponse,"#");
        ptr = strtok(NULL,"#");

        if (strcmp(ptr,"ok") == 0)
        {
            
            nbAuthors = 0;
            this->clearComboBoxAuthors();

            while ((ptr = strtok(NULL,"#")) != NULL)
            {
                int id = atoi(ptr);

                ptr = strtok(NULL,"#");
                string lastName = ptr;

                ptr = strtok(NULL,"#");
                string firstName = ptr;

                idAuthors[nbAuthors] = id;
                nbAuthors++;

                this->addComboBoxAuthors(firstName + " " + lastName);
            }
        }

        // GET_SUBJECTS
        sprintf(requete,"GET_SUBJECTS");
        Send(sClient,requete,strlen(requete));

        nbLus = Receive(sClient,reponse);
        if (nbLus <= 0){
           return; 
        } 

        reponse[nbLus] = 0;

        ptr = strtok(reponse,"#");
        ptr = strtok(NULL,"#");

        if (strcmp(ptr,"ok") == 0)
        {
            nbSubjects = 0;
            this->clearComboBoxSubjects();

            while ((ptr = strtok(NULL,"#")) != NULL)
            {
                int id = atoi(ptr);

                ptr = strtok(NULL,"#");
                string name = ptr;

                idSubjects[nbSubjects] = id;
                nbSubjects++;
                this->addComboBoxSubjects(name);
            }
        }
    }
    else
    {
        ptr = strtok(NULL,"#");
        this->dialogError("Login",ptr);
        ::close(sClient);
        sClient = -1;
    }
}

void MainWindowClientBookEncoder::on_actionLogout_triggered() {
    char requete[200], reponse[10000];

    sprintf(requete,"LOGOUT");

    Send(sClient,requete,strlen(requete));

    Receive(sClient,reponse);

    ::close(sClient);
    sClient = -1;

    this->logoutOk();
}

void MainWindowClientBookEncoder::on_actionQuitter_triggered(){
    QApplication::exit(0);
}
