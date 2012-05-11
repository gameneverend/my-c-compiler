#ifndef BATTERIE_TEST
#define BATTERIE_TEST
/* This test returns 42, test of function with no parameters */
extern int test_simple();

/* This function returns -x */
extern int oppose(int x);

/* This function returns a + b */
extern int addition(int a,int b);

/* This function returns a * b + c */
extern int polynome_simple(int a ,int b,int c);

/* This function tests nested function calls,
	 it returns (a + 2) + (b + c) */
extern int appel_imbrique(int a, int b, int c);

/* This function tests logic blocks */
extern int test_logic(int);

/* This function tests nested IF conditions */
extern int test_multi_if(int, int);

/* This function tests loops */
extern int test_while(int);

/* This function tests recursion */
extern int test_fibo(int);

/* This function tests array access */
extern int test_array(int);

/* This function tests binary operations */
extern int test_binary(void);

/* This function tests the handling of unreachable code */
extern int test_unreachable(int);

#endif
