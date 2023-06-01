package cz.cuni.mff.java.zapocet;

/*
Piškvorky - interaktivní hra a bot
Autor: Pavel Humlíček, II. ročník, informatika
Programování v jazyce Java
2022/2023
*/

/**
 * Slouží pro spuštění hry.
 */
public class Main {
    /**
     * Nakonfiguruje a spustí hru
     * @param args argumenty z konzole (nevyužívají se)
     */
    public static void main(String[] args) {
        Game game = new Game(12);       //ovládání hry
        Player p1 = new Player(game, Configuration.player1,true, Configuration.player1Name);        // hráč 1
        Player p2 = new Player(game, Configuration.player2, false, Configuration.player2Name);      // hráč 2
        game.setPlayers(p1,p2);     //hráči budou hrát onu hru
        game.run();     //spuštění hry
    }
}