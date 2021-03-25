package org.chaostocosmos.porta.properties;

/**
 * AppConfig VO
 */
public class AppConfigs {

    String name;
    String version;
    long statisticsProbeMillis;
    
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

    public long getStatisticsProbeMillis() {
        return this.statisticsProbeMillis;
    }

    public void setStatisticsProbeMillis(long statisticsProbeMillis) {
        this.statisticsProbeMillis = statisticsProbeMillis;
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + name + "'" +
            ", version='" + version + "'" +
            ", statisticsProbeMillis='" + statisticsProbeMillis + "'" +
            "}";
    }
}
