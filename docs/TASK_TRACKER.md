# Task Guide Mingguan — CampusLostHub

Dokumen ini memetakan instruksi Final Project PBO menjadi tugas yang dapat dilaporkan setiap minggu.

## Identitas Proyek

- **Judul:** Smart Campus Lost & Found Hub
- **Nama aplikasi:** CampusLostHub
- **Bahasa:** Java
- **Tim:** Kelompok 7
- **Anggota:** Aris Kurniawan, Sabilu Rosyada (Bilu), Intan Kusumaningarum
- **Durasi:** 4 minggu

## Aturan Pelaporan

Setiap akhir minggu, perbarui:

1. Checklist tugas yang selesai.
2. Bukti file, commit, screenshot, atau hasil pengujian.
3. Kendala yang ditemukan.
4. Rencana perbaikan minggu berikutnya.
5. `CHANGELOG.md` untuk setiap perubahan repository.

Status yang digunakan:

- `[ ]` Belum dikerjakan.
- `[~]` Sedang dikerjakan.
- `[x]` Selesai dan memiliki bukti.
- `[!]` Terhambat atau membutuhkan keputusan.

---

## Minggu 1 — Analisis dan Perancangan Sistem

**Target resmi:** Dokumen analisis kebutuhan dan desain sistem.

### Tugas tim

- [x] Menentukan judul dan studi kasus.
- [x] Menjelaskan masalah barang hilang/temuan di lingkungan kampus.
- [x] Menentukan tujuan dan batasan aplikasi.
- [x] Menentukan kebutuhan fungsional.
- [x] Menentukan tiga aktor: Finder, Claimant, dan Petugas/Administrator.
- [x] Menjelaskan proses pelaporan barang.
- [x] Menjelaskan proses pencarian dan filter.
- [x] Menjelaskan proses verifikasi klaim.
- [x] Menjelaskan proses serah terima fisik.
- [x] Membuat UML Use Case Diagram.
- [x] Membuat UML Class Diagram.
- [x] Menentukan pembagian tugas anggota.
- [x] Menyusun laporan Minggu 1.
- [x] Menentukan struktur modular repository.

### Pembagian tugas

#### Aris — Lead Architect & Core OOP

- [x] Menentukan rancangan domain `Item`.
- [x] Menentukan `BarangElektronik` dan `BarangDokumen`.
- [x] Menentukan interface `Verifiable`.
- [x] Menyusun UML Use Case dan Class Diagram.
- [x] Mendokumentasikan relasi inheritance, realization, aggregation, dan dependency.

#### Bilu — Data Persistence & Exception

- [x] Menentukan kebutuhan `ArrayList<Item>`.
- [x] Menentukan format CSV.
- [x] Menentukan kebutuhan `LostAndFoundManager`.
- [x] Menentukan kebutuhan custom exception dan validasi data.

#### Intan — UI Controller & QA Tester

- [x] Menentukan alur menu aplikasi.
- [x] Menentukan kebutuhan input `Claimant`.
- [x] Menentukan skenario pengujian awal.
- [x] Menentukan kebutuhan dokumentasi demo.

### Bukti

- `docs/SPECIFICATION.md`
- `docs/ARCHITECTURE.md`
- `docs/CONTRACTS_AND_CSV.md`
- `docs/ROLE_ARIS.md`
- `docs/ROLE_BILU.md`
- `docs/ROLE_INTAN.md`
- `Kelompok7_Final Project Minggu 1.docx`

### Laporan akhir Minggu 1

- **Sudah dilakukan:** Analisis kebutuhan, desain UML, pembagian role, format CSV, dan struktur modular.
- **Kendala:** Source code Java belum diimplementasikan.
- **Next step:** Mulai implementasi kontrak dan model inti.

---

## Minggu 2 — Implementasi Konsep OOP

**Target resmi:** Source code inti dengan penerapan konsep OOP.

### Tugas tim

