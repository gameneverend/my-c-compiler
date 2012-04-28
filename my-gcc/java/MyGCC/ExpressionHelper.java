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
				 s.equals(OperationType.IDIV.toString()) ||
				 s.equals(OperationType.MOD.toString()))
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
					stack.push(Integer.parseInt(tmp)); //push operand
				
				else{
					op1 = (Integer)(stack.pop());
					op2 = (Integer)(stack.pop());
					
					switch(OperationType.getOp(tmp)){	//TODO: always floor values (integer only).
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
				
				else if(tmp.equals("(")){	//TODO: if stack runs out without finding rparen -> mismatched parentheses
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
				//TODO: if top is parenthesis -> mismatched parentheses
				output.push(opStack.pop());
			}
			
			while(!output.empty())
				sb.append(output.pop() + " ");

			return sb.toString();
		}
		
    
    
    
		public static StringBuffer handleFunctionCall(StringBuffer sb, FunctionCall f, Context context) throws Exception{
      Variable tmp;
      String val;
      Integer num;
      Register reg;
      System.out.println("\tTag: " + f.getTag());
      int i = f.getArgs().size() - 1;
      for(Expression e : f.getArgs()){
				System.out.println("\tHandling next arg");
				
				if(e.isFullyNumeric()){
					num = calculateNum(e);
					sb.append("\t" + Assembly.MOV + "\t$" + num + ", " + Parser.regMan.getArgReg(String.valueOf(num), i) + "\n"); 
				}
        
        else if(e.op == null){
          tmp = (Variable)e;
          val = String.valueOf(tmp.getValue());
          
					if(Parser.regMan.isListedVariable(val)){
						reg = Parser.regMan.addVariableToRegister(val, Register.RegisterType.CALLER_SAVED);
            sb.append("\t" + Assembly.MOV + "\t" + reg.toString() + ", " + Parser.regMan.getArgReg(val, i) + "\n");
          }
          else
            sb.append("\t" + Assembly.MOV + "\t" + context.getVariableLocation(val) + ", " + Parser.regMan.getArgReg(val, i) + "\n");
        }
        
        else{
					sb.append(e.handleExpression(null, context).toString());
					sb.append("\t" + Assembly.MOV + "\t" + sb.substring(sb.lastIndexOf(",") + 2).replace("\n","") + ", " + Parser.regMan.getArgReg(null, i) + "\n");  //getArgReg param1: ?
        }
          
        i--;
      }
        
      sb.append("\tcall\t" + f.getTag() + "\n");
      return sb;
    }
    
    
    public static StringBuffer handleVariable(StringBuffer sb, Variable a, Register dst, Context context) throws Exception{
      if(a.getValue() != null){
        String val = String.valueOf(a.getValue());
          
        if(a.getValue() instanceof Integer)
          sb.append("\t" + Assembly.MOV + "\t$" + a.getValue() + ", " + dst + "\n");
          
        else if(a.getValue() instanceof String)
					sb.append("\t" + Assembly.MOV + "\t" + context.getVariableLocation((String)a.getValue()) + ", " + dst + "\n");
      }
			return sb;
		}
    
    
    public static StringBuffer handleOperation(StringBuffer sb, OperationType op, Register src, Register dst) throws Exception{
			if(op.equals(OperationType.IDIV) || op.equals(OperationType.MOD)){
				//TODO Elyas: consider situation when src = %rax
				sb.append("\t" + Assembly.MOV + "\t" + src + ", " + Register.RBX + "\n");
				sb.append("\t" + Assembly.CONVERT + "\n");
				sb.append("\t" + op + "\t" + Register.RBX + "\n");
				if(op.equals(OperationType.MOD))
					sb.append("\t" + Assembly.MOV + "\t" + Register.RDX + ", " + Register.RAX + "\n");				
			}
			
			else
				sb.append("\t" + op + "\t" + src + ", " + dst + "\n");
			return sb;
    }
    
    public static StringBuffer handleOperation(StringBuffer sb, OperationType op, String src) throws Exception{
			if(op.equals(OperationType.IDIV) || op.equals(OperationType.MOD)){
				//TODO Elyas: consider situation when src = %rax
				sb.append("\t" + Assembly.MOV + "\t" + src + ", " + Register.RBX + "\n");
				sb.append("\t" + Assembly.CONVERT + "\n");
				sb.append("\t" + op + "\t" + Register.RBX + "\n");
				if(op.equals(OperationType.MOD))
					sb.append("\t" + Assembly.MOV + "\t" + Register.RDX + ", " + Register.RAX + "\n");
			}
			
			else
				sb.append("\t" + op + "\t" + src + ", " + Register.RAX + "\n");
			return sb;
    }
    
    
    
     
    
}
