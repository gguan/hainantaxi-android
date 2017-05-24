package com.hainantaxi.modle.entity;

import com.amap.api.services.geocoder.RegeocodeAddress;


import java.util.List;

/**
 * Created by develop on 2017/5/24.
 */

public class Address {

    public Address(RegeocodeAddress address) {

        this.address = address;
    }

    public Address() {
    }

    private RegeocodeAddress address;

    public String getAddress() {
        if (address != null) {
            try {
                String formatAddress = address.getFormatAddress();
                String province = address.getProvince();
                String city = address.getCity();
                if (formatAddress.contains(city)) {
                    formatAddress = formatAddress.replace(city, "");
                }
                if (formatAddress.contains(province)) {
                    formatAddress = formatAddress.replace(province, "");
                }
                return formatAddress.isEmpty() ? address.getFormatAddress() : formatAddress;
            } catch (Exception ex) {
                return address.getFormatAddress();
            }
        }
        return "";
    }

    public void setAddress(RegeocodeAddress address) {
        this.address = address;
    }
}
