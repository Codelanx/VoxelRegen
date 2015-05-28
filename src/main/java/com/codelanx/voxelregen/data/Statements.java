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

/**
 * Class description for {@link Statements}
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public class Statements {

    static final String CREATE_REGIONS_TABLE = "CREATE TABLE IF NOT EXISTS `regions` ("
            + " `id` INTEGER UNSIGNED PRIMARY KEY NOT NULL,"
            + " `name` TEXT NOT NULL,"
            + " `world_uuid` TEXT NOT NULL,"
            + " UNIQUE (`name`) ON CONFLICT ROLLBACK"
            + ")";
    static final String CREATE_BLOCKS_TABLE = "CREATE TABLE IF NOT EXISTS `blocks` ("
            + " `id` INTEGER UNSIGNED PRIMARY KEY NOT NULL,"
            + " `region` INTEGER NOT NULL,"
            + " `x` INTEGER NOT NULL,"
            + " `y` INTEGER NOT NULL,"
            + " `z` INTEGER NOT NULL,"
            + " `material` TEXT NOT NULL,"
            + " `data` INTEGER NOT NULL,"
            + " UNIQUE (x, y, z) ON CONFLICT REPLACE"
            + ")";
    static final String ADD_REGION = "INSERT OR IGNORE INTO `regions` (`name`, `world_uuid`) VALUES (?, ?)";
    static final String ADD_BLOCKS_TO_REGION = "INSERT OR REPLACE INTO `blocks` (region, x, y, z, material, data)"
            + " SELECT regions.id AS region, ? AS x, ? AS y, ? AS z, ? AS material, ? AS data"
            + " FROM regions WHERE regions.name = ?";
    static final String GET_REGION_CONTENTS = "SELECT blocks.x, blocks.y, blocks.z, blocks.material, blocks.data"
            + " FROM blocks"
            + " INNER JOIN regions ON regions.id = blocks.region"
            + " WHERE regions.name = ?";
    static final String GET_REGION_ID = "SELECT id FROM regions WHERE name = ?";
    static final String REMOVE_BLOCKS_WITH_ID = "DELETE FROM blocks WHERE region = ?";
    static final String REMOVE_REGION = "DELETE FROM `regions` WHERE `name` = ?";
    static final String GET_WORLD = "SELECT `world_uuid` FROM `regions` WHERE `name` = ?";
    static final String GET_REGION_META = "SELECT `name`, `world_uuid` FROM `regions`";
    static final String GET_ALL_REGIONS = "SELECT regions.name, blocks.x, blocks.y, blocks.z, blocks.material, blocks.data"
            + " FROM blocks"
            + " INNER JOIN regions ON regions.id = blocks.region";

}
