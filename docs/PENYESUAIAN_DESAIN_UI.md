# Penyesuaian Desain Antarmuka (UI) & Penyederhanaan Alur Pengguna — CampusLostHub

Dokumen ini berisi spesifikasi penyesuaian tata letak antarmuka (UI), struktur menu, serta penyederhanaan langkah (reduksi iterasi) pengguna untuk aplikasi **CampusLostHub**. Dokumen ini menjadi acuan desain bagi tim (terutama Intan pada *Presentation Layer*) serta bahan penyusunan draf Bab II dan Bab III laporan proyek.

---

## 1. Latar Belakang & Analisis Masalah UI Awal

### 1.1 Masalah pada Desain Awal
1. **Iterasi & Langkah Terlalu Panjang:**
   Pengguna harus berpindah menu berkali-kali untuk satu kebutuhan (contoh: buka katalog $\rightarrow$ keluar $\rightarrow$ buka pencarian $\rightarrow$ keluar $\rightarrow$ buka form klaim).
2. **Beban Kognitif Manual (Copy-Paste ID):**
   Pengguna dipaksa menghafal atau mencatat string ID barang (misal: `BRG-1002`) dari tabel, lalu mengetikkannya kembali secara manual pada form klaim.
3. **Menu Petugas Terfragmentasi:**
   Menu konfirmasi serah terima dan pembatalan klaim dipisah menjadi menu berbeda, serta mencampuradukkan seluruh data barang alih-alih berfokus pada barang yang sedang dalam sengketa klaim.

### 1.2 Prinsip Desain yang Diterapkan
* **Task-Oriented:** Menu dikelompokkan berdasarkan tujuan spesifik pengguna (Mencari & Mengklaim, Melaporkan, atau Memproses Serah Terima).
* **Zero Manual ID Entry:** Saat pengguna memilih baris barang di katalog, sistem otomatis mengenali objek tersebut sehingga pengguna tidak perlu mengetik ID manual.
* **Fokus Kriteria PBO:** Mengutamakan kemudahan pengujian fungsional dan pembuktian 4 pilar OOP saat demonstrasi langsung di hadapan dosen penguji.

---

## 2. Struktur Menu Terpadu (Hasil Reduksi)

Sistem dirampingkan dari 6 menu terpisah menjadi **3 Menu/Tab Utama**:

```text
============================================================
      CampusLostHub — Smart Campus Lost & Found System
============================================================
 [1] 🔍 Jelajah & Klaim Barang  (Katalog, Filter, & Klaim Terpadu)
 [2] ➕ Laporkan Barang Temuan  (Form Cepat Dinamis)
 [3] 🛡️ Panel Petugas Pos       (Serah Terima & Pembatalan Klaim)
 [0] Keluar
============================================================
```

---

## 3. Spesifikasi Rinci Antarmuka & Fitur

### 3.1 Layar 1: Jelajah & Klaim Barang (Pusat Aktivitas Mahasiswa)

Mengintegrasikan fitur **Lihat Katalog**, **Pencarian**, **Filter Lokasi**, dan **Pengajuan Klaim** dalam satu layar interaktif.

#### A. Wireframe / Mockup Tampilan
```text
┌─────────────────────────────────────────────────────────────────────────────────┐
│ 🔍 JELAJAH & KLAIM BARANG TEMUAN                                                │
├─────────────────────────────────────────────────────────────────────────────────┤
│ Cari Nama: [ Flashdisk          ]   Filter Lokasi: [ Lab PBO        ▼ ] [Cari]  │
├─────────────────────────────────────────────────────────────────────────────────┤
│ ID       | Kategori   | Nama Barang      | Lokasi Ditemukan | Status            │
│ ─────────┼────────────┼──────────────────┼──────────────────┼────────────────── │
│ BRG-1001 | ELEKTRONIK | Flashdisk 32GB   | Lab PBO          | [Ajukan Klaim]    │
│ BRG-1002 | DOKUMEN    | KTM PENS         | Kantin Pusat     | [Ajukan Klaim]    │
│ BRG-1003 | ELEKTRONIK | Charger Asus     | Perpustakaan     | [Diproses]        │
├─────────────────────────────────────────────────────────────────────────────────┤
│ Klik tombol [Ajukan Klaim] pada baris TERSEDIA atau pilih baris -> [Ajukan]     │
└─────────────────────────────────────────────────────────────────────────────────┘
```

