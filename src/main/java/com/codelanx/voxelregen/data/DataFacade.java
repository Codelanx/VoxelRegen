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

import com.codelanx.codelanxlib.data.types.SQLite;
import com.codelanx.codelanxlib.util.BlockData;
import com.codelanx.codelanxlib.util.Scheduler;
import com.codelanx.voxelregen.RegenRegion;
import com.codelanx.voxelregen.VoxelRegion;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bukkit.util.Vector;

/**
 * Class description for {@link DataFacade}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class DataFacade {

    private final SQLite db = new SQLite();
    
    public DataFacade(File location) {
        try {
            this.db.open(location);
        } catch (SQLException ex) {
            Logger.getLogger(DataFacade.class.getName()).log(Level.SEVERE, "Error opening SQLite connection", ex);
        }
        if (!db.checkTable("regions")) {
            db.update(Statements.CREATE_REGIONS_TABLE);
        }
        if (!db.checkTable("blocks")) {
            db.update(Statements.CREATE_BLOCKS_TABLE);
        }
    }

    public synchronized void addRegion(String name, VoxelRegion region) {
        Scheduler.runAsyncTask(() -> {
            Map<Vector, BlockData> materials = region.calculate();
            this.addRegion(name, materials, region.getWorld().getUID());
        });
    }
    
    public synchronized void addRegion(String name, Map<Vector, BlockData> blocks, UUID world) {
        this.db.update(Statements.ADD_REGION, name, world.toString());
        this.db.batchUpdate(Statements.ADD_BLOCKS_TO_REGION, 100, blocks.entrySet(),
                ent -> ent.getKey().getBlockX(),
                ent -> ent.getKey().getBlockY(),
                ent -> ent.getKey().getBlockZ(),
                ent -> ent.getValue().getMaterial().toString(),
                ent -> ent.getValue().getData(),
                ent -> name);
    }

    public synchronized void removeRegion(String name) {
        int id = this.db.query(rs -> { return rs.getInt("id"); }, Statements.GET_REGION_ID, name).getResponse();
        this.db.update(Statements.REMOVE_BLOCKS_WITH_ID, id);
        this.db.update(Statements.REMOVE_REGION, name);
    }

    public RegenRegion getRegion(String name) {
        UUID world = this.db.query(rs -> { return UUID.fromString(rs.getString("world_uuid")); }, Statements.GET_WORLD, name).getResponse();
        return this.db.query(rs -> {
            Map<Vector, BlockData> back = new HashMap<>();
            while (rs.next()) {
                back.put(new Vector(rs.getInt("x"), rs.getInt("y"), rs.getInt("z")), BlockData.fromString(rs.getString("material") + ":" + rs.getByte("data")));
            }
            return new RegenRegion(world, back);
        }, Statements.GET_REGION_CONTENTS, name).getResponse();
    }
    
    public Map<String, RegenRegion> getRegions() {
        Map<String, UUID> regions = this.db.query(rs -> { 
            Map<String, UUID> back = new HashMap<>();
            while (rs.next()) {
                back.put(rs.getString("name"), UUID.fromString(rs.getString("world_uuid")));
            }
            return back;
        }, Statements.GET_REGION_META).getResponse();
        return this.db.query(rs -> {
            Map<String, Map<Vector, BlockData>> data = new HashMap<>();
            while (rs.next()) {
                this.getSafeSubMap(data, rs.getString("name")).put(new Vector(rs.getInt("x"), rs.getInt("y"), rs.getInt("z")), BlockData.fromString(rs.getString("material") + ":" + rs.getByte("data")));
            }
            return data.entrySet().stream().collect(Collectors.toMap(ent -> ent.getKey(), ent -> new RegenRegion(regions.get(ent.getKey()), ent.getValue())));
        }, Statements.GET_ALL_REGIONS).getResponse();
    }
    
    private <R, C, V> Map<C, V> getSafeSubMap(Map<R, Map<C, V>> map, R key) {
        Map<C, V> back = map.get(key);
        if (back == null) {
            back = new HashMap<>();
            map.put(key, back);
        }
        return back;
    }

}
