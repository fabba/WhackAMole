WhackAMole
==========

Naitive app studio project UvA

We implement a game of whack the mole. The purpose of the game is that when you see a mole you must whack ( click ) it.


Features

- Our app consist of several moles

- Our app maintains a history of high scores that’s displayed anytime a game is won or lost. The history of high scores should persist even when your app is backgrounded or force-quit

- Our app consist of four levels with different backgrounds and different unique moles. If you finished these four levels there will be more moles and the moles will disappear quicker.

- The moles will be generated random

- When the users quits the game, it can resume the game at the start of a level.

- Our app consist of title, a logo and one button that starts a new game.

- When settings are changed, they should only take effect for new games, not one already in progress, if any.

- There will be a page with a manual on how to play the game and what the ability is of each mole

- At the start you have five lives, for each mole you miss one life will be deleted. If all your lives are gone, you're score will be displayed and you will be linked to the highscore.

- At the end of each round ( four levels ) you get one life extra.

- If you finish a round ( four levels ) at the settings you can decide to always start at the next round. The points will be equal to if you wack all the moles in the previous rounds and the user will start with 5 lives


Technical requirements

- Our app is implemented with the Android 4 SDK1.

- Our app’s UI is sized for a standard smart phone resolution. H

- We implement unit tests for your UI (i.e., application tests) and any models (i.e., logic tests).

- We use git and GitHub for version control.

- Our app works within Android Simulator; you need not test it on actual hardware.

- We use sqldatabse to save the topscore of the user and to save games.

- We use different sprites for the different moles.

- We implement the round selection with a slider control


Android specifics

- During game play, the user should be allowed to hit the MENU button on the Android device (or emulator) to cause a menu to appear and allow the user to reset the game, or to open the settings menu. This menu should only appear during game play and not during display of the high scores.

- Our app comes with default values for the app’s settings; those defaults should be set through a PreferenceActivity.


Moles ( Name / Ability / Points )

- Normy / Need 1 click to kill / 1

- Hatty / Need 2 clicks to kill / 2

- Tanky / Need 3 clicks to kill / 3

- Sniffy / Lose life if touched / 0 

- Fasty / Need 1 click to kill and appearance time is shorter / 2

- Goldy / Need 1 click to kill and lots of poits / 5

- Burny / Need 1 click to kill and will burn others ( this will cost you a life ) / 3

- Icy / Need 1 click to freeze the rest of the moles ( appearance time will be stuck for 2 seconds )/ 0






