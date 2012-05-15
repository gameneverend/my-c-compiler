	.file "tests/singular_tests/test_binary.c"
	.section	.rodata
.LC0:
	.string	"%d"
	.text

.globl test_binary
	.type	test_binary, @function
test_binary:
.LFB0:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	movq	%rsp, %rbp
	.cfi_offset 6, -16
	.cfi_def_cfa_register 6
	movq	$1, -16(%rbp)
	movq	$0, -8(%rbp)
	movq	-16(%rbp), %rax
	cmpq	$0, %rax
	jne .L1.1
	movq	$111, %rax
	jmp .L0.1
.L1.1:
	movq	-16(%rbp), %rax
	movq	-8(%rbp), %rdx
	andq	%rdx, %rax
	testq	%rax, %rax
	jne .L1.3
	movq	$222, %rax
	jmp .L0.1
.L1.3:
	movq	$333, %rax
	jmp .L0.1
.L0.1: 
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size test_binary, .-test_binary

