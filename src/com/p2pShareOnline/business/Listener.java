package com.p2pShareOnline.business;

import java.beans.PropertyChangeListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener implements Runnable {
    private File file;
    private Socket pair;
    private int listenPort;

    public Listener(File file, int listenPort) {
        this.file = file;
        this.listenPort = listenPort;
    }

    @Override
    public void run() {
        try {
            ServerSocket miniServer = new ServerSocket(listenPort);
            pair = miniServer.accept();

            DataOutputStream dataOutputStream = new DataOutputStream(pair.getOutputStream());
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            dataOutputStream.writeUTF(file.getName());
            byte[] buffer = new byte[1024];
            while (randomAccessFile.read(buffer) != -1) {
                dataOutputStream.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
