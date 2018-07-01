package com.mean.csts.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BasicWin extends JFrame {
    public int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    public int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    public String title = "BasicWin";
    public int winWedth = 400;
    public int winHeight = 350;
    public BasicWin() {
        this.setTitle(title);
        this.setVisible(true);
        this.setBounds((screenWidth - winWedth) / 2,
                (screenHeight - winHeight) / 2, winWedth, winHeight);
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
        this.setBounds((screenWidth - winWedth) / 2,
                (screenHeight - winHeight) / 2, winWedth, winHeight);
    }
    public void close()
    {

        //System.exit(0);
    }
    public static void main(String[] args) {
        new BasicWin();
    }
}
