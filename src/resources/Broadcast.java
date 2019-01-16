package resources;

public class Broadcast {
	private String name, msg;

	public Broadcast(String text) {
		int index = text.indexOf(':');
		name = text.substring(0, index++);
		msg = text.substring(index);
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return msg;
	}
}
