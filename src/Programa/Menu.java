package Programa;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import Entidades.Cliente;
import Entidades.ContaBancaria;

public class Menu {
    private static ArrayList<ContaBancaria> contas = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public  void exibirMenu(){
        boolean executando = true;

        while (executando) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Cadastrar conta");
            System.out.println("2. Depositar");
            System.out.println("3. Sacar");
            System.out.println("4. Transferir");
            System.out.println("5. Alterar limite");
            System.out.println("6. Exportar histórico de transações");
            System.out.println("7. Exibir contas cadastradas");  // NOVO
            System.out.println("8. Sair");
            
            int opcao = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcao) {
            case 1: cadastrarConta(); break;
            case 2: depositar(); break;
            case 3: sacar(); break;
            case 4: transferir(); break;
            case 5: alterarLimite(); break;
            case 6: exportarHistorico(); break;
            case 7: exibirContas(); break;  // NOVO
            case 8:
                System.out.println("Finalizado");
                return;
            default:
                System.out.println("Opção inválida. Tente novamente.");
        }
    }
}

    private static void cadastrarConta() {
        System.out.println("\n--- Cadastro de Cliente ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        // Verifica se o cliente já possui uma conta
        for (ContaBancaria conta : contas) {
            if (conta.getCliente().getCpf().equals(cpf)) {
                System.out.println("Erro: Este cliente já possui uma conta cadastrada.");
                return;
            }
        }

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        Cliente cliente = new Cliente(nome, cpf, telefone, endereco);

        System.out.println("\n--- Cadastro de Conta Bancária ---");
        System.out.print("Número da conta: ");
        String numeroConta = scanner.nextLine();

        for (ContaBancaria conta : contas) {
            if (conta.getNumeroConta().equals(numeroConta)) {
                System.out.println("Erro: Já existe uma conta com este número. Escolha um número diferente.");
                return;
            }
        }

        System.out.print("Agência: ");
        String agencia = scanner.nextLine();
        System.out.print("Saldo inicial: ");
        double saldo = scanner.nextDouble();
        System.out.print("Limite: ");
        double limite = scanner.nextDouble();
        scanner.nextLine(); // Limpar buffer
        System.out.print("Tipo de conta (ex: Corrente, Poupança): ");
        String tipoConta = scanner.nextLine();

        ContaBancaria novaConta = new ContaBancaria(numeroConta, agencia, cliente, saldo, limite, tipoConta);
        contas.add(novaConta);

        System.out.println("Conta cadastrada com sucesso!");
    }
    
    private static double obterLimiteHorario(double limiteDiurno, double limiteNoturno) {
        LocalTime agora = LocalTime.now();
        int horaAtual = agora.getHour();
        return (horaAtual >= 6 && horaAtual < 22) ? limiteDiurno : limiteNoturno;
    }
    
    private static ContaBancaria buscarConta(String numeroConta) {
        for (ContaBancaria conta : contas) {
            if (conta.getNumeroConta().equals(numeroConta)) {
                return conta;
            }
        }
        System.out.println("Conta não encontrada.");
        return null;
    }

    private static void depositar() {
        System.out.println("\n--- Depósito ---");

        System.out.print("Número da conta: ");
        String numeroConta = scanner.nextLine();

        ContaBancaria conta = buscarConta(numeroConta);

        if (conta == null) {
            System.out.println("Erro: Conta não encontrada.");
            return;
        }

        System.out.print("Valor do depósito: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        if (conta.depositar(valor)) {
            System.out.println("Depósito realizado com sucesso.");
        } else {
            System.out.println("Erro: Valor inválido para depósito.");
        }
    }
    public static void sacar() {
        System.out.println("--- Saque ---");
        System.out.print("Número da conta: ");
        String numeroConta = scanner.nextLine();
        ContaBancaria conta = buscarConta(numeroConta);

        if (conta == null) {
            System.out.println("Conta não encontrada.");
            return;
        }

        System.out.print("Valor do saque: ");
        double valor = scanner.nextDouble();
        scanner.nextLine(); 


        double limiteHorario = obterLimiteHorario(5000.0, 1000.0);

        if (valor > limiteHorario) {
            System.out.println("Saque excede o limite permitido para o horário atual. Limite: R$ " + limiteHorario);
            return;
        }

        if (conta.sacar(valor)) {
            System.out.println("Saque realizado com sucesso.");
        } else {
            System.out.println("Saldo insuficiente ou valor inválido.");
        }
    }

    public static void transferir() {
        System.out.println("--- Transferência ---");
        System.out.print("Número da conta de origem: ");
        String numeroOrigem = scanner.nextLine();
        ContaBancaria origem = buscarConta(numeroOrigem);

        if (origem == null) {
            System.out.println("Conta de origem não encontrada.");
            return;
        }

        System.out.print("Número da conta de destino: ");
        String numeroDestino = scanner.nextLine();
        ContaBancaria destino = buscarConta(numeroDestino);

        if (destino == null) {
            System.out.println("Conta de destino não encontrada.");
            return;
        }

        System.out.print("Valor da transferência: ");
        double valor = scanner.nextDouble();
        scanner.nextLine(); 

        double limiteHorario = obterLimiteHorario(5000.0, 500.0);

        if (valor > limiteHorario) {
            System.out.println("Transferência excede o limite permitido para o horário atual. Limite: R$ " + limiteHorario);
            return;
        }

        if (origem.transferir(destino, valor)) {
            System.out.println("Transferência realizada com sucesso.");
        } else {
            System.out.println("Transferência não realizada. Saldo insuficiente.");
        }
    }
    public static void alterarLimite() {
        System.out.println("\n--- Alterar Limite ---");
        System.out.print("Número da conta: ");
        String numeroConta = scanner.nextLine();

        ContaBancaria conta = buscarConta(numeroConta);

        if (conta != null) {
            System.out.print("Novo limite: ");
            double novoLimite = scanner.nextDouble();
            scanner.nextLine();
            conta.alterarLimite(novoLimite);
            System.out.println("Limite alterado com sucesso.");
        } else {
            System.out.println("Conta não encontrada.");
        }
    }
    
    public static void exportarHistorico() {
        System.out.println("\n--- Exportar Histórico de Transações ---");
        System.out.print("Número da conta: ");
        String numeroConta = scanner.nextLine();

        ContaBancaria conta = buscarConta(numeroConta);

        if (conta != null) {
            try (FileWriter writer = new FileWriter("historico_" + numeroConta + ".csv")) {

                writer.append("Data e Hora,Descrição\n");


                for (String transacao : conta.getHistoricoTransacoes()) {
                    String[] partes = transacao.split(" - ", 2); 
                    if (partes.length == 2) {
                        writer.append(partes[0]).append(",").append(partes[1]).append("\n");
                    }
                }


                writer.append("\nSaldo Atual,R$ ").append(String.valueOf(conta.getSaldo())).append("\n");

                System.out.println("Histórico exportado com sucesso para 'historico_" + numeroConta + ".csv'");
            } catch (IOException e) {
                System.out.println("Erro ao exportar o histórico: " + e.getMessage());
            }
        } else {
            System.out.println("Conta não encontrada.");
        }
    }
    public static void exibirContas() {
        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
        } else {
            System.out.println("\n--- Contas Cadastradas ---");
            for (ContaBancaria conta : contas) {
                Cliente cliente = conta.getCliente();
                System.out.println("Cliente: " + cliente.getNome() + " (CPF: " + cliente.getCpf() + ")");
                conta.exibirInformacoes();  
                System.out.println("-------------------------");
            }
        }
    }
}