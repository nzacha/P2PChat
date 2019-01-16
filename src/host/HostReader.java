package host;

import java.io.DataInputStream;
import java.io.IOException;

import gui.Chat;
import network.Member;
import resources.Broadcast;
import resources.Message;
import resources.Reader;

public class HostReader extends Reader {
	private Host host;
	private Member member;
	private Chat chat;
	private DataInputStream in;

	public HostReader(Host host, Member member, Chat chat, DataInputStream in) {
		this.host = host;
		this.member = member;
		this.chat = chat;
		this.in = in;
	}

	@Override
	public void run() {
		while (true)
			try {
				String text = in.readUTF();
				if (text.equals(Message.exit.toString())) {
					host.removeMember(member.getName());
					cancel();
					break;
				}
				if (text.equals(Message.changeName.toString())) {
					String newName = in.readUTF();
					member.setName(newName);					
				}
				host.broadcastMessage(new Broadcast(member.getName() + ":" + text));
				chat.appendText(text);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void cancel() {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
