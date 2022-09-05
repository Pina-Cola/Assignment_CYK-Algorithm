import java.util.Arrays;

public class Grammar {

    // expected input for a rule: 
    // for example rule S -> AB as "S A B"

    public char[] NTerminalToInteger;
    

    public int inputLength;
    public int rulesLength;


    // Rulesets 
    public char[][] ruleset;      // all rules
    public char[][] rulesetNT;    // NT rules
    public char[][] rulesetT;     // T rules

    public Grammar(){
     
    }




    //____________________________________________________________________________________________________

    // returns length of input string

    //____________________________________________________________________________________________________


    public int inputLength(String[] inputString){
        inputLength = inputString.length;
        return inputLength;
    }






    //____________________________________________________________________________________________________

    // returns upper bound for rules length (amount of chars of longest rule)

    //____________________________________________________________________________________________________


    public int maxRuleLength(String[] inputString){
        int max = 0;
        for(String rules: inputString){
            max += rules.length();
        }
        return max;
    }



    //____________________________________________________________________________________________________

    // fills Array NTerminalToInteger with NT symbols (one symbol per index) 

    //____________________________________________________________________________________________________


    public void nts_to_int(String[] inputString){

        // assuming that each NT-symbol has ONE char as a name

        inputLength = inputLength(inputString);
        rulesLength = maxRuleLength(inputString);

        NTerminalToInteger = new char[inputLength];
        int amountOfNT = 0;

        // loop through all rules
        for(String rule: inputString){

            // get NT symbol (first symbol of each rule)
            char NT = rule.charAt(0);
            

            boolean foundInMap = is_NT_symbol(NT, NTerminalToInteger);

            // add to array if not already existing
            if(!foundInMap){
                NTerminalToInteger[amountOfNT] = NT;
                amountOfNT += 1;
            }
        }
        System.out.println("NT-Symbols: " + Arrays.toString(NTerminalToInteger));

        addRules(NTerminalToInteger, inputString);
}


    //____________________________________________________________________________________________________

    // show numbers assigned to NT symbols

    //____________________________________________________________________________________________________


    public void Int_NT_map (char[] NTerminalToInteger) {
        
        char[][] NT_int_map = new char[2][NTerminalToInteger.length];

        for(int i = 0; i < NTerminalToInteger.length; i++){
            char temp = (char)(i+'0');
            NT_int_map[0][i] = temp;
            NT_int_map[1][i]= NTerminalToInteger[i];
        }

        printMatrix(NT_int_map);

    }



    //____________________________________________________________________________________________________

    // returns true if char is in NTerminalToInteger

    //____________________________________________________________________________________________________


    public boolean is_NT_symbol (char NT, char[] NTerminalToInteger) {
        for(int i = 0; i <= NTerminalToInteger.length; i++){
            if(NTerminalToInteger[i] == 0){
                return false;
            }
            if(NTerminalToInteger[i] == NT){
                return true;
            }
        }
        return false;
    }




    //____________________________________________________________________________________________________

    // returns index of NT symbol in NTerminalToInteger

    //____________________________________________________________________________________________________


    public int getIndexOfNT (char NT, char[] NTerminalToInteger) {

        int index = 0;

        for(int i = 0; i < NTerminalToInteger.length; i++){
            if(NT == NTerminalToInteger[i])
            {
                index = i;
                break;
            }
        }
        return index;
    }


    //____________________________________________________________________________________________________

    // replayces NT symbols with integers (index)

    //____________________________________________________________________________________________________


    public String replace_NT_with_int (String rule, char[] NTerminalToInteger) {

        char[] ruleChar = rule.toCharArray();

        for(int i = 0; i < ruleChar.length; i++){
            for(int j = 0; j < NTerminalToInteger.length-1; j++){
                if(ruleChar[i] == NTerminalToInteger[j]){
                    char index = (char)(j+'0');
                    ruleChar[i] = index;
                    break;
                }
            }
        }
        String ruleWithInt = String.valueOf(ruleChar);
        return ruleWithInt;
    }




