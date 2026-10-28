package hepl.faad.model.dao;

import hepl.faad.model.entity.Subject;
import hepl.faad.model.viewmodel.SubjectSearchVM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubjectDAO {
    private ConnectDB connectDB;
    private ArrayList<Subject> subjects;

    public SubjectDAO()
    {
        connectDB = new ConnectDB();
        subjects = new ArrayList<>();
    }

    public ArrayList<Subject> getList()
    {
        return subjects;
    }

    public Subject getById(Integer id)
    {
        for (Subject sujet : subjects)
        {
            if (Objects.equals(sujet.getId(), id))
            {
                return sujet;
            }
        }

        return null;
    }

    public ArrayList<Subject> load()
    {
        return this.load(null);
    }

    public ArrayList<Subject> load(SubjectSearchVM ssvm)
    {
        try
        {
            String sql = "SELECT * FROM subjects ";

            if (ssvm != null)
            {
                String where = " WHERE 1=1 ";

                if (ssvm.getId() != null)
                    where += "AND id = ? ";

                if (ssvm.getName() != null)
                    where += "AND name = ? ";

                sql += where;
            }

            PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql);

            if (ssvm != null)
            {
                int paramNumber = 0;

                if (ssvm.getId() != null)
                {
                    paramNumber++;
                    pstmt.setInt(paramNumber, ssvm.getId());
                }

                if (ssvm.getName() != null)
                {
                    paramNumber++;
                    pstmt.setString(paramNumber, ssvm.getName());
                }
            }

            ResultSet rs = pstmt.executeQuery();
            subjects.clear();

            while (rs.next())
            {
                int id = rs.getInt("id");
                String nom = rs.getString("name");

                Subject sujet = new Subject(id, nom);

                subjects.add(sujet);
            }
            pstmt.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            return subjects;
        }
    }

    public void save(Subject sujet)
    {
        try
        {
            String sql;

            if (sujet != null)
            {
                if (sujet.getId() != null)
                {
                    sql = "UPDATE subjects SET " +
                            "name = ? " +
                            "WHERE id = ?";

                    PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql);

                    pstmt.setString(1, sujet.getName());
                    pstmt.setInt(2, sujet.getId());

                    pstmt.executeUpdate();
                    pstmt.close();
                }
                else
                {
                    sql = "INSERT INTO subjects " +
                            "(name) VALUES (?)";

                    PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                    pstmt.setString(1, sujet.getName());

                    pstmt.executeUpdate();

                    ResultSet rs = pstmt.getGeneratedKeys();

                    rs.next();
                    sujet.setId(rs.getInt(1));

                    rs.close();
                    pstmt.close();
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(Subject sujet)
    {
        if (sujet != null && sujet.getId() != null)
        {
            this.delete(sujet.getId());
        }
    }

    public void delete(Integer id)
    {
        if (id != null)
        {
            try
            {
                String sql = "DELETE FROM subjects WHERE id = ?";

                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

                stmt.setInt(1, id);

                stmt.executeUpdate();

                stmt.close();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
