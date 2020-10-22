import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Group10 {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException {

        if (args.length < 2) {
            System.out.println("Please run with two command line arguments: input and output file names");
            System.exit(0);
        }

        String inputFileName = args[0];
        String outFileName = args[1];

        // Read as strings
        String [] data = readData(inputFileName);
        String [] toSort = data.clone();
        Data [] sorted = sort(toSort); // Warm up the VM
        toSort = data.clone();
        Thread.sleep(10); // To let other things finish before timing; adds stability of runs

        long start = System.currentTimeMillis(); // Begin the timing
        sorted = sort(toSort);
        long end = System.currentTimeMillis();   // End the timing

        System.out.println(end - start);         // Report the results
        writeOutResult(sorted, outFileName);
    }

    // OUR SORTING METHOD WENT HERE.
    private static Data[] sort(String[] toSort) {

        Data[] fullList = new Data[toSort.length];
        for (int i = 0; i < toSort.length; i++) {
            fullList[i] = new Data(toSort[i]);
        }
        Radix radixSort = new Radix(); // Creates new radix for sorting
        radixSort.radixsort(fullList, toSort.length); // Calls our radix sorting method to sort the file
        return fullList;
    }

// Utilized GeeksforGeeks.org radix implementation for java https://www.geeksforgeeks.org/radix-sort/ (with our own adjustments)
    static class Radix {

        long getMax(Data[] arr, int n) { // Gets the max prime decomposed integer
            Data mx = arr[0];
            for (int i = 1; i < n; i++) {
                if (arr[i].prime > mx.prime) {
                    mx = arr[i];
                }
            }
            return mx.prime;
        }

        // A function to do counting sort of arr[] according to the digit represented by exp.
        void countSort(Data[] arr, int n, int exp) {
            Data[] output = new Data[n]; // Output array
            int i;
            int[] count = new int[10];
            Arrays.fill(count, 0); // Fills the count array with zeroes.

            // Store count of occurrences in count[]
            for (i = 0; i < n; i++)
                count[((arr[i].prime / exp) % 10)]++;

            // Change count[i] so that count[i] now contains the actual position of this digit in output[]
            for (i = 1; i < 10; i++)
                count[i] += count[i - 1];

            // Build the output array
            for (i = n - 1; i >= 0; i--) {
                output[count[((arr[i].prime / exp) % 10)] - 1] = arr[i];
                count[(arr[i].prime / exp) % 10]--;
            }

            // Copy the output array to arr[], so that arr[] now contains sorted numbers according to current digit
            for (i = 0; i < n; i++)
                arr[i] = output[i];
        }

        // Generates prime decomposition from lowest to highest as a int
        public double primeDecomp(long x) {
            double result = 0;
            int place = 0;

            if (x == 1) { // Checks specifically for a value of 1
                result = result + x;
            }

            for (int i = 2; i <= x; i++) {
                while(x % i == 0) {
                    result = (result * Math.pow(10, place) + i); // pow takes the first argument and raises it to the power of the second.
                    x = x / i;
                    place++;
                }
            }

            if (x > 2) { // Checks for x values greater than 2
                result = (result * Math.pow(10, place) + x);
            }
            return result;
        }

        // The main function to that sorts arr[] of size n using
        // Radix Sort
        void radixsort(Data[] arr, int n) {

            long m = getMax(arr, n); // Sets m with the max value

            for (int exp = 1; m / exp > 0; exp *= 10)
                countSort(arr, n, exp);
        }
    }

    private static String[] readData(String inFile) throws FileNotFoundException, IOException { // Reads the given file
        List<String> input = Files.readAllLines(Paths.get(inFile));
        // The string array is passed just so that the correct type can be created
        return input.toArray(new String[0]);
    }

    private static void writeOutResult(Data[] sorted, String outputFilename) throws FileNotFoundException { // Writes out the sorted array to the output file given when called
        PrintWriter out = new PrintWriter(outputFilename);
        for (Data s : sorted) {
            out.println(s.word);
        }
        out.close();
    }

    private static class GematriaComparator {
        public long toVal(char ch){ // This function is an ancient evil that has no place in a unicode-based world :(
            return (int) ch -(int) 'a' + 1;    // Type-casting a ch to (int) turns it into an ascii value
        } // Warning:  this will work with non-lower-case ascii characters too.

        public long gematrify(String str){
            char[] ch=str.toCharArray();
            long gematria = 0;
            long multiplier = 1;
            for(int i = str.length()-1; i >= 0; i--){ // Work from the right to the left
                gematria += toVal(ch[i]) * multiplier;
                multiplier = 2*multiplier;
            }
            return gematria; // Returns the gematria number
        }
    }

    private static class Data {
        Radix toprime = new Radix(); // Creates new radix object
        GematriaComparator gematrify = new GematriaComparator(); // Creates new gematrify comparator

        public String word; // The original string-- useful to outputting at the end.
        public int prime;

        public Data(String inWord) {
            word = new String(inWord); // Make a copy of the string
            prime = (int) toprime.primeDecomp(gematrify.gematrify(word)); // Takes the gematrified value of a word which is then handed to primeDecomp which is then given a separate value for sorting.
        }

    }
}
