package io.github.winx64.sse.tool.tools;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import io.github.winx64.sse.MathUtil;
import io.github.winx64.sse.SmartSignEditor;
import io.github.winx64.sse.configuration.SignMessages.Message;
import io.github.winx64.sse.player.Permissions;
import io.github.winx64.sse.player.SmartPlayer;
import io.github.winx64.sse.tool.Tool;
import io.github.winx64.sse.tool.ToolType;

public final class EraseTool extends Tool {

	public EraseTool(SmartSignEditor plugin) {
		super(plugin, ToolType.ERASE, "Sign Erase", "Line Erase", Permissions.TOOL_ERASE_ALL,
				Permissions.TOOL_ERASE_LINE, true, false);
	}

	@Override
	public void usePrimary(SmartPlayer sPlayer, Sign sign) {
		Player player = sPlayer.getPlayer();

		if (plugin.getVersionAdapter().isSignBeingEdited(sign)
				&& !player.hasPermission(Permissions.TOOL_EDIT_OVERRIDE)) {
			player.sendMessage(signMessages.get(Message.OVERRIDE_NO_PERMISSION));
			return;
		}

		for (int i = 0; i < 4; i++) {
			sign.setLine(i, "");
		}
		sign.update();
		player.sendMessage(signMessages.get(Message.TOOL_SIGN_CLEARED));
		this.primaryUseCount++;
	}

	@Override
	public void useSecondary(SmartPlayer sPlayer, Sign sign) {
		Player player = sPlayer.getPlayer();

		if (plugin.getVersionAdapter().isSignBeingEdited(sign)
				&& !player.hasPermission(Permissions.TOOL_EDIT_OVERRIDE)) {
			player.sendMessage(signMessages.get(Message.OVERRIDE_NO_PERMISSION));
			return;
		}

		Vector intersection = MathUtil.getSightSignIntersection(player, sign);
		if (intersection == null) {
			player.sendMessage(signMessages.get(Message.INVALID_LINE));
			return;
		}

		int clickedLine = MathUtil.getSignLine(intersection, sign);

		sign.setLine(clickedLine, "");
		sign.update();
		player.sendMessage(signMessages.get(Message.TOOL_LINE_CLEARED));
		this.secondaryUseCount++;
	}
}
