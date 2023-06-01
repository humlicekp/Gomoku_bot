package cz.cuni.mff.java.zapocet;
import java.util.ArrayList;;
import java.util.Comparator;
/**
 * Třída, která reprezentuje bota, který bude provádět tahy.
 */
public class Bot {
    private Analyzer analyzer;
    private Game game;
    private char maximizingSign;
    private char minimizingSign;
    private int depth;

    /**
     * Konstruktor bota.
     * @param game hra, do které bude bot generovat tah
     */
    public Bot(Game game){
        this.game = game;
        this.analyzer = game.getAnalyzer();
        this.depth = Configuration.minimax_depth;
    }

    /**
     *
     * Metoda pro získání tahu hráče.
     * @param player znak hráče, který má táhnout.
     * @return vrátí tah, který má být proveden.
     */
    public Square getMove(char player){
        // Nastaví znaky tahu pro hráče
        setMoveSigns(player);

        // Úvodní tah
        if (game.moves == 0){
            return new Square(game.board.getSize()/2,game.board.getSize()/2);
        }

        // Druhý tah
        if (game.moves == 1){
            return  getSecondMove();
        }

        // Ostatní tahy
        // Inicializuje alfa a beta hodnoty pro minimax algoritmus
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        // Nastaví nejlepší tah na null
        Square bestMove = null;

        // Inicializuje score, herní desku a seznam možných tahů
        int score;
        char[][] board = game.board.getBoard();
        ArrayList<Square> generatedMoves = generateMoves(board);



        // Seřadí seznam tahů podle priority
        prioritizeMoves(generatedMoves, analyzer.getAverageMove());

        // Prochází seznam tahů a hledá nejlepší tah pro hráče
        for(Square move : generatedMoves){
            // Vytvoří novou herní desku s provedeným tahem
            char[][] newBoard = copyBoard(board);
            newBoard[move.x][move.y] = player;

            if (analyzer.doesPlayerHaveConsecutiveFive(newBoard, Configuration.player2)){
                return move;
            }
            // Spustí minimax algoritmus a získá skóre
            score = minimax(newBoard, depth, alpha, beta, false);

            // Pokud je získané skóre větší než alfa, uloží tah jako nejlepší
            if (score > alpha){
                alpha = score;
                bestMove = move;
            }
        }
        return bestMove;
    }


    /**
     *Vrací druhý tah na základě posledního tahu hry.
     *@return druhý tah
     */
    private Square getSecondMove(){
        //poslední tahy
        int lastX = game.last_move.getX();
        int lastY = game.last_move.getY();
        int nx, ny;

        //položí kámen vedle posledního tahu na diagonálu, pokud to lze (není na kraji)
        if (lastX + 1 < game.board.getSize()) nx = lastX + 1;
        else nx = lastX - 1;

        if (lastY+ 1 < game.board.getSize()) ny = lastY + 1;
        else ny = lastY - 1;

        return new Square(nx,ny);
    }

