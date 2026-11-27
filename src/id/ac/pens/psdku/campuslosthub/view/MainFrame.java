package id.ac.pens.psdku.campuslosthub.view;

import id.ac.pens.psdku.campuslosthub.model.BarangDokumen;
import id.ac.pens.psdku.campuslosthub.model.BarangElektronik;
import id.ac.pens.psdku.campuslosthub.model.Claimant;
import id.ac.pens.psdku.campuslosthub.model.Item;
import id.ac.pens.psdku.campuslosthub.service.LostAndFoundManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Antarmuka Utama Swing CampusLostHub (Finexy / FigCraft Design System).
 * Memisahkan secara tegas:
 * - Navbar & Layar Mahasiswa: Katalog Barang, Laporkan Temuan, Panduan Lapor & Klaim
 * - Navbar & Layar Petugas (4 Halaman): Dashboard Kendali, Verifikasi Klaim, Semua Inventaris, Riwayat Selesai
 */
public class MainFrame extends JFrame {

    private final LostAndFoundManager manager;
    private boolean isAdminLoggedIn = false;

    // Card Layout Layar Utama
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // Komponen Header
    private JLabel lblTagAdmin;
    private JLabel lblUserRole;
    private UITheme.PillButton btnAuthToggle;

    // Container Navbar Dinamis (User vs Admin)
    private JPanel pnlMenuContainer;
    private CardLayout cardMenuLayout;

    // Tombol Navigasi User
    private UITheme.PillButton btnNavJelajah;
    private UITheme.PillButton btnNavLapor;
    private UITheme.PillButton btnNavPanduan;

    // Tombol Navigasi Admin (4 Halaman)
    private UITheme.PillButton btnNavAdminDashboard;
    private UITheme.PillButton btnNavAdminVerifikasi;
    private UITheme.PillButton btnNavAdminInventaris;
    private UITheme.PillButton btnNavAdminRiwayat;

    // Layar User 1: Katalog Barang + KPI
    private JLabel lblHeroTersedia;
    private JLabel lblHeroProses;
    private JLabel lblHeroSelesai;
    private DefaultTableModel modelTabelJelajah;
    private JTable tabelJelajah;
    private JTextField txtCariNama;
    private JComboBox<String> cbFilterLokasi;
    private JButton btnKlaimTerpilih;

    // Layar User 2: Laporkan Temuan
    private DefaultTableModel modelTabelLapor;
    private JTable tabelLapor;
    private JTextField txtCariLapor;
    private JComboBox<String> cbFilterLokasiLapor;

    // Layar Admin 1: Dashboard Kendali
    private JLabel lblStatTotal;
    private JLabel lblStatTersedia;
    private JLabel lblStatProsesKlaim;
    private JLabel lblStatSelesai;
    private DefaultTableModel modelTabelAdminKlaim;
    private JTable tabelAdminKlaim;

    // Layar Admin 2: Verifikasi Klaim & Pos
    private JLabel lblPosProses;
    private JLabel lblPosTersedia;
    private JLabel lblPosSelesai;
    private JList<String> listAntreanKlaim;
    private DefaultListModel<String> modelListAntrean;
    private JLabel lblDetailKlaimId;
    private JLabel lblDetailKlaimNama;
    private JLabel lblDetailKlaimKategori;
    private JLabel lblDetailKlaimLokasi;
    private JLabel lblDetailKlaimTanggal;
    private JLabel lblDetailKlaimPemohon;
    private JCheckBox chkKtmDicek;
    private JCheckBox chkBuktiCocok;
    private JCheckBox chkBastDisetujui;
    private JLabel lblFotoSerahTerima;
    private JLabel lblStatusTtd;
    private boolean ttdSudahDitandatangani = false;
    private Item itemTerpilihPos = null;

    // Layar Admin 3: Semua Inventaris
    private DefaultTableModel modelTabelAdminKelola;
    private JTable tabelAdminKelola;
    private JTextField txtAdminCariKelola;
    private JComboBox<String> cbAdminFilterStatus;

    // Layar Admin 4: Riwayat Selesai
    private DefaultTableModel modelTabelRiwayatSelesai;
    private JTable tabelRiwayatSelesai;

    public MainFrame(LostAndFoundManager manager) {
        this.manager = manager;

        setTitle("CampusLostHub — Smart Campus Lost & Found System (PENS)");
        setSize(1240, 780);
        setMinimumSize(new Dimension(1060, 660));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        refreshSemuaData();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_MAIN);

        root.add(buatHeaderNavigasi(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(UITheme.BG_MAIN);

        // Layar Mahasiswa (3 Halaman)
        mainContentPanel.add(buatLayarJelajah(), "JELAJAH");
        mainContentPanel.add(buatLayarLapor(), "LAPOR");
        mainContentPanel.add(buatLayarPanduan(), "PANDUAN");

        // Layar Petugas Pos (4 Halaman)
        mainContentPanel.add(buatLayarAdminDashboard(), "ADMIN_DASHBOARD");
        mainContentPanel.add(buatLayarAdminVerifikasiPos(), "ADMIN_VERIFIKASI");
        mainContentPanel.add(buatLayarAdminKelolaBarang(), "ADMIN_INVENTARIS");
        mainContentPanel.add(buatLayarAdminRiwayatSelesai(), "ADMIN_RIWAYAT");

        root.add(mainContentPanel, BorderLayout.CENTER);
        setContentPane(root);

        gantiLayarUtama("JELAJAH");
    }

    // =========================================================================
    // 1. HEADER & NAVBAR FINEXY (USER VS ADMIN)
    // =========================================================================
    private JPanel buatHeaderNavigasi() {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(UITheme.SURFACE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER),
                new EmptyBorder(12, 18, 12, 18)
        ));

        // Kiri: Coral Dot + Brand + Badge ADMIN (muncul saat login)
        JPanel pnlLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pnlLogo.setBackground(UITheme.SURFACE);

        JPanel coralDot = new UITheme.RoundedPanel(999, UITheme.PRIMARY, null);
        coralDot.setPreferredSize(new Dimension(26, 26));

        JLabel lblLogo = new JLabel("CampusLostHub");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblLogo.setForeground(UITheme.TEXT_MAIN);

        lblTagAdmin = new JLabel(" ADMIN ");
        lblTagAdmin.setFont(UITheme.FONT_BADGE);
        lblTagAdmin.setBackground(UITheme.DANGER_BG);
        lblTagAdmin.setForeground(UITheme.DANGER_TEXT);
        lblTagAdmin.setOpaque(true);
        lblTagAdmin.setBorder(new EmptyBorder(3, 8, 3, 8));
        lblTagAdmin.setVisible(false);

        pnlLogo.add(coralDot);
        pnlLogo.add(lblLogo);
        pnlLogo.add(lblTagAdmin);

        // Tengah: Center Pill Container
        cardMenuLayout = new CardLayout();
        pnlMenuContainer = new JPanel(cardMenuLayout);
        pnlMenuContainer.setBackground(UITheme.SURFACE);

        // A. Menu Mahasiswa (3 Pill)
        JPanel pnlUserWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pnlUserWrap.setBackground(UITheme.SURFACE);
        JPanel pnlMenuUser = new UITheme.RoundedPanel(
                new FlowLayout(FlowLayout.CENTER, 4, 4), 999, UITheme.SURFACE, UITheme.BORDER);

