int staticAux(int);
int test_static_arrays(int);

int staticAux(int a) {
	int c;
	static int b[10];
	c = 0;
	b[c + 4] = b[c + 4] + a;
	return b[c + 4];
}

int test_static_arrays(int d) {
	int v;
	v = staticAux(d);
	v = staticAux(d);
	return v;
}
