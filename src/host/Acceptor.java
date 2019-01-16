package host;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Acceptor implements Runnable {
	private Host host;
	private ServerSocket serverSocket;
	private Socket s;
	private boolean running = true;

	public Acceptor(Host server, ServerSocket serverSocket) {
		this.host = server;
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		while (running) {
			try {
				Socket s = serverSocket.accept();
				host.addMember(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		running = false;
		if (s != null)
			try {
				s.getInputStream().close();
				s.getOutputStream().close();
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
