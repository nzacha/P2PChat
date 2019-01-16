package resources;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public abstract class Module {
	protected DataOutputStream out;
	protected DataInputStream in;

	public abstract void stop();
}
