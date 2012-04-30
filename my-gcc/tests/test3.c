//#include "testinclude.h"

int addition(int, int);
int falsemain(int);
int somme(int);
int oppose(int);
int appel_imbrique(int, int, int);




int addition(int a, int b){
  int c;
  c = a + b;
  return c;
}

/*int somme(int x){
  int c;
  c = x + 1;
  return c;
}*/

int oppose(int a){
	a = 0 - a;
	return a;
}

/*int appel_imbrique(int a, int b, int c){
	return addition(a,b) + addition(b,c);
}*/

int falsemain(int z){
  int c;
  int d;
  int e;
  
  //c = substraction(e, 1) + 2;
  //c = 11 + d - 7;
  //c = 5;
  //d = 8 % (5 + 1) - 1;
  //d = -10;
  

  e = addition(-oppose(6) + 2, -1);
  //e = appel_imbrique(5,4,6);
  //e = 3 * d % c;
  //e = 6 / d;
  //e = 2 * (d + c * 6);
  //e = addition(d - 2, 1);
  //e = somme(4) + 2 * d;
  //e = somme(6) - somme(d);
  //e = addition(d + 7 + d * d, 2) - 2 * d;
  //e = somme(3) / somme(1) - d;
  
  //exit(42);  
  //return d + 4;
  //return;
	return e;
}







