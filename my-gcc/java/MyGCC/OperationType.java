package MyGCC;

import java.util.ArrayList;
import java.util.HashSet;
import java.lang.String;

public enum OperationType{
  ADD("addl", "addq"),
  SUB("subl", "subq"),
  IMUL("imull", "imulq"),
  IDIV("idivl", "idivq"),
  LESS("jl", "jl"),
  GREATER("jg", "jg"),
  EQUALS("je", "je"),
  LEQL("jle", "jle"),
  GEQL("jge", "jge"),
  DIFF("jne", "jne"),
  MOD("mod", "mod"),
  OR("orl", "orq"),
  AND("andl", "andq");
  
  public String name32;
  public String name64;
  private static ArrayList<OperationType> ops = new ArrayList<OperationType>();
  
  static {
    ops.add(ADD);
    ops.add(SUB);
    ops.add(IMUL);
    ops.add(IDIV);
    ops.add(MOD);
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
  
  public String getName(){
		if(CodeGenerator.mode64)
			return this.name64;
		return this.name32;
	}
  
  public String toString(){
		if(CodeGenerator.mode64)
			return this.name64;
		return this.name32;	
  }

}
