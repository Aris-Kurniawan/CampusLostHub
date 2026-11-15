import id.ac.pens.psdku.campuslosthub.exception.InvalidClaimException;
import id.ac.pens.psdku.campuslosthub.exception.ItemNotFoundException;
import id.ac.pens.psdku.campuslosthub.model.BarangDokumen;
import id.ac.pens.psdku.campuslosthub.model.BarangElektronik;
import id.ac.pens.psdku.campuslosthub.model.Claimant;
import id.ac.pens.psdku.campuslosthub.model.Item;
import id.ac.pens.psdku.campuslosthub.service.LostAndFoundManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Self-check untuk modul Bilu (CsvStorage, LostAndFoundManager, dan Exception).
 */
public class ServiceStorageSelfCheck {
    public static void main(String[] args) throws Exception {
        // 1. Uji baca dataset utama (read-only)
        LostAndFoundManager prodReader = new LostAndFoundManager("data/lost_found_data.csv");
        cek(prodReader.getAllItems().size() == 4, "Harus memuat 4 data awal dari data/lost_found_data.csv");
        cek(prodReader.getBarangAktif().size() == 3, "Harus ada 3 barang aktif (BRG-1004 sudah SELESAI)");

        // 2. Uji CRUD, klaim, exception, dan persistensi pada file sementara
        Path tempCsv = Files.createTempFile("campuslosthub_test_", ".csv");
        try {
            Files.deleteIfExists(tempCsv); // Uji kondisi file belum ada
            LostAndFoundManager manager = new LostAndFoundManager(tempCsv.toString());
            cek(manager.getAllItems().isEmpty(), "File belum ada harus menghasilkan list kosong");

            Item laptop = new BarangElektronik("BRG-2001", "Laptop ThinkPad",
                    "Hitam stiker PENS", "Lab Jaringan", "2026-11-15", "SN-9988-XY", "Lenovo");
            Item ktm = new BarangDokumen("BRG-2002", "KTM Mahasiswa",
                    "Dompet kartu biru", "Kantin", "2026-11-15", "3125521009", "Sabilu Rosyada");

            manager.addItem(laptop);
            manager.addItem(ktm);
            cek(manager.getAllItems().size() == 2, "Tambah 2 barang harus tersimpan");
            cek(manager.searchByName("thinkpad").size() == 1, "Pencarian case-insensitive harus cocok");
            cek(manager.filterByLocation("kantin").size() == 1, "Filter lokasi harus cocok");

            // Uji klaim gagal (bukti salah -> InvalidClaimException)
            Claimant pemohon = new Claimant("Sabilu Rosyada", "3125521009", "08123456789");
            boolean gagalKlaim = false;
            try {
                manager.claimItem("BRG-2001", pemohon, "SN-SALAH");
            } catch (InvalidClaimException e) {
                gagalKlaim = true;
            }
            cek(gagalKlaim, "Bukti salah harus melempar InvalidClaimException");
            cek(Item.STATUS_TERSEDIA.equals(manager.findById("BRG-2001").getStatus()),
                    "Status tetap TERSEDIA saat klaim ditolak");

            // Uji klaim berhasil (polimorfik elektronik tanpa tanda hubung)
            cek(manager.claimItem("BRG-2001", pemohon, "sn9988xy"), "Klaim elektronik valid harus berhasil");
            cek(Item.STATUS_PROSES_KLAIM.equals(manager.findById("BRG-2001").getStatus()),
                    "Status harus berubah menjadi PROSES_KLAIM");

            // Uji batalkan klaim -> kembali TERSEDIA
            cek(manager.cancelClaim("BRG-2001"), "Pembatalan klaim harus berhasil");
            cek(Item.STATUS_TERSEDIA.equals(manager.findById("BRG-2001").getStatus()),
                    "Status harus kembali TERSEDIA setelah dibatalkan");

            // Uji klaim dokumen & serah terima -> SELESAI
            cek(manager.claimItem("BRG-2002", pemohon, "3125521009"), "Klaim dokumen valid harus berhasil");
            cek(manager.completeHandover("BRG-2002"), "Serah terima harus berhasil");
            cek(Item.STATUS_SELESAI.equals(manager.findById("BRG-2002").getStatus()),
                    "Status harus menjadi SELESAI");

            // Uji ItemNotFoundException
            boolean tidakKetemu = false;
            try {
                manager.cariById("BRG-9999");
            } catch (ItemNotFoundException e) {
                tidakKetemu = true;
            }
            cek(tidakKetemu, "ID tidak ada harus melempar ItemNotFoundException");

            // Uji persistensi: muat ulang dari file CSV yang sama
            LostAndFoundManager reloaded = new LostAndFoundManager(tempCsv.toString());
            cek(reloaded.getAllItems().size() == 2, "Data harus tetap 2 setelah reload CSV");
            cek(Item.STATUS_SELESAI.equals(reloaded.findById("BRG-2002").getStatus()),
                    "Status SELESAI harus tetap bertahan setelah reload CSV");

            System.out.println("SERVICE & STORAGE SELF-CHECK: PASS");
        } finally {
            Files.deleteIfExists(tempCsv);
        }
    }

    private static void cek(boolean kondisi, String pesan) {
        if (!kondisi) {
            throw new AssertionError("GAGAL: " + pesan);
        }
    }
}