- [ ] Membuat project Java yang dapat dikompilasi.
- [ ] Membuat package modular sesuai `src/id/ac/pens/psdku/campuslosthub/`.
- [ ] Membuat minimal lima class yang saling berinteraksi.
- [ ] Membuat constructor berparameter.
- [ ] Menerapkan atribut private dan getter/setter sesuai kebutuhan.
- [ ] Menerapkan inheritance.
- [ ] Menerapkan polymorphism melalui overriding/interface.
- [ ] Menerapkan abstraction melalui abstract class/interface.
- [ ] Mengimplementasikan logika domain dasar.
- [ ] Membuat self-check atau test kecil untuk model inti.

### Tugas Aris

- [ ] Membuat `contract/Verifiable.java`.
- [ ] Membuat abstract class `model/Item.java`.
- [ ] Membuat `model/BarangElektronik.java`.
- [ ] Membuat `model/BarangDokumen.java`.
- [ ] Menetapkan konstanta status dan aturan transisi.
- [ ] Mengimplementasikan `verifyClaim(String proofData)` secara polimorfik.
- [ ] Mengimplementasikan `getKategori()`, `displayInfo()`, dan `toCsvString()`.
- [ ] Memastikan seluruh field model teren kapsulasi.
- [ ] Memperbarui UML jika struktur implementasi berubah.

### Tugas Bilu

- [ ] Membuat `exception/ItemNotFoundException.java`.
- [ ] Membuat `exception/InvalidClaimException.java`.
- [ ] Membuat `exception/StorageException.java` jika diperlukan.
- [ ] Menetapkan kontrak awal `LostAndFoundManager`.
- [ ] Menyusun aturan validasi integritas data.

### Tugas Intan

- [ ] Membuat `model/Claimant.java`.
- [ ] Menentukan kontrak input menu.
- [ ] Menyusun format pesan validasi pengguna.
- [ ] Menyiapkan data uji untuk elektronik dan dokumen.

### Bukti yang wajib dikumpulkan

- Source code Java.
- Screenshot struktur package.
- Bukti compile berhasil.
- Self-check model.
- Catatan penerapan empat pilar OOP.
- Perubahan dicatat di `CHANGELOG.md`.

### Kriteria selesai

- [ ] Project dapat dikompilasi.
- [ ] Object dapat dibuat melalui constructor.
- [ ] Subclass dapat dipakai melalui referensi `Item`.
- [ ] Verifikasi elektronik dan dokumen menggunakan implementasi berbeda.
- [ ] Error domain dapat dilempar dan ditangani.

---

## Minggu 3 — Pengembangan dan Integrasi Aplikasi

**Target resmi:** Aplikasi dengan fitur utama yang sudah berjalan.

### Tugas tim

- [ ] Mengembangkan fitur tambah, tampil, ubah status, dan hapus/arsip data.
- [ ] Mengembangkan pencarian data.
- [ ] Membuat antarmuka Console atau GUI.
- [ ] Menggunakan `ArrayList` untuk koleksi objek.
- [ ] Membaca data dari CSV saat startup.
- [ ] Menulis perubahan data ke CSV.
- [ ] Mengintegrasikan exception handling.
- [ ] Mengintegrasikan seluruh class.
- [ ] Melakukan pengujian awal dan debugging.

### Tugas Aris

- [ ] Meninjau kesesuaian model dengan service dan UI.
- [ ] Memastikan logika verifikasi tetap berada pada subclass model.
- [ ] Memeriksa aturan transisi status.
- [ ] Meninjau ulang UML berdasarkan source code aktual.
- [ ] Membantu debugging integrasi kontrak antarkelas.

### Tugas Bilu

- [ ] Membuat `service/LostAndFoundManager.java`.
- [ ] Mengelola `ArrayList<Item>`.
- [ ] Membuat operasi tambah dan tampil data.
- [ ] Membuat pencarian berdasarkan keyword.
- [ ] Membuat filter berdasarkan lokasi.
- [ ] Membuat proses klaim, serah terima, dan pembatalan.
- [ ] Membuat `storage/CsvStorage.java`.
- [ ] Membaca dan menulis `data/lost_found_data.csv`.
- [ ] Menangani file hilang, baris rusak, dan exception I/O.

### Tugas Intan

