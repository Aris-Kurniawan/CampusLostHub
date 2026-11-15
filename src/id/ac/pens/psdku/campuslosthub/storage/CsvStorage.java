package id.ac.pens.psdku.campuslosthub.storage;

import id.ac.pens.psdku.campuslosthub.exception.StorageException;
import id.ac.pens.psdku.campuslosthub.model.BarangDokumen;
import id.ac.pens.psdku.campuslosthub.model.BarangElektronik;
import id.ac.pens.psdku.campuslosthub.model.Item;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Mengelola pembacaan dan penulisan data barang ke file CSV.
 */
public class CsvStorage {
    private static final String HEADER =
            "id,kategori,nama,deskripsi,lokasiDitemukan,tanggalDitemukan,status,extra1,extra2";

    private final Path filePath;

    public CsvStorage(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Path file CSV tidak boleh kosong.");
        }
        this.filePath = Paths.get(filePath);
    }

    public String getFilePath() {
        return filePath.toString();
    }

    /**
     * Memuat seluruh data barang dari file CSV.
     * Jika file belum ada, mengembalikan list kosong.
     * Baris yang rusak dilewati dengan peringatan agar aplikasi tidak crash.
     */
    public ArrayList<Item> load() throws StorageException {
        ArrayList<Item> items = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return items;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                if (lineNumber == 1 && trimmed.toLowerCase().startsWith("id,")) {
                    continue;
                }

                try {
                    Item item = parseLine(trimmed);
                    items.add(item);
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.err.println("[Peringatan CSV] Baris " + lineNumber
                            + " dilewati karena tidak valid: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new StorageException("Gagal membaca file CSV: " + filePath, e);
        }

        return items;
    }

    /**
     * Menyimpan seluruh daftar barang ke file CSV (menulis ulang isi file).
     */
    public void save(List<Item> items) throws StorageException {
        try {
            Path parent = filePath.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                writer.write(HEADER);
                writer.newLine();

                if (items != null) {
                    for (Item item : items) {
                        if (item != null) {
                            writer.write(item.toCsvString());
                            writer.newLine();
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new StorageException("Gagal menyimpan data ke file CSV: " + filePath, e);
        }
    }

    private Item parseLine(String line) {
        String[] cols = line.split(",", -1);
        if (cols.length != 9) {
            throw new IllegalArgumentException("Jumlah kolom harus 9, ditemukan " + cols.length);
        }

        String id = cols[0].trim();
        String kategori = cols[1].trim().toUpperCase();
        String nama = cols[2].trim();
        String deskripsi = cols[3].trim();
        String lokasi = cols[4].trim();
        String tanggal = cols[5].trim();
        String status = cols[6].trim().toUpperCase();
        String extra1 = cols[7].trim();
        String extra2 = cols[8].trim();

        Item item;
        if ("ELEKTRONIK".equals(kategori)) {
            item = new BarangElektronik(id, nama, deskripsi, lokasi, tanggal, extra1, extra2);
        } else if ("DOKUMEN".equals(kategori)) {
            item = new BarangDokumen(id, nama, deskripsi, lokasi, tanggal, extra1, extra2);
        } else {
            throw new IllegalArgumentException("Kategori tidak dikenal: " + kategori);
        }

        pulihkanStatus(item, status);
        return item;
    }

    private void pulihkanStatus(Item item, String status) {
        if (Item.STATUS_TERSEDIA.equals(status)) {
            return;
        }
        if (Item.STATUS_PROSES_KLAIM.equals(status)) {
            item.setStatus(Item.STATUS_PROSES_KLAIM);
            return;
        }
        if (Item.STATUS_SELESAI.equals(status)) {
            // Item baru berstatus TERSEDIA; transisi sah menuju SELESAI melewati PROSES_KLAIM.
            item.setStatus(Item.STATUS_PROSES_KLAIM);
            item.setStatus(Item.STATUS_SELESAI);
            return;
        }
        throw new IllegalArgumentException("Status tidak dikenal: " + status);
    }
}
