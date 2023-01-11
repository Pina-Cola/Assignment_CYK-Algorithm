import java.util.ArrayList;
import java.util.Arrays;

public class Grammar {

    // expected input for a rule: 
    // for example rule S -> AB as "SAB"

    // Some tests:
    // java Main "SSS" "SLA" "SLR" "ASR" "L(" "R)" "(())"
    // java Main "SAc" "Sb" "AaS" "AaB" "BbS" "abc"
    // java Main "SAL" "Aa" "Bb" "Cc" "Dd" "Ee" "Ff" "Gg" "Hh" "Ii" "Jj" "Kk" "Ll" "Mm" "al"

    public char[] NTerminalToInteger;
    public ArrayList<Character> Int_ArrayList = new ArrayList<Character>();
    
    public int inputLength;
    public int rulesLength;

    public String alreadyInt = "";

    public boolean isCNF;

    // Rulesets 
    public String[][] ruleset;      // all rules
    public String[][] rulesetNT;    // NT rules
    public String[][] rulesetT;     // T rules

    // Rulesets with number replacements
    public String[][][] ruleset_numbers;      // all rules
    public String[][][] rulesetNT_numbers;    // NT rules
    public String[][][] rulesetT_numbers;     // T rules



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


    public String[][] getRuleset(){
        return ruleset;
    }


    //____________________________________________________________________________________________________

    // returns rulesetT

    //____________________________________________________________________________________________________


    public String[][] getRulesetT(){
        return rulesetT;
    }


    //____________________________________________________________________________________________________

    // returns rulesetNT

    //____________________________________________________________________________________________________


    public String[][] getRulesetNT(){
        return rulesetNT;
    }


    //____________________________________________________________________________________________________

    // checks if the nonterminal leads to a terminal symbol

    //____________________________________________________________________________________________________


