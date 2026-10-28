package hepl.faad.model.viewmodel;

public class CaddyItemSearchVM {
    private Integer id;
    private Integer caddyId;
    private Integer bookId;

    public CaddyItemSearchVM()
    {
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
}
