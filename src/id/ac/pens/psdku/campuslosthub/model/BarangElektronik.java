package id.ac.pens.psdku.campuslosthub.model;

/** Barang temuan yang memiliki serial number atau IMEI. */
public class BarangElektronik extends Item {
    private final String nomorSeri;
    private final String brand;

    public BarangElektronik(String id, String nama, String deskripsi,
                            String lokasi, String tanggal,
                            String nomorSeri, String brand) {
        super(id, nama, deskripsi, lokasi, tanggal);
        this.nomorSeri = wajib(nomorSeri, "nomorSeri");
        this.brand = wajib(brand, "brand");
    }

    private static String wajib(String nilai, String namaField) {
        if (nilai == null || nilai.trim().isEmpty()) {
            throw new IllegalArgumentException(namaField + " tidak boleh kosong");
        }
        return nilai.trim();
    }

    public String getNomorSeri() { return nomorSeri; }
    public String getBrand() { return brand; }

    @Override
    public boolean verifyClaim(String proofData) {
        if (proofData == null) return false;
        String bukti = proofData.replace("-", "")
                .replace(" ", "").toLowerCase();
        String seri = nomorSeri.replace("-", "")
                .replace(" ", "").toLowerCase();
        return seri.equals(bukti);
    }

    @Override
    public String getKategori() { return "ELEKTRONIK"; }

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
                csv(nomorSeri), csv(brand));
    }
}
