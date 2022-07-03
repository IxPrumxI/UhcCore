package com.gmail.val59000mc.configuration.options;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.YamlConfiguration;

public class EnumOption<T extends Enum<T>> implements Option<T> {

	private static final Logger LOGGER = Logger.getLogger(EnumOption.class.getCanonicalName());

	private final String path;
	private final Class<T> type;
	private final T def;
	private final String defString;

	public EnumOption(String path, T def) {
		this.path = path;
		type = (Class<T>) def.getClass();
		this.def = def;
		defString = def.name();
	}

	public EnumOption(String path, Class<T> type, String defString) {
		this.path = path;
		this.type = type;
		this.def = null;
		this.defString = defString;
	}

	@Override
	public T getValue(YamlConfiguration config) {
		String enumType = config.getString(path, defString);

		try {
			Validate.notNull(enumType, "No enum type specified!");
			return Enum.valueOf(type, enumType);
		}catch (IllegalArgumentException ex){
			LOGGER.log(Level.WARNING, "Invalid enum constant name", ex);
			return def;
		}
	}

}
