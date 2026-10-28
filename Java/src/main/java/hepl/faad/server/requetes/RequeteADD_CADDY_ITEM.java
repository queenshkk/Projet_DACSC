package hepl.faad.server.requetes;

public class RequeteADD_CADDY_ITEM implements Requete {
    private Integer clientId;
    private Integer bookId;
    private int quantity;

    public RequeteADD_CADDY_ITEM(Integer clientId, Integer bookId, int quantity)
    {
        this.clientId = clientId;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public Integer getClientId()
    {
        return clientId;
    }

    public Integer getBookId()
    {
        return bookId;
    }

    public int getQuantity()
    {
        return quantity;
    }
}
