	.file "tests/singular_tests/test_appel_imbrique.c"
	.section	.rodata
.LC0:
	.string	"%d"
	.text

.globl addition
	.type	addition, @function
addition:
.LFB0:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	movq	%rsp, %rbp
	.cfi_offset 6, -16
	.cfi_def_cfa_register 6
	movq	%rdi, -24(%rbp)
	movq	%rsi, -32(%rbp)
	movq	-24(%rbp), %rax
	movq	-32(%rbp), %rdx
	addq	%rdx, %rax
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size addition, .-addition


.globl test_appel_imbrique
	.type	test_appel_imbrique, @function
test_appel_imbrique:
.LFB1:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	movq	%rsp, %rbp
	.cfi_offset 6, -16
	.cfi_def_cfa_register 6
	pushq	%rbx
	subq	$96, %rsp
	movq	%rdi, -24(%rbp)
	movq	%rsi, -32(%rbp)
	movq	%rdx, -40(%rbp)
	movq	-32(%rbp), %rax
	movq	%rax, %rsi
	movq	-24(%rbp), %rax
	movq	%rax, %rdi
	call	addition
	movq	%rax, -48(%rbp)
	movq	-40(%rbp), %rax
	movq	%rax, %rsi
	movq	-32(%rbp), %rax
	movq	%rax, %rdi
	call	addition
	movq	-48(%rbp), %rdx
	addq	%rax, %rdx
	movq	%rdx, %rax
	movq	%rax, %rdx
	addq	$96, %rsp
	popq	%rbx
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE1:
	.size test_appel_imbrique, .-test_appel_imbrique

