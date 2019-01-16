package peer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import gui.Chat;
import resources.Module;

public class Peer extends Module {
	private PeerReader reader;

	public Peer(Chat chat, Socket s, int port) {
		try {
			out = new DataOutputStream(s.getOutputStream());
			in = new DataInputStream(s.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		new PeerSender(this, chat, out);
		reader = new PeerReader(chat, in);
	}

	public void stop() {
		reader.cancel();
	}
}
