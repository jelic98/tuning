import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private JPanel panel;
    private JButton bSave, bLoad, bResults, bAdd, bRemove, bExport;
    private JLabel lEcloga;
    private String directoryName = System.getProperty("user.home") + "/TuningRef";
    private static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Date date = new Date();
    private String session = dateFormat.format(date);
    private boolean saveStatus, loadStatus;
    private String loadDirectory;
    public static boolean tableShown;
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static Map<String, String[]> classes = new HashMap<String, String[]>();
    private static Map<String, String[]> competitors = new HashMap<String, String[]>();

    public Main() {
        bAdd.setForeground(Color.GREEN);
        bRemove.setForeground(Color.RED);
        lEcloga.setForeground(Color.DARK_GRAY);

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
                if(tableShown) {
                    DefaultTableModel model = (DefaultTableModel) Table.table.getModel();
                    int selectedIndex = Table.table.getSelectedRow();

                    String numberValue = String.valueOf(Table.table.getValueAt(selectedIndex, 0));

                    if(selectedIndex != -1) {
                        model.removeRow(selectedIndex);
                        competitors.remove(numberValue);
                    }
                }
            }
        });

        bSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStatus = true;

                createProgramDirectory();

                if(saveStatus) {
                    createSessionDirectory();
                }

                if(saveStatus) {
                    createRatingDirectory();
                }

                if(saveStatus) {
                    createRatingFiles();
                }

                if(saveStatus) {
                    createListFile();
                }

                if(saveStatus) {
                    JOptionPane.showMessageDialog(null, "Data is successfully saved", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadStatus = true;
                loadDirectory = "";

                loadData();

                if(loadStatus) {
                    loadRatings();
                }
            }
        });

        bResults.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createResultFile();
            }
        });

        bExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportData();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("TuningRef");
        frame.setSize(new Dimension((int) (screenSize.width * 0.25), screenSize.height));
        frame.setLocation(screenSize.width / 2 - (int) (screenSize.width * 0.25) / 2, screenSize.height);
        frame.setContentPane(new Main().panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        Table tableWindow = new Table();
        tableWindow.show();
        tableShown = true;
    }

    public void addRow(Object[] row) {
        String numberValue = String.valueOf(row[0]);
        String nameValue = String.valueOf(row[1]);
        String vehicleValue = String.valueOf(row[2]);
        String classValue = String.valueOf(row[3]);
        String ratedValue = String.valueOf(row[4]);

        String[] info = {nameValue, vehicleValue, classValue, ratedValue};
        competitors.put(numberValue, info);

        Table.model.addRow(row);
        Table.table.setModel(Table.model);
    }

    private void createProgramDirectory() {
        if(!Files.exists(Paths.get(directoryName))) {
            try {
                File file = new File(directoryName);
                boolean created = file.mkdir();

                if(!created) {
                    saveStatus = false;
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
                    saveStatus = false;
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

                if(!created) {
                    saveStatus = false;
                    JOptionPane.showMessageDialog(null, "Rating directory could not be created", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createRatingFiles() {
        boolean status = true;

        for(String numberValue : competitors.keySet()) {
            String[] data = competitors.get(numberValue);

            String ratedValue = data[3];

            try {
                PrintWriter w = new PrintWriter(directoryName + "/" + session + "/ratings/" + numberValue + ".trr", "UTF-8");

                if(ratedValue.equals("NO")) {
                    w.println(ratedValue);
                }else {
                    //todo get current rating
                }

                w.close();
            }catch(FileNotFoundException | UnsupportedEncodingException e) {
                status = false;
                e.printStackTrace();
            }
        }

        if(!status) {
            JOptionPane.showMessageDialog(null, "Competitor ratings could not be generated", "Error", JOptionPane.ERROR_MESSAGE);
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

            classes.clear();

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

                    classes.remove(classValue, oldNumbers);
                    classes.put(classValue, numbers);
                }
            }

            for(String classValue : classes.keySet()) {
                String[] numbers = classes.get(classValue);

                for(String number : numbers) {
                    if(!number.isEmpty()) {
                        String[] data = competitors.get(number);

                        String nameValue = data[0];
                        String vehicleValue = data[1];
                        String ratedValue = data[3];

                        if(ratedValue.equals("YES")) {
                            //todo get rating file, calculate rating, sort array
                        }
                    }
                }
            }

            w.close();

            JOptionPane.showMessageDialog(null, "Results are successfully generated", "Success", JOptionPane.INFORMATION_MESSAGE);
        }catch(FileNotFoundException | UnsupportedEncodingException e) {
            JOptionPane.showMessageDialog(null, "Results could not be generated", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadData() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(directoryName));
        chooser.setDialogTitle("Select session folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            loadDirectory = String.valueOf(chooser.getSelectedFile());

            competitors.clear();

            for(int i = Table.model.getRowCount() - 1; i >= 0; i--) {
                Table.model.removeRow(i);
            }

            try{
                FileInputStream fis = new FileInputStream(loadDirectory + "/list.trl");
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                int i = 0;

                while((line = br.readLine()) != null) {
                    i++;

                    String numberValue = line.substring(0, line.indexOf(":"));
                    line = line.replaceFirst(numberValue + ":", "");

                    String nameValue = line.substring(0, line.indexOf(":"));
                    line = line.replaceFirst(nameValue + ":", "");

                    String vehicleValue = line.substring(0, line.indexOf(":"));
                    line = line.replaceFirst(vehicleValue + ":", "");

                    String classValue = line.substring(0, line.indexOf(":"));
                    line = line.replaceFirst(classValue + ":", "");

                    String ratedValue = line;

                    String[] info = {nameValue, vehicleValue, classValue, ratedValue};

                    competitors.put(numberValue, info);
                    addRow(new Object[]{numberValue, nameValue, vehicleValue, classValue, ratedValue});
                }

                in.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            loadStatus = false;
            JOptionPane.showMessageDialog(null, "No session folder is selected", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadRatings() {
        for(String numberValue : competitors.keySet()) {
            try{
                FileInputStream fis = new FileInputStream(loadDirectory + "/ratings/" + numberValue + ".trr");
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                int i = 0;

                while((line = br.readLine()) != null) {
                    i++;

                    if(line.equals("NO")) {
                        break;
                    }else {
                        //todo extract ratings
                    }
                }

                in.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void exportData() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(directoryName));
        chooser.setDialogTitle("Select session folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File srcDir = new File(directoryName);
            File destDir = new File(String.valueOf(chooser.getSelectedFile()) + "/TuningRef");

            boolean created = destDir.mkdir();

            if(!created) {
                JOptionPane.showMessageDialog(null, "Program folder could not be created", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                FileUtils.copyDirectory(srcDir, destDir);
            }catch(IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Program folder could not be copied", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }else {
            JOptionPane.showMessageDialog(null, "No device is selected", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
