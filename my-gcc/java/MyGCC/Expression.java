package MyGCC;

import java.util.Stack;

public abstract class Expression{
  
    public Expression left;
    public Expression right;
    public OperationType op;
    
    public Expression(){
      this.left = null;
      this.right = null;
      this.op = null;
    }
    
    /**
     * Checks if the expression is composed exclusively of numeric values
     **/
    public boolean isFullyNumeric(){
			boolean bl = true;
			boolean br = true;
			Arithmetic tmp = null;
			
			if(this.left != null)
				bl = this.left.isFullyNumeric();
			if(this.right != null)
				br = this.right.isFullyNumeric();
				
			tmp = (Arithmetic)this;
			if(tmp.getValue() instanceof String)
				return false;
				
			return bl && br;
		}
		
		/**
		 * Generates a String of the numeric expression.
		 **/
		public String toNumeric(){
			StringBuffer sb = new StringBuffer();
			Arithmetic tmp = (Arithmetic)this;
			if(this.op != null){
				Arithmetic lft = (Arithmetic)this.left;
				sb.append(lft.getValue().toString());
				
				while(tmp.op != null){
					if(tmp.right.left != null){
						sb.append(" " + tmp.op.toString() + " " +((Arithmetic)tmp.right.left).getValue());
						tmp = (Arithmetic)tmp.right;
					}
					else{
						sb.append(" " + tmp.op.toString() + " " + ((Arithmetic)tmp.right).getValue());
						break;
					}
				}
				return sb.toString();
			}
			return (tmp.getValue()).toString();
		}

    
    
    public StringBuffer handleExpression(Context context) throws Exception{
			StringBuffer sb = new StringBuffer();
			
			if(this == null){
				//sb.append("\tleave\n\tret\n"); Already handled in epilogue()
				System.err.println("Expression is null\n");
			}
			
			if(this.isFullyNumeric()){
				//TODO get the corresponding register for this evaluation (always eax?)
				return sb.append("\tmovl %" + StringManipulator.calculateNum(this) + ", %TMP\n");
			}
			
			else{
				
				Arithmetic myLeft = (Arithmetic)this.left;
				Arithmetic myRight = (Arithmetic)this.right;
				String lval = String.valueOf(myLeft.getValue());
				String rval = String.valueOf(myRight.getValue());
				Register regL = null;
				Register regR = null;
			
				if(this.left.op != null)
					sb.append(this.left.handleExpression(context));
				if(this.right.op != null)
					sb.append(this.right.handleExpression(context)); 
      

				if(myLeft.getValue() instanceof String && myRight.getValue() instanceof String){
					regL = Parser.regMan.addVariableToRegister(lval, Register.RegisterType.CALLEE_SAVED);
					regR = Parser.regMan.addVariableToRegister(rval, Register.RegisterType.CALLEE_SAVED);
					sb.append("\tmovl %" + myLeft.getValue() + ", %" + regL.toString() + "\n");
					sb.append("\tmovl %" + myRight.getValue() + ", %" + regR.toString() + "\n");
					sb.append("\t" + this.op.toString() + " %" + regL.toString() + ", %" + regR.toString() + "\n");
				}
            
				else if(myLeft.getValue() instanceof Integer && myRight.getValue() instanceof String){
					regR = Parser.regMan.addVariableToRegister(rval, Register.RegisterType.CALLEE_SAVED);
					sb.append("\tmovl %" + context.getVariableLocation((String)myRight.getValue()) + ", %" + regR.toString() + "\n");
					sb.append("\t" + this.op.toString() + " %" + myLeft.getValue() + ", %" + regR.toString() + "\n");
				}
            
				else if(myLeft.getValue() instanceof String && myRight.getValue() instanceof Integer){
					regR = Parser.regMan.addVariableToRegister(lval, Register.RegisterType.CALLEE_SAVED);
					sb.append("\tmovl %" + context.getVariableLocation((String)myLeft.getValue()) + ", %" + regR.toString() + "\n");
					sb.append("\t" + this.op.toString() + " %" + myRight.getValue() + ", %" + regR.toString() + "\n");
				}
			}
      
      return sb;
		}
    
    
}
