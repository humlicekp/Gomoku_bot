package cz.cuni.mff.java.zapocet;
/**

 Třída Analyzer je zodpovědná za analýzu a ukládání stavu hry.

 Udržuje informace o herní desce a  provedených tazích.

 Též poskytuje metody pro přidání tahu do paměti, resetování stavu analyzeru a nastavení desky.
 */
public class Analyzer {
    private char[][] board;     // reprezentuje desku, kterou analyzuje
    private int size;           // velikost desky
    private int memorySize;     // počet tahů, které si bude pamatovat
    private Integer[] memoryX;      // x-ové souřadnice posledních tahů
    private Integer[] memoryY;      // y-ové souřadnice posledních tahů
    private int memoryPointer;      // ukazuje na místo v paměti, na které se bude přidávat další tah


    /**

     Konstruuje nový objekt Analyzer s danou deskou.

     @param board herní deska k analýze
     */
    public Analyzer(Board board){
        this.board = board.getBoard();
        this.size = board.getSize();
        setUpMemory();
    }

    /**

     Nastaví paměť pro ukládání tahu.

     Inicializuje velikost paměti, pole pro ukládání tahu a ukazatel do paměti.
     */
    private void setUpMemory(){
        //nastavi pamet pro tahy
        this.memorySize = Configuration.moveMemory;
        this.memoryX = new Integer[this.memorySize];
        this.memoryY = new Integer[this.memorySize];
        this.memoryPointer = 0;

    }

    /**

     Přidá daný tah do paměti.

     @param move tah, který se má přidat do paměti
     */
    public void addMove(Move move){
        memoryX[memoryPointer] = move.getX();
        memoryY[memoryPointer] = move.getY();
        memoryPointer = (memoryPointer + 1)%memorySize; //posunutí memory pointeru
    }

    /**

     Resetuje stav hry a paměť pro ukládání tahu.
     */
    public void reset(){
        setUpMemory();
    }

    /**

     Nastaví herní desku a její velikost při vyresetování hry.

     @param board herní deska k nastavení

     @param size velikost herní desky
     */
    public void setBoard(char[][] board, int size){
        this.board = board;
        this.size = size;
    }

    /**

     Vrací průměrný tah, tedy okolí, kde se aktuálně nejvíce hraje z posledních max 5 tahů.

     @return Square objekt reprezentující průměrný tah.
     */
    public Square getAverageMove() {
        //vrati prumerny tah, tedy okoli, kde se aktualne nejvic hraje z posledních 5 tahů
        //vhodnější než začinat od středu: ne vždy se tam musí začínat
        int resX = 0;
        int resY = 0;
        int count = 0;
        for(int i = 0; i < memorySize; i++){
            if (memoryX[i] == null || memoryY == null)  break;      //pokud bylo méně než 5 tahů
            resX += memoryX[i];
            resY += memoryY[i];
            count++;
        }
        //vrácení průměrného tahu
        return new Square(resX/count, resY/count);
    }

    /**

     Kontroluje, zda hra byla vyhrána.

     @return true, pokud hráč vyhrál, jinak false.
     */
    public boolean checkWin() {
        return hasConsecutiveFive(board);
    }

    /**

     Vrací true, pokud je daná pozice již konečná.

     @param board herní deska.

     @return true, pokud je daná pozice konečná, jinak false.
     */
    public boolean isTerminal(char[][] board){
        //vrati true, pokud je dana pozice jiz konecna
        return (hasConsecutiveFive(board) || isFull(board));
    }

    /**

     Vrací true, pokud je herní deska plná.

     @param board herní deska.

     @return true, pokud je herní deska plná, jinak false.
     */
    private boolean isFull(char[][] board){
        int n = board.length;;
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                if (board[i][j] == Configuration.empty)
                    return false;
        return true;
    }

