/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codcoverage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
    String path = "src/codcoverage/source/src/example/Triangle.java";
    


/** realiza a leitura de um arquivo 
 *
 * @return String do arquivo
 */
public String insertFile() {
        String source="";
        try {
         source = FileUtils.readFileToString(new File(path), "UTF-8");
         System.out.println("Arquivo obtido!");
        } catch (Exception e) {
            System.out.println("Deu ruim... não encontrou o arquivo");
        }

      return source;      
 }

 
 /**
  * Inserir um trecho de código para log nos métodos especificos 
  * tratados pelo teste. 
 * @return 
 * @throws SecurityException tentativa de tratar o erro de assinatura das bibliotecas, contornando o erro.
 */
public boolean insertLog() throws SecurityException{ 
     
     Logger log = logIn(); //criando arquivo de log
     ASTParser parser = ASTParser.newParser(AST.JLS8); 
     parser.setSource(insertFile().toCharArray()); //adicionar o source ao parser
     parser.setCompilerOptions(JavaCore.getOptions()); 
     parser.setKind(ASTParser.K_COMPILATION_UNIT); 

 CompilationUnit unit = (CompilationUnit) parser.createAST(null);//criar AST do tipo CompilationUnit (a raiz contem o arquivo completo) 
 AST ast = unit.getAST(); 

 List<MethodDeclaration> methodDeclarations = MethodDeclarationFinder.perform(unit); 
 for (MethodDeclaration methodDeclaration : methodDeclarations) { // inserir declarações de log dentro dos métodos
     MethodInvocation methodInvocation = ast.newMethodInvocation();
     
     methodInvocation.setName(ast.newSimpleName("logIn"));
     
     methodInvocation.setName(ast.newSimpleName("log"));
     
     StringLiteral literal = ast.newStringLiteral();
     literal.setLiteralValue("Level.INFO,\"passou no\""+methodDeclaration.getName().getFullyQualifiedName());
     methodInvocation.arguments().add(literal);
     
     System.out.println(methodDeclaration.getName().getFullyQualifiedName());

 }
 return true;
 }
 
 
 public Logger logIn() {
     
     Logger log = Logger.getLogger("Log");
     FileHandler filetxt = null;

         try {
            filetxt = new FileHandler("loggin.txt");
        } catch (SecurityException | IOException e) {
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
     
     CoverageTransformer cc = new CoverageTransformer();
         cc.insertLog();
    
     
//     CoverageTransformer cc = new CoverageTransformer();
//     try {
//         cc.insertLog();
//    } catch (SecurityException e) {
//       System.out.println("Erro de assinatura da biblioteca..."+ e.getMessage());
//    }
     
 }




    
}
