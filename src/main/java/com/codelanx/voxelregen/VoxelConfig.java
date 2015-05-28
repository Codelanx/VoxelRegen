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

import com.codelanx.codelanxlib.annotation.PluginClass;
import com.codelanx.codelanxlib.annotation.RelativePath;
import com.codelanx.codelanxlib.config.Config;
import com.codelanx.codelanxlib.config.DataHolder;
import com.codelanx.codelanxlib.data.types.Yaml;
import java.util.ArrayList;

/**
 * Config values
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
@PluginClass(VoxelRegen.class)
@RelativePath("config.yml")
public enum VoxelConfig implements Config {
    
    BLOCKS_TO_REGEN("regenerated-blocks", new ArrayList<>()),
    REGEN_TIME("seconds-between-regens", 300),
    MYSQL_HOST("mysql.host", "localhost"),
    MYSQL_USER("mysql.user", "root"),
    MYSQL_PASS("mysql.pass", "password"),
    MYSQL_DATABASE("mysql.database", "skyblock"),
    MYSQL_PORT("mysql.port", "3306"),
    MYSQL_KEEP_ALIVE("mysql.keep-alive", 10),
    ;

    private static final DataHolder<Yaml> DATA = new DataHolder<>(Yaml.class);
    private final String path;
    private final Object def;

    private VoxelConfig(String path, Object def) {
        this.path = path;
        this.def = def;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public Object getDefault() {
        return this.def;
    }

    @Override
    public DataHolder<Yaml> getData() {
        return VoxelConfig.DATA;
    }

}
