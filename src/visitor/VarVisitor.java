package visitor;

import syntaxtree.*;
import java.util.*;

public class VarVisitor extends Visitor {
    public HashMap<String, Integer> uses;

    public VarVisitor()
    {
        uses = new HashMap<String, Integer>();
    }

    public Object visit(InstVarAccess e)
    {
        if(uses.containsKey(e.varName)) {
            uses.put(e.varName, uses.get(e.varName) + 1);
        } else {
            uses.put(e.varName, 1);
        }

        e.exp.accept(this);
        return null;
    }
    public Object visit(IdentifierExp e)
    {
        if(uses.containsKey(e.name)) {
            uses.put(e.name, uses.get(e.name) + 1);
        } else {
            uses.put(e.name, 1);
        }
        //e.accept(this);
        return null;
    }

    public Object visit(Plus e)
    {
        if(uses.containsKey("+")) {
            uses.put("+", uses.get("+") + 1);
        } else {
            uses.put("+", 1);
        }

        return null;
    }

    public Object visit(Minus e)
    {
        if(uses.containsKey("-")) {
            uses.put("-", uses.get("-") + 1);
        } else {
            uses.put("-", 1);
        }

        return null;
    }

    public Object visit(Divide e)
    {
        if(uses.containsKey("/")) {
            uses.put("/", uses.get("/") + 1);
        } else {
            uses.put("/", 1);
        }

        return null;
    }

    public Object visit(Times e)
    {
        if(uses.containsKey("*")) {
            uses.put("*", uses.get("*") + 1);
        } else {
            uses.put("*", 1);
        }

        return null;
    }

    public Object visit(Remainder e)
    {
        if(uses.containsKey("%")) {
            uses.put("%", uses.get("%") + 1);
        } else {
            uses.put("%", 1);
        }

        return null;
    }

    public Object visit(Equals e)
    {
        if(uses.containsKey("==")) {
            uses.put("==", uses.get("==") + 1);
        } else {
            uses.put("==", 1);
        }

        return null;
    }

    public Object visit(Assign e)
    {
        if(uses.containsKey("=")) {
            uses.put("=", uses.get("=") + 1);
        } else {
            uses.put("=", 1);
        }

        return null;
    }

    public Object visit(ArrayLength e)
    {
        if(uses.containsKey("")) {
            uses.put("=", uses.get("=") + 1);
        } else {
            uses.put("=", 1);
        }

        return null;
    }
}
