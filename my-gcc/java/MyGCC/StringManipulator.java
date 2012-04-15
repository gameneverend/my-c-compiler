package MyGCC;

import java.util.Stack;

public class StringManipulator{
    
		
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
					
					switch(OperationType.valueOf(tmp.toUpperCase())){
						case ADDL:
							result = op1 + op2;
							break;
							
						case SUBL:
							result = op1 - op2;
							break;
							
						case IMULL:
							result = op1 * op2;
							break;
							
						case IDIVL:
							result = op1 / op2;
							break;
							
						default:
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
				
				else if(tmp.equals("(")){
					//TODO: push to opStack
				}
				
				else if(tmp.equals(")")){
					//TODO: until opStack.top is lparen, pop operators off the stack onto output.
					// pop lparen from stack
					// if stack runs out without finding lparen -> mismatched parentheses
				}
					
				else{ //operator
					if(!opStack.empty()){
						while(opStack.size() > 0 && getPrecedence(tmp) <= getPrecedence(opStack.peek())){
							//Empty opStack into output
							output.push(opStack.pop());
						}
					}
					opStack.push(tmp);
				}
			}
			
			while(!opStack.empty()){
				//TODO if top is parenthesis -> mismatched parentheses
				output.push(opStack.pop());
			}
			
			while(!output.empty())
				sb.append(output.pop() + " ");
				
			return sb.toString();
		}
		
    
    
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
			if(s.equals("imull") || s.equals("idivl"))
				return 2;
			else
				return 1;
		}
		
		
		public static StringBuffer handleArithmetics(StringBuffer sb, Arithmetic l, Arithmetic r, OperationType op, Context context) throws Exception{
			Register regL;
			Register regR;
			String lval = String.valueOf(l);
			String rval = String.valueOf(r);
			
			if(l.getValue() instanceof String && r.getValue() instanceof String){
				regL = Parser.regMan.addVariableToRegister(lval, Register.RegisterType.CALLEE_SAVED);
				regR = Parser.regMan.addVariableToRegister(rval, Register.RegisterType.CALLEE_SAVED);
				sb.append("\tmovl %" + l.getValue() + ", %" + regL.toString() + "\n");
				sb.append("\tmovl %" + r.getValue() + ", %" + regR.toString() + "\n");
				sb.append("\t" + op.toString() + " %" + regL.toString() + ", %" + regR.toString() + "\n");
			}
          
			else if(l.getValue() instanceof Integer && r.getValue() instanceof String){   
				regR = Parser.regMan.addVariableToRegister(rval, Register.RegisterType.CALLEE_SAVED);
				sb.append("\tmovl %" + context.getVariableLocation((String)r.getValue()) + ", %" + regR.toString() + "\n");
				sb.append("\t" + op.toString() + " %" + l.getValue() + ", %" + regR.toString() + "\n");
			}
         
			else if(l.getValue() instanceof String && r.getValue() instanceof Integer){   
				regR = Parser.regMan.addVariableToRegister(lval, Register.RegisterType.CALLEE_SAVED);
				sb.append("\tmovl %" + context.getVariableLocation((String)l.getValue()) + ", %" + regR.toString() + "\n");
				sb.append("\t" + op.toString() + " %" + r.getValue() + ", %" + regR.toString() + "\n");
			}	
			
			return sb;
		}
     
    
}
