# Changelog — CampusLostHub

Semua perubahan penting pada repository dicatat di dokumen ini.

Format versi mengikuti tahap pengerjaan proyek, bukan klaim rilis produk.

## [Unreleased]

### 2026-11-27 — Pembaruan Panduan Akses GUI di README.md

#### Changed
- `README.md`: menambahkan `-encoding UTF-8` pada perintah kompilasi `javac` serta mendokumentasikan panduan akses GUI Mode Mahasiswa (3 layar) dan Mode Petugas Pos (4 layar + kanvas TTD & akun login).

### 2026-11-27 — Implementasi Desain FigCraft (Finexy) ke Frontend Java Swing

#### Added
- `src/id/ac/pens/psdku/campuslosthub/view/SignatureDialog.java`: modal kanvas tanda tangan interaktif untuk berita acara serah terima barang di Pos Satpam.

#### Changed
- `src/id/ac/pens/psdku/campuslosthub/view/UITheme.java`: menerapkan palet Finexy (`#F4F5F7` canvas, `#171717` charcoal pill CTA, `#F05423` coral focal accent, `#FFF7ED` highlight callout) beserta `RoundedPanel`, `PillButton`, `buatKpiCard`, serta tinggi standar `34px` pada `buatTextField`.
- `src/id/ac/pens/psdku/campuslosthub/view/AdminLoginDialog.java`: memperbaiki ukuran dialog (`460x460`), menggunakan `BoxLayout` agar input `Username` & `Password` tidak terpotong vertikal, serta mencegah teks tombol `"Masuk ke Panel Petugas"` terpotong.
- `src/id/ac/pens/psdku/campuslosthub/view/ClaimDialog.java`: menyelaraskan UI modal klaim, menambahkan unggah foto/bukti unik opsional via `JFileChooser`, dan `JScrollPane` vertikal agar seluruh form dapat di-scroll.
- `src/id/ac/pens/psdku/campuslosthub/view/LaporBarangDialog.java`: menyelaraskan UI form laporan dengan kotak highlight coral untuk atribut rahasia dinamis serta menambahkan `JScrollPane` vertikal.
- `src/id/ac/pens/psdku/campuslosthub/view/MainFrame.java`: mengimplementasikan navbar pill dinamis, 3 layar mahasiswa (*Katalog Barang*, *Laporkan Temuan*, *Panduan Lapor & Klaim*), serta 4 layar petugas (*Dashboard Kendali*, *Verifikasi Klaim*, *Semua Inventaris*, *Riwayat Selesai*).

#### Tests
- Kompilasi: `javac -d out -encoding UTF-8 (seluruh source code src)` -> Berhasil.
- `git diff --check` -> Bersih.

### 2026-11-27 — Relasi In-Memory Claimant pada Item & Tampilan Pemohon di Verifikasi Pos

#### Changed
- `src/id/ac/pens/psdku/campuslosthub/model/Item.java`: menambahkan field `claimant` beserta `getClaimant()` dan `setClaimant(Claimant)` untuk menyimpan relasi pemohon di memori tanpa mengubah format 9 kolom CSV.
- `src/id/ac/pens/psdku/campuslosthub/service/LostAndFoundManager.java`: menyimpan `claimant` ke objek `Item` saat `claimItem(...)` berhasil dan mereset ke `null` saat `cancelClaim(...)`.
- `src/id/ac/pens/psdku/campuslosthub/view/MainFrame.java`: menampilkan nama, NIM, dan nomor HP/WA pemohon (`Claimant`) pada panel detail `Verifikasi Klaim & Pos`.

#### Tests
- Kompilasi: `javac -d out -encoding UTF-8 (seluruh source code src)` -> Berhasil.
- `git diff --check` -> Bersih.

### 2026-11-23 — Penyelarasan Layar Laporkan Temuan Berbasis Model Tabel & Dialog

