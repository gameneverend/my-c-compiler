#!/bin/sh
ant clean
ant

OUR_COMPILER="java -jar jar/Compiler.jar"

TEST="tests/batterie_test"
#TEST="tests/test3"


$OUR_COMPILER $TEST.c

gcc -g $TEST.s tests/full_test.c -o test
#gcc -g $TEST.s tests/executor.c -o test

./test

