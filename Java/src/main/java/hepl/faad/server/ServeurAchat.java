package hepl.faad.server;

import hepl.faad.server.protocole.BSPP;
import hepl.faad.server.protocole.Protocole;
import hepl.faad.server.threads.ThreadServeurPool;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServeurAchat {
    public static void main(String[] args)
    {
        try
        {
            Properties properties = new Properties();

            FileInputStream input = new FileInputStream("serveurachat.properties");
            properties.load(input);
            input.close();

            int port = Integer.parseInt(properties.getProperty("PORT_PAYMENT"));

            Logger logger = new LoggerConsole();
            Protocole protocole = new BSPP(logger);
            ThreadServeurPool serveur = new ThreadServeurPool(port, protocole, 5, logger);
            serveur.start();

            System.out.println("Serveur Achat démarré sur le port " + port);
        }
        catch (IOException ex)
        {
            System.out.println("Erreur serveur : " + ex.getMessage());
        }
    }
}
