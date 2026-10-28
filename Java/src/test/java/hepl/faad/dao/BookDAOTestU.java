package hepl.faad.dao;

import hepl.faad.model.entity.Book;
import hepl.faad.model.dao.BookDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.BookSearchVM;

import java.util.ArrayList;

public class BookDAOTestU {
    public static void main(String[] args)
    {
        BookDAO dao = new BookDAO();

        BookSearchVM bsvm = new BookSearchVM();
        bsvm.setTitle("Livre test");

        ArrayList<Book> livres = dao.load(bsvm);

        if (livres.size() != 0)
        {
            Book livre = livres.get(0);

            livre.setPrice(24.99);

            dao.save(livre);

            System.out.println("Modification effectuée.");


            bsvm.setId(livre.getId());
            bsvm.setTitle(null);

            livres = dao.load(bsvm);

            if (livres.size() != 0)
            {
                System.out.println("Nouveau prix : " + livres.get(0).getPrice());
            }
        }
        else
        {
            System.out.println("Livre de test introuvable !");
        }

        ConnectDB.close();
    }
}
