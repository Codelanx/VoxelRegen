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

import com.codelanx.codelanxlib.config.Config;
import com.codelanx.codelanxlib.util.BlockData;
import com.codelanx.voxelregen.RegenRegion;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

/**
 * Persistent YAML data storage for region information
 * 
 * TODO: Bit lazy on the thread safety here, could use some improvement
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class YamlDataFacade implements DataFacade {

    private final File folder;
    private final Map<String, FileConfiguration> configs = new HashMap<>();

    public YamlDataFacade(File folder) {
        this.folder = folder;
    }

    @Override
    public synchronized void addRegion(String name, Map<Vector, BlockData> blocks, UUID world) {
        FileConfiguration f = this.getConfig(name);
        f.set("world", world.toString());
        List<Map<String, Object>> setter = blocks.entrySet().stream().map(ent -> {
            Map<String, Object> add = new HashMap<>();
            add.put("location", ent.getKey());
            add.put("block", ent.getValue().toString());
            return add;
        }).collect(Collectors.toList());
        f.set("blocks", setter);
        try {
            f.save(this.getFile(name));
        } catch (IOException ex) {
            Logger.getLogger(YamlDataFacade.class.getName()).log(Level.SEVERE, "Error saving region '" + name + "'", ex);
        }
    }

    @Override
    public synchronized void removeRegion(String name) {
        this.getFile(name).delete();
    }

    @Override
    public synchronized RegenRegion getRegion(String name) {
        FileConfiguration f = this.getConfig(name);
        UUID world = UUID.fromString(f.getString("world"));
        List<?> data = f.getList("blocks");
        Map<Vector, BlockData> blocks = new HashMap<>();
        data.stream().map(o -> Config.getConfigSectionValue(o)).forEach(bl -> {
            blocks.put((Vector) bl.get("location"), BlockData.fromString(String.valueOf(bl.get("block"))));
        });
        return new RegenRegion(world, blocks);
    }

    @Override
    public synchronized Map<String, RegenRegion> getRegions() {
        Map<String, RegenRegion> back = new HashMap<>();
        for (File f : this.folder.listFiles(f -> f.getName().endsWith(".yml"))) {
            String name = f.getName();
            name = name.length() > 4 ? name.substring(0, name.length() - 4) : name;
            System.out.println("Loading region '" + name + "'...");
            back.put(name, this.getRegion(name));
        }
        return back;
    }

    private File getFile(String name) {
        return new File(this.folder, name + ".yml");
    }
    
    private FileConfiguration getConfig(String name) {
        FileConfiguration back = this.configs.get(name);
        if (back == null) {
            synchronized (this) {
                back = this.configs.get(name);
                if (back == null) {
                    File f = this.getFile(name);
                    if (!f.exists()) {
                        try {
                            f.createNewFile();
                        } catch (IOException ex) {
                            Logger.getLogger(YamlDataFacade.class.getName()).log(Level.SEVERE, "Error creating new configuration file", ex);
                        }
                    }
                    back = YamlConfiguration.loadConfiguration(f);
                    this.configs.put(name, back);
                }
            }
        }
        return back;
    }

}
