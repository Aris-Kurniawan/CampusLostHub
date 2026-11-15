package id.ac.pens.psdku.campuslosthub.view;

import id.ac.pens.psdku.campuslosthub.model.Item;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Fungsi bantu yang dipakai ConsoleView dan MainFrame.
 */
public class ViewUtil {

    // ID baru mengikuti format di CSV: BRG-1001, BRG-1002, dst.
    // Kalau nanti Bilu menyediakan pembuat ID di manager, ganti method ini.
    public static String buatIdBaru(ArrayList<Item> daftar) {
        int terbesar = 1000;
        for (Item item : daftar) {
            String id = item.getId();
            if (id.startsWith("BRG-")) {
                try {
                    int angka = Integer.parseInt(id.substring(4));
                    if (angka > terbesar) {
                        terbesar = angka;
                    }
                } catch (NumberFormatException e) {
                    // ID dengan format lain diabaikan
                }
            }
        }
        return "BRG-" + (terbesar + 1);
    }

    // tanggal hari ini, format sama dengan CSV (contoh 11/8/2026)
    public static String tanggalHariIni() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("M/d/yyyy"));
    }

    // tanggal harus format M/d/yyyy dan benar-benar ada, contoh 11/15/2026
    public static boolean tanggalValid(String teks) {
        try {
            LocalDate.parse(teks, DateTimeFormatter.ofPattern("M/d/yyyy"));
            return true;
        } catch (java.time.format.DateTimeParseException e) {
            return false;
        }
    }

    // nomor HP Indonesia: diawali 08, total 10 sampai 13 digit
    public static boolean noHpValid(String teks) {
        return teks.matches("08\\d{8,11}");
    }

    // katalog aktif = barang yang belum SELESAI
    public static ArrayList<Item> hanyaAktif(ArrayList<Item> daftar) {
        ArrayList<Item> hasil = new ArrayList<>();
        for (Item item : daftar) {
            if (!item.getStatus().equals(Item.STATUS_SELESAI)) {
                hasil.add(item);
            }
        }
        return hasil;
    }
}