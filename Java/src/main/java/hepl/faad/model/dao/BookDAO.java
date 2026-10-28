package hepl.faad.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import hepl.faad.model.entity.Book;
import hepl.faad.model.viewmodel.BookSearchVM;

public class BookDAO {
    private ConnectDB connectDB;
    private ArrayList<Book> books;

    public BookDAO()
    {
        connectDB = new ConnectDB();
        books = new ArrayList<>();
    }

    public ArrayList<Book> getList()
    {
        return books;
    }

    public Book getById(Integer id)
    {
        for (Book livre : books)
        {
            if (Objects.equals(livre.getId(), id))
            {
                return livre;
            }
        }

        return null;
    }

    public ArrayList<Book> load()
    {
        return this.load(null);
    }

    public ArrayList<Book> load(BookSearchVM bsvm)  {
        try {
            String sql = "SELECT books.* FROM books " +
                        "JOIN authors ON books.author_id = authors.id " +
                        "JOIN subjects ON books.subject_id = subjects.id ";

            if (bsvm != null)
            {
                String where = " WHERE 1=1 ";

                if (bsvm.getId() != null)
                    where += "AND books.id = ? ";

                if (bsvm.getTitle() != null)
                    where += "AND books.title LIKE ? ";

                if (bsvm.getAuthorLastName() != null)
                    where += "AND authors.last_name LIKE ? ";

                if (bsvm.getAuthorFirstName() != null)
                    where += "AND authors.first_name LIKE ? ";

                if (bsvm.getSubjectName() != null)
                    where += "AND subjects.name LIKE ? ";

                if (bsvm.getMaxPrice() != null)
                    where += "AND books.price <= ? ";

                sql += where;
            }

            PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql);

            if (bsvm != null)
            {
                int paramNumber = 0;

                if (bsvm.getId() != null)
                {
                    paramNumber++;
                    pstmt.setInt(paramNumber, bsvm.getId());
                }

                if (bsvm.getTitle() != null)
                {
                    paramNumber++;
                    pstmt.setString(paramNumber, "%" + bsvm.getTitle() + "%");
                }

                if (bsvm.getAuthorLastName() != null)
                {
                    paramNumber++;
                    pstmt.setString(paramNumber, "%" + bsvm.getAuthorLastName() + "%");
                }

                if (bsvm.getAuthorFirstName() != null)
                {
                    paramNumber++;
                    pstmt.setString(paramNumber, "%" + bsvm.getAuthorFirstName() + "%");
                }

                if (bsvm.getSubjectName() != null)
                {
                    paramNumber++;
                    pstmt.setString(paramNumber, "%" + bsvm.getSubjectName() + "%");
                }

                if (bsvm.getMaxPrice() != null)
                {
                    paramNumber++;
                    pstmt.setDouble(paramNumber, bsvm.getMaxPrice());
                }
            }

            ResultSet rs = pstmt.executeQuery();
            books.clear();

            while (rs.next()) {
                int idAuthor = rs.getInt("author_id");
                int idSubject = rs.getInt("subject_id");
                String title = rs.getString("title");
                String isbn = rs.getString("isbn");
                int pageCount = rs.getInt("page_count");
                int stockQuantity = rs.getInt("stock_quantity");
                double price = rs.getDouble("price");
                int publishYear = rs.getInt("publish_year");
                int id = rs.getInt("id");

                Book livre = new Book(idAuthor, idSubject, title, isbn, pageCount, stockQuantity, price, publishYear);
                livre.setId(id);

                books.add(livre);
            }
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            return books;
        }
    }

    public void save(Book livre)
    {
        try
        {
            String sql;

            if (livre != null)
            {
                if (livre.getId() != null)
                {
                    sql = "UPDATE books SET " +
                            "author_id = ?, " +
                            "subject_id = ?, " +
                            "title = ?, " +
                            "isbn = ?, " +
                            "page_count = ?, " +
                            "stock_quantity = ?, " +
                            "price = ?, " +
                            "publish_year = ? " +
                            "WHERE id = ?";

                    PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql);

                    pstmt.setInt(1, livre.getIdAuthor());
                    pstmt.setInt(2, livre.getIdSubject());
                    pstmt.setString(3, livre.getTitle());
                    pstmt.setString(4, livre.getIsbn());
                    pstmt.setInt(5, livre.getPageCount());
                    pstmt.setInt(6, livre.getStockQuantity());
                    pstmt.setDouble(7, livre.getPrice());
                    pstmt.setInt(8, livre.getPublishYear());
                    pstmt.setInt(9, livre.getId());

                    pstmt.executeUpdate();
                    pstmt.close();
                }
                else
                {
                    sql = "INSERT INTO books " +
                            "(author_id, subject_id, title, isbn, " +
                            "page_count, stock_quantity, price, publish_year) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                    PreparedStatement pstmt = connectDB.getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                    pstmt.setInt(1, livre.getIdAuthor());
                    pstmt.setInt(2, livre.getIdSubject());
                    pstmt.setString(3, livre.getTitle());
                    pstmt.setString(4, livre.getIsbn());
                    pstmt.setInt(5, livre.getPageCount());
                    pstmt.setInt(6, livre.getStockQuantity());
                    pstmt.setDouble(7, livre.getPrice());
                    pstmt.setInt(8, livre.getPublishYear());

                    pstmt.executeUpdate();

                    ResultSet rs = pstmt.getGeneratedKeys();

                    rs.next();

                    livre.setId(rs.getInt(1));

                    rs.close();
                    pstmt.close();
                }
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(Book livre)
    {
        if (livre != null && livre.getId() != null)
        {
            this.delete(livre.getId());
        }
    }

    public void delete(Integer id)
    {
        if (id != null)
        {
            try
            {
                String sql = "DELETE FROM books WHERE id = ?";

                PreparedStatement stmt = connectDB.getConn().prepareStatement(sql);

                stmt.setInt(1, id);

                stmt.executeUpdate();

                stmt.close();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(BookDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    public boolean diminuerStock(Connection conn, Integer bookId, int quantity) throws SQLException
    {
        if (bookId == null || quantity <= 0){
            return false;
        }

        String sql = "UPDATE books " +
                "SET stock_quantity = stock_quantity - ? " +
                "WHERE id = ? AND stock_quantity >= ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, quantity);
        pstmt.setInt(2, bookId);
        pstmt.setInt(3, quantity);

        int nbLignes = pstmt.executeUpdate();

        pstmt.close();

        return nbLignes == 1;
    }

    public void augmenterStock(Connection conn, Integer bookId, int quantity) throws SQLException
    {
        String sql = "UPDATE books " +
                "SET stock_quantity = stock_quantity + ? " +
                "WHERE id = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, quantity);
        pstmt.setInt(2, bookId);

        int nbLignes = pstmt.executeUpdate();

        pstmt.close();

        if (nbLignes != 1) {
            throw new SQLException("Impossible de remettre le livre en stock");
        }
    }
}
