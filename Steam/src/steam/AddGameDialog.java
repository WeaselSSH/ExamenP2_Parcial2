/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author saidn
 */

public class AddGameDialog extends JDialog {
    private Steam steam;

    private JTextField titleField = new JTextField();
    private JTextField genreField = new JTextField();
    private JComboBox<String> osComboBox = new JComboBox<>(new String[]{"Windows", "Mac", "Linux"});
    private JTextField ageField = new JTextField();
    private JTextField priceField = new JTextField();
    private JTextField photoField = new JTextField();

    public AddGameDialog(JFrame parent, Steam steam) {
        super(parent, "Añadir Nuevo Juego", true);
        this.steam = steam;
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Título:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Género:"));
        formPanel.add(genreField);
        formPanel.add(new JLabel("Sistema Operativo:"));
        formPanel.add(osComboBox);
        formPanel.add(new JLabel("Edad Mínima:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Precio:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Ruta de Foto:"));
        formPanel.add(photoField);
        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Añadir");
        JButton cancelButton = new JButton("Cancelar");
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addGame());
        cancelButton.addActionListener(e -> dispose());
    }

    private void addGame() {
        JOptionPane.showMessageDialog(this, "Funcionalidad 'Añadir Juego' no implementada.");
    }
}
