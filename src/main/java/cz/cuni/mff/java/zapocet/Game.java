package cz.cuni.mff.java.zapocet;

import java.util.Scanner;
/**

 *Třída, která reprezentuje onu hru a zaručuje její chod. Uchovává hrací desku, počet tahů, poslední provedený tah, hráče a další informace o hře.
 */
public class Game {
    //bude reprezentovat danou hru
    Board board;        //hrací plocha
    int boardSize;          //velikost hraci desky
    int moves;      //udává počet provedených tahů
    Move last_move;     //poslední provedený tah
    Player player1;     //hráč 1
    Player player2;     //hráč 2
    Player current_player;      //aktuální hráč, který je na tahu
    Player starting_player;     //hráč, který začíná hru
    Scanner scanner;        //skener pro čtení vstupu
    Analyzer analyzer;      //objekt, který slouží k analýze hry
    char[] signs;           //znaky, které jsou přiřazené hráčům

    /**

     *Konstruktor třídy Game.
     *@param size velikost hrací desky
     */
    public Game(int size){
        boardSize = size;
        board = new Board(boardSize);
        moves = 0;
        scanner = new Scanner(System.in);
        analyzer = new Analyzer(board);
    }

    /**

     *Spustí hru.
     */
    public void run(){
        while(!isFinished()){
            board.print();      //vytiskne aktuální stav hrací desky
            last_move = current_player.getMove();      //získá tah od aktuálního hráče
            enterAMove(last_move);      //vloží tah na hrací desku
            analyzer.addMove(last_move);        //aktualizuje analyzátor hry
            switchPlayer();     //přepne na dalšího hráče
        }
        printResult();      //vytiskne výsledek hry
        restartOrLeave();       //zajistí opětovné spuštění hry, nebo ukončení programu
    }

    /**

    *Získá instanci analyzátoru hry.
    *@return instance analyzátoru hry
    */
    public Analyzer getAnalyzer(){
        return analyzer;
    }

    /**

     *Opětovné spuštění hry nebo ukončení programu.
     */
    private void restartOrLeave(){
        //Opětovné spuštění hry nebo ukončení programu
        System.out.println("Přejete si pokračovat?(y/n)");
        String ans = scanner.nextLine();

        while(!ans.equals("y") && !ans.equals("n")){    //dokud nedostaneme vhodnou odpoved
            System.out.println("Neplatná odpověď!\n" + "Přejete si pokračovat? (y/n");
            ans = scanner.nextLine();
        }

        if (ans.equals("y")) reset();   //pokud si přeje pokračovat -> restart
    }

    /**

     *Resetuje hru na počáteční stav
     */
    private void reset(){
        //Resetuje hru na počáteční stav
        board = new Board(boardSize);       //reset desky
        moves = 0;
        last_move = null;
        analyzer.reset();
        analyzer.setBoard(board.getBoard(), boardSize);

        //prohodi zacinajici hrace
        switchStartingPlayer();
        current_player = starting_player;

        run();      //znovuspusteni
    }

    /**

     *vypíše výsledek hry (výhra hráče nebo remízu)
     */
    private void printResult(){
        if (board.isFull()){
            System.out.println("Remíza");
        }
        else{
            switchPlayer();
            System.out.println(current_player.getName() + " vyhrál.");
            switchPlayer();
            printBoard();
        }
    }

    /**

     Nastaví hráče hry, zahajovacího hráče a znak, kterým budou hrát.

     * @param p1 hráč 1

     * @param p2 hráč 2
     */
    public void setPlayers(Player p1, Player p2){
        player1 = p1;
        player2 = p2;
        starting_player =  player1;
        current_player = starting_player;
        signs = new char[]{p1.getSign(), p2.getSign()};
    }

    /**
     *přepne hráče
     */
    private void switchPlayer(){
        //přepne hráče
        if (current_player.equals(player1))
            current_player = player2;
        else current_player = player1;
    }

    /**

     *přepne počátečního hráče
     */
    private void switchStartingPlayer(){
        if (starting_player.equals(player1))
            starting_player = player2;
        else starting_player = player1;
    }

    /**

     *  Kontroluje jestli je dana hra u konce
     * @return true, pokud je daná hra již dohrána, jinak false
     */
    private boolean isFinished(){
        if (board.getMoves() == 0) return false;    //pocatectni krok jeste neni nastaven
        return  (analyzer.checkWin() || board.isFull());
    }

    /**

     *Vloží zadaný tah do hrací desky.
     *@param move tah, který bude vložen
     */
    public void enterAMove(Move move){
        moves++;
        board.enterAMove(move);
    }

    /**
     * Vrátí všechny znaky, které hráči ve hře používají
     * @return znaky hráčů
     */
    public char[] getPlayerSigns(){
        //vrátí pole s znaky hráčů
        return signs;
    }

    /**
     * Vytiskne desku.
     */
    public void printBoard(){
        //vytiskne hrací desku
        board.print();
    }

    /**
     * Provede příkaz, jako například změnu hloubky, zonovuvytisknutí desky, restartování hry aj.
     * @param command příkaz, který má být proveden
     */
    public void readCommand(String command) {
        //změní chování podle zadaného příkazu
        String[] words = command.split(" ");
        switch(words[0]){
            case "depth":
                if (words.length == 1)
                    printDepth();
                else    changeDepth(words[1]);
                break;
            case "size":
                if (words.length == 1)
                    System.out.println("Velikost hraci plochy je " + boardSize);
                else changeSize(words[1]);
                break;
            case "restart":
                reset();
                break;
            case "print":
                printBoard();
                break;
        }
    }

    /**
     *
     * Změní hloubku minimaxu
     * @param depth hloubka, na kterou se má změnit
     */
    private void changeDepth(String depth){
        //změní prohledávací hloubku pro všechny boty
        try{
            Integer.parseInt(depth);
        }
        catch(NumberFormatException nfe){
            System.out.println("Neočekávaný vstup!");
        }
        int n = Integer.parseInt(depth);
        if (n < Configuration.minDepth || n>Configuration.maxDepth)     //nepovolená hloubka
            System.out.println("Nepovolená hloubka!");

        else {      //změna hloubky
            if (!player1.isHuman()) player1.changeDepth(n);
            if (!player2.isHuman()) player2.changeDepth(n);
        }
    }

    /**
     * vypíše hloubku pro všechny boty
     */
    private void printDepth(){
        if (!player1.isHuman()){
            System.out.println(player1.getName() + " má hloubku " + player1.getDepth() + ".");
        }
        if (!player2.isHuman()){
            System.out.println(player2.getName() + " má hloubku " + player2.getDepth() + ".");
        }

    }

    /**
     * změní velikost desky
     * @param s na velikost s, pokud jde o celé číslo
     */
    private void changeSize(String s){
        try{
            int n = Integer.parseInt(s);
            //nepovolená velikost
            if (n < Configuration.minSize  || n > Configuration.maxSize){
                System.out.println("Nepovolená velikost!");
            }
            else{
                //změna velikosti
                this.boardSize = n;
                System.out.println("Změna byla úspěšná, projeví se při restartování hry.");
                System.out.println("(Pro restartování hry napište '/restart'.)");
            }
        }
        catch(NumberFormatException nfe){
            System.out.println("Neočekávaný vstup!");
        }
    }
}
