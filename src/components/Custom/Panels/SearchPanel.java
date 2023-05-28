package components.Custom.Panels;

import components.KapeTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SearchPanel extends JPanel implements KeyListener {

    KapeTable table;
    TableRowSorter<DefaultTableModel> sorter;
    private final JTextField search = new JTextField(20);

    public SearchPanel(KapeTable table){
        this.table = table;
        this.sorter = new TableRowSorter<>(table.getModel());
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        search.addKeyListener(this);
        this.add(new JLabel("Search: "));
        this.add(search);
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        search();
    }

    private void search(){
        sorter.setRowFilter(RowFilter.regexFilter(search.getText().trim()));
        table.setRowSorter(sorter);
    }
}
