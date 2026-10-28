package hepl.faad.server.reponses;

public class ReponsePAY_CADDY implements Reponse {
    private boolean valide;

    public ReponsePAY_CADDY(boolean valide)
    {
        this.valide = valide;
    }

    public boolean isValide()
    {
        return valide;
    }
}
