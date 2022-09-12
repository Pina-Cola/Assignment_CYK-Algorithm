import java.util.Arrays;

public class Grammar {

    // expected input for a rule: 
    // for example rule S -> AB as "SAB"

    public char[] NTerminalToInteger;
    

    public int inputLength;
    public int rulesLength;


    // Rulesets 
    public String[][] ruleset;      // all rules
    public String[][] rulesetNT;    // NT rules
    public String[][] rulesetT;     // T rules

    public Grammar(){

        // nts_to_int(grammar);
     
    }




    //____________________________________________________________________________________________________

    // returns length of input string

    //____________________________________________________________________________________________________


    public int inputLength(String[] inputString){
        inputLength = inputString.length;
        return inputLength;
    }


    //____________________________________________________________________________________________________

    // returns ruleset

    //____________________________________________________________________________________________________


    public String[][] getRuleset(String[] inputString){
        return ruleset;
    }


    //____________________________________________________________________________________________________

    // returns rulesetT

    //____________________________________________________________________________________________________


    public String[][] getRulesetT(String[] inputString){
        return rulesetT;
    }


    //____________________________________________________________________________________________________

    // returns rulesetNT

    //____________________________________________________________________________________________________


    public String[][] getRulesetNT(String[] inputString){
        return rulesetNT;
    }






    //____________________________________________________________________________________________________

    // returns upper bound for rules length (amount of chars of longest rule)

    //____________________________________________________________________________________________________


    public int maxRuleLength(String[] inputString){
        int max = 0;
        for(String rules: inputString){
            max += 1;
        }
        // max = 2;
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
        // System.out.println("NT-Symbols: " + Arrays.toString(NTerminalToInteger));

        addRules(NTerminalToInteger, inputString);
}


    //____________________________________________________________________________________________________

    // show numbers assigned to NT symbols

    //____________________________________________________________________________________________________


