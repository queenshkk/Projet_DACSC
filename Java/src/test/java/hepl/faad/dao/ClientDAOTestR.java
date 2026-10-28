package hepl.faad.dao;

import hepl.faad.model.entity.Client;
import hepl.faad.model.dao.ClientDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.ClientSearchVM;

import java.util.ArrayList;

public class ClientDAOTestR {
    public static void main(String[] args)
    {
        ClientDAO dao = new ClientDAO();

        try
        {
            System.out.println("--- TOUS LES CLIENTS ---");

            ArrayList<Client> clients = dao.load();

            for (Client client : clients)
            {
                System.out.println(client.getId() + " - " +
                        client.getLastName() + " " +
                        client.getFirstName());
            }

            System.out.println("--- SELECTION PAR ID (2) ---");

            ClientSearchVM csvm = new ClientSearchVM();
            csvm.setId(2);

            clients = dao.load(csvm);

            Client client = dao.getById(2);

            if (client != null)
            {
                System.out.println("Client trouvé : " + client.getLastName());
            }
            else
            {
                System.out.println("Client introuvable.");
            }

            System.out.println("--- SELECTION PAR NOM (Dupont) ---");

            csvm.setId(null);
            csvm.setLastName("Dupont");

            clients = dao.load(csvm);

            for (Client c : clients)
            {
                System.out.println(c.getLastName() + " " + c.getFirstName());
            }

            System.out.println("--- SELECTION PAR PRENOM (Jean) ---");

            csvm.setLastName(null);
            csvm.setFirstName("Jean");

            clients = dao.load(csvm);

            for (Client c : clients)
            {
                System.out.println(c.getLastName() + " " + c.getFirstName());
            }
        }
        finally
        {
            ConnectDB.close();
        }
    }
}
