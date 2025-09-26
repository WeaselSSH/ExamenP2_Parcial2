/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 *
 * @author saidn
 */

public class AddGameDialog extends JDialog {

    private JTextField titleField = new JTextField();
    private JTextField genreField = new JTextField();
    private JComboBox<String> osComboBox = new JComboBox<>(new String[] { "Windows", "Mac", "Linux" });
    private JTextField ageField = new JTextField();
    private JTextField priceField = new JTextField();
    private JTextField photoField = new JTextField();

    public AddGameDialog(JFrame parent) {
        super(parent, "Anadir Nuevo Juego", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Titulo:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Genero:"));
        formPanel.add(genreField);
        formPanel.add(new JLabel("Sistema Operativo:"));
        formPanel.add(osComboBox);
        formPanel.add(new JLabel("Edad Minima:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Precio:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Ruta de Foto:"));
        formPanel.add(photoField);
        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Anadir");
        JButton cancelButton = new JButton("Cancelar");
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addGame());
        cancelButton.addActionListener(e -> dispose());
    }

    private void addGame() {
        try {
            String titulo = titleField.getText();
            String genero = genreField.getText();
            String osSelection = (String) osComboBox.getSelectedItem();
            char sistemaOperativo = 'W';
            if (osSelection != null) {
                sistemaOperativo = osSelection.charAt(0);
            }

            int edadMinima = Integer.parseInt(ageField.getText());
            double precio = Double.parseDouble(priceField.getText());
            String fotoPath = photoField.getText();

            if (titulo.isEmpty() || genero.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Titulo y Genero no pueden estar vacios.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Steam.getINSTANCE().addGame(titulo, genero, sistemaOperativo, edadMinima, precio, fotoPath);
            JOptionPane.showMessageDialog(this, "Juego anadido exitosamente.", "Exito",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Edad y Precio deben ser numeros validos.", "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al anadir el juego: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}