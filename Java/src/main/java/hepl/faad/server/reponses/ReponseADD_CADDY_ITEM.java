package hepl.faad.server.reponses;

public class ReponseADD_CADDY_ITEM implements Reponse {
    private boolean valide;

    public ReponseADD_CADDY_ITEM(boolean valide)
    {
        this.valide = valide;
    }

    public boolean isValide()
    {
        return valide;
    }
}
