import syntaxtree.*;
import visitor.PrettyPrintVisitor;
import java.util.List;
import java.util.*;
import java.io.PrintStream;

public class Practice
{
    public static void main(String[] args)
    {
        // class Main extends Object {
        //     public void main() {
        //          int x = 0;
        //          x = x + 1;
        // }
        ClassDeclList classes = new ClassDeclList();
        DeclList mainDecls = new DeclList();
        VarDeclList formals = new VarDeclList();
        StatementList mainBody = new StatementList();
        mainBody.add(new LocalDeclStatement(0, new LocalVarDecl(0, new IntegerType(0),
                                        "x", new IntegerLiteral(0,0))));

        Exp ltEq = new Or(0,
                        new LessThan(0,
                                new IdentifierExp(0, "x"),
                                new IntegerLiteral(0, 10)),
                        new Equals(0,
                                new IdentifierExp(0, "x"),
                                new IntegerLiteral(0, 10)));

        Statement whBod = new Assign(0,
                                    new IdentifierExp(0, "x"),
                                    new Plus(0,
                                            new IdentifierExp(0, "x"),
                                            new IntegerLiteral(0, 1)
                                    ));

        Statement forBod = new Assign(0,
                new IdentifierExp(0, "i"),
                new Plus(0,
                        new IdentifierExp(0, "i"),
                        new IntegerLiteral(0, 1)
                ));

        Exp ltTen = new LessThan(0,
                                new IdentifierExp(0, "i"),
                                new IntegerLiteral(0, 10));

        Statement varDec = new LocalDeclStatement(0, new LocalVarDecl(0, new IntegerType(0),
            "i", new IntegerLiteral(0,0)));

        Statement forInc = new Assign(0,
                new IdentifierExp(0, "i"),
                new Plus(0,
                        new IdentifierExp(0, "i"),
                        new IntegerLiteral(0, 1)
                ));

        List<Statement> manualList = new ArrayList<Statement>();
        manualList.add(varDec);
        manualList.add(new While(0, ltTen, forInc));
        StatementList forBlock = new StatementList(manualList);

        mainBody.add(new While(0, ltEq, whBod));

        mainBody.add(new Block(0,
                        forBlock));
        mainDecls.add(new MethodDeclVoid(0,"main",formals,mainBody));
        classes.add(new ClassDecl(0,"Main","Object", mainDecls));
        AstNode ast = new Program(0, classes);

        PrettyPrintVisitor ppv = new PrettyPrintVisitor(false, false,
                                        new PrintStream(System.out));

        ast.accept(ppv);
    }
}
