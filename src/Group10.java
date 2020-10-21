import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.List;
import java.math.BigInteger;

public class Group10 {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException,IOException {
        // testing the comparator:
        //Data.test_Data(); // This MUST be commented out for your submission to the competition!


        if (args.length < 2) {
            System.out.println("Please run with two command line arguments: input and output file names");
            System.exit(0);
        }

        String inputFileName = args[0];
        String outFileName = args[1];

        // read as strings
        String [] data = readData(inputFileName);
        String [] toSort = data.clone();
        Data [] sorted = sort(toSort); // Warm up the VM
        toSort = data.clone();
        Thread.sleep(10); //to let other things finish before timing; adds stability of runs

        long start = System.currentTimeMillis(); // Begin the timing
        sorted = sort(toSort);
        long end = System.currentTimeMillis();   // End the timing

        System.out.println(end - start);         // Report the results
        writeOutResult(sorted, outFileName);
    }

    // YOUR SORTING METHOD GOES HERE.
    // You may call other methods and use other classes.
    // You may ALSO modify the methods, innner classes, etc, of Data[]
    // But not in way that transfers information from the warmup sort to the timed sort.
    // Note: you may change the return type of the method.
    // You would need to provide your own function that prints your sorted array to
    // a file in the exact same format that my program outputs
    private static Data[] sort(String[] toSort) {

        Data[] fullList = new Data[toSort.length];
        for (int i = 0; i < toSort.length; ++i) {
            fullList[i] = new Data(toSort[i]);
        }
        Radix radixSort = new Radix();
        radixSort.radixsort(fullList,toSort.length);
        for(int i = 0; i < toSort.length - 1; i++){

            if(fullList[i].word.compareTo(fullList[i+1].word) == 1){
                Data save = fullList[i];
                fullList[i] = fullList[i + 1];
                fullList[i + 1] = save;
            }
        }

        return fullList;
    }

//GeeksforGeeks.org
    static class Radix {

        long getMax(Data[] arr, int n) {
            Data mx = arr[0];
            for (int i = 1; i < n; i++) {
                if (arr[i].prime > mx.prime) {
                    mx = arr[i];
                }
            }
            return mx.prime;
        }

        // A function to do counting sort of arr[] according to
        // the digit represented by exp.
        void countSort(Data[] arr, int n, int exp) {
            Data[] output = new Data[n]; // output array
            int i;
            int[] count = new int[10];
            Arrays.fill(count, 0);

            // Store count of occurrences in count[]
            for (i = 0; i < n; i++)
                count[((arr[i].prime / exp) % 10)]++;

            // Change count[i] so that count[i] now contains
            // actual position of this digit in output[]
            for (i = 1; i < 10; i++)
                count[i] += count[i - 1];

            // Build the output array
            for (i = n - 1; i >= 0; i--) {
                output[count[((arr[i].prime / exp) % 10)] - 1] = arr[i];
                count[(arr[i].prime / exp) % 10]--;
            }

            // Copy the output array to arr[], so that arr[] now
            // contains sorted numbers according to curent digit
            for (i = 0; i < n; i++)
                arr[i] = output[i];
        }

        //generates prime decomposition from lowest to highest as a int
        public double primeDecomp(long x){
            double result = 0;
            int place = 0;

            if(x == 1){
                result = result + x;
            }

            for(int i = 2; i<= x; i++) {
                while(x%i == 0) {
                    result = (result*Math.pow(10,place) + i);
                    x = x/i;
                    place++;
                }
            }

            if(x >2) {
                result = (result*Math.pow(10,place) + x);
            }
            return result;
        }

        // The main function to that sorts arr[] of size n using
        // Radix Sort
        void radixsort(Data[] arr, int n) {

            long m = getMax(arr, n);

            for (int exp = 1; m / exp > 0; exp *= 10)
                countSort(arr, n, exp);
        }
    }

    private static void printArray(String[] Arr, int n) {
        for(int i = 0; i < n; i++) {
            System.out.println(Arr[i]);
        }
    }

