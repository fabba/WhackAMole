TODO
==========

- Create more rounds and especially levels (and play test them quite a few times).
- Get time of current game, and not system time! (When pausing a game this can cause serious issues).
- Fix background bug when switching from game to menu and back again.
- Actualy make login work.
- Tons of Todo's in the code itself.
- Should probably rename LevelModel to GameModel and move some logic to RoundModel.
- Select specific level + round, instead of settings a big menu of all levels/rounds (only allow the user to play levels/rounds he has 'unlocked').

KNOWN BUGS
============

- Repeatedly pressing back between main menu and game will eventually result in a crash, use onResume to restart game?
- Game crashes after fresh install with a new user on score insertion. Nullpointerexception -> user = null! Register/login fails on fresh install?
- Game crashes from time to time on switching from level 1 round 1 to level 1 round 2. (Cause unknown, concurrency issues?)
- A mole appearing in front of patch, location remains bug throughout the game. (Cause unknown)