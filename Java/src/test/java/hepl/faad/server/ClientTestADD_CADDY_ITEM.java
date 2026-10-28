package hepl.faad.server;

import hepl.faad.server.reponses.ReponseADD_CADDY_ITEM;
import hepl.faad.server.requetes.RequeteADD_CADDY_ITEM;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientTestADD_CADDY_ITEM {
    public static void main(String[] args)
    {
        try
        {
            Socket socket = new Socket("localhost", 5001);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            // Remplacer ces identifiants par ceux de ta base MySQL
            int clientId = 2;
            int bookId = 1;
            int quantite = 2;

            RequeteADD_CADDY_ITEM requete = new RequeteADD_CADDY_ITEM(clientId, bookId, quantite);

            oos.writeObject(requete);
            oos.flush();

            System.out.println("Requête ADD_CADDY_ITEM envoyée.");

            ReponseADD_CADDY_ITEM reponse = (ReponseADD_CADDY_ITEM) ois.readObject();

            System.out.println("Ajout réussi : " + reponse.isValide());

            socket.close();
        }
        catch (IOException | ClassNotFoundException ex)
        {
            System.out.println("Erreur : " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
