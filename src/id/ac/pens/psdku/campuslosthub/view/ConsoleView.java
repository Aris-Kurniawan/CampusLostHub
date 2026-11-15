package id.ac.pens.psdku.campuslosthub.view;

import id.ac.pens.psdku.campuslosthub.exception.InvalidClaimException;
import id.ac.pens.psdku.campuslosthub.model.BarangDokumen;
import id.ac.pens.psdku.campuslosthub.model.BarangElektronik;
import id.ac.pens.psdku.campuslosthub.model.Claimant;
import id.ac.pens.psdku.campuslosthub.model.Item;
import id.ac.pens.psdku.campuslosthub.service.LostAndFoundManager;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Tampilan console CampusLostHub.
 * Class ini hanya mengurus input/output, logika bisnis ada di LostAndFoundManager
 * dan verifikasi klaim ada di model (verifyClaim).
 */
public class ConsoleView {

    private LostAndFoundManager manager;
    private InputValidator input;

    public ConsoleView(LostAndFoundManager manager) {
        this.manager = manager;
        this.input = new InputValidator(new Scanner(System.in));
    }

    public void jalankan() {
        boolean running = true;

        while (running) {
            tampilkanMenu();
            int pilihan = input.bacaPilihan("Pilih menu", 0, 6);

            switch (pilihan) {
                case 1:
                    laporBarang();
                    break;
                case 2:
                    System.out.println("\n--- Katalog Barang Aktif ---");
                    tampilkanTabel(ViewUtil.hanyaAktif(manager.getAllItems()));
                    break;
                case 3:
                    cariBarang();
                    break;
                case 4:
                    ajukanKlaim();
                    break;
                case 5:
                    serahTerima();
                    break;
                case 6:
                    batalKlaim();
                    break;
                case 0:
                    System.out.println("Terima kasih telah menggunakan CampusLostHub.");
                    running = false;
                    break;
            }
        }
    }

    private void tampilkanMenu() {
        System.out.println();
        System.out.println("=====================================");
        System.out.println("   CampusLostHub - Lost & Found PENS");
        System.out.println("=====================================");
        System.out.println("1. Lapor barang temuan");
        System.out.println("2. Lihat katalog barang aktif");
        System.out.println("3. Cari barang");
        System.out.println("4. Ajukan klaim barang");
        System.out.println("5. Konfirmasi serah terima (petugas pos)");
        System.out.println("6. Batalkan klaim");
        System.out.println("0. Keluar");
        System.out.println("-------------------------------------");
    }

    // ---------- 1. Lapor barang ----------
    private void laporBarang() {
        System.out.println("\n--- Lapor Barang Temuan ---");
        System.out.println("1. Barang Elektronik");
        System.out.println("2. Barang Dokumen");
        int jenis = input.bacaPilihan("Pilih jenis", 1, 2);

        String nama = input.bacaTeks("Nama barang");
        String deskripsi = input.bacaTeks("Deskripsi singkat");
        String lokasi = input.bacaTeks("Lokasi ditemukan (contoh: Lab Pemrograman)");
        String tanggal = input.bacaTanggal("Tanggal ditemukan");

        String id = ViewUtil.buatIdBaru(manager.getAllItems());

        try {
            Item itemBaru;
            if (jenis == 1) {
                String nomorSeri = input.bacaTeks("Serial Number / IMEI");
                String brand = input.bacaTeks("Brand");
                itemBaru = new BarangElektronik(id, nama, deskripsi, lokasi, tanggal,
                        nomorSeri, brand);
            } else {
                String nomorIdentitas = input.bacaTeks("Nomor identitas dokumen (NIM/plat/nomor lain)");
                String namaPemilik = input.bacaTeks("Nama pemilik di dokumen");
                itemBaru = new BarangDokumen(id, nama, deskripsi, lokasi, tanggal,
                        nomorIdentitas, namaPemilik);
            }
            manager.addItem(itemBaru);
            System.out.println("Barang berhasil dilaporkan dengan ID " + id);
        } catch (Exception e) {
            System.out.println("[!] Gagal menyimpan barang: " + e.getMessage());
        }
    }

