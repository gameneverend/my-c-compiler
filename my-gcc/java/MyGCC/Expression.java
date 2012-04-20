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
			Variable tmp = null;
			
			if(this.left != null){
				if(this.left instanceof Variable)
					bl = this.left.isFullyNumeric();
				else
					return false;
			}
			
			if(this.right != null){
				if(this.right instanceof Variable)
					br = this.right.isFullyNumeric();
				else
					return false;
			}
			
			if(this instanceof Variable)
				tmp = (Variable)this;
			else
				return false;
				
			if(tmp.getValue() instanceof String)
				return false;
				
			return bl && br;
		}
		
		/**
		 * Generates a String of the numeric expression.
		 **/
		public String toNumeric(){
			StringBuffer sb = new StringBuffer();
			Variable tmp = (Variable)this;
      
			if(this.op != null){
				Variable lft = (Variable)this.left;
				sb.append(lft.getValue().toString());
				
				while(tmp.op != null){
					if(tmp.right.left != null){
						sb.append(" " + tmp.op.toString() + " " +((Variable)tmp.right.left).getValue());
						tmp = (Variable)tmp.right;
					}
					else{
						sb.append(" " + tmp.op.toString() + " " + ((Variable)tmp.right).getValue());
						break;
          }
				}
				return sb.toString();
			}
			return (tmp.getValue()).toString();
		}
    

    //TODO gérer de manière plus simple les cas où left ou right est fully numerical
    public StringBuffer handleExpression(Expression e, Context context) throws Exception{
      StringBuffer sb = new StringBuffer();
				
      if(this.left != null)
        sb.append(this.left.handleExpression(e, context));
				
      if(this.right!= null)
        sb.append(this.right.handleExpression(e, context));

        
      if(this instanceof Variable && this.op == null){
        System.out.println("Variable caught: " + ((Variable)this).getValue());
        sb = StringManipulator.handleVariable(sb, (Variable)this, context);
      }

      if(this instanceof FunctionCall && this.op == null){
        System.out.println("Function-call caught");
        sb = StringManipulator.handleFunctionCall(sb, (FunctionCall)this, context);
        if(!((FunctionCall)this).getTag().equals(InstructionType.EXIT.toString()))
          sb.append("\tmovq %rax, " + context.getVariableLocation(String.valueOf(((Variable)e).getValue())) + "\n"); 
      }
        
      if(this.op != null){
        System.out.println("Operation handling");
        sb = StringManipulator.handleOperation(sb, this.op, this.left, this.right, context);
      }
      
      return sb;
    }
    
    
    public int length(){
      int i = 0;
      
      if(this.left != null)
        i += this.left.length();
      if(this.right != null)
        i += this.right.length();
      
      if(this != null && this.op == null)
        i++;
      return i;
    }
    
}
