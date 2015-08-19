package org.ocram.collections.objects;


public class ComparableInterceptor implements Comparable<ComparableInterceptor> {

    private Integer priority;
    private Object interceptor;

    public ComparableInterceptor(Integer priority, Object interceptor) {
        this.priority = priority;
        this.interceptor = interceptor;
    }

    public Integer getPriority() {
        return priority;
    }

    public Object getInterceptor() {
        return interceptor;
    }

    @Override
    public int compareTo( ComparableInterceptor other ) {
        return this.getPriority().compareTo(other.getPriority());
    }
}
