package hepl.faad.dao;

import hepl.faad.model.entity.Subject;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.dao.SubjectDAO;

public class SubjectDAOTestC {
    public static void main(String[] args)
    {
        SubjectDAO dao = new SubjectDAO();

        Subject sujet = new Subject(null, "Sujet Test");

        dao.save(sujet);

        System.out.println("Sujet ajouté, ID = " + sujet.getId());

        ConnectDB.close();
    }
}
