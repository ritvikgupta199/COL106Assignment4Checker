# Unofficial Test Case Generator and Evaluator for COL106 Assignment 4

## How to use?
* Clone the repository or download the zip.
* Replace the assignment4.java file with your own java file.
* Use command `make all` to compile all java files and then use `make run` to run them. (or use `make build`)
* Enter your name and wait for some time while the testcases are generated, run and compared with the model outputs.

### The Testcases
There are three types of testcases currently present.
* small - This is a Graph with 1000 vertices and almost 350,000 edges.
* large - This is a Graph with 100,000 vertices and around 170,000 edges.
* dfs - This is a Graph with around 7500 vertices and 1.1 million edges. This has around 25 independent storylines.

### FAQ
* **Outputs don't match:**
  I can in no way guarantee that my outputs are correct. You can also change the model outputs to your own outputs to compare it with others. Also there may be some differences in the outputs due to differences in the way the output is being printed by the program. You may change the model outputs for the same.
  
* **Program taking too much time to run:**
  The execution should take a few seconds at max. If the program takes too much time to execute, then the program may be stuck in a loop or the sorting function would not have been O(nlogn).

* **Driver giving StackOverflowError:**
  This is generally caused when the Driver is run with normal stack size as the test cases are very large and there would be a large number of recursive calls. To avoid this, use `make run`, `make build`, or `java -Xss16m Driver` instead of using `java Driver`. This would increase the stack size which should be sufficient to run the test cases.
  
### Constants
* `SEED`: This is the seed used in generating the pseudo random numbers. This ensures that the same input sequences are generated everytime. You may change the seed to produce a different input sequence.
* `IN_DIR`: This is the directory where the CSV files generated for the inputs are saved.
* `OUT_DIR`: This is the directory where the outputs are saved in txt files after execution.
* `CORR_DIR`: This is the directory where the model outputs are stored. If you wish to replace the model outputs, name them as "model_[type]_out.txt", where [type] is the type of testcase (small, large, or dfs).
* `cases`: This stores the types of testcases on which the program is evaluated. You may remove some testcases from this if you do not wish to evaluate the code on them.

**NOTE:** Matching outputs in no way guarantees that your program is correct.
