package dev.lopyluna.dndecor.register;

import com.simibubi.create.*;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.palettes.AllPaletteBlocks;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.decoration.palettes.ConnectedGlassPaneBlock;
import com.simibubi.create.content.decoration.palettes.WindowBlock;
import com.simibubi.create.content.kinetics.belt.BeltModel;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlock;
import com.simibubi.create.content.kinetics.flywheel.FlywheelBlock;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlock;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import com.simibubi.create.content.trains.display.FlapDisplayBlock;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.block.ItemUseOverrides;
import com.simibubi.create.foundation.block.connected.*;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.lopyluna.dndecor.DnDecor;
import dev.lopyluna.dndecor.DnDecorBlockStateGen;
import dev.lopyluna.dndecor.content.blocks.*;
import dev.lopyluna.dndecor.content.blocks.beam.BeamBlock;
import dev.lopyluna.dndecor.content.blocks.beam.BeamCTBehaviour;
import dev.lopyluna.dndecor.content.blocks.cogs.DnDCogWheelBlock;
import dev.lopyluna.dndecor.content.blocks.cogs.DnDCogwheelBlockItem;
import dev.lopyluna.dndecor.content.blocks.diagonal_girder.DiagonalGirderBlock;
import dev.lopyluna.dndecor.content.blocks.diagonal_girder.DiagonalGirderGenerator;
import dev.lopyluna.dndecor.content.blocks.flywheel.FlywheelTypeBlock;
import dev.lopyluna.dndecor.content.blocks.frontlight.Frontlight;
import dev.lopyluna.dndecor.content.blocks.frontlight.FrontlightBlock;

import dev.lopyluna.dndecor.content.blocks.metal_supports.DiagonalMetalSupportBlock;
import dev.lopyluna.dndecor.content.blocks.metal_supports.DiagonalMetalSupportCtBehavior;
import dev.lopyluna.dndecor.content.blocks.metal_supports.MetalSupportBlock;
import dev.lopyluna.dndecor.content.blocks.stepped_lever.SteppedLeverBlock;
import dev.lopyluna.dndecor.content.blocks.storage_container.ColoredStorageContainerBlock;
import dev.lopyluna.dndecor.content.blocks.storage_container.ColoredStorageContainerCTBehaviour;
import dev.lopyluna.dndecor.content.configs.server.kinetics.DStress;
import dev.lopyluna.dndecor.content.entries.BoltEntry;
import dev.lopyluna.dndecor.register.client.DnDecorPartialModels;
import dev.lopyluna.dndecor.register.client.DnDecorSpriteShifts;
import dev.lopyluna.dndecor.register.helpers.list_providers.MaterialTypeProvider;
import dev.lopyluna.dndecor.register.helpers.list_providers.MetalTypeBlockList;
import dev.lopyluna.dndecor.register.helpers.list_providers.MetalTypeBoltBlockList;
import dev.lopyluna.dndecor.register.helpers.list_providers.StoneTypeBlockList;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;


import java.util.function.Function;
import java.util.ArrayList;
import java.util.List;

import static com.simibubi.create.api.behaviour.display.DisplaySource.displaySource;
import static com.simibubi.create.api.behaviour.display.DisplayTarget.displayTarget;
import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;
import static dev.lopyluna.dndecor.DnDecor.REGISTRATE;
import static dev.lopyluna.dndecor.register.DnDecorTags.commonItemTag;
import static dev.lopyluna.dndecor.register.DnDecorTags.optionalTag;
import static dev.lopyluna.dndecor.register.helpers.BlockTransgender.*;

@SuppressWarnings({"removal", "deprecation", "SameParameterValue", "unused"})
public class DnDecorBlocks {

    private static void emptyLoot(RegistrateBlockLootTables tables, Block block) {
        tables.add(block, LootTable.lootTable());
    }

    private static final String[][] LEGACY_LARGE_CHAINS = {
            {"aluminium", "Aluminium"},
            {"andesite", "Andesite"},
            {"brass", "Brass"},
            {"bronze", "Bronze"},
            {"cast_iron", "Cast Iron"},
            {"cobalt", "Cobalt"},
            {"copper", "Copper"},
            {"electrum", "Electrum"},
            {"gold", "Gold"},
            {"hepatizon", "Hepatizon"},
            {"industrial_iron", "Industrial Iron"},
            {"invar", "Invar"},
            {"iron", "Iron"},
            {"knightslime", "Knightslime"},
            {"lead", "Lead"},
            {"manyullyn", "Manyullyn"},
            {"mithril", "Mithril"},
            {"netherite", "Netherite"},
            {"nethersteel", "Nethersteel"},
            {"nickel", "Nickel"},
            {"pig_iron", "Pig Iron"},
            {"queen_slime", "Queen Slime"},
            {"rose_gold", "Rose Gold"},
            {"silver", "Silver"},
            {"steel", "Steel"},
            {"strong_bronze", "Strong Bronze"},
            {"tin", "Tin"},
            {"zinc", "Zinc"}
    };

    /** Original large-chain registry paths retained for world and item compatibility. */
    public static final List<BlockEntry<LargeChain>> LEGACY_LARGE_CHAIN_BLOCKS = registerLegacyLargeChains();

    private static List<BlockEntry<LargeChain>> registerLegacyLargeChains() {
        List<BlockEntry<LargeChain>> chains = new ArrayList<>(LEGACY_LARGE_CHAINS.length);
        for (String[] chain : LEGACY_LARGE_CHAINS)
            chains.add(registerLegacyLargeChain(chain[0], chain[1]));
        return List.copyOf(chains);
    }

    private static BlockEntry<LargeChain> registerLegacyLargeChain(String metal, String displayName) {
        String id = metal + "_large_chain";
        String chainTexture = metal + "_large_chain";
        var builder = REGISTRATE.block(id, LargeChain::new)
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.sound(DnDecorSoundTypes.CHAIN_HEAVY))
                .addLayer(() -> RenderType::cutout)
                .transform(pickaxeOnly())
                .lang("Large " + displayName + " Chain")
                .blockstate((c, p) -> {
                    p.models().withExistingParent("block/" + c.getName(), DnDecor.asResource("block/large_chain"))
                            .texture("0", DnDecor.asResource("block/" + chainTexture));
                    p.models().withExistingParent("block/" + c.getName() + "/block", DnDecor.asResource("block/large_chain"))
                            .texture("0", DnDecor.asResource("block/" + chainTexture));
                    p.models().withExistingParent("block/" + c.getName() + "/item", DnDecor.asResource("block/large_chain"))
                            .texture("0", DnDecor.asResource("block/" + chainTexture));
                    BlockStateGen.axisBlock(c, p, getBlockModel(true, c, p));
                })
                .tag(AllTags.AllBlockTags.BRITTLE.tag, BlockTags.CLIMBABLE)
                .loot((tables, block) -> tables.dropSelf(block));

