package ink.ptms.zaphkiel.impl.meta

import ink.ptms.zaphkiel.item.meta.Meta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import taboolib.common.platform.function.warning
import taboolib.common5.Coerce
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.nms.ItemTag
import java.util.*

@MetaKey("potion")
class MetaPotion(root: ConfigurationSection) : Meta(root) {

    val base = root.getString("meta.potion.base")
    val potions = root.getConfigurationSection("meta.potion")?.getValues(false)
        ?.filter { PotionEffectType.getByName(it.key) != null }
        ?.map {
            PotionEffect(
                PotionEffectType.getByName(it.key)!!,
                Coerce.toInteger(it.value.toString().split("-")[0]),
                Coerce.toInteger(it.value.toString().split("-").getOrElse(1) { 0 })
            )
        }?.toList()
    override val id: String
        get() = "potion"

    override fun fromMeta(key: String, itemMeta: ItemMeta, compound: ItemTag) {
        if (itemMeta !is PotionMeta) return
        val section = root.createSection(key)
        section["base"] = itemMeta.basePotionData.type.name
        val customEffects = itemMeta.customEffects
        if (customEffects.isEmpty()) return
        for (customEffect in customEffects) {
            section[customEffect.type.name] = "${customEffect.duration}-${customEffect.amplifier}"
        }
        return
    }

    override fun build(itemMeta: ItemMeta) {
        if (itemMeta is PotionMeta) {
            if (base != null) {
                try {
                    itemMeta.basePotionData = PotionData(PotionType.valueOf(base.uppercase(Locale.getDefault())))
                } catch (ignored: Throwable) {
                    warning("Unknown base potion: $base")
                }
            }
            potions?.forEach { itemMeta.addCustomEffect(it, true) }
        }
    }

    override fun drop(itemMeta: ItemMeta) {
        if (itemMeta is PotionMeta) {
            itemMeta.basePotionData = PotionData(PotionType.WATER)
            itemMeta.clearCustomEffects()
        }
    }

    override fun toString(): String {
        return "MetaPotion(potions=$potions)"
    }
}