package hepl.faad.server;

import hepl.faad.server.reponses.ReponsePAY_CADDY;
import hepl.faad.server.requetes.RequetePAY_CADDY;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientTestPAY_CADDY {
    public static void main(String[] args)
    {
        try
        {
            Socket socket = new Socket("localhost", 5001);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            int clientId = 2;

            RequetePAY_CADDY requete = new RequetePAY_CADDY(clientId);

            oos.writeObject(requete);

            ReponsePAY_CADDY reponse = (ReponsePAY_CADDY) ois.readObject();

            System.out.println("Paiement réussi : " + reponse.isValide());

            socket.close();
        }
        catch (IOException | ClassNotFoundException ex)
        {
            System.out.println("Erreur : " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
