package me.awabi2048.customheadpainter.publish

import com.destroystokyo.paper.profile.ProfileProperty
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ResolvableProfile
import java.util.UUID
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object HeadItemFactory {
    fun create(name: String, skin: PublishedSkin): ItemStack {
        val item = ItemStack(Material.PLAYER_HEAD)
        val profileBuilder = ResolvableProfile.resolvableProfile()
            .uuid(UUID.randomUUID())
            .name("CHPainter")
            .addProperty(
                ProfileProperty(
                    "textures",
                    skin.textureValue,
                    skin.textureSignature,
                ),
            )
        item.setData(DataComponentTypes.PROFILE, profileBuilder)

        val meta = item.itemMeta
        meta.itemName(Component.text(name))
        item.itemMeta = meta
        return item
    }
}
