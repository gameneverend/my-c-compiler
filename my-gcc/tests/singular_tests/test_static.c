int test_static(int);

int test_static(int a) {
	static int b;
	b = b + a;
	return b;
}
