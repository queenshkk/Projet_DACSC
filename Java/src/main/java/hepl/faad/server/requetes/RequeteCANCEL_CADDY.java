package hepl.faad.server.requetes;

public class RequeteCANCEL_CADDY implements Requete {
    private Integer clientId;

    public RequeteCANCEL_CADDY(Integer clientId)
    {
        this.clientId = clientId;
    }

    public Integer getClientId()
    {
        return clientId;
    }
}
