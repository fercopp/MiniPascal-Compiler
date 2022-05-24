
package compi;

public class variable {
   
   variable siguiente = null; 
   public String lexema;
   public String tipoVariable; 

   public variable(String lexema, String tipoVariable) {
        this.lexema = lexema;
        this.tipoVariable = tipoVariable;
   }
       
}
