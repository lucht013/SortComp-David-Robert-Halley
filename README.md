# Sorting Competition Materials 2020
Materials for UMM CSci 3501 "Algorithms and Computability" 2020 Sorting Competition.

# Table of contents
* [Notes for round 2](#notes)
* [Goal of the competition](#goal)
* [The data](#data)
* [How is the data generated](#generating)
* [How do you need to sort the data](#sortingRules)
* [Setup for sorting](#setup)
* [Submision deadlines](#deadlines)
* [Scoring](#scoring)
* [System specs](#specs)
* [Results of the first preliminary round](#round1)
* [Results of the final competition](#final)
* [Presentations](#presentation)


## Notes for round 2 <a name="notes"></a>
* get rid of second for loop in sort
* use linked list for non-common list

## Goal of the competition <a name="goal"></a>

The Sorting Competition is a multi-lab exercise on developing the fastest sorting algorithm for a given type of data. By "fast" we mean the actual running time and not the Big-Theta approximation. The solutions are developed in Java and will be ran on a single processor.

## The data  <a name="data"></a>

Data for this sorting competition consists of standard ASCII characters stored in a text file and separated by spaces.  The words will be selected from the text file `t8.shakespeare.txt` (included in this repository in the `src` directory) at random.  In addition, roughly 10 percent of the words will have two randomly chosen characters transposed.  Words will consist entirely of lowercase letters (all punctation will be removed... so the word "Don't" would become "dont").   The task will be to order the words according to the following criteria:

* The prime decomposition of a "gematria number" associated to that word (see below)
* Ties, if they occur, are broken by the alphabetical order of the word 

### Gematria Number

Gematria is the idea of relating a word to a number by assigning numeric values to the characters and then adding them up.  We'll associate letters to numeric values by assigning `a` to be `1`, `b` to be `2`, and so on until `z` is assigned a value of `26`, however we are going to use a variation-- the location of the letter will **ALSO** factor into the calculation.  We'll multiply the value of the letter by a power of two-- specifically by two to the power of the letter's position-- with the right-most position being position 0, the second to the last postition 1, and so on.  The word "cat" is associated to the value 330:

Position | Letter | Value | Place Value | Letter contribution
---------|--------|-------|-------------|---------------------
0        | t      | 20    | 2^0 = 1    | 20*1 = 20
1        | a      | 1     | 2^1 = 2    | 1*2  = 2
2        | c      | 3     | 2^2 = 4    | 3*4 =  12
**total:**|       |       |            | 20 + 2 + 12 = 34

### Prime Number Decomposition

Every positive integer greater than 1 has a unique decomposition into positive primes.  We will consider the prime decomposition of 1 to be 1.  We list the primes left to right from smallest prime to largest prime (with repeats allowed).  For example 12 = 2*2*3.  To sort the decompositions follow these rules:

1. Compare the left-most primes (note:  although 1 is not a prime it is a possible value).  The decomposition with the smallest value precedes the other
2. In the even of a tie, remove the left-most prime and repeat.   

Note that a consequence of these rule is that a prime (like 23) will precede ALL numbers for which 23 is the smallest prime.  So, for example, 23 precedes 667 after one application of rule 2:

value | decomposition | decomposition after removal of tie
------|---------------|-------------------------------------
23    | 23            | 1
667   |23 * 29        |29

But 56 precedes 23 (by rule 1) because the smallest prime in 56 (a 2) is smaller than the smallest prime in 23 (which is 23 itself):

* 23 = 23
* 56 = 2 * 23 


# How is the data generated <a name="generating"></a>

Roughly 40,000 words (as identified by Java's `Scanner` object, will be extracted from the file [t8.shakespeare.txt](src/t8.shakespeare.txt) with the same *relative* order as they appeared in the file.  All characters that are not English letters will be removed.  The remaining letters are converted to lower-case if necessary.  Ten percent of the time two locations in the reduced word (potentially the SAME location) will be transposed.  The results are stored one per line in the output file.


### The data files

The generated values are written into a data file one per line. Sample data files are: [data1.txt](src/data1.txt) (~40,000 elements), [data2.txt](src/data2.txt) (~40,000 elements), and [data3.txt](src/data3.txt) (~40,000 elements). **NOTE:** The actual data files used in the competition should be of comparable size.

## How do you need to sort the data <a name="sortingRules"></a>

Sorting is done first by the prime decomposition of the gematria numeric value.  If two words have the same gematria number the tie is resolved lexicographically.

The file [Group10.java](src/Group0.java) provides a Comparator that implements this comparison and provides some tests. Please consult it as needed. However, note that this not an optimized implementation, and you should think of a way to make it much faster. 

Once the data is sorted, it is written out to the output file, also one number per line, in the increasing order (according to the comparison given above). The files [out1.txt](src/out1.txt), [out2.txt](src/out2.txt), and [out3.txt](src/out3.txt) have the results of sorting for the three given data files. 

## Setup for sorting <a name="setup"></a>

The file [Group10.java](src/Group0.java) provides a template for the setup for your solution. Your class will be called `GroupN`, where `N` is the group number that is assigned to your group. The template class runs the sorting method once before the timing for the [JVM warmup](https://www.ibm.com/developerworks/library/j-jtp12214/index.html). It also pauses for 10ms before the actual test to let any leftover I/O or garbage collection to finish. Since the warmup and the actual sorting are done on the same array (for no reason other than simplicity), the array is cloned from the same input data. 

The data reading, the array cloning, the warmup sorting, and writing out the output are all outside of the timed portion of the method, and thus do not affect the total time. 

You may not use any global variables that depend on your data. You may, however, have global constants that are initialized to fixed values (no computation!) before the data is being read and stay the same throughout the run. These constants may be arrays of no more than 1000 `long` numberss or equivalent amount of memory. For instance, if you are storing an array of objects that contain two `long` fields, you can only have 500 of them. We consider one `long` to be the same as two `int` numbers, so you can store an array of 2000 `int` numbers.  
If in doubt about specific cases, please discuss with me. 

The method in the [Group10.java](src/Group0.java) files that you may modify is the `sort` method. It must take the array of strings. The return type of the method can be what it is now, which is the same as the parameter type `String []`, or it can be a different array type. If you are sorting in-place, i.e. the sorted result is in the same array, then you can just return a reference to that array, as my prototype method does, or make your sorting method `void`. If you are returning a different type of an array, the following rules have to be followed:
* Your `sort` method return type needs to be changed to whatever  array you are returning, and consequently the type of `sorted` array in `main` needs to be changed. 
* Your return type has to be an array (not an array list!) and it has to have the same number of elements as the original array. 
* You need to supply a method to write out your result into a file. The file has to be exactly the same as in the prototype implementation; they will be compared using `diff` system command. 

If you are not changing the return type, you don't need to modify anything other than `sort` method. 

Even though you are not modifying anything other than the `sort` method, you still need to submit your entire class: copy the template, rename the Java class to your group number, and change the`sort` method. You may use supplementary classes, just don't forget to submit them. Make sure to add your names in comments when you submit. 

Your program must print **the only value**, which is the **time** (as it currently does). Any other printed output disqualifies your submission. If you use test prints, make sure to turn them off for submission.  You **must disable** the testing output that is turned on, by default, in `Group10.java`.

**Important:** if the sorting times are too small to distinguish groups based on just one run of the sorting, so I may loop over the sorting section multiple times. If this is the case, I will let you know no later than a day after the preliminary competition and will modify `Group10` file accordingly.  

## Submision deadlines <a name="deadlines"></a>

See Canvas for the deadlines of this competition (if you're not in the class I'll send you a copy via email).

*Thursday, Oct 1st* in the lab is the *preliminary* competition.  All that is required is a good-faith effort.  It does not need to support correctly as long as you gave it a good try.  Be sure you get your code to me (in the proper format and generating the proper output) by the previous evening.

*Thursday, Oct 21* in the lab is the *final* competition. All source code is posted immediately after that. Those in class will have their names revealed, others may choose to remain anonymous (but the code will still be posted). 

## Scoring <a name="scoring"></a>

The programs are tested on a few (between 1 and 3) data sets. For each data set each group's program is run three times, the median value counts. The groups are ordered by their median score for each data file and assigned places, from 1 to N. 

The final score is given by the sum of places for all data sets. If the sum of places is equal for two groups, the sum of median times for all the runs resolves the tie. So if one group was first for one data set and third for the other one (2 sets total being run), it scored better than a group that was third for the first data set and second for the other. However, if one group was first for the first set and third for the other one, and the second group was second in both, the sum of times determines which one of them won since the sum of places is the same (1 + 3 = 2 + 2). 

If a program has a compilation or a runtime error, doesn't sort correctly, or prints anything other than the total time in milliseconds, it gets a penalty of 1000000ms for that run. 

## System specs <a name="specs"></a>

The language used is Java 8 (as installed in the CSci lab). It's ran on a single CPU core.  

I will post a script for running this program (with a correctness check and all), but for now a couple of things to know: run your program out of `/tmp` directory to avoid overhead of communications with the file server, and pin your program to a single core, i.e. run it like this:
``taskset -c 0 java GroupN``


<!--
## Results of the first preliminary round <a name="round1"></a>

This round occured October 10th 2019

The results of the first preliminary round are in the folder [round1/bin](round1/bin). The folder has all the `.class` files. Groups that didn't have a submission (or their submission didn't compile or would go into an infinite loop) show up as errors. 

Each data file was ran three times for each group, and the median result was used for scoring. 

The data files were: [prelim1.txt](round1/bin/prelim1.txt) (1,000,000 entries) and [prelim2.txt](round1/bin/prelim2.txt) (1,000,000 entries). The correct output is in files [outRun1Group0.txt](round1/bin/outRun1Group0.txt) and [outRun2Group0.txt](round1/bin/outRun2Group0.txt).

The files [results1.txt](round1/bin/results1.txt) and [results2.txt](round1/bin/results2.txt) have the complete timing results for the two data sets. The file [scoreboard.txt](round1/bin/scoreboard.txt) has the places that each team got.  

The ruby script [run_all.rb](round1/bin/run_all.rb) was used to run the programs. If you want to reproduce the results or try them on a different set, so the following:
* Create a directory in `/tmp` directory on a lab machine. 
* Copy the entire `bin` folder from github into that directory. 
* Remove the output files, results1.txt, results2.txt, and the scoreboard.txt. 
* If you want to run the programs on different data sets, call your data files `prelim1.txt` and 'prelim2.txt` and copy them into the same folder - or copy then by different names, abnd then open the  script and change the files in the `inFileNames`. 
* Type `taskset -c 0 ruby run_all.rb` to run the script. 

## Results of the competition <a name="final"></a>

Congratulations to everybody who contributed!  We had some great competition.  Thanks to everybody who participated.  You can look at the scoreboard in [round2](round2) for more exact details on how the scoring went.  The results, in order of achievement:-->

## Presentations <a name="presentation"></a>

The presentations date will be November 12(we may adjust this date when the time gets closer). Each group needs to submit a set of 4-5 pdf slides by 11:59pm the day before the presentation; you will have 3-4 minutes to present. Both group members must be a part of a presentation. The presentation must have:

* Your names (first names ok since this will be on github) and group number,
* Your results of the competition (overall place, times, correctness). If there were any correctness concerns, they need to be addressed.
* Big-picture description of your algorithm and data representation (what sorting have you used?
what were you storing? Precomputing?)
* The theoretical worst case efficiency and expected efficiency. For instance, insertion sort in theory has a quadratic efficiency, but if you are using it only on almost-sorted data, it's close to linear.
* The most intersting features of your algorithm.
* What worked, what didn't, what you would've done differently.

Be prepared to answer questions. 

