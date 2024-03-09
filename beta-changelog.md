## Features
```diff
+ add chests per hour and chests per minute to powder grinding overlay
+ add treasure chest lock highlighter
+ change hud overlay title color to gold
```

## Bug Fixes

```diff
+ fix some NPEs
+ fix concurrent modification exception in TableHudWidget
+ fix data resetting of powder grinding tracker
+ now update pickaxe ability level from /hotm screen
```

## Technical changes

```diff
+ add function for filled box rendering
+ create new Tracker Api for future extendibility
+ change parameters of ParticleSpawnEvent
+ merge TrackerWidget into Tracker
+ refactor code
```