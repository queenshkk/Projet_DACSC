package hepl.faad.server.reponses;

import hepl.faad.model.entity.Book;

import java.util.ArrayList;

public class ReponseSELECT_BOOK implements Reponse {
    private ArrayList<Book> livres;

    public ReponseSELECT_BOOK(ArrayList<Book> livres)
    {
        this.livres = livres;
    }

    public ArrayList<Book> getLivres()
    {
        return livres;
    }
}
