package coverage.target;
import codcoverage.CoverageTransformer;
public class TriangleException extends Exception {
  public TriangleException(){
    CoverageTransformer.getInstance().logIn("loggin").info("*5*");
  }
}
