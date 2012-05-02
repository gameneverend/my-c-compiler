#include "batterie_test.h"
#include <assert.h>
#include <stdlib.h>

int test_validity(int result, int expected_result){
	if (result != expected_result){
		printf("FAILED\n");
		printf("\tRésultat inattendu : %d, le résultat attendu était : %d\n",
					 result,
					 expected_result);
		exit(EXIT_FAILURE);
	}
}

int main(int argc, char ** argv){
	int result;
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
  
  printf("testing if (if i  == 10 , i++, else i = -4) : ");
  test_validity(test_logic(1), -4);
  printf("OK\n");
  
  printf("testing while (while i != 10, i++) : ");
  test_validity(test_while(0), 10);
  printf("OK\n");
}
