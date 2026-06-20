package br.ucsal.compiladores.lexico;

/**
 * AtomResult representa o resultado de 1 chamada ao analisador léxico.
 * É um "record" do Java (uma classe só de dados, imutável). Os campos são:
 *  - lexeme: o texto do átomo já em CAIXA ALTA e já truncado em 30 chars.
 *  - codigo: o código do átomo conforme o Apêndice A
 *  - indiceTabSimb: o índice na tabela de símbolos (só para identificadores);
 *  - linha: o número da linha onde o átomo começou.
 *
 * Este arquivo é o "contrato" usado por todos os módulos, por isso fica
 * num lugar central. (Definido na Etapa 1 - PROJ.)
 */
public record AtomResult(String lexeme, String codigo, int indiceTabSimb, int linha) {

    public static final String EOF = "EOF";

    public boolean isEOF() {
        return EOF.equals(codigo);
    }
}
