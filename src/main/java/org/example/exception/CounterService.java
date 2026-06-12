package org.example.exception;

public class CounterService {
    public double countSalary(int gross) {
        if (gross < 0) {
            throw new LessThenZeroException("Salary is less then zero");
        } else if (gross > 100) {
            throw new BiggerThenHundredException("Salary is bigger then 100");
        }

        return gross * 1.2;
    }

    public boolean isValidSalary(int gross) {
        return gross >= 0 && gross <= 100;
    }
}
