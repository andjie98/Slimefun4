package io.github.thebusybiscuit.slimefun4.libraries.dough.protection;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

/**
 * 精简版兼容层：模拟新版 Slimefun4 的 dough 库 {@code ProtectionModule} 接口。
 * <p>
 * Residence 5.1.6.2 的 {@code SlimeFunResidenceModule} 实现了此接口，
 * 精简版本身不调用它，仅为满足 Residence 启动时的类加载。
 */
public interface ProtectionModule {

	/**
	 * 模块加载时调用。
	 */
	void load();

	/**
	 * @return 该模块所属的插件
	 */
	Plugin getPlugin();

	/**
	 * 检查玩家在指定位置是否拥有执行指定动作的权限。
	 *
	 * @param p      玩家
	 * @param l      位置
	 * @param action 动作类型
	 * @return 有权限返回 true
	 */
	boolean hasPermission(OfflinePlayer p, Location l, Interaction action);

}
