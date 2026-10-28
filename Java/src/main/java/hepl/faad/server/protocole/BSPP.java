package hepl.faad.server.protocole;

import hepl.faad.model.dao.*;
import hepl.faad.model.entity.Book;
import hepl.faad.model.entity.Caddy;
import hepl.faad.model.entity.CaddyItem;
import hepl.faad.model.entity.Client;
import hepl.faad.model.viewmodel.CaddyItemSearchVM;
import hepl.faad.model.viewmodel.ClientSearchVM;
import hepl.faad.server.Logger;
import hepl.faad.server.reponses.*;
import hepl.faad.server.requetes.*;

import java.net.Socket;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BSPP implements Protocole {
    private Logger logger;
    private BookDAO bookDAO;
    private CaddyDAO caddyDAO;
    private CaddyItemDAO caddyItemDAO;
    private ClientDAO clientDAO;

    public BSPP(Logger logger)
    {
        this.logger = logger;
        bookDAO = new BookDAO();
        caddyDAO = new CaddyDAO();
        caddyItemDAO = new CaddyItemDAO();
        clientDAO = new ClientDAO();
    }

    @Override
    public String getNom()
    {
        return "BSPP";
    }

    @Override
    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException
    {
        if (requete instanceof RequeteCLIENT)
        {
            return TraiteRequeteCLIENT((RequeteCLIENT) requete);
        }

        if (requete instanceof RequeteSELECT_BOOK){
            return TraiteRequeteSELECT_BOOK((RequeteSELECT_BOOK) requete);
        }

        if (requete instanceof RequeteADD_CADDY_ITEM){
            return TraiteRequeteADD_CADDY_ITEM((RequeteADD_CADDY_ITEM) requete);
        }

        if (requete instanceof RequeteDEL_CADDY_ITEM)
        {
            return TraiteRequeteDEL_CADDY_ITEM((RequeteDEL_CADDY_ITEM) requete);
        }

        if (requete instanceof RequeteCANCEL_CADDY)
        {
            return TraiteRequeteCANCEL_CADDY((RequeteCANCEL_CADDY) requete);
        }

        if (requete instanceof RequetePAY_CADDY)
        {
            return TraiteRequetePAY_CADDY((RequetePAY_CADDY) requete);
        }

        return null;
    }

    private ReponseSELECT_BOOK TraiteRequeteSELECT_BOOK(RequeteSELECT_BOOK requete)
    {
        logger.Trace("RequeteSELECT_BOOK reçue");

        ArrayList<Book> livres = bookDAO.load(requete.getCriteres());

        return new ReponseSELECT_BOOK(livres);
    }


    private ReponseADD_CADDY_ITEM TraiteRequeteADD_CADDY_ITEM(RequeteADD_CADDY_ITEM requete)
    {
        logger.Trace("RequeteADD_CADDY_ITEM reçue");

        Integer clientId = requete.getClientId();
        Integer bookId = requete.getBookId();
        int quantite = requete.getQuantity();

        if (clientId == null || bookId == null || quantite <= 0)
        {
            return new ReponseADD_CADDY_ITEM(false);
        }


        ConnectDB connectDB = new ConnectDB();
        Connection conn = connectDB.getConn();

        try
        {
            if (conn == null)
            {
                logger.Trace("Connexion à la BD impossible");
                return new ReponseADD_CADDY_ITEM(false);
            }

            String sql = "SELECT price FROM books WHERE id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next())
            {
                rs.close();
                pstmt.close();
                throw new SQLException("Livre introuvable");
            }

            double prix = rs.getDouble("price");

            rs.close();
            pstmt.close();

            if (!bookDAO.diminuerStock(conn, bookId, quantite))
            {
                logger.Trace("Stock insuffisant");
                return new ReponseADD_CADDY_ITEM(false);
            }

            Integer idCaddy = caddyDAO.getCaddyNonPaye(conn, clientId);

            if (idCaddy == null)
            {
                Caddy caddy = new Caddy(null, clientId, LocalDate.now(), 0.0, false
                );

                caddyDAO.ajouterCaddy(conn, caddy);
                idCaddy = caddy.getId();

                if (idCaddy == null)
                {
                    throw new SQLException("Création du panier impossible");
                }
            }

            Integer idItem = caddyItemDAO.getCaddyItemId(conn, idCaddy, bookId);

            if (idItem == null)
            {
                CaddyItem item = new CaddyItem(null, idCaddy, bookId, quantite
                );

                caddyItemDAO.ajouterCaddyItem(conn, item);
            }
            else
            {
                caddyItemDAO.augmenterQuantite(conn, idItem, quantite);
            }

            caddyDAO.augmenterMontant(conn, idCaddy, prix * quantite);

            logger.Trace("Livre ajouté au panier");

            return new ReponseADD_CADDY_ITEM(true);
        }
        catch (SQLException ex)
        {
            logger.Trace("Erreur ADD_CADDY_ITEM : " + ex.getMessage());
            return new ReponseADD_CADDY_ITEM(false);
        }
    }

    private ReponseCLIENT TraiteRequeteCLIENT(RequeteCLIENT requete)
    {
        logger.Trace("RequeteCLIENT reçue");

        String nom = requete.getLastName();
        String prenom = requete.getFirstName();
        boolean nouveau = requete.isNouveau();

        if (nom == null || prenom == null)
        {
            return new ReponseCLIENT(null);
        }

        if (nouveau)
        {
            Client client = new Client(null, nom, prenom, "");

            clientDAO.save(client);

            return new ReponseCLIENT(client.getId());
        }
        else
        {
            ClientSearchVM csvm = new ClientSearchVM();

            csvm.setLastName(nom);
            csvm.setFirstName(prenom);

            ArrayList<Client> clients = clientDAO.load(csvm);

            for (Client client : clients)
            {
                if (client.getLastName().equals(nom) &&
                        client.getFirstName().equals(prenom))
                {
                    return new ReponseCLIENT(client.getId());
                }
            }

            return new ReponseCLIENT(null);
        }
    }

    private ReponseDEL_CADDY_ITEM TraiteRequeteDEL_CADDY_ITEM(RequeteDEL_CADDY_ITEM requete)
    {
        logger.Trace("RequeteDEL_CADDY_ITEM reçue");

        Integer clientId = requete.getClientId();
        Integer bookId = requete.getBookId();
        int quantite = requete.getQuantity();

        if (clientId == null || bookId == null || quantite <= 0)
        {
            return new ReponseDEL_CADDY_ITEM(false);
        }

        ConnectDB connectDB = new ConnectDB();
        Connection conn = connectDB.getConn();

        try
        {
            if (conn == null)
            {
                logger.Trace("Connexion à la BD impossible");
                return new ReponseDEL_CADDY_ITEM(false);
            }

            Integer idCaddy = caddyDAO.getCaddyNonPaye(conn, clientId);

            if (idCaddy == null)
            {
                logger.Trace("Panier introuvable");
                return new ReponseDEL_CADDY_ITEM(false);
            }

            Integer idItem = caddyItemDAO.getCaddyItemId(conn, idCaddy, bookId);

            if (idItem == null)
            {
                logger.Trace("Livre absent du panier");
                return new ReponseDEL_CADDY_ITEM(false);
            }

            int quantitePanier = caddyItemDAO.getQuantite(conn, idItem);

            if (quantite > quantitePanier)
            {
                logger.Trace("Quantité demandée trop grande");
                return new ReponseDEL_CADDY_ITEM(false);
            }

            String sql = "SELECT price FROM books WHERE id = ?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next())
            {
                rs.close();
                pstmt.close();
                throw new SQLException("Livre introuvable");
            }

            double prix = rs.getDouble("price");

            rs.close();
            pstmt.close();

            if (quantite == quantitePanier)
            {
                caddyItemDAO.supprimerCaddyItem(conn, idItem);
            }
            else
            {
                caddyItemDAO.diminuerQuantite(conn, idItem, quantite);
            }

            bookDAO.augmenterStock(conn, bookId, quantite);

            caddyDAO.diminuerMontant(conn, idCaddy, prix * quantite);

            logger.Trace("Livre retiré du panier");

            return new ReponseDEL_CADDY_ITEM(true);
        }
        catch (SQLException ex)
        {
            logger.Trace("Erreur DEL_CADDY_ITEM : " + ex.getMessage());
            return new ReponseDEL_CADDY_ITEM(false);
        }
    }

    private ReponseCANCEL_CADDY TraiteRequeteCANCEL_CADDY(RequeteCANCEL_CADDY requete)
    {
        logger.Trace("RequeteCANCEL_CADDY reçue");

        Integer clientId = requete.getClientId();

        if (clientId == null)
        {
            return new ReponseCANCEL_CADDY(false);
        }

        ConnectDB connectDB = new ConnectDB();
        Connection conn = connectDB.getConn();

        try
        {
            if (conn == null)
            {
                logger.Trace("Connexion à la BD impossible");
                return new ReponseCANCEL_CADDY(false);
            }

            Integer idCaddy = caddyDAO.getCaddyNonPaye(conn, clientId);

            if (idCaddy == null)
            {
                logger.Trace("Panier introuvable");
                return new ReponseCANCEL_CADDY(false);
            }

            CaddyItemSearchVM cisvm = new CaddyItemSearchVM();
            cisvm.setCaddyId(idCaddy);

            ArrayList<CaddyItem> items = caddyItemDAO.load(cisvm);

            for (CaddyItem item : items)
            {
                bookDAO.augmenterStock(conn, item.getBookId(), item.getQuantity());

                caddyItemDAO.supprimerCaddyItem(conn, item.getId());
            }

            caddyDAO.supprimerCaddy(conn, idCaddy);

            logger.Trace("Panier annulé");

            return new ReponseCANCEL_CADDY(true);
        }
        catch (SQLException ex)
        {
            logger.Trace("Erreur CANCEL_CADDY : " + ex.getMessage());
            return new ReponseCANCEL_CADDY(false);
        }
    }

    private ReponsePAY_CADDY TraiteRequetePAY_CADDY(RequetePAY_CADDY requete)
    {
        logger.Trace("RequetePAY_CADDY reçue");

        Integer clientId = requete.getClientId();

        if (clientId == null)
        {
            return new ReponsePAY_CADDY(false);
        }

        ConnectDB connectDB = new ConnectDB();
        Connection conn = connectDB.getConn();

        try
        {
            if (conn == null)
            {
                logger.Trace("Connexion à la BD impossible");
                return new ReponsePAY_CADDY(false);
            }

            Integer idCaddy = caddyDAO.getCaddyNonPaye(conn, clientId);

            if (idCaddy == null)
            {
                logger.Trace("Panier introuvable");
                return new ReponsePAY_CADDY(false);
            }

            caddyDAO.payerCaddy(conn, idCaddy);

            logger.Trace("Panier payé");

            return new ReponsePAY_CADDY(true);
        }
        catch (SQLException ex)
        {
            logger.Trace("Erreur PAY_CADDY : " + ex.getMessage());
            return new ReponsePAY_CADDY(false);
        }
    }


}

