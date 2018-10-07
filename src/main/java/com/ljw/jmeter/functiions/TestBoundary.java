package com.ljw.jmeter.functiions;

import com.google.common.collect.Lists;
import com.ljw.utils.TestFrame;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author 林杰炜 linjiewei
 * @Title TODO 类描述
 * @Description TODO 详细描述
 * @date 2018/10/8 1:19
 */
public class TestBoundary extends AbstractFunction {

    private static final List<String> desc = Lists.newLinkedList();
    private static final String KEY = "__testBoundary";
    private CompoundVariable excelPath;
    private CompoundVariable sheetName;

    @Override
    public List<String> getArgumentDesc() {
        return desc;
    }

    static {
        desc.add("excelPath");
        desc.add("sheetName");
    }

    @Override
    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        String excelPathTrim = this.excelPath.execute().trim();
        String sheetNameTrim = this.sheetName.execute().trim();
        try {
            TestFrame.testBoundary(excelPathTrim, sheetNameTrim);
        } catch (IOException e) {
            return "边界测试失败";
        }
        return "边界测试成功";
    }

    @Override
    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkParameterCount(collection, 2, 2);
        Object[] values = collection.toArray();
        this.excelPath = (CompoundVariable) values[0];
        this.sheetName = (CompoundVariable) values[1];
    }

    @Override
    public String getReferenceKey() {
        return "__testBoundary";
    }
}
