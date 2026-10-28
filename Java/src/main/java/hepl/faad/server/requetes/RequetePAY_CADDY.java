package hepl.faad.server.requetes;

public class RequetePAY_CADDY implements Requete {
    private Integer clientId;

    public RequetePAY_CADDY(Integer clientId)
    {
        this.clientId = clientId;
    }

    public Integer getClientId()
    {
        return clientId;
    }
}
