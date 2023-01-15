/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.smartplayer;

import java.util.List;
import java.util.Random;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class SmartPlayer extends Player {

    private Random random = new Random(0xdeadbeef);


    @Override
    public String getName() {
        return "Olga Gerlich 148088 Kacper Garncarek 148114";
    }


    @Override
    public Move nextMove(Board b) {
        long timeframe = getTime(); //czas do wykorzystania (w milisekundach)

        List<Move> moves = b.getMovesFor(getColor());
        return moves.get(random.nextInt(moves.size()));
    }
}
