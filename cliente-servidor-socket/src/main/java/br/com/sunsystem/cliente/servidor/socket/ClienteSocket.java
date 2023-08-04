/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sunsystem.cliente.servidor.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adelino
 */
public class ClienteSocket {
    
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public ClienteSocket(Socket socket) throws IOException {
        this.socket = socket;
        System.out.println("Cliente " + socket.getRemoteSocketAddress() + " conectado!");
        this.in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }
    
    public SocketAddress getRemoteSocketAddress(){
        return this.socket.getRemoteSocketAddress();
    }
    
    public void close(){
        try {
            this.in.close();
            this.out.close();
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClienteSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getMessage() {
        try {
            return this.in.readLine();
        } catch (IOException ex) {
            Logger.getLogger(ClienteSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public boolean sendMsg(String msg){
        out.println(msg);
        return !out.checkError();
    }
}
