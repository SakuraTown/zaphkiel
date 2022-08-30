package ink.ptms.zaphkiel.impl.feature.hook

import org.bukkit.Bukkit

internal object SakuraBindHook {
    val plugin = Bukkit.getPluginManager().getPlugin("SakuraBind")
    val hasHook = plugin != null

}