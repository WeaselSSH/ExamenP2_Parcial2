package steam;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AddGameDialog extends JDialog {

    private JTextField titleField = new JTextField();
    private JTextField genreField = new JTextField();
    private JComboBox<String> osComboBox = new JComboBox<>(new String[] { "Windows", "Mac", "Linux" });
    private JTextField ageField = new JTextField();
    private JTextField priceField = new JTextField();
    private JTextField photoField = new JTextField();
    private JButton selectPhotoButton = new JButton("Seleccionar...");

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
        formPanel.add(new JLabel("Foto del Juego:"));

        JPanel photoSelectorPanel = new JPanel(new BorderLayout(5, 0));
        photoField.setEditable(false); 
        photoSelectorPanel.add(photoField, BorderLayout.CENTER);
        photoSelectorPanel.add(selectPhotoButton, BorderLayout.EAST);
        formPanel.add(photoSelectorPanel);
        
        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Anadir");
        JButton cancelButton = new JButton("Cancelar");
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addGame());
        cancelButton.addActionListener(e -> dispose());
        selectPhotoButton.addActionListener(e -> selectPhoto());
    }
    
    private void selectPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione una imagen para el juego");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Imagenes (JPG, PNG, GIF)", "jpg", "png", "gif");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            photoField.setText(selectedFile.getAbsolutePath());
        }
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

            if (titulo.isEmpty() || genero.isEmpty() || fotoPath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error",
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