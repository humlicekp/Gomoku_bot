package cz.cuni.mff.java.zapocet;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Třída, která reprezentuje hrací desku.
 */
public class Board {
    private int size;   //velikost hraciho pole
    private char[][] board;        //reprezentuje hraci pole
    int moves;

    /**
     * Kontruktor desky
     * @param size velikost jedné hrany desky, která je čtvercová.
     */
    public Board(int size){
        this.size = size;
        this.board = createBoard();
        this.moves = 0;
    }

    /**
     * Získá konkrétní obsah desky.
     * @return obsah desky
     */
    char[][] getBoard(){
        //vrati policka
        return board;
    }

    /**
     * Vrátí velikost desky.
     * @return velikost
     */
    int getSize(){
        return size;
    }

    /**
     * Získá počet odehranách dahů.
     * @return počet tahů
     */
    public int getMoves(){
        return moves;
    }

    /**
     *  Určí zda je deska plná.
     * @return true, pokud je plná, jinak false
     */
    public boolean isFull(){
        return moves >= size*size;
    }

    /**
     * Vytvoří novou  prázdnou desku
     * @return prázdná deska
     */
    private char[][] createBoard(){
        //vytvori hraci pole
        char[][] res = new char[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                res[i][j] = Configuration.empty;
            }
        }
        return res;
    }

    /**
     * Vytiskne obsah desky.
     */
    public void print() {
        //vytiskne tabulku
        printXAxis();
        for (int row = 0; row < size; row++) {
            System.out.printf("%2d", (row + 1));        //y-ová osa

            for (int col = 0; col < size; col++) {  //obsah tabulky
                char value = board[col][row];
                System.out.printf(" %c", (value == '\0' ? Configuration.empty : value));    //vytiskne obsah tabulky tak, aby byla zarovnaná
            }
            System.out.println();
        }
    }

    /**
     * Vytiskne vodorovnou osu, ktera popisuje desku.
     */
    private void printXAxis() {
        //vytiskne popisky vodorovné osy
        System.out.print("   ");
        for (int i = 0; i < size; i++) {
            char letter = (char) ('A' + i);     //převod indexu do písmena
            System.out.print(letter + " ");
        }
        System.out.println();
    }

    /**
     * Vloží tah do desky, jakožto jejího obsahu.
     * @param move daný tah
     */
    public void enterAMove(Move move){
        //vlozi dany tah do sachovnice
        moves++;
        board[move.getX()][move.getY()] = move.player;
    }

    /**
     * Určí, zda daný tah vyhovuje pravidlům.
     * @param x x-ová souřadnice tahu
     * @param y y-ová souřadnice tahu
     * @return true, pokud je tah legální, jinak false
     */
    public boolean isLegal(int x, int y){
        //vrati true, pokud je tah legalni
        return isOnBoard(x, y) && isEmpty(x,y);
    }

    /**
     * Určí, zda není tah mimo desku
     * @param x souřadnice x
     * @param y souřadnice y
     * @return true, pokud není mimo desku, jinak false
     */
    private boolean isOnBoard(int x, int y){
        return (x >= 0 && x < size && y >= 0 && y < size);
    }

    /**
     * Určí, zda je políčko prázdné
     * @param x souřadnice x
     * @param y souřadnice y
     * @return true, pokud je prázdné, jinak false
     */
    private boolean isEmpty(int x, int y) { return board[x][y]==Configuration.empty;}
}
