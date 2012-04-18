package MyGCC;

public enum OperationType{
  ADDL("addq"),
  SUBL("subq"),
  IMULL("imulq"),
  IDIVL("idivq"),
  LESS("jl"),
  GREATER("jg"),
  EQUALS("je"),
  LEQL("jle"),
  GEQL("jge"),
  DIFF("jne"),
  MOD("mod"),
  OR("orl"),
  AND("andq");
  
  public String name;

  private OperationType(String s){
    this.name = s;
  }
  
  public String toString(){
    return this.name;
  }

}
