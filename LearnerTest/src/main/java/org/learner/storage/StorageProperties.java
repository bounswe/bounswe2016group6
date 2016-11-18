package org.learner.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    @Value("${staticpath}")
    private String staticPath;
	
    private String location = staticPath;

    public String getLocation() {
        return staticPath;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