        if (metal.equals("netherite"))
            builder = builder.item().properties(Item.Properties::fireResistant).build();
        else
            builder = builder.simpleItem();
        return builder.register();
    }

    /** All original wallpaper paths retained for world and item compatibility. */
    public static final List<BlockEntry<Block>> LEGACY_WALLPAPERS = registerLegacyWallpapers();

    private static List<BlockEntry<Block>> registerLegacyWallpapers() {
        List<BlockEntry<Block>> wallpapers = new ArrayList<>(DyeColor.values().length * 3);
        for (DyeColor color : DyeColor.values()) {
            String colorId = color.getName();
            String colorName = switch (color) {
                case LIGHT_BLUE -> "Light Blue";
                case LIGHT_GRAY -> "Light Gray";
                default -> Character.toUpperCase(colorId.charAt(0)) + colorId.substring(1);
            };
            wallpapers.add(registerLegacyWallpaper("wallpaper_arrow_" + colorId,
                    colorName + " Arrow Wallpaper"));
            wallpapers.add(registerLegacyWallpaper("wallpaper_striped_" + colorId,
                    colorName + " Striped Wallpaper"));
            wallpapers.add(registerLegacyWallpaper(colorId + "_wallpaper_wavy",
                    colorName + " Wavy Wallpaper"));
        }
        return List.copyOf(wallpapers);
    }

    private static BlockEntry<Block> registerLegacyWallpaper(String id, String displayName) {
        return REGISTRATE.block(id, Block::new)
                .initialProperties(SharedProperties::wooden)
                .transform(axeOrPickaxe())
                .lang(displayName)
                .simpleItem()
                .register();
    }

    private static final String[][] LEGACY_CASTEL_MATERIALS = {
            {"andesite", "Andesite"}, {"asurine", "Asurine"}, {"calcite", "Calcite"},
            {"crimsite", "Crimsite"}, {"deepslate", "Deepslate"}, {"diorite", "Diorite"},
            {"dripstone", "Dripstone"}, {"granite", "Granite"}, {"limestone", "Limestone"},
            {"ochrum", "Ochrum"}, {"scorchia", "Scorchia"}, {"scoria", "Scoria"},
            {"tuff", "Tuff"}, {"veridium", "Veridium"}
    };

    /** All 112 original "castel" paths retained with the historical spelling. */
    public static final List<BlockEntry<?>> LEGACY_CASTEL_BLOCKS = registerLegacyCastelBlocks();

    private static List<BlockEntry<?>> registerLegacyCastelBlocks() {
        List<BlockEntry<?>> blocks = new ArrayList<>(LEGACY_CASTEL_MATERIALS.length * 8);
        for (String[] material : LEGACY_CASTEL_MATERIALS) {
            String id = material[0];
            String name = material[1];
            Block source = legacyCastelSource(id);
            blocks.addAll(registerLegacyCastelFamily(id, name, "brick", "Bricks", source));
            blocks.addAll(registerLegacyCastelFamily(id, name, "tile", "Tiles", source));
        }
        return List.copyOf(blocks);
    }

    private static Block legacyCastelSource(String material) {
        return switch (material) {
            case "andesite" -> Blocks.ANDESITE;
            case "calcite", "ochrum" -> Blocks.CALCITE;
            case "diorite" -> Blocks.DIORITE;
            case "dripstone" -> Blocks.DRIPSTONE_BLOCK;
            case "granite" -> Blocks.GRANITE;
            case "limestone" -> Blocks.SANDSTONE;
            case "scoria", "scorchia" -> Blocks.BLACKSTONE;
            case "tuff", "veridium" -> Blocks.TUFF;
            default -> Blocks.DEEPSLATE;
        };
    }

    private static List<BlockEntry<?>> registerLegacyCastelFamily(String material, String displayName,
                                                                   String pattern, String plural,
                                                                   Block source) {
        String texture = material + "_castel_" + pattern + "s";
        String baseId = texture;
        String singular = pattern.equals("brick") ? "Brick" : "Tile";

        BlockEntry<Block> base = REGISTRATE.block(baseId, Block::new)
                .initialProperties(() -> source)
                .properties(p -> p.destroyTime(1.25f))
                .transform(pickaxeOnly())
                .lang(displayName + " Castel " + plural)
                .loot(RegistrateBlockLootTables::dropSelf)
                .simpleItem()
                .register();

        BlockEntry<SlabBlock> slab = REGISTRATE.block(material + "_castel_" + pattern + "_slab", SlabBlock::new)
                .initialProperties(() -> source)
                .properties(p -> p.destroyTime(1f))
                .transform(pickaxeOnly())
                .tag(BlockTags.SLABS)
                .lang(displayName + " Castel " + singular + " Slab")
                .blockstate((c, p) -> p.slabBlock(c.get(), DnDecor.asResource("block/" + texture),
                        DnDecor.asResource("block/" + texture)))
                .loot(RegistrateBlockLootTables::dropSelf)
                .simpleItem()
                .register();

        BlockEntry<StairBlock> stairs = REGISTRATE.block(material + "_castel_" + pattern + "_stairs",
                        p -> new StairBlock(base.getDefaultState(), p))
                .initialProperties(() -> source)
                .properties(p -> p.destroyTime(1.25f))
                .transform(pickaxeOnly())
                .tag(BlockTags.STAIRS)
                .lang(displayName + " Castel " + singular + " Stairs")
                .blockstate((c, p) -> p.stairsBlock(c.get(),
                        DnDecor.asResource("block/stairs/" + texture)))
                .loot(RegistrateBlockLootTables::dropSelf)
                .simpleItem()
                .register();

        BlockEntry<WallBlock> wall = REGISTRATE.block(material + "_castel_" + pattern + "_wall", WallBlock::new)
                .initialProperties(() -> source)
                .properties(p -> p.destroyTime(1.25f))
                .transform(pickaxeOnly())
                .tag(BlockTags.WALLS)
                .lang(displayName + " Castel " + singular + " Wall")
                .blockstate((c, p) -> p.wallBlock(c.get(),
                        DnDecor.asResource("block/walls/" + texture)))
                .loot(RegistrateBlockLootTables::dropSelf)
                .item()
                .model((c, p) -> p.wallInventory(c.getName(),
                        DnDecor.asResource("block/walls/" + texture)))
                .build()
                .register();

        return List.of(base, slab, stairs, wall);
    }

    private static final String[][] LEGACY_BOILERS = {
            {"brass_boiler", "brass_boiler", "Brass Boiler"},
            {"aluminum_boiler", "aluminium_boiler", "Aluminium Boiler"},
            {"aluminum_boiler_special", "aluminium_boiler_special", "Aluminium Boiler"},
            {"gold_boiler", "gold_boiler", "Gold Boiler"},
            {"copper_boiler", "copper_boiler", "Copper Boiler"},
            {"zinc_boiler", "zinc_boiler", "Zinc Boiler"},
            {"industrial_iron_boiler", "industrial_iron_boiler", "Industrial Iron Boiler"},
            {"andesite_boiler", "andesite_boiler", "Andesite Boiler"},
            {"cast_iron_boiler", "cast_iron_boiler", "Cast Iron Boiler"},
            {"capitalism_boiler", "capitalism_boiler", "Capitalism Boiler"}
    };

    /** Original boiler registry entries retained for world and item compatibility. */
    public static final List<BlockEntry<LegacyBoilerBlock>> LEGACY_BOILER_BLOCKS = registerLegacyBoilers();

    private static final String[][] LEGACY_LARGE_BOILERS = {
            {"aluminium", "Aluminium"},
            {"andesite", "Andesite"},
            {"brass", "Brass"},
            {"capitalism", "Capitalism"},
            {"cast_iron", "Cast Iron"},
            {"copper", "Copper"},
            {"gold", "Gold"},
            {"industrial_iron", "Industrial Iron"},
            {"zinc", "Zinc"}
    };

    public static final List<BlockEntry<LegacyLargeBoilerBlock>> LEGACY_LARGE_BOILER_BLOCKS =
            registerLegacyLargeBoilers();
    public static final List<BlockEntry<LegacyBoilerStructureBlock>> LEGACY_BOILER_STRUCTURE_BLOCKS =
            registerLegacyBoilerStructures();

    private static List<BlockEntry<LegacyBoilerBlock>> registerLegacyBoilers() {
        List<BlockEntry<LegacyBoilerBlock>> boilers = new ArrayList<>(LEGACY_BOILERS.length);
        for (String[] boiler : LEGACY_BOILERS)
            boilers.add(registerLegacyBoiler(boiler[0], boiler[1], boiler[2]));
        return List.copyOf(boilers);
    }

    private static BlockEntry<LegacyBoilerBlock> registerLegacyBoiler(String id, String texture,
                                                                       String displayName) {
        return REGISTRATE.block(id, LegacyBoilerBlock::new)
                .initialProperties(SharedProperties::copperMetal)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .transform(pickaxeOnly())
                .lang(displayName)
                .addLayer(() -> RenderType::cutoutMipped)
                // The original 0.4.0b boiler uses Forge's OBJ loader. Its blockstate
                // and model chain are preserved as static assets instead of being
                // replaced by Registrate's default cube model during data generation.
                .blockstate((c, p) -> {})
                .loot((tables, block) -> tables.dropSelf(block))
                .item()
                .model((c, p) -> {})
                .build()
                .register();
    }

    private static List<BlockEntry<LegacyLargeBoilerBlock>> registerLegacyLargeBoilers() {
        List<BlockEntry<LegacyLargeBoilerBlock>> boilers = new ArrayList<>(LEGACY_LARGE_BOILERS.length);
        for (String[] boiler : LEGACY_LARGE_BOILERS) {
            String id = boiler[0] + "_boiler_large";
            boilers.add(REGISTRATE.block(id, LegacyLargeBoilerBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .lang("Large " + boiler[1] + " Boiler")
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate((c, p) -> {})
                    .loot((tables, block) -> tables.add(block, LootTable.lootTable()))
                    .item()
                    .model((c, p) -> {})
                    .build()
                    .register());
        }
        return List.copyOf(boilers);
    }

    private static List<BlockEntry<LegacyBoilerStructureBlock>> registerLegacyBoilerStructures() {
        List<BlockEntry<LegacyBoilerStructureBlock>> structures = new ArrayList<>(LEGACY_LARGE_BOILERS.length);
        for (String[] boiler : LEGACY_LARGE_BOILERS) {
            String id = boiler[0] + "_boiler_structure";
            structures.add(REGISTRATE.block(id, LegacyBoilerStructureBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(BlockBehaviour.Properties::noOcclusion)
                    .transform(pickaxeOnly())
                    .lang("Large " + boiler[1] + " Boiler")
                    .blockstate((c, p) -> {})
                    .loot((tables, block) -> tables.add(block, LootTable.lootTable()))
                    .register());
        }
        return List.copyOf(structures);
    }

    // Standalone registry entries retained for 0.4.0b world compatibility.
    public static final BlockEntry<LeverBlock> BREAKER_SWITCH = REGISTRATE.block("breaker_switch", LeverBlock::new)
            .initialProperties(() -> Blocks.LEVER).lang("Breaker Switch")
            .blockstate((c, p) -> {}).loot(DnDecorBlocks::emptyLoot)
            .item().model((c, p) -> {}).build().register();
    public static final BlockEntry<Block> CAPITALISM_BLOCK = legacySimple("capitalism_block", "Block of Capitalism");

    /** The complete 0.4.0b Metal Decorations registry set, with its original state schemas. */
    public static final List<BlockEntry<?>> LEGACY_METAL_DECORATIONS = registerLegacyMetalDecorations();

    private static List<BlockEntry<?>> registerLegacyMetalDecorations() {
        List<BlockEntry<?>> blocks = new ArrayList<>(26);
        blocks.add(legacyMetal("andesite_floodlight", "Andesite Floodlight", LegacyMetalDecorationBlocks.Floodlight::new, true));
        blocks.add(legacyMetal("brass_floodlight", "Brass Floodlight", LegacyMetalDecorationBlocks.Floodlight::new, true));
        blocks.add(legacyMetal("copper_floodlight", "Copper Floodlight", LegacyMetalDecorationBlocks.Floodlight::new, true));
        for (String metal : List.of("brass", "copper", "iron", "zinc"))
            blocks.add(legacyMetal(metal + "_catwalk", title(metal) + " Catwalk", LegacyMetalDecorationBlocks.Catwalk::new, true));
        for (String metal : List.of("brass", "copper", "zinc"))
            blocks.add(legacyMetal(metal + "_lamp", title(metal) + " Lamp", LegacyMetalDecorationBlocks.Lamp::new, true));
        for (String metal : List.of("brass", "copper", "zinc"))
            blocks.add(legacyMetal(metal + "_light", title(metal) + " Light", Block::new, false));
        for (String metal : List.of("brass", "copper", "iron", "zinc"))
            blocks.add(legacyMetal(metal + "_railing", title(metal) + " Railing", LegacyMetalDecorationBlocks.Railing::new, true));
        for (String metal : List.of("brass", "copper", "zinc"))
            blocks.add(legacyMetal(metal + "_screw", title(metal) + " Screw", LegacyMetalDecorationBlocks.Screw::new, true));
        for (String metal : List.of("brass", "copper", "zinc"))
            blocks.add(legacyMetal(metal + "_bolt", title(metal) + " Bolt", LegacyMetalDecorationBlocks.Screw::new, true));
        blocks.add(legacyMetal("blue_container", "Blue Container", LegacyMetalDecorationBlocks.Container::new, false));
        blocks.add(legacyMetal("green_container", "Green Container", LegacyMetalDecorationBlocks.Container::new, false));
        blocks.add(legacyMetal("red_container", "Red Container", LegacyMetalDecorationBlocks.Container::new, false));
        return List.copyOf(blocks);
    }

    private static String title(String value) {
        return Character.toUpperCase(value.charAt(0)) + value.substring(1).replace('_', ' ');
    }

    private static <T extends Block> BlockEntry<T> legacyMetal(String id, String name,
            NonNullFunction<BlockBehaviour.Properties, T> factory, boolean cutout) {
        var builder = REGISTRATE.block(id, factory)
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion().lightLevel(state -> id.endsWith("_light") || id.endsWith("_lamp") ? 15
                        : id.endsWith("_floodlight") && state.getValue(LegacyMetalDecorationBlocks.Floodlight.TURNED_ON) ? 15 : 0))
                .transform(pickaxeOnly()).lang(name)
                .blockstate((c, p) -> {})
                .loot((tables, block) -> tables.dropSelf(block));
        if (cutout) builder = builder.addLayer(() -> RenderType::cutoutMipped);
        // Legacy catwalks use their preserved static model chain. Registering Create's
        // connected-texture callbacks here leaves callbacks pending while Create's own
        // registrate instance receives its first registry event, which is fatal in dev.
        return builder.item().model((c, p) -> {}).build().register();
    }
    public static final BlockEntry<LegacyDirectionalBlock> CARDBOARD_BOX = REGISTRATE.block("cardboard_box", LegacyDirectionalBlock::new)
            .initialProperties(() -> Blocks.OAK_PLANKS).lang("Cardboard Box")
            .blockstate((c, p) -> {}).loot(DnDecorBlocks::emptyLoot)
            .item().model((c, p) -> {}).build().register();
    public static final BlockEntry<LegacyCeilingFanBlock> CEILING_FAN = REGISTRATE
            .block("ceiling_fan", LegacyCeilingFanBlock::new)
            .initialProperties(SharedProperties::softMetal).lang("Ceiling Fan")
            .addLayer(() -> RenderType::cutout)
            .blockstate((c, p) -> {}).loot(DnDecorBlocks::emptyLoot)
            .item().model((c, p) -> {}).build().register();
    public static final BlockEntry<LegacyShapedBlock> COPPER_GAS_TANK = legacyShaped("copper_gas_tank", "Compact Fluid Tank", Block.box(1, 0, 1, 15, 16, 15), true);
    public static final BlockEntry<LegacyCrushingWheelControllerBlock> CRUSHING_WHEEL_CONTROLLER = REGISTRATE
            .block("crushing_wheel_controller", LegacyCrushingWheelControllerBlock::new)
            .initialProperties(SharedProperties::stone).blockstate((c, p) -> {}).loot(DnDecorBlocks::emptyLoot).register();
    public static final BlockEntry<LegacyShapedBlock> GAS_TANK = legacyShaped("gas_tank", "Compact Iron Fluid Tank", Block.box(1, 0, 1, 15, 16, 15), true);
    public static final BlockEntry<Block> HORIZONTAL_TINTED_FRAMED_GLASS = legacyGlass("horizontal_tinted_framed_glass", "Horizontal Tinted Framed Glass");
    public static final BlockEntry<LegacyIndustrialGearBlock> INDUSTRIAL_GEAR = legacyGear("industrial_gear", "Industrial Gear", false);
    public static final BlockEntry<LegacyIndustrialGearBlock> INDUSTRIAL_GEAR_LARGE = legacyGear("industrial_gear_large", "Large Industrial Gear", true);
    public static final BlockEntry<Block> INDUSTRIAL_GOLD_BLOCK = legacySimple("industrial_gold_block", "Block of Industrial Gold");
    public static final BlockEntry<Block> INDUSTRIAL_GOLD_FLOOR = legacySimple("industrial_gold_floor", "Industrial Gold Floor");
    public static final BlockEntry<Block> INDUSTRIAL_IRON_FLOOR = legacySimple("industrial_iron_floor", "Industrial Iron Floor");
    public static final BlockEntry<Block> METAL_PLATE = legacySimple("metal_plate", "Metal Plate");
    public static final BlockEntry<SlabBlock> METAL_PLATE_SLAB = legacySlab("metal_plate_slab", "Metal Plate Slab");
    public static final BlockEntry<StairBlock> METAL_PLATE_STAIRS = legacyStairs("metal_plate_stairs", "Metal Plate Stairs", METAL_PLATE);
    public static final BlockEntry<WallBlock> METAL_PLATE_WALL = legacyWall("metal_plate_wall", "Metal Plate Wall");
    public static final BlockEntry<Block> METAL_SHEET = legacySimple("metal_sheet", "Metal Sheet");
    public static final BlockEntry<SlabBlock> METAL_SHEET_SLAB = legacySlab("metal_sheet_slab", "Metal Sheet Slab");
    public static final BlockEntry<StairBlock> METAL_SHEET_STAIRS = legacyStairs("metal_sheet_stairs", "Metal Sheet Stairs", METAL_SHEET);
    public static final BlockEntry<WallBlock> METAL_SHEET_WALL = legacyWall("metal_sheet_wall", "Metal Sheet Wall");
    /** The 128 dyed metal plate/sheet entries from the original release. */
    public static final List<BlockEntry<?>> LEGACY_COLORED_METAL_BLOCKS = registerLegacyColoredMetalBlocks();
    public static final BlockEntry<Block> RED_STONE_TILES = legacySimple("red_stone_tiles", "Red Deepslate Tiles");
    public static final BlockEntry<Block> STONE_TILES = legacySimple("stone_tiles", "Deepslate Tiles");
    public static final BlockEntry<Block> TINTED_FRAMED_GLASS = legacyGlass("tinted_framed_glass", "Tinted Framed Glass");
    public static final BlockEntry<Block> VERTICAL_TINTED_FRAMED_GLASS = legacyGlass("vertical_tinted_framed_glass", "Vertical Tinted Framed Glass");
    public static final BlockEntry<LegacyShapedBlock> WOOD_SUPPORT = REGISTRATE.block("wood_support",
                    p -> new LegacyShapedBlock(p, Block.box(4, 0, 4, 12, 16, 12)))
            .initialProperties(() -> Blocks.OAK_PLANKS).lang("Wooden Support")
            .blockstate((c, p) -> {}).loot(DnDecorBlocks::emptyLoot)
            .item().model((c, p) -> {}).build().register();

    private static BlockEntry<Block> legacySimple(String id, String name) {
        return REGISTRATE.block(id, Block::new).initialProperties(SharedProperties::softMetal).lang(name)
                .blockstate((c, p) -> {}).loot(DnDecorBlocks::emptyLoot)
                .item().model((c, p) -> {}).build().register();
    }

    private static BlockEntry<Block> legacyGlass(String id, String name) {
        return REGISTRATE.block(id, Block::new).initialProperties(() -> Blocks.TINTED_GLASS)
                .properties(BlockBehaviour.Properties::noOcclusion).addLayer(() -> RenderType::translucent).lang(name)
                .blockstate((c, p) -> {}).loot(DnDecorBlocks::emptyLoot)
                .item().model((c, p) -> {}).build().register();
    }

    private static BlockEntry<LegacyIndustrialGearBlock> legacyGear(String id, String name, boolean large) {
        return REGISTRATE.block(id, p -> new LegacyIndustrialGearBlock(p, large)).initialProperties(SharedProperties::softMetal).lang(name)
                .blockstate((c, p) -> {}).loot(DnDecorBlocks::emptyLoot)
                .item().model((c, p) -> {}).build().register();
    }

    private static BlockEntry<LegacyShapedBlock> legacyShaped(String id, String name,
                                                               net.minecraft.world.phys.shapes.VoxelShape shape,
                                                               boolean cutout) {
        var builder = REGISTRATE.block(id, p -> new LegacyShapedBlock(p, shape))
                .initialProperties(SharedProperties::softMetal).lang(name)
                .blockstate((c, p) -> {}).loot(DnDecorBlocks::emptyLoot);
        if (cutout)
            builder = builder.addLayer(() -> RenderType::cutout);
        return builder.item().model((c, p) -> {}).build().register();
    }

    private static BlockEntry<SlabBlock> legacySlab(String id, String name) {
        return REGISTRATE.block(id, SlabBlock::new).initialProperties(SharedProperties::softMetal).lang(name)
                .blockstate((c, p) -> {}).loot(DnDecorBlocks::emptyLoot)
                .item().model((c, p) -> {}).build().register();
    }

    private static BlockEntry<StairBlock> legacyStairs(String id, String name, BlockEntry<Block> base) {
        return REGISTRATE.block(id, p -> new StairBlock(base.getDefaultState(), p))
                .initialProperties(SharedProperties::softMetal).lang(name)
                .blockstate((c, p) -> {}).loot(DnDecorBlocks::emptyLoot)
                .item().model((c, p) -> {}).build().register();
    }

    private static BlockEntry<WallBlock> legacyWall(String id, String name) {
        return REGISTRATE.block(id, WallBlock::new).initialProperties(SharedProperties::softMetal).lang(name)
                .blockstate((c, p) -> {}).loot(DnDecorBlocks::emptyLoot)
                .item().model((c, p) -> {}).build().register();
    }

    private static List<BlockEntry<?>> registerLegacyColoredMetalBlocks() {
        List<BlockEntry<?>> blocks = new ArrayList<>(DyeColor.values().length * 8);
        for (DyeColor color : DyeColor.values()) {
            registerLegacyColoredMetalFamily(blocks, color, "metal_plate", "Metal Plate");
            registerLegacyColoredMetalFamily(blocks, color, "metal_sheet", "Metal Sheet");
        }
        return List.copyOf(blocks);
    }

    private static void registerLegacyColoredMetalFamily(List<BlockEntry<?>> blocks, DyeColor color,
                                                           String pattern, String displayName) {
        String colorName = color.getSerializedName();
        String id = colorName + "_" + pattern;
        String name = title(colorName) + " " + displayName;
        ResourceLocation texture = DnDecor.asResource("block/old/" + id);

        BlockEntry<Block> base = REGISTRATE.block(id, Block::new)
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.mapColor(color.getMapColor()))
                .transform(pickaxeOnly())
                .lang(name)
                .blockstate((c, p) -> p.simpleBlock(c.get(), p.models().cubeAll(c.getName(), texture)))
                .loot((tables, block) -> tables.dropSelf(block))
                .simpleItem()
                .register();
        blocks.add(base);

        BlockEntry<SlabBlock> slab = REGISTRATE.block(id + "_slab", SlabBlock::new)
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.mapColor(color.getMapColor()))
                .transform(pickaxeOnly())
                .lang(name + " Slab")
                .blockstate((c, p) -> p.slabBlock(c.get(), base.getId(), texture, texture, texture))
                .loot((tables, block) -> tables.add(block, tables.createSlabItemTable(block)))
                .simpleItem()
                .register();
        blocks.add(slab);

        BlockEntry<StairBlock> stairs = REGISTRATE.block(id + "_stairs",
                        p -> new StairBlock(base.getDefaultState(), p))
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.mapColor(color.getMapColor()))
                .transform(pickaxeOnly())
                .lang(name + " Stairs")
                .blockstate((c, p) -> p.stairsBlock(c.get(), texture))
                .loot((tables, block) -> tables.dropSelf(block))
                .simpleItem()
                .register();
        blocks.add(stairs);

        BlockEntry<WallBlock> wall = REGISTRATE.block(id + "_wall", WallBlock::new)
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.mapColor(color.getMapColor()).forceSolidOn())
                .transform(pickaxeOnly())
                .lang(name + " Wall")
                .blockstate((c, p) -> p.wallBlock(c.get(), c.getName(), texture))
                .loot((tables, block) -> tables.dropSelf(block))
                .item()
                .model((c, p) -> p.wallInventory(c.getName(), texture))
                .build()
                .register();
        blocks.add(wall);
    }

    public static final TagKey<Item> SIGNS = DnDecorTags.modItemTag("signs");
    public static final TagKey<Item> LETTER_SIGNS = DnDecorTags.modItemTag("letter_signs");

    private static final String[] LEGACY_LETTER_SIGNS = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    };
    private static final String[][] LEGACY_PICTOGRAM_SIGNS = {
            {"moyai", "moyai", "Moyai Sign"},
            {"warning", "warning", "Warning Sign"},
            {"arrow_up", "up", "Arrow Up Sign"},
            {"tap", "tap", "Tap Sign"},
            {"stop", "stop", "Stop Sign"},
            {"arrow_right", "right", "Arrow Right Sign"},
            {"arrow_left", "left", "Arrow Left Sign"},
            {"glitch_warning", "glitch_warning", "Glitch Warning Sign"},
            {"broken_wrench", "broken_wrench", "Broken Wrench Sign"},
            {"biohazard", "biohazard", "Biohazard Sign"},
            {"capitalism_warning", "capitalism_warning", "Capitalism Warning Sign"},
            {"arrow_down", "down", "Arrow Down Sign"},
            {"gear", "gear", "Gear Sign"},
            {"creeper", "creeper", "Creeper Sign"},
            {"bun", "bun", "Bun Sign"},
            {"silly", "silly", "Silly Sign"},
            {"american", "american", "Oil Sign"},
            {"magnet", "magnet", "Magnet Sign"},
            {"blank", "blank", "Blank Sign"}
    };

    /** All 56 block entries retained so compatibility tests and integrations can enumerate them. */
    public static final List<BlockEntry<LegacySignBlock>> LEGACY_SIGNS = registerLegacySigns();

    private static List<BlockEntry<LegacySignBlock>> registerLegacySigns() {
        List<BlockEntry<LegacySignBlock>> signs = new ArrayList<>(56);
        for (String symbol : LEGACY_LETTER_SIGNS)
            signs.add(registerLegacySign(symbol + "_sign", "old/letter_signs/" + symbol,
                    symbol.toUpperCase() + " Letter Sign", LETTER_SIGNS));
        signs.add(registerLegacySign("letter_sign", "old/letter_signs/blank", "Blank Letter Sign", LETTER_SIGNS));
        for (String[] sign : LEGACY_PICTOGRAM_SIGNS)
            signs.add(registerLegacySign(sign[0] + "_sign", "old/signs/" + sign[1], sign[2], SIGNS));
        return List.copyOf(signs);
    }

    private static BlockEntry<LegacySignBlock> registerLegacySign(String id, String texture,
                                                                   String displayName, TagKey<Item> tag) {
        return REGISTRATE.block(id, LegacySignBlock::new)
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion())
                .transform(axeOrPickaxe())
                .lang(displayName)
                .blockstate((c, p) -> {
                    ModelFile model = p.models().withExistingParent("block/" + c.getName() + "/block",
                                    DnDecor.asResource("block/legacy_sign"))
                            .texture("0", DnDecor.asResource("block/" + texture))
                            .texture("particle", DnDecor.asResource("block/" + texture));
                    p.getVariantBuilder(c.get()).forAllStates(state -> {
                        Direction facing = state.getValue(DirectionalBlock.FACING);
                        Direction rotation = state.getValue(LegacySignBlock.ROTATION);
                        int xRotation;
                        int yRotation;
                        if (facing == Direction.DOWN) {
                            xRotation = 180;
                            yRotation = rotation.toYRot() == 0 ? 0 : (int) rotation.toYRot();
                        } else if (facing == Direction.UP) {
                            xRotation = 0;
                            yRotation = rotation.toYRot() == 0 ? 0 : (int) rotation.toYRot();
                        } else {
                            xRotation = 90;
                            // FACING points toward the supporting wall. The base model
                            // lies against the top edge, so its wall rotation differs
                            // from Direction#toYRot (which describes entity yaw).
                            yRotation = switch (facing) {
                                case EAST -> 90;
                                case SOUTH -> 180;
                                case WEST -> 270;
                                default -> 0;
                            };
                        }
                        return ConfiguredModel.builder()
                                .modelFile(model)
                                .rotationX(xRotation)
                                .rotationY(yRotation)
                                .build();
                    });
                })
                .recipe((c, p) -> {
                    p.stonecutting(DataIngredient.tag(tag), RecipeCategory.DECORATIONS, c, 1);
                    if (id.equals("blank_sign"))
                        p.stonecutting(DataIngredient.tag(commonItemTag("nuggets/zinc")),
                                RecipeCategory.DECORATIONS, c, 1);
                    if (id.equals("letter_sign"))
                        p.stonecutting(DataIngredient.tag(commonItemTag("nuggets/brass")),
                                RecipeCategory.DECORATIONS, c, 1);
                })
                .loot((tables, block) -> tables.dropSelf(block))
                .item()
                .tag(tag)
                .model((c, p) -> p.generated(c, DnDecor.asResource("block/" + texture)))
                .build()
                .register();
    }

    public static TagKey<Item> darkMetalDecorTag = optionalTag(BuiltInRegistries.ITEM, DnDecor.asResource("dark_metal_decor"));



    public static final BlockEntry<Block> DEEPSLATE_TILES = REGISTRATE.block("deepslate_tiles",Block::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .recipe((c, p) -> {
                p.stonecutting(DataIngredient.items(Blocks.COBBLED_DEEPSLATE), RecipeCategory.BUILDING_BLOCKS, c, 1);
            })
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(DnDecorSpriteShifts.STONE_TILES)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.makeCasing(block, DnDecorSpriteShifts.STONE_TILES)))
            .simpleItem()
            .register();



    public static final BlockEntry<Block> RED_DEEPSLATE_TILES = REGISTRATE.block("red_deepslate_tiles",Block::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .recipe((c, p) -> {
                p.stonecutting(DataIngredient.items(Blocks.COBBLED_DEEPSLATE), RecipeCategory.BUILDING_BLOCKS, c, 1);
            })
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(DnDecorSpriteShifts.RED_STONE_TILES)))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.makeCasing(block, DnDecorSpriteShifts.RED_STONE_TILES)))
            .simpleItem()
            .register();

    public static final BlockEntry<SteppedLeverBlock> STEPPED_LEVER = REGISTRATE.block("stepped_lever", SteppedLeverBlock::new)
            .initialProperties(() -> Blocks.LEVER)
            .transform(axeOrPickaxe())
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .recipe((c, p) ->
                    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 1)
                            .pattern(" L ")
                            .pattern(" B ")
                            .pattern(" R ")
                            .define('L', Items.LEVER)
                            .define('R', Items.REDSTONE)
                            .define('B', commonItemTag("plates/brass"))
                            .unlockedBy("has_" + c.getName(), has(c.get()))
                            .save(p, DnDecor.asResource("crafting/" + c.getName()))
            )
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> p.horizontalFaceBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            .onRegister(ItemUseOverrides::addBlock)
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<MetalSupportBlock> METAL_SUPPORT = REGISTRATE.block("metal_support", MetalSupportBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.noOcclusion())
            .transform(pickaxeOnly())
            .recipe((c, p) -> {
                p.stonecutting(DataIngredient.items(AllBlocks.INDUSTRIAL_IRON_BLOCK.get()), RecipeCategory.BUILDING_BLOCKS, c, 2);
                p.stonecutting(DataIngredient.tag(darkMetalDecorTag), RecipeCategory.BUILDING_BLOCKS, c, 1);
            })
            .blockstate(DnDecorBlockStateGen.metalSupportBlockState())
            .onRegister(CreateRegistrate.connectedTextures(() -> new VerticalCtBehavior(DnDecorSpriteShifts.METAL_SUPPORT)))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<DiagonalMetalSupportBlock> DIAGONAL_METAL_SUPPORT = REGISTRATE.block("diagonal_metal_support", DiagonalMetalSupportBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.noOcclusion())
            .transform(pickaxeOnly())
            .recipe((c, p) -> {
                p.stonecutting(DataIngredient.items(AllBlocks.INDUSTRIAL_IRON_BLOCK.get()), RecipeCategory.BUILDING_BLOCKS, c, 2);
                p.stonecutting(DataIngredient.tag(darkMetalDecorTag), RecipeCategory.BUILDING_BLOCKS, c, 1);
            })
            .onRegister(CreateRegistrate.connectedTextures(() -> new DiagonalMetalSupportCtBehavior(DnDecorSpriteShifts.DIAGONAL_METAL_SUPPORT)))
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();



    public static final DyedBlockList<FlapDisplayBlock> DYED_DISPLAY_BOARDS = new DyedBlockList<>(color -> {
        String colorName = color.getSerializedName();
        return REGISTRATE.block(colorName + "_display_board", FlapDisplayTypeBlock::new)
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.mapColor(color.getMapColor()))
                .addLayer(() -> RenderType::cutoutMipped)
                .transform(pickaxeOnly())
                .transform(DStress.setNoImpact())
                .recipe((c, p) ->
                        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 8)
                                .pattern("DDD")
                                .pattern("DCD")
                                .pattern("DDD")
                                .define('C', color.getTag())
                                .define('D', commonItemTag("assets/create/display_boards"))
                                .unlockedBy("has_" + c.getName(), has(c.get()))
                                .save(p, DnDecor.asResource("crafting/" + c.getName()))
                )
                .blockstate((c, p) -> {
                    p.models().withExistingParent("block/" + c.getName() + "/block", DnDecor.asResource("block/display_board_base/block"))
                            .texture("7", DnDecor.asResource("block/display_boards/" + colorName))
                            .texture("particle", DnDecor.asResource("block/display_boards/" + colorName));
                    p.models().withExistingParent("block/" + c.getName() + "/item", DnDecor.asResource("block/display_board_base/item"))
                            .texture("7", DnDecor.asResource("block/display_boards/" + colorName))
                            .texture("particle", DnDecor.asResource("block/display_boards/" + colorName));

                    p.horizontalBlock(c.get(), AssetLookup.partialBaseModel(c, p));
                })
                .transform(displayTarget(AllDisplayTargets.DISPLAY_BOARD))
                .item()
                .tag(commonItemTag("assets/create/display_boards"), commonItemTag("assets/create/dyed_display_boards"))
                .transform(customItemModel())
                .register();
    });



    public static final DyedBlockList<FlywheelBlock> DYED_FLYWHEELS = new DyedBlockList<>(color -> {
        String colorName = color.getSerializedName();
        return REGISTRATE.block(colorName + "_flywheel", p -> new FlywheelTypeBlock(color, p))
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion().mapColor(color.getMapColor()))
                .transform(axeOrPickaxe())
                .transform(DStress.setNoImpact())
                .recipe((c, p) ->
                        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, c.get(), 8)
                                .pattern("DDD")
                                .pattern("DCD")
                                .pattern("DDD")
                                .define('C', color.getTag())
                                .define('D', commonItemTag("assets/create/flywheels"))
                                .unlockedBy("has_" + c.getName(), has(c.get()))
                                .save(p, DnDecor.asResource("crafting/" + c.getName()))
                )
                .blockstate((c, p) -> {
                    p.models().withExistingParent("block/" + c.getName() + "/block", Create.asResource("block/flywheel/block"))
                            .texture("0", DnDecor.asResource("block/flywheel/" + colorName))
                            .texture("particle", DnDecor.asResource("block/flywheel/" + colorName));
                    p.models().withExistingParent("block/" + c.getName() + "/item", Create.asResource("block/flywheel/item"))
                            .texture("0", DnDecor.asResource("block/flywheel/" + colorName))
                            .texture("particle", DnDecor.asResource("block/flywheel/" + colorName));

                    BlockStateGen.axisBlock(c, p, getBlockModel(true, c, p));
                })
                .item()
                .tag(commonItemTag("assets/create/flywheels"), commonItemTag("assets/create/dyed_flywheels"))
                .transform(customItemModel())
                .register();
    });

 //   public static final BlockEntry<FullBeltBlock> BELT = REGISTRATE.block("belt", FullBeltBlock::new)
 //           .properties(p -> p.sound(SoundType.WOOL).strength(0.8f).mapColor(MapColor.COLOR_GRAY))
 //           .addLayer(() -> RenderType::cutoutMipped)
 //           .transform(axeOrPickaxe())
 //           .blockstate((c, p) -> {
 //               p.models().withExistingParent("block/belt/diagonal_end", Create.asResource("block/belt/diagonal_end"))
 //                       .texture("0", DnDecor.asResource("block/belt_diagonal")).texture("particle", DnDecor.asResource("block/belt_diagonal"));
 //               p.models().withExistingParent("block/belt/diagonal_middle", Create.asResource("block/belt/diagonal_middle"))
 //                       .texture("0", DnDecor.asResource("block/belt_diagonal")).texture("particle", DnDecor.asResource("block/belt_diagonal"));
 //               p.models().withExistingParent("block/belt/diagonal_start", Create.asResource("block/belt/diagonal_start"))
 //                       .texture("0", DnDecor.asResource("block/belt_diagonal")).texture("particle", DnDecor.asResource("block/belt_diagonal"));
