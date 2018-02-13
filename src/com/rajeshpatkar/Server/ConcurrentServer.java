package com.rajeshpatkar.Server;

import static com.rajeshpatkar.Server.ConcurrentServer.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConcurrentServer{

    public static ArrayList<PrintWriter> al = new ArrayList<>();
    static String LogFile = "D:\\Server\\Log.txt";
    public static PrintWriter pwLog;

    static{
        try {
            pwLog = new PrintWriter(
                        new BufferedWriter(
                            new FileWriter(LogFile,true)
                        ),true
            );
        } catch (IOException ex) {
            System.out.println("Some Problem with IO or File");
        }
    }
    public static void main(String[] args) throws Exception {
        System.out.println("Server signing On");
        ServerSocket ss = new ServerSocket(9081);
        for (int i = 0; i < 10; i++) {
            Socket soc = ss.accept();
            Conversation c = new Conversation(soc);
            c.start();
        }
        System.out.println("Server signing Off");
    }

}

class Conversation extends Thread {

    Socket soc;
    String Username;
    
    
    public Conversation(Socket soc) {
        this.soc = soc;
    }

    @Override
    public void run() {
        System.out.println("Conversation thread  "
                + Thread.currentThread().getName()
                + "   signing On");
        try {

            BufferedReader nis = new BufferedReader(
                    new InputStreamReader(
                            soc.getInputStream()
                    )
            );
            PrintWriter nos = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    soc.getOutputStream()
                            )
                    ), true
            );
            al.add(nos);
            Username = nis.readLine();
            String message = nis.readLine();
            while (!message.equalsIgnoreCase("End")) {

                System.out.println("Server Recieved  " + message);

                for (PrintWriter o : al) {
                    o.println(Username + ": " + message);
                }
                
                String TimeStamp = new SimpleDateFormat(
                                        "MM/dd/yyyy HH:mm:ss"
                                   ).format(new Date()) 
                                   + " " + Username 
                                   + ": " + message;
                pwLog.println(TimeStamp);
                System.out.println("Server is Logging " + TimeStamp);
                message = nis.readLine();
                
            }
            nos.println("End");
            al.remove(nos);
        } catch (Exception e) {
            System.out.println(
                    "Client Seems to have abruptly closed the connection"
            );
        }
        System.out.println("Conversation thread  "
                + Thread.currentThread().getName()
                + "   signing Off");
    }
}