import javax.xml.stream.FactoryConfigurationError;

/*
 *  Input example:  "SSS" "SLA" "SLR" "ASR" "L(" "R)" "(())"
 *  first: rules without arrows (one rule as one String)
 *  last argument: input word
 *  first symbol is the start symbol (NT) (usually called S)
 */

class Main {


    public static void main(String[] args) {

        int grammarLength = (args.length)-1;
        String[] inputString = new String[grammarLength];
        String inputWord = new String();

        for(int i = 0; i < grammarLength; i++){
            inputString[i]=args[i];
        }
        inputWord = args[grammarLength];

        Parser parser = new Parser(inputString, inputWord);
    }
}
