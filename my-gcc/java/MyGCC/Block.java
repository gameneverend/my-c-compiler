package MyGCC;

import java.util.*;

public class Block {
  
  public Block parent = null;
  public LinkedList<Object> code;
  //public LinkedList<Block> blocks;
  public Context myContext;

  
  public Block() {
    this.code = new LinkedList<Object>();
    //this.blocks = new LinkedList<Block>();
    this.myContext = null;
  }
  
  public Block(LinkedList<Object> code) {
    this.code = code;
    this.myContext = null;
  }
  
  public Block(Block b, Context c) {
    this.code = new LinkedList<Object>(b.code);
    //this.blocks = new LinkedList<Block>();
    this.myContext = c;
  }
  
  public Block(Context c) {
    this.code = new LinkedList<Object>();
    //this.blocks = new LinkedList<Block>();
    this.myContext = c;
  }
  
  /**
   * This functions return the highest number of parameters contained in a
   * function call of the block.
   * The value returned is <b>-1</b> if there's no call to functions in this block
   */  
  public int maxParameters(){
    int n = -1;
    for (Object i : code){
      if(i instanceof Instruction)
        n = Math.max(((Instruction) i).maxParameters(), n);
      else
        n = Math.max(((Block) i).maxParameters(), n);
    }
    return n;
  }
  
  public void pushInstruction(Instruction e) {
    if(this.code != null)
      if(e != null) {
        this.code.add(e);
        System.out.println(code.size());
      } else
        System.err.println("The specified instruction is null");
    else
      System.err.println("this.instructions: is null");
  }
  
  public void pushInstruction(LogicalBlock e) {
    if(this.code != null)
      if(e != null) {
        this.code.add(e);
        System.out.println(code.size());
      } else
        System.err.println("The specified instruction is null");
    else
      System.err.println("this.instructions: is null");
  }
  
  public void pushInstruction(LogicalIfElse e) {
    if(this.code != null)
      if(e != null) {
        this.code.add(e);
        System.out.println(code.size());
      } else
        System.err.println("The specified instruction is null");
    else
      System.err.println("this.instructions: is null");
  }
  
  public void pushBlock(Block bl) {
    if(this.code != null)
      if(bl != null)
        this.code.add(bl);
      else
        System.err.println("The specified block is null");
    else
      System.err.println("this.blocks: is null");
  }

  public LinkedList<Object> getInstructions() {
    return this.code;
  }
    
  public String toString(){
    StringBuffer sb = new StringBuffer();
    for(Object i : code){
      try{
        if(i instanceof Body) {
          sb.append(i.toString());
        } else if(i instanceof LogicalIfElse) {
          sb.append(LogicalIfElse.instructionToAssembly((LogicalIfElse)i, myContext));
        } else if(i instanceof LogicalBlock) {
          sb.append(LogicalBlock.instructionToAssembly((LogicalBlock)i, myContext));
        } else if(i instanceof Instruction) {
          sb.append(Instruction.instructionToAssembly((Instruction)i, myContext));
        }
      }catch(Exception e){e.printStackTrace();}
    }
    
    return sb.toString();
  }
}
