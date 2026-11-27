package id.ac.pens.psdku.campuslosthub.view;

import id.ac.pens.psdku.campuslosthub.exception.InvalidClaimException;
import id.ac.pens.psdku.campuslosthub.model.Claimant;
import id.ac.pens.psdku.campuslosthub.model.Item;
import id.ac.pens.psdku.campuslosthub.service.LostAndFoundManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 * Modal Dialog "Ajukan Klaim Barang" sesuai desain FigCraft (Screen 2).
 * Mengeliminasi input ID manual karena ID dan data barang sudah terisi otomatis,
 * serta menyediakan unggah foto/bukti unik opsional sebagai lampiran pendukung.
 */
public class ClaimDialog extends JDialog {

    private final LostAndFoundManager manager;
    private final Item item;
    private boolean sukses = false;

    private JTextField txtNama;
    private JTextField txtNim;
    private JTextField txtNoHp;
    private JTextField txtBukti;
    private JLabel lblFotoUnik;
    private JCheckBox chkSetuju;

    public ClaimDialog(Frame owner, LostAndFoundManager manager, Item item) {
        super(owner, "Ajukan Klaim Kepemilikan Barang", true);
        this.manager = manager;
        this.item = item;

        setSize(540, 650);
        setLocationRelativeTo(owner);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel contentPane = new JPanel(new BorderLayout(14, 14));
        contentPane.setBackground(UITheme.BG_MAIN);
        contentPane.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Header Dialog
        JPanel headerPanel = new JPanel(new BorderLayout(5, 4));
        headerPanel.setBackground(UITheme.BG_MAIN);

        JLabel lblTitle = new JLabel("Ajukan Klaim Barang");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.TEXT_MAIN);

