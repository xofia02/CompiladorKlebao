package br.ucsal.compiladores.tabela;

import java.util.HashMap;
import java.util.Map;

/**
 * TabelaReservados é a tabela fixa de palavras e símbolos reservados da
 * linguagem Klebão
 * Ela é carregada UMA única vez no início do programa (antes de qualquer
 * análise) e nunca muda. Serve para o léxico descobrir, depois de formar uma
 * palavra ou um símbolo, se ele é uma palavra/símbolo reservado (e qual o seu
 * código A.. ou B..) ou se é um identificador comum.
 */
public class TabelaReservados {

    private final Map<String, String> reservados = new HashMap<>();

    /**
     * Carrega todas as palavras reservadas (A01-A26) e símbolos reservados
     * (B01-B22) do Apêndice A. Chamado uma vez pelo Main na inicialização.
     */
    public void carregarReservados() {
        reservados.put("BOOLEAN", "A01");
        reservados.put("BREAK", "A02");
        reservados.put("CHARACTER", "A03");
        reservados.put("DECLARATIONS", "A04");
        reservados.put("ELSE", "A05");
        reservados.put("ENDDECLARATIONS", "A06");
        reservados.put("ENDFUNCTION", "A07");
        reservados.put("ENDFUNCTIONS", "A08");
        reservados.put("ENDIF", "A09");
        reservados.put("ENDPROGRAM", "A10");
        reservados.put("ENDWHILE", "A11");
        reservados.put("FALSE", "A12");
        reservados.put("FUNCTIONS", "A13");
        reservados.put("FUNCTYPE", "A14");
        reservados.put("IF", "A15");
        reservados.put("INTEGER", "A16");
        reservados.put("PARAMTYPE", "A17");
        reservados.put("PRINT", "A18");
        reservados.put("PROGRAM", "A19");
        reservados.put("REAL", "A20");
        reservados.put("RETURN", "A21");
        reservados.put("STRING", "A22");
        reservados.put("TRUE", "A23");
        reservados.put("VARTYPE", "A24");
        reservados.put("VOID", "A25");
        reservados.put("WHILE", "A26");

        //Símbolos A
        reservados.put(";", "B01");
        reservados.put(",", "B02");
        reservados.put(":", "B03");
        reservados.put(":=", "B04");
        reservados.put("?", "B05");
        reservados.put("(", "B06");
        reservados.put(")", "B07");
        reservados.put("[", "B08");
        reservados.put("]", "B09");
        reservados.put("{", "B10");
        reservados.put("}", "B11");
        reservados.put("+", "B12");
        reservados.put("-", "B13");
        reservados.put("*", "B14");
        reservados.put("/", "B15");
        reservados.put("%", "B16");
        reservados.put("==", "B17");
        reservados.put("!=", "B18");
        reservados.put("#", "B18");   // # é alias de != (mesmo código B18, conforme Apêndice A)
        reservados.put("<", "B19");
        reservados.put("<=", "B20");
        reservados.put(">", "B21");
        reservados.put(">=", "B22");
    }

    /**
     * Procura um lexeme (já em maiúsculas) na tabela de reservados.
     * Devolve o código do átomo se for reservado, ou null se não for
     */
    public String consultar(String lexeme) {
        return reservados.get(lexeme);
    }
}
