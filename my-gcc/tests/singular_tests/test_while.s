	.file "tests/singular_tests/test_while.c"
	.section	.rodata
.LC0:
	.string	"%d"
	.text

.globl test_while
	.type	test_while, @function
test_while:
.LFB0:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	movq	%rsp, %rbp
	.cfi_offset 6, -16
	.cfi_def_cfa_register 6
	movq	%rdi, -24(%rbp)
.L1.1:
	movq	-24(%rbp), %rax
	cmpq	$10, %rax
	je 	.L1.2
	movq	-24(%rbp), %rax
	addq	$1, %rax
	movq	%rax, -24(%rbp)
	jmp .L1.1
.L1.2:
	movq	-24(%rbp), %rax
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size test_while, .-test_while

