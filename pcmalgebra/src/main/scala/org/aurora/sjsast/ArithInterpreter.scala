package org.aurora.sjsast

class ArithInterpreter {
    private var functions = Map[String, (List[String], Expression)]()

    def runModule(statements: List[Statement]): List[Value] = {
        statements.flatMap {
        case d: Definition =>
            functions += (d.name -> (d.args, d.expr))
            None
        case e: Evaluation =>
            Some(eval(e.expr, Map.empty))
        }
    }

    private def eval(expr: Expression, scope: Map[String, Value]): Value = expr match {
        case NumberLiteral(v) => v

        case BinaryExpression(left, op, right) =>
        val l = eval(left, scope).asDouble.getOrElse(
            throw new Exception(s"Left operand is not a number in expression: $expr")
        )
        val r = eval(right, scope).asDouble.getOrElse(
            throw new Exception(s"Right operand is not a number in expression: $expr")
        )
        
        val result = op match {
            case "+" => l + r
            case "-" => l - r
            case "*" => l * r
            case "/" => l / r
            case "^" => Math.pow(l, r)
            case "%" => l % r
            case _   => throw new Exception(s"Unknown operator: $op")
        }
        DoubleValue(result)

        case FunctionCall(name, providedArgs) =>
        if (scope.contains(name)) {
            scope(name)
        } else if (functions.contains(name)) {
            val (argNames, body) = functions(name)
            val evaluatedArgs = providedArgs.map(eval(_, scope))
            val localScope = argNames.zip(evaluatedArgs).toMap
            eval(body, localScope)
        } else {
            throw new Exception(s"Undefined reference: $name")
        }
        
        case VariableAccess(name) => 
        scope.getOrElse(name, throw new Exception(s"Variable $name not found"))
    }
}