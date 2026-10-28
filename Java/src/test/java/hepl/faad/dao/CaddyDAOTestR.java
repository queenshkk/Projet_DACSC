package hepl.faad.dao;

import hepl.faad.model.entity.Caddy;
import hepl.faad.model.dao.CaddyDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.CaddySearchVM;

import java.util.ArrayList;

public class CaddyDAOTestR {
    public static void main(String[] args)
    {
        CaddyDAO dao = new CaddyDAO();

        try
        {
            System.out.println("--- TOUS LES PANIERS ---");

            ArrayList<Caddy> paniers = dao.load();

            for (Caddy panier : paniers)
            {
                System.out.println(panier.getId() + " - Client : " +
                        panier.getClientId() + " - Montant : " +
                        panier.getAmount());
            }

            System.out.println("--- SELECTION PAR CLIENT (2) ---");

            CaddySearchVM csvm = new CaddySearchVM();
            csvm.setClientId(2);

            paniers = dao.load(csvm);

            for (Caddy panier : paniers)
            {
                System.out.println(panier.getId() + " - Payé : " + panier.isPayed());
            }

            System.out.println("--- SELECTION NON PAYES ---");

            csvm.setClientId(null);
            csvm.setPayed(false);

            paniers = dao.load(csvm);

            for (Caddy panier : paniers)
            {
                System.out.println(panier.getId());
            }
        }
        finally
        {
            ConnectDB.close();
        }
    }
}
