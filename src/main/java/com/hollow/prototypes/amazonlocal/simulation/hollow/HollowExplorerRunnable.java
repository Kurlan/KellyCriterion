package com.hollow.prototypes.amazonlocal.simulation.hollow;

import java.io.File;

import com.hollow.prototypes.amazonlocal.hollow.producer.HollowVoucherProducer;
import com.hollow.prototypes.amazonlocal.voucher.api.VoucherAPI;
import com.netflix.hollow.api.consumer.HollowConsumer;
import com.netflix.hollow.api.consumer.fs.HollowFilesystemAnnouncementWatcher;
import com.netflix.hollow.api.consumer.fs.HollowFilesystemBlobRetriever;
import com.netflix.hollow.explorer.ui.jetty.HollowExplorerUIServer;
import com.netflix.hollow.history.ui.jetty.HollowHistoryUIServer;

public class HollowExplorerRunnable implements Runnable {

    @Override
    public void run() {
        File publishDir = new File(HollowVoucherProducer.SCRATCH_DIR, "publish-dir");

        System.out.println("I AM THE CONSUMER.  I WILL READ FROM " + publishDir.getAbsolutePath());

        HollowConsumer.BlobRetriever blobRetriever = new HollowFilesystemBlobRetriever(publishDir);
        HollowConsumer.AnnouncementWatcher announcementWatcher = new HollowFilesystemAnnouncementWatcher(publishDir);

        HollowConsumer consumer = HollowConsumer.withBlobRetriever(blobRetriever)
                .withAnnouncementWatcher(announcementWatcher)
                .withGeneratedAPIClass(VoucherAPI.class)
                .build();

        consumer.triggerRefresh();
        try {

            /// start a history server on port 7777
            HollowHistoryUIServer historyServer = new HollowHistoryUIServer(consumer, 7777);

            historyServer.start();

            /// start an explorer server on port 7778
            HollowExplorerUIServer explorerServer = new HollowExplorerUIServer(consumer, 7778);
            explorerServer.start();

            historyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
