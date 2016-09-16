package com.hutgroup.robocode.utils;

public class Tuple<X, Y>
{
    private X fst;
    private Y snd;

    public Tuple(X x, Y y)
    {
	fst = x;
	snd = y;
    }

    public X fst() { return fst; }

    public Y snd() { return snd; }
    
}
