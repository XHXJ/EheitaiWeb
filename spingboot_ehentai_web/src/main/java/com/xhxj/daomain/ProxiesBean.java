package com.xhxj.daomain;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProxiesBean {
    /**
     * id : 503
     * ip : 95.85.50.218
     * port : 80
     * is_valid : true
     * created_at : 1547275551
     * updated_at : 1547295292
     * latency : 85
     * stability : 0.5
     * is_anonymous : true
     * is_https : false
     * attempts : 1
     * https_attempts : 0
     * location : 52.2965,4.9542
     * organization : AS14061 DigitalOcean, LLC
     * region : Noord-Holland
     * country : NL
     * city : Amsterdam
     */

    private int id;
    private String ip;
    private int port;
    private boolean is_valid;
    private int created_at;
    private int updated_at;
    private int latency;
    private double stability;
    private boolean is_anonymous;
    private boolean is_https;
    private int attempts;
    private int https_attempts;
    private String location;
    private String organization;
    private String region;
    private String country;
    private String city;
}
