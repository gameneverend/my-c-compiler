#!/bin/sh
ant clean
ant

OUR_COMPILER="java -jar jar/Compiler.jar"

TEST="tests/testIf"


$OUR_COMPILER $TEST.c

gcc $TEST.s tests/executor.c -o test

./test
