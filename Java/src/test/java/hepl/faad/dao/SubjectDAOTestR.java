package hepl.faad.dao;

import hepl.faad.model.entity.Subject;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.dao.SubjectDAO;
import hepl.faad.model.viewmodel.SubjectSearchVM;

import java.util.ArrayList;

public class SubjectDAOTestR {
    public static void main(String[] args)
    {
        SubjectDAO dao = new SubjectDAO();

        try
        {
            System.out.println("--- TOUS LES SUJETS ---");

            ArrayList<Subject> sujets = dao.load();

            for (Subject sujet : sujets)
            {
                System.out.println(sujet.getId() + " - " + sujet.getName());
            }

            System.out.println("--- SELECTION PAR ID (1) ---");
            SubjectSearchVM ssvm = new SubjectSearchVM();
            ssvm.setId(1);

            sujets = dao.load(ssvm);

            Subject sujet = dao.getById(1);

            if (sujet != null)
            {
                System.out.println("Sujet trouvé : " + sujet.getName());
            }
            else
            {
                System.out.println("Sujet introuvable.");
            }

            System.out.println("--- SELECTION PAR NOM (sujet 5) ---");

            ssvm.setId(null);
            ssvm.setName("sujet 5");

            sujets = dao.load(ssvm);

            for (Subject s : sujets)
            {
                System.out.println(s.getName());
            }
        }
        finally
        {
            ConnectDB.close();
        }
    }
}
