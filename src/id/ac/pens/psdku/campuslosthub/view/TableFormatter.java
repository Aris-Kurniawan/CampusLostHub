package id.ac.pens.psdku.campuslosthub.view;

import id.ac.pens.psdku.campuslosthub.model.Item;

import java.util.ArrayList;

/**
 * Mencetak daftar barang dalam bentuk tabel di console.
 * Hanya data publik yang ditampilkan (bukti rahasia tidak ikut).
 */
public class TableFormatter {

    public static void cetak(ArrayList<Item> daftar) {
        if (daftar.isEmpty()) {
            System.out.println("Tidak ada barang yang ditemukan.");
            return;
        }

        System.out.printf("%-10s %-11s %-26s %-20s %-11s %-12s%n",
                "ID", "Kategori", "Nama", "Lokasi", "Tanggal", "Status");
        System.out.println("--------------------------------------------------------------------------------------------");
        for (Item item : daftar) {
            System.out.printf("%-10s %-11s %-26s %-20s %-11s %-12s%n",
                    item.getId(), item.getKategori(), item.getNama(),
                    item.getLokasiDitemukan(), item.getTanggalDitemukan(), item.getStatus());
        }
    }
}