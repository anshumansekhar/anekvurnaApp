/**
 * Created by Anshuman-HP on 27-09-2017.
 */
package com.main.cognichamp.CogniChamp;
/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

        import android.content.pm.ActivityInfo;
        import android.content.pm.ApplicationInfo;
        import android.content.pm.ConfigurationInfo;
        import android.content.pm.FeatureInfo;
        import android.content.pm.InstrumentationInfo;
        import android.content.pm.PermissionInfo;
        import android.content.pm.ProviderInfo;
        import android.content.pm.ServiceInfo;
        import android.content.pm.Signature;
        import android.os.Parcel;
        import android.os.Parcelable;

/**
 * Overall information about the contents of a package.  This corresponds
 * to all of the information collected from AndroidManifest.xml.
 */
public class PackageInfo implements Parcelable {
    /**
     * The name of this package.  From the <manifest> tag's "name"
     * attribute.
     */
    public String packageName;



    public int versionCode;


    public String versionName;


    public String sharedUserId;


    public int sharedUserLabel;

    /**
     * Information collected from the <application> tag, or null if
     * there was none.
     */
    public ApplicationInfo applicationInfo;


    public int[] gids;


    public ActivityInfo[] activities;


    public ActivityInfo[] receivers;


    public ServiceInfo[] services;


    public ProviderInfo[] providers;


    public InstrumentationInfo[] instrumentation;


    public PermissionInfo[] permissions;

     String[] requestedPermissions;


    public Signature[] signatures;


    public ConfigurationInfo[] configPreferences;

    /**
     * The features that this application has said it requires.
     */
    public FeatureInfo[] reqFeatures;

    /**
     * Constant corresponding to <code>auto in
     * the {@link android.R.attr#installLocation} attribute.
     * @hide
     */
    public static final int INSTALL_LOCATION_UNSPECIFIED = -1;
    /**
     * Constant corresponding to <code>auto in
     * the {@link android.R.attr#installLocation} attribute.
     * @hide
     */
    public static final int INSTALL_LOCATION_AUTO = 0;
    /**
     * Constant corresponding to <code>internalOnly in
     * the {@link android.R.attr#installLocation} attribute.
     * @hide
     */
    public static final int INSTALL_LOCATION_INTERNAL_ONLY = 1;
    /**
     * Constant corresponding to <code>preferExternal in
     * the {@link android.R.attr#installLocation} attribute.
     * @hide
     */
    public static final int INSTALL_LOCATION_PREFER_EXTERNAL = 2;
    /**
     * The install location requested by the activity.  From the
     * {@link android.R.attr#installLocation} attribute, one of
     * {@link #INSTALL_LOCATION_AUTO},
     * {@link #INSTALL_LOCATION_INTERNAL_ONLY},
     * {@link #INSTALL_LOCATION_PREFER_EXTERNAL}
     * @hide
     */
    public int installLocation = INSTALL_LOCATION_INTERNAL_ONLY;

    public PackageInfo() {
    }

    public String toString() {
        return "PackageInfo{"
                + Integer.toHexString(System.identityHashCode(this))
                + " " + packageName + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeString(packageName);
        dest.writeInt(versionCode);
        dest.writeString(versionName);
        dest.writeString(sharedUserId);
        dest.writeInt(sharedUserLabel);
        if (applicationInfo != null) {
            dest.writeInt(1);
            applicationInfo.writeToParcel(dest, parcelableFlags);
        } else {
            dest.writeInt(0);
        }
        dest.writeIntArray(gids);
        dest.writeTypedArray(activities, parcelableFlags);
        dest.writeTypedArray(receivers, parcelableFlags);
        dest.writeTypedArray(services, parcelableFlags);
        dest.writeTypedArray(providers, parcelableFlags);
        dest.writeTypedArray(instrumentation, parcelableFlags);
        dest.writeTypedArray(permissions, parcelableFlags);
        dest.writeStringArray(requestedPermissions);
        dest.writeTypedArray(signatures, parcelableFlags);
        dest.writeTypedArray(configPreferences, parcelableFlags);
        dest.writeTypedArray(reqFeatures, parcelableFlags);
        dest.writeInt(installLocation);
    }

    public static final Parcelable.Creator<PackageInfo> CREATOR
            = new Parcelable.Creator<PackageInfo>() {
        public PackageInfo createFromParcel(Parcel source) {
            return new PackageInfo(source);
        }

        public PackageInfo[] newArray(int size) {
            return new PackageInfo[size];
        }
    };

    private PackageInfo(Parcel source) {
        packageName = source.readString();
        versionCode = source.readInt();
        versionName = source.readString();
        sharedUserId = source.readString();
        sharedUserLabel = source.readInt();
        int hasApp = source.readInt();
        if (hasApp != 0) {
            applicationInfo = ApplicationInfo.CREATOR.createFromParcel(source);
        }
        gids = source.createIntArray();
        activities = source.createTypedArray(ActivityInfo.CREATOR);
        receivers = source.createTypedArray(ActivityInfo.CREATOR);
        services = source.createTypedArray(ServiceInfo.CREATOR);
        providers = source.createTypedArray(ProviderInfo.CREATOR);
        instrumentation = source.createTypedArray(InstrumentationInfo.CREATOR);
        permissions = source.createTypedArray(PermissionInfo.CREATOR);
        requestedPermissions = source.createStringArray();
        signatures = source.createTypedArray(Signature.CREATOR);
        configPreferences = source.createTypedArray(ConfigurationInfo.CREATOR);
        reqFeatures = source.createTypedArray(FeatureInfo.CREATOR);
        installLocation = source.readInt();
    }
}