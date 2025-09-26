package steam;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginFrame extends JFrame {

    private JTextField userText;
    private JPasswordField passwordText;

    public LoginFrame() {
        setTitle("Steam Login");
        setSize(400, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        userText = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(userText, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        passwordText = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordText, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton loginButton = new JButton("Entrar");
        JButton registerButton = new JButton("Registrar Usuario");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        add(panel);

        loginButton.addActionListener(e -> attemptLogin());

        registerButton.addActionListener(e -> {
            this.setVisible(false);
            RegisterFrame registerWindow = new RegisterFrame(this);
            registerWindow.setVisible(true);
        });
    }

    private void attemptLogin() {
        String username = userText.getText();
        String password = new String(passwordText.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El usuario y la contrasena no pueden estar vacios.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String userType = Steam.getINSTANCE().login(username, password);

            if (userType != null) {
                switch (userType) {
                    case "ADMIN":
                        AdminFrame adminWindow = new AdminFrame(this);
                        adminWindow.setVisible(true);
                        this.dispose();
                        break;
                    case "NORMAL":
                        UserFrame userWindow = new UserFrame(this, username);
                        userWindow.setVisible(true);
                        this.dispose();
                        break;
                    case "INACTIVE":
                        JOptionPane.showMessageDialog(this, "Esta cuenta ha sido desactivada.", "Login Fallido", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contrasena incorrectos.", "Login Fallido", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de usuarios: " + ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }
}
