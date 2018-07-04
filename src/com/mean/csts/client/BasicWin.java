package com.mean.csts.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;

public class BasicWin extends JFrame {
    public String ADDRESS = "localhost";
    public int PORT = 2333;
    public int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public String title = "BasicWin";
    public int winWedth = 400;
    public int winHeight = 350;
    public BasicWin() {
        this.setTitle(title);
        this.setVisible(true);
        this.setBounds((SCREEN_WIDTH - winWedth) / 2,
                (SCREEN_HEIGHT - winHeight) / 2, winWedth, winHeight);
        this.validate();
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    public BasicWin(String title,int winWedth,int winHeight) {
        this();
        this.title = title;
        this.winWedth = winWedth;
        this.winHeight = winHeight;
        this.setTitle(title);
        this.setBounds((SCREEN_WIDTH - winWedth) / 2,
                (SCREEN_HEIGHT - winHeight) / 2, winWedth, winHeight);
    }
    public void close()
    {

        //System.exit(0);
    }
    public static void main(String[] args) {
        new BasicWin();
    }
}
