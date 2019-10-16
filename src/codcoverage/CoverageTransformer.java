/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codcoverage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
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
 * OBS: O método deve buscar na suite de testes as linhas específicas a serem
 * buscadas, até o momento tenta inserir um log em cada método, mas por conta do
 * erro na biblioteca não foi prosseguido para buscar os pontos especificos;
 * 
 * @author jason
 * 
 **/
public class CoverageTransformer {
    String pathSource = "src/codcoverage/source/src/example/";
    File pathTarget = new File("./target/");
    File pathTargetold = pathTarget;
    File pathCopy;
    Collection<File> files;
    String testSuite = "src/codcoverage/source/src/test/TriangleTest.java";
    static Logger log = null;
    static FileHandler filetxt = null;
    int currentLine = 1;
    List<Integer> linhas = new ArrayList<Integer>();
    private static CoverageTransformer instance;

    private CoverageTransformer() {

    }

    public static synchronized CoverageTransformer getInstance() {
        if (instance == null)
            instance = new CoverageTransformer();

        return instance;
    }

    /**
     * realiza a leitura de um arquivo
     *
     * @return String do arquivo
     */
    public String insertFile(String path) throws NullPointerException {
        String source = "";
        try {
            source = FileUtils.readFileToString(new File(path), "UTF-8");
        } catch (Exception e) {
            source = null;
        }

        return source;
    }

    public void copyFile(String path, String name) {
        pathTarget = pathTargetold;
        pathTarget = new File(pathTarget.getPath() + "/" + name);
        pathCopy = new File(path);
        try {
            FileUtils.copyFile(pathCopy, pathTarget);
            System.out.println("Arquivo copiado :" + pathTarget);
        } catch (IOException e) {

        }

    }

    public void saveFile(File path, String data, ASTNode methodInvocation) {
        try {
            String[] list = data.split("\n");
            List<String> lista = new ArrayList<String>();
            for (int i = 0; i < list.length; i++) {
                lista.add(i, list[i]);
            }
            int position = currentLine + linhas.indexOf(currentLine);
            if (currentLine > 0) {
                if (position < lista.size() && position != 1) {
                    lista.add(position, methodInvocation.toString() + ";");
                }
            } else {
                lista.add(currentLine, methodInvocation.toString());
            }

            FileUtils.writeLines(path, lista);

            System.out.println("arquivo salvo em " + path.getPath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void saveFile(File path, String data) {
        try {
            FileUtils.write(path, data);
            System.out.println("arquivo salvo em " + path.getPath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean analise() {
        if (files == null) {
            String source = "src/codcoverage/source/src/example/";
            String[] extensions = { "java" };
            File directory = new File(source);

            files = FileUtils.listFiles(directory, extensions, false);
            // pathSource = pathTarget.toString();
            for (File file : files) {

                copyFile(file.toString(), file.getName());

                System.out.println("Obtido: " + pathCopy.getName());
                pathCopy = pathTarget;
                insertLog(pathCopy.toString());
            }
        } else {
            for (File file : files) {
                System.out.println("Obtido: " + pathCopy.getName());
                pathCopy = pathTarget;
                insertLog(pathCopy.toString());
            }
        }

        return true;
    }

    public boolean insertLog(String source) {
        // Document document = new Document(source);

        // Logger log = logIn(); //criando arquivo de log

        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(insertFile(source).toCharArray()); // adicionar o//
                                                            // source ao parser

        // Map<String, String> options = JavaCore.getOptions();
        // options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_5);
        // options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
        // JavaCore.VERSION_1_5);
        // options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);
        //
        parser.setCompilerOptions(JavaCore.getOptions());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        CompilationUnit unit = (CompilationUnit) parser.createAST(null);
        // criar AST do tipo CompilationUnit (a raiz contem o arquivo completo)

        AST ast = unit.getAST();

        List<ASTNode> astnodes = ASTnodeFinder.getASTnodes(unit);
        for (ASTNode astnode : astnodes) {

            if (currentLine != unit.getLineNumber(astnode.getStartPosition())) {
                if (linhas.indexOf(currentLine) != -1)
                    currentLine = unit.getLineNumber(astnode.getStartPosition());
                linhas.add(currentLine);
            }

        }

        for (Integer linha : linhas) {
            currentLine = linha;
            insertLog(ast, unit);
            
        }
        insertPackage(pathCopy.getPath());

        return true;
    }

    public void insertLog(AST ast, CompilationUnit unit) {
        MethodInvocation methodInvocation = ast.newMethodInvocation();
        MethodInvocation methodInvocation2 = ast.newMethodInvocation();// logIn().info(msg);
        MethodInvocation methodInvocation3 = ast.newMethodInvocation();
        SimpleName qName = ast.newSimpleName("CoverageTransformer");

        methodInvocation.setExpression(qName);
        methodInvocation.setName(ast.newSimpleName("getInstance"));

        methodInvocation3.setName(ast.newSimpleName("logIn"));
        methodInvocation3.setExpression(methodInvocation);
        
        methodInvocation2.setExpression(methodInvocation3);
        methodInvocation2.setName(ast.newSimpleName("info"));
        
        StringLiteral sl = ast.newStringLiteral();
        sl.setLiteralValue("loggin");
        methodInvocation3.arguments().add(sl);
        StringLiteral sl2 = ast.newStringLiteral();
        sl2.setLiteralValue("*" + currentLine + "*");
        methodInvocation2.arguments().add(sl2);
        
        // System.out.println(methodInvocation2.toString());

        // saveFile(pathCopy, unit.getRoot().toString());

        saveFile(pathCopy, insertFile(pathCopy.toString()), methodInvocation2);
        /// System.out.println("Salvo no arquivo");

    }

    public boolean insertPackage(String source) {

        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(insertFile(source).toCharArray()); // adicionar o//
                                                            // source ao parser
        parser.setCompilerOptions(JavaCore.getOptions());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        CompilationUnit unit = (CompilationUnit) parser.createAST(null);
        // criar AST do tipo CompilationUnit (a raiz contem o arquivo completo)

        AST ast = unit.getAST();
        List<ASTNode> astnodes = ASTnodeFinder.getASTnodes(unit);
        for (ASTNode astnode : astnodes) {

            if (astnode instanceof PackageDeclaration) {
                unit.getPackage().delete();
                PackageDeclaration pd = ast.newPackageDeclaration();
                pd.setName(ast.newName("target"));// this.pathTargetold.toString()));
                unit.setPackage(pd);
                
                ast.newImportDeclaration().setName(ast.newQualifiedName(ast.newSimpleName("codcoverage"),ast.newSimpleName("CoverageTransformer")));
                //falta inserir este import
            }
            
            saveFile(pathCopy, unit.getRoot().toString());
            return true;
        }

        return true;
    }

    public static Logger logIn(String logName) {

        log = Logger.getLogger("Log");

        try {
            if (filetxt == null) {
                filetxt = new FileHandler(logName + ".txt");
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

        // CoverageTransformer cc = new CoverageTransformer();
        // try {
        // cc.insertLog();
        // } catch (SecurityException e) {
        // System.out.println("Erro de assinatura da biblioteca..."+
        // e.getMessage());
        // }

    }

}
