package id.ac.pens.psdku.campuslosthub.view;

import id.ac.pens.psdku.campuslosthub.model.Claimant;
import id.ac.pens.psdku.campuslosthub.model.Item;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Modal Dialog Kanvas Tanda Tangan Serah Terima (Screen 10 pada FigCraft).
 * Digunakan petugas saat serah terima barang fisik di pos agar mahasiswa
 * membubuhkan tanda tangan persetujuan secara langsung.
 */
public class SignatureDialog extends JDialog {

    private final Item item;
    private boolean saved = false;
    private final List<List<Point>> strokes = new ArrayList<>();
    private List<Point> currentStroke = null;
    private JPanel canvasPanel;
    private JCheckBox chkSetuju;

    public SignatureDialog(Frame owner, Item item) {
        super(owner, "Tanda Tangan Serah Terima (" + item.getId() + ")", true);
        this.item = item;

        setSize(500, 460);
        setLocationRelativeTo(owner);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setBackground(UITheme.SURFACE);
        root.setBorder(new EmptyBorder(24, 24, 20, 24));

        // Header
        JPanel header = new JPanel(new GridLayout(0, 1, 4, 4));
        header.setBackground(UITheme.SURFACE);

        JLabel lblTitle = new JLabel("Tanda Tangan Serah Terima (" + item.getId() + ")");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.TEXT_MAIN);

        Claimant c = item.getClaimant();
        String penerima = c != null
                ? "Penerima: " + c.getNama() + " (NIM: " + c.getNim() + ") • " + item.getNama()
                : "Barang: " + item.getNama() + " (" + item.getLokasiDitemukan() + ")";
        JLabel lblSub = new JLabel(penerima);
        lblSub.setFont(UITheme.FONT_SMALL);
        lblSub.setForeground(UITheme.TEXT_MUTED);

        header.add(lblTitle);
        header.add(lblSub);

        // Kanvas TTD Interaktif
        canvasPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (strokes.isEmpty()) {
                    g2.setColor(new Color(156, 163, 175));
                    g2.setFont(UITheme.FONT_REGULAR);
                    String hint = "Goreskan tanda tangan mahasiswa di dalam kotak ini";
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(hint)) / 2;
                    int y = (getHeight() + fm.getAscent()) / 2;
                    g2.drawString(hint, x, y);
                } else {
                    g2.setColor(UITheme.CHARCOAL);
                    g2.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    for (List<Point> stroke : strokes) {
                        for (int i = 1; i < stroke.size(); i++) {
                            Point p1 = stroke.get(i - 1);
                            Point p2 = stroke.get(i);
                            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                        }
                    }
                }
                g2.dispose();
            }
        };
        canvasPanel.setBackground(UITheme.SURFACE_SUBTLE);
        canvasPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        canvasPanel.setBorder(BorderFactory.createLineBorder(UITheme.CHARCOAL, 1));

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentStroke = new ArrayList<>();
                currentStroke.add(e.getPoint());
                strokes.add(currentStroke);
                canvasPanel.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentStroke != null) {
                    currentStroke.add(e.getPoint());
                    canvasPanel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentStroke = null;
            }
        };
        canvasPanel.addMouseListener(mouseHandler);
        canvasPanel.addMouseMotionListener(mouseHandler);

        // Checkbox Persetujuan
        chkSetuju = new JCheckBox("<html>Saya menyatakan telah menerima barang milik saya dalam kondisi baik dan sesuai.</html>");
        chkSetuju.setFont(UITheme.FONT_SMALL);
        chkSetuju.setBackground(UITheme.SURFACE);
        chkSetuju.setForeground(UITheme.TEXT_SUBTLE);

        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBackground(UITheme.SURFACE);
        pnlCenter.add(canvasPanel, BorderLayout.CENTER);
        pnlCenter.add(chkSetuju, BorderLayout.SOUTH);

        // Footer Tombol Aksi
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(UITheme.SURFACE);

        JButton btnUlangi = UITheme.buatTombolOutline("↺ Ulangi", UITheme.TEXT_MUTED);
        btnUlangi.addActionListener(e -> {
            strokes.clear();
            canvasPanel.repaint();
        });

        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pnlRight.setBackground(UITheme.SURFACE);

        JButton btnBatal = UITheme.buatTombolOutline("Batal", UITheme.TEXT_MAIN);
        btnBatal.addActionListener(e -> dispose());

        JButton btnSimpan = UITheme.buatTombol("Simpan Tanda Tangan", UITheme.CHARCOAL, Color.WHITE);
        btnSimpan.addActionListener(e -> {
            if (strokes.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Mohon goreskan tanda tangan mahasiswa terlebih dahulu.",
                        "Tanda Tangan Kosong", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!chkSetuju.isSelected()) {
                JOptionPane.showMessageDialog(this,
                        "Harap centang pernyataan penerimaan barang terlebih dahulu.",
                        "Persetujuan Belum Dicentang", JOptionPane.WARNING_MESSAGE);
                return;
            }
            saved = true;
            dispose();
        });

        pnlRight.add(btnBatal);
        pnlRight.add(btnSimpan);

        footer.add(btnUlangi, BorderLayout.WEST);
        footer.add(pnlRight, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);
        root.add(pnlCenter, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    public boolean isSaved() {
        return saved;
    }
}
