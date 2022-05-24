package compi;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class lexico {

    Nodo cabeza = null, p;
    int estado = 0;
    int columna = 1;
    int valorMatriz = 1;
    int numRenglon = 1;
    int caracter = 0;
    int posIdentificador;
    String lexema = "";
    boolean errorEncontrado = false;
    variable cabezaVariable;
    boolean errorSintaxis = false;
    long startTime = System.nanoTime();
    
    String type;
    String varPrograma;
    String verificar;
    
    int cont_If = 0;
    int cont_While = 0;
    int prioridad;
    
    Stack<String> pilaAuxiliar = new Stack();
    Stack<String> listaPosfijo = new Stack();
    ArrayList<Nodo> listaInfijo = new ArrayList<>();
    Stack<String> listaPolishLexemas = new Stack();
    

    
    String archivo = "C:\\Users\\ferna\\eclipse-workspace\\Mini-PascalCompiler\\Compi\\Programa1.txt";
    
    
    int matriz[][] = {
        {1, 2, 103, 104, 105, 107, 5, 6, 7, 8, 9, 501, 117, 118, 119, 121, 122, 0, 0, 0, 0, 0, 503},
        {1, 1, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100},
        {101, 2, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 3, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101},
        {500, 4, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500},
        {102, 4, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102},
        {111, 111, 111, 111, 111, 112, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111, 111},
        {109, 109, 109, 109, 109, 110, 108, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109, 109},
        {7, 7, 7, 7, 7, 7, 7, 7, 123, 7, 7, 7, 7, 7, 7, 7, 7, 7, 502, 7, 7, 7, 7},
        {124, 124, 124, 124, 124, 116, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124, 124},
        {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 501, 9}
    };

    String palabrasReservadas[][] = {
    	/*    PALABRA     TOKEN*/
    	{"program",       "200"},
	    {"begin",         "201"},
	    {"end",           "202"},
	    {"if",            "203"},
	    {"then",          "204"},
	    {"else",          "205"},
	    {"while",         "206"},
	    {"do",            "207"},
	    {"or",            "208"},
	    {"and",           "209"},
	    {"not",           "210"},
	    {"div",           "211"},
	    {"write",         "212"},
	    {"read",          "213"},
	    {"true",          "214"},
	    {"false",         "215"},
	    {"var",           "216"},
	    {"integer",       "217"},
	    {"real",          "218"},
	    {"string",        "219"}
    };
    
    //ERRORES 
    String errores[][] = {
        /*DESCRIPCION DEL ERROR        TOKEN*/
	    {"Se esperaba digito ",        "500"},
	    {"End of file inesperado ",    "501"},
	    {"End of line inseperado ",    "502"},
	    {"Simbolo no valido ",         "503"},
	    {"Variable ya declarada ",     "510"},
	    {"Variable no declarada ",     "511"},
	    {"Incompatibilidad de tipos ", "512"}
    };

    RandomAccessFile file = null;
    
  //Prioridades
    String arrayPrioridades[][] = {    
        {":=",    "0"},
        {"or",    "1"},
        {"and",   "2"},
        {"not",   "3"},
        {"<",     "4"},
        {">",     "4"},
        {"<=",    "4"},
        {">=",    "4"},
        {"=",     "4"},
        {"<>",    "4"},
        {"+",     "5"},
        {"-",     "5"},
        {"",      "6"},
        {"div",   "6"},
        {"s+",    "7"},
        {"s-",    "7"},
        {"(",    "-1"},
        {")",    "-1"},
    };
    
    //SISTEMA DE TIPOS
    
    String idTipo[][] = {
                //  0      1
        /*0*/ {"integer", "0"},
        /*1*/ {"real",    "1"},
        /*2*/ {"string",  "2"},
        /*3*/ {"bool",    "3"},
    };
    
     String tipoSuma[][] = { 
     /*integer*/ {"integer", "real",  "error",  "error"},
        /*real*/ {"real",    "real",  "error",  "error"},
      /*string*/ {"error",   "error", "string", "error"},
        /*bool*/ {"error",   "error", "error",  "error"},
    };
     
    String tipoResta[][] = {
     /*integer*/ {"integer", "real",  "error",  "error"},
        /*real*/ {"real",    "real",  "error",  "error"},
      /*string*/ {"error",   "error", "string", "error"},
        /*bool*/ {"error",   "error", "error",  "error"},
    };
    
    String tipoMultiplicacion[][] = {
     /*integer*/ {"integer", "real",  "error",  "error"},
        /*real*/ {"real",    "real",  "error",  "error"},
      /*string*/ {"error",   "error", "error", "error"},
        /*bool*/ {"error",   "error", "error",  "error"},
    };
    
    String tipoDivision[][] = {
     /*integer*/ {"real",  "real",  "error",  "error"},
        /*real*/ {"real",  "real",  "error",  "error"},
      /*string*/ {"error", "error", "error",  "error"},
        /*bool*/ {"error",  "error", "error", "error"},
    };
    
    String tipoMenorQue[][] = {
     /*integer*/ {"bool",  "bool",  "error",  "error"},
        /*real*/ {"bool",  "bool",  "error",  "error"},
      /*string*/ {"error", "error", "error",  "error"},
        /*bool*/ {"error", "error", "error",  "error"},
    };
    String tipoMayorQue[][] = {
     /*integer*/ {"bool",  "bool",  "error",  "error"},
        /*real*/ {"bool",  "bool",  "error",  "error"},
      /*string*/ {"error", "error", "error",  "error"},
        /*bool*/ {"error", "error", "error",  "error"},
    };
    
    String tipoMenorIgual[][] = {
     /*integer*/ {"bool",  "bool",  "error",  "error"},
        /*real*/ {"bool",  "bool",  "error",  "error"},
      /*string*/ {"error", "error", "error",  "error"},
        /*bool*/ {"error", "error", "error",  "error"},
    };
    String tipoMayorIgual[][] = {
     /*integer*/ {"bool",  "bool",  "error",  "error"},
        /*real*/ {"bool",  "bool",  "error",  "error"},
      /*string*/ {"error", "error", "error",  "error"},
        /*bool*/ {"error", "error", "error",  "error"},
    };
    
    String tipoIgual[][] = {
     /*integer*/ {"bool",  "bool",  "error",  "error"},
        /*real*/ {"bool",  "bool",  "error",  "error"},
      /*string*/ {"error", "error", "bool",  "error"},
        /*bool*/ {"error", "error", "error",  "bool"},
    };
    
    String tipoDiferente[][] = {
     /*integer*/ {"bool",  "bool",  "error",  "error"},
        /*real*/ {"bool",  "bool",  "error",  "error"},
      /*string*/ {"error", "error", "bool",   "error"},
        /*bool*/ {"error", "error", "error",  "boll"},
    };
    
    String tipoOr[][] = {
     /*integer*/ {"error", "error", "error", "error"},
        /*real*/ {"error", "error", "error", "error"},
      /*string*/ {"error", "error", "error", "error"},
        /*bool*/ {"error", "error", "error", "bool"},
    };
    
    String tipoAnd[][] = {
     /*integer*/ {"error", "error", "error", "error"},
        /*real*/ {"error", "error", "error", "error"},
      /*string*/ {"error", "error", "error", "error"},
        /*bool*/ {"error", "error", "error", "bool"},
    };
    
    String tipoAsignacion[][] = {
     /*integer*/ {"1",     "error", "error", "error"},
        /*real*/ {"1",     "1",     "error", "error"},
      /*string*/ {"error", "error", "1",     "error"},
        /*bool*/ {"error", "error", "error", "1"},
    };
    
    String tipoNot[] = 
        /*bool*/ {"error", "error", "error",  "bool"}
    ;
    
    String tipoMasUnitario[] = 
        /*bool*/ {"integer", "real", "error",  "error"}
    ;
    
    String tipoMenosUnitario[] = 
        /*bool*/ {"integer", "real", "error",  "error"}
    ;
    
    
    
    

    public lexico() {
        try {
            file = new RandomAccessFile(archivo, "r");
            while (caracter != -1) {
                caracter = file.read();

                if (Character.isLetter(((char) caracter))) {
                    columna = 0;
                } else if (Character.isDigit(((char) caracter))) {
                    columna = 1;
                } else {
                    switch (caracter) {
                        case '+':
                            columna = 2;
                            break;
                        case '-':
                            columna = 3;
                            break;
                        case '*':
                            columna = 4;
                            break;
                        case '=':
                            columna = 5;
                            break;
                        case '>':
                            columna = 6;
                            break;
                        case '<':
                            columna = 7;
                            break;
                        case '"':
                            columna = 8;
                            break;
                        case ':':
                            columna = 9;
                            break;
                        case '{':
                            columna = 10;
                            break;
                        case '}':
                            columna = 11;
                            break;
                        case '.':
                            columna = 12;
                            break;
                        case ',':
                            columna = 13;
                            break;
                        case ';':
                            columna = 14;
                            break;
                        case '(':
                            columna = 15;
                            break;
                        case ')':
                            columna = 16;
                            break;
                        case ' ':
                            columna = 17;
                            break;
                        case 13:
                            columna = 18;
                            break;
                        case 10:
                            columna = 19;
                            numRenglon = numRenglon + 1;
                            break;
                        case 9:
                            columna = 20;
                            break;
                        case -1:
                            columna = 21;
                            break;
                        default:
                            columna = 22;
                            break;
                    }
                }

                valorMatriz = matriz[estado][columna];

                if (valorMatriz < 100) {
                    estado = valorMatriz;

                    if (estado == 0) {
                        lexema = "";
                    } else {
                        lexema = lexema + (char) caracter;
                    }
                } else if (valorMatriz >= 100 && valorMatriz < 500) {
                    if (valorMatriz == 100) {
                        validarPalabraReservada();

                    }
                    if (valorMatriz == 100 || valorMatriz == 101 || valorMatriz == 102
                            || valorMatriz == 109 || valorMatriz == 111 || valorMatriz == 115 || valorMatriz == 124 || valorMatriz >= 200) {
                        file.seek(file.getFilePointer() - 1);
                    } else {
                        lexema = lexema + (char) caracter;
                    }

                    insertarNodo();
                    estado = 0;
                    lexema = "";
                } else {
                    imprimirMensajeError();
                    break;
                }
            }
            if (errorEncontrado != true) {
                imprimirNodos();
                
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        sintaxis();
        imprimirListas(p);
    }

    private void validarPalabraReservada() {
        for (String[] palActual : palabrasReservadas) {
            if (lexema.equals(palActual[0])) {
                valorMatriz = Integer.valueOf(palActual[1]);

            }
        }
    }

    private void insertarNodo() {
        Nodo nodo = new Nodo(lexema, valorMatriz, numRenglon);
        if (cabeza == null) {
            cabeza = nodo;
            p = cabeza;
        } else {
            p.siguiente = nodo;
            p = nodo;
        }
    }

    private void imprimirNodos() {
        p = cabeza;
        System.out.println("Lexema    |     Token    |   Renglon");
        while (p != null) {
            System.out.println(p.lexema + "\t\t" + p.token + "\t\t" + p.renglon);
            p = p.siguiente;
        }
    }

    private void imprimirMensajeError() {
        if (valorMatriz >= 500) {
            for (String[] err : errores) {
                if (valorMatriz == Integer.valueOf(err[1])) {
                    System.out.println("Error encontrado: "
                            + err[1] + " " + err[0]);
                    System.out.println("En linea: " + p.renglon);
                }
            }
            errorEncontrado = true;
        }
    }   

    private void sintaxis() {
        p = cabeza;
        if (p.token == 200) {
            p = p.siguiente;
            varPrograma = p.lexema;
            if (p.token == 100) {
                p = p.siguiente;
                if (p.token == 119) {
                    p = p.siguiente;
                    block();
                    if (p.token == 117) {
                        p = p.siguiente;
                    } else {
                        System.out.println("se esperaba . ");
                    }
                } else {
                    System.out.println("se espera ;");
                }
            } else {
                System.out.println("se espera id");
            }
        } else {
            System.out.println("Se espera la palabra program");
        }
    }

    private void block() {
        variableDeclarationPart();
        compoundStatement();
    }

    private void variableDeclarationPart() {
        if (p != null && p.token == 216) {
            p = p.siguiente;
            variableDeclaration();
            if (p != null && p.token == 119) {
                p = p.siguiente;
                while (p != null && p.token == 100) {
                    variableDeclaration();
                    if (p != null && p.token == 119) {
                        p = p.siguiente;
                    } else {
                        System.out.println("se esperaba ;");
                    }
                }
            } else {
                System.out.println("se esperaba ;");
            }
        }
    }

    private void variableDeclaration() {
        ArrayList<String> identificador = new ArrayList<>();
        variable variables = cabezaVariable;

        if (p != null && p.token == 100) {
            identificador.add(p.lexema);
            p = p.siguiente;
            while (p != null && p.token == 118) {
                p = p.siguiente;
                if (p != null && p.token == 100) {
                    identificador.add(p.lexema);
                    p = p.siguiente;
                } else {
                    System.out.println("se espera id" + p.renglon);
                }
            }
            // Validación de variable ya declarada
            System.out.println("");
            System.out.println("    ---Variables declaradas---");
            if (p != null && p.token == 124) {
                p = p.siguiente;
                type();
                for (String idntf : identificador) {
                    boolean existeVariable = false;
                    //Validación de variable programa
                    if (idntf.equals(varPrograma)) {
                        System.out.println("Error: Variable no puede ser declarada igual que el nombre del programa.");
                    }
                    while (variables != null) {
                        if (idntf.equals(variables.lexema)) {
                            System.out.println("Error: Variable ya declarada. En renglon: " + p.renglon);
                            errorSintaxis = true;
                            existeVariable = true;
                            break;
                        }
                        if (variables.siguiente != null) {
                            variables = variables.siguiente;
                        } else {
                            break;
                        }
                    }

                    if (existeVariable != true) {
                        variable nuevaVariable = new variable(idntf, type);
                        if (cabezaVariable == null) {
                            cabezaVariable = nuevaVariable;
                            variables = cabezaVariable;
                        } else {
                            if (variables != null) {
                                variables.siguiente = nuevaVariable;
                                variables = nuevaVariable;
                            } else {
                                break;
                            }
                        }
                        System.out.println("Variable y tipo: " + variables.lexema + "       " + variables.tipoVariable);
                    }
                }
            } else {
                System.out.println("se espera :");
            }
        } else {
            System.out.println("se espera id");
        }
    }

    // Verificar variable no declarada
    public Nodo verificarVariable(Nodo p) {
        variable variables = cabezaVariable;
        boolean existe = false;
        while (variables != null) {

            if (variables.lexema.equals(p.lexema)) {
                existe = true;
                break;
            }
            variables = variables.siguiente;
        }
        if (existe == false) {
            System.out.println("Variable no declarada en renglon:" + p.renglon);
        }
        return p;
    }

    private void type() {
        if (p != null && (p.token == 217 || p.token == 218 || p.token == 219)) {
            type = p.lexema;
            p = p.siguiente;
        } else {
            System.out.println("se esperaba tipo");
        }
    }

    private void compoundStatement() {
        if (p != null && p.token == 201) {
            p = p.siguiente;
            statement();
            while (p != null && p.token == 119) {
                p = p.siguiente;
                statement();
            }
            if (p != null && p.token == 202) {
                p = p.siguiente;
            } else {
                System.out.println("se esperaba end");
            }
        } else {
            System.out.println("se esperaba begin");
        }
    }

    private void statement() {
        if (p != null && (p.token == 100 || p.token == 212 || p.token == 213)) {
            simpleStatement();
            
        } else if (p != null && (p.token == 203 || p.token == 206 || p.token == 201)) {
            structuredStatement();
        } else {
            System.out.println("se esperaba statement");
        }
    }

    private void simpleStatement() {
        if (p != null && p.token == 100) {
            assignmentStatement();
            
        }
        if (p != null && p.token == 213) {
            readStatement();
        }
        if (p != null && p.token == 212) {
            writeStatement();
        }

    }

    private void assignmentStatement() {
        if (p != null && p.token == 100) {
            verificarVariable(p);
            listaInfijo.add(p);///
            p = p.siguiente;
            if (p != null && p.token == 116) {
            	listaInfijo.add(p);///
                p = p.siguiente;
                expression();
                infijoPosfijo(p);
            } else {
                System.out.println("se espereba :=");
            }
        }       

    }

    private void readStatement() {
        if (p != null && p.token == 213) {
            p = p.siguiente;
            if (p != null && p.token == 121) {
                p = p.siguiente;
                if (p != null && p.token == 100) {
                    verificarVariable(p);
                    listaPolishLexemas.add(p.lexema);
                    p = p.siguiente;
                    infijoPosfijo(p);
                    listaPolishLexemas.add("read");                   
                    while (p != null && p.token == 118) {
                        p = p.siguiente;
                        if (p != null && p.token == 100) {
                            verificarVariable(p);
                            listaPolishLexemas.add(p.lexema);
                            p = p.siguiente;
                            infijoPosfijo(p);
                            listaPolishLexemas.add("read");                            
                        } else {
                            System.out.println("se espera id");
                        }
                    }
                    if (p != null && p.token == 122) {
                        p = p.siguiente;
                    } else {
                        System.out.println("se esperaba ')'");
                    }
                }
            } else {
                System.out.println("se esperaba '(' ");
            }
        }
    }

    private void writeStatement() {
        
        /*if (p != null && p.token == 212) {*/
            p = p.siguiente;
            if (p != null && p.token == 121) {
                p = p.siguiente;
                expression();
                infijoPosfijo(p);
                listaPolishLexemas.add("write");
                
                while (p != null && p.token == 118) {
                    p = p.siguiente;
                    expression();
                    infijoPosfijo(p);
                    listaPolishLexemas.add("write");
                    
                }
                if (p != null && p.token == 122) {
                    p = p.siguiente;
                } else {
                    System.out.println("se esperaba ')'");
                }
            } else {
                System.out.println("se esperaba '(' ");
            }
        }
    

	private void expression() {
	    simpleExpression();
	    if (p != null && (p.token == 107 || p.token == 108 || p.token == 109 || p.token == 110 || p.token == 111 || p.token == 112)) {
	        listaInfijo.add(p);///////
	        relationalOperator();
	        simpleExpression();
	    }
	}

	private void simpleExpression() {
        sign();
        term();
        while (p != null && (p.token == 103 || p.token == 104 || p.token == 208)) {
            listaInfijo.add(p);/////
            p = p.siguiente;
            term();
        }
    }
    private void sign() {
        if (p != null && (p.token == 103 || p.token == 104)) {
        	listaInfijo.add(p);
            p = p.siguiente;
        }
    }

    private void term() {
        factor();
        while (p != null && (p.token == 105 || p.token == 211 || p.token == 209)) {
            listaInfijo.add(p);
            p = p.siguiente;
            factor();
        }
    }

    private void factor() {
        if (p != null && (p.token == 100 || p.token == 101 || p.token == 102 || p.token == 123)) {
            if (p.token == 100) {
                verificarVariable(p);
            }
            listaInfijo.add(p);
            p = p.siguiente;
        } else if (p != null && p.token == 121) {
            listaInfijo.add(p);//////////
            p = p.siguiente;
            expression();
            if (p != null && p.token == 122) {
                listaInfijo.add(p);////////
                p = p.siguiente;
            } else {
                System.out.println("se esperaba ')'");
            }
        } else if (p != null && p.token == 210) {
            listaInfijo.add(p);///////
            p = p.siguiente;
            factor();
        }
    }

    private void relationalOperator() {
        if (p != null && (p.token == 107 || p.token == 108 || p.token == 109 || p.token == 110 || p.token == 111 || p.token == 112)) {
            p = p.siguiente;
        }
    }

    private void structuredStatement() {
        if (p != null && p.token == 203) {
            ifStatement();
        } else {
            if (p != null && p.token == 206) {
                whileStatement();
            } else {
                compoundStatement();
            }
        }
    }

    private void ifStatement() {
        if (p != null && p.token == 203) {
            p = p.siguiente;
            expression();
            infijoPosfijo(p);
            listaInfijo.clear();
            listaPosfijo.clear();
            pilaAuxiliar.clear();
            int contador_If = cont_If;
            cont_If ++;
            listaPolishLexemas.add("BRF-A"+ contador_If);
            if (p != null && p.token == 204) {
                p = p.siguiente;
                statement();
                listaPolishLexemas.add("BRI-B"+ contador_If);
                listaPolishLexemas.add("A"+ contador_If + ": ");
            } else {
                System.out.println("se esperaba then");
            }
            if (p != null && p.token == 205) {
                p = p.siguiente;
                statement();
            }
            listaPolishLexemas.add("B"+ contador_If + ": ");
        }
        
    }

    private void whileStatement() {
        if (p != null && p.token == 206) {
            int contador_While = cont_While;
            cont_While ++;
            p = p.siguiente;
            listaPolishLexemas.add("D" + contador_While + ":");
            expression();
            infijoPosfijo(p);
            listaPolishLexemas.add("BRF-C" + contador_While);
            if (p != null && p.token == 207) {
                p = p.siguiente;
                statement();
            }else {
                System.out.println("se esperaba do");
            }
            listaPolishLexemas.add("BRI-D" + contador_While);
            listaPolishLexemas.add("C" + contador_While + ":");
        }
        
    }
    
    private int evaluarOperadores(Nodo p) {
        if (p.token == 100 || p.token == 101 || p.token == 102 || p.token == 123) {
            switch (p.token) {
                case 100:
                    variable variables = cabezaVariable;
                    while (variables != null) {
                        if (variables.lexema.equals(p.lexema)) {
                            listaPosfijo.add(variables.tipoVariable);
                            listaPolishLexemas.add(variables.lexema);
                            break;
                        }
                        variables = variables.siguiente;
                    }
                    break;

                case 101:
                    listaPosfijo.add("integer");
                    listaPolishLexemas.add(p.lexema);
                    break;

                case 102:
                    listaPosfijo.add("real");
                    listaPolishLexemas.add(p.lexema);
                    break;

                case 123:
                    listaPosfijo.add("string");
                    listaPolishLexemas.add(p.lexema);
                    break;
            }
        } else {
            if (pilaAuxiliar.size() > 0) {
                int prioridad1 = verificarPrioridad(p.lexema);
                int prioridad2 = verificarPrioridad(pilaAuxiliar.peek());
                if (p.token == 122) {
                    String nodoArriba = pilaAuxiliar.pop();
                    while (!nodoArriba.equals("(")) {
                        listaPosfijo.add(nodoArriba);
                        listaPolishLexemas.add(nodoArriba);
                        nodoArriba = pilaAuxiliar.pop();
                    }
                } else if (p.token == 121 || prioridad1 > prioridad2) {
                    pilaAuxiliar.push(p.lexema);
                } else {
                    listaPolishLexemas.add(pilaAuxiliar.peek());
                    listaPosfijo.add(pilaAuxiliar.pop());
                    evaluarOperadores(p);
                }
            } else {
                pilaAuxiliar.push(p.lexema);
            }
        }
        return p.token;
    }

    private int verificarPrioridad(String operador) {
        for (String[] operador1 : arrayPrioridades) {
            if (operador.equals(operador1[0])) {
                prioridad = Integer.parseInt(operador1[1]);
            }
        }
        return prioridad;
    }
    
    private void infijoPosfijo(Nodo p) {
        for (Nodo posicion : listaInfijo) {
            evaluarOperadores(posicion);
        }
        while (!pilaAuxiliar.isEmpty()) {
            listaPosfijo.add(pilaAuxiliar.peek());
            listaPolishLexemas.add(pilaAuxiliar.pop());
        }
        tiposPosfijo();
        pilaAuxiliar.clear();
        listaPosfijo.clear();
        listaInfijo.clear(); 
    }

    public int idTiposMatriz(String a){
        if (a.equals("integer")){
            return 0;
        }
        if (a.equals("real")){
            return 1;
        }
        if (a.equals("string")){
            return 2;
        }
         if (a.equals("bool")){
            return 3;
        }
         return -1;
    }
    
    private void tiposPosfijo(){
        for (String tipoP : listaPosfijo){
            if (tipoP.equals("integer")|| tipoP.equals("real") || tipoP.equals("string") ) {
                pilaAuxiliar.push(tipoP);
            }else if (tipoP.equals( "s+") || tipoP.equals("s-") || tipoP.equals("not"))  {
                String op1 = pilaAuxiliar.pop();
                switch (tipoP) {
                    case "s+" :
                        verificar = tipoMasUnitario[idTiposMatriz(op1)];
                        break;
                    case "s-" :
                        verificar = tipoMenosUnitario[idTiposMatriz(op1)];
                        break;
                    case "not" :
                        verificar = tipoNot[idTiposMatriz(op1)];
                        break;
                }
                if (verificar.equals("error")) {
                    System.out.println("Error incompatibilidad de tipos");
                    break;
                }else{
                    pilaAuxiliar.push(verificar);
                }
            }else{
                String op1 = pilaAuxiliar.pop();
                String op2 = pilaAuxiliar.pop();
                switch (tipoP){
                    case "+" :
                        verificar = tipoSuma[idTiposMatriz(op1)][idTiposMatriz(op2)];
                        break;
                    case "-" :
                        verificar = tipoResta[idTiposMatriz(op1)][idTiposMatriz(op2)];
                        break;
                    case "*" :
                        verificar = tipoMultiplicacion[idTiposMatriz(op1)][idTiposMatriz(op2)];
                        break;
                    case "div" :
                        verificar = tipoDivision[idTiposMatriz(op1)][idTiposMatriz(op2)];
                        break;
                    case "<" :
                        verificar = tipoMenorQue[idTiposMatriz(op1)][idTiposMatriz(op2)];
                        break;
                    case ">" :
                        verificar = tipoMayorQue[idTiposMatriz(op1)][idTiposMatriz(op2)];
                        break;
                    case "<=" :
                        verificar = tipoMenorIgual[idTiposMatriz(op1)][idTiposMatriz(op2)];
                        break;
                    case ">=" :
                        verificar = tipoMayorIgual[idTiposMatriz(op1)][idTiposMatriz(op2)];
                        break;
                    case "=" :
                        verificar = tipoIgual[idTiposMatriz(op1)][idTiposMatriz(op2)];
                        break;
                    case "<>" :
                        verificar = tipoDiferente[idTiposMatriz(op1)][idTiposMatriz(op2)];
                        break;
                    case "or" :
                        verificar = tipoOr[idTiposMatriz(op1)][idTiposMatriz(op2)];
                        break;
                    case "and" :
                        verificar = tipoAnd[idTiposMatriz(op1)][idTiposMatriz(op2)];
                        break;
                    case ":=" :
                        verificar = tipoAsignacion[idTiposMatriz(op1)][idTiposMatriz(op2)];
                        break;
                }
                if (verificar.equals("error")) {
                    System.out.println("\nError incompatibilidad de tipos ");
                    break;
                }else{
                    pilaAuxiliar.push(verificar);
                }
            }
        }
    }
    
    public void imprimirListas(Nodo p){
    	System.out.println("");
    	System.out.println("Lista Polish");
    	System.out.println("------------------------");
    	for(String var:listaPolishLexemas) {
    		System.out.println(var);
    	}
    	
    	//long endTime = System.nanoTime();
    	//long timeElapsed = endTime - startTime;
    	//System.out.println("Execution time in nanoseconds: " + timeElapsed);
    	//System.out.println("Execution time in milliseconds: " + timeElapsed / 1000000);
        //System.out.println(listaPolishLexemas);
    }


}
               
