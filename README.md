# CampusLostHub — Smart Campus Lost & Found Management System

> Final Project Praktikum Pemrograman Berorientasi Objek (PBO)  
> D3 Teknik Informatika PSDKU Lamongan — Politeknik Elektronika Negeri Surabaya (PENS)  
> Tahun Akademik 2026/2027

---

## 1. Ringkasan Eksekutif

**CampusLostHub** adalah sistem terpusat untuk pelaporan, pencarian, dan verifikasi klaim barang hilang/temuan di lingkungan kampus. Proyek ini menyelesaikan masalah penyebaran informasi barang hilang yang sebelumnya terfragmentasi di media sosial tanpa database terpusat dan tanpa verifikasi kepemilikan yang valid.

Aplikasi dibangun menggunakan **Java SE** dengan menerapkan 4 pilar utama Object-Oriented Programming (OOP) secara terstruktur.

---

## 2. Tim Pengembang & Pembagian Peran

| Nama | NRP | Peran | Tanggung Jawab Modul |
|---|---|---|---|
| **Aris Kurniawan** | 3125521004 | Lead System Architect & Core OOP | Arsitektur domain (`Item`, `BarangElektronik`, `BarangDokumen`, `Verifiable`, `Claimant`), algoritma verifikasi klaim, UML design. |
| **Sabilu Rosyada** | 3125521015 | Data Persistence & Storage Controller | Service layer (`LostAndFoundService`), manipulasi `ArrayList<Item>`, File I/O CSV, Exception Handling. |
| **Intan Kusumaningarum** | 3125521024 | UI Presentation & Flow Controller | Antarmuka pengguna (Console/Swing GUI), validasi input form, skenario pengujian fungsional, demo video. |

---

## 3. Struktur Direktori Proyek

```text
CampusLostHub/
├── docs/
│   ├── SPECIFICATION.md          # Spesifikasi fungsional, non-fungsional & pemenuhan PBO
│   ├── ARCHITECTURE.md           # Desain kelas, diagram UML, dan pola arsitektur
│   ├── CONTRACTS_AND_CSV.md      # Spesifikasi format file penyimpanan CSV & aturan data
│   └── TASK_TRACKER.md           # Rencana sprint & checklist pengerjaan 4 minggu
├── src/
│   └── id/ac/pens/psdku/campuslosthub/
│       ├── Main.java             # Entry point aplikasi
│       ├── model/                # Domain entities (Item, Elektronik, Dokumen, Claimant)
│       ├── contract/             # Interface (Verifiable)
│       ├── service/              # Business logic & Manager (LostAndFoundService)
│       ├── exception/            # Custom exception handling
│       ├── storage/              # File I/O handling (CSV Reader/Writer)
│       └── view/                 # Antarmuka Console / Swing GUI
├── data/
│   └── lost_found_data.csv       # File penyimpanan persisten data barang
├── README.md
└── .gitignore
```

---

## 4. Alur Bisnis Utama

1. **Pelaporan Barang Temuan (Reporting):**
   * Penemu menginput informasi barang dan memilih jenisnya (`BarangElektronik` atau `BarangDokumen`).
   * Sistem membuat objek `Item` berstatus awal `TERSEDIA` dan menyimpannya secara persisten ke file CSV.
2. **Pencarian Katalog (Search & Filter):**
   * Mahasiswa mencari barang berdasarkan kata kunci nama atau memfilter lokasi ruangan kampus.
3. **Verifikasi Klaim Polimorfik (Verification):**
   * Pemilik mengajukan klaim dengan menginput data identitas (`Claimant`) dan bukti rahasia.
   * Method `verifyClaim(proof)` dieksekusi secara polimorfik:
     * Barang Elektronik: mencocokkan *Serial Number* / IMEI.
     * Barang Dokumen: mencocokkan Nama Pemilik / NIM.
   * Jika valid, status barang berubah menjadi `PROSES_KLAIM`. Jika gagal, melempar `InvalidClaimException`.
4. **Serah Terima Fisik (Resolution):**
   * Pemilik membawa kartu identitas fisik ke pos kampus untuk verifikasi akhir.
   * Status barang diperbarui menjadi `SELESAI`.

---

## 5. Cara Menjalankan Aplikasi

### Prasyarat
* Java Development Kit (JDK) 11, 17, 21, atau 26+.

### A. Menjalankan Antarmuka GUI Modern (Java Swing)

Aplikasi utama secara default berjalan menggunakan antarmuka grafis (**Java Swing**) modern:

#### Melalui PowerShell (Windows):
```powershell
# 1. Pastikan berada di direktori root proyek CampusLostHub
cd CampusLostHub

# 2. Kompilasi seluruh file Java ke direktori bin/
$files = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d bin $files

# 3. Jalankan aplikasi GUI Java Swing
java -cp bin id.ac.pens.psdku.campuslosthub.Main
```

#### Melalui Command Prompt (CMD) / Linux / macOS (Bash):
```bash
# 1. Kompilasi semua file Java
javac -d bin src/id/ac/pens/psdku/campuslosthub/*.java src/id/ac/pens/psdku/campuslosthub/*/*.java

# 2. Jalankan aplikasi GUI Java Swing
java -cp bin id.ac.pens.psdku.campuslosthub.Main
```

---

### B. Menjalankan Mode Console / Terminal (CLI)
Jika ingin menjalankan antarmuka alternatif berbasis Terminal / Console teks (misalnya untuk pengujian cepat CLI):
```bash
java -cp bin id.ac.pens.psdku.campuslosthub.Main console
```

---

### C. Menjalankan Pengujian Otomatis (Self-Check Tests)
Proyek menyediakan tiga berkas self-check pengujian fungsional untuk menguji model inti, persistensi CSV, dan GUI:
```powershell
# Kompilasi berkas pengujian
javac -cp bin -d bin tests/ModelSelfCheck.java tests/ServiceStorageSelfCheck.java tests/UISelfCheck.java

# Eksekusi pengujian
java -cp bin ModelSelfCheck
java -cp bin ServiceStorageSelfCheck
java -cp bin UISelfCheck
```

---

### D. Akun Pengujian Panel Petugas Pos / Administrator
Untuk menguji fitur verifikasi fisik, serah terima, dan kelola data inventaris di **Panel Petugas Pos**:
* **Username:** `admin` (atau `petugas`)
* **Password:** `admin123` (atau `petugas123`)
