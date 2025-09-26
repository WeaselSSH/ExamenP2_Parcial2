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
public class LoginFrame extends JFrame {

    private JTextField userText;
    private JPasswordField passwordText;

    public LoginFrame() {
        setTitle("Steam Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        userText = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordText = new JPasswordField();
        JButton loginButton = new JButton("Entrar");
        JButton registerButton = new JButton("Registrar Usuario");

        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(loginButton);
        panel.add(registerButton);

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
                        UserFrame userWindow = new UserFrame(this);
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