package com.exemplo.rabbitmq.publisher;

import com.exemplo.rabbitmq.config.RabbitMQConfig;
import com.exemplo.rabbitmq.modelo.EventoSaque;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class PublicadorRabbitMQ {

    private Connection conexao;
    private Channel canal;
    private Gson gson;

    public PublicadorRabbitMQ() throws Exception {
        this.gson = new Gson();
        conectar();
    }

    private void conectar() throws Exception {
        this.conexao = RabbitMQConfig.criarConexao();
        this.canal = conexao.createChannel();
        System.out.println("[PUBLISHER] Conectado ao RabbitMQ com sucesso!");
    }

    private void declararFila() throws Exception {
        canal.queueDeclare(RabbitMQConfig.NOME_FILA, true, false, false, null);
        System.out.println("[PUBLISHER] Fila '" + RabbitMQConfig.NOME_FILA + "' declarada!");
    }

    public void publicarEvento(EventoSaque evento) throws Exception {
        try {
            declararFila();
            String mensagem = gson.toJson(evento);
            canal.basicPublish("", RabbitMQConfig.NOME_FILA, null, mensagem.getBytes());
            System.out.println("Evento enviado para fila com sucesso!");
            System.out.println("[PUBLISHER] Mensagem: " + mensagem);
        } catch (Exception e) {
            System.err.println("Erro ao publicar evento: " + e.getMessage());
            throw e;
        }
    }

    public void fechar() throws Exception {
        if (canal != null && canal.isOpen()) {
            canal.close();
        }
        if (conexao != null && conexao.isOpen()) {
            conexao.close();
        }
        System.out.println("[PUBLISHER] Conexão fechada!");
    }
}
