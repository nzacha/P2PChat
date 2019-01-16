package host;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import gui.Chat;
import network.Member;
import resources.Broadcast;
import resources.Module;

public class Host extends Module {
	private LinkedList<Member> members = new LinkedList<Member>();
	@SuppressWarnings("unused")
	private ServerSocket serverSocket;
	@SuppressWarnings("unused")
	private Acceptor acceptor;
	private Chat chat;
	private Object members_mutex = new Object();
	@SuppressWarnings("unused")
	private HostSender sender;

	public Host(Chat chat, int port) {
		this.chat = chat;
		try {
			new Thread(acceptor = new Acceptor(this, serverSocket = new ServerSocket(port))).start();
			sender = new HostSender(this, chat);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		System.out.println("not doing anything");
	}

	public void broadcastMessage(Broadcast message) {
		synchronized (members_mutex) {
			for (Member m : members) {
				if (m.getName().equals(message.getName()))
					continue;
				DataOutputStream out = m.getDataOutputStream();
				try {
					out.writeUTF(m.getName() + ": " + message.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void addMember(Socket s) {
		synchronized (members_mutex) {
			members.add(new Member(this, s, chat));
		}
	}

	public synchronized void removeMember(String name) {
		synchronized (members_mutex) {
			Member m = null;
			for (Member mem : members)
				if (mem.getName().equals(name)) {
					m = mem;
					break;
				}
			if (m == null)
				throw new NullPointerException();
			m.stop();
			members.remove(m);
		}
	}

	public LinkedList<Member> getMembers() {
		return members;
	}

	public Object getMembersMutex() {
		return members_mutex;
	}
}