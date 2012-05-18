//#include "testinclude.h"

int addition(int, int);
int falsemain(void);
int somme(int);
int oppose(int);
int appel_imbrique(int, int, int);
int getIndexOne(int[3], int);




/*int addition(int a, int b){
  int c;
  c = a + b;
  return c;
}*/

/*int somme(int x){
  int c;
  c = x + 1;
  return c;
}*/

/*int oppose(int a){
	a = 0 - a;
	return a;
}*/

/*int appel_imbrique(int a, int b, int c){
	return addition(a,b) + addition(b,c);
}*/

int getIndexOne(int tab[], int i){
  tab[1] = tab[i] + 10;
  return tab[1];
}

int falsemain(void){
  int a;
  int t[3];
  a = 2;
  t[0] = 0;
  t[1] = 1;
  t[2] = 2;

  return getIndexOne(t, 2);
}






