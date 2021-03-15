package org.chaostocosmos.porta.properties;

/**
 * ManagementConfigs
 * 
 */
public class ManagementConfigs {
    boolean managementActivation;
    String managementAddress;
    int managementPort;
    String managementResourceBase;

    public ManagementConfigs() {
    }

    public boolean isManagementActivation() {
        return this.managementActivation;
    }

    public boolean getManagementActivation() {
        return this.managementActivation;
    }

    public void setManagementActivation(boolean managementActivation) {
        this.managementActivation = managementActivation;
    }

    public String getManagementAddress() {
        return this.managementAddress;
    }

    public void setManagementAddress(String managementAddress) {
        this.managementAddress = managementAddress;
    }

    public int getManagementPort() {
        return this.managementPort;
    }

    public void setManagementPort(int managementPort) {
        this.managementPort = managementPort;
    }

    public String getManagementResourceBase() {
        return this.managementResourceBase;
    }

    public void setManagementResourceBase(String managementResourceBase) {
        this.managementResourceBase = managementResourceBase;
    }

    @Override
    public String toString() {
        return "{" +
            " managementActivation='" + managementActivation + "'" +
            ", managementAddress='" + managementAddress + "'" +
            ", managementPort='" + managementPort + "'" +
            ", managementResourceBase='" + managementResourceBase + "'" +
            "}";
    }
}
