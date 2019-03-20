package com.example.bahirbarakzai.osmdroidapptest;

import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;

public class CustomTileSource extends BitmapTileSourceBase {
    /**
     * Constructor
     *
     * @param aName                a human-friendly name for this tile source
     * @param aResourceId          resource id used to get the localized name of this tile source
     * @param aZoomMinLevel        the minimum zoom level this tile source can provide
     * @param aZoomMaxLevel        the maximum zoom level this tile source can provide
     * @param aTileSizePixels      the tile size in pixels this tile source provides
     * @param aImageFilenameEnding the file name extension used when constructing the filename
     */
    public CustomTileSource(String aName, ResourceProxy.string aResourceId, int aZoomMinLevel, int aZoomMaxLevel, int aTileSizePixels, String aImageFilenameEnding) {
        super(aName, aResourceId, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels, aImageFilenameEnding);
    }
}
