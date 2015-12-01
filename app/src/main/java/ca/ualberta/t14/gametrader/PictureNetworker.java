/*
 * Copyright (C) 2015  Aaron Arnason, Tianyu Hu, Michael Xi, Ryan Satyabrata, Joel Johnston, Suzanne Boulet, Ng Yuen Tung(Brigitte)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package ca.ualberta.t14.gametrader;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Ryan on 2015-11-30.
 */
public class PictureNetworker extends FileIO {
    private PictureManager pm;
    public static final String PictureNetworkId = "PictureManagerAndNetworker";

    private ArrayList<String> imageFileToUpload;
    private ArrayList<String> imageFileToRemove;

    public PictureNetworker() {
        pm = new PictureManager();
        imageFileToUpload = new ArrayList<String>();
        imageFileToRemove = new ArrayList<String>();
    }

    public PictureManager getPictureManager() {
        return pm;
    }

    public ArrayList<String> getImageFileToUpload() {
        return imageFileToUpload;
    }

    public void setImageFileToUpload(String imageFileToUpload, Context context) {
        saveJson(PictureNetworkId, context);
        this.imageFileToUpload.add(imageFileToUpload);
    }

    public ArrayList<String> getImageFileToRemove() {
        return imageFileToRemove;
    }

    public void setImageFileToRemove(String imageFileToRemove, Context context) {
        saveJson(PictureNetworkId, context);
        this.imageFileToRemove.add(imageFileToRemove);
    }
}
