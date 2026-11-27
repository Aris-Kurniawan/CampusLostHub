package id.ac.pens.psdku.campuslosthub.view;

import id.ac.pens.psdku.campuslosthub.model.BarangDokumen;
import id.ac.pens.psdku.campuslosthub.model.BarangElektronik;
import id.ac.pens.psdku.campuslosthub.model.Item;
import id.ac.pens.psdku.campuslosthub.service.LostAndFoundManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Modal Dialog untuk input pelaporan barang temuan baru.
 * Ditampilkan saat tombol [+ Buat Laporan Barang Temuan] diklik.
 */
public class LaporBarangDialog extends JDialog {

    private final LostAndFoundManager manager;
    private boolean sukses = false;

    private JRadioButton rbElektronik;
    private JRadioButton rbDokumen;
    private JTextField txtNama;
    private JTextField txtDeskripsi;
    private JComboBox<String> cbLokasi;
    private JTextField txtTanggal;
    private JTextField txtDetail1;
    private JTextField txtDetail2;
    private JLabel lblDetail1;
    private JLabel lblDetail2;

    public LaporBarangDialog(Frame owner, LostAndFoundManager manager) {
        super(owner, "Formulir Laporan Barang Temuan Kampus", true);
        this.manager = manager;

        setSize(540, 600);
        setLocationRelativeTo(owner);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel contentPane = new JPanel(new BorderLayout(14, 14));
        contentPane.setBackground(UITheme.BG_MAIN);
        contentPane.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Header Dialog
        JPanel headerPanel = new JPanel(new GridLayout(0, 1, 4, 4));
        headerPanel.setBackground(UITheme.BG_MAIN);

        JLabel lblTitle = new JLabel("Formulir Laporan Barang Temuan");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.TEXT_MAIN);

        JLabel lblSub = new JLabel("Catat barang temuan di area kampus PENS secara cepat dan ringkas.");
        lblSub.setFont(UITheme.FONT_SMALL);
        lblSub.setForeground(UITheme.TEXT_MUTED);

        headerPanel.add(lblTitle);
        headerPanel.add(lblSub);

        // Form Container
        JPanel cardForm = UITheme.buatCardPanel();
        cardForm.setLayout(new GridLayout(0, 1, 6, 6));

