package com.mean.csts.client;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class AccountPanel extends JPanel {
    public static int winWedth =400;
    public static int winHeight =350;
    public InetAddress address;
    public int port;
    public AccountPanel(String address,int port) {
        super();
        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {e.printStackTrace();}
        this.port = port;
    }
}