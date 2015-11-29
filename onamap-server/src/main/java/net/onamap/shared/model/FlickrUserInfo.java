package net.onamap.shared.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FlickrUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String id;
    private String nsid;
    private String token;
    private String tokenSecret;
}