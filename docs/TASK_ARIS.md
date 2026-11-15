# Task Guide Aris — CampusLostHub

Dokumen ini berisi pekerjaan Aris Kurniawan sampai bagian role Aris selesai dan siap diintegrasikan dengan Bilu serta Intan.

## Role

- **Nama:** Aris Kurniawan
- **NRP:** 3125521004
- **Role:** Lead Architect & Core OOP
- **Fokus:** Arsitektur sistem, UML, model domain, abstraction, inheritance, encapsulation, dan polymorphism.

## Batas ownership

```text
contract/
model/Item.java
model/BarangElektronik.java
model/BarangDokumen.java
UML dan kontrak antarmodul
```

`Claimant.java` dikerjakan Intan. Custom exception dikerjakan Bilu. Aris tetap melakukan review agar kontrak keduanya konsisten dengan model inti.

---

## Tahap 0 — Persiapan sebelum coding

- [x] Membaca `README.md`.
- [x] Membaca `AGENTS.md`.
- [x] Membaca `CHANGELOG.md`.
- [x] Membaca `docs/TASK_TRACKER.md`.
- [x] Membaca `docs/SPECIFICATION.md`.
- [x] Membaca `docs/ARCHITECTURE.md`.
- [x] Membaca `docs/CONTRACTS_AND_CSV.md`.
- [x] Memastikan package target: `id.ac.pens.psdku.campuslosthub`.
- [x] Memastikan status barang: `TERSEDIA`, `PROSES_KLAIM`, `SELESAI`.
- [x] Membuat atau berpindah ke branch `development`.

## Tahap 1 — Finalisasi kontrak model

Selesaikan sebelum Bilu membuat service dan Intan membuat UI.

- [x] Menetapkan daftar field `Item`:
  - [x] `id`.
  - [x] `nama`.
  - [x] `deskripsi`.
  - [x] `lokasiDitemukan`.
  - [x] `tanggalDitemukan`.
  - [x] `status`.
- [x] Menetapkan constructor `Item`.
- [x] Menetapkan getter yang dibutuhkan service dan storage.
- [x] Menetapkan aturan setter dan transisi status.
- [x] Menetapkan method abstract:
  - [x] `getKategori()`.
  - [x] `displayInfo()`.
  - [x] `toCsvString()`.
- [x] Menetapkan method interface `verifyClaim(String proofData)`.
- [x] Menetapkan pemetaan CSV `extra1` dan `extra2`.
- [x] Menulis keputusan kontrak ke `docs/CONTRACTS_AND_CSV.md` jika ada perubahan.

## Tahap 2 — Implementasi `Verifiable`

File:

```text
src/id/ac/pens/psdku/campuslosthub/contract/Verifiable.java
```

- [x] Membuat interface `Verifiable`.
- [x] Menambahkan method:

```java
boolean verifyClaim(String proofData);
```

- [x] Memastikan interface tidak menyimpan logika elektronik atau dokumen.
- [x] Menambahkan JavaDoc singkat tentang kontrak verifikasi.

## Tahap 3 — Implementasi abstract class `Item`

File:

```text
src/id/ac/pens/psdku/campuslosthub/model/Item.java
```

- [x] Membuat `abstract class Item implements Verifiable`.
- [x] Membuat seluruh field menjadi `private`.
- [x] Membuat constructor berparameter.
- [x] Membuat getter.
- [x] Membuat setter hanya untuk field yang memang boleh berubah.
- [x] Menetapkan konstanta status.
- [x] Memvalidasi field wajib agar tidak kosong.
- [x] Membatasi status pada nilai yang sah.
- [x] Menentukan aturan transisi:
  - [x] `TERSEDIA` → `PROSES_KLAIM`.
  - [x] `PROSES_KLAIM` → `SELESAI`.
  - [x] `PROSES_KLAIM` → `TERSEDIA`.
- [x] Menyediakan abstract method sesuai arsitektur.
- [x] Memastikan informasi rahasia tidak muncul di `displayInfo()`.

## Tahap 4 — Implementasi `BarangElektronik`

File:

```text
src/id/ac/pens/psdku/campuslosthub/model/BarangElektronik.java
```

- [x] Extend `Item`.
- [x] Menambahkan field private `nomorSeri`.
- [x] Menambahkan field private `brand`.
- [x] Membuat constructor dengan `super(...)`.
- [x] Override `verifyClaim` berdasarkan serial number/IMEI.
- [x] Membuat pencocokan case-insensitive.
- [x] Menangani spasi atau tanda hubung secara konsisten.
- [x] Override `getKategori()` dengan `ELEKTRONIK`.
- [x] Override `displayInfo()`.
- [x] Override `toCsvString()` dengan `extra1=nomorSeri` dan `extra2=brand`.
- [x] Tidak menampilkan serial number pada katalog publik.

## Tahap 5 — Implementasi `BarangDokumen`

File:

```text
src/id/ac/pens/psdku/campuslosthub/model/BarangDokumen.java
```

