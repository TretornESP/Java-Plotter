/*
 * Importamos los paquetes de graficos
 */
import java.awt.*; 
import java.awt.geom.*;
import javax.swing.*;

/*
 * Importamos el parser
 */
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/*
 * Importamos el paquete ArrayList
 */
import java.util.ArrayList;


/*
 * Esta clase se compone de funciones, campos y métodos de tipo estático cuyas funciones son calcular y dibujar la grafica de dos ecuaciones dadas
 */
public class Plotter
{
    
    /*
     * Definimos constantes y parametros de configuracion
     */
    public static int MAX_X = 800; //Estos valores se modifican en runtime, están puestos a 800 como valor inicial
    public static int MAX_Y = 800;
    
    private static final int espacio_ejes = 20; //Espacio entre las divisiones en los ejes, se puede cambiar al gusto
    private static final int profundidad_ejes = 5; //Profundidad de las divisiones de los ejes, se puede cambiar al gusto
    private static final boolean mostrar_unidades = false; //Si se pone a true, muestra las unidades, lo dejo a false porque empeora la claridad con la configuración actual.
    
    private static final Color color1 = new Color(100, 120, 240); //Color de la primera gráfica, se puede cambiar al gusto
    private static final Color color2 = new Color(240, 120, 120); //Color de la segunda gráfica, se puede modificar al gusto
    /*
     * Estos son los parametros mas importantes del programa, rigen desde donde a donde se evalua la expresión, un mayor rango da una información mas completa a cambio de mas tiempo de cómputo (lineal)
     * Un paso mas pequeño da mas precisión a costa de mas tiempo de computo (exponencial)
     */
    public static double cota_inferior = -100; //Número mas bajo que se evalua
    public static double cota_superior = 100; //Número mas alto que se evalua
    private static double paso = 0.1; //Paso entre los dos números
    
    private static String ecuacion1; //Declaramos sendas Strings para nuestras ecuaciones
    private static String ecuacion2;
    
    /*
     * Declaramos el motor de Scripting que traducirá nuestras Strings a ecuaciones matemáticas (en este caso usaremos JavaScript embebido)
     */
    private static ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
    
    //Declaramos arraylists para guardar las lineas punto a punto que vayamos calculando
    private static ArrayList<Shape> lines1 = new ArrayList<>();
    private static ArrayList<Shape> lines2 = new ArrayList<>();
    
    private static String backup; //Declaramos espacio para una string en la que guardaremos nuestra ecuacion mientras calculamos soluciones, así si algo sale mal, no se corromperá
    
    public Plotter() {} //Plotter es una clase estática, no necesitamos constructor
    
    /*
     * calculamos la primera ecuación punto a punto
     * @Params: double (el punto x para el que calcularemos una imagen), boolean (Control para ver si trabajamos con la primera o la segunda ecuacion)
     * @Return double (el punto y correspondiente al input)
     */
    private static double calculate(double x, boolean primera) {
        
            if (primera) { //Vemos con que ecuacion estamos trabajando
                backup = ecuacion1; //Seleccionamos la adecuada
            } else {
                backup = ecuacion2; //Seleccionamos la adecuada
            }

            /*
             * Estamos usando notacion de JavaScript, aquí ^ es XOR no una potencia, así que hacemos un primer parse para transformar la String 
             */
            backup = backup.replace("x^2", "Math.pow(x, 2)");
            backup = backup.replace("X^2", "Math.pow(x, 2)");
            
            /*
             * Ahora sustituimos todas las x por el valor que toque evaluar
             */
            backup = backup.replace("x", String.valueOf(x));
            backup = backup.replace("X", String.valueOf(x));
            
            try {
                    Object result = engine.eval(backup); //Evaluamos la expresión 
                    return (double) result; // Devolvemos el elemento
                } catch (ScriptException a) {
                    return 0; //Ignoramos el error devolviendo 0 ya que no se especifica como tratar un error de sintaxis
                }
    }
    
