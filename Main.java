class Main {
    public static void main(String[] args) {

        Parser parser = new Parser(args);
    }
}

/*
 * A main class that works roughly as follows for a given parser, an input grammar, 
 * and an enumeration of longer and longer input strings. 
 * 
 * For every input string, it runs the parser on that input string and measures the time that takes. 
 * 
 * The length of the input string as well as the time, number of operations executed, 
 * and truth value returned is printed on the standard output before advancing to the next input string. 
 * 
 * This results in a table that can be pasted into some tool for creating a graph of the running time 
 * and/or number of operations. 
 * 
 * To easily create enumerations of input strings, it may be convenient to realize each as an implementation 
 * of Enumeration String. 
 * For example, to run an experiment using strings of the form anbn where n increases in steps of 20 
 * one could define a class in which nextElement() first returns a20b20 and then prepends 
 * and appends 20 as and bs, resp., with each subsequent call of nextElement(). 
 * 
 * It pays off to think a while about a convenient infrastructure for this type of task.
 * 
 */