# Dokumentasi Role — Sabilu Rosyada (Bilu)

## 1. Identitas dan Peran

- **Nama:** Sabilu Rosyada (Bilu)
- **NRP:** 3125521009
- **Role:** Data Persistence & Exception
- **Fokus:** `LostAndFoundManager`, `ArrayList<Item>`, CSV persistence, exception handling, dan integritas data.

## 2. Tanggung Jawab Utama

Bilu bertanggung jawab menghubungkan model domain Aris dengan data CSV dan menyediakan operasi bisnis yang dipanggil oleh UI Intan. UI tidak boleh membaca atau menulis CSV secara langsung.

### Modul yang dikerjakan

```text
src/id/ac/pens/psdku/campuslosthub/
├── service/
│   └── LostAndFoundManager.java
├── storage/
│   └── CsvStorage.java
└── exception/
    ├── ItemNotFoundException.java
    ├── InvalidClaimException.java
    └── StorageException.java
```

### Pekerjaan

1. Membuat class `LostAndFoundManager` sebagai service/controller utama.
2. Mengelola `ArrayList<Item>` sebagai koleksi aktif.
2. Membuat `tambahBarang(Item item)`.
3. Membuat `getSemuaBarang()` dan `getBarangAktif()`.
4. Membuat pencarian berdasarkan keyword secara case-insensitive.
5. Membuat filter berdasarkan lokasi.
6. Membuat pencarian barang berdasarkan ID.
7. Menjalankan proses klaim melalui `verifyClaim` tanpa menyalin logika verifikasi subclass.
8. Mengubah status menjadi `PROSES_KLAIM`, `SELESAI`, atau kembali `TERSEDIA` melalui aturan service.
9. Menyimpan data ke `data/lost_found_data.csv`.
10. Memuat data CSV saat aplikasi dimulai.
11. Menangani file tidak tersedia, format baris rusak, dan exception input/output.

## 3. Struktur Data dan CSV

Lokasi data:

```text
data/lost_found_data.csv
```

Kolom:

```text
id,kategori,nama,deskripsi,lokasiDitemukan,tanggalDitemukan,status,extra1,extra2
```

Pemetaan:

| Kategori | `extra1` | `extra2` |
|---|---|---|
| `ELEKTRONIK` | Nomor seri/IMEI | Brand |
| `DOKUMEN` | Nomor identitas/NIM | Nama pemilik |

## 4. Kontrak untuk UI

Intan menggunakan `LostAndFoundManager` melalui method berikut:

```java
manager.tambahBarang(item);
manager.tampilkanSemuaBarang();
manager.cariBarang(keyword);
manager.filterByLokasi(lokasi);
manager.klaimBarang(id, bukti, claimant);
manager.selesaikanKlaim(id);
manager.batalkanKlaim(id);
```

Intan tidak boleh memanggil `CsvStorage` secara langsung. Setiap mutasi data wajib memanggil penyimpanan setelah operasi berhasil.

## 5. Aturan Validasi

- ID barang harus unik dan menggunakan format `BRG-XXXX`.
- Kategori hanya `ELEKTRONIK` atau `DOKUMEN`.
- Field wajib tidak boleh kosong.
- Tanggal menggunakan format `YYYY-MM-DD`.
- Status hanya `TERSEDIA`, `PROSES_KLAIM`, atau `SELESAI`.
- Koma pada isi field harus di-escape atau diganti agar tidak merusak kolom CSV.
- Kegagalan satu operasi tidak boleh menghentikan aplikasi secara mendadak.
- `ItemNotFoundException`, `InvalidClaimException`, dan `IOException` harus ditangani dengan pesan yang dapat dipahami.

## 6. Acceptance Check

- Data tetap ada setelah aplikasi ditutup dan dijalankan kembali.
- Data baru muncul di katalog setelah disimpan.
- Klaim valid mengubah status dengan benar.
- Klaim invalid tidak mengubah status.
- CSV rusak atau tidak tersedia menghasilkan pesan kesalahan yang dapat dipahami pengguna.
