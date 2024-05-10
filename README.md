# Process to run project:

*CDCLSolver\\runnable\\CDCLSolver.jar* is the file to run the program.  
The code is intended to run on a Windows machine, it has not been tested on any other operating system.  
In order to see output, the code must be run in a command prompt.

**To run, follow these steps:**
1) Open your CMD in ```.\CDCLSolver\ ```, and run the following command: ```jar cmf .\META-INF\MANIFEST.MF runnable\CDCLSolver.jar Solver\*.class Reader\*.class```
2) To run, use the following command ```java -jar runnable\CDCLSolver.jar {filename}```
   1) The file specified by the filename argument must be present in either
      *```CDCLSolver\runnable\inputs\sat```* or
      *```CDCLSolver\runnable\inputs\sat```*
   2) Examples of accepted file names are: "block0", "block0.cnf", and "add4.cnf"
