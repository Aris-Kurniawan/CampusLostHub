# Modul Pembelajaran Komprehensif PBO — CampusLostHub

Modul ini disusun sebagai panduan belajar mendalam untuk memahami konsep, arsitektur, dan implementasi kode pada proyek **CampusLostHub (Smart Campus Lost & Found System)**. Modul ini mencakup 8 topik metode utama yang menjadi kriteria penilaian Final Project Praktikum Pemrograman Berorientasi Objek (PBO).

---

## Daftar Isi
1. [Lingkungan Pengembangan](#1-lingkungan-pengembangan)
2. [Struktur Program](#2-struktur-program)
3. [Implementasi Class dan Object](#3-implementasi-class-dan-object)
4. [Implementasi Encapsulation](#4-implementasi-encapsulation)
5. [Implementasi Inheritance](#5-implementasi-inheritance)
6. [Implementasi Polymorphism](#6-implementasi-polymorphism)
7. [Implementasi Abstraction](#7-implementasi-abstraction)
8. [Implementasi Exception Handling & Collection](#8-implementasi-exception-handling--collection)

---

## 1. Lingkungan Pengembangan

### 1.1 Spesifikasi Teknis
* **Bahasa Pemrograman:** Java SE (Standard Edition) — kompatibel JDK 11, 17, 21, hingga 26.
* **Pustaka (Libraries):** 100% Java Standard Library (`java.util`, `java.io`, `javax.swing`). Tidak menggunakan dependensi/framework eksternal (seperti Maven, Gradle, Spring, atau Lombok) agar kode murni dan portabel saat dinilai dosen.
* **Penyimpanan Data:** File teks berbasis CSV (`data/lost_found_data.csv`). Tidak membutuhkan instalasi DBMS server (seperti MySQL/PostgreSQL).
* **Bentuk Antarmuka:** 
  1. **Mode GUI:** Java Swing Desktop ([MainFrame.java](file:///c:/Users/arisp/Documents/KAMPUS/Semester%203/PBO/Projek/CampusLostHub/src/id/ac/pens/psdku/campuslosthub/view/MainFrame.java)).
  2. **Mode CLI:** Console Terminal interaktif ([ConsoleView.java](file:///c:/Users/arisp/Documents/KAMPUS/Semester%203/PBO/Projek/CampusLostHub/src/id/ac/pens/psdku/campuslosthub/view/ConsoleView.java)).

### 1.2 Perintah Kompilasi dan Eksekusi (CLI)
Dari direktori *root* proyek:

```bash
# 1. Kompilasi seluruh file Java ke folder bin/
javac -d bin $(find src -name '*.java' -print)
# atau di Windows PowerShell:
javac -d bin (Get-ChildItem -Recurse -Filter *.java -Path src | Select-Object -ExpandProperty FullName)

# 2. Menjalankan Mode GUI (Default)
java -cp bin id.ac.pens.psdku.campuslosthub.Main

# 3. Menjalankan Mode Console CLI
java -cp bin id.ac.pens.psdku.campuslosthub.Main console

# 4. Menjalankan Pengujian Mandiri (Self-Check)
javac -cp bin -d bin tests/ModelSelfCheck.java tests/ServiceStorageSelfCheck.java
java -cp bin ModelSelfCheck
java -cp bin ServiceStorageSelfCheck
```

---

## 2. Struktur Program

Proyek ini menerapkan **Layered Separation of Concerns** (Arsitektur Berlapis) untuk memisahkan tanggung jawab antarmodul:

```text
src/id/ac/pens/psdku/campuslosthub/
├── Main.java               # Entry Point Aplikasi
├── contract/               # Interface Abstraksi
│   └── Verifiable.java
├── model/                  # Entitas Data Inti (Domain Model)
│   ├── Item.java           # Abstract Parent Class
│   ├── BarangElektronik.java
│   ├── BarangDokumen.java
│   └── Claimant.java       # Identitas Pemohon Klaim
├── exception/              # Custom Exceptions
│   ├── ItemNotFoundException.java
│   ├── InvalidClaimException.java
│   └── StorageException.java
├── service/                # Business Logic / Controller
│   └── LostAndFoundManager.java
├── storage/                # File I/O Persistence
│   └── CsvStorage.java
└── view/                   # Antarmuka Pengguna
    ├── ConsoleView.java
    ├── MainFrame.java
    ├── InputValidator.java
    ├── TableFormatter.java
    └── ViewUtil.java
```

### Alur Ketergantungan (Dependency Flow):
```text
[ View Layer (ConsoleView / MainFrame) ]
                   │
                   ▼  (Memanggil public method service)
[ Service Layer (LostAndFoundManager) ] ──> [ Storage Layer (CsvStorage) ]
                   │
                   ▼  (Memanipulasi objek via referensi tipe Item)
[ Model & Contract Layer (Item, Elektronik, Dokumen, Verifiable) ]
```

> **Aturan Arsitektur:**
> 1. UI **dilarang keras** membaca/menulis file CSV secara langsung.
> 2. UI dan Service **dilarang mengakses atribut private model** secara langsung.
> 3. Logika verifikasi kepemilikan wajib berada pada subclass model, bukan disalin ke UI.

---

## 3. Implementasi Class dan Object

### 3.1 Konsep Dasar
* **Class** adalah cetak biru (*blueprint*) yang mendefinisikan atribut (*state*) dan perilaku (*behavior/method*).
* **Object** adalah instansiasi nyata dari class yang menempati memori runtime (*Heap Memory*).

### 3.2 Constructor Berparameter & Instansiasi
Setiap entitas model diwajibkan memiliki constructor berparameter untuk memastikan objek yang dibuat selalu berada dalam kondisi valid (*defensive programming*):

```java
// Instansiasi Objek Nyata (Polymorphic Reference):
Item item1 = new BarangElektronik(
    "BRG-1001", 
    "Flashdisk SanDisk 32GB", 
    "Warna merah hitam tali biru", 
    "Lab Pemrograman", 
    "2026-11-20", 
    "SD32G-98421",   // Atribut khusus: nomorSeri
    "SanDisk"         // Atribut khusus: brand
);

Claimant pemohon = new Claimant(
    "3125521004",           // NIM
    "Aris Kurniawan",       // Nama Mahasiswa
    "081234567890"          // No. HP
);
```

---

## 4. Implementasi Encapsulation

### 4.1 Prinsip Information Hiding & Defensive Validation
Enkapsulasi dicapai dengan:
1. Menandai seluruh atribut dengan access modifier **`private`** (atau `private final` jika nilainya tidak boleh diubah setelah inisialisasi).
2. Menyediakan method **Getter** untuk akses baca.
3. Membatasi **Setter** hanya untuk atribut yang sah dimutasi (seperti `status`) dengan validasi ketat.

### 4.2 Contoh Implementasi pada `Item.java`
```java
public abstract class Item implements Verifiable {
    // 1. Atribut private (Information Hiding)
    private final String id;
    private final String nama;
    private final String deskripsi;
    private final String lokasiDitemukan;
    private final String tanggalDitemukan;
    private String status;

    // 2. Validasi di Constructor
    public Item(String id, String nama, String deskripsi, String lokasi, String tanggal) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID tidak boleh kosong.");
        }
        this.id = id.trim();
        this.nama = nama.trim();
        this.deskripsi = deskripsi.trim();
        this.lokasiDitemukan = lokasi.trim();
        this.tanggalDitemukan = tanggal.trim();
        this.status = STATUS_TERSEDIA; // Status default awal
    }

    // 3. Setter dengan Validasi Transisi Status
    public void setStatus(String statusBaru) {
        if (!isTransitionValid(this.status, statusBaru)) {
            throw new IllegalArgumentException("Transisi status tidak sah: " 
                + this.status + " -> " + statusBaru);
        }
        this.status = statusBaru;
    }
}
```

### 4.3 Aturan Transisi Status Sah:
```text
TERSEDIA     ──(Klaim diajukan)──>   PROSES_KLAIM
PROSES_KLAIM ──(Klaim ditolak)───>   TERSEDIA
PROSES_KLAIM ──(Serah terima)────>   SELESAI
```
Status tidak dapat melompat langsung dari `TERSEDIA` ke `SELESAI` tanpa melalui proses verifikasi klaim.

---

## 5. Implementasi Inheritance

### 5.1 Hierarki Pewarisan (IS-A Relationship)
Inheritance memungkinkan subclass mewarisi atribut dan method dari superclass, sehingga mencegah duplikasi kode (*Code Reusability*).

```text
       ┌────────────────────────┐
       │     <<abstract>>       │
       │         Item           │
       └────────────────────────┘
                   ▲
          ┌────────┴────────┐
          │                 │
┌──────────────────┐ ┌──────────────────┐
│ BarangElektronik │ │  BarangDokumen   │
└──────────────────┘ └──────────────────┘
```

### 5.2 Penggunaan Kata Kunci `extends` dan `super(...)`
Subclass memanggil constructor superclass menggunakan `super(...)`:

```java
public class BarangElektronik extends Item {
    // Atribut khusus barang elektronik
    private final String nomorSeri;
    private final String brand;

    public BarangElektronik(String id, String nama, String deskripsi, 
                            String lokasi, String tanggal, 
                            String nomorSeri, String brand) {
        // Alirkan atribut umum ke constructor Item
        super(id, nama, deskripsi, lokasi, tanggal);
        
        // Inisialisasi atribut khusus
        this.nomorSeri = nomorSeri.trim();
        this.brand = brand.trim();
    }
}
```

---

## 6. Implementasi Polymorphism

Polimorfisme (banyak bentuk) diimplementasikan melalui **Method Overriding** dan **Dynamic Method Dispatch** (Runtime Polymorphism).

### 6.1 Polimorfisme pada `verifyClaim(String proofData)`
Superclass `Item` mengimplementasikan interface `Verifiable`, tetapi menyerahkan implementasi logika verifikasi kepada subclass secara independen:

1. **Pada `BarangElektronik`:**
   Memeriksa kecocokan Serial Number / IMEI secara *case-insensitive* dan membersihkan karakter spasi/tanda hubung:
   ```java
   @Override
   public boolean verifyClaim(String proofData) {
       if (proofData == null) return false;
       String cleanProof = proofData.replaceAll("[\\s-]", "").toUpperCase();
       String cleanSerial = this.nomorSeri.replaceAll("[\\s-]", "").toUpperCase();
       return cleanProof.equals(cleanSerial);
   }
   ```

2. **Pada `BarangDokumen`:**
   Memeriksa kecocokan Nomor Identitas (NIM/NIK) atau kecocokan Nama Lengkap Pemilik Dokumen:
   ```java
   @Override
   public boolean verifyClaim(String proofData) {
       if (proofData == null) return false;
       String input = proofData.trim().toLowerCase();
       return input.equals(this.nomorIdentitas.trim().toLowerCase()) 
           || input.equals(this.namaPemilik.trim().toLowerCase());
   }
   ```

### 6.2 Eksekusi Polimorfik pada Service Layer
Di `LostAndFoundManager.java`, pemanggilan verifikasi dilakukan seragam melalui referensi tipe induk `Item` **tanpa perlu memeriksa jenis objek menggunakan `instanceof`**:

```java
// Pemanggilan polimorfik murni:
Item item = findById(id);
if (!item.verifyClaim(bukti)) {
    throw new InvalidClaimException("Bukti klaim tidak cocok dengan data barang.");
}
item.setStatus(Item.STATUS_PROSES_KLAIM);
```

---

## 7. Implementasi Abstraction

Abstraksi menyembunyikan detail implementasi yang rumit dan hanya menampilkan antarmuka/kontrak fungsi yang diperlukan.

### 7.1 Abstract Class `Item`
`Item` dideklarasikan sebagai `abstract` karena secara konseptual, tidak ada barang fisik di dunia nyata yang hanya bernama "Barang/Item" tanpa jenis spesifik.

```java
public abstract class Item implements Verifiable {
    // Abstract methods wajib di-override oleh setiap subclass konkret:
    public abstract String getKategori();
    public abstract void displayInfo();
    public abstract String toCsvString();
}
```

### 7.2 Interface `Verifiable`
Interface adalah kontrak murni tanpa state (*stateless contract*):

```java
package id.ac.pens.psdku.campuslosthub.contract;

public interface Verifiable {
    boolean verifyClaim(String proofData);
}
```

| Aspek | Abstract Class (`Item`) | Interface (`Verifiable`) |
|---|---|---|
| **Tujuan** | Kerangka dasar entitas barang & *code reuse* atribut umum. | Kontrak kemampuan (*capability*) verifikasi klaim. |
| **Atribut** | Menyimpan field `private` (`id`, `nama`, dll.). | Tidak menyimpan atribut instansi. |
| **Keyword** | `extends` (Single Inheritance). | `implements` (Bisa Multiple Realization). |

---

## 8. Implementasi Exception Handling & Collection

### 8.1 Custom Exceptions
Aplikasi membuat kelas turunan `Exception` untuk merepresentasikan error spesifik domain:
* **`ItemNotFoundException`:** Dilempar saat ID barang yang dicari tidak ditemukan di memori.
* **`InvalidClaimException`:** Dilempar saat bukti klaim tidak cocok atau barang sedang tidak berstatus `TERSEDIA`.
* **`StorageException`:** Dilempar saat terjadi kegagalan baca/tulis file CSV.

### 8.2 Blok `try-catch-finally`
Contoh penanganan di `ConsoleView.java` agar aplikasi tidak *crash* saat terjadi kesalahan input:

```java
try {
    manager.claimItem(id, claimant, bukti);
    System.out.println("Klaim berhasil diproses! Silakan verifikasi fisik di pos satpam.");
} catch (InvalidClaimException e) {
    System.out.println("[KLAIM DITOLAK] " + e.getMessage());
} catch (ItemNotFoundException e) {
    System.out.println("[TIDAK DITEMUKAN] " + e.getMessage());
} catch (Exception e) {
    System.out.println("[ERROR SISTEM] Terjadi kesalahan tidak terduga: " + e.getMessage());
}
```

### 8.3 Collection Framework (`ArrayList<Item>`)
Koleksi barang di memori dikelola secara dinamis menggunakan `ArrayList<Item>` pada `LostAndFoundManager.java`:

```java
private final ArrayList<Item> daftarBarang = new ArrayList<>();

// 1. Menambahkan Objek
public void addItem(Item item) {
    daftarBarang.add(item);
    simpanKeFile(); // Auto-persist ke CSV
}

// 2. Pencarian Linear Case-Insensitive
public ArrayList<Item> searchByName(String keyword) {
    ArrayList<Item> hasil = new ArrayList<>();
    String kw = keyword.toLowerCase().trim();
    for (Item item : daftarBarang) {
        if (item.getNama().toLowerCase().contains(kw)) {
            hasil.add(item);
        }
    }
    return hasil;
}
```

### 8.4 File Handling CSV (`java.io`)
Persistensi dilakukan di `CsvStorage.java` menggunakan:
* **`BufferedReader`:** Membaca baris teks CSV per baris saat aplikasi dimulai (*startup*), lalu men-deserialisasi kolom menjadi objek `BarangElektronik` atau `BarangDokumen`.
* **`BufferedWriter`:** Menulis seluruh isi `ArrayList<Item>` kembali ke `lost_found_data.csv` setiap kali terjadi perubahan status atau penambahan barang.

---

## Rangkuman Pertanyaan & Jawaban Cepat untuk Evaluasi Dosen

1. **Kenapa `Item` dibuat sebagai `abstract class` dan bukan class biasa?**
   * *Jawaban:* Karena `Item` adalah generalisasi. Objek nyata di lapangan harus spesifik berupa `BarangElektronik` atau `BarangDokumen` yang memiliki aturan verifikasi unik masing-masing.
2. **Di mana polimorfisme terjadi di kode Anda?**
   * *Jawaban:* Pada pemanggilan `item.verifyClaim(bukti)`. Referensi bertipe `Item` memanggil method yang implementasinya berbeda secara otomatis di runtime tergantung apakah objek tersebut bertipe `BarangElektronik` atau `BarangDokumen`.
3. **Bagaimana cara mencegah pengguna mengubah status barang sembarangan?**
   * *Jawaban:* Melalui enkapsulasi. Atribut `status` bersifat `private`, dan setter `setStatus()` menerapkan validasi transisi status (*state machine*).
4. **Mengapa tidak menggunakan MySQL?**
   * *Jawaban:* Sesuai juknis PBO kriteria 8 dan 9, proyek membuktikan implementasi `ArrayList<Item>` dan File I/O (`BufferedReader`/`BufferedWriter`). Menggunakan CSV juga membuat aplikasi 100% portabel dan dapat dijalankan instan di laptop mana pun tanpa konfigurasi DBMS.
