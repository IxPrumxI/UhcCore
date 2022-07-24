package com.gmail.val59000mc.utils;

import com.gmail.val59000mc.UhcCore;
import com.gmail.val59000mc.configuration.YamlFile;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.logging.Logger;

public class FileUtils{

	private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getCanonicalName());

	private static final String API_URL = "https://paste.md-5.net/documents";
	private static final String PASTE_URL_DOMAIN = "https://paste.md-5.net/";

	public static YamlFile saveResourceIfNotAvailable(JavaPlugin plugin, String fileName)
			throws IOException, InvalidConfigurationException {
		return saveResourceIfNotAvailable(plugin, fileName, fileName);
	}

	public static YamlFile saveResourceIfNotAvailable(JavaPlugin plugin, String fileName, String sourceName)
			throws IOException, InvalidConfigurationException {
		File file = getResourceFile(plugin, fileName, sourceName);

		YamlFile yamlFile = new YamlFile(file);
		yamlFile.load();

		return yamlFile;
	}

	public static File getResourceFile(JavaPlugin plugin, String fileName) {
		return getResourceFile(plugin, fileName, fileName);
	}

	public static File getResourceFile(JavaPlugin plugin, String fileName, String sourceName) {
		File file = new File(plugin.getDataFolder(), fileName);

		if (!file.exists()){
			// save resource
			plugin.saveResource(sourceName, false);
		}

		if (!fileName.equals(sourceName)){
			File sourceFile = new File(plugin.getDataFolder(), sourceName);
			sourceFile.renameTo(file);
		}

		if (!file.exists()){
			LOGGER.warning("Failed to save file: " + fileName);
		}

		return file;
	}

	/**
	 * Method used to upload text files to paste bin.
	 * @param builder StringBuilder containing the text you want to be uploaded.
	 * @return Returns the URL of the uploaded text.
	 * @throws IOException Thrown when uploading fails.
	 */
	public static String uploadTextFile(StringBuilder builder) throws IOException{
		String data = builder.toString();

		HttpsURLConnection connection = (HttpsURLConnection) new URL(API_URL).openConnection();

		// Add headers
		connection.setRequestMethod("POST");
		connection.addRequestProperty("Accept", "application/json");
		connection.addRequestProperty("Content-Length", String.valueOf(data.length()));
		connection.setRequestProperty("Content-Type", "text/plain");
		connection.setRequestProperty("User-Agent", "UhcCore:"+ UhcCore.getPlugin().getDescription().getVersion());

		// Send data
		connection.setDoOutput(true);
		DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		outputStream.write(data.getBytes());
		outputStream.flush();
		outputStream.close();

		InputStream inputStream = connection.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

		JsonObject json = new JsonParser().parse(bufferedReader.readLine()).getAsJsonObject();

		bufferedReader.close();
		connection.disconnect();

		return PASTE_URL_DOMAIN + json.get("key").getAsString();
	}

	/**
	 * Deletes file, in case of a directory all child files and directories are deleted
	 * @param file File to delete
	 * @return Returns true if file was deleted successfully
	 */
	public static boolean deleteFile(File file) {
		if(file == null){
			return false;
		}

		if (file.isFile()) {
			return file.delete();
		}

		if (!file.isDirectory()) {
			return false;
		}

		File[] flist = file.listFiles();

		if (flist != null && flist.length > 0) {
			for (File f : flist) {
				if (!deleteFile(f)) {
					return false;
				}
			}
		}

		return file.delete();
	}

}
