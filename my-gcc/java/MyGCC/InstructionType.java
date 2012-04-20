package MyGCC;

public enum InstructionType{
  RETURN("ret"),
  EXIT("exit"),
  EQL("movq"),
  IF(""),
  WHILE("");
  
  public String name;

  private InstructionType(String s){
    this.name = s;
  }
  
  public String toString(){
    return this.name;
  }
}
