import java.util.ArrayList;
import java.util.Arrays;

public class Grammar {

    // expected input for a rule: 
    // for example rule S -> AB as "SAB"
    // java Main "SSS" "SLA" "SLR" "ASR" "L(" "R)" "(())"

    public char[] NTerminalToInteger;
    public ArrayList<Character> Int_ArrayList = new ArrayList<Character>();
    // public char[] EverythingToInt;

    public int[] inputWord;
    
    public int inputLength;
    public int rulesLength;

    public String alreadyInt = "";

    // Rulesets 
    public String[][] ruleset;      // all rules
    public String[][] rulesetNT;    // NT rules
    public String[][] rulesetT;     // T rules



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
        return max;
    }



    //____________________________________________________________________________________________________

    // fills Array NTerminalToInteger with NT symbols (one symbol per index) 

    //____________________________________________________________________________________________________


    public void nts_to_array(String[] inputString){

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
        addRules(NTerminalToInteger, inputString);
    }


    //____________________________________________________________________________________________________

    // show numbers assigned to NT symbols

    //____________________________________________________________________________________________________


    public void Int_NT_map (ArrayList<Character> ArrayList) {


        Character[][] int_map = new Character[2][ArrayList.size()];

        for(int i = 0; i < ArrayList.size(); i++){
            char temp = (char)(i+'0');
            int_map[0][i] = temp;
            int_map[1][i] = ArrayList.get(i);
        }
        printMatrix(int_map);
    }



    //____________________________________________________________________________________________________

    // returns true if char is in NTerminalToInteger

    //____________________________________________________________________________________________________


    public boolean is_NT_symbol (char NT, char[] NTerminalToInteger) {
        for(int i = 0; i <= NTerminalToInteger.length-1; i++){
            if(NTerminalToInteger[i] == NT){
                return true;
            }
            if(NTerminalToInteger[i] == 0){
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
        
        /*
        System.out.println("");
        System.out.println("Matrix all rules:");
        printStringMatrix(ruleset);
        System.out.println("");
        System.out.println("Matrix T rules:");
        printStringMatrix(rulesetT);
        System.out.println("");
        System.out.println("Matrix NT rules:");
        printStringMatrix(rulesetNT);
        System.out.println(""); */
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

    // from String[][] to int[][][]

    //____________________________________________________________________________________________________



    public Integer[][][] changeIntoIntMatrix(String[][] rulesetString){

        rulesetString = replace_T_symbols(rulesetString);

        Integer[][][] rulesetInt = new Integer[rulesetString.length][rulesetString[0].length][2];

        for(int i = 0; i < rulesetString.length; i++){
            for(int j = 0; j < rulesetString[i].length; j++){
                if(rulesetString[i][j] != null && rulesetString[i][j].length() >= 2){
                    int first = Character.getNumericValue(rulesetString[i][j].charAt(0));
                    int second = Character.getNumericValue(rulesetString[i][j].charAt(1));
                    rulesetInt[i][j][0] = first;
                    rulesetInt[i][j][1] = second;
                }
                if(rulesetString[i][j] != null && rulesetString[i][j].length() == 1){
                    int first = Character.getNumericValue(rulesetString[i][j].charAt(0));
                    rulesetInt[i][j][0] = first;
                }
            }
        }

    // replace_T_symbols(rulesetString);
    // printIntMatrix(rulesetInt);

    return rulesetInt;
        
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


    public void printIntegerArray(Integer[] array){
        for(int i = 0; i < array.length; i++){
            System.out.print(array[i] + " ");
        }
    }

    public void printMatrix(Character[][] rules){
        for (Character[] row : rules)
            System.out.println(Arrays.toString(row));
    }

    public void printMatrix(Boolean[][] rules){
        for (Boolean[] row : rules)
            System.out.println(Arrays.toString(row));
    }
    
    public void printStringMatrix(String[][] rules){
        for (String[] row : rules)
            System.out.println(Arrays.toString(row));
    }

    public void printIntMatrix(Integer[][][] printMatrix){
        String[][] stringMatrix = new String[printMatrix.length][printMatrix[0].length];
        for(int i = 0; i < printMatrix.length; i++){
            for(int j = 0; j < printMatrix[i].length; j++){
                if(printMatrix[i][j][0] != null && printMatrix[i][j][1] != null){
                String ruleTemp = String.valueOf(printMatrix[i][j][0]) + "" + String.valueOf(printMatrix[i][j][1]);
                stringMatrix[i][j] = ruleTemp;
                }
                else if(printMatrix[i][j][0] != null && printMatrix[i][j][1] == null){
                    String ruleTemp = String.valueOf(printMatrix[i][j][0]);
                    stringMatrix[i][j] = ruleTemp;
                    }
                else{
                    stringMatrix[i][j] = "";
                }
            }
        }

        printStringMatrix(stringMatrix);
    }




    //____________________________________________________________________________________________________

    // replace T symbols with Integers

    //____________________________________________________________________________________________________



    public String[][] replace_T_symbols(String[][] R){

        NTerminalToInteger = cleanCharArray(NTerminalToInteger);

        for(int i = 0; i < NTerminalToInteger.length; i++){
            String symbol = "" + NTerminalToInteger[i];
            if(!alreadyInt.contains(symbol)){
                Int_ArrayList.add(NTerminalToInteger[i]);
                alreadyInt = alreadyInt + symbol;
            }    
        }

        

        
        int startInt = checkNTAmount(NTerminalToInteger);
        // System.out.println("StartInt: " + startInt);
        String intString = "0123456789";

        // Int_NT_map(Int_ArrayList);

        for(int i = 0; i < R.length; i++){
            for(int j = 0; j < R[i].length; j++){

                for(int k = 0; k <  R[i][j].length(); k++){
                    char temp = R[i][j].charAt(k);
                    // System.out.println("Char: " + temp);
                    // Int_NT_map(Int_ArrayList);
                    if(intString.indexOf(temp) == -1 && !alreadyInt.contains(R[i][j])){
                        // System.out.println("it happens");
                        // System.out.println("");
                        String index = String.valueOf(startInt);
                        String originalSymbol = String.valueOf(temp);
                        R[i][j] = R[i][j].replace(originalSymbol, index);
                        // System.out.println("Replace " + originalSymbol + " with " + index);
                        Int_ArrayList.add(temp);
                        // System.out.println("Index: " + index);
                        // System.out.println("String: " + R[i][j]);
                        alreadyInt += originalSymbol;
                        startInt++;
                    }
                    if(intString.indexOf(temp) == -1 && alreadyInt.contains(R[i][j])){
                        int indexFromArrayList = Int_ArrayList.indexOf(temp);
                        String index = String.valueOf(indexFromArrayList);
                        String originalSymbol = String.valueOf(temp);
                        R[i][j].replace(originalSymbol, index);
                    }
                }
                

            }
        }


        return ruleset;
        
    }

    //____________________________________________________________________________________________________

    // checks amount of NT Symbols (which have integers and are not null)

    //____________________________________________________________________________________________________



    public int checkNTAmount(char[] array){
        int counter = 0;

        for(int i = 0; i < array.length; i++){
            if(array[i] != '\0'){
                counter++;
            }
        }

        return counter;
    }



    //____________________________________________________________________________________________________

    // clean the NT symbol "map"

    //____________________________________________________________________________________________________



    public char[] cleanCharArray(char[] array){

        int size = checkNTAmount(array);

        char[] arrayWithoutEmptyEntries = new char[size];

        for(int i = 0; i < size; i++){
            if(array[i] != '\0'){
                arrayWithoutEmptyEntries[i] = array[i];
            }
        }

        return arrayWithoutEmptyEntries;
    }


    //____________________________________________________________________________________________________

    // input string to int parse

    //____________________________________________________________________________________________________



    public Integer[] inputStringToInt(String inputString){
        
        Integer[] inputAsInt = new Integer[inputString.length()];

        for(int i = 0; i < inputString.length(); i++){
            char singleCharacter = inputString.charAt(i);
            int intOfCharacter = Int_ArrayList.indexOf(singleCharacter);
            inputAsInt[i] = intOfCharacter;
        }

        return inputAsInt;

    }


}

