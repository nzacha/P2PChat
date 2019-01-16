package resources;

import gui.Chat;

public abstract class Sender extends Thread {
	protected Module module;
	protected Chat chat;

	public Sender(Module module, Chat chat) {
		this.module = module;
		this.chat = chat;
		start();
	}

	public abstract void run();

	public abstract void cancel();
}
