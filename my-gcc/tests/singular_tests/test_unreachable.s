	.file "tests/singular_tests/test_unreachable.c"
	.section	.rodata
.LC0:
	.string	"%d"
	.text

.globl test_unreachable
	.type	test_unreachable, @function
test_unreachable:
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
	cmpq	$1, %rax
	jl 	.L1.2
	movq	-24(%rbp), %rax
	cmpq	$0, %rax
	jne .L1.3
	movq	$0, %rax
	jmp .L0.1
.L1.3:
	movq	-24(%rbp), %rax
	subq	$1, %rax
	movq	%rax, -24(%rbp)
	jmp .L1.1
.L1.2:
	movq	$2, %rax
	jmp .L0.1
.L0.1: 
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size test_unreachable, .-test_unreachable

