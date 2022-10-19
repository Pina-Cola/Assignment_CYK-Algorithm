import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    Grammar grammar = new Grammar();

    public String[][] ruleset; // all rules
    public String[][] rulesetNT; // NT rules
    public String[][] rulesetT; // T rules

    public String[][][] ruleset_numbers; // all rules
    public String[][][] rulesetNT_numbers; // NT rules
    public String[][][] rulesetT_numbers; // T rules

    public Integer[][][] ruleset_int; // all rules
    public Integer[][][] rulesetNT_int; // NT rules
    public Integer[][][] rulesetT_int; // T rules

    public Integer[] inputAsInt;
    public ArrayList<Character> Int_ArrayList = new ArrayList<Character>();

    Boolean[][][] table;

    long counterN;
    long counterTD;
    long counterBU;

    long timeElapsedNaive;
    long timeElapsedBU;
    long timeElapsedTD;

    int errorCounter;

    Integer[][][] DP;

    public Parser(String[] inputString, String inputWord) {

        boolean isCNF = grammar.isCNF(inputString);

        System.out.println("");
        System.out.println("Grammar in CNF? " + isCNF);

        if(!isCNF){
            inputString = grammar.changeIntoCNF(inputString);
        }

        grammar.nts_to_array(inputString);

        ruleset = grammar.getRuleset();
        rulesetNT = grammar.getRulesetNT();
        rulesetT = grammar.getRulesetT();

        ruleset_numbers = grammar.getRuleset_numbers();
        rulesetNT_numbers = grammar.getRulesetNT_numbers();
        rulesetT_numbers = grammar.getRulesetT_numbers();

        ruleset_int = grammar.changeIntoIntMatrix(ruleset_numbers);
        rulesetT_int = grammar.changeIntoIntMatrix(rulesetT_numbers);
        rulesetNT_int = grammar.changeIntoIntMatrix(rulesetNT_numbers);

        System.out.println("");
        System.out.println("Symbols as Integers:");
        Int_ArrayList = grammar.Int_ArrayList;
        grammar.Int_NT_map(Int_ArrayList);

        System.out.println("");
        System.out.println("Integer-Matrix all rules:");
        grammar.printIntMatrix(ruleset_int);
        System.out.println("");
        /* System.out.println("Matrix T rules:");
        grammar.printIntMatrix(rulesetT_int);
        System.out.println("");
        System.out.println("Matrix NT rules:");
        grammar.printIntMatrix(rulesetNT_int);
        System.out.println(""); */

        System.out.println("Inputword as Integers:");
        inputAsInt = grammar.inputStringToInt(inputWord);
        grammar.printIntegerArray(inputAsInt);
        System.out.println("");
        // this.inputWord = inputWord.toCharArray();

        // BottomUp function call
        DP = new Integer[inputAsInt.length][inputAsInt.length][inputAsInt.length];
        long startBU = System.currentTimeMillis();
        System.out.println("");
        counterBU = 0;
        System.out.println("BottomUp: " + parseBU(this.inputAsInt) + "   Amount of calls: " + counterBU + "    Amount of errors: " + errorCounter);
        long finishBU = System.currentTimeMillis();
        timeElapsedBU = finishBU - startBU;
        System.out.println("BottomUp runtime: " + timeElapsedBU + "ms");

        // TopDown function call
        long startTD = System.currentTimeMillis();
        table = new Boolean[ruleset_int.length][inputWord.length() + 1][inputWord.length() + 1];
        System.out.println("");
        counterTD = 0;
        System.out.println("TopDown: " + parseTD() + "   Amount of calls: " + counterTD);
        long finishTD = System.currentTimeMillis();
        timeElapsedTD = finishTD - startTD;
        System.out.println("TopDown runtime: " + timeElapsedTD + "ms");

        // Naive function call
        long startNaive = System.currentTimeMillis();
        System.out.println("");
        counterN = 0;
        System.out.println("Naive: " + parseNaive() + "   Amount of calls: " + counterN);
        long finishNaive = System.currentTimeMillis();
        timeElapsedNaive = finishNaive - startNaive;
        System.out.println("Naive runtime: " + timeElapsedNaive + "ms");

        // Error correction
        errorCorrection(DP, 2, 3);
    }

    // ____________________________________________________________________________________________________

    // calls recursion

    // ____________________________________________________________________________________________________

    public boolean parseNaive() {
        counterN = 0;
        return parseNaive(0, 0, inputAsInt.length);
    }

    // ____________________________________________________________________________________________________

    // naive cyk algorithm

    // ____________________________________________________________________________________________________

    public boolean parseNaive(int indexNT, int i, int j) {

        counterN += 1;

        if (i == (j - 1)) {
            for (int l = 0; l < ruleset_int[indexNT].length; l++) {
                int symbol = inputAsInt[i];
                if (ruleset_int[indexNT][l][0] != null && ruleset_int[indexNT][l][0] == symbol) {
                    return true;
                }
                if (ruleset_int[indexNT][l][1] != null && ruleset_int[indexNT][l][1] == symbol) {
                    return true;
                }
            }
            return false;
        } else {
            for (int bodyIndex = 0; bodyIndex < ruleset_int[indexNT].length; bodyIndex++) {
                if (ruleset_int[indexNT][bodyIndex][0] != null && ruleset_int[indexNT][bodyIndex][1] != null) {
                    int first = ruleset_int[indexNT][bodyIndex][0];
                    int second = ruleset_int[indexNT][bodyIndex][1];
                    for (int k = i + 1; k < j; k++) {
                        if (parseNaive(first, i, k) && parseNaive(second, k, j)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // ____________________________________________________________________________________________________

    // bottom up

    // ____________________________________________________________________________________________________

    public boolean parseBU(Integer[] word) {

        int wordLength = word.length;
        

        for (int i = 0; i < wordLength; i++) {
            if (contained(ruleset_int, word[i])) {
                int temp = containedAt(ruleset_int, word[i]);
                for(int j = 0; j < wordLength; j++){
                    if(DP[i][i][j] == null){
                        DP[i][i][j] = temp;
                        break;
                    }
                }
            }
        }

        DP = grammar.beautifyIntegerMatrix(DP);

        for (int l = 1; l < wordLength; l++) {
            for (int i = 0; i < wordLength - l; i++) {
                int j = i + l;
                for (int k = 0; k < j; k++) {

                    // for each rule:
                    for (int head = 0; head < ruleset_int.length; head++) {
                        for (int body = 0; body < ruleset_int[head].length; body++) {
                            counterBU += 1;
                            if (ruleset_int[head][body][0] != null && ruleset_int[head][body][1] != null) {
                                int first = ruleset_int[head][body][0];
                                int second = ruleset_int[head][body][1];
                                List<Integer> intListFirst = new ArrayList<>(Arrays.asList(DP[i][k]));
                                List<Integer> intListSecond = new ArrayList<>(Arrays.asList(DP[k + 1][j]));
                                if (intListFirst.contains(first) && intListSecond.contains(second)) {
                                    for(int c = 0; c < wordLength; c++){
                                        if(DP[i][j][c] == null){
                                            DP[i][j][c] = head;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        grammar.printIntMatrix(DP);
        errorCounter = errorCounter(DP);

        List<Integer> finalField = new ArrayList<>(Arrays.asList(DP[0][wordLength-1]));
        if (finalField.contains(0)) {
            return true;
        }

        return false;
    }


    // ____________________________________________________________________________________________________

    // calls recursion

    // ____________________________________________________________________________________________________

    public int errorCounter(Integer[][][] DP) {

        for(int i = 0; i < DP.length; i++){
            for(int j = DP[i].length -1 ; j >= 0; j--){
                for(int k = 0; k < DP[i][j].length; k++){
                    if(DP[i][j][k] != null && DP[i][j][k] == 0){
                        int invertJ = DP[i].length - 1 - j;
                        return Math.max(i, invertJ);
                    }
                }
            }
        }
        return DP.length-1;
    }

    // ____________________________________________________________________________________________________

    // calls recursion

    // ____________________________________________________________________________________________________

    public boolean parseTD() {
        counterTD = 0;

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++)
                for (int k = 0; k < table[i][j].length; k++) {
                    table[i][j][k] = null;
                }
        }
        return parseTD(0, 0, inputAsInt.length);
    }

    // ____________________________________________________________________________________________________

    // top down

    // ____________________________________________________________________________________________________

    public boolean parseTD(int indexNT, int i, int j) {

        counterTD += 1;
        int rulesetLength = ruleset_int[0].length;

        if (table[indexNT][i][j] != null) {
            return table[indexNT][i][j];
        }

        if (i == (j - 1)) {
            for (int l = 0; l < rulesetLength; l++) {
                int symbol = inputAsInt[i];
                if ( ruleset_int[indexNT][l][0] != null && ruleset_int[indexNT][l][0] == symbol ) {
                    return true;
                }
                if ( ruleset_int[indexNT][l][1] != null && ruleset_int[indexNT][l][1] == symbol ) {
                    return true;
                }
            }
            return false;
        } else {
            for (int bodyIndex = 0; bodyIndex < ruleset_int[indexNT].length; bodyIndex++) {
                if (ruleset_int[indexNT][bodyIndex][0] != null && ruleset_int[indexNT][bodyIndex][1] != null) {
                    for (int k = i + 1; k < j; k++) {
                        int first = ruleset_int[indexNT][bodyIndex][0];
                        int second = ruleset_int[indexNT][bodyIndex][1];
                        table[indexNT][i][j] = (parseTD(first, i, k) && parseTD(second, k, j));
                        if (table[indexNT][i][j] == true) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // ____________________________________________________________________________________________________

    // returns true if symbol in ruleset

    // ____________________________________________________________________________________________________

    public boolean contained(Integer[][][] rules, int symbol) {

        for (int i = 0; i < rules.length; i++) {
            for (int j = 0; j < rules[i].length; j++) {
                if (rules[i][j][0] != null && rules[i][j][0] == symbol) {
                    return true;
                }
                if (rules[i][j][1] != null && rules[i][j][1] == symbol) {
                    return true;
                }
            }
        }
        return false;
    }

    // ____________________________________________________________________________________________________

    // returns index (which stand for NT) if symbol in ruleset

    // ____________________________________________________________________________________________________

    public int containedAt(Integer[][][] rules, int symbol) {

        for (int i = 0; i < rules.length; i++) {
            for (int j = 0; j < rules[i].length; j++) {
                if (rules[i][j][0] != null && rules[i][j][0] == symbol) {
                    return i;
                }
                if (rules[i][j][1] != null && rules[i][j][1] == symbol) {
                    return i;
                }
            }
        }
        return -100;
    }


    // ____________________________________________________________________________________________________

    // deletes entry from CYK for deletion (symbol on "int index" and resulting entries are deleted)

    // ____________________________________________________________________________________________________

    public Integer[][][] CYKtableRemoveSymbol(Integer[][][] CYK, int index) {

        grammar.beautifyIntegerMatrix(CYK);
        // System.out.println("CYK table format: " + CYK.length);

        // replace symbol and resulting entries with -1
        for(int i = 0; i < CYK.length; i++){
            for(int j = 0; j < CYK.length; j++){
                if( i <= index && j >= index){
                        CYK[i][j][0] = -1;
                }
            }
        }
        // grammar.printIntMatrix(CYK);
        return CYK; 
    }


    // ____________________________________________________________________________________________________

    // bottom up for an replaced symbol

    // ____________________________________________________________________________________________________

    public boolean parseBU_newSymbol(Integer[][][] DP, int newSymbol) {

        int wordLength = DP.length;
        
        for (int i = 0; i < wordLength; i++) {
            if (DP[i][i][0] == -1) {
                DP[i][i][0] = newSymbol;
            }
        }
        for (int l = 1; l < wordLength; l++) {
            for (int i = 0; i < wordLength - l; i++) {
                int j = i + l;
                if(DP[i][j][0] != null && DP[i][j][0] == -1)
                {
                for (int k = 0; k < j; k++) {

                    // for each rule:
                    for (int head = 0; head < ruleset_int.length; head++) {
                        for (int body = 0; body < ruleset_int[head].length; body++) {
                            counterBU += 1;
                            if (ruleset_int[head][body][0] != null && ruleset_int[head][body][1] != null) {
                                int first = ruleset_int[head][body][0];
                                int second = ruleset_int[head][body][1];
                                List<Integer> intListFirst = new ArrayList<>(Arrays.asList(DP[i][k]));
                                List<Integer> intListSecond = new ArrayList<>(Arrays.asList(DP[k + 1][j]));
                                if (intListFirst.contains(first) && intListSecond.contains(second)) {
                                    for(int c = 0; c < wordLength; c++){
                                        if(DP[i][j][c] != null && DP[i][j][c] == head){
                                            System.out.println(head);
                                            break;
                                        }
                                        if(DP[i][j][c] == null){
                                            DP[i][j][c] = head;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                }
            }
        }
        List<Integer> finalField = new ArrayList<>(Arrays.asList(DP[0][wordLength-1]));
        if (finalField.contains(0)) {
            return true;
        }
        return false;
    }


    // ____________________________________________________________________________________________________

    // deletes -1 in CYK table

    // ____________________________________________________________________________________________________

    public Integer[][][] CYKtableCleanUp(Integer[][][] CYK) {

        for(int i = 0; i < CYK.length; i++){
            for(int j = 0; j < CYK.length; j++){
                if(CYK[i][j][0] != null && CYK[i][j][0] == -1){
                    CYK[i][j][0] = null;
                    for(int k = 1; k < CYK[i][j].length; k++){
                        if(CYK[i][j][k] != null){
                            CYK[i][j][k-1] = CYK[i][j][k];
                            CYK[i][j][k] = null;
                        }
                    }
                }
            }
        }
        return CYK;
    }


    // ____________________________________________________________________________________________________

    // deletes -1 in CYK table

    // ____________________________________________________________________________________________________

    public void errorCorrection(Integer[][][] DP, int index, int newSymbol) {

        System.out.println(" ");
        System.out.println("Error Correction: ");
        System.out.println(" " );

        CYKtableRemoveSymbol(DP, index);
        parseBU_newSymbol(DP, newSymbol);
        System.out.println("Grammar with exchanged symbol " + newSymbol + " on index " + index + ":" );
        DP = CYKtableCleanUp(DP);
        grammar.printIntMatrix(DP);
        errorCounter = errorCounter(DP);
        System.out.println("Error counter: " + errorCounter);

        System.out.println(" " );
        System.out.println("Grammar with deleted symbol " + newSymbol + " on index " + index + ":" );
        DP = CYKtableRemoveSymbol(DP, index);
        DP = CYKtableDeletion(DP);
        parseBU_newSymbol(DP, newSymbol);
        DP = CYKtableCleanUp(DP);
        grammar.printIntMatrix(DP);
        errorCounter = errorCounter(DP);
        System.out.println("Error counter: " + errorCounter);

        solveErrorWithDeletion(DP, errorCounter);
        
    }


    // ____________________________________________________________________________________________________

    // deletes row in CYK table (sets up new table)

    // ____________________________________________________________________________________________________

    public Integer[][][] CYKtableDeletion(Integer[][][] DP) {

        int indexDeletedRow = 0;
        int newSize = DP.length -1;

        Integer[][][] smallerDP = new Integer[newSize][newSize][newSize];

        for(int i = 0; i < DP.length; i++){
            if(DP[i][i][0] != null && DP[i][i][0] == -1){
                indexDeletedRow = i;
                break;
            }
        }
        for(int i = 0; i < DP.length; i++){
            for(int j = 0; j < DP[i].length; j++){
                if((i != indexDeletedRow && j != indexDeletedRow)){
                    int index_i = i;
                    int index_j = j;
                    if(i < indexDeletedRow && j < indexDeletedRow){
                        index_i = i;
                        index_j = j;
                    }
                    if(i < indexDeletedRow && j > indexDeletedRow){
                        index_i = i;
                        index_j = j-1;
                    }
                    if(i > indexDeletedRow && j < indexDeletedRow){
                        index_i = i-1;
                        index_j = j;
                    }
                    if(i > indexDeletedRow && j > indexDeletedRow){
                        index_i = i-1;
                        index_j = j-1;
                    }
                    for(int k = 0; k < newSize; k++){
                        if(DP[i][j][k] != null){
                            smallerDP[index_i][index_j][k] = DP[i][j][k];
                        }
                        else{
                            break;
                        }
                    }
                }
            }
        }
        // grammar.printIntMatrix(smallerDP);
        return smallerDP;       
    }


    // ____________________________________________________________________________________________________

    // solve error with deletion

    // ____________________________________________________________________________________________________

    public Integer[] solveErrorWithDeletion(Integer[][][] CYK, int amountOfErrors) {

        int i = 0;
        int j = 0;

        for(int index = 0; index < CYK.length; index++){
            for(int entries = 0; entries < CYK.length; entries++){
                if(CYK[amountOfErrors][index][entries] != null && CYK[amountOfErrors][index][entries] == 0){
                    i = amountOfErrors;
                    j = index;
                }
                if(CYK[index][amountOfErrors][entries] != null && CYK[amountOfErrors][index][entries] == 0){
                    i = index;
                    j = amountOfErrors;
                }
            }
        }

        int from = Math.min(i, j);
        int to = Math.max(i, j);

        // System.out.println(i + " " + j);

        Integer[] acceptedWord = new Integer[to - from];

        for(int k = 0; k < to - from; k++){
            if(inputAsInt[k + from -1] != null){
            System.out.println("k " + k + "   to " + to + "  from " + from);
            acceptedWord[k] = inputAsInt[k + from -1];
            System.out.println(acceptedWord[k]);
            }
        }

        grammar.printIntegerArray(acceptedWord);

        return acceptedWord;

    }


}