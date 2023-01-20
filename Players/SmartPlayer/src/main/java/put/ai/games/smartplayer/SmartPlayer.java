/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.smartplayer;

import java.util.ArrayList;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class SmartPlayer extends Player {

    private int maxDepth = 10;

    private long startTime;
    private boolean finishTime;

    private double timeframe;
    private Move bestMove;
    private int bestResult;


    @Override
    public String getName() {
        return "Olga Gerlich 148088 Kacper Garncarek 148114";
    }

    @Override
    public Move nextMove(Board b) {
        startTime = System.currentTimeMillis();
        finishTime = false;
        timeframe = getTime()-(0.1*getTime()); //czas do wykorzystania (w milisekundach)
        bestMove = null;
        boolean maximazingPlayer;
        if (getColor() == Color.PLAYER1){
            maximazingPlayer = true;
        } else{
            maximazingPlayer = false;
        }

        minmax(b, 0, getColor(), Integer.MIN_VALUE, Integer.MAX_VALUE, maximazingPlayer, new ArrayList<Move>());
        if (bestMove == null){
            bestMove = b.getMovesFor(getColor()).get(0);
        }

        return bestMove;
    }

    private int minmax(Board b, int depth, Color currentPlayer, int alpha, int beta, boolean maximizingPlayer, ArrayList<Move> moves) {
        if (maximizingPlayer && b.getWinner(currentPlayer) != Color.EMPTY && b.getMovesFor(Color.PLAYER1).size()-b.getMovesFor(Color.PLAYER2).size() > bestResult) {
            bestResult = b.getMovesFor(Color.PLAYER1).size()-b.getMovesFor(Color.PLAYER2).size();
            bestMove = moves.get(0);
        }
        else if (!maximizingPlayer && b.getWinner(currentPlayer) != Color.EMPTY && b.getMovesFor(Color.PLAYER1).size()-b.getMovesFor(Color.PLAYER2).size() < bestResult){

        }

        // jeśli algorytm:
        // - skończy przewidziany czas
        // - dojdzie do maksymalnej wskazanej głębokości,
        // - dojdzie do celu
        if (finishTime || depth == maxDepth || b.getWinner(currentPlayer) == getColor()) {
            return b.getMovesFor(Color.PLAYER1).size()-b.getMovesFor(Color.PLAYER2).size();
        }

        if (maximizingPlayer) {
            int value = Integer.MIN_VALUE;
            for (Move nextMove : b.getMovesFor(currentPlayer)) {
                Board nextBoard = b.clone();
                nextBoard.doMove(nextMove);
                ArrayList<Move>nextMoves = (ArrayList<Move>) moves.clone();
                nextMoves.add(nextMove);
                if (currentPlayer == Color.PLAYER1){
                    value = Math.max(value, minmax(nextBoard, depth + 1, Color.PLAYER2, alpha, beta, maximizingPlayer, nextMoves));
                } else{
                    value = Math.max(value, minmax(nextBoard, depth + 1, Color.PLAYER1, alpha, beta, maximizingPlayer, nextMoves));
                }

                alpha = Math.max(alpha, value);
                if (alpha >= beta ||
                        System.currentTimeMillis() - startTime >= timeframe) {
                    if (bestMove == null) {
                        bestMove = moves.get(0);
                    }
                    finishTime = true;
                    break;
                }
            }
            return value;
        } else {
            int value = Integer.MAX_VALUE;
            for (Move nextMove : b.getMovesFor(currentPlayer)) {
                Board nextBoard = b.clone();
                nextBoard.doMove(nextMove);
                ArrayList<Move>nextMoves = (ArrayList<Move>) moves.clone();
                nextMoves.add(nextMove);
                if (currentPlayer == Color.PLAYER1){
                    value = Math.min(value, minmax(nextBoard, depth + 1, Color.PLAYER2, alpha, beta, maximizingPlayer, nextMoves));
                } else{
                    value = Math.min(value, minmax(nextBoard, depth + 1, Color.PLAYER1, alpha, beta, maximizingPlayer, nextMoves));
                }

                beta = Math.min(beta, value);
                if (alpha >= beta ||
                        System.currentTimeMillis() - startTime >= timeframe) {
                    finishTime = true;
                    break;
                }
            }
            return value;
        }
    }

}
