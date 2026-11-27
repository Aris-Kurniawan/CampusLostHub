package id.ac.pens.psdku.campuslosthub.view;

import id.ac.pens.psdku.campuslosthub.model.BarangDokumen;
import id.ac.pens.psdku.campuslosthub.model.BarangElektronik;
import id.ac.pens.psdku.campuslosthub.model.Item;
import id.ac.pens.psdku.campuslosthub.service.LostAndFoundManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Antarmuka Utama Swing CampusLostHub.
 * Memisahkan secara tegas:
 * - Navbar & Layar Pengguna / Mahasiswa (Jelajah & Klaim, Laporkan Temuan, Riwayat Aktivitas)
 * - Navbar & Layar Petugas / Administrator (Dashboard Kendali, Verifikasi Klaim & Pos, Kelola Data Barang)
 */
public class MainFrame extends JFrame {

    private final LostAndFoundManager manager;
    private boolean isAdminLoggedIn = false;

    // Card Layout Layar Utama
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // Komponen Header
    private JLabel lblTag;
    private JLabel lblUserRole;
    private JButton btnAuthToggle;

    // Container Navbar Dinamis (User vs Admin)
    private JPanel pnlMenuContainer;
    private CardLayout cardMenuLayout;
    private JPanel pnlMenuUser;
    private JPanel pnlMenuAdmin;

    // Tombol Navigasi User
    private JButton btnNavJelajah;
    private JButton btnNavLapor;
    private JButton btnNavRiwayat;

    // Tombol Navigasi Admin
    private JButton btnNavAdminDashboard;
    private JButton btnNavAdminVerifikasi;
    private JButton btnNavAdminKelola;

    // Komponen Layar 1: Jelajah & Klaim
    private DefaultTableModel modelTabelJelajah;
    private JTable tabelJelajah;
    private JTextField txtCariNama;
    private JComboBox<String> cbFilterLokasi;
    private String filterStatusJelajah = "SEMUA";
    private JButton btnKlaimTerpilih;

    // Komponen Layar 2: Laporkan Temuan (Model Tabel seperti Jelajah)
    private DefaultTableModel modelTabelLapor;
    private JTable tabelLapor;
    private JTextField txtCariLapor;
    private JComboBox<String> cbFilterLokasiLapor;

    // Komponen Layar 3: Riwayat
    private DefaultTableModel modelTabelRiwayat;
    private JTable tabelRiwayat;
    private JLabel lblStatTotalMahasiswa;
    private JLabel lblStatMenungguPos;
    private JLabel lblStatSelesaiMahasiswa;

    // Komponen Admin: Dashboard
    private JLabel lblStatTotal;
    private JLabel lblStatTersedia;
    private JLabel lblStatProsesKlaim;
    private JLabel lblStatSelesai;
    private DefaultTableModel modelTabelAdminKlaim;
    private JTable tabelAdminKlaim;

    // Komponen Admin: Verifikasi Pos
    private JList<String> listAntreanKlaim;
    private DefaultListModel<String> modelListAntrean;
    private JLabel lblDetailKlaimId;
    private JLabel lblDetailKlaimNama;
    private JLabel lblDetailKlaimKategori;
    private JLabel lblDetailKlaimLokasi;
    private JLabel lblDetailKlaimTanggal;
    private JCheckBox chkKtmDicek;
    private JCheckBox chkBuktiCocok;
    private JCheckBox chkBastDisetujui;
    private JButton btnPosSerahTerima;
    private JButton btnPosTolak;
    private Item itemTerpilihPos = null;

    // Komponen Admin: Kelola Data Barang
    private DefaultTableModel modelTabelAdminKelola;
    private JTable tabelAdminKelola;
    private JTextField txtAdminCariKelola;
    private JComboBox<String> cbAdminFilterStatus;

    public MainFrame(LostAndFoundManager manager) {
        this.manager = manager;

        setTitle("CampusLostHub — Smart Campus Lost & Found System (PENS)");
        setSize(1100, 720);
        setMinimumSize(new Dimension(950, 620));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        refreshSemuaData();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_MAIN);

        // 1. Header & Navigasi Dinamis
        root.add(buatHeaderNavigasi(), BorderLayout.NORTH);

        // 2. Card Layout untuk Seluruh Layar
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(UITheme.BG_MAIN);

        // Daftarkan Layar Pengguna
        mainContentPanel.add(buatLayarJelajah(), "JELAJAH");
        mainContentPanel.add(buatLayarLapor(), "LAPOR");
        mainContentPanel.add(buatLayarRiwayat(), "RIWAYAT");

        // Daftarkan Layar Petugas (Langsung, tanpa subtab berlapis)
        mainContentPanel.add(buatLayarAdminDashboard(), "ADMIN_DASHBOARD");
        mainContentPanel.add(buatLayarAdminVerifikasiPos(), "ADMIN_VERIFIKASI");
        mainContentPanel.add(buatLayarAdminKelolaBarang(), "ADMIN_KELOLA");

        root.add(mainContentPanel, BorderLayout.CENTER);
        setContentPane(root);

