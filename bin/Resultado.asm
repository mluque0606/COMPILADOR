.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
@ERROR_DIVISION_POR_CERO db "ERROR: Division por cero", 0
@ERROR_OVERFLOW_SUMA_FLOTANTE db "ERROR: Overflow en suma de flotante", 0
@ERROR_INVOCACION db "ERROR: Invocacion de funcion a si misma no permitida", 0
@0 db 0
@1 db 1
cadena0 db  "&cadena0", 0
@5 db 5
_y db ?
.code
START:
f1@programa_7 PROC
MOV ECX, @0
MOV _y, ECX
MOV ECX, @5
CMP _y, ECX
MOV @aux0, 0FFh
JB aux0
MOV @aux0, 00h
aux0:
JAE L19
MOV ECX, _y
ADD ECX, @1
MOV @aux1, ECX
MOV ECX, @aux1
MOV _y, ECX
invoke MessageBox, NULL, addr cadena0, addr cadena0, MB_OK 
JMP L6
MOV EAX, Y
MOV @ret@f1@programa_7, EAX
RET
f1@programa_7 ENDP
invoke ExitProcess, 0
end START