package hepl.faad.dao;

import hepl.faad.model.entity.Subject;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.dao.SubjectDAO;
import hepl.faad.model.viewmodel.SubjectSearchVM;

import java.util.ArrayList;

public class SubjectDAOTestD {
    public static void main(String[] args)
    {
        SubjectDAO dao = new SubjectDAO();

        SubjectSearchVM ssvm = new SubjectSearchVM();
        ssvm.setName("Sujet 1");

        ArrayList<Subject> sujets = dao.load(ssvm);
        System.out.println("Nombre de sujets trouvés : " + sujets.size());

        if (sujets.size() != 0)
        {
            Subject sujet = sujets.get(0);
            Integer id = sujet.getId();

            System.out.println("ID du sujet : " + sujet.getId());

            dao.delete(sujet);

            ssvm.setName(null);
            ssvm.setId(id);

            sujets = dao.load(ssvm);

            if (sujets.size() == 0)
            {
                System.out.println("Suppression réussie.");
            }
            else
            {
                System.out.println("Le sujet existe toujours !");
            }
        }
        else
        {
            System.out.println("Aucun sujet de test trouvé : suppression annulée.");
        }

        ConnectDB.close();
    }
}
