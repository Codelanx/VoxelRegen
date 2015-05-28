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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class description for {@link RegionWorker}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class RegionWorker implements Runnable {

    private final Map<String, RegenRegion> regions = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final VoxelRegen plugin;
    
    public RegionWorker(VoxelRegen plugin) {
        this.plugin = plugin;
        Scheduler.runAsyncTask(() -> {
            Map<String, RegenRegion> in = this.plugin.getDataFacade().getRegions();
            try {
                this.lock.readLock().lock();
                this.lock.writeLock().lock();
                this.regions.putAll(in);
            } finally {
                this.lock.writeLock().unlock();
                this.lock.readLock().unlock();
            }
        });
    }

    @Override
    public void run() {
        this.regions.values().forEach(r -> {
            r.regen(this.plugin);
        });
    }

    public void removeRegion(String name) {
        try {
            this.lock.writeLock().lock();
            this.regions.remove(name);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public Set<String> getRegionNames() {
        try {
            this.lock.readLock().lock();
            return this.regions.keySet();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public void addRegion(String name, RegenRegion region) {
        try {
            this.lock.writeLock().lock();
            this.regions.put(name, region);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void retrieveRegion(String name) {
        Scheduler.runAsyncTask(() -> {
            String rname = name.toLowerCase();
            RegenRegion r = this.plugin.getDataFacade().getRegion(rname);
            if (r != null) {
                try {
                    this.lock.writeLock().lock();
                    this.regions.put(rname, r);
                } finally {
                    this.lock.writeLock().unlock();
                }
            }
        });
    }

    public void remove(String name) {
        try {
            this.lock.writeLock().lock();
            this.regions.remove(name);
        } finally {
            this.lock.writeLock().unlock();
        }
    }
    
    public boolean hasRegion(String name) {
        try {
            this.lock.readLock().lock();
            return this.regions.containsKey(name);
        } finally {
            this.lock.readLock().unlock();
        }
    }
}
