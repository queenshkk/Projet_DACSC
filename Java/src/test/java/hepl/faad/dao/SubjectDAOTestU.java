package hepl.faad.dao;

import hepl.faad.model.entity.Subject;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.dao.SubjectDAO;
import hepl.faad.model.viewmodel.SubjectSearchVM;

import java.util.ArrayList;

public class SubjectDAOTestU {
    public static void main(String[] args)
    {
        SubjectDAO dao = new SubjectDAO();

        SubjectSearchVM ssvm = new SubjectSearchVM();
        ssvm.setName("sujet 1");

        ArrayList<Subject> sujets = dao.load(ssvm);

        if (sujets.size() != 0)
        {
            Subject sujet = sujets.get(0);

            sujet.setName("sujet 2");

            dao.save(sujet);

            System.out.println("Modification effectuée.");

            ssvm.setId(sujet.getId());
            ssvm.setName(null);

            sujets = dao.load(ssvm);

            if (sujets.size() != 0)
            {
                System.out.println("Nouveau nom : " + sujets.get(0).getName());
            }
        }
        else
        {
            System.out.println("Sujet de test introuvable !");
        }

        ConnectDB.close();
    }
}
