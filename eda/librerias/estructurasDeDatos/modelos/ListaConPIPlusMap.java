package librerias.estructurasDeDatos.modelos;

public interface ListaConPIPlusMap<E> extends ListaConPI<E> {
    /** elimina los elementos repetidos de una ListaConPI, 
     *  dejando unicamente su 1 aparicion */
    void eliminarRepetidos(); 
    
    /** elimina los elementos de una ListaConPI que est�n en otra **/
    void diferencia(ListaConPI<E> otra);
} 