package hepl.faad.dao;

import hepl.faad.model.dao.ConnectDB;

import java.sql.Connection;

public class ConnectDBTest {
    public static void main(String[] args)
    {
        ConnectDB db = new ConnectDB();

        Connection con = db.getConn();

        if (con != null)
        {
            System.out.println("Connexion à MySQL réussie.");
            ConnectDB.close();
        }
        else
        {
            System.out.println("Connexion à MySQL échouée.");
        }
    }
}
