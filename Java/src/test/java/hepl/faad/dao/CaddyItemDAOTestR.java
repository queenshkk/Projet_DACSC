package hepl.faad.dao;

import hepl.faad.model.entity.CaddyItem;
import hepl.faad.model.dao.CaddyItemDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.CaddyItemSearchVM;

import java.util.ArrayList;

public class CaddyItemDAOTestR {
    public static void main(String[] args)
    {
        CaddyItemDAO dao = new CaddyItemDAO();

        try
        {
            System.out.println("--- TOUTES LES LIGNES ---");

            ArrayList<CaddyItem> items = dao.load();

            for (CaddyItem item : items)
            {
                System.out.println(item.getId() + " - Panier : " +
                        item.getCaddyId() + " - Livre : " +
                        item.getBookId() + " - Quantité : " +
                        item.getQuantity());
            }

            System.out.println("--- SELECTION PAR PANIER (7) ---");

            CaddyItemSearchVM cisvm = new CaddyItemSearchVM();
            cisvm.setCaddyId(7);

            items = dao.load(cisvm);

            for (CaddyItem item : items)
            {
                System.out.println(item.getBookId() + " - " + item.getQuantity());
            }

            System.out.println("--- SELECTION PAR LIVRE (1) ---");

            cisvm.setCaddyId(null);
            cisvm.setBookId(1);

            items = dao.load(cisvm);

            for (CaddyItem item : items)
            {
                System.out.println(item.getId() + " - " + item.getQuantity());
            }
        }
        finally
        {
            ConnectDB.close();
        }
    }
}
