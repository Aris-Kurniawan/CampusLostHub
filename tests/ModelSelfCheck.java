import id.ac.pens.psdku.campuslosthub.model.BarangDokumen;
import id.ac.pens.psdku.campuslosthub.model.BarangElektronik;
import id.ac.pens.psdku.campuslosthub.model.Item;

/** Self-check sederhana untuk model inti CampusLostHub. */
public class ModelSelfCheck {
    public static void main(String[] args) {
        Item elektronik = new BarangElektronik(
                "BRG-2001", "Flashdisk", "Merah hitam",
                "Lab PBO", "2026-11-15", "SN-123", "SanDisk");
        Item dokumen = new BarangDokumen(
                "BRG-2002", "KTM PENS", "Kartu mahasiswa",
                "Kantin", "2026-11-15", "3125521004", "Aris Kurniawan");

        check("ELEKTRONIK".equals(elektronik.getKategori()), "kategori elektronik");
        check("DOKUMEN".equals(dokumen.getKategori()), "kategori dokumen");
        check(elektronik.verifyClaim("SN123"), "klaim elektronik benar");
        check(!elektronik.verifyClaim("SALAH"), "klaim elektronik salah");
        check(dokumen.verifyClaim("3125521004"), "klaim dokumen dengan NIM");
        check(dokumen.verifyClaim("Aris Kurniawan"), "klaim dokumen dengan nama");
        check(!dokumen.verifyClaim("Orang Lain"), "klaim dokumen salah");
        check(elektronik.toCsvString().split(",", -1).length == 9, "CSV elektronik 9 kolom");
        check(dokumen.toCsvString().split(",", -1).length == 9, "CSV dokumen 9 kolom");

        elektronik.setStatus(Item.STATUS_PROSES_KLAIM);
        elektronik.setStatus(Item.STATUS_SELESAI);
        check(Item.STATUS_SELESAI.equals(elektronik.getStatus()), "transisi status valid");

        boolean ditolak = false;
        try {
            elektronik.setStatus(Item.STATUS_TERSEDIA);
        } catch (IllegalStateException e) {
            ditolak = true;
        }
        check(ditolak, "transisi status tidak valid ditolak");

        System.out.println("MODEL SELF-CHECK: PASS");
    }

    private static void check(boolean kondisi, String nama) {
        if (!kondisi) {
            throw new AssertionError("Gagal: " + nama);
        }
    }
}
