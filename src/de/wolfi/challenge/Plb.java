package de.wolfi.challenge;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Plb implements Runnable{
	ServerSocket s;
	boolean stoped = false;
	public void run() {
		try {
			s = new ServerSocket(33000);
			while(!stoped){
				Socket ss= s.accept();

				ObjectOutputStream w = new ObjectOutputStream(ss.getOutputStream());
				w.writeInt(Main.number);
				
				w.writeUTF(Main.VERSION);
				w.flush();
				w.close();
				ss.close();
			}
			
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	protected void stop(){
		stoped = true;
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}