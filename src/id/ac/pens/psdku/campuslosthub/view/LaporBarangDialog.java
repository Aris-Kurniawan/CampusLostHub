package id.ac.pens.psdku.campuslosthub.view;

import id.ac.pens.psdku.campuslosthub.model.BarangDokumen;
import id.ac.pens.psdku.campuslosthub.model.BarangElektronik;
import id.ac.pens.psdku.campuslosthub.model.Item;
import id.ac.pens.psdku.campuslosthub.service.LostAndFoundManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Modal Dialog untuk input pelaporan barang temuan baru (Screen 6 Finexy).
 * Menampilkan bidang rahasia dinamis dalam kotak highlight coral sesuai kategori.
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
    private JLabel lblKhususHeader;

    public LaporBarangDialog(Frame owner, LostAndFoundManager manager) {
        super(owner, "Formulir Pelaporan Barang Temuan", true);
        this.manager = manager;

        setSize(560, 650);
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

        JLabel lblTitle = new JLabel("Formulir Pelaporan Barang Temuan");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.TEXT_MAIN);

        JLabel lblSub = new JLabel("ID unik (BRG-XXXX) diterbitkan otomatis dengan status awal TERSEDIA.");
        lblSub.setFont(UITheme.FONT_SMALL);
        lblSub.setForeground(UITheme.TEXT_MUTED);

        headerPanel.add(lblTitle);
        headerPanel.add(lblSub);

        // Form Container
        JPanel cardForm = UITheme.buatCardPanel();
        cardForm.setLayout(new BoxLayout(cardForm, BoxLayout.Y_AXIS));

        // Radio Pilihan Kategori
        JPanel pnlRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlRadio.setBackground(UITheme.SURFACE);
        pnlRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
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
                "Lab Pemrograman", "Lab Jaringan", "Kantin PSDKU",
                "Perpustakaan Lt. 2", "Gedung D4 Lt. 1", "Masjid Kampus",
                "Parkir Motor Barat", "Pos Keamanan Utama"
        });
        cbLokasi.setEditable(true);
        cbLokasi.setFont(UITheme.FONT_REGULAR);
        cbLokasi.setBackground(UITheme.SURFACE);
        cbLokasi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cbLokasi.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtTanggal = UITheme.buatTextField(20);
        txtTanggal.setText(ViewUtil.tanggalHariIni());

        // Kotak Callout Coral untuk Bidang Khusus Rahasia
        JPanel pnlKhusus = new UITheme.RoundedPanel(
                new GridLayout(0, 1, 4, 4), 12, UITheme.PRIMARY_LIGHT, UITheme.CORAL_BORDER);
        pnlKhusus.setBorder(new EmptyBorder(12, 14, 12, 14));
        pnlKhusus.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblKhususHeader = new JLabel("BIDANG KHUSUS ELEKTRONIK (Atribut Rahasia Verifikasi)");
        lblKhususHeader.setFont(UITheme.FONT_BADGE);
        lblKhususHeader.setForeground(UITheme.PRIMARY_DARK);

        txtDetail1 = UITheme.buatTextField(20);
        txtDetail2 = UITheme.buatTextField(20);
        lblDetail1 = new JLabel("Serial Number / IMEI (Rahasia) *");
        lblDetail1.setFont(UITheme.FONT_BOLD);
        lblDetail1.setForeground(UITheme.PRIMARY_DARK);
        lblDetail2 = new JLabel("Brand / Merk *");
        lblDetail2.setFont(UITheme.FONT_BOLD);
        lblDetail2.setForeground(UITheme.PRIMARY_DARK);

        pnlKhusus.add(lblKhususHeader);
        pnlKhusus.add(lblDetail1);
        pnlKhusus.add(txtDetail1);
        pnlKhusus.add(lblDetail2);
        pnlKhusus.add(txtDetail2);

        rbElektronik.addActionListener(e -> updateFieldDinamis());
        rbDokumen.addActionListener(e -> updateFieldDinamis());

        addRow(cardForm, createFieldLabel("Kategori Barang *"), 4);
        cardForm.add(pnlRadio);
        cardForm.add(Box.createVerticalStrut(8));
        addRow(cardForm, createFieldLabel("Nama Barang Temuan *"), 4);
        addField(cardForm, txtNama, 8);
        addRow(cardForm, createFieldLabel("Deskripsi Ciri Fisik Umum *"), 4);
        addField(cardForm, txtDeskripsi, 8);
        addRow(cardForm, createFieldLabel("Lokasi Ditemukan di Kampus *"), 4);
        cardForm.add(cbLokasi);
        cardForm.add(Box.createVerticalStrut(8));
        addRow(cardForm, createFieldLabel("Tanggal Ditemukan (M/d/yyyy) *"), 4);
        addField(cardForm, txtTanggal, 12);
        cardForm.add(pnlKhusus);

        // Tombol Aksi Bawah
        JPanel pnlTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlTombol.setBackground(UITheme.BG_MAIN);

        JButton btnBatal = UITheme.buatTombolOutline("Batal", UITheme.TEXT_MAIN);
        btnBatal.addActionListener(e -> dispose());

        JButton btnSimpan = UITheme.buatTombol("Simpan Laporan (Status: TERSEDIA)", UITheme.CHARCOAL, Color.WHITE);
        btnSimpan.addActionListener(e -> simpanLaporan());

        pnlTombol.add(btnBatal);
        pnlTombol.add(btnSimpan);

        JScrollPane scrollForm = new JScrollPane(cardForm);
        scrollForm.setBorder(null);
        scrollForm.setOpaque(false);
        scrollForm.getViewport().setBackground(UITheme.BG_MAIN);
        scrollForm.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollForm.getVerticalScrollBar().setUnitIncrement(16);

        contentPane.add(headerPanel, BorderLayout.NORTH);
        contentPane.add(scrollForm, BorderLayout.CENTER);
        contentPane.add(pnlTombol, BorderLayout.SOUTH);

        setContentPane(contentPane);
    }

    private void addRow(JPanel parent, JComponent comp, int gapAfter) {
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(comp);
        parent.add(Box.createVerticalStrut(gapAfter));
    }

    private void addField(JPanel parent, JTextField field, int gapAfter) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(field);
        parent.add(Box.createVerticalStrut(gapAfter));
    }

    private JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_BOLD);
        lbl.setForeground(UITheme.TEXT_MAIN);
        return lbl;
    }

    private void updateFieldDinamis() {
        boolean dokumen = rbDokumen.isSelected();
        lblKhususHeader.setText(dokumen
                ? "BIDANG KHUSUS DOKUMEN (Atribut Rahasia Verifikasi)"
                : "BIDANG KHUSUS ELEKTRONIK (Atribut Rahasia Verifikasi)");
        lblDetail1.setText(dokumen
                ? "Nomor Identitas Dokumen (NIM / NIK / Plat) *"
                : "Serial Number / IMEI (Rahasia) *");
        lblDetail2.setText(dokumen
                ? "Nama Pemilik pada Dokumen *"
                : "Brand / Merk *");
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
