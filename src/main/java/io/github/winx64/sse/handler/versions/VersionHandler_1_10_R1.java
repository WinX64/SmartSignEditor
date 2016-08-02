/*
 *   SmartSignEditor - Edit your signs with style
 *   Copyright (C) WinX64 2013-2016
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.winx64.sse.handler.versions;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.github.winx64.sse.handler.VersionHandler;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.ChatComponentText;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_10_R1.PlayerConnection;
import net.minecraft.server.v1_10_R1.TileEntitySign;

public final class VersionHandler_1_10_R1 extends VersionHandler {

    @Override
    public void updateSignText(Player player, Sign sign, String[] text) {
	Location loc = sign.getLocation();
	BlockPosition pos = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
	TileEntitySign tileEntitySign = (TileEntitySign) ((CraftWorld) sign.getWorld()).getHandle().getTileEntity(pos);
	PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
	IChatBaseComponent[] oldSignText = new IChatBaseComponent[4];

	for (int i = 0; i < 4; i++) {
	    oldSignText[i] = tileEntitySign.lines[i];
	    tileEntitySign.lines[i] = new ChatComponentText(text[i]);
	}
	conn.sendPacket(tileEntitySign.getUpdatePacket());
	for (int i = 0; i < 4; i++) {
	    tileEntitySign.lines[i] = oldSignText[i];
	}
    }

    @Override
    public void openSignEditor(Player player, Sign sign) {
	Location loc = sign.getLocation();
	BlockPosition pos = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
	EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
	TileEntitySign tileEntitySign = (TileEntitySign) nmsPlayer.world.getTileEntity(pos);
	PlayerConnection conn = nmsPlayer.playerConnection;

	tileEntitySign.isEditable = true;
	tileEntitySign.a(nmsPlayer);
	conn.sendPacket(new PacketPlayOutOpenSignEditor(pos));
    }

    @Override
    public Collection<? extends Player> getOnlinePlayers() {
	return ((CraftServer) Bukkit.getServer()).getOnlinePlayers();
    }
}
