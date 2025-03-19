package Entidades;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ContaBancaria {
    private String numeroConta;
    private String agencia;
    private Cliente cliente; 
    private double saldo;
    private double limite;
    private String tipoConta;
    private List<String> historicoTransacoes;


    public ContaBancaria(String numeroConta, String agencia, Cliente cliente, double saldo, double limite, String tipoConta) {
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.cliente = cliente;
        this.saldo = saldo;
        this.limite = limite;
        this.tipoConta = tipoConta;
        this.historicoTransacoes = new ArrayList<>();
    }
    
    public List<String> getHistoricoTransacoes() {
        return historicoTransacoes;
    }


    public void adicionarTransacao(String descricao) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataHora = LocalDateTime.now().format(formatter);
        historicoTransacoes.add(dataHora + " - " + descricao);
    }
    

    public boolean depositar(double valor) {
        if (valor > 0) {
            saldo += valor;
            adicionarTransacao("Deposito de R$ " + valor);
            return true;
        }
        return false;
    }


    public boolean sacar(double valor) {
        if (valor > 0 && (saldo + limite) >= valor) {
            saldo -= valor;
            adicionarTransacao("Saque de R$ " + valor);
            return true;
        } else {
            return false;
        }
    }

    public boolean transferir(ContaBancaria destino, double valor) {
        if (this.sacar(valor)) {
            destino.depositar(valor);
            adicionarTransacao("Transferencia de R$ " + valor + " para conta " + destino.getNumeroConta());
            destino.adicionarTransacao("Transferencia de R$ " + valor + " recebida da conta " + this.getNumeroConta());
            return true;
        } else {
            return false;
        }
    }


    public void alterarLimite(double novoLimite) {
        this.limite = novoLimite;
        adicionarTransacao("Limite alterado para R$ " + novoLimite);
    }


    public String getNumeroConta() { return numeroConta; }
    public double getSaldo() { return saldo; }
    public Cliente getCliente() { return cliente; }


    public void exibirInformacoes() {
        System.out.println("Número: " + numeroConta);
        System.out.println("Agência: " + agencia);
        System.out.println("Saldo: " + saldo);
        System.out.println("Limite: " + limite);
        System.out.println("Tipo: " + tipoConta);
    }
}