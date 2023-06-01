package cz.cuni.mff.java.zapocet;

/**
 * Třáda, která reprezentuje daný tah
 */
public class Move {
    //třída, která reprezentuje daný tah
    Square square;      // políčko, ve kterém tah je
    public char player;     // znak hráče, který daný tah provedl

    /**
     * Konstruktor tahu
     * @param x souřadnice x
     * @param y souřadnice y
     * @param player reprezentuje hráče, který provedl tah
     */
    public Move(int x, int y, char player){
        this.square = new Square(x,y);
        this.player = player;
    }

    /**
     * Vráti x-ovou souřadnici tahu
     * @return souřadnice x
     */
    public int getX(){
        return square.x;
    }

    /**
     *
     * Vráti y-ovou souřadnici tahu
     * @return souřadnice y
     */
    public int getY(){
        return square.y;
    }
}