#### Added
- `src/id/ac/pens/psdku/campuslosthub/view/LaporBarangDialog.java`: modal dialog untuk pencatatan formulir barang temuan baru secara ringkas dan terfokus.

#### Changed
- `src/id/ac/pens/psdku/campuslosthub/view/MainFrame.java`: menyelaraskan tampilan menu "Laporkan Temuan" menjadi model tabel (seperti menu Jelajah & Klaim) lengkap dengan pencarian, filter lokasi, serta tombol "+ Buat Laporan Barang Temuan" yang memicu modal dialog.
- `src/id/ac/pens/psdku/campuslosthub/MODULE_OWNERSHIP.md`: memperbarui daftar file komponen view dengan `LaporBarangDialog.java`.

#### Tests
- Kompilasi: `javac -d bin (seluruh source code src)` -> Berhasil (Exit Code 0).
- Eksekusi: `ModelSelfCheck`, `ServiceStorageSelfCheck`, `UISelfCheck` -> Seluruhnya PASS.

### 2026-11-23 — Pemisahan Navbar Berbasis Peran, Kolom Deskripsi, & Penyempurnaan Kontras Tombol

#### Changed
- `src/id/ac/pens/psdku/campuslosthub/view/MainFrame.java`: memisahkan secara total navbar pengguna (Jelajah & Klaim, Laporkan Temuan, Riwayat Aktivitas) dan navbar petugas/admin (Dashboard Kendali, Verifikasi Klaim & Pos, Kelola Data Barang); menghilangkan subtab berlapis JTabbedPane di halaman admin; menambahkan kolom Deskripsi pada seluruh tabel; menghilangkan karakter Unicode emoji yang tidak terbaca pada font Windows.
- `src/id/ac/pens/psdku/campuslosthub/view/UITheme.java`: menggunakan `BasicButtonUI` pada pembuatan tombol kustom untuk menjamin warna latar solid dan teks putih berdaya baca kontras tinggi di semua Look-and-Feel Windows; mengubah dot status SELESAI menjadi dot `● SELESAI`.
- `src/id/ac/pens/psdku/campuslosthub/view/ClaimDialog.java`: menambahkan pratinjau deskripsi fisik barang pada dialog klaim.
- `README.md`: mendokumentasikan panduan kompilasi dan cara menjalankan aplikasi Java Swing.

#### Tests
- Kompilasi: `javac -d bin (seluruh file src)` -> Berhasil (Exit Code 0).
- Eksekusi: `ModelSelfCheck`, `ServiceStorageSelfCheck`, dan `UISelfCheck` -> PASS.

### 2026-11-23 — Implementasi Antarmuka Modern Java Swing & Integrasi Sistem Lengkap

#### Added
- `src/id/ac/pens/psdku/campuslosthub/view/UITheme.java`: palet warna modern, renderer badge status/kategori, table styling, dan helper komponen Swing.
- `src/id/ac/pens/psdku/campuslosthub/view/ClaimDialog.java`: modal dialog pengajuan klaim berbasis KTM fisik dan verifikasi polimorfik tanpa entri ID manual.
- `src/id/ac/pens/psdku/campuslosthub/view/AdminLoginDialog.java`: modal login petugas/admin kampus (`admin`/`admin123`).
- `tests/UISelfCheck.java`: unit & integration check untuk memvalidasi inisialisasi antarmuka dan interaksi manager.

#### Changed
- `src/id/ac/pens/psdku/campuslosthub/view/MainFrame.java`: perombakan menyeluruh menjadi antarmuka modern terpadu 4 layar (Jelajah & Klaim Barang, Formulir Laporan Dinamis, Riwayat Aktivitas Mahasiswa, serta Panel Petugas Pos/Admin).
- `src/id/ac/pens/psdku/campuslosthub/service/LostAndFoundManager.java`: penambahan method `deleteItem(String id)`, alias `hapusBarang(String id)`, dan `countByStatus(String status)`.
- `src/id/ac/pens/psdku/campuslosthub/Main.java`: integrasi pemanggilan `UIManager.setLookAndFeel(SystemLookAndFeel)` untuk tampilan desktop native.
- `src/id/ac/pens/psdku/campuslosthub/MODULE_OWNERSHIP.md`: pembaruan daftar file antarmuka pengguna pada ownership Intan.
- `.gitignore`: penambahan ignore folder dependencies lokal.

