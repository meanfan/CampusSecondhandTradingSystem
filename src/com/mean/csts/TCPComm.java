package com.mean.csts;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import com.mean.csts.data.Data;

import javax.swing.JOptionPane;

public class TCPComm {
	private InetAddress address;
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Data data,resp;
	public boolean connect(InetAddress address,Data data)
	{
		try {
			socket = new Socket(address,2333);
			System.out.println("connected to "+address);
		}catch(IOException e1) {
			e1.printStackTrace();
			return false;
		}
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		}catch(IOException e2) {
			e2.printStackTrace();
			return false;
		}
		return true;
	}
	public Data getResponse()
	{
		return resp;
	}
	public void close()
	{
		try {
			socket.close();
			out.close();
			in.close();
		} catch (IOException e) {e.printStackTrace();}
	}
	public TCPComm(InetAddress add, Data data)
	{
		this.address = add;
		this.data = data;
		if(connect(address,data)==false)
			JOptionPane.showMessageDialog(null, "服务器连接失败！");
		else
		{
			try {
				out.writeObject(data);
				System.out.println("request sent:"+data);
			}catch(IOException e1) {e1.getStackTrace();}
			try {
				resp = (Data)in.readObject();
			}catch (IOException e2) {e2.printStackTrace();}
			catch (ClassNotFoundException e3) {e3.printStackTrace();}
		}
	}

}