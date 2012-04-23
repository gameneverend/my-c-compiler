#!/bin/sh
ant clean
ant

OUR_COMPILER="java -jar jar/Compiler.jar"

TEST="tests/batterie_test"


$OUR_COMPILER $TEST.c

gcc -g $TEST.s tests/full_test.c -o test

./test

