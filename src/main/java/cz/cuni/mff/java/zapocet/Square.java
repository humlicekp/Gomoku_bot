package cz.cuni.mff.java.zapocet;

/**
 *  Třída, která reprezentuje políčko na hrací desce pro snažší manipulaci.
 */
public class Square {
    public int x;       //souřadnice x
    public int y;       //souřadnice y

    /**
     * konstruktor čtverce
     * @param x horizontální souřadnice
     * @param y vertikánlí souřadnice
     */
    public Square(int x, int y){
        this.x = x;
        this.y = y;
    }
}
