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
    
    private LoginFrame loginFrame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTable gamesTable;
    private DefaultTableModel gamesModel;
    private JTable usersTable;
    private DefaultTableModel usersModel;

    public AdminFrame(LoginFrame loginFrame) {
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
        JMenu catalogMenu = new JMenu("Catalogo");
        JMenuItem viewCatalogItem = new JMenuItem("Gestionar Catalogo");
        catalogMenu.add(viewCatalogItem);
        JMenu usersMenu = new JMenu("Usuarios");
        JMenuItem viewUsersItem = new JMenuItem("Gestionar Usuarios");
        JMenuItem reportItem = new JMenuItem("Generar Reporte de Cliente");
        usersMenu.add(viewUsersItem);
        usersMenu.add(reportItem);
        JMenu systemMenu = new JMenu("Sistema");
        JMenuItem logoutItem = new JMenuItem("Cerrar Sesion");
        systemMenu.add(logoutItem);
        menuBar.add(catalogMenu);
        menuBar.add(usersMenu);
        menuBar.add(systemMenu);
        setJMenuBar(menuBar);
        viewCatalogItem.addActionListener(e -> {
            refreshGamesTable();
            cardLayout.show(mainPanel, "CATALOG");
        });
        viewUsersItem.addActionListener(e -> {
            refreshUsersTable();
            cardLayout.show(mainPanel, "USERS");
        });
        reportItem.addActionListener(e -> generateClientReport());
        logoutItem.addActionListener(e -> logout());
    }

    private void createMainPanel() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.add(new JLabel("Bienvenido, Admin. Seleccione una opcion del menu para comenzar."));
        mainPanel.add(welcomePanel, "WELCOME");
        mainPanel.add(createCatalogPanel(), "CATALOG");
        mainPanel.add(createUsersPanel(), "USERS");
        add(mainPanel);
    }

    private JPanel createCatalogPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] gameColumns = { "Codigo", "Titulo", "Genero", "Precio", "Edad Min", "Downloads" };
        gamesModel = new DefaultTableModel(gameColumns, 0);
        gamesTable = new JTable(gamesModel);
        panel.add(new JScrollPane(gamesTable), BorderLayout.CENTER);
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton backButton = new JButton("Retroceder al Menu");
        JButton addButton = new JButton("Anadir Juego");
        JButton editButton = new JButton("Modificar Precio");
        JButton deleteButton = new JButton("Eliminar Juego");
        controlPanel.add(backButton);
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        panel.add(controlPanel, BorderLayout.SOUTH);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "WELCOME"));
        addButton.addActionListener(e -> addGame());
        editButton.addActionListener(e -> editGamePrice());
        deleteButton.addActionListener(e -> deleteGame());
        return panel;
    }
    
    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        String[] userColumns = { "Codigo", "Username", "Nombre", "Tipo", "Estado" };
        usersModel = new DefaultTableModel(userColumns, 0);
        usersTable = new JTable(usersModel);
        panel.add(new JScrollPane(usersTable), BorderLayout.CENTER);
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton backButton = new JButton("Retroceder al Menu");
        JButton activateButton = new JButton("Activar Usuario");
        JButton deactivateButton = new JButton("Desactivar Usuario");
        controlPanel.add(backButton);
        controlPanel.add(activateButton);
        controlPanel.add(deactivateButton);
        panel.add(controlPanel, BorderLayout.SOUTH);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "WELCOME"));
        activateButton.addActionListener(e -> updateUserStatus(true));
        deactivateButton.addActionListener(e -> updateUserStatus(false));
        return panel;
    }
    
    private void refreshGamesTable() {
        JOptionPane.showMessageDialog(this, "Funcionalidad no disponible: Steam.java no tiene un metodo para listar todos los juegos.");
    }
    
    private void refreshUsersTable() {
        JOptionPane.showMessageDialog(this, "Funcionalidad no disponible: Steam.java no tiene un metodo para listar todos los usuarios.");
    }
    
    private void addGame() {
        AddGameDialog dialog = new AddGameDialog(this);
        dialog.setVisible(true);
        refreshGamesTable();
    }

    private void editGamePrice() {
        int selectedRow = gamesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un juego de la tabla para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int gameCode = (int) gamesModel.getValueAt(selectedRow, 0);
        String newPriceStr = JOptionPane.showInputDialog(this, "Ingrese el nuevo precio para el juego con codigo " + gameCode + ":");

        if (newPriceStr != null && !newPriceStr.isEmpty()) {
            try {
                double newPrice = Double.parseDouble(newPriceStr);
                if (Steam.getINSTANCE().updatePriceFor(gameCode, newPrice)) {
                    JOptionPane.showMessageDialog(this, "Precio actualizado correctamente.");
                    refreshGamesTable();
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontro el juego.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un numero valido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error de archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteGame() {
        JOptionPane.showMessageDialog(this, "Funcionalidad no disponible: Steam.java no tiene un metodo para eliminar juegos.");
    }
    
    private void updateUserStatus(boolean newStatus) {
        JOptionPane.showMessageDialog(this, "Funcionalidad no disponible: Steam.java no tiene un metodo para cambiar el estado de un usuario.");
    }
    
    private void generateClientReport() {
        JOptionPane.showMessageDialog(this, "Funcionalidad no disponible: Steam.java no tiene un metodo para generar reportes.");
    }

    private void logout() {
        this.dispose();
        loginFrame.setVisible(true);
    }
}