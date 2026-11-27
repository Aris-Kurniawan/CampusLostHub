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

    /** TextField modern dengan sudut melengkung (curve 14px) dan padding lega. */
    public static JTextField buatTextField(int columns) {
        JTextField tf = new RoundedTextField(columns, 14);
        tf.setFont(FONT_REGULAR);
        tf.setForeground(TEXT_MAIN);
        tf.setCaretColor(PRIMARY);
        tf.setBorder(new EmptyBorder(6, 12, 6, 12));
        Dimension pref = tf.getPreferredSize();
        tf.setPreferredSize(new Dimension(pref.width, 36));
        return tf;
    }

    /** PasswordField modern dengan sudut melengkung (curve 14px). */
    public static JPasswordField buatPasswordField(int columns) {
        JPasswordField pf = new RoundedPasswordField(columns, 14);
        pf.setFont(FONT_REGULAR);
        pf.setForeground(TEXT_MAIN);
        pf.setCaretColor(PRIMARY);
        pf.setBorder(new EmptyBorder(6, 12, 6, 12));
        Dimension pref = pf.getPreferredSize();
        pf.setPreferredSize(new Dimension(pref.width, 36));
        return pf;
    }

    /** JTextField kustom bersudut melengkung. */
    public static class RoundedTextField extends JTextField {
        private final int radius;

        public RoundedTextField(int columns, int radius) {
            super(columns);
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2.setColor(SURFACE_SUBTLE);
            g2.fill(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, radius, radius));
            g2.setColor(isFocusOwner() ? CHARCOAL : BORDER);
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1f, h - 1f, radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** JPasswordField kustom bersudut melengkung. */
    public static class RoundedPasswordField extends JPasswordField {
        private final int radius;

        public RoundedPasswordField(int columns, int radius) {
            super(columns);
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            g2.setColor(SURFACE_SUBTLE);
            g2.fill(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, radius, radius));
            g2.setColor(isFocusOwner() ? CHARCOAL : BORDER);
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 1f, h - 1f, radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
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

    /** Mengaplikasikan styling modern Finexy (Orange & White) pada JTable dengan pill melengkung. */
    public static void formatTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(46);
        table.setGridColor(BORDER_LIGHT);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(TEXT_MAIN);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setBackground(SURFACE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(SURFACE_SUBTLE);
        header.setForeground(TEXT_MUTED);
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        tbl, value, isSelected, hasFocus, row, column);
                lbl.setBackground(SURFACE_SUBTLE);
                lbl.setForeground(TEXT_MUTED);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_LIGHT),
                        new EmptyBorder(0, 14, 0, 14)
                ));
                String text = value == null ? "" : value.toString();
                if (column == 0 || "Kategori".equalsIgnoreCase(text) || "Status".equalsIgnoreCase(text)) {
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    lbl.setHorizontalAlignment(SwingConstants.LEFT);
                }
                return lbl;
            }
        });

        table.setDefaultRenderer(Object.class, new ModernTableCellRenderer());
    }

    /** Renderer sel kustom dengan badge pill membulat (curve) ala Finexy Orange & White. */
    public static class ModernTableCellRenderer extends DefaultTableCellRenderer {
        private Color badgeBg = null;
        private Color badgeFg = null;
        private String badgeText = null;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            badgeBg = null;
            badgeFg = null;
            badgeText = null;

            JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            lbl.setBorder(new EmptyBorder(0, 14, 0, 14));
            String str = value == null ? "" : value.toString();

            lbl.setBackground(isSelected ? PRIMARY_LIGHT : SURFACE);

            // Kolom ID (kolom 0)
            if (column == 0) {
                lbl.setFont(FONT_BOLD);
                lbl.setForeground(isSelected ? PRIMARY_DARK : CHARCOAL);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                return lbl;
            }

            // Kategori (ELEKTRONIK / DOKUMEN) -> Pill Badge Curve
            if ("ELEKTRONIK".equalsIgnoreCase(str) || "DOKUMEN".equalsIgnoreCase(str)) {
                badgeText = str.toUpperCase();
                if ("ELEKTRONIK".equalsIgnoreCase(str)) {
                    badgeBg = BORDER_LIGHT;
                    badgeFg = TEXT_SUBTLE;
                } else {
                    badgeBg = PRIMARY_LIGHT;
                    badgeFg = PRIMARY_DARK;
                }
                lbl.setText("");
                return lbl;
            }

            // Status (TERSEDIA / PROSES_KLAIM / SELESAI) -> Pill Badge Curve
            if ("TERSEDIA".equalsIgnoreCase(str)
                    || "PROSES_KLAIM".equalsIgnoreCase(str)
                    || "SELESAI".equalsIgnoreCase(str)) {
                if ("TERSEDIA".equalsIgnoreCase(str)) {
                    badgeText = "● TERSEDIA";
                    badgeBg = SUCCESS_BG;
                    badgeFg = SUCCESS_TEXT;
                } else if ("PROSES_KLAIM".equalsIgnoreCase(str)) {
                    badgeText = "● PROSES_KLAIM";
                    badgeBg = WARNING_BG;
                    badgeFg = WARNING_TEXT;
                } else {
                    badgeText = "● SELESAI";
                    badgeBg = SUCCESS_BG;
                    badgeFg = SUCCESS_TEXT;
                }
                lbl.setText("");
                return lbl;
            }

            lbl.setFont(FONT_REGULAR);
            lbl.setForeground(TEXT_MAIN);
            lbl.setHorizontalAlignment(SwingConstants.LEFT);
            return lbl;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (badgeText != null && badgeBg != null && badgeFg != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(FONT_BADGE);
                FontMetrics fm = g2.getFontMetrics();
                int textW = fm.stringWidth(badgeText);
                int pillW = textW + 24;
                int pillH = 26;
                int px = (getWidth() - pillW) / 2;
                int py = (getHeight() - pillH) / 2;

                g2.setColor(badgeBg);
                g2.fill(new RoundRectangle2D.Float(px, py, pillW, pillH, pillH, pillH));

                g2.setColor(badgeFg);
                int tx = px + (pillW - textW) / 2;
                int ty = py + ((pillH - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(badgeText, tx, ty);
                g2.dispose();
            }
        }
    }
}
