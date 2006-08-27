package edu.uci.ece.doc.rtsjcomponents;

public interface Context
{
    Object getComponent(String name);
    
    char    getLocalChar(String name);
    int     getLocalInt(String name);
    short   getLocalShort(String name);
    long    getLocalLong(String name);
    double  getLocalDouble(String name);
    boolean getLocalBoolean(String name);
    
    char    getGlobalChar(String name);
    int     getGlobalInt(String name);
    short   getGlobalShort(String name);
    long    getGlobalLong(String name);
    double  getGlobalDouble(String name);
    boolean getGlobalBoolean(String name);    
}
