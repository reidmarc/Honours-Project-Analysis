import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI_Layer extends JFrame
{
    private APP_Layer appLayer;

    private JButton startButton;


    public GUI_Layer (APP_Layer appLayer)
    {
        this.appLayer = appLayer;

        createWindow();
    }

    // Creates the GUI window
    private void createWindow()
    {
        UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Arial", Font.PLAIN, 12));

        //Create and set up the window.
        JFrame frame = new JFrame("HQ GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setLayout(new GridLayout(2,1, 10, 10));
        frame.setLayout(new FlowLayout());
        frame.setResizable(false);

        setupStartButton(frame);

        //Display the window
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.toFront();
    }

    // Sets up the start button
    private void setupStartButton(JFrame frame)
    {
        // Creates the start button
        startButton = new JButton("Start Analysis");
        frame.add(startButton);

        // startButton listener
        startButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                appLayer.initialStartup();
            }
        });
    }














}
