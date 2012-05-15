	.file "tests/singular_tests/test_fibo.c"
	.section	.rodata
.LC0:
	.string	"%d"
	.text

.globl test_fibo
	.type	test_fibo, @function
test_fibo:
.LFB0:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	movq	%rsp, %rbp
	.cfi_offset 6, -16
	.cfi_def_cfa_register 6
	pushq	%rbx
	subq	$88, %rsp
	movq	%rdi, -24(%rbp)
	movq	-24(%rbp), %rax
	cmpq	$0, %rax
	jne .L1.1
	movq	$1, %rax
	jmp .L0.1
.L1.1:
	movq	-24(%rbp), %rax
	cmpq	$1, %rax
	jne .L1.3
	movq	$1, %rax
	jmp .L0.1
.L1.3:
	movq	-24(%rbp), %rax
	subq	$1, %rax
	movq	%rax, %rdi
	call	test_fibo
	movq	%rax, -32(%rbp)
	movq	-24(%rbp), %rax
	subq	$2, %rax
	movq	%rax, %rdi
	call	test_fibo
	movq	-32(%rbp), %rdx
	addq	%rax, %rdx
	movq	%rdx, %rax
	movq	%rax, %rdx
	jmp .L0.1
.L0.1: 
	addq	$88, %rsp
	popq	%rbx
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size test_fibo, .-test_fibo

