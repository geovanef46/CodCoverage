package codcoverage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.FixedHeightLayoutCache;

import org.apache.commons.io.FileUtils;

public class CoverageDataReader {

    List<String> ExpectedCoverage = new ArrayList<String>();
    List<String> CoverableLines = new ArrayList<String>();
    String logSource = "loggin2.txt";
    String logTarget = "loggin.txt";
    

    public List<String> getExpectedCoverage() {
        
        return ExpectedCoverage;
    }

    public void setExpectedCoverage() {
        List<String> list = new ArrayList<>();
        List<String> lista = insertFile(logSource);
        for (String string : lista) {
            if(!list.contains(string)) {
                list.add(string);
            }

        }
        ExpectedCoverage = list;
    }
    
    
    public List<String> getCoverableLines() {
        
        return CoverableLines;
    }

    public void setCoverableLines() {
        List<String> list = new ArrayList<>();
        List<String> lista = insertFile(logTarget);
        for (String string : lista) {
            if(!list.contains(string)) {
                list.add(string);
            }

        }
        CoverableLines = list;;
    }

    
    
    
    private List insertFile(String path) throws NullPointerException {
        List source = null;
        File file = null;
        try {
            file = new File(path);
            source = FileUtils.readLines(file);
        } catch (Exception e) {
            source = null;
        }

        return source;
    }
    
    public static void main(String[] args) {
        CoverageDataReader cdr = new CoverageDataReader();
        cdr.setExpectedCoverage();
        cdr.setCoverableLines();
        int valo1 = cdr.getCoverableLines().size();
        
        
        System.out.println(cdr.getCoverableLines().size());
        System.out.println(cdr.getExpectedCoverage().size());
    }
    
    
}
