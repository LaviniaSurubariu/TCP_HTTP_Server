package eu.deic.ie;

@FunctionalInterface
interface GreetingService {
    void say(String message);
}
@FunctionalInterface
interface MathOperation<T1, T2, T3> {
    T3 operation(T1 a, T2 b);
}
class MathOpImpl<T1,T2, T3> {
    public T3 operate(T1 a, T2 b, MathOperation mathOperation) {
        return (T3) mathOperation.operation(a, b);
    }
}

public class ProgMainFILambda {
    public static void main(String[] args) {
        MathOpImpl tester = new MathOpImpl();

        MathOperation<Double, Float, Double> addition = (x, y) -> x+y;
//        MathOperation substraction = (a, b) -> a - b;
//        MathOperation multiplication = (int a, int b) -> {return a * b;};

        System.out.println("10 + 5 = " + tester.operate(10.0, 5.0f, addition));
//        System.out.println("10 - 5 = " + tester.operate(10, 5, substraction));
//        System.out.println("10 * 5 = " + tester.operate(10, 5, multiplication));

        GreetingService gs1 = (String msg) -> {
            System.out.println("Bonjour " + msg + "!");
        };
        GreetingService gs2 = m -> System.out.println("Hello " + m);

        gs1.say("Jean-Yves");
        gs2.say("Jake");
        gs2.say("Jill");
    }
}
