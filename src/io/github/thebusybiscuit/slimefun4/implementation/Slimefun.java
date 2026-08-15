package io.github.thebusybiscuit.slimefun4.implementation;

import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.ProtectionManager;

/**
 * 精简版兼容层：模拟新版 Slimefun4 的 {@code Slimefun} 主类。
 * <p>
 * Residence 5.1.6.2 启动时会直接调用 {@link #getProtectionManager()}（INVOKESTATIC），
 * 因此本类必须存在且提供同签名的静态方法，否则 Residence 抛出 NoClassDefFoundError。
 * <p>
 * 注意：这与精简版自身的 {@code me.mrCookieSlime.Slimefun} 是两套体系，
 * 本类仅用于第三方插件（Residence）的兼容。
 */
public final class Slimefun {

	private static final ProtectionManager protectionManager = new ProtectionManager();

	private Slimefun() {}

	/**
	 * @return 全局保护管理器（单例）
	 */
	public static ProtectionManager getProtectionManager() {
		return protectionManager;
	}

}
