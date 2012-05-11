//#include "testinclude.h"

int addition(int, int);
int falsemain(void);
int somme(int);
int oppose(int);
int appel_imbrique(int, int, int);




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

int falsemain(void){
  int a;
  int b;
  int c;
  a = 1;
  b = 2;
  
  c = a * 3;
  c = a * @1;
  c = a * ~1;
  c = a * ~1!;
  c = b + a;
  return c;
}

/*int falsemain(int a,
							   int b,
							   int c,
							   int s){
	int somme;
	somme = s;
	if (a == 0){
		if (b == 0){
			if (c == 0){
				return somme;
			};
		};
	};
	if (a > 0){
		a = a -1;
		somme = somme + 1;
	};
	if (b > 0){
		b = b -1;
		somme = somme + 1;
	};
	if (c > 0){
		c = c -1;
		somme = somme + 1;
	};
	
	return falsemain(a,b,c,somme);
}
*/






