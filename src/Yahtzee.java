import java.util.Scanner;

public class Yahtzee {
    private final static String WELCOME_MESSAGE = "WELCOME TO YAHTZEE!\n" +
            "===============================";
    private final static String GOODBYE_MESSAGE = "===============================\n" +
            "THANK YOU FOR PLAYING YAHTZEE!";

    /**
     * This method is responsible for everything from displaying the opening
     * welcome message to printing out the final thank you.  It will clearly be
     * helpful to call several of the following methods from here, and from the
     * methods called from here.  See the Sample Runs below for a more complete
     * idea of everything this method is responsible for.
     *
     * @param args - any command line arguments may be ignored by this method.
     */
    public static void main(String[] args){
        System.out.println(WELCOME_MESSAGE);
        System.out.println();

        


    }

    /**
     * This method is used to read in all inputs from the user.  After printing
     * the specified prompt, it will check whether the user�s input is in fact
     * an integer within the specified range.  If the user�s input does not
     * represent an integer or does not fall within the required range, print
     * an error message asking for a value within that range before giving the
     * user another chance to enter valid input.  The user should be given as
     * many chances as they need to enter a valid integer within the specified
     * range.  See the Sample Runs to see how these error messages should be
     * phrased, and to see how the prompts are repeated when multiple invalid
     * inputs are entered by the user.
     *
     * @param in - user input from standard in is ready through this.
     * @param prompt - message describing what the user is expected to enter.
     * @param min - the smallest valid integer that the user may enter.
     * @param max - the largest valid integer that the user may enter.
     * @return - the valid integer between min and max entered by the user.
     */
    public static int readValidInt(int min, int max, String prompt, Scanner in){
        System.out.print(prompt);

        while(!in.hasNextInt()){
            System.out.print("Please input an integer between " + min + " and " + max + ".");
        }

        int inputInt = in.nextInt();
        while(inputInt < min || inputInt > max){
            System.out.print("Please input an integer between " + min + " and " + max + ".");
            while(!in.hasNextInt()){
                System.out.print("Please input an integer between " + min + " and " + max + ".");
            }
            inputInt = in.nextInt();
        }

        return inputInt;
    }

    /**
     * Sets up the scoreboard
     *
     * Data represented in returned array: 0: aces; 1: twos; 2: threes; 3: fours; 4: fives; 5: sixes; 6: top bonus
     * 7: 3 of a kind; 8: 4 of a kind; 9: full house; 10: small straight; 11: large straight; 12: Yahtzee;
     * 13: Chance; 14: Yahtzee bonus (up to four represented)
     *
     *
     * @return Empty scoreboard array
     */
    public static int[] createScoreBoard(){
        int[] scoreboard = new int[15]; // All scores will be set to 0 by default
        return scoreboard;
    }

    /**
     * Sets up the scoreboard used array, used to check if a spot has been used
     *
     * Index values and the slot they represent (true = used, false = not used)
     * 0: aces; 1: twos; 2: threes; 3: fours; 4: fives; 5: sixes; 6: top bonus
     * 7: 3 of a kind; 8: 4 of a kind; 9: full house; 10: small straight; 11: large straight; 12: Yahtzee;
     * 13: Chance;
     *
     * @return Empty used array (all false)
     */
    public static boolean[] createScoreboardUsed(){
        boolean[] scoreboardUsed = new boolean[14];
        return scoreboardUsed;
    }

    /**
     * Prints the scoreboard to the command line
     *
     * @param scoreboard Array representing the scoreboard to print
     */
    public static void displayScoreBoard(int[] scoreboard){
        System.out.println();

        System.out.println("Ones: " + scoreboard[0]);
        System.out.println("Twos: " + scoreboard[1]);
        System.out.println("Threes: " + scoreboard[2]);
        System.out.println("Fours: " + scoreboard[3]);
        System.out.println("Fives: " + scoreboard[4]);
        System.out.println("Sixes: " + scoreboard[5]);
        System.out.println("Top half subtotal: " + partialSum(0, 5, scoreboard));
        System.out.println("Top half bonus: " + scoreboard[6]);
        System.out.println("Top half total: " + partialSum( 0, 6, scoreboard));

        System.out.println();

        System.out.println("3 of a kind: " + scoreboard[7]);
        System.out.println("4 of a kind: " + scoreboard[8]);
        System.out.println("Full house: " + scoreboard[9]);
        System.out.println("Small straight: " + scoreboard[10]);
        System.out.println("Large straight: " + scoreboard[11]);
        System.out.println("Yahtzee: " + scoreboard[12]);
        System.out.println("Chance: " + scoreboard[13]);
        System.out.println("Yahtzee bonus: " + scoreboard[14]);
        System.out.println("Top half total: " + partialSum(0, 6, scoreboard));
        System.out.println("Bottom half total: " + partialSum(7, 14, scoreboard));

        System.out.println();
    }

    /**
     * Finds partial sum of array between startIndex and endIndex, inclusive
     *
     * @param startIndex First index value to be counted
     * @param endIndex Last index value to be counted
     * @param scoreboard Int array to find partial sum of
     * @return Partial sum between startIndex and endIndex
     */
    public static int partialSum(int startIndex, int endIndex, int[] scoreboard){
        if(endIndex >= scoreboard.length){
            return 0;
        }

        int partialSum = 0;
        for(int i = startIndex; i <= endIndex; i++){
            partialSum += scoreboard[i];
        }

        return partialSum;
    }

    /**
     * Checks how many turns are left in the game.
     *
     * @param scoreboardUsed Array representing which slots have been used
     * @return Number of turns remaining
     */
    public static int countTurnsRemaining(boolean[] scoreboardUsed){
        int turnsRemaining = 0;
        for(int i = 0; i < scoreboardUsed.length; i++){ // Step through each element
            if(!scoreboardUsed[i]){ // Check if the slot hasn't been used
                turnsRemaining++;
            }
        }
        return turnsRemaining;
    }
}
