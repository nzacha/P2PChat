package resources;

public enum Message {
	exit("exit"), joinRequest("joinReq"), changeName("change name");

	private final String value;

	Message(String value) {
		this.value = value;
	}

	public final String toString() {
		return value;
	}
}
