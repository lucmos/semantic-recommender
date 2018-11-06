package io;

import constants.PathConstants;

import java.io.Serializable;

/**
 * Objects that implement this interface may be saved and restored.
 *
 * This layer will be useful to introduce common methods to all serializable objects.
 */
public interface IndexedSerializable extends Serializable {
}
