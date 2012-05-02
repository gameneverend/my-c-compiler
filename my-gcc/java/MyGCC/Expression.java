package MyGCC;

import java.util.Stack;

public abstract class Expression{
  
    public Expression left;
    public Expression right;
    public OperationType op;
    public boolean priority;
    public Flag flag;
    
    public Expression(){
      this.left = null;
      this.right = null;
      this.op = null;
      this.priority = false;
      this.flag = null;
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
			
			if(this.op == null){
				tmp = ((Variable)this).getValue().toString();
				
				if(this.flag != null && this.flag.equals(Flag.UMINUS))
					tmp = "-" + tmp;
			}
			else{
				String l = this.left.toNumeric();
				String r = this.right.toNumeric();

				if(this.priority)
					tmp = "( " + l + " " + op + " " + r + " )";
				else
					tmp = l + " " + op + " " + r;
					
				if(this.flag != null && this.flag.equals(Flag.UMINUS))
					tmp = "( 0 " + OperationType.SUB + " " + tmp + " )" ;
			}
			

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
			if(this.flag != null && this.flag.equals(Flag.UMINUS))
				sb.append(ExpressionHelper.asm(Assembly.NEG, Register.RAX));
			
			if(this.right.op != null && this.right.priority == false){
				//Handle expression with three or more operands
				
				if(this.right.left.priority){
					//Handle priority expression first (parentheses).
					this.right.left.priority = false;
					
          sb.append(context.virtualPush(Register.RAX.toString()));
          sb.append(this.right.left.handleExpression(e, context));
          sb.append(context.virtualPop(Register.RDX.toString()));
          sb = ExpressionHelper.handleOperation(sb, this.op, Register.RAX, Register.RDX);
          sb.append(context.virtualPush(Register.RAX.toString()));
          sb.append(this.right.right.handleExpression(e, context));
          sb.append(context.virtualPop(Register.RDX.toString()));
          sb = ExpressionHelper.handleOperation(sb, this.right.op, Register.RAX, Register.RDX);
				}
				
				
				else if(ExpressionHelper.getPrecedence(this.op.toString()) > ExpressionHelper.getPrecedence(this.right.op.toString())){
					//Handle operators of higher precedence first.
					
					if(this.right.left instanceof Variable)
						sb = ExpressionHelper.handleVariable(sb, (Variable)this.right.left, Register.RDX, context);
					else{
						sb.append(ExpressionHelper.asm(Assembly.MOV, Register.RAX, Register.RDX));	
						sb = ExpressionHelper.handleFunctionCall(sb, (FunctionCall)this.right.left, context);
					}
					
					sb = ExpressionHelper.handleOperation(sb, this.op, Register.RDX, Register.RAX);
					sb.append(context.virtualPush(Register.RAX.toString()));
					sb.append(this.right.right.handleExpression(e, context));
					sb.append(context.virtualPop(Register.RDX.toString()));
					sb = ExpressionHelper.handleOperation(sb, this.right.op, Register.RAX, Register.RDX);
				}		
				
				else{
					sb.append(context.virtualPush(Register.RAX.toString()));
					sb.append(this.right.handleExpression(e, context));
					sb.append(ExpressionHelper.asm(Assembly.MOV, Register.RAX, Register.RDX));
					sb.append(context.virtualPop(Register.RAX.toString()));
					sb = ExpressionHelper.handleOperation(sb, this.op, Register.RAX, Register.RDX);
				}
			}
			
			else{
				//Handle simple node (l op r)
				
				if(this.right.priority){
					sb.append(context.virtualPush(Register.RAX.toString()));
					sb.append(this.right.handleExpression(e, context));
					sb.append(context.virtualPop(Register.RDX.toString()));
					sb = ExpressionHelper.handleOperation(sb, this.op, Register.RAX, Register.RDX);
				}
				
				else if(this.right instanceof Variable){
					
					if(((Variable)this.right).getValue() instanceof Integer){
						if(this.right.flag != null && this.right.flag.equals(Flag.UMINUS))
							sb = ExpressionHelper.handleOperation(sb, this.op, "$-"+((Variable)this.right).getValue());
						else
							sb = ExpressionHelper.handleOperation(sb, this.op, "$"+((Variable)this.right).getValue());
					}
						
					else{
						sb = ExpressionHelper.handleVariable(sb, (Variable)this.right, Register.RDX, context);
						sb = ExpressionHelper.handleOperation(sb, this.op, Register.RDX, Register.RAX);
					}
				}
					
				else if(this.right instanceof FunctionCall){
					sb.append(context.virtualPush(Register.RAX.toString()));
					sb = ExpressionHelper.handleFunctionCall(sb, (FunctionCall)this.right, context);
					sb.append(context.virtualPop(Register.RDX.toString()));
					sb = ExpressionHelper.handleOperation(sb, this.op, Register.RAX, Register.RDX);
          sb.append(ExpressionHelper.asm(Assembly.MOV, Register.RAX, Register.RDX));
				}
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
    
    public String toString(){
			//TODO
			return new String();
		}
    
}
