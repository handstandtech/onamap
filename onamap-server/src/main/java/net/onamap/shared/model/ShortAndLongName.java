package net.onamap.shared.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ShortAndLongName implements Serializable {
    String longName;
    String shortName;
}