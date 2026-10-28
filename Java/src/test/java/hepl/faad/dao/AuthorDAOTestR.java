package hepl.faad.dao;

import hepl.faad.model.entity.Author;
import hepl.faad.model.dao.AuthorDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.AuthorSearchVM;

import java.util.ArrayList;

public class AuthorDAOTestR {
    public static void main(String[] args)
    {
        AuthorDAO dao = new AuthorDAO();

        try
        {
            System.out.println("--- TOUS LES AUTEURS ---");

            ArrayList<Author> auteurs = dao.load();

            for (Author auteur : auteurs)
            {
                System.out.println(auteur.getId() + " - " +
                        auteur.getLastName() + " " +
                        auteur.getFirstName() + " - " +
                        auteur.getBirthDate());
            }

            System.out.println("--- SELECTION PAR ID (1) ---");
            AuthorSearchVM asvm = new AuthorSearchVM();
            asvm.setId(1);

            auteurs = dao.load(asvm);

            Author auteur = dao.getById(1);

            if (auteur != null)
            {
                System.out.println("Auteur trouvé : " + auteur.getLastName());
            }
            else
            {
                System.out.println("Auteur introuvable.");
            }

            System.out.println("--- SELECTION PAR NOM (guillaumy) ---");

            asvm.setId(null);
            asvm.setLastName("guillaumy");
            auteurs = dao.load(asvm);

            for (Author a : auteurs)
            {
                System.out.println(a.getLastName() + " " + a.getFirstName());
            }

            System.out.println("--- SELECTION PAR PRENOM (ntema) ---");

            asvm.setLastName(null);
            asvm.setFirstName("ntema");

            auteurs = dao.load(asvm);

            for (Author a : auteurs)
            {
                System.out.println(a.getLastName() + " " + a.getFirstName());
            }
        }
        finally
        {
            ConnectDB.close();
        }
    }
}