    /**
     *Implementace algoritmu minimax pro vyhodnocení aktuálního stavu hry.
     *@param board herní deska
     *@param depth hloubka prohledávání stromu
     *@param alpha hodnota pro alpha-beta pruning
     *@param beta hodnota pro alpha-beta pruning
     *@param maximizingPlayer zda-li hraje maximalizující hráč
     *@return hodnota stavu hry
     */
    private int minimax(char[][] board, int depth, int alpha, int beta, boolean maximizingPlayer){
        if (depth == 0 || analyzer.isTerminal(board)){
            if (analyzer.doesPlayerHaveConsecutiveFive(board,Configuration.player2)) return Integer.MAX_VALUE - analyzer.evaluate(board, Configuration.player1);
            if (analyzer.doesPlayerHaveConsecutiveFive(board,Configuration.player1)) return Integer.MIN_VALUE + analyzer.evaluate(board, Configuration.player2);
            int score1 = analyzer.evaluate(board, Configuration.player1);
            int score2 = analyzer.evaluate(board, Configuration.player2);
            return score2 - score1;
        }

        if (maximizingPlayer){
            // Pokud hraje maximalizující hráč
            int bestValue = Integer.MIN_VALUE;

            // Pro každý možný tah
            ArrayList<Square> generatedMoves = generateMoves(board);
            prioritizeMoves(generatedMoves, analyzer.getAverageMove());
            for(Square move : generatedMoves){
                // Vytvoří nový stav hracího pole
                char[][] newBoard = copyBoard(board);;
                newBoard[move.x][move.y] = maximizingSign;
                // Rekurzivně zavoláme minimax pro potomka
                int childValue = minimax(newBoard, depth - 1, alpha, beta, false);
                // Vypočítá nejlepší hodnotu pro maximálního hráče
                bestValue = Math.max(bestValue,childValue);
                alpha = Math.max(alpha,bestValue);
                // Prořezávání alfa-beta
                if (beta <= alpha) break;
            }
            return bestValue;

        }

        else{
            // Pokud hraje minimalizující hráč
            int bestValue = Integer.MAX_VALUE;

            // Pro každý možný tah
            ArrayList<Square> generatedMoves = generateMoves(board);
            prioritizeMoves(generatedMoves, analyzer.getAverageMove());

            for(Square move : generatedMoves){
                // Vytvoří nový stav hracího pole
                char[][] newBoard = copyBoard(board);;
                newBoard[move.x][move.y] = minimizingSign;
                // Rekurzivně zavoláme minimax pro potomka
                int childValue = minimax(newBoard, depth - 1, alpha, beta, true);
                // Vypočítá nejlepší hodnotu pro maximálního hráče
                bestValue = Math.min(bestValue,childValue);
                beta = Math.min(beta,bestValue);
                // Prořezávání alfa-beta
                if (beta <= alpha) break;
            }
            return bestValue;
        }
    }

    /**
     *Generuje možné tahy pro aktuální herní desku.
     v@param board herní deska
     *@return seznam možných tahů
     */
    private ArrayList<Square> generateMoves(char[][] board){
        //jednoduche generovani tahu
        ArrayList<Square> moves = new ArrayList<>();
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                if (board[i][j] == Configuration.empty)
                    moves.add(new Square(i,j));
            }
        }
        return moves;
    }

    /**
     *Metoda pro prioritizaci tahů na základě vzdálenosti od průměrného tahu.
     *@param moves seznam tahů, které mají být prioritizovány.
     *@param averageMove průměrný tah.
     */
    private void prioritizeMoves(ArrayList<Square> moves, Square averageMove){
        moves.sort(new Comparator<Square>() {
            @Override
            public int compare(Square o1, Square o2) {
                int manDisO1 = Math.abs(averageMove.x - o1.x) + Math.abs(averageMove.y - o1.y);     // Využití Manhattanské vzdálenosti
                int manDisO2 = Math.abs(averageMove.x - o2.x) + Math.abs(averageMove.y - o2.y);
                return Integer.compare(manDisO1, manDisO2);     //Využijí již porovnávání celých čísel
            }
        });
    }


    /**
     * Zkopíruje desku a vrátí jeho kopii
     * @param board původní deska
     * @return nová kopie
     */
    private char[][] copyBoard(char[][] board){
        char[][] res = new char[board.length][board.length];
        for(int i = 0; i < board.length; i++)
            for(int j = 0; j< board.length; j++)
                res[i][j]=board[i][j];
        return res;
    }

    /**
     * Změní hloubku minimaxu.
     * @param n daná hlouboka
     */
    public void changeDepth(int n){
        //zmeni hloubku
        if (depth < 0 || depth > 6) System.out.println("Nepovolená hloubka!");
        else { this.depth = n; System.out.println("Hloubka " + depth + " úšpěšně nastavena.");}
    }

    /**
     *Nastaví znaky pro hráče minimizing/maximizing.
     *@param player aktuální hráč
     */
    private void setMoveSigns(char player){
        maximizingSign = player;
        char[] signs = game.getPlayerSigns();
        if (signs[0] == player) minimizingSign = signs[1];
        else minimizingSign = signs[0];
    }

    /**
     * Vráti hloubku minimaxu.
     * @return hloubka
     */
    public int getDepth(){
        return depth;
    }
}
