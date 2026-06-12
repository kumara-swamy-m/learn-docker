import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Hi Kumara Swamy M, I am a calculator app ....");

        while (true) {

            System.out.print(
                "Enter any Expression (Example: 1 + 2 or 2 * 5): "
            );

            String input = sc.nextLine();

            if (input.equals("exit")) {
                break;
            }

            String[] parts = input.split(" ");

            if (parts.length != 3) {
                System.out.println("Invalid input. Try again.");
                continue;
            }

            try {
                int left = Integer.parseInt(parts[0]);
                String operator = parts[1];
                int right = Integer.parseInt(parts[2]);

                int result = 0;

                switch (operator) {
                    case "+":
                        result = left + right;
                        break;

                    case "-":
                        result = left - right;
                        break;

                    case "*":
                        result = left * right;
                        break;

                    case "/":
                        result = left / right;
                        break;

                    default:
                        System.out.println("Invalid operator.");
                        continue;
                }

                System.out.println("Result: " + result);

            } catch (Exception e) {
                System.out.println("Invalid input.");
            }
        }

        sc.close();
    }
}
