package hepl.faad.model.dao;

import hepl.faad.model.entity.Author;
import hepl.faad.model.viewmodel.AuthorSearchVM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthorDAO {
    private ConnectDB connectDB;
    private ArrayList<Author> authors;

    public AuthorDAO()
    {
        connectDB = new ConnectDB();
        authors = new ArrayList<>();
    }

    public ArrayList<Author> getList()
    {
        return authors;
    }

    public Author getById(Integer id)
    {
        for (Author auteur : authors)
        {
            if (Objects.equals(auteur.getId(), id))
            {
                return auteur;
            }
        }

        return null;
    }

    public ArrayList<Author> load()
    {
        return this.load(null);
    }

    public ArrayList<Author> load(AuthorSearchVM asvm)
    {
        try
        {
            String sql = "SELECT * FROM authors ";

            if (asvm != null)
            {
                String where = " WHERE 1=1 ";

                if (asvm.getId() != null)
                    where += "AND id = ? ";

                if (asvm.getLastName() != null)
                    where += "AND last_name LIKE ? ";

                if (asvm.getFirstName() != null)
                    where += "AND first_name LIKE ? ";

                sql += where;
            }

            PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql);

            if (asvm != null)
            {
                int paramNumber = 0;

                if (asvm.getId() != null)
                {
                    paramNumber++;
                    pstmt.setInt(paramNumber, asvm.getId());
                }

                if (asvm.getLastName() != null)
                {
                    paramNumber++;
                    pstmt.setString(paramNumber, "%" + asvm.getLastName() + "%");
                }

                if (asvm.getFirstName() != null)
                {
                    paramNumber++;
                    pstmt.setString(paramNumber, "%" + asvm.getFirstName() + "%");
                }
            }


            ResultSet rs = pstmt.executeQuery();
            authors.clear();

            while (rs.next())
            {
                int id = rs.getInt("id");
                String nom = rs.getString("last_name");
                String prenom = rs.getString("first_name");
                java.sql.Date dateSql = rs.getDate("birth_date");

                Author auteur = new Author(id, nom, prenom);

                if (dateSql != null)
                {
                    auteur.setBirthDate(dateSql.toLocalDate());
                }

                authors.add(auteur);
            }
            pstmt.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(AuthorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            return authors;
        }
    }

    public void save(Author auteur)
    {
        try
        {
            String sql;

            if (auteur != null)
            {
                if (auteur.getId() != null)
                {
                    sql = "UPDATE authors SET " +
                            "last_name = ?, " +
                            "first_name = ?, " +
                            "birth_date = ? " +
                            "WHERE id = ?";

                    PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql);

                    pstmt.setString(1, auteur.getLastName());
                    pstmt.setString(2, auteur.getFirstName());
                    if (auteur.getBirthDate() != null) {
                        pstmt.setDate(3, java.sql.Date.valueOf(auteur.getBirthDate()));
                    }
                    else {
                        pstmt.setNull(3, java.sql.Types.DATE);
                    }
                    pstmt.setInt(4, auteur.getId());

                    pstmt.executeUpdate();
                    pstmt.close();
                }
                else
                {
                    sql = "INSERT INTO authors " +
                            "(last_name, first_name, birth_date) " +
                            "VALUES (?, ?, ?)";

                    PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                    pstmt.setString(1, auteur.getLastName());
                    pstmt.setString(2, auteur.getFirstName());
                    if (auteur.getBirthDate() != null) {
                        pstmt.setDate(3, java.sql.Date.valueOf(auteur.getBirthDate()));
                    }
                    else {
                        pstmt.setNull(3, java.sql.Types.DATE);
                    }

                    pstmt.executeUpdate();

                    ResultSet rs = pstmt.getGeneratedKeys();

                    if (rs.next())
                    {
                        auteur.setId(rs.getInt(1));
                    }

                    rs.close();
                    pstmt.close();
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(AuthorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(Author auteur)
    {
        if (auteur != null && auteur.getId() != null)
        {
            this.delete(auteur.getId());
        }
    }

    public void delete(Integer id)
    {
        if (id != null)
        {
            try
            {
                String sql = "DELETE FROM authors WHERE id = ?";

                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

                stmt.setInt(1, id);

                stmt.executeUpdate();

                stmt.close();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(AuthorDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
