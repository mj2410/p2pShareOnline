package com.p2pShareOnline.business;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Peer implements Runnable, Serializable {
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private ArrayList<File> files;
    private String pairIp;
    private ExecutorService pool = Executors.newCachedThreadPool();
    private Client client;


    public Peer(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream.writeUTF(InetAddress.getLocalHost().getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
        files = new ArrayList<File>();
    }

    @Override
    public void run() {
        String answer = "no";
        while (true) {
            try {
                String massage = dataInputStream.readUTF();
                System.out.println("client got massage : " + massage);
                switch (massage) {
                    case "pairIp":
                        client.setPairIp(dataInputStream.readUTF());
                        massage = dataInputStream.readUTF();
                        for (File file : files)
                            if (file.getName().equals(massage))
                                new Thread(new Listener(file,client.getPort())).start();
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void search(String fileName) throws IOException {
        dataOutputStream.writeUTF("search");
        dataOutputStream.flush();
        System.out.println("peer sent command");
        dataOutputStream.writeUTF(fileName);
        System.out.println("peer sent out file name");
        dataOutputStream.flush();
    }

    public void addFile(File file) {
        files.add(file);
        try {
            dataOutputStream.writeUTF("getFile");
            dataOutputStream.writeUTF(file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
