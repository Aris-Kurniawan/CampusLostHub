# Task Tracker & Jadwal Sprint 4 Minggu — CampusLostHub

Dokumen pelacakan tugas ini memetakan milestone dan target progres per minggu sesuai instruksi resmi Final Project PBO.

---

## Ringkasan Milestone 4 Minggu

```text
[Minggu 1: Selesai] ➔ Analisis Kebutuhan, Desain UML & Arsitektur Kelas
[Minggu 2: Antrean] ➔ Implementasi Core OOP (Model, Interface, Hierarchy, & Unit Test)
[Minggu 3: Antrean] ➔ Service Layer, ArrayList, File Handling CSV, & UI View
[Minggu 4: Antrean] ➔ Pengujian Komprehensif, Video Demo, & Laporan Akhir
```

---

## 1. Minggu 1 — Analisis dan Perancangan Sistem (Target: Hari Ini)

- [x] Menentukan judul proyek dan studi kasus domain kampus (`Smart Campus Lost & Found Hub`).
- [x] Mengidentifikasi 3 akar permasalahan utama (fragmentasi info, ketiadaan inventaris, verifikasi lemah).
- [x] Menentukan kebutuhan fungsional aplikasi (FR-01 s/d FR-09).
- [x] Menentukan 3 aktor sistem dan merumuskan 4 alur proses bisnis.
- [x] Menyusun UML Use Case Diagram lengkap dengan relasi `<<include>>`.
- [x] Menyusun UML Class Diagram lengkap memenuhi 9 kriteria PBO.
- [x] Menentukan pembagian peran adil dan seimbang untuk 3 mahasiswa.
- [x] Menyusun dokumen laporan kemajuan Minggu 1 (`Kelompok7_Final Project Minggu 1.docx`).
- [x] Inisialisasi struktur repositori dan dokumentasi teknis (`SPECIFICATION.md`, `ARCHITECTURE.md`, `CONTRACTS_AND_CSV.md`).

---

## 2. Minggu 2 — Implementasi Konsep OOP (Target Minggu Depan)

Fokus pengerjaan: Arsitektur kelas inti di package `id.ac.pens.psdku.campuslosthub.model` dan `contract`.

- [ ] **Interface `Verifiable`:**
  - [ ] Deklarasi method `boolean verifyClaim(String proofData)`.
- [ ] **Abstract Class `Item`:**
  - [ ] Deklarasi atribut private (`id`, `nama`, `deskripsi`, `lokasiDitemukan`, `tanggalDitemukan`, `status`).
  - [ ] Implementasi constructor berparameter dengan validasi.
  - [ ] Implementasi getter dan setter untuk setiap atribut.
  - [ ] Deklarasi abstract methods: `getKategori()`, `displayInfo()`, `toCsvString()`.
- [ ] **Subclass `BarangElektronik`:**
  - [ ] Deklarasi atribut private `nomorSeri` dan `brand`.
  - [ ] Implementasi constructor dengan `super(...)`.
  - [ ] Implementasi method overriding `verifyClaim(proofData)`.
  - [ ] Implementasi method overriding `displayInfo()` dan `toCsvString()`.
- [ ] **Subclass `BarangDokumen`:**
  - [ ] Deklarasi atribut private `nomorIdentitas` dan `namaPemilik`.
  - [ ] Implementasi constructor dengan `super(...)`.
  - [ ] Implementasi method overriding `verifyClaim(proofData)`.
  - [ ] Implementasi method overriding `displayInfo()` dan `toCsvString()`.
- [ ] **Class `Claimant`:**
  - [ ] Deklarasi atribut private `nim`, `nama`, `noHp`.
  - [ ] Implementasi getter dan method `getDetail()`.
- [ ] **Custom Exception Classes:**
  - [ ] `ItemNotFoundException.java`
  - [ ] `InvalidClaimException.java`

---

## 3. Minggu 3 — Pengembangan dan Integrasi Sistem

Fokus pengerjaan: Logika bisnis, manipulasi `ArrayList`, File I/O CSV, dan Antarmuka Pengguna.

- [ ] **Service Layer (`LostAndFoundService.java`):**
  - [ ] Inisialisasi `ArrayList<Item> daftarBarang`.
  - [ ] Method `tambahBarang(Item item)`.
  - [ ] Method `getSemuaBarang()` dan `getBarangAktif()`.
  - [ ] Method `cariBarang(String keyword)` (case-insensitive substring match).
  - [ ] Method `filterByLokasi(String lokasi)`.
  - [ ] Method `prosesKlaim(String id, String bukti, Claimant claimant)` dengan exception handling.
  - [ ] Method `selesaikanKlaim(String id)`.
- [ ] **File Handling CSV (`CsvStorage.java` / Service Persistence):**
  - [ ] Implementasi `simpanKeFile()` menggunakan `BufferedWriter` / `FileWriter`.
  - [ ] Implementasi `muatDariFile()` menggunakan `BufferedReader` / `FileReader` dengan parsing CSV.
  - [ ] Penanganan fallback otomatis jika berkas CSV belum ada di direktori `data/`.
- [ ] **User Interface (`Main.java` / `ConsoleView.java`):**
  - [ ] Menu interaktif berbasis teks dengan navigasi angka yang rapi.
  - [ ] Form input pelaporan barang baru dengan validasi data kosong.
  - [ ] Tampilan katalog dalam bentuk tabel konsol bergaris rapi.
  - [ ] Form klaim kepemilikan dan verifikasi bukti.
  - [ ] Opsi integrasi GUI Java Swing sederhana jika waktu mencukupi.

---

## 4. Minggu 4 — Pengujian, Video Demo, dan Finalisasi

Fokus pengerjaan: Validasi fungsional, pembuatan media demonstrasi, dan penyusunan laporan akhir.

- [ ] **Skenario Pengujian (Testing Matrix):**
  - [ ] Uji input normal & validasi kesalahan input.
  - [ ] Uji verifikasi klaim lolos (bukti cocok).
  - [ ] Uji verifikasi klaim ditolak (bukti salah / `InvalidClaimException`).
  - [ ] Uji persistensi data (tutup aplikasi lalu jalankan kembali, data tetap tersimpan).
- [ ] **Penyelesaian Laporan Akhir Final Project (BAB I s/d BAB V):**
  - [ ] BAB III: Implementasi kode program per kelas.
  - [ ] BAB IV: Tangkapan layar hasil pengujian tiap skenario.
  - [ ] BAB V: Kesimpulan dan saran pengembangan sistem.
- [ ] **Produksi Video Demonstrasi Aplikasi (5–10 Menit):**
  - [ ] Perkenalan tim dan pembagian tugas.
  - [ ] Demonstrasi alur pelaporan, pencarian, dan verifikasi klaim.
  - [ ] Penjelasan implementasi 4 pilar OOP langsung pada source code.
- [ ] **Persiapan Presentasi & Sesi Tanya Jawab Dosen.**
