package com.ljw.jmeter.functions;

import com.google.common.collect.Lists;
import com.ljw.utils.RsaCoderUtil;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

import java.util.Collection;
import java.util.List;

/**
 * @author 林杰炜 linjw
 * @Title: rsa加密
 * @Description: rsa加密
 * @Copyright:
 * @date: 2018/10/15 11:13
 */
public class RsaCoder extends AbstractFunction {

    private static final String KEY = "__rsaCoder";
    private static final List<String> desc = Lists.newLinkedList();

    static {
        desc.add("sourceData");
        desc.add("publicKey");
        desc.add("Name of variable in which to store the result(optional)");
    }

    private CompoundVariable sourceData;
    private CompoundVariable publicKey;
    private CompoundVariable optional;

    @Override
    public String execute(SampleResult sampleResult, Sampler sampler) throws InvalidVariableException {
        String sourceData = this.sourceData.execute().trim();
        String publicKey = this.publicKey.execute().trim();

        try {
            String str = RsaCoderUtil.encryptBase64Encode(sourceData, publicKey);
            if (this.optional != null) {
                JMeterVariables vars = getVariables();
                String var = this.optional.execute().trim();
                if (vars != null && var.length() > 0) {
                    vars.put(var, str);
                }
            }
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void setParameters(Collection<CompoundVariable> collection) throws InvalidVariableException {
        checkParameterCount(collection, 2, 3);
        Object[] values = collection.toArray();
        this.sourceData = (CompoundVariable) values[0];
        this.publicKey = (CompoundVariable) values[1];
        if (values.length > 2) {
            this.optional = (CompoundVariable) values[2];
        } else {
            this.optional = null;
        }
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
