package hepl.faad;

import hepl.faad.controller.ClientEncodageController;
import hepl.faad.view.GUI.MainWindowClientEncodage;

import javax.swing.*;
import com.formdev.flatlaf.FlatDarculaLaf;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args)
    {
        FlatDarculaLaf.setup();
        JFrame fenetre = new JFrame("Client Encodage Java");

        MainWindowClientEncodage formulaire = new MainWindowClientEncodage(fenetre);
        ClientEncodageController controller = new ClientEncodageController(formulaire);

        fenetre.setContentPane(formulaire.getPanelMain());

        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.pack();
        fenetre.setLocationRelativeTo(null);
        fenetre.setVisible(true);
    }
}
