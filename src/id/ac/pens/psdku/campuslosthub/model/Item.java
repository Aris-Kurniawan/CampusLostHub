package id.ac.pens.psdku.campuslosthub.model;

import id.ac.pens.psdku.campuslosthub.contract.Verifiable;

/** Data umum semua barang temuan. */
public abstract class Item implements Verifiable {
    public static final String STATUS_TERSEDIA = "TERSEDIA";
    public static final String STATUS_PROSES_KLAIM = "PROSES_KLAIM";
    public static final String STATUS_SELESAI = "SELESAI";

    private final String id;
    private final String nama;
    private final String deskripsi;
    private final String lokasiDitemukan;
    private final String tanggalDitemukan;
    private String status;
    private Claimant claimant;

    protected Item(String id, String nama, String deskripsi,
                   String lokasiDitemukan, String tanggalDitemukan) {
        this.id = wajib(id, "id");
        this.nama = wajib(nama, "nama");
        this.deskripsi = wajib(deskripsi, "deskripsi");
        this.lokasiDitemukan = wajib(lokasiDitemukan, "lokasiDitemukan");
        this.tanggalDitemukan = wajib(tanggalDitemukan, "tanggalDitemukan");
        this.status = STATUS_TERSEDIA;
    }

    private static String wajib(String nilai, String namaField) {
        if (nilai == null || nilai.trim().isEmpty()) {
            throw new IllegalArgumentException(namaField + " tidak boleh kosong");
        }
        return nilai.trim();
    }

    public String getId() { return id; }
    public String getNama() { return nama; }
    public String getDeskripsi() { return deskripsi; }
    public String getLokasiDitemukan() { return lokasiDitemukan; }
    public String getTanggalDitemukan() { return tanggalDitemukan; }
    public String getStatus() { return status; }
    public Claimant getClaimant() { return claimant; }
    public void setClaimant(Claimant claimant) { this.claimant = claimant; }

    public void setStatus(String statusBaru) {
        if (!statusValid(statusBaru)) {
            throw new IllegalArgumentException("Status tidak dikenal: " + statusBaru);
        }
        if (!transisiDiizinkan(status, statusBaru)) {
            throw new IllegalStateException("Transisi status tidak diizinkan: "
                    + status + " -> " + statusBaru);
        }
        status = statusBaru;
    }

    private boolean statusValid(String nilai) {
        return STATUS_TERSEDIA.equals(nilai)
                || STATUS_PROSES_KLAIM.equals(nilai)
                || STATUS_SELESAI.equals(nilai);
    }

    private boolean transisiDiizinkan(String saatIni, String berikutnya) {
        return saatIni.equals(berikutnya)
                || (STATUS_TERSEDIA.equals(saatIni)
                    && STATUS_PROSES_KLAIM.equals(berikutnya))
                || (STATUS_PROSES_KLAIM.equals(saatIni)
                    && (STATUS_SELESAI.equals(berikutnya)
                    || STATUS_TERSEDIA.equals(berikutnya)));
    }

    public abstract String getKategori();
    public abstract void displayInfo();
    public abstract String toCsvString();

    protected String csv(String nilai) {
        return nilai.replace(",", " ");
    }
}
