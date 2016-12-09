package io.askcloud.pvr.imdb.wrapper;

import java.util.logging.Logger;

import io.askcloud.pvr.imdb.model.AbstractJsonMapping;


public abstract class AbstractWrapper<E extends AbstractJsonMapping> extends AbstractJsonMapping implements IWrapperResult<E> {

	private static final String CLASS_NAME = AbstractWrapper.class.getName();
	private static final Logger LOG = Logger.getLogger(CLASS_NAME);
    protected E result;

    @Override
    public final E getResult(Class<E> resultClass) {
        // If the wrapper has an error, propogate it to the subclass
        if (isError()) {
            try {
                // If the wrapper is in error, there is no result class, so we need to create one
                result = resultClass.newInstance();
                result.setStatusMessage(getStatusMessage());
            } catch (InstantiationException | IllegalAccessException ex) {
            	LOG.severe("Failed to instantiate class " + resultClass.getSimpleName()+ " exception: " + ex.getMessage());
                result = null;
            }
        }

        // Return the result
        return result;
    }

}
