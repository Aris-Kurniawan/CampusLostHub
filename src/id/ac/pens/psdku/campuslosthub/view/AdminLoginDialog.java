package id.ac.pens.psdku.campuslosthub.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Dialog Login Petugas / Administrator CampusLostHub sesuai Mockup 5.
 * Menggunakan autentikasi sederhana (admin / admin123).
 */
public class AdminLoginDialog extends JDialog {

    private boolean authenticated = false;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblError;

    public AdminLoginDialog(Frame owner) {
        super(owner, "Login Petugas / Administrator", true);
        setSize(420, 430);
        setLocationRelativeTo(owner);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel contentPane = new JPanel(new BorderLayout(15, 15));
        contentPane.setBackground(UITheme.BG_MAIN);
        contentPane.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Header Card
        JPanel pnlHeader = new JPanel(new GridLayout(0, 1, 4, 4));
        pnlHeader.setBackground(UITheme.BG_MAIN);

        JLabel lblLogo = new JLabel("🛡️ CampusLostHub", SwingConstants.CENTER);
        lblLogo.setFont(UITheme.FONT_TITLE);
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

        // Form Card
        JPanel card = UITheme.buatCardPanel();
        card.setLayout(new GridLayout(0, 1, 6, 6));

        lblError = new JLabel(" ");
        lblError.setFont(UITheme.FONT_SMALL);
        lblError.setForeground(UITheme.DANGER_TEXT);
        lblError.setOpaque(true);
        lblError.setBackground(UITheme.SURFACE);

        txtUsername = UITheme.buatTextField(15);
        txtUsername.setText("admin"); // default kemudahan pengujian
        txtPassword = new JPasswordField(15);
        txtPassword.setFont(UITheme.FONT_REGULAR);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
        txtPassword.setText("admin123");

        JLabel lblU = new JLabel("Username Petugas");
        lblU.setFont(UITheme.FONT_BOLD);
        JLabel lblP = new JLabel("Password");
        lblP.setFont(UITheme.FONT_BOLD);

        card.add(lblError);
        card.add(lblU);
        card.add(txtUsername);
        card.add(lblP);
        card.add(txtPassword);

        // Tombol Masuk
        JButton btnLogin = UITheme.buatTombol("Masuk ke Panel Petugas", UITheme.PRIMARY, Color.WHITE);
        btnLogin.addActionListener(e -> attemptLogin());

        JButton btnBatal = UITheme.buatTombolOutline("Batal", UITheme.TEXT_MUTED);
        btnBatal.addActionListener(e -> dispose());

        JPanel pnlAksi = new JPanel(new GridLayout(1, 2, 8, 0));
        pnlAksi.setBackground(UITheme.BG_MAIN);
        pnlAksi.add(btnBatal);
        pnlAksi.add(btnLogin);

        contentPane.add(pnlHeader, BorderLayout.NORTH);
        contentPane.add(card, BorderLayout.CENTER);
        contentPane.add(pnlAksi, BorderLayout.SOUTH);

        // Enter to login
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
