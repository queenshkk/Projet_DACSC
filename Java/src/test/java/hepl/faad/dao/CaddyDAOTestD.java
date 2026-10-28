package hepl.faad.dao;

import hepl.faad.model.entity.Caddy;
import hepl.faad.model.dao.CaddyDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.CaddySearchVM;

import java.util.ArrayList;

public class CaddyDAOTestD {
    public static void main(String[] args)
    {
        CaddyDAO dao = new CaddyDAO();

        CaddySearchVM csvm = new CaddySearchVM();
        csvm.setClientId(1);
        csvm.setPayed(false);

        ArrayList<Caddy> paniers = dao.load(csvm);

        System.out.println("Nombre de paniers trouvés : " + paniers.size());

        if (paniers.size() != 0)
        {
            Caddy panier = paniers.get(0);
            Integer id = panier.getId();

            System.out.println("ID du panier : " + id);

            dao.delete(panier);

            csvm.setClientId(null);
            csvm.setPayed(null);
            csvm.setId(id);

            paniers = dao.load(csvm);

            if (paniers.size() == 0)
            {
                System.out.println("Suppression réussie.");
            }
            else
            {
                System.out.println("Le panier existe toujours !");
            }
        }
        else
        {
            System.out.println("Aucun panier de test trouvé.");
        }

        ConnectDB.close();
    }
}
