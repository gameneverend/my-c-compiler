#include "batterie_test.h"
#include <assert.h>

int main(int argc, char ** argv){
	assert(test_simple() == 42);
	assert(oppose(5) == -5);
	assert(addition(7,13) == 20);
	assert(polynome_simple(5,4,3) == 23);
	assert(appel_imbrique(5,4,6) == 19);
}
