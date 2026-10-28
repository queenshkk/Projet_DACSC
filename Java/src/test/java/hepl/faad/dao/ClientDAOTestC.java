package hepl.faad.dao;

import hepl.faad.model.entity.Client;
import hepl.faad.model.dao.ClientDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.ClientSearchVM;

import java.util.ArrayList;

public class ClientDAOTestC {
    public static void main(String[] args)
    {
        ClientDAO dao = new ClientDAO();

        Client client = new Client(null, "Nguyen", "Quynh", "Rue du Mont, Liège");

        dao.save(client);

        System.out.println("Ajout --> ID : " + client.getId());

        if (client.getId() != null)
        {
            ClientSearchVM csvm = new ClientSearchVM();
            csvm.setId(client.getId());

            System.out.println("--- SELECTION PAR ID ---");

            ArrayList<Client> clients = dao.load(csvm);

            if (clients.size() != 0)
            {
                System.out.println("Client trouvé : " +
                        clients.get(0).getLastName() + " " +
                        clients.get(0).getFirstName());
            }
            else
            {
                System.out.println("Client introuvable !");
            }
        }
        else
        {
            System.out.println("L'ajout a échoué !");
        }

        ConnectDB.close();
    }
}
