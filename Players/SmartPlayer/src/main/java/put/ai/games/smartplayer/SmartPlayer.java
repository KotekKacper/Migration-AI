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
    private Move bestMove;
    private int bestResult = Integer.MAX_VALUE;


    @Override
    public String getName() {
        return "Olga Gerlich 148088 Kacper Garncarek 148114";
    }

    @Override
    public Move nextMove(Board b) {
        bestMove = b.getMovesFor(getColor()).get(0);

        int result;
        for (Move move: b.getMovesFor(getColor())){
            b.doMove(move);
            result = minmax(b, 0, getColor(), Integer.MIN_VALUE, Integer.MAX_VALUE);
            b.undoMove(move);
            if (result < bestResult){
                bestResult = result;
                bestMove = move;
            }
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
     * @return the value of the given node
     */
    private int minmax(Board b, int depth, Color currentPlayer, int alpha, int beta) {
        // finish calculations if:
        // - maximum depth was reached
        // - game ended
        if (depth == maxDepth || b.getWinner(currentPlayer) == getColor()) {
            if (b.getWinner(currentPlayer) == getColor()){
                return Integer.MIN_VALUE;
            } else if (b.getWinner(currentPlayer) == getOpponent(getColor())){
                return Integer.MAX_VALUE;
            } else if (getColor() == Color.PLAYER1){
                return b.getMovesFor(Color.PLAYER1).size()-b.getMovesFor(Color.PLAYER2).size();
            } else if (getColor() == Color.PLAYER2){
                return b.getMovesFor(Color.PLAYER2).size()-b.getMovesFor(Color.PLAYER1).size();
            }
        }

        if (currentPlayer == getColor()) {
            int value = Integer.MIN_VALUE;
            for (Move nextMove : b.getMovesFor(currentPlayer)) {
                b.doMove(nextMove);
                value = Math.max(value, minmax(b, depth + 1, getOpponent(currentPlayer), alpha, beta));
                b.undoMove(nextMove);
                alpha = Math.max(alpha, value);
                if (alpha >= beta) {
                    return beta;
                }
            }
            return alpha;
        } else {
            int value = Integer.MAX_VALUE;
            for (Move nextMove : b.getMovesFor(currentPlayer)) {
                b.doMove(nextMove);
                value = Math.min(value, minmax(b, depth + 1, getOpponent(currentPlayer), alpha, beta));
                b.undoMove(nextMove);
                beta = Math.min(beta, value);
                if (alpha >= beta) {
                    return alpha;
                }
            }
            return beta;
        }
    }

}
