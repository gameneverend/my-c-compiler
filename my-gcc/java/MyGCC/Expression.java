package MyGCC;

import java.util.Stack;

public abstract class Expression{
  
    public Expression left;
    public Expression right;
    public OperationType op;
    public boolean priority;
    
    public Expression(){
      this.left = null;
      this.right = null;
      this.op = null;
      this.priority = false;
    }
    
    /**
     * This functions return the highest number of parameters contained in a
     * function call of the expression.
     * The value returned is <b>-1</b> if there's no call to functions in the expression
     */
    public int maxParameters(){
      int n = -1;
      if (left != null) n = Math.max(n, left.maxParameters());
      if (right != null) n = Math.max(n, right.maxParameters());
      return n;
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
    
    
    
    public StringBuffer handleExpression(Expression e, Context context) throws Exception{
			StringBuffer sb = new StringBuffer();
			String lastReg;
			System.out.println("Handling expression");
			
			
			if(this.right == null){
				
				if(this instanceof Variable){
					System.out.println("Variable caught: " + ((Variable)this).getValue());
					sb = ExpressionHelper.handleVariable(sb, (Variable)this, Register.RAX, context);
					
				}

				else if(this instanceof FunctionCall){
					System.out.println("Function-call caught: " + ((FunctionCall)this).getTag());
					sb = ExpressionHelper.handleFunctionCall(sb, (FunctionCall)this, context);
				}
				return sb;
			}
			
			
			sb.append(this.left.handleExpression(e, context));
			if(this.right.op != null){
				
				if(this.right.priority){
					this.right.priority = false;
					
          sb.append(context.virtualPush(Register.RAX.toString()));
          sb.append(this.right.handleExpression(e, context));
          sb.append(context.virtualPop(Register.RDX.toString()));
          sb = ExpressionHelper.handleOperation(sb, this.op, Register.RAX, Register.RDX);
          sb.append(asm(Assembly.MOV, Register.RDX, Register.RAX));	
				}
				
				
				else if(ExpressionHelper.getPriority(this.op.toString()) > ExpressionHelper.getPriority(this.right.op.toString())){
					
					if(this.right.left instanceof Variable)
						sb = ExpressionHelper.handleVariable(sb, (Variable)this.right.left, Register.RDX, context);
					else{
						sb.append(asm(Assembly.MOV, Register.RAX, Register.RDX));	
						sb = ExpressionHelper.handleFunctionCall(sb, (FunctionCall)this.right.left, context);
					}
					
					sb = ExpressionHelper.handleOperation(sb, this.op, Register.RDX, Register.RAX);
					sb.append(context.virtualPush(Register.RAX.toString()));
					sb.append(this.right.right.handleExpression(e, context));
					sb.append(context.virtualPop(Register.RDX.toString()));
					sb = ExpressionHelper.handleOperation(sb, this.right.op, Register.RAX, Register.RDX);
					sb.append(asm(Assembly.MOV, Register.RDX, Register.RAX));
				}		
				
				else{
					if(this.left instanceof FunctionCall)
						lastReg = Register.RAX.toString();
					else
						lastReg = sb.substring(sb.lastIndexOf(",") + 2).replace("\n","");
					
					sb.append(context.virtualPush(lastReg));
					sb.append(this.right.handleExpression(e, context));
					sb.append(asm(Assembly.MOV, Register.RAX, Register.RDX));
					sb.append(context.virtualPop(Register.RAX.toString()));
					
					sb = ExpressionHelper.handleOperation(sb, this.op, Register.RAX, Register.RDX);
					sb.append(asm(Assembly.MOV, Register.RDX, Register.RAX));
				}
			}
			
			else{
				
				if(this.right instanceof Variable){
					
					if(((Variable)this.right).getValue() instanceof Integer)
						sb = ExpressionHelper.handleOperation(sb, this.op, "$"+((Variable)this.right).getValue());
						
					else{
						sb = ExpressionHelper.handleVariable(sb, (Variable)this.right, Register.RDX, context);
						sb = ExpressionHelper.handleOperation(sb, this.op, Register.RDX, Register.RAX);
					}
				}
					
				else{
					sb.append(asm(Assembly.MOV, Register.RAX, Register.RDX));
					sb = ExpressionHelper.handleFunctionCall(sb, (FunctionCall)this.right, context);
					sb = ExpressionHelper.handleOperation(sb, this.op, Register.RDX, Register.RAX);
				}
			}
			
			return sb;
		}
		
		public String asm(Assembly instruction, Register r1, Register r2){
			return "\t" + instruction + "\t" + r1 + ", " + r2 + "\n";
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
