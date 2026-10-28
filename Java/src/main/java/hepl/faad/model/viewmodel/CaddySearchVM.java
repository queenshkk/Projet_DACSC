package hepl.faad.model.viewmodel;

public class CaddySearchVM {
    private Integer id;
    private Integer clientId;
    private Boolean payed;

    public CaddySearchVM()
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

    public Integer getClientId()
    {
        return clientId;
    }

    public void setClientId(Integer clientId)
    {
        this.clientId = clientId;
    }

    public Boolean getPayed()
    {
        return payed;
    }

    public void setPayed(Boolean payed)
    {
        this.payed = payed;
    }
}
