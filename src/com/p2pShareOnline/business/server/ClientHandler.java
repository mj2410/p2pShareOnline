package com.p2pShareOnline.business.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private String ip;
    private ArrayList<String> fileNames;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        fileNames = new ArrayList<String>();
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String massage = dataInputStream.readUTF();
                System.out.println("massage received in server side : " + massage);
                switch (massage) {
                    case "search":
                        massage = dataInputStream.readUTF();
                        System.out.println("search for: " + massage);
                        search(massage);
                        break;
                    case "getFile":
                        fileNames.add(dataInputStream.readUTF());
                        System.out.println("file added");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void search(String file) throws IOException {
        for (ClientHandler clientHandler : Server.getHandlers()) {
            if (clientHandler == this)
                continue;
            for (String fileName : fileNames) {
                if (fileName.contains(file)){
                    System.out.println("one file found");
                    dataOutputStream.writeUTF("pairIp");
                    dataOutputStream.flush();
                    dataOutputStream.writeUTF(clientHandler.getIp());
                    dataOutputStream.flush();
                    dataOutputStream.writeUTF(fileName);
                    return;
                }
            }
        }
    }

    public void setIp() {
        try {
            ip = dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getIp() {
        return ip;
    }
}
