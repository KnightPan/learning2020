package com.stoneknife.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class BioServerThread {
    private static Charset charset = Charset.forName("UTF-8");

    public static void main(String[] args) {

        int port = 9010;

        try(ServerSocket ss = new ServerSocket(port);) {
            while (true){
                try {
                    Socket s = ss.accept();
                   new Thread(new ProgressThread(s)).start();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ProgressThread implements Runnable {

        Socket s;
         public ProgressThread(Socket s) {
             super();
             this.s = s;
         }
        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream(),charset));

                String message = null;
                while((message = reader.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}