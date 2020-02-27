package com.stoneknife.bio;

import ch.qos.logback.core.net.SyslogOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Scanner;

public class BioClient implements Runnable {

    private String host;

    private  int port;

    private static Charset charset = Charset.forName("UTF-8");

    public BioClient(String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try(Socket s = new Socket(host, port); OutputStream out = s.getOutputStream();) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入：");
            String message = scanner.nextLine();
            out.write(message.getBytes(charset));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BioClient bc = new BioClient("localhost", 9200);
        bc.run();
    }
}
