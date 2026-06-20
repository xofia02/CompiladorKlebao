# Static Checker — Klebão2026-1 (Etapa 7 / LEX)

Implementação do Static Checker da linguagem Klebão2026-1: programa principal
(controlador), análise léxica e tabela de símbolos. Gera os relatórios .LEX e .TAB.

## Estrutura

    src/br/ucsal/compiladores/
        Main.java                  -> programa principal (Componente A)
        lexico/
            Lexer.java             -> analisador léxico (Componente B)
            AtomResult.java        -> contrato de retorno de um átomo
        tabela/
            TabelaSimbolos.java    -> tabela de símbolos dinâmica (Componente C)
            EntradaSimbolo.java    -> modelo de uma entrada da tabela
            TabelaReservados.java  -> tabela fixa de reservados (A.. e B..)
        relatorio/
            RelatorioLex.java      -> gera o arquivo .LEX (Componente D)
            RelatorioTab.java      -> gera o arquivo .TAB (Componente D)
    testes/
        Teste.261                  -> arquivo de teste (exemplo da especificação)

## Como compilar e executar (no IntelliJ ou linha de comando)

Compilar (JDK 21):

    javac -d out src/br/ucsal/compiladores/*.java src/br/ucsal/compiladores/*/*.java

Executar (passar apenas o nome base do arquivo, sem extensão):

    java -cp out br.ucsal.compiladores.Main testes/Teste

Isso lê testes/Teste.261 e gera testes/Teste.LEX e testes/Teste.TAB na mesma pasta.

## Observações

- O programa não diferencia maiúsculas de minúsculas (tudo é tratado como maiúsculo).
- Átomos são truncados em 30 caracteres válidos.
- O cabeçalho dos relatórios (.LEX e .TAB) tem campos [CÓDIGO]/[Nome]/[email]/
  [telefone] para a equipe preencher com os dados reais antes da entrega.
