package org.dm;

public class CounterMoves {

    private int move = 0;

    public void nextMove(){
        move++;
    }

    public int getMove() {
        return move;
    }

    public void showMove()
    {
        System.out.println("Step: " + getMove());
    }
}
