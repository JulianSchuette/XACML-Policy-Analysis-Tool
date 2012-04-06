package org.linkality.xacmlanalysr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Utils {

	public static String readFileAsString(File file) throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

	public static void writeStringToFile(String string, File file)
			throws java.io.IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(string);
		writer.close();
	}

}
