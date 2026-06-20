package br.ucsal.compiladores;

import br.ucsal.compiladores.lexico.AtomResult;
import br.ucsal.compiladores.lexico.Lexer;
import br.ucsal.compiladores.relatorio.RelatorioLex;
import br.ucsal.compiladores.relatorio.RelatorioTab;
import br.ucsal.compiladores.tabela.TabelaReservados;
import br.ucsal.compiladores.tabela.TabelaSimbolos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Fluxo:
 *  1. Recebe o nome base do arquivo (sem extensão) como argumento.
 *  2. Monta os caminhos do .261 (entrada), .LEX e .TAB (saídas).
 *  3. Lê o texto fonte para a memória.
 *  4. Carrega a tabela de reservados e cria a tabela de símbolos e o léxico.
 *  5. Chama o léxico em laço, juntando os átomos e registrando as linhas dos
 *     identificadores na tabela de símbolos (controle de escopo/linha).
 *  6. Gera os relatórios .lex e .tab
 *  */
public class Main {

    public static void main(String[] args) {
        // Linha que usamos para teste ja que em um dos computadores a aplicacao nao rodava, se esse for o caso, descomentar
        //if (args.length == 0) args = new String[]{"testes/Teste"};

        // 1) Verifica o argumento (nome base do arquivo, sem extensão).
        if (args.length < 1) {
            System.out.println("Uso: java br.ucsal.compiladores.Main <nomeBase>");
            System.out.println("Exemplo: java br.ucsal.compiladores.Main Teste");
            return;
        }
        String nomeBase = args[0];

        // 2) Monta os caminhos de entrada e saída. Se vier só o nome, usa o diretório corrente; se vier caminho, preserva.
        Path arquivoFonte = Paths.get(nomeBase + ".261");
        String caminhoLex = nomeBase + ".LEX";
        String caminhoTab = nomeBase + ".TAB";
        String nomeFonte = arquivoFonte.getFileName().toString();

        // 3) Verifica se o arquivo fonte existe e lê o conteúdo.
        if (!Files.exists(arquivoFonte)) {
            System.out.println("Erro: arquivo fonte nao encontrado: " + arquivoFonte);
            return;
        }
        String textoFonte;
        try {
            textoFonte = Files.readString(arquivoFonte);
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo fonte: " + e.getMessage());
            return;
        }

        // 4) Inicializa as estruturas.
        TabelaReservados reservados = new TabelaReservados();
        reservados.carregarReservados();              // pré-carga (uma vez)
        TabelaSimbolos tabela = new TabelaSimbolos();
        Lexer lexer = new Lexer(textoFonte, reservados, tabela);

        // 5) forma os átomos um a um até o EOF.
        List<AtomResult> atomos = new ArrayList<>();
        AtomResult atomo = lexer.proximoAtomo();
        while (!atomo.isEOF()) {
            atomos.add(atomo);
            if (atomo.indiceTabSimb() != -1) {
                tabela.registrarLinha(atomo.indiceTabSimb(), atomo.linha());
            }
            atomo = lexer.proximoAtomo();
        }

        // 6) Gera os dois relatórios Lex e Tab.
        try {
            RelatorioLex.gerar(caminhoLex, nomeFonte, atomos);
            RelatorioTab.gerar(caminhoTab, nomeFonte, tabela.getEntradas());
            System.out.println("Analise concluida.");
            System.out.println("Relatorios gerados: " + caminhoLex + " e " + caminhoTab);
        } catch (IOException e) {
            System.out.println("Erro ao gerar os relatorios: " + e.getMessage());
        }
    }
}