#### B. Wireframe Dialog Modal Pengajuan Klaim (Dengan Instruksi KTM)
```text
┌─────────────────────────────────────────────────────────────────────────────────┐
│ 📝 PENGAJUAN KLAIM KEPEMILIKAN BARANG (ID: BRG-1002)                            │
├─────────────────────────────────────────────────────────────────────────────────┤
│ ℹ️ PETUNJUK VERIFIKASI KARTU TANDA MAHASISWA (KTM):                              │
│ • Masukkan identitas resmi sesuai Kartu Tanda Mahasiswa (KTM) aktif.           │
│ • Fisik KTM asli WAJIB dibawa dan ditunjukkan ke petugas saat serah terima pos. │
├─────────────────────────────────────────────────────────────────────────────────┤
│ [1. IDENTITAS PEMOHON / CLAIMANT]                                               │
│ * NIM Mahasiswa (KTM)     : [ 3125521004           ] (Validasi: 10 digit angka) │
│ * Nama Lengkap Sesuai KTM : [ Aris Kurniawan       ]                            │
│ * No. Handphone / WhatsApp: [ 081234567890         ]                            │
├─────────────────────────────────────────────────────────────────────────────────┤
│ [2. BUKTI RAHASIA KEPEMILIKAN BARANG]                                           │
│ * Masukkan Bukti Kepemilikan: [ 3125521004                                     ]│
│   - Jika Barang Elektronik : Masukkan Serial Number / IMEI rahasia.             │
│   - Jika Barang Dokumen/KTM: Masukkan NIM atau Nama Pemilik sesuai dokumen.     │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                        [ ❌ Batal ]    [ 🚀 Kirim Bukti Klaim ] │
└─────────────────────────────────────────────────────────────────────────────────┘
```

#### C. Alur Interaksi Pengguna & Verifikasi KTM (User Flow)
1. Mahasiswa membuka menu katalog. Tabel barang aktif langsung tersaji secara otomatis.
2. Mahasiswa menyaring barang via kolom pencarian nama atau filter lokasi ruangan kampus.
3. Begitu menemukan barang miliknya (status `TERSEDIA`), mahasiswa dapat langsung menekan tombol **"Ajukan Klaim"** di kolom **Aksi** pada baris barang tersebut (atau memilih baris lalu menekan tombol **"Ajukan Klaim"** di footer).
   * *(Alur Petugas Pos)*: Pada `Dashboard Kendali` Petugas, mengklik salah satu baris di tabel `Klaim Menunggu Verifikasi` otomatis mengarahkan petugas ke halaman **`Verifikasi Klaim & Pos`** agar seluruh persyaratan serah terima (checklist fisik, foto, dan TTD) terpenuhi sebelum status menjadi `SELESAI`.
4. Sistem membuka modal klaim dengan **ID Barang yang sudah terisi otomatis**:
   * **Instruksi KTM Tampil di Layar:** Sistem menampilkan peringatan jelas bahwa pengklaim harus mahasiswa aktif dan wajib membawa fisik KTM asli saat pengambilan di pos.
   * **Input Data Identitas KTM:** Mahasiswa mengisi `NIM` (10 digit sesuai KTM) dan `Nama Lengkap` sesuai KTM, serta `No. HP/WhatsApp` untuk kontak darurat.
   * **Input Bukti Kepemilikan:** Mahasiswa memasukkan bukti rahasia. Jika barang berupa KTM/Dokumen, mahasiswa memasukkan NIM atau nama lengkapnya untuk diverifikasi sistem secara polimorfik via `verifyClaim()`.
5. Mahasiswa menekan tombol **"Kirim Bukti Klaim"**.
   * **Jika Bukti Benar:** Status barang otomatis berubah menjadi `PROSES_KLAIM`. Muncul konfirmasi: *"Klaim diterima sementara! Silakan bawa KTM fisik asli Anda ke pos kampus untuk verifikasi serah terima."*
   * **Jika Bukti Salah:** Muncul pesan penolakan dari `InvalidClaimException` tanpa mengubah status barang.

---

### 3.2 Layar 2: Laporkan Barang Temuan (Pusat Pelaporan Penemu)

Formulir pelaporan barang temuan dalam satu halaman terpadu tanpa navigasi bertingkat.

#### A. Wireframe / Mockup Tampilan
```text
┌─────────────────────────────────────────────────────────────────────────────────┐
│ ➕ FORMULIR PELAPORAN BARANG TEMUAN KAMPUS                                       │
├─────────────────────────────────────────────────────────────────────────────────┤
│ Kategori Barang     : (•) Barang Elektronik       ( ) Barang Dokumen            │
│ Nama Barang         : [ Charger Laptop Asus Type-C                             ]│
│ Deskripsi Fisik     : [ Kabel hitam terlilit rapi, ada stiker biru             ]│
│ Lokasi Ditemukan    : [ Perpustakaan Lt. 2                                     ]│
│ Tanggal Ditemukan   : [ 2026-11-23 ] (Default: Hari ini)                        │
├─────────────────────────────────────────────────────────────────────────────────┤
│ [Bidang Khusus Kategori - Berubah Dinamis Sesuai Pilihan]:                      │
│ * Serial Number/IMEI: [ A19-065N2A                                             ]│
│ * Brand / Merk      : [ Asus                                                   ]│
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                      [ 💾 Simpan Laporan ]      │
└─────────────────────────────────────────────────────────────────────────────────┘
```

