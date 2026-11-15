package id.ac.pens.psdku.campuslosthub;

import id.ac.pens.psdku.campuslosthub.service.LostAndFoundManager;
import id.ac.pens.psdku.campuslosthub.view.ConsoleView;
import id.ac.pens.psdku.campuslosthub.view.MainFrame;

import javax.swing.SwingUtilities;

/**
 * Entry point aplikasi CampusLostHub.
 * Tanpa argumen -> tampilan GUI (Swing). Dengan argumen "console" -> tampilan console.
 */
public class Main {

    public static void main(String[] args) {
        // Catatan: sesuaikan dengan konstruktor LostAndFoundManager buatan Bilu
        // (misalnya kalau butuh path file CSV: "data/lost_found_data.csv")
        LostAndFoundManager manager = new LostAndFoundManager();

        if (args.length > 0 && args[0].equalsIgnoreCase("console")) {
            new ConsoleView(manager).jalankan();
        } else {
            SwingUtilities.invokeLater(() -> new MainFrame(manager).setVisible(true));
        }
    }
}