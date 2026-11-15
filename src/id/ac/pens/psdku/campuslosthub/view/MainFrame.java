package id.ac.pens.psdku.campuslosthub.view;

import id.ac.pens.psdku.campuslosthub.exception.InvalidClaimException;
import id.ac.pens.psdku.campuslosthub.model.BarangDokumen;
import id.ac.pens.psdku.campuslosthub.model.BarangElektronik;
import id.ac.pens.psdku.campuslosthub.model.Claimant;
import id.ac.pens.psdku.campuslosthub.model.Item;
import id.ac.pens.psdku.campuslosthub.service.LostAndFoundManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Tampilan GUI (Swing) CampusLostHub.
 * Hanya mengurus tampilan dan validasi input, logika bisnis ada di LostAndFoundManager.
 */
public class MainFrame extends JFrame {

    private LostAndFoundManager manager;

    // komponen tab Lapor
    private JComboBox<String> cbJenis;
    private JTextField txtNama, txtDeskripsi, txtLokasi, txtTanggal, txtDetail1, txtDetail2;
    private JLabel lblDetail1, lblDetail2;

    // komponen tab Daftar
    private DefaultTableModel tableModel;
    private JTextField txtCariNama, txtCariLokasi;

    // komponen tab Klaim
    private JTextField txtIdKlaim, txtNamaKlaim, txtNimKlaim, txtNoHpKlaim, txtBukti;

    // komponen tab Serah Terima
    private JTextField txtIdSerah;

    public MainFrame(LostAndFoundManager manager) {
        this.manager = manager;

        setTitle("CampusLostHub - Lost & Found PENS");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Lapor Barang", buatPanelLapor());
        tabs.addTab("Daftar & Cari", buatPanelDaftar());
        tabs.addTab("Klaim Barang", buatPanelKlaim());
        tabs.addTab("Serah Terima / Batal", buatPanelSerahTerima());
        add(tabs);

        tampilkanTabel(ViewUtil.hanyaAktif(manager.getAllItems()));
    }

    // ---------- Tab 1: Lapor barang ----------
    private JPanel buatPanelLapor() {
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        cbJenis = new JComboBox<>(new String[]{"Barang Elektronik", "Barang Dokumen"});
        txtNama = new JTextField();
        txtDeskripsi = new JTextField();
        txtLokasi = new JTextField();
        txtTanggal = new JTextField(ViewUtil.tanggalHariIni());
        txtDetail1 = new JTextField();
        txtDetail2 = new JTextField();
        lblDetail1 = new JLabel("Serial Number / IMEI");
        lblDetail2 = new JLabel("Brand");

        form.add(new JLabel("Jenis Barang"));
        form.add(cbJenis);
        form.add(new JLabel("Nama Barang"));
        form.add(txtNama);
        form.add(new JLabel("Deskripsi"));
        form.add(txtDeskripsi);
        form.add(new JLabel("Lokasi Ditemukan"));
        form.add(txtLokasi);
        form.add(new JLabel("Tanggal Ditemukan (M/d/yyyy)"));
        form.add(txtTanggal);
        form.add(lblDetail1);
        form.add(txtDetail1);
        form.add(lblDetail2);
        form.add(txtDetail2);

        // label dua kolom terakhir ikut jenis barang
        cbJenis.addActionListener(e -> {
            boolean dokumen = cbJenis.getSelectedIndex() == 1;
            lblDetail1.setText(dokumen ? "Nomor Identitas Dokumen" : "Serial Number / IMEI");
            lblDetail2.setText(dokumen ? "Nama Pemilik di Dokumen" : "Brand");
        });

        JButton btnSimpan = new JButton("Simpan Laporan");
        btnSimpan.addActionListener(e -> laporBarang());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(form, BorderLayout.NORTH);
        JPanel bawah = new JPanel();
        bawah.add(btnSimpan);
        panel.add(bawah, BorderLayout.CENTER);
        return panel;
    }

