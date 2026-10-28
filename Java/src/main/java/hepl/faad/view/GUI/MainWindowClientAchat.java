package hepl.faad.view.GUI;

import com.formdev.flatlaf.FlatDarculaLaf;
import hepl.faad.controller.ClientAchatController;
import hepl.faad.model.entity.Book;
import hepl.faad.network.ClientAchat;
import hepl.faad.controller.ControllerActions;
import hepl.faad.view.ViewClientAchat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainWindowClientAchat implements ViewClientAchat {
    private JPanel mainPanel;
    private JTextField JTextFieldLastName;
    private JTextField JTextFieldFirstName;
    private JCheckBox JCheckBoxNewClient;
    private JButton JButtonValidateClient;
    private JLabel JLabelLastName;
    private JLabel JLabelFirstName;
    private JTable JTableSearchBooks;
    private JTextField JTextFieldTitle;
    private JTextField JTextFieldAuthor;
    private JTextField JtextFieldSubject;
    private JTextField JTextFieldMaxPrice;
    private JButton JButtonSearch;
    private JSpinner JSpinnerQuantity;
    private JButton JButtonAdd;
    private JButton JButtonDelete;
    private JButton JButtonCancel;
    private JButton JButtonPay;
    private JTable JTableCaddy;
    private JLabel JLabelQuantity;
    private JLabel JLabelClientId;
    private JLabel JLabelCaddy;
    private JLabel JLabelSearchBook;

    public MainWindowClientAchat()
    {
        JFrame frame = new JFrame("Client Achat");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);

        JButtonValidateClient.setActionCommand(ControllerActions.VALIDATE_CLIENT);
        JButtonSearch.setActionCommand(ControllerActions.SEARCH_BOOK);
        JButtonAdd.setActionCommand(ControllerActions.ADD_BOOK);
        JButtonDelete.setActionCommand(ControllerActions.DELETE_BOOK);
        JButtonCancel.setActionCommand(ControllerActions.CANCEL_CADDY);
        JButtonPay.setActionCommand(ControllerActions.PAY_CADDY);

        JSpinnerQuantity.setModel(new SpinnerNumberModel(1, 1, 100, 1));

        JTextFieldTitle.putClientProperty("JTextField.placeholderText", "Titre du livre");
        JTextFieldAuthor.putClientProperty("JTextField.placeholderText", "Auteur");
        JtextFieldSubject.putClientProperty("JTextField.placeholderText", "Sujet");
        JTextFieldMaxPrice.putClientProperty("JTextField.placeholderText", "Prix maximum");

        JTableSearchBooks.setModel(new DefaultTableModel(new String[]{"ID", "Titre", "ISBN", "Auteur ID", "Sujet ID", "Pages", "Prix", "Stock", "Année"}, 0));
        JTableCaddy.setModel(new DefaultTableModel(new String[]{"ID", "Livre", "Quantité", "Prix unitaire"}, 0));

        JLabelClientId.setText("Aucun client");

        frame.setVisible(true);
    }

    public void setController(ActionListener controller)
    {
        JButtonValidateClient.addActionListener(controller);
        JButtonSearch.addActionListener(controller);
        JButtonAdd.addActionListener(controller);
        JButtonDelete.addActionListener(controller);
        JButtonCancel.addActionListener(controller);
        JButtonPay.addActionListener(controller);
    }

    public String getLastName()
    {
        return JTextFieldLastName.getText();
    }

    public String getFirstName()
    {
        return JTextFieldFirstName.getText();
    }

    public boolean isNewClient()
    {
        return JCheckBoxNewClient.isSelected();
    }

    public void setClientId(Integer id)
    {
        JLabelClientId.setText("Client n° " + id);
    }

    public String getTitle()
    {
        return JTextFieldTitle.getText();
    }

    public String getAuthor()
    {
        return JTextFieldAuthor.getText();
    }

    public String getSubject()
    {
        return JtextFieldSubject.getText();
    }

    public String getMaxPrice()
    {
        return JTextFieldMaxPrice.getText();
    }

    public void afficherLivres(ArrayList<Book> livres)
    {
        DefaultTableModel modele = new DefaultTableModel(new String[]{"ID", "Titre", "ISBN", "Auteur ID", "Sujet ID",
                "Pages", "Prix", "Stock", "Année"}, 0);

        for (Book livre : livres)
        {
            modele.addRow(new Object[]{
                    livre.getId(),
                    livre.getTitle(),
                    livre.getIsbn(),
                    livre.getIdAuthor(),
                    livre.getIdSubject(),
                    livre.getPageCount(),
                    livre.getPrice(),
                    livre.getStockQuantity(),
                    livre.getPublishYear()
            });
        }

        JTableSearchBooks.setModel(modele);
    }

    public Integer getSelectedBookId()
    {
        int ligne = JTableSearchBooks.getSelectedRow();

        if (ligne == -1) {
            return null;
        }

        return (Integer) JTableSearchBooks.getValueAt(ligne, 0);
    }

    public String getSelectedBookTitle()
    {
        int ligne = JTableSearchBooks.getSelectedRow();

        if (ligne == -1) {
            return null;
        }

        return JTableSearchBooks.getValueAt(ligne, 1).toString();
    }

    public double getSelectedBookPrice()
    {
        int ligne = JTableSearchBooks.getSelectedRow();

        if (ligne == -1) {
            return 0;
        }

        return ((Number) JTableSearchBooks.getValueAt(ligne, 6)).doubleValue();
    }

    public int getQuantity()
    {
        return (Integer) JSpinnerQuantity.getValue();
    }

    public void addCaddy(Integer bookId, String title, int quantity, double price)
    {
        DefaultTableModel modele = (DefaultTableModel) JTableCaddy.getModel();

        for (int i = 0; i < modele.getRowCount(); i++)
        {
            if (bookId.equals(modele.getValueAt(i, 0)))
            {
                int oldQuantity = (Integer) modele.getValueAt(i, 2);
                modele.setValueAt(oldQuantity + quantity, i, 2);
                return;
            }
        }

        modele.addRow(new Object[]{bookId, title, quantity, price});
    }

    @Override
    public Integer getSelectedCaddyBookId()
    {
        int ligne = JTableCaddy.getSelectedRow();

        if (ligne == -1) {
            return null;
        }
        return (Integer) JTableCaddy.getValueAt(ligne, 0);
    }

    @Override
    public int getSelectedCaddyQuantity()
    {
        int ligne = JTableCaddy.getSelectedRow();

        if (ligne == -1) {
            return 0;
        }
        return (Integer) JTableCaddy.getValueAt(ligne, 2);
    }

    @Override
    public void deleteCaddy(Integer bookId, int quantity)
    {
        DefaultTableModel modele = (DefaultTableModel) JTableCaddy.getModel();

        for (int i = 0; i < modele.getRowCount(); i++)
        {
            if (bookId.equals(modele.getValueAt(i, 0)))
            {
                int oldQuantity = (Integer) modele.getValueAt(i, 2);

                if (quantity >= oldQuantity) {
                    modele.removeRow(i);
                }
                else {
                    modele.setValueAt(oldQuantity - quantity, i, 2);
                }

                return;
            }
        }
    }

    @Override
    public void clearCaddy()
    {
        DefaultTableModel modele = (DefaultTableModel) JTableCaddy.getModel();
        modele.setRowCount(0);
    }

    @Override
    public void resetClient()
    {
        JLabelClientId.setText("Aucun client");
        JTextFieldLastName.setText("");
        JTextFieldFirstName.setText("");
        JCheckBoxNewClient.setSelected(false);

        clearCaddy();

        JSpinnerQuantity.setValue(1);
    }

    @Override
    public void showMessage(String message)
    {
        JOptionPane.showMessageDialog(mainPanel, message, "Information", JOptionPane.INFORMATION_MESSAGE);    }

    @Override
    public void showErrorMessage(String message)
    {
        JOptionPane.showMessageDialog(mainPanel, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args)
    {
        FlatDarculaLaf.setup();

        MainWindowClientAchat fenetre = new MainWindowClientAchat();
        ClientAchat client = new ClientAchat("localhost", 5001);
        ClientAchatController controller = new ClientAchatController(client, fenetre);
    }
}
