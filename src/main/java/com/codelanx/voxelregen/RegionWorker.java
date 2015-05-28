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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class description for {@link RegionWorker}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class RegionWorker implements Runnable {

    private final Map<String, RegenRegion> regions = new ConcurrentHashMap<>();
    private final VoxelRegen plugin;
    
    public RegionWorker(VoxelRegen plugin) {
        this.plugin = plugin;
        Scheduler.runAsyncTask(() -> {
            Map<String, RegenRegion> in = this.plugin.getDataFacade().getRegions();
            this.regions.putAll(in);
        });
    }

    @Override
    public void run() {
        this.regions.values().forEach(r -> {
            r.regen(this.plugin);
        });
    }

    public void removeRegion(String name) {
        this.regions.remove(name);
    }

    public Set<String> getRegionNames() {
        return this.regions.keySet();
    }

    public void addRegion(String name, RegenRegion region) {
        this.regions.put(name, region);
    }

    public void retrieveRegion(String name) {
        Scheduler.runAsyncTask(() -> {
            String rname = name.toLowerCase();
            RegenRegion r = this.plugin.getDataFacade().getRegion(rname);
            if (r != null) {
                this.regions.put(rname, r);
            }
        });
    }

    public void remove(String name) {
        this.regions.remove(name);
    }
    
    public boolean hasRegion(String name) {
        return this.regions.containsKey(name);
    }
}
