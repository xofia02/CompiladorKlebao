package br.ucsal.compiladores.relatorio;

import br.ucsal.compiladores.tabela.EntradaSimbolo;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * RelatorioTab gera o arquivo .TAB
 * Mostra a situação FINAL da tabela: para cada entrada, todos os seus atributos
 * (número, código, lexeme, contagens de caracteres, tipo e linhas) separando
 * as entradas com uma linha.
 */
public class RelatorioTab {

    /**
     * @param caminhoSaida caminho completo do arquivo .TAB a ser criado
     * @param nomeFonte    nome do arquivo fonte analisado (ex.: "Teste.261")
     * @param entradas     entradas da tabela de símbolos, em ordem
     */
    public static void gerar(String caminhoSaida, String nomeFonte, List<EntradaSimbolo> entradas)
            throws IOException {
        try (PrintWriter out = new PrintWriter(caminhoSaida, StandardCharsets.UTF_8)) {
            out.println("Código da Equipe: EQ05");
            out.println("Componentes:");
            out.println("Cauã Maia de Brito; caua.brito@ucsal.edu.br; 71 992072006");
            out.println("João Pedro Santana Sousa; joaopedrosantana.sousa@ucsal.edu.br; 71 983418181");
            out.println("Sofia Santos de Mendonça; sofia.mendonca@ucsal.edu.br; 11 989321746");
            out.println("Geraldo Manoel de Lima Alencar; geraldo.alencar@ucsal.edu.br; 71 992741706");
            out.println();
            out.println("               RELATÓRIO DA TABELA DE SÍMBOLOS.");
            out.println("               Texto fonte analisado: " + nomeFonte);
            out.println();

            for (int i = 0; i < entradas.size(); i++) {
                EntradaSimbolo e = entradas.get(i);

                out.println("Entrada: " + e.getNumero() + ", Código: " + e.getCodigo()
                        + ", Lexeme: " + e.getLexeme() + ",");
                out.println("QtdCharsAntesTrunc: " + e.getQtdAntesTrunc()
                        + ", QtdCharDepoisTrunc: " + e.getQtdDepoisTrunc() + ",");
                out.println("TipoSimb: " + e.getTipo() + ", Linhas: " + formatarLinhas(e.getLinhas()) + ".");

                // Separador entre entradas (não imprime após a última).
                if (i < entradas.size() - 1) {
                    out.println("----------------------------------------------------------------");
                }
            }
        }
    }

    /** Formata a lista de linhas como "(1, 2, 2, 3)", igual ao exemplo da spec. */
    private static String formatarLinhas(List<Integer> linhas) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < linhas.size(); i++) {
            sb.append(linhas.get(i));
            if (i < linhas.size() - 1) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }
}
