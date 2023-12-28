## IMPORTANT!
This is a version of Blueprint developed on 1.20.4 to work with the Architectury toolchain, meaning the existing content is/will be converted into common code shared between NeoForge and Fabric projects. This fork DOES NOT reflect the development of the official mod! Team Abnormals has no plans to port their mods to Fabric as of current. I use this project more-so as a workspace to test out multiplatform code I could end up using in the future.

*This fork also includes (or will include) some of my own additions:*
- A forge-like but modular biome modifier system; "selectors" and "modifiers" can be added separately in the same JSON file with a high degree of customizability. Some "selectors" use information unique to this system, such as reading what features or climate values were present before modification
- A direct port of NeoForge's config API to work on both platforms
- A port of NeoForge's codec-based resource condition API to work with Fabric's implementation
  - Also make the Blueprint config condition stuff work with this
- Use SimpleEvent to create common versions of events Blueprint or other mods may use
- Support for datagen on both platforms from the common project, done with custom data providers to avoid reflection fuckery on Forge

Anyway, continue on...

![Banner](https://i.imgur.com/2feJFS9.png)
<p align="center">
    <a href="https://www.teamabnormals.com/" align="center"><img alt="Team Abnormals Website" src="http://bit.ly/abnormalswebbadge"></a>
    <a href="https://www.teamabnormals.com/discord" align="center"><img alt="Team Abnormals Discord" src="https://img.shields.io/discord/650003402218274816?label=&color=014980&labelColor=537DB5&style=for-the-badge&logo=Discord&logoColor=DDE4EF"></a>
    <a href="https://twitter.com/TeamAbnormals" align="center"><img alt="Team Abnormals Twitter" src="https://img.shields.io/twitter/follow/teamabnormals?label=&color=014980&labelColor=537DB5&style=for-the-badge&logo=Twitter&logoColor=DDE4EF"></a>
    <a href="https://www.patreon.com/teamabnormals" align="center"><img alt="Team Abnormals Patreon" src="https://img.shields.io/endpoint?label=&color=014980&labelColor=537DB5&style=for-the-badge&logo=Patreon&logoColor=DDE4EF&url=https://shieldsio-patreon.vercel.app/api/?username=teamabnormals&type=patrons"></a>
</p>

![](https://i.imgur.com/U7uo5Va.png)
## **📖 About**
Blueprint is a mod library developed for easily accessing code which is shared across most Team Abnormals mods.
It comes with many useful features, such as a registry helper, data syncing, various data-driven modification systems, and the Endimator animation API.

![](https://i.imgur.com/U7uo5Va.png)
## **💻 For Developers**
Adding Blueprint to your mod is quite simple!

First off you need to add Blueprint as a dependency to access the library in code. To do so, add the following into your `build.gradle`:
```
repositories {
    maven {
        url = "https://maven.jaackson.me"
    }
}

dependencies {
    implementation fg.deobf("com.teamabnormals:blueprint:<version>")
}
```
Replace `<version>` with the desired version of Blueprint, including the desired version of Minecraft.<br />
For example, `1.20.1-7.0.0` will give us `blueprint-1.20.1-7.0.0.jar`.

Next, you need to add two properties to your runs in your `build.gradle`:
```
property "mixin.env.remapRefMap", "true"
property "mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg"
```
Your runs in your `build.gradle` are found under `minecraft{runs{}}`.

Next you need to add it as a dependecy on Forge to make your mod require Blueprint when loading. In your `mods.toml`, add the following block to the file:
```
[[dependencies.<modId>]]
    modId = "blueprint"
    mandatory = true
    versionRange = "[<version>,)"
    ordering = "BEFORE"
    side = "BOTH"
```
Replace `<version>` with the desired version of Blueprint.<br />
For example, `7.0.0` will target version `7.0.0` of Blueprint.<br />
The code block above for the `mods.toml` is targeting the version selected and any versions beyond. If you want to target it differently, you may want to read up on the `mods.toml` spec.

![](https://i.imgur.com/U7uo5Va.png)
## **📦 Our Mods**
-   [Abnormals Delight](https://www.curseforge.com/minecraft/mc-mods/abnormals-delight)
-   [Allurement](https://www.curseforge.com/minecraft/mc-mods/allurement)
-   [Atmospheric](https://www.curseforge.com/minecraft/mc-mods/atmospheric)
-   [Autumnity](https://www.curseforge.com/minecraft/mc-mods/autumnity)
-   [Bamboo Blocks](https://www.curseforge.com/minecraft/mc-mods/bamboo-blocks)
-   [Berry Good](https://www.curseforge.com/minecraft/mc-mods/berry-good)
-   [Buzzier Bees](https://www.curseforge.com/minecraft/mc-mods/buzzier-bees)
-   [Endergetic Expansion](https://www.curseforge.com/minecraft/mc-mods/endergetic)
-   [Environmental](https://www.curseforge.com/minecraft/mc-mods/environmental)
-   [Extra Boats](https://www.curseforge.com/minecraft/mc-mods/extra-boats)
-   [Neapolitan](https://www.curseforge.com/minecraft/mc-mods/neapolitan)
-   [Personality](https://www.curseforge.com/minecraft/mc-mods/personality)
-   [Savage & Ravage](https://www.curseforge.com/minecraft/mc-mods/savage-and-ravage)
-   [Upgrade Aquatic](https://www.curseforge.com/minecraft/mc-mods/upgrade-aquatic)
