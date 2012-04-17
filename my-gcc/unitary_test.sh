#!/bin/sh
ant clean
ant

OUR_COMPILER="java -jar jar/Compiler.jar"

TEST="tests/test3"


$OUR_COMPILER $TEST.c

gcc $TEST.s tests/executor.c -o test

./test
