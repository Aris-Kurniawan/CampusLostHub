package id.ac.pens.psdku.campuslosthub.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Tema dan utility visual antarmuka Swing modern untuk CampusLostHub.
 * Mengadopsi palet warna modern (Tailwind / Material style) sesuai desain sistem.
 */
public class UITheme {

    // Palet Warna
    public static final Color PRIMARY = new Color(37, 99, 235);         // Royal Blue
    public static final Color PRIMARY_DARK = new Color(29, 78, 216);    // Darker Blue
    public static final Color PRIMARY_LIGHT = new Color(239, 246, 255); // Soft Blue 50
    public static final Color BG_MAIN = new Color(248, 250, 252);       // Slate 50
    public static final Color SURFACE = Color.WHITE;
    public static final Color BORDER = new Color(226, 232, 240);        // Slate 200
    public static final Color TEXT_MAIN = new Color(15, 23, 42);        // Slate 900
    public static final Color TEXT_MUTED = new Color(100, 116, 139);    // Slate 500

    public static final Color SUCCESS_TEXT = new Color(22, 101, 52);    // Green 800
    public static final Color SUCCESS_BG = new Color(220, 252, 231);    // Green 100
    public static final Color WARNING_TEXT = new Color(146, 64, 14);    // Amber 800
    public static final Color WARNING_BG = new Color(254, 243, 199);    // Amber 100
    public static final Color DANGER_TEXT = new Color(153, 27, 27);     // Red 800
    public static final Color DANGER_BG = new Color(254, 226, 226);     // Red 100
    public static final Color BADGE_DOKUMEN_BG = new Color(243, 232, 255); // Purple 100
    public static final Color BADGE_DOKUMEN_TEXT = new Color(107, 33, 168); // Purple 800

    // Tipografi (Segoe UI sebagai font default di Windows)
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BADGE = new Font("Segoe UI", Font.BOLD, 11);

    /** Membuat panel kartu putih dengan border halus */
    public static JPanel buatCardPanel() {
        JPanel p = new JPanel();
        p.setBackground(SURFACE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(16, 16, 16, 16)
        ));
        return p;
    }

    /** Tombol modern bergaya pill/flat dengan jaminan kontras tinggi */
    public static JButton buatTombol(String teks, Color bg, Color fg) {
        JButton btn = new JButton(teks);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(FONT_BOLD);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker(), 1),
                new EmptyBorder(9, 18, 9, 18)
        ));
        return btn;
    }

    /** Tombol outline halus dengan background putih solid */
    public static JButton buatTombolOutline(String teks, Color warna) {
        JButton btn = new JButton(teks);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(FONT_BOLD);
        btn.setBackground(SURFACE);
        btn.setForeground(warna);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(warna, 1),
                new EmptyBorder(8, 16, 8, 16)
        ));
        return btn;
    }

    /** TextField modern dengan padding */
    public static JTextField buatTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(FONT_REGULAR);
        tf.setForeground(TEXT_MAIN);
        tf.setCaretColor(PRIMARY);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
        return tf;
    }

    /** Kartu statistik ringkas dengan angka besar */
    public static JPanel buatStatCard(String judul, String angka, Color warnaAksen) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel lblJudul = new JLabel(judul.toUpperCase());
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblJudul.setForeground(TEXT_MUTED);

        JLabel lblAngka = new JLabel(angka);
        lblAngka.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblAngka.setForeground(warnaAksen);

        card.add(lblJudul, BorderLayout.NORTH);
        card.add(lblAngka, BorderLayout.CENTER);
        return card;
    }

    /** Mengaplikasikan styling modern pada JTable */
    public static void formatTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(36);
        table.setGridColor(BORDER);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(TEXT_MAIN);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(new Color(241, 245, 249)); // Slate 100
        header.setForeground(new Color(71, 85, 105));   // Slate 600
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setReorderingAllowed(false);

        // Renderer kolom khusus
        table.setDefaultRenderer(Object.class, new ModernTableCellRenderer());
    }

    /** Renderer sel kustom untuk badge kategori, status, dan alignment */
    public static class ModernTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            lbl.setBorder(new EmptyBorder(0, 10, 0, 10));
            String str = value == null ? "" : value.toString();

            if (!isSelected) {
                lbl.setBackground(SURFACE);
            }

            // Kolom ID (kolom 0)
            if (column == 0) {
                lbl.setFont(FONT_BOLD);
                lbl.setForeground(isSelected ? TEXT_MAIN : PRIMARY);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                return lbl;
            }

            // Kategori (ELEKTRONIK / DOKUMEN)
            if ("ELEKTRONIK".equalsIgnoreCase(str) || "DOKUMEN".equalsIgnoreCase(str)) {
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(FONT_BADGE);
                if ("ELEKTRONIK".equalsIgnoreCase(str)) {
                    lbl.setText(" ELEKTRONIK ");
                    if (!isSelected) {
                        lbl.setForeground(new Color(2, 132, 199));
                        lbl.setBackground(new Color(224, 242, 254));
                    }
                } else {
                    lbl.setText(" DOKUMEN ");
                    if (!isSelected) {
                        lbl.setForeground(BADGE_DOKUMEN_TEXT);
                        lbl.setBackground(BADGE_DOKUMEN_BG);
                    }
                }
                return lbl;
            }

            // Status (TERSEDIA / PROSES_KLAIM / SELESAI)
            if ("TERSEDIA".equalsIgnoreCase(str)
                    || "PROSES_KLAIM".equalsIgnoreCase(str)
                    || "SELESAI".equalsIgnoreCase(str)) {
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(FONT_BADGE);

                if ("TERSEDIA".equalsIgnoreCase(str)) {
                    lbl.setText("● TERSEDIA");
                    if (!isSelected) {
                        lbl.setForeground(SUCCESS_TEXT);
                        lbl.setBackground(SUCCESS_BG);
                    }
                } else if ("PROSES_KLAIM".equalsIgnoreCase(str)) {
                    lbl.setText("● PROSES KLAIM");
                    if (!isSelected) {
                        lbl.setForeground(WARNING_TEXT);
                        lbl.setBackground(WARNING_BG);
                    }
                } else {
                    lbl.setText("● SELESAI");
                    if (!isSelected) {
                        lbl.setForeground(new Color(15, 118, 110));
                        lbl.setBackground(new Color(204, 251, 241));
                    }
                }
                return lbl;
            }

            lbl.setFont(FONT_REGULAR);
            lbl.setForeground(TEXT_MAIN);
            lbl.setHorizontalAlignment(SwingConstants.LEFT);
            return lbl;
        }
    }
}
