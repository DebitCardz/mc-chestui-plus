<h3 align=center>
  <img src="https://i.imgur.com/3Sk1buV.png"/><br><br>
</h3>

# ChestUI+ [![](https://jitpack.io/v/DebitCardz/mc-chestui-plus.svg)](https://jitpack.io/#DebitCardz/mc-chestui-plus)

ChestUI+ is a fork and slight recode of [**mc-chestui**](https://github.com/hazae41/mc-chestui), originally developed
by, [**hazae41**](https://github.com/hazae41/).

The primary aim of this project was to add additional features to the original project and redo a few elements to better
suite my own personal needs from the project.

## Documentation
Documentation of features of the library can be found [**here**](https://github.com/DebitCardz/mc-chestui-plus/wiki).

### Gradle

```kts
repositories {
  maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.DebitCardz:mc-chestui-plus:1.3.2")
}
```
### Maven
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.DebitCardz</groupId>
    <artifactId>mc-chestui-plus</artifactId>
    <version>1.3.2</version>
</dependency>

```

# Changes From Original

### GUI

Changes within the GUI are as following

* GUIs now support Dispenser/Hopper types.
* Shift clicking items from the players inventory into the GUI is now toggleable.
* You can now use the fillBorder method to outline your GUI with whatever item you desire, especially useful for
  repetitive placeholder items.
* Use the nextAvailableSlot method to set the next free slot in your GUI to the desired item, useful to set iterated
  items.
* Toggle flags on the GUI to enable or disable certain functionality.
* Easily attach events onto the GUI to listen for changes.

### Item

* Toggleable item glow.
* Modifiable skull owners.
* Modifiable customModelData.

### Other

* Switched from using Strings to using Kyori TextComponents for all text.

Thanks for checking out this project! And for further examples of what you can do you can checkout the [**documentation**](https://github.com/DebitCardz/mc-chestui-plus/wiki).

## Basic Usage

```kotlin
fun mainGUI(): GUI {
    return gui(
        // plugin instance.
        plugin = this, // plugin instance
        // inventory title.
        title = Component.text("Rendered UI"),
        // 1 row chest inventory.
        type = GUIType.Chest(rows = 1)
    ) {
        // render in first slot.
        slot(1, 1) { 
            item = item(Material.STONE) { 
                name = Component.text("Rendered Stone")
            }
        }
    }
}
```
