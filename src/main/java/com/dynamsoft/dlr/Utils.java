package com.dynamsoft.dlr;
import java.awt.image.BufferedImage;
import java.awt.*;
import javax.swing.*;

public class Utils {
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isLinux() {

		return (OS.indexOf("nux") >= 0);
		
	}

	public static void display(BufferedImage image, String title) {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setTitle(title);
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.pack();
		frame.setVisible(true);
	 }
}
