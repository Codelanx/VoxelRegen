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

import com.codelanx.codelanxlib.util.Lambdas;
import com.codelanx.codelanxlib.util.Reflections;
import com.codelanx.codelanxlib.util.exception.Exceptions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;

/**
 * Class description for {@link VoxelRegion}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class VoxelRegion implements ConfigurationSerializable {
    
    private final Vector max, min;
    private final World world;

    public VoxelRegion(Location one, Location two) {
        this(Reflections.nullSafeMutation(one, Location::toVector),
                Reflections.nullSafeMutation(two, Location::toVector),
                Reflections.nullSafeMutation(one, Location::getWorld));
    }
    
    public VoxelRegion(Vector one, Vector two, World world) {
        Exceptions.allNotNull(one, two, world);
        this.max = this.toFlooredVector(one, two, Math::max);
        this.min = this.toFlooredVector(one, two, Math::min);
        this.world = world;
    }

    public VoxelRegion(Map<String, Object> config) {
        this.max = (Vector) config.get("max");
        this.min = (Vector) config.get("min");
        this.world = Bukkit.getWorld(UUID.fromString(String.valueOf(config.get("world"))));
    }

    private Vector toFlooredVector(Vector one, Vector two, IntBinaryOperator pred) {
        return new Vector(pred.applyAsInt(one.getBlockX(), two.getBlockX()),
        pred.applyAsInt(one.getBlockY(), two.getBlockY()),
        pred.applyAsInt(one.getBlockZ(), two.getBlockZ()));
    }

    public Map<Vector, Material> calculate() {
        Set<Material> types = VoxelConfig.BLOCKS_TO_REGEN.as(List.class, String.class)
                .stream().map(Material::matchMaterial).filter(Lambdas::notNull).collect(Collectors.toSet());
        Map<Vector, Material> back = new HashMap<>();
        Location curr = max.toLocation(this.getWorld());
        for(; curr.getBlockY() >= 0 || curr.getBlockY() >= min.getBlockY(); curr.add(0, -1, 0)) {
            for (; curr.getBlockX() >= min.getBlockX(); curr.add(-1, 0, 0)) {
                for (; curr.getBlockZ() >= min.getBlockZ(); curr.add(0, 0, -1)) {
                    Block b = curr.getBlock();
                    if (types.contains(b.getType())) {
                        back.put(curr.toVector(), b.getType());
                    }
                }
                curr.setZ(max.getBlockZ());
            }
            curr.setX(max.getBlockX());
        }
        return back;
    }

    public World getWorld() {
        return this.world;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> back = new HashMap<>();
        back.put("world", this.world.getUID());
        back.put("max", this.max);
        back.put("min", this.min);
        return back;
    }

    public static VoxelRegion valueOf(Map<String, Object> config) {
        return new VoxelRegion(config);
    }
    
    public static VoxelRegion deserialize(Map<String, Object> config) {
        return new VoxelRegion(config);
    }

}
