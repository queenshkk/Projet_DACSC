package hepl.faad.network;

import hepl.faad.server.reponses.Reponse;
import hepl.faad.server.requetes.Requete;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientAchat {
    private String ip;
    private int port;

    public ClientAchat(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }

    public Reponse Send(Requete requete) throws IOException, ClassNotFoundException
    {
        Socket socket = new Socket(ip, port);

        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(requete);

            Reponse reponse = (Reponse) ois.readObject();

            return reponse;
        }
        finally
        {
            socket.close();
        }
    }
}
