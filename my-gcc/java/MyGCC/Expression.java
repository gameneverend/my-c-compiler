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
			String tmp;
			if(this.op == null)
				return ((Variable)this).getValue().toString();
				
			String l = this.left.toNumeric();
			String r = this.right.toNumeric();
			
			if(this.priority)
				tmp = "( " + l + " " + op + " " + r + " )";
			else
				tmp = l + " " + op + " " + r;
				
			return tmp;
		}
    
    
    
    public StringBuffer handleExpression(Expression e, Context context) throws Exception{
			StringBuffer sb = new StringBuffer();
			String lastReg;
			System.out.println("Handling expression");
			
			
			if(this.right == null){
				//Handle leaf
				
				if(this instanceof Variable){
					System.out.println("\tVariable caught: " + ((Variable)this).getValue());
					sb = ExpressionHelper.handleVariable(sb, (Variable)this, Register.RAX, context);	
				}

				else if(this instanceof FunctionCall){
					System.out.println("\tFunction-call caught: " + ((FunctionCall)this).getTag());
					sb = ExpressionHelper.handleFunctionCall(sb, (FunctionCall)this, context);
				}
				return sb;
			}
			
			sb.append(this.left.handleExpression(e, context));
			
			if(this.right.op != null){
				//Handle expression with three or more operands
				
				if(this.right.priority){
					this.right.priority = false;
					
          sb.append(context.virtualPush(Register.RAX.toString()));
          sb.append(this.right.handleExpression(e, context));
          sb.append(context.virtualPop(Register.RCX.toString()));
          sb = ExpressionHelper.handleOperation(sb, this.op, Register.RAX, Register.RCX);
          sb.append(asm(Assembly.MOV, Register.RCX, Register.RAX));	
				}
				
				
				else if(ExpressionHelper.getPrecedence(this.op.toString()) > ExpressionHelper.getPrecedence(this.right.op.toString())){
					
					if(this.right.left instanceof Variable)
						sb = ExpressionHelper.handleVariable(sb, (Variable)this.right.left, Register.RCX, context);
					else{
						sb.append(asm(Assembly.MOV, Register.RAX, Register.RCX));	
						sb = ExpressionHelper.handleFunctionCall(sb, (FunctionCall)this.right.left, context);
					}
					
					sb = ExpressionHelper.handleOperation(sb, this.op, Register.RCX, Register.RAX);
					sb.append(context.virtualPush(Register.RAX.toString()));
					sb.append(this.right.right.handleExpression(e, context));
					sb.append(context.virtualPop(Register.RCX.toString()));
					sb = ExpressionHelper.handleOperation(sb, this.right.op, Register.RAX, Register.RCX);
					sb.append(asm(Assembly.MOV, Register.RCX, Register.RAX));
				}		
				
				else{
					if(this.left instanceof FunctionCall)
						lastReg = Register.RAX.toString();
					else
						lastReg = sb.substring(sb.lastIndexOf(",") + 2).replace("\n","");
					
					sb.append(context.virtualPush(lastReg));
					sb.append(this.right.handleExpression(e, context));
					sb.append(asm(Assembly.MOV, Register.RAX, Register.RCX));
					sb.append(context.virtualPop(Register.RAX.toString()));
					
					sb = ExpressionHelper.handleOperation(sb, this.op, Register.RAX, Register.RCX);
					sb.append(asm(Assembly.MOV, Register.RCX, Register.RAX));
				}
			}
			
			else{
				//Handle simple node (l op r)
				
				if(this.right instanceof Variable){
					
					if(((Variable)this.right).getValue() instanceof Integer)
						sb = ExpressionHelper.handleOperation(sb, this.op, "$"+((Variable)this.right).getValue());
						
					else{
						sb = ExpressionHelper.handleVariable(sb, (Variable)this.right, Register.RCX, context);
						sb = ExpressionHelper.handleOperation(sb, this.op, Register.RCX, Register.RAX);
					}
				}
					
				else{
					sb.append(context.virtualPush(Register.RAX.toString()));
					sb = ExpressionHelper.handleFunctionCall(sb, (FunctionCall)this.right, context);
					sb.append(context.virtualPop(Register.RCX.toString()));
					sb = ExpressionHelper.handleOperation(sb, this.op, Register.RAX, Register.RCX);
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
