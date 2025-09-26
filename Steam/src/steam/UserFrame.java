/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam;

/**
 *
 * @author saidn
 */
import javax.swing.*;
import java.awt.*;

public class UserFrame extends JFrame {

    public UserFrame() {
        setTitle("Bienvenido a Steam");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu catalogMenu = new JMenu("Catálogo");
        JMenuItem viewCatalogItem = new JMenuItem("Ver Juegos Disponibles");
        catalogMenu.add(viewCatalogItem);

        JMenu profileMenu = new JMenu("Mi Perfil");
        JMenuItem viewDownloadsItem = new JMenuItem("Ver Mi Biblioteca / Descargas");
        profileMenu.add(viewDownloadsItem);

        JMenu systemMenu = new JMenu("Sistema");
        JMenuItem logoutItem = new JMenuItem("Cerrar Sesión");
        systemMenu.add(logoutItem);

        menuBar.add(catalogMenu);
        menuBar.add(profileMenu);
        menuBar.add(systemMenu);
        setJMenuBar(menuBar);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JLabel("Bienvenido. Explora el catálogo o revisa tu perfil.", SwingConstants.CENTER), BorderLayout.CENTER);
        add(mainPanel);
        

        
        viewCatalogItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funcionalidad 'Ver Catálogo' no implementada.");
        });   
        logoutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funcionalidad 'Cerrar Sesión' no implementada.");
        });
    }
}
