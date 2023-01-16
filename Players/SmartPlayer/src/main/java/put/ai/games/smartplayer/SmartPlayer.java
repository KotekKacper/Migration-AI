/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.smartplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.time.*;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class SmartPlayer extends Player {

    private Random random = new Random(0xdeadbeef);

    private int maxDepth = 20;
    private long startTime;
    private long timeframe;
    private Move bestMove;


    @Override
    public String getName() {
        return "Olga Gerlich 148088 Kacper Garncarek 148114";
    }

    @Override
    public Move nextMove(Board b) {
        startTime = System.currentTimeMillis();
        timeframe = getTime()-1; //czas do wykorzystania (w milisekundach)
        bestMove = null;

        minmax(b, new ArrayList<Move>(), getColor(), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return bestMove;
    }

    private int minmax(Board b, ArrayList<Move> moves, Color currentPlayer, int depth, int alpha, int beta){
        // jeśli algorytm:
        // - dojdzie do maksymalnej wskazanej głębokości,
        // - dojdzie do celu
        // - dojdzie do limitu czasowego
        if (depth == maxDepth ||
           b.getWinner(currentPlayer) == getColor() ||
           System.currentTimeMillis()-startTime >= timeframe){
            if (b.getMovesFor(Color.PLAYER1).size()==0){
                return Integer.MAX_VALUE;
            }
            else if (b.getMovesFor(Color.PLAYER2).size()==0){
                return Integer.MIN_VALUE;
            }
            return 1; //TODO - zwracać wartość danego punktu
        }

        if (currentPlayer == Color.PLAYER1){
            int value = Integer.MIN_VALUE;
            for (Move nextMove : b.getMovesFor(currentPlayer)){
                Board nextBoard = b.clone();
                nextBoard.doMove(nextMove);
                ArrayList<Move>nextMoves = (ArrayList<Move>) moves.clone();
                nextMoves.add(nextMove);
                value = Math.max(value, minmax(nextBoard, nextMoves, currentPlayer,
                                         depth+1, alpha, beta));
                alpha = Math.max(alpha, value);
                if (alpha >= beta ||
                    System.currentTimeMillis()-startTime >= timeframe){
                    if (bestMove == null){
                        bestMove = moves.get(0);
                    }
                    break;
                }
            }
            return value;
        }
        else if (currentPlayer == Color.PLAYER2){
            int value = Integer.MAX_VALUE;
            for (Move nextMove : b.getMovesFor(currentPlayer)){
                Board nextBoard = b.clone();
                nextBoard.doMove(nextMove);
                ArrayList<Move>nextMoves = (ArrayList<Move>) moves.clone();
                nextMoves.add(nextMove);
                value = Math.min(value, minmax(nextBoard, nextMoves, currentPlayer,
                        depth+1, alpha, beta));
                alpha = Math.min(alpha, value);
                if (alpha >= beta ||
                        System.currentTimeMillis()-startTime >= timeframe){
                    break;
                }
            }
            return value;
        }
        return 0; // nigdy tu nie dojdzie
    }
}
