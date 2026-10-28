package hepl.faad.dao;

import hepl.faad.model.entity.Client;
import hepl.faad.model.dao.ClientDAO;
import hepl.faad.model.dao.ConnectDB;
import hepl.faad.model.viewmodel.ClientSearchVM;

import java.util.ArrayList;

public class ClientDAOTestU {
    public static void main(String[] args)
    {
        ClientDAO dao = new ClientDAO();

        ClientSearchVM csvm = new ClientSearchVM();
        csvm.setLastName("Nguyen");

        ArrayList<Client> clients = dao.load(csvm);

        if (clients.size() != 0)
        {
            Client client = clients.get(0);

            client.setAddress("Rue du Test 20, Liège");

            dao.save(client);

            System.out.println("Modification effectuée.");

            csvm.setId(client.getId());
            csvm.setLastName(null);

            clients = dao.load(csvm);

            if (clients.size() != 0)
            {
                System.out.println("Nouvelle adresse : " + clients.get(0).getAddress());
            }
        }
        else
        {
            System.out.println("Client de test introuvable !");
        }

        ConnectDB.close();
    }
}
