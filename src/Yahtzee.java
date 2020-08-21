import java.util.Scanner;
import java.util.Random;
import java.util.Scanner;

public class Yahtzee {
    private final static String WELCOME_MESSAGE = "WELCOME TO YAHTZEE!\n" +
            "===============================";
    private final static String GOODBYE_MESSAGE = "===============================\n" +
            "THANK YOU FOR PLAYING YAHTZEE!";
    private final static String postRollPrompt = "What would you like to do?\n" +
            "1. Roll again\n" +
            "2. Save/unsave a die\n";
    private final static String PROMPT_SAVE_DIE = "Which die would you like to save? (You can only save one at a time)\n";

    static Random rand = new Random();

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

        // Initialize values
        int rollsRemaining = 0; // number of rolls remaining this turn

        int[] roll = new int[5]; // array to store the values of the dice
        boolean[] rollSaved = new boolean[5]; // array to store whether each die is being saved or rolled

        int[] scoreboard = createScoreBoard(); // create the scoreboard
        boolean[] scoreboardUsed = createScoreboardUsed(); // create the array to track which sections have been used

        int[] possibleScores = new int[15];
        boolean validScoreSelection = true;

        Scanner in = new Scanner(System.in);
        int inputInt = 0;
        int inputIntNestedLoop = 0;

        while(countTurnsRemaining(scoreboardUsed) > 0){ // loop while there are turns remaining
            rollsRemaining = 3; // You get three rolls per turn
            while(rollsRemaining > 0){
                // Do the roll and print the result
                roll = doRoll(roll, rollSaved);
                printRoll(roll, rollSaved);

                inputInt = readValidInt(1, 2, postRollPrompt, in);

                while(inputInt != 1){ // While the user doesn't want to roll again
                    inputIntNestedLoop = readValidInt(0, 5, PROMPT_SAVE_DIE, in);
                    rollSaved[inputIntNestedLoop] = !rollSaved[inputIntNestedLoop];
                    printRoll(roll, rollSaved);
                    inputInt = readValidInt(1, 2, postRollPrompt, in);
                }

                rollsRemaining--;
            }

            printPossibleScores(roll, scoreboardUsed);
            possibleScores = returnPossibleScoreValues(roll, scoreboardUsed);
            inputInt = readValidInt(0, 14, "Please input which score you would like to use: ", in);
            while(possibleScores[inputInt] == -1){ // Validate the score selection
                inputInt = readValidInt(0, 14, "Please input which score you would like to use: ", in);
            }

            scoreboard[inputInt] = possibleScores[inputInt];
            scoreboardUsed[inputInt] = true;

            displayScoreBoard(scoreboard);
        }


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
        System.out.println("Final total: " + partialSum(0, 14, scoreboard));

