package hepl.faad.dao;

import hepl.faad.model.entity.Author;
import hepl.faad.model.dao.AuthorDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.AuthorSearchVM;

import java.util.ArrayList;

public class AuthorDAOTestD {
    public static void main(String[] args)
    {
        AuthorDAO dao = new AuthorDAO();

        AuthorSearchVM asvm = new AuthorSearchVM();
        asvm.setLastName("Nguyen");

        ArrayList<Author> auteurs = dao.load(asvm);
        System.out.println("Nombre d'auteurs trouvés : " + auteurs.size());

        if (auteurs.size() != 0)
        {
            Author auteur = auteurs.get(0);
            Integer id = auteur.getId();

            System.out.println("ID de l'auteur : " + auteur.getId());

            dao.delete(auteur);

            asvm.setLastName(null);
            asvm.setId(id);

            auteurs = dao.load(asvm);

            if (auteurs.size() == 0)
            {
                System.out.println("Suppression réussie.");
            }
            else
            {
                System.out.println("L'auteur existe toujours !");
            }
        }
        else
        {
            System.out.println("Aucun auteur de test trouvé : suppression annulée.");
        }

        ConnectDB.close();
    }
}
