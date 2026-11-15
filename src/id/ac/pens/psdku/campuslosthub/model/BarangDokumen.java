package id.ac.pens.psdku.campuslosthub.model;

/** Barang temuan berupa dokumen identitas atau surat berharga. */
public class BarangDokumen extends Item {
    private final String nomorIdentitas;
    private final String namaPemilik;

    public BarangDokumen(String id, String nama, String deskripsi,
                         String lokasi, String tanggal,
                         String nomorIdentitas, String namaPemilik) {
        super(id, nama, deskripsi, lokasi, tanggal);
        this.nomorIdentitas = wajib(nomorIdentitas, "nomorIdentitas");
        this.namaPemilik = wajib(namaPemilik, "namaPemilik");
    }

    private static String wajib(String nilai, String namaField) {
        if (nilai == null || nilai.trim().isEmpty()) {
            throw new IllegalArgumentException(namaField + " tidak boleh kosong");
        }
        return nilai.trim();
    }

    public String getNomorIdentitas() { return nomorIdentitas; }
    public String getNamaPemilik() { return namaPemilik; }

    @Override
    public boolean verifyClaim(String proofData) {
        if (proofData == null) return false;
        String bukti = proofData.trim().toLowerCase();
        return nomorIdentitas.equalsIgnoreCase(bukti)
                || namaPemilik.equalsIgnoreCase(bukti);
    }

    @Override
    public String getKategori() { return "DOKUMEN"; }

    @Override
    public void displayInfo() {
        System.out.println(getId() + " | " + getKategori() + " | "
                + getNama() + " | " + getLokasiDitemukan()
                + " | " + getStatus());
    }

    @Override
    public String toCsvString() {
        return String.join(",", csv(getId()), getKategori(), csv(getNama()),
                csv(getDeskripsi()), csv(getLokasiDitemukan()),
                csv(getTanggalDitemukan()), getStatus(),
                csv(nomorIdentitas), csv(namaPemilik));
    }
}
