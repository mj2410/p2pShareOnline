package com.p2pShareOnline.business;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Peer peer;
    private static String location = System.getProperty("user.home") + File.separator + "SharedFiles";
    private String pairIp;
    private static String serverIp = "127.0.0.1";
    private static int port = 3000;
    private static int i = 1;

    public void connectServer() throws IOException {
            Socket socket = new Socket(InetAddress.getLocalHost().getHostAddress(), 3434);
            peer = new Peer(socket,this);
            new Thread(peer).start();
    }

    public void disconnect() {
    }

    public Peer getPeer() {
        return peer;
    }

    public void addFile(File file) {
        peer.addFile(file);
    }

    public void search(String string) {
        try {
            peer.search(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLocation() {
        return location;
    }

    public static void setLocation(String location) {
        Client.location = location;
    }

    public void download() {
        if (pairIp == null) {
            return;
        }
        System.out.println("download started");
        Thread thread = new Thread(new Download(pairIp, getPort()));
        thread.start();
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.connectServer();
            client.addFile(new File("D:/JTank/JTank.iml"));
            client.search("JTank");
            Thread.sleep(100);
            client.download();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setPairIp(String pairIp) {
        this.pairIp = pairIp;
    }

    public int getPort() {
        if (i % 3 == 0)
            port++;
        i++;
        return port;
    }
}
