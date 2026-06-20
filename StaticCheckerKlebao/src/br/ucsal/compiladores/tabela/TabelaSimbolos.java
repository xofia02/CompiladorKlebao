package br.ucsal.compiladores.tabela;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TabelaSimbolos é a tabela que guarda apenas os átomos do tipo
 * identificador encontrados no texto fonte (variable, programName,
 * functionName, intConst, realConst, stringConst, charConst).
 */
public class TabelaSimbolos {

    private final List<EntradaSimbolo> entradas = new ArrayList<>();

    private final Map<String, Integer> indicePorLexeme = new HashMap<>();

    /**
     * Procura o lexeme na tabela. Se já existir, devolve o número da entrada
     * existente (sem duplicar). Se não existir, cria uma nova entrada e devolve
     * o número dela.
     * @param lexeme         já em caixa alta e já truncado (30 chars)
     * @param codigo         código do átomo (ex.: "C01")
     * @param qtdAntesTrunc  nº de caracteres válidos antes de truncar
     * @param qtdDepoisTrunc nº de caracteres válidos depois de truncar
     * @return número da entrada (1-based)
     */
    public int buscarOuInserir(String lexeme, String codigo,
                               int qtdAntesTrunc, int qtdDepoisTrunc) {
        // Já existe --> Devolve o índice guardado.
        Integer existente = indicePorLexeme.get(lexeme);
        if (existente != null) {
            return existente;
        }
        // Não existe --> cria nova entrada. O número é a posição na lista + 1.
        int numero = entradas.size() + 1;
        EntradaSimbolo nova = new EntradaSimbolo(numero, codigo, lexeme,
                                                 qtdAntesTrunc, qtdDepoisTrunc);
        entradas.add(nova);
        indicePorLexeme.put(lexeme, numero);
        return numero;
    }

    /**
     * Registra a linha onde o símbolo apareceu
     * (até as 5 primeiras ocorrências). Chamado pelo Main a cada vez que o
     * identificador aparece no texto.
     */
    public void registrarLinha(int numero, int linha) {
        EntradaSimbolo e = entradas.get(numero - 1);
        e.adicionarLinha(linha);
    }

    /**
     * Define o tipo de um símbolo (sigla de 2 letras).
     */
    public void definirTipo(int numero, String tipo) {
        entradas.get(numero - 1).setTipo(tipo);
    }

    // Devolve todas as entradas em ordem para o relatório .TAB.
    public List<EntradaSimbolo> getEntradas() {
        return entradas;
    }
}
