package id.ac.pens.psdku.campuslosthub;

import id.ac.pens.psdku.campuslosthub.service.LostAndFoundManager;
import id.ac.pens.psdku.campuslosthub.view.ConsoleView;
import id.ac.pens.psdku.campuslosthub.view.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Entry point aplikasi CampusLostHub.
 * Tanpa argumen -> antarmuka modern GUI (Java Swing).
 * Dengan argumen "console" -> antarmuka Terminal / Console CLI.
 */
public class Main {

    public static void main(String[] args) {
        // Terapkan system look-and-feel agar tampilan native & rapi
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Gunakan look and feel bawaan jika gagal
        }

        LostAndFoundManager manager = new LostAndFoundManager();

        if (args.length > 0 && args[0].equalsIgnoreCase("console")) {
            new ConsoleView(manager).jalankan();
        } else {
            SwingUtilities.invokeLater(() -> new MainFrame(manager).setVisible(true));
        }
    }
}
