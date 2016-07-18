import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public JPanel panel;
    private JTable table;
    private JButton bExport, bImport, bResults, bAdd, bRemove;
    private JLabel lEcloga;
    private JScrollPane scrollPane;
    private static DefaultTableModel model;
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public Main() {
        bAdd.setForeground(Color.GREEN);
        bRemove.setForeground(Color.RED);
        lEcloga.setForeground(Color.DARK_GRAY);

        model = new DefaultTableModel();

        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] columns = {"Number", "Name", "Vehicle", "Class", "Rated"};

        for(String value : columns) {
            model.addColumn(value);
        }

        table.setPreferredScrollableViewportSize(new Dimension(screenSize.width, screenSize.height));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);

        scrollPane = new JScrollPane(table);

        bAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Add add = new Add();
                //add.show();

                JLabel labela = new JLabel();
                labela.setText("blah blah blah");
                panel.add(labela);
            }
        });
        bRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int selectedIndex = table.getSelectedRow();

                int numberValue = (int) table.getValueAt(selectedIndex, 0);
                String nameValue = String.valueOf(table.getValueAt(selectedIndex, 1));
                String vehicleValue = String.valueOf(table.getValueAt(selectedIndex, 2));
                String classValue = String.valueOf(table.getValueAt(selectedIndex, 3));
                String ratedValue = String.valueOf(table.getValueAt(selectedIndex, 4));

                if(selectedIndex != -1) {
                    model.removeRow(selectedIndex);
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("TuningRef");
        frame.setSize(new Dimension(screenSize.width, screenSize.height));
        frame.setContentPane(new Main().panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
    }

    public void addRow(Object[] row) {
        int numberValue = (int) row[0];
        String nameValue = String.valueOf(row[1]);
        String vehicleValue = String.valueOf(row[2]);
        String classValue = String.valueOf(row[3]);
        String ratedValue = String.valueOf(row[4]);

        model.addRow(row);
        table.setModel(model);
    }
}
