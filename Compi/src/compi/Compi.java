
package compi;


public class Compi {
    Nodo p;
    public static void main(String[] args) {
        lexico lexico = new lexico();
        if (!lexico.errorEncontrado) {
        	System.out.println("---------------------------------");  
            System.out.println("\tAnalisis exitoso");       
            System.out.println("---------------------------------");  
        }   
    }
    
}
