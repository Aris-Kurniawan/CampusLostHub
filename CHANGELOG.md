# Changelog — CampusLostHub

Semua perubahan penting pada repository dicatat di dokumen ini.

Format versi mengikuti tahap pengerjaan proyek, bukan klaim rilis produk.

## [Unreleased]

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
