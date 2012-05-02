#ifndef BATTERIE_TEST
#define BATTERIE_TEST
/* Ce test est sensé renvoyé 42, test sans paramètre à la fonction */
extern int test_simple();

/* Cette fonction est sensée renvoyer -x */
extern int oppose(int x);

/* Cette fonction est sensée renvoyer a + b */
extern int addition(int a,int b);

/* Cette fonction est sensée renvoyée a * b + c */
extern int polynome_simple(int a ,int b,int c);

/* Cette fonction test un appel de fonction imbriqué,
	 le résultat doit être a + 2 * b + c */
extern int appel_imbrique(int a, int b, int c);

/* Cette fonction teste les blocs logiques */
extern int test_logic(int);

/* Cette fonction teste le fonctionnement des boucles */
extern int test_while(int);

/* Cette fonction teste la récursivité */
extern int test_fibo(int);

#endif
