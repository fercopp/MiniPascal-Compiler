
package compi;

class Nodo {

    String lexema;
    int token;
    int renglon;
    Nodo siguiente = null;

    Nodo(String lexema, int token, int renglon) {
        this.lexema = lexema;
        this.token = token;
        this.renglon = renglon;
    }
}
