package com.rajeshpatkar.Client;

import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ConcurrentClient {

    public static void main(String[] args) throws Exception{
        
        Socket soc = new Socket("127.0.01",9081);
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
                              ),true
        );
        String Username = JOptionPane.showInputDialog(
                "Please Enter you Username"
        );
        nos.println(Username);
        JFrame f1 = new JFrame(Username + "`s Chat");
        JButton b1 = new JButton("Send");
        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        JTextField tf = new JTextField(20);
        JPanel p1 = new JPanel();
        p1.add(tf);
        p1.add(b1);
        f1.add(ta);
        f1.add(BorderLayout.SOUTH,p1);
        ChatListener l1 = new ChatListener(tf,nos,Username);
        b1.addActionListener(l1);
        tf.addActionListener(l1);
        f1.setSize(400,400);
        f1.setVisible(true);
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String str = nis.readLine();
        while(!str.equalsIgnoreCase("End")){
            ta.append(str + "\n" );
            str = nis.readLine();
        }
        ta.append("Client Signing Off");
        Thread.sleep(1000);
        System.exit(0);
    }
}

class ChatListener implements ActionListener{
   JTextField tf ;
   PrintWriter nos;
   String Username;
    public ChatListener(JTextField tf,PrintWriter nos,String Username){
        this.tf = tf;
        this.nos = nos;
        this.Username = Username;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String str  = tf.getText();
        nos.println(str);
        tf.setText("");
    }
    
}