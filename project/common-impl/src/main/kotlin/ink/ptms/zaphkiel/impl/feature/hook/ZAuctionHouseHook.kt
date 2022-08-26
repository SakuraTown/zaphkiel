package ink.ptms.zaphkiel.impl.feature.compat

import fr.maxlego08.zauctionhouse.api.AuctionPlugin
import fr.maxlego08.zauctionhouse.api.blacklist.IBlacklist
import fr.maxlego08.zauctionhouse.api.blacklist.IBlacklistManager
import ink.ptms.zaphkiel.impl.item.toItemStream
import org.bukkit.Bukkit
import org.bukkit.Bukkit.getServer
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent


internal object ZAuctionHouseHook {
    val plugin: AuctionPlugin? = Bukkit.getPluginManager().getPlugin("zAuctionHouseV3") as? AuctionPlugin

    init {
        if (plugin != null) {
            val blacklistManager = getProvider(IBlacklistManager::class.java)
            blacklistManager?.registerBlacklist(ZaphkielIlacklist)
        }
    }

    //用于唤醒类
    @SubscribeEvent
    fun e(e: PluginDisableEvent) {
    }

    private fun <T> getProvider(classz: Class<T>): T? = getServer().servicesManager.getRegistration(classz)?.provider

    object ZaphkielIlacklist : IBlacklist {
        override fun getName(): String = "Zaphkiel_Soul_Bind"

        override fun isBlacklist(item: ItemStack?): Boolean = item?.toItemStream()?.isExtension() == true

    }
}
