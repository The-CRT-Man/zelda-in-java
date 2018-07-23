package com.link.load;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextFileLoader {
	private BufferedReader file;
	
	public BufferedReader loadFile(String path) throws IOException{
		file = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));
		return file;
	}
}
