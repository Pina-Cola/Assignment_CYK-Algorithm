// import java.lang.reflect.Constructor;

public class Parser extends Grammar {

    Grammar grammar = new Grammar();
    public String[][] ruleset;      // all rules
    public String[][] rulesetNT;    // NT rules
    public String[][] rulesetT;     // T rules

    public char[] inputWord;

    int counter = 0;

    public Parser(String[] inputString, String inputWord){
        ruleset = grammar.getRuleset(inputString);
        rulesetNT = grammar.getRulesetNT(inputString);
        rulesetT = grammar.getRulesetT(inputString);

        this.inputWord = inputWord.toCharArray();

        System.out.println("Naive: " + parseNaive());
    }



    //____________________________________________________________________________________________________

    // for recursion

    //____________________________________________________________________________________________________



    public boolean parseNaive(){
        return parseNaive(inputWord, 0, inputWord.length);
    }



    //____________________________________________________________________________________________________

    // naive cyk algorithm

    //____________________________________________________________________________________________________



    public boolean parseNaive(char[] wordString, int i, int j){
 

        return true;
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