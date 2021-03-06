package com.capgemini.thegameoflife;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// REVIEW bzychal - please add missing comments for classes and public methods
public class TheGameOfLife {

    private final int CELL_ALIVE_HAS_TWO_NEIGHBORS = 2;
    private final int CELL_ALIVE_HAS_THREE_NEIGHBORS = 3;
    private Board board;

    public TheGameOfLife(Integer sizeX, Integer sizeY) {
        board = new Board(sizeX, sizeY);
    }

    public void setInitialState(List<Cell> params) {
        // REVIEW bzychal - please use Java 8 lambdas and streams here
        for (Cell cell : params) {
            boolean cellExists = board.containsCell(cell);

            if (!cellExists) {
                throw new IllegalArgumentException("The cell not exist.");
            }

            board.changeStateOfCell(cell, State.ALIVE);
        }
    }

    public void evolve() {
        Board newBoard = board.getNewClearBoard();
        // REVIEW bzychal - why iterating over entries, since only cells are necessary here? please use only keys from
        // the map
        for (Map.Entry<Cell, State> entry : board.entrySet()) {
            Cell cell = entry.getKey();
            State newStateForCell = getNewStateForCell(cell);
            newBoard.changeStateOfCell(cell, newStateForCell);
        }
        board = newBoard;
    }

    private State getNewStateForCell(Cell cell) {
        int numberOfLifeNeighborCells = getNumberOfLifeNeighborCells(cell);
        State actualStateOfCell = board.get(cell);
        State newState = State.DEAD;

        // REVIEW bzychal - please add else clause, otherwise not necessary check is performed in the second if clause
        if (CELL_ALIVE_HAS_THREE_NEIGHBORS == numberOfLifeNeighborCells) {
            newState = State.ALIVE;
        }

        // REVIEW bzychal - please use == to compare enums, it is guaranted that only single enum class is loaded in the
        // JVM, therefore it is safe to compare it through the reference
        if (CELL_ALIVE_HAS_TWO_NEIGHBORS == numberOfLifeNeighborCells && State.ALIVE.equals(actualStateOfCell)) {
            newState = State.ALIVE;
        }

        return newState;
    }

    private int getNumberOfLifeNeighborCells(Cell cell) {
        Integer x = cell.getX();
        Integer y = cell.getY();
        int numberOfLifeNeighborCells = 0;

        for (int xNeigbor = x - 1; xNeigbor <= x + 1; xNeigbor++) {
            for (int yNeigbor = y - 1; yNeigbor <= y + 1; yNeigbor++) {

                if (xNeigbor != x || yNeigbor != y) {
                    // REVIEW bzychal - better would be to ask board for a cell by passing x and y, then the board knows
                    // the internal representation and uses new cell to fetch the state
                    State state = board.get(new Cell(xNeigbor, yNeigbor));

                    if (State.ALIVE.equals(state)) {
                        numberOfLifeNeighborCells++;
                    }
                }
            }
        }
        return numberOfLifeNeighborCells;
    }

    public List<Cell> getAliveCell() {
        List<Cell> listOfAliveCell = new ArrayList<>();
        for (Map.Entry<Cell, State> entry : board.entrySet()) {
            // REVIEW bzychal - please use == to compare enums
            if (entry.getValue().equals(State.ALIVE)) {
                listOfAliveCell.add(entry.getKey());
            }
        }
        return listOfAliveCell;
    }

}
