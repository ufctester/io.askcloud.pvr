package io.askcloud.pvr.imdb.model;

public interface IStatusMessage {

    public ImdbStatusMessage getStatusMessage();

    public void setStatusMessage(ImdbStatusMessage statusMessage);

    public void setStatusMessageMsg(String message);

    public void setStatusMessage(String message, Throwable error);

    public boolean isError();
}
