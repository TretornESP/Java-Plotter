import java.awt.*; //Importamos las librerias
import javax.swing.*;

/*
 * Clase que genera los ejes y el canvas sobre el que dibujaremos las graficas
 */

public class Ejes extends JFrame //Heredamos de JFrame
{   
    /*
     * Declaramos nuestros objetos y parametros constantes
     */
    private JPanel panel; 
    private JProgressBar bar;
    private static final int LINE_THICKNESS = 2; //Finura de la linea de la gráfica, se puede cambiar al gusto
    private static final int AXIS_THICKNESS = 1; //Finura de los ejes, se puede cambiar al gusto
    
    private static final Color bgcolor = new Color(240, 220, 220); //Color de fondo, se puede cambiar al gusto
    
    /*
     * Configuramos el panel y el handle para dibujar g2d
     */
    private void setup() {
        
        this.setSize(800,800); //Generamos una ventana de 800,800
    
        bar = new JProgressBar(); //Creamos una barra de progreso y la configuramos
        bar.setMinimum((int)Plotter.cota_inferior);
        bar.setMaximum((int)Plotter.cota_superior);
        
        panel = new JPanel() { //Creamos una clase anonima para el panel (Canvas)
            @Override
            public void paint(Graphics e) { //Modificamos el metodo paint de la clase JPanel
                super.paint(e); //Borramos la pantalla
                
                Graphics2D g2d = (Graphics2D) e; //Generamos un objeto de tipo Graphics2D a partir del objeto Graphics 
                g2d.translate(this.getSize().width/2, this.getSize().height/2); //Situamos el centro de la pantalla como origen de coordenadas
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //Activamos el antialiassing
                
                Plotter.MAX_X = this.getSize().width; //Mandamos a la clase plotter el tamaño actual de la pantalla
                Plotter.MAX_Y = this.getSize().height;
                
                g2d.setStroke(new BasicStroke(AXIS_THICKNESS)); //Seleccionamos el grosor de la linea deseado
                Plotter.drawAxis(g2d); //Llamamos a la funcion que dibuja los ejes
                g2d.setStroke(new BasicStroke(LINE_THICKNESS)); 
                Plotter.draw(g2d); //Llamamos a la funcion que dibuja las ecuaciones
            }
        };
        
        panel.setBackground(bgcolor); //Ponemos el color de fondo
        panel.add(bar); //añadimos la barra de progreso
        
        this.setTitle("Function Plotter"); //Titulo de la ventana
        this.setSize(Plotter.MAX_X, Plotter.MAX_Y); //Tamaño de la ventana
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Operacion de cierre por defecto
        this.setContentPane(panel); //Añadimos como panel de contenidos el JPanel que acabamos de crear
        this.setVisible(true); //Mostramos todo
    }
    
    /* 
     * Funcion principal, llama a configuracion y a calcular
     */
    public Ejes() {
        setup();
        Plotter.calculate(bar, panel); //Le pasamos la barra de progreso a la funcion para que la vaya actualizando, y el jpanel para que cuando termine de la orden de repintar
    }
    
}
