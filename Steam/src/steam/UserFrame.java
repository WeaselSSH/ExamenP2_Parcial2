/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author saidn
 */
public class UserFrame extends JFrame {
    
    private LoginFrame loginFrame;
    private String loggedInUsername;
    private int loggedInUserCode;
    private JPanel mainPanel;
    private CardLayout cardLayout;

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

        viewCatalogItem.addActionListener(e -> showCatalogPanel());
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
    
    private void showCatalogPanel() {
        JOptionPane.showMessageDialog(this, "Funcionalidad no disponible: Steam.java no tiene un metodo para listar todos los juegos.");
    }
}