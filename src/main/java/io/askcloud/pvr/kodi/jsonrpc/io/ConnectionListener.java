package io.askcloud.pvr.kodi.jsonrpc.io;

import io.askcloud.pvr.kodi.jsonrpc.notification.AbstractEvent;

public interface ConnectionListener {

  public void connected();

  public void disconnected();

  public void notificationReceived(AbstractEvent event);

}
