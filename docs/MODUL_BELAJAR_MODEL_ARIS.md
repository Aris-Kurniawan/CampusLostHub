# Modul Belajar Model Inti CampusLostHub

Modul lokal untuk mempelajari kode role Aris. Folder ini sengaja di-ignore Git dan tidak dikirim ke GitHub.

## Tujuan belajar

Setelah menyelesaikan modul ini, kamu harus bisa menjelaskan dan mendemonstrasikan:

- Interface.
- Abstract class.
- Encapsulation.
- Constructor.
- Inheritance.
- Polymorphism.
- Overriding.
- Validasi input.
- Transisi status.
- Serialisasi sederhana ke CSV.

## File yang dipelajari

```text
src/id/ac/pens/psdku/campuslosthub/
├── contract/Verifiable.java
└── model/
    ├── Item.java
    ├── BarangElektronik.java
    └── BarangDokumen.java
```

---

## Bagian 1 — Baca alur program

Urutan hubungan class:

```text
Verifiable
    ↑
Item
  ↑       ↑
Elektronik Dokumen
```

Penjelasan:

1. `Verifiable` menentukan kemampuan memeriksa klaim.
2. `Item` menyimpan data umum semua barang.
3. `BarangElektronik` menambahkan serial number dan brand.
4. `BarangDokumen` menambahkan nomor identitas dan nama pemilik.
5. Kedua subclass memiliki cara verifikasi yang berbeda.

### Pertanyaan cek pemahaman

- Mengapa `Item` tidak dibuat sebagai `class` biasa?
- Mengapa `verifyClaim` tidak ditulis di UI?
- Apa perbedaan data umum dan data khusus?

Jawaban singkat:

- `Item` abstract karena hanya menjadi dasar bersama dan belum tahu cara verifikasi setiap kategori.
- Verifikasi adalah aturan domain, jadi berada pada subclass barang.
- Data umum berupa ID, nama, lokasi, tanggal, dan status. Data khusus berupa serial number atau identitas pemilik.

---

## Bagian 2 — Interface `Verifiable`

Kode:

```java
public interface Verifiable {
    boolean verifyClaim(String proofData);
}
```

Interface hanya menyatakan **apa yang wajib dimiliki**, bukan bagaimana cara melakukannya.

Pada proyek ini:

```text
Elektronik → verifikasi serial number
Dokumen    → verifikasi NIM atau nama pemilik
```

### Demo perubahan sederhana

Ubah nama parameter:

```java
boolean verifyClaim(String bukti);
```

Tidak mengubah perilaku program karena parameter hanya nama lokal. Kembalikan ke `proofData` agar konsisten dengan dokumentasi.

### Pertanyaan presentasi

**Mengapa memakai interface?**

Jawaban:

> Karena semua barang harus memiliki kemampuan verifikasi klaim, tetapi algoritma verifikasinya berbeda sesuai kategori barang.

---

## Bagian 3 — Encapsulation pada `Item`

Contoh field:

```java
private final String id;
private final String nama;
private String status;
```

Field dibuat `private` agar class lain tidak dapat mengubah data secara langsung.

Akses dilakukan lewat method:

```java
public String getId() {
    return id;
}

public String getStatus() {
    return status;
}
```

Status tidak boleh diubah sembarangan. Perubahan harus melewati:

```java
item.setStatus(Item.STATUS_PROSES_KLAIM);
```

### Mengapa field memakai `final`?

`id`, `nama`, deskripsi, lokasi, dan tanggal tidak boleh berubah setelah object dibuat. `status` tidak final karena status memang berubah selama proses klaim.

### Relasi In-Memory `Claimant` pada `Item`

`Item` juga menyimpan relasi objek `private Claimant claimant;` beserta `getClaimant()` dan `setClaimant(Claimant claimant)`:

- Field `claimant` tidak memakai `final` karena nilainya diisi saat mahasiswa mengajukan klaim (`claimItem`), dibersihkan menjadi `null` jika klaim dibatalkan (`cancelClaim`), dan tetap tersimpan di memori agar panel **Verifikasi Klaim** dan **Riwayat Selesai** bisa menampilkan identitas pemohon.
- Relasi ini hanya hidup di memori selama sesi berjalan dan **tidak mengubah format 9 kolom `lost_found_data.csv`**.

