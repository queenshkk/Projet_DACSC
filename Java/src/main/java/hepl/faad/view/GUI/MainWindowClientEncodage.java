package hepl.faad.view.GUI;

import hepl.faad.controller.ClientEncodageController;
import hepl.faad.controller.ControllerActions;
import hepl.faad.model.entity.Author;
import hepl.faad.model.entity.Book;
import hepl.faad.model.entity.Subject;
import hepl.faad.view.ViewClientEncodage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class MainWindowClientEncodage implements ViewClientEncodage {
    private JComboBox JComboBoxAuthor;
    private JComboBox JComboBoxSubject;
    private JTextField JTextFieldTitle;
    private JTextField JTextFieldISBN;
    private JSpinner JSpinnerPages;
    private JSpinner JSpinnerYearOfPublication;
    private JSpinner JSpinnerPrice;
    private JSpinner JSpinnerStock;
    private JTable JTableBook;
    private JButton JButtonClearFields;
    private JButton JButtonAddNewBook;
    private JLabel JLabelTitle;
    private JLabel JLabelISBN;
    private JLabel JLabelPage;
    private JLabel JLabelPrice;
    private JLabel JLabelAuthor;
    private JLabel JLabelSubject;
    private JLabel JLabelYearOfPublication;
    private JLabel JLabelStock;
    private JButton JButtonAddAuthor;
    private JButton JButtonAddSubject;
    private JPanel mainPanel;

    private JMenuItem menuLogin;
    private JMenuItem menuLogout;
    private JMenuItem menuQuit;
    private JFrame fenetre;

    private ArrayList<Book> livres = new ArrayList<Book>();
    private ArrayList<Author> auteurs = new ArrayList<Author>();
    private ArrayList<Subject> sujets = new ArrayList<Subject>();

    private BookTableModel bookTableModel;

    public MainWindowClientEncodage(JFrame fenetre)
    {
        this.fenetre = fenetre;
        bookTableModel = new BookTableModel(livres, auteurs, sujets);

        JTableBook.setModel(bookTableModel);
        JTableBook.setColumnModel(new BookTableColumnModel());

        JMenuBar menuBar = new JMenuBar();
        JMenu menuSession = new JMenu("Session");

        menuLogin = new JMenuItem("Login");
        menuLogout = new JMenuItem("Logout");
        menuQuit = new JMenuItem("Quitter");

        menuSession.add(menuLogin);
        menuSession.add(menuLogout);
        menuSession.add(menuQuit);

        menuBar.add(menuSession);

        fenetre.setJMenuBar(menuBar);

        setLoggedIn(false);


    }
    @Override
    public void setController(ClientEncodageController controller)
    {
        menuLogin.setActionCommand(ControllerActions.LOGIN_ENCODING);
        menuLogout.setActionCommand(ControllerActions.LOGOUT_ENCODING);
        menuQuit.setActionCommand(ControllerActions.QUIT_ENCODING);

        JButtonAddAuthor.setActionCommand(ControllerActions.ADD_AUTHOR);
        JButtonAddSubject.setActionCommand(ControllerActions.ADD_SUBJECT);
        JButtonAddNewBook.setActionCommand(ControllerActions.ADD_NEW_BOOK);
        JButtonClearFields.setActionCommand(ControllerActions.CLEAR_FIELDS);

        menuLogin.addActionListener(controller);
        menuLogout.addActionListener(controller);
        menuQuit.addActionListener(controller);

        JButtonAddAuthor.addActionListener(controller);
        JButtonAddSubject.addActionListener(controller);
        JButtonAddNewBook.addActionListener(controller);
        JButtonClearFields.addActionListener(controller);
    }

    @Override
    public String getLogin()
    {
        return JOptionPane.showInputDialog(fenetre, "Login :");
    }

    @Override
    public String getPassword()
    {
        return JOptionPane.showInputDialog(fenetre, "Password :");
    }

    @Override
    public String getAuthorLastName()
    {
        return JOptionPane.showInputDialog(fenetre, "Nom de l'auteur :");
    }

    @Override
    public String getAuthorFirstName()
    {
        return JOptionPane.showInputDialog(fenetre, "Prénom de l'auteur :");
    }

    @Override
    public String getSubjectName()
    {
        return JOptionPane.showInputDialog(fenetre, "Nom du sujet :");
    }

    @Override
    public Book getNewBook()
    {
        String isbn = JTextFieldISBN.getText();
        String titre = JTextFieldTitle.getText();

        int indexAuteur = JComboBoxAuthor.getSelectedIndex();
        int indexSujet = JComboBoxSubject.getSelectedIndex();

        if (isbn.trim().isEmpty() || titre.trim().isEmpty() || indexAuteur == -1 || indexSujet == -1)
        {
            return null;
        }

        int pages = ((Number) JSpinnerPages.getValue()).intValue();
        int annee = ((Number) JSpinnerYearOfPublication.getValue()).intValue();
        double prix = ((Number) JSpinnerPrice.getValue()).doubleValue();
        int stock = ((Number) JSpinnerStock.getValue()).intValue();

        return new Book(
                auteurs.get(indexAuteur).getId(),
                sujets.get(indexSujet).getId(),
                titre,
                isbn,
                pages,
                stock,
                prix,
                annee
        );
    }

    @Override
    public void displayAuthors(ArrayList<Author> liste)
    {
        auteurs.clear();
        auteurs.addAll(liste);

        JComboBoxAuthor.removeAllItems();

        for (Author auteur : auteurs)
        {
            JComboBoxAuthor.addItem( auteur.getLastName() + " " + auteur.getFirstName());
        }

        bookTableModel.fireTableDataChanged();
    }

    @Override
    public void displaySubjects(ArrayList<Subject> liste)
    {
        sujets.clear();
        sujets.addAll(liste);

        JComboBoxSubject.removeAllItems();

        for (Subject sujet : sujets)
        {
            JComboBoxSubject.addItem(sujet.getName());
        }

        bookTableModel.fireTableDataChanged();
    }

    @Override
    public void addDisplayedBook(Book livre)
    {
        livres.add(livre);
        bookTableModel.fireTableDataChanged();
    }

    @Override
    public void clearSession()
    {
        livres.clear();
        auteurs.clear();
        sujets.clear();

        bookTableModel.fireTableDataChanged();

        JComboBoxAuthor.removeAllItems();
        JComboBoxSubject.removeAllItems();
    }

    @Override
    public void clearFields()
    {
        JTextFieldTitle.setText("");
        JTextFieldISBN.setText("");

        JSpinnerPages.setValue(0);
        JSpinnerYearOfPublication.setValue(0);
        JSpinnerPrice.setValue(0);
        JSpinnerStock.setValue(0);

        if (JComboBoxAuthor.getItemCount() > 0) {
            JComboBoxAuthor.setSelectedIndex(0);
        }
        if (JComboBoxSubject.getItemCount() > 0) {
            JComboBoxSubject.setSelectedIndex(0);
        }
    }

    @Override
    public void setLoggedIn(boolean connected)
    {
        menuLogin.setEnabled(!connected);
        menuLogout.setEnabled(connected);

        JButtonAddAuthor.setEnabled(connected);
        JButtonAddSubject.setEnabled(connected);
        JButtonAddNewBook.setEnabled(connected);
    }

    @Override
    public void showMessage(String message)
    {
        JOptionPane.showMessageDialog(fenetre, message);
    }

    @Override
    public void showErrorMessage(String message)
    {
        JOptionPane.showMessageDialog(fenetre,message,"Erreur",JOptionPane.ERROR_MESSAGE
        );
    }

    @Override
    public void closeWindow()
    {
        fenetre.dispose();
    }

    public JPanel getPanelMain()
    {
        return mainPanel;
    }


}
