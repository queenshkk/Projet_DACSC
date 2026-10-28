package hepl.faad.controller;

import hepl.faad.model.entity.Author;
import hepl.faad.model.entity.Book;
import hepl.faad.network.ClientEncodage;
import hepl.faad.model.entity.Subject;
import hepl.faad.view.ViewClientEncodage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class ClientEncodageController implements ActionListener {
    private ClientEncodage client;
    private ViewClientEncodage view;


    public ClientEncodageController(ViewClientEncodage view)
    {
        this.view = view;
        view.setController(this);
    }

    public boolean Login(String ip, int port, String login, String password) throws IOException
    {
        client = new ClientEncodage(ip, port);

        String requete = "LOGIN#" + login + "#" + password;
        client.Send(requete);

        String reponse = client.Receive();

        if (reponse.equals("LOGIN#ok"))
        {
            System.out.println("Login réussi.");
            return true;
        }

        client.Close();
        client = null;

        return false;
    }

    public void Logout() throws IOException
    {
        if (client != null)
        {
            client.Send("LOGOUT");

            String reponse = client.Receive();
            System.out.println(reponse);

            client.Close();
            client = null;
        }
    }


    public ArrayList<Author> GetAuthors() throws IOException
    {
        client.Send("GET_AUTHORS");

        String reponse = client.Receive();
        String[] champs = reponse.split("#");

        ArrayList<Author> auteurs = new ArrayList<Author>();

        if (champs.length >= 2 && champs[0].equals("GET_AUTHORS") && champs[1].equals("ok"))
        {
            for (int i = 2; i + 2 < champs.length; i = i + 3)
            {
                int id = Integer.parseInt(champs[i]);
                String nom = champs[i + 1];
                String prenom = champs[i + 2];

                auteurs.add(new Author(id, nom, prenom));
            }
        }

        return auteurs;
    }

    public ArrayList<Subject> GetSubjects() throws IOException
    {
        client.Send("GET_SUBJECTS");

        String reponse = client.Receive();
        String[] champs = reponse.split("#");

        ArrayList<Subject> sujets = new ArrayList<Subject>();

        if (champs.length >= 2 && champs[0].equals("GET_SUBJECTS") && champs[1].equals("ok"))
        {
            for (int i = 2; i + 1 < champs.length; i = i + 2)
            {
                int id = Integer.parseInt(champs[i]);
                String nom = champs[i + 1];

                sujets.add(new Subject(id, nom));
            }
        }

        return sujets;
    }

    public boolean AddAuthor(String nom, String prenom) throws IOException
    {
        client.Send("ADD_AUTHOR#" + nom + "#" + prenom);

        String reponse = client.Receive();

        String[] champs = reponse.split("#");

        return champs.length == 3 && champs[0].equals("ADD_AUTHOR") && champs[1].equals("ok");
    }

    public boolean AddSubject(String nom) throws IOException
    {
        client.Send("ADD_SUBJECT#" + nom);

        String reponse = client.Receive();

        String[] champs = reponse.split("#");

        return champs.length == 3 && champs[0].equals("ADD_SUBJECT") && champs[1].equals("ok");
    }

    public boolean AddBook(Book livre) throws IOException
    {
        String requete = "ADD_BOOK#" +
                livre.getIdAuthor() + "#" +
                livre.getIdSubject() + "#" +
                livre.getTitle() + "#" +
                livre.getIsbn() + "#" +
                livre.getPageCount() + "#" +
                livre.getStockQuantity() + "#" +
                livre.getPrice() + "#" +
                livre.getPublishYear();

        client.Send(requete);

        String reponse = client.Receive();

        System.out.println("Réponse ADD_BOOK : " + reponse);

        String[] champs = reponse.split("#");

        return champs.length == 3 && champs[0].equals("ADD_BOOK") && champs[1].equals("ok");
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals(ControllerActions.LOGIN_ENCODING))
        {
            String login = view.getLogin();

            if (login == null || login.isEmpty()) {
                return;
            }

            String password = view.getPassword();

            if (password == null || password.isEmpty()) {
                return;
            }

            try
            {
                if (Login("192.168.5.128", 5000, login, password))
                {
                    view.setLoggedIn(true);
                    view.displayAuthors(GetAuthors());
                    view.displaySubjects(GetSubjects());
                    view.showMessage("Login réussi.");
                }
                else
                {
                    view.showErrorMessage("Login incorrect.");
                }
            }
            catch (IOException ex)
            {
                view.showErrorMessage("Erreur : " + ex.getMessage());
            }
        }

        if (e.getActionCommand().equals(ControllerActions.LOGOUT_ENCODING))
        {
            try
            {
                Logout();
                view.clearSession();
                view.setLoggedIn(false);
                view.showMessage("Logout réussi.");
            }
            catch (IOException ex)
            {
                view.showErrorMessage("Erreur : " + ex.getMessage());
            }
        }

        if (e.getActionCommand().equals(ControllerActions.ADD_AUTHOR))
        {
            String nom = view.getAuthorLastName();

            if (nom == null || nom.isEmpty()) {
                return;
            }

            String prenom = view.getAuthorFirstName();

            if (prenom == null || prenom.isEmpty()) {
                return;
            }

            try
            {
                if (AddAuthor(nom, prenom))
                {
                    view.displayAuthors(GetAuthors());
                    view.showMessage("Auteur ajouté.");
                }
                else
                {
                    view.showErrorMessage("Erreur lors de l'ajout de l'auteur.");
                }
            }
            catch (IOException ex)
            {
                view.showErrorMessage("Erreur : " + ex.getMessage());
            }
        }

        if (e.getActionCommand().equals(ControllerActions.ADD_SUBJECT))
        {
            String nom = view.getSubjectName();

            if (nom == null || nom.isEmpty()) {
                return;
            }

            try
            {
                if (AddSubject(nom))
                {
                    view.displaySubjects(GetSubjects());
                    view.showMessage("Sujet ajouté.");
                }
                else
                {
                    view.showErrorMessage("Erreur lors de l'ajout du sujet.");
                }
            }
            catch (IOException ex)
            {
                view.showErrorMessage("Erreur : " + ex.getMessage());
            }
        }

        if (e.getActionCommand().equals(ControllerActions.ADD_NEW_BOOK))
        {
            Book livre = view.getNewBook();

            if (livre == null)
            {
                view.showErrorMessage("Veuillez compléter les champs.");
                return;
            }

            try
            {
                if (AddBook(livre))
                {
                    view.addDisplayedBook(livre);
                    view.showMessage("Livre ajouté.");
                }
                else
                {
                    view.showErrorMessage("Erreur lors de l'ajout du livre.");
                }
            }
            catch (IOException ex)
            {
                view.showErrorMessage("Erreur : " + ex.getMessage());
            }
        }

        if (e.getActionCommand().equals(ControllerActions.CLEAR_FIELDS))
        {
            view.clearFields();
        }

        if (e.getActionCommand().equals(ControllerActions.QUIT_ENCODING))
        {
            try
            {
                Logout();
            }
            catch (IOException ex)
            {
                view.showErrorMessage("Erreur : " + ex.getMessage());
            }
            view.closeWindow();
        }
    }
}
