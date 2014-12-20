package in.nerdworks.serviceexplorertree;

import com.microsoftopentechnologies.intellij.components.ServerExplorerToolWindowFactory;

import javax.swing.*;
import java.awt.*;

public class Program {
    //Optionally set the look and feel.
    private static boolean useSystemLookAndFeel = false;

    public static void main(String[] a) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }

        //Create and set up the window.
        JFrame frame = new JFrame("Service Explorer Tree");
        frame.setPreferredSize(new Dimension(300, 600));
        //noinspection MagicConstant
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        ServerExplorerToolWindowFactory toolWindowFactory = new ServerExplorerToolWindowFactory();
        toolWindowFactory.createToolWindowContent(frame);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
