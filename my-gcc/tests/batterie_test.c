/* Ce test est sensé renvoyé 42, test sans paramètre à la fonction */
int test_simple(void);

/* Cette fonction est sensée renvoyer -x */
int oppose(int);

/* Cette fonction est sensée renvoyer a + b */
int addition(int, int);

/* Cette fonction est sensée renvoyée a * b + c */
int polynome_simple(int, int, int);

/* Cette fonction test un appel de fonction imbriqué,
	 le résultat doit être a + 2 * b + c */
int appel_imbrique(int, int, int);

/* Cette fonction teste les blocs logiques */
int test_while(void);

/* Cette fonction teste la récursivité */
// TODO

/* Cette fonction teste le fonctionnement des boucles */
// TODO


/***** Implémentation *****/
int test_simple(void){
	int a;
	int b;
	a = 7;
	b = 6;
	return a * b; 
}

int oppose(int x){
	x = 0 - x;
	return x;
}

int addition(int a, int b){
	return a + b;
}

int polynome_simple(int a, int b, int c){
	return a * b + c;
}

int appel_imbrique(int a, int b, int c){
	return addition(a,b) + addition(b,c);
}

void test_while(void) {
  int i;
  i = 0;
  while(i < 10)
    i++;
  return i;
}
