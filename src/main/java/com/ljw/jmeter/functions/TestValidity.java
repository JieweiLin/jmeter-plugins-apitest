package com.ljw.jmeter.functions;

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
 * @author 林杰炜 linjw
 * @Title: 有效性测验
 * @Description: 有效性测验
 * @Copyright:
 * @date: 2018/10/15 17:27
 */
public class TestValidity extends AbstractFunction {
    private static final List<String> desc = Lists.newLinkedList();
    private static final String KEY = "__testValidity";

    static {
        desc.add("excelPath");
        desc.add("sheetName");
    }

    private CompoundVariable excelPath;
    private CompoundVariable sheetName;

    @Override
    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        String excelPath = this.excelPath.execute().trim();
        String sheetName = this.sheetName.execute().trim();
        try {
            TestFrame.testValidity(excelPath, sheetName);
        } catch (IOException e) {
            e.printStackTrace();
            return "Validity test failed";
        }
        return "Validity verification succeeded";
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
        return KEY;
    }

    @Override
    public List<String> getArgumentDesc() {
        return desc;
    }
}
