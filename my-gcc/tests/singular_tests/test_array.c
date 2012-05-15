int test_array(int);

int test_array(int i){
  int c[3];
  c[0] = 0;
  c[1] = 1;
  c[2] = 2;
  
  return c[i%3];
}
