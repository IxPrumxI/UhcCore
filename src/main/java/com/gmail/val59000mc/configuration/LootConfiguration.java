package com.gmail.val59000mc.configuration;

import com.gmail.val59000mc.exceptions.ParseException;
import com.gmail.val59000mc.utils.JsonItemUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LootConfiguration<T extends Enum<T>> {

	private static final Logger LOGGER = Logger.getLogger(LootConfiguration.class.getCanonicalName());

	private final Class<T> classType;

	private T type;
	private final List<ItemStack> loot;
	private int addXp;

	public LootConfiguration(Class<T> classType) {
		this.classType = classType;
		this.type = null;
		this.loot = new ArrayList<>();
		this.addXp = 0;
	}

	public boolean parseConfiguration(ConfigurationSection section){
		if (section == null){
			return false;
		}

		try{
			type = Enum.valueOf(classType, section.getName());
		}catch(IllegalArgumentException e){
			LOGGER.warning("Couldn't parse section '"+section.getName()+"' in custom loot: Invalid " + classType.getSimpleName());
			return false;
		}

		List<String> itemStrings;
		if (section.isList("loot")) {
			itemStrings = section.getStringList("loot");
		}else {
			itemStrings = Collections.singletonList(section.getString("loot"));
		}

		if (itemStrings.isEmpty()){
			LOGGER.warning("Couldn't parse section '"+section.getName()+"' in custom loot: Missing loot item(s)");
			return false;
		}

		for (String itemStr : itemStrings) {
			try {
				loot.add(JsonItemUtils.getItemFromJson(itemStr));
			} catch (ParseException ex) {
				LOGGER.log(Level.WARNING, "Couldn't parse loot '" + type.name() + "' in custom loot", ex);
				return false;
			}
		}

		addXp = section.getInt("add-xp",0);
		return true;
	}

	public T getType() {
		return type;
	}

	public List<ItemStack> getLoot() {
		return loot;
	}

	public int getAddXp() {
		return addXp;
	}

}
