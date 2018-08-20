package com.p2pShareOnline.business.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private static ArrayList<ClientHandler> handlers;
    public static String serverIP = "127.0.0.1";
    public static final int defaultPort = 3434;
    private ExecutorService pool = Executors.newCachedThreadPool();

    public Server() {
        handlers = new ArrayList<ClientHandler>();
        try {
            serverSocket = new ServerSocket(defaultPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            System.out.println("waiting for a client to be connected.");
            try{
                Socket socket = serverSocket.accept();
                System.out.println("client connected.");
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.setIp();
                handlers.add(clientHandler);
                pool.execute(clientHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<ClientHandler> getHandlers() {
        return handlers;
    }

    public static void main(String[] args) {
        new Server();
    }

}
