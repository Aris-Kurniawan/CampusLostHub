# Dokumentasi Role — Aris Kurniawan

## 1. Identitas dan Peran

- **Nama:** Aris Kurniawan
- **NRP:** 3125521004
- **Role:** Lead Architect & Core OOP
- **Fokus:** Arsitektur sistem, UML, abstraksi, inheritance, dan polimorfisme inti.

## 2. Tanggung Jawab Utama

Aris bertanggung jawab terhadap fondasi arsitektur dan model inti aplikasi. Modul yang dibuat harus dapat digunakan oleh `LostAndFoundManager` milik Bilu dan `Main` milik Intan tanpa mengakses atribut model secara langsung.

### Modul yang dikerjakan

```text
src/id/ac/pens/psdku/campuslosthub/
├── contract/
│   └── Verifiable.java
├── model/
│   ├── Item.java
│   ├── BarangElektronik.java
│   ├── BarangDokumen.java
│   └── Claimant.java  # model pemohon dikerjakan bersama Intan
└── exception/
    ├── InvalidClaimException.java  # implementasi Bilu
    └── ItemNotFoundException.java  # implementasi Bilu
```

### Pekerjaan

1. Membuat interface `Verifiable` dengan method `verifyClaim(String proofData)`.
2. Membuat abstract class `Item` dengan atribut private, constructor berparameter, getter, setter tervalidasi, dan abstract method.
3. Membuat subclass `BarangElektronik`.
4. Membuat subclass `BarangDokumen`.
5. Menetapkan konstanta status `TERSEDIA`, `PROSES_KLAIM`, dan `SELESAI`.
6. Menerapkan inheritance, abstraction, encapsulation, dan polymorphism.
7. Menyediakan `toCsvString()` dan method pembaca atribut yang dibutuhkan storage layer.
8. Menyusun UML Use Case Diagram dan UML Class Diagram.
9. Menjelaskan hubungan `realization`, `inheritance`, `aggregation`, dan `dependency`.
10. Menjadi pemilik keputusan desain model sebelum diintegrasikan Bilu dan Intan.

## 3. Kontrak Antarmodul

Sabilu boleh menggunakan:

```java
item.getId();
item.getKategori();
item.getStatus();
item.setStatus(Item.STATUS_PROSES_KLAIM);
item.verifyClaim(proofData);
item.toCsvString();
```

Sabilu tidak boleh mengakses atribut private secara langsung. Intan hanya menggunakan method publik dari model/service, bukan field internal.

## 4. Kriteria PBO yang Dibuktikan

| Kriteria | Bukti pada modul Aris |
|---|---|
| Class dan object | `Item`, `BarangElektronik`, `BarangDokumen`, `Claimant` |
| Constructor | Constructor berparameter pada semua entity |
| Encapsulation | Field private dan akses melalui method |
| Inheritance | Dua subclass meng-extend `Item` |
| Polymorphism | Override `verifyClaim`, `getKategori`, `displayInfo`, `toCsvString` |
| Abstraction | `Item` abstract dan `Verifiable` interface |
| Exception | Kontrak error disiapkan untuk digunakan Bilu |

## 5. Acceptance Check

- Elektronik menerima klaim berdasarkan serial number/IMEI.
- Dokumen menerima klaim berdasarkan nomor identitas atau nama pemilik.
- Status tidak dapat berpindah secara sembarangan.
- Data rahasia tidak ditampilkan pada informasi publik.
- Setiap subclass dapat disimpan melalui referensi tipe `Item`.
