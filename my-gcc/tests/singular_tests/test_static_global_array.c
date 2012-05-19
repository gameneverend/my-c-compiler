int test_sga_aux(int);
int test_static_global_array(int);

static int a[10];

int test_sga_aux(int q) {
	a[5] = a[5] + q;
	return a;
}

int test_static_global_array(int b) {
	a[5] = b;
	a[6] = test_sga_aux(8);
	return a[6];
}
