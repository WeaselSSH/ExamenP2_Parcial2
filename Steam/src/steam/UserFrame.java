/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam;

import java.awt.BorderLayout;
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

    public UserFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        setTitle("Bienvenido a Steam");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu catalogMenu = new JMenu("Catalogo");
        JMenuItem viewCatalogItem = new JMenuItem("Ver Juegos Disponibles");
        catalogMenu.add(viewCatalogItem);

        JMenu profileMenu = new JMenu("Mi Perfil");
        JMenuItem viewDownloadsItem = new JMenuItem("Ver Mi Biblioteca / Descargas");
        profileMenu.add(viewDownloadsItem);

        JMenu systemMenu = new JMenu("Sistema");
        JMenuItem logoutItem = new JMenuItem("Cerrar Sesion");
        systemMenu.add(logoutItem);

        menuBar.add(catalogMenu);
        menuBar.add(profileMenu);
        menuBar.add(systemMenu);
        setJMenuBar(menuBar);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JLabel("Bienvenido. Explora el catalogo o revisa tu perfil.", SwingConstants.CENTER),
                BorderLayout.CENTER);
        add(mainPanel);

        viewCatalogItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funcionalidad 'Ver Catalogo' no implementada.");
        });
        logoutItem.addActionListener(e -> {
            this.dispose();
            loginFrame.setVisible(true);
        });
    }
}