package hepl.faad.dao;

import hepl.faad.model.dao.CaddyDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.entity.Caddy;
import hepl.faad.model.viewmodel.CaddySearchVM;

import java.time.LocalDate;
import java.util.ArrayList;

public class CaddyDAOTestU {
    public static void main(String[] args) {
        CaddyDAO dao = new CaddyDAO();

        CaddySearchVM csvm = new CaddySearchVM();
        csvm.setId(7);

        ArrayList<Caddy> paniers = dao.load(csvm);

        if (paniers.size() != 0) {
            Caddy panier = paniers.get(0);

            panier.setAmount(50.0);

            dao.save(panier);

            System.out.println("Modification effectuée.");

            csvm.setId(panier.getId());

            paniers = dao.load(csvm);

            if (paniers.size() != 0) {
                System.out.println("Nouveau montant : " + paniers.get(0).getAmount());
            }
        } else {
            System.out.println("Panier de test introuvable !");
        }

        ConnectDB.close();
    }
}
