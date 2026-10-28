package hepl.faad.server.reponses;

public class ReponseCANCEL_CADDY implements Reponse {
    private boolean valide;

    public ReponseCANCEL_CADDY(boolean valide)
    {
        this.valide = valide;
    }

    public boolean isValide()
    {
        return valide;
    }
}
