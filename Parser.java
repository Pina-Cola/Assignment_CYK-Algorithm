// import java.lang.reflect.Constructor;

public class Parser extends Grammar {

    Grammar grammar = new Grammar();
    public String[][] ruleset;      // all rules
    public String[][] rulesetNT;    // NT rules
    public String[][] rulesetT;     // T rules

    public char[] inputWord;

    int counter = 0;

    public Parser(String[] inputString, String inputWord){

        grammar.nts_to_int(inputString);

        ruleset = grammar.getRuleset(inputString);
        rulesetNT = grammar.getRulesetNT(inputString);
        rulesetT = grammar.getRulesetT(inputString);

        System.out.println("Input word: " + inputWord);

        this.inputWord = inputWord.toCharArray();

        System.out.println("Naive: " + parseNaive() + "   Amount of calls: " + counter);
    }


    //____________________________________________________________________________________________________

    // returns counter

    //____________________________________________________________________________________________________



    public int getCounter(){
        return counter;
    }



    //____________________________________________________________________________________________________

    // calls recursion

    //____________________________________________________________________________________________________



    public boolean parseNaive(){
        counter = 0;
        return parseNaive(0, 0, inputWord.length);
    }



    //____________________________________________________________________________________________________

    // naive cyk algorithm

    /* 
    It returns parse(S, 0, n), where S is the initial nonterminal of the grammar. 
    The method parse performs a naive recursive test. 

    When being invoked as parse(A, i, j) it does the following: if i = j −1 then it simply checks whether A → s[i] is a rule of the grammar 
    and returns the resulting Boolean. 
    Otherwise, it checks for all rules A → BC and for k = i+1, . . . , j −1 whether both parse(B, i, k) and parse(C, k, j) return true. 
    If such a pair is found, it returns true, otherwise false. */

    //____________________________________________________________________________________________________



    public boolean parseNaive(int indexNT, int i, int j){

        counter += 1;
        int rulesetLength = ruleset[0].length;

        if(i == (j-1)){
            for(int l = 0; l < rulesetLength; l++){
                String symbol = String.valueOf(inputWord[i]);
                if(ruleset[indexNT][l].equals(symbol)){
                    return true;
                }
            }
            return false;
        }
        else{
            for(int headIndex = indexNT; headIndex < ruleset.length; headIndex++){

                for(int bodyIndex = 0; bodyIndex < ruleset[headIndex].length; bodyIndex++){
                    if(ruleset[headIndex][bodyIndex].length() >= 2){
                    for(int k = i+1; k < j; k++){
                        int first = Character.getNumericValue(ruleset[headIndex][bodyIndex].charAt(0));
                        int second = Character.getNumericValue(ruleset[headIndex][bodyIndex].charAt(1));
                        if(parseNaive(first,i,k) && parseNaive(second,k,j)){
                            return true;
                        }
                    }
                }
                }
            }

        } 

        return false;
    }



    //____________________________________________________________________________________________________

    // bottom up

    //____________________________________________________________________________________________________



    public boolean parseBU(String word){

        return true;
    }



    //____________________________________________________________________________________________________

    // top down

    //____________________________________________________________________________________________________



    public boolean parseTD(String word){

        return true;
    }

    
    
}

/*
 * A class Parser whose constructor takes the grammar to be parsed as an argument, 
 * and which provides parsing methods parseNaive, parseBU, and parseTD. 
 * 
 * The methods should take the string to be parsed as an argument, 
 * and should of course return a truth value indicating whether the input string was found 
 * to belong to the language or not. 
 * 
 * In addition, the parser should contain a counter which is initialized to zero when parsing starts. 
 * 
 * The iterative parser (i.e., the standard bottom-up CYK parser) increments the counter with each 
 * execution of the innermost loop. 
 * 
 * Similarly, the recursive variants increment the counter each time a recursive call is made. 
 * 
 * In this way, the counter gives an estimate of the number of operations that have been executed 
 * when parsing has finished. 
 * 
 * This yields an abstract measure of how much work the algorithm has performed 
 * and can later on be compared to the physical time measurements in order to see 
 * whether the two types of measurements support the same conclusions.
 * 
 */