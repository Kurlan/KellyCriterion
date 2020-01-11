package com.hollow.prototypes.amazonlocal.assignment;

import com.hollow.prototypes.amazonlocal.order.CustomerOrder;
import com.hollow.prototypes.amazonlocal.voucher.Voucher;

public interface AssignVoucherService {
    boolean assign(CustomerOrder customerOrder, Voucher voucher);
}
