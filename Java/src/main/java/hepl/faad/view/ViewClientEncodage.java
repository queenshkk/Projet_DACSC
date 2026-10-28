package hepl.faad.view;

import hepl.faad.controller.ClientEncodageController;
import hepl.faad.model.entity.Author;
import hepl.faad.model.entity.Book;
import hepl.faad.model.entity.Subject;

import java.util.ArrayList;

public interface ViewClientEncodage {
    String getLogin();
    String getPassword();

    String getAuthorLastName();
    String getAuthorFirstName();
    String getSubjectName();

    Book getNewBook();

    void displayAuthors(ArrayList<Author> auteurs);
    void displaySubjects(ArrayList<Subject> sujets);
    void addDisplayedBook(Book livre);

    void clearSession();
    void clearFields();
    void setLoggedIn(boolean connected);

    void showMessage(String message);
    void showErrorMessage(String message);

    void closeWindow();

    void setController(ClientEncodageController controller);

}