- [ ] Membuat `Main.java`.
- [ ] Membuat menu interaktif.
- [ ] Membuat form pelaporan elektronik dan dokumen.
- [ ] Membuat form pencarian dan filter.
- [ ] Membuat form pengajuan klaim.
- [ ] Membuat alur serah terima dan pembatalan klaim.
- [ ] Membuat validasi input.
- [ ] Menampilkan data tanpa membocorkan bukti rahasia.
- [ ] Menyusun test case awal.

### Kriteria selesai

- [ ] Aplikasi dapat dijalankan dari entry point.
- [ ] Data CSV berhasil dimuat.
- [ ] Data baru dapat disimpan.
- [ ] Pencarian berjalan.
- [ ] Klaim valid dan invalid menghasilkan status yang benar.
- [ ] Input salah tidak menyebabkan aplikasi crash.
- [ ] Perubahan dicatat di `CHANGELOG.md`.

---

## Minggu 4 — Pengujian dan Finalisasi

**Target resmi:** Aplikasi final, dokumentasi, dan presentasi proyek.

### Tugas tim

- [ ] Melakukan pengujian keseluruhan aplikasi.
- [ ] Memperbaiki bug.
- [ ] Menyelesaikan laporan BAB I–V.
- [ ] Membuat screenshot aplikasi dan hasil pengujian.
- [ ] Membuat video demonstrasi berdurasi 5–10 menit.
- [ ] Menyiapkan slide presentasi.
- [ ] Melakukan simulasi tanya jawab dan live coding.
- [ ] Memastikan seluruh anggota memahami seluruh sistem.

### Matriks pengujian minimum

- [ ] Tambah barang elektronik dengan data valid.
- [ ] Tambah barang dokumen dengan data valid.
- [ ] Input field kosong.
- [ ] Input tanggal tidak valid.
- [ ] Pencarian keyword cocok.
- [ ] Pencarian tanpa hasil.
- [ ] Filter lokasi.
- [ ] Klaim dengan bukti benar.
- [ ] Klaim dengan bukti salah.
- [ ] Konfirmasi serah terima.
- [ ] Pembatalan klaim.
- [ ] Persistensi setelah aplikasi dijalankan ulang.
- [ ] File CSV tidak tersedia.
- [ ] Baris CSV rusak.

### Isi laporan final

- [ ] BAB I — Pendahuluan.
- [ ] BAB II — Analisis dan Perancangan Sistem.
- [ ] BAB III — Implementasi.
- [ ] BAB IV — Pengujian dan Hasil.
- [ ] BAB V — Kesimpulan.

### Video demonstrasi

- [ ] Penjelasan singkat masalah dan aplikasi.
- [ ] Demonstrasi pelaporan barang.
- [ ] Demonstrasi pencarian.
- [ ] Demonstrasi klaim valid.
- [ ] Demonstrasi klaim invalid.
- [ ] Demonstrasi persistensi CSV.
- [ ] Penjelasan empat pilar OOP.
- [ ] Penjelasan pembagian tugas anggota.

### Presentasi individu

Setiap anggota harus mampu menjelaskan:

- [ ] Modul yang dikerjakan.
- [ ] Alur kerja aplikasi.
- [ ] Alasan penggunaan setiap class.
- [ ] Implementasi encapsulation, inheritance, polymorphism, dan abstraction.
- [ ] Exception handling dan file handling.
- [ ] Perubahan sederhana pada kode saat live coding.

### Kriteria selesai akhir

- [ ] Source code lengkap dan dapat dijalankan.
- [ ] Struktur folder rapi.
- [ ] Laporan final selesai.
- [ ] Bukti pengujian tersedia.
- [ ] Video selesai.
- [ ] Presentasi selesai.
- [ ] Semua perubahan terakhir dicatat di `CHANGELOG.md`.

---

## Ringkasan Status Pelaporan

| Minggu | Target | Status | Bukti utama |
|---|---|---|---|
| 1 | Analisis dan desain | Selesai | Dokumen analisis, UML, role, dan arsitektur |
| 2 | Core OOP | Belum mulai | Source code model dan self-check |
| 3 | Integrasi aplikasi | Belum mulai | Aplikasi berjalan dan CSV persistence |
| 4 | Finalisasi | Belum mulai | Laporan, testing, video, dan presentasi |
