	.file "tests/singular_tests/test_somme_rec_multi_param.c"
	.section	.rodata
.LC0:
	.string	"%d"
	.text

.globl test_somme_rec_multi_param
	.type	test_somme_rec_multi_param, @function
test_somme_rec_multi_param:
.LFB0:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	movq	%rsp, %rbp
	.cfi_offset 6, -16
	.cfi_def_cfa_register 6
	pushq	%rbx
	subq	$184, %rsp
	movq	%rdi, -40(%rbp)
	movq	%rsi, -48(%rbp)
	movq	%rdx, -56(%rbp)
	movq	%rcx, -64(%rbp)
	movq	%r8, -72(%rbp)
	movq	%r9, -80(%rbp)
	movq	48(%rbp), %rax
	movq	%rax, -16(%rbp)
	movq	-40(%rbp), %rax
	cmpq	$0, %rax
	jne .L1.1
	movq	-48(%rbp), %rax
	cmpq	$0, %rax
	jne .L1.3
	movq	-56(%rbp), %rax
	cmpq	$0, %rax
	jne .L1.5
	movq	-64(%rbp), %rax
	cmpq	$0, %rax
	jne .L1.7
	movq	-72(%rbp), %rax
	cmpq	$0, %rax
	jne .L1.9
	movq	-80(%rbp), %rax
	cmpq	$0, %rax
	jne .L1.11
	movq	24(%rbp), %rax
	cmpq	$0, %rax
	jne .L1.13
	movq	-16(%rbp), %rax
	jmp .L0.1
.L1.13:
.L1.11:
.L1.9:
.L1.7:
.L1.5:
.L1.3:
.L1.1:
	movq	-40(%rbp), %rax
	cmpq	$0, %rax
	jle .L1.15
	movq	-40(%rbp), %rax
	subq	$1, %rax
	movq	%rax, -40(%rbp)
	movq	-16(%rbp), %rax
	addq	$1, %rax
	movq	%rax, -16(%rbp)
.L1.15:
	movq	-48(%rbp), %rax
	cmpq	$0, %rax
	jle .L1.17
	movq	-48(%rbp), %rax
	subq	$1, %rax
	movq	%rax, -48(%rbp)
	movq	-16(%rbp), %rax
	addq	$1, %rax
	movq	%rax, -16(%rbp)
.L1.17:
	movq	-56(%rbp), %rax
	cmpq	$0, %rax
	jle .L1.19
	movq	-56(%rbp), %rax
	subq	$1, %rax
	movq	%rax, -56(%rbp)
	movq	-16(%rbp), %rax
	addq	$1, %rax
	movq	%rax, -16(%rbp)
.L1.19:
	movq	-64(%rbp), %rax
	cmpq	$0, %rax
	jle .L1.21
	movq	-64(%rbp), %rax
	subq	$1, %rax
	movq	%rax, -64(%rbp)
	movq	-16(%rbp), %rax
	addq	$1, %rax
	movq	%rax, -16(%rbp)
.L1.21:
	movq	-72(%rbp), %rax
	cmpq	$0, %rax
	jle .L1.23
	movq	-72(%rbp), %rax
	subq	$1, %rax
	movq	%rax, -72(%rbp)
	movq	-16(%rbp), %rax
	addq	$1, %rax
	movq	%rax, -16(%rbp)
.L1.23:
	movq	-80(%rbp), %rax
	cmpq	$0, %rax
	jle .L1.25
	movq	-80(%rbp), %rax
	subq	$1, %rax
	movq	%rax, -80(%rbp)
	movq	-16(%rbp), %rax
	addq	$1, %rax
	movq	%rax, -16(%rbp)
.L1.25:
	movq	24(%rbp), %rax
	cmpq	$0, %rax
	jle .L1.27
	movq	24(%rbp), %rax
	subq	$1, %rax
	movq	%rax, 24(%rbp)
	movq	-16(%rbp), %rax
	addq	$1, %rax
	movq	%rax, -16(%rbp)
.L1.27:
	movq	-16(%rbp), %rax
	movq	%rax, 32(%rsp)
	movq	40(%rbp), %rax
	movq	%rax, 24(%rsp)
	movq	32(%rbp), %rax
	movq	%rax, 16(%rsp)
	movq	24(%rbp), %rax
	movq	%rax, 8(%rsp)
	movq	16(%rbp), %rax
	movq	%rax, 0(%rsp)
	movq	-80(%rbp), %rax
	movq	%rax, %r9
	movq	-72(%rbp), %rax
	movq	%rax, %r8
	movq	-64(%rbp), %rax
	movq	%rax, %rcx
	movq	-56(%rbp), %rax
	movq	%rax, %rdx
	movq	-48(%rbp), %rax
	movq	%rax, %rsi
	movq	-40(%rbp), %rax
	movq	%rax, %rdi
	call	test_somme_rec_multi_param
	movq	%rax, -8(%rbp)
	movq	-8(%rbp), %rax
	jmp .L0.1
.L0.1: 
	addq	$184, %rsp
	popq	%rbx
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size test_somme_rec_multi_param, .-test_somme_rec_multi_param

