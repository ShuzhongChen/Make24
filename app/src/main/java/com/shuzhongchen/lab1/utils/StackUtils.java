package com.shuzhongchen.lab1.utils;

import java.util.Stack;

/**
 * Created by shuzhongchen on 3/3/18.
 */

public class StackUtils {
    public static String addToStack(Stack<Character> stack, Character c) {
        stack.add(c);
        return makeString(stack);
    }

    public static String removeFromStack(Stack<Character> stack) {
        if (!stack.empty()) {
            stack.pop();
        }
        return makeString(stack);
    }

    public static String makeString(Stack<Character> stack) {
        StringBuilder sb = new StringBuilder();
        Stack<Character> tmpStack = (Stack<Character>)stack.clone();
        while (!tmpStack.empty()) {
            sb.insert(0, tmpStack.pop());
        }

        return sb.toString();
    }
}
