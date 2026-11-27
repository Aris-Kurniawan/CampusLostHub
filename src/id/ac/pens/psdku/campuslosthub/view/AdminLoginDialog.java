package id.ac.pens.psdku.campuslosthub.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Dialog Login Petugas / Administrator CampusLostHub (Finexy Design).
 * Menggunakan autentikasi sederhana (admin / admin123).
 */
public class AdminLoginDialog extends JDialog {

    private boolean authenticated = false;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblError;

    public AdminLoginDialog(Frame owner) {
        super(owner, "Login Petugas / Administrator", true);
        setSize(460, 460);
        setLocationRelativeTo(owner);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel contentPane = new JPanel(new BorderLayout(15, 16));
        contentPane.setBackground(UITheme.BG_MAIN);
        contentPane.setBorder(new EmptyBorder(24, 26, 24, 26));

        // Header
        JPanel pnlHeader = new JPanel(new GridLayout(0, 1, 4, 4));
        pnlHeader.setBackground(UITheme.BG_MAIN);

        JLabel lblLogo = new JLabel("CampusLostHub", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogo.setForeground(UITheme.PRIMARY);

        JLabel lblSub = new JLabel("Smart Campus Lost & Found System • PENS", SwingConstants.CENTER);
        lblSub.setFont(UITheme.FONT_SMALL);
        lblSub.setForeground(UITheme.TEXT_MUTED);

        JLabel lblTitle = new JLabel("Login Administrator / Pos", SwingConstants.CENTER);
        lblTitle.setFont(UITheme.FONT_SECTION);
        lblTitle.setForeground(UITheme.TEXT_MAIN);

        pnlHeader.add(lblLogo);
        pnlHeader.add(lblSub);
        pnlHeader.add(Box.createVerticalStrut(4));
        pnlHeader.add(lblTitle);

        // Form Card (BoxLayout agar tinggi input field konsisten & tidak terpotong)
        JPanel card = UITheme.buatCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        lblError = new JLabel(" ");
        lblError.setFont(UITheme.FONT_SMALL);
        lblError.setForeground(UITheme.DANGER_TEXT);
        lblError.setOpaque(true);
        lblError.setBackground(UITheme.SURFACE);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblError.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        JLabel lblU = new JLabel("Username Petugas");
        lblU.setFont(UITheme.FONT_BOLD);
        lblU.setForeground(UITheme.TEXT_MAIN);
        lblU.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = UITheme.buatTextField(15);
        txtUsername.setText("admin");
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        txtUsername.setPreferredSize(new Dimension(360, 36));

        JLabel lblP = new JLabel("Password");
        lblP.setFont(UITheme.FONT_BOLD);
        lblP.setForeground(UITheme.TEXT_MAIN);
        lblP.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = UITheme.buatPasswordField(15);
        txtPassword.setText("admin123");
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        txtPassword.setPreferredSize(new Dimension(360, 36));

        card.add(lblError);
        card.add(Box.createVerticalStrut(6));
        card.add(lblU);
        card.add(Box.createVerticalStrut(6));
        card.add(txtUsername);
        card.add(Box.createVerticalStrut(12));
        card.add(lblP);
        card.add(Box.createVerticalStrut(6));
        card.add(txtPassword);

        // Tombol Masuk (FlowLayout / Border agar teks tombol tidak terpotong)
        JButton btnBatal = UITheme.buatTombolOutline("Batal", UITheme.TEXT_MAIN);
        btnBatal.addActionListener(e -> dispose());

        JButton btnLogin = UITheme.buatTombol("Masuk ke Panel Petugas", UITheme.CHARCOAL, Color.WHITE);
        btnLogin.addActionListener(e -> attemptLogin());

        JPanel pnlAksi = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlAksi.setBackground(UITheme.BG_MAIN);
        pnlAksi.add(btnBatal);
        pnlAksi.add(btnLogin);

        contentPane.add(pnlHeader, BorderLayout.NORTH);
        contentPane.add(card, BorderLayout.CENTER);
        contentPane.add(pnlAksi, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnLogin);
        setContentPane(contentPane);
    }

    private void attemptLogin() {
        String u = txtUsername.getText().trim();
        String p = new String(txtPassword.getPassword()).trim();

        if (("admin".equals(u) && "admin123".equals(p))
                || ("petugas".equals(u) && "petugas123".equals(p))) {
            authenticated = true;
            dispose();
        } else {
            lblError.setText(" Autentikasi Gagal: Username atau password salah! ");
            lblError.setBackground(UITheme.DANGER_BG);
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
