	.file "tests/singular_tests/test_multi_if.c"
	.section	.rodata
.LC0:
	.string	"%d"
	.text

.globl test_multi_if
	.type	test_multi_if, @function
test_multi_if:
.LFB0:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	movq	%rsp, %rbp
	.cfi_offset 6, -16
	.cfi_def_cfa_register 6
	movq	%rdi, -24(%rbp)
	movq	%rsi, -32(%rbp)
	movq	$1, -8(%rbp)
	movq	-24(%rbp), %rax
	cmpq	$10, %rax
	jge .L1.1
	movq	-32(%rbp), %rax
	cmpq	$10, %rax
	jge .L1.3
	movq	$42, %rax
	jmp .L0.1
.L1.3:
	movq	$2, %rax
	jmp .L0.1
	jmp .L1.2
.L1.1:
	movq	$0, %rax
	jmp .L0.1
.L1.2:
	movq	$3, -8(%rbp)
.L0.1: 
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size test_multi_if, .-test_multi_if

