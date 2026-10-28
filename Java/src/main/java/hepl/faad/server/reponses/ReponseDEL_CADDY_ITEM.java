package hepl.faad.server.reponses;

public class ReponseDEL_CADDY_ITEM implements Reponse {
    private boolean valide;

    public ReponseDEL_CADDY_ITEM(boolean valide)
    {
        this.valide = valide;
    }

    public boolean isValide()
    {
        return valide;
    }
}
