package com.linemetrics.api.returntypes;

/**
 * Created by Klemens on 07.03.2017.
 */
public class DataStreamType {

    //TODO möglicherweise getter und setter machen
    private String input;
    private String output;

    public Class<?> getInput() {
        try {
            return Class.forName("com.linemetrics.api.datatypes."+input);
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void setInput(Class<?> type){
        this.input = type.getName().toString();
    }

    public Class<?> getOutput() {
        try {
            return Class.forName("com.linemetrics.api.datatypes."+output);
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void setOutput(Class<?> type){
        this.output = type.getName().toString();
    }

    @Override
    public String toString(){
        return String.format("Input: %s, Output: %s", this.input, this.output);
    }
}