        JLabel lblSub = new JLabel("Verifikasi kepemilikan barang sebelum mengambil di Pos Keamanan Kampus");
        lblSub.setFont(UITheme.FONT_SMALL);
        lblSub.setForeground(UITheme.TEXT_MUTED);

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.CENTER);

        // Preview Card Barang Terpilih (Soft Coral Callout)
        JPanel cardPreview = new UITheme.RoundedPanel(
                new BorderLayout(10, 6), 14, UITheme.PRIMARY_LIGHT, UITheme.CORAL_BORDER);
        cardPreview.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel lblItemNama = new JLabel(item.getNama());
        lblItemNama.setFont(UITheme.FONT_BOLD);
        lblItemNama.setForeground(UITheme.PRIMARY_DARK);

        JPanel pnlInfoPreview = new JPanel(new GridLayout(0, 1, 2, 2));
        pnlInfoPreview.setOpaque(false);

        JLabel lblItemLokasi = new JLabel("Lokasi: " + item.getLokasiDitemukan() + " (" + item.getKategori() + ")");
        lblItemLokasi.setFont(UITheme.FONT_SMALL);
        lblItemLokasi.setForeground(UITheme.TEXT_SUBTLE);

        JLabel lblItemDeskripsi = new JLabel("Deskripsi: " + item.getDeskripsi());
        lblItemDeskripsi.setFont(UITheme.FONT_SMALL);
        lblItemDeskripsi.setForeground(UITheme.TEXT_SUBTLE);

        pnlInfoPreview.add(lblItemLokasi);
        pnlInfoPreview.add(lblItemDeskripsi);

        JLabel lblBadgeId = new JLabel("  " + item.getId() + "  ");
        lblBadgeId.setFont(UITheme.FONT_BADGE);
        lblBadgeId.setOpaque(true);
        lblBadgeId.setBackground(UITheme.PRIMARY);
        lblBadgeId.setForeground(Color.WHITE);

        cardPreview.add(lblItemNama, BorderLayout.NORTH);
        cardPreview.add(pnlInfoPreview, BorderLayout.CENTER);
        cardPreview.add(lblBadgeId, BorderLayout.EAST);

        // Form Fields Container
        JPanel formCard = UITheme.buatCardPanel();
        formCard.setLayout(new GridLayout(0, 1, 6, 6));

        txtNama = UITheme.buatTextField(20);
        txtNim = UITheme.buatTextField(20);
        txtNoHp = UITheme.buatTextField(20);
        txtBukti = UITheme.buatTextField(20);

        String petunjukBukti = "ELEKTRONIK".equalsIgnoreCase(item.getKategori())
                ? "Serial Number / IMEI perangkat"
                : "NIM atau Nama Lengkap pada dokumen";

        formCard.add(createFieldLabel("Nama Lengkap Pemohon *"));
        formCard.add(txtNama);
        formCard.add(createFieldLabel("NRP / NIM (10 Digit Angka) *"));
        formCard.add(txtNim);
        formCard.add(createFieldLabel("Nomor WhatsApp / HP Aktif (08...) *"));
        formCard.add(txtNoHp);
        formCard.add(createFieldLabel("Bukti Rahasia (" + petunjukBukti + ") *"));
        formCard.add(txtBukti);

        // Upload Foto / Bukti Unik (Opsional) - Screen 2 FigCraft
        formCard.add(createFieldLabel("Foto / Bukti Kepemilikan Unik (Opsional)"));
        JPanel pnlFoto = new UITheme.RoundedPanel(
                new BorderLayout(10, 0), 10, UITheme.SURFACE_SUBTLE, UITheme.BORDER);
        pnlFoto.setBorder(new EmptyBorder(8, 12, 8, 12));

        lblFotoUnik = new JLabel("Belum ada file dipilih (.jpg / .png)");
        lblFotoUnik.setFont(UITheme.FONT_SMALL);
        lblFotoUnik.setForeground(UITheme.TEXT_MUTED);

        JButton btnPilihFoto = UITheme.buatTombolOutline("Pilih Foto", UITheme.TEXT_MAIN);
        btnPilihFoto.setFont(UITheme.FONT_BADGE);
        btnPilihFoto.addActionListener(e -> pilihFotoBukti());

        pnlFoto.add(lblFotoUnik, BorderLayout.CENTER);
        pnlFoto.add(btnPilihFoto, BorderLayout.EAST);
        formCard.add(pnlFoto);

        chkSetuju = new JCheckBox("Saya bersedia menunjukkan kartu identitas fisik asli (KTM) di Pos Satpam.");
        chkSetuju.setFont(UITheme.FONT_SMALL);
        chkSetuju.setBackground(UITheme.SURFACE);
        chkSetuju.setForeground(UITheme.TEXT_MUTED);
        formCard.add(chkSetuju);

        // Tombol Aksi Bawah
        JPanel pnlTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlTombol.setBackground(UITheme.BG_MAIN);

        JButton btnBatal = UITheme.buatTombolOutline("Batal", UITheme.TEXT_MAIN);
        btnBatal.addActionListener(e -> dispose());

        JButton btnKirim = UITheme.buatTombol("Verifikasi & Ajukan Klaim", UITheme.CHARCOAL, Color.WHITE);
        btnKirim.addActionListener(e -> prosesKlaim());

        pnlTombol.add(btnBatal);
        pnlTombol.add(btnKirim);

        // Gabungkan ke Content Pane
        JPanel pnlTengah = new JPanel(new BorderLayout(0, 12));
        pnlTengah.setBackground(UITheme.BG_MAIN);
        pnlTengah.add(cardPreview, BorderLayout.NORTH);
        pnlTengah.add(formCard, BorderLayout.CENTER);

        JScrollPane scrollTengah = new JScrollPane(pnlTengah);
        scrollTengah.setBorder(null);
        scrollTengah.getViewport().setBackground(UITheme.BG_MAIN);
        scrollTengah.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTengah.getVerticalScrollBar().setUnitIncrement(16);

        contentPane.add(headerPanel, BorderLayout.NORTH);
        contentPane.add(scrollTengah, BorderLayout.CENTER);
        contentPane.add(pnlTombol, BorderLayout.SOUTH);

        setContentPane(contentPane);
    }

    private void pilihFotoBukti() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Pilih Foto / Bukti Kepemilikan Unik");
        fc.setFileFilter(new FileNameExtensionFilter("Gambar (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            lblFotoUnik.setText("Terpilih: " + fc.getSelectedFile().getName());
            lblFotoUnik.setForeground(UITheme.SUCCESS_TEXT);
        }
    }

    private JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_BOLD);
        lbl.setForeground(UITheme.TEXT_MAIN);
        return lbl;
    }

    private void prosesKlaim() {
        String nama = txtNama.getText().trim();
        String nim = txtNim.getText().trim();
        String noHp = txtNoHp.getText().trim();
        String bukti = txtBukti.getText().trim();

        if (nama.isEmpty()) {
            tampilkanError("Nama pemohon wajib diisi.");
            txtNama.requestFocus();
            return;
        }
        if (!nim.matches("\\d{10}")) {
            tampilkanError("NIM harus tepat 10 digit angka (contoh: 3125521024).");
            txtNim.requestFocus();
            return;
        }
        if (!ViewUtil.noHpValid(noHp)) {
            tampilkanError("Nomor HP harus diawali '08' dan terdiri dari 10-13 digit angka.");
            txtNoHp.requestFocus();
            return;
        }
        if (bukti.isEmpty()) {
            tampilkanError("Bukti rahasia kepemilikan wajib diisi.");
            txtBukti.requestFocus();
            return;
        }
        if (!chkSetuju.isSelected()) {
            tampilkanError("Silakan centang persetujuan menunjukkan kartu identitas fisik di pos.");
            chkSetuju.requestFocus();
            return;
        }

        try {
            Claimant claimant = new Claimant(nama, nim, noHp);
            manager.claimItem(item.getId(), claimant, bukti);

            sukses = true;
            JOptionPane.showMessageDialog(this,
                    "Klaim berhasil diverifikasi!\n"
                            + "Status barang " + item.getId() + " kini: PROSES_KLAIM.\n\n"
                            + "Silakan datang ke Pos Keamanan Kampus dengan membawa identitas fisik asli (KTM).",
                    "Klaim Diterima", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (InvalidClaimException ex) {
            JOptionPane.showMessageDialog(this,
                    "Klaim Ditolak: " + ex.getMessage(),
                    "Verifikasi Gagal", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Terjadi kesalahan saat memproses klaim: " + ex.getMessage(),
                    "Error Sistem", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tampilkanError(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Validasi Input", JOptionPane.WARNING_MESSAGE);
    }

    public boolean isSukses() {
        return sukses;
    }
}
