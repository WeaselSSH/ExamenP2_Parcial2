package steam;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class RegisterFrame extends JFrame {

    private LoginFrame loginFrame;
    private JTextField photoPathField;
    private JLabel imagePreviewLabel;
    private JTextField userText;
    private JPasswordField passwordText;
    private JTextField nameText;
    private JDateChooser birthDateChooser;
    private JComboBox<String> typeComboBox;

    public RegisterFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        setTitle("Registro de Nuevo Usuario");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        userText = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordText = new JPasswordField();
        JLabel nameLabel = new JLabel("Nombre Completo:");
        nameText = new JTextField();
        JLabel birthLabel = new JLabel("Fecha Nacimiento:");
        birthDateChooser = new JDateChooser();
        birthDateChooser.setDateFormatString("yyyy-MM-dd");

        birthDateChooser.getJCalendar().setWeekOfYearVisible(false);
        birthDateChooser.setMaxSelectableDate(new Date());
        
        JLabel photoLabel = new JLabel("Foto de Perfil:");
        JPanel photoSelectorPanel = new JPanel(new BorderLayout(5, 0));
        photoPathField = new JTextField();
        photoPathField.setEditable(false);
        JButton selectPhotoButton = new JButton("Seleccionar...");
        photoSelectorPanel.add(photoPathField, BorderLayout.CENTER);
        photoSelectorPanel.add(selectPhotoButton, BorderLayout.EAST);
        JLabel typeLabel = new JLabel("Tipo de Usuario:");
        String[] userTypes = {"NORMAL", "ADMIN"};
        typeComboBox = new JComboBox<>(userTypes);
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
        imagePreviewPanel.setBorder(BorderFactory.createTitledBorder("Previsualizacion de Foto"));
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePreviewLabel.setVerticalAlignment(SwingConstants.CENTER);
        imagePreviewPanel.add(imagePreviewLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
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

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                loginFrame.setVisible(true);
            }
        });

        selectPhotoButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccione una imagen de perfil");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Imagenes (JPG, PNG, GIF)", "jpg", "png", "gif");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                photoPathField.setText(selectedFile.getAbsolutePath());
                displayImagePreview(selectedFile);
            }
        });

        registerButton.addActionListener(e -> registerUser());

        cancelButton.addActionListener(e -> {
            loginFrame.setVisible(true);
            this.dispose();
        });
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasSymbol = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSymbol = true;
            }
        }
        return hasUpper && hasLower && hasSymbol;
    }

    private void registerUser() {
        try {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());
            String fullName = nameText.getText();
            Date birthDate = birthDateChooser.getDate();
            String photoPath = photoPathField.getText();
            String userType = (String) typeComboBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, llene todos los campos de texto.", "Campos Vacios", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (birthDate == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una fecha de nacimiento.", "Fecha Faltante", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isPasswordValid(password)) {
                JOptionPane.showMessageDialog(this, "La contrasena debe tener al menos 8 caracteres, una mayuscula, una minuscula y un simbolo.", "Contrasena Invalida", JOptionPane.WARNING_MESSAGE);
                return; 
            }

            long birthMillis = birthDate.getTime();
            Steam.getINSTANCE().addPlayer(username, password, fullName, birthMillis, photoPath, userType);
            JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            loginFrame.setVisible(true);
            this.dispose();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el usuario en el archivo: " + ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrio un error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayImagePreview(File file) {
        try {
            BufferedImage originalImage = ImageIO.read(file);
            if (originalImage == null) {
                imagePreviewLabel.setIcon(null);
                imagePreviewLabel.setText("No es una imagen valida");
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