//
 //               p.models().withExistingParent("block/belt/end", Create.asResource("block/belt/end")).texture("0", DnDecor.asResource("block/belt"));
 //               p.models().withExistingParent("block/belt/middle", Create.asResource("block/belt/middle")).texture("0", DnDecor.asResource("block/belt"));
 //               p.models().withExistingParent("block/belt/start", Create.asResource("block/belt/start")).texture("0", DnDecor.asResource("block/belt"));
//
 //               p.models().withExistingParent("block/belt/end_bottom", Create.asResource("block/belt/end_bottom")).texture("1", DnDecor.asResource("block/belt_offset"));
 //               p.models().withExistingParent("block/belt/middle_bottom", Create.asResource("block/belt/middle_bottom")).texture("1", DnDecor.asResource("block/belt_offset"));
 //               p.models().withExistingParent("block/belt/start_bottom", Create.asResource("block/belt/start_bottom")).texture("1", DnDecor.asResource("block/belt_offset"));
//
//
 //               p.models().withExistingParent("block/belt/particle", Create.asResource("block/belt/particle")).texture("particle", DnDecor.asResource("block/belt"));
//
//
 //               new FullBeltGenerator().generate(c, p);
 //           })
 //           .transform(DStress.setNoImpact())
 //           .transform(displaySource(AllDisplaySources.ITEM_NAMES))
 //           .onRegister(CreateRegistrate.blockModel(() -> BeltModel::new))
 //           .register();

    public static final BlockEntry<DnDCogWheelBlock> DARK_METAL_COGWHEEL = REGISTRATE.block("dark_metal_cogwheel", p -> new DnDCogWheelBlock(DnDecorPartialModels.DARK_METAL_COGWHEEL,false,p))
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.noOcclusion())
            .transform(axeOrPickaxe())
            .transform(DStress.setNoImpact())
            .recipe((c, p) ->
                     ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,c.get())
                             .requires(AllBlocks.SHAFT)
                             .requires(DnDecorBlocks.DARK_METAL_BLOCK)
                             .unlockedBy("has_" + c.getName(), has(c.get()))
                             .save(p, DnDecor.asResource("crafting/" + c.getName()))
             )
            .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
            .blockstate(DnDecorBlockStateGen.cogwheelBlockState(false))
            .item(DnDCogwheelBlockItem::new)
            .transform(customItemModel())
            .register();

    public static final BlockEntry<DnDCogWheelBlock> LARGE_DARK_METAL_COGWHEEL = REGISTRATE.block("large_dark_metal_cogwheel", p -> new DnDCogWheelBlock(DnDecorPartialModels.LARGE_DARK_METAL_COGWHEEL,true,p))
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.noOcclusion())
            .transform(axeOrPickaxe())
            .transform(DStress.setNoImpact())
            .recipe((c, p) ->
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,c.get())
                            .requires(AllBlocks.SHAFT)
                            .requires(DnDecorBlocks.DARK_METAL_BLOCK)
                            .requires(DnDecorBlocks.DARK_METAL_BLOCK)
                            .unlockedBy("has_" + c.getName(), has(c.get()))
                            .save(p, DnDecor.asResource("crafting/" + c.getName()))
            )
            .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
            .blockstate(DnDecorBlockStateGen.cogwheelBlockState(true))
            .item(DnDCogwheelBlockItem::new)
            .transform(customItemModel())
            .register();

    public static final BlockEntry<DnDCogWheelBlock> INDUSTRIAL_COGWHEEL = REGISTRATE.block("industrial_cogwheel", p -> new DnDCogWheelBlock(DnDecorPartialModels.INDUSTRIAL_COGWHEEL,false,p))
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.noOcclusion())
            .transform(axeOrPickaxe())
            .transform(DStress.setNoImpact())
            .recipe((c, p) ->
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,c.get())
                            .requires(AllBlocks.SHAFT)
                            .requires(DnDecorBlocks.INDUSTRIAL_PLATING_BLOCK)
                            .unlockedBy("has_" + c.getName(), has(c.get()))
                            .save(p, DnDecor.asResource("crafting/" + c.getName()))
            )
            .blockstate((c, p) -> {
                BlockStateGen.axisBlock(c, p, getBlockModel(true, c, p));})
            .item(DnDCogwheelBlockItem::new)
            .transform(customItemModel())
            .register();

    public static final BlockEntry<DnDCogWheelBlock> LARGE_INDUSTRIAL_COGWHEEL = REGISTRATE.block("large_industrial_cogwheel", p -> new DnDCogWheelBlock(DnDecorPartialModels.LARGE_INDUSTRIAL_COGWHEEL,true,p))
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.noOcclusion())
            .transform(axeOrPickaxe())
            .transform(DStress.setNoImpact())
            .recipe((c, p) ->
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS,c.get())
                            .requires(AllBlocks.SHAFT)
                            .requires(DnDecorBlocks.INDUSTRIAL_PLATING_BLOCK)
                            .requires(DnDecorBlocks.INDUSTRIAL_PLATING_BLOCK)
                            .unlockedBy("has_" + c.getName(), has(c.get()))
                            .save(p, DnDecor.asResource("crafting/" + c.getName()))
            )
            .blockstate((c, p) -> {
                BlockStateGen.axisBlock(c, p, getBlockModel(true, c, p));})
            .item(DnDCogwheelBlockItem::new)
            .transform(customItemModel())
            .register();


    public static final DyedBlockList<DnDCogWheelBlock> DYED_COGWHEELS = new DyedBlockList<>(color -> {
        String colorName = color.getSerializedName();
        return REGISTRATE.block(colorName + "_cogwheel", p -> new DnDCogWheelBlock(color,false, p))
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion().mapColor(color.getMapColor()))
                .transform(axeOrPickaxe())
                .transform(DStress.setNoImpact())
                .recipe((c, p) ->
                        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,c.get(),8)
                                .pattern("DDD")
                                .pattern("DCD")
                                .pattern("DDD")
                                .define('C', color.getTag())
                                .define('D', DARK_METAL_COGWHEEL)
                                .unlockedBy("has_" + c.getName(), has(c.get()))
                                .save(p, DnDecor.asResource("crafting/" + c.getName()))
                )
                .blockstate((c, p) -> {
                    p.models().withExistingParent("block/" + c.getName() + "/block_shaftless", Create.asResource("block/cogwheel_shaftless"))
                            .texture("1_2", DnDecor.asResource("block/cogwheels/" + colorName))
                            .texture("particle", DnDecor.asResource("block/cogwheels/" + colorName));
                    p.models().withExistingParent("block/" + c.getName() + "/block", ResourceLocation.withDefaultNamespace("air"))
                            .texture("particle", DnDecor.asResource("block/cogwheels/" + colorName));;
                    p.models().withExistingParent("block/" + c.getName() + "/item", Create.asResource("block/cogwheel"))
                            .texture("1_2", DnDecor.asResource("block/cogwheels/" + colorName))
                            .texture("particle", DnDecor.asResource("block/cogwheels/" + colorName));

                    BlockStateGen.axisBlock(c, p, getBlockModel(true, c, p));
                })
                .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                .item(DnDCogwheelBlockItem::new)
                .transform(customItemModel())
                .register();
    });

    public static final DyedBlockList<DnDCogWheelBlock> DYED_LARGE_COGWHEELS = new DyedBlockList<>(color -> {
        String colorName = color.getSerializedName();
        return REGISTRATE.block(colorName + "_large_cogwheel", p -> new DnDCogWheelBlock(color,true, p))
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion().mapColor(color.getMapColor()))
                .transform(axeOrPickaxe())
                .transform(DStress.setNoImpact())
                .recipe((c, p) ->
                        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,c.get(),8)
                                .pattern("DDD")
                                .pattern("DCD")
                                .pattern("DDD")
                                .define('C', color.getTag())
                                .define('D', LARGE_DARK_METAL_COGWHEEL)
                                .unlockedBy("has_" + c.getName(), has(c.get()))
                                .save(p, DnDecor.asResource("crafting/" + c.getName()))
                )
                .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                .blockstate((c, p) -> {
                    p.models().withExistingParent("block/" + c.getName() + "/block_shaftless", Create.asResource("block/large_cogwheel_shaftless"))
                            .texture("4", DnDecor.asResource("block/large_cogwheels/" + colorName))
                            .texture("particle", DnDecor.asResource("block/large_cogwheels/" + colorName));
                    p.models().withExistingParent("block/" + c.getName() + "/block", ResourceLocation.withDefaultNamespace("air"))
                            .texture("particle", DnDecor.asResource("block/large_cogwheels/" + colorName));;
                    p.models().withExistingParent("block/" + c.getName() + "/item", Create.asResource("block/large_cogwheel"))
                            .texture("4", DnDecor.asResource("block/large_cogwheels/" + colorName))
                            .texture("particle", DnDecor.asResource("block/large_cogwheels/" + colorName));

                    BlockStateGen.axisBlock(c, p, getBlockModel(true, c, p));
                })
                .item(DnDCogwheelBlockItem::new)
                .transform(customItemModel())
                .register();
    });



    public static final BlockEntry<WindowBlock> ORNATE_IRON_GLASS =
            customWindowBlock("ornate_iron_glass", () -> omni("palettes/ornate_iron_glass"), () -> omni("palettes/ornate_iron_glass_end"), () -> RenderType::cutout, false, () -> MapColor.TERRACOTTA_LIGHT_GRAY)
                    .recipe((c, p) -> {
                        p.stonecutting(DataIngredient.items(AllPaletteBlocks.ORNATE_IRON_WINDOW.get()), RecipeCategory.BUILDING_BLOCKS, c, 1);
                        p.stonecutting(DataIngredient.items(c), RecipeCategory.BUILDING_BLOCKS, AllPaletteBlocks.ORNATE_IRON_WINDOW, 1);
                    }).register();

    public static final BlockEntry<ConnectedGlassPaneBlock> ORNATE_IRON_GLASS_PANE =
            customWindowPane("ornate_iron_glass", ORNATE_IRON_GLASS, () -> omni("palettes/ornate_iron_glass"), () -> RenderType::cutoutMipped).register();

    // AllPaletteStoneTypes is not yet populated when MaterialTypeProvider first builds its list on 1.20.1.
    // Seed the thirteen original machine materials here, immediately before the machine lists consume it.
    private static final boolean LEGACY_MACHINE_STONES_READY = registerLegacyMachineStoneTypes();

    private static boolean registerLegacyMachineStoneTypes() {
        addLegacyMachineStone(() -> AllPaletteStoneTypes.ASURINE.baseBlock.get(), "asurine");
        addLegacyMachineStone(() -> Blocks.CALCITE, "calcite");
        addLegacyMachineStone(() -> AllPaletteStoneTypes.CRIMSITE.baseBlock.get(), "crimsite");
        addLegacyMachineStone(() -> Blocks.DEEPSLATE, "deepslate");
        addLegacyMachineStone(() -> Blocks.DIORITE, "diorite");
        addLegacyMachineStone(() -> Blocks.DRIPSTONE_BLOCK, "dripstone");
        addLegacyMachineStone(() -> Blocks.GRANITE, "granite");
        addLegacyMachineStone(() -> AllPaletteStoneTypes.LIMESTONE.baseBlock.get(), "limestone");
        addLegacyMachineStone(() -> AllPaletteStoneTypes.OCHRUM.baseBlock.get(), "ochrum");
        addLegacyMachineStone(() -> AllPaletteStoneTypes.SCORCHIA.baseBlock.get(), "scorchia");
        addLegacyMachineStone(() -> AllPaletteStoneTypes.SCORIA.baseBlock.get(), "scoria");
        addLegacyMachineStone(() -> Blocks.TUFF, "tuff");
        addLegacyMachineStone(() -> AllPaletteStoneTypes.VERIDIUM.baseBlock.get(), "veridium");
        return true;
    }

    private static void addLegacyMachineStone(com.tterrag.registrate.util.nullness.NonNullSupplier<Block> block,
                                               String id) {
        if (!MaterialTypeProvider.stoneTypes.contains(block)) {
            MaterialTypeProvider.stoneTypes.add(block);
            MaterialTypeProvider.stoneTypesRegister.put(block, id);
        }
    }

    public static final StoneTypeBlockList<CrushingWheelBlock> STONE_TYPE_CRUSHING_WHEELS = new StoneTypeBlockList<>((block, id) -> {
        return REGISTRATE.block(id + "_crushing_wheel", p -> new CrushingWheelTypeBlock(block, p))
                .properties(p -> p.mapColor(block.get().defaultMapColor()).sound(block.get().defaultBlockState().getSoundType()))
                .initialProperties(SharedProperties::stone)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .transform(pickaxeOnly())
                .recipe((c, p) ->
                        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 1)
                                .requires(block.get())
                                .requires(commonItemTag("assets/create/crushing_wheels"))
                                .unlockedBy("has_" + c.getName(), has(c.get()))
                                .save(p, DnDecor.asResource("crafting/" + c.getName()))
                )
                .blockstate((c, p) -> {
                    p.models().withExistingParent("block/" + c.getName() + "/block", Create.asResource("block/crushing_wheel/block"))
                            .texture("insert", DnDecor.asResource("block/crushing_wheels/" + id + "/insert"))
                            .texture("plates", DnDecor.asResource("block/crushing_wheels/" + id + "/plates"));
                    p.models().withExistingParent("block/" + c.getName() + "/item", Create.asResource("block/crushing_wheel/item"))
                            .texture("insert", DnDecor.asResource("block/crushing_wheels/" + id + "/insert"))
                            .texture("plates", DnDecor.asResource("block/crushing_wheels/" + id + "/plates"));
                    BlockStateGen.axisBlock(c, p, s -> AssetLookup.partialBaseModel(c, p));
                })
                .addLayer(() -> RenderType::cutoutMipped)
                .transform(DStress.setImpact(8.0))
                .item()
                .tag(commonItemTag("assets/create/crushing_wheels"))
                .transform(customItemModel())
                .register();
    });

    public static final StoneTypeBlockList<MillstoneBlock> STONE_TYPE_MILLSTONE = new StoneTypeBlockList<>((block, id) -> {
        return REGISTRATE.block(id + "_millstone", p -> new MillstoneTypeBlock(id, block, p))
                .properties(p -> p.mapColor(block.get().defaultMapColor()).sound(block.get().defaultBlockState().getSoundType()))
                .initialProperties(SharedProperties::stone)
                .transform(pickaxeOnly())
                .recipe((c, p) ->
                        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, c.get(), 1)
                                .requires(block.get())
                                .requires(commonItemTag("assets/create/millstones"))
                                .unlockedBy("has_" + c.getName(), has(c.get()))
                                .save(p, DnDecor.asResource("crafting/" + c.getName()))
                )
                .blockstate((c, p) -> {
                    p.models().withExistingParent("block/" + c.getName() + "/block", Create.asResource("block/millstone/block"))
                            .texture("5", DnDecor.asResource("block/millstones/" + id));
                    p.models().withExistingParent("block/" + c.getName() + "/inner", Create.asResource("block/millstone/inner"))
                            .texture("5", DnDecor.asResource("block/millstones/" + id));
                    p.models().withExistingParent("block/" + c.getName() + "/item", Create.asResource("block/millstone/item"))
                            .texture("5", DnDecor.asResource("block/millstones/" + id))
                            .texture("4", DnDecor.asResource("block/crushing_wheels/" + id + "/plates"));
                    p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p));
                })
                .transform(DStress.setImpact(4.0))
                .item()
                .tag(commonItemTag("assets/create/millstones"))
                .transform(customItemModel())
                .register();
    });

    public static final BlockEntry<Block> INDUSTRIAL_PLATING_BLOCK = REGISTRATE.block("industrial_plating_block", Block::new)
            .transform(layeredConnected(() -> omni("industrial_plating_block_side"), () -> omni("industrial_plating_block")))
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .recipe((c, p) -> {
                p.stonecutting(DataIngredient.items(AllBlocks.INDUSTRIAL_IRON_BLOCK.get()), RecipeCategory.BUILDING_BLOCKS, c, 1);
                p.stonecutting(DataIngredient.items(c), RecipeCategory.BUILDING_BLOCKS, AllBlocks.INDUSTRIAL_IRON_BLOCK, 1);
                p.stonecutting(DataIngredient.tag(Tags.Items.INGOTS_IRON), RecipeCategory.BUILDING_BLOCKS, c, 2);
            })
            .transform(pickaxeOnly())
            .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
            .simpleItem()
            .lang("Block of Industrial Plating")
            .register();

    public static final BlockEntry<LargeGirderBlock> LARGE_METAL_GIRDER = REGISTRATE.block("large_metal_girder", LargeGirderBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 1)
                    .pattern("CC").pattern("CC")
                    .define('C', AllBlocks.METAL_GIRDER)
                    .unlockedBy("has_" + c.getName(), has(c.get())).save(p, DnDecor.asResource("crafting/" + c.getName()))
            ).transform(pickaxeOnly())
            .onRegister(CreateRegistrate.connectedTextures(() -> new RotatedPillarCTBehaviour(rectangle("large_girder"), omni("large_girder_top"))))
            .blockstate((c, p) -> p.axisBlock(c.get(), DnDecor.asResource("block/large_girder"), DnDecor.asResource("block/large_girder_top")))
            .simpleItem()
            .register();

    public static final BlockEntry<BeamBlock> BEAM = REGISTRATE.block("beam", BeamBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.noOcclusion().sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GRAY))
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .blockstate((c, p) -> p.getVariantBuilder(c.get()).forAllStates(s -> {
                var beam = s.getValue(BeamBlock.BEAM);
                var axis = s.getValue(BeamBlock.AXIS) == Direction.Axis.X ? "_x" : "_z";
                var modelBoth = p.models().getExistingFile(DnDecor.asResource("block/beam/block" + axis));
                var modelTop = p.models().getExistingFile(DnDecor.asResource("block/beam/top" + axis));
                var modelBottom = p.models().getExistingFile(DnDecor.asResource("block/beam/bottom" + axis));
                var model = switch (beam) {
                    case TOP -> modelTop;
                    case BOTTOM -> modelBottom;
                    case BOTH -> modelBoth;
                };
                return ConfiguredModel.builder().modelFile(model).build();
            }))
            .onRegister(connectedTextures(() -> new BeamCTBehaviour(horizontalKryppers("beam/beam"), vertical("beam/beam_top_z"), horizontalKryppers("beam/beam_top_x"))))
            .recipe((c, p) -> {
                p.stonecutting(DataIngredient.tag(Tags.Items.INGOTS_IRON), RecipeCategory.BUILDING_BLOCKS, c, 2);
                p.stonecutting(DataIngredient.items(LARGE_METAL_GIRDER.get()), RecipeCategory.BUILDING_BLOCKS, c, 2);
                p.stonecutting(DataIngredient.items(AllBlocks.INDUSTRIAL_IRON_BLOCK.get(), INDUSTRIAL_PLATING_BLOCK.get()), RecipeCategory.BUILDING_BLOCKS, c, 1);
            })
            .transform(pickaxeOnly())
            .item()
            .model((c, p) -> p.withExistingParent("item/" + c.getName(), DnDecor.asResource("block/beam/item")))
            .build()
            .lang("Beam")
            .register();

    public static final BlockEntry<DiagonalGirderBlock> DIAGONAL_GIRDER = REGISTRATE.block("diagonal_girder", DiagonalGirderBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutout)
            .recipe((c, p) -> {
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 2)
                        .pattern(" C")
                        .pattern("C ")
                        .define('C', AllBlocks.METAL_GIRDER)
                        .unlockedBy("has_" + c.getName(), has(c.get())).save(p, DnDecor.asResource("crafting/" + c.getName()));
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 2)
                        .pattern("C ")
                        .pattern(" C")
                        .define('C', AllBlocks.METAL_GIRDER)
                        .unlockedBy("has_" + c.getName(), has(c.get())).save(p, DnDecor.asResource("crafting/mirrored_" + c.getName()));
            }).transform(axeOrPickaxe())
            .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
            .blockstate(new DiagonalGirderGenerator()::generate)
            .lang("Diagonal Girder")
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<OrnateGrateBlock> ORNATE_GRATE = REGISTRATE.block("ornate_grate", OrnateGrateBlock::new)
            .transform(ornateConnected(() -> omni("ornate_grate")))
            .initialProperties(SharedProperties::wooden)
            .properties(p -> p.sound(SoundType.WOOD).mapColor(MapColor.TERRACOTTA_GRAY))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 8)
                    .pattern("ISI")
                    .pattern("S S")
                    .pattern("ISI")
                    .define('S', Tags.Items.RODS_WOODEN)
                    .define('I', Tags.Items.INGOTS_IRON)
                    .unlockedBy("has_" + c.getName(), has(c.get())).save(p, DnDecor.asResource("crafting/" + c.getName()))
            ).transform(axeOrPickaxe())
            .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
            .lang("Ornate Grate")
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .transform(b -> b.model((c, p) -> p.blockItem(() -> c.getEntry().getBlock())).build())
            .register();

    public static final BlockEntry<Block> ZINC_BRICKS = REGISTRATE.block("zinc_bricks", Block::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.sound(SoundType.METAL).mapColor(MapColor.GLOW_LICHEN))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 8)
                    .pattern("ZZ")
                    .pattern("ZZ")
                    .define('Z', AllBlocks.ZINC_BLOCK)
                    .unlockedBy("has_" + c.getName(), has(c.get())).save(p, DnDecor.asResource("crafting/" + c.getName()))
            ).transform(pickaxeOnly())
            .lang("Zinc Bricks")
            .simpleItem()
            .register();

    public static final BlockEntry<Block> ZINC_CHECKER_TILES = REGISTRATE.block("zinc_checker_tiles", Block::new)
            .transform(connected(() -> omni("zinc_checker_tiles")))
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.sound(SoundType.METAL).mapColor(MapColor.GLOW_LICHEN))
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 8)
                    .pattern("III")
                    .pattern("IZI")
                    .pattern("III")
                    .define('I', commonItemTag("ingots/zinc"))
                    .define('Z', commonItemTag("storage_blocks/zinc"))
                    .unlockedBy("has_" + c.getName(), has(c.get())).save(p, DnDecor.asResource("crafting/" + c.getName()))
            ).transform(pickaxeOnly())
            .lang("Zinc Checker Tiles")
            .simpleItem()
            .register();

    public static final BlockEntry<Block> STONE_METAL = REGISTRATE.block("stone_metal", Block::new)
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_CYAN).sound(DnDecorSoundTypes.METAL_HEAVY).strength(1.5f,2f))
            .blockstate((c, p) -> p.simpleBlock(c.get()))
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(omni("stone_metal"))))
            .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, omni("stone_metal"))))
            .transform(pickaxeOnly())
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 4)
                    .pattern("AS").pattern("SA")
                    .define('S', commonItemTag("plates/iron")).define('A', AllPaletteStoneTypes.ASURINE.baseBlock.get())
                    .unlockedBy("has_" + c.getName(), has(c.get())).save(p, DnDecor.asResource("crafting/" + c.getName())))
            .blockstate((c, p) -> {
                var model = p.models().cubeAll(c.getName(), DnDecor.asResource("block/stone_metal"));
                p.simpleBlockItem(c.get(), model);
                p.simpleBlock(c.get(), model);
            })
            .item().tag(DnDecorTags.modItemTag("stone_metal_decor")).build()
            .register();

    public static final DyedBlockList<Block> DYED_STONE_METAL = new DyedBlockList<>(color -> {
        var baseID = "stone_metal";
        var colorID = color.getSerializedName();
        var blockID = colorID + "_" + baseID;
        var ct = omni(baseID + "/" + colorID);
        return REGISTRATE.block(blockID, Block::new)
                .properties(p -> p.mapColor(color.getMapColor()).sound(DnDecorSoundTypes.METAL_HEAVY).strength(1.5f,2f))
                .blockstate((c, p) -> p.simpleBlock(c.get()))
                .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(ct)))
                .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, ct)))
                .transform(pickaxeOnly())
                .recipe((c, p) -> {
                    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 8)
                            .pattern("ASA").pattern("SDS").pattern("ASA")
                            .define('S', commonItemTag("plates/iron")).define('A', AllPaletteStoneTypes.ASURINE.baseBlock.get()).define('D', color.getTag())
                            .unlockedBy("has_" + c.getName(), has(c.get())).save(p, DnDecor.asResource("crafting/" + c.getName()));
                    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 8)
                            .pattern("SSS").pattern("SDS").pattern("SSS")
                            .define('S', DnDecorTags.modItemTag("stone_metal_decor")).define('D', color.getTag())
                            .unlockedBy("has_" + c.getName(), has(c.get())).save(p, DnDecor.asResource("crafting/" + c.getName() + "_dyed"));
                })
                .blockstate((c, p) -> {
                    var model = p.models().cubeAll(c.getName(), DnDecor.asResource("block/" + baseID + "/" + colorID));
                    p.simpleBlockItem(c.get(), model);
                    p.simpleBlock(c.get(), model);
                })
                .item().tag(DnDecorTags.modItemTag("stone_metal_decor"), DnDecorTags.modItemTag("dyed_stone_metal_decor")).build()
                .register();
    });

    public static final DyedBlockList<VelvetBlock> DYED_VELVET_BLOCKS = new DyedBlockList<>(color -> velvetBlock(color.getSerializedName(), color.getMapColor(), color));