    private void laporBarang() {
        String nama = txtNama.getText().trim();
        String deskripsi = txtDeskripsi.getText().trim();
        String lokasi = txtLokasi.getText().trim();
        String tanggal = txtTanggal.getText().trim();
        String detail1 = txtDetail1.getText().trim();
        String detail2 = txtDetail2.getText().trim();
        boolean dokumen = cbJenis.getSelectedIndex() == 1;

        // validasi input
        if (!cekTeks(nama, "Nama barang") || !cekTeks(deskripsi, "Deskripsi")
                || !cekTeks(lokasi, "Lokasi") || !cekTeks(detail1, lblDetail1.getText())
                || !cekTeks(detail2, lblDetail2.getText())) {
            return;
        }
        if (!ViewUtil.tanggalValid(tanggal)) {
            JOptionPane.showMessageDialog(this, "Format tanggal salah, contoh: 11/15/2026.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = ViewUtil.buatIdBaru(manager.getAllItems());

        try {
            if (dokumen) {
                manager.addItem(new BarangDokumen(id, nama, deskripsi, lokasi, tanggal,
                        detail1, detail2));
            } else {
                manager.addItem(new BarangElektronik(id, nama, deskripsi, lokasi, tanggal,
                        detail1, detail2));
            }
            JOptionPane.showMessageDialog(this, "Barang berhasil dilaporkan dengan ID " + id);
            kosongkanFormLapor();
            tampilkanTabel(ViewUtil.hanyaAktif(manager.getAllItems()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void kosongkanFormLapor() {
        txtNama.setText("");
        txtDeskripsi.setText("");
        txtLokasi.setText("");
        txtTanggal.setText(ViewUtil.tanggalHariIni());
        txtDetail1.setText("");
        txtDetail2.setText("");
    }

    // ---------- Tab 2: Daftar & cari ----------
    private JPanel buatPanelDaftar() {
        JPanel atas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtCariNama = new JTextField(10);
        txtCariLokasi = new JTextField(10);
        JButton btnCariNama = new JButton("Cari Nama");
        JButton btnFilter = new JButton("Filter Lokasi");
        JButton btnSemua = new JButton("Katalog Aktif");

        atas.add(new JLabel("Nama:"));
        atas.add(txtCariNama);
        atas.add(btnCariNama);
        atas.add(new JLabel("Lokasi:"));
        atas.add(txtCariLokasi);
        atas.add(btnFilter);
        atas.add(btnSemua);

        btnCariNama.addActionListener(e ->
                tampilkanTabel(manager.searchByName(txtCariNama.getText().trim())));
        btnFilter.addActionListener(e ->
                tampilkanTabel(manager.filterByLocation(txtCariLokasi.getText().trim())));
        btnSemua.addActionListener(e -> tampilkanTabel(ViewUtil.hanyaAktif(manager.getAllItems())));

        String[] kolom = {"ID", "Kategori", "Nama", "Lokasi", "Tanggal", "Status"};
        tableModel = new DefaultTableModel(kolom, 0);
        JTable tabel = new JTable(tableModel);
        tabel.setEnabled(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(atas, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabel), BorderLayout.CENTER);
        return panel;
    }

    private void tampilkanTabel(ArrayList<Item> daftar) {
        tableModel.setRowCount(0);
        for (Item item : daftar) {
            tableModel.addRow(new Object[]{
                    item.getId(), item.getKategori(), item.getNama(),
                    item.getLokasiDitemukan(), item.getTanggalDitemukan(), item.getStatus()});
        }
    }

    // ---------- Tab 3: Klaim ----------
    private JPanel buatPanelKlaim() {
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtIdKlaim = new JTextField();
        txtNamaKlaim = new JTextField();
        txtNimKlaim = new JTextField();
        txtNoHpKlaim = new JTextField();
        txtBukti = new JTextField();

        form.add(new JLabel("ID Barang (contoh: BRG-1001)"));
        form.add(txtIdKlaim);
        form.add(new JLabel("Nama Pengklaim"));
        form.add(txtNamaKlaim);
        form.add(new JLabel("NIM Pengklaim (10 digit)"));
        form.add(txtNimKlaim);
        form.add(new JLabel("No. HP Pengklaim (08...)"));
        form.add(txtNoHpKlaim);
        form.add(new JLabel("Bukti (Serial/IMEI atau No. Identitas/Nama Pemilik)"));
        form.add(txtBukti);

        JButton btnKlaim = new JButton("Ajukan Klaim");
        btnKlaim.addActionListener(e -> ajukanKlaim());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(form, BorderLayout.NORTH);
        JPanel bawah = new JPanel();
        bawah.add(btnKlaim);
        panel.add(bawah, BorderLayout.CENTER);
        return panel;
    }

    private void ajukanKlaim() {
        String id = txtIdKlaim.getText().trim();
        String nama = txtNamaKlaim.getText().trim();
        String nim = txtNimKlaim.getText().trim();
        String noHp = txtNoHpKlaim.getText().trim();
        String bukti = txtBukti.getText().trim();

        if (!cekTeks(id, "ID barang") || !cekTeks(nama, "Nama pengklaim")
                || !cekNim(nim) || !cekTeks(bukti, "Bukti")) {
            return;
        }
        if (!ViewUtil.noHpValid(noHp)) {
            JOptionPane.showMessageDialog(this, "Nomor HP harus diawali 08 dan 10-13 digit angka.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Item item = manager.findById(id);
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Barang dengan ID " + id + " tidak ditemukan.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!item.getStatus().equals(Item.STATUS_TERSEDIA)) {
            JOptionPane.showMessageDialog(this,
                    "Barang tidak bisa diklaim karena statusnya " + item.getStatus() + ".",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            manager.claimItem(id, new Claimant(nama, nim, noHp), bukti);
            JOptionPane.showMessageDialog(this,
                    "Klaim valid! Status barang menjadi PROSES_KLAIM.\n"
                            + "Silakan bawa kartu identitas fisik ke pos kampus.");
            tampilkanTabel(ViewUtil.hanyaAktif(manager.getAllItems()));
        } catch (InvalidClaimException e) {
            JOptionPane.showMessageDialog(this, "Klaim ditolak: " + e.getMessage(),
                    "Klaim Gagal", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------- Tab 4: Serah terima ----------
    private JPanel buatPanelSerahTerima() {
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtIdSerah = new JTextField();
        form.add(new JLabel("ID Barang"));
        form.add(txtIdSerah);

        JButton btnSelesai = new JButton("Selesaikan Serah Terima");
        btnSelesai.addActionListener(e -> serahTerima());
        JButton btnBatal = new JButton("Batalkan Klaim");
        btnBatal.addActionListener(e -> batalKlaim());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(form, BorderLayout.NORTH);
        JPanel bawah = new JPanel();
        bawah.add(btnSelesai);
        bawah.add(btnBatal);
        panel.add(bawah, BorderLayout.CENTER);
        return panel;
    }

    private void serahTerima() {
        String id = txtIdSerah.getText().trim();
        if (!cekTeks(id, "ID barang")) {
            return;
        }

        Item item = manager.findById(id);
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Barang tidak ditemukan.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!item.getStatus().equals(Item.STATUS_PROSES_KLAIM)) {
            JOptionPane.showMessageDialog(this,
                    "Serah terima hanya untuk barang berstatus PROSES_KLAIM.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int jawab = JOptionPane.showConfirmDialog(this,
                "Apakah kartu identitas fisik sudah dicek?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (jawab != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            manager.completeHandover(id);
            JOptionPane.showMessageDialog(this, "Serah terima selesai. Status: SELESAI.");
            txtIdSerah.setText("");
            tampilkanTabel(ViewUtil.hanyaAktif(manager.getAllItems()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void batalKlaim() {
        String id = txtIdSerah.getText().trim();
        if (!cekTeks(id, "ID barang")) {
            return;
        }

        Item item = manager.findById(id);
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Barang tidak ditemukan.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!item.getStatus().equals(Item.STATUS_PROSES_KLAIM)) {
            JOptionPane.showMessageDialog(this,
                    "Hanya klaim berstatus PROSES_KLAIM yang bisa dibatalkan.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int jawab = JOptionPane.showConfirmDialog(this,
                "Yakin batalkan klaim barang ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (jawab != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            manager.cancelClaim(id);
            JOptionPane.showMessageDialog(this, "Klaim dibatalkan. Status kembali TERSEDIA.");
            txtIdSerah.setText("");
            tampilkanTabel(ViewUtil.hanyaAktif(manager.getAllItems()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------- validasi ----------
    private boolean cekTeks(String teks, String namaField) {
        if (teks.isEmpty()) {
            JOptionPane.showMessageDialog(this, namaField + " tidak boleh kosong.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    // NIM mahasiswa harus 10 digit angka
    private boolean cekNim(String nim) {
        if (!nim.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "NIM harus 10 digit angka.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}