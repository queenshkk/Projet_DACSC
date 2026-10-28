package hepl.faad.server;

import hepl.faad.server.reponses.ReponseCANCEL_CADDY;
import hepl.faad.server.requetes.RequeteCANCEL_CADDY;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientTestCANCEL_CADDY {
    public static void main(String[] args)
    {
        try
        {
            Socket socket = new Socket("localhost", 5001);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            int clientId = 2;

            RequeteCANCEL_CADDY requete = new RequeteCANCEL_CADDY(clientId);

            oos.writeObject(requete);
            oos.flush();

            ReponseCANCEL_CADDY reponse = (ReponseCANCEL_CADDY) ois.readObject();

            System.out.println("Annulation réussie : " + reponse.isValide());

            socket.close();
        }
        catch (IOException | ClassNotFoundException ex)
        {
            System.out.println("Erreur : " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
