package steam;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserFrame extends JFrame {
    
    private LoginFrame loginFrame;
    private String loggedInUsername;
    private int loggedInUserCode;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTable gamesTable;
    private DefaultTableModel gamesModel;

    public UserFrame(LoginFrame loginFrame, String username) {
        this.loginFrame = loginFrame;
        this.loggedInUsername = username;
        
        setTitle("Bienvenido a Steam");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            Steam.Player player = Steam.getINSTANCE().getPlayerByUsername(username);
            if(player != null){
                this.loggedInUserCode = player.code();
            }
        } catch(IOException e){
            JOptionPane.showMessageDialog(null, "Error al cargar datos del usuario.");
        }
        
        createMenuBar();
        createMainPanel();
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu catalogMenu = new JMenu("Catalogo");
        JMenuItem viewCatalogItem = new JMenuItem("Ver Juegos y Descargar");
        catalogMenu.add(viewCatalogItem);
        JMenu profileMenu = new JMenu("Mi Perfil");
        JMenuItem viewProfileItem = new JMenuItem("Ver Mi Perfil");
        profileMenu.add(viewProfileItem);
        JMenu systemMenu = new JMenu("Sistema");
        JMenuItem logoutItem = new JMenuItem("Cerrar Sesion");
        systemMenu.add(logoutItem);
        menuBar.add(catalogMenu);
        menuBar.add(profileMenu);
        menuBar.add(systemMenu);
        setJMenuBar(menuBar);

        viewCatalogItem.addActionListener(e -> {
            refreshGamesTable();
            cardLayout.show(mainPanel, "CATALOG");
        });
        viewProfileItem.addActionListener(e -> showProfilePanel());
        logoutItem.addActionListener(e -> {
            this.dispose();
            loginFrame.setVisible(true);
        });
    }

    private void createMainPanel() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.add(new JLabel("Bienvenido. Explora el catalogo o revisa tu perfil."));
        mainPanel.add(welcomePanel, "WELCOME");
        mainPanel.add(createCatalogPanel(), "CATALOG");
        add(mainPanel);
    }

    private void showProfilePanel() {
        try {
            Steam.Player playerData = Steam.getINSTANCE().getPlayerByUsername(loggedInUsername);
            if (playerData != null) {
                mainPanel.add(createProfilePanel(playerData), "PROFILE");
                cardLayout.show(mainPanel, "PROFILE");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudieron cargar los datos del perfil.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch(IOException e) {
            JOptionPane.showMessageDialog(this, "Error de archivo al cargar el perfil.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createProfilePanel(Steam.Player player) {
        JPanel profilePanel = new JPanel(new BorderLayout(10,10));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel detailsPanel = new JPanel(new GridLayout(5, 2, 5, 10));
        detailsPanel.add(new JLabel("Codigo de Usuario:"));
        detailsPanel.add(new JLabel(String.valueOf(player.code())));
        detailsPanel.add(new JLabel("Username:"));
        detailsPanel.add(new JLabel(player.username()));
        detailsPanel.add(new JLabel("Nombre Completo:"));
        detailsPanel.add(new JLabel(player.fullName()));
        detailsPanel.add(new JLabel("Fecha de Nacimiento:"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy");
        detailsPanel.add(new JLabel(sdf.format(new Date(player.birthDate()))));
        detailsPanel.add(new JLabel("Descargas Totales:"));
        detailsPanel.add(new JLabel(String.valueOf(player.downloads())));
        
        profilePanel.add(detailsPanel, BorderLayout.CENTER);

        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(150, 150));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createTitledBorder("Foto de Perfil"));
        if (player.photoPath() != null && !player.photoPath().isEmpty()) {
            try {
                BufferedImage img = ImageIO.read(new File(player.photoPath()));
                Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImg));
            } catch (IOException e) {
                imageLabel.setText("No se pudo cargar la imagen");
            }
        } else {
            imageLabel.setText("Sin foto de perfil");
        }
        profilePanel.add(imageLabel, BorderLayout.EAST);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Retroceder");
        bottomPanel.add(backButton);
        profilePanel.add(bottomPanel, BorderLayout.SOUTH);
        
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "WELCOME"));
        
        return profilePanel;
    }
    
    private JPanel createCatalogPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] gameColumns = { "Codigo", "Titulo", "Genero", "SO", "Edad Min", "Precio", "Downloads" };
        gamesModel = new DefaultTableModel(gameColumns, 0){
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        gamesTable = new JTable(gamesModel);
        panel.add(new JScrollPane(gamesTable), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton backButton = new JButton("Retroceder al Menu");
        JButton downloadButton = new JButton("Descargar Juego Seleccionado");
        controlPanel.add(backButton);
        controlPanel.add(downloadButton);
        panel.add(controlPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "WELCOME"));
        downloadButton.addActionListener(e -> downloadGame());

        return panel;
    }

    private void refreshGamesTable() {
        gamesModel.setRowCount(0);
        try {
            GameNode current = Steam.getINSTANCE().printGames();
            while (current != null) {
                String os = "";
                switch (current.so) {
                    case 'W': os = "Windows"; break;
                    case 'M': os = "Mac"; break;
                    case 'L': os = "Linux"; break;
                }
                gamesModel.addRow(new Object[]{current.code, current.titulo, current.genero, os, current.edadMin, String.format("%.2f", current.precio), current.dls});
                current = current.next;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar juegos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void downloadGame(){
        int selectedRow = gamesTable.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un juego de la tabla para descargar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int gameCode = (int) gamesModel.getValueAt(selectedRow, 0);
        String[] osOptions = {"Windows", "Mac", "Linux"};
        String osChoice = (String) JOptionPane.showInputDialog(this, "Seleccione su sistema operativo:", "Confirmar Descarga", JOptionPane.QUESTION_MESSAGE, null, osOptions, osOptions[0]);

        if(osChoice != null){
            try {
                char os = osChoice.charAt(0);
                boolean success = Steam.getINSTANCE().downloadGame(gameCode, loggedInUserCode, os);
                if(success){
                    JOptionPane.showMessageDialog(this, "Descarga completada! El juego ha sido anadido a tu biblioteca.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                    refreshGamesTable();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo realizar la descarga. Verifique la compatibilidad de SO y la edad minima requerida.", "Descarga Fallida", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error de archivo durante la descarga: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}