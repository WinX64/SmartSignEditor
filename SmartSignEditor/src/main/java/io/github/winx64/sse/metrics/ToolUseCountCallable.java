package io.github.winx64.sse.metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import io.github.winx64.sse.SmartSignEditor;
import io.github.winx64.sse.tool.Tool;
import io.github.winx64.sse.tool.ToolType;

public class ToolUseCountCallable implements Callable<Map<String, Map<String, Integer>>> {

	private final SmartSignEditor plugin;

	public ToolUseCountCallable(SmartSignEditor plugin) {
		this.plugin = plugin;
	}

	@Override
	public Map<String, Map<String, Integer>> call() throws Exception {
		Map<String, Map<String, Integer>> map = new HashMap<>();

		for (ToolType type : ToolType.values()) {
			Map<String, Integer> subMap = new HashMap<>();
			map.put(type.getName(), subMap);
			Tool tool = plugin.getTool(type);

			subMap.put(tool.getPrimaryName(), tool.getPrimaryUseCount());
			if (tool.getSecondaryName() != null) {
				subMap.put(tool.getSecondaryName(), tool.getSecondaryUseCount());
			}
		}

		return map;
	}

}