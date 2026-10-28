package hepl.faad.dao;

import hepl.faad.model.entity.Book;
import hepl.faad.model.dao.BookDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.BookSearchVM;

import java.util.ArrayList;

public class BookDAOTestR {
    public static void main(String[] args)
    {
        BookDAO dao = new BookDAO();

        try
        {
            System.out.println("--- TOUS LES LIVRES ---");

            ArrayList<Book> livres = dao.load();

            for (Book livre : livres)
            {
                System.out.println(livre.getId() + " - " + livre.getTitle());
            }

            System.out.println("--- SELECTION PAR ID (1) ---");
            BookSearchVM bsvm = new BookSearchVM();
            bsvm.setId(1);

            livres = dao.load(bsvm);

            Book livre = dao.getById(1);

            if (livre != null)
            {
                System.out.println("Livre trouvé : " + livre.getTitle());
            }
            else
            {
                System.out.println("Livre introuvable.");
            }

            System.out.println("--- SELECTION PAR TITRE (Livre 1) ---");

            bsvm.setId(null);
            bsvm.setTitle("Livre 1");
            livres = dao.load(bsvm);

            for (Book b : livres)
            {
                System.out.println(b.getTitle());
            }

            System.out.println("--- SELECTION PAR PRIX MAX (30) ---");

            bsvm.setTitle(null);
            bsvm.setMaxPrice(30.0);

            livres = dao.load(bsvm);

            for (Book b : livres)
            {
                System.out.println(b.getTitle() + " : " + b.getPrice());
            }
        }
        finally
        {
            ConnectDB.close();
        }
    }
}
