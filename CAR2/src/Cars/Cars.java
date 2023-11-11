/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cars;

import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author Balloon
 */
public class Cars extends JFrame {
    public static void main(String[] args) throws IOException{
        JFrame app=new JFrame();
        work w=new work();
        app.add(w);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(500,720);
        app.setVisible(true);
    }
    
}
