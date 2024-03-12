## Features
```diff
+ display particle highlight now from 5 blocks away instead of 3
+ add highlighter to non-completed collections in /collections menu
+ locate structures now per tick from their respective npcs
    = Goblin Queen's Den waypoint is still only created, when finding the crystal
+ allow custom waypoints in Crystal Hollows via /skylper hollows waypoints add <name> <x> <y> <z>
```

## Bug Fixes

```diff
+ fix metal detector solver showing wrong waypoints
```

## Technical changes

```diff
+ add callbacks to events
    = the second generic in listenEvent is now the return type of the event
    = there is a new Event interface, that takes the return type as a generic
+ add RenderItemBackground event, that takes a color as a callback
+ add FILLED_WITH_BEAM and FILLED Waypoint type
+ remove state of metal detector solver and just check for existing successWaypoint
+ add LocatedHollowsStructureEvent
+ add ktlint to workflows and build project after commit
```
