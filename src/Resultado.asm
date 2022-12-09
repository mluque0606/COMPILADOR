.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
aux2bytes dw ? 
@ERROR_DIVISION_POR_CERO db "ERROR: Division por cero", 0
@ERROR_OVERFLOW_SUMA_FLOTANTE db "ERROR: Overflow en suma de flotante", 0
@ERROR_INVOCACION db "ERROR: Invocacion de funcion a si misma no permitida", 0
_2 db 2
_3 db 3
@out1 db  "&cadena0", 0
_10 db 10
.code
START:
MOV EAX, 2
MOV _a@programa_1, EAX
MOV EAX, 3
MOV _z@programa_1, EAX
MOV EAX, 10
MOV _resultado@programa_1, EAX
invoke MessageBox, NULL, addr cadena0, addr cadena0, MB_OK 
invoke ExitProcess, 0
end START