    public void Int_NT_map (char[] NTerminalToInteger) {

        int length = 0;
        for(int i = 0; i < NTerminalToInteger.length; i++){
            if(NTerminalToInteger[i] != 0){
                length += 1;
            }
        }
        
        char[][] NT_int_map = new char[2][length];

        for(int i = 0; i < length; i++){
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
        for(int i = 0; i <= NTerminalToInteger.length-1; i++){
            if(NTerminalToInteger[i] == NT){
                // System.out.println("NT detected: " + NT);
                return true;
            }
            if(NTerminalToInteger[i] == 0){
                // System.out.println("T detected: " + NT);
                return false;
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

    // replaces NT symbols with integers (index)

    //____________________________________________________________________________________________________


    public String[][] replace_NT_with_int (String[][] ruleArray) {

        // loop over input ruleset:
        for(int i = 0; i < ruleArray.length; i++){
            for(int j = 0; j < ruleArray[i].length; j++){
                
                // loop over String in ruleset (body of the rule)
                for(int singleSymbolIndex = 0; singleSymbolIndex < ruleArray[i][j].length(); singleSymbolIndex++){

                    // loop over NT symbole
                    for(int k = 0; k < NTerminalToInteger.length; k++){

                        // replaces NT with int
                        if(ruleArray[i][j].charAt(singleSymbolIndex) == NTerminalToInteger[k]){
                            char oldEntry = ruleArray[i][j].charAt(singleSymbolIndex);
                            char replacement = (char) (k+'0');
                            ruleArray[i][j] = ruleArray[i][j].replace(oldEntry, replacement);
                        }

                    }


                }

                
            }
        }



        return ruleArray;
        
        
    }




    //____________________________________________________________________________________________________

    // add rules to ruleset, rulesetNT and rulesetT
    // DOES NOT WORK CORRECTLY YET

    //____________________________________________________________________________________________________



    public void addRules(char[] NTerminalToInteger, String[] inputStrings){

        inputLength = NTerminalToInteger.length;

        rulesetNT = new String[inputLength][rulesLength];
        rulesetT = new String[inputLength][rulesLength];

        ruleset = new String[inputLength][rulesLength];


        for(String rule: inputStrings){

            char[] rulesChar = rule.toCharArray();
            char NT = rulesChar[0];

            // body of rule
            if(rule.length() >= 1){
                rule = rule.substring(1);
            }

            // String ruleWithInt = replace_NT_with_int(rule, NTerminalToInteger);

            // addSingleRule(NTerminalToInteger, ruleWithInt, NT);
            addSingleRule(NTerminalToInteger, rule, NT);
        }     
        
        ruleset = beautifyStringMatrix(ruleset);
        rulesetNT = beautifyStringMatrix(rulesetNT);
        rulesetT = beautifyStringMatrix(rulesetT);

        System.out.println("");
        System.out.println("Matrix all rules:");
        printStringMatrix(ruleset);
        System.out.println("");
        System.out.println("Matrix T rules:");
        printStringMatrix(rulesetT);
        System.out.println("");
        System.out.println("Matrix NT rules:");
        printStringMatrix(rulesetNT);
        System.out.println("");

        ruleset = replace_NT_with_int(ruleset);
        rulesetNT = replace_NT_with_int(rulesetNT);
        rulesetT = replace_NT_with_int(rulesetT);
        
        System.out.println("");
        System.out.println("Integers of NT symbols:");
        Int_NT_map(NTerminalToInteger);
        System.out.println("");
        System.out.println("Matrix all rules:");
        printStringMatrix(ruleset);
        System.out.println("");
        System.out.println("Matrix T rules:");
        printStringMatrix(rulesetT);
        System.out.println("");
        System.out.println("Matrix NT rules:");
        printStringMatrix(rulesetNT);
        System.out.println("");
    }


    //____________________________________________________________________________________________________

    // adds a single rule

    //____________________________________________________________________________________________________



    public void addSingleRule(char[] NTerminalToInteger, String rule, char NT){

        char[] ruleChar = rule.toCharArray();
        boolean is_NT_rule = is_NT_rule(ruleChar);

        int index = getIndexOfNT(NT, NTerminalToInteger);

        for(int i = 0; i < ruleset[index].length; i ++){
            if(ruleset[index][i] == null)
            {
                ruleset[index][i] = rule;
                break;
                /* for(int j = 0; j < ruleChar.length; j++){
                    ruleset[index][i] = ruleChar[j];
                    i++;
                }
                break; */
            }
        }

        if(is_NT_rule){
            for(int i = 0; i < rulesetNT[index].length; i ++){
                if(rulesetNT[index][i] == null)
                {
                    rulesetNT[index][i] = rule;
                    break;
                }
            }
        } else {
            for(int i = 0; i < rulesetT[index].length; i ++){
                if(rulesetT[index][i] == null)
                {
                    rulesetT[index][i] = rule;
                    break;
                }
        } 
    }
}


    //____________________________________________________________________________________________________

    // checks if rule is NT rule

    //____________________________________________________________________________________________________



    public boolean is_NT_rule(char[] rule){
        for (char inputChar : rule) {
            if(!is_NT_symbol(inputChar, NTerminalToInteger)){
                return false;
            }
        }
        return true;
    }


    //____________________________________________________________________________________________________

    // make 2D array smaller and nicer

    //____________________________________________________________________________________________________



    public String[][] beautifyStringMatrix(String[][] matrix){
        int first = 0;
        int second = 0;
        int tempfirst = 0;
        int tempsecond = 0;

        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                if(matrix[i][j] != null){
                    tempfirst = i;
                    tempsecond = j;
                }
            }
            if(tempfirst >= first){
                first = tempfirst;
            }
            if(tempsecond >= second){
                second = tempsecond;
            }
        }

        String[][] smallerMatrix = new String[first+1][second+1];

        for(int i = 0; i < smallerMatrix.length; i++){
            for(int j = 0; j < smallerMatrix[i].length; j++){
                if(matrix[i][j] != null){
                    smallerMatrix[i][j] = matrix[i][j];
                }
                else{
                    smallerMatrix[i][j] = "";
                }
            }
        }

        return smallerMatrix;
    }





    //____________________________________________________________________________________________________

    // prints 2D matrix in terminal

    //____________________________________________________________________________________________________



    public void printMatrix(char[][] rules){
        for (char[] row : rules)
            System.out.println(Arrays.toString(row));
    }


    
    public void printStringMatrix(String[][] rules){
        for (String[] row : rules)
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