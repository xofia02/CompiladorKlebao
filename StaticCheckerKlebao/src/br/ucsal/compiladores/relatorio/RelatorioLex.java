package br.ucsal.compiladores.relatorio;

import br.ucsal.compiladores.lexico.AtomResult;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * RelatorioLex gera o arquivo .LEX
 * O relatório tem um cabeçalho (código da equipe, componentes, título e nome
 * do texto fonte) e uma linha-detalhe para CADA átomo, na ordem em que
 * apareceu (repetindo quantas vezes apareceu).
 */
public class RelatorioLex {

    /**
     * @param caminhoSaida   caminho completo do arquivo .LEX a ser criado
     * @param nomeFonte      nome do arquivo fonte analisado (ex.: "Teste.261")
     * @param atomos         lista de átomos na ordem em que apareceram
     */
    public static void gerar(String caminhoSaida, String nomeFonte, List<AtomResult> atomos)
            throws IOException {
        try (PrintWriter out = new PrintWriter(caminhoSaida, StandardCharsets.UTF_8)) {
            out.println("Código da Equipe: EQ05");
            out.println("Componentes:");
            out.println("Cauã Maia de Brito; caua.brito@ucsal.edu.br; 71 992072006");
            out.println("João Pedro Santana Sousa; joaopedrosantana.sousa@ucsal.edu.br; 71 983418181");
            out.println("Sofia Santos de Mendonça; sofia.mendonca@ucsal.edu.br; 11 989321746");
            out.println("Geraldo Manoel de Lima Alencar; geraldo.alencar@ucsal.edu.br; 71 992741706");
            out.println();
            out.println("                 RELATÓRIO DA ANÁLISE LÉXICA.");
            out.println("               Texto fonte analisado: " + nomeFonte);
            out.println();

            for (AtomResult a : atomos) {
                // Quando o átomo não vai para a tabela, mostra "-" no índice.
                String indice = (a.indiceTabSimb() == -1) ? "-" : String.valueOf(a.indiceTabSimb());
                out.println("Lexeme: " + a.lexeme() + ", Código: " + a.codigo()
                        + ", indiceTabSimb: " + indice + ", Linha: " + a.linha() + ".");
            }
        }
    }
}
