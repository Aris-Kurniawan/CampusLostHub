# Spesifikasi Sistem & Pemenuhan Kriteria PBO — CampusLostHub

Dokumen ini mendefinisikan seluruh kebutuhan fungsional, batasan sistem, dan pembuktian pemenuhan 9 kriteria penilaian dosen pengampu Praktikum PBO.

---

## 1. Kebutuhan Fungsional (Functional Requirements)

| Kode | Nama Fitur | Aktor | Deskripsi Perilaku Sistem |
|---|---|---|---|
| **FR-01** | Input Barang Temuan Baru | Penemu, Petugas | Sistem mencatat barang temuan dengan atribut umum dan khusus sesuai kategori, memberikan ID unik (`BRG-XXXX`), dan status awal `TERSEDIA`. |
| **FR-02** | Katalog Inventaris Aktif | Semua Aktor | Sistem menampilkan daftar barang temuan yang berstatus `TERSEDIA` dan `PROSES_KLAIM` dengan atribut publik (nama, kategori, lokasi, tanggal). Atribut rahasia disembunyikan. |
| **FR-03** | Pencarian Multi-Kriteria | Pemilik | Sistem mencari barang berdasarkan kecocokan kata kunci pada nama barang (*case-insensitive*) atau memfilter berdasarkan lokasi ruangan kampus. |
| **FR-04** | Pengajuan Klaim Kepemilikan | Pemilik | Sistem menerima data diri pemohon (`Claimant`: NIM, Nama, No. HP) dan meminta bukti rahasia verifikasi sesuai jenis barang. |
| **FR-05** | Verifikasi Klaim Polimorfik | Sistem | Sistem mengeksekusi method `verifyClaim(proof)` secara polimorfik: <br>• Elektronik: cek kecocokan Serial Number / IMEI.<br>• Dokumen: cek kecocokan Nama Pemilik / NIM.<br>Jika lolos, status berubah menjadi `PROSES_KLAIM`. |
| **FR-06** | Validasi Serah Terima Fisik | Petugas | Petugas mengonfirmasi serah terima fisik barang kepada pemilik sah dan mengubah status barang menjadi `SELESAI`. |
| **FR-07** | Pembatalan / Penolakan Klaim | Petugas | Petugas dapat mengembalikan status barang dari `PROSES_KLAIM` ke `TERSEDIA` jika saat pencocokan fisik pemohon gagal membuktikan kepemilikan. |
| **FR-08** | Persistensi File CSV | Sistem | Sistem memuat seluruh data dari `data/lost_found_data.csv` saat inisialisasi dan menuliskan perubahan setiap kali terjadi operasi mutasi. |
| **FR-09** | Validasi Input & Pencegahan Crash | Sistem | Sistem memvalidasi kelengkapan string, format tanggal (`YYYY-MM-DD`), dan menangani runtime exception tanpa menghentikan program secara mendadak. |

---

## 2. Pemenuhan 9 Kriteria Penilaian Dosen PBO

| No | Kriteria Juknis PBO | Implementasi Teknis di CampusLostHub |
|:---:|---|---|
| **1** | **Class dan Object (Min. 5 Class)** | Mengimplementasikan 7 entitas: `Verifiable` (Interface), `Item` (Abstract Class), `BarangElektronik`, `BarangDokumen`, `Claimant`, `LostAndFoundService`, dan `Main`. |
| **2** | **Constructor Berparameter** | `Item(id, nama, deskripsi, lokasi, tanggal)` menggunakan constructor berparameter. Subclass menggunakan `super(...)` untuk mengalirkan atribut induk. |
| **3** | **Encapsulation** | Seluruh atribut kelas berstatus `private`. Akses data diatur ketat via method getter dan setter dengan aturan validasi (*defensive coding*). |
| **4** | **Inheritance** | `BarangElektronik` dan `BarangDokumen` meng-extend kelas induk abstrak `Item`. |
| **5** | **Polymorphism** | Method overriding pada `verifyClaim(String proofData)`, `displayInfo()`, dan `toCsvString()`. Pemanggilan dilakukan secara dinamis melalui referensi tipe induk `Item`. |
| **6** | **Abstraction** | Penggunaan `abstract class Item` dengan abstract methods, serta `interface Verifiable` sebagai kontrak verifikasi sistem. |
| **7** | **Exception Handling** | Custom exception: `ItemNotFoundException`, `InvalidClaimException`. Blok `try-catch-finally` untuk operasi I/O dan input parsing. |
| **8** | **Collection Framework** | Menggunakan `ArrayList<Item>` untuk pengelolaan dinamis entitas barang dalam memori. |
| **9** | **File Handling** | Pembacaan (`BufferedReader`) dan penulisan (`BufferedWriter`) file teks terstruktur berformat CSV (`lost_found_data.csv`). |

---

## 3. Batasan Sistem (Constraints)
1. Single-user session (antarmuka berorientasi standalone desktop, peran dibedakan melalui menu/dialog).
2. Format tanggal standar ISO: `YYYY-MM-DD`.
3. Tidak menggunakan database server eksternal demi portabilitas saat presentasi dan evaluasi live coding.

---

## 4. Pembagian Modul Berdasarkan Role

### 4.1 Aris Kurniawan — Lead System Architect & Core OOP

Modul: `contract/`, `model/`, dan exception domain.

- Menetapkan `Verifiable`, `Item`, `BarangElektronik`, `BarangDokumen`, dan `Claimant`.
- Menjamin empat pilar OOP terlihat pada source code.
- Menetapkan aturan transisi status barang.
- Menyediakan method publik yang menjadi kontrak bagi service dan UI.

### 4.2 Sabilu Rosyada (Bilu) — Data Persistence & Exception

Modul: `service/`, `storage/`, dan `exception/`.

- Mengembangkan `LostAndFoundManager` dan mengelola `ArrayList<Item>`.
- Menjalankan pencarian, filter, klaim, serah terima, dan pembatalan.
- Membaca/menulis `data/lost_found_data.csv`.
- Menangani error file dan data yang tidak valid.

### 4.3 Intan Kusumaningarum — UI Controller & QA Tester

Modul: `Claimant`, `view/`, dan `Main.java`.

- Menyediakan menu, form, dan tampilan katalog.
- Mengembangkan model data `Claimant`.
- Memvalidasi input pengguna sebelum memanggil service.
- Menangani pesan sukses/gagal dan navigasi menu.
- Menjalankan matriks pengujian fungsional serta menyiapkan demo.
- Mendokumentasikan hasil pengujian dan menyiapkan video demonstrasi.

### 4.4 Aturan Integrasi

1. UI hanya memanggil public method pada `LostAndFoundService`.
2. Service hanya menggunakan public method pada `Item` dan turunannya.
3. Storage bertanggung jawab terhadap format CSV, bukan UI.
4. Logika verifikasi kepemilikan berada di subclass model, bukan di UI.
5. Setiap perubahan signature method dicatat dalam `docs/ARCHITECTURE.md` dan diuji ulang oleh pemilik modul terkait.
