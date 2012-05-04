package MyGCC;

import java.util.Stack;

public class ExpressionHelper{
    
		
    
    public static boolean isInteger(String input){
			try{
				Integer.parseInt(input);
				return true;
			}
			catch(Exception e){
				return false;
			}
		}
		
		public static int getPrecedence(String s){
			if(s.equals(OperationType.IMUL.toString()) ||
				 s.equals(OperationType.IDIV.toString()))
				return 3;
			else if(s.equals(OperationType.MOD.toString()))
				return 2;
			else
				return 1;
		}
    
    
		/**
     * Calculates the value of a numeric expression, written in prefix notation
     **/
    public static Integer calculateNum(Expression e){
			String s = e.toNumeric();
			s = infixToPrefix(s);
			String tmp;
			Integer op1, op2;
			Integer result = 0;
			String[] sp = s.split(" ");
			Stack<Object> stack = new Stack();
			
			for(int i = sp.length-1; i >= 0; i--){
				tmp = sp[i];
				
				if(isInteger(tmp))
					//push operand
					stack.push(Integer.parseInt(tmp));
				
				else{
					op1 = (Integer)(stack.pop());
					op2 = (Integer)(stack.pop());
					
					switch(OperationType.getOp(tmp)){
						case ADD:
							result = op1 + op2;
							break;
							
						case SUB:
							result = op1 - op2;
							break;
							
						case IMUL:
							result = op1 * op2;
							break;
							
						case IDIV:
							result = op1 / op2;
							break;
							
						case MOD:
							result = op1 % op2;
							break;
							
						default:
							System.err.println("Error: unrecognized operation.");
							break;
					}
					stack.push(result);
				}
			}
			return (Integer)(stack.pop());
		}
		
		/**
		 * Returns a prefix version of the infix-notation input.
		 * Inspired from Shunting-yard algorithm.
		 **/
		public static String infixToPrefix(String s){
			StringBuffer sb = new StringBuffer();
			String[] sp = s.split(" ");
			String tmp;
			Stack<String> output = new Stack();
			Stack<String> opStack = new Stack();
			
			for(int i = sp.length-1; i >= 0; i--){
				tmp = sp[i];
				
				if(isInteger(tmp))
					output.push(tmp);
				
				else if(tmp.equals(")"))
          opStack.push(tmp);
				
				else if(tmp.equals("(")){
					while(!opStack.empty() && !opStack.peek().equals(")"))
						output.push(opStack.pop());
						
					if(!opStack.empty())
						opStack.pop();
				}
					
				else{ //operator
					while(!opStack.empty() && !opStack.peek().equals(")") && getPrecedence(tmp) < getPrecedence(opStack.peek()))
							output.push(opStack.pop());
							
					opStack.push(tmp);
				}
			}
			
			while(!opStack.empty()){
				if(opStack.peek().equals(")")){
					opStack.pop();
					continue;
				}
				output.push(opStack.pop());
			}
			
			while(!output.empty())
				sb.append(output.pop() + " ");

			return sb.toString();
		}
		
    
    
    
		public static StringBuffer handleFunctionCall(StringBuffer sb, FunctionCall f, Context context) throws Exception {
      
      Variable tmp;
      String val;
      Integer num;
      Register reg;
      System.out.println("\tTag: " + f.getTag());
      int i = f.getArgs().size() - 1;
      for(Expression e : f.getArgs()){
				
				if(e.isFullyNumeric()){
					num = calculateNum(e);
					sb.append(asm(Assembly.MOV, "$" + num, Parser.regMan.getArgReg(String.valueOf(num), i))); 
				}
        
        else if(e.op == null){
					if(e instanceof Variable){
						tmp = (Variable)e;
						val = String.valueOf(tmp.getValue());
						
						if(Parser.regMan.isListedVariable(val)){
							reg = Parser.regMan.addVariableToRegister(val, Register.RegisterType.CALLER_SAVED);
							sb.append(asm(Assembly.MOV, reg, Parser.regMan.getArgReg(val, i)));
						}
						else
							sb.append(asm(Assembly.MOV, context.getVariableLocation(val), Parser.regMan.getArgReg(val, i)));
					}
					else{
						sb.append(handleFunctionCall(sb, (FunctionCall)e, context));
						sb.append(asm(Assembly.MOV, Register.RAX, Parser.regMan.getArgReg(Register.RAX.toString(), i)));
					}
        }
        
        else{
					sb.append(e.handleExpression(null, context).toString());
					sb.append(asm(Assembly.MOV, sb.substring(sb.lastIndexOf(",") + 2).replace("\n",""), Parser.regMan.getArgReg(null, i)));  //getArgReg param1: ?
        }
          
        i--;
      }
        
      sb.append(asm(Assembly.CALL, f.getTag()));
      if(f.flag != null && f.flag.equals(Flag.UMINUS))
				sb.append(asm(Assembly.NEG, Register.RAX));
      return sb;
    }
    
    
    public static StringBuffer handleReadInt(StringBuffer sb, FunctionCall f, Context c) throws Exception {
      for(Expression e : f.getArgs()){
        sb.append(asm(Assembly.MOV, "$" + LabelManager.getStringLabel(), Register.RAX.toString()));
        
        String ident = String.valueOf(((Variable)e).getValue());
        //if(Parser.regMan.isListedVariable(ident)) {
          sb.append(asm(Assembly.LEA, c.getVariableLocation(ident), Register.RDX));
          sb.append(asm(Assembly.MOV, Register.RDX, Register.RSI));
          sb.append(asm(Assembly.MOV, Register.RAX, Register.RDI));
          sb.append(asm(Assembly.MOV, "$0", Register.RAX));
          sb.append(asm(Assembly.CALL, "__isoc99_scanf"));
        //}
      }
      
      return sb;
    }
    
    
    public static StringBuffer handleVariable(StringBuffer sb, Variable a, Register dst, Context context) throws Exception{
			if(a.index != null){
					sb.append(a.index.handleExpression(null, context).toString());
					sb.append(asm(Assembly.MOV, context.getVariableLocation((String)a.getValue()), dst));
			}
			
      else if(a.getValue() != null){
        String val = String.valueOf(a.getValue());
        
        if(a.getValue() instanceof Integer){
					if(a.flag != null && a.flag.equals(Flag.UMINUS))
						sb.append(asm(Assembly.MOV, "$-" + a.getValue(), dst));
					else
						sb.append(asm(Assembly.MOV, "$" + a.getValue(), dst));	
				}
				
				else if(a.getValue() instanceof String){
					sb.append(asm(Assembly.MOV, context.getVariableLocation((String)a.getValue()), dst));
					if(a.flag != null && a.flag.equals(Flag.UMINUS))
						sb.append(asm(Assembly.NEG, dst));
				}
      }
			return sb;
		}
    
