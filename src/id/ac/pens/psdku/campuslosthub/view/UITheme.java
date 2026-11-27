package id.ac.pens.psdku.campuslosthub.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Tema dan utility visual antarmuka Swing CampusLostHub.
 * Mengadopsi Finexy Design System (#F4F5F7 canvas, #FFFFFF surface,
 * #171717 charcoal pill CTA, #F05423 coral focal accent).
 */
public class UITheme {

    // Palet Warna Finexy / FigCraft
    public static final Color PRIMARY = new Color(240, 84, 35);         // Coral Accent (#F05423)
    public static final Color PRIMARY_DARK = new Color(154, 52, 18);    // Coral Dark (#9A3412)
    public static final Color PRIMARY_LIGHT = new Color(255, 247, 237); // Soft Coral (#FFF7ED)
    public static final Color CORAL_BORDER = new Color(253, 186, 116);  // Coral Border (#FDBA74)

    public static final Color CHARCOAL = new Color(23, 23, 23);         // Dark CTA / Active Pill (#171717)
    public static final Color BG_MAIN = new Color(244, 245, 247);       // Canvas (#F4F5F7)
    public static final Color SURFACE = Color.WHITE;                    // Card Surface (#FFFFFF)
    public static final Color SURFACE_SUBTLE = new Color(249, 250, 251);// Subtle Surface (#F9FAFB)
    public static final Color BORDER = new Color(229, 231, 235);        // Neutral Border (#E5E7EB)
    public static final Color BORDER_LIGHT = new Color(243, 244, 246);  // Light Row Border (#F3F4F6)
    public static final Color TEXT_MAIN = new Color(23, 23, 23);        // Heading/Main (#171717)
    public static final Color TEXT_SUBTLE = new Color(75, 85, 99);      // Body (#4B5563)
    public static final Color TEXT_MUTED = new Color(107, 114, 128);    // Muted (#6B7280)

    public static final Color SUCCESS_TEXT = new Color(22, 101, 52);    // #166534
    public static final Color SUCCESS_BG = new Color(220, 252, 231);    // #DCFCE7
    public static final Color WARNING_TEXT = new Color(146, 64, 14);    // #92400E
    public static final Color WARNING_BG = new Color(254, 243, 199);    // #FEF3C7
    public static final Color DANGER_TEXT = new Color(220, 38, 38);     // #DC2626
    public static final Color DANGER_BG = new Color(254, 226, 226);     // #FEE2E2
    public static final Color BADGE_DOKUMEN_BG = new Color(243, 232, 255);
    public static final Color BADGE_DOKUMEN_TEXT = new Color(107, 33, 168);

    // Tipografi
    public static final Font FONT_HERO = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BADGE = new Font("Segoe UI", Font.BOLD, 11);

    /** Panel dengan sudut membulat (Finexy Card / Pill Container). */
    public static class RoundedPanel extends JPanel {
        private final int radius;
        private Color fillColor;
        private Color borderColor;

        public RoundedPanel(int radius, Color fillColor, Color borderColor) {
            this.radius = radius;
            this.fillColor = fillColor;
            this.borderColor = borderColor;
            setOpaque(false);
        }

        public RoundedPanel(LayoutManager layout, int radius, Color fillColor, Color borderColor) {
            super(layout);
            this.radius = radius;
            this.fillColor = fillColor;
            this.borderColor = borderColor;
            setOpaque(false);
        }

        public void setFillColor(Color fillColor) {
            this.fillColor = fillColor;
            repaint();
        }

        public void setBorderColor(Color borderColor) {
            this.borderColor = borderColor;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            int arc = Math.min(radius * 2, Math.min(w, h));

            if (fillColor != null) {
                g2.setColor(fillColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, arc, arc));
            }
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1f, h - 1f, arc, arc));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Membuat panel kartu putih membulat (radius 18px) dengan padding Finexy. */
    public static JPanel buatCardPanel() {
        JPanel p = new RoundedPanel(18, SURFACE, BORDER);
        p.setBorder(new EmptyBorder(18, 20, 18, 20));
        return p;
    }

    /** Tombol pill modern (corner radius penuh) dengan jaminan kontras tinggi. */
    public static JButton buatTombol(String teks, Color bg, Color fg) {
        return new PillButton(teks, bg, fg, null);
    }

    /** Tombol pill outline halus dengan background putih solid. */
    public static JButton buatTombolOutline(String teks, Color warna) {
        return new PillButton(teks, SURFACE, warna, BORDER);
    }

    /** Implementasi JButton berbentuk pill membulat. */
    public static class PillButton extends JButton {
        private Color pillBg;
        private Color pillBorder;

        public PillButton(String teks, Color bg, Color fg, Color borderColor) {
            super(teks);
            this.pillBg = bg;
            this.pillBorder = borderColor;
            setFont(FONT_BOLD);
            setForeground(fg);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(9, 18, 9, 18));
        }

        public void setPillStyle(Color bg, Color fg, Color borderColor) {
            this.pillBg = bg;
            this.pillBorder = borderColor;
            setForeground(fg);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            int arc = h;

            if (pillBg != null) {
                Color drawBg = getModel().isPressed() ? pillBg.darker() : pillBg;
                g2.setColor(drawBg);
                g2.fill(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, arc, arc));
            }
            if (pillBorder != null) {
                g2.setColor(pillBorder);
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1f, h - 1f, arc, arc));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** TextField modern dengan padding lega & border halus. */
    public static JTextField buatTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(FONT_REGULAR);
        tf.setForeground(TEXT_MAIN);
        tf.setBackground(SURFACE_SUBTLE);
        tf.setCaretColor(PRIMARY);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
        Dimension pref = tf.getPreferredSize();
        tf.setPreferredSize(new Dimension(pref.width, 34));
        return tf;
    }

    /** Kartu statistik ringkas dengan angka besar (Finexy Bento Card). */
    public static JPanel buatStatCard(String judul, String angka, Color warnaAksen) {
        JPanel card = new RoundedPanel(new BorderLayout(5, 6), 16, SURFACE, BORDER);
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel lblJudul = new JLabel(judul);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblJudul.setForeground(TEXT_MUTED);

        JLabel lblAngka = new JLabel(angka);
        lblAngka.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblAngka.setForeground(warnaAksen);

        card.add(lblJudul, BorderLayout.NORTH);
        card.add(lblAngka, BorderLayout.CENTER);
        return card;
    }

    /** Kartu KPI Finexy dengan dukungan varian Focal Coral (#F05423) dan subtext. */
    public static JPanel buatKpiCard(String judul, JLabel lblAngka, String subtext,
                                     boolean isFocalCoral, Color subColor) {
        Color bg = isFocalCoral ? PRIMARY : SURFACE;
        Color border = isFocalCoral ? PRIMARY : BORDER;
        Color titleFg = isFocalCoral ? new Color(255, 232, 224) : TEXT_MUTED;
        Color valFg = isFocalCoral ? Color.WHITE : CHARCOAL;
        Color subFg = isFocalCoral ? new Color(255, 232, 224) : (subColor != null ? subColor : TEXT_MUTED);

        JPanel card = new RoundedPanel(new BorderLayout(4, 6), 16, bg, border);
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel lblJudul = new JLabel(judul);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblJudul.setForeground(titleFg);

        lblAngka.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblAngka.setForeground(valFg);

        JLabel lblSub = new JLabel(subtext);
        lblSub.setFont(FONT_SMALL);
        lblSub.setForeground(subFg);

        card.add(lblJudul, BorderLayout.NORTH);
        card.add(lblAngka, BorderLayout.CENTER);
        card.add(lblSub, BorderLayout.SOUTH);
        return card;
    }

    /** Mengaplikasikan styling modern Finexy pada JTable. */
    public static void formatTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(42);
        table.setGridColor(BORDER_LIGHT);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(TEXT_MAIN);
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(SURFACE_SUBTLE);
        header.setForeground(TEXT_MUTED);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new ModernTableCellRenderer());
    }

    /** Renderer sel kustom untuk badge kategori, status, dan alignment. */
    public static class ModernTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            lbl.setBorder(new EmptyBorder(0, 12, 0, 12));
            String str = value == null ? "" : value.toString();

            if (!isSelected) {
                lbl.setBackground(SURFACE);
            }

            // Kolom ID (kolom 0)
            if (column == 0) {
                lbl.setFont(FONT_BOLD);
                lbl.setForeground(isSelected ? TEXT_MAIN : CHARCOAL);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                return lbl;
            }

            // Kategori (ELEKTRONIK / DOKUMEN)
            if ("ELEKTRONIK".equalsIgnoreCase(str) || "DOKUMEN".equalsIgnoreCase(str)) {
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(FONT_BADGE);
                if ("ELEKTRONIK".equalsIgnoreCase(str)) {
                    lbl.setText("ELEKTRONIK");
                    if (!isSelected) {
                        lbl.setForeground(TEXT_MUTED);
                        lbl.setBackground(SURFACE);
                    }
                } else {
                    lbl.setText("DOKUMEN");
                    if (!isSelected) {
                        lbl.setForeground(BADGE_DOKUMEN_TEXT);
                        lbl.setBackground(SURFACE);
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
                    lbl.setText("● PROSES_KLAIM");
                    if (!isSelected) {
                        lbl.setForeground(WARNING_TEXT);
                        lbl.setBackground(WARNING_BG);
                    }
                } else {
                    lbl.setText("● SELESAI");
                    if (!isSelected) {
                        lbl.setForeground(SUCCESS_TEXT);
                        lbl.setBackground(SUCCESS_BG);
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
