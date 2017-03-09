package com.linemetrics.api.returntypes;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Klemens on 08.03.2017.
 */
public class AggregatedDataReadResponse extends DataReadResponse {

    @SerializedName("ts")
    private Long unixTicks;

    @SerializedName("val")
    private Double average;

    @SerializedName("min")
    private Double minimum;

    @SerializedName("max")
    private Double maximum;

    @SerializedName("sum")
    private Double sum;

    @SerializedName("count")
    private Integer count;

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return String.format("Average: %s, Minimum: %s, Maximum: %s, Sum: %s, Count: %s, Timestamp: %s"
                , this.average
                , this.minimum
                , this.maximum
                , this.sum
                , this.count
                , sdf.format(this.getTimestamp()));
    }

    public Long getUnixTicks() {
        return unixTicks;
    }

    public void setUnixTicks(Long unixTicks) {
        this.unixTicks = unixTicks;
    }

    public Date getTimestamp() {
        return new Date(this.unixTicks);
    }

    public void setTimestamp(Date timestamp) {
        this.unixTicks = timestamp.getTime();
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getMinimum() {
        return minimum;
    }

    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }

    public Double getMaximum() {
        return maximum;
    }

    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
