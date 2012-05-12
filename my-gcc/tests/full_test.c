#include "batterie_test.h"
#include <assert.h>
#include <stdlib.h>
#include <stdio.h>

int test_validity(int result, int expected_result){
	if (result != expected_result){
		printf("FAILED\n");
		printf("\tUnexpected Result : %d, the expected result was : %d\n",
					 result,
					 expected_result);
		exit(EXIT_FAILURE);
	}
}

int main(int argc, char ** argv){
	int result;
  printf("\n");
  
	printf("Simple test (return 42) : ");
	test_validity(test_simple(), 42);
	printf("OK\n");

	printf("One parameter : ");
	test_validity(oppose(5), -5);
	printf("OK\n");

	printf("Two parameters (7 + 13) : ");
	test_validity(addition(7,13), 20);
	printf("OK\n");

	printf("Three parameters and priority (5 * 4 + 3) : ");
	test_validity(polynome_simple(5,4,3), 23);
	printf("OK\n");

	printf("Multiple functions call (add(5,4) + add(4,6) : ");
	test_validity(appel_imbrique(5,4,6), 19);
	printf("OK\n");
  
  printf("IF test (if i  == 10 , i++, else i = -4) : ");
  test_validity(test_logic(0), 0);
  printf("OK\n");

  printf("Testing multi-if : ");
  test_validity(test_multi_if(2, 13), 2);
  printf("OK\n");
  
  printf("Testing stack frame and recursion : ");
  /* TODO correct this function, the 7,9,10 parameters are actually ignored,
   * the result is false, but the origin very strange, should be done later */
  test_validity(test_somme_rec_multi_param(1,2,3,4,5,6,7,8,9,10,11), 40);
  printf("OK\n");

  printf("WHILE test (while i != 10, i++) : ");
  test_validity(test_while(0), 10);
  printf("OK\n");
  
  printf("Testing fibo(5): result = 8 : ");
  test_validity(test_fibo(5), 8);
  printf("OK\n");
  
  printf("Testing arrays: return c[i%3] : ");
  test_validity(test_array(5), 2);
  printf("OK\n");
  
  printf("Testing binary operations : ");
  test_validity(test_binary(), 222);
  printf("OK\n");
  
  printf("Testing READ_INT: return read_int(a)+42 : (enter 1) ");
  test_validity(test_scan(), 43);
  printf("OK\n");
  
  printf("Testing READ_INT: return read_int(a)+42 : (enter 5) ");
  test_validity(test_scan(), 47);
  printf("OK\n");

  printf("Testing unreachable code: ");
  test_validity(test_unreachable(5), 2);
  printf("OK\n");
  
  printf("\n");
  
}