#### Tests
- Perintah kompilasi: `javac -d bin (semua file java di src/)` -> Berhasil (Exit Code 0).
- Eksekusi test: `ModelSelfCheck`, `ServiceStorageSelfCheck`, dan `UISelfCheck` -> Semua PASS tanpa error.

#### Notes
- Antarmuka tetap berbasis Java Swing sesuai arahan pengguna dengan estetika modern, responsif, dan mempertahankan seluruh 4 pilar OOP serta persistensi file CSV.

### 2026-11-23 — Penyesuaian Desain Antarmuka & Reduksi Alur UI

#### Added
- Menambahkan workspace skill `.agents/skills/frontend-design/SKILL.md` ke dalam repositori proyek agar dapat digunakan bersama oleh seluruh anggota tim untuk acuan desain antarmuka.
- Menambahkan `docs/PENYESUAIAN_DESAIN_UI.md` yang merinci penyederhanaan alur antarmuka pengguna (reduksi langkah berulang).
- Menambahkan instruksi eksplisit dan wireframe dialog klaim untuk verifikasi berbasis Kartu Tanda Mahasiswa (KTM): input NIM 10 digit, nama sesuai KTM, serta verifikasi fisik KTM asli oleh petugas pos.
- Merampingkan struktur menu dari 6 menu terpisah menjadi 3 menu utama: Jelajah & Klaim Terpadu, Formulir Laporan Cepat, dan Panel Petugas Pos.
- Menambahkan spesifikasi wireframe ASCII dan pemetaan alur ke komponen OOP (`Item`, `LostAndFoundManager`, `Claimant`, `CsvStorage`).

#### Tests
- Kompilasi source code (`javac`) dan eksekusi `ModelSelfCheck` serta `ServiceStorageSelfCheck`: PASS (berhasil tanpa error).
- Verifikasi struktur Markdown: berhasil.

### 2026-11-16 — Dokumentasi BAB III Implementasi

#### Added
- Menambahkan `docs/BAB_III_IMPLEMENTASI.md` sesuai struktur laporan Final Project PBO.
- Menambahkan `docs/BAB_III_IMPLEMENTASI.docx` sebagai dokumen Word untuk dikirim dan digunakan dalam laporan.
- Mendokumentasikan lingkungan pengembangan, struktur package, class/object, Encapsulation, Inheritance, Polymorphism, Abstraction, Exception Handling, Collection Framework, dan File Handling CSV.
- Menyesuaikan isi dokumen dengan source code aktual yang tersedia, termasuk `LostAndFoundManager`, `CsvStorage`, `Main`, `ConsoleView`, dan `MainFrame`.

#### Tests
- `git -c core.whitespace=cr-at-eol diff --check`: berhasil; CRLF lama diperlakukan sebagai akhir baris valid.
- Kompilasi Java dan self-check belum dijalankan karena `javac` tidak tersedia pada environment.

#### Notes
- Dokumen tidak mengklaim kompilasi atau self-check berhasil.
- Operasi ubah data umum dan hapus permanen belum dinyatakan tersedia sebagai fitur terpisah.

### 2026-11-15 — Implementasi Exception, CsvStorage, dan LostAndFoundManager (Bilu)

