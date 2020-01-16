package com.hollow.prototypes.amazonlocal.simulation.hollow;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import com.hollow.prototypes.amazonlocal.assignment.AssignVoucherService;
import com.hollow.prototypes.amazonlocal.hollow.producer.HollowVoucherProducer;
import com.hollow.prototypes.amazonlocal.order.CustomerOrder;
import com.hollow.prototypes.amazonlocal.order.queue.OrderQueue;
import com.hollow.prototypes.amazonlocal.search.HollowSearchService;
import com.hollow.prototypes.amazonlocal.search.VoucherSearchService;
import com.hollow.prototypes.amazonlocal.voucher.Voucher;
import com.hollow.prototypes.amazonlocal.voucher.api.VoucherAPI;
import com.hollow.prototypes.amazonlocal.voucher.api.VoucherAPIHashIndex;
import com.hollow.prototypes.amazonlocal.voucher.pool.VoucherPool;
import com.netflix.hollow.api.consumer.HollowConsumer;
import com.netflix.hollow.api.consumer.fs.HollowFilesystemAnnouncementWatcher;
import com.netflix.hollow.api.consumer.fs.HollowFilesystemBlobRetriever;

public class HollowSimulationWorkerCallable implements Callable<LinkedList<CustomerOrder>> {
    private static final int ASSIGNMENT_RETRIES = 3;
    private final VoucherPool voucherPool;
    private final OrderQueue orderQueue;
    private final VoucherSearchService voucherSearchService;
    private final AssignVoucherService assignVoucherService;
    private VoucherAPIHashIndex voucherAPIHashIndex;
    private HollowConsumer consumer;

    public HollowSimulationWorkerCallable(final VoucherPool voucherPool, final OrderQueue orderQueue, final AssignVoucherService assignVoucherService) {
        this.voucherPool = voucherPool;
        this.orderQueue = orderQueue;
        this.assignVoucherService = assignVoucherService;
        this.voucherSearchService = new HollowSearchService();
    }

    @Override
    public LinkedList<CustomerOrder> call() throws Exception {

        File publishDir = new File(HollowVoucherProducer.SCRATCH_DIR, "publish-dir");
        System.out.println("I AM THE CONSUMER.  I WILL READ FROM " + publishDir.getAbsolutePath());
        HollowConsumer.BlobRetriever blobRetriever = new HollowFilesystemBlobRetriever(publishDir);
        HollowConsumer.AnnouncementWatcher announcementWatcher = new HollowFilesystemAnnouncementWatcher(publishDir);

        this.consumer = HollowConsumer.withBlobRetriever(blobRetriever)
                .withAnnouncementWatcher(announcementWatcher)
                .withGeneratedAPIClass(VoucherAPI.class)
                .build();
        this.consumer.triggerRefresh();
        this.voucherAPIHashIndex = new VoucherAPIHashIndex(consumer,"Voucher", "", "customerId.value");

        LinkedList<CustomerOrder> fulfilledOrders = new LinkedList<>();

        while (!orderQueue.getCustomerOrders().isEmpty()) {

            CustomerOrder customerOrder = orderQueue.getCustomerOrders().poll();
            if (customerOrder == null) {
                continue;
            }
            customerOrder.getSearchTries().addAndGet(1);
            List<Voucher> searchResult = getSearchResultsFromConsumer(consumer);
            boolean assignmentSuccess = false;
            for (int i = 0; i < Math.min (searchResult.size(), ASSIGNMENT_RETRIES); i++) {
                customerOrder.getAssignmentTries().addAndGet(1);

                if (assignVoucherService.assign(customerOrder,searchResult.get(i))) {
                    fulfilledOrders.push(customerOrder);
                    assignmentSuccess = true;
                    //System.out.println("Assignment SUCCESS: " + searchResult.get(i).getVoucherId());
                    break;
                }
                //System.out.println("Assignment failure: " + searchResult.get(i).getVoucherId());
            }

            if (assignmentSuccess == false) {
                orderQueue.getCustomerOrders().offer(customerOrder);
            }
            consumer.triggerRefresh();
        }
        return fulfilledOrders;
}

    private List<Voucher> getSearchResultsFromConsumer(final HollowConsumer consumer) {
        List<Voucher> results = new ArrayList<>();

        consumer.triggerRefresh();

        Iterable<com.hollow.prototypes.amazonlocal.voucher.api.Voucher> iterable = voucherAPIHashIndex.findVoucherMatches("-1");

        final Iterator<com.hollow.prototypes.amazonlocal.voucher.api.Voucher> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            com.hollow.prototypes.amazonlocal.voucher.api.Voucher voucher = iterator.next();
            results.add(Voucher
                    .builder()
                    .voucherId(voucher.getVoucherId().getValue())
                    .customerId("-1")
                    .build());
        }
        Collections.shuffle(results);

        return results;
    }

}
