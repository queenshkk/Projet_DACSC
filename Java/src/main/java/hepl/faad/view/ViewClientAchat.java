package hepl.faad.view;

import hepl.faad.model.entity.Book;

import java.awt.event.ActionListener;
import java.util.ArrayList;

public interface ViewClientAchat {
    String getLastName();
    String getFirstName();
    boolean isNewClient();
    void setClientId(Integer id);

    String getTitle();
    String getAuthor();
    String getSubject();
    String getMaxPrice();

    void afficherLivres(ArrayList<Book> livres);

    Integer getSelectedBookId();
    String getSelectedBookTitle();
    double getSelectedBookPrice();
    int getQuantity();

    void addCaddy(Integer bookId, String titre, int quantity, double price);

    Integer getSelectedCaddyBookId();
    int getSelectedCaddyQuantity();
    void deleteCaddy(Integer bookId, int quantite);
    void clearCaddy();
    void resetClient();

    void setController(ActionListener controller);
    void showMessage(String message);
    void showErrorMessage(String message);
}
