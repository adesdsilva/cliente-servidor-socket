/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sunsystem.cliente.servidor.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 *
 * @author adelino
 */
public class Servidor implements Runnable {
    
    public static final int PORT = 4000;
    private static ServerSocket servidor;
    private final List<ClienteSocket> clientes = new LinkedList<>();

    public Servidor(int parseInt, JTable tRequests, JLabel lMessage) {
    }

    private Servidor() {
        
    }
    
    public void start() throws IOException{
        Servidor.servidor = new ServerSocket(PORT);
        System.out.println("Servidor iniciado na porta: " + PORT);
        clientConnectionLoop();
    }
    
    private void clientConnectionLoop() throws IOException{
        while(true){
            ClienteSocket clientSocket = new ClienteSocket(servidor.accept()); 
            this.clientes.add(clientSocket);
            new Thread(() -> this.clientMessageLoop(clientSocket)).start();
        }
    }
    
    private void clientMessageLoop(ClienteSocket clienteSocket){
        String msg;
        try{
            while((msg = clienteSocket.getMessage()) != null){
            if("sair".equalsIgnoreCase(msg)){
                return;
            }else{
                System.out.printf("Mensagem recebida do cliente %s: %s\n", 
                    clienteSocket.getRemoteSocketAddress(),
                    msg);
                this.sendMessageForAll(clienteSocket, msg);
            }          
        }
        }finally {
            clienteSocket.close();
        }       
    }
    
    private void sendMessageForAll(ClienteSocket emissor, String msg){
        Iterator<ClienteSocket> iterator = this.clientes.iterator();
        while(iterator.hasNext()) {
            ClienteSocket clienteSocket = iterator.next();
            if(!emissor.equals(clienteSocket)){
                if(!clienteSocket.sendMsg("Cliente: " + 
                        emissor.getRemoteSocketAddress() + msg)){
                    iterator.remove();
                }
            }           
        }
    }

    @Override
    public void run() {
        try{
            Servidor s = new Servidor();
            s.start();
        } catch(IOException ioe){
            System.out.println("Erro ao executar o servidor: " + ioe.getMessage());
        }
//       try {
//            servidor = new ServerSocket(50000);
//            System.out.println("Servidor iniciado. Aguardando conexões...");
//
//            while (true) {
//                Socket socket = servidor.accept();
//                System.out.println("Novo cliente conectado: " + 
//                        socket.getInetAddress() + ":" + PORT);
//
//                new Thread(() -> {
//                    try {
//                        servidor.accept();
//                    } catch (IOException ex) {
//                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }).start();
//            }
//        } catch (IOException e) {
//        } finally {
//            try {
//                if (servidor != null)
//                    servidor.close();
//            } catch (IOException e) {
//            }
//        }
    }
    
    public static void main(String[] args) {
        try{
            Servidor s = new Servidor();
            s.start();
        } catch(IOException ioe){
            System.out.println("Erro ao executar o servidor: " + ioe.getMessage());
        }
    }
}
