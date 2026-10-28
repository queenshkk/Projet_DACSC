package hepl.faad.server.threads;

import hepl.faad.server.Logger;
import hepl.faad.server.protocole.FinConnexionException;
import hepl.faad.server.protocole.Protocole;
import hepl.faad.server.reponses.Reponse;
import hepl.faad.server.requetes.Requete;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class ThreadClient extends Thread{
    protected Protocole protocole;
    protected Socket csocket;
    protected Logger logger;
    private int numero;
    private static int numCourant = 1;

    public ThreadClient(Protocole protocole, Socket csocket, Logger logger) throws IOException
    {
        super("TH Client " + numCourant + " (protocole=" + protocole.getNom() + ")");
        this.protocole = protocole;
        this.csocket = csocket;
        this.logger = logger;
        this.numero = numCourant++;
    }

    public ThreadClient(Protocole protocole, ThreadGroup groupe, Logger logger) throws IOException
    {
        super(groupe, "TH Client " + numCourant + " (protocole=" + protocole.getNom() + ")");
        this.protocole = protocole;
        this.csocket = null;
        this.logger = logger;
        this.numero = numCourant++;
    }

    @Override
    public void run()
    {
        try {
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;

            try {
                ois = new ObjectInputStream(csocket.getInputStream());
                oos = new ObjectOutputStream(csocket.getOutputStream());


                Requete requete = (Requete) ois.readObject();
                Reponse reponse = protocole.TraiteRequete(requete, csocket);
                oos.writeObject(reponse);

            } catch (FinConnexionException ex) {
                logger.Trace("Fin de connexion demandée par le protocole.");

                if (oos != null && ex.getReponse() != null) {
                    oos.writeObject(ex.getReponse());
                }
            }
        }
        catch (IOException ex)
        {
            logger.Trace("Erreur I/O");
        }
        catch (ClassNotFoundException ex)
        {
            logger.Trace("Erreur requete invalide");
        }
        finally
        {
            try
            {
                csocket.close();
            }
            catch (IOException ex)
            {
                logger.Trace("Erreur I/O : " + ex.getMessage());
            }
        }
    }

}