        btnNavJelajah = buatNavPill("Katalog Barang");
        btnNavLapor = buatNavPill("Laporkan Temuan");
        btnNavPanduan = buatNavPill("Panduan Lapor & Klaim");

        btnNavJelajah.addActionListener(e -> gantiLayarUtama("JELAJAH"));
        btnNavLapor.addActionListener(e -> gantiLayarUtama("LAPOR"));
        btnNavPanduan.addActionListener(e -> gantiLayarUtama("PANDUAN"));

        pnlMenuUser.add(btnNavJelajah);
        pnlMenuUser.add(btnNavLapor);
        pnlMenuUser.add(btnNavPanduan);
        pnlUserWrap.add(pnlMenuUser);

        // B. Menu Petugas Pos (4 Pill)
        JPanel pnlAdminWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pnlAdminWrap.setBackground(UITheme.SURFACE);
        JPanel pnlMenuAdmin = new UITheme.RoundedPanel(
                new FlowLayout(FlowLayout.CENTER, 4, 4), 999, UITheme.SURFACE, UITheme.BORDER);

        btnNavAdminDashboard = buatNavPill("Dashboard Kendali");
        btnNavAdminVerifikasi = buatNavPill("Verifikasi Klaim");
        btnNavAdminInventaris = buatNavPill("Semua Inventaris");
        btnNavAdminRiwayat = buatNavPill("Riwayat Selesai");

        btnNavAdminDashboard.addActionListener(e -> gantiLayarUtama("ADMIN_DASHBOARD"));
        btnNavAdminVerifikasi.addActionListener(e -> gantiLayarUtama("ADMIN_VERIFIKASI"));
        btnNavAdminInventaris.addActionListener(e -> gantiLayarUtama("ADMIN_INVENTARIS"));
        btnNavAdminRiwayat.addActionListener(e -> gantiLayarUtama("ADMIN_RIWAYAT"));

        pnlMenuAdmin.add(btnNavAdminDashboard);
        pnlMenuAdmin.add(btnNavAdminVerifikasi);
        pnlMenuAdmin.add(btnNavAdminInventaris);
        pnlMenuAdmin.add(btnNavAdminRiwayat);
        pnlAdminWrap.add(pnlMenuAdmin);

        pnlMenuContainer.add(pnlUserWrap, "USER_NAV");
        pnlMenuContainer.add(pnlAdminWrap, "ADMIN_NAV");

