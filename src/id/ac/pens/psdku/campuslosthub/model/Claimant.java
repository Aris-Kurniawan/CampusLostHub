package id.ac.pens.psdku.campuslosthub.model;

/** Data orang yang mengajukan klaim barang. */
public class Claimant {
    private final String nama;
    private final String nim;
    private final String noHp;

    public Claimant(String nama, String nim, String noHp) {
        this.nama = wajib(nama, "nama");
        this.nim = wajib(nim, "nim");
        this.noHp = wajib(noHp, "noHp");
    }

    private static String wajib(String nilai, String namaField) {
        if (nilai == null || nilai.trim().isEmpty()) {
            throw new IllegalArgumentException(namaField + " tidak boleh kosong");
        }
        return nilai.trim();
    }

    public String getNama() { return nama; }
    public String getNim() { return nim; }
    public String getNoHp() { return noHp; }
}