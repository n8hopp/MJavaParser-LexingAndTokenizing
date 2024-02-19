package parse;
import java.util.List;

import errorMsg.*;
import syntaxtree.*;

public class MJGrammar
    implements wrangLR.runtime.MessageObject,
    wrangLR.runtime.FilePosObject
{

    // tells whether to use the filtered grammar
    public static final boolean FILTER_GRAMMAR = true;

    // constructor
    // @param em error-message object
    public MJGrammar(ErrorMsg em)
    {
        errorMsg = em;
        topObject = null;
    }

    // error message object
    private ErrorMsg errorMsg;

    // object to be returned by the parser
    private Program topObject;

    // method for printing error message and marking that
    // compilation has failed
    // @param pos the file position for error message
    // @param msg the error message
    public void error(int pos, String msg)
    {
        errorMsg.error(pos, msg);
    }

    // method for printing warning message
    // @param pos file position for warning message
    // @param msg the warning message
    public void warning(int pos, String msg)
    {
        errorMsg.warning(pos, msg);
    }

    // method for converting file position to line/char position
    // @param pos the file position
    // @return the string that denotes the file position
    public String filePosString(int pos)
    {
        return errorMsg.lineAndChar(pos);
    }

    // method that registers a newline
    // @param pos the file position of the newline character
    public void registerNewline(int pos)
    {
        errorMsg.newline(pos-1);
    }

    // returns the object produced by the parse
    // @return the top-level object produced by the parser
    public Program parseResult()
    {
        return topObject;
    }

    //===============================================================
    // start symbol
    //===============================================================

    //: <start> ::= ws* <program> =>
    public void topLevel(Program obj)
    {
        topObject = obj;
    }

    //================================================================
    // top-level constructs
    //================================================================

    //: <program> ::= # <class decl>+ =>
    public Program createProgram(int pos, List<ClassDecl> vec)
    {
        return new Program(pos, new ClassDeclList(vec));
    }

    //: <class decl> ::= `class # ID `{ <decl in class>* `} =>
    public ClassDecl createClassDecl(int pos, String name, List<Decl> vec)
    {
        return new ClassDecl(pos, name, "Object", new DeclList(vec));
    }

    //: <class decl> ::= `class # ID `extends ID `{ <decl in class>* `} =>
    public ClassDecl createExtensionClassDecl(int pos, String name, String xtnds, List<Decl> vec)
    {
        return new ClassDecl(pos, name, xtnds, new DeclList(vec));
    }

    //: <decl in class> ::= <method decl> => pass
    //: <decl in class> ::= <inst var decl> => pass
    //: <decl in class> ::= <constr decl> => pass

    // IMPLEMENTED THIS IN CLASS 2/15 !!!
    //: <constr decl> ::= `public `void # ID `( <formal-list> `) `{ <stmt decl>* `} =>
    public Decl createConstructorDeclVoid(int pos, String name, List<VarDecl> formalList, List<Statement> stmts)
    {
        return new MethodDeclVoid(pos, name, new VarDeclList(formalList), new StatementList(stmts));
    }

    //: <constr decl> ::= `public # <type> ID `( <formal-list> `) `{ <stmt decl>* `return <exp> `; `} =>
    public Decl createConstructorDeclType(int pos, Type type, String name, List<VarDecl> formalList, List<Statement> stmts, Exp retExp)
    {
        return new MethodDeclNonVoid(pos, type, name, new VarDeclList(formalList),
                                new StatementList(stmts), retExp);
    }

    //: <formal-list> ::= <formal> # <formal-comma>* =>
    public List<VarDecl> formalList(VarDecl single, int pos, List<VarDecl> formalCommas)
    {
        formalCommas.add(0, single);
        return formalCommas;
    }
    //: <formal-comma> ::= # `, <type> ID =>
    public VarDecl createFormalDeclComma(int pos, Type type, String name)
    {
        return new FormalDecl(pos, type, name);
    }
    //: <formal> ::= # !`, <type> ID =>
    public VarDecl createFormalDeclFirst(int pos, Type type, String name)
    {
        return new FormalDecl(pos, type, name);
    }

    // <exp1> ::= `new ID `( <exp-list> `)
    ////////////

    //: <method decl> ::= `public # <type> ID `( `) `{ <stmt decl>* `return <exp> `; `} =>
    public Decl createMethodDeclType(int pos, Type type, String name, List<Statement> stmts, Exp retExp)
    {
        return new MethodDeclNonVoid(pos, type, name, new VarDeclList(new VarDeclList()),
                                new StatementList(stmts), retExp);
    }
    //: <method decl> ::= `public `void # ID `( `) `{ <stmt decl>* `} =>
    public Decl createMethodDeclVoid(int pos, String name, List<Statement> stmts)
    {
        return new MethodDeclVoid(pos, name, new VarDeclList(new VarDeclList()),
                                  new StatementList(stmts));
    }

    //: <inst var decl> ::= # <type> ID `; =>
    public Decl createInstVarDecl(int pos, Type type, String name){
        return new InstVarDecl(pos, type, name);
    }

    //: <type> ::= # `int =>
    public Type intType(int pos)
    {
        return new IntegerType(pos);
    }
    //: <type> ::= # `boolean =>
    public Type booleanType(int pos)
    {
        return new BooleanType(pos);
    }
    //: <type> ::= # ID =>
    public Type identifierType(int pos, String name)
    {
        return new IdentifierType(pos, name);
    }
    //: <type> ::= # <type> <empty bracket pair> =>
    public Type newArrayType(int pos, Type t, Object dummy)
    {
        return new ArrayType(pos, t);
    }

    //: <empty bracket pair> ::= `[ `] => null

    //================================================================
    // statement-level constructs
    //================================================================

    //: <stmt> ::= <assign> `; => pass

    //: <stmt> ::= # `{ <stmt decl>* `} =>
    public Statement newBlock(int pos, List<Statement> sl)
    {
        return new Block(pos, new StatementList(sl));
    }

    //: <stmt decl> ::= <stmt> => pass
    //: <stmt decl> ::= <local var decl> => pass

    //: <stmt> ::= `while # `( <exp> `) <stmt> =>
    public Statement newWhile(int pos, Exp e1, Statement body) {
        return new While(pos, e1, body);
    }


    // <stmt> ::= `for <for1> <for2> <for3> <stmt> =>

    //: <stmt> ::= `for # `( <forinit> <forloop> <forinc> `) <stmt> =>
    public Statement newFor(int pos, Statement decl, Exp loop, Statement assign, Statement body)
    {
        // {}
        StatementList enclosing = new StatementList();

        // {int i=0;}
        enclosing.add(decl);

        While whl = new While(pos, loop, body);

        // {int i=0; while(i<=10){{body1(); body2();} i++}}
        enclosing.add(whl);
        enclosing.add(assign);

        Block forBlock = new Block(pos, enclosing);
        return forBlock;
    }

    // forinit:
    // includes ';' already, implements statement
    //: <forinit> ::= <local var decl> => pass

    //only need to add ';' - assign implements statement
    //: <forinit> ::= <assign> `; => pass

    // call stmt = callExp + ';'
    //: <forinit> ::= <call stmt> => pass

    // already statement - empty block
    //: <forinit> ::= <semicolon> => pass

    // forloop:
    // not statement - one of only cases where something is followed by semicolon and it's not statement
    //: <forloop> ::= <exp> `; => pass
    //: <forloop> ::= # `; =>
    public Exp emptyForExp(int pos){
        return new True(pos);
    }

    // forincrement:
    // don't want semicolon - no semicolon at end
    //: <forinc> ::= <assign> => pass

    // callexp turned into callstatement w/o semicolon - gross
    //: <forinc> ::= # <call exp> =>
    public Statement forIncrementStatement(int pos, Exp e1)
    {
        return new CallStatement(pos, (Call)e1);
    }
    // nothing here means empty statement - block
    //: <forinc> ::= # =>
    public Statement forIncrementEmpty(int pos)
    {
        return new Block(pos, new StatementList());
    }

    //: <stmt> ::= `if # `( <exp> `) <stmt> !`else =>
    public Statement newIf(int pos, Exp e1, Statement body) {
        return new If(pos, e1, body, new Block(pos, new StatementList()));
    }

    //: <stmt> ::= `if # `( <exp> `) <stmt> `else <stmt> =>
    public Statement newIfElse(int pos, Exp e1, Statement body, Statement elseBody) {
        return new If(pos, e1, body, elseBody);
    }

    //: <stmt> ::= `switch # `( <exp> `) `{ <switchBody>* `} =>
    public Statement newSwitch(int pos, Exp e1, List<Statement> body) {
        return new Switch(pos, e1, new StatementList(body));
    }

    //: <switchBody> ::= <stmt decl> => pass
    //: <switchBody> ::= `case # <exp> `: =>
    public Statement switchCase(int pos, Exp e1)
    {
        return new Case(pos, e1);
    }

    //: <switchBody> ::= `default # `: =>
    public Statement defaultCase(int pos)
    {
        return new Default(pos);
    }

    //: <stmt> ::= <call stmt> => pass
    //: <call stmt> ::= # <call exp> `; =>
    public Statement callStatement(int pos, Exp eCall)
    {
        return new CallStatement(pos, (Call)eCall);
    }

    //: <stmt> ::= `break # `; =>
    public Statement newBreak(int pos)
    {
        return new Break(pos);
    }

    //: <stmt> ::= <semicolon> => pass


    //: <semicolon> ::= # `; =>
    public Statement semicolon(int pos)
    {
        return new Block(pos, new StatementList());
    }

    //: <assign> ::= <exp> # `= <exp> =>
    public Statement assign(Exp lhs, int pos, Exp rhs)
    {
        return new Assign(pos, lhs, rhs);
    }

    //: <assign> ::= # ID `++ =>
    public Statement postIncrement(int pos, String name)
    {
        return new Assign(pos, new IdentifierExp(pos, name),
                                    new Plus(pos,
                                        new IdentifierExp(pos, name),
                                        new IntegerLiteral(pos, 1)));
    }

    //: <assign> ::= # ID `-- =>
    public Statement postDecrement(int pos, String name)
    {
        return new Assign(pos, new IdentifierExp(pos, name),
                new Minus(pos,
                        new IdentifierExp(pos, name),
                        new IntegerLiteral(pos, 1)));
    }

    // not really sure how to make these differ in the AST - just need to mess with precedence somehow? idk
    //: <assign> ::= # `++ ID  =>
    public Statement preIncrement(int pos, String name)
    {
        return new Assign(pos, new IdentifierExp(pos, name),
                new Plus(pos,
                        new IdentifierExp(pos, name),
                        new IntegerLiteral(pos, 1)));
    }

    //: <assign> ::= # `-- ID =>
    public Statement preDecrement(int pos, String name)
    {
        return new Assign(pos, new IdentifierExp(pos, name),
                new Minus(pos,
                        new IdentifierExp(pos, name),
                        new IntegerLiteral(pos, 1)));
    }

    //: <local var decl> ::= <type> # ID `= <exp> `; =>
    public Statement localVarDecl(Type t, int pos, String name, Exp init)
    {
        return new LocalDeclStatement(500, new LocalVarDecl(pos, t, name, init));
    }


    //================================================================
    // expressions
    //================================================================

    //: <exp> ::= <exp8> => pass

    // these precedence levels have been filled in - nate, 2/9 1:00pm


    // ----------------------- exp8 ----------------------- //
    //: <exp8> ::= <exp8> # `|| <exp7> =>
    public Exp newOr(Exp e1, int pos, Exp e2)
    {
        return new Or(pos, e1, e2);
    }
    //: <exp8> ::= <exp7> => pass


    // ----------------------- exp7 ----------------------- //
    //: <exp7> ::= <exp7> # `&& <exp6> =>
    public Exp newAnd(Exp e1, int pos, Exp e2)
    {
        return new And(pos, e1, e2);
    }
    //: <exp7> ::= <exp6> => pass


    // ----------------------- exp6 ----------------------- //
    //: <exp6> ::= <exp6> # `== <exp5> =>
    public Exp newEquals(Exp e1, int pos, Exp e2)
    {
        return new Equals(pos, e1, e2);
    }

    //: <exp6> ::= <exp6> # `!= <exp5> =>
    public Exp newNotEquals(Exp e1, int pos, Exp e2)
    {
        return new Not(pos, new Equals(pos, e1, e2));
    }
    //: <exp6> ::= <exp5> => pass


    // ----------------------- exp5 ----------------------- //
    //: <exp5> ::= <compare exp> => pass
    //: <exp5> ::= <instof exp> => pass

    //: <compare exp> ::= <compare exp> # `< <exp4> =>
    public Exp newLt(Exp e1, int pos, Exp e2)
    {
        return new LessThan(pos, e1, e2);
    }

    //: <compare exp> ::= <compare exp> # `<= <exp4> =>
    public Exp newLeq(Exp e1, int pos, Exp e2)
    {
        return new Not(pos, new GreaterThan(pos, e1, e2));
    }

    //: <compare exp> ::= <compare exp> # `> <exp4> =>
    public Exp newGt(Exp e1, int pos, Exp e2)
    {
        return new GreaterThan(pos, e1, e2);
    }

    //: <compare exp> ::= <compare exp> # `>= <exp4> =>
    public Exp newGeq(Exp e1, int pos, Exp e2)
    {
        return new Not(pos, new LessThan(pos, e1, e2));
    }
    //: <compare exp> ::= <exp4> => pass

    //: <instof exp> ::= <instof exp> # `instanceof `( <type> `) =>
    public Exp newInstanceOf(Exp e1, int pos, Type t)
    {
        return new InstanceOf(pos, e1, t);
    }
    //: <instof exp> ::= <exp4> # `instanceof `( <type> `) => Exp newInstanceOf(Exp, int, Type)
    // ----------------------- end exp5 ----------------------- //


    // these remaining precedence levels have been filled in to some extent,
    // but most or all of them have need to be expanded


    // ----------------------- exp4 ----------------------- //
    //: <exp4> ::= <exp4> # `+ <exp3> =>
    public Exp newPlus(Exp e1, int pos, Exp e2)
    {
        return new Plus(pos, e1, e2);
    }

    //: <exp4> ::= <exp4> # `- <exp3> =>
    public Exp newMinus(Exp e1, int pos, Exp e2)
    {
        return new Minus(pos, e1, e2);
    }
    //: <exp4> ::= <exp3> => pass


    // ----------------------- exp3 ----------------------- //
    //: <exp3> ::= <exp3> # `* <exp2> =>
    public Exp newTimes(Exp e1, int pos, Exp e2)
    {
        return new Times(pos, e1, e2);
    }

    //: <exp3> ::= <exp3> # `/ <exp2> =>
    public Exp newDiv(Exp e1, int pos, Exp e2) {
        return new Divide(pos, e1, e2);
    }

    //: <exp3> ::= <exp3> # `% <exp2> =>
    public Exp newMod(Exp e1, int pos, Exp e2) {
        return new Remainder(pos, e1, e2);
    }
    //: <exp3> ::= <exp2> => pass


    // ----------------------- exp2 ----------------------- //
    //: <exp2> ::= <cast exp> => pass
    //: <exp2> ::= <unary exp> => pass

    //: <cast exp> ::= # `( <type> `) <cast exp> =>
    public Exp newCast(int pos, Type t, Exp e)
    {
        return new Cast(pos, t, e);
    }
    //: <cast exp> ::= # `( <type> `) <exp1> => Exp newCast(int, Type, Exp)

    //: <unary exp> ::= # `- <unary exp> =>
    public Exp newUnaryMinus(int pos, Exp e)
    {
        return new Minus(pos, new IntegerLiteral(pos, 0), e);
    }

    //: <unary exp> ::= # `+ <unary exp> =>
    public Exp newUnaryPlus(int pos, Exp e)
    {
        return new Plus(pos, new IntegerLiteral(pos, 0), e);
    }
    //: <unary exp> ::= <exp1> => pass

    //: <unary exp> ::= # `! <exp1> =>
    public Exp logicalNot(int pos, Exp e) { return new Not(pos, e); }

    // ----------------------- exp1 ----------------------- //
    //: <exp1> ::= # !`( ID !`) =>
    public Exp newIdentifierExp(int pos, String name)
    {
        return new IdentifierExp(pos, name);
    }

    //: <exp1> ::= # INTLIT =>
    public Exp newIntegerLiteral(int pos, int n)
    {
        return new IntegerLiteral(pos, n);
    }

    //: <exp1> ::= # CHARLIT =>
    public Exp newCharLiteral(int pos, int c)
    {
        return new IntegerLiteral(pos, c);
    }

    //: <exp1> ::= # STRINGLIT =>
    public Exp newStringLiteral(int pos, String str)
    {
        return new StringLiteral(pos, str);
    }

    //: <exp1> ::= # `true =>
    public Exp newTrue(int pos)
    {
        return new True(pos);
    }

    //: <exp1> ::= # `false =>
    public Exp newFalse(int pos)
    {
        return new False(pos);
    }

    //: <exp1> ::= # `null =>
    public Exp newNull(int pos)
    {
        return new Null(pos);
    }

    //: <exp1> ::= <exp1> !<empty bracket pair> # `[ <exp> `] =>
    public Exp newArrayLookup(Exp e1, int pos, Exp e2)
    {
        return new ArrayLookup(pos, e1, e2);
    }

    // todo: huh?
    //: <exp1> ::= `( <exp> `) => pass

    //: <exp1> ::= # `this =>
    public Exp newThis(int pos)
    {
        return new This(pos);
    }

    //: <exp1> ::= # <exp1> `. ID =>
    public Exp newInstVarAccess(int pos, Exp e1, String varname)
    {
        return new InstVarAccess(pos, e1, varname);
    }

    //: <exp1> ::= # `new <type> !<empty bracket pair> `[ <exp> `] <empty bracket pair>* =>
    public Exp newArray(int pos, Type type, Exp e1, List<Object> dummies)
    {
        return new NewArray(pos, type, e1);
    }

    //: <exp1> ::= # `new ID `( `) =>
    public Exp newObject(int pos, String id)
    {
        //TODO: IMPLEMENT THIS
        return new NewObject(pos, new IdentifierType(pos, id));
    }

    //: <exp1> ::= <call exp> => pass

    //: <call exp> ::= ID # `( <exp list> `) =>
    public Exp newSelfFuncCall(String methodName, int pos, List<Exp> expList)
    {
        return new Call(pos, new This(pos), methodName, new ExpList(expList));
    }

    //: <call exp> ::= <exp1> `. ID # `( <exp list> `) =>
    public Exp newObjectFuncCall(Exp obj, String methodName, int pos, List<Exp> expList)
    {
        return new Call(pos, obj, methodName, new ExpList(expList));
    }

    //: <call exp> ::= `super # `. ID # `( <exp list> `) =>
    public Exp newSuperFuncCall(int superPos, String methodName, int pos, List<Exp> expList)
    {
        return new Call(pos, new Super(superPos), methodName, new ExpList(expList));
    }

    //: <exp list> ::= <exp> # <expcomma>* =>
    public List<Exp> expList(Exp start, int pos, List<Exp> expComma)
    {
        expComma.add(0, start);
        return expComma;
    }

    //: <expcomma> ::= `, <exp> => pass

    //making it able to be nothing here - since every usage of it has it ? as well
    //: <exp list> ::= =>
    public List<Exp> emptyExpList()
    {
        return null;
    }

    //================================================================
    // Lexical grammar for filtered language begins here: DO NOT
    // MODIFY ANYTHING BELOW THIS, UNLESS YOU REPLACE IT WITH YOUR
    // ENTIRE LEXICAL GRAMMAR, and set the constant FILTER_GRAMMAR
    // (defined near the top of this file) to false.
    //================================================================

    //: letter ::= {"a".."z" "A".."Z"} => pass
    //: letter128 ::= {225..250 193..218} =>
    public char sub128(char orig)
    {
        return (char)(orig-128);
    }
    //: digit ::= {"0".."9"} => pass
    //: digit128 ::= {176..185} => char sub128(char)
    //: any ::= {0..127} => pass
    //: any128 ::= {128..255} => char sub128(char)
    //: ws ::= " "
    //: ws ::= {10} registerNewline
    //: registerNewline ::= # => void registerNewline(int)
    //: `boolean ::= "#bo" ws*
    //: `class ::= "#cl" ws*
    //: `extends ::= "#ex" ws*
    //: `void ::= "#vo" ws*
    //: `int ::= "#it" ws*
    //: `while ::= "#wh" ws*
    //: `if ::= '#+' ws*
    //: `else ::= "#el" ws*
    //: `for ::= "#fo" ws*
    //: `break ::= "#br" ws*
    //: `this ::= "#th" ws*
    //: `false ::= '#fa' ws*
    //: `true ::= "#tr" ws*
    //: `super ::= "#su" ws*
    //: `null ::= "#nu" ws*
    //: `return ::= "#re" ws*
    //: `instanceof ::= "#in" ws*
    //: `new ::= "#ne" ws*
    //: `case ::= "#ce" ws*
    //: `default ::= "#de" ws*
    //: `do ::= "#-" ws*
    //: `public ::= "#pu" ws*
    //: `switch ::= "#sw" ws*

    //: `! ::=  "!" ws* => void
    //: `!= ::=  "@!" ws* => void
    //: `% ::= "%" ws* => void
    //: `&& ::= "@&" ws* => void
    //: `* ::= "*" ws* => void
    //: `( ::= "(" ws* => void
    //: `) ::= ")" ws* => void
    //: `{ ::= "{" ws* => void
    //: `} ::= "}" ws* => void
    //: `- ::= "-" ws* => void
    //: `+ ::= "+" ws* => void
    //: `= ::= "=" ws* => void
    //: `== ::= "@=" ws* => void
    //: `[ ::= "[" ws* => void
    //: `] ::= "]" ws* => void
    //: `|| ::= "@|" ws* => void
    //: `< ::= "<" ws* => void
    //: `<= ::= "@<" ws* => void
    //: `, ::= "," ws* => void
    //: `> ::= ">"  !'=' ws* => void
    //: `>= ::= "@>" ws* => void
    //: `: ::= ":" ws* => void
    //: `. ::= "." ws* => void
    //: `; ::= ";" ws* => void
    //: `++ ::= "@+" ws* => void
    //: `-- ::= "@-" ws* => void
    //: `/ ::= "/" ws* => void


    //: ID ::= letter128 ws* => text
    //: ID ::= letter idChar* idChar128 ws* => text

    //: INTLIT ::= {"1".."9"} digit* digit128 ws* =>
    public int convertToInt(char c, List<Character> mid, char last)
    {
        return Integer.parseInt(""+c+mid+last);
    }
    //: INTLIT ::= digit128 ws* =>
    public int convertToInt(char c)
    {
        return Integer.parseInt(""+c);
    }
    //: INTLIT ::= "0" hexDigit* hexDigit128 ws* =>
    public int convert16ToInt(char c, List<Character> mid, char last)
    {
        return Integer.parseInt(""+c+mid+last, 16);
    }
    //: STRINGLIT ::= '@"' ws* =>
    public String emptyString(char x, char xx)
    {
        return "";
    }
    //: STRINGLIT ::= '"' any* any128 ws* =>
    public String string(char x, List<Character> mid, char last)
    {
        return ""+mid+last;
    }
    //: CHARLIT ::= "'" any ws* =>
    public int charVal(char x, char val)
    {
        return val;
    }

    //: idChar ::= letter => pass
    //: idChar ::= digit => pass
    //: idChar ::= "_" => pass
    //: idChar128 ::= letter128 => pass
    //: idChar128 ::= digit128 => pass
    //: idChar128 ::= {223} =>
    public char underscore(char x)
    {
        return '_';
    }
    //: hexDigit ::= {"0".."9" "A".."Z" "a".."z"} => pass
    //: hexDigit128 ::= {176..185 225..230 193..198} => char sub128(char)

}
