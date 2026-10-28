package hepl.faad.model.dao;

import hepl.faad.model.entity.CaddyItem;
import hepl.faad.model.viewmodel.CaddyItemSearchVM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CaddyItemDAO {
    private ConnectDB connectDB;
    private ArrayList<CaddyItem> caddyItems;

    public CaddyItemDAO()
    {
        connectDB = new ConnectDB();
        caddyItems = new ArrayList<>();
    }

    public ArrayList<CaddyItem> getList()
    {
        return caddyItems;
    }

    public CaddyItem getById(Integer id)
    {
        for (CaddyItem item : caddyItems)
        {
            if (Objects.equals(item.getId(), id))
            {
                return item;
            }
        }

        return null;
    }

    public ArrayList<CaddyItem> load()
    {
        return this.load(null);
    }

    public ArrayList<CaddyItem> load(CaddyItemSearchVM cisvm)
    {
        try
        {
            String sql = "SELECT * FROM caddy_items ";

            if (cisvm != null)
            {
                String where = " WHERE 1=1 ";

                if (cisvm.getId() != null)
                    where += "AND id = ? ";

                if (cisvm.getCaddyId() != null)
                    where += "AND caddyId = ? ";

                if (cisvm.getBookId() != null)
                    where += "AND bookId = ? ";

                sql += where;
            }

            PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql);

            if (cisvm != null)
            {
                int paramNumber = 0;

                if (cisvm.getId() != null)
                {
                    paramNumber++;
                    pstmt.setInt(paramNumber, cisvm.getId());
                }

                if (cisvm.getCaddyId() != null)
                {
                    paramNumber++;
                    pstmt.setInt(paramNumber, cisvm.getCaddyId());
                }

                if (cisvm.getBookId() != null)
                {
                    paramNumber++;
                    pstmt.setInt(paramNumber, cisvm.getBookId());
                }
            }

            ResultSet rs = pstmt.executeQuery();
            caddyItems.clear();

            while (rs.next())
            {
                int id = rs.getInt("id");
                int caddyId = rs.getInt("caddyId");
                int bookId = rs.getInt("bookId");
                int quantity = rs.getInt("quantity");

                CaddyItem item = new CaddyItem(id, caddyId, bookId, quantity);

                caddyItems.add(item);
            }
            pstmt.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CaddyItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            return caddyItems;
        }
    }

    public void save(CaddyItem item)
    {
        try
        {
            String sql;

            if (item != null)
            {
                if (item.getId() != null)
                {
                    sql = "UPDATE caddy_items SET " +
                            "caddyId = ?, " +
                            "bookId = ?, " +
                            "quantity = ? " +
                            "WHERE id = ?";

                    PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql);

                    pstmt.setInt(1, item.getCaddyId());
                    pstmt.setInt(2, item.getBookId());
                    pstmt.setInt(3, item.getQuantity());
                    pstmt.setInt(4, item.getId());

                    pstmt.executeUpdate();
                    pstmt.close();
                }
                else
                {
                    sql = "INSERT INTO caddy_items " +
                            "(caddyId, bookId, quantity) " +
                            "VALUES (?, ?, ?)";

                    PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                    pstmt.setInt(1, item.getCaddyId());
                    pstmt.setInt(2, item.getBookId());
                    pstmt.setInt(3, item.getQuantity());

                    pstmt.executeUpdate();

                    ResultSet rs = pstmt.getGeneratedKeys();

                    rs.next();
                    item.setId(rs.getInt(1));

                    rs.close();
                    pstmt.close();
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CaddyItemDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(CaddyItem item)
    {
        if (item != null && item.getId() != null)
        {
            this.delete(item.getId());
        }
    }

    public void delete(Integer id)
    {
        if (id != null)
        {
            try
            {
                String sql = "DELETE FROM caddy_items WHERE id = ?";

                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

                stmt.setInt(1, id);

                stmt.executeUpdate();

                stmt.close();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(CaddyItemDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void ajouterCaddyItem(Connection conn, CaddyItem item) throws SQLException
    {
        String sql = "INSERT INTO caddy_items (caddyId, bookId, quantity) " +
                "VALUES (?, ?, ?)";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, item.getCaddyId());
        pstmt.setInt(2, item.getBookId());
        pstmt.setInt(3, item.getQuantity());

        pstmt.executeUpdate();
        pstmt.close();
    }


    public Integer getCaddyItemId(Connection conn, Integer caddyId, Integer bookId) throws SQLException
    {
        String sql = "SELECT id FROM caddy_items " +
                "WHERE caddyId = ? AND bookId = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, caddyId);
        pstmt.setInt(2, bookId);

        ResultSet rs = pstmt.executeQuery();

        Integer idItem = null;

        if(rs.next()){
            idItem = rs.getInt("id");
        }

        rs.close();
        pstmt.close();

        return idItem;
    }


    public void augmenterQuantite(Connection conn, Integer itemId, int quantity) throws SQLException
    {
        String sql = "UPDATE caddy_items " +
                "SET quantity = quantity + ? " +
                "WHERE id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, quantity);
        pstmt.setInt(2, itemId);

        pstmt.executeUpdate();
        pstmt.close();
    }

    public int getQuantite(Connection conn, Integer itemId) throws SQLException
    {
        String sql = "SELECT quantity FROM caddy_items WHERE id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, itemId);

        ResultSet rs = pstmt.executeQuery();

        int quantite = 0;

        if(rs.next()){
            quantite = rs.getInt("quantity");
        }

        rs.close();
        pstmt.close();

        return quantite;
    }

    public void diminuerQuantite(Connection conn, Integer itemId, int quantity) throws SQLException
    {
        String sql = "UPDATE caddy_items " +
                "SET quantity = quantity - ? " +
                "WHERE id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, quantity);
        pstmt.setInt(2, itemId);

        int nbLignes = pstmt.executeUpdate();

        pstmt.close();

        if (nbLignes != 1) {
            throw new SQLException("Impossible de diminuer la quantité");
        }
    }

    public void supprimerCaddyItem(Connection conn, Integer itemId) throws SQLException
    {
        String sql = "DELETE FROM caddy_items WHERE id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, itemId);

        int nbLignes = pstmt.executeUpdate();

        pstmt.close();

        if (nbLignes != 1) {
            throw new SQLException("Impossible de supprimer l'article");
        }
    }
}
