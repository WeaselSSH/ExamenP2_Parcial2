/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
/**
 *
 * @author saidn
 */
public class AdminFrame extends JFrame {

    private Steam steam;
    private LoginFrame loginFrame;
    
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTable gamesTable;
    private DefaultTableModel gamesModel;
    private JTable usersTable;
    private DefaultTableModel usersModel;

    public AdminFrame(Steam steam, LoginFrame loginFrame) {
        this.steam = steam;
        this.loginFrame = loginFrame;

        setTitle("Admin");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createMenuBar();
        createMainPanel();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu catalogMenu = new JMenu("Catálogo");
        JMenuItem viewCatalogItem = new JMenuItem("Gestionar Catálogo");
        catalogMenu.add(viewCatalogItem);

        JMenu usersMenu = new JMenu("Usuarios");
        JMenuItem viewUsersItem = new JMenuItem("Gestionar Usuarios");
        JMenuItem reportItem = new JMenuItem("Generar Reporte de Cliente");
        usersMenu.add(viewUsersItem);
        usersMenu.add(reportItem);

        JMenu systemMenu = new JMenu("Sistema");
        JMenuItem logoutItem = new JMenuItem("Cerrar Sesión");
        systemMenu.add(logoutItem);

        menuBar.add(catalogMenu);
        menuBar.add(usersMenu);
        menuBar.add(systemMenu);
        setJMenuBar(menuBar);

        viewCatalogItem.addActionListener(e -> cardLayout.show(mainPanel, "CATALOG"));
        viewUsersItem.addActionListener(e -> cardLayout.show(mainPanel, "USERS"));
        reportItem.addActionListener(e -> generateClientReport());
        logoutItem.addActionListener(e -> logout());
    }

    private void createMainPanel() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.add(new JLabel("Bienvenido, Admin. Seleccione una opción del menú para comenzar."));
        
        mainPanel.add(welcomePanel, "WELCOME");
        mainPanel.add(createCatalogPanel(), "CATALOG");
        mainPanel.add(createUsersPanel(), "USERS");
        
        add(mainPanel);
    }

    private JPanel createCatalogPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] gameColumns = {"Código", "Título", "Género", "Precio", "Edad Min", "Downloads"};
        gamesModel = new DefaultTableModel(gameColumns, 0);
        gamesTable = new JTable(gamesModel);
        panel.add(new JScrollPane(gamesTable), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton addButton = new JButton("Añadir Juego");
        JButton editButton = new JButton("Modificar Precio");
        JButton deleteButton = new JButton("Eliminar Juego");
        
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        addButton.addActionListener(e -> addGame());
        editButton.addActionListener(e -> editGamePrice());
        deleteButton.addActionListener(e -> deleteGame());
        
        return panel;
    }
    
    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] userColumns = {"Código", "Username", "Nombre", "Tipo", "Estado"};
        usersModel = new DefaultTableModel(userColumns, 0);
        usersTable = new JTable(usersModel);
        panel.add(new JScrollPane(usersTable), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton activateButton = new JButton("Activar Usuario");
        JButton deactivateButton = new JButton("Desactivar Usuario");
        
        controlPanel.add(activateButton);
        controlPanel.add(deactivateButton);
        panel.add(controlPanel, BorderLayout.SOUTH);

        activateButton.addActionListener(e -> updateUserStatus(true));
        deactivateButton.addActionListener(e -> updateUserStatus(false));

        return panel;
    }

    private void refreshGamesTable() {
        JOptionPane.showMessageDialog(this, "Funcionalidad 'Refrescar Tabla de Juegos' no implementada.");
    }
    
    private void refreshUsersTable() {
        JOptionPane.showMessageDialog(this, "Funcionalidad 'Refrescar Tabla de Usuarios' no implementada.");
    }
    
    private void addGame() {
        AddGameDialog dialog = new AddGameDialog(this, steam);
        dialog.setVisible(true);
    }

    private void editGamePrice() {
        JOptionPane.showMessageDialog(this, "Funcionalidad 'Modificar Precio' no implementada.");
    }

    private void deleteGame() {
        JOptionPane.showMessageDialog(this, "Funcionalidad 'Eliminar Juego' no implementada.");
    }
    
    private void updateUserStatus(boolean newStatus) {
        JOptionPane.showMessageDialog(this, "Funcionalidad 'Actualizar Estado de Usuario' no implementada.");
    }
    
    private void generateClientReport() {
        JOptionPane.showMessageDialog(this, "Funcionalidad 'Generar Reporte' no implementada.");
    }

    private void logout() {
        this.dispose();
        loginFrame.setVisible(true);
    }
}