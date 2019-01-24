package com.prakass.products.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NumberUtilTest {

    @Test
    public void parsesIntValueToDouble() throws Exception {
        assertThat(NumberUtil.parseNumberSafely("2"), is(2.0));
    }

    @Test
    public void parsesDoubleValueToDouble() throws Exception {
        assertThat(NumberUtil.parseNumberSafely("2.2222"), is(2.2222));
    }

    @Test
    public void parsesInvalidToZero() throws Exception {
        assertThat(NumberUtil.parseNumberSafely("invalid-num"), is(0.0));
    }

    @Test
    public void parsesEmptyStringToZero() throws Exception {
        assertThat(NumberUtil.parseNumberSafely(""), is(0.0));
    }

    @Test
    public void parsesNullToZero() throws Exception {
        assertThat(NumberUtil.parseNumberSafely(null), is(0.0));
    }

    @Test
    public void isNumberAnInt_true() throws Exception {
        assertThat(NumberUtil.isNumberAnInt(1.0), is(true));
        assertThat(NumberUtil.isNumberAnInt(11.0), is(true));
        assertThat(NumberUtil.isNumberAnInt(-1.0), is(true));
        assertThat(NumberUtil.isNumberAnInt(0.0), is(true));
    }

    @Test
    public void isNumberAnInt_false() throws Exception {
        assertThat(NumberUtil.isNumberAnInt(1.0000001), is(false));
        assertThat(NumberUtil.isNumberAnInt(11.2), is(false));
        assertThat(NumberUtil.isNumberAnInt(-1.9), is(false));
        assertThat(NumberUtil.isNumberAnInt(0.00001), is(false));
    }
}
