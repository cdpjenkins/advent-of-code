package utils


fun List<String>.toGrid() =
    flatMapIndexed { y: Int, line: String ->
        line.mapIndexed { x, c ->
            Vector2D(x, y) to c
        }
    }.toMap()