    //____________________________________________________________________________________________________

    // add rules to ruleset, rulesetNT and rulesetT
    // DOES NOT WORK CORRECTLY YET

    //____________________________________________________________________________________________________



    public void addRules(char[] NTerminalToInteger, String[] inputStrings){

        inputLength = NTerminalToInteger.length -1;

        rulesetNT = new char[inputLength][rulesLength];
        rulesetT = new char[inputLength][rulesLength];

        ruleset = new char[inputLength][rulesLength];


        for(String rule: inputStrings){

            char[] rulesChar = rule.toCharArray();
            char NT = rulesChar[0];

            // body of rule
            if(rule.length() >= 1){
                rule = rule.substring(1);
            }

            String ruleWithInt = replace_NT_with_int(rule, NTerminalToInteger);

            addSingleRule(NTerminalToInteger, ruleWithInt, NT);
        }        
        
        System.out.println("");
        System.out.println("Integers of NT symbols:");
        Int_NT_map(NTerminalToInteger);
        System.out.println("");
        System.out.println("Matrix all rules:");
        printMatrix(ruleset);
        System.out.println("");
        System.out.println("Matrix T rules:");
        printMatrix(rulesetT);
        System.out.println("");
        System.out.println("Matrix NT rules:");
        printMatrix(rulesetNT);
        System.out.println("");
    }


    //____________________________________________________________________________________________________

    // adds a single rule

    //____________________________________________________________________________________________________



    public void addSingleRule(char[] NTerminalToInteger, String rule, char NT){



        char[] ruleChar = rule.toCharArray();
        String all_NT_symbols = NTerminalToInteger.toString();
        boolean is_NT_rule = true;


        for (char inputChar : ruleChar) {

            String charToString = "" + inputChar;
            if(!all_NT_symbols.contains(charToString)){
                is_NT_rule = false;
                break;
            }

        }

        int index = getIndexOfNT(NT, NTerminalToInteger);


        for(int i = 0; i < ruleset[index].length; i ++){
            if(ruleset[index][i] == 0)
            {
                for(int j = 0; j < ruleChar.length; j++){
                    ruleset[index][i] = ruleChar[j];
                    i++;
                }
                break;
            }
        }

        if(is_NT_rule){
            for(int i = 0; i < rulesetNT[index].length; i ++){
                if(rulesetNT[index][i] == 0)
                {
                    for(int j = 0; j < ruleChar.length; j++){
                        rulesetNT[index][i] = ruleChar[j];
                        i++;
                    }
                    break;
                }
            }
        } else {
            for(int i = 0; i < rulesetT[index].length; i ++){
                if(rulesetT[index][i] == 0)
                {
                    for(int j = 0; j < ruleChar.length; j++){
                        rulesetT[index][i] = ruleChar[j];
                        i++;
                    }
                    break;
                }
        } 
    }
}




    //____________________________________________________________________________________________________

    // prints 2D matrix in terminal

    //____________________________________________________________________________________________________



    public void printMatrix(char[][] rules){
        for (char[] row : rules)
            System.out.println(Arrays.toString(row));
    }
}


/*

An abstract class Grammar that can be subclassed by specifying the rules of a context-free grammar in 
Chomsky normal. 

The class should allow efficient access to the rules, 
where efficiency should be judged relative to the way in which the rules are accessed in the CYK algorithm. 

In particular, it is probably a good idea to represent the terminal rules separately from the nonterminal ones.
Nichtterminalsymbole: Grossbuchstaben
Terminalsymbole: kleine Buchstaben 

Moreover, I suggest to translate the nonterminals into integers while initializing the internal data structures. 

In this way, one may for example represent a right-hand side as a pair of integers, 
and can implement the set of nonterminal rules as an array of arrays of right-hand sides. 

Then, if a given nonterminal i has n nonterminal rules, one can efficiently loop over their right-hand sides 
as rule[i][j] for j = 0, . . . , n − 1 (and one can loop over all rules by additionally looping over i).

 */