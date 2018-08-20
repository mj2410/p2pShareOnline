package com.p2pShareOnline.business;

import java.io.*;
import java.net.Socket;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class Download implements Runnable, Serializable {
    private String pairIp;
    private Socket p2p;

    public Download(String pairIp, int port) {
        this.pairIp = pairIp;
        try {
            p2p = new Socket(pairIp, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        try {
            DataInputStream input = new DataInputStream(p2p.getInputStream());
            File file = new File(Client.getLocation());
                if (!file.exists())
                    file.mkdir();
            RandomAccessFile randomAccessFile = new RandomAccessFile(new File(Client.getLocation() + "/" + input.readUTF()), "rw");
            while (input.read(buffer) != -1) {
                randomAccessFile.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
