package com.dynamsoft.dlr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import com.google.gson.*;

public class NativeLabelRecognizer {

	private long nativePtr = 0;

	static {
		try {
			if (NativeLoader.load()) {
				System.out.println("Successfully loaded Dynamsoft Document Normalizer.");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public NativeLabelRecognizer() {
		nativePtr = nativeCreateInstance();
	}

	public void destroyInstance() {
		if (nativePtr != 0)
			nativeDestroyInstance(nativePtr);
	}

	public static int setLicense(String license) {
		return nativeInitLicense(license);
	}

	public ArrayList<MrzResult> detectFile(String fileName) {
		return nativeDetectFile(nativePtr, fileName);
	}

	public String getVersion() {
		return nativeGetVersion();
	}

	public int loadModel() throws IOException {
		String modeFile = "MRZ.json";
		String tempFolder = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
		String targetPath = new File(tempFolder, modeFile).getAbsolutePath();
		// Modify the model path based on your own environment
		FileReader reader = new FileReader(targetPath);
		char[] chars = new char[1024];
		int len = 0;
		StringBuilder sb = new StringBuilder();
		while ((len = reader.read(chars)) != -1) {
			sb.append(new String(chars, 0, len));
		}
		String template = sb.toString();
		if (reader != null) {
			reader.close();
		}

		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(template, JsonObject.class);
		JsonArray array = jsonObject.get("CharacterModelArray").getAsJsonArray();
		JsonObject object = array.get(0).getAsJsonObject();
		String modelPath = object.get("DirectoryPath").getAsString();

		if (modelPath != null && modelPath.contains("model")) {
			object.addProperty("DirectoryPath", tempFolder);
		}

		FileWriter writer = new FileWriter(targetPath);
		writer.write(jsonObject.toString());
		writer.flush();
		writer.close();

		return nativeLoadModel(nativePtr, targetPath);
	}

	private native static int nativeInitLicense(String license);

	private native long nativeCreateInstance();

	private native void nativeDestroyInstance(long nativePtr);

	private native ArrayList<MrzResult> nativeDetectFile(long nativePtr, String fileName);

	private native String nativeGetVersion();

	private native int nativeLoadModel(long nativePtr, String modelPath);
}
