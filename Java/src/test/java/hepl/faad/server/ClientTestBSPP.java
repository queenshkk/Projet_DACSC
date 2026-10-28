package hepl.faad.server;

import hepl.faad.model.entity.Book;
import hepl.faad.model.viewmodel.BookSearchVM;
import hepl.faad.server.reponses.ReponseSELECT_BOOK;
import hepl.faad.server.requetes.RequeteSELECT_BOOK;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientTestBSPP {
    public static void main(String[] args)
    {
        try
        {
            Socket socket = new Socket("localhost", 5001);

            // Création des flux
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            // Préparation de la requête
            BookSearchVM criteres = new BookSearchVM();

            RequeteSELECT_BOOK requete = new RequeteSELECT_BOOK(criteres);

            // Envoi de la requête
            oos.writeObject(requete);
            oos.flush();

            System.out.println("Requête SELECT_BOOK envoyée.");

            // Lecture de la réponse
            ReponseSELECT_BOOK reponse = (ReponseSELECT_BOOK) ois.readObject();

            System.out.println("Nombre de livres : " +
                    reponse.getLivres().size());

            for (Book livre : reponse.getLivres())
            {
                System.out.println(livre.getId() + " - " +
                        livre.getTitle() + " - " +
                        livre.getPrice() + " EUR");
            }

            // Fermeture de la connexion
            socket.close();
        }
        catch (IOException | ClassNotFoundException ex)
        {
            System.out.println("Erreur : " + ex.getMessage());ex.printStackTrace();
        }
    }
}
