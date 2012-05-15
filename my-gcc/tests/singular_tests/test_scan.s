	.file "tests/singular_tests/test_scan.c"
	.section	.rodata
.LC0:
	.string	"%d"
	.text

.globl test_scan
	.type	test_scan, @function
test_scan:
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
	movq	$0, -8(%rbp)
	movq	$.LC0, %rax
	leaq	-8(%rbp), %rdx
	movq	%rdx, %rsi
	movq	%rax, %rdi
	movq	$0, %rax
	call	__isoc99_scanf
	movq	-8(%rbp), %rax
	addq	$42, %rax
	addq	$88, %rsp
	popq	%rbx
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size test_scan, .-test_scan

