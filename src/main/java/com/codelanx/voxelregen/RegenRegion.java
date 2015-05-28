/*
 * Copyright (C) 2015 Codelanx, All Rights Reserved
 *
 * This work is licensed under a Creative Commons
 * Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 *
 * This program is protected software: You are free to distrubute your
 * own use of this software under the terms of the Creative Commons BY-NC-ND
 * license as published by Creative Commons in the year 2015 or as published
 * by a later date. You may not provide the source files or provide a means
 * of running the software outside of those licensed to use it.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the Creative Commons BY-NC-ND license
 * long with this program. If not, see <https://creativecommons.org/licenses/>.
 */
package com.codelanx.voxelregen;

import com.codelanx.codelanxlib.util.BlockData;
import com.codelanx.codelanxlib.util.exception.Exceptions;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * Class description for {@link RegenRegion}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class RegenRegion {

    private final World world;
    private final Map<Vector, BlockData> blocks = new HashMap<>();

    public RegenRegion(UUID world, Map<Vector, BlockData> blocks) {
        World set = Bukkit.getWorld(world);
        Exceptions.notNull(set, IllegalStateException.class);
        this.world = set;
        this.blocks.putAll(blocks);
    }
    
    public Map<Vector, BlockData> getBlocks() {
        return Collections.unmodifiableMap(this.blocks);
    }
    
    public World getWorld() {
        return this.world;
    }

    public void regen(Plugin p) {
        Bukkit.getScheduler().callSyncMethod(p, () -> {
            this.blocks.entrySet().forEach(ent -> {
                Block b = this.world.getBlockAt(ent.getKey().getBlockX(), ent.getKey().getBlockY(), ent.getKey().getBlockZ());
                b.setType(ent.getValue().getMaterial());
                b.setData(ent.getValue().getData());
            });
            return null;
        });
    }
}
