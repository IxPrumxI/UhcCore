package com.gmail.val59000mc.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MojangUtils{

	private static final Logger LOGGER = Logger.getLogger(MojangUtils.class.getCanonicalName());

	public static UUID getPlayerUuid(String name){
		if (Bukkit.isPrimaryThread()){
			throw new RuntimeException("Requesting player UUID is not allowed on the primary thread!");
		}

		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			JsonParser parser = new JsonParser();
			JsonElement json = parser.parse(new InputStreamReader(connection.getInputStream()));

			connection.disconnect();

			if (!json.isJsonObject()){
				return null;
			}

			String stringUuid = json.getAsJsonObject().get("id").getAsString();
			return insertDashUUID(stringUuid);
		}catch (IOException ex){
			LOGGER.log(Level.WARNING, "Unable to get player UUID", ex);
			return null;
		}
	}

	public static String getPlayerName(String name){
		if (Bukkit.isPrimaryThread()){
			throw new RuntimeException("Requesting player UUID is not allowed on the primary thread!");
		}

		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			JsonParser parser = new JsonParser();
			JsonElement json = parser.parse(new InputStreamReader(connection.getInputStream()));

			connection.disconnect();

			if (!json.isJsonObject()){
				return name;
			}

			return json.getAsJsonObject().get("name").getAsString();
		}catch (IOException ex){
			LOGGER.log(Level.WARNING, "Unable to get player name", ex);
			return name;
		}
	}

	public static String getPlayerName(UUID uuid){
		if (Bukkit.isPrimaryThread()){
			throw new RuntimeException("Requesting player UUID is not allowed on the primary thread!");
		}

		try {
			URL url = new URL("https://api.mojang.com/user/profiles/"+uuid.toString().replace("-", "")+"/names");

			URLConnection request = url.openConnection();
			request.connect();

			JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

			JsonArray names = root.getAsJsonArray();

			return names.get(names.size()-1).getAsJsonObject().get("name").getAsString();
		}catch (IOException ex){
			LOGGER.log(Level.WARNING, "Unable to get player name", ex);
			return null;
		}
	}

	private static UUID insertDashUUID(String uuid) {
		StringBuffer sb = new StringBuffer(uuid);
		sb.insert(8, "-");

		sb = new StringBuffer(sb.toString());
		sb.insert(13, "-");

		sb = new StringBuffer(sb.toString());
		sb.insert(18, "-");

		sb = new StringBuffer(sb.toString());
		sb.insert(23, "-");

		return UUID.fromString(sb.toString());
	}

}
