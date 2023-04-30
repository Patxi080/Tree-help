package librerias.estructurasDeDatos.deDispersion;

import librerias.estructurasDeDatos.modelos.Map;

// para implementar toClaves
import librerias.estructurasDeDatos.modelos.ListaConPI; 
import librerias.estructurasDeDatos.lineales.LEGListaConPI;

/**
 * Implementacion de una TablaHash Enlazada 
 * con Listas con PI y SIN REHASHING
 */

public class TablaHash<C, V> implements Map<C, V> {
    
    // Una Tabla Hash de Entradas de Clave de tipo C y  Valor de tipo V ...
    
    /** El valor (float) del factor de carga de una Tabla Hash 
     *  (valor por defecto en la clase java.util.HashMap) 
     */
    public static final double FACTOR_DE_CARGA = 0.75;
    
    /** TIENE UN array de Listas Con PI de Tipo Generico EntradaHashLPI<C, V>:
     *  - elArray[h] representa una cubeta, 
     *    o lista de colisiones asociadas al indice Hash h
     *  - elArray[h] contiene la referencia a la Lista Con PI 
     *    donde se encuentran todas las 
     *    Entradas cuya Clave tiene un indice Hash h 
     */
    protected ListaConPI<EntradaHash<C,V>>[] elArray;
    
    /** TIENE UNA talla que representa el numero de Entradas 
     *  almacenadas en una Tabla Hash o, si se prefiere, en sus cubetas
     */
    protected int talla; 
    public static final boolean REHASHING = true;
    private int numRH;
    
    /** Devuelve el indice Hash de la Clave c de una Entrada, i.e. 
     *  la cubeta en la que se debe encontrar la Entrada de clave c
     *  *** SIN ESTE METODO NO SE TIENE UNA TABLA HASH, SOLO UN ARRAY ***
     */
    protected int indiceHash(C c) {
        int indiceHash = c.hashCode() % elArray.length;
        if (indiceHash < 0) indiceHash += elArray.length; 
        return indiceHash;
    }    
    
    /** Crea una Tabla Hash vacia, con una capacidad (inicial) maxima  
     *  de tallaMaximaEstimada entradas y factor de carga 0.75
     */
    @SuppressWarnings("unchecked") 
    public TablaHash(int tallaMaximaEstimada) {
        int capacidad = siguientePrimo((int) (tallaMaximaEstimada / FACTOR_DE_CARGA));
        elArray = new LEGListaConPI[capacidad];
        for (int i = 0; i < elArray.length; i++) 
            elArray[i] = new LEGListaConPI<EntradaHash<C,V>>();
        talla = 0;
    }
    
    // Devuelve un numero primo MAYOR o IGUAL a n, i.e. el primo que sigue a n
     protected static final int siguientePrimo(int n) {
         if (n % 2 == 0) n++;
         for ( ; !esPrimo(n); n += 2); 
         return n;
     } 
     
     // Comprueba si n es un numero primo
     protected static final boolean esPrimo(int n) {
         for (int i = 3 ; i * i <= n; i += 2) 
            if (n % i == 0) return false; // n NO es primo
         return true; // n SI es primo
     } 
    
    /** Devuelve el valor de la entrada con clave c de una Tabla Hash,
     *  o null si no hay una entrada con clave c en la Tabla
     */
    public V recuperar(C c) {
        int pos = indiceHash(c);
        ListaConPI<EntradaHash<C,V>> cubeta = elArray[pos];
        V valor = null;
        /*COMPLETAR*/
        cubeta.inicio();
        while (!cubeta.esFin() && !cubeta.recuperar().clave.equals(c)) { 
            cubeta.siguiente(); 
        }
        // Resolucion de la Busqueda: SII esta en la Tabla, se   
        // recupera el valor la Entrada de Clave c 
        if (!cubeta.esFin()) { valor = cubeta.recuperar().valor; }
        return valor;
    }
    
    /** Elimina la entrada con clave c de una Tabla Hash y devuelve
     *  su valor asociado, o null si no hay ninguna entrada con 
     *  clave c en la Tabla
     */
    public V eliminar(C c) {
        int pos = indiceHash(c);
        ListaConPI<EntradaHash<C, V>> l = elArray[pos];
        V valor = null; 
        // Busqueda de la Entrada de Clave c en la cubeta l
        l.inicio();
        while (!l.esFin() && !l.recuperar().clave.equals(c)) { 
            l.siguiente(); 
        }
        // Resolucion de la Busqueda: SII esta en la cubeta     
        // l, se recupera el valor de la Entrada de Clave c  
        // y, luego, se elimina de l
        if (!l.esFin()) {
            valor = l.recuperar().valor;
            l.eliminar();
            talla--;
        }
        return valor;
    }
        
