package hepl.faad.server;

import hepl.faad.server.reponses.ReponseDEL_CADDY_ITEM;
import hepl.faad.server.requetes.RequeteDEL_CADDY_ITEM;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientTestDEL_CADDY_ITEM {
    public static void main(String[] args)
    {
        try
        {
            Socket socket = new Socket("localhost", 5001);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            int clientId = 2;
            int bookId = 1;
            int quantite = 2;

            RequeteDEL_CADDY_ITEM requete = new RequeteDEL_CADDY_ITEM(clientId, bookId, quantite);

            oos.writeObject(requete);

            ReponseDEL_CADDY_ITEM reponse = (ReponseDEL_CADDY_ITEM) ois.readObject();

            System.out.println("Suppression réussie : " + reponse.isValide());

            socket.close();
        }
        catch (IOException | ClassNotFoundException ex)
        {
            System.out.println("Erreur : " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
