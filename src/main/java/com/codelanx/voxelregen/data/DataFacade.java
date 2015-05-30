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
package com.codelanx.voxelregen.data;

import com.codelanx.codelanxlib.util.BlockData;
import com.codelanx.codelanxlib.util.Scheduler;
import com.codelanx.voxelregen.RegenRegion;
import com.codelanx.voxelregen.VoxelConfig;
import com.codelanx.voxelregen.VoxelRegen;
import com.codelanx.voxelregen.VoxelRegion;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * Class description for {@link DataFacade}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public interface DataFacade {
    
    default public void addRegion(VoxelRegen plugin, String name, VoxelRegion region) {
        Scheduler.runAsyncTask(() -> {
            Map<Vector, BlockData> materials = region.calculate();
            this.addRegion(name, materials, region.getWorld().getUID());
            plugin.getWorker().retrieveRegion(name);
        });
    }
    
    public void addRegion(String name, Map<Vector, BlockData> blocks, UUID world);
    
    public void removeRegion(String name);
    
    public RegenRegion getRegion(String name);
    
    public Map<String, RegenRegion> getRegions();
    
    public static DataFacade getData(Plugin plugin) {
        if ("sqlite".equalsIgnoreCase(VoxelConfig.STORAGE_TYPE.as(String.class))) {
            File db = new File(plugin.getDataFolder(), "data.db");
            plugin.getDataFolder().mkdirs();
            if (!db.exists()) {
                try {
                    db.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(VoxelRegen.class.getName()).log(Level.SEVERE, "Error creating SQLite database file", ex);
                }
            }
            return new SQLiteDataFacade(db);
        }
        File folder = new File(plugin.getDataFolder(), "data");
        folder.mkdirs();
        return new YamlDataFacade(folder);
    }

}