//public static final BlockEntry<ColoredStorageContainerBlock> DYED_STORAGE_CONTAINER = REGISTRATE.block("storage_container", ColoredStorageContainerBlock::new)
//       // .lang("Storage Container")
//        .initialProperties(SharedProperties::softMetal)
//        .properties(p -> p.mapColor(MapColor.COLOR_GRAY)
//                .sound(SoundType.NETHERITE_BLOCK)
//                .explosionResistance(1200))
//        .transform(pickaxeOnly())
//       .blockstate((c, p) -> p.getVariantBuilder(c.get()).forAllStates(s -> {
//           var refModel = Create.asResource("block/item_vault");
//           var refModelItem = Create.asResource("item/item_vault");
//           var color = s.getValue(ColoredStorageContainerBlock.COLOR);
//           var id = color.getSerializedName();
//           var path0 = DnDecor.asResource("block/storage_container/" + id + "_storage_container_bottom_small");
//           var path1 = DnDecor.asResource("block/storage_container/" + id + "_storage_container_front_small");
//           var path2 = DnDecor.asResource("block/storage_container/" + id + "_storage_container_side_small");
//           var path3 = DnDecor.asResource("block/storage_container/" + id + "_storage_container_top_small") ; //particle
//
//           ModelFile model = p.models().withExistingParent("block/storage_containers/" + id, refModel)
//                   .texture("0", path0).texture("1", path1).texture("2", path2).texture("3", path3).texture("particle", path3);
//
//           p.models().withExistingParent("item/" + id + "_storage_container", refModelItem).parent(model);
//           return ConfiguredModel.builder()
//                   .modelFile(model)
//                   .rotationY(s.getValue(ItemVaultBlock.HORIZONTAL_AXIS) == Direction.Axis.X ? 90 : 0)
//                   .build();
//       }))
//       .onRegister(CreateRegistrate.connectedTextures(ColoredStorageContainerCTBehaviour::new))
//       .register();
    


    public static TagKey<Block> stairsBlockTag = optionalTag(BuiltInRegistries.BLOCK, ResourceLocation.withDefaultNamespace("stairs"));
    public static TagKey<Item> stairsItemTag = optionalTag(BuiltInRegistries.ITEM, ResourceLocation.withDefaultNamespace("stairs"));
    public static TagKey<Block> slabsBlockTag = optionalTag(BuiltInRegistries.BLOCK, ResourceLocation.withDefaultNamespace("slabs"));
    public static TagKey<Item> slabsItemTag = optionalTag(BuiltInRegistries.ITEM, ResourceLocation.withDefaultNamespace("slabs"));

    public static final BlockEntry<Block> DARK_METAL_BLOCK = REGISTRATE.block("dark_metal_block", Block::new)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK).sound(SoundType.NETHERITE_BLOCK).strength(0.5f,1.5f))
            .blockstate((c, p) -> p.simpleBlock(c.get()))
            .transform(pickaxeOnly())
            .recipe((c, p) -> {
                p.stonecutting(DataIngredient.items(AllBlocks.INDUSTRIAL_IRON_BLOCK.get()), RecipeCategory.BUILDING_BLOCKS, c, 2);
                p.stonecutting(DataIngredient.tag(darkMetalDecorTag), RecipeCategory.BUILDING_BLOCKS, c, 1);
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 4)
                        .pattern("CC")
                        .pattern("CC")
                        .define('C', AllBlocks.INDUSTRIAL_IRON_BLOCK.get())
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, DnDecor.asResource("crafting/" + c.getName() + "_from_" + c.getName()));
            })
            .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
            .item()
            .tag(darkMetalDecorTag)
            .build()
            .register();

    public static final BlockEntry<Block> DARK_METAL_PLATING = REGISTRATE.block("dark_metal_plating", Block::new)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK).sound(SoundType.NETHERITE_BLOCK).strength(0.5f,1.5f))
            .blockstate((c, p) -> p.simpleBlock(c.get()))
            .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(omni("dark_metal_plating"))))
            .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, omni("dark_metal_plating"))))
            .transform(pickaxeOnly())
            .recipe((c, p) -> {
                p.stonecutting(DataIngredient.items(AllBlocks.INDUSTRIAL_IRON_BLOCK.get()), RecipeCategory.BUILDING_BLOCKS, c, 2);
                p.stonecutting(DataIngredient.items(DARK_METAL_BLOCK.get()), RecipeCategory.BUILDING_BLOCKS, c, 1);
                p.stonecutting(DataIngredient.items(c.get()), RecipeCategory.BUILDING_BLOCKS, DARK_METAL_BLOCK, 1);
                p.stonecutting(DataIngredient.tag(darkMetalDecorTag), RecipeCategory.BUILDING_BLOCKS, c, 1);
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 9)
                        .pattern("CCC")
                        .pattern("CCC")
                        .pattern("CCC")
                        .define('C', DARK_METAL_BLOCK.get())
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, DnDecor.asResource("crafting/" + c.getName() + "_from_" + c.getName()));
            })
            .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
            .item()
            .tag(darkMetalDecorTag)
            .build()
            .register();

    public static final BlockEntry<SlabBlock> DARK_METAL_SLAB = REGISTRATE.block("dark_metal_block_slab", SlabBlock::new)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK).sound(SoundType.NETHERITE_BLOCK).strength(0.5f,1.5f))
            .blockstate((c, p) -> p.slabBlock(c.get(), DnDecor.asResource("block/dark_metal_block"),
                    DnDecor.asResource("block/dark_metal_block_slab"), DnDecor.asResource("block/dark_metal_block"), DnDecor.asResource("block/dark_metal_block")))
            .transform(pickaxeOnly())
            .tag(stairsBlockTag)
            .recipe((c, p) -> {
                p.stonecutting(DataIngredient.items(AllBlocks.INDUSTRIAL_IRON_BLOCK.get()), RecipeCategory.BUILDING_BLOCKS, c, 4);
                p.stonecutting(DataIngredient.tag(darkMetalDecorTag), RecipeCategory.BUILDING_BLOCKS, c, 2);
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 6)
                        .pattern("CCC")
                        .define('C', DARK_METAL_BLOCK.get())
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, DnDecor.asResource("crafting/" + c.getName() + "_from_" + c.getName()));
            })
            .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
            .item()
            .tag(slabsItemTag)
            .build()
            .register();

    public static final BlockEntry<StairBlock> DARK_METAL_STAIRS = REGISTRATE.block("dark_metal_block_stairs", p -> new StairBlock(DARK_METAL_BLOCK.getDefaultState(), p))
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK).sound(SoundType.NETHERITE_BLOCK).strength(0.5f,1.5f))
            .blockstate((c, p) -> p.stairsBlock(c.get(), DnDecor.asResource("block/dark_metal_block")))
            .transform(pickaxeOnly())
            .tag(stairsBlockTag)
            .recipe((c, p) -> {
                p.stonecutting(DataIngredient.items(AllBlocks.INDUSTRIAL_IRON_BLOCK.get()), RecipeCategory.BUILDING_BLOCKS, c, 2);
                p.stonecutting(DataIngredient.tag(darkMetalDecorTag), RecipeCategory.BUILDING_BLOCKS, c, 1);
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 4)
                        .pattern("X  ").pattern("XX ").pattern("XXX")
                        .define('X', DARK_METAL_BLOCK.get())
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, DnDecor.asResource("crafting/" + c.getName() + "_from_" + c.getName()));
            })
            .item()
            .tag(darkMetalDecorTag, stairsItemTag)
            .build()
            .register();

    public static final BlockEntry<Block> DARK_METAL_BRICKS = REGISTRATE.block("dark_metal_bricks", Block::new)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK).sound(SoundType.NETHERITE_BLOCK).strength(0.5f,1.5f))
            .blockstate((c, p) -> p.simpleBlock(c.get()))
            .transform(pickaxeOnly())
            .recipe((c, p) -> {
                p.stonecutting(DataIngredient.items(AllBlocks.INDUSTRIAL_IRON_BLOCK.get()), RecipeCategory.BUILDING_BLOCKS, c, 2);
                p.stonecutting(DataIngredient.tag(darkMetalDecorTag), RecipeCategory.BUILDING_BLOCKS, c, 1);
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 4)
                        .pattern("CC")
                        .pattern("CC")
                        .define('C', DARK_METAL_BLOCK.get())
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, DnDecor.asResource("crafting/" + c.getName() + "_from_" + c.getName()));
            })
            .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
            .item()
            .tag(darkMetalDecorTag)
            .build()
            .register();

    public static final BlockEntry<SlabBlock> DARK_METAL_BRICK_SLAB = REGISTRATE.block("dark_metal_brick_slab", SlabBlock::new)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK).sound(SoundType.NETHERITE_BLOCK).strength(0.5f,1.5f))
            .blockstate((c, p) -> p.slabBlock(c.get(), DnDecor.asResource("block/dark_metal_bricks"),
                    DnDecor.asResource("block/dark_metal_bricks"), DnDecor.asResource("block/dark_metal_bricks"), DnDecor.asResource("block/dark_metal_bricks")))
            .transform(pickaxeOnly())
            .tag(stairsBlockTag)
            .recipe((c, p) -> {
                p.stonecutting(DataIngredient.items(AllBlocks.INDUSTRIAL_IRON_BLOCK.get()), RecipeCategory.BUILDING_BLOCKS, c, 4);
                p.stonecutting(DataIngredient.tag(darkMetalDecorTag), RecipeCategory.BUILDING_BLOCKS, c, 2);
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 6)
                        .pattern("CCC")
                        .define('C', DARK_METAL_BRICKS.get())
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, DnDecor.asResource("crafting/" + c.getName() + "_from_" + c.getName()));
            })
            .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
            .item()
            .tag(slabsItemTag)
            .build()
            .register();

    public static final BlockEntry<StairBlock> DARK_METAL_BRICK_STAIRS = REGISTRATE.block("dark_metal_brick_stairs", p -> new StairBlock(DARK_METAL_BLOCK.getDefaultState(), p))
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK).sound(SoundType.NETHERITE_BLOCK).strength(0.5f,1.5f))
            .blockstate((c, p) -> p.stairsBlock(c.get(), DnDecor.asResource("block/dark_metal_bricks")))
            .transform(pickaxeOnly())
            .tag(stairsBlockTag)
            .recipe((c, p) -> {
                p.stonecutting(DataIngredient.items(AllBlocks.INDUSTRIAL_IRON_BLOCK.get()), RecipeCategory.BUILDING_BLOCKS, c, 2);
                p.stonecutting(DataIngredient.tag(darkMetalDecorTag), RecipeCategory.BUILDING_BLOCKS, c, 1);
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 4)
                        .pattern("X  ").pattern("XX ").pattern("XXX")
                        .define('X', DARK_METAL_BRICKS.get())
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, DnDecor.asResource("crafting/" + c.getName() + "_from_" + c.getName()));
            })
            .item()
            .tag(darkMetalDecorTag, stairsItemTag)
            .build()
            .register();




    public static final BlockEntry<FrontlightBlock> BRASS_FRONTLIGHT = REGISTRATE.block("brass_frontlight", FrontlightBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.noOcclusion().sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_YELLOW).lightLevel(FrontlightBlock::getLight))
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.getVariantBuilder(c.get()).forAllStates(s -> {
                var dir = s.getValue(FrontlightBlock.FACING);
                var lit = s.getValue(FrontlightBlock.LIT);
                var top = s.getValue(FrontlightBlock.ADDITIVE);
                var rot = s.getValue(FrontlightBlock.ROTATED);
                var target = "frontlight";
                if (!lit) target = target + "_off";
                if (top != Frontlight.EMPTY) target = top == Frontlight.TOP ? target + "_top" : target + "_grate" ;
                if (rot) target = target + "_rot";
                var refPath = "block/frontlight/";
                var refModel = DnDecor.asResource(refPath + target);
                var texture = DnDecor.asResource("block/" + c.getName());
                ModelFile model = p.models().withExistingParent("block/" + c.getName() + "/" + target, refModel).texture("0", texture).texture("particle", texture);

                if (lit && top == Frontlight.TOP && !rot) p.models().withExistingParent("block/" + c.getName(), DnDecor.asResource(refPath + "frontlight_item")).texture("0", texture).texture("particle", texture);
                return ConfiguredModel.builder().modelFile(model)
                        .rotationX(dir == Direction.DOWN ? 90 : dir.getAxis().isHorizontal() ? 0 : 270)
                        .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360).build();
            })).recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 4)
                    .pattern("GM").pattern("MB")
                    .define('G', Ingredient.of(Items.GLOWSTONE_DUST, Items.PRISMARINE_CRYSTALS, Items.BLAZE_ROD))
                    .define('M', commonItemTag("ingots/brass"))
                    .define('B', Tags.Items.STONE)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, DnDecor.asResource("crafting/" + c.getName()))).simpleItem()
            .register();

    @SuppressWarnings("all")
    public static final MetalTypeBlockList<FrontlightBlock> METAL_TYPE_FRONTLIGHTS = new MetalTypeBlockList<FrontlightBlock>(type -> {
        var metal = type.get();
        if (metal.equals(AllMetalTypes.BRASS)) return BRASS_FRONTLIGHT;
        if (metal.requireMods()) return null;
        if (!DnDecor.LOAD_ALL_METALS) {
            if (metal.modIDs.equals(MaterialTypeProvider.NA)) return null;
            boolean anyModLoaded = metal.modIDs.isEmpty();
            if (metal.requireMods()) for (String mod : metal.modIDs) if (ModList.get().isLoaded(mod)) { anyModLoaded = true; break; }
            if (!anyModLoaded) return null;
        }
        var builder = REGISTRATE.block(metal.id + "_frontlight", FrontlightBlock::new)
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion().sound(SoundType.NETHERITE_BLOCK).mapColor(metal.color).lightLevel(FrontlightBlock::getLight))
                .addLayer(() -> RenderType::cutoutMipped)
                .transform(pickaxeOnly())
                .blockstate((c, p) -> p.getVariantBuilder(c.get()).forAllStates(s -> {
                    var dir = s.getValue(FrontlightBlock.FACING);
                    var lit = s.getValue(FrontlightBlock.LIT);
                    var top = s.getValue(FrontlightBlock.ADDITIVE);
                    var rot = s.getValue(FrontlightBlock.ROTATED);
                    var target = "frontlight";
                    if (!lit) target = target + "_off";
                    if (top != Frontlight.EMPTY) target = top == Frontlight.TOP ? target + "_top" : target + "_grate" ;
                    if (rot) target = target + "_rot";
                    var refPath = "block/frontlight/";
                    var refModel = DnDecor.asResource(refPath + target);
                    var texture = DnDecor.asResource("block/" + c.getName());
                    ModelFile model = p.models().withExistingParent("block/" + c.getName() + "/" + target, refModel).texture("0", texture).texture("particle", texture);

                    if (lit && top == Frontlight.TOP && !rot) p.models().withExistingParent("block/" + c.getName(), DnDecor.asResource(refPath + "frontlight_item")).texture("0", texture).texture("particle", texture);
                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationX(dir == Direction.DOWN ? 90 : dir.getAxis().isHorizontal() ? 0 : 270)
                            .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                            .build();
                }));

        builder = builder.recipe((c, p) -> {
            var ingredient = metal.getIngredient();
            if (ingredient != null) {
                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 4)
                        .pattern("GM")
                        .pattern("MB")
                        .define('G', Ingredient.of(Items.GLOWSTONE_DUST, Items.PRISMARINE_CRYSTALS, Items.BLAZE_ROD))
                        .define('M', ingredient)
                        .define('B', Tags.Items.STONE)
                        .unlockedBy("has_" + c.getName(), has(c.get()))
                        .save(p, DnDecor.asResource("crafting/" + c.getName()));
            }
        });
        if (metal.equals(AllMetalTypes.NETHERITE)) builder = builder.item().properties(p -> p.fireResistant()).build();
        else builder = builder.simpleItem();
        return builder.register();
    });

    @SuppressWarnings("all")
    public static final MetalTypeBlockList<Block> METAL_TYPE_FLOORS = new MetalTypeBlockList<Block>(type -> {
        var metal = type.get();
        if (metal.requireMods()) return null;
        if (!DnDecor.LOAD_ALL_METALS) {
            if (metal.modIDs.equals(MaterialTypeProvider.NA)) return null;
            boolean anyModLoaded = metal.modIDs.isEmpty();
            if (metal.requireMods()) for (String mod : metal.modIDs)
                if (ModList.get().isLoaded(mod)) {
                    anyModLoaded = true;
                    break;
                }
            if (!anyModLoaded) return null;
        }
        var builder = REGISTRATE.block(metal.id + "_floor", Block::new)
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.sound(metal.sound).mapColor(metal.color))
                .transform(pickaxeOnly());

        builder = builder.recipe((c, p) -> {
            var ingredient = metal.getIngredient();
            if (ingredient != null) p.stonecutting(ingredient, RecipeCategory.BUILDING_BLOCKS, c, 2);
        });
        if (metal.equals(AllMetalTypes.NETHERITE)) builder = builder.item().properties(p -> p.fireResistant()).build();
        else builder = builder.simpleItem();
        return builder.simpleItem().register();
    });

    @SuppressWarnings("all")
    public static final MetalTypeBlockList<LargeChain> METAL_TYPE_LARGE_CHAINS = new MetalTypeBlockList<LargeChain>(type -> {
        var metal = type.get();
        if (metal.requireMods()) return null;
        if (!DnDecor.LOAD_ALL_METALS) {
            if (metal.modIDs.equals(MaterialTypeProvider.NA)) return null;
            boolean anyModLoaded = metal.modIDs.isEmpty();
            if (metal.requireMods()) for (String mod : metal.modIDs)
                if (ModList.get().isLoaded(mod)) {
                    anyModLoaded = true;
                    break;
                }
            if (!anyModLoaded) return null;
        }

        var builder = REGISTRATE.block("large_" + metal.id + "_chain", LargeChain::new)
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.sound(DnDecorSoundTypes.CHAIN_HEAVY).mapColor(metal.color))
                .addLayer(() -> RenderType::cutout)
                .transform(pickaxeOnly())
                .blockstate((c, p) -> {
                    p.models().withExistingParent("block/" + c.getName(), DnDecor.asResource("block/large_chain")).texture("0", DnDecor.asResource("block/" + metal.id + "_large_chain"));
                    p.models().withExistingParent("block/" + c.getName() + "/block", DnDecor.asResource("block/large_chain")).texture("0", DnDecor.asResource("block/" + metal.id + "_large_chain"));
                    p.models().withExistingParent("block/" + c.getName() + "/item", DnDecor.asResource("block/large_chain")).texture("0", DnDecor.asResource("block/" + metal.id + "_large_chain"));
                    BlockStateGen.axisBlock(c, p, getBlockModel(true, c, p));
                }).tag(AllTags.AllBlockTags.BRITTLE.tag, BlockTags.CLIMBABLE);
        builder = builder.recipe((c, p) -> {
            var ingredient = metal.getIngredient();
            if (ingredient != null) p.stonecutting(ingredient, RecipeCategory.BUILDING_BLOCKS, c, 4);
        });
        if (metal.equals(AllMetalTypes.NETHERITE)) builder = builder.item().properties(p -> p.fireResistant()).build();
        else builder = builder.simpleItem();
        return builder.register();
    });

    public static final MetalTypeBoltBlockList<?> METAL_TYPE_BOLTS = new MetalTypeBoltBlockList<>(type -> {
        var metal = type.get();
        if (!DnDecor.LOAD_ALL_METALS) {
            if (metal.modIDs.equals(MaterialTypeProvider.NA)) return null;
            boolean anyModLoaded = metal.modIDs.isEmpty();
            if (metal.requireMods()) for (String mod : metal.modIDs) if (ModList.get().isLoaded(mod)) { anyModLoaded = true; break; }
            if (!anyModLoaded) return null;
        }
        return new BoltEntry<>(metal);
    });

    private static CTSpriteShiftEntry horizontal(String blockTextureName, String connectedTextureName) {
        return getCT(AllCTTypes.HORIZONTAL, blockTextureName, connectedTextureName);
    }
    private static CTSpriteShiftEntry horizontalKryppers(String blockTextureName, String connectedTextureName) {
        return getCT(AllCTTypes.HORIZONTAL_KRYPPERS, blockTextureName, connectedTextureName);
    }
    private static CTSpriteShiftEntry vertical(String blockTextureName, String connectedTextureName) {
        return getCT(AllCTTypes.VERTICAL, blockTextureName, connectedTextureName);
    }
    private static CTSpriteShiftEntry rectangle(String blockTextureName, String connectedTextureName) {
        return getCT(AllCTTypes.RECTANGLE, blockTextureName, connectedTextureName);
    }
    private static CTSpriteShiftEntry cross(String blockTextureName, String connectedTextureName) {
        return getCT(AllCTTypes.CROSS, blockTextureName, connectedTextureName);
    }
    private static CTSpriteShiftEntry roof(String blockTextureName, String connectedTextureName) {
        return getCT(AllCTTypes.ROOF, blockTextureName, connectedTextureName);
    }
    private static CTSpriteShiftEntry roofStair(String blockTextureName, String connectedTextureName) {
        return getCT(AllCTTypes.ROOF_STAIR, blockTextureName, connectedTextureName);
    }
    private static CTSpriteShiftEntry omni(String blockTextureName, String connectedTextureName) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, blockTextureName, connectedTextureName);
    }

    private static CTSpriteShiftEntry horizontal(String texture) {
        return getCT(AllCTTypes.HORIZONTAL, texture, texture);
    }
    private static CTSpriteShiftEntry horizontalKryppers(String texture) {
        return getCT(AllCTTypes.HORIZONTAL_KRYPPERS, texture, texture);
    }
    private static CTSpriteShiftEntry vertical(String texture) {
        return getCT(AllCTTypes.VERTICAL, texture, texture);
    }
    private static CTSpriteShiftEntry rectangle(String texture) {
        return getCT(AllCTTypes.RECTANGLE, texture, texture);
    }
    private static CTSpriteShiftEntry cross(String texture) {
        return getCT(AllCTTypes.CROSS, texture, texture);
    }
    private static CTSpriteShiftEntry roof(String texture) {
        return getCT(AllCTTypes.ROOF, texture, texture);
    }
    private static CTSpriteShiftEntry roofStair(String texture) {
        return getCT(AllCTTypes.ROOF_STAIR, texture, texture);
    }
    private static CTSpriteShiftEntry omni(String texture) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, texture, texture);
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, DnDecor.asResource("block/" + blockTextureName),
                DnDecor.asResource("block/" + connectedTextureName + "_connected"));
    }

    protected static String getItemName(ItemLike pItemLike) {
        return BuiltInRegistries.ITEM.getKey(pItemLike.asItem()).getPath();
    }

    public static <T extends Block> Function<BlockState, ModelFile> getBlockModel(boolean customItem, DataGenContext<Block, T> c, RegistrateBlockstateProvider p) {
        return $ -> customItem ? AssetLookup.partialBaseModel(c, p) : AssetLookup.standardModel(c, p);
    }

    public static void register() {
    }
}
