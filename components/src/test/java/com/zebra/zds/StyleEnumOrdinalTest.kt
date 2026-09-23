package com.zebra.zds

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * The `enum` values declared in `attr.xml` are read as plain ints and mapped onto these enums by
 * ordinal, so reordering a constant silently changes what a layout attribute means.
 */
class StyleEnumOrdinalTest {

    @Test
    fun labelStyleOrdinalsMatchTheLabelStyleAttribute() {
        assertEquals(0, ZdsTextInput.LabelStyle.DEFAULT.ordinal)
        assertEquals(1, ZdsTextInput.LabelStyle.MANDATORY.ordinal)
        assertEquals(2, ZdsTextInput.LabelStyle.OPTIONAL.ordinal)
        assertEquals(3, ZdsTextInput.LabelStyle.values().size)
    }

    @Test
    fun bannerStyleOrdinalsMatchTheBannerStyleAttribute() {
        assertEquals(0, ZdsBanner.Style.DEFAULT.ordinal)
        assertEquals(1, ZdsBanner.Style.INFO.ordinal)
        assertEquals(2, ZdsBanner.Style.POSITIVE.ordinal)
        assertEquals(3, ZdsBanner.Style.WARNING.ordinal)
        assertEquals(4, ZdsBanner.Style.NEGATIVE.ordinal)
        assertEquals(5, ZdsBanner.Style.values().size)
    }

    @Test
    fun systemBannerStyleOrdinalsMatchTheSystemBannerStyleAttribute() {
        assertEquals(0, ZdsSystemBanner.Style.DEFAULT.ordinal)
        assertEquals(1, ZdsSystemBanner.Style.POSITIVE.ordinal)
        assertEquals(2, ZdsSystemBanner.Style.WARNING.ordinal)
        assertEquals(3, ZdsSystemBanner.Style.NEGATIVE.ordinal)
        assertEquals(4, ZdsSystemBanner.Style.values().size)
    }
}