        // Kanan: Tombol Masuk/Keluar Petugas
        JPanel pnlKanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 2));
        pnlKanan.setBackground(UITheme.SURFACE);

        lblUserRole = new JLabel("");
        lblUserRole.setFont(UITheme.FONT_SMALL);
        lblUserRole.setForeground(UITheme.SUCCESS_TEXT);
        lblUserRole.setVisible(false);

        btnAuthToggle = new UITheme.PillButton("Masuk Petugas Pos", UITheme.SURFACE, UITheme.TEXT_MAIN, UITheme.BORDER);
        btnAuthToggle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAuthToggle.setBorder(new EmptyBorder(7, 14, 7, 14));
        btnAuthToggle.addActionListener(e -> toggleAdminAuth());

        pnlKanan.add(btnAuthToggle);

        header.add(pnlLogo, BorderLayout.WEST);
        header.add(pnlMenuContainer, BorderLayout.CENTER);
        header.add(pnlKanan, BorderLayout.EAST);
        return header;
    }

    private UITheme.PillButton buatNavPill(String teks) {
        UITheme.PillButton btn = new UITheme.PillButton(teks, UITheme.SURFACE, UITheme.TEXT_MUTED, null);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(new EmptyBorder(6, 12, 6, 12));
        return btn;
    }

    private void updateNavHighlight(UITheme.PillButton btn, boolean aktif) {
        if (aktif) {
            btn.setPillStyle(UITheme.CHARCOAL, Color.WHITE, null);
        } else {
            btn.setPillStyle(UITheme.SURFACE, UITheme.TEXT_MUTED, null);
        }
    }

    private void gantiLayarUtama(String namaLayar) {
        cardLayout.show(mainContentPanel, namaLayar);

        updateNavHighlight(btnNavJelajah, "JELAJAH".equals(namaLayar));
        updateNavHighlight(btnNavLapor, "LAPOR".equals(namaLayar));
        updateNavHighlight(btnNavPanduan, "PANDUAN".equals(namaLayar));

        updateNavHighlight(btnNavAdminDashboard, "ADMIN_DASHBOARD".equals(namaLayar));
        updateNavHighlight(btnNavAdminVerifikasi, "ADMIN_VERIFIKASI".equals(namaLayar));
        updateNavHighlight(btnNavAdminInventaris, "ADMIN_INVENTARIS".equals(namaLayar));
        updateNavHighlight(btnNavAdminRiwayat, "ADMIN_RIWAYAT".equals(namaLayar));

        refreshSemuaData();
    }

    private void toggleAdminAuth() {
        if (!isAdminLoggedIn) {
            AdminLoginDialog dialog = new AdminLoginDialog(this);
            dialog.setVisible(true);
            if (dialog.isAuthenticated()) {
                isAdminLoggedIn = true;
                cardMenuLayout.show(pnlMenuContainer, "ADMIN_NAV");
                lblTagAdmin.setVisible(true);
                btnAuthToggle.setText("Keluar Mode Petugas");
                gantiLayarUtama("ADMIN_DASHBOARD");
            }
        } else {
            int jawab = JOptionPane.showConfirmDialog(this,
                    "Keluar dari mode Petugas Pos dan kembali ke portal mahasiswa?",
                    "Konfirmasi Keluar Mode Petugas", JOptionPane.YES_NO_OPTION);
            if (jawab == JOptionPane.YES_OPTION) {
                isAdminLoggedIn = false;
                cardMenuLayout.show(pnlMenuContainer, "USER_NAV");
                lblTagAdmin.setVisible(false);
                lblUserRole.setVisible(false);
                btnAuthToggle.setText("Masuk Petugas Pos");
                gantiLayarUtama("JELAJAH");
            }
        }
    }

    // =========================================================================
    // 2. LAYAR USER 1: KATALOG BARANG (SCREEN 1 FINEXY)
    // =========================================================================
    private JPanel buatLayarJelajah() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(UITheme.BG_MAIN);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Top Stack: Hero Header + 3 KPI Bento Row
        JPanel pnlTop = new JPanel(new BorderLayout(0, 16));
        pnlTop.setBackground(UITheme.BG_MAIN);

        // Hero Row
        JPanel pnlHero = new JPanel(new BorderLayout());
        pnlHero.setBackground(UITheme.BG_MAIN);

        JPanel pnlHeroText = new JPanel(new GridLayout(0, 1, 2, 2));
        pnlHeroText.setBackground(UITheme.BG_MAIN);
        JLabel lblHeroTitle = new JLabel("Temukan & Kembalikan Barang Kampus");
        lblHeroTitle.setFont(UITheme.FONT_HERO);
        lblHeroTitle.setForeground(UITheme.TEXT_MAIN);
        JLabel lblHeroSub = new JLabel("PENS PSDKU Lamongan • Katalog barang temuan aktif tanpa perlu login");
        lblHeroSub.setFont(UITheme.FONT_REGULAR);
        lblHeroSub.setForeground(UITheme.TEXT_MUTED);
        pnlHeroText.add(lblHeroTitle);
        pnlHeroText.add(lblHeroSub);

        JButton btnLaporCta = UITheme.buatTombol("+ Laporkan Temuan", UITheme.CHARCOAL, Color.WHITE);
        btnLaporCta.addActionListener(e -> bukaDialogLaporBarang());

        pnlHero.add(pnlHeroText, BorderLayout.WEST);
        pnlHero.add(btnLaporCta, BorderLayout.EAST);

        // 3 KPI Bento Row
        JPanel pnlKpi = new JPanel(new GridLayout(1, 3, 14, 0));
        pnlKpi.setBackground(UITheme.BG_MAIN);

        lblHeroTersedia = new JLabel("0 Barang");
        lblHeroProses = new JLabel("0 Barang");
        lblHeroSelesai = new JLabel("0 Barang");

        pnlKpi.add(UITheme.buatKpiCard("Barang Tersedia (Siap Diklaim)", lblHeroTersedia,
                "Siap diverifikasi & diklaim pemilik", true, null));
        pnlKpi.add(UITheme.buatKpiCard("Dalam Proses Klaim", lblHeroProses,
                "• Menunggu verifikasi KTM di pos", false, UITheme.WARNING_TEXT));
        pnlKpi.add(UITheme.buatKpiCard("Sudah Dikembalikan", lblHeroSelesai,
                "↑ Terverifikasi selesai diserahkan", false, UITheme.SUCCESS_TEXT));

        pnlTop.add(pnlHero, BorderLayout.NORTH);
        pnlTop.add(pnlKpi, BorderLayout.CENTER);

        // Catalog Card
        JPanel cardCatalog = UITheme.buatCardPanel();
        cardCatalog.setLayout(new BorderLayout(0, 12));

        // Toolbar Catalog
        JPanel pnlToolbar = new JPanel(new BorderLayout(12, 0));
        pnlToolbar.setBackground(UITheme.SURFACE);

        JLabel lblCatTitle = new JLabel("Katalog Barang Aktif (Atribut Rahasia Disembunyikan)");
        lblCatTitle.setFont(UITheme.FONT_SECTION);
        lblCatTitle.setForeground(UITheme.TEXT_MAIN);

        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlFilter.setBackground(UITheme.SURFACE);

        txtCariNama = UITheme.buatTextField(16);
        txtCariNama.setToolTipText("Cari nama barang...");
        cbFilterLokasi = new JComboBox<>(new String[]{"Semua Lokasi Kampus"});
        cbFilterLokasi.setFont(UITheme.FONT_REGULAR);
        cbFilterLokasi.setBackground(UITheme.SURFACE);

        JButton btnFilter = UITheme.buatTombol("Cari", UITheme.CHARCOAL, Color.WHITE);
        btnFilter.addActionListener(e -> terapkanFilterJelajah());

        JButton btnReset = UITheme.buatTombolOutline("Reset", UITheme.TEXT_MUTED);
        btnReset.addActionListener(e -> {
            txtCariNama.setText("");
            cbFilterLokasi.setSelectedIndex(0);
            terapkanFilterJelajah();
        });

        pnlFilter.add(txtCariNama);
        pnlFilter.add(cbFilterLokasi);
        pnlFilter.add(btnFilter);
        pnlFilter.add(btnReset);

        pnlToolbar.add(lblCatTitle, BorderLayout.WEST);
        pnlToolbar.add(pnlFilter, BorderLayout.EAST);

        // Tabel Katalog
        String[] kolom = {"ID Barang", "Kategori", "Nama Barang", "Deskripsi", "Lokasi Ditemukan", "Tanggal", "Status"};
        modelTabelJelajah = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelJelajah = new JTable(modelTabelJelajah);
        UITheme.formatTable(tabelJelajah);
        aturLebarKolomStandar(tabelJelajah);

        tabelJelajah.getSelectionModel().addListSelectionListener(e -> {
            int row = tabelJelajah.getSelectedRow();
            btnKlaimTerpilih.setEnabled(row >= 0);
        });

        JScrollPane scroll = new JScrollPane(tabelJelajah);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_LIGHT, 1));
        scroll.getViewport().setBackground(UITheme.SURFACE);

        // Footer Card
        JPanel pnlBawah = new JPanel(new BorderLayout());
        pnlBawah.setBackground(UITheme.SURFACE);

        JLabel lblPetunjuk = new JLabel("Pilih baris barang berstatus TERSEDIA pada tabel lalu klik 'Ajukan Klaim'.");
        lblPetunjuk.setFont(UITheme.FONT_SMALL);
        lblPetunjuk.setForeground(UITheme.TEXT_MUTED);

        btnKlaimTerpilih = UITheme.buatTombol("Ajukan Klaim", UITheme.CHARCOAL, Color.WHITE);
        btnKlaimTerpilih.setEnabled(false);
        btnKlaimTerpilih.addActionListener(e -> bukaDialogKlaim());

        pnlBawah.add(lblPetunjuk, BorderLayout.WEST);
        pnlBawah.add(btnKlaimTerpilih, BorderLayout.EAST);

        cardCatalog.add(pnlToolbar, BorderLayout.NORTH);
        cardCatalog.add(scroll, BorderLayout.CENTER);
        cardCatalog.add(pnlBawah, BorderLayout.SOUTH);

        panel.add(pnlTop, BorderLayout.NORTH);
        panel.add(cardCatalog, BorderLayout.CENTER);
        return panel;
    }

    private void aturLebarKolomStandar(JTable t) {
        t.getColumnModel().getColumn(0).setPreferredWidth(90);
        t.getColumnModel().getColumn(1).setPreferredWidth(100);
        t.getColumnModel().getColumn(2).setPreferredWidth(180);
        t.getColumnModel().getColumn(3).setPreferredWidth(240);
        t.getColumnModel().getColumn(4).setPreferredWidth(140);
        t.getColumnModel().getColumn(5).setPreferredWidth(95);
        t.getColumnModel().getColumn(6).setPreferredWidth(120);
    }

    private void terapkanFilterJelajah() {
        String keyword = txtCariNama.getText().trim().toLowerCase();
        String lokasi = (String) cbFilterLokasi.getSelectedItem();
        boolean filterLokasiAktif = lokasi != null && !lokasi.equals("Semua Lokasi Kampus");

        modelTabelJelajah.setRowCount(0);
        for (Item item : manager.getAllItems()) {
            boolean matchNama = keyword.isEmpty()
                    || item.getNama().toLowerCase().contains(keyword)
                    || item.getDeskripsi().toLowerCase().contains(keyword)
                    || item.getId().toLowerCase().contains(keyword);

            boolean matchLokasi = !filterLokasiAktif
                    || item.getLokasiDitemukan().equalsIgnoreCase(lokasi);

            if (matchNama && matchLokasi) {
                modelTabelJelajah.addRow(new Object[]{
                        item.getId(), item.getKategori(), item.getNama(),
                        item.getDeskripsi(), item.getLokasiDitemukan(),
                        item.getTanggalDitemukan(), item.getStatus()
                });
            }
        }
    }

    private void bukaDialogKlaim() {
        int selectedRow = tabelJelajah.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Silakan pilih salah satu baris barang terlebih dahulu.",
                    "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String id = (String) modelTabelJelajah.getValueAt(selectedRow, 0);
        Item item = manager.findById(id);
        if (item == null) return;

        if (!Item.STATUS_TERSEDIA.equals(item.getStatus())) {
            JOptionPane.showMessageDialog(this,
                    "Barang " + item.getId() + " sedang berstatus: " + item.getStatus()
                            + ".\nHanya barang berstatus TERSEDIA yang dapat diklaim.",
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
    // 3. LAYAR USER 2: LAPORKAN BARANG TEMUAN (SCREEN 6 FINEXY)
    // =========================================================================
    private JPanel buatLayarLapor() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(UITheme.BG_MAIN);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel pnlToolbar = UITheme.buatCardPanel();
        pnlToolbar.setLayout(new BorderLayout(12, 0));

        JPanel pnlKiri = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlKiri.setBackground(UITheme.SURFACE);

        JLabel lblCari = new JLabel("Cari Laporan:");
        lblCari.setFont(UITheme.FONT_BOLD);
        txtCariLapor = UITheme.buatTextField(16);

        cbFilterLokasiLapor = new JComboBox<>(new String[]{"Semua Lokasi Kampus"});
        cbFilterLokasiLapor.setFont(UITheme.FONT_REGULAR);
        cbFilterLokasiLapor.setBackground(UITheme.SURFACE);

        JButton btnFilter = UITheme.buatTombol("Cari", UITheme.CHARCOAL, Color.WHITE);
        btnFilter.addActionListener(e -> terapkanFilterLapor());

        JButton btnReset = UITheme.buatTombolOutline("Reset", UITheme.TEXT_MUTED);
        btnReset.addActionListener(e -> {
            txtCariLapor.setText("");
            cbFilterLokasiLapor.setSelectedIndex(0);
            terapkanFilterLapor();
        });

        pnlKiri.add(lblCari);
        pnlKiri.add(txtCariLapor);
        pnlKiri.add(cbFilterLokasiLapor);
        pnlKiri.add(btnFilter);
        pnlKiri.add(btnReset);

        JButton btnBuatLaporan = UITheme.buatTombol("+ Buat Laporan Barang Temuan", UITheme.PRIMARY, Color.WHITE);
        btnBuatLaporan.addActionListener(e -> bukaDialogLaporBarang());

        pnlToolbar.add(pnlKiri, BorderLayout.WEST);
        pnlToolbar.add(btnBuatLaporan, BorderLayout.EAST);

        JPanel cardTabel = UITheme.buatCardPanel();
        cardTabel.setLayout(new BorderLayout(0, 10));

        JLabel lblInfo = new JLabel("Daftar Barang Temuan Tercatat (Atribut rahasia otomatis disembunyikan untuk keamanan)");
        lblInfo.setFont(UITheme.FONT_SMALL);
        lblInfo.setForeground(UITheme.TEXT_MUTED);

        String[] kolom = {"ID Barang", "Kategori", "Nama Barang", "Deskripsi", "Lokasi Ditemukan", "Tanggal", "Status"};
        modelTabelLapor = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelLapor = new JTable(modelTabelLapor);
        UITheme.formatTable(tabelLapor);
        aturLebarKolomStandar(tabelLapor);

        JScrollPane scroll = new JScrollPane(tabelLapor);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_LIGHT, 1));
        scroll.getViewport().setBackground(UITheme.SURFACE);

        cardTabel.add(lblInfo, BorderLayout.NORTH);
        cardTabel.add(scroll, BorderLayout.CENTER);

        panel.add(pnlToolbar, BorderLayout.NORTH);
        panel.add(cardTabel, BorderLayout.CENTER);
        return panel;
    }

    private void terapkanFilterLapor() {
        String keyword = txtCariLapor.getText().trim().toLowerCase();
        String lokasi = (String) cbFilterLokasiLapor.getSelectedItem();
        boolean filterLokasiAktif = lokasi != null && !lokasi.equals("Semua Lokasi Kampus");

        modelTabelLapor.setRowCount(0);
        for (Item item : manager.getAllItems()) {
            boolean matchNama = keyword.isEmpty()
                    || item.getNama().toLowerCase().contains(keyword)
                    || item.getDeskripsi().toLowerCase().contains(keyword)
                    || item.getId().toLowerCase().contains(keyword);
            boolean matchLokasi = !filterLokasiAktif
                    || item.getLokasiDitemukan().equalsIgnoreCase(lokasi);

            if (matchNama && matchLokasi) {
                modelTabelLapor.addRow(new Object[]{
                        item.getId(), item.getKategori(), item.getNama(),
                        item.getDeskripsi(), item.getLokasiDitemukan(),
                        item.getTanggalDitemukan(), item.getStatus()
                });
            }
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
    // 4. LAYAR USER 3: PANDUAN LAPOR & KLAIM (SCREEN 5 FINEXY)
    // =========================================================================
    private JPanel buatLayarPanduan() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(UITheme.BG_MAIN);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));

        JPanel pnlHeader = new JPanel(new GridLayout(0, 1, 4, 4));
        pnlHeader.setBackground(UITheme.BG_MAIN);

        JLabel lblTitle = new JLabel("Panduan Cara Melaporkan & Klaim Barang");
        lblTitle.setFont(UITheme.FONT_HERO);
        lblTitle.setForeground(UITheme.TEXT_MAIN);

        JLabel lblSub = new JLabel("Alur lengkap pelaporan barang temuan hingga serah terima kepada pemilik sah tanpa perlu login akun.");
        lblSub.setFont(UITheme.FONT_REGULAR);
        lblSub.setForeground(UITheme.TEXT_MUTED);

        pnlHeader.add(lblTitle);
        pnlHeader.add(lblSub);

        JPanel pnlSteps = new JPanel(new GridLayout(1, 3, 18, 0));
        pnlSteps.setBackground(UITheme.BG_MAIN);

        pnlSteps.add(buatKartuPanduan("01", "Laporkan Barang Temuan",
                "Buka tab Laporkan Temuan, pilih kategori (Elektronik/Dokumen), isi lokasi & ciri fisik, serta data rahasia (SN/NIM) lalu simpan dan titipkan ke Pos Satpam.",
                false));
        pnlSteps.add(buatKartuPanduan("02", "Cari & Ajukan Klaim Barang",
                "Pemilik mencari barang di Katalog Barang, klik Ajukan Klaim, lalu isi identitas KTM (NIM & Nama), bukti rahasia (SN/NIM), serta unggah foto bukti kepemilikan.",
                true));
        pnlSteps.add(buatKartuPanduan("03", "Verifikasi & Serah Terima di Pos",
                "Datang ke Pos Satpam Gedung D3 membawa KTM asli. Petugas mencocokkan bukti fisik, foto serah terima, dan mahasiswa menandatangani berita acara (SELESAI).",
                false));

        JPanel pnlWrap = new JPanel(new BorderLayout());
        pnlWrap.setBackground(UITheme.BG_MAIN);
        pnlWrap.add(pnlSteps, BorderLayout.NORTH);

        panel.add(pnlHeader, BorderLayout.NORTH);
        panel.add(pnlWrap, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buatKartuPanduan(String nomor, String judul, String deskripsi, boolean isFocal) {
        Color bg = isFocal ? UITheme.PRIMARY : UITheme.SURFACE;
        Color border = isFocal ? UITheme.PRIMARY : UITheme.BORDER;
        Color numFg = isFocal ? Color.WHITE : (nomor.equals("01") ? UITheme.PRIMARY : UITheme.TEXT_MAIN);
        Color titleFg = isFocal ? Color.WHITE : UITheme.TEXT_MAIN;
        Color descFg = isFocal ? new Color(255, 232, 224) : UITheme.TEXT_SUBTLE;

        JPanel card = new UITheme.RoundedPanel(new BorderLayout(0, 12), 18, bg, border);
        card.setBorder(new EmptyBorder(24, 24, 28, 24));
        card.setPreferredSize(new Dimension(320, 240));

        JLabel lblNum = new JLabel(nomor);
        lblNum.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblNum.setForeground(numFg);

        JPanel pnlText = new JPanel(new BorderLayout(0, 8));
        pnlText.setOpaque(false);

        JLabel lblJudul = new JLabel(judul);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJudul.setForeground(titleFg);

        JLabel lblDesc = new JLabel("<html><div style='width:260px;line-height:1.45;'>" + deskripsi + "</div></html>");
        lblDesc.setFont(UITheme.FONT_REGULAR);
        lblDesc.setForeground(descFg);
        lblDesc.setVerticalAlignment(SwingConstants.TOP);

        pnlText.add(lblJudul, BorderLayout.NORTH);
        pnlText.add(lblDesc, BorderLayout.CENTER);

        card.add(lblNum, BorderLayout.NORTH);
        card.add(pnlText, BorderLayout.CENTER);
        return card;
    }

    // =========================================================================
    // 5. LAYAR ADMIN 1: DASHBOARD KENDALI (SCREEN 9 FINEXY)
    // =========================================================================
    private JPanel buatLayarAdminDashboard() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(UITheme.BG_MAIN);
        p.setBorder(new EmptyBorder(20, 24, 20, 24));

        // 4 KPI Bento Cards
        JPanel pnlKpi = new JPanel(new GridLayout(1, 4, 14, 0));
        pnlKpi.setBackground(UITheme.BG_MAIN);

        lblStatTotal = new JLabel("0");
        lblStatTersedia = new JLabel("0");
        lblStatProsesKlaim = new JLabel("0");
        lblStatSelesai = new JLabel("0");

        pnlKpi.add(UITheme.buatKpiCard("TOTAL BARANG", lblStatTotal, "Total seluruh item CSV", false, UITheme.TEXT_MUTED));
        pnlKpi.add(UITheme.buatKpiCard("TERSEDIA", lblStatTersedia, "Siap diklaim pemilik", false, UITheme.SUCCESS_TEXT));
        pnlKpi.add(UITheme.buatKpiCard("PROSES KLAIM", lblStatProsesKlaim, "Perlu verifikasi di pos", true, null));
        pnlKpi.add(UITheme.buatKpiCard("SELESAI", lblStatSelesai, "Sudah diserahterimakan", false, UITheme.TEXT_MUTED));

        // Card Antrean Verifikasi
        JPanel pnlTabelCard = UITheme.buatCardPanel();
        pnlTabelCard.setLayout(new BorderLayout(0, 12));

        JLabel lblAntreanTitle = new JLabel("Klaim Menunggu Verifikasi Fisik di Pos (Status: PROSES_KLAIM)");
        lblAntreanTitle.setFont(UITheme.FONT_SECTION);
        lblAntreanTitle.setForeground(UITheme.TEXT_MAIN);

        String[] col = {"ID Tiket", "Kategori", "Nama Barang", "Deskripsi", "Lokasi Ditemukan", "Tanggal", "Status"};
        modelTabelAdminKlaim = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelAdminKlaim = new JTable(modelTabelAdminKlaim);
        UITheme.formatTable(tabelAdminKlaim);
        aturLebarKolomStandar(tabelAdminKlaim);

        JScrollPane scroll = new JScrollPane(tabelAdminKlaim);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_LIGHT, 1));
        scroll.getViewport().setBackground(UITheme.SURFACE);

        JPanel pnlAksi = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlAksi.setBackground(UITheme.SURFACE);

        JButton btnBatalKlaim = UITheme.buatTombol("Tolak / Batalkan Klaim", UITheme.DANGER_BG, UITheme.DANGER_TEXT);
        btnBatalKlaim.addActionListener(e -> adminBatalkanKlaim(tabelAdminKlaim, modelTabelAdminKlaim));

        JButton btnSelesaiKlaim = UITheme.buatTombol("Konfirmasi Serah Terima (Selesai)", UITheme.CHARCOAL, Color.WHITE);
        btnSelesaiKlaim.addActionListener(e -> adminSelesaikanKlaim(tabelAdminKlaim, modelTabelAdminKlaim));

        pnlAksi.add(btnBatalKlaim);
        pnlAksi.add(btnSelesaiKlaim);

        pnlTabelCard.add(lblAntreanTitle, BorderLayout.NORTH);
        pnlTabelCard.add(scroll, BorderLayout.CENTER);
        pnlTabelCard.add(pnlAksi, BorderLayout.SOUTH);

        p.add(pnlKpi, BorderLayout.NORTH);
        p.add(pnlTabelCard, BorderLayout.CENTER);
        return p;
    }

    // =========================================================================
    // 6. LAYAR ADMIN 2: VERIFIKASI KLAIM & POS (SCREEN 3 + SCREEN 10)
    // =========================================================================
    private JPanel buatLayarAdminVerifikasiPos() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setBackground(UITheme.BG_MAIN);
        p.setBorder(new EmptyBorder(18, 24, 18, 24));

        // Top: Banner Panduan + 3 KPI Mini
        JPanel pnlTop = new JPanel(new BorderLayout(0, 12));
        pnlTop.setBackground(UITheme.BG_MAIN);

        JPanel banner = new UITheme.RoundedPanel(new GridLayout(0, 1, 2, 2), 14, UITheme.SURFACE, UITheme.BORDER);
        banner.setBorder(new EmptyBorder(12, 16, 12, 16));
        JLabel lblBannerTitle = new JLabel("PANDUAN PETUGAS POS SATPAM PENS PSDKU LAMONGAN");
        lblBannerTitle.setFont(UITheme.FONT_BADGE);
        lblBannerTitle.setForeground(UITheme.TEXT_MAIN);
        JLabel lblBannerSub = new JLabel("Cocokkan fisik kartu KTM asli mahasiswa dengan NIM & Nama Claimant, ambil foto serah terima, dan lakukan tanda tangan berita acara.");
        lblBannerSub.setFont(UITheme.FONT_SMALL);
        lblBannerSub.setForeground(UITheme.TEXT_MUTED);
        banner.add(lblBannerTitle);
        banner.add(lblBannerSub);

        JPanel pnlKpiMini = new JPanel(new GridLayout(1, 3, 14, 0));
        pnlKpiMini.setBackground(UITheme.BG_MAIN);
        lblPosProses = new JLabel("0 Klaim");
        lblPosTersedia = new JLabel("0 Barang");
        lblPosSelesai = new JLabel("0 Barang");

        pnlKpiMini.add(UITheme.buatKpiCard("Menunggu Verifikasi (PROSES_KLAIM)", lblPosProses,
                "• Perlu cek fisik KTM di pos satpam", true, null));
        pnlKpiMini.add(UITheme.buatKpiCard("Tersedia di Pos (TERSEDIA)", lblPosTersedia,
                "• Siap diklaim pemilik sah", false, UITheme.TEXT_MUTED));
        pnlKpiMini.add(UITheme.buatKpiCard("Selesai Diserahkan (SELESAI)", lblPosSelesai,
                "● Sinkron ke lost_found_data.csv", false, UITheme.SUCCESS_TEXT));

        pnlTop.add(banner, BorderLayout.NORTH);
        pnlTop.add(pnlKpiMini, BorderLayout.CENTER);

        // Main Split 2 Kolom
        JPanel pnlSplit = new JPanel(new BorderLayout(16, 0));
        pnlSplit.setBackground(UITheme.BG_MAIN);

        // Kiri: Antrean Klaim
        JPanel pnlKiri = UITheme.buatCardPanel();
        pnlKiri.setLayout(new BorderLayout(0, 10));
        pnlKiri.setPreferredSize(new Dimension(290, 0));

        JLabel lblAntrean = new JLabel("Antrean Klaim Aktif di Pos");
        lblAntrean.setFont(UITheme.FONT_SECTION);
        lblAntrean.setForeground(UITheme.TEXT_MAIN);

        modelListAntrean = new DefaultListModel<>();
        listAntreanKlaim = new JList<>(modelListAntrean);
        listAntreanKlaim.setFont(UITheme.FONT_REGULAR);
        listAntreanKlaim.setFixedCellHeight(38);
        listAntreanKlaim.setSelectionBackground(UITheme.PRIMARY_LIGHT);
        listAntreanKlaim.setSelectionForeground(UITheme.PRIMARY_DARK);
        listAntreanKlaim.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateDetailPosTerpilih();
            }
        });

        JScrollPane scrollList = new JScrollPane(listAntreanKlaim);
        scrollList.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_LIGHT, 1));

        pnlKiri.add(lblAntrean, BorderLayout.NORTH);
        pnlKiri.add(scrollList, BorderLayout.CENTER);

        // Kanan: Detail Pencocokan, Checklist, Foto Serah Terima & TTD
        JPanel pnlKanan = UITheme.buatCardPanel();
        pnlKanan.setLayout(new BorderLayout(0, 12));

        JLabel lblDetailTitle = new JLabel("Pencocokan Bukti & Validasi Fisik di Pos");
        lblDetailTitle.setFont(UITheme.FONT_SECTION);
        lblDetailTitle.setForeground(UITheme.TEXT_MAIN);

        JPanel pnlInfoItem = new JPanel(new GridLayout(0, 1, 4, 4));
        pnlInfoItem.setBackground(UITheme.SURFACE);

        lblDetailKlaimId = new JLabel("ID Barang: -");
        lblDetailKlaimId.setFont(UITheme.FONT_BOLD);
        lblDetailKlaimNama = new JLabel("Nama Barang: -");
        lblDetailKlaimNama.setFont(UITheme.FONT_BOLD);
        lblDetailKlaimKategori = new JLabel("Kategori: -");
        lblDetailKlaimLokasi = new JLabel("Lokasi Pos: -");
        lblDetailKlaimTanggal = new JLabel("Tanggal Ditemukan: -");
        lblDetailKlaimPemohon = new JLabel("Pemohon Klaim: -");
        lblDetailKlaimPemohon.setFont(UITheme.FONT_BOLD);
        lblDetailKlaimPemohon.setForeground(UITheme.PRIMARY_DARK);

        pnlInfoItem.add(lblDetailKlaimId);
        pnlInfoItem.add(lblDetailKlaimNama);
        pnlInfoItem.add(lblDetailKlaimKategori);
        pnlInfoItem.add(lblDetailKlaimLokasi);
        pnlInfoItem.add(lblDetailKlaimTanggal);
        pnlInfoItem.add(lblDetailKlaimPemohon);

        // Checklist Verifikasi Fisik
        JPanel pnlChecklist = new UITheme.RoundedPanel(
                new GridLayout(0, 1, 4, 4), 12, UITheme.SURFACE_SUBTLE, UITheme.BORDER);
        pnlChecklist.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel lblChecklistTitle = new JLabel("Checklist Verifikasi Fisik di Pos:");
        lblChecklistTitle.setFont(UITheme.FONT_BOLD);
        lblChecklistTitle.setForeground(UITheme.TEXT_MAIN);

        chkKtmDicek = new JCheckBox("KTM Mahasiswa fisik asli telah diperiksa & sah.");
        chkKtmDicek.setBackground(UITheme.SURFACE_SUBTLE);
        chkBuktiCocok = new JCheckBox("Bukti nomor seri / identitas & foto unik cocok dengan data sistem.");
        chkBuktiCocok.setBackground(UITheme.SURFACE_SUBTLE);
        chkBastDisetujui = new JCheckBox("Mahasiswa menandatangani Berita Acara Serah Terima.");
        chkBastDisetujui.setBackground(UITheme.SURFACE_SUBTLE);

        pnlChecklist.add(lblChecklistTitle);
        pnlChecklist.add(chkKtmDicek);
        pnlChecklist.add(chkBuktiCocok);
        pnlChecklist.add(chkBastDisetujui);

        // Row Bukti Serah Terima (2 Kotak: Foto Petugas + Kanvas TTD Mahasiswa)
        JPanel pnlEvidenceRow = new JPanel(new GridLayout(1, 2, 12, 0));
        pnlEvidenceRow.setBackground(UITheme.SURFACE);

        // Box 1: Foto Bukti Serah Terima (Petugas)
        JPanel boxFoto = new UITheme.RoundedPanel(
                new BorderLayout(0, 6), 12, UITheme.SURFACE_SUBTLE, UITheme.BORDER);
        boxFoto.setBorder(new EmptyBorder(10, 12, 10, 12));
        JLabel lblFotoTitle = new JLabel("Foto Bukti Serah Terima (Petugas)");
        lblFotoTitle.setFont(UITheme.FONT_BOLD);
        lblFotoSerahTerima = new JLabel("Belum ada foto (.jpg / .png)");
        lblFotoSerahTerima.setFont(UITheme.FONT_SMALL);
        lblFotoSerahTerima.setForeground(UITheme.TEXT_MUTED);
        JButton btnUnggahFoto = UITheme.buatTombolOutline("Unggah Foto", UITheme.TEXT_MAIN);
        btnUnggahFoto.setFont(UITheme.FONT_BADGE);
        btnUnggahFoto.addActionListener(e -> pilihFotoSerahTerima());

        JPanel pnlFotoFooter = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlFotoFooter.setOpaque(false);
        pnlFotoFooter.add(btnUnggahFoto);

        boxFoto.add(lblFotoTitle, BorderLayout.NORTH);
        boxFoto.add(lblFotoSerahTerima, BorderLayout.CENTER);
        boxFoto.add(pnlFotoFooter, BorderLayout.SOUTH);

        // Box 2: Tanda Tangan Serah Terima (Mahasiswa)
        JPanel boxTtd = new UITheme.RoundedPanel(
                new BorderLayout(0, 6), 12, UITheme.PRIMARY_LIGHT, UITheme.CORAL_BORDER);
        boxTtd.setBorder(new EmptyBorder(10, 12, 10, 12));
        JLabel lblTtdTitle = new JLabel("Tanda Tangan Serah Terima (Mahasiswa)");
        lblTtdTitle.setFont(UITheme.FONT_BOLD);
        lblTtdTitle.setForeground(UITheme.PRIMARY_DARK);
        lblStatusTtd = new JLabel("Belum ditandatangani");
        lblStatusTtd.setFont(UITheme.FONT_SMALL);
        lblStatusTtd.setForeground(UITheme.PRIMARY_DARK);
        JButton btnBukaTtd = UITheme.buatTombol("Buka Kanvas TTD", UITheme.CHARCOAL, Color.WHITE);
        btnBukaTtd.setFont(UITheme.FONT_BADGE);
        btnBukaTtd.addActionListener(e -> bukaKanvasTtd());

        JPanel pnlTtdFooter = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTtdFooter.setOpaque(false);
        pnlTtdFooter.add(btnBukaTtd);

        boxTtd.add(lblTtdTitle, BorderLayout.NORTH);
        boxTtd.add(lblStatusTtd, BorderLayout.CENTER);
        boxTtd.add(pnlTtdFooter, BorderLayout.SOUTH);

        pnlEvidenceRow.add(boxFoto);
        pnlEvidenceRow.add(boxTtd);

        // Stack Body Kanan
        JPanel pnlBodyKanan = new JPanel();
        pnlBodyKanan.setLayout(new BoxLayout(pnlBodyKanan, BoxLayout.Y_AXIS));
        pnlBodyKanan.setBackground(UITheme.SURFACE);

        pnlInfoItem.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlChecklist.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlEvidenceRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlBodyKanan.add(pnlInfoItem);
        pnlBodyKanan.add(Box.createVerticalStrut(10));
        pnlBodyKanan.add(pnlChecklist);
        pnlBodyKanan.add(Box.createVerticalStrut(10));
        pnlBodyKanan.add(pnlEvidenceRow);

        // Tombol Konfirmasi Pos
        JPanel pnlAksiPos = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlAksiPos.setBackground(UITheme.SURFACE);

        JButton btnPosTolak = UITheme.buatTombol("Tolak / Batalkan Klaim", UITheme.DANGER_BG, UITheme.DANGER_TEXT);
        btnPosTolak.addActionListener(e -> {
            if (itemTerpilihPos != null) {
                eksekusiBatalkanKlaim(itemTerpilihPos.getId());
            }
        });

        JButton btnPosSerahTerima = UITheme.buatTombol("Konfirmasi Serah Terima & Selesai", UITheme.CHARCOAL, Color.WHITE);
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

        pnlKanan.add(lblDetailTitle, BorderLayout.NORTH);
        pnlKanan.add(pnlBodyKanan, BorderLayout.CENTER);
        pnlKanan.add(pnlAksiPos, BorderLayout.SOUTH);

        pnlSplit.add(pnlKiri, BorderLayout.WEST);
        pnlSplit.add(pnlKanan, BorderLayout.CENTER);

        p.add(pnlTop, BorderLayout.NORTH);
        p.add(pnlSplit, BorderLayout.CENTER);
        return p;
    }

    private void pilihFotoSerahTerima() {
        if (itemTerpilihPos == null) {
            JOptionPane.showMessageDialog(this, "Pilih klaim pada antrean di sebelah kiri terlebih dahulu.",
                    "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Pilih Foto Bukti Serah Terima Barang & KTM");
        fc.setFileFilter(new FileNameExtensionFilter("Gambar (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            lblFotoSerahTerima.setText("Terlampir: " + fc.getSelectedFile().getName());
            lblFotoSerahTerima.setForeground(UITheme.SUCCESS_TEXT);
        }
    }

    private void bukaKanvasTtd() {
        if (itemTerpilihPos == null) {
            JOptionPane.showMessageDialog(this, "Pilih klaim pada antrean di sebelah kiri terlebih dahulu.",
                    "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        SignatureDialog dialog = new SignatureDialog(this, itemTerpilihPos);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            ttdSudahDitandatangani = true;
            lblStatusTtd.setText("● Tanda tangan tersimpan (" + itemTerpilihPos.getId() + ")");
            lblStatusTtd.setForeground(UITheme.SUCCESS_TEXT);
            chkBastDisetujui.setSelected(true);
        }
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
            lblDetailKlaimPemohon.setText("Pemohon Klaim: -");
            lblFotoSerahTerima.setText("Belum ada foto (.jpg / .png)");
            lblFotoSerahTerima.setForeground(UITheme.TEXT_MUTED);
            lblStatusTtd.setText("Belum ditandatangani");
            lblStatusTtd.setForeground(UITheme.PRIMARY_DARK);
            ttdSudahDitandatangani = false;
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
            Claimant pemohon = itemTerpilihPos.getClaimant();
            if (pemohon != null) {
                lblDetailKlaimPemohon.setText("Pemohon Klaim: " + pemohon.getNama()
                        + " (NIM: " + pemohon.getNim() + " | WA: " + pemohon.getNoHp() + ")");
            } else {
                lblDetailKlaimPemohon.setText("Pemohon Klaim: (Data sesi belum tercatat / dimuat dari CSV)");
            }
            chkKtmDicek.setSelected(false);
            chkBuktiCocok.setSelected(false);
            chkBastDisetujui.setSelected(false);
            ttdSudahDitandatangani = false;
            lblStatusTtd.setText("Belum ditandatangani");
            lblStatusTtd.setForeground(UITheme.PRIMARY_DARK);
        }
    }

    // =========================================================================
    // 7. LAYAR ADMIN 3: SEMUA INVENTARIS (SCREEN 7 FINEXY)
    // =========================================================================
    private JPanel buatLayarAdminKelolaBarang() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setBackground(UITheme.BG_MAIN);
        p.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel pnlToolbar = UITheme.buatCardPanel();
        pnlToolbar.setLayout(new BorderLayout(10, 0));

        JPanel pnlKiri = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlKiri.setBackground(UITheme.SURFACE);

        JLabel lblCari = new JLabel("Cari Barang / ID:");
        lblCari.setFont(UITheme.FONT_BOLD);
        txtAdminCariKelola = UITheme.buatTextField(16);

        cbAdminFilterStatus = new JComboBox<>(new String[]{"SEMUA", "TERSEDIA", "PROSES_KLAIM", "SELESAI"});
        cbAdminFilterStatus.setFont(UITheme.FONT_REGULAR);
        cbAdminFilterStatus.setBackground(UITheme.SURFACE);

        JButton btnCari = UITheme.buatTombol("Filter", UITheme.CHARCOAL, Color.WHITE);
        btnCari.addActionListener(e -> filterTabelKelolaAdmin());

        pnlKiri.add(lblCari);
        pnlKiri.add(txtAdminCariKelola);
        pnlKiri.add(cbAdminFilterStatus);
        pnlKiri.add(btnCari);

        JButton btnInputPetugas = UITheme.buatTombol("+ Input Temuan (Petugas)", UITheme.CHARCOAL, Color.WHITE);
        btnInputPetugas.addActionListener(e -> bukaDialogLaporBarang());

        pnlToolbar.add(pnlKiri, BorderLayout.WEST);
        pnlToolbar.add(btnInputPetugas, BorderLayout.EAST);

        JPanel cardTabel = UITheme.buatCardPanel();
        cardTabel.setLayout(new BorderLayout(0, 10));

        JLabel lblSub = new JLabel("Semua Inventaris Internal (Atribut Rahasia Terlihat Khusus Petugas • Sumber: data/lost_found_data.csv)");
        lblSub.setFont(UITheme.FONT_SMALL);
        lblSub.setForeground(UITheme.SUCCESS_TEXT);

        String[] col = {"ID", "Kategori", "Nama Barang", "Lokasi", "Atribut Rahasia (SN / NIM)", "Tanggal", "Status"};
        modelTabelAdminKelola = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelAdminKelola = new JTable(modelTabelAdminKelola);
        UITheme.formatTable(tabelAdminKelola);
        aturLebarKolomStandar(tabelAdminKelola);

        JScrollPane scroll = new JScrollPane(tabelAdminKelola);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_LIGHT, 1));
        scroll.getViewport().setBackground(UITheme.SURFACE);

        JPanel pnlBawah = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlBawah.setBackground(UITheme.SURFACE);
        JButton btnHapus = UITheme.buatTombol("Hapus Barang Terpilih", UITheme.DANGER_BG, UITheme.DANGER_TEXT);
        btnHapus.addActionListener(e -> hapusBarangTerpilih());
        pnlBawah.add(btnHapus);

        cardTabel.add(lblSub, BorderLayout.NORTH);
        cardTabel.add(scroll, BorderLayout.CENTER);
        cardTabel.add(pnlBawah, BorderLayout.SOUTH);

        p.add(pnlToolbar, BorderLayout.NORTH);
        p.add(cardTabel, BorderLayout.CENTER);
        return p;
    }

    private String formatAtributRahasia(Item item) {
        if (item instanceof BarangElektronik) {
            BarangElektronik e = (BarangElektronik) item;
            return "SN: " + e.getNomorSeri() + " | Brand: " + e.getBrand();
        } else if (item instanceof BarangDokumen) {
            BarangDokumen d = (BarangDokumen) item;
            return "NIM: " + d.getNomorIdentitas() + " | Pemilik: " + d.getNamaPemilik();
        }
        return "-";
    }

    private void filterTabelKelolaAdmin() {
        String keyword = txtAdminCariKelola.getText().trim().toLowerCase();
        String status = (String) cbAdminFilterStatus.getSelectedItem();
        boolean filterStatus = status != null && !status.equals("SEMUA");

        modelTabelAdminKelola.setRowCount(0);
        for (Item item : manager.getAllItems()) {
            boolean matchKey = keyword.isEmpty()
                    || item.getId().toLowerCase().contains(keyword)
                    || item.getNama().toLowerCase().contains(keyword)
                    || item.getDeskripsi().toLowerCase().contains(keyword)
                    || item.getLokasiDitemukan().toLowerCase().contains(keyword);
            boolean matchStat = !filterStatus || item.getStatus().equalsIgnoreCase(status);

            if (matchKey && matchStat) {
                modelTabelAdminKelola.addRow(new Object[]{
                        item.getId(), item.getKategori(), item.getNama(),
                        item.getLokasiDitemukan(), formatAtributRahasia(item),
                        item.getTanggalDitemukan(), item.getStatus()
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

    // =========================================================================
    // 8. LAYAR ADMIN 4: RIWAYAT SELESAI (SCREEN 8 FINEXY)
    // =========================================================================
    private JPanel buatLayarAdminRiwayatSelesai() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setBackground(UITheme.BG_MAIN);
        p.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel card = UITheme.buatCardPanel();
        card.setLayout(new BorderLayout(0, 12));

        JPanel header = new JPanel(new GridLayout(0, 1, 2, 2));
        header.setBackground(UITheme.SURFACE);
        JLabel lblTitle = new JLabel("Arsip Riwayat Serah Terima Selesai (Status: SELESAI)");
        lblTitle.setFont(UITheme.FONT_SECTION);
        lblTitle.setForeground(UITheme.TEXT_MAIN);
        JLabel lblSub = new JLabel("Catatan barang yang telah diverifikasi petugas dan diserahkan kepada pemilik sah.");
        lblSub.setFont(UITheme.FONT_SMALL);
        lblSub.setForeground(UITheme.TEXT_MUTED);
        header.add(lblTitle);
        header.add(lblSub);

        String[] col = {"ID Tiket", "Kategori", "Nama Barang", "Lokasi Ditemukan", "Penerima (Sesi)", "Tanggal", "Status"};
        modelTabelRiwayatSelesai = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelRiwayatSelesai = new JTable(modelTabelRiwayatSelesai);
        UITheme.formatTable(tabelRiwayatSelesai);
        aturLebarKolomStandar(tabelRiwayatSelesai);

        JScrollPane scroll = new JScrollPane(tabelRiwayatSelesai);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_LIGHT, 1));
        scroll.getViewport().setBackground(UITheme.SURFACE);

        card.add(header, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        p.add(card, BorderLayout.CENTER);
        return p;
    }

    // =========================================================================
    // 9. AKSI ADMIN & SINKRONISASI DATA
    // =========================================================================
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

    public void refreshSemuaData() {
        ArrayList<Item> semua = manager.getAllItems();

        int countTotal = semua.size();
        int countTersedia = manager.countByStatus(Item.STATUS_TERSEDIA);
        int countProses = manager.countByStatus(Item.STATUS_PROSES_KLAIM);
        int countSelesai = manager.countByStatus(Item.STATUS_SELESAI);

        // KPI Mahasiswa
        lblHeroTersedia.setText(countTersedia + " Barang");
        lblHeroProses.setText(countProses + " Barang");
        lblHeroSelesai.setText(countSelesai + " Barang");

        // KPI Petugas Dashboard & Verifikasi Pos
        lblStatTotal.setText(String.valueOf(countTotal));
        lblStatTersedia.setText(String.valueOf(countTersedia));
        lblStatProsesKlaim.setText(String.valueOf(countProses));
        lblStatSelesai.setText(String.valueOf(countSelesai));

        lblPosProses.setText(countProses + " Klaim");
        lblPosTersedia.setText(countTersedia + " Barang");
        lblPosSelesai.setText(countSelesai + " Barang");

        // Dropdown lokasi unik
        Set<String> lokasiSet = new HashSet<>();
        for (Item item : semua) {
            if (item.getLokasiDitemukan() != null && !item.getLokasiDitemukan().trim().isEmpty()) {
                lokasiSet.add(item.getLokasiDitemukan().trim());
            }
        }
        syncComboLokasi(cbFilterLokasi, lokasiSet);
        syncComboLokasi(cbFilterLokasiLapor, lokasiSet);

        terapkanFilterJelajah();
        terapkanFilterLapor();

        // Antrean Klaim Admin & Riwayat Selesai
        modelTabelAdminKlaim.setRowCount(0);
        modelListAntrean.clear();
        modelTabelRiwayatSelesai.setRowCount(0);

        for (Item item : semua) {
            if (Item.STATUS_PROSES_KLAIM.equals(item.getStatus())) {
                modelTabelAdminKlaim.addRow(new Object[]{
                        item.getId(), item.getKategori(), item.getNama(),
                        item.getDeskripsi(), item.getLokasiDitemukan(), item.getTanggalDitemukan(), item.getStatus()
                });
                modelListAntrean.addElement(item.getId() + " - " + item.getNama() + " (" + item.getLokasiDitemukan() + ")");
            } else if (Item.STATUS_SELESAI.equals(item.getStatus())) {
                Claimant c = item.getClaimant();
                String penerima = c != null ? c.getNama() + " (" + c.getNim() + ")" : "Terverifikasi di Pos";
                modelTabelRiwayatSelesai.addRow(new Object[]{
                        item.getId(), item.getKategori(), item.getNama(),
                        item.getLokasiDitemukan(), penerima, item.getTanggalDitemukan(), item.getStatus()
                });
            }
        }

        filterTabelKelolaAdmin();
        updateDetailPosTerpilih();
    }

    private void syncComboLokasi(JComboBox<String> combo, Set<String> lokasiSet) {
        if (combo == null) return;
        String terpilih = (String) combo.getSelectedItem();
        combo.removeAllItems();
        combo.addItem("Semua Lokasi Kampus");
        for (String lok : lokasiSet) {
            combo.addItem(lok);
        }
        if (terpilih != null) {
            combo.setSelectedItem(terpilih);
        }
    }
}
