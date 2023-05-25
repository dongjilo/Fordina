package components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class KapeTable extends JTable {
    public KapeTable (Vector<Vector<Object>> data, Vector<String> columnNames) {
        super(new DefaultTableModel(data, columnNames));
        initTable();
    }

    private void initTable() {
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowHeight(25);
        setGridColor(Color.LIGHT_GRAY);
        setFillsViewportHeight(true);
    }

}

class customTableModel extends DefaultTableModel{



}