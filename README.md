# ChestUI+
ChestUI+ is a fork and slight recode of [**mc-chestui**](https://github.com/hazae41/mc-chestui), originally developed by, [**hazae41**](https://github.com/hazae41/).

The primary aim of this project was to add additional features to the original project and redo a few elements to better suite my own personal needs from the project.

# Changes From Original
### GUI
Changes within the GUI are as following

* GUIs now support Dispenser/Hopper types.
* Shift clicking items from the players inventory into the GUI is now toggleable.
* You can now use the fillBorder method to outline your GUI with whatever item you desire, especially useful for repetitive placeholder items.

### Item
* Toggleable item glow.
* Modifiable skull owners.

### Other
* Switched from using Strings to using Kyori TextComponents for all text.

Thanks for checking out this project! And for further examples of what you can do you can checkout the original page for [**mc-chestui**](https://github.com/hazae41/mc-chestui).

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
    	slot(0, 0) {
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
        // Hoppers only stay at Y=0, you shouldn't need to change it.
    	slot(0, 0) {
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
    	slot(0, 0) {
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
fun myHeadInAGUI(): GUI {
	return gui(
		plugin = this,
		title = Component.text("Dispenser GUI", NamedTextColor.GOLD),
		type = GUIType.DISPENSER
	) {
		slot(1, 1) {
			item = item(Material.PLAYER_HEAD) {
				name = Component.text("Your Head!", NamedTextColor.RED)
                skullOwner = player
			}
		}
	}
}
```
We use a Player or OfflinePlayer object to assign the skull owner, this also supports Player Profiles for skulls.

## Glowing Items Support
```kotlin
fun myHeadIsNowGlowingInAGUI(): GUI {
	return gui(
		plugin = this,
		title = Component.text("Dispenser GUI", NamedTextColor.GOLD),
		type = GUIType.DISPENSER
	) {
		slot(1, 1) {
			item = item(Material.PLAYER_HEAD) {
				name = Component.text("Your Glowing Head!", NamedTextColor.RED)
                skullOwner = player
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
        		name = Component.text("")
            }
        }
	}
}
```
This'll automatically outline your GUI with whatever item you put as input.
