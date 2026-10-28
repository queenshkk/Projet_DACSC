package hepl.faad.dao;

import hepl.faad.model.entity.Author;
import hepl.faad.model.dao.AuthorDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.AuthorSearchVM;

import java.time.LocalDate;
import java.util.ArrayList;

public class AuthorDAOTestC {
    public static void main(String[] args)
    {
        AuthorDAO dao = new AuthorDAO();

        Author auteur = new Author(null, "Nguyen", "Quynh");
        auteur.setBirthDate(LocalDate.of(1999, 9, 23));

        dao.save(auteur);
        auteur.getId();
        System.out.println("Ajout réussi --> ID : " + auteur.getId());

        AuthorSearchVM asvm = new AuthorSearchVM();
        asvm.setId(auteur.getId());

        System.out.println("--- SELECTION PAR ID ---");
        ArrayList<Author> auteurs = dao.load(asvm);

        if (auteurs.size() != 0)
        {
            System.out.println("Auteur trouvé : " +
                    auteurs.get(0).getLastName() + " " +
                    auteurs.get(0).getFirstName());
        }
        else
        {
            System.out.println("Auteur introuvable !");
        }

        ConnectDB.close();
    }
}
