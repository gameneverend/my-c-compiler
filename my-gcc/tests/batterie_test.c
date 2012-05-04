/* This test returns 42, test of function with no parameters */
int test_simple(void);

/* This function returns -x */
int oppose(int);

/* This function returns a + b */
int addition(int, int);

/* This function returns a * b + c */
int polynome_simple(int, int, int);

/* This function tests nested function calls,
	 it returns (a + 2) + (b + c) */
int appel_imbrique(int, int, int);

/* This function tests logic blocks */
int test_logic(int);

/* This function tests loops */
int test_while(int);

/* This function tests recursion */
int test_fibo(int);

/* This function tests array access */
int test_array(int);


/***** Implémentation *****/
int test_simple(void){
	int a;
	int b;
	a = 7;
	b = 6;
	return a * b; 
}

int oppose(int x){
	return -x;
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

int test_logic(int i) {
  if(i == 10)
    i = i + 1;
  ;
  return i;
}

int test_while(int i) {
  while(i != 10)
    i = i + 1;
  ;
  return i;
}

int test_fibo(int i){
  if(i == 0)
    return 1;
  ;
  
  if(i == 1)
    return 1;
  ;
  
  return test_fibo(i-1) + test_fibo(i-2);
}

int test_array(int i){
  int c[3];
  c[0] = 0;
  c[1] = 1;
  c[2] = 2;
  
  return c[i%3];
}

int test_scan(int i){
  int a;
  read_int(a);
  return a + 42;
}
