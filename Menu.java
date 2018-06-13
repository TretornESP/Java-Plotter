import javax.swing.*; //Importamos las librerias
import java.awt.*;
import java.awt.event.*;

/*
 * Clase que situa los componentes del menú
 */
public class Menu extends JPanel //Esta clase hereda de JPanel
{   
    private JTextField ecuacion1; //Nuestros objetos declarados
    private JTextField ecuacion2;
    
    private JButton graficar;
    private JButton salir;
    
    /*
     * Configuramos los botones
     */
    private void setup_buttons() {
        JPanel buttonsPane = new JPanel();
        buttonsPane.setLayout(new BoxLayout(buttonsPane, BoxLayout.LINE_AXIS)); //Creamos un panel para los botones con layout BoxLayout
        buttonsPane.setAlignmentX(JPanel.CENTER_ALIGNMENT); //Aliamos los botones al centro
        
        graficar = new JButton("Graficar"); //Creamos el boton de graficar
        graficar.setAlignmentX(Component.CENTER_ALIGNMENT); //Alineamos al centro
        graficar.addActionListener(new ActionListener() { //Añadimos el actionListener (lo que ocurre cuando haces click) para el boton de graficar
        
            @Override
            public void actionPerformed(ActionEvent e) { //Evento que se ejecuta al hacer click
                Plotter.setEcuacion1(ecuacion1.getText()); //Mandamos el contenido de los JTextField a la clase Plotter
                Plotter.setEcuacion2(ecuacion2.getText());
                
                new Ejes(); //Creamos el canvas
            }
        });
        
        salir = new JButton("Salir"); //Creamos el boton de salir
        salir.setAlignmentX(JPanel.CENTER_ALIGNMENT); //Alineamos al centro
        salir.addActionListener(new ActionListener() { //Añadimos el actionListener
        
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); //Salimos con codigo de error 0
            }
        });   
        
        buttonsPane.add(graficar); //Añadimos el boton de graficar
        buttonsPane.add(Box.createRigidArea(new Dimension(5, 20))); //Dejamos un espacio ente medias
        buttonsPane.add(salir); //Añadimso el boton de salir
        this.add(buttonsPane); //Añadimos el panel
    }
    
    /*
     * Configuramos el menu principal
     */
    private void setup() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS)); //Seleccionamos el layout BoxLayout
        
        Font font = new Font("SansSerif", Font.BOLD, 20); //Declaramos la fuente que vamos a usar

        ecuacion1 = new JTextField(50); //Añadimos el primer cuadro de texto
        ecuacion1.setPreferredSize(new Dimension(600, 30)); //Definimos su tamaño
        ecuacion1.setFont(font); //Definimos su fuente
        
        ecuacion2 = new JTextField(50); //Añadimos el segundo cuadro de texto
        ecuacion2.setPreferredSize(new Dimension(600, 30)); //Definimos su tamaño
        ecuacion2.setFont(font); //Definimos su fuente
                       
        this.add(new JLabel("Ecuacion 1:")); //Creamos un JLabel
        this.add(ecuacion1); //Situamos el JTextField1
        this.add(new JLabel("Ecuacion 2:")); //Creamos otro JLabel
        this.add(ecuacion2); //Situamos el JTextField2
        
        this.add(Box.createRigidArea(new Dimension(600, 20))); //Dejamos un espacio
        setup_buttons(); //Añadimos los botones
    } 
    
    /*
     * Llamamos a setup
     */
    public Menu() {
        setup();
    }
}
