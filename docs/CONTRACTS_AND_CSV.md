# Spesifikasi Format File Penyimpanan & Kontrak Data CSV

Dokumen ini mendefinisikan struktur data teks untuk file penyimpanan persisten `lost_found_data.csv`.

---

## 1. Spesifikasi Teknis CSV

* **Nama Berkas:** `data/lost_found_data.csv`
* **Pemisah Kolom (Delimiter):** Koma (`,`) atau Titik Koma (`;`)
* **Encoding:** `UTF-8`
* **Format Tanggal:** `YYYY-MM-DD` (contoh: `2026-11-10`)
* **Format Identifier (ID):** Prefix `BRG-` diikuti nomor urut 4 digit, misal `BRG-1001`.

---

## 2. Struktur Baris CSV (Schema)

Setiap baris data dalam file CSV memiliki format berikut:

```csv
id,kategori,nama,deskripsi,lokasiDitemukan,tanggalDitemukan,status,extra1,extra2
```

### Keterangan Kolom:

| Index | Nama Kolom | Tipe Data | Deskripsi |
|:---:|---|---|---|
| 0 | `id` | String | ID unik barang (contoh: `BRG-1001`) |
| 1 | `kategori` | String | `ELEKTRONIK` atau `DOKUMEN` |
| 2 | `nama` | String | Nama barang temuan (contoh: `KTM PENS`) |
| 3 | `deskripsi` | String | Deskripsi ciri fisik umum (contoh: `Warna biru dalam dompet mika`) |
| 4 | `lokasiDitemukan` | String | Ruangan/lokasi di kampus (contoh: `Lab Jaringan Komputer`) |
| 5 | `tanggalDitemukan` | String | Format ISO `YYYY-MM-DD` |
| 6 | `status` | String | `TERSEDIA`, `PROSES_KLAIM`, atau `SELESAI` |
| 7 | `extra1` | String | Khusus Elektronik: `nomorSeri`<br>Khusus Dokumen: `nomorIdentitas` (NIM) |
| 8 | `extra2` | String | Khusus Elektronik: `brand`<br>Khusus Dokumen: `namaPemilik` |

---

## 3. Contoh Isi Berkas (`data/lost_found_data.csv`)

```csv
id,kategori,nama,deskripsi,lokasiDitemukan,tanggalDitemukan,status,extra1,extra2
BRG-1001,ELEKTRONIK,Flashdisk SanDisk 32GB,Warna merah hitam gantungan tali biru,Lab Pemrograman,2026-11-08,TERSEDIA,SD32G-98421,SanDisk
BRG-1002,DOKUMEN,KTM PENS D3 TI,Atas nama Aris Kurniawan foto jelas,Kantin PSDKU,2026-11-09,TERSEDIA,3125521004,Aris Kurniawan
BRG-1003,ELEKTRONIK,Charger Laptop Asus Type-C,Kabel terlilit rapi adaptor hitam,Perpustakaan Lt. 2,2026-11-09,PROSES_KLAIM,A19-065N2A,Asus
BRG-1004,DOKUMEN,STNK Motor Honda Vario,Plat nomor L 4521 AA dompet kulit coklat,Parkir Motor Barat,2026-11-07,SELESAI,L4521AA,Ahmad Dahlan
```

---

## 4. Mekanisme Serialisasi & Deserialisasi

1. **Serialisasi (`Item.toCsvString()`):**
   * Kelas turunan mengonversi atributnya ke string CSV menggunakan format di atas. Karakter koma pada teks deskripsi akan digantikan dengan spasi atau di-escape agar tidak merusak kolom.
2. **Deserialisasi (`LostAndFoundService.muatDariFile()`):**
   * Membaca per baris menggunakan `BufferedReader`.
   * Men-split baris dengan `line.split(",")`.
   * Memeriksa kolom `kategori` pada indeks 1:
     * Jika `ELEKTRONIK`, lakukan instansiasi `new BarangElektronik(...)` dengan parameter `extra1` sebagai `nomorSeri` dan `extra2` sebagai `brand`.
     * Jika `DOKUMEN`, lakukan instansiasi `new BarangDokumen(...)` dengan parameter `extra1` sebagai `nomorIdentitas` dan `extra2` sebagai `namaPemilik`.
   * Memasukkan objek yang baru dibuat ke dalam `ArrayList<Item>`.

---

## 5. Kepemilikan Modul dan Kontrak Tim

### Aris — Model dan Kontrak OOP

Aris menjaga kontrak `Item`, `Verifiable`, `BarangElektronik`, `BarangDokumen`, dan `Claimant`. Perubahan nama field, status, format kategori, atau method serialisasi harus diperbarui pada dokumen ini sebelum diterapkan ke storage.

### Bilu — Storage dan Service

Bilu menjadi pemilik `CsvStorage` dan `LostAndFoundService`. Hanya layer ini yang boleh membaca atau menulis `data/lost_found_data.csv`. Implementasi wajib mempertahankan urutan kolom dan aturan kategori pada schema di atas.

### Intan — Presentation

Intan menggunakan service untuk menampilkan dan mengubah data. `ConsoleView` atau GUI tidak boleh memanipulasi CSV secara langsung. Informasi rahasia pada `extra1` dan `extra2` hanya digunakan saat proses verifikasi klaim dan tidak ditampilkan pada katalog publik.

## 6. Kontrak Modular Minimum

```java
// Model milik Aris
boolean verifyClaim(String proofData);
String toCsvString();

// Service milik Bilu
void tambahBarang(Item item);
List<Item> getBarangAktif();
List<Item> cariBarang(String keyword);
boolean prosesKlaim(String id, String bukti, Claimant claimant);

// UI milik Intan
// Memanggil service, memvalidasi input, dan menampilkan hasil.
```

Setiap perubahan pada kontrak tersebut harus disertai pembaruan pada `ARCHITECTURE.md`, pengujian terkait, dan catatan perubahan.
