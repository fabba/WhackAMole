TODO
==========

- Create more rounds and especially levels (and play test them quite a few times).
- Get time of current game, and not system time! (When pausing a game this can cause serious issues).
- Fix background bug when switching from game to menu and back again.
- Actualy make login work.
- Save game states.
- Allow resume.
- Tons of Todo's in the code itself.
- Should probably rename LevelModel to GameModel and move some logic to RoundModel.
- (EXTRA) Select specific level + round?

KNOWN BUGS
============

- Game crashes after fresh install with a new user on score insertion. Nullpointerexception -> user = null! Register/login fails on fresh install?
- Game crashes from time to time on switching from level 1 round 1 to level 1 round 2. (Cause unknown, concurrency issues?)
- A mole appearing in front of patch, location remains bug throughout the game. (Cause unknown)