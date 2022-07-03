package com.gmail.val59000mc.configuration.options;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnumListOption<T extends Enum<T>> implements Option<List<T>> {

	private static final Logger LOGGER = Logger.getLogger(EnumListOption.class.getCanonicalName());

	private final String path;
	private final Class<T> type;

	public EnumListOption(String path, Class<T> type) {
		this.path = path;
		this.type = type;
	}

	@Override
	public List<T> getValue(YamlConfiguration config) {
		List<String> stringList = config.getStringList(path);
		List<T> enumList = new ArrayList<>();

		for (String s : stringList){
			try {
				enumList.add(Enum.valueOf(type, s));
			}catch (IllegalArgumentException ex){
				LOGGER.log(Level.WARNING, "Invalid enum constant name", ex);
			}
		}

		return enumList;
	}

}
