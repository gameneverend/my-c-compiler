package MyGCC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class Function{
  
  private Type returnType;
  public String name;
  public Body body;
  private FunctionContext myContext;

  public Function(String name,
                  Type returnType,
                  ArrayList<Parameter> parameters){
    this.name = name;
    this.returnType = returnType;
    this.myContext = new FunctionContext(null);
    myContext.setParameters(parameters);
    body = new Body(myContext);
  }
  
  

  public String toString(){
    int i = LabelManager.getFunctionNumber();
    StringBuffer sb = new StringBuffer();
    sb.append(".globl "); sb.append(name); sb.append('\n');
    sb.append("\t.type\t"); sb.append(name); sb.append(", @function\n");
    sb.append(name); sb.append(":\n");
    sb.append('.'); sb.append(LabelManager.getBeginLabel(i)); sb.append(":\n");
    sb.append(prelude());
    sb.append(loadParameters());
    sb.append(body.toString());
    sb.append(epilogue());
    sb.append('.'); sb.append(LabelManager.getEndLabel(i)); sb.append(":\n");
    sb.append("\t.size "); sb.append(name); sb.append(", .-"); sb.append(name); sb.append('\n');
    return sb.toString();
  }
  
  /** Initially, some parameters are left in some caller-saved Registers.
   *  This function returns the String that ensures that the parameters are
   *  loaded before starting the code **/
  private String loadParameters(){
    System.out.println("Loading parameters for function: " + this.name);
    myContext.prepareParametersLocation();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < myContext.nbParameters() && i < 6; i++){
      sb.append("\t" + Assembly.MOV + "\t");
      sb.append(Register.getArgumentRegister(i));
      sb.append(", ");
      try{
      sb.append(myContext.getVariableLocation(myContext.getParameter(i).name));
      System.out.println("\tParameter : " + myContext.getParameter(i).name);
      System.out.println("\tLocation : " + myContext.getVariableLocation(myContext.getParameter(i).name));
      }catch(Exception e){e.printStackTrace();}
      sb.append("\n");
    }
    return sb.toString();
  }
  
  //TODO this code is written, but not fully understood by it's writer, more work should be done on it
  private String prelude(){
    StringBuilder sb = new StringBuilder();
    sb.append("\t.cfi_startproc\n");
    sb.append("\t" + Assembly.PUSH + "\t" + Register.RBP + "\n");
    sb.append("\t.cfi_def_cfa_offset 16\n");
    sb.append("\t" + Assembly.MOV + "\t" + Register.RSP + ", " + Register.RBP + "\n");
    sb.append("\t.cfi_offset 6, -16\n");
    sb.append("\t.cfi_def_cfa_register 6\n");
    // If there's function calls in this function rsp should be modified
    if (body.maxParameters() >= 0){
      sb.append("\t" + OperationType.SUBQ + "\t$"+ 32 +", " + Register.RSP + "\n");
    }
    return sb.toString();
  }
  
  //TODO this code is written, but not fully understood by it's writer, more work should be done on it
  private String epilogue(){
    StringBuilder sb = new StringBuilder();
    sb.append("\tleave\n");
    sb.append("\t.cfi_def_cfa 7, 8\n");
    sb.append("\tret\n");
    sb.append("\t.cfi_endproc\n");
    return sb.toString();    
  }



  public void addDeclaration(Type type, String identifier, int arraySize) {
	  myContext.addVariable(type, identifier, arraySize);
  }
    
}
