package hepl.faad.dao;

import hepl.faad.model.entity.Book;
import hepl.faad.model.dao.BookDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.BookSearchVM;

import java.util.ArrayList;

public class BookDAOTestD {
    public static void main(String[] args)
    {
        BookDAO dao = new BookDAO();

        BookSearchVM bsvm = new BookSearchVM();
        bsvm.setTitle("Livre 5");

        ArrayList<Book> livres = dao.load(bsvm);
        System.out.println("Nombre de livres trouvés : " + livres.size());

        if (livres.size() !=0)
        {
            Book livre = livres.get(0);
            Integer id = livre.getId();

            System.out.println("ID du livre : " + livre.getId());

            dao.delete(livre);

            bsvm.setTitle(null);
            bsvm.setId(id);

            livres = dao.load(bsvm);

            if (livres.size() == 0)
            {
                System.out.println("Suppression réussie.");
            }
            else
            {
                System.out.println("Le livre existe toujours !");
            }
        }
        else
        {
            System.out.println("Aucun livre unique de test trouvé : suppression annulée.");
        }

        ConnectDB.close();
    }
}
