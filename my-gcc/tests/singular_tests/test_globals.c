int modify_it(void);
int test_globals(int);

int var;
int poo;
int hello;

int modify_it(void) {
  var = 15;
  return 4;
}

int test_globals(int a) {
  int c;
	int b;
  var = a;
  hello = 5;
	c = 4;
  b = modify_it();
  var = (var / 3) * 4;
  return var;
}
