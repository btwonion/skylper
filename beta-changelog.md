## Features
```diff
+ display particle highlight now from 5 blocks away instead of 3
+ add highlighter to non-completed collections in /collections menu
```

## Bug Fixes

```diff
```

## Technical changes

```diff
+ add callbacks to events
    = the second generic in listenEvent is now the return type of the event
    = there is a new Event interface, that takes the return type as a generic
+ add RenderItemBackground event, that takes a color as a callback
```