package com.thermatk.android.l.hsewifi.data;

class NetworksSettings {
    private String ssid;
    private String name;
    private boolean POST;
    private String requestLink;
    private boolean enabled;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPOST() {
        return POST;
    }

    public void setPOST(boolean POST) {
        this.POST = POST;
    }

    public String getRequestLink() {
        return requestLink;
    }

    public void setRequestLink(String requestLink) {
        this.requestLink = requestLink;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
