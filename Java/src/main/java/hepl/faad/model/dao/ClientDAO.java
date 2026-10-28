package hepl.faad.model.dao;

import hepl.faad.model.entity.Client;
import hepl.faad.model.viewmodel.ClientSearchVM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientDAO {
    private ConnectDB connectDB;
    private ArrayList<Client> clients;

    public ClientDAO()
    {
        connectDB = new ConnectDB();
        clients = new ArrayList<>();
    }

    public ArrayList<Client> getList()
    {
        return clients;
    }

    public Client getById(Integer id)
    {
        for (Client client : clients)
        {
            if (Objects.equals(client.getId(), id))
            {
                return client;
            }
        }

        return null;
    }

    public ArrayList<Client> load()
    {
        return this.load(null);
    }

    public ArrayList<Client> load(ClientSearchVM csvm)
    {
        try
        {
            String sql = "SELECT * FROM clients ";

            if (csvm != null)
            {
                String where = " WHERE 1=1 ";

                if (csvm.getId() != null)
                    where += "AND id = ? ";

                if (csvm.getLastName() != null)
                    where += "AND last_name LIKE ? ";

                if (csvm.getFirstName() != null)
                    where += "AND first_name LIKE ? ";

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

                if (csvm.getLastName() != null)
                {
                    paramNumber++;
                    pstmt.setString(paramNumber, "%" + csvm.getLastName() + "%");
                }

                if (csvm.getFirstName() != null)
                {
                    paramNumber++;
                    pstmt.setString(paramNumber, "%" + csvm.getFirstName() + "%");
                }
            }

            ResultSet rs = pstmt.executeQuery();
            clients.clear();

            while (rs.next())
            {
                int id = rs.getInt("id");
                String lastName = rs.getString("last_name");
                String firstName = rs.getString("first_name");
                String address = rs.getString("address");

                Client client = new Client(id, lastName, firstName, address);

                clients.add(client);
            }
            pstmt.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ClientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return clients;
    }

    public void save(Client client)
    {
        try
        {
            String sql;

            if (client != null)
            {
                if (client.getId() != null)
                {
                    sql = "UPDATE clients SET " +
                            "last_name = ?, " +
                            "first_name = ?, " +
                            "address = ? " +
                            "WHERE id = ?";

                    PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql);

                    pstmt.setString(1, client.getLastName());
                    pstmt.setString(2, client.getFirstName());
                    pstmt.setString(3, client.getAddress());
                    pstmt.setInt(4, client.getId());

                    pstmt.executeUpdate();
                    pstmt.close();
                }
                else
                {
                    sql = "INSERT INTO clients " +
                            "(last_name, first_name, address) " +
                            "VALUES (?, ?, ?)";

                    PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                    pstmt.setString(1, client.getLastName());
                    pstmt.setString(2, client.getFirstName());
                    pstmt.setString(3, client.getAddress());

                    pstmt.executeUpdate();

                    ResultSet rs = pstmt.getGeneratedKeys();

                    rs.next();
                    client.setId(rs.getInt(1));

                    rs.close();
                    pstmt.close();
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ClientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(Client client)
    {
        if (client != null && client.getId() != null)
        {
            this.delete(client.getId());
        }
    }

    public void delete(Integer id)
    {
        if (id != null)
        {
            try
            {
                String sql = "DELETE FROM clients WHERE id = ?";

                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

                stmt.setInt(1, id);

                stmt.executeUpdate();

                stmt.close();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(ClientDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
