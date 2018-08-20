package com.solutions.atm.atmservice.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Denomination {

    private String faceValue;

    private Integer quantity;

    public String getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(String faceValue) {
        this.faceValue = faceValue;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Denomination that = (Denomination) o;

        return new EqualsBuilder()
                .append(faceValue, that.faceValue)
                .append(quantity, that.quantity)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(faceValue)
                .append(quantity)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("faceValue", faceValue)
                .append("quantity", quantity)
                .toString();
    }


    public static final class DenominationBuilder {
        private String faceValue;
        private Integer quantity;

        private DenominationBuilder() {
        }

        public static DenominationBuilder aDenomination() {
            return new DenominationBuilder();
        }

        public DenominationBuilder withFaceValue(String faceValue) {
            this.faceValue = faceValue;
            return this;
        }

        public DenominationBuilder withQuantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Denomination build() {
            Denomination denomination = new Denomination();
            denomination.setFaceValue(faceValue);
            denomination.setQuantity(quantity);
            return denomination;
        }
    }
}
