/*
 * Graficador de funciones de primer y segundo grado
 * @Version 1.0
 * @Author: Miguel
 */

import java.awt.*; // importamos las librerías 
import javax.swing.*;

public class Main extends JFrame //Main pertenece a la clase padre JFrame
{
    /*
     * Configuramos la ventana
     */
    private void setup() {
        this.setTitle("Graficador de funciones");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /*
     * Creamos la ventana y creamos un objeto Menu
     */
    public Main() {
        setup();
        this.add(new Menu());
        this.pack();
        this.setVisible(true);
    }
    
    /*
     * Función main, crea el objeto principal del programa
     */
    public static void main(String[] args) { 
        new Main();
    }
}
