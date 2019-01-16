package host;

import java.io.DataOutputStream;
import java.io.IOException;

import gui.Chat;
import network.Member;
import resources.Message;
import resources.Sender;

public class HostSender extends Sender {
	private Host host;

	public HostSender(Host host, Chat chat) {
		super(host, chat);
		this.host = host;
	}

	@Override
	public void run() {
		while (true) {
			String text = chat.getBroadcast();
			if (text.equals(Message.exit.toString())) {
				cancel();
				System.out.println("should auto host migrate, HostSender.run/exit");
				break;
			}
			broadcastMessage(text);
		}
	}

	public void broadcastMessage(String message) {
		synchronized (host.getMembersMutex()) {
			for (Member m : host.getMembers()) {
				DataOutputStream out = m.getDataOutputStream();
				try {
					out.writeUTF(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void cancel() {
		module.stop();
	}
}
