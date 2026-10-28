package hepl.faad.dao;

import hepl.faad.model.entity.Caddy;
import hepl.faad.model.dao.CaddyDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.CaddySearchVM;

import java.time.LocalDate;
import java.util.ArrayList;

public class CaddyDAOTestC {
    public static void main(String[] args)
    {
        CaddyDAO dao = new CaddyDAO();

        Caddy panier = new Caddy(7, 2, LocalDate.now(), 0.0, false);

        dao.save(panier);

        System.out.println("Ajout --> ID : " + panier.getId());

        if (panier.getId() != null)
        {
            CaddySearchVM csvm = new CaddySearchVM();
            csvm.setId(panier.getId());

            System.out.println("--- SELECTION PAR ID ---");

            ArrayList<Caddy> paniers = dao.load(csvm);

            if (paniers.size() != 0)
            {
                System.out.println("Panier trouvé : " + paniers.get(0).getId());
            }
            else
            {
                System.out.println("Panier introuvable !");
            }
        }
        else
        {
            System.out.println("L'ajout a échoué !");
        }

        ConnectDB.close();
    }
}
