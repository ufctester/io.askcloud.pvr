package io.askcloud.pvr.imdb.wrapper;

public interface IWrapperResult<E> {

    public void setResult(E result);

    public E getResult();

    public E getResult(Class<E> resultClass);
}
