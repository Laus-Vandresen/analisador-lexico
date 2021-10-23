import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Analisador {

    public static final char OPERADOR_FINALIZACAO = ';';
    public static int estado = 0;
    public static StringBuilder tokenAtual = new StringBuilder();
    public static List<String> tokens = new ArrayList<>();

    public static void analisar() throws Exception {

        char[] caracteres = lerArquivo();
        for (char caracter: caracteres) {
            switch (estado) {
                case 0:
                    if (String.valueOf(caracter).matches("[a-zA-Z]+")) {
                        tokenAtual.append(caracter);
                        estado = 1;
                    }
                    else if (String.valueOf(caracter).matches("^[0-9]")) {
                        tokenAtual.append(caracter);
                        estado = 3;
                    } else if (caracter == '=') {
                        tokenAtual.append(caracter);
                        estado = 5;
                    } else if (caracter == '+') {
                        tokenAtual.append(caracter);
                        estado = 7;
                    } else {
                        throw new Exception("Erro léxico");
                    }
                    break;
                case 1:
                    if (String.valueOf(caracter).matches("[a-zA-Z]+") || String.valueOf(caracter).matches("^[0-9]") || caracter == '_') {
                        tokenAtual.append(caracter);
                        break;
                    } else if (caracter == OPERADOR_FINALIZACAO) {
                        finalizaToken("identificadores");
                        break;
                    }
                    break;
                case 3:
                    if (String.valueOf(caracter).matches("^[0-9]")) {
                        tokenAtual.append(caracter);
                    } else if (caracter == OPERADOR_FINALIZACAO) {
                        finalizaToken("número inteiro");
                        break;
                    } else {
                        throw new Exception("Erro léxico");
                    }
                    break;
                case 5:
                    if (caracter == OPERADOR_FINALIZACAO) {
                        finalizaToken("atribuição");
                        break;
                    } else {
                        throw new Exception("Erro léxico");
                    }
                case 7:
                    if (caracter == OPERADOR_FINALIZACAO) {
                        finalizaToken("operadores");
                        break;
                    } else {
                        throw new Exception("Erro léxico");
                    }
            }
        }
        tokens.forEach(System.out::println);
    }

    private static void finalizaToken(String tipoToken) {
        tokens.add("Token: " + tokenAtual.toString() + "    Tipo: " + tipoToken);
        tokenAtual.setLength(0);
        estado = 0;
    }

    private static char[] lerArquivo() {
        try {
            InputStream fis = new FileInputStream("expressaoAnalisada.txt");
            Reader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String linha = br.readLine();
            StringBuilder caracteres = new StringBuilder();
            while (linha != null) {
                caracteres.append(linha.toCharArray());
                linha = br.readLine();
            }
            return caracteres.toString().toCharArray();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível ler o arquivo");
        }
        return null;
    }
}
