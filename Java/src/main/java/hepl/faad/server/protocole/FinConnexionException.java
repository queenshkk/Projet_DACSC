package hepl.faad.server.protocole;

import hepl.faad.server.reponses.Reponse;

public class FinConnexionException extends Exception {
    private Reponse reponse;

    public FinConnexionException(Reponse reponse)
    {
        super("Fin de connexion décidée par le protocole");
        this.reponse = reponse;
    }

    public Reponse getReponse()
    {
        return reponse;
    }
}
