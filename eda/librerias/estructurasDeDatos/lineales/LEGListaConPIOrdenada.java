package librerias.estructurasDeDatos.lineales;
import java.lang.Comparable;

/**
 * Write a description of class LEGListaConPIOrdenada here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LEGListaConPIOrdenada <T extends Comparable> extends LEGListaConPI<T>{
    public void insertar(T elem){
        inicio();
        while(!esFin() && (recuperar().compareTo(elem) < 0)){
            siguiente();
        }
        super.insertar(elem);
    }
    
}
