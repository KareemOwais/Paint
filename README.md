# Paint

A desktop paint program written in Java Swing. Built as a project in the ITI intensive program.

## Features

- **Tools:** pencil, line, rectangle, oval, triangle and eraser (with adjustable eraser size)
- **Options:** fill shapes, or draw dotted outlines
- **Colours:** one-click colour palette
- **Editing:** undo, redo and clear
- **Files:** save your drawing as a PNG or load an image back in

## Structure

| File | Role |
|---|---|
| `PaintBrushApp.java` | Window, toolbar and colour palette, plus the `main` method |
| `DrawingPanel.java` | Canvas: mouse handling, shape drawing, undo/redo |
| `ShapeData.java` | A drawn shape with its colour, fill, dotted and stroke settings |

## Run it

Requires JDK 8 or newer.

```bash
git clone https://github.com/KareemOwais/Paint.git
cd Paint/src
javac *.java
java PaintBrushApp
```
