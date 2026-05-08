package com.exemplo.rabbitmq.modelo;

public class EventoSaque {

    private String numeroConta;
    private String nomeCliente;
    private double valorSaque;
    private double saldoRestante;
    private String dataHora;

    public EventoSaque() {
    }

    public EventoSaque(String numeroConta, String nomeCliente, double valorSaque, 
                       double saldoRestante, String dataHora) {
        this.numeroConta = numeroConta;
        this.nomeCliente = nomeCliente;
        this.valorSaque = valorSaque;
        this.saldoRestante = saldoRestante;
        this.dataHora = dataHora;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public double getValorSaque() {
        return valorSaque;
    }

    public void setValorSaque(double valorSaque) {
        this.valorSaque = valorSaque;
    }

    public double getSaldoRestante() {
        return saldoRestante;
    }

    public void setSaldoRestante(double saldoRestante) {
        this.saldoRestante = saldoRestante;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return "EventoSaque{" +
                "numeroConta='" + numeroConta + '\'' +
                ", nomeCliente='" + nomeCliente + '\'' +
                ", valorSaque=" + valorSaque +
                ", saldoRestante=" + saldoRestante +
                ", dataHora='" + dataHora + '\'' +
                '}';
    }
}