### Demo perubahan sederhana

Tambahkan validasi ke constructor:

```java
if (id == null || id.trim().isEmpty()) {
    throw new IllegalArgumentException("id tidak boleh kosong");
}
```

Efeknya: object dengan ID kosong ditolak sejak awal.

---

## Bagian 4 — Abstract class `Item`

Deklarasi:

```java
public abstract class Item implements Verifiable
```

Artinya:

- `Item` tidak dapat dibuat langsung dengan `new Item(...)`.
- `Item` menggunakan kontrak `Verifiable`.
- Subclass wajib menyediakan implementasi method abstract.

Method abstract:

```java
public abstract String getKategori();
public abstract void displayInfo();
public abstract String toCsvString();
```

`Item` mengetahui bahwa semua barang punya kategori, informasi tampilan, dan bentuk CSV. Namun detailnya diserahkan kepada subclass.

### Pertanyaan presentasi

**Mengapa `getKategori()` dibuat abstract?**

Jawaban:

> Karena kategori ditentukan oleh subclass. `BarangElektronik` mengembalikan `ELEKTRONIK`, sedangkan `BarangDokumen` mengembalikan `DOKUMEN`.

---

## Bagian 5 — Inheritance

Deklarasi subclass:

```java
public class BarangElektronik extends Item
public class BarangDokumen extends Item
```

Keduanya mewarisi:

- `id` melalui getter.
- `nama` melalui getter.
- `deskripsi` melalui getter.
- `lokasiDitemukan` melalui getter.
- `tanggalDitemukan` melalui getter.
- `status` melalui getter dan setter.

Constructor subclass memanggil constructor induk:

```java
super(id, nama, deskripsi, lokasi, tanggal);
```

### Mengapa memakai `super(...)`?

Agar data umum diinisialisasi oleh `Item`, sedangkan data khusus diinisialisasi oleh subclass.

---

## Bagian 6 — Polymorphism

Contoh:

```java
Item barang1 = new BarangElektronik(...);
Item barang2 = new BarangDokumen(...);

barang1.verifyClaim("SN123");
barang2.verifyClaim("3125521004");
```

Variabelnya sama-sama bertipe `Item`, tetapi method yang dijalankan berbeda sesuai object sebenarnya.

```text
barang1 → BarangElektronik.verifyClaim()
barang2 → BarangDokumen.verifyClaim()
```

Inilah polymorphism melalui method overriding.

### Demo presentasi

Tunjukkan kode berikut:

```java
Item barang = new BarangElektronik(
        "BRG-1", "Flashdisk", "Merah",
        "Lab", "2026-11-15", "SN-123", "SanDisk");

System.out.println(barang.getKategori());
System.out.println(barang.verifyClaim("SN123"));
```

Output yang dijelaskan:

```text
ELEKTRONIK
true
```

Lalu ganti object menjadi `BarangDokumen` tanpa mengubah tipe variabel `Item`. Itu menunjukkan polymorphism.

---

## Bagian 7 — Verifikasi elektronik

Kode inti:

```java
String bukti = proofData.replace("-", "")
        .replace(" ", "").toLowerCase();
String seri = nomorSeri.replace("-", "")
        .replace(" ", "").toLowerCase();
return seri.equals(bukti);
```

Contoh:

```text
Nomor tersimpan: SN-123
Bukti pengguna:  sn123
Hasil: true
```

Spasi, tanda hubung, dan huruf besar tidak memengaruhi pencocokan.

### Kenapa serial number tidak ditampilkan?

Serial number adalah bukti rahasia. Jika ditampilkan di katalog, semua orang dapat menyalin bukti klaim.

---

## Bagian 8 — Verifikasi dokumen

Kode inti:

```java
return nomorIdentitas.equalsIgnoreCase(bukti)
        || namaPemilik.equalsIgnoreCase(bukti);
```

Dokumen dapat diverifikasi memakai:

- NIM/nomor identitas.
- Nama pemilik.

