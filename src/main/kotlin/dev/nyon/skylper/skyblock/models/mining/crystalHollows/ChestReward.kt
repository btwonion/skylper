package dev.nyon.skylper.skyblock.models.mining.crystalHollows

import dev.nyon.skylper.extensions.regex

enum class ChestReward(val displayName: String) {
    MITHRIL_POWDER("Mithril Powder"),
    GEMSTONE_POWDER("Gemstone Powder"),
    ROUGH_RUBY_GEMSTONE("Rough Ruby Gemstone"),
    FLAWED_RUBY_GEMSTONE("Flawed Sapphire Gemstone"),
    FINE_RUBY_GEMSTONE("Fine Ruby Gemstone"),
    FLAWLESS_RUBY_GEMSTONE("Flawless Ruby Gemstone"),

    ROUGH_SAPPHIRE_GEMSTONE("Rough Sapphire Gemstone"),
    FLAWED_SAPPHIRE_GEMSTONE("Flawed Sapphire Gemstone"),
    FINE_SAPPHIRE_GEMSTONE("Fine Sapphire Gemstone"),
    FLAWLESS_SAPPHIRE_GEMSTONE("Flawless Sapphire Gemstone"),

    ROUGH_AMBER_GEMSTONE("Rough Amber Gemstone"),
    FLAWED_AMBER_GEMSTONE("Flawed Amber Gemstone"),
    FINE_AMBER_GEMSTONE("Fine Amber Gemstone"),
    FLAWLESS_AMBER_GEMSTONE("Flawless Amber Gemstone"),

    ROUGH_AMETHYST_GEMSTONE("Rough Amethyst Gemstone"),
    FLAWED_AMETHYST_GEMSTONE("Flawed Amethyst Gemstone"),
    FINE_AMETHYST_GEMSTONE("Fine Amethyst Gemstone"),
    FLAWLESS_AMETHYST_GEMSTONE("Flawless Amethyst Gemstone"),

    ROUGH_JADE_GEMSTONE("Rough Jade Gemstone"),
    FLAWED_JADE_GEMSTONE("Flawed Jade Gemstone"),
    FINE_JADE_GEMSTONE("Fine Jade Gemstone"),
    FLAWLESS_JADE_GEMSTONE("Flawless Jade Gemstone"),

    ROUGH_TOPAZ_GEMSTONE("Rough Topaz Gemstone"),
    FLAWED_TOPAZ_GEMSTONE("Flawed Topaz Gemstone"),
    FINE_TOPAZ_GEMSTONE("Fine Topaz Gemstone"),
    FLAWLESS_TOPAZ_GEMSTONE("Flawless Topaz Gemstone"),

    FTX_3070("FTX 3070"),
    ELECTRON_TRANSIMTTER("Electron Transmitter"),
    ROBOTRON_REFLECTOR("Robotron Reflector"),
    SUPERLITE_MOTOR("Superlite Motor"),
    CONTROL_SWITCH("Control Switch"),
    SYNTHETIC_HEART("Synthetic Heart"),

    GOBLIN_EGG("Goblin Egg"),
    GREEN_GOBLIN_EGG("Green Goblin Egg"),
    RED_GOBLIN_EGG("Red Goblin Egg"),
    YELLOW_GOBLIN_EGG("Yellow Goblin Egg"),
    BLUE_GOBLIN_EGG("Blue Goblin Egg"),

    WISHING_COMPASS("Wishing Compass"),

    SLUDGE_JUICE("Sludge Juice"),
    ASCENSION_ROPE("Ascension Rope"),
    TREASURITE("Treasurite"),
    JUNGLE_HEART("Jungle Heart"),
    PICKONIMBUS_2000("Pickonimbus 2000"),
    YOGGIE("Yoggie"),
    PREHISTORIC_EGG("Prehistoric Egg"),
    OIL_BARREL("Oil Barrel"),

    DIAMOND_ESSENCE("Diamond Essence"),
    GOLD_ESSENCE("Gold Essence");

    fun getRegex(): Regex {
        return regex("chat.hollows.tracker.reward.${name.lowercase().replace("_", "")}")
    }
}