    /**
     * This method generates the assembly code for handling an operation.
     * The calculated result is placed in Register.RAX
     */
    public static StringBuffer handleOperation(StringBuffer sb, OperationType op, Register src, Register dst) throws Exception{
      if(OperationType.isConditional(op))
        sb.append(asm(Assembly.COMPARE, src, dst));
      
			else if(op.equals(OperationType.IDIV) || op.equals(OperationType.MOD)){
				sb.append(asm(Assembly.MOV, src, Register.RBX));
				
				if(src.equals(Register.RAX))
					sb.append(asm(Assembly.MOV, Register.RDX, Register.RAX));
				sb.append(asm(Assembly.CONVERT, ""));	//sign extend RAX to RDX:RAX
				sb.append(asm(OperationType.IDIV, Register.RBX));
				
				if(op.equals(OperationType.MOD))
					sb.append(asm(Assembly.MOV, Register.RDX, Register.RAX));				
			}
			
			else{
				sb.append(asm(op, src, dst));
				if(!dst.equals(Register.RAX))
					sb.append(asm(Assembly.MOV, dst, Register.RAX));
			}
				return sb;
    }
    
    public static StringBuffer handleOperation(StringBuffer sb, OperationType op, String src) throws Exception{
      if(OperationType.isConditional(op))
        sb.append(asm(Assembly.COMPARE, src, Register.RAX));     
      
			else if(op.equals(OperationType.IDIV) || op.equals(OperationType.MOD)){
				sb.append(asm(Assembly.MOV, src, Register.RBX));
				sb.append(asm(Assembly.CONVERT, ""));	//sign extend RAX to RDX:RAX
				sb.append(asm(OperationType.IDIV, Register.RBX));
				
				if(op.equals(OperationType.MOD))
					sb.append(asm(Assembly.MOV, Register.RDX, Register.RAX));
			}
			
			else
				sb.append(asm(op, src, Register.RAX));
			return sb;
    }
    
    
    public static String asm(Object instruction, Object src, Object dst){
			return "\t" + instruction.toString() + "\t" + src.toString() + ", " + dst.toString() + "\n";
		}
		
		
    public static String asm(Object instruction, Object r){
			return "\t" + instruction + "\t" + r + "\n";
		}
    
     
    
}