Data tersebut tetap tidak ditampilkan pada katalog publik.

---

## Bagian 9 — Transisi status

Status awal setiap object:

```text
TERSEDIA
```

Alur sah:

```text
TERSEDIA → PROSES_KLAIM → SELESAI
```

Jika klaim ditolak:

```text
PROSES_KLAIM → TERSEDIA
```

Contoh:

```java
barang.setStatus(Item.STATUS_PROSES_KLAIM);
barang.setStatus(Item.STATUS_SELESAI);
```

Contoh yang harus ditolak:

```java
barang.setStatus(Item.STATUS_SELESAI);
```

Object baru tidak boleh langsung menjadi `SELESAI` karena belum melewati proses klaim.

---

## Bagian 10 — Serialisasi CSV

Method:

```java
public String toCsvString()
```

Menghasilkan sembilan kolom:

```text
id,kategori,nama,deskripsi,lokasi,tanggal,status,extra1,extra2
```

Untuk elektronik:

```text
extra1 = nomorSeri
extra2 = brand
```

Untuk dokumen:

```text
extra1 = nomorIdentitas
extra2 = namaPemilik
```

### Pertanyaan presentasi

**Mengapa `toCsvString()` dioverride?**

Jawaban:

> Karena format data khusus elektronik dan dokumen berbeda, walaupun keduanya memiliki field umum yang sama.

---

## Bagian 11 — Latihan mandiri

Kerjakan tanpa melihat jawaban terlebih dahulu.

### Latihan 1

Tambahkan method berikut pada `Item`:

```java
public boolean isAvailable()
```

Method mengembalikan `true` jika status adalah `TERSEDIA`.

### Latihan 2

Ubah `displayInfo()` pada elektronik agar menampilkan brand tetapi tidak menampilkan serial number.

### Latihan 3

Buat object dokumen baru:

```text
ID: BRG-3001
Nama: KTM PENS
Deskripsi: Kartu mahasiswa
Lokasi: Perpustakaan
Tanggal: 2026-11-15
Nomor identitas: 3125521024
Nama pemilik: Intan Kusumaningarum
```

Uji klaim dengan nama dan NIM.

### Latihan 4

Coba ubah status object dari `TERSEDIA` langsung ke `SELESAI`. Jelaskan exception yang muncul.

### Latihan 5

Hitung jumlah kolom output `toCsvString()` menggunakan:

```java
hasil.split(",", -1).length
```

Target:

```text
9
```

---

## Bagian 12 — Checklist sebelum presentasi

- [ ] Bisa menjelaskan interface.
- [ ] Bisa menjelaskan abstract class.
- [ ] Bisa menunjukkan field private.
- [ ] Bisa menjelaskan constructor `super(...)`.
- [ ] Bisa menunjukkan inheritance.
- [ ] Bisa menunjukkan overriding.
- [ ] Bisa mendemonstrasikan polymorphism.
- [ ] Bisa menjelaskan validasi input.
- [ ] Bisa menjelaskan transisi status.
- [ ] Bisa menjelaskan format CSV.
- [ ] Bisa melakukan satu perubahan kecil pada kode.
- [ ] Bisa compile dan menjalankan self-check di laptop presentasi.

## Jawaban singkat untuk dosen

**Apa inti bagianmu?**

> Saya membuat fondasi model domain dan kontrak OOP. `Item` menjadi abstract class untuk data umum barang, lalu `BarangElektronik` dan `BarangDokumen` mewarisi `Item` dengan cara verifikasi klaim yang berbeda.

**Di mana polymorphism-nya?**

> Pada method `verifyClaim`. Service dapat memanggil method yang sama melalui referensi `Item`, tetapi Java menjalankan implementasi subclass sesuai object sebenarnya.

**Mengapa data dibuat private?**

> Untuk menerapkan encapsulation dan mencegah perubahan data secara langsung. Perubahan status harus melalui aturan yang sudah divalidasi.

**Mengapa menggunakan CSV?**

> Karena aplikasi merupakan project Java standalone dan kebutuhan penyimpanan masih sederhana. CSV mudah dibaca, mudah didemonstrasikan, dan tidak membutuhkan database eksternal.
