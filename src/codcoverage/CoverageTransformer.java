/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codcoverage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.text.Document;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.StringLiteral;



/**
 * Deve receber o source e reescrever em target 
 * 
 * OBS: O método deve buscar na suite de testes as linhas específicas a serem buscadas,
 * até o momento tenta inserir um log em cada método, mas por conta do erro na biblioteca 
 * não foi prosseguido para buscar os pontos especificos;
 * 
 * @author jason
 * 
 **/
public class CoverageTransformer {
    String pathSource = "src/codcoverage/source/src/example";
    String pathTarget = "src/codcoverage/target";
    String testSuite = "src/codcoverage/source/src/test/TriangleTest.java";
    private static CoverageTransformer instance;
    
    private CoverageTransformer() {
       
    }
    
    public static synchronized CoverageTransformer getInstance() {
        if (instance == null)
            instance = new CoverageTransformer();
 
        return instance;
    }

/** realiza a leitura de um arquivo 
 *
 * @return String do arquivo
 */
public String insertFile(String path) throws NullPointerException {
        String source="";
        try {
         source = FileUtils.readFileToString(new File(path), "UTF-8");
        } catch (Exception e) {
           source = null;
        }

      return source;      
 }


public boolean analise() {
    insertLog(testSuite, null);
    return true;
}


public boolean analise(String name) {
    String source=pathSource;
    String[] extensions = {"*.java"};
    File directory = new File(source);
    Collection<File> files = FileUtils.listFiles(directory,extensions, false);
    try {
        while(!files.isEmpty()) {
            source = FileUtils.readFileToString(new File(source), "UTF-8");
            insertLog(source,name);
        }
    
    } catch (Exception e) {
  System.out.println("erro ao buscar arquivo...");
    }

    return true;
}

 
 /**
  * Inserir um trecho de código para log nos métodos especificos 
  * tratados pelo teste. 
 * @return 
 * @throws SecurityException tentativa de tratar o erro de assinatura das bibliotecas, contornando o erro.
 */
public boolean insertLog(String source, String name) throws SecurityException{ 
     
     Logger log = logIn(); //criando arquivo de log
     ASTParser parser = ASTParser.newParser(AST.JLS8); 
     parser.setSource(insertFile(source).toCharArray()); //adicionar o source ao parser
     parser.setCompilerOptions(JavaCore.getOptions()); 
     parser.setKind(ASTParser.K_COMPILATION_UNIT); 

 CompilationUnit unit = (CompilationUnit) parser.createAST(null);//criar AST do tipo CompilationUnit (a raiz contem o arquivo completo) 
 AST ast = unit.getAST(); 

 List<MethodDeclaration> methodDeclarations = MethodDeclarationFinder.perform(unit); 
 for (MethodDeclaration methodDeclaration : methodDeclarations) { // inserir declarações de log dentro dos métodos
     if (name != null) {
        if(name == methodDeclaration.getName().getIdentifier()) {
            MethodInvocation methodInvocation = ast.newMethodInvocation();
            
//          log.log(Level.INFO,"#"+new org.eclipse.jface.text.Document(this.insertFile()).getNumberOfLines());
          
          methodInvocation.setExpression(ast.newSimpleName("logIn"));c 
         
          methodInvocation.setName(ast.newSimpleName("log"));
          
          StringLiteral literal = ast.newStringLiteral();
          literal.setLiteralValue("Level.INFO,\"OK\"");
          methodInvocation.arguments().add(literal);
           
        }
    }
     
     analise(methodDeclaration.getName().getIdentifier());

     System.out.println(methodDeclaration.getName().getFullyQualifiedName());

 }
 return true;
 }
 
 
 public Logger logIn() {
     
     Logger log = Logger.getLogger("Log");
     FileHandler filetxt = null;

         try {
            filetxt = new FileHandler("loggin.txt");
        } catch (IOException e) {
            // TODO Auto-generated catch block
           
        }
         filetxt.setFormatter(new SimpleFormatter());

     log.addHandler(filetxt);
     
     return log;
    
}
 
 
 
 
 public boolean directoryNav() {
     
     return true;
 }
 
 
 public static void main(String[] args) {
     
     CoverageTransformer cc = CoverageTransformer.getInstance();
         cc.analise();
    
     
//     CoverageTransformer cc = new CoverageTransformer();
//     try {
//         cc.insertLog();
//    } catch (SecurityException e) {
//       System.out.println("Erro de assinatura da biblioteca..."+ e.getMessage());
//    }
     
 }




    
}
