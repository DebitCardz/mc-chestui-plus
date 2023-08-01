<h3 align=center>
  <img src="https://i.imgur.com/3Sk1buV.png"/><br><br>
</h3>

# ChestUI+ [![](https://jitpack.io/v/DebitCardz/mc-chestui-plus.svg)](https://jitpack.io/#DebitCardz/mc-chestui-plus)

ChestUI+ is a fork and slight recode of [**mc-chestui**](https://github.com/hazae41/mc-chestui), originally developed
by, [**hazae41**](https://github.com/hazae41/).

The primary aim of this project was to add additional features to the original project and redo a few elements to better
suite my own personal needs from the project.

### Gradle

```kts
repositories {
  maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.DebitCardz:mc-chestui-plus:0.0.8")
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
    <version>0.0.8</version>
</dependency>

```

## Supported Versions
* 1.19.4
* 1.20
* 1.20.1

*ChestUI+ will only work properly on versions 1.19.4 and above due to issues with Kyori Components.*

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

Thanks for checking out this project! And for further examples of what you can do you can checkout the original page
for [**mc-chestui**](https://github.com/hazae41/mc-chestui).

# Examples

## GUI Creation

```kotlin
fun mainGUI(): GUI {
    return gui(
        plugin = this,
        title = Component.text("Example GUI", NamedTextColor.GOLD),
        type = GUIType.CHEST,
        rows = 1
    ) {
        slot(1, 1) {
            item = item(Material.STONE) {
                name = Component.text("Cool stone!")
            }
        }
    }
}
```

## Hoppers & Dispensers Are Supported

```kotlin
fun hopperGUI(): GUI {
    return gui(
        plugin = this,
        title = Component.text("Hopper GUI", NamedTextColor.GOLD),
        type = GUIType.HOPPER
    ) {
        slot(1, 1) {
            item = item(Material.CAKE) {
                name = Component.text("Cooler Cake")
            }
        }
    }
}
```

```kotlin
fun dispenserGUI(): GUI {
    return gui(
        plugin = this,
        title = Component.text("Dispenser GUI", NamedTextColor.GOLD),
        type = GUIType.DISPENSER
    ) {
        slot(1, 1) {
            item = item(Material.CAKE) {
                name = Component.text("Dispensed Cake")
            }
        }
    }
}
```

You don't need to specify the rows with these types since their rows will stay static.

## Skull Support

```kotlin
fun headGUI(): GUI {
    return gui(
        plugin = this,
        title = Component.text("Head GUI", NamedTextColor.GOLD),
        type = GUIType.DiSPENSER
    ) {
        // Middle of the dispenser.
        slot(2, 2) {
            item = item(Material.PLAYER_HEAD) {
                name = Component.text("Your head!", NamedTextColor.RED)
                skullOwner = player
            }
        }
    }
}
```

We use a Player or OfflinePlayer object to assign the skull owner, this also supports Player Profiles for skulls.

## Glowing Items Support

```kotlin
fun glowingHeadGUI(): GUI {
    return gui(
        plugin = this,
        title = Component.text("Glowing Head GUI", NamedTextColor.GOLD),
        type = GUIType.DISPENSER
    ) {
        slot(2, 2) {
            item = item(Material.PLAYER_HEAD) {
                name = Component.text("Your glowing head!", NamedTextColor.GOLD)
                skulOwner = player
                glowing = true
            }
        }
    }
}
```

You can make items glow in a GUI, it automatically hides the enchantments of the item if you do this as well.

## Fill The Border Of Your GUI

```kotlin
fun filledBorderGUI(): GUI {
    return gui(
        plugin = this,
        title = Component.text("Chest GUI", NamedTextColor.GOLD),
        type = GUIType.CHEST,
        rows = 3
    ) {
        fillBorder {
            item = item(Material.GRAY_STAINED_GLASS_PANE) {
                name = Component.empty()
            }
        }
    }
}
```

This'll automatically outline your GUI with whatever item you put as input.

## Set the Next Available Slot in a GUI

```kotlin
fun nextAvailableSlotUi(): GUI {
    return gui(
        plugin = this,
        title = Component.text("Chest GUI", NamedTextColor.GOLD),
        type = GUIType.CHEST,
        rows = 3
    ) {
        fillBorder {
            item = item(Material.GRAY_STAINED_GLASS_PANE) {
                name = Component.empty()
            }
        }
        
        for(_ in 0..5) {
            nextAvailableSlot {
                item = item(Material.STONE)
            }
        }
    }
}
```

This will avoid the slots already filled by the border glass and fill the slots of 
the inventory that are AIR. Any null item slot will be overridden as this method only 
checks for slots occupied by an ItemStack.

## Check when ItemStacks are placed into the GUI

```kotlin
fun itemPlacement(): GUI {
    return gui(
        plugin = this,
        title = Component.text("Chest GUI", NamedTextColor.GOLD),
        type = GUIType.CHEST,
        rows = 3
    ) {
        allowItemPlacement = true
        
        onPlaceItem = { player, item, slot ->
            player.sendMessage("You have placed ${item.type.name} in slot ${slot}.")            
        }
    }
}
```

Allow for items to be placed into the GUI and check when they are placed with a simple anonymous function, if it is placed over a slot that is taken (even if the slot is AIR) the event will be cancelled.

## Check when ItemStacks are dragged into the GUI

```kotlin
fun itemDrag(): GUI {
    return gui(
        plugin = this,
        title = Component.text("Chest GUI", NamedTextColor.GOLD),
        type = GUIType.CHEST,
        rows = 3
    ) {
        allowItemPlacement = true
        
        // items is a Map of <Int, ItemStack>.
        onDragItem = { player, items ->
            for((slot, itemStack) in items) {
                player.sendMessage("You have placed ${item.type.name} in slot ${slot}.")
            }
        }
    }
}
```

Similarly to `onPlaceItem` we can also detect when items are dragged into the GUI, if they are dragged over a slot that is taken (even if the slot is AIR) the event will be cancelled.

## Prevent GUI Listener from being unregistered

```kotlin
fun singleInstanceGui(): GUI {
    return gui(
        plugin = this,
        title = Component.text("Chest GUI", NamedTextColor.GOLD),
        type = GUIType.CHEST,
        rows = 3
    ) {
        automaticallyUnregisterListener = false
        
        slot(4, 1) {
            item = item(Material.CAKE) {
                name = Component.text("Cake", NamedTextColor.GOLD)
            }
        }
    }
}

val gui = singleInstanceGui()

getCommand("openUi").setExecutor(object : CommandExecutor {
    override fun onCommand(sender: Player) {
        sender.openGUI(gui)
    }
})
```

If a single instance of a GUI is being used to be opened to multiple people or is cached it's
extremely important to mark `automaticallyUnregisterListener` as false or the `Listener` attached to the `GUI` 
will be unregistered once all initial viewers of the `GUI` exit the menu resulting in undefined behavior. 