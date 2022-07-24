package com.gmail.val59000mc.utils;

import java.util.Optional;

import org.bukkit.Sound;

import io.papermc.lib.PaperLib;

public enum UniversalSound {
	CLICK("CLICK", "UI_BUTTON_CLICK", "UI_BUTTON_CLICK"),
	ENDERDRAGON_GROWL("ENDERDRAGON_GROWL", "ENTITY_ENDERDRAGON_GROWL", "ENTITY_ENDER_DRAGON_GROWL"),
	WITHER_SPAWN("WITHER_SPAWN", "ENTITY_WITHER_SPAWN", "ENTITY_WITHER_SPAWN"),
	FIREWORK_LAUNCH("FIREWORK_LAUNCH", "ENTITY_FIREWORK_LAUNCH", "ENTITY_FIREWORK_ROCKET_LAUNCH"),
	BLOCK_GRASS_BREAK("DIG_GRASS", "BLOCK_GRASS_BREAK", "BLOCK_GRASS_BREAK");

	private final String name8, name9, name13;
	private Sound sound;

	UniversalSound(String name8, String name9, String name13){
		this.name8 = name8;
		this.name9 = name9;
		this.name13 = name13;

		sound = null;
	}

	public Sound getSound(){
		if (sound == null){
			if (PaperLib.getMinecraftVersion() < 9){
				sound = Sound.valueOf(name8);
			}else if (PaperLib.getMinecraftVersion() < 13){
				sound = Sound.valueOf(name9);
			}else {
				sound = Sound.valueOf(name13);
			}
		}

		return sound;
	}

	public static final String CONFIG_VALUE_NONE = "none";
	public static final String CONFIG_VALUE_DEFAULT = "default";
	public static final String CONFIG_BUKKIT_SOUND_PREFIX = "org.bukkit.Sound#";

	/**
	 * Parses a sound string into a {@link Sound}.
	 * <p>
	 *     If the sound string is prefixed by {@link #CONFIG_BUKKIT_SOUND_PREFIX},
	 *     it will be looked up using {@link Sound#valueOf(String)}.
	 * </p>
	 *
	 * @param value the sound string
	 * @param defaultSound the sound to use for {@link #CONFIG_VALUE_DEFAULT}
	 * @return the sound specified by the string, or {@code Optional.empty()} for {@link #CONFIG_VALUE_NONE}
	 * @throws SoundParseException if the sound string is invalid
	 */
	public static Optional<Sound> parse(String value, UniversalSound defaultSound) throws SoundParseException {
		if (value.equals(CONFIG_VALUE_NONE)) {
			return Optional.empty();
		} else if (value.equals(CONFIG_VALUE_DEFAULT)) {
			return Optional.of(defaultSound.getSound());
		} else if (value.startsWith(CONFIG_BUKKIT_SOUND_PREFIX)) {
			final String soundName = value.substring(CONFIG_BUKKIT_SOUND_PREFIX.length());
			final Sound sound;
			try {
				sound = Sound.valueOf(soundName);
			} catch (IllegalArgumentException e) {
				throw new SoundParseException("Invalid org.bukkit.Sound", e);
			}
			return Optional.of(sound);
		}
		throw new SoundParseException("Invalid sound '" + value + "'");
	}

	public static class SoundParseException extends Exception {

		private final String message;

		public SoundParseException(String reason, Throwable cause) {
			super(reason, cause);
			this.message = reason;
		}

		public SoundParseException(String reason) {
			super(reason);
			this.message = reason;
		}

		public String getMessage() {
			return message;
		}

	}

}
