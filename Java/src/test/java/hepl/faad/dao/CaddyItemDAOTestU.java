package hepl.faad.dao;

import hepl.faad.model.entity.CaddyItem;
import hepl.faad.model.dao.CaddyItemDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.CaddyItemSearchVM;

import java.util.ArrayList;

public class CaddyItemDAOTestU {
    public static void main(String[] args)
    {
        CaddyItemDAO dao = new CaddyItemDAO();

        int caddyId = 1; // Même ID que dans CaddyItemDAOTestC

        CaddyItemSearchVM cisvm = new CaddyItemSearchVM();
        cisvm.setCaddyId(caddyId);
        cisvm.setBookId(1);

        ArrayList<CaddyItem> items = dao.load(cisvm);

        if (items.size() != 0)
        {
            CaddyItem item = items.get(0);

            item.setQuantity(3);

            dao.save(item);

            System.out.println("Modification effectuée.");

            cisvm.setId(item.getId());
            cisvm.setCaddyId(null);
            cisvm.setBookId(null);

            items = dao.load(cisvm);

            if (items.size() != 0)
            {
                System.out.println("Nouvelle quantité : " + items.get(0).getQuantity());
            }
        }
        else
        {
            System.out.println("Ligne de test introuvable !");
        }

        ConnectDB.close();
    }
}
