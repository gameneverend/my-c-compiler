	.file "tests/singular_tests/test_oppose.c"
	.section	.rodata
.LC0:
	.string	"%d"
	.text

.globl test_oppose
	.type	test_oppose, @function
test_oppose:
.LFB0:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	movq	%rsp, %rbp
	.cfi_offset 6, -16
	.cfi_def_cfa_register 6
	movq	%rdi, -24(%rbp)
	movq	-24(%rbp), %rax
	negq	%rax
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size test_oppose, .-test_oppose

