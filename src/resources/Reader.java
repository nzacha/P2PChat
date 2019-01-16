package resources;

public abstract class Reader extends Thread {
	public Reader() {
		start();
	}

	public abstract void run();

	public abstract void cancel();
}
