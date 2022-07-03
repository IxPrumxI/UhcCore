package com.gmail.val59000mc.utils;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

/**
 * A {@link Handler} which forwards log records to a plugin logger.
 * <p>
 *     Log records with a level below {@code INFO} will have their level
 *     increased to {@code INFO}, because they would otherwise not be logged
 *     by the plugin logger.
 * </p>
 */
public class PluginForwardingHandler extends Handler {

	private final Logger pluginLogger;
	private final String pluginLoggerName;

	/**
	 * Creates a new {@code PluginForwardingHandler}.
	 *
	 * @param pluginLogger the plugin logger to which log records are forwarded
	 * @param pluginLoggerName the logger name to use for all log records
	 */
	public PluginForwardingHandler(Logger pluginLogger, String pluginLoggerName) {
		this.pluginLogger = pluginLogger;
		this.pluginLoggerName = pluginLoggerName;
	}

	/**
	 * Creates a logger which forwards log records to a plugin logger.
	 * <p>
	 *     The logger will be named after the package name of the plugin,
	 *     which means that it parents all loggers named after classes
	 *     in that package or in subpackages.
	 * </p>
	 * <p>
	 *     The forwarding logger will use a {@link PluginForwardingHandler}
	 *     instead of its parent handlers, and the logging level will be set
	 *     to {@code INFO} by default.
	 * </p>
	 *
	 * @param plugin the plugin to forward log records for
	 * @return the forwarding logger
	 */
	public static Logger createForwardingLogger(Plugin plugin) {
		final Logger logger = Logger.getLogger(plugin.getClass().getPackage().getName());
		final String prefix = plugin.getDescription().getPrefix();
		final String pluginLoggerName = prefix != null ? prefix : plugin.getName();

		logger.setLevel(Level.INFO);
		logger.setUseParentHandlers(false);
		logger.addHandler(new PluginForwardingHandler(plugin.getLogger(), pluginLoggerName));
		return logger;
	}

	@Override
	public void publish(LogRecord record) {
		// The plugin logger will ignore anything below INFO,
		// so we have to increase the level.
		if (record.getLevel().intValue() < Level.INFO.intValue()) {
			record.setLevel(Level.INFO);
		}
		record.setLoggerName(pluginLoggerName);
		pluginLogger.log(record);
	}

	@Override
	public void flush() {
		// Nothing to flush
	}

	@Override
	public void close() throws SecurityException {
		// Nothing to close
	}

}