    /*
     * Dibujamos los ejes
     */
    public static void drawAxis(Graphics2D g2d) {
        g2d.drawLine(-MAX_X, 0, MAX_X, 0); //Dibujamos los ejes como dos lineas que recorren toda la pantalla
        g2d.drawLine(0, -MAX_Y, 0, MAX_Y); 
        
        //Vamos recorriendo las lineas que acabamos de crear para generar las subdivisiones
        for (int i = -MAX_X; i < MAX_X; i+=espacio_ejes) {
            g2d.drawLine(i, -profundidad_ejes, i, profundidad_ejes); //Dibuja las subdivisiones
            if (mostrar_unidades) {g2d.drawString(""+i, i, -profundidad_ejes-2);} //Si se quiere muestra las unidades
        }
        for (int i = -MAX_Y; i < MAX_Y; i+=espacio_ejes) {
            g2d.drawLine(-profundidad_ejes, i, profundidad_ejes, i); //Dibuja las subdivisiones
            if (mostrar_unidades) {g2d.drawString(""+i, -profundidad_ejes-2,i);}; //Si se quiere muestra las unidades
        }
    }
    
    
    /*
     * Calculamos las ecuaciones, funcion core del programa
     * @Return: void
     * @Paramas: JProgressBar, JPanel
     * 
     * Emplea multithreading
     */
    public static void calculate(JProgressBar bar, JPanel panel) {
        Thread calculate = new Thread() { //Abrimos un nuevo hilo anonimo
            /*
             * Funcion main de un Thread, se ejecuta cuando se llama a start
             */
            public void run() {
                double x, y1, y2; //Declaramos las variable para las coordenadas (la x es compartida y una "y" para cada ecuacion
                double anterior_x = cota_inferior; //Necesitamos un primer punto para calcular las lineas
                double anterior_y1; //Coordenada y de ese primer punto para la primera ecuacion
                double anterior_y2; //Coordenada y de ese primer punto para la segunda ecuacion
                
                if (!ecuacion1.isEmpty()) {anterior_y1=calculate(cota_inferior,true);} else {anterior_y1=0;} //Comprobamos si se ha introducido una ecuacion vacia (es el único control de input que haremos)
                if (!ecuacion2.isEmpty()) {anterior_y2=calculate(cota_inferior, false);} else {anterior_y2=0;} //Si se ha introducido una cadena vacia iniciamos los valores a 0
                
                Shape l; //Declaramos un objeto forma
                bar.setVisible(true); //Mostramos la barra de progreso
                
                /*
                 * Este es el bucle que recorrerá los números a evaluar
                 */
                for (double i = cota_inferior+1; i <= cota_superior; i+=paso) { 
                    x = i; //Definimos la x
                    
                    if (!ecuacion1.isEmpty()) { //Si la ecuacion1 no está vacia
                        y1 = calculate(i, true); //Llamamos a calculate
                        l = new Line2D.Double(x, -y1, anterior_x, -anterior_y1); //Describimos la linea correspondiente
                        lines1.add(l); //Añadimos la linea 
                        anterior_y1 = y1; //El punto final calculado pasa a ser el punto inicial de la siguiente linea
                    }
                    
                    if (!ecuacion2.isEmpty()) { //Si la ecuacion2 no está vacia
                        y2 = calculate(i, false); //Llamamos a calculate
                        l = new Line2D.Double(x, -y2, anterior_x, -anterior_y2); //Describimos la linea correspondiente
                        lines2.add(l); //Añadimos la linea
                        anterior_y2 = y2; //El punto final calculado pasa a ser el punto inicial de la siguiente linea
                    }
                    
                    anterior_x = i; //El punto final calculado pasa a ser el punto inicial de la siguiente linea
                    bar.setValue((int)i); //Actualizamos la barra de progreso
                }
                bar.setVisible(false); //Una vez terminamos, ocultamos la barra de progreso
                panel.repaint(); //Mandamos una señal al JPanel para que repinte
            }
        };
        calculate.start(); //Iniciamos el hilo
    }   
    
    
    /*
     * Dibujamos las lineas que hemos calculado
     */
    public static void draw(Graphics2D g2d) { //Recibimos el handler para dibujar
        g2d.setColor(color1); //Seleccionamos el color para la primera grafica
        for (Shape s: lines1) { //Recorremos nuestro ArrayList 
            g2d.draw(s); //Vamos dibujando
        }
        g2d.setColor(color2); //Seleccionamos el color para la segunda grafica
        for (Shape s: lines2) { //Recorremos nuestro ArrayList
            g2d.draw(s); //Vamos dibujando
        }
    }
    
    /*
     * Getters y Setters
     */
    public static void setEcuacion1(String ec) {
        ecuacion1 = ec; //Actualizamos el parametro ecuacion1
    }
    public static void setEcuacion2(String ec) {
        ecuacion2 = ec; //Actualizamos el parametro ecuacion2
    }
}
