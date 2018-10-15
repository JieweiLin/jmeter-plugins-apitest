package com.ljw.jmeter.functions;

import com.google.common.collect.Lists;
import com.ljw.utils.TestFrame;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;

import java.util.Collection;
import java.util.List;


/**
 * @author linjw
 */
public class TestSpecial extends AbstractFunction {

    private static final List<String> desc = Lists.newLinkedList();
    private static final String KEY = "__testSpecialCharacters";
    private CompoundVariable excelPath;
    private CompoundVariable sheetName;

    static {
        desc.add("excelPath");
        desc.add("sheetName");
    }

    @Override
    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        String excelPath = this.excelPath.execute().trim();
        String sheetName = this.sheetName.execute().trim();
        try {
            TestFrame.testSpecialCharacters(excelPath, sheetName);
        } catch (Exception e){
            return "Special character test failed";
        }
        return "Envoy character test succeeded";
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