    /**

     Kontroluje, zda jsou na herní desce 5 po sobě jdoucích kamenů v řadě, sloupci nebo diagonále.

     @param board herní deska.

     @return true, pokud jsou na herní desce 5 po sobě jdoucích kamenů v řadě, sloupci nebo diagonále, jinak false.
     */
    public boolean hasConsecutiveFive(char[][] board) {
        //slo by zkontrolovat i pomoci funkce pro dany pocet, ale tahle je rychlejsi
        return (checkRows(board, Configuration.player1) || checkColumns(board, Configuration.player1) || checkDiagonals(board, Configuration.player1) ||
                checkRows(board, Configuration.player2) || checkColumns(board, Configuration.player2) || checkDiagonals(board, Configuration.player2));
    }

    public boolean doesPlayerHaveConsecutiveFive(char[][] board, char player){
       return checkRows(board, player) || checkColumns(board, player) || checkDiagonals(board, player);
    }
    /**

     Kontroluje, zda existuje vodorovná řada 5 kamenů stejného typu na herní desce.

     @param board dvourozměrné pole znaků reprezentující aktuální herní desku

     @return true, pokud existuje vítězná kombinace, jinak false
     */
    private boolean checkRows(char[][] board, char player){
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col <= board.length - 5; col++) {
                if (    board[row][col] == player &&
                        board[row][col] == board[row][col+1] &&
                        board[row][col+1] == board[row][col+2] &&
                        board[row][col+2] == board[row][col+3] &&
                        board[row][col+3] == board[row][col+4]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**

     Kontroluje, zda existuje svislá řada 5 kamenů stejného typu na herní desce.

     @param board dvourozměrné pole znaků reprezentující aktuální herní desku

     @return true, pokud existuje vítězná kombinace, jinak false
     */
    private  boolean checkColumns(char[][] board, char player){
        // Zkontroluje sloupce
        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row <= board.length - 5; row++) {
                if (    board[row][col] == player &&
                        board[row][col] == board[row+1][col] &&
                        board[row+1][col] == board[row+2][col] &&
                        board[row+2][col] == board[row+3][col] &&
                        board[row+3][col] == board[row+4][col]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**

     Kontroluje, zda existuje řada 5 kamenů stejného typu v diagonále na herní desce.

     @param board dvourozměrné pole znaků reprezentující aktuální herní desku

     @return true, pokud existuje vítězná kombinace, jinak false
     */
    private boolean checkDiagonals(char[][] board, char player){
    // Zkontroluje diagonaly
        for (int row = 0; row <= board.length - 5; row++) {
            for (int col = 0; col <= board.length - 5; col++) {
                if (    board[row][col] == player &&
                        board[row][col] == board[row+1][col+1] &&
                        board[row+1][col+1] == board[row+2][col+2] &&
                        board[row+2][col+2] == board[row+3][col+3] &&
                        board[row+3][col+3] == board[row+4][col+4]) {
                    return true;
                }
            }
        }

        for (int row = 0; row <= board.length - 5; row++) {
            for (int col = board.length - 1; col >= 4; col--) {
                if (    board[row][col] == player &&
                        board[row][col] == board[row+1][col-1] &&
                        board[row+1][col-1] == board[row+2][col-2] &&
                        board[row+2][col-2] == board[row+3][col-3] &&
                        board[row+3][col-3] == board[row+4][col-4]) {
                    return true;
                }
            }
        }
    return false;
    }

    /**

     Metoda evaluate ohodnocuje hrací plochu na základě segmentů v řádcích, sloupcích a diagonálách pro zadaného hráče.

     @param board hrací plocha reprezentovaná dvourozměrným polem znaků

     @param player hráč, pro kterého se hrací plocha ohodnocuje

     @return ohodnocení hrací plochy pro zadaného hráče
     */
    public int evaluate(char[][] board, char player){
        //ohodnoti hraci plochu, kterou dostane jako argument
        int score = evaluateRows(board,player) + evaluateColumns(board, player) + diagonalsDLToTR(board, player) + diagonalsDRToTL(board, player);
        return score;
    }

    /**

     Metoda evaluateRows vyhodnocuje segmenty v řádcích na základě počtu po sobě jdoucích symbolů pro zadaného hráče.

     @param board hrací plocha reprezentovaná dvourozměrným polem znaků

     @param player hráč, pro kterého se segmenty vyhodnocují

     @return ohodnocení segmentů v řádcích pro zadaného hráče
     */
    private int evaluateRows(char[][] board, char player){
        //vyhodnoti po sobe jdouci segmenty v radku
        int score = 0;      //celkové skóre za všechny řádky
        int consecutiveCount = 0;       //délka vzoru
        int emptyEnds = 0;      //počet ohraničujících políček vzoru, které jsou prízdné
        int size = board.length;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == player)
                    //nalezli jsme další políčko v souvislém úseku
                    consecutiveCount++;

                else if (board[row][col] == Configuration.empty && consecutiveCount > 0) {
                    //ukončili jsme souvislý úsek prázdným políčkem
                    emptyEnds++;
                    score += evaluatePattern(consecutiveCount, emptyEnds);  //vyhodnocení vzoru
                    consecutiveCount = 0;
                    emptyEnds = 1;      //nalezli jsme jeden volný konec do budoucna
                }

                else if (board[row][col]==Configuration.empty){
                    //pokud bylo více volných konců vedle sebe, budeme počítat jen jeden
                    emptyEnds = 1;
                }

                else if (consecutiveCount > 0){
                    //je zde oponent
                    score += evaluatePattern(consecutiveCount, emptyEnds);
                    consecutiveCount = 0;
                    emptyEnds = 0;
                }
                else    emptyEnds = 0;
            }
            if (consecutiveCount > 0)
                //vyhodnocení zbytku, tedy těch co jsou ohraničeny případnou zdí
                score += evaluatePattern(consecutiveCount, emptyEnds);
            //začátek nového řádku
            consecutiveCount = 0;
            emptyEnds = 0;
        }
        return score;
    }

    /**

     Metoda evaluateColumns vyhodnocuje segmenty ve slopucích na základě počtu po sobě jdoucích symbolů pro zadaného hráče.

     @param board hrací plocha reprezentovaná dvourozměrným polem znaků

     @param player hráč, pro kterého se segmenty vyhodnocují

     @return ohodnocení segmentů ve sloupcích pro zadaného hráče
     */
    private int evaluateColumns(char[][] board, char player){
        //vyhodnoti po sobe jdouci segmenty ve slopci
        int score = 0;      //celkové skóre za všechny sloupce
        int consecutiveCount = 0;       //délka vzoru
        int emptyEnds = 0;      //počet ohraničujících políček vzoru, které jsou prázdné
        int size = board.length;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[col][row] == player)
                    //nalezli jsme další políčko v souvislém úseku
                    consecutiveCount++;

                else if (board[col][row] == Configuration.empty && consecutiveCount > 0) {
                    //ukončili jsme souvislý úsek prázdným políčkem
                    emptyEnds++;
                    score += evaluatePattern(consecutiveCount, emptyEnds);  //vyhodnocení vzoru
                    consecutiveCount = 0;
                    emptyEnds = 1;      //nalezli jsme jeden volný konec do budoucna
                }

                else if (board[col][row]==Configuration.empty){
                    //pokud je náhodou více volných vzorů zasebou
                    emptyEnds = 1;
                }

                else if (consecutiveCount > 0){
                    //je zde oponent
                    score += evaluatePattern(consecutiveCount, emptyEnds);
                    consecutiveCount = 0;
                    emptyEnds = 0;
                }
                else    emptyEnds = 0;
            }

