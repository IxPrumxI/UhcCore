package com.gmail.val59000mc.schematics;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SchematicHandler13{

	private static final Logger LOGGER = Logger.getLogger(SchematicHandler13.class.getCanonicalName());

	public static ArrayList<Integer> pasteSchematic(Location loc, String path) throws Exception{
		LOGGER.info("Pasting "+path);
		File schematic = new File(path);
		World world = BukkitAdapter.adapt(loc.getWorld());

		ClipboardFormat format = ClipboardFormats.findByFile(schematic);
		ClipboardReader reader = format.getReader(new FileInputStream(schematic));
		Clipboard clipboard = reader.read();

		EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);

		enableWatchdog(editSession);

		Operation operation = new ClipboardHolder(clipboard)
				.createPaste(editSession)
				.to(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))
				.ignoreAirBlocks(false)
				.build();

		Operations.complete(operation);
		editSession.flushSession();

		ArrayList<Integer> dimensions = new ArrayList<>();
		dimensions.add(clipboard.getDimensions().getY());
		dimensions.add(clipboard.getDimensions().getX());
		dimensions.add(clipboard.getDimensions().getZ());

		LOGGER.info("Successfully pasted '"+path+"' at "+loc.getBlockX()+" "+loc.getBlockY()+" "+loc.getBlockZ());
		return dimensions;
	}

	/**
	 * This feature for added on later versions of WorldEdit 7. It prevents the server from crashing when pasting large schematics.
	 * @param session The edit session you want to enable watchdog on.
	 */
	private static void enableWatchdog(EditSession session){
		try{
			Method setTickingWatchdog = session.getClass().getMethod("setTickingWatchdog", boolean.class);
			setTickingWatchdog.invoke(session, true);
		}catch (ReflectiveOperationException ex){
			// Couldn't turn on watchdog, old WorldEdit version?
			LOGGER.log(Level.FINE, "Unable to enable watchdog", ex);
		}
	}

}
