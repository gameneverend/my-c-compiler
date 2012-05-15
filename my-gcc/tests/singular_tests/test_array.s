	.file "tests/singular_tests/test_array.c"
	.section	.rodata
.LC0:
	.string	"%d"
	.text

.globl test_array
	.type	test_array, @function
test_array:
.LFB0:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	movq	%rsp, %rbp
	.cfi_offset 6, -16
	.cfi_def_cfa_register 6
	movq	%rdi, -40(%rbp)
	movq	$0, %rax
	movq	$0, -24(%rbp, %rax, 8)
	movq	$1, %rax
	movq	$1, -24(%rbp, %rax, 8)
	movq	$2, %rax
	movq	$2, -24(%rbp, %rax, 8)
	movq	-40(%rbp), %rax
	movq	$3, %rbx
	cqto	
	idivq	%rbx
	movq	%rdx, %rax
	movq	-24(%rbp, %rax, 8), %rax
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size test_array, .-test_array

