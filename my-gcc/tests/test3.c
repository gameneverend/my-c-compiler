int falsemain(void);
int total_sum(int[3]);


int total_sum(int tab[3]){
  int sum;
  int index;
  sum = 0;
  index = 0;
  while(index < 3){
		sum = sum + tab[index];
		index = index + 1;
	};
  return sum;
}

int falsemain(void){
  int a;
  int t[3];
  a = 2;
  t[0] = 8;
  t[1] = 9;
  t[2] = 10;

  return total_sum(t);
}






