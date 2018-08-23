/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package hds.aplications.com.mycp.sync.adapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import hds.aplications.com.mycp.MyCP;
import hds.aplications.com.mycp.models.User;
import hds.aplications.com.mycp.repositories.UserRepository;
import hds.aplications.com.mycp.sync.DataSynchronizer;
import mgleon.common.com.LogUtils;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = LogUtils.makeLogTag(SyncAdapter.class);
    private static final String SYNC_MARKER_KEY = "hds.aplications.com.mycplight.sync.adapter.marker";
    private static final boolean NOTIFY_AUTH_FAILURE = true;

    private final AccountManager mAccountManager;

    private final Context mContext;

    private static List<SyncAdapterListener> syncAdapterListeners = new ArrayList<>();

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try {
            for (int i = 0, z = syncAdapterListeners.size(); i < z; i++) {
                SyncAdapterListener listener = syncAdapterListeners.get(i);
                if (listener != null) {
                    if (!listener.beforeSync())
                        return;
                }
            }

            final DataSynchronizer synchronizer = new DataSynchronizer();
            final User user = synchronizer.synchronizeUserData(account.name, mAccountManager.getPassword(account), mContext);
            if (user != null){
                LogUtils.LOGI(TAG, "Sync successfully through onPerformSync");

                UserRepository.saveUserSync(user);

                if(MyCP.getApplication(mContext).initiatedWelcome){
                   // MyCPLight.getApplication(mContext).goLogin();
                }
            }
        }
        catch(Exception ex){
            LogUtils.LOGE(TAG, ex.getMessage(), ex);
        }
    }

    /**
     * This helper function fetches the last known high-water-mark
     * we received from the server - or 0 if we've never synced.
     * @param account the account we're syncing
     * @return the change high-water-mark
     */
    private long getServerSyncMarker(Account account) {
        String markerString = mAccountManager.getUserData(account, SYNC_MARKER_KEY);
        if (!TextUtils.isEmpty(markerString)) {
            return Long.parseLong(markerString);
        }
        return 0;
    }

    /**
     * Save off the high-water-mark we receive back from the server.
     * @param account The account we're syncing
     * @param marker The high-water-mark we want to save.
     */
    private void setServerSyncMarker(Account account, long marker) {
        mAccountManager.setUserData(account, SYNC_MARKER_KEY, Long.toString(marker));
    }

    public static void addSyncAdapterListener(SyncAdapterListener listener) {
        syncAdapterListeners.add(listener);
    }

    public interface SyncAdapterListener {
        public boolean beforeSync();
    }
}

