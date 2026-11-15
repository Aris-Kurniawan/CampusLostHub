# Dokumentasi Role — Intan Kusumaningarum

## 1. Identitas dan Peran

- **Nama:** Intan Kusumaningarum
- **NRP:** 3125521024
- **Role:** UI Controller & QA Tester
- **Fokus:** Antarmuka pengguna, class `Claimant`, validasi input, pengujian fungsional, dan video demonstrasi.

## 2. Tanggung Jawab Utama

Intan bertanggung jawab menyediakan antarmuka yang memandu pengguna melalui pelaporan barang, pencarian, pengajuan klaim, dan serah terima. Intan juga mengembangkan model data `Claimant`, menguji skenario CRUD/klaim, dan menyiapkan video demonstrasi. UI hanya mengatur input/output dan memanggil `LostAndFoundManager` milik Bilu.

### Modul yang dikerjakan

```text
src/id/ac/pens/psdku/campuslosthub/
├── view/
│   ├── Claimant.java
│   └── view/
│       ├── ConsoleView.java
│       ├── InputValidator.java
│       └── TableFormatter.java
└── Main.java
```

Jika waktu cukup, `view/` dapat memiliki implementasi Swing tanpa mengubah model dan service:

```text
view/
├── ConsoleView.java
└── SwingView.java
```

### Pekerjaan

1. Membuat class `Claimant` dengan atribut private `nim`, `nama`, dan `noHp`.
2. Membuat menu utama aplikasi melalui class `Main`.
3. Membuat form input barang elektronik dan dokumen.
4. Memvalidasi input kosong, tanggal, kategori, dan pilihan menu.
5. Menampilkan katalog barang aktif dalam tabel yang mudah dibaca.
6. Menyediakan pencarian berdasarkan nama atau keyword.
7. Menyediakan filter berdasarkan lokasi.
8. Membuat form pengajuan klaim dengan data `Claimant` dan bukti kepemilikan.
9. Menampilkan hasil klaim tanpa membocorkan bukti rahasia pada katalog.
10. Menangani exception dari `LostAndFoundManager` dengan pesan pengguna yang jelas.
11. Menguji skenario CRUD dan klaim barang.
12. Menyusun dokumentasi pengujian dan video demonstrasi.

## 3. Aturan Batas UI

- UI tidak mengakses field model secara langsung.
- UI tidak membaca atau menulis file CSV langsung.
- UI tidak mengimplementasikan algoritma verifikasi serial number atau identitas.
- UI memanggil service melalui method publik.
- Data rahasia hanya diminta pada form klaim dan tidak ditampilkan pada katalog.
- Setiap input invalid dikembalikan ke form dengan pesan yang jelas.

## 4. Alur Menu

```text
1. Laporkan barang temuan
2. Lihat katalog barang aktif
3. Cari barang
4. Ajukan klaim
5. Konfirmasi serah terima
6. Batalkan/menolak klaim
0. Keluar
```

## 5. Matriks Pengujian Fungsional

| ID | Skenario | Hasil yang diharapkan |
|---|---|---|
| UI-01 | Input barang elektronik lengkap | Barang tersimpan dengan kategori elektronik |
| UI-02 | Input barang dokumen lengkap | Barang tersimpan dengan kategori dokumen |
| UI-03 | Field wajib kosong | Sistem menolak input dan meminta perbaikan |
| UI-04 | Pencarian keyword cocok | Barang yang sesuai ditampilkan |
| UI-05 | Klaim dengan bukti benar | Status berubah menjadi `PROSES_KLAIM` |
| UI-06 | Klaim dengan bukti salah | Klaim ditolak tanpa perubahan status |
| UI-07 | Konfirmasi serah terima | Status berubah menjadi `SELESAI` |
| UI-08 | Batalkan klaim | Status kembali menjadi `TERSEDIA` |
| UI-09 | Jalankan ulang aplikasi | Data sebelumnya tetap tampil |

## 6. Acceptance Check

- Semua menu dapat kembali ke menu utama.
- Input invalid tidak menyebabkan aplikasi crash.
- Pesan sukses dan gagal menyebutkan hasil operasi.
- Katalog hanya menampilkan informasi publik.
- Skenario UI dapat dijalankan menggunakan data CSV yang disediakan.