#### B. Logika Form
* Jika memilih **Barang Elektronik**: Label bidang khusus menampilkan *Serial Number / IMEI* dan *Brand / Merk*.
* Jika memilih **Barang Dokumen**: Label bidang khusus berubah menjadi *Nomor Identitas Dokumen (NIM/NIK/Plat)* dan *Nama Pemilik pada Dokumen*.
* Sistem menghasilkan ID otomatis dengan format urut `BRG-XXXX` dan status awal `TERSEDIA`.
* Data langsung tersimpan secara persisten ke `data/lost_found_data.csv`.

---

### 3.3 Layar 3: Panel Petugas Pos (Pusat Resolusi Fisik)

Panel khusus petugas pos kampus untuk memproses serah terima fisik atau menolak klaim palsu.

#### A. Wireframe / Mockup Tampilan
```text
┌─────────────────────────────────────────────────────────────────────────────────┐
│ 🛡️ PANEL VERIFIKASI PETUGAS POS (KLAIM AKTIF)                                   │
├─────────────────────────────────────────────────────────────────────────────────┤
│ Menampilkan hanya barang yang sedang berstatus [PROSES_KLAIM]:                  │
│                                                                                 │
│ ID       | Kategori   | Nama Barang      | Pemohon (Claimant) | No. Kontak      │
│ ─────────┼────────────┼──────────────────┼────────────────────┼──────────────── │
│ BRG-1003 | ELEKTRONIK | Charger Asus     | Ahmad Dahlan       | 081234567890    │
├─────────────────────────────────────────────────────────────────────────────────┤
│ [ Pilih Baris Barang yang Sedang Diverifikasi Fisik di Pos ]:                   │
│                                                                                 │
│   [ ✅ Konfirmasi Serah Terima Fisik ]         [ ❌ Tolak / Batalkan Klaim ]     │
│   (Status barang berubah -> SELESAI)           (Status barang kembali -> TERSEDIA)│
└─────────────────────────────────────────────────────────────────────────────────┘
```

#### B. Logika Aksi Petugas & Verifikasi Fisik KTM
* **Pengecekan Fisik Kartu KTM:** Sebelum menekan tombol konfirmasi, petugas pos memeriksa kecocokan fisik KTM asli yang dibawa mahasiswa dengan data `NIM` dan `Nama Claimant` yang tertera pada tabel klaim aktif.
* **Tombol "Konfirmasi Serah Terima Fisik":** Ditekan setelah fisik KTM terbukti asli dan sesuai. Method `completeHandover(id)` dijalankan dan status berubah menjadi `SELESAI`.
* **Tombol "Tolak / Batalkan Klaim":** Ditekan jika saat verifikasi fisik mahasiswa tidak dapat menunjukkan fisik KTM atau bukti tidak sesuai. Method `cancelClaim(id)` dijalankan dan status dikembalikan menjadi `TERSEDIA` agar mahasiswa lain tetap dapat mencari/mengklaimnya.

---

## 4. Matriks Pemetaan Fitur UI ke Komponen OOP

| Elemen Antarmuka Baru | Komponen Backend / Model | Kriteria Penilaian PBO |
|---|---|---|
| **Filter & Pencarian Katalog** | `LostAndFoundManager.searchByName()` & `filterByLocation()` | Collection Framework (`ArrayList<Item>`) |
| **Pilihan Kategori Dinamis** | Constructor `BarangElektronik` vs `BarangDokumen` | Inheritance (`super(...)`) & Encapsulation |
| **Input Bukti Verifikasi** | `Item.verifyClaim(proofData)` | Polymorphism (Dynamic Binding) & Abstraction (`Verifiable`) |
| **Penanganan Klaim Gagal** | `try-catch (InvalidClaimException e)` | Custom Exception Handling |
| **Persistensi Otomatis** | `CsvStorage.save()` & `load()` | File Handling (`BufferedReader`/`BufferedWriter`) |

---

## 5. Ringkasan Keuntungan Perubahan

1. **Efisiensi Interaksi Pengguna:** Memangkas lebih dari 60% klik dan iterasi menu yang sebelumnya berulang-ulang.
2. **Eliminasi Human Error:** Mengeliminasi kesalahan pengetikan ID barang secara manual oleh pengguna.
3. **Kejelasan Demo Presentasi:** Sangat mudah dipraktikkan saat live demo di depan dosen penguji karena alur bisnis barang hilang/temuan digambarkan secara berurutan dan logis.
