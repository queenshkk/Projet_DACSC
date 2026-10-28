package hepl.faad.model.entity;

public class CaddyItem {
    private Integer id;
    private Integer caddyId;
    private Integer bookId;
    private int quantity;
    public CaddyItem(Integer id, Integer caddyId, Integer bookId, int quantity)
    {
        this.id = id;
        this.caddyId = caddyId;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getCaddyId()
    {
        return caddyId;
    }

    public void setCaddyId(Integer caddyId)
    {
        this.caddyId = caddyId;
    }

    public Integer getBookId()
    {
        return bookId;
    }

    public void setBookId(Integer bookId)
    {
        this.bookId = bookId;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    @Override
    public String toString(){
        return "CaddyItem{" +
                "id = " + id +
                ", caddyId = " + caddyId +
                ", bookId = " + bookId +
                ", quantity = " + quantity +
                '}';
    }
}
