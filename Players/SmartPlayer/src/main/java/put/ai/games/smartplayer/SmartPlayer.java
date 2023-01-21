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

        // check which player is it
        boolean maximazingPlayer;
        if (getColor() == Color.PLAYER1){
            maximazingPlayer = true;
        } else{
            maximazingPlayer = false;
        }

        minmax(b, 0, getColor(), Integer.MIN_VALUE, Integer.MAX_VALUE, maximazingPlayer, new ArrayList<Move>());

        // if minmax won't get any answer then make first possible move
        if (bestMove == null){
            bestMove = b.getMovesFor(getColor()).get(0);
        }

        return bestMove;
    }

    /**
     *
     * @param b - state of the board to analyze
     * @param depth - current depth of searching
     * @param currentPlayer - which player now makes the move
     * @param alpha - parameter for pruning
     * @param beta - parameter for pruning
     * @param maximizingPlayer - which player is the algorithm running for
     * @param moves - list of moves made before that point in the game
     * @return the value of the given node
     */
    private int minmax(Board b, int depth, Color currentPlayer, int alpha, int beta, boolean maximizingPlayer, ArrayList<Move> moves) {
        // decide the best result up to this point
        if (maximizingPlayer && b.getWinner(currentPlayer) != Color.EMPTY && b.getMovesFor(Color.PLAYER1).size()-b.getMovesFor(Color.PLAYER2).size() > bestResult) {
            bestResult = b.getMovesFor(Color.PLAYER1).size()-b.getMovesFor(Color.PLAYER2).size();
            bestMove = moves.get(0);
        }
        else if (!maximizingPlayer && b.getWinner(currentPlayer) != Color.EMPTY && b.getMovesFor(Color.PLAYER1).size()-b.getMovesFor(Color.PLAYER2).size() < bestResult){
            bestResult = b.getMovesFor(Color.PLAYER1).size()-b.getMovesFor(Color.PLAYER2).size();
            bestMove = moves.get(0);
        }

        // finish calculations if:
        // - time run out
        // - maximum depth was reached
        // - game ended
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
                value = Math.max(value, minmax(nextBoard, depth + 1, getOpponent(currentPlayer), alpha, beta, maximizingPlayer, nextMoves));

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
                value = Math.min(value, minmax(nextBoard, depth + 1, getOpponent(currentPlayer), alpha, beta, maximizingPlayer, nextMoves));

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
