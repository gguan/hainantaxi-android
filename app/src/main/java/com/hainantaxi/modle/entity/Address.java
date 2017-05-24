package com.hainantaxi.modle.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.services.geocoder.RegeocodeAddress;


import java.util.List;

/**
 * Created by develop on 2017/5/24.
 */

public class Address implements Parcelable {

    private String name;
    private double latidute;
    private double longtidute;
    private String description;

    public Address(String name, double latidute, double longtidute, String description) {
        this.name = name;
        this.latidute = latidute;
        this.longtidute = longtidute;
        this.description = description;
    }

    public Address(RegeocodeAddress address) {

        this.address = address;
    }

    public Address() {
    }

    private RegeocodeAddress address;

    protected Address(Parcel in) {
        name = in.readString();
        latidute = in.readDouble();
        longtidute = in.readDouble();
        description = in.readString();
        address = in.readParcelable(RegeocodeAddress.class.getClassLoader());
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(latidute);
        dest.writeDouble(longtidute);
        dest.writeString(description);
        dest.writeParcelable(address, flags);
    }
}