        // Default tampilkan halaman Jelajah untuk Pengguna
        gantiLayarUtama("JELAJAH");
    }

    // =========================================================================
    // 1. HEADER & NAVBAR DINAMIS (USER VS ADMIN)
    // =========================================================================
    private JPanel buatHeaderNavigasi() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.SURFACE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));
        header.setPreferredSize(new Dimension(getWidth(), 68));

        // Kiri: Logo & Tag Identitas
        JPanel pnlLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 14));
        pnlLogo.setBackground(UITheme.SURFACE);

        JLabel lblLogo = new JLabel("CampusLostHub");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogo.setForeground(UITheme.PRIMARY);

        lblTag = new JLabel(" PENS ");
        lblTag.setFont(UITheme.FONT_BADGE);
        lblTag.setBackground(UITheme.PRIMARY_LIGHT);
        lblTag.setForeground(UITheme.PRIMARY);
        lblTag.setOpaque(true);
        lblTag.setBorder(new EmptyBorder(3, 8, 3, 8));

        JLabel lblSub = new JLabel("• Smart Campus Lost & Found");
        lblSub.setFont(UITheme.FONT_SMALL);
        lblSub.setForeground(UITheme.TEXT_MUTED);

        pnlLogo.add(lblLogo);
        pnlLogo.add(lblTag);
        pnlLogo.add(lblSub);

        // Tengah: Menu Navigasi Berbasis Peran (CardLayout)
        cardMenuLayout = new CardLayout();
        pnlMenuContainer = new JPanel(cardMenuLayout);
        pnlMenuContainer.setBackground(UITheme.SURFACE);

        // A. Menu User (Mahasiswa)
        pnlMenuUser = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 14));
        pnlMenuUser.setBackground(UITheme.SURFACE);

        btnNavJelajah = buatNavButton("Jelajah & Klaim");
        btnNavLapor = buatNavButton("Laporkan Temuan");
        btnNavRiwayat = buatNavButton("Riwayat Aktivitas");

        btnNavJelajah.addActionListener(e -> gantiLayarUtama("JELAJAH"));
        btnNavLapor.addActionListener(e -> gantiLayarUtama("LAPOR"));
        btnNavRiwayat.addActionListener(e -> gantiLayarUtama("RIWAYAT"));

        pnlMenuUser.add(btnNavJelajah);
        pnlMenuUser.add(btnNavLapor);
        pnlMenuUser.add(btnNavRiwayat);

        // B. Menu Khusus Petugas / Administrator
        pnlMenuAdmin = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 14));
        pnlMenuAdmin.setBackground(UITheme.SURFACE);

        btnNavAdminDashboard = buatNavButton("Dashboard Kendali");
        btnNavAdminVerifikasi = buatNavButton("Verifikasi Klaim & Pos");
        btnNavAdminKelola = buatNavButton("Kelola Data Barang");

        btnNavAdminDashboard.addActionListener(e -> gantiLayarUtama("ADMIN_DASHBOARD"));
        btnNavAdminVerifikasi.addActionListener(e -> gantiLayarUtama("ADMIN_VERIFIKASI"));
        btnNavAdminKelola.addActionListener(e -> gantiLayarUtama("ADMIN_KELOLA"));

        pnlMenuAdmin.add(btnNavAdminDashboard);
        pnlMenuAdmin.add(btnNavAdminVerifikasi);
        pnlMenuAdmin.add(btnNavAdminKelola);

        pnlMenuContainer.add(pnlMenuUser, "USER_NAV");
        pnlMenuContainer.add(pnlMenuAdmin, "ADMIN_NAV");

        // Kanan: Indikator Status & Tombol Login/Logout
        JPanel pnlKanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 16));
        pnlKanan.setBackground(UITheme.SURFACE);

        lblUserRole = new JLabel("● Tamu (Mahasiswa)");
        lblUserRole.setFont(UITheme.FONT_SMALL);
        lblUserRole.setForeground(UITheme.TEXT_MUTED);

        btnAuthToggle = UITheme.buatTombolOutline("Login Admin", UITheme.PRIMARY);
        btnAuthToggle.setFont(UITheme.FONT_SMALL);
        btnAuthToggle.addActionListener(e -> toggleAdminAuth());

        pnlKanan.add(lblUserRole);
        pnlKanan.add(btnAuthToggle);

        header.add(pnlLogo, BorderLayout.WEST);
        header.add(pnlMenuContainer, BorderLayout.CENTER);
        header.add(pnlKanan, BorderLayout.EAST);
        return header;
    }

    private JButton buatNavButton(String teks) {
        JButton btn = new JButton(teks);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(UITheme.FONT_BOLD);
        btn.setBackground(UITheme.SURFACE);
        btn.setForeground(UITheme.TEXT_MUTED);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void updateNavButtonHighlight(JButton btn, boolean aktif) {
        if (aktif) {
            btn.setBackground(UITheme.PRIMARY_LIGHT);
            btn.setForeground(UITheme.PRIMARY);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(191, 219, 254), 1),
                    new EmptyBorder(7, 15, 7, 15)
            ));
        } else {
            btn.setBackground(UITheme.SURFACE);
            btn.setForeground(UITheme.TEXT_MUTED);
            btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        }
    }

    private void gantiLayarUtama(String namaLayar) {
        cardLayout.show(mainContentPanel, namaLayar);

        // Update highlight tombol nav user
        updateNavButtonHighlight(btnNavJelajah, "JELAJAH".equals(namaLayar));
        updateNavButtonHighlight(btnNavLapor, "LAPOR".equals(namaLayar));
        updateNavButtonHighlight(btnNavRiwayat, "RIWAYAT".equals(namaLayar));

        // Update highlight tombol nav admin
        updateNavButtonHighlight(btnNavAdminDashboard, "ADMIN_DASHBOARD".equals(namaLayar));
        updateNavButtonHighlight(btnNavAdminVerifikasi, "ADMIN_VERIFIKASI".equals(namaLayar));
        updateNavButtonHighlight(btnNavAdminKelola, "ADMIN_KELOLA".equals(namaLayar));

        refreshSemuaData();
    }

    private void toggleAdminAuth() {
        if (!isAdminLoggedIn) {
            AdminLoginDialog dialog = new AdminLoginDialog(this);
            dialog.setVisible(true);
            if (dialog.isAuthenticated()) {
                isAdminLoggedIn = true;

                // Beralih ke Navbar Petugas
                cardMenuLayout.show(pnlMenuContainer, "ADMIN_NAV");
                lblTag.setText(" ADMIN ");
                lblTag.setBackground(new Color(254, 226, 226));
                lblTag.setForeground(UITheme.DANGER_TEXT);

                lblUserRole.setText("● Petugas Aktif (Admin)");
                lblUserRole.setForeground(UITheme.SUCCESS_TEXT);

                btnAuthToggle.setText("Logout Admin");
                btnAuthToggle.setForeground(UITheme.DANGER_TEXT);

                gantiLayarUtama("ADMIN_DASHBOARD");
            }
        } else {
            int jawab = JOptionPane.showConfirmDialog(this,
                    "Keluar dari sesi administrator dan kembali ke portal mahasiswa?",
                    "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
            if (jawab == JOptionPane.YES_OPTION) {
                isAdminLoggedIn = false;

                // Beralih kembali ke Navbar User
                cardMenuLayout.show(pnlMenuContainer, "USER_NAV");
                lblTag.setText(" PENS ");
                lblTag.setBackground(UITheme.PRIMARY_LIGHT);
                lblTag.setForeground(UITheme.PRIMARY);

                lblUserRole.setText("● Tamu (Mahasiswa)");
                lblUserRole.setForeground(UITheme.TEXT_MUTED);

                btnAuthToggle.setText("Login Admin");
                btnAuthToggle.setForeground(UITheme.PRIMARY);

                gantiLayarUtama("JELAJAH");
            }
        }
    }

    // =========================================================================
    // 2. LAYAR USER 1: JELAJAH & KLAIM BARANG (KATALOG TERPADU)
    // =========================================================================
    private JPanel buatLayarJelajah() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(UITheme.BG_MAIN);
        panel.setBorder(new EmptyBorder(18, 20, 18, 20));

        // Baris Atas: Form Pencarian & Filter Lokasi
        JPanel pnlFilter = UITheme.buatCardPanel();
        pnlFilter.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 4));

        JLabel lblCari = new JLabel("Cari Barang:");
        lblCari.setFont(UITheme.FONT_BOLD);
        txtCariNama = UITheme.buatTextField(16);

        JLabel lblLokasi = new JLabel("Lokasi Kampus:");
        lblLokasi.setFont(UITheme.FONT_BOLD);
        cbFilterLokasi = new JComboBox<>(new String[]{"Semua Lokasi Kampus"});
        cbFilterLokasi.setFont(UITheme.FONT_REGULAR);
        cbFilterLokasi.setBackground(UITheme.SURFACE);

        JButton btnTerapkanFilter = UITheme.buatTombol("Cari / Filter", UITheme.PRIMARY, Color.WHITE);
        btnTerapkanFilter.addActionListener(e -> terapkanFilterJelajah());

        JButton btnResetFilter = UITheme.buatTombolOutline("Reset", UITheme.TEXT_MAIN);
        btnResetFilter.addActionListener(e -> {
            txtCariNama.setText("");
            cbFilterLokasi.setSelectedIndex(0);
            filterStatusJelajah = "SEMUA";
            terapkanFilterJelajah();
        });

        pnlFilter.add(lblCari);
        pnlFilter.add(txtCariNama);
        pnlFilter.add(lblLokasi);
        pnlFilter.add(cbFilterLokasi);
        pnlFilter.add(btnTerapkanFilter);
        pnlFilter.add(btnResetFilter);

        // Tabel Katalog Barang
        String[] kolom = {"ID", "Kategori", "Nama Barang", "Deskripsi", "Lokasi Temuan", "Tanggal", "Status"};
        modelTabelJelajah = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelJelajah = new JTable(modelTabelJelajah);
        UITheme.formatTable(tabelJelajah);

        // Lebar kolom tabel proporsional
        tabelJelajah.getColumnModel().getColumn(0).setPreferredWidth(85);
        tabelJelajah.getColumnModel().getColumn(1).setPreferredWidth(105);
        tabelJelajah.getColumnModel().getColumn(2).setPreferredWidth(160);
        tabelJelajah.getColumnModel().getColumn(3).setPreferredWidth(230);
        tabelJelajah.getColumnModel().getColumn(4).setPreferredWidth(130);
        tabelJelajah.getColumnModel().getColumn(5).setPreferredWidth(90);
        tabelJelajah.getColumnModel().getColumn(6).setPreferredWidth(115);

        // Selection listener untuk tombol klaim
        tabelJelajah.getSelectionModel().addListSelectionListener(e -> {
            int row = tabelJelajah.getSelectedRow();
            btnKlaimTerpilih.setEnabled(row >= 0);
        });

        JScrollPane scroll = new JScrollPane(tabelJelajah);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        scroll.getViewport().setBackground(UITheme.SURFACE);

        // Baris Bawah: Tombol Aksi Klaim Terpilih
        JPanel pnlBawah = new JPanel(new BorderLayout());
        pnlBawah.setBackground(UITheme.BG_MAIN);

        JLabel lblPetunjuk = new JLabel("Petunjuk: Klik pada salah satu baris barang di tabel, lalu klik 'Ajukan Klaim'.");
        lblPetunjuk.setFont(UITheme.FONT_SMALL);
        lblPetunjuk.setForeground(UITheme.TEXT_MUTED);

        btnKlaimTerpilih = UITheme.buatTombol("Ajukan Klaim Kepemilikan Barang Ini", UITheme.PRIMARY, Color.WHITE);
        btnKlaimTerpilih.setEnabled(false);
        btnKlaimTerpilih.addActionListener(e -> bukaDialogKlaim());

        pnlBawah.add(lblPetunjuk, BorderLayout.WEST);
        pnlBawah.add(btnKlaimTerpilih, BorderLayout.EAST);

        panel.add(pnlFilter, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(pnlBawah, BorderLayout.SOUTH);
        return panel;
    }

    private void terapkanFilterJelajah() {
        String keyword = txtCariNama.getText().trim().toLowerCase();
        String lokasi = (String) cbFilterLokasi.getSelectedItem();
        boolean filterLokasiAktif = lokasi != null && !lokasi.equals("Semua Lokasi Kampus");

        ArrayList<Item> semua = manager.getAllItems();
        ArrayList<Item> hasil = new ArrayList<>();

        for (Item item : semua) {
            boolean matchNama = keyword.isEmpty()
                    || item.getNama().toLowerCase().contains(keyword)
                    || item.getDeskripsi().toLowerCase().contains(keyword)
                    || item.getId().toLowerCase().contains(keyword);

            boolean matchLokasi = !filterLokasiAktif
                    || item.getLokasiDitemukan().equalsIgnoreCase(lokasi);

            boolean matchStatus = "SEMUA".equals(filterStatusJelajah)
                    || item.getStatus().equalsIgnoreCase(filterStatusJelajah);

            if (matchNama && matchLokasi && matchStatus) {
                hasil.add(item);
            }
        }

        isiTabelJelajah(hasil);
    }

    private void isiTabelJelajah(ArrayList<Item> items) {
        modelTabelJelajah.setRowCount(0);
        for (Item item : items) {
            modelTabelJelajah.addRow(new Object[]{
                    item.getId(),
                    item.getKategori(),
                    item.getNama(),
                    item.getDeskripsi(),
                    item.getLokasiDitemukan(),
                    item.getTanggalDitemukan(),
                    item.getStatus()
            });
        }
    }

    private void bukaDialogKlaim() {
        int selectedRow = tabelJelajah.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Silakan pilih salah satu baris barang di tabel terlebih dahulu.",
                    "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String id = (String) modelTabelJelajah.getValueAt(selectedRow, 0);
        Item item = manager.findById(id);

        if (item == null) {
            JOptionPane.showMessageDialog(this, "Barang tidak ditemukan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!Item.STATUS_TERSEDIA.equals(item.getStatus())) {
            JOptionPane.showMessageDialog(this,
                    "Barang " + item.getId() + " sedang berstatus: " + item.getStatus()
                            + ".\nHanya barang berstatus TERSEDIA yang dapat diajukan klaim.",
                    "Tidak Dapat Diklaim", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ClaimDialog dialog = new ClaimDialog(this, manager, item);
        dialog.setVisible(true);

        if (dialog.isSukses()) {
            refreshSemuaData();
        }
    }

    // =========================================================================
    // 3. LAYAR USER 2: LAPORKAN BARANG TEMUAN (TABEL & DIALOG TERPADU)
    // =========================================================================
    private JPanel buatLayarLapor() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(UITheme.BG_MAIN);
        panel.setBorder(new EmptyBorder(18, 20, 18, 20));

        // Toolbar Atas: Pencarian, Filter Lokasi, & Tombol Buat Laporan Baru
        JPanel pnlToolbar = UITheme.buatCardPanel();
        pnlToolbar.setLayout(new BorderLayout(10, 0));

        // Kiri: Filter & Pencarian
        JPanel pnlKiri = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlKiri.setBackground(UITheme.SURFACE);

        JLabel lblCari = new JLabel("Cari Laporan:");
        lblCari.setFont(UITheme.FONT_BOLD);
        txtCariLapor = UITheme.buatTextField(16);

        JLabel lblLokasi = new JLabel("Lokasi:");
        lblLokasi.setFont(UITheme.FONT_BOLD);
        cbFilterLokasiLapor = new JComboBox<>(new String[]{"Semua Lokasi Kampus"});
        cbFilterLokasiLapor.setFont(UITheme.FONT_REGULAR);
        cbFilterLokasiLapor.setBackground(UITheme.SURFACE);

        JButton btnFilter = UITheme.buatTombol("Cari / Filter", UITheme.PRIMARY, Color.WHITE);
        btnFilter.addActionListener(e -> terapkanFilterLapor());

        JButton btnReset = UITheme.buatTombolOutline("Reset", UITheme.TEXT_MAIN);
        btnReset.addActionListener(e -> {
            txtCariLapor.setText("");
            cbFilterLokasiLapor.setSelectedIndex(0);
            terapkanFilterLapor();
        });

        pnlKiri.add(lblCari);
        pnlKiri.add(txtCariLapor);
        pnlKiri.add(lblLokasi);
        pnlKiri.add(cbFilterLokasiLapor);
        pnlKiri.add(btnFilter);
        pnlKiri.add(btnReset);

        // Kanan: Tombol Utama Tambah Laporan Baru
        JPanel pnlKanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pnlKanan.setBackground(UITheme.SURFACE);

        JButton btnBukaDialogLaporAtas = UITheme.buatTombol("+ Buat Laporan Barang Temuan",
                new Color(22, 163, 74), Color.WHITE);
        btnBukaDialogLaporAtas.addActionListener(e -> bukaDialogLaporBarang());
        pnlKanan.add(btnBukaDialogLaporAtas);

        pnlToolbar.add(pnlKiri, BorderLayout.WEST);
        pnlToolbar.add(pnlKanan, BorderLayout.EAST);

        // Tabel Daftar Barang Temuan
        String[] kolom = {"ID", "Kategori", "Nama Barang", "Deskripsi", "Lokasi Temuan", "Tanggal", "Status"};
        modelTabelLapor = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelLapor = new JTable(modelTabelLapor);
        UITheme.formatTable(tabelLapor);
        tabelLapor.getColumnModel().getColumn(0).setPreferredWidth(85);
        tabelLapor.getColumnModel().getColumn(1).setPreferredWidth(105);
        tabelLapor.getColumnModel().getColumn(2).setPreferredWidth(160);
        tabelLapor.getColumnModel().getColumn(3).setPreferredWidth(230);
        tabelLapor.getColumnModel().getColumn(4).setPreferredWidth(130);
        tabelLapor.getColumnModel().getColumn(5).setPreferredWidth(90);
        tabelLapor.getColumnModel().getColumn(6).setPreferredWidth(115);

        JScrollPane scroll = new JScrollPane(tabelLapor);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        scroll.getViewport().setBackground(UITheme.SURFACE);

        // Baris Bawah: Info & Tombol Aksi Tambah
        JPanel pnlBawah = new JPanel(new BorderLayout());
        pnlBawah.setBackground(UITheme.BG_MAIN);

        JLabel lblInfo = new JLabel("Daftar seluruh barang temuan yang telah dilaporkan. Klik tombol di samping untuk mencatat temuan baru.");
        lblInfo.setFont(UITheme.FONT_SMALL);
        lblInfo.setForeground(UITheme.TEXT_MUTED);

        JButton btnBukaDialogLaporBawah = UITheme.buatTombol("+ Buat Laporan Barang Temuan Baru", UITheme.PRIMARY, Color.WHITE);
        btnBukaDialogLaporBawah.addActionListener(e -> bukaDialogLaporBarang());

        pnlBawah.add(lblInfo, BorderLayout.WEST);
        pnlBawah.add(btnBukaDialogLaporBawah, BorderLayout.EAST);

        panel.add(pnlToolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(pnlBawah, BorderLayout.SOUTH);
        return panel;
    }

    private void terapkanFilterLapor() {
        String keyword = txtCariLapor.getText().trim().toLowerCase();
        String lokasi = (String) cbFilterLokasiLapor.getSelectedItem();
        boolean filterLokasiAktif = lokasi != null && !lokasi.equals("Semua Lokasi Kampus");

        ArrayList<Item> semua = manager.getAllItems();
        ArrayList<Item> hasil = new ArrayList<>();

        for (Item item : semua) {
            boolean matchKey = keyword.isEmpty()
                    || item.getId().toLowerCase().contains(keyword)
                    || item.getNama().toLowerCase().contains(keyword)
                    || item.getDeskripsi().toLowerCase().contains(keyword)
                    || item.getLokasiDitemukan().toLowerCase().contains(keyword);

            boolean matchLok = !filterLokasiAktif
                    || item.getLokasiDitemukan().equalsIgnoreCase(lokasi);

            if (matchKey && matchLok) {
                hasil.add(item);
            }
        }

        isiTabelLapor(hasil);
    }

    private void isiTabelLapor(ArrayList<Item> items) {
        modelTabelLapor.setRowCount(0);
        for (Item item : items) {
            modelTabelLapor.addRow(new Object[]{
                    item.getId(),
                    item.getKategori(),
                    item.getNama(),
                    item.getDeskripsi(),
                    item.getLokasiDitemukan(),
                    item.getTanggalDitemukan(),
                    item.getStatus()
            });
        }
    }

    private void bukaDialogLaporBarang() {
        LaporBarangDialog dialog = new LaporBarangDialog(this, manager);
        dialog.setVisible(true);
        if (dialog.isSukses()) {
            refreshSemuaData();
        }
    }

    // =========================================================================
    // 4. LAYAR USER 3: RIWAYAT AKTIVITAS MAHASISWA
    // =========================================================================
    private JPanel buatLayarRiwayat() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(UITheme.BG_MAIN);
        panel.setBorder(new EmptyBorder(18, 20, 18, 20));

        // Baris Kartu Status
        JPanel pnlStats = new JPanel(new GridLayout(1, 3, 14, 0));
        pnlStats.setBackground(UITheme.BG_MAIN);

        lblStatTotalMahasiswa = new JLabel("0");
        lblStatMenungguPos = new JLabel("0");
        lblStatSelesaiMahasiswa = new JLabel("0");

        pnlStats.add(UITheme.buatStatCard("Total Aktivitas Terdata", "0", UITheme.PRIMARY));
        pnlStats.add(UITheme.buatStatCard("Siap Diambil di Pos", "0", UITheme.WARNING_TEXT));
        pnlStats.add(UITheme.buatStatCard("Selesai Diserahkan", "0", UITheme.SUCCESS_TEXT));

        // Tabel Riwayat
        String[] kolom = {"ID Tiket", "Kategori", "Nama Barang", "Deskripsi", "Lokasi Pengambilan", "Tanggal", "Status Progres"};
        modelTabelRiwayat = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelRiwayat = new JTable(modelTabelRiwayat);
        UITheme.formatTable(tabelRiwayat);
        tabelRiwayat.getColumnModel().getColumn(0).setPreferredWidth(85);
        tabelRiwayat.getColumnModel().getColumn(1).setPreferredWidth(105);
        tabelRiwayat.getColumnModel().getColumn(2).setPreferredWidth(160);
        tabelRiwayat.getColumnModel().getColumn(3).setPreferredWidth(230);
        tabelRiwayat.getColumnModel().getColumn(4).setPreferredWidth(130);
        tabelRiwayat.getColumnModel().getColumn(5).setPreferredWidth(90);
        tabelRiwayat.getColumnModel().getColumn(6).setPreferredWidth(115);

        JScrollPane scroll = new JScrollPane(tabelRiwayat);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        scroll.getViewport().setBackground(UITheme.SURFACE);

        // Panduan Pengambilan di Pos
        JPanel cardInfo = UITheme.buatCardPanel();
        cardInfo.setLayout(new BorderLayout(8, 8));

        JLabel lblInfoTitle = new JLabel("Prosedur Resmi Pengambilan Barang di Pos Keamanan Kampus");
        lblInfoTitle.setFont(UITheme.FONT_BOLD);
        lblInfoTitle.setForeground(UITheme.PRIMARY);

        JLabel lblInfoBody = new JLabel("<html>"
                + "1. Bawa <b>Kartu Tanda Mahasiswa (KTM) Asli</b> yang masih berlaku.<br>"
                + "2. Tunjukkan kode ID barang (misal: <i>BRG-1003</i>) kepada petugas jaga di Pos Keamanan Gedung D4.<br>"
                + "3. Petugas mencocokkan nomor seri/identitas dan memandu tanda tangan Berita Acara Serah Terima (BAST)."
                + "</html>");
        lblInfoBody.setFont(UITheme.FONT_REGULAR);
        lblInfoBody.setForeground(UITheme.TEXT_MAIN);

        cardInfo.add(lblInfoTitle, BorderLayout.NORTH);
        cardInfo.add(lblInfoBody, BorderLayout.CENTER);

        JPanel pnlCenter = new JPanel(new BorderLayout(0, 14));
        pnlCenter.setBackground(UITheme.BG_MAIN);
        pnlCenter.add(scroll, BorderLayout.CENTER);
        pnlCenter.add(cardInfo, BorderLayout.SOUTH);

        panel.add(pnlStats, BorderLayout.NORTH);
        panel.add(pnlCenter, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================================
    // 5. LAYAR ADMIN 1: PUSAT KENDALI (DASHBOARD ADMIN)
    // =========================================================================
    private JPanel buatLayarAdminDashboard() {
        JPanel p = new JPanel(new BorderLayout(14, 14));
        p.setBackground(UITheme.BG_MAIN);
        p.setBorder(new EmptyBorder(18, 20, 18, 20));

        // 4 Kartu Metrik
        JPanel pnlStats = new JPanel(new GridLayout(1, 4, 14, 0));
        pnlStats.setBackground(UITheme.BG_MAIN);

        lblStatTotal = new JLabel("0");
        lblStatTersedia = new JLabel("0");
        lblStatProsesKlaim = new JLabel("0");
        lblStatSelesai = new JLabel("0");

        pnlStats.add(UITheme.buatStatCard("Total Barang", "0", UITheme.PRIMARY));
        pnlStats.add(UITheme.buatStatCard("Tersedia", "0", UITheme.SUCCESS_TEXT));
        pnlStats.add(UITheme.buatStatCard("Proses Klaim", "0", UITheme.WARNING_TEXT));
        pnlStats.add(UITheme.buatStatCard("Selesai", "0", new Color(15, 118, 110)));

        // Tabel Antrean Klaim Menunggu Verifikasi
        JPanel pnlTabelKlaim = UITheme.buatCardPanel();
        pnlTabelKlaim.setLayout(new BorderLayout(10, 10));

        JLabel lblAntreanTitle = new JLabel("Klaim Menunggu Verifikasi Fisik di Pos (Status: PROSES_KLAIM)");
        lblAntreanTitle.setFont(UITheme.FONT_SECTION);
        lblAntreanTitle.setForeground(UITheme.TEXT_MAIN);

        String[] colKlaim = {"ID Tiket", "Kategori", "Nama Barang", "Deskripsi", "Lokasi Ditemukan", "Tanggal", "Status"};
        modelTabelAdminKlaim = new DefaultTableModel(colKlaim, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tabelAdminKlaim = new JTable(modelTabelAdminKlaim);
        UITheme.formatTable(tabelAdminKlaim);
        tabelAdminKlaim.getColumnModel().getColumn(0).setPreferredWidth(85);
        tabelAdminKlaim.getColumnModel().getColumn(1).setPreferredWidth(105);
        tabelAdminKlaim.getColumnModel().getColumn(2).setPreferredWidth(160);
        tabelAdminKlaim.getColumnModel().getColumn(3).setPreferredWidth(230);
        tabelAdminKlaim.getColumnModel().getColumn(4).setPreferredWidth(130);
        tabelAdminKlaim.getColumnModel().getColumn(5).setPreferredWidth(90);
        tabelAdminKlaim.getColumnModel().getColumn(6).setPreferredWidth(115);

        JScrollPane scrollKlaim = new JScrollPane(tabelAdminKlaim);
        scrollKlaim.setPreferredSize(new Dimension(scrollKlaim.getPreferredSize().width, 240));

        // Tombol Aksi Cepat dengan Kontras Tinggi
        JPanel pnlAksiKlaim = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 4));
        pnlAksiKlaim.setBackground(UITheme.SURFACE);

        JButton btnTolakCepat = UITheme.buatTombol("Tolak / Batalkan Klaim",
                new Color(220, 38, 38), Color.WHITE);
        btnTolakCepat.addActionListener(e -> adminBatalkanKlaim(tabelAdminKlaim, modelTabelAdminKlaim));

        JButton btnHandoverCepat = UITheme.buatTombol("Konfirmasi Serah Terima (Selesai)",
                new Color(22, 163, 74), Color.WHITE);
        btnHandoverCepat.addActionListener(e -> adminSelesaikanKlaim(tabelAdminKlaim, modelTabelAdminKlaim));

        pnlAksiKlaim.add(btnTolakCepat);
        pnlAksiKlaim.add(btnHandoverCepat);

        pnlTabelKlaim.add(lblAntreanTitle, BorderLayout.NORTH);
        pnlTabelKlaim.add(scrollKlaim, BorderLayout.CENTER);
        pnlTabelKlaim.add(pnlAksiKlaim, BorderLayout.SOUTH);

        p.add(pnlStats, BorderLayout.NORTH);
        p.add(pnlTabelKlaim, BorderLayout.CENTER);
        return p;
    }

    // =========================================================================
    // 6. LAYAR ADMIN 2: VERIFIKASI KLAIM & POS FISIK
    // =========================================================================
    private JPanel buatLayarAdminVerifikasiPos() {
        JPanel p = new JPanel(new BorderLayout(14, 14));
        p.setBackground(UITheme.BG_MAIN);
        p.setBorder(new EmptyBorder(18, 20, 18, 20));

        // Kiri: Daftar antrean
        JPanel pnlKiri = UITheme.buatCardPanel();
        pnlKiri.setLayout(new BorderLayout(8, 8));
        pnlKiri.setPreferredSize(new Dimension(320, 0));

        JLabel lblListTitle = new JLabel("Antrean Klaim Aktif di Pos");
        lblListTitle.setFont(UITheme.FONT_BOLD);
        pnlKiri.add(lblListTitle, BorderLayout.NORTH);

        modelListAntrean = new DefaultListModel<>();
        listAntreanKlaim = new JList<>(modelListAntrean);
        listAntreanKlaim.setFont(UITheme.FONT_REGULAR);
        listAntreanKlaim.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listAntreanKlaim.addListSelectionListener(e -> updateDetailPosTerpilih());

        JScrollPane scrollList = new JScrollPane(listAntreanKlaim);
        pnlKiri.add(scrollList, BorderLayout.CENTER);

        // Kanan: Panel Pencocokan Bukti & Validasi Identitas
        JPanel pnlKanan = UITheme.buatCardPanel();
        pnlKanan.setLayout(new BorderLayout(12, 12));

        JLabel lblDetailTitle = new JLabel("Pencocokan Bukti & Validasi Identitas Pemohon");
        lblDetailTitle.setFont(UITheme.FONT_SECTION);
        lblDetailTitle.setForeground(UITheme.TEXT_MAIN);

        // Konten Informasi
        JPanel pnlInfoItem = new JPanel(new GridLayout(0, 1, 6, 6));
        pnlInfoItem.setBackground(UITheme.SURFACE);

        lblDetailKlaimId = new JLabel("ID Barang: -");
        lblDetailKlaimId.setFont(UITheme.FONT_BOLD);
        lblDetailKlaimNama = new JLabel("Nama Barang: -");
        lblDetailKlaimNama.setFont(UITheme.FONT_BOLD);
        lblDetailKlaimKategori = new JLabel("Kategori: -");
        lblDetailKlaimLokasi = new JLabel("Lokasi Pos: -");
        lblDetailKlaimTanggal = new JLabel("Tanggal Ditemukan: -");

        pnlInfoItem.add(lblDetailKlaimId);
        pnlInfoItem.add(lblDetailKlaimNama);
        pnlInfoItem.add(lblDetailKlaimKategori);
        pnlInfoItem.add(lblDetailKlaimLokasi);
        pnlInfoItem.add(lblDetailKlaimTanggal);

        // Checklist Verifikasi Fisik
        JPanel pnlChecklist = new JPanel(new GridLayout(0, 1, 6, 6));
        pnlChecklist.setBackground(UITheme.PRIMARY_LIGHT);
        pnlChecklist.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel lblChecklistTitle = new JLabel("Checklist Verifikasi Fisik di Pos:");
        lblChecklistTitle.setFont(UITheme.FONT_BOLD);
        lblChecklistTitle.setForeground(UITheme.PRIMARY_DARK);

        chkKtmDicek = new JCheckBox("KTM Mahasiswa fisik asli telah diperiksa & sah.");
        chkKtmDicek.setBackground(UITheme.PRIMARY_LIGHT);
        chkBuktiCocok = new JCheckBox("Bukti nomor seri / identitas cocok dengan data sistem.");
        chkBuktiCocok.setBackground(UITheme.PRIMARY_LIGHT);
        chkBastDisetujui = new JCheckBox("Mahasiswa bersedia menandatangani Berita Acara Serah Terima.");
        chkBastDisetujui.setBackground(UITheme.PRIMARY_LIGHT);

        pnlChecklist.add(lblChecklistTitle);
        pnlChecklist.add(chkKtmDicek);
        pnlChecklist.add(chkBuktiCocok);
        pnlChecklist.add(chkBastDisetujui);

        // Tombol Konfirmasi Pos dengan Kontras Tinggi
        JPanel pnlAksiPos = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlAksiPos.setBackground(UITheme.SURFACE);

        btnPosTolak = UITheme.buatTombol("Tolak / Batalkan Klaim",
                new Color(220, 38, 38), Color.WHITE);
        btnPosTolak.addActionListener(e -> {
            if (itemTerpilihPos != null) {
                eksekusiBatalkanKlaim(itemTerpilihPos.getId());
            }
        });

        btnPosSerahTerima = UITheme.buatTombol("Konfirmasi Serah Terima & Selesai",
                new Color(22, 163, 74), Color.WHITE);
        btnPosSerahTerima.addActionListener(e -> {
            if (itemTerpilihPos == null) return;
            if (!chkKtmDicek.isSelected() || !chkBuktiCocok.isSelected() || !chkBastDisetujui.isSelected()) {
                JOptionPane.showMessageDialog(this,
                        "Harap centang semua checklist verifikasi fisik terlebih dahulu sebelum serah terima.",
                        "Checklist Belum Lengkap", JOptionPane.WARNING_MESSAGE);
                return;
            }
            eksekusiSelesaikanKlaim(itemTerpilihPos.getId());
        });

        pnlAksiPos.add(btnPosTolak);
        pnlAksiPos.add(btnPosSerahTerima);

        JPanel pnlBodyKanan = new JPanel(new BorderLayout(10, 10));
        pnlBodyKanan.setBackground(UITheme.SURFACE);
        pnlBodyKanan.add(pnlInfoItem, BorderLayout.NORTH);
        pnlBodyKanan.add(pnlChecklist, BorderLayout.CENTER);

        pnlKanan.add(lblDetailTitle, BorderLayout.NORTH);
        pnlKanan.add(pnlBodyKanan, BorderLayout.CENTER);
        pnlKanan.add(pnlAksiPos, BorderLayout.SOUTH);

        p.add(pnlKiri, BorderLayout.WEST);
        p.add(pnlKanan, BorderLayout.CENTER);
        return p;
    }

    private void updateDetailPosTerpilih() {
        int idx = listAntreanKlaim.getSelectedIndex();
        if (idx < 0) {
            itemTerpilihPos = null;
            lblDetailKlaimId.setText("ID Barang: -");
            lblDetailKlaimNama.setText("Nama Barang: -");
            lblDetailKlaimKategori.setText("Kategori: -");
            lblDetailKlaimLokasi.setText("Lokasi Pos: -");
            lblDetailKlaimTanggal.setText("Tanggal Ditemukan: -");
            return;
        }

        String itemStr = modelListAntrean.get(idx);
        String id = itemStr.split(" - ")[0].trim();
        itemTerpilihPos = manager.findById(id);

        if (itemTerpilihPos != null) {
            lblDetailKlaimId.setText("ID Tiket: " + itemTerpilihPos.getId());
            lblDetailKlaimNama.setText("Nama Barang: " + itemTerpilihPos.getNama());
            lblDetailKlaimKategori.setText("Kategori: " + itemTerpilihPos.getKategori());
            lblDetailKlaimLokasi.setText("Lokasi Pos: " + itemTerpilihPos.getLokasiDitemukan());
            lblDetailKlaimTanggal.setText("Tanggal Ditemukan: " + itemTerpilihPos.getTanggalDitemukan());
            chkKtmDicek.setSelected(false);
            chkBuktiCocok.setSelected(false);
            chkBastDisetujui.setSelected(false);
        }
    }

    // =========================================================================
    // 7. LAYAR ADMIN 3: KELOLA DATA INVENTARIS
    // =========================================================================
    private JPanel buatLayarAdminKelolaBarang() {
        JPanel p = new JPanel(new BorderLayout(12, 12));
        p.setBackground(UITheme.BG_MAIN);
        p.setBorder(new EmptyBorder(18, 20, 18, 20));

        // Toolbar Atas
        JPanel pnlToolbar = UITheme.buatCardPanel();
        pnlToolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));

        JLabel lblCari = new JLabel("Cari Barang / ID:");
        lblCari.setFont(UITheme.FONT_BOLD);
        txtAdminCariKelola = UITheme.buatTextField(16);

        JLabel lblStatus = new JLabel("Filter Status:");
        lblStatus.setFont(UITheme.FONT_BOLD);
        cbAdminFilterStatus = new JComboBox<>(new String[]{"SEMUA", "TERSEDIA", "PROSES_KLAIM", "SELESAI"});
        cbAdminFilterStatus.setFont(UITheme.FONT_REGULAR);
        cbAdminFilterStatus.setBackground(UITheme.SURFACE);

        JButton btnCari = UITheme.buatTombol("Filter", UITheme.PRIMARY, Color.WHITE);
        btnCari.addActionListener(e -> filterTabelKelolaAdmin());

        pnlToolbar.add(lblCari);
        pnlToolbar.add(txtAdminCariKelola);
        pnlToolbar.add(lblStatus);
        pnlToolbar.add(cbAdminFilterStatus);
        pnlToolbar.add(btnCari);

        // Tabel Lengkap Semua Barang
        String[] col = {"ID", "Kategori", "Nama Barang", "Deskripsi", "Lokasi", "Tanggal", "Status"};
        modelTabelAdminKelola = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tabelAdminKelola = new JTable(modelTabelAdminKelola);
        UITheme.formatTable(tabelAdminKelola);
        tabelAdminKelola.getColumnModel().getColumn(0).setPreferredWidth(85);
        tabelAdminKelola.getColumnModel().getColumn(1).setPreferredWidth(105);
        tabelAdminKelola.getColumnModel().getColumn(2).setPreferredWidth(160);
        tabelAdminKelola.getColumnModel().getColumn(3).setPreferredWidth(230);
        tabelAdminKelola.getColumnModel().getColumn(4).setPreferredWidth(130);
        tabelAdminKelola.getColumnModel().getColumn(5).setPreferredWidth(90);
        tabelAdminKelola.getColumnModel().getColumn(6).setPreferredWidth(115);

        JScrollPane scroll = new JScrollPane(tabelAdminKelola);

        // Baris Bawah: Tombol Hapus Barang Terpilih
        JPanel pnlBawah = new JPanel(new BorderLayout());
        pnlBawah.setBackground(UITheme.BG_MAIN);

        JButton btnHapus = UITheme.buatTombol("Hapus Barang Terpilih", new Color(220, 38, 38), Color.WHITE);
        btnHapus.addActionListener(e -> hapusBarangTerpilih());

        pnlBawah.add(btnHapus, BorderLayout.WEST);

        p.add(pnlToolbar, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        p.add(pnlBawah, BorderLayout.SOUTH);
        return p;
    }

    private void filterTabelKelolaAdmin() {
        String keyword = txtAdminCariKelola.getText().trim().toLowerCase();
        String status = (String) cbAdminFilterStatus.getSelectedItem();
        boolean filterStatus = status != null && !status.equals("SEMUA");

        ArrayList<Item> list = manager.getAllItems();
        modelTabelAdminKelola.setRowCount(0);

        for (Item item : list) {
            boolean matchKey = keyword.isEmpty()
                    || item.getId().toLowerCase().contains(keyword)
                    || item.getNama().toLowerCase().contains(keyword)
                    || item.getDeskripsi().toLowerCase().contains(keyword)
                    || item.getLokasiDitemukan().toLowerCase().contains(keyword);

            boolean matchStat = !filterStatus || item.getStatus().equalsIgnoreCase(status);

            if (matchKey && matchStat) {
                modelTabelAdminKelola.addRow(new Object[]{
                        item.getId(), item.getKategori(), item.getNama(),
                        item.getDeskripsi(), item.getLokasiDitemukan(), item.getTanggalDitemukan(), item.getStatus()
                });
            }
        }
    }

    private void hapusBarangTerpilih() {
        int row = tabelAdminKelola.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Silakan pilih barang yang ingin dihapus pada tabel.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) modelTabelAdminKelola.getValueAt(row, 0);
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus barang " + id + " secara permanen dari sistem dan file CSV?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                manager.deleteItem(id);
                JOptionPane.showMessageDialog(this, "Barang " + id + " berhasil dihapus.",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                refreshSemuaData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void adminSelesaikanKlaim(JTable tabel, DefaultTableModel model) {
        int row = tabel.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih salah satu baris klaim yang ingin diselesaikan.",
                    "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String id = (String) model.getValueAt(row, 0);
        eksekusiSelesaikanKlaim(id);
    }

    private void eksekusiSelesaikanKlaim(String id) {
        int jawab = JOptionPane.showConfirmDialog(this,
                "Konfirmasi serah terima fisik barang " + id + " kepada pemohon sah?\nStatus akan berubah menjadi SELESAI.",
                "Konfirmasi Serah Terima", JOptionPane.YES_NO_OPTION);
        if (jawab == JOptionPane.YES_OPTION) {
            try {
                manager.completeHandover(id);
                JOptionPane.showMessageDialog(this, "Serah terima fisik selesai! Status barang: SELESAI.",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                refreshSemuaData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyelesaikan klaim: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void adminBatalkanKlaim(JTable tabel, DefaultTableModel model) {
        int row = tabel.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih salah satu baris klaim yang ingin ditolak/dibatalkan.",
                    "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String id = (String) model.getValueAt(row, 0);
        eksekusiBatalkanKlaim(id);
    }

    private void eksekusiBatalkanKlaim(String id) {
        int jawab = JOptionPane.showConfirmDialog(this,
                "Tolak/batalkan klaim barang " + id + "?\nStatus barang akan dikembalikan menjadi TERSEDIA di katalog.",
                "Konfirmasi Pembatalan", JOptionPane.YES_NO_OPTION);
        if (jawab == JOptionPane.YES_OPTION) {
            try {
                manager.cancelClaim(id);
                JOptionPane.showMessageDialog(this, "Klaim dibatalkan. Status barang kembali TERSEDIA.",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                refreshSemuaData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal membatalkan klaim: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // =========================================================================
    // 8. SYNCHRONIZE & REFRESH SELURUH DATA
    // =========================================================================
    public void refreshSemuaData() {
        ArrayList<Item> semua = manager.getAllItems();

        // 1. Refresh Katalog Jelajah
        terapkanFilterJelajah();

        // Update dropdown lokasi unik di filter katalog
        Set<String> lokasiSet = new HashSet<>();
        for (Item item : semua) {
            if (item.getLokasiDitemukan() != null && !item.getLokasiDitemukan().trim().isEmpty()) {
                lokasiSet.add(item.getLokasiDitemukan().trim());
            }
        }
        String lokasiTerpilih = (String) cbFilterLokasi.getSelectedItem();
        cbFilterLokasi.removeAllItems();
        cbFilterLokasi.addItem("Semua Lokasi Kampus");
        for (String lok : lokasiSet) {
            cbFilterLokasi.addItem(lok);
        }
        if (lokasiTerpilih != null) {
            cbFilterLokasi.setSelectedItem(lokasiTerpilih);
        }

        // 2. Refresh Tabel Laporan Temuan
        terapkanFilterLapor();
        if (cbFilterLokasiLapor != null) {
            String lokLaporTerpilih = (String) cbFilterLokasiLapor.getSelectedItem();
            cbFilterLokasiLapor.removeAllItems();
            cbFilterLokasiLapor.addItem("Semua Lokasi Kampus");
            for (String lok : lokasiSet) {
                cbFilterLokasiLapor.addItem(lok);
            }
            if (lokLaporTerpilih != null) {
                cbFilterLokasiLapor.setSelectedItem(lokLaporTerpilih);
            }
        }

        // 3. Refresh Riwayat Aktivitas Mahasiswa
        modelTabelRiwayat.setRowCount(0);
        int totalAktivitas = semua.size();
        int jmlMenunggu = 0;
        int jmlSelesai = 0;

        for (Item item : semua) {
            if (Item.STATUS_PROSES_KLAIM.equals(item.getStatus())) {
                jmlMenunggu++;
            } else if (Item.STATUS_SELESAI.equals(item.getStatus())) {
                jmlSelesai++;
            }

            modelTabelRiwayat.addRow(new Object[]{
                    item.getId(), item.getKategori(), item.getNama(),
                    item.getDeskripsi(), item.getLokasiDitemukan(), item.getTanggalDitemukan(), item.getStatus()
            });
        }

        lblStatTotalMahasiswa.setText(String.valueOf(totalAktivitas));
        lblStatMenungguPos.setText(String.valueOf(jmlMenunggu));
        lblStatSelesaiMahasiswa.setText(String.valueOf(jmlSelesai));

        // 3. Refresh Dashboard & Kontrol Admin
        int countTotal = manager.getAllItems().size();
        int countTersedia = manager.countByStatus(Item.STATUS_TERSEDIA);
        int countProses = manager.countByStatus(Item.STATUS_PROSES_KLAIM);
        int countSelesai = manager.countByStatus(Item.STATUS_SELESAI);

        lblStatTotal.setText(String.valueOf(countTotal));
        lblStatTersedia.setText(String.valueOf(countTersedia));
        lblStatProsesKlaim.setText(String.valueOf(countProses));
        lblStatSelesai.setText(String.valueOf(countSelesai));

        // Isi tabel antrean klaim admin
        modelTabelAdminKlaim.setRowCount(0);
        modelListAntrean.clear();

        for (Item item : semua) {
            if (Item.STATUS_PROSES_KLAIM.equals(item.getStatus())) {
                modelTabelAdminKlaim.addRow(new Object[]{
                        item.getId(), item.getKategori(), item.getNama(),
                        item.getDeskripsi(), item.getLokasiDitemukan(), item.getTanggalDitemukan(), item.getStatus()
                });
                modelListAntrean.addElement(item.getId() + " - " + item.getNama() + " (" + item.getLokasiDitemukan() + ")");
            }
        }

        // Isi tabel kelola barang admin
        filterTabelKelolaAdmin();
    }
}