    // ---------- 3. Cari barang ----------
    private void cariBarang() {
        System.out.println("\n--- Cari Barang ---");
        System.out.println("1. Cari berdasarkan nama");
        System.out.println("2. Filter berdasarkan lokasi");
        int pilihan = input.bacaPilihan("Pilih", 1, 2);

        ArrayList<Item> hasil;
        if (pilihan == 1) {
            String kata = input.bacaTeks("Kata kunci nama");
            hasil = manager.searchByName(kata);
        } else {
            String lokasi = input.bacaTeks("Lokasi ruangan");
            hasil = manager.filterByLocation(lokasi);
        }
        tampilkanTabel(hasil);
    }

    // ---------- 4. Klaim ----------
    private void ajukanKlaim() {
        System.out.println("\n--- Ajukan Klaim ---");
        String id = input.bacaTeks("ID barang yang diklaim (contoh: BRG-1001)");

        Item item = manager.findById(id);
        if (item == null) {
            System.out.println("[!] Barang dengan ID " + id + " tidak ditemukan.");
            return;
        }

        System.out.println("Barang: " + item.getNama() + " | Status: " + item.getStatus());
        if (!item.getStatus().equals(Item.STATUS_TERSEDIA)) {
            System.out.println("[!] Barang ini tidak bisa diklaim karena statusnya bukan TERSEDIA.");
            return;
        }

        String namaClaimant = input.bacaTeks("Nama lengkap pengklaim");
        String nim = input.bacaNim("NIM pengklaim");
        String noHp = input.bacaNoHp("No. HP pengklaim");

        String petunjuk;
        if (item instanceof BarangElektronik) {
            petunjuk = "Bukti (Serial Number / IMEI)";
        } else {
            petunjuk = "Bukti (Nomor identitas / Nama pemilik di dokumen)";
        }
        String bukti = input.bacaTeks(petunjuk);

        try {
            Claimant claimant = new Claimant(namaClaimant, nim, noHp);
            manager.claimItem(id, claimant, bukti);
            System.out.println("Klaim valid! Status barang sekarang PROSES_KLAIM.");
            System.out.println("Silakan bawa kartu identitas fisik ke pos kampus.");
        } catch (InvalidClaimException e) {
            System.out.println("[!] Klaim ditolak: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[!] Terjadi kesalahan: " + e.getMessage());
        }
    }

    // ---------- 5. Serah terima ----------
    private void serahTerima() {
        System.out.println("\n--- Serah Terima Barang ---");
        String id = input.bacaTeks("ID barang");

        Item item = manager.findById(id);
        if (item == null) {
            System.out.println("[!] Barang tidak ditemukan.");
            return;
        }
        if (!item.getStatus().equals(Item.STATUS_PROSES_KLAIM)) {
            System.out.println("[!] Serah terima hanya untuk barang berstatus PROSES_KLAIM.");
            return;
        }

        boolean yakin = input.bacaYaTidak("Kartu identitas fisik sudah dicek, selesaikan serah terima");
        if (!yakin) {
            System.out.println("Serah terima dibatalkan.");
            return;
        }

        try {
            manager.completeHandover(id);
            System.out.println("Serah terima selesai. Status barang: SELESAI.");
        } catch (Exception e) {
            System.out.println("[!] Gagal: " + e.getMessage());
        }
    }

    // ---------- 6. Batalkan klaim ----------
    private void batalKlaim() {
        System.out.println("\n--- Batalkan Klaim ---");
        String id = input.bacaTeks("ID barang");

        Item item = manager.findById(id);
        if (item == null) {
            System.out.println("[!] Barang tidak ditemukan.");
            return;
        }
        if (!item.getStatus().equals(Item.STATUS_PROSES_KLAIM)) {
            System.out.println("[!] Hanya klaim berstatus PROSES_KLAIM yang bisa dibatalkan.");
            return;
        }

        boolean yakin = input.bacaYaTidak("Yakin batalkan klaim barang ini");
        if (!yakin) {
            System.out.println("Pembatalan dibatalkan.");
            return;
        }

        try {
            manager.cancelClaim(id);
            System.out.println("Klaim dibatalkan. Status barang kembali TERSEDIA.");
        } catch (Exception e) {
            System.out.println("[!] Gagal: " + e.getMessage());
        }
    }

    private void tampilkanTabel(ArrayList<Item> daftar) {
        TableFormatter.cetak(daftar);
    }
}