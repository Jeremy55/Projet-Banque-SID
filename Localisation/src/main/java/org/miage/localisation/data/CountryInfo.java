package org.miage.localisation.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryInfo {
    private String language;
    private String distance;
    private String countryCode;
    private String countryName;
}
