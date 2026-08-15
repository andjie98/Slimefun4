package io.github.thebusybiscuit.slimefun4.libraries.dough.protection;

/**
 * 精简版兼容层：模拟新版 Slimefun4 dough 库的 {@code Interaction} 枚举。
 * <p>
 * <b>注意：枚举声明顺序不可更改。</b> Residence 5.1.6.2 在字节码层面通过
 * {@code ordinal()} 生成 switch 表：BREAK_BLOCK=1、PLACE_BLOCK=2、
 * INTERACT_BLOCK=3、ATTACK_PLAYER=4、ATTACK_ENTITY=5、INTERACT_ENTITY=6。
 * 第 0 位未被 Residence 引用，仅作占位。
 */
public enum Interaction {

	/** 占位（Residence 未引用 ordinal 0） */
	UNKNOWN,

	/** 破坏方块 */
	BREAK_BLOCK,

	/** 放置方块 */
	PLACE_BLOCK,

	/** 与方块交互（打开容器等） */
	INTERACT_BLOCK,

	/** 攻击玩家 */
	ATTACK_PLAYER,

	/** 攻击实体 */
	ATTACK_ENTITY,

	/** 与实体交互 */
	INTERACT_ENTITY

}
