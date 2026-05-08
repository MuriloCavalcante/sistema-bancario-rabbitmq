package com.exemplo.rabbitmq.subscriber;

import com.exemplo.rabbitmq.config.RabbitMQConfig;
import com.exemplo.rabbitmq.modelo.EventoSaque;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

public class ConsumidorRabbitMQ {

    private Connection conexao;
    private Channel canal;
    private Gson gson;

    public ConsumidorRabbitMQ() throws Exception {
        this.gson = new Gson();
        conectar();
    }

    private void conectar() throws Exception {
        this.conexao = RabbitMQConfig.criarConexao();
        this.canal = conexao.createChannel();
        System.out.println("[SUBSCRIBER] Conectado ao RabbitMQ com sucesso!");
    }

    private void declararFila() throws Exception {
        canal.queueDeclare(RabbitMQConfig.NOME_FILA, true, false, false, null);
        System.out.println("[SUBSCRIBER] Fila '" + RabbitMQConfig.NOME_FILA + "' declarada!");
    }

    public void consumirMensagens() throws Exception {
        try {
            declararFila();
            canal.basicQos(1);

            System.out.println("[SUBSCRIBER] Aguardando mensagens na fila...");
            System.out.println("[SUBSCRIBER] Pressione CTRL+C para sair.\n");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    String mensagem = new String(delivery.getBody(), "UTF-8");
                    EventoSaque evento = gson.fromJson(mensagem, EventoSaque.class);
                    exibirEmailSaque(evento);
                    canal.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception e) {
                    System.err.println("[SUBSCRIBER] Erro ao processar mensagem: " + e.getMessage());
                    e.printStackTrace();
                    try {
                        canal.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };

            canal.basicConsume(RabbitMQConfig.NOME_FILA, false, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            System.err.println("Erro ao consumir mensagens: " + e.getMessage());
            throw e;
        }
    }

    private void exibirEmailSaque(EventoSaque evento) {
        System.out.println("========================");
        System.out.println("EMAIL ENVIADO");
        System.out.println("Cliente: " + evento.getNomeCliente());
        System.out.println("Conta: " + evento.getNumeroConta());
        System.out.println("Valor do saque: R$" + String.format("%.2f", evento.getValorSaque()));
        System.out.println("Saldo restante: R$" + String.format("%.2f", evento.getSaldoRestante()));
        System.out.println("Data: " + evento.getDataHora().split(" ")[0]);
        System.out.println("================");
        System.out.println();
    }


    public void fechar() throws Exception {
        if (canal != null && canal.isOpen()) {
            canal.close();
        }
        if (conexao != null && conexao.isOpen()) {
            conexao.close();
        }
        System.out.println("[SUBSCRIBER] Conexão fechada!");
    }
}