    public boolean is_T_rule(int symbol){

        if(rulesetT_numbers[symbol][0][0] != null){
            // return true;
            if(symbol >= rulesetNT_numbers.length){
                return true;
            }
            if(symbol < rulesetNT_numbers.length && rulesetNT_numbers[symbol][0][0] == null)
            {
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }





    //____________________________________________________________________________________________________

    // returns ruleset with numbers

    //____________________________________________________________________________________________________


    public String[][][] getRulesetNT_numbers(){
        return rulesetNT_numbers;
    }

    public String[][][] getRulesetT_numbers(){
        return rulesetT_numbers;
    }

    public String[][][] getRuleset_numbers(){
        return ruleset_numbers;
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

    // translates integer back to T-symbol

    //____________________________________________________________________________________________________


    public char intToSymbol(Integer inputSymbol){
        if(Int_ArrayList.get(inputSymbol) !=  null){
            return Int_ArrayList.get(inputSymbol);  
        }
        return 'x';
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


        String[][] int_map = new String[2][ArrayList.size()];

        for(int i = 0; i < ArrayList.size(); i++){
            // char temp = (char)(i+'0');
            String temp = String.valueOf(i);
            int_map[0][i] = temp;
            int_map[1][i] = "" + ArrayList.get(i);
        }
        printTable(int_map);
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


    public String[][][] replace_NT_with_int (String[][] ruleArray) {


        String[][][] stringRuleArrayWithInteger = new String[ruleArray.length][ruleArray[0].length][2];
        // loop over input ruleset:
        for(int i = 0; i < ruleArray.length; i++){
            for(int j = 0; j < ruleArray[i].length; j++){
                
                // loop over String in ruleset (body of the rule)
                for(int singleSymbolIndex = 0; singleSymbolIndex < ruleArray[i][j].length(); singleSymbolIndex++){

                    // loop over NT symbole
                    for(int k = 0; k < NTerminalToInteger.length; k++){

                        // replaces NT with int
                        if(ruleArray[i][j].charAt(singleSymbolIndex) == NTerminalToInteger[k]){
                            // char oldEntry = ruleArray[i][j].charAt(singleSymbolIndex);
                            String replacement = "" + k;
                            for(int l = 0; l < stringRuleArrayWithInteger[i][j].length; l++){
                                if(stringRuleArrayWithInteger[i][j][l] == null){
                                stringRuleArrayWithInteger[i][j][l] = replacement;
                                break;
                                }
                            }
                            
                        }
                        else if(k == (NTerminalToInteger.length - 1) && ruleArray[i][j] != null
                         && !is_NT_symbol(ruleArray[i][j].charAt(singleSymbolIndex), NTerminalToInteger)){
                            for(int l = 0; l < stringRuleArrayWithInteger[i][j].length; l++){
                                if(stringRuleArrayWithInteger[i][j][l] == null){
                                stringRuleArrayWithInteger[i][j][l] = ruleArray[i][j];
                                break;
                                }
                            }
                        } 
                    }
                } 
            }
        }
        return stringRuleArrayWithInteger; 
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

        ruleset_numbers = replace_NT_with_int(ruleset);
        rulesetNT_numbers = replace_NT_with_int(rulesetNT);
        rulesetT_numbers = replace_NT_with_int(rulesetT);
        
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



    public Integer[][][] changeIntoIntMatrix(String[][][] rulesetString){

        rulesetString = replace_T_symbols(rulesetString);


        Integer[][][] rulesetInt = new Integer[rulesetString.length][rulesetString[0].length][2];

        for(int i = 0; i < rulesetString.length; i++){
            for(int j = 0; j < rulesetString[i].length; j++){
                    if(rulesetString[i][j][0] != null){
                        if(rulesetString[i][j][1] != null){
                            int first = Integer.parseInt(rulesetString[i][j][0]);
                            int second = Integer.parseInt(rulesetString[i][j][1]);
                            rulesetInt[i][j][0] = first;
                            rulesetInt[i][j][1] = second;
                        }
                        else{
                            int first = Integer.parseInt(rulesetString[i][j][0]);
                            rulesetInt[i][j][0] = first;
                        }

                    }
            }
        }

    return rulesetInt;
        
    }


    //____________________________________________________________________________________________________

    // make arrays smaller and nicer

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



    public String[] beautify1DStringArray(String[] matrix){

        int counter = 0;

        for(int i = 0; i < matrix.length; i++){
            if(matrix[i] != null){
                counter += 1;
            }
        }

        String[] smallerArray = new String[counter];

        for(int i = 0; i < counter; i++){
            smallerArray[i] = matrix[i];
        }

        return smallerArray;
        
    }


    //____________________________________________________________________________________________________



    public Integer[][][] beautifyIntegerMatrix(Integer[][][] matrix){
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

        Integer[][][] smallerMatrix = new Integer[first+1][second+1][2];

        for(int i = 0; i < smallerMatrix.length; i++){
            for(int j = 0; j < smallerMatrix[i].length; j++){
                if(i < matrix.length && j < matrix[i].length){
                    if(matrix[i][j] != null && matrix[i][j][0] != null){
                        smallerMatrix[i][j][0] = matrix[i][j][0];
                    }
                    else{
                        smallerMatrix[i][j][0] = null;
                    }
                    if(matrix[i][j] != null && matrix[i][j].length > 1 && matrix[i][j][1] != null){
                        smallerMatrix[i][j][1] = matrix[i][j][1];
                    }
                    else{
                        smallerMatrix[i][j][1] = null;
                    }
                }
            }
        }
        return smallerMatrix;
    }



    //____________________________________________________________________________________________________

    // prints matrix in terminal

    //____________________________________________________________________________________________________


    public void printIntegerArray(Integer[] array){
        for(int i = 0; i < array.length; i++){
            System.out.print(array[i] + " ");
        }
    }

    public void printCharArray(char[] array){
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

    public void printIntMatrix(Integer[][] rules){
        for (Integer[] row : rules)
            System.out.println(Arrays.toString(row));
    }

    public void printIntMatrix(Integer[][][] printMatrix){
        String[][] stringMatrix = new String[printMatrix.length][printMatrix[0].length];
        for(int i = 0; i < printMatrix.length; i++){
            for(int j = 0; j < printMatrix[i].length; j++){
                if(printMatrix[i][j][0] != null && printMatrix[i][j][1] != null){
                String ruleTemp = String.valueOf(printMatrix[i][j][0]) + " " + String.valueOf(printMatrix[i][j][1]);
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


    public void print3DStringMatrix(String[][][] printMatrix){
        String[][] stringMatrix = new String[printMatrix.length][printMatrix[0].length];
        for(int i = 0; i < printMatrix.length; i++){
            for(int j = 0; j < printMatrix[i].length; j++){
                String temp = "";
                for(int k = 0; k < printMatrix[i][j].length; k++){
                    if(printMatrix[i][j][k] != null){
                    temp = temp + printMatrix[i][j][k];
                    }
                }
                stringMatrix[i][j] = temp;
            }
        }

        printStringMatrix(stringMatrix);
    }


    public void print3DBooleanMatrix(Boolean[][][] printMatrix){
        String[][] stringMatrix = new String[printMatrix.length][printMatrix[0].length];
        for(int i = 0; i < printMatrix.length; i++){
            for(int j = 0; j < printMatrix[i].length; j++){
                String temp = "   ";
                for(int k = 0; k < printMatrix[i][j].length; k++){
                    if(printMatrix[i][j][k] != null){
                        if(printMatrix[i][j][k] == true){
                            temp = " 1 ";
                        }
                        if(printMatrix[i][j][k] == false){
                            temp = " 0 ";
                        }
                    }
                }
                stringMatrix[i][j] = temp;
            }
        }

        printStringMatrix(stringMatrix);
    }



    public static void printTable(String[][] table) {
        int maxColumns = 0;
        for (int i = 0; i < table.length; i++) {
          maxColumns = Math.max(table[i].length, maxColumns);
        }
      
        int[] lengths = new int[maxColumns];
        for (int i = 0; i < table.length; i++) {
          for (int j = 0; j < table[i].length; j++) {
            lengths[j] = Math.max(table[i][j].length(), lengths[j]);
          }
        }
      
        String[] formats = new String[lengths.length];
        for (int i = 0; i < lengths.length; i++) {
         formats[i] = "%1$" + lengths[i] + "s" 
             + (i + 1 == lengths.length ? "\n" : " ");
       }
      
        for (int i = 0; i < table.length; i++) {
          for (int j = 0; j < table[i].length; j++) {
            System.out.printf(formats[j], table[i][j]);
          }
        }
      }




    //____________________________________________________________________________________________________

    // replace T symbols with Integers

    //____________________________________________________________________________________________________



    public String[][][] replace_T_symbols(String[][][] R){
      

        NTerminalToInteger = cleanCharArray(NTerminalToInteger);

        for(int i = 0; i < NTerminalToInteger.length; i++){
            String symbol = "" + NTerminalToInteger[i];
            if(!alreadyInt.contains(symbol)){
                Int_ArrayList.add(NTerminalToInteger[i]);
                alreadyInt = alreadyInt + symbol;
            }    
        }
       
        int startInt = checkNTAmount(NTerminalToInteger);

        String intString = "0123456789";

        for(int i = 0; i < R.length; i++){
            for(int j = 0; j < R[i].length; j++){
                for(int k = 0; k <  R[i][j].length; k++){
                    if(R[i][j][k] != null){

                    String originalSymbol = R[i][j][k];
                    char originalChar = originalSymbol.charAt(0);

                    if(intString.indexOf(originalSymbol) == -1 && !alreadyInt.contains(R[i][j][k])){
                      
                        String index = String.valueOf(startInt);
                        R[i][j][k] = index;
                        
                        Int_ArrayList.add(originalSymbol.charAt(0));
                        alreadyInt += originalSymbol;
                        startInt++;
                    }
                    if(intString.indexOf(originalSymbol) == -1 && alreadyInt.contains(originalSymbol)){
                        int indexFromArrayList = Int_ArrayList.indexOf(originalChar);
                        String index = String.valueOf(indexFromArrayList);
                        R[i][j][k] = index;
                    }
                    }
                }
                

            }
        }


        return R;  
        
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


    public Integer[] cleanIntArray(Integer[] array){

        int size = 0;
        for(int i = 0; i < array.length; i++){
            if(array[i] != null)
            {
                size += 1;
            }
        }
        Integer[] newArray = new Integer[size];
        for(int i = 0; i < size; i++){
            if(array[i] != null)
            {
                newArray[i] = array[i];
            }
        }

        return newArray;
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
        // printIntegerArray(inputAsInt);

        return inputAsInt;

    }


    //____________________________________________________________________________________________________

    // tests if grammar is in CNF

    //____________________________________________________________________________________________________



    public boolean isCNF(String[] inputRules){

        for(String rule : inputRules){
            if(rule.length() == 3){
                char firstSymbol = rule.charAt(1);
                char secondSymbol = rule.charAt(2);
                if(!Character.isUpperCase(firstSymbol) || !Character.isUpperCase(secondSymbol)){
                    isCNF = false;
                    return false;
                }
            }
        }        
        
        isCNF = true;
        return true;

    }

    //____________________________________________________________________________________________________

    // changes grammar into CNF

    //____________________________________________________________________________________________________



    public String[] changeIntoCNF(String[] inputRules){

        String usedNTsymbol = getNTsymbols(inputRules);
        String[] CNFrules = new String[inputRules.length * 3];  
        int index = 0;  

        String[][] newNTsymbols = new String[inputRules.length][2];
        int indexForNewSymbols = 0; 

        for(String rule : inputRules){

            if(rule.length() == 3){
                char head = rule.charAt(0);
                char firstSymbol = rule.charAt(1);
                char secondSymbol = rule.charAt(2);

                // both symbols are NT symbols
                if(Character.isUpperCase(firstSymbol) && Character.isUpperCase(secondSymbol)){
                    CNFrules[index] = rule;
                    index = index+1;
                    usedNTsymbol = usedNTsymbol + rule;
                }

                // first NT then T symbol
                if(Character.isUpperCase(firstSymbol) && !Character.isUpperCase(secondSymbol)){

                    String newNT = "";

                    if(isAlreadyContained(secondSymbol, newNTsymbols)){
                        newNT = isAlreadyContainedAt(secondSymbol, newNTsymbols);
                        CNFrules[index] = "" + head + firstSymbol + newNT;
                        index = index+1;
                    }
                    else{
                    usedNTsymbol = findNewNTsymbol(usedNTsymbol);
                    newNT = "" + usedNTsymbol.charAt(usedNTsymbol.length()-1);
                    CNFrules[index] = "" + head + firstSymbol + newNT;
                    index = index+1;
                    CNFrules[index] = "" + newNT  + secondSymbol;
                    index = index+1;
                    newNTsymbols[indexForNewSymbols][0] = "" + secondSymbol;
                    newNTsymbols[indexForNewSymbols][1] = newNT;
                    indexForNewSymbols += 1;
                    }


                    

                }

                // first T then NT symbol
                if(!Character.isUpperCase(firstSymbol) && Character.isUpperCase(secondSymbol)){
                    String newNT = "";

                    if(isAlreadyContained(firstSymbol, newNTsymbols)){
                        newNT = isAlreadyContainedAt(firstSymbol, newNTsymbols);
                        CNFrules[index] = "" + head  + newNT + secondSymbol;
                        index = index+1;
                    }
                    else{
                    usedNTsymbol = findNewNTsymbol(usedNTsymbol);
                    newNT = "" + usedNTsymbol.charAt(usedNTsymbol.length()-1);
                    CNFrules[index] = "" + head  + newNT + secondSymbol;
                    index = index+1;
                    CNFrules[index] = "" + newNT + firstSymbol;
                    index = index+1;
                    newNTsymbols[indexForNewSymbols][0] = "" + firstSymbol;
                    newNTsymbols[indexForNewSymbols][1] = newNT;
                    indexForNewSymbols += 1;
                    }

                }
            }

            if(rule.length() == 2){
                /* char head = rule.charAt(0);
                char tSymbol = rule.charAt(1);
                String newNT = "";

                if(isAlreadyContained(head, newNTsymbols)){
                    usedNTsymbol = findNewNTsymbol(usedNTsymbol);
                    newNT = "" + usedNTsymbol.charAt(usedNTsymbol.length()-1);
                } */

                CNFrules[index] = rule;
                index = index+1;
                usedNTsymbol = usedNTsymbol + rule;
            }
        } 

        CNFrules = beautify1DStringArray(CNFrules);

        return CNFrules;

    }

    //____________________________________________________________________________________________________

    // changes grammar into CNF

    //____________________________________________________________________________________________________



    public Integer[][][] produceLinearGrammarMatrix(String[] inputRules){

        Integer[][][] linearGrammarMatrix = new Integer[inputRules.length][inputRules.length][2];

        Integer[][] inputRulesAsInteger = new Integer[inputRules.length][3];
        
        for(int i= 0; i < inputRules.length; i++){
            inputRulesAsInteger[i] = inputStringToInt(inputRules[i]);
        }

       printIntMatrix(inputRulesAsInteger); 

       for(int currentRule = 0; currentRule < inputRulesAsInteger.length; currentRule++){

            Integer headOfRule = inputRulesAsInteger[currentRule][0];

            for(int indexInMatrix = 0; indexInMatrix < inputRules.length; indexInMatrix++){
                if(linearGrammarMatrix[headOfRule][indexInMatrix][0] == null){
                    if(inputRulesAsInteger[currentRule][1] != null){
                        linearGrammarMatrix[headOfRule][indexInMatrix][0] = inputRulesAsInteger[currentRule][1];
                    }
                    if(inputRulesAsInteger[currentRule][2] != null){
                        linearGrammarMatrix[headOfRule][indexInMatrix][1] = inputRulesAsInteger[currentRule][2];
                    }
                    break;
                }
            }
       }

       printIntMatrix(linearGrammarMatrix);

        // Integer[][][] linearGrammarMatrix = new Integer[inputRules.length][inputRules.length][2];

        // linearGrammarMatrix = changeIntoIntMatrix(linearGrammarStringMatrix);

        return linearGrammarMatrix;


    }


    //____________________________________________________________________________________________________

    // puts NT symbols into string

    //____________________________________________________________________________________________________



    public String getNTsymbols(String[] inputRules){

        String NTsymbols = "";

        for(String rule : inputRules){
                NTsymbols = NTsymbols + rule.charAt(0);
        }  

        return NTsymbols;

    }



    //____________________________________________________________________________________________________

    // finds not used NT symbol

    //____________________________________________________________________________________________________



    public String findNewNTsymbol(String usedSymbols){

        for(char c = 'A'; c <= 'Z'; ++c){
          String symbolAsString = "" + c;
          if (!usedSymbols.contains(symbolAsString)){
            usedSymbols = usedSymbols + symbolAsString;
            return usedSymbols;
           }
        }

        return usedSymbols;

    }


    //____________________________________________________________________________________________________

    // checks if T symbol already has a replacement NT symbol

    //____________________________________________________________________________________________________



    public boolean isAlreadyContained(char symbol, String[][] newSymbols){

        String s = "" + symbol;

        for(int i = 0; i < newSymbols.length; i++){
            if(newSymbols[i][0] != null && newSymbols[i][0].contains(s)){
                return true;
            }
        }

        return false;

    }


    //____________________________________________________________________________________________________

    // returns replacement NT symbol of T symbol

    //____________________________________________________________________________________________________



    public String isAlreadyContainedAt(char symbol, String[][] newSymbols){

        String s = "" + symbol;

        for(int i = 0; i < newSymbols.length; i++){
            if(newSymbols[i][0] != null && newSymbols[i][0].contains(s)){
                return newSymbols[i][1];
            }
        }
        return "";
    }


}

