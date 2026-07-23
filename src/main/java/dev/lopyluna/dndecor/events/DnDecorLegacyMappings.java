package dev.lopyluna.dndecor.events;

import dev.lopyluna.dndecor.DnDecor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;

/**
 * Migrates registry entries from the original Design n' Decor mod id.
 *
 * <p>Only exact path matches are remapped. Removed entries are deliberately
 * left unresolved until a compatible block or item has been restored.</p>
 */
public final class DnDecorLegacyMappings {
    public static final String LEGACY_MOD_ID = "design_decor";

    private DnDecorLegacyMappings() {
    }

    public static void onMissingMappings(MissingMappingsEvent event) {
        int blocks = remapExactPaths(event, ForgeRegistries.Keys.BLOCKS, ForgeRegistries.BLOCKS);
        int items = remapExactPaths(event, ForgeRegistries.Keys.ITEMS, ForgeRegistries.ITEMS);

        if (blocks > 0 || items > 0) {
            DnDecor.LOGGER.info(
                    "Remapped {} legacy Design n' Decor blocks and {} items from '{}' to '{}'",
                    blocks, items, LEGACY_MOD_ID, DnDecor.MOD_ID
            );
        }
    }

    private static <T> int remapExactPaths(
            MissingMappingsEvent event,
            ResourceKey<? extends Registry<T>> registryKey,
            IForgeRegistry<T> registry
    ) {
        int remapped = 0;

        for (MissingMappingsEvent.Mapping<T> mapping : event.getMappings(registryKey, LEGACY_MOD_ID)) {
            ResourceLocation targetId = ResourceLocation.fromNamespaceAndPath(
                    DnDecor.MOD_ID,
                    mapping.getKey().getPath()
            );
            if (!registry.containsKey(targetId)) continue;

            T target = registry.getValue(targetId);
            if (target == null) continue;

            mapping.remap(target);
            remapped++;
        }

        return remapped;
    }
}
