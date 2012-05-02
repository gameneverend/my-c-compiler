package MyGCC;

import java.util.ArrayList;
import java.util.HashSet;
import java.lang.String;

public enum OperationType{
  ADD("addl", "addq"),
  SUB("subl", "subq"),
  IMUL("imull", "imulq"),
  IDIV("idivl", "idivq"),
  
  /* In order to maintain the same order of code as the input
   * we invert all the tests.
   */
  LESS("jge", "jge"),
  GREATER("jle", "jle"),
  //EQUALS("jne", "jne"),
  EQUALS("cmpl", "cmpq"),
  LEQL("jg", "jg"),
  GEQL("jl", "jl"),
  DIFF("je", "je"),
  MOD("mod", "mod"),
  OR("orl", "orq"),
  AND("andl", "andq");
  
  public String name32;
  public String name64;
  private static ArrayList<OperationType> ops = new ArrayList<OperationType>();
  
  static{
    ops.add(ADD);
    ops.add(SUB);
    ops.add(IMUL);
    ops.add(IDIV);
    ops.add(MOD);
    
    ops.add(LESS); 
    ops.add(GREATER);
    ops.add(LEQL); 
    ops.add(GEQL);
    
    ops.add(OR);
    ops.add(AND);   
  }

  private OperationType(String s32, String s64){
    this.name32 = s32;
    this.name64 = s64;
  }
  
  public static OperationType getOp(String s){
		for(OperationType op : ops){
			if(op.name32.equals(s) || op.name64.equals(s))
				return op;
		}
		return null;
	}
  
  public String toString(){
		if(CodeGenerator.mode64)
			return this.name64;
		return this.name32;	
  }

}
