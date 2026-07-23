package dev.lopyluna.dndecor.gametest;

import dev.lopyluna.dndecor.DnDecor;
import dev.lopyluna.dndecor.events.DnDecorLegacyMappings;
import dev.lopyluna.dndecor.content.blocks.LegacySignBlock;
import dev.lopyluna.dndecor.register.DnDecorBlocks;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;

import java.util.List;

@GameTestHolder(DnDecor.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DnDecorLegacyMappingsGameTests {
    private static final List<String> LEGACY_STONE_MACHINES = List.of(
            "asurine", "calcite", "crimsite", "deepslate", "diorite", "dripstone", "granite",
            "limestone", "ochrum", "scorchia", "scoria", "tuff", "veridium");
    private DnDecorLegacyMappingsGameTests() {
    }

    @GameTest(template = "empty")
    public static void remapsLegacyBlockAndItemNamespaces(GameTestHelper helper) {
        ResourceLocation oldId = ResourceLocation.fromNamespaceAndPath(
                DnDecorLegacyMappings.LEGACY_MOD_ID,
                "brass_floor"
        );
        ResourceLocation currentId = ResourceLocation.fromNamespaceAndPath(DnDecor.MOD_ID, "brass_floor");

        TrackingMapping<Block> blockMapping = mapping(ForgeRegistries.BLOCKS, oldId);
        DnDecorLegacyMappings.onMissingMappings(new MissingMappingsEvent(
                ForgeRegistries.Keys.BLOCKS,
                ForgeRegistries.BLOCKS,
                List.of(blockMapping)
        ));
        helper.assertTrue(blockMapping.target == ForgeRegistries.BLOCKS.getValue(currentId),
                "Legacy design_decor block did not remap to the matching dndecor block");

        TrackingMapping<Item> itemMapping = mapping(ForgeRegistries.ITEMS, oldId);
        DnDecorLegacyMappings.onMissingMappings(new MissingMappingsEvent(
                ForgeRegistries.Keys.ITEMS,
                ForgeRegistries.ITEMS,
                List.of(itemMapping)
        ));
        helper.assertTrue(itemMapping.target == ForgeRegistries.ITEMS.getValue(currentId),
                "Legacy design_decor item did not remap to the matching dndecor item");

        ResourceLocation removedId = ResourceLocation.fromNamespaceAndPath(
                DnDecorLegacyMappings.LEGACY_MOD_ID,
                "removed_legacy_test_block"
        );
        TrackingMapping<Block> removedMapping = mapping(ForgeRegistries.BLOCKS, removedId);
        DnDecorLegacyMappings.onMissingMappings(new MissingMappingsEvent(
                ForgeRegistries.Keys.BLOCKS,
                ForgeRegistries.BLOCKS,
                List.of(removedMapping)
        ));
        helper.assertTrue(removedMapping.target == null,
                "A removed legacy block must stay unresolved until a compatible target exists");

        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void registersAndRemapsEveryLegacySign(GameTestHelper helper) {
        helper.assertTrue(DnDecorBlocks.LEGACY_SIGNS.size() == 56,
                "Expected all 56 legacy sign registrations");

        Block sign = DnDecorBlocks.LEGACY_SIGNS.get(0).get();
        for (Direction attachment : List.of(Direction.UP, Direction.DOWN)) {
            for (Direction rotation : Direction.Plane.HORIZONTAL) {
                helper.assertTrue(sign.defaultBlockState()
                                .setValue(LegacySignBlock.FACING, attachment)
                                .setValue(LegacySignBlock.ROTATION, rotation)
                                .getValue(LegacySignBlock.ROTATION) == rotation,
                        "Vertical sign is missing rotation " + rotation + " for " + attachment);
            }
        }

        for (var entry : DnDecorBlocks.LEGACY_SIGNS) {
            ResourceLocation currentId = entry.getId();
            ResourceLocation oldId = ResourceLocation.fromNamespaceAndPath(
                    DnDecorLegacyMappings.LEGACY_MOD_ID, currentId.getPath());

            TrackingMapping<Block> blockMapping = mapping(ForgeRegistries.BLOCKS, oldId);
            DnDecorLegacyMappings.onMissingMappings(new MissingMappingsEvent(
                    ForgeRegistries.Keys.BLOCKS, ForgeRegistries.BLOCKS, List.of(blockMapping)));
            helper.assertTrue(blockMapping.target == entry.get(),
                    "Legacy sign block did not remap: " + oldId);

            TrackingMapping<Item> itemMapping = mapping(ForgeRegistries.ITEMS, oldId);
            DnDecorLegacyMappings.onMissingMappings(new MissingMappingsEvent(
                    ForgeRegistries.Keys.ITEMS, ForgeRegistries.ITEMS, List.of(itemMapping)));
            helper.assertTrue(itemMapping.target == entry.asItem(),
                    "Legacy sign item did not remap: " + oldId);
        }

        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void registersAndRemapsEveryLegacyMetalDecoration(GameTestHelper helper) {
        helper.assertTrue(DnDecorBlocks.LEGACY_METAL_DECORATIONS.size() == 26,
                "Expected all 26 legacy metal-decoration registrations listed in missingRegistries.md");
        for (var entry : DnDecorBlocks.LEGACY_METAL_DECORATIONS) {
            ResourceLocation currentId = entry.getId();
            ResourceLocation oldId = ResourceLocation.fromNamespaceAndPath(
                    DnDecorLegacyMappings.LEGACY_MOD_ID, currentId.getPath());
            helper.assertTrue(ForgeRegistries.BLOCKS.containsKey(currentId), "Missing block " + currentId);
            helper.assertTrue(ForgeRegistries.ITEMS.containsKey(currentId), "Missing item " + currentId);

            TrackingMapping<Block> blockMapping = mapping(ForgeRegistries.BLOCKS, oldId);
            DnDecorLegacyMappings.onMissingMappings(new MissingMappingsEvent(
                    ForgeRegistries.Keys.BLOCKS, ForgeRegistries.BLOCKS, List.of(blockMapping)));
            helper.assertTrue(blockMapping.target == entry.get(), "Legacy metal block did not remap: " + oldId);

            TrackingMapping<Item> itemMapping = mapping(ForgeRegistries.ITEMS, oldId);
            DnDecorLegacyMappings.onMissingMappings(new MissingMappingsEvent(
                    ForgeRegistries.Keys.ITEMS, ForgeRegistries.ITEMS, List.of(itemMapping)));
            helper.assertTrue(itemMapping.target == entry.asItem(), "Legacy metal item did not remap: " + oldId);
        }
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void registersLegacyStoneMachinesAndCreateRecipes(GameTestHelper helper) {
        for (String material : LEGACY_STONE_MACHINES) {
            for (String machine : List.of("crushing_wheel", "millstone")) {
                String path = material + "_" + machine;
                ResourceLocation currentId = ResourceLocation.fromNamespaceAndPath(DnDecor.MOD_ID, path);
                ResourceLocation oldId = ResourceLocation.fromNamespaceAndPath(DnDecorLegacyMappings.LEGACY_MOD_ID, path);
                helper.assertTrue(ForgeRegistries.BLOCKS.containsKey(currentId), "Missing stone machine block " + currentId);
                helper.assertTrue(ForgeRegistries.ITEMS.containsKey(currentId), "Missing stone machine item " + currentId);

                TrackingMapping<Block> mapping = mapping(ForgeRegistries.BLOCKS, oldId);
                DnDecorLegacyMappings.onMissingMappings(new MissingMappingsEvent(
                        ForgeRegistries.Keys.BLOCKS, ForgeRegistries.BLOCKS, List.of(mapping)));
                helper.assertTrue(mapping.target == ForgeRegistries.BLOCKS.getValue(currentId),
                        "Legacy stone machine did not remap: " + oldId);

                String folder = machine.equals("crushing_wheel") ? "crushing_wheels" : "millstones";
                ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(
                        DnDecor.MOD_ID, "item_application/" + folder + "/" + path);
                helper.assertTrue(helper.getLevel().getServer().getRecipeManager().byKey(recipeId).isPresent(),
                        "Missing Create item-application recipe " + recipeId);
            }
        }
        helper.succeed();
    }

    private static <T> TrackingMapping<T> mapping(IForgeRegistry<T> registry, ResourceLocation oldId) {
        return new TrackingMapping<>(registry, oldId);
    }

    private static final class TrackingMapping<T> extends MissingMappingsEvent.Mapping<T> {
        private T target;

        private TrackingMapping(IForgeRegistry<T> registry, ResourceLocation oldId) {
            super(registry, registry, oldId, -1);
        }

        @Override
        public void remap(T target) {
            super.remap(target);
            this.target = target;
        }
    }
}
