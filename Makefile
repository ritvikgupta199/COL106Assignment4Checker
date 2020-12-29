run:
	java -Xss16m Driver
all:
	javac *.java
clean:
	rm *.class
build:
	javac *.java
	java -Xss16m Driver