package dev.nyon.skylper.skyblock.mining.hollows.tracker.powder

import java.util.regex.Pattern

enum class ChestReward(val displayName: String, val pattern: Pattern) {
    MITHRIL_POWDER("Mithril Powder", "You received (?<amount>.*) Mithril Powder.".toPattern()),
    GEMSTONE_POWDER("Gemstone Powder", "You received (?<amount>.*) Gemstone Powder.".toPattern()),
    GLACITE_POWDER("Glacite Powder", "You received (?<amount>.*) Glacite Powder.".toPattern()),

    ROUGH_RUBY_GEMSTONE(
        "Rough Ruby Gemstone",
        "You received (?<amount>.*) ❤ Rough Ruby Gemstone.".toPattern()
    ),
    FLAWED_RUBY_GEMSTONE(
        "Flawed Sapphire Gemstone",
        "You received (?<amount>.*) ❤ Flawed Ruby Gemstone.".toPattern()
    ),
    FINE_RUBY_GEMSTONE(
        "Fine Ruby Gemstone",
        "You received (?<amount>.*) ❤ Fine Ruby Gemstone.".toPattern()
    ),
    FLAWLESS_RUBY_GEMSTONE(
        "Flawless Ruby Gemstone",
        "You received (?<amount>.*) ❤ Flawless Ruby Gemstone.".toPattern()
    ),

    ROUGH_SAPPHIRE_GEMSTONE(
        "Rough Sapphire Gemstone",
        "You received (?<amount>.*) ✎ Rough Sapphire Gemstone.".toPattern()
    ),
    FLAWED_SAPPHIRE_GEMSTONE(
        "Flawed Sapphire Gemstone",
        "You received (?<amount>.*) ✎ Flawed Sapphire Gemstone.".toPattern()
    ),
    FINE_SAPPHIRE_GEMSTONE(
        "Fine Sapphire Gemstone",
        "You received (?<amount>.*) ✎ Fine Sapphire Gemstone.".toPattern()
    ),
    FLAWLESS_SAPPHIRE_GEMSTONE(
        "Flawless Sapphire Gemstone",
        "You received (?<amount>.*) ✎ Flawless Sapphire Gemstone.".toPattern()
    ),

    ROUGH_AMBER_GEMSTONE(
        "Rough Amber Gemstone",
        "You received (?<amount>.*) ⸕ Rough Amber Gemstone.".toPattern()
    ),
    FLAWED_AMBER_GEMSTONE(
        "Flawed Amber Gemstone",
        "You received (?<amount>.*) ⸕ Flawed Amber Gemstone.".toPattern()
    ),
    FINE_AMBER_GEMSTONE(
        "Fine Amber Gemstone",
        "You received (?<amount>.*) ⸕ Fine Amber Gemstone.".toPattern()
    ),
    FLAWLESS_AMBER_GEMSTONE(
        "Flawless Amber Gemstone",
        "You received (?<amount>.*) ⸕ Flawless Amber Gemstone.".toPattern()
    ),

    ROUGH_AMETHYST_GEMSTONE(
        "Rough Amethyst Gemstone",
        "You received (?<amount>.*) ❈ Rough Amethyst Gemstone.".toPattern()
    ),
    FLAWED_AMETHYST_GEMSTONE(
        "Flawed Amethyst Gemstone",
        "You received (?<amount>.*) ❈ Flawed Amethyst Gemstone.".toPattern()
    ),
    FINE_AMETHYST_GEMSTONE(
        "Fine Amethyst Gemstone",
        "You received (?<amount>.*) ❈ Fine Amethyst Gemstone.".toPattern()
    ),
    FLAWLESS_AMETHYST_GEMSTONE(
        "Flawless Amethyst Gemstone",
        "You received (?<amount>.*) ❈ Flawless Amethyst Gemstone.".toPattern()
    ),

