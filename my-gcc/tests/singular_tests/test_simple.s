	.file "tests/singular_tests/test_simple.c"
	.section	.rodata
.LC0:
	.string	"%d"
	.text

.globl test_simple
	.type	test_simple, @function
test_simple:
.LFB0:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	movq	%rsp, %rbp
	.cfi_offset 6, -16
	.cfi_def_cfa_register 6
	movq	$7, -16(%rbp)
	movq	$6, -8(%rbp)
	movq	-16(%rbp), %rax
	movq	-8(%rbp), %rdx
	imulq	%rdx, %rax
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size test_simple, .-test_simple

