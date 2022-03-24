package com.myblog.service.base.util;

/**
 * 二元组
 *
 * @author 李斯特
 * @date 2022/03/24
 */
public class TwoTuple<A, B> {

    private final A first;

    private final B second;

    public TwoTuple(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}
