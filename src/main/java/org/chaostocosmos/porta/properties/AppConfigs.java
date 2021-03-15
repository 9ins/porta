package org.chaostocosmos.porta.properties;

/**
 * AppConfig VO
 */
public class AppConfigs {

    String name;
    String version;
    
    public AppConfigs() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + name + "'" +
            ", version='" + version + "'" +
            "}";
    }
}
