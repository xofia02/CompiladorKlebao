package br.ucsal.compiladores.tabela;

import java.util.ArrayList;
import java.util.List;

/**
 * EntradaSimbolo representa uma linha (uma entrada) da tabela de símbolos.
 *
 * A especificação (seção "Tabela de Símbolos") exige que cada símbolo guarde
 * exatamente estes atributos:
 *  1. número da entrada (índice na tabela), começa em 1.
 *  2. código do átomo (ex.: "C01" para variable).
 *  3. lexeme: os 30 primeiros caracteres válidos, em caixa alta.
 *  4. qtdAntesTrunc: quantos caracteres válidos havia ANTES de truncar.
 *  5. qtdDepoisTrunc: quantos caracteres válidos ficaram DEPOIS de truncar (máx 30).
 *  6. tipo: sigla de 2 letras (IN, FP, ST, CH...) ou "-" se não tiver.
 *  7. linhas: as 5 PRIMEIRAS linhas onde o símbolo apareceu.
 */
public class EntradaSimbolo {

    private static final int MAX_LINHAS = 5;

    private final int numero;      // não muda
    private final String codigo;   // não muda
    private final String lexeme;   // não muda

    private int qtdAntesTrunc;     // pode mudar
    private int qtdDepoisTrunc;    // pode mudar
    private String tipo;           // pode mudar
    private final List<Integer> linhas; // pode mudar (até 5)

    public EntradaSimbolo(int numero, String codigo, String lexeme,
                          int qtdAntesTrunc, int qtdDepoisTrunc) {
        this.numero = numero;
        this.codigo = codigo;
        this.lexeme = lexeme;
        this.qtdAntesTrunc = qtdAntesTrunc;
        this.qtdDepoisTrunc = qtdDepoisTrunc;
        this.tipo = "-";           // começa sem tipo; só preenchemos quando dá para saber
        this.linhas = new ArrayList<>();
    }


    public void adicionarLinha(int linha) {
        if (linhas.size() < MAX_LINHAS) {
            linhas.add(linha);
        }
    }

    //getters usados pelo relatório tab
    public int getNumero()          { return numero; }
    public String getCodigo()       { return codigo; }
    public String getLexeme()       { return lexeme; }
    public int getQtdAntesTrunc()   { return qtdAntesTrunc; }
    public int getQtdDepoisTrunc()  { return qtdDepoisTrunc; }
    public String getTipo()         { return tipo; }
    public List<Integer> getLinhas(){ return linhas; }

    public void setTipo(String tipo) { this.tipo = tipo; }
}
