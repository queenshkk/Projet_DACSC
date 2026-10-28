package hepl.faad.dao;

import hepl.faad.model.entity.Author;
import hepl.faad.model.dao.AuthorDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.AuthorSearchVM;

import java.time.LocalDate;
import java.util.ArrayList;

public class AuthorDAOTestU {
    public static void main(String[] args)
    {
        AuthorDAO dao = new AuthorDAO();

        AuthorSearchVM asvm = new AuthorSearchVM();
        asvm.setLastName("Nguyen");

        ArrayList<Author> auteurs = dao.load(asvm);

        if (auteurs.size() != 0)
        {
            Author auteur = auteurs.get(0);

            auteur.setFirstName("QT");

            dao.save(auteur);

            System.out.println("Modification effectuée.");

            asvm.setId(auteur.getId());
            asvm.setLastName(null);

            auteurs = dao.load(asvm);

            if (auteurs.size() != 0)
            {
                System.out.println("Nouveau prénom : " + auteurs.get(0).getFirstName());
            }
        }
        else
        {
            System.out.println("Auteur de test introuvable !");
        }

        ConnectDB.close();
    }
}
