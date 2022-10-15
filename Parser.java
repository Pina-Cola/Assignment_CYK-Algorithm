public class Parser {

    Grammar grammar = new Grammar();
    
    public String[][] ruleset;      // all rules
    public String[][] rulesetNT;    // NT rules
    public String[][] rulesetT;     // T rules

    public Integer[][][] ruleset_int;      // all rules
    public Integer[][][] rulesetNT_int;    // NT rules
    public Integer[][][] rulesetT_int;     // T rules

    public char[] inputWord;

    public Integer[] inputAsInt;

    Boolean [][][] table;

    long counterN;
    long counterTD;
    long counterBU;

    long timeElapsedNaive;
    long timeElapsedBU;
    long timeElapsedTD;

    public Parser(String[] inputString, String inputWord){

        grammar.nts_to_array(inputString);

        ruleset = grammar.getRuleset(inputString);
        rulesetNT = grammar.getRulesetNT(inputString);
        rulesetT = grammar.getRulesetT(inputString);

        ruleset_int = grammar.changeIntoIntMatrix(ruleset);
        rulesetT_int = grammar.changeIntoIntMatrix(rulesetT);
        rulesetNT_int = grammar.changeIntoIntMatrix(rulesetNT);

        inputAsInt = grammar.inputStringToInt(inputWord);

        System.out.println("");
        System.out.println("Matrix all rules:");
        grammar.printIntMatrix(ruleset_int);
        System.out.println("");
        System.out.println("Matrix T rules:");
        grammar.printIntMatrix(rulesetT_int);
        System.out.println("");
        System.out.println("Matrix NT rules:");
        grammar.printIntMatrix(rulesetNT_int);
        System.out.println("");

        System.out.println("Inputword as Integers:");
        grammar.printIntegerArray(inputAsInt);
        System.out.println("");
        // this.inputWord = inputWord.toCharArray();

        // BottomUp function call
        long startBU = System.currentTimeMillis();
        System.out.println("");
        counterBU = 0;
        // System.out.println("BottomUp: " + parseBU(this.inputWord) + "   Amount of calls: " + counterBU);
        long finishBU = System.currentTimeMillis();
        timeElapsedBU = finishBU - startBU;
        System.out.println("BottomUp runtime: " + timeElapsedBU + "ms");

        // TopDown function call
        long startTD = System.currentTimeMillis();
        table = new Boolean[ruleset.length][inputWord.length()+1][inputWord.length()+1];
        System.out.println("");
        counterTD = 0;
        // System.out.println("TopDown: " + parseTD() + "   Amount of calls: " + counterTD);
        long finishTD = System.currentTimeMillis();
        timeElapsedTD = finishTD - startTD;
        System.out.println("TopDown runtime: " + timeElapsedTD + "ms");


        // Naive function call
        long startNaive = System.currentTimeMillis();
        System.out.println("");
        counterN = 0;
        // System.out.println("Naive: " + parseNaive() + "   Amount of calls: " + counterN);
        long finishNaive = System.currentTimeMillis();
        timeElapsedNaive = finishNaive - startNaive;
        System.out.println("Naive runtime: " + timeElapsedNaive + "ms");
    }



    //____________________________________________________________________________________________________

    // calls recursion

    //____________________________________________________________________________________________________



    public boolean parseNaive(){
        counterN = 0;
        return parseNaive(0, 0, inputWord.length);
    }



    //____________________________________________________________________________________________________

    // naive cyk algorithm

    //____________________________________________________________________________________________________



    public boolean parseNaive(int indexNT, int i, int j){

        counterN += 1;

        if(i == (j-1)){
            for(int l = 0; l < ruleset[0].length; l++){
                String symbol = String.valueOf(inputWord[i]);
                if(ruleset[indexNT][l].equals(symbol)){
                    return true;
                }
            }
            return false;
        }
        else{
                for(int bodyIndex = 0; bodyIndex < ruleset[indexNT].length; bodyIndex++){
                    if(ruleset[indexNT][bodyIndex].length() >= 2){
                        int first = Character.getNumericValue(ruleset[indexNT][bodyIndex].charAt(0));
                        int second = Character.getNumericValue(ruleset[indexNT][bodyIndex].charAt(1));
                    for(int k = i+1; k < j; k++){
                        if(parseNaive(first,i,k) && parseNaive(second,k,j)){
                            return true;
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



    public boolean parseBU(char[] word){

        int wordLength = word.length;
        String[][] DP = new String[wordLength][wordLength];

        for (int i = 0; i < word.length; i++){
            if(contained(rulesetT, word[i])){
                String temp = String.valueOf(containedAt(rulesetT, word[i]));
                if(DP[i][i] != null){
                    DP[i][i] = DP[i][i] + temp;
                }
                else{
                    DP[i][i] = temp; 
                }
            }
        }

        DP = grammar.beautifyStringMatrix(DP);

        for(int l = 1; l < wordLength; l++){
            for(int i = 0; i < wordLength - l ; i++){
                int j = i + l;
                for(int k = 0; k < j; k++){

                    // for each rule:
                    for(int head = 0; head < ruleset.length; head++){
                        for(int body = 0; body < ruleset[head].length; body++){
                            counterBU += 1;
                            if(ruleset[head][body].length() >= 2 && !ruleset[head][body].isEmpty()){
                                String first = "" + ruleset[head][body].charAt(0);
                                String second = "" + ruleset[head][body].charAt(1);
                                first = first.toString();
                                second = second.toString();

                                if(DP[i][k].contains(first) && DP[k+1][j].contains(second)){
                                    String temp = "" + head;
                                    DP[i][j] = DP[i][j] + temp;                               
                                }
                            }                          
                        }
                    }
                }
            } 
        } 

        // grammar.printStringMatrix(DP);

        if(DP[0][wordLength-1].contains("0")){
            return true;
        }

        return false;
    }


    //____________________________________________________________________________________________________

    // calls recursion

    //____________________________________________________________________________________________________



    public boolean parseTD(){
        counterTD = 0;
        
        for(int i = 0; i < table.length; i++){
            for(int j = 0; j < table[i].length; j++)
                for(int k = 0; k < table[i][j].length; k++){
                    table[i][j][k] = null;
                }
        }
        return parseTD(0, 0, inputWord.length);
    }



    //____________________________________________________________________________________________________

    // top down

    //____________________________________________________________________________________________________



    public boolean parseTD(int indexNT, int i, int j){

        counterTD += 1;
        int rulesetLength = ruleset[0].length;

        if(table[indexNT][i][j] != null){
            return table[indexNT][i][j];
        }

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
                for(int bodyIndex = 0; bodyIndex < ruleset[indexNT].length; bodyIndex++){
                    if(ruleset[indexNT][bodyIndex].length() >= 2){
                    for(int k = i+1; k < j; k++){
                        int first = Character.getNumericValue(ruleset[indexNT][bodyIndex].charAt(0));
                        int second = Character.getNumericValue(ruleset[indexNT][bodyIndex].charAt(1));
                        table[indexNT][i][j] = (parseTD(first,i,k) && parseTD(second,k,j));
                        if(table[indexNT][i][j] == true){
                            return true;
                        }
                    }
                }
            }
        } 
        return false;
    }



    //____________________________________________________________________________________________________

    // returns true if symbol in ruleset

    //____________________________________________________________________________________________________



    public boolean contained(String[][] rules, char symbol){

        String s = symbol + "";

        for(int i = 0; i < rules.length; i++){
            for(int j = 0; j < rules[i].length; j++){
                if(rules[i][j].contains(s)){
                    return true;
                }
            }
        }
        return false;
    }



    //____________________________________________________________________________________________________

    // returns index (which stand for NT) if symbol in ruleset

    //____________________________________________________________________________________________________



    public int containedAt(String[][] rules, char symbol){

        String s = symbol + "";

        for(int i = 0; i < rules.length; i++){
            for(int j = 0; j < rules[i].length; j++){
                if(rules[i][j].contains(s)){
                    return i;
                }
            }
        }
        return -100;
    }
}

