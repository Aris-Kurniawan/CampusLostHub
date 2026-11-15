package id.ac.pens.psdku.campuslosthub.view;

import java.util.Scanner;

/**
 * Class bantu untuk membaca dan memvalidasi input dari user di console.
 */
public class InputValidator {

    private Scanner scanner;

    public InputValidator(Scanner scanner) {
        this.scanner = scanner;
    }

    // baca teks, tidak boleh kosong
    public String bacaTeks(String label) {
        while (true) {
            System.out.print(label + ": ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("  [!] " + label + " tidak boleh kosong.");
            } else {
                return input;
            }
        }
    }

    // baca angka pilihan menu dalam rentang min sampai max
    public int bacaPilihan(String label, int min, int max) {
        while (true) {
            System.out.print(label + ": ");
            String input = scanner.nextLine().trim();

            try {
                int angka = Integer.parseInt(input);
                if (angka >= min && angka <= max) {
                    return angka;
                }
                System.out.println("  [!] Pilihan harus antara " + min + " sampai " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Input harus berupa angka.");
            }
        }
    }

    // NIM mahasiswa harus 10 digit angka, contoh 3125521024
    public String bacaNim(String label) {
        while (true) {
            System.out.print(label + " (10 digit): ");
            String input = scanner.nextLine().trim();

            if (input.matches("\\d{10}")) {
                return input;
            }
            System.out.println("  [!] NIM harus 10 digit angka.");
        }
    }

    // konfirmasi ya/tidak
    public boolean bacaYaTidak(String label) {
        while (true) {
            System.out.print(label + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            }
            System.out.println("  [!] Ketik y atau n saja.");
        }
    }

    // tanggal format M/d/yyyy, kalau dikosongkan dipakai tanggal hari ini
    public String bacaTanggal(String label) {
        while (true) {
            System.out.print(label + " (M/d/yyyy, Enter = hari ini): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return ViewUtil.tanggalHariIni();
            }
            if (ViewUtil.tanggalValid(input)) {
                return input;
            }
            System.out.println("  [!] Format tanggal salah, contoh: 11/15/2026.");
        }
    }

    // nomor HP diawali 08, 10 sampai 13 digit
    public String bacaNoHp(String label) {
        while (true) {
            System.out.print(label + " (contoh 081234567890): ");
            String input = scanner.nextLine().trim();

            if (ViewUtil.noHpValid(input)) {
                return input;
            }
            System.out.println("  [!] Nomor HP harus diawali 08 dan 10-13 digit angka.");
        }
    }
}