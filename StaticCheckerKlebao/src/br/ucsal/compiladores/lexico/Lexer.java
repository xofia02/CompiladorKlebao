package br.ucsal.compiladores.lexico;

import br.ucsal.compiladores.tabela.TabelaReservados;
import br.ucsal.compiladores.tabela.TabelaSimbolos;

/**
 * Lê o texto fonte caractere a caractere e, a cada chamada de proximoAtomo(),
 * forma e devolve 1 átomo, usando o critério do máximo comprimento possível
 * Responsabilidades:
 *  - converter tudo para CAIXA ALTA;
 *  - filtro de primeiro nível: descartar caracteres inválidos sem dar erro;
 *  - pular espaços/arrumação e comentários (de bloco e de linha);
 *  - reconhecer os padrões léxicos do Apêndice C (identificadores, números,
 *    cadeias, símbolos reservados);
 *  - truncar átomos em 30 caracteres válidos;
 *  - classificar o átomo (código A.., B.. ou C..);
 *  - inserir identificadores na tabela de símbolos.
 */
public class Lexer {

    private static final int MAX_ATOMO = 30; // limite de caracteres válidos por átomo

    private final String fonte;              // texto fonte inteiro em maiúsculas
    private final TabelaReservados reservados;
    private final TabelaSimbolos tabela;

    private int pos = 0;                     // posição de leitura
    private int linhaAtual = 1;              // linha (começa em 1)

    public Lexer(String textoFonte, TabelaReservados reservados, TabelaSimbolos tabela) {
        // Converte todo o texto para caixa alta logo de uma vez.
        this.fonte = textoFonte.toUpperCase();
        this.reservados = reservados;
        this.tabela = tabela;
    }

    /** Devolve o número da linha corrente (usado pelo Main, se precisar). */
    public int getLinhaAtual() {
        return linhaAtual;
    }

    /**
     * Forma e devolve o próximo átomo.
     * Quando o arquivo termina, devolve um AtomResult com código EOF.
     */
    public AtomResult proximoAtomo() {
        // 1) Pular tudo o que não faz parte de um átomo: espaços, arrumação,
        //    comentários e caracteres inválidos (filtro de primeiro nível).
        pularIrrelevantes();

        // Se chegou ao fim do arquivo mostra EOF.
        if (pos >= fonte.length()) {
            return new AtomResult("", AtomResult.EOF, -1, linhaAtual);
        }

        int linhaInicio = linhaAtual;
        char c = fonte.charAt(pos);

        // 2) Decide qual padrão seguir olhando o primeiro caractere.
        if (ehLetra(c) || c == '_') {
            return formarIdentificador(linhaInicio);
        }
        if (ehDigito(c)) {
            return formarNumero(linhaInicio);
        }
        if (c == '"') {
            return formarString(linhaInicio);
        }
        if (c == '\'') {
            return formarChar(linhaInicio);
        }
        return formarSimbolo(linhaInicio);
    }

