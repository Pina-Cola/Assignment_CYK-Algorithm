public class Parser extends Grammar {

    Grammar grammar = new Grammar();
    public String[][] ruleset;      // all rules
    public String[][] rulesetNT;    // NT rules
    public String[][] rulesetT;     // T rules

    public char[] inputWord;

    Boolean [][][] table;

    int counter = 0;

    public Parser(String[] inputString, String inputWord){

        grammar.nts_to_int(inputString);

        ruleset = grammar.getRuleset(inputString);
        rulesetNT = grammar.getRulesetNT(inputString);
        rulesetT = grammar.getRulesetT(inputString);


        System.out.println("Input word: " + inputWord);
        this.inputWord = inputWord.toCharArray();

        // Naive function call
        long startNaive = System.currentTimeMillis();
        System.out.println("");
        System.out.println("Naive: " + parseNaive() + "   Amount of calls: " + counter);
        long finishNaive = System.currentTimeMillis();
        long timeElapsedNaive = finishNaive - startNaive;
        System.out.println("Naive runtime: " + timeElapsedNaive + "ms");

        // BottomUp function call
        long startBU = System.currentTimeMillis();
        System.out.println("");
        counter = 0;
        System.out.println("BottomUp: " + parseBU(this.inputWord) + "   Amount of calls: " + counter);
        long finishBU = System.currentTimeMillis();
        long timeElapsedBU = finishBU - startBU;
        System.out.println("Naive runtime: " + timeElapsedBU + "ms");

        // TopDown function call
        long startTD = System.currentTimeMillis();
        table = new Boolean[ruleset.length][inputWord.length()+1][inputWord.length()+1];
        System.out.println("");
        counter = 0;
        System.out.println("TopDown: " + parseTD() + "   Amount of calls: " + counter);
        long finishTD = System.currentTimeMillis();
        long timeElapsedTD = finishTD - startTD;
        System.out.println("Naive runtime: " + timeElapsedTD + "ms");
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
        // grammar.printStringMatrix(DP);
        // System.out.println("");

        for(int l = 1; l < wordLength; l++){
            for(int i = 0; i < wordLength - l + 1; i++){
                int j = i + l - 1;
                for(int k = 0; k < l + 1; k++){

                    // for each rule:
                    for(int head = 0; head < ruleset.length; head++){
                        for(int body = 0; body < ruleset[head].length; body++){
                            counter += 1;
                            if(ruleset[head][body].length() >= 2 && !ruleset[head][body].isEmpty()){
                                // System.out.println("String: " + ruleset[head][body]);
                                String first = "" + ruleset[head][body].charAt(0);
                                String second = "" + ruleset[head][body].charAt(1);
                                first = first.toString();
                                second = second.toString();
                                // System.out.println("first: " + first);
                                // System.out.println("second: " + second);
                                // System.out.println("i: " + i + " j: " + " k : " + k);
                                // System.out.println("");

                                if(DP[i][k].contains(first) && DP[k+1][j].contains(second)){
                                    String temp = "" + head;
                                    // System.out.println(temp);
                                    DP[i][j] = DP[i][j] + temp;                               
                                }
                            }                          
                        }
                    }
                }
            } 
        } 

        grammar.printStringMatrix(DP);

        if(DP[0][wordLength-1].equals("0")){
            return true;
        }

        return false;
    }


    //____________________________________________________________________________________________________

    // calls recursion

    //____________________________________________________________________________________________________



    public boolean parseTD(){
        counter = 0;
        int wordLength = inputWord.length;
        
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

        counter += 1;

        int rulesetLength = ruleset[0].length;
        // int wordLength = inputWord.length;

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
            for(int headIndex = indexNT; headIndex < ruleset.length; headIndex++){
                for(int bodyIndex = 0; bodyIndex < ruleset[headIndex].length; bodyIndex++){
                    if(ruleset[headIndex][bodyIndex].length() >= 2){
                    for(int k = i+1; k < j; k++){
                        int first = Character.getNumericValue(ruleset[headIndex][bodyIndex].charAt(0));
                        int second = Character.getNumericValue(ruleset[headIndex][bodyIndex].charAt(1));
                        table[headIndex][i][j] = (parseTD(first,i,k) && parseTD(second,k,j));
                        if(table[headIndex][i][j] == true){
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

