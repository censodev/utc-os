package org.censo.dev.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class FileUtil {
	public static List<Process> getListProcessFromJSONFile(String path) {
		File file = new File(path);
		
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			StringBuilder jsonString = new StringBuilder();
			String line;
			
			while ((line = br.readLine()) != null) {
				jsonString.append(line.trim());
			}
			
			br.close();
			
			Gson gson = new Gson();
			Type typeToken = new TypeToken<List<Process>>(){}.getType();
			return gson.fromJson(jsonString.toString(), typeToken);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}

	public static Memory getMemoryInfoFromJSONFIle(String path) {
		File file = new File(path);

		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			StringBuilder jsonString = new StringBuilder();
			String line;

			while ((line = br.readLine()) != null) {
				jsonString.append(line.trim());
			}

			br.close();

			Gson gson = new Gson();
			return gson.fromJson(jsonString.toString(), Memory.class);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}
}
