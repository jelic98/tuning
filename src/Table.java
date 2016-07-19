import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Table {
    public JPanel panel;
    public static JTable table;
    public static DefaultTableModel model;
    private JScrollPane scrollPane;
    private JFrame frame = new JFrame();
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int width = (int) (screenSize.getWidth() * 0.5);
    private static int height = (int) (screenSize.getHeight() * 0.75);

    public Table() {
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

        table.setPreferredScrollableViewportSize(new Dimension(width, height));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);

        scrollPane = new JScrollPane(table);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                frame.dispose();
                Main.tableShown = false;
            }
        });
    }

    public void show() {
        panel.add(scrollPane);

        frame.setTitle("Competitors");
        frame.setSize(new Dimension(width, height));
        frame.setLocation(screenSize.width / 2 - width / 2, screenSize.height / 2 + height / 2);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
