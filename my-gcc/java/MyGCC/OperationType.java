package MyGCC;

public enum OperationType{
  ADDQ("addl", "addq"),
  SUBQ("subl", "subq"),
  IMULQ("imull", "imulq"),
  IDIVQ("idivl", "idivq"),
  LESS("jl", "jl"),
  GREATER("jg", "jg"),
  EQUALS("je", "je"),
  LEQL("jle", "jle"),
  GEQL("jge", "jge"),
  DIFF("jne", "jne"),
  MOD("mod", "mod"),
  ORL("orl", "orq"),
  ANDQ("andl", "andq");
  
  public String name32;
  public String name64;

  private OperationType(String s32, String s64){
    this.name32 = s32;
    this.name64 = s64;
  }
  
  public String toString(){
		if(CodeGenerator.mode64)
			return this.name64;
		return this.name32;	
  }

}
