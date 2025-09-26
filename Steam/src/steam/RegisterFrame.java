/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.Date;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 *
 * @author saidn
 */
public class RegisterFrame extends JFrame {

    private JTextField photoPathField;
    private JLabel imagePreviewLabel;

    public RegisterFrame() {
        setTitle("Registro de Nuevo Usuario");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordText = new JPasswordField();

        JLabel nameLabel = new JLabel("Nombre Completo:");
        JTextField nameText = new JTextField();

        JLabel birthLabel = new JLabel("Fecha Nacimiento:");
        JDateChooser birthDateChooser = new JDateChooser();
        birthDateChooser.setDateFormatString("yyyy-MM-dd");

        JLabel photoLabel = new JLabel("Foto de Perfil:");
        JPanel photoSelectorPanel = new JPanel(new BorderLayout(5, 0));
        photoPathField = new JTextField();
        photoPathField.setEditable(false);
        JButton selectPhotoButton = new JButton("Seleccionar...");
        photoSelectorPanel.add(photoPathField, BorderLayout.CENTER);
        photoSelectorPanel.add(selectPhotoButton, BorderLayout.EAST);

        JLabel typeLabel = new JLabel("Tipo de Usuario:");
        String[] userTypes = {"NORMAL", "ADMIN"};
        JComboBox<String> typeComboBox = new JComboBox<>(userTypes);

        formPanel.add(userLabel);
        formPanel.add(userText);
        formPanel.add(passwordLabel);
        formPanel.add(passwordText);
        formPanel.add(nameLabel);
        formPanel.add(nameText);
        formPanel.add(birthLabel);
        formPanel.add(birthDateChooser);
        formPanel.add(photoLabel);
        formPanel.add(photoSelectorPanel);
        formPanel.add(typeLabel);
        formPanel.add(typeComboBox);

        JPanel imagePreviewPanel = new JPanel(new BorderLayout());
        imagePreviewPanel.setBorder(BorderFactory.createTitledBorder("Previsualización de Foto"));
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePreviewLabel.setVerticalAlignment(SwingConstants.CENTER);
        imagePreviewPanel.add(imagePreviewLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("Registrar");
        JButton cancelButton = new JButton("Cancelar");
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        bottomPanel.add(imagePreviewPanel);
        bottomPanel.add(buttonPanel);

        mainContentPanel.add(formPanel, BorderLayout.NORTH);
        mainContentPanel.add(bottomPanel, BorderLayout.CENTER);

        add(mainContentPanel);

        selectPhotoButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccione una imagen de perfil");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes (JPG, PNG, GIF)", "jpg", "png", "gif");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                photoPathField.setText(selectedFile.getAbsolutePath());
                displayImagePreview(selectedFile);
            }
        });

        registerButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Lógica de registro no implementada.");
        });

        cancelButton.addActionListener(e -> {
            this.dispose();
        });
    }

    private void displayImagePreview(File file) {
        try {
            BufferedImage originalImage = ImageIO.read(file);
            if (originalImage == null) {
                imagePreviewLabel.setIcon(null);
                imagePreviewLabel.setText("No es una imagen válida");
                return;
            }

            int labelWidth = 150;
            int labelHeight = 150;

            Image scaledImage = originalImage.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(scaledImage);

            imagePreviewLabel.setText("");
            imagePreviewLabel.setIcon(imageIcon);

        } catch (Exception ex) {
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("Error al cargar imagen");
            System.err.println("Error al cargar imagen: " + ex.getMessage());
        }
    }
}
