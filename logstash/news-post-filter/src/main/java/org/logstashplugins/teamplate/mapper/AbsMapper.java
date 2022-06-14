package org.logstashplugins.teamplate.mapper;

public abstract class AbsMapper<I, O> {
    public abstract O toOEs(I input);
}
