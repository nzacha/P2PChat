package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import resources.Message;

public class Chat extends JFrame {
	private static final long serialVersionUID = 3588647902897110284L;
	private JTextArea textArea;
	private JTextField textField;
	private JScrollPane scroll;
	private String broadcastText;
	private boolean requestNewName = false;
	private Object newName = new Object();
	private Object ready = new Object();
	private String name;

	public Chat() {
		frameSetup();
	}

	private void frameSetup() {
		Dimension screen = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
		setTitle("CHATROOM");
		// setResizable(false);

		textArea = new JTextArea("");
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setMargin(new Insets(5, 5, 5, 5));
		scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setSize(100, 100);

		textField = new JTextField("Write here...");
		textField.setMaximumSize(new Dimension(0, 300));
		textField.setMargin(new Insets(5, 5, 5, 5));
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent k) {
				int key = k.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					if (requestNewName) {
						name = textField.getText();
						requestNewName = false;
						textArea.append("Great! your name is now: " + name + "\n");
						synchronized (newName) {
							newName.notify();
						}
					} else {
						String text = textField.getText();
						textArea.append("You: " + text + System.getProperty("line.separator"));
						broadcastText = text;
						synchronized (ready) {
							ready.notify();
						}
					}
					scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
					textField.setText("");
				}
			}
		});

		Container contentPane = getContentPane();
		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.WEST, scroll, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, scroll, -5, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.NORTH, scroll, 5, SpringLayout.NORTH, contentPane);

		layout.putConstraint(SpringLayout.WEST, textField, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, textField, 0, SpringLayout.EAST, scroll);
		layout.putConstraint(SpringLayout.NORTH, textField, 5, SpringLayout.SOUTH, scroll);

		layout.putConstraint(SpringLayout.SOUTH, contentPane, 5, SpringLayout.SOUTH, textField);

		contentPane.setLayout(layout);
		contentPane.add(textField, BorderLayout.PAGE_END);
		contentPane.add(scroll);

		pack();

		setMinimumSize(new Dimension(536, 294));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent wc) {
				broadcastText = Message.exit.toString();
				synchronized (ready) {
					ready.notify();
				}
			}
		});
		setLocation((int) ((screen.getWidth() - getWidth()) / 2), (int) ((screen.getHeight() - getHeight()) / 2));
		contentPane.setBackground(Color.LIGHT_GRAY);

		setVisible(true);
	}

	public String getBroadcast() {
		try {
			synchronized (ready) {
				ready.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return broadcastText;
	}

	public String requestNameAndWait() {
		appendText("Please type your new Name");
		synchronized (newName) {
			requestNewName = true;
			try {
				newName.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return name;
	}

	public void appendText(String text) {
		textArea.append(text + "\n");
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
	}
}
