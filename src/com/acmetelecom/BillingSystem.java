package com.acmetelecom;

import com.acmetelecom.customer.CentralCustomerDatabase;
import com.acmetelecom.customer.CentralTariffDatabase;
import com.acmetelecom.customer.Customer;
import com.acmetelecom.customer.Tariff;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class BillingSystem {

    private List<CallEvent> callLog = new ArrayList<CallEvent>();

    public void callInitiated(String caller, String callee) {
        callLog.add(new CallStart(caller, callee));
    }

    public void callCompleted(String caller, String callee) {
        callLog.add(new CallEnd(caller, callee));
    }

    public void createCustomerBills() {
        List<Customer> customers = CentralCustomerDatabase.getInstance().getCustomers();
        //Create bill for every customer in the list
        for (Customer customer : customers) {
            createBillFor(customer);
        }
        callLog.clear();
    }

    private void createBillFor(Customer customer) {
        List<CallEvent> customerEvents = new ArrayList<CallEvent>();
        
        //Add all the call events that are relevant to the given customer
        for (CallEvent callEvent : callLog) {
            if (callEvent.getCaller().equals(customer.getPhoneNumber())) {
                customerEvents.add(callEvent);
            }
        }
        
        //Match the start event and end event of every call
        List<Call> calls = new ArrayList<Call>();

        CallEvent start = null;
        for (CallEvent event : customerEvents) {
            if (event instanceof CallStart) {
                start = event;
            }
            if (event instanceof CallEnd && start != null) {
                calls.add(new Call(start, event));
                start = null;
            }
        }

        BigDecimal totalBill = new BigDecimal(0);
        List<LineItem> items = new ArrayList<LineItem>();
        
        //TODO
        //Calculate the cost for each call of the customer
        for (Call call : calls) {
        	
            Tariff tariff = CentralTariffDatabase.getInstance().tarriffFor(customer);

            BigDecimal cost;
            BigDecimal onPeakCost;
            BigDecimal offPeakCost;
            
            //Calculate respectively the onPeakCost and offPeakCost of each call
            onPeakCost = new BigDecimal(call.onPeakDurationSeconds(call.getType())).multiply(tariff.peakRate());
            offPeakCost = new BigDecimal(call.offPeakDurationSeconds(call.getType())).multiply(tariff.offPeakRate());
            cost = onPeakCost.add(offPeakCost);
            
            /* Original code to get the cost
             * 
            //Test if starttime and endtime are both offPeaktime, calculation depends on offpeakRate
            if (peakPeriod.offPeak(call.startTime()) && peakPeriod.offPeak(call.endTime()) && call.durationSeconds() < 12 * 60 * 60) {
                cost = new BigDecimal(call.durationSeconds()).multiply(tariff.offPeakRate());
            } else {
                cost = new BigDecimal(call.durationSeconds()).multiply(tariff.peakRate());
            }
            */

            cost = cost.setScale(0, RoundingMode.HALF_UP);
            BigDecimal callCost = cost;
            totalBill = totalBill.add(callCost);
            items.add(new LineItem(call, callCost));
        }

        new BillGenerator().send(customer, items, MoneyFormatter.penceToPounds(totalBill));
    }

    static class LineItem {
        private Call call;
        private BigDecimal callCost;

        public LineItem(Call call, BigDecimal callCost) {
            this.call = call;
            this.callCost = callCost;
        }

        public String date() {
            return call.date();
        }

        public String callee() {
            return call.callee();
        }
        
        //Calculate the onPeakDuration in minute
        public String onPeakDurationMinutes() {
        	return "" + call.onPeakDurationSeconds(call.getType()) / 60 + ":" + String.format("%02d", call.onPeakDurationSeconds(call.getType()) % 60);
        }
        
        //Calculate the offPeakDuration in minute
        public String offPeakDurationMinutes() {
        	return "" + call.offPeakDurationSeconds(call.getType()) / 60 + ":" + String.format("%02d", call.offPeakDurationSeconds(call.getType()) % 60);
        }
        
        //Calculate the total duration in minute of each call
        public String durationMinutes() {
            return "" + (call.onPeakDurationSeconds(call.getType()) + call.offPeakDurationSeconds(call.getType())) / 60 + ":" + String.format("%02d", (call.onPeakDurationSeconds(call.getType()) + call.offPeakDurationSeconds(call.getType())) % 60);
        }

        public BigDecimal cost() {
            return callCost;
        }
    }
}
