
package compi;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class Ensablador {

    String programa_completo;
    String ensamblador_variable = "INCLUDE macros.mac\nDOSSEG\n.MODEL SMALL\n"
                + ".STACK 100h\n.DATA\n\t\t\tMAXLEN DB 254\n\t\t\tLEN DB 0\n\t\t\tMSG DB 254 DUP(?)\n\t\t\tMSG_DD DD MSG"
                + "\n\t\t\tBUFFER DB 8 DUP('$')\n\t\t\tCADENA_NUM DB 10 DUP('$')"
                + "\n\t\t\tBUFFERTEMP DB 8 DUP('$')\n\t\t\tBLANCO DB '#'\n\t\t\tBLANCOS"
                + " DB '$'\n\t\t\tMENOS DB '-$'\n\t\t\tCOUNT DW 0\n\t\t\tNEGATIVO "
                + "DB 0\n\t\t\tBUF DW 10\n\t\t\tLISTAPAR LABEL BYTE\n\t\t\tLONGMAX "
                + "DB 254\n\t\t\tTRUE DW 1\n\t\t\tFALSE DW 0\n\t\t\tINTRODUCIDOS "
                + "DB 254 DUP ('$')\n\t\t\tMULT10 DW 1\n\t\t\ts_true DB 'true$'\n\t\t\t"
                + "s_false DB 'false$'\n";
    String ensamblador_programa = ".CODE\n.386\nBEGIN:\n\t\t\tMOV   AX, @DATA\n\t\t\t"
                 + "MOV DS, AX\n CALL COMPI\n\t\t\t MOV AX, 4C00H\n\t\t\t INT 21H\nCOMPI PROC\n"; 
    
    
    variable cabezav;
    int variablesTempContador = 0;
    String variableTemp;
    String temp1, temp2;
    int contador =0;
    Stack<String> ListaPolishLexemas;
    Stack<String> ListaAuxiliar = new Stack<>();
    
    public Ensablador(Stack<String> ListaPolishLexemasEnsamblador, variable nuevacabeza) {
        this.ListaPolishLexemas = ListaPolishLexemasEnsamblador;
        this.cabezav = nuevacabeza;

        while (nuevacabeza != null) {
        	if(nuevacabeza.tipoVariable.equals("integer")) {
        		ensamblador_variable += "\t\t\t" + nuevacabeza.lexema + "  DW 0\n";
        	}
            if (nuevacabeza.siguiente != null) {
                nuevacabeza = nuevacabeza.siguiente;
            } else {
                break;
            }
        }
        
        for (String variab : ListaPolishLexemasEnsamblador) {
            switch (variab) {
                case "+":
                    temp1 = ListaAuxiliar.pop();
                    temp2 = ListaAuxiliar.pop();
                    variableTemp = "t" + variablesTempContador;
                    variablesTempContador++;
                    ensamblador_variable += "\t\t\t" + variableTemp + " DW 0\n";
                    ensamblador_programa += "\tSUMAR " + temp2 + "," + temp1 + "," + variableTemp + "\n";
   
                    ListaAuxiliar.push(variableTemp);
                    break;
                case "-":
                    temp1 = ListaAuxiliar.pop();
                    temp2 = ListaAuxiliar.pop();
                    variableTemp = "t" + variablesTempContador;
                    variablesTempContador++;
                    ensamblador_variable += "\t\t\t" + variableTemp + " DW 0\n";
                    ensamblador_programa += "\tRESTA " + temp2 + "," + temp1 + "," + variableTemp + "\n";
                    ListaAuxiliar.push(variableTemp);
                    break;
                case "*":
                    temp1 = ListaAuxiliar.pop();
                    temp2 = ListaAuxiliar.pop();
                    variableTemp = "t" + variablesTempContador;
                    variablesTempContador++;
                    ensamblador_variable += "\t\t\t" + variableTemp + " DW 0\n";
                    ensamblador_programa += "\tMULTI " + temp2 + "," + temp1 + "," + variableTemp + "\n";
                    ListaAuxiliar.push(variableTemp);
                    break;
                case "div":
                    temp1 = ListaAuxiliar.pop();
                    temp2 = ListaAuxiliar.pop();
                    variableTemp = "t" + variablesTempContador;
                    variablesTempContador++;
                    ensamblador_variable += "\t\t\t" + variableTemp + " DW 0\n";
                    ensamblador_programa += "\tDIVIDE " + temp2 + "," + temp1 + "," + variableTemp + "\n";
                    ListaAuxiliar.push(variableTemp);
                    break;
                case ":=":
                    temp1 = ListaAuxiliar.pop();
                    temp2 = ListaAuxiliar.pop();
                    if(nuevacabeza.tipoVariable.equals("string")) {
                    	ensamblador_variable += "\t\t\t" + temp2 + "  DB "  + temp1  + ",'$'\n";
                    }else {
                    	ensamblador_programa += "\tI_ASIGNAR " + temp2 + "," + temp1 + "\n";
                    }
                    
                    break;
                case ">":
                    temp1 = ListaAuxiliar.pop();
                    temp2 = ListaAuxiliar.pop();
                    variableTemp = "t" + variablesTempContador;
                    variablesTempContador++;
                    ensamblador_variable += "\t\t\t" + variableTemp + " DW 0\n";
                    ensamblador_programa += "\tI_MAYOR " + temp2 + "," + temp1 + "," + variableTemp + "\n";
                    ListaAuxiliar.push(variableTemp);
                    break;
                case "<":
                    temp1 = ListaAuxiliar.pop();
                    temp2 = ListaAuxiliar.pop();
                    variableTemp = "t" + variablesTempContador;
                    variablesTempContador++;
                    ensamblador_variable += "\t\t\t" + variableTemp + " DW 0\n";
                    ensamblador_programa += "\tI_MENOR " + temp2 + "," + temp1 + "," + variableTemp + "\n";
                    ListaAuxiliar.push(variableTemp);
                    break;
                case ">=":
                    temp1 = ListaAuxiliar.pop();
                    temp2 = ListaAuxiliar.pop();
                    variableTemp = "t" + variablesTempContador;
                    variablesTempContador++;
                    ensamblador_variable += "\t\t\t" + variableTemp + " DW 0\n";
                    ensamblador_programa += "\tI_MAYORIGUAL " + temp2 + "," + temp1 + "," + variableTemp + "\n";
                    ListaAuxiliar.push(variableTemp);
                    break;
                case "<=":
                    temp1 = ListaAuxiliar.pop();
                    temp2 = ListaAuxiliar.pop();
                    variableTemp = "t" + variablesTempContador;
                    variablesTempContador++;
                    ensamblador_variable += "\t\t\t" + variableTemp + " DW 0\n";
                    ensamblador_programa += "\tI_MENORIGUAL " + temp2 + "," + temp1 + "," + variableTemp + "\n";
                    ListaAuxiliar.push(variableTemp);
                    break;
                case "=":
                    temp1 = ListaAuxiliar.pop();
                    temp2 = ListaAuxiliar.pop();
                    variableTemp = "t" + variablesTempContador;
                    variablesTempContador++;
                    ensamblador_variable += "\t\t\t" + variableTemp + " DW 0\n";
                    ensamblador_programa += "\tI_IGUAL " + temp2 + "," + temp1 + "," + variableTemp + "\n";
                    ListaAuxiliar.push(variableTemp);
                    break;
                case "<>":
                    temp1 = ListaAuxiliar.pop();
                    temp2 = ListaAuxiliar.pop();
                    variableTemp = "t" + variablesTempContador;
                    variablesTempContador++;
                    ensamblador_variable += "\t\t\t" + variableTemp + " DW 0\n";
                    ensamblador_programa += "\tI_DIFERENTES " + temp2 + "," + temp1 + "," + variableTemp + "\n";
                    ListaAuxiliar.push(variableTemp);
                    break;
                case "and":
                    temp1 = ListaAuxiliar.pop();
                    temp2 = ListaAuxiliar.pop();
                    variableTemp = "t" + variablesTempContador;
                    variablesTempContador++;
                    ensamblador_variable += "\t\t\t" + variableTemp + " DW 0\n";
                    ensamblador_programa += "\tI_AND " + temp2 + "," + temp1 + "," + variableTemp + "\n";
                    ListaAuxiliar.push(variableTemp);
                    break;
                case "or":
                    temp1 = ListaAuxiliar.pop();
                    temp2 = ListaAuxiliar.pop();
                    variableTemp = "t" + variablesTempContador;
                    variablesTempContador++;
                    ensamblador_variable += "\t\t\t" + variableTemp + " DW 0\n";
                    ensamblador_programa += "\tI_OR " + temp2 + "," + temp1 + "," + variableTemp + "\n";
                    ListaAuxiliar.push(variableTemp);
                    break;
                case "not":
                    temp1 = ListaAuxiliar.pop();
                    variableTemp = "t" + variablesTempContador;
                    variablesTempContador++;
                    ensamblador_variable += "\t\t\t" + variableTemp + " DW 0\n";
                    ensamblador_programa += "\tI_NOT " + temp1 + "," + variableTemp + "\n";
                    ListaAuxiliar.push(variableTemp);
                    break;
                case "s+":
                    break;
                case "s-":
                    temp1 = ListaAuxiliar.pop();
                    variableTemp = "t" + variablesTempContador;
                    variablesTempContador++;
                    ensamblador_variable += "\t\t\t" + variableTemp + " DW 0\n";
                    ensamblador_programa += "\tSIGNOMENOS " + temp1 + "," + variableTemp + "\n";
                    ListaAuxiliar.push(variableTemp);
                    break;
                case "read":
                    temp1 = ListaAuxiliar.pop();
                    ensamblador_programa += "\tREAD\n" + "\tASCTODEC " + temp1 + "," + "MSG\n";
                    ensamblador_programa += "\tWRITELN\n";
                    break;
                case "write":
                    temp1 = ListaAuxiliar.pop();

                    if(temp1.startsWith("t")) {
                    	ensamblador_programa += "\tITOA BUFFER, " + variableTemp + "\n";
                        ensamblador_programa += "\tWRITE BUFFERTEMP\n";
                    }else if(nuevacabeza.tipoVariable.equals("string")) {
                    	ensamblador_programa += "\tWRITE " + temp1 + "\n";
                    	ensamblador_programa += "\tWRITELN\n";
                    }             
                    
                    break;
                default:
                    if (variab.contains(":")) {
                        ensamblador_programa += variab + "\n";
                    } else if (variab.contains("BRF")) {
                        temp1 = ListaAuxiliar.pop();
                        ensamblador_programa += "\tJF " + temp1 + "," + variab.split("-")[1] + "\n";
                    } else if (variab.contains("BRI")) {
                        ensamblador_programa += "\tJMP " + variab.split("-")[1] + "\n";
                    } else {
                        ListaAuxiliar.push(variab);
                    }
                    break;
            }
        }
        
        ensamblador_programa += "\t\t ret\nCOMPI ENDP\nEND BEGIN";
        programa_completo = ensamblador_variable + ensamblador_programa;
        
        try
        {
            FileWriter archivo = new FileWriter ("C:\\Users\\ferna\\eclipse-workspace\\Mini-PascalCompiler\\Compi.asm");
            archivo.write(programa_completo);
            archivo.close();
        } catch (IOException ex) {
            System.out.println("Ocurrio un error al escribir archivo. " + ex.getMessage());
        }
    }
    
}
