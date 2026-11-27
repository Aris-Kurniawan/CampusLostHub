package id.ac.pens.psdku.campuslosthub.service;

import id.ac.pens.psdku.campuslosthub.exception.InvalidClaimException;
import id.ac.pens.psdku.campuslosthub.exception.ItemNotFoundException;
import id.ac.pens.psdku.campuslosthub.exception.StorageException;
import id.ac.pens.psdku.campuslosthub.model.Claimant;
import id.ac.pens.psdku.campuslosthub.model.Item;
import id.ac.pens.psdku.campuslosthub.storage.CsvStorage;

import java.util.ArrayList;

/**
 * Service/controller utama yang mengelola daftar barang (ArrayList<Item>),
 * alur klaim, pencarian, serta sinkronisasi dengan CsvStorage.
 */
public class LostAndFoundManager {
    private static final String DEFAULT_CSV_PATH = "data/lost_found_data.csv";

    private final ArrayList<Item> daftarBarang;
    private final CsvStorage storage;

    public LostAndFoundManager() {
        this(DEFAULT_CSV_PATH);
    }

    public LostAndFoundManager(String storageFilePath) {
        this.storage = new CsvStorage(storageFilePath);
        this.daftarBarang = new ArrayList<>();
        try {
            muatDariFile();
        } catch (StorageException e) {
            System.err.println("[Peringatan] Gagal memuat data awal: " + e.getMessage());
        }
    }

    /** Menambahkan barang baru setelah validasi ID unik dan menyimpannya ke CSV. */
    public void addItem(Item item) throws StorageException {
        if (item == null) {
            throw new IllegalArgumentException("Data barang tidak boleh null.");
        }
        if (findById(item.getId()) != null) {
            throw new IllegalArgumentException("ID barang sudah terdaftar: " + item.getId());
        }

        daftarBarang.add(item);
        try {
            simpanKeFile();
        } catch (StorageException e) {
            daftarBarang.remove(daftarBarang.size() - 1);
            throw e;
        }
    }

    /** Mengembalikan salinan daftar seluruh barang. */
    public ArrayList<Item> getAllItems() {
        return new ArrayList<>(daftarBarang);
    }

    /** Mengembalikan daftar barang yang masih aktif (TERSEDIA atau PROSES_KLAIM). */
    public ArrayList<Item> getBarangAktif() {
        ArrayList<Item> hasil = new ArrayList<>();
        for (Item item : daftarBarang) {
            if (!Item.STATUS_SELESAI.equals(item.getStatus())) {
                hasil.add(item);
            }
        }
        return hasil;
    }

