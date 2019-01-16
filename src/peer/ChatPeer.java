package peer;

import java.net.Socket;
import java.util.ArrayList;

import gui.Chat;
import host.Host;
import network.NetworkScanner;
import resources.Module;

public class ChatPeer {
	private static final int port = 2612;
	@SuppressWarnings("unused")
	private Module module;
	private Chat chat;

	public ChatPeer() {
		chat = new Chat();
		chat.appendText("Looking for other servers...");

		ArrayList<Socket> servers = new NetworkScanner().getDevices();
		chat.appendText("Found " + servers.size() + " servers");

		if (servers.size() == 0) {
			chat.appendText("Becoming host...");
			chat.appendText("--------------------------");
			module = new Host(chat, port);
		} else if (servers.size() == 1) {
			chat.appendText("Joining server...");
			chat.appendText("--------------------------");
			module = new Peer(chat, servers.get(0), port);
		}
	}

	public static void main(String[] args) {
		new ChatPeer();
	}
}
