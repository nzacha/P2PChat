package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import gui.Chat;
import host.Host;
import host.HostReader;

public class Member {
	private static int ID = 0;

	@SuppressWarnings("unused")
	private String address, name = "client";
	private Socket socket;
	private DataOutputStream out = null;
	private DataInputStream in = null;
	private HostReader reader;
	private int id;

	public Member(Host host, Socket s, Chat chat) {
		this.id = ID++;
		this.socket = s;
		try {
			address = s.getLocalAddress().getHostAddress();
			out = new DataOutputStream(s.getOutputStream());
			in = new DataInputStream(s.getInputStream());
			reader = new HostReader(host, this, chat, in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			reader.cancel();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DataOutputStream getDataOutputStream() {
		return out;
	}

	public DataInputStream getDataInputStream() {
		return in;
	}

	public boolean equals(Member obj) {
		return id == obj.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		this.name = newName;
	}
}