    private static String[] readData(String inFile) throws FileNotFoundException,IOException {
        List<String> input = Files.readAllLines(Paths.get(inFile));
        // the string array is passed just so that the correct type can be created
        return input.toArray(new String[0]);
    }

    private static void writeOutResult(Data[] sorted, String outputFilename) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(outputFilename);
        for (Data s : sorted) {
            out.println(s.word);
        }
        out.close();
    }

    private static class GematriaComparator implements Comparator<Data> {
        public long toVal(char ch){ // This function is an ancient evil that has no place in a unicode-based world :(
            return (int) ch -(int) 'a' + 1;    //type-casting a ch to (int) turns it into an ascii value
        } //Warning:  this will work with non-lower-case ascii characters too.

        public long gematrify(String str){
            char[] ch=str.toCharArray();
            long gematria = 0;
            long multiplier=1;
            for(int i = str.length()-1; i>=0 ;i--){ //Work from the right to the left
                gematria += toVal(ch[i])*multiplier;
                multiplier = 2*multiplier;
            }
            return gematria;
        }
        boolean isPrime(long n){
            for(int i=2;i<=Math.sqrt(n); i++){
                if(n%i==0){return false;}
            }
            return true;
        }
        long nextPrime(long n){
            long m=n;
            while(true){ // In a worst-case scenario we'll run out of values for m... if we don't throw an exception we'll wrap around to the negatives.  We'll find a prime eventually
                m=m+1;
                if(isPrime(m)){return(m);}
            }
        }
        @Override
        public int compare(Data s1, Data s2) {
            long g1 = gematrify(s1.word);
            long g2 = gematrify(s2.word);

            if(g1==g2){return s1.word.compareTo(s2.word);} // in case of tie, compare lexicographically
            long p = 2;
            boolean done = false;
            boolean d1 = false;
            boolean d2 = false;
            while(!done) { //Since g1 != g2 as we strip divisors we either find a smaller one, or one of the two reaches 1 first
                if(g1==1){return(-1);}
                if(g2==1){return(1);}
                // We know that g1 AND g2 have unprocessed divisors
                d1 = (g1%p==0); //d1 is a BOOLEAN value
                d2 = (g2%p==0); //d2 is a BOOLEAN VALUE
                if((!d1) && (!d2)){p=nextPrime(p);continue;}//Neither are divisible, move to next prime
                if(d1 && d2){ //both are divisible.  Remove the divisor and continue
                    g1=g1/p;
                    g2=g2/p;
                    continue;
                }
                if(d1){return -1;} else {return 1;}
            }
            System.out.println("WARNING... you should't be here!");
            return(0); //This should NEVER happen
        }
    }

    private static class Data {
        Radix toprime = new Radix();
        GematriaComparator gematrify = new GematriaComparator();

        public String word; // The original string-- useful to outputting at the end.
        public int prime;

        public Data(String inWord) {
            word = new String(inWord); // Make a copy of the string
            prime = (int) toprime.primeDecomp(gematrify.gematrify(word));
        }
        public static void print_test(String s1,String s2){
            Data testItem1 = new Data(s1);
            Data testItem2 = new Data(s2);
            GematriaComparator comparator = new GematriaComparator();

            System.out.println("Defining: s1 = "+s1+" and s2 = "+s2);
            System.out.println("\tg1 = "+comparator.gematrify(s1)+ " and g2 = "+comparator.gematrify(s2));
            System.out.println("Compare: "+s1+" to "+s2+": ");
            System.out.println("Result="+comparator.compare(testItem1,testItem2));
            System.out.println("Compare: "+s2+" to "+s1+": ");
            System.out.println("Result="+comparator.compare(testItem2,testItem1));
            System.out.println("---");
        }

        public static void test_Data() {
            String s1,s2;


            Data a = new Data("cat");
            System.out.println(a.prime);
            Data b = new Data("b");
            System.out.println(b.prime);
            Data c = new Data("a");
            System.out.println(c.prime);



            print_test("a","b");      // Two of the same thing
            print_test("be","a");  // Two different
            print_test("cat","doh");      // both with even gematria
            System.out.println("---");
        }

    }
}
