package com.javarush.task.task35.task3513;

import java.math.BigInteger;
import java.util.*;

public class Model {
    private final static int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;

    int score;
    int maxTile;

    private Stack<Tile[][]> previousStates = new Stack<>();
    private Stack<Integer> previousScores = new Stack<>();
    private Stack<Integer> previousMaxTiles = new Stack<>();
    private boolean isSaveNeeded = true;

    private final int ANALYSIS_DEPTH = 5;
    private Move[] basicMoves = new Move[] {this::left, this::up, this::right, this::down};


    public Model() {
        resetGameTiles();
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public int getScore() {
        return score;
    }

    boolean canMove() {
        if (!getEmptyTiles().isEmpty()) {
            return true;
        }

        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (j != FIELD_WIDTH-1 && gameTiles[i][j].value == gameTiles[i][j+1].value) {
                    return true;
                }
                if (i != FIELD_WIDTH-1 && gameTiles[i][j].value == gameTiles[i+1][j].value) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addTile() {
        List<Tile> emptyTiles = getEmptyTiles();
        if (emptyTiles.isEmpty()) {
            return;
        }
        Tile emptyTile = emptyTiles.get((int)(Math.random() * emptyTiles.size()));
        emptyTile.value = Math.random() < 0.9 ? 2 : 4;
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> emptyTiles = new ArrayList<>();
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                if (gameTiles[i][j].isEmpty()) {
                    emptyTiles.add(gameTiles[i][j]);
                }
            }
        }
        return emptyTiles;
    }

    void resetGameTiles() {
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean changed = false;
        int[] compressed = new int[tiles.length];
        int k = 0;
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].value > 0) {
                compressed[k] = tiles[i].value;
                k++;
            }
        }

        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].value != compressed[i]) {
                changed = true;
                tiles[i].value = compressed[i];
            }
        }
        return changed;
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean changed = false;
        for (int i = 0; i < tiles.length-1; i++) {
            if (tiles[i].value != 0 && tiles[i].value == tiles[i+1].value) {
                changed = true;
                tiles[i].value *= 2;

                score += tiles[i].value;
                maxTile = tiles[i].value > maxTile ? tiles[i].value : maxTile;

                for (int j = i+1; j < tiles.length-1; j++) {
                    tiles[j].value = tiles[j+1].value;
                }
                tiles[tiles.length-1].value = 0;
            }
        }
        return changed;
    }

    void left() {
        if (isSaveNeeded) {
            saveState(gameTiles);
        }

        boolean changed = false;
        for (int i = 0; i < gameTiles.length; i++) {
            changed |= compressTiles(gameTiles[i]);
            changed |= mergeTiles(gameTiles[i]);
        }

        if (changed) {
            addTile();
        }
        isSaveNeeded = true;
    }

    void up() {
        saveState(gameTiles);
        isSaveNeeded = false;

        rotate();
        rotate();
        rotate();
        left();
        rotate();
    }

    void right() {
        saveState(gameTiles);
        isSaveNeeded = false;

        rotate();
        rotate();
        left();
        rotate();
        rotate();
    }

    void down() {
        saveState(gameTiles);
        isSaveNeeded = false;

        rotate();
        left();
        rotate();
        rotate();
        rotate();
    }

    private void rotate() {
        int len = gameTiles.length;
        int[][] rotated = new int[len][len];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                rotated[j][len-i-1] = gameTiles[i][j].value;
            }
        }

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                gameTiles[i][j].value = rotated[i][j];
            }
        }
    }

    private void saveState(Tile[][] tiles) {
        Tile[][] savedTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                savedTiles[i][j] = new Tile(tiles[i][j].value);
            }
        }
        previousStates.push(savedTiles);
        previousScores.push(score);
        previousMaxTiles.push(maxTile);

        isSaveNeeded = false;
    }

    void rollback() {
        if (!previousStates.empty() && !previousScores.empty()) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
            maxTile = previousMaxTiles.pop();
        }
    }

    void randomMove() {
        int n = ((int)(Math.random()*100))%4;
        switch (n) {
            case 0: left(); break;
            case 1: up(); break;
            case 2: right(); break;
            case 3: down(); break;
        }
    }

    void autoMove() {
        int numOfVariants = (int)Math.pow(4, ANALYSIS_DEPTH);
        Map<Move, Move> firstSteps = new HashMap<>();
        PriorityQueue<MoveEfficiency> possibleMoves = new PriorityQueue<>(numOfVariants, Collections.reverseOrder());

        Move[][] complexMoves = new Move[numOfVariants][ANALYSIS_DEPTH];
        BigInteger bi = new BigInteger("0", 4);

        for (int i = 0; i < numOfVariants; i++) {
            String s = bi.toString(4);
            String format = String.format("%%%ds", ANALYSIS_DEPTH);
            s = String.format(format, s).replace(' ', '0');
            for (int j = 0; j < s.length(); j++) {
                int indexOfBasics = Character.digit(s.charAt(j), 10);
                complexMoves[i][j] = basicMoves[indexOfBasics];
            }
            bi = bi.add(new BigInteger("1"));
        }

        for (int i = 0; i < numOfVariants; i++) {
            final int finI = i;
            Move complexMove = () -> {
                for (int j = 0; j < ANALYSIS_DEPTH; j++) {
                    complexMoves[finI][j].move();
                }
            };
            possibleMoves.offer(getMoveEfficiency(complexMove));
            firstSteps.put(complexMove, complexMoves[finI][0]);
        }

        Move bestComplexMove = possibleMoves.poll().getMove();
        firstSteps.get(bestComplexMove).move();
    }

    boolean hasBoardChanged() {
        int sumAfter = 0, sumBefore = 0;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                sumAfter += gameTiles[i][j].value;
            }
        }
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                //sumBefore += previousStates.peek()[i][j].value;
                sumBefore += previousStates.get(previousStates.size()-ANALYSIS_DEPTH)[i][j].value;
            }
        }
        return sumAfter != sumBefore;
    }

    MoveEfficiency getMoveEfficiency(Move move) {
        move.move();
        MoveEfficiency moveEfficiency = new MoveEfficiency(-1, 0, move);
        if (hasBoardChanged()) {
            moveEfficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);
        }
        for (int i = 0; i < ANALYSIS_DEPTH; i++) {
            rollback();
        }
        return moveEfficiency;
    }
}