    ROUGH_JADE_GEMSTONE(
        "Rough Jade Gemstone",
        "You received (?<amount>.*) ☘ Rough Jade Gemstone.".toPattern()
    ),
    FLAWED_JADE_GEMSTONE(
        "Flawed Jade Gemstone",
        "You received (?<amount>.*) ☘ Flawed Jade Gemstone.".toPattern()
    ),
    FINE_JADE_GEMSTONE(
        "Fine Jade Gemstone",
        "You received (?<amount>.*) ☘ Fine Jade Gemstone.".toPattern()
    ),
    FLAWLESS_JADE_GEMSTONE(
        "Flawless Jade Gemstone",
        "You received (?<amount>.*) ☘ §5Flawless Jade Gemstone.".toPattern()
    ),

    ROUGH_TOPAZ_GEMSTONE(
        "Rough Topaz Gemstone",
        "You received (?<amount>.*) ✧ Rough Topaz Gemstone.".toPattern()
    ),
    FLAWED_TOPAZ_GEMSTONE(
        "Flawed Topaz Gemstone",
        "You received (?<amount>.*) ✧ Flawed Topaz Gemstone.".toPattern()
    ),
    FINE_TOPAZ_GEMSTONE(
        "Fine Topaz Gemstone",
        "You received (?<amount>.*) ✧ Fine Topaz Gemstone.".toPattern()
    ),
    FLAWLESS_TOPAZ_GEMSTONE(
        "Flawless Topaz Gemstone",
        "You received (?<amount>.*) ✧ Flawless Topaz Gemstone.".toPattern()
    ),

    FTX_3070("FTX 3070", "You received (?<amount>.*) FTX 3070.".toPattern()),
    ELECTRON_TRANSIMTTER(
        "Electron Transmitter",
        "You received (?<amount>.*) Electron Transmitter.".toPattern()
    ),
    ROBOTRON_REFLECTOR(
        "Robotron Reflector",
        "You received (?<amount>.*) Robotron Reflector.".toPattern()
    ),
    SUPERLITE_MOTOR("Superlite Motor", "You received (?<amount>.*) Superlite Motor.".toPattern()),
    CONTROL_SWITCH("Control Switch", "You received (?<amount>.*) Control Switch.".toPattern()),
    SYNTHETIC_HEART("Synthetic Heart", "You received (?<amount>.*) Synthetic Heart.".toPattern()),

    GOBLIN_EGG("Goblin Egg", "You received (?<amount>.*) Goblin Egg.".toPattern()),
    GREEN_GOBLIN_EGG(
        "Green Goblin Egg",
        "You received (?<amount>.*) Green Goblin Egg.".toPattern()
    ),
    RED_GOBLIN_EGG("Red Goblin Egg", "You received (?<amount>.*) Red Goblin Egg.".toPattern()),
    YELLOW_GOBLIN_EGG(
        "Yellow Goblin Egg",
        "You received (?<amount>.*) Yellow Goblin Egg.".toPattern()
    ),
    BLUE_GOBLIN_EGG("Blue Goblin Egg", "You received (?<amount>.*) Blue Goblin Egg.".toPattern()),

    WISHING_COMPASS("Wishing Compass", "You received (?<amount>.*) Wishing Compass.".toPattern()),

    SLUDGE_JUICE("Sludge Juice", "You received (?<amount>.*) Sludge Juice.".toPattern()),
    ASCENSION_ROPE("Ascension Rope", "You received (?<amount>.*) Ascension Rope.".toPattern()),
    TREASURITE("Treasurite", "You received (?<amount>.*) §5Treasurite.".toPattern()),
    JUNGLE_HEART("Jungle Heart", "You received (?<amount>.*) Jungle Heart.".toPattern()),
    PICKONIMBUS_2000("Pickonimbus 2000", "You received (?<amount>.*) §5Pickonimbus 2000.".toPattern()),
    YOGGIE("Yoggie", "You received (?<amount>.*) Yoggie.".toPattern()),
    PREHISTORIC_EGG("Prehistoric Egg", "You received (?<amount>.*) Prehistoric Egg.".toPattern()),
    OIL_BARREL("Oil Barrel", "You received (?<amount>.*) Oil Barrel.".toPattern()),

    DIAMOND_ESSENCE("Diamond Essence", "You received [+](?<amount>.*) Diamond Essence.".toPattern()),
    GOLD_ESSENCE("Gold Essence", "You received [+](?<amount>.*) Gold Essence.".toPattern())
}
