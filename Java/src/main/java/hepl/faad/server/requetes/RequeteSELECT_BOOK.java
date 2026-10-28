package hepl.faad.server.requetes;

import hepl.faad.model.viewmodel.BookSearchVM;

public class RequeteSELECT_BOOK implements Requete {
    private BookSearchVM criteres;

    public RequeteSELECT_BOOK(BookSearchVM criteres)
    {
        this.criteres = criteres;
    }

    public BookSearchVM getCriteres()
    {
        return criteres;
    }
}
