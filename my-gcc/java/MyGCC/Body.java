package MyGCC;

import java.util.*;

public class Body {
  
  public LinkedList<Declaration> declarations;
  public Block mainBlock;
  private Context myContext;
  
  public Body(Context c) {
    this.declarations = new LinkedList<Declaration>();
    this.mainBlock = new Block();
    this.myContext = c;
  }
  
  public Body(LinkedList<Declaration> dec, Block b, Context c) {
    this.declarations = dec;
    this.mainBlock = b;
    this.myContext = c;
  }
  
  /**
   * This functions return the highest number of parameters contained in a
   * function call of the body.
   * The value returned is <b>-1</b> if there's no call to functions in this body
   */  
  public int maxParameters(){
    return mainBlock.maxParameters();
  }
  public void pushDeclaration(Declaration d) {
    if(this.declarations != null)
      if(d != null)
        this.declarations.add(d);
      else
        System.err.println("The specified declaration is null");
    else
      System.err.println("this.declarations: is null");
  }
  
  public void pushInstructionToBlock(Instruction i) {
    if(this.mainBlock != null)
      if(i != null){
        this.mainBlock.instructions.add(i);
        //System.out.println("instruction added to block");
      }
      else
        System.err.println("The specified instruction is null");
    else
      System.err.println("this.mainBlock: is null");
  }
  
  /*public void addBlock(){
  }*/
  
  public LinkedList<Declaration> getDeclarations() {
    return this.declarations;
  }

  public Block getMainBlock() {
    return this.mainBlock;
  }

  public String toString(){
    StringBuffer sb = new StringBuffer();
    Instruction i;
    Iterator<Instruction> iter = mainBlock.instructions.iterator();
    
    while(iter.hasNext()){
      i = iter.next();
      try{
      sb.append(Instruction.instructionToAssembly(i, myContext));
      }catch(Exception e){e.printStackTrace();}
    }
    
    return sb.toString();
  }
}
