package hepl.faad.dao;

import hepl.faad.model.entity.CaddyItem;
import hepl.faad.model.dao.CaddyItemDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.CaddyItemSearchVM;

import java.util.ArrayList;

public class CaddyItemDAOTestC {
    public static void main(String[] args)
    {
        CaddyItemDAO dao = new CaddyItemDAO();

        int caddyId = 7; // À remplacer par l'ID affiché lors du test CaddyDAOTestC

        CaddyItem item = new CaddyItem(null, caddyId, 1, 2);

        dao.save(item);

        System.out.println("Ajout --> ID : " + item.getId());

        if (item.getId() != null)
        {
            CaddyItemSearchVM cisvm = new CaddyItemSearchVM();
            cisvm.setId(item.getId());

            System.out.println("--- SELECTION PAR ID ---");

            ArrayList<CaddyItem> items = dao.load(cisvm);

            if (items.size() != 0)
            {
                System.out.println("Ligne trouvée : " + items.get(0).getId());
            }
            else
            {
                System.out.println("Ligne introuvable !");
            }
        }
        else
        {
            System.out.println("L'ajout a échoué !");
        }

        ConnectDB.close();
    }
}
