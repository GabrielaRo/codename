package org.codename.core.chat.layer.impl;

import java.io.Serializable;

/**
 * Your Layer app's configuration values.
 *
 * All values are available in the Layer Dashboard for your project.
 */
public class LayerConfig implements Serializable {
    private final static String LAYER_KEY_ID = "layer:///keys/6de4d26a-5abb-11e5-a3de-15da00000108";
    private final static String LAYER_PROVIDER_ID = "layer:///providers/a093921c-52e8-11e5-bdd4-faf631006ce3";
    private final static String LAYER_RSA_KEY_PATH = "/key.pk8";

    public String getProviderId() {
        return LAYER_PROVIDER_ID;
    }

    public String getLayerKeyId() {
        return LAYER_KEY_ID;
    }

    public String getRsaKeyPath() {
        return LAYER_RSA_KEY_PATH;
    }
}
