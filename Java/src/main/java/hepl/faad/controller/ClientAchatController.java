package hepl.faad.controller;

import hepl.faad.model.viewmodel.BookSearchVM;
import hepl.faad.network.ClientAchat;
import hepl.faad.server.reponses.*;
import hepl.faad.server.requetes.*;

import hepl.faad.model.entity.Book;
import hepl.faad.view.ViewClientAchat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class ClientAchatController implements ActionListener {
    private ClientAchat clientAchat;
    private ViewClientAchat view;
    private Integer clientId;

    public ClientAchatController(ClientAchat clientAchat, ViewClientAchat view)
    {
        this.clientAchat = clientAchat;
        this.view = view;
        this.clientId = null;

        view.setController(this);
    }

    public Integer identifierClient(String lastName, String firstName, boolean nouveau) throws IOException, ClassNotFoundException
    {
        RequeteCLIENT requete = new RequeteCLIENT(lastName, firstName, nouveau);
        ReponseCLIENT reponse = (ReponseCLIENT) clientAchat.Send(requete);

        clientId = reponse.getIdClient();

        return clientId;
    }

    public Integer getClientId()
    {
        return clientId;
    }

    public ArrayList<Book> selectBook(BookSearchVM criteres) throws IOException, ClassNotFoundException
    {
        RequeteSELECT_BOOK requete = new RequeteSELECT_BOOK(criteres);
        ReponseSELECT_BOOK reponse = (ReponseSELECT_BOOK) clientAchat.Send(requete);

        return reponse.getLivres();
    }

    public boolean addBook(Integer bookId, int quantity) throws IOException, ClassNotFoundException
    {
        RequeteADD_CADDY_ITEM requete = new RequeteADD_CADDY_ITEM(clientId, bookId, quantity);
        ReponseADD_CADDY_ITEM reponse = (ReponseADD_CADDY_ITEM) clientAchat.Send(requete);

        return reponse.isValide();
    }

    public boolean deleteBook(Integer bookId, int quantity) throws IOException, ClassNotFoundException
    {
        RequeteDEL_CADDY_ITEM requete = new RequeteDEL_CADDY_ITEM(clientId, bookId, quantity);
        ReponseDEL_CADDY_ITEM reponse = (ReponseDEL_CADDY_ITEM) clientAchat.Send(requete);

        return reponse.isValide();
    }

    public boolean cancelCaddy() throws IOException, ClassNotFoundException
    {
        RequeteCANCEL_CADDY requete = new RequeteCANCEL_CADDY(clientId);
        ReponseCANCEL_CADDY reponse = (ReponseCANCEL_CADDY) clientAchat.Send(requete);

        return reponse.isValide();
    }

    public boolean payCaddy() throws IOException, ClassNotFoundException
    {
        RequetePAY_CADDY requete = new RequetePAY_CADDY(clientId);
        ReponsePAY_CADDY reponse = (ReponsePAY_CADDY) clientAchat.Send(requete);

        return reponse.isValide();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals(ControllerActions.VALIDATE_CLIENT))
        {
            if (clientId != null)
            {
                view.showErrorMessage("Vous devez payer ou annuler le panier du client actuel !");
                return;
            }

            try
            {
                Integer id = identifierClient(view.getLastName(), view.getFirstName(), view.isNewClient());

                if (id != null && id > 0)
                {
                    view.setClientId(id);
                    view.showMessage("Client identifié !");
                }
                else
                {
                    clientId = null;
                    view.showErrorMessage("Client introuvable !");
                }
            }
            catch (Exception ex)
            {
                view.showErrorMessage(ex.getMessage());
            }
        }

        if (e.getActionCommand().equals(ControllerActions.SEARCH_BOOK))
        {
            try
            {
                BookSearchVM criteres = new BookSearchVM();

                if (!view.getTitle().trim().isEmpty()) {
                    criteres.setTitle(view.getTitle().trim());
                }
                if (!view.getAuthor().trim().isEmpty()) {
                    criteres.setAuthorLastName(view.getAuthor().trim());
                }
                if (!view.getSubject().trim().isEmpty()) {
                    criteres.setSubjectName(view.getSubject().trim());
                }
                if (!view.getMaxPrice().trim().isEmpty()) {
                    criteres.setMaxPrice(Double.parseDouble(view.getMaxPrice().trim()));
                }
                ArrayList<Book> livres = selectBook(criteres);

                view.afficherLivres(livres);
            }
            catch (Exception ex)
            {
                view.showErrorMessage(ex.getMessage());
            }
        }

        if (e.getActionCommand().equals(ControllerActions.ADD_BOOK))
        {
            if (!clientIdentifie()) {
                return;
            }

            Integer bookId = view.getSelectedBookId();

            if (bookId == null)
            {
                view.showErrorMessage("Vous devez sélectionner un livre !");
                return;
            }

            try
            {
                int quantite = view.getQuantity();

                if (addBook(bookId, quantite))
                {
                    view.addCaddy(bookId, view.getSelectedBookTitle(), quantite, view.getSelectedBookPrice());

                    view.showMessage("Livre ajouté au panier !");
                }
                else
                {
                    view.showErrorMessage("Ajout impossible !");
                }
            }
            catch (Exception ex)
            {
                view.showErrorMessage(ex.getMessage());
            }
        }

        if (e.getActionCommand().equals(ControllerActions.DELETE_BOOK)) {
            if (!clientIdentifie()) {
                return;
            }

            Integer bookId = view.getSelectedCaddyBookId();

            if (bookId == null) {
                view.showErrorMessage("Vous devez sélectionner un livre dans le panier !");
                return;
            }

            int quantite = view.getQuantity();

            if (quantite > view.getSelectedCaddyQuantity()) {
                view.showErrorMessage("Quantité insuffisante dans le panier !");
                return;
            }

            try {
                if (deleteBook(bookId, quantite)) {
                    view.deleteCaddy(bookId, quantite);
                    view.showMessage("Livre supprimé du panier !");
                } else {
                    view.showErrorMessage("Erreur de suppression...");
                }
            } catch (Exception ex) {
                view.showErrorMessage(ex.getMessage());
            }
        }

        if (e.getActionCommand().equals(ControllerActions.CANCEL_CADDY))
        {
            if (!clientIdentifie()) {
                return;
            }

            try
            {
                if (cancelCaddy())
                {
                    clientId = null;
                    view.resetClient();
                    view.showMessage("Panier annulé !");
                }
                else
                {
                    view.showErrorMessage("Erreur d'annulation...");
                }
            }
            catch (Exception ex)
            {
                view.showErrorMessage(ex.getMessage());
            }
        }

        if (e.getActionCommand().equals(ControllerActions.PAY_CADDY)) {
            if (!clientIdentifie()) {
                return;
            }

            try {
                if (payCaddy()) {
                    clientId = null;
                    view.resetClient();
                    view.showMessage("Paiement effectué !");
                } else {
                    view.showErrorMessage("Erreur de paiement...");
                }
            } catch (Exception ex) {
                view.showErrorMessage(ex.getMessage());
            }
        }
    }

    private boolean clientIdentifie()
    {
        if (clientId == null)
        {
            view.showErrorMessage("Vous devez identifier un client !");
            return false;
        }

        return true;
    }

}
