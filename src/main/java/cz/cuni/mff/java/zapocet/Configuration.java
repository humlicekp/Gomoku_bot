package cz.cuni.mff.java.zapocet;

/**
 * Obsahuje statické hodnoty, které slouží ke konfiguraci programu a usnadňuje k nim přístupnost.
 */
public class Configuration {
    static Character empty = '.';       // znak, ktery reprezentuje prázdné políčko
    static Character player1 = 'O';     // znak, který reprezentuje hráče 1
    static Character player2 = 'X';     // znak, který reprezentuje hráče 2

    static String player1Name = "Hráč 1";           // jméno prvního hráče
    static String player2Name = "Hráč 2";           // jméno druého hráče
    static int minimax_depth = 3;   //hloubka minimaxu
    static int moveMemory = 5;  //počet tahů, které si bude analyzer pamatovat
    static int minDepth = 1;        //minimální hloubka minimaxu
    static int maxDepth = 5;        //maximální hloubka minimaxu
    static int minSize = 6;         //minimální velikost desky
    static int maxSize = 19;        //maximální velikost desky
}
