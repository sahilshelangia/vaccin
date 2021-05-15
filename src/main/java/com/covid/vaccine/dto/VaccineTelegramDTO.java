package com.covid.vaccine.dto;

import lombok.*;

@Builder
@Data
public class VaccineTelegramDTO {
    private String pinCode;
    private String address;
    private String district;
    private String capacity;

    @Override
    public String toString() {
        return capacity +
                " vaccines are available at " +
                address + ", " + district + ", " +
                "(" + pinCode + ")\n";
    }
}
