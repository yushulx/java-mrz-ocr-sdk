package com.dynamsoft.dlr;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NativeLoader {
	
	
	private static synchronized boolean loadNativeLibrary(String path, String name) {
		File libPath = new File(path, name);
		if (!name.contains("MRZ")) {
			try {
				System.load(libPath.getAbsolutePath());
				return true;
			} catch (UnsatisfiedLinkError e) {
				System.err.println(e);
				return false;
			}

		} else
			return false;
	}

	private static boolean extractResourceFiles(String dlrNativeLibraryPath, String dlrNativeLibraryName,
			String tempFolder) throws IOException {
		String[] filenames = null;
		if (Utils.isWindows()) {
			filenames = new String[] {"DynamsoftLicenseClientx64.dll",
			"vcomp140.dll",
			"DynamicPdfx64.dll", "DynamsoftLabelRecognizerx64.dll", "dlr.dll"};
		}
		else if (Utils.isLinux()) {
			filenames = new String[] {"libDynamicPdf.so", "libDynamsoftLicenseClient.so", "libDynamsoftLabelRecognizer.so", "libdlr.so"};
		}
		
		boolean ret = true;
		
		for (String file : filenames) {
			ret &= extractAndLoadLibraryFile(dlrNativeLibraryPath, file, tempFolder);
		}
		
		// Extract model files
		String modelPath = "/model";
		filenames = new String[] {"MRZ.json", "MRZ.caffemodel", "MRZ.txt", "MRZ.prototxt"};
		for (String file : filenames) {
			ret &= extractAndLoadLibraryFile(modelPath, file, tempFolder);
		}
		return ret;
	}

	public static boolean load() throws Exception {

		// Load the os-dependent library from the jar file
		String dlrNativeLibraryName = System.mapLibraryName("dlr");
		if (dlrNativeLibraryName != null && dlrNativeLibraryName.endsWith("dylib")) {
			dlrNativeLibraryName = dlrNativeLibraryName.replace("dylib", "jnilib");
		}

		String dlrNativeLibraryPath = "/com/dynamsoft/dlr/native";
		if (Utils.isWindows()) {
			dlrNativeLibraryPath = "/com/dynamsoft/dlr/native/win";
		}
		else if (Utils.isLinux()) {
			dlrNativeLibraryPath = "/com/dynamsoft/dlr/native/linux";
		}

		if (NativeLabelRecognizer.class.getResource(dlrNativeLibraryPath + "/" + dlrNativeLibraryName) == null) {
			throw new Exception("Error loading native library: " + dlrNativeLibraryPath + "/" + dlrNativeLibraryName);
		}

		// Temporary library folder
		String tempFolder = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();

		// Extract resource files
		return extractResourceFiles(dlrNativeLibraryPath, dlrNativeLibraryName, tempFolder);
	}

	static String md5sum(InputStream input) throws IOException {
		BufferedInputStream in = new BufferedInputStream(input);

		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			DigestInputStream digestInputStream = new DigestInputStream(in, digest);
			for (; digestInputStream.read() >= 0;) {

			}
			ByteArrayOutputStream md5out = new ByteArrayOutputStream();
			md5out.write(digest.digest());
			return md5out.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("MD5 algorithm is not available: " + e);
		} finally {
			in.close();
		}
	}

	private static boolean extractAndLoadLibraryFile(String libFolderForCurrentOS, String libraryFileName,
			String targetFolder) {
		String nativeLibraryFilePath = libFolderForCurrentOS + "/" + libraryFileName;

		String extractedLibFileName = libraryFileName;
		File extractedLibFile = new File(targetFolder, extractedLibFileName);

		try {
			if (extractedLibFile.exists()) {
				// test md5sum value
				String md5sum1 = md5sum(NativeLabelRecognizer.class.getResourceAsStream(nativeLibraryFilePath));
				String md5sum2 = md5sum(new FileInputStream(extractedLibFile));

				if (md5sum1.equals(md5sum2)) {
					return loadNativeLibrary(targetFolder, extractedLibFileName);
				} else {
					// remove old native library file
					boolean deletionSucceeded = extractedLibFile.delete();
					if (!deletionSucceeded) {
						throw new IOException(
								"failed to remove existing native library file: " + extractedLibFile.getAbsolutePath());
					}
				}
			}

			// Extract file into the current directory
			InputStream reader = NativeLabelRecognizer.class.getResourceAsStream(nativeLibraryFilePath);
			FileOutputStream writer = new FileOutputStream(extractedLibFile);
			byte[] buffer = new byte[1024];
			int bytesRead = 0;
			while ((bytesRead = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, bytesRead);
			}

			writer.close();
			reader.close();

			if (!System.getProperty("os.name").contains("Windows")) {
				try {
					Runtime.getRuntime().exec(new String[] { "chmod", "755", extractedLibFile.getAbsolutePath() })
							.waitFor();
				} catch (Throwable e) {
				}
			}

			return loadNativeLibrary(targetFolder, extractedLibFileName);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return false;
		}

	}
}
