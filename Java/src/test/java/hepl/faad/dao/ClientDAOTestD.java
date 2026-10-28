package hepl.faad.dao;

import hepl.faad.model.entity.Client;
import hepl.faad.model.dao.ClientDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.ClientSearchVM;

import java.util.ArrayList;

public class ClientDAOTestD {
    public static void main(String[] args)
    {
        ClientDAO dao = new ClientDAO();

        ClientSearchVM csvm = new ClientSearchVM();
        csvm.setLastName("Nguyen");

        ArrayList<Client> clients = dao.load(csvm);

        System.out.println("Nombre de clients trouvés : " + clients.size());

        if (clients.size() != 0)
        {
            Client client = clients.get(0);
            Integer id = client.getId();

            System.out.println("ID du client : " + id);

            dao.delete(client);

            csvm.setLastName(null);
            csvm.setId(id);

            clients = dao.load(csvm);

            if (clients.size() == 0)
            {
                System.out.println("Suppression réussie.");
            }
            else
            {
                System.out.println("Le client existe toujours !");
            }
        }
        else
        {
            System.out.println("Aucun client de test trouvé : suppression annulée.");
        }

        ConnectDB.close();
    }
}
