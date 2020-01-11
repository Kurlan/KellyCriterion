package com.hollow.prototypes.amazonlocal.hollow.producer;

import java.io.File;

import com.hollow.prototypes.amazonlocal.voucher.Voucher;
import com.hollow.prototypes.amazonlocal.voucher.pool.VoucherPool;
import com.netflix.hollow.api.consumer.HollowConsumer;
import com.netflix.hollow.api.consumer.fs.HollowFilesystemAnnouncementWatcher;
import com.netflix.hollow.api.consumer.fs.HollowFilesystemBlobRetriever;
import com.netflix.hollow.api.producer.HollowProducer;
import com.netflix.hollow.api.producer.fs.HollowFilesystemAnnouncer;
import com.netflix.hollow.api.producer.fs.HollowFilesystemPublisher;

public class HollowVoucherProducer implements Runnable {

    public static final String SCRATCH_DIR = System.getProperty("java.io.tmpdir");
    private static final long MIN_TIME_BETWEEN_CYCLES = 10000;

    private final VoucherPool voucherPool;

    public HollowVoucherProducer(VoucherPool voucherPool) {
        this.voucherPool = voucherPool;
    }
    @Override
    public void run() {
        File publishDir = new File(SCRATCH_DIR, "publish-dir");
        publishDir.mkdir();

        System.out.println("I AM THE PRODUCER.  I WILL PUBLISH TO " + publishDir.getAbsolutePath());

        HollowProducer.Publisher publisher = new HollowFilesystemPublisher(publishDir);
        HollowProducer.Announcer announcer = new HollowFilesystemAnnouncer(publishDir);

        HollowConsumer.BlobRetriever blobRetriever = new HollowFilesystemBlobRetriever(publishDir);
        HollowConsumer.AnnouncementWatcher announcementWatcher = new HollowFilesystemAnnouncementWatcher(publishDir);

        HollowProducer producer = HollowProducer.withPublisher(publisher)
                .withAnnouncer(announcer)
                .build();

        producer.initializeDataModel(Voucher.class);
        restoreIfAvailable(producer, blobRetriever, announcementWatcher);
        cycleForever(producer, voucherPool);
    }

    public void restoreIfAvailable(HollowProducer producer,
            HollowConsumer.BlobRetriever retriever,
            HollowConsumer.AnnouncementWatcher unpinnableAnnouncementWatcher) {

        System.out.println("ATTEMPTING TO RESTORE PRIOR STATE...");
        long latestVersion = unpinnableAnnouncementWatcher.getLatestVersion();
        if(latestVersion != HollowConsumer.AnnouncementWatcher.NO_ANNOUNCEMENT_AVAILABLE) {
            producer.restore(latestVersion, retriever);
            System.out.println("RESTORED " + latestVersion);
        } else {
            System.out.println("RESTORE NOT AVAILABLE");
        }
    }


    public void cycleForever(HollowProducer producer, VoucherPool voucherPool) {
        long lastCycleTime = Long.MIN_VALUE;
        while(true) {
            waitForMinCycleTime(lastCycleTime);
            lastCycleTime = System.currentTimeMillis();
            producer.runCycle(writeState -> {
                for(Voucher voucher : voucherPool.getPool()) {
                    writeState.add(voucher);  /// <-- this is thread-safe, and can be done in parallel
                }
            });
        }
    }

    private void waitForMinCycleTime(long lastCycleTime) {
        long targetNextCycleTime = lastCycleTime + MIN_TIME_BETWEEN_CYCLES;

        while(System.currentTimeMillis() < targetNextCycleTime) {
            try {
                Thread.sleep(targetNextCycleTime - System.currentTimeMillis());
            } catch(InterruptedException ignore) { }
        }
    }


}
