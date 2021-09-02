package chat;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleChatClient {
    JTextField outgoing;
    PrintWriter writer;
    Socket sock;
    BufferedReader reader;

    public void go() {
        JFrame frame = new JFrame("Ludicrously Simple Chat Client");
        JPanel mainPanel = new JPanel();
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        setUpNetworking();
        try
        {
            reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                String message;
                try
                {
                    while(true)
                    {
                        if((message=reader.readLine()) != null)
                        {
                            System.out.println("server sent : " +message);
                        }
                    }
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }


            }
        });

        t.start();
        frame.setSize(400,500);
        frame.setVisible(true);
    } // close go

    private void setUpNetworking() {
        try {
            sock = new Socket("127.0.0.1", 5000);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("networking established");
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    } // close setUpNetworking

    public class SendButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ev) {
            try {
                writer.println(outgoing.getText());
                writer.flush();
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    } // close SendButtonListener inner class

    public static void main(String[] args) {
         new SimpleChatClient().go();
    }
}