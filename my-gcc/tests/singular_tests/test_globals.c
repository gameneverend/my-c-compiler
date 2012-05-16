int modify_it(void);
int test_globals(int);

int var;

int modify_it(void) {
  var = 15;
  return 4;
}

int test_globals(int a) {
  int b;
  var = a;
  b = modify_it();
  var = (var / 3) * 4;
  return var;
}