    /** Inserta la entrada (c, v)  a una Tabla Hash y devuelve  
     *  el antiguo valor asociado a c, o null si no hay ninguna 
     *  entrada con clave c en la Tabla
     */
    // NO HACE REHASHING. En la practica 3 se modificara este metodo
    // de forma que el rehashing se efectua cuando tras insertar una 
    // nueva entrada en la correspondiente cubeta (lista enlazada 
    // directa) de elArray, e incrementar la talla, ...
    // factorDeCarga() > FACTOR_DE_CARGA.
    // Ello equivale, basicamente, a que la talla actual 
    // supere la tallaMaximaEstimada.
    public V insertar(C c, V v) {
        int pos = indiceHash(c);
        ListaConPI<EntradaHash<C, V>> l = elArray[indiceHash(c)];
        V antiguoValor = null;
        // Busqueda de la Entrada de Clave c en la cubeta l
        l.inicio();
        while (!l.esFin() && !l.recuperar().clave.equals(c)) { 
            l.siguiente(); 
        }
        // Resolucion de la busqueda: si la Entrada (c, v) NO
        // esta en la Tabla, se inserta al final de la cubeta
        // l, se incrementa la talla de la Tabla y, en su caso,
        // se efectua Rehashing; sino, si la Entrada ya esta en 
        // l, se actualiza su valor.
        if (l.esFin()) {
            // Insercion efectiva de la Entrada (c, v)
            l.insertar(new EntradaHash<C, V>(c, v));
            talla++;
            
            if (factorDeCarga() > FACTOR_DE_CARGA && REHASHING) { 
                numRH++;
                rehashing(); 
            }
        }
        else {
            // Recuperacion del valor actual de la Entrada de
            // Clave C, para devolverlo, y actualizacion de 
            // dicho valor a v
            antiguoValor = l.recuperar().valor; l.recuperar().valor = v;
        }
        return antiguoValor;
    }
     
    /** Comprueba si una Tabla Hash esta vacia, i.e. si tiene 0 entradas.
     */
    public boolean esVacio() { 
        return talla == 0; 
    }
    
    /** Devuelve la talla, o numero de entradas, de una Tabla Hash.
     */
    public int talla() { 
        return talla; 
    } 

    /** Devuelve una ListaConPI con las talla() claves de una Tabla Hash
     */
    public ListaConPI<C> claves() {
        ListaConPI<C> l = new LEGListaConPI<C>();
        /*COMPLETAR*/
        return l;
    }
    
    /** Devuelve el factor de carga actual de una Tabla Hash, i.e. la longitud
     *  media de sus cubetas
     */
    // RECUERDA: este metodo tiene 
    // un coste INDEPENDIENTE de la talla del problema
    // NO hace falta calcular con un bucle la longitud media de las cubetas!!!
    public final double factorDeCarga() {
        return (double) talla / elArray.length; 
    }
    
    /*******************************
     * SOLO PARA EJEMPLOS DE TEORIA
     *******************************/
    /** Devuelve un String con las Entradas de una Tabla Hash 
     */
    // RECUERDA: se usa la clase StringBuilder porque es mas eficiente
    public final String toString() {
        StringBuilder res = new StringBuilder();
        for (ListaConPI<EntradaHash<C, V>> l : elArray) {
            for (l.inicio(); !l.esFin(); l.siguiente()) {
                res.append(l.recuperar() + "\n");
            }
        }   
        return res.toString(); 
    }
    
    public double desviacionTipica(){
        /* COMPLETAR */
        double media = factorDeCarga();
        double suma = 0.0;
                                                          
        for (ListaConPI<EntradaHash<C,V>> cub: elArray)
            suma += (cub.talla() - media) * (cub.talla() - media);
        return Math.sqrt(suma/elArray.length);
    }
    
    public double costeMLocalizar(){
        double res =0;
        for(int i = 0; i < elArray.length; i++){
            res += ((elArray[i].talla() * (elArray[i].talla() - 1) / 2));
        }
        return res / talla;
    }
    
    public final void rehashing(){
        ListaConPI<EntradaHash<C,V>>[] aux = elArray;
        elArray = new LEGListaConPI[siguientePrimo(elArray.length * 2)];
        
        for (int i = 0; i < elArray.length; i++)
            elArray[i] = new LEGListaConPI<>();
        
        for(ListaConPI<EntradaHash<C,V>> lista: aux){
            for (lista.inicio(); !lista.esFin(); lista.siguiente()){
                EntradaHash<C,V> elemento = lista.recuperar();
                elArray[indiceHash(elemento.clave)].insertar(new EntradaHash<>(elemento.clave, elemento.valor));
            }
        }
    }
    /** Devuelve un String con el histograma de ocupacion 
     *  de una Tabla Hash Enlazada en formato texto. Asi, 
     *  en cada una de sus lineas deben aparecer dos valores 
     *  enteros separados por un tabulador: una longitud de 
     *  cubeta (valor int en el intervalo [0, 9]) y un numero 
     *  de cubetas. 
     *  MUY IMPORTANTE: el numero de cubetas que aparece en
     *  la linea i, ES: 
     ** (a) Si i en [0, 8], el numero de cubetas de la Tabla  
     **     que tienen una longitud i
     ** (b) Si i = 9 (ultima linea), el numero de cubetas de 
     **     la Tabla que tienen una longitud 9 o MAYOR
     */      
    public String histograma() {
        String res = "";
        int[] histo = new int[10];
        for (int i = 0; i < elArray.length; i++) {
            int longCubeta = elArray[i].talla();
            if (longCubeta < 9) { histo[longCubeta]++; }
            else { histo[9]++; }
        }
        for (int i = 0; i < histo.length; i++) {
            res += i + "\t" + histo[i] + "\n";
        }        
        return res;        
    }
    /** Devuelve el numero de operaciones de Rehashing que, 
      *  en su caso, se hayan efectuado para crear una 
      *  Tabla Hash */
    public int numeroDeRH() { return numRH; }
}