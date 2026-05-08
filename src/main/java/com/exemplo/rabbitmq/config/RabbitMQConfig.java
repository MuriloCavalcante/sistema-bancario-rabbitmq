package com.exemplo.rabbitmq.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConfig {

    public static final String HOST = "localhost";
    public static final int PORTA = 5672;
    public static final String USUARIO = "guest";
    public static final String SENHA = "guest";
    public static final String NOME_FILA = "fila.saque";

    public static Connection criarConexao() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORTA);
        factory.setUsername(USUARIO);
        factory.setPassword(SENHA);
        return factory.newConnection();
    }
}
