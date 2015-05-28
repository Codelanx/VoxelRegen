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

import com.codelanx.codelanxlib.permission.Permissions;

/**
 * Represents permissions used in for this plugin
 *
 * @since 1.0.0
 * @author 1Rogue
 * @version 1.0.0
 */
public enum VoxelPerms implements Permissions {
    
    CREATE("create"),
    ;
    
    private final String node;
    
    private VoxelPerms(String node) {
        this.node = node;
    }

    @Override
    public String getBase() {
        return "voxelregen";
    }

    @Override
    public String getNode() {
        return this.node;
    }

}
