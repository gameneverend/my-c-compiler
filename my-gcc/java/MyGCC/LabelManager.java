package MyGCC;

public class LabelManager {
  
  static private int nbFunctions = 0;
  static private int labelNb = 0;
  
  public static String getStringLabel() {
    return ".LC0";
  }
  
  public static int getFunctionNumber(){
    labelNb = 0;
    return nbFunctions++;
  }
  
  public static String getLabel() {
    labelNb ++;
    return ".L" + (nbFunctions - 1) + "." + labelNb;
  }
  
  public static String getBeginLabel(int i){
    return ".LFB" + i;
  }
  
  public static String getEndLabel(int i){
    return ".LFE" + i;
  }

}
