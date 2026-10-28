package hepl.faad.server;

import hepl.faad.server.reponses.ReponseCLIENT;
import hepl.faad.server.requetes.RequeteCLIENT;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientTestCLIENT {
    public static void main(String[] args)
    {
        try
        {
            Socket socket = new Socket("localhost", 5001);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            RequeteCLIENT requete = new RequeteCLIENT("Nguyen", "Quynh", true);

            oos.writeObject(requete);
            oos.flush();

            ReponseCLIENT reponse = (ReponseCLIENT) ois.readObject();

            System.out.println("Id client : " + reponse.getIdClient());

            socket.close();
        }
        catch (IOException | ClassNotFoundException ex)
        {
            System.out.println("Erreur : " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
