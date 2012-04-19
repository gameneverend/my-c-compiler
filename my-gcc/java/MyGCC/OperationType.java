package MyGCC;

public enum OperationType{
  ADDQ("addq"),
  SUBQ("subq"),
  IMULQ("imulq"),
  IDIVQ("idivq"),
  LESS("jl"),
  GREATER("jg"),
  EQUALS("je"),
  LEQL("jle"),
  GEQL("jge"),
  DIFF("jne"),
  MOD("mod"),
  ORL("orl"),
  ANDQ("andq");
  
  public String name;

  private OperationType(String s){
    this.name = s;
  }
  
  public String toString(){
    return this.name;
  }

}
