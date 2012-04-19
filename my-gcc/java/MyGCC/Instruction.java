package MyGCC;

public class Instruction {
  
  public Expression lexpr;
  public Expression rexpr;
  public InstructionType instrType;
  public String assemblyStr;
  public Context myContext = null;
  
  public Instruction(Expression r, InstructionType op){
    this.lexpr = null;
    this.rexpr = r;
    this.instrType = op;
    this.assemblyStr = null;
  }
  
  public Instruction(Expression l, Expression r, InstructionType op){
    this.lexpr = l;
    this.rexpr = r;
    this.instrType = op;
    this.assemblyStr = null;
  }
  
  
  public static String instructionToAssembly(Instruction instruct, Context context) throws Exception{
		StringBuffer sb = new StringBuffer();
		Variable a = (Variable)instruct.lexpr;
    if(instruct != null){
      if(instruct.rexpr == null){
        // do nothing: already handled in epilogue()
        return sb.toString();
      }
      
      switch(instruct.instrType){
        
        case RETURN:
					sb.append(instruct.rexpr.handleExpression(a, context));
					sb.append("\tmovq\t " + sb.substring(sb.lastIndexOf(",") + 2).replace("\n","") + ", %rax\n");
          break;
          
        case EXIT:
          //TODO
          //movq argument to stack
          //call exit
          //do not generate ret/leave.
          break;
          
        case EQL:
          
          if(instruct.rexpr.isFullyNumeric()){
            System.out.println("Fully numeric found");
            sb.append("\tmovq\t $" + StringManipulator.calculateNum(instruct.rexpr) + ", " + context.getVariableLocation(String.valueOf(a.getValue())) + "\n"); 
          }
          
          else{
            sb.append(instruct.rexpr.handleExpression(a, context));
            String lastReg = sb.substring(sb.lastIndexOf(",") + 2).replace("\n","");
            
            if(!lastReg.equals(context.getVariableLocation(String.valueOf(a.getValue()))))
              //temporary fix: to avoid unnecessary instruction (after a function-call) such as: mov -x(%rbp), -x(%rbp)
              sb.append("\tmovq\t " + lastReg + ", " + context.getVariableLocation(String.valueOf(a.getValue())) + "\n");
          }
          break;
          
        default:
          // case null, ie. for instructions like "1+2;"
          // optimize and don't calculate
          break;
      }
      instruct.assemblyStr = sb.toString();
      return instruct.assemblyStr;
    }
    
    else{
      System.err.println("ERROR: instruction is null");
      return null;
    }
  }
  
  
}
