package hepl.faad.dao;

import hepl.faad.model.entity.CaddyItem;
import hepl.faad.model.dao.CaddyItemDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.CaddyItemSearchVM;

import java.util.ArrayList;

public class CaddyItemDAOTestD {
    public static void main(String[] args)
    {
        CaddyItemDAO dao = new CaddyItemDAO();

        int caddyId = 1; // Même ID que dans CaddyItemDAOTestC

        CaddyItemSearchVM cisvm = new CaddyItemSearchVM();
        cisvm.setCaddyId(caddyId);
        cisvm.setBookId(1);

        ArrayList<CaddyItem> items = dao.load(cisvm);

        System.out.println("Nombre de lignes trouvées : " + items.size());

        if (items.size() != 0)
        {
            CaddyItem item = items.get(0);
            Integer id = item.getId();

            System.out.println("ID de la ligne : " + id);

            dao.delete(item);

            cisvm.setCaddyId(null);
            cisvm.setBookId(null);
            cisvm.setId(id);

            items = dao.load(cisvm);

            if (items.size() == 0)
            {
                System.out.println("Suppression réussie.");
            }
            else
            {
                System.out.println("La ligne existe toujours !");
            }
        }
        else
        {
            System.out.println("Aucune ligne de test trouvée.");
        }

        ConnectDB.close();
    }
}
