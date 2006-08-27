package edu.uci.ece.doc.rtsjcomponents.utils;

public class ObjectHolder
{
    public ObjectHolder(){};
    
    public Object held = null;
    
    public void reset() {
        this.held = null;
    }
    
    // TODO include getter and setter methods 
    // TODO it can be released after invoking the get method 
}
