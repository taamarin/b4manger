package xyz.chz.bfm.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.MenuItem;
import xyz.chz.bfm.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppHelper {

    public static final String SETTINGS_CATEGORY =
            "de.robv.android.xposed.category.MODULE_SETTINGS";
    private static List<PackageInfo> appList;

    public static Intent getSettingsIntent(String packageName, PackageManager packageManager) {
        // taken from
        // ApplicationPackageManager.getLaunchIntentForPackage(String)
        // first looks for an Xposed-specific category, falls back to
        // getLaunchIntentForPackage

        Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
        intentToResolve.addCategory(SETTINGS_CATEGORY);
        intentToResolve.setPackage(packageName);
        List<ResolveInfo> ris = packageManager.queryIntentActivities(intentToResolve, 0);

        if (ris.size() <= 0) {
            return packageManager.getLaunchIntentForPackage(packageName);
        }

        Intent intent = new Intent(intentToResolve);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(ris.get(0).activityInfo.packageName, ris.get(0).activityInfo.name);
        return intent;
    }

    public static boolean onOptionsItemSelected(MenuItem item, SharedPreferences preferences) {
        int itemId = item.getItemId();
        if (itemId == R.id.item_sort_by_name) {
            item.setChecked(true);
            preferences.edit().putInt("list_sort", 0).apply();
        } else if (itemId == R.id.item_sort_by_name_reverse) {
            item.setChecked(true);
            preferences.edit().putInt("list_sort", 1).apply();
        } else if (itemId == R.id.item_sort_by_package_name) {
            item.setChecked(true);
            preferences.edit().putInt("list_sort", 2).apply();
        } else if (itemId == R.id.item_sort_by_package_name_reverse) {
            item.setChecked(true);
            preferences.edit().putInt("list_sort", 3).apply();
        } else if (itemId == R.id.item_sort_by_install_time) {
            item.setChecked(true);
            preferences.edit().putInt("list_sort", 4).apply();
        } else if (itemId == R.id.item_sort_by_install_time_reverse) {
            item.setChecked(true);
            preferences.edit().putInt("list_sort", 5).apply();
        } else if (itemId == R.id.item_sort_by_update_time) {
            item.setChecked(true);
            preferences.edit().putInt("list_sort", 6).apply();
        } else if (itemId == R.id.item_sort_by_update_time_reverse) {
            item.setChecked(true);
            preferences.edit().putInt("list_sort", 7).apply();
        } else {
            return false;
        }
        return true;
    }

    public static Comparator<PackageInfo> getAppListComparator(int sort, PackageManager pm) {
        ApplicationInfo.DisplayNameComparator displayNameComparator =
                new ApplicationInfo.DisplayNameComparator(pm);
        switch (sort) {
            case 7:
                return Collections.reverseOrder(
                        Comparator.comparingLong((PackageInfo a) -> a.lastUpdateTime));
            case 6:
                return Comparator.comparingLong((PackageInfo a) -> a.lastUpdateTime);
            case 5:
                return Collections.reverseOrder(
                        Comparator.comparingLong((PackageInfo a) -> a.firstInstallTime));
            case 4:
                return Comparator.comparingLong((PackageInfo a) -> a.firstInstallTime);
            case 3:
                return Collections.reverseOrder(Comparator.comparing(a -> a.packageName));
            case 2:
                return Comparator.comparing(a -> a.packageName);
            case 1:
                return Collections.reverseOrder(
                        (PackageInfo a, PackageInfo b) ->
                                displayNameComparator.compare(
                                        a.applicationInfo, b.applicationInfo));
            case 0:
            default:
                return (PackageInfo a, PackageInfo b) ->
                        displayNameComparator.compare(a.applicationInfo, b.applicationInfo);
        }
    }

    public static List<PackageInfo> getAppList(Context context) {
        if (appList == null) {
            appList =
                    context.getPackageManager()
                            .getInstalledPackages(
                                    PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        }
        return appList;
    }
}
