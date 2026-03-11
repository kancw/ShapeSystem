# ShapeSystem (Clevis)

A Java 2D vector graphics application that supports a CLI REPL and a Swing GUI. Users can create, move, group, and query shapes, with full undo/redo history and session logging.

## Features

- **Shapes**: `rectangle`, `square`, `circle`, `line`
- **Grouping**: group multiple shapes under one name; ungroup to split them back
- **Manipulation**: `move`, `delete`, `undo`, `redo`
- **Queries**: `boundingbox`, `intersect`, `shapeat`, `list`, `listall`
- **Logging**: every mutating command is logged to a plain-text file and an HTML file
- **GUI mode**: Swing canvas with zoom in/out, command input field, and output log panel

## Project Structure

```
clevis/
  Application.java        — entry point (CLI or GUI mode)
  CommandProcessor.java   — parses and routes user commands
  model/
    Shape.java            — abstract base class
    Rectangle.java / Square.java / Circle.java / Line.java
    Group.java            — composite shape
    ShapeManager.java     — central controller (create, move, delete, undo/redo)
    Logger.java           — writes commands to .txt and .html log files
    ShapeManagerTest.java — JUnit 5 test suite
    command/              — Command pattern (undo/redo)
  view/
    ClevisGUI.java        — Swing main window
    GraphicsPanel.java    — Java2D canvas renderer
lib/                      — JUnit 5 JARs (test dependencies)
```

## Running

**CLI mode:**
```
java -cp . clevis.Application [-html <path>] [-txt <path>]
```

**GUI mode:**
```
java -cp . clevis.Application -gui [-html <path>] [-txt <path>]
```

Default log paths are `clevis/log/log.html` and `clevis/log/log.txt`.

## Commands

| Command | Syntax | Description |
|---|---|---|
| `rectangle` | `rectangle name x y w h` | Create a rectangle |
| `square` | `square name x y side` | Create a square |
| `circle` | `circle name x y r` | Create a circle |
| `line` | `line name x1 y1 x2 y2` | Create a line |
| `group` | `group name m1 m2 ...` | Group shapes |
| `ungroup` | `ungroup name` | Ungroup a group |
| `delete` | `delete name` | Delete a shape |
| `move` | `move name dx dy` | Move a shape |
| `boundingbox` | `boundingbox name` | Print bounding box |
| `intersect` | `intersect n1 n2` | Print whether two shapes intersect |
| `shapeat` | `shapeat x y` | Print topmost shape at point |
| `list` | `list name` | Describe a shape |
| `listall` | `listall` | Describe all shapes |
| `undo` / `redo` | `undo` / `redo` | Undo or redo last action |
| `quit` | `quit` | Exit |