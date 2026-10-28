package hepl.faad.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientEncodage {
    Socket csocket;
    private DataOutputStream dos;
    private DataInputStream dis;

    public ClientEncodage(String ip, int port) throws IOException
    {
        csocket = new Socket(ip, port);
        System.out.println("Connexion établie.");

        System.out.println("--- Socket ---");
        System.out.println("Adresse IP locale : " + csocket.getLocalAddress().getHostAddress());
        System.out.println("Port local : " + csocket.getLocalPort());
        System.out.println("Adresse IP distante : " + csocket.getInetAddress().getHostAddress());
        System.out.println("Port distant : " + csocket.getPort());

        dos = new DataOutputStream(csocket.getOutputStream());
        dis = new DataInputStream(csocket.getInputStream());
    }

    public void Send(String requete) throws IOException
    {
        byte[] donnees = requete.getBytes("UTF-8");

        String longueur = String.format("%04d", donnees.length);

        dos.write(longueur.getBytes("US-ASCII"));
        dos.write(donnees);
        dos.flush();

        System.out.println("Requête envoyée.");
    }


    public String Receive() throws IOException
    {
        StringBuffer buffer = new StringBuffer();

        for (int i=0; i<4; i++)
        {
            byte b = dis.readByte();
            buffer.append((char)b);
        }

        int longueur = Integer.parseInt(buffer.toString());

        if (longueur < 0 || longueur > 10000)
            throw new IOException("Longueur invalide.");

        byte[] donnees = new byte[longueur];

        for (int i=0; i<longueur; i++)
        {
            donnees[i] = dis.readByte();
        }

        String reponse = new String(donnees,"UTF-8");

        System.out.println("Réponse reçue.");
        return reponse;
    }

    public void Close() throws IOException
    {
        csocket.close();

        System.out.println("Connexion fermée.");
    }
}