            if (consecutiveCount > 0)
                //dovyhodnocení zbytku
                score += evaluatePattern(consecutiveCount, emptyEnds);

            //nový sloupec
            consecutiveCount = 0;
            emptyEnds = 0;
        }
        return score;
    }

    /**

     Metoda diagonalsDLToTR vyhodnocuje segmenty v diagonálách zlevo dole nahoru doprava na základě počtu po sobě jdoucích symbolů pro zadaného hráče.

     @param board hrací plocha reprezentovaná dvourozměrným polem znaků

     @param player hráč, pro kterého se segmenty vyhodnocují

     @return ohodnocení segmentů v daných diagonálách pro zadaného hráče
     */
    private int diagonalsDLToTR(char[][] board, char player){
        // inicializace proměnných
        int score = 0;          //výsledné skóre
        int consecutiveCount = 0;       //počítá délku aktuálního úseku
        int emptyEnds = 0;      //počítá prázdné konce u daného segmentu
        int diagonalsValue = 2; //bonus za diagonaly
        int size = board.length;

        // procházení diagonál
        for (int row = 0; row < size; row++) {
            for (int col = 0; col <= row; col++) {
                // pokud je nalezen hráčův kámen
                if (board[row-col][col] == player)
                    consecutiveCount++;

                // pokud je nalezen prázdný prostor a předtím byly nalezeny kameny hráče
                else if (board[row-col][col] == Configuration.empty && consecutiveCount > 0) {
                    emptyEnds++;
                    score += evaluatePattern(consecutiveCount, emptyEnds);
                    consecutiveCount = 0;
                    emptyEnds = 1;
                }

                //pokud je nalezen prázdný prostor
                else if (board[row-col][col]==Configuration.empty){
                    emptyEnds = 1;
                }

                // pokud je nalezen kámen protihráče
                else if (consecutiveCount > 0){
                    //je zde oponent
                    score += evaluatePattern(consecutiveCount, emptyEnds);
                    consecutiveCount = 0;
                    emptyEnds = 0;
                }
                else    emptyEnds = 0;
            }

            // po dokončení průchodu řádkem, vypočítání bodů z dané diagonály
            if (consecutiveCount > 0)
                score += evaluatePattern(consecutiveCount, emptyEnds);
            consecutiveCount = 0;
            emptyEnds = 0;

            // průchod zbytkem diagonál
            for (int col = row+1; col < size; col++) {
                // pokud je nalezen hráčův kámen
                if (board[size-col+row][col] == player)
                    consecutiveCount++;

                // pokud je nalezen prázdný prostor a předtím byly nalezeny kameny hráče
                else if (board[size-col+row][col] == Configuration.empty && consecutiveCount > 0) {
                    emptyEnds++;
                    score += evaluatePattern(consecutiveCount, emptyEnds);
                    consecutiveCount = 0;
                    emptyEnds = 1;
                }

                // pokud je nalezen prázdný prostor
                else if (board[size-col+row][col]==Configuration.empty){
                    emptyEnds = 1;
                }

                // pokud je nalezen kámen protihráče
                else if (consecutiveCount > 0){
                    //je zde oponent
                    score += evaluatePattern(consecutiveCount, emptyEnds);
                    consecutiveCount = 0;
                    emptyEnds = 0;
                }
                else    emptyEnds = 0;
            }

            // po dokončení průchodu řádkem, vypočítání bodů
            if (consecutiveCount > 0)
                score += evaluatePattern(consecutiveCount, emptyEnds);
            consecutiveCount = 0;
            emptyEnds = 0;
            }
        return score*diagonalsValue;
    }

    /**

     Metoda diagonalsDLToTR vyhodnocuje segmenty v diagonálách z vpravo dole nahoru doleva na základě počtu po sobě jdoucích symbolů pro zadaného hráče.

     @param board hrací plocha reprezentovaná dvourozměrným polem znaků

     @param player hráč, pro kterého se segmenty vyhodnocují

     @return ohodnocení segmentů v daných diagonálách pro zadaného hráče
     */
    private int diagonalsDRToTL(char[][] board, char player){
        //diagonaly z vpravo dole do vlevo nahore
        int score = 0;      //výsledné skóre za tento typ diagonál
        int consecutiveCount = 0;       //délka aktuálního segmentu
        int emptyEnds = 0;       //počet volných konců
        int diagonalsValue = 2; //bonus za diagonaly
        int size = board.length;

        for (int row = size - 1; row >= 0; row--) {
            for (int col = 0; col <= row; col++) {
                if (board[size - row + col - 1][col] == player)
                    //pokud nalezneme pokračování segmentu
                    consecutiveCount++;

                else if (board[size - row + col - 1][col] == Configuration.empty && consecutiveCount > 0) {
                    // pokud je nalezen prázdný prostor a předtím byly nalezeny kameny hráče
                    emptyEnds++;
                    score += evaluatePattern(consecutiveCount, emptyEnds);      //vyhodnocení
                    consecutiveCount = 0;       //zapomenutí aktuálního segmentu
                    emptyEnds = 1;      //nový prázdný okraj pro příští segment
                }

                else if (board[size - row + col - 1][col]==Configuration.empty){
                    //pokud jsme narazili na prázdné políčko bez předchozího segmentu
                    emptyEnds = 1;
                }

                else if (consecutiveCount > 0){
                    //je zde oponent
                    score += evaluatePattern(consecutiveCount, emptyEnds);
                    consecutiveCount = 0;
                    emptyEnds = 0;
                }
                else    emptyEnds = 0;
            }

            //dokončení zbytku
            if (consecutiveCount > 0)
                score += evaluatePattern(consecutiveCount, emptyEnds);
            //reset
            consecutiveCount = 0;
            emptyEnds = 0;

            //druhá půlka tabulky (pravděpodobně by šlo zapsat úsporněji)
            //stejný princip, pouze jiné indexy
            for (int col = row+1; col < size; col++) {
                if (board[col - row - 1][col] == player)
                    //navýšení délky současnohé segmentu
                    consecutiveCount++;

                else if (board[col - row - 1][col] == Configuration.empty && consecutiveCount > 0) {
                    //pokud jsme narazili na konec segmentu
                    emptyEnds++;
                    score += evaluatePattern(consecutiveCount, emptyEnds);
                    consecutiveCount = 0;
                    emptyEnds = 1;
                }

                else if (board[col - row - 1][col]==Configuration.empty){
                    //pokračování v prázdných políčkách
                    emptyEnds = 1;
                }

                else if (consecutiveCount > 0){
                    //je zde oponent
                    score += evaluatePattern(consecutiveCount, emptyEnds);
                    consecutiveCount = 0;
                    emptyEnds = 0;
                }
                else    emptyEnds = 0;
            }

            //vyhodnocení
            if (consecutiveCount > 0)
                score += evaluatePattern(consecutiveCount, emptyEnds);
            //kontrola nové diagonály -> promazání dosud nalezeného
            consecutiveCount = 0;
            emptyEnds = 0;
        }
        return score*diagonalsValue;

    }

    /**

     Metoda pro vyhodnocení vzorce v hracím poli a přiřazení bodů.
     @param consecutiveCount počet souvislých kamenů v řadě nalezených v daném vzorci
     @param openEnds počet volných konců nalezených v daném vzorci
     @return počet přiřazených bodů
     */
    private int evaluatePattern(int consecutiveCount, int openEnds){
        //promeni nalezene vzorce na body
        if (consecutiveCount < 5 && openEnds == 0)
            //pokud máme uzavřenou posloupnost stejných znaků menší než 5, je nám k ničemu
            return 0;

        switch(consecutiveCount){
            case 5:
                //nejlepší možnost: 5 vítězných (menší rezerva pro nepřetečení)
                return 10000000;
            case 4:
                switch (openEnds) {
                    case 1:
                        return 40; //čtyřka s jedním volným koncem - ne tolik žádoucí ale furt lepší než nic
                    case 2:
                        return 100000;     //celá volná čtyřka - vede k pětce a už nelze zastavit
                }
                break;
            case 3:
                switch (openEnds){
                    case 1:
                        return 20;     //začátek trojky je fajn, stále v útoku, ale ne tolik jako samovolné trojky
                    case 2:
                        return 100;    //samovolná trojka je lepší
                }
                break;
            case 2:
                //prázdná dvojka je lepší než nic
                switch (openEnds){
                    case 1:
                        return 3;
                    case 2:
                        return 5;
                }
                break;
            case 1:
                //políčka s prostorem jsou také fajn
                switch (openEnds){
                    case 1:
                        return 1;
                    case 2:
                        return 2;
            }
                break;
        }
        return 0;
    }
}