        // Radio Pilihan Kategori
        JPanel pnlRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlRadio.setBackground(UITheme.SURFACE);
        rbElektronik = new JRadioButton("Barang Elektronik", true);
        rbDokumen = new JRadioButton("Barang Dokumen", false);
        rbElektronik.setFont(UITheme.FONT_BOLD);
        rbDokumen.setFont(UITheme.FONT_BOLD);
        rbElektronik.setBackground(UITheme.SURFACE);
        rbDokumen.setBackground(UITheme.SURFACE);

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbElektronik);
        bg.add(rbDokumen);
        pnlRadio.add(rbElektronik);
        pnlRadio.add(rbDokumen);

        txtNama = UITheme.buatTextField(20);
        txtDeskripsi = UITheme.buatTextField(20);

        cbLokasi = new JComboBox<>(new String[]{
                "Lab Pemrograman", "Lab Jaringan", "Kantin Pusat",
                "Perpustakaan Lt. 2", "Gedung D4 Lt. 1", "Masjid Kampus",
                "Parkir Motor Barat", "Pos Keamanan Utama"
        });
        cbLokasi.setEditable(true);
        cbLokasi.setFont(UITheme.FONT_REGULAR);
        cbLokasi.setBackground(UITheme.SURFACE);

        txtTanggal = UITheme.buatTextField(20);
        txtTanggal.setText(ViewUtil.tanggalHariIni());

        txtDetail1 = UITheme.buatTextField(20);
        txtDetail2 = UITheme.buatTextField(20);
        lblDetail1 = new JLabel("Serial Number / IMEI (Bukti Rahasia) *");
        lblDetail1.setFont(UITheme.FONT_BOLD);
        lblDetail2 = new JLabel("Brand / Merk *");
        lblDetail2.setFont(UITheme.FONT_BOLD);

        rbElektronik.addActionListener(e -> updateFieldDinamis());
        rbDokumen.addActionListener(e -> updateFieldDinamis());

        cardForm.add(createFieldLabel("Kategori Barang *"));
        cardForm.add(pnlRadio);
        cardForm.add(createFieldLabel("Nama Barang Temuan *"));
        cardForm.add(txtNama);
        cardForm.add(createFieldLabel("Deskripsi Fisik & Ciri Karakteristik *"));
        cardForm.add(txtDeskripsi);
        cardForm.add(createFieldLabel("Lokasi Ditemukan di Kampus *"));
        cardForm.add(cbLokasi);
        cardForm.add(createFieldLabel("Tanggal Ditemukan (M/d/yyyy) *"));
        cardForm.add(txtTanggal);
        cardForm.add(lblDetail1);
        cardForm.add(txtDetail1);
        cardForm.add(lblDetail2);
        cardForm.add(txtDetail2);

        // Tombol Aksi Bawah
        JPanel pnlTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlTombol.setBackground(UITheme.BG_MAIN);

        JButton btnBatal = UITheme.buatTombolOutline("Batal", UITheme.TEXT_MAIN);
        btnBatal.addActionListener(e -> dispose());

        JButton btnSimpan = UITheme.buatTombol("Simpan Laporan Barang", UITheme.PRIMARY, Color.WHITE);
        btnSimpan.addActionListener(e -> simpanLaporan());

        pnlTombol.add(btnBatal);
        pnlTombol.add(btnSimpan);

        contentPane.add(headerPanel, BorderLayout.NORTH);
        contentPane.add(cardForm, BorderLayout.CENTER);
        contentPane.add(pnlTombol, BorderLayout.SOUTH);

        setContentPane(contentPane);
    }

    private JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_BOLD);
        lbl.setForeground(UITheme.TEXT_MAIN);
        return lbl;
    }

    private void updateFieldDinamis() {
        boolean dokumen = rbDokumen.isSelected();
        lblDetail1.setText(dokumen
                ? "Nomor Identitas Dokumen (NIM/NIK/Plat) *"
                : "Serial Number / IMEI (Bukti Rahasia) *");
        lblDetail2.setText(dokumen
                ? "Nama Pemilik pada Dokumen *"
                : "Brand / Merk & Model *");
    }

    private void simpanLaporan() {
        String nama = txtNama.getText().trim();
        String deskripsi = txtDeskripsi.getText().trim();
        Object objLokasi = cbLokasi.getSelectedItem();
        String lokasi = objLokasi == null ? "" : objLokasi.toString().trim();
        String tanggal = txtTanggal.getText().trim();
        String detail1 = txtDetail1.getText().trim();
        String detail2 = txtDetail2.getText().trim();
        boolean dokumen = rbDokumen.isSelected();

        if (nama.isEmpty() || deskripsi.isEmpty() || lokasi.isEmpty()
                || detail1.isEmpty() || detail2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field formulir wajib diisi.",
                    "Validasi Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!ViewUtil.tanggalValid(tanggal)) {
            JOptionPane.showMessageDialog(this,
                    "Format tanggal harus M/d/yyyy (contoh: 11/15/2026).",
                    "Validasi Tanggal", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idBaru = ViewUtil.buatIdBaru(manager.getAllItems());

        try {
            Item itemBaru;
            if (dokumen) {
                itemBaru = new BarangDokumen(idBaru, nama, deskripsi, lokasi, tanggal, detail1, detail2);
            } else {
                itemBaru = new BarangElektronik(idBaru, nama, deskripsi, lokasi, tanggal, detail1, detail2);
            }

            manager.addItem(itemBaru);
            sukses = true;
            JOptionPane.showMessageDialog(this,
                    "Laporan barang temuan berhasil disimpan!\nID Barang: " + idBaru + "\nStatus: TERSEDIA",
                    "Laporan Berhasil Disimpan", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan laporan: " + ex.getMessage(),
                    "Error Penyimpanan", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSukses() {
        return sukses;
    }
}
