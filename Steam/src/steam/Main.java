/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package steam;

import javax.swing.SwingUtilities;

/**
 *
 * @author saidn
 */
public class Main {
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
