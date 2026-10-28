package hepl.faad.dao;

import hepl.faad.model.entity.Book;
import hepl.faad.model.dao.BookDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.BookSearchVM;

import java.util.ArrayList;

public class BookDAOTestC {
    public static void main(String[] args)
    {
        BookDAO dao = new BookDAO();

        Book livre = new Book(1, 1, "Livre test JDBC", "9781234567890", 200, 2026, 19.99, 10);
        livre.setId(null);

        dao.save(livre);

        System.out.println("Ajout réussi --> ID : " + livre.getId());

        BookSearchVM bsvm = new BookSearchVM();
        bsvm.setId(livre.getId());

        System.out.println("--- SELECTION PAR ID ---");
        ArrayList<Book> livres = dao.load(bsvm);

        if (livres.size() != 0)
        {
            System.out.println("Livre trouvé : " + livres.get(0).getTitle());
        }
        else
        {
            System.out.println("Livre introuvable !");
        }

        ConnectDB.close();
    }
}
