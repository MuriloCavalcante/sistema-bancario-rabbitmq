package com.exemplo.rabbitmq.publisher;

import com.exemplo.rabbitmq.modelo.EventoSaque;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServicoBanco {

    private double saldoConta;

    public ServicoBanco(double saldoInicial) {
        this.saldoConta = saldoInicial;
    }

    public void realizarSaque(String nomeCliente, String numeroConta, double valorSaque) throws Exception {
        if (valorSaque > saldoConta) {
            System.out.println("Saldo insuficiente!");
            System.out.println("Saldo disponível: R$ " + String.format("%.2f", saldoConta));
            System.out.println("Valor solicitado: R$ " + String.format("%.2f", valorSaque));
            return;
        }

        saldoConta -= valorSaque;

        String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        EventoSaque evento = new EventoSaque(
            numeroConta,
            nomeCliente,
            valorSaque,
            saldoConta,
            dataHora
        );

        PublicadorRabbitMQ publicador = new PublicadorRabbitMQ();
        try {
            publicador.publicarEvento(evento);
            System.out.println("Saque realizado com sucesso!");
            System.out.println("Novo saldo: R$ " + String.format("%.2f", saldoConta));
        } finally {
            publicador.fechar();
        }
    }

    public double getSaldoConta() {
        return saldoConta;
    }

    public void setSaldoConta(double saldo) {
        this.saldoConta = saldo;
    }
}
