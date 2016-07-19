import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private JPanel panel;
    private JTable table;
    private JButton bExport, bImport, bResults, bAdd, bRemove;
    private JLabel lEcloga;
    private JScrollPane scrollPane;
    private static DefaultTableModel model;
    private String directoryName = System.getProperty("user.home") + "/TuningRef";
    private static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Date date = new Date();
    private String session = dateFormat.format(date);
    private boolean exportStatus;
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static Map<String, String[]> classes = new HashMap<String, String[]>();
    private static Map<String, String[]> competitors = new HashMap<String, String[]>();

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
                add.show(competitors.size());
            }
        });

        bRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int selectedIndex = table.getSelectedRow();

                String numberValue = String.valueOf(table.getValueAt(selectedIndex, 0));
                String nameValue = String.valueOf(table.getValueAt(selectedIndex, 1));
                String vehicleValue = String.valueOf(table.getValueAt(selectedIndex, 2));
                String classValue = String.valueOf(table.getValueAt(selectedIndex, 3));
                String ratedValue = String.valueOf(table.getValueAt(selectedIndex, 4));

                if(selectedIndex != -1) {
                    model.removeRow(selectedIndex);
                    competitors.remove(numberValue);
                }
            }
        });

        bExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportStatus = true;

                createProgramDirectory();

                if(exportStatus) {
                    createSessionDirectory();
                }

                if(exportStatus) {
                    createRatingDirectory();
                }

                if(exportStatus) {
                    createListFile();
                }

                if(exportStatus) {
                    JOptionPane.showMessageDialog(null, "Data is successfully exported", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        bResults.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createResultFile();
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
        String numberValue = String.valueOf(row[0]);
        String nameValue = String.valueOf(row[1]);
        String vehicleValue = String.valueOf(row[2]);
        String classValue = String.valueOf(row[3]);
        String ratedValue = String.valueOf(row[4]);

        String[] info = {nameValue, vehicleValue, classValue, ratedValue};
        competitors.put(numberValue, info);

        model.addRow(row);
        table.setModel(model);
    }

    private void createProgramDirectory() {
        if(!Files.exists(Paths.get(directoryName))) {
            try {
                File file = new File(directoryName);
                boolean created = file.mkdir();

                if(!created) {
                    exportStatus = false;
                    JOptionPane.showMessageDialog(null, "Program directory could not be created", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createSessionDirectory() {
        if(!Files.exists(Paths.get(directoryName + "/" + session))) {
            try {
                File file = new File(directoryName + "/" + session);
                boolean created = file.mkdir();

                if (!created) {
                    exportStatus = false;
                    JOptionPane.showMessageDialog(null, "Session directory could not be created", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createRatingDirectory() {
        if(!Files.exists(Paths.get(directoryName + "/" + session + "/ratings"))) {
            try {
                File dir = new File(directoryName + "/" + session + "/ratings");

                boolean created = dir.mkdir();

                if (!created) {
                    exportStatus = false;
                    JOptionPane.showMessageDialog(null, "Rating directory could not be created", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createListFile() {
        try {
            PrintWriter w = new PrintWriter(directoryName + "/" + session + "/list.trl", "UTF-8");

            for(String numberValue : competitors.keySet()) {
                String[] data = competitors.get(numberValue);

                String nameValue = data[0];
                String vehicleValue = data[1];
                String classValue = data[2];
                String ratedValue = data[3];

                w.println(numberValue + ":" + nameValue + ":" + vehicleValue + ":" + classValue + ":" + ratedValue);
            }

            w.close();
        }catch(FileNotFoundException | UnsupportedEncodingException e) {
            JOptionPane.showMessageDialog(null, "Competitor list could not be generated", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void createResultFile() {
        try {
            PrintWriter w = new PrintWriter(directoryName + "/" + session + "/result.txt", "UTF-8");

            for(String numberValue : competitors.keySet()) {
                String[] data = competitors.get(numberValue);

                String classValue = data[2];

                if(classes.get(classValue) == null) {
                    String[] numbers = {numberValue};
                    classes.put(classValue, numbers);
                }else {
                    String[] oldNumbers = classes.get(classValue);
                    String[] numbers = new String[oldNumbers.length + 1];

                    System.arraycopy(oldNumbers, 0, numbers, 0, oldNumbers.length);
                    numbers[oldNumbers.length] = numberValue;

                    classes.remove(classValue);
                    classes.put(classValue, numbers);
                }

//                for(String element : classes.keySet()) {
//                    w.println(element + " " + Arrays.toString(classes.get(element)));
//                }
            }

            w.close();

            JOptionPane.showMessageDialog(null, "Results are successfully generated", "Success", JOptionPane.INFORMATION_MESSAGE);
        }catch(FileNotFoundException | UnsupportedEncodingException e) {
            JOptionPane.showMessageDialog(null, "Results could not be generated", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