- [x] Extend `Item`.
- [x] Menambahkan field private `nomorIdentitas`.
- [x] Menambahkan field private `namaPemilik`.
- [x] Membuat constructor dengan `super(...)`.
- [x] Override `verifyClaim` berdasarkan nomor identitas/NIM atau nama pemilik.
- [x] Menentukan aturan pencocokan nama secara konsisten.
- [x] Override `getKategori()` dengan `DOKUMEN`.
- [x] Override `displayInfo()`.
- [x] Override `toCsvString()` dengan `extra1=nomorIdentitas` dan `extra2=namaPemilik`.
- [x] Tidak menampilkan nomor identitas pada katalog publik.

## Tahap 6 — Self-check model

- [x] Membuat self-check kecil tanpa framework atau gunakan test yang sudah disepakati tim.

- [ ] Membuat objek `BarangElektronik` melalui constructor.
- [ ] Membuat objek `BarangDokumen` melalui constructor.
- [ ] Memanggil keduanya melalui variabel bertipe `Item`.
- [ ] Membuktikan `getKategori()` menghasilkan kategori berbeda.
- [ ] Membuktikan `verifyClaim` elektronik menerima bukti benar.
- [ ] Membuktikan `verifyClaim` elektronik menolak bukti salah.
- [ ] Membuktikan `verifyClaim` dokumen menerima bukti benar.
- [ ] Membuktikan `verifyClaim` dokumen menolak bukti salah.
- [ ] Membuktikan `toCsvString()` menghasilkan sembilan kolom sesuai kontrak.
- [ ] Membuktikan field model tidak dapat diakses langsung.
- [!] Menjalankan compile dan mencatat hasilnya di `CHANGELOG.md`; tertunda karena `javac` tidak tersedia.

## Tahap 7 — Handoff ke Bilu dan Intan

Kirim kontrak berikut kepada Bilu dan Intan:

- [ ] Nama class final.
- [ ] Constructor dan parameter.
- [ ] Method publik.
- [ ] Konstanta status.
- [ ] Aturan transisi status.
- [ ] Format CSV.
- [ ] Contoh pembuatan objek.
- [ ] Contoh pemanggilan `verifyClaim`.
- [ ] Field yang tidak boleh diakses langsung.

Handoff dianggap selesai jika:

- [ ] Bilu dapat membuat `LostAndFoundManager` tanpa menebak API model.
- [ ] Bilu dapat melakukan serialisasi objek ke CSV.
- [ ] Intan dapat membuat form `Claimant` sesuai kontrak klaim.
- [ ] Tidak ada konflik nama class atau method.

## Tahap 8 — Review integrasi Minggu 3

- [ ] Meninjau `LostAndFoundManager` milik Bilu.
- [ ] Memastikan service memanggil `verifyClaim`, bukan menyalin algoritmanya.
- [ ] Memastikan service tidak mengakses field private.
- [ ] Meninjau `CsvStorage` dan kesesuaian sembilan kolom CSV.
- [ ] Meninjau `Claimant` milik Intan.
- [ ] Memastikan UI tidak menampilkan bukti rahasia.
- [ ] Memastikan perubahan status hanya melalui aturan model/service.
- [ ] Membantu menyelesaikan konflik API.
- [ ] Memperbarui UML sesuai source code aktual.

## Tahap 9 — Persiapan presentasi individu

- [ ] Menjelaskan alasan `Item` dibuat abstract.
- [ ] Menjelaskan alasan `Verifiable` dibuat interface.
- [ ] Menjelaskan inheritance dua subclass.
- [ ] Menjelaskan polymorphism pada `verifyClaim`.
- [ ] Menjelaskan encapsulation pada field private.
- [ ] Menjelaskan hubungan model dengan service dan CSV.
- [ ] Menunjukkan perubahan kode sederhana saat live coding.
- [ ] Menjelaskan satu kendala dan cara penyelesaiannya.

## Definition of Done — Role Aris

Role Aris selesai jika seluruh kondisi berikut terpenuhi:

- [ ] `Verifiable.java` selesai.
- [ ] `Item.java` selesai.
- [ ] `BarangElektronik.java` selesai.
- [ ] `BarangDokumen.java` selesai.
- [ ] Source code dapat dikompilasi.
- [ ] Self-check model berhasil.
- [ ] Kontrak API didokumentasikan.
- [ ] Bilu dapat mengintegrasikan service dan storage.
- [ ] Intan dapat mengintegrasikan UI dan `Claimant`.
- [ ] UML sesuai source code.
- [ ] Perubahan dicatat di `CHANGELOG.md`.
- [ ] Commit berada di branch `development`.

## Urutan kerja paling efisien

```text
Finalisasi kontrak
→ Verifiable
→ Item
→ BarangElektronik
→ BarangDokumen
→ self-check
→ handoff ke Bilu dan Intan
→ review integrasi
→ update UML
→ persiapan presentasi
```
