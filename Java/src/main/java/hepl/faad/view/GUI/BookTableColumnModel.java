package hepl.faad.view.GUI;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class BookTableColumnModel extends DefaultTableColumnModel {

    public BookTableColumnModel(){
        super();


        int[] taillesColonnes = {120, 200, 70, 70, 60, 60, 70, 60};

        String[] nomsColonnes = {"ISBN", "Titre", "Auteur", "Sujet", "Pages", "Stock", "Prix", "Année"};

        for (int i = 0; i < taillesColonnes.length; i++)
        {
            TableColumn colonne = new TableColumn(i, taillesColonnes[i]);

            colonne.setHeaderValue(nomsColonnes[i]);

            addColumn(colonne);
        }
    }
}
