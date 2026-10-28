package hepl.faad.server.protocole;

import hepl.faad.server.reponses.Reponse;
import hepl.faad.server.requetes.Requete;

import java.net.Socket;

public interface Protocole {
    String getNom();

    Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException;
}
