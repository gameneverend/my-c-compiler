package MyGCC;

public class LabelManager {
  
  static private int nbFunctions = 0;
  static private int labelNb = 0;
  static private int stringLabel = 0;
  
  public static String getStringLabel() {
    return ".LC" + stringLabel++;
  }
  
  public static int getFunctionNumber(){
    labelNb = 0;
    return nbFunctions++;
  }
  
  public static String getLabel() {
    labelNb ++;
    return ".L" + nbFunctions + "." + labelNb;
  }
  
  public static String getBeginLabel(int i){
    return ".LFB" + i;
  }
  
  public static String getEndLabel(int i){
    return ".LFE" + i;
  }

}