#### Added
- `src/id/ac/pens/psdku/campuslosthub/exception/ItemNotFoundException.java`
- `src/id/ac/pens/psdku/campuslosthub/exception/InvalidClaimException.java`
- `src/id/ac/pens/psdku/campuslosthub/exception/StorageException.java`
- `src/id/ac/pens/psdku/campuslosthub/storage/CsvStorage.java`
- `src/id/ac/pens/psdku/campuslosthub/service/LostAndFoundManager.java`
- `tests/ServiceStorageSelfCheck.java`

#### Tests
- `javac -d bin (Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName })`: berhasil (seluruh modul Aris, Bilu, dan Intan terkompilasi tanpa error).
- `javac -cp bin -d bin tests/ServiceStorageSelfCheck.java; java -cp bin ServiceStorageSelfCheck`: `SERVICE & STORAGE SELF-CHECK: PASS`.

#### Notes
- Method `LostAndFoundManager` menyelaraskan kontrak yang dipanggil oleh `ConsoleView` & `MainFrame` (`addItem`, `getAllItems`, `searchByName`, `filterByLocation`, `findById`, `claimItem`, `completeHandover`, `cancelClaim`) serta alias berbahasa Indonesia sesuai `docs/ROLE_BILU.md`.

### 2026-11-15 — View, Main, dan Claimant (Intan)

#### Added
- `model/Claimant.java`
- `Main.java`
- `view/ConsoleView.java`, `view/MainFrame.java`, `view/InputValidator.java`, `view/TableFormatter.java`, `view/ViewUtil.java`
- `docs/TEST_SCENARIOS.md`

#### Tests
- Compile belum dijalankan karena `LostAndFoundManager` dan `InvalidClaimException` dari Bilu belum tersedia.

#### Notes
- Nama method pada `LostAndFoundManager` masih asumsi dan perlu dicocokkan dengan Bilu.

### 2026-11-15 — Modul belajar lokal Aris

#### Added

- Menambahkan modul belajar pribadi di `.local-learning/` untuk memahami model inti, OOP, polymorphism, status, dan CSV.
- Menambahkan `.local-learning/` ke `.gitignore` agar modul tidak masuk GitHub.

#### Tests

- `git check-ignore -v .local-learning/MODUL_BELAJAR_MODEL_ARIS.md`: berhasil; folder diabaikan Git.
- `git diff --check`: berhasil.

### 2026-11-15 — Implementasi core model Aris

#### Added

- Menambahkan `Verifiable.java`.
- Menambahkan abstract class `Item.java`.
- Menambahkan `BarangElektronik.java`.
- Menambahkan `BarangDokumen.java`.
- Menambahkan `tests/ModelSelfCheck.java`.

#### Changed

- Menyelaraskan nama service pada dokumentasi dari `LostAndFoundService` menjadi `LostAndFoundManager`.
- Menambahkan aturan validasi field wajib dan transisi status pada model.
- Menambahkan serialisasi sembilan kolom CSV pada subclass model.
- Menambahkan pencocokan klaim elektronik tanpa mempermasalahkan spasi atau tanda hubung.
- Memperbarui `docs/TASK_ARIS.md` berdasarkan implementasi yang tersedia.

#### Tests

- `SOURCE STRUCTURE: PASS`.
- `git diff --check`: berhasil.
- Compile dan self-check Java belum dijalankan karena `javac` tidak tersedia.
- Instalasi JDK belum dilakukan karena membutuhkan hak administrator.

#### Notes

- Handoff service Bilu, `Claimant` Intan, dan review integrasi belum selesai.
- Perubahan berada di branch lokal `development`.

### 2026-11-15 — Task guide personal Aris

#### Added

- Menambahkan `docs/TASK_ARIS.md`.
- Memetakan pekerjaan Aris dari implementasi `Verifiable` sampai review integrasi dan persiapan presentasi.
- Menambahkan Definition of Done untuk role Lead Architect & Core OOP.

#### Tests

- Verifikasi struktur Markdown dan checklist dilakukan.
- Pengujian Java belum dijalankan karena source code Java belum tersedia.

