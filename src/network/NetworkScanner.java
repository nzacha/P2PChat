package network;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkScanner {
	private ArrayList<Socket> devices = new ArrayList<Socket>();
	private Barrier mutex = new Barrier();
	private final boolean DEBUG = false;

	public NetworkScanner() {
		System.setProperty("java.net.preferIPv4Stack", "true");
		scanNetwork();
	}

	public void scanNetwork() {
		final byte[] ip;
		try {
			ip = InetAddress.getLocalHost().getAddress();
			if (DEBUG)
				System.out.println("Beginning network scan");
			for (int i = 1; i <= 254; i++) {
				final int j = i;
				ip[3] = (byte) j;
				InetAddress address = InetAddress.getByAddress(ip);
				scanFor(address);
			}
			synchronized (mutex) {
				mutex.wait();
			}
			if (DEBUG)
				System.out.println("Network Scan complete!!");
		} catch (Exception e) {
			System.out.println("something went wrong");
			return;
		}
	}

	private void scanFor(InetAddress address) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Socket s = new Socket();
				try {
					s.connect(new InetSocketAddress(address, 2612), 100);
					System.out.println(address + " is opened at port " + 2612);
					synchronized (mutex) {
						devices.add(s);
					}
				} catch (Exception e) {
					if (DEBUG) {
						System.out.println(address + " is closed at port " + 2612);
						e.printStackTrace();
					}
				} finally {
					mutex.increment();
					if (mutex.isDone())
						synchronized (mutex) {
							mutex.notifyAll();
						}
				}
			}
		}).start();
	}

	public ArrayList<Socket> getDevices() {
		if (!mutex.isDone()) {
			if (DEBUG)
				System.out.println("waiting to get Devices");
			try {
				synchronized (mutex) {
					mutex.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (DEBUG)
			System.out.println("done waiting!");
		return devices;
	}

	private class Barrier {
		int count = 0;

		public synchronized void increment() {
			count++;
		}

		public synchronized boolean isDone() {
			return count == 254;
		}
	};

	public static void main(String[] args) {
		new NetworkScanner();
	}
}
