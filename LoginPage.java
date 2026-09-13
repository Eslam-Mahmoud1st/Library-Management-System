import java.sql.*;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginPage extends JFrame {

    Color navy = new Color(8, 25, 50);
    Color blue = new Color(55, 95, 225);
    Color lightBlue = new Color(115, 135, 255);

    HintTextField usernameField;
    HintPasswordField passwordField;
    HintTextField fullNameField;
    HintTextField regUsernameField;
    HintPasswordField regPasswordField;
    HintPasswordField confirmPasswordField;

    CardLayout cardLayout = new CardLayout();
    JPanel cardPanel = new JPanel(cardLayout);

    JLabel loginTab, registerTab;
    JPanel loginLine, registerLine;

    public LoginPage() {
        setTitle("LIBRARY MANAGEMENT SYSTEM");
        setSize(1200, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(null);
        root.setBackground(new Color(230, 235, 242));
        setContentPane(root);

        LeftSide left = new LeftSide();
        left.setBounds(0, 0, 405, 760);
        root.add(left);

        RightBackground right = new RightBackground();
        right.setBounds(405, 0, 795, 760);
        right.setLayout(null);
        root.add(right);

        RoundPanel box = new RoundPanel(28, Color.WHITE);
        box.setLayout(null);
        box.setBounds(115, 50, 580, 610);
        right.add(box);

        loginTab = tab("Login", blue, Font.BOLD);
        loginTab.setBounds(0, 25, 290, 55);
        box.add(loginTab);

        registerTab = tab("Register", new Color(120, 120, 120), Font.PLAIN);
        registerTab.setBounds(290, 25, 290, 55);
        box.add(registerTab);

        JSeparator sep = new JSeparator();
        sep.setBounds(0, 88, 580, 1);
        box.add(sep);

        loginLine = new JPanel();
        loginLine.setBackground(blue);
        loginLine.setBounds(45, 88, 200, 3);
        box.add(loginLine);

        registerLine = new JPanel();
        registerLine.setBackground(blue);
        registerLine.setBounds(335, 88, 200, 3);
        registerLine.setVisible(false);
        box.add(registerLine);

        cardPanel.setOpaque(false);
        cardPanel.setBounds(45, 125, 490, 450);
        box.add(cardPanel);

        cardPanel.add(loginPanel(), "login");
        cardPanel.add(registerPanel(), "register");

        loginTab.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showLogin();
            }
        });

        registerTab.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showRegister();
            }
        });
    }

    JLabel tab(String text, Color color, int style) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", style, 22));
        l.setForeground(color);
        l.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return l;
    }

    JPanel loginPanel() {
        JPanel p = new JPanel(null);
        p.setOpaque(false);

        usernameField = (HintTextField) input("Username", "Enter your username", false, 0, p);
        passwordField = (HintPasswordField) input("Password", "Enter your password", true, 100, p);

        JCheckBox remember = new JCheckBox("Remember me");
        remember.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        remember.setOpaque(false);
        remember.setFocusPainted(false);
        remember.setBounds(0, 205, 160, 30);
        p.add(remember);

        JLabel forgot = new JLabel("Forgot Password?");
        forgot.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        forgot.setForeground(new Color(38, 75, 170));
        forgot.setBounds(345, 207, 150, 25);
        p.add(forgot);

        RoundButton login = new RoundButton("Login", blue, Color.WHITE, true);
        login.setFont(new Font("Segoe UI", Font.BOLD, 20));
        login.setBounds(0, 275, 490, 60);
        p.add(login);

        login.addActionListener(e -> {
            try {
                Connection con = DriverManager.getConnection(
                        "jdbc:sqlserver://localhost:1433;databaseName=LibraryDB;encrypt=false;trustServerCertificate=true",
                        "library_user",
                        "1234"
                );

                String sql = "SELECT * FROM Users WHERE Username=? AND Password=?";
                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, usernameField.getText());
                ps.setString(2, String.valueOf(passwordField.getPassword()));

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Login Success");

                    new HomePage(usernameField.getText()).setVisible(true);
                    dispose();

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        JLabel or = new JLabel("OR", SwingConstants.CENTER);
        or.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        or.setForeground(Color.GRAY);
        or.setBounds(220, 345, 50, 25);
        p.add(or);

        JSeparator s1 = new JSeparator();
        s1.setBounds(0, 358, 210, 1);
        p.add(s1);

        JSeparator s2 = new JSeparator();
        s2.setBounds(280, 358, 210, 1);
        p.add(s2);

        RoundButton create = new RoundButton("Create New Account", Color.WHITE, new Color(38, 75, 170), false);
        create.setFont(new Font("Segoe UI", Font.BOLD, 18));
        create.setBounds(0, 390, 490, 58);
        p.add(create);

        create.addActionListener(e -> showRegister());

        return p;
    }

    JPanel registerPanel() {
        JPanel p = new JPanel(null);
        p.setOpaque(false);

        fullNameField = (HintTextField) input("Full Name", "Enter your full name", false, 0, p);

        regUsernameField = (HintTextField) input("Username", "Enter your username", false, 88, p);

        regPasswordField = (HintPasswordField) input("Password", "Enter your password", true, 176, p);

        confirmPasswordField = (HintPasswordField) input("Confirm Password", "Confirm your password", true, 264, p);

        RoundButton register = new RoundButton("Register", blue, Color.WHITE, true);
        register.setFont(new Font("Segoe UI", Font.BOLD, 20));
        register.setBounds(0, 365, 490, 58);
        p.add(register);

        register.addActionListener(e -> {
            try {
                String fullName = fullNameField.getText();
                String username = regUsernameField.getText();

                String password = new String(regPasswordField.getPassword()).trim();
                String confirm = new String(confirmPasswordField.getPassword()).trim();

                if (password.isEmpty() || confirm.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields!");
                    return;
                }

                if (!password.equals(confirm)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match!");
                    return;
                }

                if (!password.equals(confirm)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match!");
                    return;
                }

                Connection con = DriverManager.getConnection(
                        "jdbc:sqlserver://localhost:1433;databaseName=LibraryDB;encrypt=false;trustServerCertificate=true",
                        "library_user",
                        "1234"
                );

                String sql = "INSERT INTO Users (FullName, Username, Password) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, fullName);
                ps.setString(2, username);
                ps.setString(3, password);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(null, "Registered Successfully!");

                showLogin();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        JLabel back = new JLabel("Already have an account? Login", SwingConstants.CENTER);
        back.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        back.setForeground(new Color(38, 75, 170));
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        back.setBounds(0, 430, 490, 25);
        p.add(back);

        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showLogin();
            }
        });

        return p;
    }

    Component input(String title, String hint, boolean pass, int y, JPanel parent) {

        RoundPanel box = new RoundPanel(16, Color.WHITE);
        box.border = new Color(220, 224, 232);
        box.setLayout(null);
        box.setBounds(0, y, 490, 75);

        RoundPanel icon = new RoundPanel(14, new Color(242, 245, 255));
        icon.setBounds(0, 0, 68, 75);
        icon.setLayout(new BorderLayout());
        box.add(icon);

        JLabel ic = new JLabel(pass ? "🔒" : "👤", SwingConstants.CENTER);
        ic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        ic.setForeground(blue);
        icon.add(ic);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 17));
        t.setBounds(85, 13, 250, 24);
        box.add(t);

        if (pass) {
            HintPasswordField f = new HintPasswordField(hint);
            f.setBounds(85, 40, 330, 25);
            box.add(f);
            parent.add(box);
            return f;
        } else {
            HintTextField f = new HintTextField(hint);
            f.setBounds(85, 40, 350, 25);
            box.add(f);
            parent.add(box);
            return f;
        }
    }

    void showLogin() {
        cardLayout.show(cardPanel, "login");
        loginTab.setForeground(blue);
        registerTab.setForeground(Color.GRAY);
        loginTab.setFont(new Font("Segoe UI", Font.BOLD, 22));
        registerTab.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        loginLine.setVisible(true);
        registerLine.setVisible(false);
    }

    void showRegister() {
        cardLayout.show(cardPanel, "register");
        registerTab.setForeground(blue);
        loginTab.setForeground(Color.GRAY);
        registerTab.setFont(new Font("Segoe UI", Font.BOLD, 22));
        loginTab.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        loginLine.setVisible(false);
        registerLine.setVisible(true);
    }

    class LeftSide extends JPanel {

        LeftSide() {
            setOpaque(false);
            setLayout(null);

            JLabel title = new JLabel("<html><center>LIBRARY<br>MANAGEMENT SYSTEM</center></html>", SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 24));
            title.setForeground(Color.WHITE);
            title.setBounds(10, 200, 385, 60);
            add(title);

            JLabel welcome = new JLabel("Welcome Back!", SwingConstants.CENTER);
            welcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
            welcome.setForeground(lightBlue);
            welcome.setBounds(0, 305, 405, 35);
            add(welcome);
        }

        protected void paintComponent(Graphics gr) {
            Graphics2D g = (Graphics2D) gr;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(navy);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(4));
            g.drawRoundRect(145, 105, 48, 48, 8, 8);
            g.drawRoundRect(212, 105, 48, 48, 8, 8);
            g.drawLine(202, 105, 202, 160);

            g.setColor(new Color(70, 95, 175));
            g.setStroke(new BasicStroke(1));
            g.drawLine(80, 270, 185, 270);
            g.drawLine(220, 270, 325, 270);

            g.setColor(lightBlue);
            Polygon d = new Polygon();
            d.addPoint(202, 263);
            d.addPoint(209, 270);
            d.addPoint(202, 277);
            d.addPoint(195, 270);
            g.fillPolygon(d);

            drawBooks(g);
        }

        void drawBooks(Graphics2D g) {
            book(g, 95, 610, 215, 45, new Color(65, 95, 205));
            book(g, 105, 562, 205, 43, new Color(80, 115, 230));
            book(g, 120, 518, 180, 40, new Color(55, 90, 200));

            g.setColor(new Color(235, 238, 245));
            g.fillRoundRect(178, 468, 50, 45, 14, 14);

            g.setColor(new Color(70, 165, 105));
            g.setStroke(new BasicStroke(5));
            g.draw(new QuadCurve2D.Double(203, 470, 170, 430, 175, 410));
            g.draw(new QuadCurve2D.Double(203, 470, 230, 430, 235, 405));
            g.draw(new QuadCurve2D.Double(203, 470, 202, 425, 205, 395));
        }

        void book(Graphics2D g, int x, int y, int w, int h, Color c) {
            g.setColor(c);
            g.fillRoundRect(x, y, w, h, 12, 12);
            g.setColor(new Color(235, 240, 255));
            g.fillRoundRect(x + 20, y + h - 14, w - 40, 10, 8, 8);
            g.setColor(c.darker());
            g.fillRect(x + 35, y + 5, 8, h - 10);
        }
    }

    class RightBackground extends JPanel {

        protected void paintComponent(Graphics gr) {
            Graphics2D g = (Graphics2D) gr;
            GradientPaint gp = new GradientPaint(0, 0, new Color(225, 232, 244),
                    getWidth(), getHeight(), new Color(245, 240, 235));
            g.setPaint(gp);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(new Color(255, 255, 255, 145));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    class RoundPanel extends JPanel {

        int radius;
        Color bg;
        Color border;

        RoundPanel(int radius, Color bg) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        protected void paintComponent(Graphics gr) {
            Graphics2D g = (Graphics2D) gr;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(bg);
            g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            if (border != null) {
                g.setColor(border);
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            }

            super.paintComponent(gr);
        }
    }

    class RoundButton extends JButton {

        Color bg, fg;
        boolean filled;

        RoundButton(String text, Color bg, Color fg, boolean filled) {
            super(text);
            this.bg = bg;
            this.fg = fg;
            this.filled = filled;
            setForeground(fg);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorder(new EmptyBorder(0, 0, 0, 0));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        protected void paintComponent(Graphics gr) {
            Graphics2D g = (Graphics2D) gr;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(bg);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

            if (!filled) {
                g.setColor(new Color(38, 75, 170));
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            }

            super.paintComponent(gr);
        }
    }

    class HintTextField extends JTextField {

        String hint;

        HintTextField(String hint) {
            this.hint = hint;
            setText("");
            setForeground(Color.GRAY);
            setFont(new Font("Segoe UI", Font.PLAIN, 16));
            setBorder(null);
            setOpaque(false);

            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    if (getText().equals(hint)) {
                        setText("");
                        setForeground(Color.BLACK);
                    }
                }

                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(hint);
                        setForeground(Color.GRAY);
                    }
                }
            });
        }
    }

    class HintPasswordField extends JPasswordField {

        String hint;

        HintPasswordField(String hint) {
            this.hint = hint;
            setText(hint);
            setEchoChar((char) 0);
            setForeground(Color.GRAY);
            setFont(new Font("Segoe UI", Font.PLAIN, 16));
            setBorder(null);
            setOpaque(false);

            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    if (String.valueOf(getPassword()).equals(hint)) {
                        setText("");
                        setEchoChar('●');
                        setForeground(Color.BLACK);
                    }
                }

                public void focusLost(FocusEvent e) {
                    if (String.valueOf(getPassword()).isEmpty()) {
                        setText(hint);
                        setEchoChar((char) 0);
                        setForeground(Color.GRAY);
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        new LoginPage().setVisible(true);
    }
}
