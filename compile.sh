#!/bin/bash

javac -d bin src/cs1302/game/MinesweeperGame.java
javac -cp bin -d bin src/cs1302/game/MinesweeperDriver.java
java -cp bin cs1302.game.MinesweeperDriver tests/tc01.seed.txt