        System.out.println();
    }

    /**
     * Prints out the current roll and which dice are being saved
     *
     * @param roll Array of the integer values of the dice
     * @param saved Array saying which dice are being saved
     */
    public static void printRoll(int[] roll, boolean[] saved){
        for(int i = 0; i < roll.length; i++){
            System.out.print(i);
        }
        System.out.println();

        for(int i = 0; i < roll.length; i++){
            System.out.print(roll[i]);
        }
        System.out.println();

        for(int i = 0; i < roll.length; i++){
            if(saved[i]){
                System.out.print("S");
            } else {
                System.out.print("N");
            }
        }
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

    /**
     * Does the random rolling of each "die" that isn't saved
     *
     * @param roll The previous roll value (only needed to preserve saved dice)
     * @param saved Array representing which dice are being saved
     * @return New roll array with random roll for each non-saved die
     */
    public static int[] doRoll(int[] roll, boolean[] saved){
        for(int i = 0; i < roll.length; i++){ // Step through each value in the roll
            if(!saved[i]){ // If that die isn't being saved
                roll[i] = rand.nextInt(6) + 1; // Generates random number from 0-5 then add 1 to represent true dice
            }
        }

        return roll;
    }

    /**
     * Calculates the possible score values for the current roll and returns that as an array the same size and indices as the scoreboard array
     *
     * @param roll Array representing the dice roll
     * @param scoreboardUsed Array representing whether each slot on the scoreboard has been used or not
     * @return Array representing possible point values for each slot on the scorecard based on the current roll (-1 represents a slot that has already been used)
     */
    public static int[] returnPossibleScoreValues(int[] roll, boolean[] scoreboardUsed){
        int[] numDice = new int[6];

        int[] possibleScores = new int[15];
        possibleScores[6] = -1;
        possibleScores[14] = -1;

        // Count the number of each number in the roll
        for(int i = 0; i < roll.length; i++){
            switch (roll[i]){
                case 1:
                    numDice[0]++;
                    break;
                case 2:
                    numDice[1]++;
                    break;
                case 3:
                    numDice[2]++;
                    break;
                case 4:
                    numDice[3]++;
                    break;
                case 5:
                    numDice[4]++;
                    break;
                case 6:
                    numDice[5]++;
                    break;
            }
        }

        // Determine the possible point values for the top six scores (aces through sixes)
        for(int i = 0; i < 6; i++){
            if(!scoreboardUsed[i]){
                possibleScores[i] = numDice[i] * (i+1);
            } else {
                possibleScores[i] = -1;
            }
        }

        // Determine point values for the other boxes

        if(!scoreboardUsed[7]){ // Three of a kind
            for(int i = 0; i < 6; i++){
                if(numDice[i] >= 3){
                    possibleScores[7] = numDice[i] * (i+1);
                }
            }
        } else {
            possibleScores[7] = -1;
        }

        if(!scoreboardUsed[8]){ // Four of a kind
            for(int i = 0; i < 6; i++){
                if(numDice[i] >= 4){
                    possibleScores[8] = numDice[i] * (i+1);
                }
            }
        } else {
            possibleScores[8] = -1;
        }

        if(!scoreboardUsed[9]){ // Full house
            boolean twoOfAKind = false;
            boolean threeOfAKind = false;
            for(int i = 0; i < 6; i++){
                if(numDice[i] == 3){
                    threeOfAKind = true;
                } else if (numDice[i] == 2){
                    twoOfAKind = true;
                }
            }

            if(threeOfAKind && twoOfAKind){
                possibleScores[9] = 25;
            } else {
                possibleScores[9] = -1;
            }
        } else {
            possibleScores[9] = -1;
        }

        if(!scoreboardUsed[10]){ // Small straight
            if(numDice[2] > 0 && numDice[3] > 0){ // Small straight needs at least one 3 and one 4
                if(numDice[0] > 0 && numDice[1] > 0){ // 1-4 straight
                    possibleScores[10] = 30;
                } else if(numDice[1] > 0 && numDice[4] > 0) { // 2-5 straight
                    possibleScores[10] = 30;
                } else if(numDice[4] > 0 && numDice[5] > 0){ // 3-6 straight
                    possibleScores[10] = 30;
                }
            }
        } else {
            possibleScores[10] = -1;
        }

        if(!scoreboardUsed[11]){ // Large straight
            if(numDice[1] > 0 && numDice[2] > 0 && numDice[3] > 0 && numDice[4] > 0){ // Lg straight needs at least one 2, 3, 4, and 5
                if(numDice[0] > 0 || numDice[5] > 0){ // 1-5 or 2-6 straight
                    possibleScores[11] = 40;
                }
            }
        } else {
            possibleScores[11] = -1;
        }

        if(!scoreboardUsed[12]){ // Yahtzee & bonus
            for(int i = 0; i < 6; i++){ // Check each die number
                if(numDice[i] == 5){
                    possibleScores[12] = 50;
                }
            }
        } else {
            possibleScores[12] = -1;
            for(int i = 0; i < 6; i++){ // Check each die number
                if(numDice[i] == 5){
                    possibleScores[14] = 100;
                }
            }
        }

        if(!scoreboardUsed[13]){ // Chance
            for(int i = 0; i < 6; i++){
                possibleScores[13] += numDice[i] * (i+1);
            }
        } else {
            possibleScores[13] = -1;
        }

        return possibleScores;
    }

    /**
     * Prints the possible score values to the terminal with an identifier so that the player can choose which they want to pick
     *
     * @param roll Array representing current dice roll
     * @param scoreboardUsed Array representing whether each score card box has been used or not
     */
    public static void printPossibleScores(int[] roll, boolean[] scoreboardUsed){
        int[] possibleScores = returnPossibleScoreValues(roll, scoreboardUsed); // Get the possible score values
        String[] scoreMeanings = new String[] {"Ones", "Twos", "Threes", "Fours", "Fives", "Sixes", "Top bonus", "3 of a kind", "4 of a kind", "Full house", "Small straight",
        "Large straight", "Yahtzee", "Chance", "Yahtzee bonus"};

        System.out.println("Possible scores from your roll: (Note: only slots you haven't used will be shown)");
        for(int i = 0; i < possibleScores.length; i++){
            if(possibleScores[i] != -1){
                System.out.println(i + ": " + scoreMeanings[i] + ": " + possibleScores[i]);
            }
        }
    }
}
