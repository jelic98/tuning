import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Add {
    private JPanel panel;
    private JButton bDone;
    private JTextField tfName, tfVehicle, tfClass;
    private static JFrame frame;
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int width = (int) (screenSize.getWidth() * 0.25);
    private static int height = (int) (screenSize.getHeight() * 0.5);

    public Add() {
        frame = new JFrame();

        bDone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numberValue = 0;
                String nameValue = tfName.getText();
                String vehicleValue = tfVehicle.getText();
                String classValue = tfClass.getText();
                String ratedValue = "NO";

                if(nameValue.isEmpty() || vehicleValue.isEmpty() || classValue.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                }else {
                    Main main = new Main();
                    main.addRow(new Object[]{numberValue, nameValue, vehicleValue, classValue, ratedValue});

                    frame.dispose();
                }
            }
        });
    }

    public void show() {
        frame.setTitle("Add competitor");
        frame.setSize(new Dimension(width, height));
        frame.setLocation(screenSize.width / 2 - width / 2,screenSize.height / 2 - height / 2);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
