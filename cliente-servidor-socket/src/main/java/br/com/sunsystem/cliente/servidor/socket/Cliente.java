/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sunsystem.cliente.servidor.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author adelino
 */
public class Cliente implements Runnable {

    private static final String SERVER_ADDRESS = "127.0.0.1";

    private static Socket socket;

    private static ObjectOutputStream saida;

    private ObjectInputStream entrada;

    private ClienteSocket clienteSocket;

    private Scanner scanner;

    public Cliente() {
        this.scanner = new Scanner(System.in);
    }

    public Cliente(Socket socket) {
        Cliente.socket = socket;
    }

    public void start() throws IOException {
        try {
            this.clienteSocket = new ClienteSocket(new Socket(SERVER_ADDRESS, Servidor.PORT));
            System.out.println(""
                    + "Cliente conectado ao servidor em: " + SERVER_ADDRESS + " :"
                    + Servidor.PORT);
            new Thread(this).start();
            messageLoop();
        } finally {
            this.clienteSocket.close();
        }
    }

    private void messageLoop() {
        String msg;
        do {
            System.out.println("Digite uma mensagem (ou sair para finalizar): ");
            msg = this.scanner.nextLine();
            this.clienteSocket.sendMsg(msg);
        } while (!msg.equalsIgnoreCase("sair"));
    }

    @Override
    public void run() {
        String msg;
        while ((msg = this.clienteSocket.getMessage()) != null) {
            System.out.printf("Mensagem recebida do servidor: %s\n", msg);
        }

    }

    public static void main(String[] args) {

        try {         
            Cliente cliente = new Cliente();
            cliente.start();
        } catch (IOException e) {

        }

    }
}