    //  ETAPA 1: pular espaços, arrumação, comentários e inválidos
    private void pularIrrelevantes() {
        boolean continuar = true;
        while (continuar && pos < fonte.length()) {
            char c = fonte.charAt(pos);

            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                if (c == '\n') linhaAtual++;   // ASCII 10 incrementa a linha
                pos++;
            }
            else if (c == '/' && espiar(1) == '*') {
                pularComentarioBloco();
            }
            else if (c == '/' && espiar(1) == '/') {
                pularComentarioLinha();
            }
            else if (ehInvalido(c)) {
                pos++;
            }
            else {
                continuar = false;
            }
        }
    }

    private void pularComentarioBloco() {
        pos += 2;
        while (pos < fonte.length()) {
            char c = fonte.charAt(pos);
            if (c == '*' && espiar(1) == '/') {
                pos += 2;
                return;
            }
            if (c == '\n') linhaAtual++;
            pos++;
        }
    }

    private void pularComentarioLinha() {
        pos += 2;
        while (pos < fonte.length() && fonte.charAt(pos) != '\n') {
            pos++;
        }
    }

    //  ETAPA 2: padrões léxicos (Apêndice C)
    /**
     * Forma um identificador OU palavra reservada.
     * Padrão: começa com letra ou '_'; segue com letras, dígitos e '_'.
     * Depois de formado, consulta a tabela de reservados.
     */
    private AtomResult formarIdentificador(int linhaInicio) {
        int qtdValidos = 0;          // conta TODOS os caracteres válidos do átomo
        StringBuilder sb = new StringBuilder();

        while (pos < fonte.length()) {
            char c = fonte.charAt(pos);
            if (ehLetra(c) || ehDigito(c) || c == '_') {
                qtdValidos++;
                if (qtdValidos <= MAX_ATOMO) sb.append(c); // trunca em 30
                pos++;
            } else if (ehInvalido(c)) {
                pos++; // caractere inválido no meio: descarta e continua
            } else {
                break; //fim do átomo
            }
        }

        String lexeme = sb.toString();
        int qtdDepois = Math.min(qtdValidos, MAX_ATOMO);

        String codReservado = reservados.consultar(lexeme);
        if (codReservado != null) {
            return new AtomResult(lexeme, codReservado, -1, linhaInicio);
        }

        int indice = tabela.buscarOuInserir(lexeme, "C01", qtdValidos, qtdDepois);
        return new AtomResult(lexeme, "C01", indice, linhaInicio);
    }

    /**
     * Forma um número: intConst (só dígitos) ou realConst (com '.' e dígitos,
     * com parte exponencial opcional).
     */
    private AtomResult formarNumero(int linhaInicio) {
        int qtdValidos = 0;
        StringBuilder sb = new StringBuilder();
        boolean ehReal = false;

        qtdValidos = consumirDigitos(sb, qtdValidos);

        if (pos < fonte.length() && fonte.charAt(pos) == '.') {
            ehReal = true;
            qtdValidos++;
            if (qtdValidos <= MAX_ATOMO) sb.append('.');
            pos++;
            qtdValidos = consumirDigitos(sb, qtdValidos);

            if (pos < fonte.length() && fonte.charAt(pos) == 'E') {
                qtdValidos++;
                if (qtdValidos <= MAX_ATOMO) sb.append('E');
                pos++;
                if (pos < fonte.length() && (fonte.charAt(pos) == '+' || fonte.charAt(pos) == '-')) {
                    qtdValidos++;
                    if (qtdValidos <= MAX_ATOMO) sb.append(fonte.charAt(pos));
                    pos++;
                }
                qtdValidos = consumirDigitos(sb, qtdValidos);
            }
        }

        String lexeme = sb.toString();
        int qtdDepois = Math.min(qtdValidos, MAX_ATOMO);

        String codigo = ehReal ? "C07" : "C06";
        int indice = tabela.buscarOuInserir(lexeme, codigo, qtdValidos, qtdDepois);
        tabela.definirTipo(indice, ehReal ? "FP" : "IN");
        return new AtomResult(lexeme, codigo, indice, linhaInicio);
    }

    private int consumirDigitos(StringBuilder sb, int qtdValidos) {
        while (pos < fonte.length() && ehDigito(fonte.charAt(pos))) {
            qtdValidos++;
            if (qtdValidos <= MAX_ATOMO) sb.append(fonte.charAt(pos));
            pos++;
        }
        return qtdValidos;
    }

    private AtomResult formarString(int linhaInicio) {
        int qtdValidos = 0;
        StringBuilder sb = new StringBuilder();

        qtdValidos++;
        sb.append('"');
        pos++;

        while (pos < fonte.length() && fonte.charAt(pos) != '"') {
            char c = fonte.charAt(pos);
            if (c == '\n') linhaAtual++; // pode cruzar linha; conta a linha
            qtdValidos++;
            if (qtdValidos <= MAX_ATOMO) sb.append(c);
            pos++;
        }

        if (pos < fonte.length() && fonte.charAt(pos) == '"') {
            qtdValidos++;
            if (qtdValidos <= MAX_ATOMO) sb.append('"');
            pos++;
        }

        String lexeme = sb.toString();
        int qtdDepois = Math.min(qtdValidos, MAX_ATOMO);
        int indice = tabela.buscarOuInserir(lexeme, "C04", qtdValidos, qtdDepois);
        tabela.definirTipo(indice, "ST"); // stringConst -> ST
        return new AtomResult(lexeme, "C04", indice, linhaInicio);
    }

    /**
     * Forma um charConst: aspas simples, uma letra, aspas simples. As aspas
     * contam no tamanho. Código no Apêndice A: charConst = C05.
     */
    private AtomResult formarChar(int linhaInicio) {
        int qtdValidos = 0;
        StringBuilder sb = new StringBuilder();

        qtdValidos++;
        sb.append('\'');
        pos++;

        while (pos < fonte.length() && fonte.charAt(pos) != '\'') {
            char c = fonte.charAt(pos);
            if (c == '\n') linhaAtual++;
            qtdValidos++;
            if (qtdValidos <= MAX_ATOMO) sb.append(c);
            pos++;
        }

        if (pos < fonte.length() && fonte.charAt(pos) == '\'') {
            qtdValidos++;
            if (qtdValidos <= MAX_ATOMO) sb.append('\'');
            pos++;
        }

        String lexeme = sb.toString();
        int qtdDepois = Math.min(qtdValidos, MAX_ATOMO);
        int indice = tabela.buscarOuInserir(lexeme, "C05", qtdValidos, qtdDepois);
        tabela.definirTipo(indice, "CH"); // charConst -> CH
        return new AtomResult(lexeme, "C05", indice, linhaInicio);
    }

    /**
     * Forma um símbolo reservado. Tenta primeiro casar os símbolos de DOIS
     * caracteres (:= <= >= == !=) e, se não der, usa o de um caractere.
     * Símbolos NÃO vão para a tabela de símbolos (indiceTabSimb = -1).
     */
    private AtomResult formarSimbolo(int linhaInicio) {
        char c = fonte.charAt(pos);
        char prox = espiar(1);

        // Tenta os símbolos de dois caracteres primeiro.
        String doisChars = "" + c + prox;
        if (doisChars.equals(":=") || doisChars.equals("<=") || doisChars.equals(">=")
                || doisChars.equals("==") || doisChars.equals("!=")) {
            String cod = reservados.consultar(doisChars);
            pos += 2;
            return new AtomResult(doisChars, cod, -1, linhaInicio);
        }

        String umChar = String.valueOf(c);
        String cod = reservados.consultar(umChar);
        pos++;
        if (cod != null) {
            return new AtomResult(umChar, cod, -1, linhaInicio);
        }
        return proximoAtomo();
    }

    //  Auxiliares
    private char espiar(int offset) {
        int p = pos + offset;
        return (p < fonte.length()) ? fonte.charAt(p) : '\0';
    }

    private boolean ehLetra(char c)  { return c >= 'A' && c <= 'Z'; } // já está em maiúsculas
    private boolean ehDigito(char c) { return c >= '0' && c <= '9'; }

    /**
     * Um caractere é inválido se NÃO for um caractere válido da linguagem.
     * Caracteres válidos: letras, dígitos, '_', os símbolos reservados, aspas,
     * o ponto, e a arrumação (espaço/tab/quebra de linha). Os de arrumação são
     * tratados antes; aqui só decidimos se um caractere "solto" é inválido.
     */
    private boolean ehInvalido(char c) {
        if (ehLetra(c) || ehDigito(c)) return false;
        // caracteres válidos isolados da linguagem:
        String validos = "_;,:?()[]{}+-*/%=<>!#\"'. \t\n\r";
        return validos.indexOf(c) < 0;
    }
}
