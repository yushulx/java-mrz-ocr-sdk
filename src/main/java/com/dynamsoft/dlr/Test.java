package com.dynamsoft.dlr;
import java.util.ArrayList;
import java.util.Set;

import javax.imageio.ImageIO;

import com.google.gson.JsonObject;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length < 1) {
			System.out.println("Please input an image file.");
			return;
		}
		String fileName = args[0];
		String license = "DLS2eyJoYW5kc2hha2VDb2RlIjoiMjAwMDAxLTE2NDk4Mjk3OTI2MzUiLCJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSIsInNlc3Npb25QYXNzd29yZCI6IndTcGR6Vm05WDJrcEQ5YUoifQ==";

		if (args.length == 2) {
			license = args[1];
		}

		try {
			int ret = NativeLabelRecognizer.setLicense(license);
			NativeLabelRecognizer labelRecognizer = new NativeLabelRecognizer();
			System.out.println("Version number: " + labelRecognizer.getVersion());
			ret = labelRecognizer.loadModel();
			ArrayList<MrzResult> results = (ArrayList<MrzResult>)labelRecognizer.detectFile(fileName);
			
			if (results != null) {
				String[] lines = new String[results.size()];
				for (int i = 0; i < results.size(); i++) {
					lines[i] = results.get(i).text;
				}
				JsonObject info = MrzParser.parse(lines);
				
				BufferedImage image = null;
				try
				{
					image = ImageIO.read(new File(fileName));
					Graphics2D graphics2D = image.createGraphics();

					for (MrzResult result : results) {
						System.out.println("Confidence: " + result.confidence);
						System.out.println("Text: " + result.text);
						System.out.println("x1: " + result.x1 + ", y1: " + result.y1);
						System.out.println("x2: " + result.x2 + ", y2: " + result.y2);
						System.out.println("x3: " + result.x3 + ", y3: " + result.y3);
						System.out.println("x4: " + result.x4 + ", y4: " + result.y4);

						graphics2D.setColor(Color.RED);
						graphics2D.setStroke(new BasicStroke(3));
						graphics2D.drawLine(result.x1, result.y1, result.x2, result.y2);
						graphics2D.drawLine(result.x2, result.y2, result.x3, result.y3);
						graphics2D.drawLine(result.x3, result.y3, result.x4, result.y4);
						graphics2D.drawLine(result.x4, result.y4, result.x1, result.y1);
					}

					Set<String> keys = info.keySet();
					int y = 10;
					for (String key : keys) {
						String kv = key + ": " + info.get(key).getAsString();
						System.out.println(kv);
						graphics2D.drawString(kv, 10, y);
						y += 20;
					}
					
					graphics2D.dispose();

					boolean success = ImageIO.write(image, "png", new java.io.File("passport-mrz-detection.png"));
					if (success) {
						System.out.println("Successfully saved the image to passport-mrz-detection.png");
					}
					else {
						System.out.println("Failed to save the image.");
					}

					Utils.display(image, "Passport MRZ Detection");

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			labelRecognizer.destroyInstance();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