### 2026-11-15 — Aturan Git dua branch

#### Changed

- Menambahkan aturan penggunaan dua branch permanen: `development` dan `main`.
- Menetapkan `development` sebagai branch pekerjaan dan integrasi.
- Menetapkan `main` sebagai branch stabil untuk demo atau pengumpulan.
- Menambahkan aturan commit, push, merge, konflik, dan pemeriksaan sebelum merge.

#### Tests

- `git diff --check` akan dijalankan sebelum commit.
- Belum ada pengujian Java karena source code Java belum tersedia.

### 2026-11-15 — Task guide dan aturan AI

#### Added

- Menambahkan `AGENTS.md` sebagai aturan kerja AI.
- Menyusun ulang `docs/TASK_TRACKER.md` berdasarkan tahapan resmi Minggu 1–4.
- Menambahkan checklist pelaporan per role dan kriteria selesai setiap minggu.

#### Tests

- Verifikasi struktur dan isi Markdown dilakukan setelah penulisan.
- Pengujian Java belum dijalankan karena source code Java belum tersedia.

### Belum dikerjakan

- Implementasi source code Java inti.
- Implementasi `Verifiable`, `Item`, dan subclass.
- Implementasi `LostAndFoundManager`.
- Implementasi File Handling CSV.
- Implementasi `Main` dan UI.
- Pengujian otomatis/fungsional aplikasi.
- Laporan final BAB I–V, video, dan presentasi.

## 2026-11-15 — Struktur dan Dokumentasi Awal

### Added

- Menambahkan `.gitignore` untuk file hasil kompilasi dan file lokal.
- Menambahkan dataset awal `data/lost_found_data.csv`.
- Menambahkan `docs/SPECIFICATION.md`.
- Menambahkan `docs/ARCHITECTURE.md`.
- Menambahkan `docs/CONTRACTS_AND_CSV.md`.
- Menambahkan `docs/TASK_TRACKER.md` sebagai task guide empat minggu.
- Menambahkan `docs/ROLE_ARIS.md`.
- Menambahkan `docs/ROLE_BILU.md`.
- Menambahkan `docs/ROLE_INTAN.md`.
- Menambahkan struktur modular pada `src/id/ac/pens/psdku/campuslosthub/`.
- Menambahkan `MODULE_OWNERSHIP.md` untuk kepemilikan modul dan dependency flow.
- Menambahkan aturan pemisahan `contract`, `model`, `service`, `storage`, `exception`, dan `view`.

### Updated

- Memperbarui `README.md` dengan ringkasan proyek, anggota, alur bisnis, struktur target, dan cara menjalankan aplikasi.
- Menyesuaikan dokumentasi role dengan laporan Minggu 1:
  - Aris: arsitektur, UML, model inti, abstraction, inheritance, dan polymorphism.
  - Bilu: `LostAndFoundManager`, `ArrayList`, CSV, exception, dan integritas data.
  - Intan: `Claimant`, `Main`, UI, validasi input, testing, dan video demo.
- Menetapkan `LostAndFoundManager` sebagai nama service sesuai dokumen Minggu 1.
- Menetapkan NRP Bilu sebagai `3125521009` sesuai dokumen sumber.

### Git

- Commit lokal terakhir: `6edaa9f docs: add CampusLostHub modular structure`.
- Push ke remote belum terverifikasi berhasil karena autentikasi GitHub belum tersedia pada environment.

## Format entri perubahan berikutnya

Gunakan format berikut setiap kali repository berubah:

```markdown
## YYYY-MM-DD — Judul perubahan

### Added
- ...

### Changed
- ...

### Fixed
- ...

### Tests
- Perintah: `...`
- Hasil: ...

### Notes
- ...
```

Jangan menulis perubahan yang belum benar-benar dilakukan. Jika pengujian belum dijalankan, tulis statusnya sebagai belum dijalankan.
