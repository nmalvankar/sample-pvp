package org.redhat.services.model;

import org.kie.server.api.model.ReleaseId;

public class Kjar {

    private String groupId;
    private String artifactId;
    private String version;
    private String containerId;
    private String alias;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ReleaseId getReleaseId() {
        return new ReleaseId(groupId, artifactId, version);

    }
}
