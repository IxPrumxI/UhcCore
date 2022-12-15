package com.gmail.val59000mc.utils;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;

import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class FloodFill {

	/**
	 * Performs a non-destructive flood fill to find all 26-connected blocks from an origin block.
	 *
	 * @param origin the origin block
	 * @param isInside predicate to test whether a neighbor is "inside" the fill
	 * @param limit size limit in blocks for the flood fill (approximate, not an exact limit)
	 * @return coordinate vectors for all of the connected blocks, starting from (and including) the origin
	 *
	 * @see <a href="https://en.wikipedia.org/wiki/Flood_fill">Wikipedia: Flood fill</a>
	 * @see <a href="https://en.wikipedia.org/wiki/Pixel_connectivity">Wikipedia: Pixel connectivity</a>
	 */
	public static Set<Vector> floodFind26(Block origin, Predicate<Block> isInside, int limit) {
		final Set<Vector> found = new HashSet<>();
		final Queue<Block> queue = new ArrayDeque<>();
		queue.add(origin);
		found.add(new Vector(origin.getX(), origin.getY(), origin.getZ()));

		while (!queue.isEmpty() && found.size() < limit) {
			final Block current = queue.remove();

			for (int dx = -1; dx <= 1; dx++) {
				for (int dy = -1; dy <= 1; dy++) {
					for (int dz = -1; dz <= 1; dz++) {
						if (dx == 0 && dy == 0 && dz == 0) continue;
						final Block neighbor = current.getRelative(dx, dy, dz);
						final Vector neighborPos = new Vector(neighbor.getX(), neighbor.getY(), neighbor.getZ());
						// If it's inside, mark as found and add to queue if not already found
						if (isInside.test(neighbor) && found.add(neighborPos)) {
							queue.add(neighbor);
						}
					}
				}
			}
		}
		return found;
	}

}
