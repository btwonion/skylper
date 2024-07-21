package dev.nyon.skylper.skyblock.mining.hollows.tracker.powder

import dev.nyon.skylper.extensions.regex

enum class ChestReward(val displayName: String, val regexKey: String) {
    MITHRIL_POWDER("Mithril Powder", "mithrilpowder"),
    GEMSTONE_POWDER("Gemstone Powder", "gemstonepowder"),
    ROUGH_RUBY_GEMSTONE(
        "Rough Ruby Gemstone", "roughrubygemstone"
    ),
    FLAWED_RUBY_GEMSTONE(
        "Flawed Sapphire Gemstone", "flawedrubygemstone"
    ),
    FINE_RUBY_GEMSTONE(
        "Fine Ruby Gemstone", "finerubygemstone"
    ),
    FLAWLESS_RUBY_GEMSTONE(
        "Flawless Ruby Gemstone", "flawlessrubygemstone"
    ),

    ROUGH_SAPPHIRE_GEMSTONE(
        "Rough Sapphire Gemstone", "roughsapphiregemstone"
    ),
    FLAWED_SAPPHIRE_GEMSTONE(
        "Flawed Sapphire Gemstone", "flawedsapphiregemstone"
    ),
    FINE_SAPPHIRE_GEMSTONE(
        "Fine Sapphire Gemstone", "finesapphiregemstone"
    ),
    FLAWLESS_SAPPHIRE_GEMSTONE(
        "Flawless Sapphire Gemstone", "flawlesssapphiregemstone"
    ),

    ROUGH_AMBER_GEMSTONE(
        "Rough Amber Gemstone", "roughambergemstone"
    ),
    FLAWED_AMBER_GEMSTONE(
        "Flawed Amber Gemstone", "flawedambergemstone"
    ),
    FINE_AMBER_GEMSTONE(
        "Fine Amber Gemstone", "fineambergemstone"
    ),
    FLAWLESS_AMBER_GEMSTONE(
        "Flawless Amber Gemstone", "flawlessambergemstone"
    ),

    ROUGH_AMETHYST_GEMSTONE(
        "Rough Amethyst Gemstone", "roughamethystgemstone"
    ),
    FLAWED_AMETHYST_GEMSTONE(
        "Flawed Amethyst Gemstone", "flawedamethystgemstone"
    ),
    FINE_AMETHYST_GEMSTONE(
        "Fine Amethyst Gemstone", "fineamethystgemstone"
    ),
    FLAWLESS_AMETHYST_GEMSTONE(
        "Flawless Amethyst Gemstone", "flawlessamethystgemstone"
    ),

    ROUGH_JADE_GEMSTONE(
        "Rough Jade Gemstone", "roughjadegemstone"
    ),
    FLAWED_JADE_GEMSTONE(
        "Flawed Jade Gemstone", "flawedjadegemstone"
    ),
    FINE_JADE_GEMSTONE(
        "Fine Jade Gemstone", "finejadegemstone"
    ),
    FLAWLESS_JADE_GEMSTONE(
        "Flawless Jade Gemstone", "flawlessjadegemstone"
    ),

    ROUGH_TOPAZ_GEMSTONE(
        "Rough Topaz Gemstone", "roughtopazgemstone"
    ),
    FLAWED_TOPAZ_GEMSTONE(
        "Flawed Topaz Gemstone", "flawedtopazgemstone"
    ),
    FINE_TOPAZ_GEMSTONE(
        "Fine Topaz Gemstone", "finetopazgemstone"
    ),
    FLAWLESS_TOPAZ_GEMSTONE(
        "Flawless Topaz Gemstone", "flawlesstopazgemstone"
    ),

    FTX_3070("FTX 3070", "ftx3070"),
    ELECTRON_TRANSIMTTER(
        "Electron Transmitter", "electrontransimtter"
    ),
    ROBOTRON_REFLECTOR(
        "Robotron Reflector", "robotronreflector"
    ),
    SUPERLITE_MOTOR("Superlite Motor", "superlitemotor"),
    CONTROL_SWITCH("Control Switch", "controlswitch"),
    SYNTHETIC_HEART("Synthetic Heart", "syntheticheart"),

    GOBLIN_EGG("Goblin Egg", "goblinegg"),
    GREEN_GOBLIN_EGG(
        "Green Goblin Egg", "greengoblinegg"
    ),
    RED_GOBLIN_EGG("Red Goblin Egg", "redgoblinegg"),
    YELLOW_GOBLIN_EGG(
        "Yellow Goblin Egg", "yellowgoblinegg"
    ),
    BLUE_GOBLIN_EGG("Blue Goblin Egg", "bluegoblinegg"),

    WISHING_COMPASS("Wishing Compass", "wishingcompass"),

    SLUDGE_JUICE("Sludge Juice", "sludgejuice"),
    ASCENSION_ROPE("Ascension Rope", "ascensionrope"),
    TREASURITE("Treasurite", "treasurite"),
    JUNGLE_HEART("Jungle Heart", "jungleheart"),
    PICKONIMBUS_2000("Pickonimbus 2000", "pickonimbus2000"),
    YOGGIE("Yoggie", "yoggie"),
    PREHISTORIC_EGG("Prehistoric Egg", "prehistoricegg"),
    OIL_BARREL("Oil Barrel", "oilbarrel"),

    DIAMOND_ESSENCE("Diamond Essence", "diamondessence"),
    GOLD_ESSENCE("Gold Essence", "goldessence");

    fun getRegex(): Regex {
        return regex("mining.powder.tracker.reward.$regexKey.new")
    }
}
