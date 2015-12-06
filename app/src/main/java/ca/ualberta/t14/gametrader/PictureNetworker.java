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

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;


/**
 * Created by Ryan on 2015-11-30.
 */
public class PictureNetworker extends FileIO implements AppObservable, NetworkerCommander {
    private PictureManager pm;
    transient private Activity activity;
    transient private Context context;
    public static final String PictureNetworkId = "PictureManagerAndNetworker";

    // Keeps track of all the local/downloaded images.
    private ArrayList<String> localCopyOfImageIds;
    private ArrayList<String> imagesToDownload;

    private ArrayList<String> imageIdsToUpload;
    private ArrayList<String> imageIdsToRemove;

    private transient ArrayList<AppObserver> observers;
    private transient ArrayList<NetworkerListener> listeners;

    public static final int PULL_IMAGES = 128;
    public static final int PUSH_IMAGE = 129;
    public static final int PUSH_IMAGES_TO_DELETE = 130;

    public PictureNetworker() {
        pm = new PictureManager();
        imageIdsToUpload = new ArrayList<String>();
        imageIdsToRemove = new ArrayList<String>();
        localCopyOfImageIds = new ArrayList<String>();
        imagesToDownload = new ArrayList<String>();
        observers = new ArrayList<AppObserver>();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public PictureManager getPictureManager() {
        return pm;
    }

    public ArrayList<String> getLocalCopyOfImageIds() {
        return localCopyOfImageIds;
    }

    public ArrayList<String> getImagesToDownload() {
        return imagesToDownload;
    }

    public void addImageToDownload(String imageToDownload) {
        imagesToDownload.add(imageToDownload);
        savePictureNetworker();
        if(ObjParseSingleton.getInstance().keywordExists(NetworkConnectivity.IS_NETWORK_ONLINE)
                && ((Boolean)ObjParseSingleton.getInstance().getObject(NetworkConnectivity.IS_NETWORK_ONLINE)))
            notifyAllListeners(PULL_IMAGES);
    }

    public void setLocalCopyOfImageIds(ArrayList<String> localCopyOfImageIds) {
        this.localCopyOfImageIds = localCopyOfImageIds;
    }

    public ArrayList<String> getImageFilesToUpload() {
        return imageIdsToUpload;
    }

    public void addImageFileToUpload(String imageFileToUpload) {
        imageIdsToUpload.add(imageFileToUpload);
        savePictureNetworker();
        if(ObjParseSingleton.getInstance().keywordExists(NetworkConnectivity.IS_NETWORK_ONLINE)
                && ((Boolean)ObjParseSingleton.getInstance().getObject(NetworkConnectivity.IS_NETWORK_ONLINE)))
            notifyAllListeners(PUSH_IMAGE);
    }

    public ArrayList<String> getImageFilesToRemove() {
        return imageIdsToRemove;
    }

    public void addImageFileToRemove(String imageFileToRemove) {
        this.imageIdsToRemove.add(imageFileToRemove);
        savePictureNetworker();
        if(ObjParseSingleton.getInstance().keywordExists(NetworkConnectivity.IS_NETWORK_ONLINE)
                && ((Boolean)ObjParseSingleton.getInstance().getObject(NetworkConnectivity.IS_NETWORK_ONLINE)))
            notifyAllListeners(PUSH_IMAGES_TO_DELETE);
    }

    public void savePictureNetworker() {
        saveJson(PictureNetworkId, context);
    }

    public void addObserver(AppObserver observer) {
        observers.add(observer);
    }

    public void deleteObserver(AppObserver observer) {
        observers.remove(observer);
    }

    public void notifyAllObservers() {
        final AppObservable thisThing = this;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for(AppObserver obs : observers) {
                    obs.appNotify(thisThing);
                }
            }
        };
        activity.runOnUiThread(r);
    }

    public void addListener(NetworkerListener listener) {
        if(listeners == null) {
            listeners = new ArrayList<NetworkerListener>();
        }
        listeners.add(listener);
    }

    public void deletelisteners(NetworkerListener listener) {
        if(listeners != null) {
            listeners.remove(listener);
        }
    }

    /**
     * Called to notify all observers that the model has been updated.
     */
    public void notifyAllListeners(int commandCode) {
        if(listeners != null) {
            for (NetworkerListener obs : listeners) {
                obs.netListenerNotify(commandCode);
            }
        }
    }

}
