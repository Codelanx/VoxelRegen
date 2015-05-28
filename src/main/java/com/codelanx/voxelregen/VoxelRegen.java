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

import com.codelanx.codelanxlib.util.Scheduler;
import com.codelanx.codelanxlib.util.exception.Exceptions;
import com.codelanx.voxelregen.command.VoxelRegenCommand;
import com.codelanx.voxelregen.data.DataFacade;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Class description for {@link VoxelRegen}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class VoxelRegen extends JavaPlugin {

    private final Map<UUID, VoxelRegion> creations = new HashMap<>();
    private DataFacade data;
    private RegionWorker worker;

    @Override
    public void onEnable() {
        File db = new File(this.getDataFolder(), "data.db");
        this.getDataFolder().mkdirs();
        if (!db.exists()) {
            try {
                db.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(VoxelRegen.class.getName()).log(Level.SEVERE, "Error creating SQLite database file", ex);
            }
        }
        this.data = new DataFacade(db);
        this.worker = new RegionWorker(this);
        Scheduler.runAsyncTaskRepeat(this.worker, 0, VoxelConfig.REGEN_TIME.as(int.class));
        new VoxelRegenCommand(this);
    }
    
    public void queueRegionCreation(UUID player, VoxelRegion make) {
        Exceptions.allNotNull(player, make);
        this.creations.put(player, make);
    }
    
    public void createQueuedRegion(UUID player, String name) {
        name = name.toLowerCase();
        Exceptions.allNotNull(player, name);
        VoxelRegion cr = this.creations.remove(player);
        Validate.notNull(cr, "No queued region creation for UUID '" + player + "'");
        this.data.addRegion(this, name, cr);
    }
    
    public boolean hasQueuedRegion(UUID player) {
        return this.creations.containsKey(player);
    }

    public boolean regionExists(String name) {
        name = name.toLowerCase();
        return this.worker.hasRegion(name);
    }

    public void removeRegion(String name) {
        Scheduler.runAsyncTask(() -> this.data.removeRegion(name.toLowerCase()));
        this.worker.remove(name.toLowerCase());
    }
    
    public Set<String> getRegionNames() {
        return this.worker.getRegionNames();
    }
    
    public DataFacade getDataFacade() {
        return this.data;
    }

    public RegionWorker getWorker() {
        return this.worker;
    }

}
