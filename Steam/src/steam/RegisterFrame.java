/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author saidn
 */
import com.toedter.calendar.JDateChooser; 
import javax.swing.*;
import java.awt.*;
import java.util.Date; 

public class RegisterFrame extends JFrame {

    public RegisterFrame() {
        setTitle("Registro de Nuevo Usuario");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null); 

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordText = new JPasswordField();

        JLabel nameLabel = new JLabel("Nombre Completo:");
        JTextField nameText = new JTextField();

        JLabel birthLabel = new JLabel("Fecha Nacimiento:");
        JDateChooser birthDateChooser = new JDateChooser();
        birthDateChooser.setDateFormatString("yyyy-MM-dd");

        JLabel photoLabel = new JLabel("Ruta de la Foto de Perfil:");
        JTextField photoText = new JTextField();

        JLabel typeLabel = new JLabel("Tipo de Usuario:");
        String[] userTypes = {"NORMAL", "ADMIN"};
        JComboBox<String> typeComboBox = new JComboBox<>(userTypes);
        
        JButton registerButton = new JButton("Registrar");
        JButton cancelButton = new JButton("Cancelar");

        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(nameLabel);
        panel.add(nameText);
        panel.add(birthLabel);

        panel.add(birthDateChooser); 
        panel.add(photoLabel);
        panel.add(photoText);
        panel.add(typeLabel);
        panel.add(typeComboBox);
        panel.add(registerButton);
        panel.add(cancelButton);

        add(panel);

        registerButton.addActionListener(e -> {

            Date fechaSeleccionada = birthDateChooser.getDate();
            
            if (fechaSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona una fecha de nacimiento.");
            } else {

                long milisegundosNacimiento = fechaSeleccionada.getTime();
                JOptionPane.showMessageDialog(this, "Lógica de registro no implementada.\nFecha en millis: " + milisegundosNacimiento);
            }
        });

        cancelButton.addActionListener(e -> {
            this.dispose(); 
        });
    }
}