# Changelog — CampusLostHub

Semua perubahan penting pada repository dicatat di dokumen ini.

Format versi mengikuti tahap pengerjaan proyek, bukan klaim rilis produk.

## [Unreleased]

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
