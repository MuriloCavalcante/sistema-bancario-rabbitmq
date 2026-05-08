package com.exemplo.rabbitmq.publisher;

public class PublisherMain {

    public static void main(String[] args) {
        try {
            System.out.println("========================================");
            System.out.println("  SISTEMA BANCÁRIO - PUBLICADOR");
            System.out.println("========================================\n");

            ServicoBanco banco = new ServicoBanco(5000);

            System.out.println("Saldo inicial: R$ 5000,00\n");

            realizarMultiplosSaques(banco);

            System.out.println("\n========================================");
            System.out.println("  Todas as operações concluídas!");
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("Erro ao executar o sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void realizarMultiplosSaques(ServicoBanco banco) throws Exception {
        String[][] saques = {
            {"João Silva", "12345", "200"},
            {"Maria Souza", "67890", "150"},
            {"Carlos Lima", "54321", "300"}
        };

        for (int i = 0; i < saques.length; i++) {
            String nome = saques[i][0];
            String conta = saques[i][1];
            double valor = Double.parseDouble(saques[i][2]);

            System.out.println("----------------------------------------");
            System.out.println("Operação " + (i + 1) + " de " + saques.length);
            System.out.println("Cliente: " + nome);
            System.out.println("Conta: " + conta);
            System.out.println("Valor do saque: R$ " + String.format("%.2f", valor));
            System.out.println("----------------------------------------\n");

            banco.realizarSaque(nome, conta, valor);

            if (i < saques.length - 1) {
                System.out.println("Aguardando 2 segundos para próxima operação...\n");
                Thread.sleep(2000);
            }
        }
    }
}
