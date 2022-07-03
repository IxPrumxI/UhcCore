package com.gmail.val59000mc.threads;

import com.gmail.val59000mc.UhcCore;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ChunkLoaderThread implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(ChunkLoaderThread.class.getCanonicalName());

	private final World world;
	private final int restEveryNumOfChunks, restDuration;

	private final int maxChunk;
	private int x, z;
	private final int totalChunksToLoad;
	private int chunksLoaded;

	public ChunkLoaderThread(World world, int size, int restEveryNumOfChunks, int restDuration) {
		this.world = world;
		this.restEveryNumOfChunks = restEveryNumOfChunks;
		this.restDuration = restDuration;

		maxChunk = Math.round(size/16f) + 1;

		totalChunksToLoad = (2*maxChunk+1)*(2*maxChunk+1);

		x = -maxChunk;
		z = -maxChunk;
	}

	public abstract void onDoneLoadingWorld();
	public abstract void onDoneLoadingChunk(Chunk chunk);

	@Override
	public void run() {
		int loaded = 0;
		while(x <= maxChunk && loaded < restEveryNumOfChunks){
			try {
				Chunk chunk = PaperLib.getChunkAtAsync(world, x, z, true).get();

				if (Bukkit.isPrimaryThread()){
					onDoneLoadingChunk(chunk);
				}else {
					Bukkit.getScheduler().runTask(UhcCore.getPlugin(), () -> onDoneLoadingChunk(chunk));
				}
			} catch (InterruptedException e) {
				LOGGER.log(Level.WARNING, "Chunk loader interrupted", e);
				return;
			} catch (ExecutionException e) {
				LOGGER.log(Level.WARNING, "Unable to load chunk", e);
			}

			loaded++;
			z++;

			if (z > maxChunk){
				z = -maxChunk;
				x++;
			}
		}

		chunksLoaded += loaded;

		// Cancel world generation if the plugin has been disabled.
		if (!UhcCore.getPlugin().isEnabled()) {
			LOGGER.info("Plugin is disabled, stopping world generation!");
			return;
		}

		// Not yet done loading all chunks
		if(x <= maxChunk){
			LOGGER.info("Loading map "+getLoadingState()+"% - "+chunksLoaded+"/"+totalChunksToLoad+" chunks loaded");

			if (PaperLib.isPaper() && PaperLib.getMinecraftVersion() >= 13){
				Bukkit.getScheduler().scheduleAsyncDelayedTask(UhcCore.getPlugin(), this, restDuration);
			}else {
				Bukkit.getScheduler().scheduleSyncDelayedTask(UhcCore.getPlugin(), this, restDuration);
			}
		}
		// Done loading all chunks
		else{
			Bukkit.getScheduler().runTask(UhcCore.getPlugin(), this::onDoneLoadingWorld);
		}
	}

	public void printSettings(){
		LOGGER.info("Generating environment "+world.getEnvironment().toString());
		LOGGER.info("Loading a total "+Math.floor(totalChunksToLoad)+" chunks, up to chunk ( "+maxChunk+" , "+maxChunk+" )");
		LOGGER.info("Resting "+restDuration+" ticks every "+restEveryNumOfChunks+" chunks");
		LOGGER.info("Loading map "+getLoadingState()+"%");
	}

	private String getLoadingState(){
		double percentage = 100*(double)chunksLoaded/totalChunksToLoad;
		return world.getEnvironment()+" "+(Math.floor(10*percentage)/10);
	}

}
