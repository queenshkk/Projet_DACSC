package hepl.faad.model.dao;

import hepl.faad.model.entity.Caddy;
import hepl.faad.model.viewmodel.CaddySearchVM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CaddyDAO {
    private ConnectDB connectDB;
    private ArrayList<Caddy> caddies;

    public CaddyDAO()
    {
        connectDB = new ConnectDB();
        caddies = new ArrayList<>();
    }

    public ArrayList<Caddy> getList()
    {
        return caddies;
    }

    public Caddy getById(Integer id)
    {
        for (Caddy caddy : caddies)
        {
            if (Objects.equals(caddy.getId(), id))
            {
                return caddy;
            }
        }

        return null;
    }

    public ArrayList<Caddy> load()
    {
        return this.load(null);
    }

    public ArrayList<Caddy> load(CaddySearchVM csvm)
    {
        try
        {
            String sql = "SELECT * FROM caddies ";

            if (csvm != null)
            {
                String where = " WHERE 1=1 ";

                if (csvm.getId() != null)
                    where += "AND id = ? ";

                if (csvm.getClientId() != null)
                    where += "AND clientId = ? ";

                if (csvm.getPayed() != null)
                    where += "AND payed = ? ";

                sql += where;
            }

            PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql);

            if (csvm != null)
            {
                int paramNumber = 0;

                if (csvm.getId() != null)
                {
                    paramNumber++;
                    pstmt.setInt(paramNumber, csvm.getId());
                }

                if (csvm.getClientId() != null)
                {
                    paramNumber++;
                    pstmt.setInt(paramNumber, csvm.getClientId());
                }

                if (csvm.getPayed() != null)
                {
                    paramNumber++;
                    pstmt.setBoolean(paramNumber, csvm.getPayed());
                }
            }

            ResultSet rs = pstmt.executeQuery();
            caddies.clear();

            while (rs.next())
            {
                int id = rs.getInt("id");
                int clientId = rs.getInt("clientId");
                java.sql.Date dateSql = rs.getDate("date");
                double amount = rs.getDouble("amount");
                boolean payed = rs.getBoolean("payed");

                Caddy caddy = new Caddy(id, clientId, dateSql.toLocalDate(), amount, payed);

                caddies.add(caddy);
            }
            pstmt.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CaddyDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            return caddies;
        }
    }

    public void save(Caddy caddy)
    {
        try
        {
            String sql;

            if (caddy != null)
            {
                if (caddy.getId() != null)
                {
                    sql = "UPDATE caddies SET " +
                            "clientId = ?, " +
                            "date = ?, " +
                            "amount = ?, " +
                            "payed = ? " +
                            "WHERE id = ?";

                    PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql);

                    pstmt.setInt(1, caddy.getClientId());
                    pstmt.setDate(2, java.sql.Date.valueOf(caddy.getDate()));
                    pstmt.setDouble(3, caddy.getAmount());
                    pstmt.setBoolean(4, caddy.isPayed());
                    pstmt.setInt(5, caddy.getId());

                    pstmt.executeUpdate();
                    pstmt.close();
                }
                else
                {
                    sql = "INSERT INTO caddies " +
                            "(clientId, date, amount, payed) " +
                            "VALUES (?, ?, ?, ?)";

                    PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                    pstmt.setInt(1, caddy.getClientId());
                    pstmt.setDate(2, java.sql.Date.valueOf(caddy.getDate()));
                    pstmt.setDouble(3, caddy.getAmount());
                    pstmt.setBoolean(4, caddy.isPayed());

                    pstmt.executeUpdate();

                    ResultSet rs = pstmt.getGeneratedKeys();

                    rs.next();
                    caddy.setId(rs.getInt(1));

                    rs.close();
                    pstmt.close();
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CaddyDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(Caddy caddy)
    {
        if (caddy != null && caddy.getId() != null)
        {
            this.delete(caddy.getId());
        }
    }

    public void delete(Integer id)
    {
        if (id != null)
        {
            try
            {
                String sql = "DELETE FROM caddies WHERE id = ?";

                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

                stmt.setInt(1, id);

                stmt.executeUpdate();

                stmt.close();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(CaddyDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void ajouterCaddy(Connection conn, Caddy caddy) throws SQLException
    {
        String sql = "INSERT INTO caddies (clientId, date, amount, payed) " +
                "VALUES (?, ?, ?, ?)";

        PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        pstmt.setInt(1, caddy.getClientId());
        pstmt.setDate(2, java.sql.Date.valueOf(caddy.getDate()));
        pstmt.setDouble(3, caddy.getAmount());
        pstmt.setBoolean(4, caddy.isPayed());

        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();

        rs.next();
        caddy.setId(rs.getInt(1));

        rs.close();
        pstmt.close();
    }


    public Integer getCaddyNonPaye(Connection conn, Integer clientId) throws SQLException
    {
        String sql = "SELECT id FROM caddies " +
                "WHERE clientId = ? AND payed = false " +
                "ORDER BY id DESC LIMIT 1";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, clientId);

        ResultSet rs = pstmt.executeQuery();

        Integer idCaddy = null;

        if(rs.next()) {
            idCaddy = rs.getInt("id");
        }

        rs.close();
        pstmt.close();

        return idCaddy;
    }

    public void augmenterMontant(Connection conn, Integer caddyId, double amount) throws SQLException
    {
        String sql = "UPDATE caddies " +
                "SET amount = amount + ? " +
                "WHERE id = ? AND payed = false";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setDouble(1, amount);
        pstmt.setInt(2, caddyId);

        int nbLignes = pstmt.executeUpdate();

        pstmt.close();

        if (nbLignes != 1) {
            throw new SQLException("Impossible de mettre à jour le montant du panier");
        }
    }

    public void diminuerMontant(Connection conn, Integer caddyId, double amount) throws SQLException
    {
        String sql = "UPDATE caddies " +
                "SET amount = amount - ? " +
                "WHERE id = ? AND payed = false";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setDouble(1, amount);
        pstmt.setInt(2, caddyId);

        int nbLignes = pstmt.executeUpdate();

        pstmt.close();

        if (nbLignes != 1) {
            throw new SQLException("Impossible de diminuer le montant du panier");
        }
    }

    public void supprimerCaddy(Connection conn, Integer caddyId) throws SQLException
    {
        String sql = "DELETE FROM caddies WHERE id = ? AND payed = false";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, caddyId);

        int nbLignes = pstmt.executeUpdate();

        pstmt.close();

        if (nbLignes != 1){
            throw new SQLException("Impossible de supprimer le panier");
        }
    }

    public void payerCaddy(Connection conn, Integer caddyId) throws SQLException
    {
        String sql = "UPDATE caddies SET payed = true " +
                "WHERE id = ? AND payed = false";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, caddyId);

        int nbLignes = pstmt.executeUpdate();

        pstmt.close();

        if (nbLignes != 1) {
            throw new SQLException("Impossible de payer le panier");
        }
    }
}
