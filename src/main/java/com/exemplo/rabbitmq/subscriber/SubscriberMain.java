package com.exemplo.rabbitmq.subscriber;

public class SubscriberMain {

    public static void main(String[] args) {
        try {
            System.out.println("========================================");
            System.out.println("  SISTEMA BANCÁRIO - CONSUMIDOR");
            System.out.println("========================================\n");

            ConsumidorRabbitMQ consumidor = new ConsumidorRabbitMQ();
            consumidor.consumirMensagens();
            Thread.currentThread().join();

        } catch (InterruptedException e) {
            System.out.println("\n[SUBSCRIBER] Aplicação encerrada pelo usuário.");
        } catch (Exception e) {
            System.err.println("Erro ao executar o consumidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
