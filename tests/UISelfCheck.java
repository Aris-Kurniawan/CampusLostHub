import id.ac.pens.psdku.campuslosthub.model.Item;
import id.ac.pens.psdku.campuslosthub.service.LostAndFoundManager;
import id.ac.pens.psdku.campuslosthub.view.MainFrame;

import java.util.ArrayList;

/**
 * Self check untuk memverifikasi inisialisasi antarmuka Swing dan integrasi dengan LostAndFoundManager.
 */
public class UISelfCheck {
    public static void main(String[] args) {
        System.out.println("Running UI & Integration Self-Check...");

        LostAndFoundManager manager = new LostAndFoundManager("data/lost_found_data.csv");
        ArrayList<Item> items = manager.getAllItems();
        assertFalse(items.isEmpty(), "Data CSV awal tidak boleh kosong.");

        // Verifikasi inisialisasi MainFrame
        MainFrame frame = new MainFrame(manager);
        assertNotNull(frame, "MainFrame gagal diinisialisasi.");

        // Verifikasi penghitungan status
        int total = manager.getAllItems().size();
        int tersedia = manager.countByStatus(Item.STATUS_TERSEDIA);
        int prosesKlaim = manager.countByStatus(Item.STATUS_PROSES_KLAIM);
        int selesai = manager.countByStatus(Item.STATUS_SELESAI);
        assertTrue(total == (tersedia + prosesKlaim + selesai), "Jumlah total status harus konsisten.");

        frame.dispose();
        System.out.println("UI & INTEGRATION SELF-CHECK: PASS");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("[GAGAL] " + message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        assertTrue(!condition, message);
    }

    private static void assertNotNull(Object obj, String message) {
        assertTrue(obj != null, message);
    }
}
