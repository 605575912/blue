package com.show.blue;

import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * Created by apple on 2016/10/26.
 */

public class ShowTestRule implements MethodRule {
    @Override
    public Statement apply(final Statement base, FrameworkMethod method, Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                System.out.print("开始运行\n");
                base.evaluate();
                System.out.print("结束测试");
            }
        };
    }
}
