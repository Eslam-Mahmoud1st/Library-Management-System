import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class HomePage extends JFrame {

    private final Color SIDEBAR_BG = new Color(23, 42, 70);
    private final Color SIDEBAR_HOVER = new Color(35, 60, 95);
    private final Color SIDEBAR_ACTIVE = new Color(52, 98, 184);
    private final Color MAIN_BG = new Color(248, 249, 252);
    
    private JPanel activeNavPanel;
    private JPanel contentPanel;
    private String username;

    public HomePage(String username) {
        this.username = username;
        setTitle("Library Management System - Home");
        setSize(1300, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(createMainContent(), BorderLayout.CENTER);
    }

    public HomePage() {
        this("Admin");
    }

    // Database Connection Helper
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:sqlserver://localhost:1433;databaseName=LibraryDB;encrypt=false;trustServerCertificate=true",
                "library_user", "1234");
    }

    // Sidebar Creation
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(260, getHeight()));

        // Logo Panel
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(SIDEBAR_BG);
        logoPanel.setBorder(new EmptyBorder(40, 0, 30, 0));

        JPanel logoIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(55, 105, 200));
                g2.fillOval(10, 10, 70, 70);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(28, 30, 15, 25, 3, 3);
                g2.drawRoundRect(43, 30, 15, 25, 3, 3);
                g2.drawLine(43, 30, 43, 55);
            }
        };
        logoIcon.setPreferredSize(new Dimension(90, 90));
        logoIcon.setMaximumSize(new Dimension(90, 90));
        logoIcon.setOpaque(false);
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle1 = new JLabel("LIBRARY");
        lblTitle1.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle1.setForeground(Color.WHITE);
        lblTitle1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle2 = new JLabel("MANAGEMENT SYSTEM");
        lblTitle2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle2.setForeground(new Color(200, 210, 230));
        lblTitle2.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(logoIcon);
        logoPanel.add(Box.createVerticalStrut(10));
        logoPanel.add(lblTitle1);
        logoPanel.add(Box.createVerticalStrut(5));
        logoPanel.add(lblTitle2);

        sidebar.add(logoPanel, BorderLayout.NORTH);

        // Navigation Panel
        JPanel navContainer = new JPanel();
        navContainer.setLayout(new BoxLayout(navContainer, BoxLayout.Y_AXIS));
        navContainer.setBackground(SIDEBAR_BG);

        navContainer.add(createNavItem("Home", true, 1));
        navContainer.add(createNavItem("Add Book", false, 2));
        navContainer.add(createNavItem("View Books", false, 3));
        navContainer.add(createNavItem("Search Books", false, 4));
        navContainer.add(createNavItem("Borrow Book", false, 5));
        navContainer.add(createNavItem("Return Book", false, 6));
        navContainer.add(createNavItem("Statistics", false, 7));
        navContainer.add(createNavItem("Update Book", false, 8));

        sidebar.add(navContainer, BorderLayout.CENTER);

        // Admin Panel
        JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        adminPanel.setBackground(SIDEBAR_BG);
        adminPanel.setBorder(new EmptyBorder(10, 10, 20, 10));

        JPanel adminIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(55, 105, 200));
                g2.fillOval(0, 0, 45, 45);
                g2.setColor(Color.WHITE);
                g2.fillOval(15, 8, 15, 15);
                g2.fillArc(8, 25, 29, 25, 0, 180);
            }
        };
        adminIcon.setPreferredSize(new Dimension(45, 45));
        adminIcon.setOpaque(false);

        JPanel adminText = new JPanel();
        adminText.setLayout(new BoxLayout(adminText, BoxLayout.Y_AXIS));
        adminText.setOpaque(false);

        JLabel lblAdminName = new JLabel("Welcome, " + username);
        lblAdminName.setForeground(Color.WHITE);
        lblAdminName.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel lblAdminRole = new JLabel("Administrator");
        lblAdminRole.setForeground(new Color(200, 210, 230));
        lblAdminRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        adminText.add(lblAdminName);
        adminText.add(lblAdminRole);

        adminPanel.add(adminIcon);
        adminPanel.add(adminText);

        sidebar.add(adminPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private JPanel createNavItem(String text, boolean isActive, int iconType) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        panel.setBackground(isActive ? SIDEBAR_ACTIVE : SIDEBAR_BG);
        panel.setMaximumSize(new Dimension(260, 55));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (isActive) {
            activeNavPanel = panel;
        }

        JPanel icon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));

                switch (iconType) {
                    case 1: // Home
                        g2.drawPolygon(new int[]{2, 12, 22}, new int[]{12, 2, 12}, 3);
                        g2.drawRect(5, 12, 14, 10);
                        break;
                    case 2: // Add Book
                        g2.drawRoundRect(2, 2, 14, 20, 3, 3);
                        g2.drawLine(9, 2, 9, 22);
                        g2.setStroke(new BasicStroke(2.5f));
                        g2.drawLine(17, 10, 23, 10);
                        g2.drawLine(20, 7, 20, 13);
                        break;
                    case 3: // View Books
                        g2.drawArc(0, 4, 12, 6, 0, 180);
                        g2.drawArc(12, 4, 12, 6, 0, 180);
                        g2.drawLine(0, 7, 0, 22);
                        g2.drawLine(24, 7, 24, 22);
                        g2.drawLine(12, 7, 12, 22);
                        g2.drawLine(0, 22, 12, 20);
                        g2.drawLine(24, 22, 12, 20);
                        g2.setStroke(new BasicStroke(1f));
                        g2.drawLine(3, 12, 10, 11);
                        g2.drawLine(3, 15, 10, 14);
                        g2.drawLine(14, 11, 21, 12);
                        g2.drawLine(14, 14, 21, 15);
                        break;
                    case 4: // Search
                        g2.drawRoundRect(0, 2, 10, 14, 2, 2);
                        g2.drawLine(5, 2, 5, 16);
                        g2.setStroke(new BasicStroke(2f));
                        g2.drawOval(13, 8, 10, 10);
                        g2.drawLine(21, 16, 24, 20);
                        break;
                    case 5: // Borrow
                        g2.drawRoundRect(0, 2, 12, 18, 3, 3);
                        g2.drawLine(6, 2, 6, 20);
                        g2.setStroke(new BasicStroke(2.5f));
                        g2.drawLine(15, 11, 24, 11);
                        g2.drawLine(21, 7, 24, 11);
                        g2.drawLine(21, 15, 24, 11);
                        break;
                    case 6: // Return
                        g2.drawRoundRect(0, 2, 12, 18, 3, 3);
                        g2.drawLine(6, 2, 6, 20);
                        g2.setStroke(new BasicStroke(2.5f));
                        g2.drawLine(15, 11, 24, 11);
                        g2.drawLine(15, 11, 18, 7);
                        g2.drawLine(15, 11, 18, 15);
                        break;
                    case 7: // Statistics
                        g2.drawRect(2, 2, 20, 20);
                        g2.fillRect(4, 14, 4, 6);
                        g2.fillRect(10, 10, 4, 10);
                        g2.fillRect(16, 6, 4, 14);
                        break;
                    case 8: // Update
                        g2.drawOval(2, 2, 20, 20);
                        g2.setStroke(new BasicStroke(2.5f));
                        g2.drawLine(16, 8, 8, 16);
                        g2.drawLine(18, 6, 6, 18);
                        break;
                }
            }
        };
        icon.setPreferredSize(new Dimension(24, 24));
        icon.setOpaque(false);

        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));

        panel.add(icon);
        panel.add(lbl);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (panel != activeNavPanel) {
                    panel.setBackground(SIDEBAR_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (panel != activeNavPanel) {
                    panel.setBackground(SIDEBAR_BG);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (activeNavPanel != null) {
                    activeNavPanel.setBackground(SIDEBAR_BG);
                }
                panel.setBackground(SIDEBAR_ACTIVE);
                activeNavPanel = panel;
                handleNavigation(text);
            }
        });

        return panel;
    }

    // Navigation Handler
    private void handleNavigation(String text) {
        switch (text) {
            case "Home":
                showHome();
                break;
            case "Add Book":
                openAddBook();
                break;
            case "View Books":
                showBooksTable();
                break;
            case "Search Books":
                openSearchBook();
                break;
            case "Borrow Book":
                openBorrowBook();
                break;
            case "Return Book":
                openReturnBook();
                break;
            case "Statistics":
                showStatistics();
                break;
            case "Update Book":
                openUpdateBook();
                break;
        }
    }

    private void showHome() {
        contentPanel.removeAll();
        contentPanel.add(createMainCards(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Main Content
    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(MAIN_BG);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(MAIN_BG);
        headerPanel.setBorder(new EmptyBorder(50, 0, 30, 0));

        JLabel lblHome = new JLabel("HOME");
        lblHome.setFont(new Font("Segoe UI", Font.BOLD, 65));
        lblHome.setForeground(new Color(25, 45, 75));
        lblHome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel dotsLinePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int width = getWidth();
                int midX = width / 2;
                g2.setColor(new Color(220, 225, 230));
                g2.drawLine(50, 10, width - 50, 10);
                g2.setColor(MAIN_BG);
                g2.fillRect(midX - 30, 0, 60, 20);
                g2.setColor(new Color(55, 105, 200));
                g2.fillOval(midX - 15, 7, 6, 6);
                g2.fillOval(midX - 3, 7, 6, 6);
                g2.fillOval(midX + 9, 7, 6, 6);
            }
        };
        dotsLinePanel.setPreferredSize(new Dimension(800, 30));
        dotsLinePanel.setMaximumSize(new Dimension(800, 30));
        dotsLinePanel.setOpaque(false);
        dotsLinePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(lblHome);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(dotsLinePanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content Panel
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(MAIN_BG);
        contentPanel.add(createMainCards(), BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
                new EmptyBorder(15, 30, 15, 30)
        ));

        JLabel lblProjectName = new JLabel("Advanced Software Project");
        lblProjectName.setForeground(new Color(150, 160, 170));
        lblProjectName.setFont(new Font("Segoe UI", Font.BOLD, 13));

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        JLabel lblDateTime = new JLabel("🕒 " + timeFormat.format(new Date()) + "    📅 " + dateFormat.format(new Date()));
        lblDateTime.setForeground(new Color(150, 160, 170));
        lblDateTime.setFont(new Font("Segoe UI", Font.BOLD, 13));

        new Timer(1000, e -> lblDateTime.setText("🕒 " + timeFormat.format(new Date()) + "    📅 " + dateFormat.format(new Date()))).start();

        footer.add(lblProjectName, BorderLayout.WEST);
        footer.add(lblDateTime, BorderLayout.EAST);

        mainPanel.add(footer, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createMainCards() {
        JPanel topCards = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 20));
        topCards.setBackground(MAIN_BG);
        topCards.add(new DashboardCard("ADD BOOK", 1, new Color(54, 104, 201)));
        topCards.add(new DashboardCard("VIEW BOOKS", 2, new Color(76, 175, 80)));
        topCards.add(new DashboardCard("SEARCH BOOKS", 3, new Color(229, 57, 53)));

        JPanel bottomCards = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 20));
        bottomCards.setBackground(MAIN_BG);
        bottomCards.add(new DashboardCard("BORROW BOOK", 4, new Color(142, 68, 173)));
        bottomCards.add(new DashboardCard("RETURN BOOK", 5, new Color(26, 188, 156)));
        bottomCards.add(new DashboardCard("STATISTICS", 7, new Color(255, 152, 0)));

        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(MAIN_BG);
        cardsContainer.add(topCards);
        cardsContainer.add(bottomCards);

        return cardsContainer;
    }

    // Dashboard Card Class
    class DashboardCard extends JPanel {
        private String title;
        private int iconType;
        private Color accentColor;
        private boolean isHover = false;

        public DashboardCard(String title, int iconType, Color accentColor) {
            this.title = title;
            this.iconType = iconType;
            this.accentColor = accentColor;

            setPreferredSize(new Dimension(230, 160));
            setBackground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHover = false;
                    repaint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    String t = title.trim().toUpperCase();
                    switch (t) {
                        case "ADD BOOK":
                            openAddBook();
                            break;
                        case "VIEW BOOKS":
                            showBooksTable();
                            break;
                        case "SEARCH BOOKS":
                            openSearchBook();
                            break;
                        case "BORROW BOOK":
                            openBorrowBook();
                            break;
                        case "RETURN BOOK":
                            openReturnBook();
                            break;
                        case "STATISTICS":
                            showStatistics();
                            break;
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (isHover) {
                g2.setColor(new Color(250, 252, 255));
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.fillRoundRect(2, 2, w - 4, h - 4, 15, 15);

            g2.setColor(new Color(225, 230, 235));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(2, 2, w - 4, h - 4, 15, 15);

            g2.setColor(accentColor);
            g2.fillRoundRect(2, h - 8, w - 4, 6, 5, 5);
            g2.fillRect(2, h - 10, w - 4, 5);

            drawCardIcon(g2, w / 2, 65, accentColor);

            g2.setColor(new Color(30, 40, 50));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(title);
            g2.drawString(title, (w - textWidth) / 2, h - 35);
        }

        private void drawCardIcon(Graphics2D g2, int centerX, int centerY, Color color) {
            g2.setColor(color);
            int x = centerX - 25;
            int y = centerY - 25;

            switch (iconType) {
                case 1: // Add Book
                    g2.setStroke(new BasicStroke(3f));
                    g2.fillRoundRect(x, y, 28, 40, 6, 6);
                    g2.setColor(Color.WHITE);
                    g2.fillRect(x + 6, y + 8, 16, 2);
                    g2.fillRect(x + 6, y + 14, 12, 2);
                    g2.fillRect(x + 6, y + 20, 14, 2);
                    g2.setColor(color);
                    g2.fillOval(x + 25, y + 25, 22, 22);
                    g2.setColor(Color.WHITE);
                    g2.fillOval(x + 27, y + 27, 18, 18);
                    g2.setColor(color);
                    g2.setStroke(new BasicStroke(3f));
                    g2.drawLine(x + 36, y + 31, x + 36, y + 41);
                    g2.drawLine(x + 31, y + 36, x + 41, y + 36);
                    break;
                case 2: // View Books
                    g2.setStroke(new BasicStroke(3f));
                    g2.fillOval(x + 5, y + 5, 40, 30);
                    g2.setColor(Color.WHITE);
                    g2.fillOval(x + 10, y + 10, 30, 20);
                    g2.setColor(color);
                    g2.fillRect(x + 15, y + 35, 3, 15);
                    g2.fillRect(x + 32, y + 35, 3, 15);
                    break;
                case 3: // Search
                    g2.setStroke(new BasicStroke(3.5f));
                    g2.drawOval(x, y, 30, 30);
                    g2.drawLine(x + 25, y + 25, x + 45, y + 45);
                    break;
                case 4: // Borrow
                    g2.setStroke(new BasicStroke(3.5f));
                    g2.drawRoundRect(x, y, 30, 35, 5, 5);
                    g2.drawLine(x + 15, y, x + 15, y + 35);
                    g2.drawLine(x + 35, y + 17, x + 50, y + 17);
                    g2.drawLine(x + 45, y + 12, x + 50, y + 17);
                    g2.drawLine(x + 45, y + 22, x + 50, y + 17);
                    break;
                case 5: // Return
                    g2.setStroke(new BasicStroke(3.5f));
                    g2.drawRoundRect(x + 20, y, 30, 35, 5, 5);
                    g2.drawLine(x + 35, y, x + 35, y + 35);
                    g2.drawLine(x, y + 17, x + 15, y + 17);
                    g2.drawLine(x + 5, y + 12, x, y + 17);
                    g2.drawLine(x + 5, y + 22, x, y + 17);
                    break;
                case 7: // Statistics
                    g2.setStroke(new BasicStroke(3f));
                    g2.drawRect(x, y, 40, 40);
                    g2.fillRect(x + 5, y + 25, 6, 12);
                    g2.fillRect(x + 17, y + 15, 6, 22);
                    g2.fillRect(x + 29, y + 5, 6, 32);
                    break;
            }
        }
    }

    // Book Management Functions
    private void openAddBook() {
        JDialog dialog = new JDialog(this, "Add Book", true);
        dialog.setSize(420, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel titleLbl = new JLabel("Title:");
        titleLbl.setBounds(30, 30, 100, 25);
        JTextField titleField = new JTextField();
        titleField.setBounds(130, 30, 220, 28);

        JLabel authorLbl = new JLabel("Author:");
        authorLbl.setBounds(30, 80, 100, 25);
        JTextField authorField = new JTextField();
        authorField.setBounds(130, 80, 220, 28);

        JLabel priceLbl = new JLabel("Price:");
        priceLbl.setBounds(30, 130, 100, 25);
        JTextField priceField = new JTextField();
        priceField.setBounds(130, 130, 220, 28);

        JButton saveBtn = new JButton("Save");
        saveBtn.setBounds(130, 200, 120, 35);

        panel.add(titleLbl);
        panel.add(titleField);
        panel.add(authorLbl);
        panel.add(authorField);
        panel.add(priceLbl);
        panel.add(priceField);
        panel.add(saveBtn);

        saveBtn.addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(
                         "INSERT INTO Books (Title, Author, Price, IsBorrowed) VALUES (?, ?, ?, 0)")) {
                ps.setString(1, titleField.getText());
                ps.setString(2, authorField.getText());
                ps.setDouble(3, Double.parseDouble(priceField.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(dialog, "Book Added Successfully!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showBooksTable() {
        String[] columns = {"ID", "Title", "Author", "Price", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Books")) {

            while (rs.next()) {
                String status = rs.getBoolean("IsBorrowed") ? "Borrowed" : "Available";
                model.addRow(new Object[]{
                        rs.getInt("BookID"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getDouble("Price"),
                        status
                });
            }

            JTable table = new JTable(model);
            table.setRowHeight(25);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

            JScrollPane scrollPane = new JScrollPane(table);
            contentPanel.removeAll();
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void openSearchBook() {
        JDialog dialog = new JDialog(this, "Search Books", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Enter Title:");
        lbl.setBounds(30, 30, 100, 25);
        JTextField searchField = new JTextField();
        searchField.setBounds(130, 30, 220, 28);
        JButton searchBtn = new JButton("Search");
        searchBtn.setBounds(360, 30, 100, 30);

        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 80, 430, 250);

        panel.add(lbl);
        panel.add(searchField);
        panel.add(searchBtn);
        panel.add(scrollPane);

        searchBtn.addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(
                         "SELECT * FROM Books WHERE Title LIKE ?")) {
                ps.setString(1, "%" + searchField.getText() + "%");
                ResultSet rs = ps.executeQuery();

                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("ID");
                model.addColumn("Title");
                model.addColumn("Author");
                model.addColumn("Price");
                model.addColumn("Status");

                while (rs.next()) {
                    String status = rs.getBoolean("IsBorrowed") ? "Borrowed" : "Available";
                    model.addRow(new Object[]{
                            rs.getInt("BookID"),
                            rs.getString("Title"),
                            rs.getString("Author"),
                            rs.getDouble("Price"),
                            status
                    });
                }
                table.setModel(model);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openBorrowBook() {
        JDialog dialog = new JDialog(this, "Borrow Book", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Enter Book Title:");
        lbl.setBounds(30, 30, 150, 25);
        JTextField titleField = new JTextField();
        titleField.setBounds(170, 30, 180, 28);
        JButton borrowBtn = new JButton("Borrow");
        borrowBtn.setBounds(130, 80, 120, 35);

        panel.add(lbl);
        panel.add(titleField);
        panel.add(borrowBtn);

        borrowBtn.addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(
                         "UPDATE Books SET IsBorrowed = 1 WHERE Title = ? AND IsBorrowed = 0")) {
                ps.setString(1, titleField.getText());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(dialog, "Book Borrowed Successfully!");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Book Not Available or Not Found!");
                }
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openReturnBook() {
        JDialog dialog = new JDialog(this, "Return Book", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Enter Book Title:");
        lbl.setBounds(30, 30, 150, 25);
        JTextField titleField = new JTextField();
        titleField.setBounds(170, 30, 180, 28);
        JButton returnBtn = new JButton("Return");
        returnBtn.setBounds(130, 80, 120, 35);

        panel.add(lbl);
        panel.add(titleField);
        panel.add(returnBtn);

        returnBtn.addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(
                         "UPDATE Books SET IsBorrowed = 0 WHERE Title = ? AND IsBorrowed = 1")) {
                ps.setString(1, titleField.getText());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(dialog, "Book Returned Successfully!");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Book Not Borrowed or Not Found!");
                }
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void showStatistics() {
        JDialog dialog = new JDialog(this, "Book Statistics", true);
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("BOOK STATISTICS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(25, 45, 75));
        title.setBounds(110, 30, 250, 30);

        JLabel maxLbl = new JLabel();
        maxLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        maxLbl.setForeground(new Color(54, 104, 201));
        maxLbl.setBounds(60, 90, 300, 30);

        JLabel minLbl = new JLabel();
        minLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        minLbl.setForeground(new Color(229, 57, 53));
        minLbl.setBounds(60, 140, 300, 30);

        JLabel countLbl = new JLabel();
        countLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        countLbl.setForeground(new Color(76, 175, 80));
        countLbl.setBounds(60, 190, 300, 30);

        JLabel borrowedLbl = new JLabel();
        borrowedLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        borrowedLbl.setForeground(new Color(142, 68, 173));
        borrowedLbl.setBounds(60, 240, 300, 30);

        try (Connection con = getConnection();
             Statement st = con.createStatement()) {

            // Max Price
            ResultSet rs1 = st.executeQuery("SELECT MAX(Price) AS MaxPrice FROM Books");
            if (rs1.next()) {
                double maxPrice = rs1.getDouble("MaxPrice");
                maxLbl.setText("Maximum Price: $" + String.format("%.2f", maxPrice));
            }

            // Min Price
            ResultSet rs2 = st.executeQuery("SELECT MIN(Price) AS MinPrice FROM Books");
            if (rs2.next()) {
                double minPrice = rs2.getDouble("MinPrice");
                minLbl.setText("Minimum Price: $" + String.format("%.2f", minPrice));
            }

            // Total Books
            ResultSet rs3 = st.executeQuery("SELECT COUNT(*) AS TotalBooks FROM Books");
            if (rs3.next()) {
                int totalBooks = rs3.getInt("TotalBooks");
                countLbl.setText("Total Books: " + totalBooks);
            }

            // Borrowed Books
            ResultSet rs4 = st.executeQuery("SELECT COUNT(*) AS Borrowed FROM Books WHERE IsBorrowed = 1");
            if (rs4.next()) {
                int borrowed = rs4.getInt("Borrowed");
                borrowedLbl.setText("Borrowed Books: " + borrowed);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
        }

        panel.add(title);
        panel.add(maxLbl);
        panel.add(minLbl);
        panel.add(countLbl);
        panel.add(borrowedLbl);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openUpdateBook() {
        JDialog dialog = new JDialog(this, "Update Book", true);
        dialog.setSize(470, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel idLbl = new JLabel("Book ID:");
        idLbl.setBounds(30, 30, 100, 25);
        JTextField idField = new JTextField();
        idField.setBounds(150, 30, 250, 28);

        JLabel titleLbl = new JLabel("New Title:");
        titleLbl.setBounds(30, 80, 100, 25);
        JTextField titleField = new JTextField();
        titleField.setBounds(150, 80, 250, 28);

        JLabel authorLbl = new JLabel("New Author:");
        authorLbl.setBounds(30, 130, 100, 25);
        JTextField authorField = new JTextField();
        authorField.setBounds(150, 130, 250, 28);

        JLabel priceLbl = new JLabel("New Price:");
        priceLbl.setBounds(30, 180, 100, 25);
        JTextField priceField = new JTextField();
        priceField.setBounds(150, 180, 250, 28);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(150, 240, 120, 35);
        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(280, 240, 120, 35);

        panel.add(idLbl);
        panel.add(idField);
        panel.add(titleLbl);
        panel.add(titleField);
        panel.add(authorLbl);
        panel.add(authorField);
        panel.add(priceLbl);
        panel.add(priceField);
        panel.add(updateBtn);
        panel.add(deleteBtn);

        updateBtn.addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(
                         "UPDATE Books SET Title=?, Author=?, Price=? WHERE BookID=?")) {
                ps.setString(1, titleField.getText());
                ps.setString(2, authorField.getText());
                ps.setDouble(3, Double.parseDouble(priceField.getText()));
                ps.setInt(4, Integer.parseInt(idField.getText()));
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(dialog, "Book Updated Successfully!");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Book ID Not Found!");
                }
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Are you sure you want to delete this book?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection con = getConnection();
                     PreparedStatement ps = con.prepareStatement(
                             "DELETE FROM Books WHERE BookID=?")) {
                    ps.setInt(1, Integer.parseInt(idField.getText()));
                    int rows = ps.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(dialog, "Book Deleted Successfully!");
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Book Not Found!");
                    }
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
                }
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new HomePage("Admin").setVisible(true);
        });
    }
}