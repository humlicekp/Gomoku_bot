package cz.cuni.mff.java.zapocet;
import java.util.Scanner;

/**
 * Třída reprezentující hráče.
 */

public class Player {
    private Character sign; //znak, který hráč používá (X nebo O)
    private Game game; //instance hry, ve které hráč hraje
    private boolean isHuman; //boolean hodnota, zda je hráč člověk nebo počítač
    private Scanner scanner; //Scanner pro načítání vstupu od hráče

    private String name; //jméno hráče
    private Bot bot; //instance nějakého bota


    /**
     * konstruktor pro inicializaci hráče
     * @param game hra, kterou hráč hraje
     * @param sign znak, kterým je hráč ve hře reprezentovaný
     * @param isHuman true, pokud je hráč člověk a ne počítač
     * @param name jméno hráče
     */
    public Player(Game game, Character sign, boolean isHuman, String name){
        this.game = game;
        this.sign = sign;
        this.isHuman = isHuman;
        this.scanner = new Scanner(System.in);
        this.name = name;

        if (!isHuman) this.bot = new Bot(game); // pokud je hráč počítač, vytvoří se instance třídy Bot
    }

    /**
     * Vrátí tah hráče.
     * @return Move move tah, který se pak vloží na desku
     */
    public Move getMove(){
        // metoda pro získání tahu hráče
        if (isHuman){
            // pokud je hráč člověk
            boolean hasMove = false; // boolean hodnota, zda byl zadán platný tah
            System.out.println("[" + name + "] " + "Zadejte tah (formát D7): "); // výzva k zadání tahu
            return getMoveFromConsole(); // získání tahu od hráče z konzole
        }
        else{
            // pokud je hráč počítač
            Square square = bot.getMove(sign); // počítač získá další tah
            System.out.println("[" + name + "] "  + (char)(square.x + 'A') + (square.y+1) ); // výpis tahu ve vhodném formátu
            return new Move(square.x, square.y, sign); // vytvoření instance třídy Move reprezentujícího tah hráče
        }
    }

    /**
     * Vrátí jméno hráče.
     * @return jméno hráče
     */
    public String getName(){
        return this.name;
    }

    /**
     * Metoda pro získání tahu hráče z konzole.
     *
     * @return tah hráče
     */
    private Move getMoveFromConsole(){
        int x = 0;
        int y = 0;

        boolean hasMove = false;
        while (!hasMove){       // dokud jsme nedostali žádný tah
            String inp = scanner.nextLine();
            if ( inp.length() > 0 && inp.charAt(0) == '/')       //jde o příkaz
                game.readCommand(inp.substring(1));

            else {
                try {
                    y = Integer.parseInt(inp.replaceAll("[^0-9]", "")) - 1;     // x-ová souřadnice je pouhé číslo
                    x = Character.toUpperCase(inp.replaceAll("[0-9]", "").charAt(0)) - 'A';  // y-ová souřadnice je zadaná písmenem

                    if (game.board.isLegal(x, y))       // jde o legální tah
                        hasMove = true;
                    else
                        System.out.println("[" + name + "] " + "Neplatný tah!\nZadej prosím tah znovu (formát D7): ");
                }
                catch (Exception e){
                    System.out.println("[" + name + "] " + "Neplatný tah!\nZadej prosím tah znovu (formát D7): ");
                }
            }
        }
        return new Move(x,y,sign);
    }

    /**
     * Vrátí true, pokud je hráč člověk.
     * @return true, pokud je hráč člověk, jinak false
     */
    public boolean isHuman(){
        return isHuman;
    }

    /**
     * Vrátí znak hráče.
     * @return znak hráče
     */
    public Character getSign(){
        return sign;
    }

    /**
     * Změní hloubku.
     * @param n hloubka, na kterou se bude měnit
     */
    public void changeDepth(int n){
        bot.changeDepth(n);
    }

    /**
     * Vrátí aktuální hloubku.
     * @return hloubka
     */
    public int getDepth(){
        //vrátí hloubku
        return bot.getDepth();
    }

}
