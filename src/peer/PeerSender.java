package peer;

import java.io.DataOutputStream;
import java.io.IOException;

import gui.Chat;
import resources.Message;
import resources.Sender;

public class PeerSender extends Sender {
	private DataOutputStream out;

	public PeerSender(Peer peer, Chat chat, DataOutputStream out) {
		super(peer, chat);
		this.out = out;
	}

	@Override
	public void run() {
		while (true)
			try {
				String text = chat.getBroadcast();
				if (!text.startsWith("/")) {
					out.writeUTF(text);
					continue;
				}
				text = text.replaceFirst("/", "");
				if (text.equals(Message.exit.toString())) {
					out.writeUTF(Message.exit.toString());
					cancel();
					break;
				}
				if (text.equals(Message.changeName.toString())) {
					out.writeUTF(Message.changeName.toString());
					System.out.println("changing name");
					continue;
				}
				System.out.println("unkown command");
				chat.appendText("unkown command");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void cancel() {
		module.stop();
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