    /** Mencari barang berdasarkan kata kunci nama secara case-insensitive. */
    public ArrayList<Item> searchByName(String keyword) {
        ArrayList<Item> hasil = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return hasil;
        }
        String kata = keyword.trim().toLowerCase();
        for (Item item : daftarBarang) {
            if (item.getNama().toLowerCase().contains(kata)) {
                hasil.add(item);
            }
        }
        return hasil;
    }

    /** Memfilter barang berdasarkan lokasi ditemukan secara case-insensitive. */
    public ArrayList<Item> filterByLocation(String lokasi) {
        ArrayList<Item> hasil = new ArrayList<>();
        if (lokasi == null || lokasi.trim().isEmpty()) {
            return hasil;
        }
        String target = lokasi.trim().toLowerCase();
        for (Item item : daftarBarang) {
            if (item.getLokasiDitemukan().toLowerCase().contains(target)) {
                hasil.add(item);
            }
        }
        return hasil;
    }

    /** Mencari barang berdasarkan ID (mengembalikan null jika tidak ditemukan). */
    public Item findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        String target = id.trim();
        for (Item item : daftarBarang) {
            if (item.getId().equalsIgnoreCase(target)) {
                return item;
            }
        }
        return null;
    }

    /** Mencari barang berdasarkan ID dan melempar ItemNotFoundException jika tidak ada. */
    public Item cariById(String id) throws ItemNotFoundException {
        Item item = findById(id);
        if (item == null) {
            throw new ItemNotFoundException("Barang dengan ID '" + id + "' tidak ditemukan.");
        }
        return item;
    }

    /**
     * Memproses pengajuan klaim dengan memanggil verifyClaim(bukti) secara polimorfik.
     * Jika cocok, status berubah menjadi PROSES_KLAIM dan disimpan ke CSV.
     */
    public boolean claimItem(String id, Claimant claimant, String bukti)
            throws ItemNotFoundException, InvalidClaimException, StorageException {
        if (claimant == null) {
            throw new InvalidClaimException("Data pengklaim (Claimant) wajib diisi.");
        }
        if (bukti == null || bukti.trim().isEmpty()) {
            throw new InvalidClaimException("Bukti kepemilikan tidak boleh kosong.");
        }

        Item item = cariById(id);
        if (!Item.STATUS_TERSEDIA.equals(item.getStatus())) {
            throw new InvalidClaimException("Barang " + item.getId()
                    + " tidak dapat diklaim karena berstatus " + item.getStatus() + ".");
        }

        if (!item.verifyClaim(bukti)) {
            throw new InvalidClaimException("Bukti kepemilikan tidak cocok dengan data barang.");
        }

        item.setStatus(Item.STATUS_PROSES_KLAIM);
        item.setClaimant(claimant);
        simpanKeFile();
        return true;
    }

    /** Menyelesaikan serah terima fisik barang (PROSES_KLAIM -> SELESAI). */
    public boolean completeHandover(String id)
            throws ItemNotFoundException, InvalidClaimException, StorageException {
        Item item = cariById(id);
        if (!Item.STATUS_PROSES_KLAIM.equals(item.getStatus())) {
            throw new InvalidClaimException("Serah terima hanya bisa dilakukan untuk barang berstatus PROSES_KLAIM.");
        }

        item.setStatus(Item.STATUS_SELESAI);
        simpanKeFile();
        return true;
    }

    /** Membatalkan klaim yang sedang berjalan (PROSES_KLAIM -> TERSEDIA). */
    public boolean cancelClaim(String id)
            throws ItemNotFoundException, InvalidClaimException, StorageException {
        Item item = cariById(id);
        if (!Item.STATUS_PROSES_KLAIM.equals(item.getStatus())) {
            throw new InvalidClaimException("Pembatalan hanya bisa dilakukan untuk barang berstatus PROSES_KLAIM.");
        }

        item.setStatus(Item.STATUS_TERSEDIA);
        item.setClaimant(null);
        simpanKeFile();
        return true;
    }

    /** Menghapus barang berdasarkan ID dari daftar dan memperbarui file CSV. */
    public boolean deleteItem(String id) throws ItemNotFoundException, StorageException {
        Item item = cariById(id);
        daftarBarang.remove(item);
        simpanKeFile();
        return true;
    }

    /** Menghitung jumlah barang berdasarkan status tertentu. */
    public int countByStatus(String status) {
        if (status == null) return 0;
        int count = 0;
        for (Item item : daftarBarang) {
            if (status.equalsIgnoreCase(item.getStatus())) {
                count++;
            }
        }
        return count;
    }

    /** Menyimpan seluruh daftar barang ke file CSV. */
    public void simpanKeFile() throws StorageException {
        storage.save(daftarBarang);
    }

    /** Memuat ulang seluruh daftar barang dari file CSV. */
    public void muatDariFile() throws StorageException {
        ArrayList<Item> loaded = storage.load();
        daftarBarang.clear();
        daftarBarang.addAll(loaded);
    }

    // --- Alias kontrak berbahasa Indonesia sesuai dokumentasi ROLE_BILU.md ---

    public void tambahBarang(Item item) throws StorageException {
        addItem(item);
    }

    public ArrayList<Item> getSemuaBarang() {
        return getAllItems();
    }

    public ArrayList<Item> cariBarang(String keyword) {
        return searchByName(keyword);
    }

    public ArrayList<Item> filterByLokasi(String lokasi) {
        return filterByLocation(lokasi);
    }

    public boolean prosesKlaim(String id, String bukti, Claimant claimant)
            throws ItemNotFoundException, InvalidClaimException, StorageException {
        return claimItem(id, claimant, bukti);
    }

    public boolean selesaikanKlaim(String id)
            throws ItemNotFoundException, InvalidClaimException, StorageException {
        return completeHandover(id);
    }

    public boolean batalkanKlaim(String id)
            throws ItemNotFoundException, InvalidClaimException, StorageException {
        return cancelClaim(id);
    }

    public boolean hapusBarang(String id)
            throws ItemNotFoundException, StorageException {
        return deleteItem(id);
    }
}
