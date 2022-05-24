INCLUDE macros.mac
DOSSEG
.MODEL SMALL
.STACK 100h
.DATA
			MAXLEN DB 254
			LEN DB 0
			MSG DB 254 DUP(?)
			MSG_DD DD MSG
			BUFFER DB 8 DUP('$')
			CADENA_NUM DB 10 DUP('$')
			BUFFERTEMP DB 8 DUP('$')
			BLANCO DB '#'
			BLANCOS DB '$'
			MENOS DB '-$'
			COUNT DW 0
			NEGATIVO DB 0
			BUF DW 10
			LISTAPAR LABEL BYTE
			LONGMAX DB 254
			TRUE DW 1
			FALSE DW 0
			INTRODUCIDOS DB 254 DUP ('$')
			MULT10 DW 1
			s_true DB 'true$'
			s_false DB 'false$'
			num1  DW 0
			num2  DW 0
			num3  DW 0
			inicio  DB "Calculadora basica",'$'
			pregNum1  DB "Escriba el primer numero",'$'
			pregNum2  DB "Escriba el segundo numero",'$'
			r1  DB "--- El resultado es positivo ---",'$'
			r2  DB "--- El resultado es negativo ---",'$'
			t0 DW 0
			t1 DW 0
			t2 DW 0
			t3 DW 0
			t4 DW 0
.CODE
.386
BEGIN:
			MOV   AX, @DATA
			MOV DS, AX
 CALL COMPI
			 MOV AX, 4C00H
			 INT 21H
COMPI PROC
	WRITE inicio
	WRITELN
	WRITE pregNum1
	WRITELN
	READ
	ASCTODEC num1,MSG
	WRITELN
	WRITE pregNum2
	WRITELN
	READ
	ASCTODEC num2,MSG
	WRITELN
	SUMAR num1,num2,t0
	ITOA BUFFER, t0
	WRITE BUFFERTEMP
	SUMAR num1,num2,t1
	I_MAYOR t1,0,t2
	JF t2,A0
	WRITE r1
	WRITELN
	JMP B0
A0: 
	SUMAR num1,num2,t3
	I_MENOR t3,0,t4
	JF t4,A1
	WRITE r2
	WRITELN
	JMP B1
A1: 
B1: 
B0: 
		 ret
COMPI ENDP
END BEGIN