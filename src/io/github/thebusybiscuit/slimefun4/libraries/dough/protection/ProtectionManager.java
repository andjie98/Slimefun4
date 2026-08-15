package io.github.thebusybiscuit.slimefun4.libraries.dough.protection;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * 精简版兼容层：模拟新版 Slimefun4 dough 库的 {@code ProtectionManager}。
 * <p>
 * Residence 5.1.6.2 通过以下两种方式注册保护模块（先反射、失败后直调）：
 * <ul>
 *   <li>{@code registerModule(Server, String, Function&lt;Plugin, ProtectionModule&gt;)}</li>
 *   <li>{@code registerModule(PluginManager, String, Function&lt;Plugin, ProtectionModule&gt;)}</li>
 * </ul>
 * 两个重载都必须存在，否则 Residence 无法完成注册。
 */
public class ProtectionManager {

	private final Map<String, ProtectionModule> modules = new HashMap<String, ProtectionModule>();

	/**
	 * 反射路径：Residence 通过 {@code Server} 参数反射调用此方法。
	 *
	 * @param server      服务器实例
	 * @param name        插件名（如 "Residence"）
	 * @param constructor 模块构造器
	 */
	public void registerModule(Server server, String name, Function<Plugin, ProtectionModule> constructor) {
		registerModule(server != null ? server.getPluginManager(): null, name, constructor);
	}

	/**
	 * 直接调用路径。
	 *
	 * @param manager     插件管理器
	 * @param name        插件名（如 "Residence"）
	 * @param constructor 模块构造器
	 */
	public void registerModule(PluginManager manager, String name, Function<Plugin, ProtectionModule> constructor) {
		if (manager == null || name == null || constructor == null) return;

		Plugin plugin = manager.getPlugin(name);
		if (plugin != null && plugin.isEnabled()) {
			ProtectionModule module = constructor.apply(plugin);
			if (module != null) {
				module.load();
				modules.put(name, module);
			}
		}
	}

	/**
	 * 检查玩家在指定位置是否拥有执行指定动作的权限。
	 * 任一已注册模块拒绝则返回 false；无模块时默认放行。
	 *
	 * @param p      玩家
	 * @param l      位置
	 * @param action 动作类型
	 * @return 有权限返回 true
	 */
	public boolean hasPermission(OfflinePlayer p, Location l, Interaction action) {
		for (ProtectionModule module: modules.values()) {
			if (module.getPlugin() != null && module.getPlugin().isEnabled()) {
				if (!module.hasPermission(p, l, action)) return false;
			}
		}
		return true;
	}

	/**
	 * 检查玩家在指定方块处是否拥有执行指定动作的权限。
	 *
	 * @param p      玩家
	 * @param b      方块
	 * @param action 动作类型
	 * @return 有权限返回 true
	 */
	public boolean hasPermission(OfflinePlayer p, Block b, Interaction action) {
		return hasPermission(p, b.getLocation(), action);
	}

}
