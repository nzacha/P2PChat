package peer;

import java.io.DataInputStream;
import java.io.IOException;

import gui.Chat;
import resources.Reader;

public class PeerReader extends Reader {
	private DataInputStream in;
	private Chat chat;
	private boolean running = true;

	public PeerReader(Chat chat, DataInputStream in) {
		this.chat = chat;
		this.in = in;
	}

	@Override
	public void run() {
		while (running)
			try {
				chat.appendText(in.readUTF());
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void cancel() {
		running = false;
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
