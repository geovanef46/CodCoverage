/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codcoverage;



import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;



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
    String pathSource = "src/codcoverage/source/src/example/";
    File pathTarget = new File("src/codcoverage/target/");
    File pathTargetold = pathTarget;
    File pathCopy;
    Collection<File> files;
    String testSuite = "src/codcoverage/source/src/test/TriangleTest.java";
    static Logger log = null;
    static FileHandler filetxt = null;
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


public void copyFile(String path, String name) {
    pathTarget = pathTargetold;
	pathTarget = new File(pathTarget.getPath()+"/"+name);
	pathCopy = new File(path);
    try {
      FileUtils.copyFile(pathCopy, pathTarget);
      System.out.println("Arquivo copiado :"+ pathTarget);
    } catch (IOException e) {
      
    }
     
}

public void saveFile(File path,String data) {
    try {
        FileUtils.write(path, data);
        System.out.println("arquivo salvo em "+path.getPath());
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}


public boolean analise() {
    
    ElementsSuite(testSuite);
    return true;
}


public boolean analise(ASTNode node) {
    if(files == null) {
    String source = "src/codcoverage/source/src/example/";
    String[] extensions = {"java"};
    File directory = new File(source);
   
    files = FileUtils.listFiles(directory,extensions, false);
   // pathSource = pathTarget.toString();
    for(File file : files) {
        
        copyFile(file.toString(), file.getName());
 
     System.out.println("Obtido: "+pathCopy.getName());
     pathCopy = pathTarget;
     insertLog(pathCopy.toString(),node);
 }
    }else {
        for(File file : files) {
            System.out.println("Obtido: "+pathCopy.getName());
            pathCopy = pathTarget;
            insertLog(pathCopy.toString(),node);
        }
    }

    return true;
}

 
 /**
  * Inserir um trecho de código para log nos métodos especificos 
  * tratados pelo teste. 
 * @return 
 * 
 */
public boolean insertLog(String source, ASTNode node) {
//     Document document = new Document(source);
     
//     Logger log = logIn(); //criando arquivo de log
     
     ASTParser parser = ASTParser.newParser(AST.JLS3); 
     parser.setSource(insertFile(source).toCharArray()); //adicionar o source ao parser
     parser.setCompilerOptions(JavaCore.getOptions()); 
     parser.setKind(ASTParser.K_COMPILATION_UNIT); 

 CompilationUnit unit = (CompilationUnit) parser.createAST(null);//criar AST do tipo CompilationUnit (a raiz contem o arquivo completo) 
 AST ast = unit.getAST(); 

 PackageDeclaration pd = ast.newPackageDeclaration();
 pd.setName(ast.newQualifiedName(ast.newSimpleName("codcoverage"),ast.newSimpleName("target")));
 
 ASTRewrite rewriter = ASTRewrite.create(ast);
 PackageDeclaration pd2 = unit.getPackage();
 pd2.setName(pd.getName());
 ListRewrite listRewrite = rewriter.getListRewrite(pd2, unit.TYPES_PROPERTY);
 TextEdit edits = rewriter.rewriteAST();
 
 // apply the text edits to the compilation unit
 Document document = new Document(source);

 edits.apply(document);

 
 
 List<ASTNode> astnodes = ASTnodeFinder.getASTnodes(unit); 
 for (ASTNode astnode : astnodes) {
     
     if (astnode instanceof MethodDeclaration && node instanceof MethodInvocation) {
         if (((MethodDeclaration)astnode).getName().getIdentifier().equals(((MethodInvocation)node).getName().getIdentifier())) {
           
             System.out.println("test "+node +"source:"+astnode);
             ast = insertLog(ast, unit, astnode);
             return true;
        }
     }
         //if(((SimpleName)node).toString().equals(methodDeclaration.getName())) {

         //}falta implementar o nível de profundidade que deseja comparar metodos ou invocação de metodos ou expressoes/palavras



//         List<MethodInvocation> methodInvocations = MethodDeclarationFinder.getInvocations(methodDeclaration);
//         for (MethodInvocation methodInvocation : methodInvocations) {
//     
//             if(name == methodInvocation.getName().getIdentifier()) {
//                 insertLog(ast, unit, methodInvocation);
//                 return true;
//
//              }
          //log.log(Level.INFO,"#"+new org.eclipse.jface.text.Document(this.insertFile()).getNumberOfLines());
//          
//          methodInvocation.setExpression(ast.newSimpleName("logIn")); 
//         
//          methodInvocation.setName(ast.newSimpleName("log"));
//          
//          StringLiteral literal = ast.newStringLiteral();
//          literal.setLiteralValue("Level.INFO,\"OK\"");
//          methodInvocation.arguments().add(literal);
          
        
     

    }
 return true;
 }



public AST insertLog(AST ast, CompilationUnit unit, ASTNode node) {
    MethodInvocation methodInvocation = ast.newMethodInvocation();
    MethodInvocation methodInvocation2 = ast.newMethodInvocation();//logIn().info(msg);
    QualifiedName qName =
            ast.newQualifiedName(ast.newSimpleName("codcoverage"), ast.newSimpleName("CoverageTransformer"));

        methodInvocation.setExpression(qName);
        methodInvocation.setName(ast.newSimpleName("logIn"));
        methodInvocation2.setName(ast.newSimpleName("info"));
        methodInvocation2.setExpression(methodInvocation);
        
        StringLiteral sl = ast.newStringLiteral();
                sl.setLiteralValue("*"+unit.getLineNumber(node.getStartPosition())+"*");
        methodInvocation2.arguments().add(sl);
        
        
        // Append the statement
        if(node instanceof MethodDeclaration) {
        ((MethodDeclaration) node).getBody().statements().add(0, ast.newExpressionStatement(methodInvocation2));
        }else {
        node.setProperty(methodInvocation2.getName().getIdentifier(), ast.newExpressionStatement(methodInvocation2));
//       System.out.println(unit.getRoot().toString());
        }
       saveFile(pathCopy,unit.getRoot().toString());
    System.out.println("Salvo no arquivo");
    return ast;
}
 
 

public boolean ElementsSuite(String source) {
  
  ASTParser parser = ASTParser.newParser(AST.JLS3); 
  parser.setSource(insertFile(source).toCharArray()); //adicionar o source ao parser
  parser.setCompilerOptions(JavaCore.getOptions()); 
  parser.setKind(ASTParser.K_COMPILATION_UNIT); 

CompilationUnit unit = (CompilationUnit) parser.createAST(null);//criar AST do tipo CompilationUnit (a raiz contem o arquivo completo) 
AST ast = unit.getAST(); 

List<ASTNode> ASTnodes = ASTnodeFinder.getASTnodes(unit); 
for (ASTNode astnode : ASTnodes) {  
    
        //System.out.println(astnode.toString());
        analise(astnode); //////////////   
    }
return true;
}





public static Logger logIn() {
     
         log = Logger.getLogger("Log");

         try {
             if(filetxt == null) {
                 filetxt = new FileHandler("loggin.txt");
             }
             
        } catch (IOException e) {
           
        }
         filetxt.setFormatter(new SimpleFormatter());

     log.addHandler(filetxt);
     
     return log;
    
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
