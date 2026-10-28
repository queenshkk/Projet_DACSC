package hepl.faad.view.GUI;

import hepl.faad.model.entity.Author;
import hepl.faad.model.entity.Book;
import hepl.faad.model.entity.Subject;

import javax.swing.table.AbstractTableModel;

import java.util.ArrayList;

public class BookTableModel extends AbstractTableModel{
    private ArrayList<Book> livres;
    private ArrayList<Author> auteurs;
    private ArrayList<Subject> sujets;

    public BookTableModel(ArrayList<Book> data, ArrayList<Author> auteurs, ArrayList<Subject> sujets)
    {
        livres = data;
        this.auteurs = auteurs;
        this.sujets = sujets;
    }

    @Override
    public Class getColumnClass(int c)
    {
        if (c == 0) return String.class;
        if (c == 1) return String.class;
        if (c == 2) return String.class;
        if (c == 3) return String.class;
        if (c == 4) return Integer.class;
        if (c == 5) return Integer.class;
        if (c == 6) return Double.class;
        if (c == 7) return Integer.class;

        return null;
    }

    @Override
    public int getRowCount()
    {
        return livres.size();
    }

    @Override
    public int getColumnCount()
    {
        return 8;
    }

    @Override
    public Object getValueAt(int l, int c)
    {
        Book livre = livres.get(l);

        if (c == 0) return livre.getIsbn();
        if (c == 1) return livre.getTitle();
        if (c == 2)
        {
            for (Author auteur : auteurs)
            {
                if (auteur.getId() == livre.getIdAuthor())
                {
                    return auteur.getLastName() + " " + auteur.getFirstName();
                }
            }
            return "";
        }
        if (c == 3)
        {
            for (Subject sujet : sujets)
            {
                if (sujet.getId() == livre.getIdSubject())
                {
                    return sujet.getName();
                }
            }
            return "";
        }
        if (c == 4) return livre.getPageCount();
        if (c == 5) return livre.getStockQuantity();
        if (c == 6) return livre.getPrice();
        if (c == 7) return livre.getPublishYear();

        return null;
    }

    @Override
    public void setValueAt(Object value, int l, int c)
    {
    }

    @Override
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }

    public Book getBookAt(int index)
    {
        return livres.get(index);
    }
}
