# AGENTS.md — Aturan AI untuk CampusLostHub

## Tujuan

Dokumen ini mengatur cara AI bekerja di repository CampusLostHub agar perubahan konsisten dengan instruksi Final Project PBO, desain sistem, pembagian role, dan status pekerjaan aktual.

## Aturan wajib sebelum bekerja

1. Jika pertama kali membuka repository, baca dokumen Markdown berikut sebelum membuat perubahan:
   - `README.md`
   - `AGENTS.md`
   - `CHANGELOG.md`
   - `docs/TASK_TRACKER.md`
   - `docs/SPECIFICATION.md`
   - `docs/ARCHITECTURE.md`
   - `docs/CONTRACTS_AND_CSV.md`
   - `docs/ROLE_ARIS.md`
   - `docs/ROLE_BILU.md`
   - `docs/ROLE_INTAN.md`
2. Jika ada perubahan baru di repository, jalankan `git status` dan baca file Markdown yang berubah sebelum melakukan pekerjaan berikutnya.
3. Periksa instruksi Final Project PBO apabila keputusan teknis tidak tercakup pada dokumentasi repository.
4. Jangan mengandalkan asumsi dari percakapan lama jika source code atau dokumentasi repository menunjukkan keadaan yang berbeda.
5. Jangan mengklaim fitur selesai sebelum source code, pengujian, atau bukti file benar-benar tersedia.

## Aturan perubahan repository

1. Ikuti struktur modular:

   ```text
   src/id/ac/pens/psdku/campuslosthub/
   ├── contract/
   ├── model/
   ├── exception/
   ├── service/
   ├── storage/
   └── view/
   ```

2. Jangan memindahkan tanggung jawab antarmodul tanpa memperbarui:
   - `docs/ARCHITECTURE.md`
   - `docs/SPECIFICATION.md`
   - `src/id/ac/pens/psdku/campuslosthub/MODULE_OWNERSHIP.md`
   - Dokumentasi role terkait.
3. Jangan mengakses field private model secara langsung.
4. UI tidak boleh membaca atau menulis CSV secara langsung.
5. Logika verifikasi klaim harus berada pada model/subclass yang sesuai, bukan disalin ke UI.
6. Gunakan Java standard library terlebih dahulu. Jangan menambah dependency tanpa alasan teknis dan persetujuan tim.
7. Jangan menambahkan abstraksi, factory, interface, atau konfigurasi yang tidak diperlukan.
8. Jangan menghapus data atau dokumentasi yang dibuat anggota lain tanpa memeriksa riwayat dan alasan perubahan.
9. Jangan mengubah format CSV tanpa memperbarui `docs/CONTRACTS_AND_CSV.md` dan data uji.
10. Jangan menambahkan password, token, API key, credential, atau data pribadi yang tidak diperlukan ke repository.

## Pembagian ownership

- **Aris:** `contract/`, model inti `Item` dan subclass, UML, abstraction, inheritance, polymorphism.
- **Bilu:** `service/`, `storage/`, `exception/`, `ArrayList<Item>`, CSV, dan exception handling.
- **Intan:** `model/Claimant.java`, `Main.java`, `view/`, validasi input, testing, dan video demo.

Ownership tidak menghalangi review silang, tetapi perubahan besar pada modul anggota lain harus dikomunikasikan dan dicatat.

## Aturan pengujian

1. Setelah perubahan Java, compile project dengan perintah yang sesuai struktur aktual.
2. Jalankan self-check atau test yang relevan.
3. Uji minimal alur yang terdampak.
4. Catat perintah dan hasil pengujian di `CHANGELOG.md`.
5. Jika pengujian gagal, catat kegagalan dan jangan menyatakan task selesai.
6. Periksa `git diff --check` sebelum commit.

## Aturan dokumentasi perubahan

Setiap perubahan repository wajib dicatat di `CHANGELOG.md` pada bagian paling atas setelah `## [Unreleased]` atau pada tanggal perubahan.

Minimal catat:

- Tanggal.
- File yang berubah.
- Ringkasan perubahan.
- Alasan perubahan jika tidak jelas.
- Pengujian yang dijalankan.
- Hasil pengujian.
- Kendala atau pekerjaan yang belum selesai.

Contoh:

```markdown
## 2026-11-15 — Implementasi model Item

### Added
- `src/.../model/Item.java`

### Tests
- Perintah: `javac ...`
- Hasil: berhasil.

### Notes
- Status transisi dibatasi pada status yang tercantum di spesifikasi.
```

Jika perubahan hanya berupa dokumentasi, tetap catat file dokumentasi yang diubah.

## Aturan workflow AI

```text
Baca Markdown dan status git
→ Identifikasi kontrak yang berlaku
→ Tentukan file minimum yang perlu berubah
→ Edit dengan diff kecil
→ Jalankan validasi/pengujian
→ Baca ulang hasil penting
→ Catat CHANGELOG.md
→ Tampilkan status dan bukti aktual
```

AI harus berhenti dan meminta keputusan jika:

- Dua dokumen menetapkan kontrak yang bertentangan.
- Perubahan menghapus data atau mengubah format CSV secara tidak kompatibel.
- Perubahan membutuhkan credential atau akses eksternal.
- Kebutuhan fitur tidak jelas dan pilihan desain mengubah arsitektur utama.

## Aturan Git

1. Periksa `git status` sebelum dan sesudah perubahan.
2. Stage file secara eksplisit; jangan menggunakan `git add -A` tanpa memeriksa daftar file.
3. Gunakan pesan commit yang jelas dan berbentuk imperative/conventional commit jika diminta.
4. Jangan mengklaim push berhasil tanpa output push atau verifikasi remote.
5. Jangan melakukan force push, reset destruktif, atau `git clean` tanpa persetujuan eksplisit.
